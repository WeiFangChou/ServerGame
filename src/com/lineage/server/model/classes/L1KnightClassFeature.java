package com.lineage.server.model.classes;

/* access modifiers changed from: package-private */
public class L1KnightClassFeature extends L1ClassFeature {
    L1KnightClassFeature() {
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAcDefenseMax(int ac) {
        return ac >> 1;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getMagicLevel(int playerLevel) {
        return playerLevel / 50;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 6;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getHitLevel(int playerLevel) {
        return playerLevel / 10;
    }
}
