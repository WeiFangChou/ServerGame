package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

public interface ServerGmCommandStorage {
    void create(L1PcInstance l1PcInstance, String str);
}
