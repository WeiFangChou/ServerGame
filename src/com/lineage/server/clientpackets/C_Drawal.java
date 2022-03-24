package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.types.ULong32;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Drawal extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Drawal.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int castle_id;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    readD();
                    long count = (long) readD();
                    if (count > ULong32.MAX_UNSIGNEDLONG_VALUE) {
                        count = ULong32.MAX_UNSIGNEDLONG_VALUE;
                    }
                    long count2 = Math.max(0L, count);
                    L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                    if (!(clan == null || (castle_id = clan.getCastleId()) == 0)) {
                        L1Castle l1castle = CastleReading.get().getCastleTable(castle_id);
                        long money = l1castle.getPublicMoney() - count2;
                        L1ItemInstance item = ItemTable.get().createItem(L1ItemId.ADENA);
                        if (item != null) {
                            l1castle.setPublicMoney(money);
                            CastleReading.get().updateCastle(l1castle);
                            if (pc.getInventory().checkAddItem(item, count2) == 0) {
                                pc.getInventory().storeItem(L1ItemId.ADENA, count2);
                            } else {
                                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(L1ItemId.ADENA, count2);
                            }
                            pc.sendPackets(new S_ServerMessage(143, "$457", "$4 (" + count2 + ")"));
                        }
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
