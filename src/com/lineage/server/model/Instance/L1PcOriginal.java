package com.lineage.server.model.Instance;

public class L1PcOriginal {
    public static int resetOriginalHpup(L1PcInstance pc) {
        int originalCon = pc.getOriginalCon();
        if (pc.isCrown()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    return 0;
                case 12:
                case 13:
                    return 1;
                case 14:
                case 15:
                    return 2;
                default:
                    return 3;
            }
        } else if (pc.isKnight()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                case 15:
                case 16:
                    return 1;
                default:
                    return 3;
            }
        } else if (pc.isElf()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 0;
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDarkelf()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    return 0;
                case 10:
                case 11:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isWizard()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    return 0;
                case 14:
                case 15:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDragonKnight()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                case 15:
                case 16:
                    return 1;
                default:
                    return 3;
            }
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 0;
                case 13:
                case 14:
                    return 1;
                default:
                    return 2;
            }
        }
    }

    public static int resetOriginalMpup(L1PcInstance pc) {
        int originalWis = pc.getOriginalWis();
        if (pc.isCrown()) {
            switch (originalWis) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    return 0;
                default:
                    return 1;
            }
        } else if (pc.isKnight()) {
            return 0;
        } else {
            if (pc.isElf()) {
                switch (originalWis) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        return 0;
                    case 14:
                    case 15:
                    case 16:
                        return 1;
                    default:
                        return 2;
                }
            } else if (pc.isDarkelf()) {
                switch (originalWis) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        return 0;
                    default:
                        return 1;
                }
            } else if (pc.isWizard()) {
                switch (originalWis) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        return 0;
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        return 1;
                    default:
                        return 2;
                }
            } else if (pc.isDragonKnight()) {
                switch (originalWis) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        return 0;
                    case 13:
                    case 14:
                    case 15:
                        return 1;
                    default:
                        return 2;
                }
            } else if (!pc.isIllusionist()) {
                return 0;
            } else {
                switch (originalWis) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        return 0;
                    case 13:
                    case 14:
                    case 15:
                        return 1;
                    default:
                        return 2;
                }
            }
        }
    }

    public static int resetOriginalStrWeightReduction(L1PcInstance pc) {
        int originalStr = pc.getOriginalStr();
        if (pc.isCrown()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    return 0;
                case 14:
                case 15:
                case 16:
                    return 1;
                case 17:
                case 18:
                case 19:
                    return 2;
                default:
                    return 3;
            }
        } else if (pc.isKnight()) {
            return 0;
        } else {
            if (pc.isElf()) {
                switch (originalStr) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        return 0;
                    default:
                        return 2;
                }
            } else if (pc.isDarkelf()) {
                switch (originalStr) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        return 0;
                    case 13:
                    case 14:
                    case 15:
                        return 2;
                    default:
                        return 3;
                }
            } else if (pc.isWizard()) {
                switch (originalStr) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                        return 0;
                    default:
                        return 1;
                }
            } else if (pc.isDragonKnight()) {
                switch (originalStr) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                        return 0;
                    default:
                        return 1;
                }
            } else if (!pc.isIllusionist()) {
                return 0;
            } else {
                switch (originalStr) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        return 0;
                    default:
                        return 1;
                }
            }
        }
    }

    public static int resetOriginalDmgup(L1PcInstance pc) {
        int originalStr = pc.getOriginalStr();
        if (pc.isCrown()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                case 15:
                case 16:
                case 17:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isKnight()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                case 17:
                    return 0;
                case 18:
                case 19:
                    return 2;
                default:
                    return 4;
            }
        } else if (pc.isElf()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    return 0;
                case 12:
                case 13:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDarkelf()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    return 0;
                case 14:
                case 15:
                case 16:
                case 17:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isWizard()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    return 0;
                case 10:
                case 11:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDragonKnight()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                case 15:
                case 16:
                case 17:
                    return 1;
                default:
                    return 3;
            }
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    return 0;
                case 12:
                case 13:
                    return 1;
                default:
                    return 2;
            }
        }
    }

    public static int resetOriginalConWeightReduction(L1PcInstance pc) {
        int originalCon = pc.getOriginalCon();
        if (pc.isCrown()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    return 0;
                default:
                    return 1;
            }
        } else if (pc.isKnight()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                default:
                    return 1;
            }
        } else if (pc.isElf()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                default:
                    return 2;
            }
        } else if (pc.isDarkelf()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                    return 0;
                default:
                    return 1;
            }
        } else if (pc.isWizard()) {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 0;
                case 13:
                case 14:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDragonKnight() || !pc.isIllusionist()) {
            return 0;
        } else {
            switch (originalCon) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    return 0;
                case 17:
                    return 1;
                default:
                    return 2;
            }
        }
    }

    public static int resetOriginalBowDmgup(L1PcInstance pc) {
        int originalDex = pc.getOriginalDex();
        if (pc.isCrown()) {
            switch (originalDex) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 0;
                default:
                    return 1;
            }
        } else if (pc.isKnight()) {
            return 0;
        } else {
            if (pc.isElf()) {
                switch (originalDex) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                        return 0;
                    case 14:
                    case 15:
                    case 16:
                        return 2;
                    default:
                        return 3;
                }
            } else if (pc.isDarkelf()) {
                switch (originalDex) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                        return 0;
                    default:
                        return 2;
                }
            } else if (!pc.isWizard() && !pc.isDragonKnight() && pc.isIllusionist()) {
                return 0;
            } else {
                return 0;
            }
        }
    }

    public static int resetOriginalHitup(L1PcInstance pc) {
        int originalStr = pc.getOriginalStr();
        if (pc.isCrown()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    return 0;
                case 16:
                case 17:
                case 18:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isKnight()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    return 0;
                case 17:
                case 18:
                    return 2;
                default:
                    return 4;
            }
        } else if (pc.isElf()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                    return 0;
                case 13:
                case 14:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDarkelf()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                    return 0;
                case 15:
                case 16:
                case 17:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isWizard()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    return 0;
                case 11:
                case 12:
                    return 1;
                default:
                    return 2;
            }
        } else if (pc.isDragonKnight()) {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    return 0;
                case 14:
                case 15:
                case 16:
                    return 1;
                default:
                    return 3;
            }
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            switch (originalStr) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    return 0;
                case 12:
                case 13:
                    return 1;
                case 14:
                case 15:
                    return 2;
                case 16:
                    return 3;
                default:
                    return 4;
            }
        }
    }

    public static int resetOriginalBowHitup(L1PcInstance pc) {
        int originalDex = pc.getOriginalDex();
        if (pc.isCrown() || pc.isKnight()) {
            return 0;
        }
        if (pc.isElf()) {
            if (originalDex >= 13 && originalDex <= 15) {
                return 2;
            }
            if (originalDex >= 16) {
                return 3;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalDex == 17) {
                return 1;
            }
            if (originalDex == 18) {
                return 2;
            }
            return 0;
        } else if (!pc.isWizard() && !pc.isDragonKnight() && pc.isIllusionist()) {
            return 0;
        } else {
            return 0;
        }
    }

    public static int resetOriginalMr(L1PcInstance pc) {
        int originalWis = pc.getOriginalWis();
        if (pc.isCrown()) {
            if (originalWis == 12 || originalWis == 13) {
                return 1;
            }
            if (originalWis >= 14) {
                return 2;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalWis == 10 || originalWis == 11) {
                return 1;
            }
            if (originalWis >= 12) {
                return 2;
            }
            return 0;
        } else if (pc.isElf()) {
            if (originalWis >= 13 && originalWis <= 15) {
                return 1;
            }
            if (originalWis >= 16) {
                return 2;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalWis >= 11 && originalWis <= 13) {
                return 1;
            }
            if (originalWis == 14) {
                return 2;
            }
            if (originalWis == 15) {
                return 3;
            }
            if (originalWis >= 16) {
                return 4;
            }
            return 0;
        } else if (pc.isWizard()) {
            if (originalWis >= 15) {
                return 1;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalWis >= 14) {
                return 2;
            }
            return 0;
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            if (originalWis >= 15 && originalWis <= 17) {
                return 2;
            }
            if (originalWis == 18) {
                return 4;
            }
            return 0;
        }
    }

    public static int resetOriginalMagicHit(L1PcInstance pc) {
        int originalInt = pc.getOriginalInt();
        if (pc.isCrown()) {
            if (originalInt == 12 || originalInt == 13) {
                return 1;
            }
            if (originalInt >= 14) {
                return 2;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalInt == 10 || originalInt == 11) {
                return 1;
            }
            if (originalInt == 12) {
                return 2;
            }
            return 0;
        } else if (pc.isElf()) {
            if (originalInt == 13 || originalInt == 14) {
                return 1;
            }
            if (originalInt >= 15) {
                return 2;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalInt == 12 || originalInt == 13) {
                return 1;
            }
            if (originalInt >= 14) {
                return 2;
            }
            return 0;
        } else if (pc.isWizard()) {
            if (originalInt <= 14) {
                return 0;
            }
            if (originalInt > 14 && originalInt <= 80) {
                return 1;
            }
            if (originalInt > 80) {
                return 2;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalInt == 12 || originalInt == 13) {
                return 2;
            }
            if (originalInt == 14 || originalInt == 15) {
                return 3;
            }
            if (originalInt >= 16) {
                return 4;
            }
            return 0;
        } else if (!pc.isIllusionist() || originalInt < 13) {
            return 0;
        } else {
            return 1;
        }
    }

    public static int resetOriginalMagicCritical(L1PcInstance pc) {
        int originalInt = pc.getOriginalInt();
        if (pc.isCrown() || pc.isKnight()) {
            return 0;
        }
        if (pc.isElf()) {
            if (originalInt == 14 || originalInt == 15) {
                return 2;
            }
            if (originalInt >= 16) {
                return 4;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            return 0;
        } else {
            if (pc.isWizard()) {
                if (originalInt == 15) {
                    return 2;
                }
                if (originalInt == 16) {
                    return 4;
                }
                if (originalInt == 17) {
                    return 6;
                }
                if (originalInt == 18) {
                    return 8;
                }
                return 0;
            } else if (!pc.isDragonKnight() && !pc.isIllusionist()) {
                return 0;
            } else {
                return 0;
            }
        }
    }

    public static int resetOriginalMagicConsumeReduction(L1PcInstance pc) {
        int originalInt = pc.getOriginalInt();
        if (pc.isCrown()) {
            if (originalInt == 11 || originalInt == 12) {
                return 1;
            }
            if (originalInt >= 13) {
                return 2;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalInt == 9 || originalInt == 10) {
                return 1;
            }
            if (originalInt >= 11) {
                return 2;
            }
            return 0;
        } else if (pc.isElf()) {
            return 0;
        } else {
            if (pc.isDarkelf()) {
                if (originalInt == 13 || originalInt == 14) {
                    return 1;
                }
                if (originalInt >= 15) {
                    return 2;
                }
                return 0;
            } else if (pc.isWizard() || pc.isDragonKnight() || !pc.isIllusionist()) {
                return 0;
            } else {
                if (originalInt == 14) {
                    return 1;
                }
                if (originalInt >= 15) {
                    return 2;
                }
                return 0;
            }
        }
    }

    public static int resetOriginalMagicDamage(L1PcInstance pc) {
        int originalInt = pc.getOriginalInt();
        if (pc.isCrown() || pc.isKnight() || pc.isElf() || pc.isDarkelf()) {
            return 0;
        }
        if (pc.isWizard()) {
            if (originalInt >= 13) {
                return 1;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalInt == 13 || originalInt == 14) {
                return 1;
            }
            if (originalInt == 15 || originalInt == 16) {
                return 2;
            }
            if (originalInt == 17) {
                return 3;
            }
            return 0;
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            if (originalInt == 16) {
                return 1;
            }
            if (originalInt == 17) {
                return 2;
            }
            return 0;
        }
    }

    public static int resetOriginalAc(L1PcInstance pc) {
        int originalDex = pc.getOriginalDex();
        if (pc.isCrown()) {
            if (originalDex >= 12 && originalDex <= 14) {
                return 1;
            }
            if (originalDex == 15 || originalDex == 16) {
                return 2;
            }
            if (originalDex >= 17) {
                return 3;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalDex == 13 || originalDex == 14) {
                return 1;
            }
            if (originalDex >= 15) {
                return 3;
            }
            return 0;
        } else if (pc.isElf()) {
            if (originalDex >= 15 && originalDex <= 17) {
                return 1;
            }
            if (originalDex == 18) {
                return 2;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalDex >= 17) {
                return 1;
            }
            return 0;
        } else if (pc.isWizard()) {
            if (originalDex == 8 || originalDex == 9) {
                return 1;
            }
            if (originalDex >= 10) {
                return 2;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalDex == 12 || originalDex == 13) {
                return 1;
            }
            if (originalDex >= 14) {
                return 2;
            }
            return 0;
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            if (originalDex == 11 || originalDex == 12) {
                return 1;
            }
            if (originalDex >= 13) {
                return 2;
            }
            return 0;
        }
    }

    public static int resetOriginalEr(L1PcInstance pc) {
        int originalDex = pc.getOriginalDex();
        if (pc.isCrown()) {
            if (originalDex == 14 || originalDex == 15) {
                return 1;
            }
            if (originalDex == 16 || originalDex == 17) {
                return 2;
            }
            if (originalDex == 18) {
                return 3;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalDex == 14 || originalDex == 15) {
                return 1;
            }
            if (originalDex == 16) {
                return 3;
            }
            return 0;
        } else if (pc.isElf()) {
            return 0;
        } else {
            if (pc.isDarkelf()) {
                if (originalDex >= 16) {
                    return 2;
                }
                return 0;
            } else if (pc.isWizard()) {
                if (originalDex == 9 || originalDex == 10) {
                    return 1;
                }
                if (originalDex == 11) {
                    return 2;
                }
                return 0;
            } else if (pc.isDragonKnight()) {
                if (originalDex == 13 || originalDex == 14) {
                    return 1;
                }
                if (originalDex >= 15) {
                    return 2;
                }
                return 0;
            } else if (!pc.isIllusionist()) {
                return 0;
            } else {
                if (originalDex == 12 || originalDex == 13) {
                    return 1;
                }
                if (originalDex >= 14) {
                    return 2;
                }
                return 0;
            }
        }
    }

    public static short resetOriginalHpr(L1PcInstance pc) {
        int originalCon = pc.getOriginalCon();
        if (pc.isCrown()) {
            if (originalCon == 13 || originalCon == 14) {
                return 1;
            }
            if (originalCon == 15 || originalCon == 16) {
                return 2;
            }
            if (originalCon == 17) {
                return 3;
            }
            if (originalCon == 18) {
                return 4;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalCon == 16 || originalCon == 17) {
                return 2;
            }
            if (originalCon == 18) {
                return 4;
            }
            return 0;
        } else if (pc.isElf()) {
            if (originalCon == 14 || originalCon == 15) {
                return 1;
            }
            if (originalCon == 16) {
                return 2;
            }
            if (originalCon >= 17) {
                return 3;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalCon == 11 || originalCon == 12) {
                return 1;
            }
            if (originalCon >= 13) {
                return 2;
            }
            return 0;
        } else if (pc.isWizard()) {
            if (originalCon == 17) {
                return 1;
            }
            if (originalCon == 18) {
                return 2;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalCon == 16 || originalCon == 17) {
                return 1;
            }
            if (originalCon == 18) {
                return 3;
            }
            return 0;
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            if (originalCon == 14 || originalCon == 15) {
                return 1;
            }
            if (originalCon >= 16) {
                return 2;
            }
            return 0;
        }
    }

    public static short resetOriginalMpr(L1PcInstance pc) {
        int originalWis = pc.getOriginalWis();
        if (pc.isCrown()) {
            if (originalWis == 13 || originalWis == 14) {
                return 1;
            }
            if (originalWis >= 15) {
                return 2;
            }
            return 0;
        } else if (pc.isKnight()) {
            if (originalWis == 11 || originalWis == 12) {
                return 1;
            }
            if (originalWis == 13) {
                return 2;
            }
            return 0;
        } else if (pc.isElf()) {
            if (originalWis >= 15 && originalWis <= 17) {
                return 1;
            }
            if (originalWis == 18) {
                return 2;
            }
            return 0;
        } else if (pc.isDarkelf()) {
            if (originalWis >= 13) {
                return 1;
            }
            return 0;
        } else if (pc.isWizard()) {
            if (originalWis == 14 || originalWis == 15) {
                return 1;
            }
            if (originalWis == 16 || originalWis == 17) {
                return 2;
            }
            if (originalWis == 18) {
                return 3;
            }
            return 0;
        } else if (pc.isDragonKnight()) {
            if (originalWis == 15 || originalWis == 16) {
                return 1;
            }
            if (originalWis >= 17) {
                return 2;
            }
            return 0;
        } else if (!pc.isIllusionist()) {
            return 0;
        } else {
            if (originalWis >= 14 && originalWis <= 16) {
                return 1;
            }
            if (originalWis >= 17) {
                return 2;
            }
            return 0;
        }
    }
}
