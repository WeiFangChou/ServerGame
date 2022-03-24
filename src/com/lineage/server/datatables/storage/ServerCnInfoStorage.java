package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;

public interface ServerCnInfoStorage {
    void create(L1PcInstance l1PcInstance, L1Item l1Item, int i);
}
