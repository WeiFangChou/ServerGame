package com.lineage.server.utils;

import com.lineage.server.model.Instance.L1PcInstance;

public class CalcInitHpMp {
    private CalcInitHpMp() {
    }

    public static int calcInitHp(L1PcInstance pc) {
        if (pc.isCrown()) {
            return 14;
        }
        if (pc.isKnight()) {
            return 16;
        }
        if (pc.isElf()) {
            return 15;
        }
        if (pc.isWizard() || pc.isDarkelf()) {
            return 12;
        }
        if (!pc.isDragonKnight() && !pc.isIllusionist()) {
            return 1;
        }
        return 15;
    }

    public static int calcInitMp(L1PcInstance pc) {
        if (pc.isCrown()) {
            switch (pc.getWis()) {
                case 11:
                    return 2;
                case 12:
                case 13:
                case 14:
                case 15:
                    return 3;
                case 16:
                case 17:
                case 18:
                    return 4;
                default:
                    return 2;
            }
        } else if (pc.isKnight()) {
            switch (pc.getWis()) {
                case 9:
                case 10:
                case 11:
                    return 1;
                case 12:
                case 13:
                    return 2;
                default:
                    return 1;
            }
        } else if (pc.isElf()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    return 4;
                case 16:
                case 17:
                case 18:
                    return 6;
                default:
                    return 4;
            }
        } else if (pc.isWizard()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    return 6;
                case 16:
                case 17:
                case 18:
                    return 8;
                default:
                    return 6;
            }
        } else if (pc.isDarkelf()) {
            switch (pc.getWis()) {
                case 10:
                case 11:
                    return 3;
                case 12:
                case 13:
                case 14:
                case 15:
                    return 4;
                case 16:
                case 17:
                case 18:
                    return 6;
                default:
                    return 3;
            }
        } else if (pc.isDragonKnight()) {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    return 4;
                case 16:
                case 17:
                case 18:
                    return 6;
                default:
                    return 4;
            }
        } else if (!pc.isIllusionist()) {
            return 1;
        } else {
            switch (pc.getWis()) {
                case 12:
                case 13:
                case 14:
                case 15:
                    return 4;
                case 16:
                case 17:
                case 18:
                    return 6;
                default:
                    return 4;
            }
        }
    }
}
