package com.lineage.server.model;

import com.lineage.server.datatables.lock.DwarfForClanReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.world.World;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DwarfForClanInventory extends L1Inventory {
    public static final Log _log = LogFactory.getLog(L1DwarfForClanInventory.class);
    private static final long serialVersionUID = 1;
    private final L1Clan _clan;

    public L1DwarfForClanInventory(L1Clan clan) {
        this._clan = clan;
    }

    @Override // com.lineage.server.model.L1Inventory
    public synchronized void loadItems() {
        try {
            CopyOnWriteArrayList<L1ItemInstance> items = DwarfForClanReading.get().loadItems(this._clan.getClanName());
            if (items != null) {
                this._items = items;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return;
    }

    @Override // com.lineage.server.model.L1Inventory
    public synchronized void insertItem(L1ItemInstance item) {
        try {
            DwarfForClanReading.get().insertItem(this._clan.getClanName(), item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return;
    }

    @Override // com.lineage.server.model.L1Inventory
    public synchronized void updateItem(L1ItemInstance item) {
        try {
            DwarfForClanReading.get().updateItem(item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return;
    }

    @Override // com.lineage.server.model.L1Inventory
    public synchronized void deleteItem(L1ItemInstance item) {
        try {
            this._items.remove(item);
            DwarfForClanReading.get().deleteItem(this._clan.getClanName(), item);
            World.get().removeObject(item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return;
    }

    public synchronized void deleteAllItems() {
        try {
            DwarfForClanReading.get().delUserItems(this._clan.getClanName());
            this._items.clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return;
    }
}
