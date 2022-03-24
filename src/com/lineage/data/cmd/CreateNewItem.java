package com.lineage.data.cmd;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharShiftingReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.DigitalUtil;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CreateNewItem {
    private static final Log _log = LogFactory.getLog(CreateNewItem.class);

    public static boolean delItems(L1PcInstance pc, int[] srcItemIds, int[] counts, long amount) {
        if (pc == null || amount <= 0) {
            return false;
        }
        try {
            if (srcItemIds.length <= 0 || counts.length <= 0) {
                return false;
            }
            if (srcItemIds.length != counts.length) {
                _log.error("道具交換物品與數量陣列設置異常!");
                return false;
            }
            for (int i = 0; i < srcItemIds.length; i++) {
                if (pc.getInventory().checkItemX(srcItemIds[i], ((long) counts[i]) * amount) == null) {
                    return false;
                }
            }
            for (int i2 = 0; i2 < srcItemIds.length; i2++) {
                long itemCount1 = ((long) counts[i2]) * amount;
                L1ItemInstance item = pc.getInventory().checkItemX(srcItemIds[i2], itemCount1);
                if (item == null) {
                    return false;
                }
                pc.getInventory().removeItem(item, itemCount1);
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public static boolean getItem(L1PcInstance pc, L1NpcInstance npc, String cmd, int[] items, int[] counts, int[] gitems, int[] gcounts, long amount) {
        long xcount = checkNewItem(pc, items, counts);
        if (xcount <= 0) {
            return true;
        }
        if (amount == 0) {
            pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, cmd));
            return false;
        }
        if (xcount >= amount) {
            createNewItem(pc, items, counts, gitems, amount, gcounts);
        }
        return true;
    }

    public static long checkNewItem(L1PcInstance pc, int srcItemId, int count) {
        if (pc == null) {
            return -1;
        }
        try {
            L1ItemInstance item = pc.getInventory().findItemIdNoEq(srcItemId);
            long itemCount = -1;
            if (item != null) {
                itemCount = item.getCount() / ((long) count);
            }
            if (itemCount >= 1) {
                return itemCount;
            }
            pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(srcItemId).getName()) + "(" + (((long) count) - (item == null ? 0 : item.getCount())) + ")"));
            return -1;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return -1;
        }
    }

    public static long checkNewItem(L1PcInstance pc, int[] srcItemIds, int[] counts) {
        if (pc == null) {
            return -1;
        }
        try {
            if (srcItemIds.length <= 0) {
                return -1;
            }
            if (counts.length <= 0) {
                return -1;
            }
            if (srcItemIds.length != counts.length) {
                _log.error("道具交換物品與數量陣列設置異常!");
                return -1;
            }
            long[] checkCount = new long[srcItemIds.length];
            boolean error = false;
            for (int i = 0; i < srcItemIds.length; i++) {
                int itemid = srcItemIds[i];
                int count = counts[i];
                L1ItemInstance item = pc.getInventory().findItemIdNoEq(itemid);
                if (item != null) {
                    long itemCount = item.getCount() / ((long) count);
                    checkCount[i] = itemCount;
                    if (itemCount < 1) {
                        pc.sendPackets(new S_ServerMessage(337, String.valueOf(item.getName()) + "(" + (((long) count) - item.getCount()) + ")"));
                        error = true;
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(itemid).getName()) + "(" + count + ")"));
                    error = true;
                }
            }
            if (!error) {
                return DigitalUtil.returnMin(checkCount);
            }
            return -1;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return  -1;
        }
    }

    public static void createNewItem(L1PcInstance pc, int[] srcItemIds, int[] counts, int[] getItemIds, long amount, int[] getCounts) {
        if (pc != null && amount > 0) {
            try {
                if (srcItemIds.length > 0 && counts.length > 0) {
                    if (srcItemIds.length != counts.length) {
                        _log.error("道具交換物品與數量陣列設置異常!");
                    } else if (getItemIds.length > 0 && getCounts.length > 0) {
                        if (getItemIds.length != getCounts.length) {
                            _log.error("道具交換物品與數量陣列設置異常!");
                            return;
                        }
                        boolean error = false;
                        for (int i = 0; i < srcItemIds.length; i++) {
                            if (pc.getInventory().checkItemX(srcItemIds[i], ((long) counts[i]) * amount) == null) {
                                error = true;
                            }
                        }
                        if (!error) {
                            for (int i2 = 0; i2 < getItemIds.length; i2++) {
                                if (!getItemIsOk(pc, getItemIds[i2], amount, getCounts[i2])) {
                                    error = true;
                                }
                            }
                        }
                        if (!error) {
                            for (int i3 = 0; i3 < srcItemIds.length; i3++) {
                                long itemCount1 = ((long) counts[i3]) * amount;
                                L1ItemInstance item = pc.getInventory().checkItemX(srcItemIds[i3], itemCount1);
                                if (item != null) {
                                    pc.getInventory().removeItem(item, itemCount1);
                                } else {
                                    error = true;
                                }
                            }
                        }
                        if (!error) {
                            for (int i4 = 0; i4 < getItemIds.length; i4++) {
                                getItemIs(pc, getItemIds[i4], amount, getCounts[i4]);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private static boolean getItemIsOk(L1PcInstance pc, int getItemId, long amount, int getCount) {
        if (pc == null) {
            return false;
        }
        try {
            if (pc.getInventory().checkAddItem(ItemTable.get().getTemplate(getItemId), ((long) getCount) * amount) == 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return  false;
        }
    }

    private static void getItemIs(L1PcInstance pc, int getItemId, long amount, int getCount) {
        if (pc != null) {
            try {
                if (ItemTable.get().getTemplate(getItemId).isStackable()) {
                    L1ItemInstance tgItemX = ItemTable.get().createItem(getItemId);
                    tgItemX.setCount(((long) getCount) * amount);
                    createNewItem(pc, tgItemX);
                    return;
                }
                for (int get = 0; ((long) get) < ((long) getCount) * amount; get++) {
                    L1ItemInstance tgItemX2 = ItemTable.get().createItem(getItemId);
                    tgItemX2.setCount(1);
                    createNewItem(pc, tgItemX2);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void createNewItem(L1PcInstance pc, int srcItemId, int count, int getItemId, int getCount) throws Exception {
        createNewItem(pc, srcItemId, count, getItemId, 1, getCount);
    }

    public static void createNewItem(L1PcInstance pc, int srcItemId, int count, int getItemId, long amount, int getCount) throws Exception {
        long itemCount1 = ((long) count) * amount;
        L1ItemInstance item1 = pc.getInventory().checkItemX(srcItemId, itemCount1);
        if (item1 != null) {
            L1ItemInstance tgItem = ItemTable.get().createItem(getItemId);
            if (pc.getInventory().checkAddItem(tgItem, ((long) getCount) * amount) == 0) {
                pc.getInventory().removeItem(item1, itemCount1);
                if (tgItem.isStackable()) {
                    tgItem.setCount(((long) getCount) * amount);
                    createNewItem(pc, tgItem);
                    return;
                }
                for (int get = 0; ((long) get) < ((long) getCount) * amount; get++) {
                    L1ItemInstance tgItemX = ItemTable.get().createItem(getItemId);
                    tgItemX.setCount(1);
                    createNewItem(pc, tgItemX);
                }
                return;
            }
            World.get().removeObject(tgItem);
        }
    }

    public static void createNewItem(L1PcInstance pc, int srcItemId, int count, int getItemId, long amount) throws Exception {
        long itemCount1 = ((long) count) * amount;
        L1ItemInstance item1 = pc.getInventory().checkItemX(srcItemId, itemCount1);
        if (item1 != null) {
            L1ItemInstance tgItem = ItemTable.get().createItem(getItemId);
            if (pc.getInventory().checkAddItem(tgItem, amount) == 0) {
                pc.getInventory().removeItem(item1, itemCount1);
                if (tgItem.isStackable()) {
                    tgItem.setCount(amount);
                    createNewItem(pc, tgItem);
                    return;
                }
                for (int get = 0; ((long) get) < amount; get++) {
                    L1ItemInstance tgItemX = ItemTable.get().createItem(getItemId);
                    tgItemX.setCount(1);
                    createNewItem(pc, tgItemX);
                }
            }
        }
    }

    public static void createNewItem(L1PcInstance pc, L1ItemInstance item) {
        if (pc != null && item != null) {
            try {
                pc.getInventory().storeItem(item);
                pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void createNewItem(L1PcInstance pc, L1ItemInstance item, long count) {
        if (pc != null && item != null) {
            try {
                item.setCount(count);
                if (pc.getInventory().checkAddItem(item, count) == 0) {
                    pc.getInventory().storeItem(item);
                } else {
                    item.set_showId(pc.get_showId());
                    World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
                }
                pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static boolean createNewItem(L1PcInstance pc, int item_id, long count) {
        if (pc == null) {
            return false;
        }
        try {
            L1ItemInstance item = ItemTable.get().createItem(item_id);
            if (item != null) {
                item.setCount(count);
                if (pc.getInventory().checkAddItem(item, count) == 0) {
                    pc.getInventory().storeItem(item);
                } else {
                    item.set_showId(pc.get_showId());
                    World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
                }
                pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                return true;
            }
            _log.error("給予物件失敗 原因: 指定編號物品不存在(" + item_id + ")");
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public static void getQuestItem(L1Character atk, L1NpcInstance npc, int item_id, long count) {
        if (atk != null) {
            try {
                L1ItemInstance item = ItemTable.get().createItem(item_id);
                if (item != null) {
                    item.setCount(count);
                    if (atk.getInventory().checkAddItem(item, count) == 0) {
                        atk.getInventory().storeItem(item);
                    } else {
                        item.set_showId(atk.get_showId());
                        World.get().getInventory(atk.getX(), atk.getY(), atk.getMapId()).storeItem(item);
                    }
                    if (atk instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance) atk;
                        if (npc != null) {
                            pc.sendPackets(new S_HelpMessage("\\fW" + npc.getNameId() + "給你" + item.getLogName()));
                        } else {
                            pc.sendPackets(new S_HelpMessage("\\fW給你", item.getLogName()));
                        }
                    }
                } else {
                    _log.error("給予物件失敗 原因: 指定編號物品不存在(" + item_id + ")");
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void updateA(L1PcInstance pc, L1ItemInstance srcItem, L1ItemInstance newItem, int enchant, int down, int mode) {
        if (pc != null && srcItem != null && newItem != null) {
            try {
                newItem.setCount(1);
                if (srcItem.getEnchantLevel() > enchant) {
                    newItem.setEnchantLevel(srcItem.getEnchantLevel() - down);
                } else {
                    newItem.setEnchantLevel(srcItem.getEnchantLevel());
                }
                newItem.setAttrEnchantKind(srcItem.getAttrEnchantKind());
                newItem.setAttrEnchantLevel(srcItem.getAttrEnchantLevel());
                newItem.setIdentified(true);
                int srcObjid = srcItem.getId();
                L1Item srcItemX = srcItem.getItem();
                if (pc.getInventory().removeItem(srcItem) == 1) {
                    pc.getInventory().storeItem(newItem);
                    pc.sendPackets(new S_ServerMessage(403, newItem.getLogName()));
                    CharShiftingReading.get().newShifting(pc, 0, null, srcObjid, srcItemX, newItem, mode);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void updateB(L1PcInstance pc, L1ItemInstance srcItem, int newid) {
        if (pc != null && srcItem != null) {
            try {
                L1ItemInstance newItem = ItemTable.get().createItem(newid);
                if (newItem == null) {
                    _log.error("給予物件失敗 原因: 指定編號物品不存在(" + newid + ")");
                } else if (pc.getInventory().removeItem(srcItem) == 1) {
                    pc.getInventory().storeItem(newItem);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
