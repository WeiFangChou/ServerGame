package com.lineage.data.item_armor.set;

import com.lineage.server.model.Instance.L1PcInstance;

public class EffectMr implements ArmorSetEffect {
    private final int _add;

    public EffectMr(int add) {
        this._add = add;
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public void giveEffect(L1PcInstance pc) {
        pc.addMr(this._add);
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public void cancelEffect(L1PcInstance pc) {
        pc.addMr(-this._add);
    }

    @Override // com.lineage.data.item_armor.set.ArmorSetEffect
    public int get_mode() {
        return this._add;
    }
}
