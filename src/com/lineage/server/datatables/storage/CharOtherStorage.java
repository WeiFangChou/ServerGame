package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;

public interface CharOtherStorage {
    L1PcOther getOther(L1PcInstance l1PcInstance);

    void load();

    void storeOther(int i, L1PcOther l1PcOther);

    void tam();
}
