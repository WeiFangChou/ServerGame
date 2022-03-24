package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

public interface ArmorSetEffect {
    void cancelEffect(L1PcInstance l1PcInstance);

    int get_mode();

    void giveEffect(L1PcInstance l1PcInstance);
}
