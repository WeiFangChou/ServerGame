package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.types.ULong32;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_PickUpItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_PickUpItem.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else if (pc.isInvisble()) {
                    over();
                } else if (pc.isInvisDelay()) {
                    over();
                } else {
                    int x = readH();
                    int y = readH();
                    int objectId = readD();
                    long pickupCount = (long) readD();
                    if (pickupCount > ULong32.MAX_UNSIGNEDLONG_VALUE) {
                        pickupCount = ULong32.MAX_UNSIGNEDLONG_VALUE;
                    }
                    long pickupCount2 = Math.max(0L, pickupCount);
                    L1Inventory groundInventory = World.get().getInventory(x, y, pc.getMapId());
                    L1Object object = groundInventory.getItem(objectId);
                    if (object != null && !pc.isDead()) {
                        L1ItemInstance item = (L1ItemInstance) object;
                        if (item.getCount() <= 0) {
                            over();
                            return;
                        } else if (item.getItemOwnerId() != 0 && pc.getId() != item.getItemOwnerId()) {
                            pc.sendPackets(new S_ServerMessage(623));
                            over();
                            return;
                        } else if (pc.getLocation().getTileLineDistance(item.getLocation()) > 3) {
                            over();
                            return;
                        } else {
                            item.set_showId(-1);
                            if (!(pc.getInventory().checkAddItem(item, pickupCount2) != 0 || item.getX() == 0 || item.getY() == 0)) {
                                WriteLogTxt.Recording("拾取物品記錄", "IP(" + ((Object) pc.getNetConnection().getIp()) + ")" + "玩家:【" + pc.getName() + "】" + "拾取物品" + "【+" + item.getEnchantLevel() + " " + item.getName() + "(" + pickupCount2 + ")" + "】" + " 物品,");
                                groundInventory.tradeItem(item, pickupCount2, pc.getInventory());
                                pc.turnOnOffLight();
                                pc.setHeading(pc.targetDirection(item.getX(), item.getY()));
                                if (!pc.isGmInvis()) {
                                    pc.broadcastPacketAll(new S_ChangeHeading(pc));
                                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 15));
                                    if (item.getKillDeathName() != null) {
                                        String killName = item.getKillDeathName();
                                        String mapName = MapsTable.get().getMapName(pc.getMapId(), pc.getX(), pc.getY());
                                        item.setKillDeathName(null);
                                        World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "玩家：【" + pc.getName() + "】地點【" + mapName + "】撿到【" + killName + "】物品【" + item.getLogName() + "】。"));
                                    }
                                }
                            }
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
