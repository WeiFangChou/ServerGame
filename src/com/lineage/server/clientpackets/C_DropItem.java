package com.lineage.server.clientpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_DropItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_DropItem.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int x = readH();
            int y = readH();
            int objectId = readD();
            int count = readD();
            if (count > Integer.MAX_VALUE) {
                count = Integer.MAX_VALUE;
            }
            int count2 = Math.max(0, count);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (!ConfigAlt.DORP_ITEM && !pc.isGm()) {
                    pc.sendPackets(new S_ServerMessage(125));
                    over();
                } else if (pc.getMapId() < 16384 || pc.getMapId() > 25088) {
                    L1ItemInstance item = pc.getInventory().getItem(objectId);
                    if (item == null) {
                        over();
                    } else if (item.getCount() <= 0) {
                        over();
                    } else {
                        if (!pc.isGm()) {
                            if (!item.getItem().isTradable()) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                over();
                                return;
                            } else if (item.get_time() != null) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                over();
                                return;
                            } else if (ItemRestrictionsTable.RESTRICTIONS.contains(Integer.valueOf(item.getItemId()))) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                over();
                                return;
                            }
                        }
                        Object[] petlist = pc.getPetList().values().toArray();
                        for (Object petObject : petlist) {
                            if ((petObject instanceof L1PetInstance) && item.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                over();
                                return;
                            }
                        }
                        if (pc.getDoll(item.getId()) != null) {
                            pc.sendPackets(new S_ServerMessage(1181));
                            over();
                        } else if (item.isEquipped()) {
                            pc.sendPackets(new S_ServerMessage(125));
                            over();
                        } else if (item.getBless() >= 128) {
                            pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                            over();
                        } else {
                            pc.getInventory().tradeItem(item, count2, pc.get_showId(), World.get().getInventory(x, y, pc.getMapId()));
                            pc.turnOnOffLight();
                            WriteLogTxt.Recording("丟棄物品紀錄", "IP：(" + ((Object) pc.getNetConnection().getIp()) + ")人物：【" + pc.getName() + "】丟棄到地上：【(+" + item.getEnchantLevel() + ")" + item.getName() + "(" + count2 + ")" + "】 ItmeID：┌" + item.getItemId() + "┐ 物品OBJID：┌" + item.getId() + "┐ ");
                            over();
                        }
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(539));
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
