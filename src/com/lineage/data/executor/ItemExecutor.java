package com.lineage.data.executor;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;

public abstract class ItemExecutor {
    public abstract void execute(int[] iArr, L1PcInstance l1PcInstance, L1ItemInstance l1ItemInstance) throws Exception;

    public String[] get_set() {
        return null;
    }

    public void set_set(String[] set) {
    }
}
