package com.lineage.server.model.doll;

import com.lineage.server.model.Instance.L1PcInstance;

public abstract class L1DollExecutor {
    public abstract String get_note();

    public abstract boolean is_reset();

    public abstract void removeDoll(L1PcInstance l1PcInstance);

    public abstract void setDoll(L1PcInstance l1PcInstance);

    public abstract void set_note(String str);

    public abstract void set_power(int i, int i2, int i3);
}
