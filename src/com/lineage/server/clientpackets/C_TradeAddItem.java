package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.L1Trade;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.types.ULong32;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_TradeAddItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_TradeAddItem.class);

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
                } else {
                    int itemObjid = readD();
                    long itemcount = (long) readD();
                    if (itemcount > ULong32.MAX_UNSIGNEDLONG_VALUE) {
                        itemcount = ULong32.MAX_UNSIGNEDLONG_VALUE;
                    }
                    long itemcount2 = Math.max(0L, itemcount);
                    L1ItemInstance item = pc.getInventory().getItem(itemObjid);
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
                            } else if (item.getBless() >= 128) {
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
                        if (item.isEquipped()) {
                            pc.sendPackets(new S_ServerMessage(141));
                            over();
                            return;
                        }
                        Object[] petlist = pc.getPetList().values().toArray();
                        for (Object petObject : petlist) {
                            if ((petObject instanceof L1PetInstance) && item.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                                pc.sendPackets(new S_ServerMessage(1187));
                                over();
                                return;
                            }
                        }
                        if (pc.getDoll(item.getId()) != null) {
                            pc.sendPackets(new S_ServerMessage(1181));
                            over();
                            return;
                        }
                        L1PcInstance tradingPartner = (L1PcInstance) World.get().findObject(pc.getTradeID());
                        if (tradingPartner == null) {
                            over();
                        } else if (pc.getTradeOk()) {
                            over();
                        } else if (tradingPartner.getInventory().checkAddItem(item, itemcount2) != 0) {
                            tradingPartner.sendPackets(new S_ServerMessage(270));
                            pc.sendPackets(new S_ServerMessage(271));
                            over();
                        } else {
                            L1Trade trade = new L1Trade();
                            if (itemcount2 <= 0) {
                                _log.error("要求增加交易物品傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                                over();
                                return;
                            }
                            trade.tradeAddItem(pc, itemObjid, itemcount2);
                            over();
                        }
                    }
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
