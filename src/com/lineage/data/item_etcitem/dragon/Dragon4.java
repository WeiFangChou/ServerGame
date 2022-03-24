package com.lineage.data.item_etcitem.dragon;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Dragon4 extends ItemExecutor {
    private final int[] _items1 = {40341, 40342, 40343, 40344, 40345, 40346, 40347, 40348};
    private final int[] _items2 = {40349, 40350, 40351, 40352, 40353, 40354, 40355, 40356};
    private final int[] _items3 = {40357, 40358, 40359, 40360, 40361, 40362, 40363, 40364};
    private final int[] _items4 = {40365, 40366, 40367, 40368, 40369, 40370, 40371, 40372};

    private Dragon4() {
    }

    public static ItemExecutor get() {
        return new Dragon4();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        switch (item.getItemId()) {
            case 40341:
            case 40342:
            case 40343:
            case 40344:
            case 40345:
            case 40346:
            case 40347:
            case 40348:
                if (allItem(pc)) {
                    delAll(pc, null, 49500);
                    return;
                } else if (allItem(pc, this._items1)) {
                    delAll(pc, this._items1, 49504);
                    return;
                }
                break;
            case 40349:
            case 40350:
            case 40351:
            case 40352:
            case 40353:
            case 40354:
            case 40355:
            case 40356:
                if (allItem(pc)) {
                    delAll(pc, null, 49500);
                    return;
                } else if (allItem(pc, this._items2)) {
                    delAll(pc, this._items2, 49501);
                    return;
                }
                break;
            case 40357:
            case 40358:
            case 40359:
            case 40360:
            case 40361:
            case 40362:
            case 40363:
            case 40364:
                if (allItem(pc)) {
                    delAll(pc, null, 49500);
                    return;
                } else if (allItem(pc, this._items3)) {
                    delAll(pc, this._items3, 49502);
                    return;
                }
                break;
            case 40365:
            case 40366:
            case 40367:
            case 40368:
            case 40369:
            case 40370:
            case 40371:
            case 40372:
                if (allItem(pc)) {
                    delAll(pc, null, 49500);
                    return;
                } else if (allItem(pc, this._items4)) {
                    delAll(pc, this._items4, 49503);
                    return;
                }
                break;
        }
        pc.sendPackets(new S_ServerMessage(74, item.getLogName()));
    }

    private void delAll(L1PcInstance pc, int[] items, int mode) throws Exception {
        int i = 0;
        boolean isError = false;
        if (items == null) {
            for (int itemid : this._items1) {
                if (!pc.getInventory().consumeItem(itemid, 1)) {
                    isError = true;
                }
            }
            for (int itemid2 : this._items2) {
                if (!pc.getInventory().consumeItem(itemid2, 1)) {
                    isError = true;
                }
            }
            for (int itemid3 : this._items3) {
                if (!pc.getInventory().consumeItem(itemid3, 1)) {
                    isError = true;
                }
            }
            int[] iArr = this._items4;
            int length = iArr.length;
            while (i < length) {
                if (!pc.getInventory().consumeItem(iArr[i], 1)) {
                    isError = true;
                }
                i++;
            }
        } else {
            int length2 = items.length;
            while (i < length2) {
                if (!pc.getInventory().consumeItem(items[i], 1)) {
                    isError = true;
                }
                i++;
            }
        }
        if (isError) {
            pc.sendPackets(new S_ServerMessage(79));
        } else {
            CreateNewItem.createNewItem(pc, mode, 1);
        }
    }

    private boolean allItem(L1PcInstance pc) {
        int check = 0;
        for (int itemid : this._items1) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        for (int itemid2 : this._items2) {
            if (pc.getInventory().checkItem(itemid2)) {
                check++;
            }
        }
        for (int itemid3 : this._items3) {
            if (pc.getInventory().checkItem(itemid3)) {
                check++;
            }
        }
        for (int itemid4 : this._items4) {
            if (pc.getInventory().checkItem(itemid4)) {
                check++;
            }
        }
        if (check == 32) {
            return true;
        }
        return false;
    }

    private boolean allItem(L1PcInstance pc, int[] items) {
        int check = 0;
        for (int itemid : items) {
            if (pc.getInventory().checkItem(itemid)) {
                check++;
            }
        }
        if (check == 8) {
            return true;
        }
        return false;
    }
}
