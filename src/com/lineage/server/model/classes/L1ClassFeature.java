package com.lineage.server.model.classes;

import com.lineage.server.model.Instance.L1PcInstance;

public abstract class L1ClassFeature {
    public abstract int getAcDefenseMax(int i);

    public abstract int getAttackLevel(int i);

    public abstract int getHitLevel(int i);

    public abstract int getMagicLevel(int i);

    public static L1ClassFeature newClassFeature(int classId) {
        switch (classId) {
            case 0:
            case 1:
                return new L1RoyalClassFeature();
            case 37:
            case 138:
                return new L1ElfClassFeature();
            case 48:
            case 61:
                return new L1KnightClassFeature();
            case L1PcInstance.CLASSID_WIZARD_MALE:
            case L1PcInstance.CLASSID_WIZARD_FEMALE:
                return new L1WizardClassFeature();
            case L1PcInstance.CLASSID_DARK_ELF_MALE:
            case L1PcInstance.CLASSID_DARK_ELF_FEMALE:
                return new L1DarkElfClassFeature();
            case L1PcInstance.CLASSID_ILLUSIONIST_FEMALE:
            case 6671:
                return new L1IllusionistClassFeature();
            case L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE:
            case L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE:
                return new L1DragonKnightClassFeature();
            default:
                throw new IllegalArgumentException();
        }
    }
}
