package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.server.IdFactory;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ItemTimeTable;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.datatables.lock.FurnitureSpawnReading;
import com.lineage.server.datatables.sql.LetterTable;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_EquipmentWindow;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemTime;
import com.lineage.server.world.World;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Inventory extends L1Object {
    public static final int AMOUNT_OVER = 3;
    public static final int MAX_WEIGHT = 1500;

    /* renamed from: OK */
    public static final int f16OK = 0;
    public static final int SIZE_OVER = 1;
    public static final int WAREHOUSE_TYPE_CLAN = 1;
    public static final int WAREHOUSE_TYPE_PERSONAL = 0;
    public static final int WEIGHT_OVER = 2;
    private static final Log _log = LogFactory.getLog(L1Inventory.class);
    private static final long serialVersionUID = 1;
    protected List<L1ItemInstance> _items = new CopyOnWriteArrayList();
    public int[] slot_earring = new int[2];
    public int[] slot_ring = new int[4];

    public void toSlotPacket(L1PcInstance pc, L1ItemInstance item) {
        int select_idx = -1;
        int idx = 0;
        if (item.getItem().getType2() == 2) {
            switch (item.getItem().getType()) {
                case 1:
                case 2:
                case 3:
                case 4:
                    idx = item.getItem().getType();
                    break;
                case 5:
                    idx = 6;
                    break;
                case 6:
                    idx = 5;
                    break;
                case 7:
                case 13:
                    idx = 7;
                    break;
                case 8:
                    idx = 10;
                    break;
                case 9:
                    for (int i = 0; i < this.slot_ring.length; i++) {
                        if (this.slot_ring[i] == item.getId()) {
                            select_idx = i;
                        }
                    }
                    if (item.isEquipped() && select_idx == -1) {
                        int i2 = 0;
                        while (true) {
                            if (i2 < this.slot_ring.length) {
                                if (this.slot_ring[i2] == 0) {
                                    this.slot_ring[i2] = item.getId();
                                    idx = i2 + 18;
                                } else {
                                    i2++;
                                }
                            }
                        }
                    }
                    if (!item.isEquipped() && select_idx != -1) {
                        this.slot_ring[select_idx] = 0;
                        idx = select_idx + 18;
                        break;
                    }
                case 10:
                    idx = 11;
                    break;
                case 12:
                    for (int i3 = 0; i3 < this.slot_earring.length; i3++) {
                        if (this.slot_earring[i3] == item.getId()) {
                            select_idx = i3;
                        }
                    }
                    if (item.isEquipped() && select_idx == -1) {
                        int i4 = 0;
                        while (true) {
                            if (i4 < this.slot_earring.length) {
                                if (this.slot_earring[i4] == 0) {
                                    this.slot_earring[i4] = item.getId();
                                    idx = i4 + 12;
                                } else {
                                    i4++;
                                }
                            }
                        }
                    }
                    if (!item.isEquipped() && select_idx != -1) {
                        this.slot_earring[select_idx] = 0;
                        idx = select_idx + 12;
                        break;
                    }
                case 14:
                    idx = 22;
                    break;
                case 15:
                    idx = 23;
                    break;
                case 16:
                    idx = 24;
                    break;
                case 17:
                    idx = 14;
                    break;
                case 18:
                    idx = 25;
                    break;
                case 19:
                    idx = 26;
                    break;
                case 20:
                    idx = 27;
                    break;
            }
        } else if (item.getItem().getType2() == 1) {
            if (pc.getWeapon() == null) {
                idx = 8;
            } else if (item.isEquipped()) {
                idx = 8;
            }
        }
        if (idx != 0) {
            pc.sendPackets(new S_EquipmentWindow(pc, item.getId(), idx, item.isEquipped()));
        }
    }

    public int getSize() {
        if (this._items.isEmpty()) {
            return 0;
        }
        return this._items.size();
    }

    public List<L1ItemInstance> getItems() {
        return this._items;
    }

    public int getWeight() {
        int weight = 0;
        for (L1ItemInstance item : this._items) {
            weight += item.getWeight();
        }
        return weight;
    }

    public int checkAddItem(int item, long count) {
        return -1;
    }

    public int checkAddItem(L1ItemInstance item, long count) {
        if (item == null || item.getCount() <= 0 || count <= 0) {
            return -1;
        }
        if (getSize() > ConfigAlt.MAX_NPC_ITEM || (getSize() == ConfigAlt.MAX_NPC_ITEM && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
            return 1;
        }
        long weight = ((long) getWeight()) + ((((long) item.getItem().getWeight()) * count) / 1000) + serialVersionUID;
        if (weight < 0 || (((long) item.getItem().getWeight()) * count) / 1000 < 0 || ((double) weight) > 1500.0d * ConfigRate.RATE_WEIGHT_LIMIT_PET) {
            return 2;
        }
        L1ItemInstance itemExist = findItemId(item.getItemId());
        if (itemExist == null || itemExist.getCount() + count <= Long.MAX_VALUE) {
            return 0;
        }
        return 3;
    }

    public int checkAddItemToWarehouse(L1ItemInstance item, long count, int type) {
        if (item == null || item.getCount() <= 0 || count <= 0) {
            return -1;
        }
        int maxSize = 100;
        if (type == 0) {
            maxSize = ConfigAlt.MAX_PERSONAL_WAREHOUSE_ITEM;
        } else if (type == 1) {
            maxSize = ConfigAlt.MAX_CLAN_WAREHOUSE_ITEM;
        }
        if (getSize() > maxSize || (getSize() == maxSize && (!item.isStackable() || !checkItem(item.getItem().getItemId())))) {
            return 1;
        }
        return 0;
    }

    public synchronized L1ItemInstance storeItem(int id, long count) {
        L1ItemInstance result;
        if (count <= 0) {
            result = null;
        } else {
            try {
                L1Item temp = ItemTable.get().getTemplate(id);
                if (temp == null) {
                    result = null;
                } else if (temp.isStackable()) {
                    L1ItemInstance item = new L1ItemInstance(temp, count);
                    if (findItemId(id) == null) {
                        item.setId(IdFactory.get().nextId());
                        World.get().storeObject(item);
                    }
                    result = storeItem(item);
                } else {
                    result = null;
                    for (int i = 0; ((long) i) < count; i++) {
                        L1ItemInstance item2 = new L1ItemInstance(temp, serialVersionUID);
                        item2.setId(IdFactory.get().nextId());
                        World.get().storeObject(item2);
                        storeItem(item2);
                        result = item2;
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                result = null;
            }
        }
        return result;
    }

    public synchronized L1ItemInstance storeItem(L1ItemInstance item) {
        L1ItemInstance tgitem;
        L1PcInstance pc;
        if (item == null) {
            tgitem = null;
        } else {
            try {
                if (item.getCount() <= 0) {
                    tgitem = null;
                } else {
                    if (item.isStackable()) {
                        if (item.getItem().getUseType() == -5) {
                            L1ItemInstance[] items = findItemsId(item.getItemId());
                            int length = items.length;
                            int i = 0;
                            while (true) {
                                if (i >= length) {
                                    break;
                                }
                                tgitem = items[i];
                                if (item.getGamNo().equals(tgitem.getGamNo())) {
                                    tgitem.setCount(tgitem.getCount() + item.getCount());
                                    updateItem(tgitem);
                                    break;
                                }
                                i++;
                            }
                        } else {
                            L1ItemInstance findItem = findItemId(item.getItem().getItemId());
                            if (findItem != null) {
                                findItem.setCount(findItem.getCount() + item.getCount());
                                updateItem(findItem);
                                tgitem = findItem;
                            }
                        }
                    }
                    item.setX(getX());
                    item.setY(getY());
                    item.setMap(getMapId());
                    int chargeCount = item.getItem().getMaxChargeCount();
                    switch (item.getItem().getItemId()) {
                        case 20383:
                            chargeCount = 50;
                            break;
                        case 40006:
                        case 40007:
                        case 40008:
                        case 40009:
                        case 140006:
                        case 140008:
                            chargeCount -= new Random().nextInt(5);
                            break;
                    }
                    if (item.getChargeCount() > 0) {
                        item.setChargeCount(item.getChargeCount());
                    } else {
                        item.setChargeCount(chargeCount);
                    }
                    if (item.getRemainingTime() > 0) {
                        item.setRemainingTime(item.getRemainingTime());
                    }
                    L1ItemTime itemTime = ItemTimeTable.TIME.get(Integer.valueOf(item.getItemId()));
                    if (itemTime != null) {
                        Timestamp ts = new Timestamp(System.currentTimeMillis() + ((long) (86400000 * itemTime.get_remain_time())));
                        item.set_time(ts);
                        CharItemsTimeReading.get().addTime(item.getId(), ts);
                        if ((this instanceof L1PcInventory) && (pc = ((L1PcInventory) this).getOwner()) != null) {
                            WriteLogTxt.Recording("時效物品紀錄", "人物: " + pc.getName() + ", 道具: " + item.getName() + ", 到期日: " + ts);
                        }
                    }
                    if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
                        item.setRemainingTime(item.getItem().getLightFuel());
                    } else {
                        item.setRemainingTime(item.getItem().getMaxUseTime());
                    }
                    item.setBless(item.getItem().getBless());
                    this._items.add(item);
                    insertItem(item);
                    tgitem = item;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                tgitem = null;
            }
        }
        return tgitem;
    }

    public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
        L1ItemInstance tgitem;
        if (item == null) {
            tgitem = null;
        } else {
            try {
                if (item.getCount() <= 0) {
                    tgitem = null;
                } else {
                    if (item.isStackable()) {
                        if (item.getItem().getUseType() == -5) {
                            L1ItemInstance[] items = findItemsId(item.getItemId());
                            int length = items.length;
                            int i = 0;
                            while (true) {
                                if (i >= length) {
                                    break;
                                }
                                tgitem = items[i];
                                if (item.getGamNo().equals(tgitem.getGamNo())) {
                                    tgitem.setCount(tgitem.getCount() + item.getCount());
                                    updateItem(tgitem);
                                    break;
                                }
                                i++;
                            }
                        } else {
                            L1ItemInstance findItem = findItemId(item.getItem().getItemId());
                            if (findItem != null) {
                                findItem.setCount(findItem.getCount() + item.getCount());
                                updateItem(findItem);
                                tgitem = findItem;
                            }
                        }
                    }
                    item.setX(getX());
                    item.setY(getY());
                    item.setMap(getMapId());
                    this._items.add(item);
                    insertItem(item);
                    tgitem = item;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                tgitem = null;
            }
        }
        return tgitem;
    }

    public boolean consumeItem(int itemid, long count) throws Exception {
        if (count <= 0) {
            return false;
        }
        if (ItemTable.get().getTemplate(itemid).isStackable()) {
            L1ItemInstance item = findItemId(itemid);
            if (item == null || item.getCount() < count) {
                return false;
            }
            removeItem(item, count);
            return true;
        }
        L1ItemInstance[] itemList = findItemsId(itemid);
        if (((long) itemList.length) == count) {
            for (int i = 0; ((long) i) < count; i++) {
                removeItem(itemList[i], serialVersionUID);
            }
            return true;
        } else if (((long) itemList.length) <= count) {
            return false;
        } else {
            Arrays.sort(itemList, new DataComparator());
            for (int i2 = 0; ((long) i2) < count; i2++) {
                removeItem(itemList[i2], serialVersionUID);
            }
            return true;
        }
    }

    public class DataComparator implements Comparator<Object> {
        public DataComparator() {
        }

        @Override // java.util.Comparator
        public int compare(Object item1, Object item2) {
            return ((L1ItemInstance) item1).getEnchantLevel() - ((L1ItemInstance) item2).getEnchantLevel();
        }
    }

    public L1ItemInstance shiftingItem(int objectId, long count) {
        L1ItemInstance item = getItem(objectId);
        if (item == null) {
            return null;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return null;
        }
        if (item.getCount() < count) {
            return null;
        }
        if (item.getCount() != count || item.isEquipped()) {
            return null;
        }
        deleteItem(item);
        return item;
    }

    public long removeItem(int objectId, long count) throws Exception {
        return removeItem(getItem(objectId), count);
    }

    public long removeItem(L1ItemInstance item) throws Exception {
        return removeItem(item, item.getCount());
    }

    public long removeItem(L1ItemInstance item, long count) throws Exception {
        if (item == null) {
            return 0;
        }
        if (!this._items.contains(item)) {
            return 0;
        }
        if (item.getCount() <= 0 || count <= 0) {
            return 0;
        }
        if (item.getCount() < count) {
            count = item.getCount();
        }
        if (item.getCount() == count) {
            int itemId = item.getItem().getItemId();
            if (itemId >= 49016 && itemId <= 49025) {
                new LetterTable().deleteLetter(item.getId());
            } else if (itemId >= 41383 && itemId <= 41400) {
                for (L1Object l1object : World.get().getObject()) {
                    if (l1object instanceof L1FurnitureInstance) {
                        L1FurnitureInstance furniture = (L1FurnitureInstance) l1object;
                        if (furniture.getItemObjId() == item.getId()) {
                            FurnitureSpawnReading.get().deleteFurniture(furniture);
                        }
                    }
                }
            }
            deleteItem(item);
            World.get().removeObject(item);
        } else {
            item.setCount(item.getCount() - count);
            updateItem(item);
        }
        return count;
    }

    public void deleteItem(L1ItemInstance item) {
        this._items.remove(item);
    }

    public synchronized L1ItemInstance tradeItem(int objectId, long count, L1Inventory inventory) throws Exception {
        return tradeItem(getItem(objectId), count, inventory);
    }

    public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, int showId, L1GroundInventory inventory) throws Exception {
        L1ItemInstance carryItem;
        L1ItemInstance l1ItemInstance = null;
        synchronized (this) {
            if (item != null) {
                if (item.getCount() > 0 && count > 0 && !item.isEquipped() && item.getCount() >= ((long) count)) {
                    if (item.getCount() == ((long) count)) {
                        deleteItem(item);
                        carryItem = item;
                        carryItem.set_showId(showId);
                    } else {
                        item.setCount(item.getCount() - ((long) count));
                        updateItem(item);
                        carryItem = ItemTable.get().createItem(item.getItem().getItemId());
                        carryItem.set_showId(showId);
                        carryItem.setCount((long) count);
                        carryItem.setEnchantLevel(item.getEnchantLevel());
                        carryItem.setIdentified(item.isIdentified());
                        carryItem.set_durability(item.get_durability());
                        carryItem.setChargeCount(item.getChargeCount());
                        carryItem.setRemainingTime(item.getRemainingTime());
                        carryItem.setLastUsed(item.getLastUsed());
                        carryItem.setBless(item.getBless());
                    }
                    l1ItemInstance = inventory.storeTradeItem(carryItem);
                }
            }
        }
        return l1ItemInstance;
    }

    public synchronized L1ItemInstance tradeItem(L1ItemInstance item, long count, L1Inventory inventory) throws Exception {
        L1ItemInstance carryItem;
        L1ItemInstance l1ItemInstance = null;
        synchronized (this) {
            if (item != null) {
                if (item.getCount() > 0 && count > 0 && !item.isEquipped() && item.getCount() >= count) {
                    if (item.getCount() == count) {
                        deleteItem(item);
                        carryItem = item;
                    } else {
                        item.setCount(item.getCount() - count);
                        updateItem(item);
                        carryItem = ItemTable.get().createItem(item.getItem().getItemId());
                        carryItem.setCount(count);
                        carryItem.setEnchantLevel(item.getEnchantLevel());
                        carryItem.setIdentified(item.isIdentified());
                        carryItem.set_durability(item.get_durability());
                        carryItem.setChargeCount(item.getChargeCount());
                        carryItem.setRemainingTime(item.getRemainingTime());
                        carryItem.setLastUsed(item.getLastUsed());
                        carryItem.setBless(item.getBless());
                    }
                    l1ItemInstance = inventory.storeTradeItem(carryItem);
                }
            }
        }
        return l1ItemInstance;
    }

    public L1ItemInstance receiveDamage(int objectId) {
        return receiveDamage(getItem(objectId));
    }

    public L1ItemInstance receiveDamage(L1ItemInstance item) {
        return receiveDamage(item, 1);
    }

    public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
        if (item == null) {
            return null;
        }
        int itemType = item.getItem().getType2();
        int currentDurability = item.get_durability();
        if (!(currentDurability == 0 && itemType == 0) && currentDurability >= 0) {
            if (itemType == 0) {
                int minDurability = (item.getEnchantLevel() + 5) * -1;
                int durability = currentDurability - count;
                if (durability < minDurability) {
                    durability = minDurability;
                }
                if (currentDurability > durability) {
                    item.set_durability(durability);
                }
            } else {
                int maxDurability = item.getEnchantLevel() + 5;
                int durability2 = currentDurability + count;
                if (durability2 > maxDurability) {
                    durability2 = maxDurability;
                }
                if (currentDurability < durability2) {
                    item.set_durability(durability2);
                }
            }
            updateItem(item, 1);
            return item;
        }
        item.set_durability(0);
        return null;
    }

    public L1ItemInstance recoveryDamage(L1ItemInstance item) {
        if (item == null) {
            return null;
        }
        int itemType = item.getItem().getType2();
        int durability = item.get_durability();
        if ((durability != 0 || itemType == 0) && durability >= 0) {
            if (itemType == 0) {
                item.set_durability(durability + 1);
            } else {
                item.set_durability(durability - 1);
            }
            updateItem(item, 1);
            return item;
        }
        item.set_durability(0);
        return null;
    }

    public L1ItemInstance findItemIdNoEq(int itemId) {
        for (L1ItemInstance item : this._items) {
            if (item.getItem().getItemId() == itemId && !item.isEquipped() && item.get_time() == null) {
                return item;
            }
        }
        return null;
    }

    public L1ItemInstance findItemId(int itemId) {
        for (L1ItemInstance item : this._items) {
            if (item.getItem().getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    public L1ItemInstance findItemId(String nameid) {
        for (L1ItemInstance item : this._items) {
            if (item.getName().equals(nameid)) {
                return item;
            }
        }
        return null;
    }

    public L1ItemInstance[] findItemsId(int itemId) {
        ArrayList<L1ItemInstance> itemList = new ArrayList<>();
        for (L1ItemInstance item : this._items) {
            if (item.getItemId() == itemId && item.get_time() == null) {
                itemList.add(item);
            }
        }
        return (L1ItemInstance[]) itemList.toArray(new L1ItemInstance[0]);
    }

    public L1ItemInstance[] findItemsIdNotEquipped(int itemId) {
        ArrayList<L1ItemInstance> itemList = new ArrayList<>();
        for (L1ItemInstance item : this._items) {
            if (item.getItemId() == itemId && !item.isEquipped()) {
                itemList.add(item);
            }
        }
        return (L1ItemInstance[]) itemList.toArray(new L1ItemInstance[0]);
    }

    public L1ItemInstance[] findItemsIdNotEquipped(String nameid) {
        ArrayList<L1ItemInstance> itemList = new ArrayList<>();
        for (L1ItemInstance item : this._items) {
            if (item.getName().equals(nameid) && !item.isEquipped()) {
                itemList.add(item);
            }
        }
        return (L1ItemInstance[]) itemList.toArray(new L1ItemInstance[0]);
    }

    public L1ItemInstance getItem(int objectId) {
        for (L1ItemInstance item : this._items) {
            if (item.getId() == objectId) {
                return item;
            }
        }
        return null;
    }

    public boolean checkItem(int id) {
        return checkItem(id, serialVersionUID);
    }

    public boolean checkItem(int itemId, long count) {
        if (count <= 0) {
            return true;
        }
        if (ItemTable.get().getTemplate(itemId).isStackable()) {
            L1ItemInstance item = findItemId(itemId);
            if (item == null || item.getCount() < count) {
                return false;
            }
            return true;
        } else if (((long) findItemsId(itemId).length) >= count) {
            return true;
        }
        return false;
    }

    public boolean checkItem(L1ItemInstance item, long count) {
        if (count > 0 && item.getCount() < count) {
            return false;
        }
        return true;
    }

    public L1ItemInstance checkItemX(int itemid, long count) {
        L1ItemInstance item;
        if (count <= 0) {
            return null;
        }
        if (ItemTable.get().getTemplate(itemid) == null || (item = findItemIdNoEq(itemid)) == null || item.getCount() < count) {
            return null;
        }
        return item;
    }

    public L1ItemInstance checkItemXNoEq(int itemid, long count) {
        L1ItemInstance item;
        if (count <= 0) {
            return null;
        }
        if (ItemTable.get().getTemplate(itemid) == null || (item = findItemIdNoEq(itemid)) == null || item.getCount() < count) {
            return null;
        }
        return item;
    }

    public boolean checkEnchantItem(int id, int enchant, long count) {
        int num = 0;
        for (L1ItemInstance item : this._items) {
            if (!item.isEquipped() && item.getItemId() == id && item.getEnchantLevel() == enchant) {
                num++;
                if (((long) num) == count) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean consumeEnchantItem(int id, int enchant, long count) throws Exception {
        for (L1ItemInstance item : this._items) {
            if (!item.isEquipped() && item.getItemId() == id && item.getEnchantLevel() == enchant) {
                removeItem(item);
                return true;
            }
        }
        return false;
    }

    public boolean checkItemNotEquipped(String nameid, long count) {
        if (count != 0 && count > countItems(nameid)) {
            return false;
        }
        return true;
    }

    public boolean checkItemNotEquipped(int id, long count) {
        if (count != 0 && count > countItems(id)) {
            return false;
        }
        return true;
    }

    public boolean productionList(int id, int enchant, long count) {
        int num = 0;
        for (L1ItemInstance item : this._items) {
            if (!item.isEquipped() && item.getItemId() == id && item.getEnchantLevel() == enchant) {
                num = (int) (((long) num) + item.getCount());
                if (((long) num) >= count) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isProductionList(int itemid, long count, int en) throws Exception {
        if (count <= 0) {
            return false;
        }
        if (ItemTable.get().getTemplate(itemid).isStackable()) {
            L1ItemInstance item = findItemId(itemid);
            if (item != null && item.getCount() >= count && item.getEnchantLevel() == en) {
                removeItem(item, count);
                return true;
            }
        } else {
            L1ItemInstance[] itemList = findItemsId(itemid);
            if (((long) itemList.length) == count) {
                int j = 0;
                for (int i = 0; ((long) i) < count; i++) {
                    if (itemList[i].getEnchantLevel() == en) {
                        removeItem(itemList[i], serialVersionUID);
                        j++;
                        if (((long) j) == count) {
                            break;
                        }
                    }
                }
                return true;
            } else if (((long) itemList.length) > count) {
                extracted(itemList, new DataComparator());
                int j2 = 0;
                for (int i2 = 0; i2 < itemList.length; i2++) {
                    if (itemList[i2].getEnchantLevel() == en) {
                        removeItem(itemList[i2], serialVersionUID);
                        j2++;
                        if (((long) j2) == count) {
                            break;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean checkItem(int[] ids) {
        int len = ids.length;
        int[] counts = new int[len];
        for (int i = 0; i < len; i++) {
            counts[i] = 1;
        }
        return checkItem(ids, counts);
    }

    public boolean checkItem(int[] ids, int[] counts) {
        for (int i = 0; i < ids.length; i++) {
            if (!checkItem(ids[i], (long) counts[i])) {
                return false;
            }
        }
        return true;
    }

    public long countItems(int itemId) {
        if (!ItemTable.get().getTemplate(itemId).isStackable()) {
            return (long) findItemsIdNotEquipped(itemId).length;
        }
        L1ItemInstance item = findItemId(itemId);
        if (item != null) {
            return item.getCount();
        }
        return 0;
    }

    public long countItems(String nameid) {
        if (!ItemTable.get().getTemplate(nameid).isStackable()) {
            return (long) findItemsIdNotEquipped(nameid).length;
        }
        L1ItemInstance item = findItemId(nameid);
        if (item != null) {
            return item.getCount();
        }
        return 0;
    }

    public void shuffle() {
        Collections.shuffle(this._items);
    }

    public void clearItems() {
        for (L1ItemInstance item : this._items) {
            World.get().removeObject(item);
        }
    }

    public void loadItems() {
    }

    public void insertItem(L1ItemInstance item) {
    }

    public void updateItem(L1ItemInstance item) throws Exception {
    }

    public void updateItem(L1ItemInstance item, int colmn) {
    }

    private void extracted(L1ItemInstance[] itemList, DataComparator dc) {
        Arrays.sort(itemList, dc);
    }

    public boolean consumeItemsIdArray(ArrayList<Integer> itemsidarray) throws Exception {
        int[] itemsid = new int[itemsidarray.size()];
        long[] counts = new long[itemsidarray.size()];
        for (int i = 0; i < itemsidarray.size(); i++) {
            itemsid[i] = itemsidarray.get(i).intValue();
            counts[i] = 1;
        }
        return consumeItemsId(itemsid, counts);
    }

    public boolean consumeItemsId(int[] itemsid, long[] counts) throws Exception {
        if (itemsid.length != counts.length) {
            return false;
        }
        int removecount = 0;
        for (int i = 0; i < itemsid.length; i++) {
            if (ItemTable.get().getTemplate(itemsid[i]).isStackable()) {
                L1ItemInstance item = findItemId(itemsid[i]);
                if (item != null && item.getCount() >= counts[i]) {
                    removeItem(item, counts[i]);
                    removecount++;
                }
                if (removecount == itemsid.length) {
                    return true;
                }
            } else {
                L1ItemInstance[] itemList = findItemsId(itemsid[i]);
                if (((long) itemList.length) == counts[i]) {
                    for (int c = 0; ((long) c) < counts[i]; c++) {
                        removeItem(itemList[c], serialVersionUID);
                    }
                    removecount++;
                    if (removecount == itemsid.length) {
                        return true;
                    }
                }
                if (((long) itemList.length) > counts[i]) {
                    Arrays.sort(itemList, new DataComparator());
                    for (int c2 = 0; ((long) c2) < counts[i]; c2++) {
                        removeItem(itemList[c2], serialVersionUID);
                    }
                    removecount++;
                    if (removecount == itemsid.length) {
                        return true;
                    }
                } else {
                    continue;
                }
            }
        }
        return false;
    }

    public boolean consumeItemsId(int[] itemsid) throws Exception {
        long[] counts = new long[itemsid.length];
        for (int i = 0; i < itemsid.length; i++) {
            counts[i] = 1;
        }
        return consumeItemsId(itemsid, counts);
    }
}
