package com.lineage.list;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import java.util.ArrayList;

public class PcLvSkillList {
    public static ArrayList<Integer> scount(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        switch (pc.getType()) {
            case 0:
                if (pc.getLevel() >= 10) {
                    for (int skillid = 0; skillid < 8; skillid++) {
                        skillList.add(new Integer(skillid));
                    }
                }
                if (pc.getLevel() >= 20) {
                    for (int skillid2 = 8; skillid2 < 16; skillid2++) {
                        skillList.add(new Integer(skillid2));
                    }
                    break;
                }
                break;
            case 1:
                if (pc.getLevel() >= 50) {
                    for (int skillid3 = 0; skillid3 < 8; skillid3++) {
                        skillList.add(new Integer(skillid3));
                    }
                    break;
                }
                break;
            case 2:
                if (pc.getLevel() >= 8) {
                    for (int skillid4 = 0; skillid4 < 8; skillid4++) {
                        skillList.add(new Integer(skillid4));
                    }
                }
                if (pc.getLevel() >= 16) {
                    for (int skillid5 = 8; skillid5 < 16; skillid5++) {
                        skillList.add(new Integer(skillid5));
                    }
                }
                if (pc.getLevel() >= 24) {
                    for (int skillid6 = 16; skillid6 < 23; skillid6++) {
                        skillList.add(new Integer(skillid6));
                    }
                    break;
                }
                break;
            case 3:
                if (pc.getLevel() >= 4) {
                    for (int skillid7 = 0; skillid7 < 8; skillid7++) {
                        skillList.add(new Integer(skillid7));
                    }
                }
                if (pc.getLevel() >= 8) {
                    for (int skillid8 = 8; skillid8 < 16; skillid8++) {
                        skillList.add(new Integer(skillid8));
                    }
                }
                if (pc.getLevel() >= 12) {
                    for (int skillid9 = 16; skillid9 < 23; skillid9++) {
                        skillList.add(new Integer(skillid9));
                    }
                    break;
                }
                break;
            case 4:
                if (pc.getLevel() >= 12) {
                    for (int skillid10 = 0; skillid10 < 8; skillid10++) {
                        skillList.add(new Integer(skillid10));
                    }
                }
                if (pc.getLevel() >= 24) {
                    for (int skillid11 = 8; skillid11 < 16; skillid11++) {
                        skillList.add(new Integer(skillid11));
                    }
                    break;
                }
                break;
        }
        return skillList;
    }

