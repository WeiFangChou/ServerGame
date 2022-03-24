package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public interface LogEnchantStorage {
    void failureEnchant(L1PcInstance l1PcInstance, L1ItemInstance l1ItemInstance);
}
