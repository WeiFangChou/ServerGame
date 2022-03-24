package com.lineage.server.model;

import com.lineage.config.ConfigRate;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.datatables.BlessDystem;
import com.lineage.server.datatables.BlessSystem;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_AddItem;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_DeleteInventoryItem;
import com.lineage.server.serverpackets.S_ItemColor;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BlessDystem;
import com.lineage.server.templates.L1BlessSystem;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1SuperCard;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PcInventory extends L1Inventory {
    public static final int COL_ATTR_ENCHANT_KIND = 1024;
    public static final int COL_ATTR_ENCHANT_LEVEL = 2048;
    public static final int COL_BLESS = 512;
    public static final int COL_CHARGE_COUNT = 128;
    public static final int COL_COUNT = 16;
    public static final int COL_DELAY_EFFECT = 32;
    public static final int COL_DURABILITY = 1;
    public static final int COL_ENCHANTLVL = 4;
    public static final int COL_EQUIPPED = 8;
    public static final int COL_IS_ID = 2;
    public static final int COL_ITEMID = 64;
    public static final int COL_REMAINING_TIME = 256;
    private static final int MAX_SIZE = 180;
    private static final Log _log = LogFactory.getLog(L1PcInventory.class);
    private static final long serialVersionUID = 1;
    private int _arrowId = 0;
    private final L1PcInstance _owner;
    private int _stingId = 0;

    public L1PcInventory(L1PcInstance owner) {
        this._owner = owner;
    }

    public L1PcInstance getOwner() {
        return this._owner;
    }

    public int getWeight240() {
        return calcWeight240((long) getWeight());
    }

    public int calcWeight240(long weight) {
        if (ConfigRate.RATE_WEIGHT_LIMIT == 0.0d) {
            return 0;
        }
        double maxWeight = this._owner.getMaxWeight();
        if (((double) weight) > maxWeight) {
            return 240;
        }
        double wpTemp = ((((double) (100 * weight)) / maxWeight) * 240.0d) / 100.0d;
        new DecimalFormat("00.##").format(wpTemp);
        return (int) ((double) Math.round(wpTemp));
    }

    @Override // com.lineage.server.model.L1Inventory
    public int checkAddItem(L1ItemInstance item, long count) {
        return checkAddItem(item, count, true);
    }

    public int checkAddItem(L1Item item, long count) {
        if (item == null) {
            return -1;
        }
        boolean isMaxSize = false;
        boolean isWeightOver = false;
        if (item.isStackable()) {
            if (!checkItem(item.getItemId()) && getSize() + 1 >= 180) {
                isMaxSize = true;
            }
        } else if (getSize() + 1 >= 180) {
            isMaxSize = true;
        }
        if (isMaxSize) {
            sendOverMessage(263);
            return 1;
        }
        long weight = ((long) getWeight()) + ((((long) item.getWeight()) * count) / 1000) + serialVersionUID;
        if (weight < 0 || (((long) item.getWeight()) * count) / 1000 < 0) {
            isWeightOver = true;
        }
        if (calcWeight240(weight) >= 240 && !isWeightOver) {
            isWeightOver = true;
        }
        if (!isWeightOver) {
            return 0;
        }
        sendOverMessage(82);
        return 2;
    }

    public int checkAddItem(L1ItemInstance item, long count, boolean message) {
        if (item == null || count <= 0) {
            return -1;
        }
        boolean isMaxSize = false;
        boolean isWeightOver = false;
        if (item.isStackable()) {
            if (!checkItem(item.getItem().getItemId()) && getSize() + 1 >= 180) {
                isMaxSize = true;
            }
        } else if (getSize() + 1 >= 180) {
            isMaxSize = true;
        }
        if (isMaxSize) {
            if (message) {
                sendOverMessage(263);
            }
            return 1;
        }
        long weight = ((long) getWeight()) + ((((long) item.getItem().getWeight()) * count) / 1000) + serialVersionUID;
        if (weight < 0 || (((long) item.getItem().getWeight()) * count) / 1000 < 0) {
            isWeightOver = true;
        }
        if (calcWeight240(weight) >= 240 && !isWeightOver) {
            isWeightOver = true;
        }
        if (!isWeightOver) {
            return 0;
        }
        if (message) {
            sendOverMessage(82);
        }
        return 2;
    }

    public void sendOverMessage(int message_id) {
        this._owner.sendPackets(new S_ServerMessage(message_id));
    }

    @Override // com.lineage.server.model.L1Inventory
    public void loadItems() {
        try {
            CopyOnWriteArrayList<L1ItemInstance> items = CharItemsReading.get().loadItems(Integer.valueOf(this._owner.getId()));
            if (items != null) {
                this._items = items;
                List<L1ItemInstance> equipped = new CopyOnWriteArrayList<>();
                Iterator<L1ItemInstance> it = items.iterator();
                while (it.hasNext()) {
                    L1ItemInstance item = it.next();
                    if (item.isEquipped()) {
                        equipped.add(item);
                    }
                    item.setEquipped(false);
                    if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
                        item.setRemainingTime(item.getItem().getLightFuel());
                    }
                }
                for (L1ItemInstance item2 : equipped) {
                    setEquipped(item2, true, true, false);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void insertItem(L1ItemInstance item) {
        if (item.getCount() > 0) {
            item.set_char_objid(this._owner.getId());
            this._owner.sendPackets(new S_AddItem(item));
            if (item.getItem().getWeight() != 0) {
                this._owner.sendPackets(new S_PacketBox(10, getWeight240()));
            }
            try {
                CharItemsReading.get().storeItem(this._owner.getId(), item);
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void updateItem(L1ItemInstance item) throws Exception {
        updateItem(item, 16);
        if (item.getItem().isToBeSavedAtOnce()) {
            saveItem(item, 16);
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void updateItem(L1ItemInstance item, int column) {
        if (column >= 2048) {
            this._owner.sendPackets(new S_ItemStatus(item));
            column -= 2048;
        }
        if (column >= 1024) {
            this._owner.sendPackets(new S_ItemStatus(item));
            column -= 1024;
        }
        if (column >= 512) {
            this._owner.sendPackets(new S_ItemColor(item));
            column -= 512;
        }
        if (column >= 256) {
            this._owner.sendPackets(new S_ItemName(item));
            column -= 256;
        }
        if (column >= 128) {
            this._owner.sendPackets(new S_ItemName(item));
            column -= 128;
        }
        if (column >= 64) {
            this._owner.sendPackets(new S_ItemStatus(item));
            this._owner.sendPackets(new S_ItemColor(item));
            this._owner.sendPackets(new S_PacketBox(10, getWeight240()));
            column -= 64;
        }
        if (column >= 32) {
            column -= 32;
        }
        if (column >= 16) {
            this._owner.sendPackets(new S_ItemStatus(item));
            int weight = item.getWeight();
            if (weight != item.getLastWeight()) {
                item.setLastWeight(weight);
                this._owner.sendPackets(new S_ItemStatus(item));
            } else {
                this._owner.sendPackets(new S_ItemName(item));
            }
            if (item.getItem().getWeight() != 0) {
                this._owner.sendPackets(new S_PacketBox(10, getWeight240()));
            }
            column -= 16;
        }
        if (column >= 8) {
            this._owner.sendPackets(new S_ItemName(item));
            column -= 8;
        }
        if (column >= 4) {
            this._owner.sendPackets(new S_ItemStatus(item));
            column -= 4;
        }
        if (column >= 2) {
            this._owner.sendPackets(new S_ItemStatus(item));
            this._owner.sendPackets(new S_ItemColor(item));
            column -= 2;
        }
        if (column >= 1) {
            this._owner.sendPackets(new S_ItemStatus(item));
            int column2 = column - 1;
        }
    }

    public void saveItem(L1ItemInstance item, int column) throws Exception {
        if (column != 0) {
            if (column >= 2048) {
                try {
                    CharItemsReading.get().updateItemAttrEnchantLevel(item);
                    column -= 2048;
                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
            if (column >= 1024) {
                CharItemsReading.get().updateItemAttrEnchantKind(item);
                column -= 1024;
            }
            if (column >= 512) {
                CharItemsReading.get().updateItemBless(item);
                column -= 512;
            }
            if (column >= 256) {
                CharItemsReading.get().updateItemRemainingTime(item);
                column -= 256;
            }
            if (column >= 128) {
                CharItemsReading.get().updateItemChargeCount(item);
                column -= 128;
            }
            if (column >= 64) {
                CharItemsReading.get().updateItemId(item);
                column -= 64;
            }
            if (column >= 32) {
                CharItemsReading.get().updateItemDelayEffect(item);
                column -= 32;
            }
            if (column >= 16) {
                CharItemsReading.get().updateItemCount(item);
                column -= 16;
            }
            if (column >= 8) {
                CharItemsReading.get().updateItemEquipped(item);
                column -= 8;
            }
            if (column >= 4) {
                CharItemsReading.get().updateItemEnchantLevel(item);
                column -= 4;
            }
            if (column >= 2) {
                CharItemsReading.get().updateItemIdentified(item);
                column -= 2;
            }
            if (column >= 1) {
                CharItemsReading.get().updateItemDurability(item);
                int column2 = column - 1;
            }
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void deleteItem(L1ItemInstance item) {
        try {
            CharItemsReading.get().deleteItem(this._owner.getId(), item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        if (item.isEquipped()) {
            setEquipped(item, false);
        }
        if (item != null) {
            this._owner.sendPackets(new S_DeleteInventoryItem(item));
            this._items.remove(item);
            if (item.getItem().getWeight() != 0) {
                this._owner.sendPackets(new S_PacketBox(10, getWeight240()));
            }
        }
    }

    public void setEquipped(L1ItemInstance item, boolean equipped) {
        setEquipped(item, equipped, false, false);
    }

    public void setEquipped(L1ItemInstance item, boolean equipped, boolean loaded, boolean changeWeapon) {
        if (item.isEquipped() != equipped) {
            if (equipped) {
                item.setEquipped(true);
                this._owner.getEquipSlot().set(item);
            } else if (loaded || (!(item.getItemId() == 20077 || item.getItemId() == 20062 || item.getItemId() == 120077) || !this._owner.isInvisble())) {
                item.setEquipped(false);
                this._owner.getEquipSlot().remove(item);
            } else {
                this._owner.delInvis();
                return;
            }
            if (!loaded) {
                this._owner.getInventory().toSlotPacket(this._owner, item);
                updateItem(item, 8);
                this._owner.setCurrentHp(this._owner.getCurrentHp());
                this._owner.setCurrentMp(this._owner.getCurrentMp());
                this._owner.sendPackets(new S_SPMR(this._owner));
                this._owner.sendPackets(new S_OwnCharStatus(this._owner));
                if (item.getItem().getType2() == 1 && !changeWeapon) {
                    this._owner.sendPacketsAll(new S_CharVisualUpdate(this._owner));
                }
            }
        }
    }

    public final int getEquippedCountByItemId(int id) {
        int equippedCount = 0;
        for (L1ItemInstance item : this._items) {
            if (item.isEquipped() && item.getItem().getItemId() == id) {
                equippedCount++;
            }
        }
        return equippedCount;
    }

    public final int getEquippedCountByActivityItem() {
        int equippedCount = 0;
        for (L1ItemInstance item : this._items) {
            if (item.isEquipped() && item.getItem().isActivity()) {
                equippedCount++;
            }
        }
        return equippedCount;
    }

    public L1ItemInstance checkEquippedItem(int id) {
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getItem().getItemId() == id && item.isEquipped()) {
                    return item;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    public boolean checkEquipped(int id) {
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getItem().getItemId() == id && item.isEquipped()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    public boolean checkEquipped(String nameid) {
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getName().equals(nameid) && item.isEquipped()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return false;
    }

    public boolean checkEquipped(int[] ids) {
        try {
            for (int id : ids) {
                if (!checkEquipped(id)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return true;
    }

    public boolean checkEquipped(String[] names) {
        try {
            for (String name : names) {
                if (!checkEquipped(name)) {
                    return false;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return true;
    }

    public int getTypeEquipped(int type2, int type) {
        int equipeCount = 0;
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
                    equipeCount++;
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeCount;
    }

    public L1ItemInstance getItemEquipped(int type2, int type) {
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getItem().getType2() == type2 && item.getItem().getType() == type && item.isEquipped()) {
                    return item;
                }
            }
            return null;
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
            return null;
        }
    }

    public void setPartMode(ArmorSet armorSet, boolean isMode) {
        L1ItemInstance[] tgItems = findItemsId(armorSet.get_ids()[0]);
        for (L1ItemInstance tgItem : tgItems) {
            tgItem.setIsMatch(isMode);
            this._owner.sendPackets(new S_ItemStatus(tgItem));
        }
    }

    public L1ItemInstance[] getRingEquipped() {
        L1ItemInstance[] equipeItem = new L1ItemInstance[2];
        int equipeCount = 0;
        try {
            for (L1ItemInstance item : this._items) {
                if (item.getItem().getUseType() == 23 && item.isEquipped()) {
                    equipeItem[equipeCount] = item;
                    equipeCount++;
                    if (equipeCount == 2) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            _log.error(ex.getLocalizedMessage(), ex);
        }
        return equipeItem;
    }

    public void takeoffEquip(int polyid) {
        takeoffWeapon(polyid);
        takeoffArmor(polyid);
    }

    private void takeoffWeapon(int polyid) {
        if (this._owner.getWeapon() != null) {
            if (!L1PolyMorph.isEquipableWeapon(polyid, this._owner.getWeapon().getItem().getType())) {
                setEquipped(this._owner.getWeapon(), false, false, false);
            }
        }
    }

    private void takeoffArmor(int polyid) {
        for (int type = 0; type <= 13; type++) {
            if (getTypeEquipped(2, type) != 0 && !L1PolyMorph.isEquipableArmor(polyid, type)) {
                if (type == 9) {
                    L1ItemInstance armor = getItemEquipped(2, type);
                    if (armor != null) {
                        setEquipped(armor, false, false, false);
                    }
                    L1ItemInstance armor2 = getItemEquipped(2, type);
                    if (armor2 != null) {
                        setEquipped(armor2, false, false, false);
                    }
                } else {
                    L1ItemInstance armor3 = getItemEquipped(2, type);
                    if (armor3 != null) {
                        setEquipped(armor3, false, false, false);
                    }
                }
            }
        }
    }

    public L1ItemInstance getArrow() {
        return getBullet(-2);
    }

    public L1ItemInstance getSting() {
        return getBullet(-3);
    }

    private L1ItemInstance getBullet(int useType) {
        int priorityId = 0;
        if (useType == -2) {
            if (this._owner.getWeapon().getItemId() == 192) {
                L1ItemInstance bullet = findItemId(40742);
                if (bullet != null) {
                    return bullet;
                }
                this._owner.sendPackets(new S_ServerMessage(329, "$2377"));
                return bullet;
            }
            priorityId = this._arrowId;
        }
        if (useType == -3) {
            priorityId = this._stingId;
        }
        if (priorityId > 0) {
            L1ItemInstance bullet2 = findItemId(priorityId);
            if (bullet2 != null) {
                return bullet2;
            }
            if (useType == -2) {
                this._arrowId = 0;
            }
            if (useType == -3) {
                this._stingId = 0;
            }
        }
        for (L1ItemInstance bullet3 : this._items) {
            if (bullet3.getItem().getUseType() == useType) {
                if (useType == -2) {
                    this._arrowId = bullet3.getItem().getItemId();
                }
                if (useType != -3) {
                    return bullet3;
                }
                this._stingId = bullet3.getItem().getItemId();
                return bullet3;
            }
        }
        return null;
    }

    public void setArrow(int id) {
        this._arrowId = id;
    }

    public void setSting(int id) {
        this._stingId = id;
    }

    public int hpRegenPerTick() {
        int hpr = 0;
        try {
            for (L1ItemInstance item : this._items) {
                if (item.isEquipped()) {
                    int cardHpr = 0;
                    L1SuperCard card = item.getItem().getCard();
                    if (card != null) {
                        card.getExtraHpr();
                        cardHpr = card.getWakeHpr();
                    }
                    L1BlessSystem Bless = BlessSystem.get(item.getItemId());
                    if (Bless != null) {
                        cardHpr += Bless.getHpr();
                    }
                    L1BlessDystem Bl_ess = BlessDystem.get(item.getItemId());
                    if (Bl_ess != null) {
                        cardHpr += Bl_ess.getHpr();
                    }
                    hpr += item.getItem().get_addhpr() + cardHpr;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return hpr;
    }

    public int mpRegenPerTick() {
        int mpr = 0;
        try {
            for (L1ItemInstance item : this._items) {
                if (item.isEquipped()) {
                    int cardMpr = 0;
                    L1SuperCard card = item.getItem().getCard();
                    if (card != null) {
                        card.getExtraMpr();
                        cardMpr = card.getWakeMpr();
                    }
                    L1BlessSystem Bless = BlessSystem.get(item.getItemId());
                    if (Bless != null) {
                        cardMpr += Bless.getMpr();
                    }
                    L1BlessDystem Bl_ess = BlessDystem.get(item.getItemId());
                    if (Bl_ess != null) {
                        cardMpr += Bl_ess.getMpr();
                    }
                    mpr += item.getItem().get_addmpr() + cardMpr;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return mpr;
    }

    public L1ItemInstance caoPenalty() {
        try {
            L1ItemInstance penaltyItem = (L1ItemInstance) this._items.get(new Random().nextInt(this._items.size()));
            if (penaltyItem.getItem().getItemId() == 44070) {
                return null;
            }
            if (penaltyItem.getItem().getItemId() == 40308) {
                return null;
            }
            if (penaltyItem.getItem().isCantDelete()) {
                return null;
            }
            if (!penaltyItem.getItem().isTradable()) {
                return null;
            }
            if (penaltyItem.get_time() != null) {
                return null;
            }
            Object[] petlist = this._owner.getPetList().values().toArray();
            for (Object petObject : petlist) {
                if ((petObject instanceof L1PetInstance) && penaltyItem.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                    return null;
                }
            }
            if (this._owner.getDoll(penaltyItem.getId()) != null) {
                return null;
            }
            setEquipped(penaltyItem, false);
            return penaltyItem;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public boolean checkNPItem(int itemid, int enchant) {
        for (L1ItemInstance item : this._items) {
            if (item != null && item.getItem().getItemId() == itemid && !item.isEquipped() && item.getEnchantLevel() == enchant && item.getCount() >= serialVersionUID) {
                return true;
            }
        }
        return false;
    }

    public boolean consumeNPItem(int itemid, int enchant) throws Exception {
        for (L1ItemInstance item : this._items) {
            if (item != null && item.getItem().getItemId() == itemid && !item.isEquipped() && item.getEnchantLevel() == enchant && item.getCount() >= serialVersionUID) {
                removeItem(item, serialVersionUID);
                return true;
            }
        }
        return false;
    }

    public boolean checkNPitem_id(int itemid) {
        for (L1ItemInstance item : this._items) {
            if (item != null && item.getItem().getItemId() == itemid && !item.isEquipped() && item.getCount() >= serialVersionUID) {
                return true;
            }
        }
        return false;
    }

    public boolean consumeNPitem_id(int itemid) throws Exception {
        for (L1ItemInstance item : this._items) {
            if (item != null && item.getItem().getItemId() == itemid && !item.isEquipped() && item.getCount() >= serialVersionUID) {
                removeItem(item, serialVersionUID);
                return true;
            }
        }
        return false;
    }

    public void delQuestItem(int itemId) {
        try {
            Random random = new Random();
            for (L1ItemInstance item : this._items) {
                if (item.getItemId() == itemId) {
                    removeItem(item);
                    this._owner.sendPackets(new S_ServerMessage(random.nextInt(4) + 445, item.getName()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1ItemInstance findEquippedSuperCardByType(int type) {
        L1Item tmp;
        L1SuperCard card;
        for (L1ItemInstance item : this._items) {
            if (!(!item.isEquipped() || (tmp = item.getItem()) == null || (card = tmp.getCard()) == null || card.getCardType() == 0 || card.getCardType() != type)) {
                return item;
            }
        }
        return null;
    }
}
