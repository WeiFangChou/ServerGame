package com.lineage.server.model;

import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.OtherUserTitleReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AddItem;
import com.lineage.server.serverpackets.S_DeleteInventoryItem;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_TradeAddItem;
import com.lineage.server.serverpackets.S_TradeStatus;
import com.lineage.server.templates.L1TradeItem;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Trade {
    private static final Log _log = LogFactory.getLog(L1Trade.class);

    public void tradeAddItem(L1PcInstance pc, int itemObjid, long itemcount) {
        L1PcInstance trading_partner = null;
        try {
            L1PcInstance trading_partner2 = (L1PcInstance) World.get().findObject(pc.getTradeID());
            L1ItemInstance item = pc.getInventory().getItem(itemObjid);
            if (item != null && trading_partner2 != null && !item.isEquipped()) {
                ArrayList<L1TradeItem> map = pc.get_trade_items();
                if (map.size() < 16) {
                    long count = 0;
                    Iterator<L1TradeItem> iter = map.iterator();
                    while (iter.hasNext()) {
                        L1TradeItem tg = iter.next();
                        if (tg.get_objid() == item.getId()) {
                            count += tg.get_count();
                        }
                    }
                    long itemcount2 = Math.max(0L, itemcount);
                    long now_count = itemcount2 + count;
                    if (pc.getInventory().checkItem(item.getItemId(), now_count)) {
                        L1TradeItem info = new L1TradeItem();
                        info.set_objid(item.getId());
                        info.set_item_id(item.getItemId());
                        info.set_item(item);
                        info.set_count(itemcount2);
                        pc.add_trade_item(info);
                        long out_count = item.getCount() - now_count;
                        if (out_count <= 0) {
                            pc.sendPackets(new S_DeleteInventoryItem(item.getId()));
                        } else {
                            pc.sendPackets(new S_ItemStatus(item, out_count));
                        }
                        pc.sendPackets(new S_TradeAddItem(item, itemcount2, 0));
                        trading_partner2.sendPackets(new S_TradeAddItem(item, itemcount2, 1));
                        return;
                    }
                    pc.sendPackets(new S_TradeStatus(1));
                    trading_partner2.sendPackets(new S_TradeStatus(1));
                    pc.setTradeOk(false);
                    trading_partner2.setTradeOk(false);
                    pc.setTradeID(0);
                    trading_partner2.setTradeID(0);
                }
            }
        } catch (Exception e) {
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (0 != 0) {
                trading_partner.get_trade_clear();
            }
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void tradeOK(L1PcInstance pc) {
        L1PcInstance trading_partner = null;
        try {
            L1PcInstance trading_partner2 = (L1PcInstance) World.get().findObject(pc.getTradeID());
            if (trading_partner2 != null) {
                ArrayList<L1TradeItem> map_1 = pc.get_trade_items();
                ArrayList<L1TradeItem> map_2 = trading_partner2.get_trade_items();
                HashMap<L1ItemInstance, Long> temp1 = new HashMap<>();
                HashMap<L1ItemInstance, Long> temp2 = new HashMap<>();
                if (!map_1.isEmpty()) {
                    Iterator<L1TradeItem> iter = map_1.iterator();
                    while (iter.hasNext()) {
                        L1TradeItem tg = iter.next();
                        if (!CharItemsReading.get().getUserItems(pc.getId(), tg.get_objid(), tg.get_count())) {
                            _log.error("人物交易異常(指定數據數量有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                            pc.getNetConnection().kick();
                            trading_partner2.getNetConnection().kick();
                        } else {
                            L1ItemInstance tg_item = pc.getInventory().getItem(tg.get_objid());
                            if (tg_item == null) {
                                _log.error("人物交易異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                                pc.getNetConnection().kick();
                                trading_partner2.getNetConnection().kick();
                            } else {
                                Long count = temp1.get(tg_item);
                                if (count == null) {
                                    temp1.put(tg_item, Long.valueOf(tg.get_count()));
                                } else {
                                    temp1.put(tg_item, Long.valueOf(tg.get_count() + count.longValue()));
                                }
                                pc.getInventory().removeItem(tg.get_objid(), tg.get_count());
                            }
                        }
                    }
                }
                if (!map_2.isEmpty()) {
                    Iterator<L1TradeItem> iter2 = map_2.iterator();
                    while (iter2.hasNext()) {
                        L1TradeItem tg2 = iter2.next();
                        if (!CharItemsReading.get().getUserItems(trading_partner2.getId(), tg2.get_objid(), tg2.get_count())) {
                            _log.error("人物交易異常(指定數據數量有誤): " + trading_partner2.getName() + "/" + trading_partner2.getNetConnection().hashCode());
                            pc.getNetConnection().kick();
                            trading_partner2.getNetConnection().kick();
                        } else {
                            L1ItemInstance tg_item2 = trading_partner2.getInventory().getItem(tg2.get_objid());
                            if (tg_item2 == null) {
                                _log.error("人物交易異常(物品為空): " + trading_partner2.getName() + "/" + trading_partner2.getNetConnection().hashCode());
                                pc.getNetConnection().kick();
                                trading_partner2.getNetConnection().kick();
                            } else {
                                Long count2 = temp2.get(tg_item2);
                                if (count2 == null) {
                                    temp2.put(tg_item2, Long.valueOf(tg2.get_count()));
                                } else {
                                    temp2.put(tg_item2, Long.valueOf(tg2.get_count() + count2.longValue()));
                                }
                                trading_partner2.getInventory().removeItem(tg2.get_objid(), tg2.get_count());
                            }
                        }
                    }
                }
                if (!temp1.isEmpty()) {
                    for (L1ItemInstance item : temp1.keySet()) {
                        long count3 = temp1.get(item).longValue();
                        if (item.isStackable()) {
                            L1ItemInstance tgItem = ItemTable.get().createItem(item.getItemId());
                            tgItem.setCount(count3);
                            trading_partner2.getInventory().storeItem(tgItem);
                            WriteLogTxt.Recording("被邀請紀錄", "IP(" + ((Object) pc.getNetConnection().getIp()) + ")" + "玩家" + ":【" + pc.getName() + "】 " + "的" + "【+" + item.getEnchantLevel() + " " + item.getName() + "(" + item.getCount() + ")" + "】" + " 轉移給玩家" + ":【" + trading_partner2.getName() + "】，" + "地點: " + pc.getLocation());
                        } else {
                            trading_partner2.getInventory().storeItem(item);
                        }
                        OtherUserTitleReading.get().add(String.valueOf(item.getItem().getName()) + "(" + item.getItemId() + ")", item.getId(), 0, count3, trading_partner2.getId(), trading_partner2.getName(), pc.getId(), pc.getName());
                    }
                }
                if (!temp2.isEmpty()) {
                    for (L1ItemInstance item2 : temp2.keySet()) {
                        long count4 = temp2.get(item2).longValue();
                        if (item2.isStackable()) {
                            L1ItemInstance tgItem2 = ItemTable.get().createItem(item2.getItemId());
                            tgItem2.setCount(count4);
                            pc.getInventory().storeItem(tgItem2);
                            WriteLogTxt.Recording("主動找紀錄", "IP(" + ((Object) trading_partner2.getNetConnection().getIp()) + ")" + "玩家" + ":【" + trading_partner2.getName() + "】 " + "的" + "【+" + item2.getEnchantLevel() + " " + item2.getName() + "(" + item2.getCount() + ")" + "】" + " 轉移給玩家" + ":【" + pc.getName() + "】，" + "地點: " + pc.getLocation() + "時間:" + "(" + TimeInform.getNowTime(3, 0) + ")。");
                        } else {
                            pc.getInventory().storeItem(item2);
                        }
                        OtherUserTitleReading.get().add(String.valueOf(item2.getItem().getName()) + "(" + item2.getItemId() + ")", item2.getId(), 0, count4, pc.getId(), pc.getName(), trading_partner2.getId(), trading_partner2.getName());
                    }
                }
                temp1.clear();
                temp2.clear();
                pc.sendPackets(new S_TradeStatus(0));
                trading_partner2.sendPackets(new S_TradeStatus(0));
                pc.setTradeOk(false);
                trading_partner2.setTradeOk(false);
                pc.setTradeID(0);
                trading_partner2.setTradeID(0);
            }
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (trading_partner2 != null) {
                trading_partner2.get_trade_clear();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (0 != 0) {
                trading_partner.get_trade_clear();
            }
        } catch (Throwable th) {
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (0 != 0) {
                trading_partner.get_trade_clear();
            }
            throw th;
        }
    }

    public void tradeCancel(L1PcInstance pc) {
        L1PcInstance trading_partner = null;
        try {
            L1PcInstance trading_partner2 = (L1PcInstance) World.get().findObject(pc.getTradeID());
            if (trading_partner2 != null) {
                ArrayList<L1TradeItem> map_1 = pc.get_trade_items();
                ArrayList<L1TradeItem> map_2 = trading_partner2.get_trade_items();
                HashMap<Integer, Long> temp1 = new HashMap<>();
                HashMap<Integer, Long> temp2 = new HashMap<>();
                if (!map_1.isEmpty()) {
                    Iterator<L1TradeItem> iter = map_1.iterator();
                    while (iter.hasNext()) {
                        L1TradeItem tg = iter.next();
                        Long count = temp1.get(Integer.valueOf(tg.get_objid()));
                        if (count == null) {
                            temp1.put(Integer.valueOf(tg.get_objid()), Long.valueOf(tg.get_count()));
                        } else {
                            temp1.put(Integer.valueOf(tg.get_objid()), Long.valueOf(tg.get_count() + count.longValue()));
                        }
                    }
                }
                if (!map_2.isEmpty()) {
                    Iterator<L1TradeItem> iter2 = map_2.iterator();
                    while (iter2.hasNext()) {
                        L1TradeItem tg2 = iter2.next();
                        Long count2 = temp2.get(Integer.valueOf(tg2.get_objid()));
                        if (count2 == null) {
                            temp2.put(Integer.valueOf(tg2.get_objid()), Long.valueOf(tg2.get_count()));
                        } else {
                            temp2.put(Integer.valueOf(tg2.get_objid()), Long.valueOf(tg2.get_count() + count2.longValue()));
                        }
                    }
                }
                if (!temp1.isEmpty()) {
                    for (Integer key : temp1.keySet()) {
                        long count3 = temp1.get(key).longValue();
                        L1ItemInstance tg_item = pc.getInventory().getItem(key.intValue());
                        if (tg_item != null) {
                            if (count3 == tg_item.getCount()) {
                                pc.sendPackets(new S_AddItem(tg_item));
                            } else {
                                pc.sendPackets(new S_ItemStatus(tg_item, tg_item.getCount()));
                            }
                        }
                    }
                }
                if (!temp2.isEmpty()) {
                    for (Integer key2 : temp2.keySet()) {
                        long count4 = temp2.get(key2).longValue();
                        L1ItemInstance tg_item2 = trading_partner2.getInventory().getItem(key2.intValue());
                        if (count4 == tg_item2.getCount()) {
                            trading_partner2.sendPackets(new S_AddItem(tg_item2));
                        } else {
                            trading_partner2.sendPackets(new S_ItemStatus(tg_item2, tg_item2.getCount()));
                        }
                    }
                }
                temp1.clear();
                temp2.clear();
                pc.sendPackets(new S_TradeStatus(1));
                trading_partner2.sendPackets(new S_TradeStatus(1));
                pc.setTradeOk(false);
                trading_partner2.setTradeOk(false);
                pc.setTradeID(0);
                trading_partner2.setTradeID(0);
            }
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (trading_partner2 != null) {
                trading_partner2.get_trade_clear();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (0 != 0) {
                trading_partner.get_trade_clear();
            }
        } catch (Throwable th) {
            if (pc != null) {
                pc.get_trade_clear();
            }
            if (0 != 0) {
                trading_partner.get_trade_clear();
            }
            throw th;
        }
    }
}
