package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;

public interface LogChatStorage {
    void isTarget(L1PcInstance l1PcInstance, L1PcInstance l1PcInstance2, String str, int i);

    void noTarget(L1PcInstance l1PcInstance, String str, int i);
}
