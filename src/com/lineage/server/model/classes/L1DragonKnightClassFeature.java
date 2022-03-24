package com.lineage.server.model.classes;

/* access modifiers changed from: package-private */
public class L1DragonKnightClassFeature extends L1ClassFeature {
    L1DragonKnightClassFeature() {
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAcDefenseMax(int ac) {
        return ac / 3;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getMagicLevel(int playerLevel) {
        return Math.min(4, playerLevel / 9);
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getAttackLevel(int playerLevel) {
        return playerLevel / 13;
    }

    @Override // com.lineage.server.model.classes.L1ClassFeature
    public int getHitLevel(int playerLevel) {
        return playerLevel / 25;
    }
}
