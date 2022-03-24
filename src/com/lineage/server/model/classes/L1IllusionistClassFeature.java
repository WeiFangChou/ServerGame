package com.lineage.server.model.classes;

/* access modifiers changed from: package-private */
public class L1IllusionistClassFeature extends L1ClassFeature {
    L1IllusionistClassFeature() {
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAcDefenseMax(int ac) {
        return ac >> 2;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getMagicLevel(int playerLevel) {
        return Math.min(6, playerLevel >> 3);
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 16;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getHitLevel(int playerLevel) {
        return playerLevel / 40;
    }
}
