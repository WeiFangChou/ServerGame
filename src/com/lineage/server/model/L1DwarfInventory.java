package com.lineage.server.model;

import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DwarfInventory extends L1Inventory {
    public static final Log _log = LogFactory.getLog(L1DwarfInventory.class);
    private static final long serialVersionUID = 1;
    private final L1PcInstance _owner;

    public L1DwarfInventory(L1PcInstance owner) {
        this._owner = owner;
    }

    @Override // com.lineage.server.model.L1Inventory
    public void loadItems() {
        try {
            CopyOnWriteArrayList<L1ItemInstance> items = DwarfReading.get().loadItems(this._owner.getAccountName());
            if (items != null) {
                this._items = items;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void insertItem(L1ItemInstance item) {
        if (item.getCount() > 0) {
            try {
                DwarfReading.get().insertItem(this._owner.getAccountName(), item);
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void updateItem(L1ItemInstance item) {
        try {
            DwarfReading.get().updateItem(item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Inventory
    public void deleteItem(L1ItemInstance item) {
        try {
            this._items.remove(item);
            DwarfReading.get().deleteItem(this._owner.getAccountName(), item);
            World.get().removeObject(item);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
