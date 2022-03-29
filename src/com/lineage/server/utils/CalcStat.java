package com.lineage.server.utils;

import com.lineage.config.ConfigCharSetting;
import java.util.Random;

public class CalcStat {
    private static Random rnd = new Random();

    private CalcStat() {
    }

    public static int calcAc(int level, int dex) {
        switch (dex) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return 10 - (level >> 3);
            case 10:
            case 11:
            case 12:
                return 10 - (level / 7);
            case 13:
            case 14:
            case 15:
                return 10 - (level / 6);
            case 16:
            case 17:
                return 10 - (level / 5);
            default:
                return 10 - (level >> 2);
        }
    }

    public static int calcStatMr(int wis) {
        switch (wis) {
            case 0:
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
                return 3;
            case 17:
                return 6;
            case 18:
                return 10;
            case 19:
                return 15;
            case 20:
                return 21;
            case 21:
                return 28;
            case 22:
                return 37;
            case 23:
                return 47;
            default:
                return 50;
        }
    }

    public static int calcDiffMr(int wis, int diff) {
        return calcStatMr(wis + diff) - calcStatMr(wis);
    }

    public static int calcStatHp(int charType, int baseMaxHp, int baseCon, int originalHpup) {
        int randomhp = 0;
        if (baseCon > 15) {
            randomhp =  (baseCon - 15);
        }
        switch (charType) {
            case 0:
                randomhp =  (( (rnd.nextInt(2) + 11)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.PRINCE_MAX_HP) {
                    randomhp =  (ConfigCharSetting.PRINCE_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 1:
                randomhp =  (( (rnd.nextInt(2) + 17)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.KNIGHT_MAX_HP) {
                    randomhp =  (ConfigCharSetting.KNIGHT_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 2:
                randomhp =  (( (rnd.nextInt(2) + 10)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.ELF_MAX_HP) {
                    randomhp =  (ConfigCharSetting.ELF_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 3:
                randomhp =  (( (rnd.nextInt(2) + 7)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.WIZARD_MAX_HP) {
                    randomhp =  (ConfigCharSetting.WIZARD_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 4:
                randomhp =  (( (rnd.nextInt(2) + 10)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.DARKELF_MAX_HP) {
                    randomhp =  (ConfigCharSetting.DARKELF_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 5:
                randomhp =  (( (rnd.nextInt(2) + 13)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.DRAGONKNIGHT_MAX_HP) {
                    randomhp =  (ConfigCharSetting.DRAGONKNIGHT_MAX_HP - baseMaxHp);
                    break;
                }
                break;
            case 6:
                randomhp =  (( (rnd.nextInt(2) + 9)) + randomhp);
                if (baseMaxHp + randomhp > ConfigCharSetting.ILLUSIONIST_MAX_HP) {
                    randomhp =  (ConfigCharSetting.ILLUSIONIST_MAX_HP - baseMaxHp);
                    break;
                }
                break;
        }
        int randomhp2 =  (randomhp + originalHpup);
        if (randomhp2 < 0) {
            return 0;
        }
        return randomhp2;
    }

    public static int calcStatMp(int charType, int baseMaxMp, int baseWis, int originalMpup) {
        int seedY;
        int seedZ;
        switch (baseWis) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 10:
            case 11:
                seedY = 2;
                break;
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                seedY = 3;
                break;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 25:
            case 26:
            case 29:
            case 30:
            case 34:
                seedY = 4;
                break;
            case 24:
            case 27:
            case 28:
            case 31:
            case 32:
            case 33:
            default:
                seedY = 5;
                break;
        }
        switch (baseWis) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                seedZ = 0;
                break;
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                seedZ = 1;
                break;
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
                seedZ = 2;
                break;
            case 21:
            case 22:
            case 23:
            case 24:
                seedZ = 3;
                break;
            case 25:
            case 26:
            case 27:
            case 28:
                seedZ = 4;
                break;
            case 29:
            case 30:
            case 31:
            case 32:
                seedZ = 5;
                break;
            default:
                seedZ = 6;
                break;
        }
        int randommp = rnd.nextInt(seedY) + 1 + seedZ;
        switch (charType) {
            case 0:
                if (baseMaxMp + randommp > ConfigCharSetting.PRINCE_MAX_MP) {
                    randommp = ConfigCharSetting.PRINCE_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 1:
                randommp = (randommp * 2) / 3;
                if (baseMaxMp + randommp > ConfigCharSetting.KNIGHT_MAX_MP) {
                    randommp = ConfigCharSetting.KNIGHT_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 2:
                randommp = (int) (((double) randommp) * 1.5d);
                if (baseMaxMp + randommp > ConfigCharSetting.ELF_MAX_MP) {
                    randommp = ConfigCharSetting.ELF_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 3:
                randommp *= 2;
                if (baseMaxMp + randommp > ConfigCharSetting.WIZARD_MAX_MP) {
                    randommp = ConfigCharSetting.WIZARD_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 4:
                randommp = (int) (((double) randommp) * 1.5d);
                if (baseMaxMp + randommp > ConfigCharSetting.DARKELF_MAX_MP) {
                    randommp = ConfigCharSetting.DARKELF_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 5:
                randommp = (randommp * 2) / 3;
                if (baseMaxMp + randommp > ConfigCharSetting.DRAGONKNIGHT_MAX_MP) {
                    randommp = ConfigCharSetting.DRAGONKNIGHT_MAX_MP - baseMaxMp;
                    break;
                }
                break;
            case 6:
                randommp = (randommp * 5) / 3;
                if (baseMaxMp + randommp > ConfigCharSetting.ILLUSIONIST_MAX_MP) {
                    randommp = ConfigCharSetting.ILLUSIONIST_MAX_MP - baseMaxMp;
                    break;
                }
                break;
        }
        int randommp2 = randommp + originalMpup;
        if (randommp2 < 0) {
            randommp2 = 0;
        }
        return  randommp2;
    }
}
