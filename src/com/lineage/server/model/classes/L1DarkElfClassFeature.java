package com.lineage.server.model.classes;

/* access modifiers changed from: package-private */
public class L1DarkElfClassFeature extends L1ClassFeature {
    L1DarkElfClassFeature() {
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAcDefenseMax(int ac) {
        return ac >> 2;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getMagicLevel(int playerLevel) {
        return Math.min(2, playerLevel / 12);
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 16;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getHitLevel(int playerLevel) {
        return playerLevel / 30;
    }
}
