package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;

public interface CharShiftingStorage {
    void newShifting(L1PcInstance l1PcInstance, int i, String str, int i2, L1Item l1Item, L1ItemInstance l1ItemInstance, int i3);
}