    public static ArrayList<Integer> isIllusionist(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 10) {
            for (int skillid = 200; skillid < 208; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 20) {
            for (int skillid2 = 208; skillid2 < 212; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 30) {
            for (int skillid3 = 212; skillid3 < 216; skillid3++) {
                skillList.add(new Integer(skillid3));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid4 = 216; skillid4 < 220; skillid4++) {
                skillList.add(new Integer(skillid4));
            }
        }
        return skillList;
    }

    public static ArrayList<Integer> isDragonKnight(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 15) {
            for (int skillid = OpcodesServer.S_OPCODE_INVLIST; skillid < 184; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 30) {
            for (int skillid2 = 184; skillid2 < 192; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 45) {
            for (int skillid3 = 192; skillid3 < 195; skillid3++) {
                skillList.add(new Integer(skillid3));
            }
        }
        return skillList;
    }

    public static ArrayList<Integer> isDarkelf(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 12) {
            for (int skillid = 0; skillid < 8; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid2 = 8; skillid2 < 16; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 15) {
            for (int skillid3 = 96; skillid3 < 100; skillid3++) {
                skillList.add(new Integer(skillid3));
            }
            skillList.add(new Integer((int) L1SkillId.FINAL_BURN));
        }
        if (pc.getLevel() >= 30) {
            for (int skillid4 = 100; skillid4 < 104; skillid4++) {
                skillList.add(new Integer(skillid4));
            }
            skillList.add(new Integer(109));
        }
        if (pc.getLevel() >= 45) {
            for (int skillid5 = 104; skillid5 < 108; skillid5++) {
                skillList.add(new Integer(skillid5));
            }
            skillList.add(new Integer(110));
        }
        return skillList;
    }

    public static ArrayList<Integer> isWizard(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 4) {
            for (int skillid = 0; skillid < 8; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 8) {
            for (int skillid2 = 8; skillid2 < 16; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 12) {
            for (int skillid3 = 16; skillid3 < 23; skillid3++) {
                skillList.add(new Integer(skillid3));
            }
        }
        if (pc.getLevel() >= 16) {
            for (int skillid4 = 24; skillid4 < 32; skillid4++) {
                skillList.add(new Integer(skillid4));
            }
        }
        if (pc.getLevel() >= 20) {
            for (int skillid5 = 32; skillid5 < 40; skillid5++) {
                skillList.add(new Integer(skillid5));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid6 = 40; skillid6 < 48; skillid6++) {
                skillList.add(new Integer(skillid6));
            }
        }
        if (pc.getLevel() >= 28) {
            for (int skillid7 = 48; skillid7 < 56; skillid7++) {
                skillList.add(new Integer(skillid7));
            }
        }
        if (pc.getLevel() >= 32) {
            for (int skillid8 = 56; skillid8 < 64; skillid8++) {
                skillList.add(new Integer(skillid8));
            }
        }
        if (pc.getLevel() >= 36) {
            for (int skillid9 = 64; skillid9 < 72; skillid9++) {
                skillList.add(new Integer(skillid9));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid10 = 72; skillid10 < 80; skillid10++) {
                skillList.add(new Integer(skillid10));
            }
        }
        return skillList;
    }

    public static ArrayList<Integer> isElf(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 8) {
            for (int skillid = 0; skillid < 8; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 16) {
            for (int skillid2 = 8; skillid2 < 16; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 24) {
            for (int skillid3 = 16; skillid3 < 23; skillid3++) {
                skillList.add(new Integer(skillid3));
            }
        }
        if (pc.getLevel() >= 32) {
            for (int skillid4 = 24; skillid4 < 32; skillid4++) {
                skillList.add(new Integer(skillid4));
            }
        }
        if (pc.getLevel() >= 40) {
            for (int skillid5 = 32; skillid5 < 40; skillid5++) {
                skillList.add(new Integer(skillid5));
            }
        }
        if (pc.getLevel() >= 48) {
            for (int skillid6 = 40; skillid6 < 48; skillid6++) {
                skillList.add(new Integer(skillid6));
            }
        }
        if (pc.getLevel() >= 10) {
            skillList.add(new Integer(128));
            skillList.add(new Integer(129));
            skillList.add(new Integer((int) L1SkillId.BODY_TO_MIND));
        }
        if (pc.getLevel() >= 20) {
            skillList.add(new Integer(136));
            skillList.add(new Integer(137));
        }
        if (pc.getLevel() >= 30) {
            skillList.add(new Integer(131));
            skillList.add(new Integer(144));
            skillList.add(new Integer(145));
            skillList.add(new Integer((int) L1SkillId.BLOODY_SOUL));
            switch (pc.getElfAttr()) {
                case 1:
                    skillList.add(new Integer(150));
                    skillList.add(new Integer(151));
                    break;
                case 2:
                    skillList.add(new Integer((int) L1SkillId.ELEMENTAL_PROTECTION));
                    break;
                case 4:
                    skillList.add(new Integer((int) L1SkillId.EXOTIC_VITALIZE));
                    break;
                case 8:
                    skillList.add(new Integer(148));
                    skillList.add(new Integer(149));
                    break;
            }
        }
        if (pc.getLevel() >= 40) {
            skillList.add(new Integer((int) L1SkillId.TRIPLE_ARROW));
            skillList.add(new Integer((int) L1SkillId.ENTANGLE));
            skillList.add(new Integer(153));
            switch (pc.getElfAttr()) {
                case 1:
                    skillList.add(new Integer((int) L1SkillId.STORM_EYE));
                    skillList.add(new Integer((int) L1SkillId.NATURES_TOUCH));
                    break;
                case 2:
                    skillList.add(new Integer(154));
                    break;
                case 4:
                    skillList.add(new Integer(157));
                    skillList.add(new Integer(159));
                    break;
                case 8:
                    skillList.add(new Integer(155));
                    break;
            }
        }
        if (pc.getLevel() >= 50) {
            skillList.add(new Integer(133));
            skillList.add(new Integer((int) L1SkillId.AQUA_PROTECTER));
            skillList.add(new Integer(161));
            switch (pc.getElfAttr()) {
                case 1:
                    skillList.add(new Integer(167));
                    skillList.add(new Integer((int) L1SkillId.IRON_SKIN));
                    break;
                case 2:
                    skillList.add(new Integer(162));
                    skillList.add(new Integer(170));
                    skillList.add(new Integer(174));
                    skillList.add(new Integer((int) L1SkillId.SOUL_OF_FLAME));
                    break;
                case 4:
                    skillList.add(new Integer((int) L1SkillId.BURNING_WEAPON));
                    skillList.add(new Integer(164));
                    skillList.add(new Integer((int) L1SkillId.STORM_WALK));
                    break;
                case 8:
                    skillList.add(new Integer(165));
                    skillList.add(new Integer(166));
                    skillList.add(new Integer(173));
                    break;
            }
        }
        return skillList;
    }

    public static ArrayList<Integer> isKnight(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 50) {
            for (int skillid = 0; skillid < 8; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 50) {
            skillList.add(new Integer(86));
            skillList.add(new Integer(87));
            skillList.add(new Integer(89));
            skillList.add(new Integer(90));
        }
        if (pc.getLevel() >= 60) {
            skillList.add(new Integer(89));
        }
        return skillList;
    }

    public static ArrayList<Integer> isCrown(L1PcInstance pc) {
        ArrayList<Integer> skillList = new ArrayList<>();
        if (pc.getLevel() >= 10) {
            for (int skillid = 0; skillid < 8; skillid++) {
                skillList.add(new Integer(skillid));
            }
        }
        if (pc.getLevel() >= 20) {
            for (int skillid2 = 8; skillid2 < 16; skillid2++) {
                skillList.add(new Integer(skillid2));
            }
        }
        if (pc.getLevel() >= 15) {
            skillList.add(new Integer(112));
        }
        if (pc.getLevel() >= 30) {
            skillList.add(new Integer(115));
        }
        if (pc.getLevel() >= 40) {
            skillList.add(new Integer(113));
        }
        if (pc.getLevel() >= 45) {
            skillList.add(new Integer(117));
        }
        if (pc.getLevel() >= 50) {
            skillList.add(new Integer(116));
        }
        if (pc.getLevel() >= 55) {
            skillList.add(new Integer((int) L1SkillId.GLOWING_AURA));
        }
        return skillList;
    }
}
