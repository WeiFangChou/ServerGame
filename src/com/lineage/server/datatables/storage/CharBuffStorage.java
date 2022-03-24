package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

public interface CharBuffStorage {
    void buff(L1PcInstance l1PcInstance);

    void deleteBuff(int i);

    void deleteBuff(L1PcInstance l1PcInstance);

    void load();

    void saveBuff(L1PcInstance l1PcInstance);
}
