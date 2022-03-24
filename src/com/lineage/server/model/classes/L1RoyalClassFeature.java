package com.lineage.server.model.classes;

/* access modifiers changed from: package-private */
public class L1RoyalClassFeature extends L1ClassFeature {
    L1RoyalClassFeature() {
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAcDefenseMax(int ac) {
        return ac / 3;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getMagicLevel(int playerLevel) {
        return Math.min(2, playerLevel / 10);
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 10;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getHitLevel(int playerLevel) {
        return playerLevel / 15;
    }
}
