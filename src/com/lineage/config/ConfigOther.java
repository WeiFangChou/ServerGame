package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

public final class ConfigOther {
    public static boolean ALT_WARPUNISHMENT = false;
    public static byte ArmorOverSafeBoard = 0;
    public static byte Armor_EnchantLevel = 0;
    public static byte Armor_Over = 0;
    public static byte Armor_Over_SafeBoard = 0;
    public static int CASTLEHPR = 0;
    public static int CASTLEMPR = 0;
    public static boolean CHECK_ATTACK_INTERVAL = false;
    public static boolean CHECK_MOVE_INTERVAL = false;
    public static int CHECK_MOVE_STRICTNESS = 0;
    public static boolean CHECK_SPELL_INTERVAL = false;
    public static int CHECK_STRICTNESS = 0;
    public static int CLANCOUNT = 0;
    public static boolean CLANDEL = false;
    public static boolean CLANTITLE = false;
    public static boolean Class_Id = false;
    public static boolean DEBUG_MODE = false;
    public static int ENCOUNTER_LV = 0;
    public static int Enchant = 0;
    public static int FORESTHPR = 0;
    public static int FORESTMPR = 0;
    public static int GASH_SHOP_ITEM_ID = 0;
    public static List<Integer> GIVE_ITEM_LIST = new ArrayList();
    public static boolean GM_OVERHEARD = false;
    public static boolean GM_OVERHEARD0 = false;
    public static boolean GM_OVERHEARD11 = false;
    public static boolean GM_OVERHEARD13 = false;
    public static boolean GM_OVERHEARD4 = false;
    public static boolean GUI = false;
    public static int Gamesleep = 0;
    public static int HOMEHPR = 0;
    public static int HOMEMPR = 0;
    public static boolean HPBAR = false;
    public static int INJUSTICE_COUNT = 0;
    public static int INNHPR = 0;
    public static int INNMPR = 0;
    public static int JUSTICE_COUNT = 0;
    public static boolean KILLRED = true;
    private static final String LIANG = "./config/other.properties";
    public static boolean LIGHT;
    public static int MAX_LEVEL;
    public static int ONLINE_GIFT_LEVEL;
    public static int PET_MAX_LEVEL;
    public static int PUNISHMENT_MAP_ID;
    public static int PUNISHMENT_TIME;
    public static int PUNISHMENT_TYPE;
    public static int Pc_Level;
    public static int SET_GLOBAL;
    public static int SET_GLOBAL_COUNT;
    public static int SET_GLOBAL_TIME;
    public static int SET_ITEM;
    public static int SET_ITEM_COUNT;
    public static int SET_ITEM_COUNT_Party;
    public static int SET_ITEM_Party;
    public static int[] SHOCK;
    public static boolean SHOPINFO;
    public static boolean SuccessBoard;
    public static boolean Success_Board;
    public static double THUNDER_GRAB_INT;
    public static double THUNDER_GRAB_MR;
    public static int THUNDER_GRAB_RND;
    public static boolean WAR_DOLL;
    public static boolean WAR_Hier;
    public static boolean WAR_summon;
    public static byte WeaponOverSafeBoard;
    public static byte Weapon_EnchantLevel;
    public static byte Weapon_Over;
    public static byte Weapon_Over_SafeBoard;
    public static boolean Xljnet;
    public static int chanskill_ITEM;
    public static int chanskill_ITEM_COUNT;
    public static boolean item_getLogName;
    public static boolean pc_is;
    public static int pcskill_ITEM;
    public static int pcskill_ITEM_COUNT;
    public static int skillId_ITEM;
    public static int skillId_ITEM_COUNT;
    public static boolean skill_getName;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(LIANG));
            set.load(is);
            is.close();
            INJUSTICE_COUNT = Integer.parseInt(set.getProperty("InjusticeCount", "5"));
            JUSTICE_COUNT = Integer.parseInt(set.getProperty("JusticeCount", "5"));
            CHECK_STRICTNESS = Integer.parseInt(set.getProperty("CheckStrictness", "5"));
            CHECK_MOVE_STRICTNESS = Integer.parseInt(set.getProperty("CheckMoveStrictness", "5"));
            PUNISHMENT_TYPE = Integer.parseInt(set.getProperty("PunishmentType", "0"));
            PUNISHMENT_TIME = Integer.parseInt(set.getProperty("PunishmentTime", "0"));
            PUNISHMENT_MAP_ID = Integer.parseInt(set.getProperty("PunishmentMap", "0"));
            CHECK_MOVE_INTERVAL = Boolean.parseBoolean(set.getProperty("CheckMoveInterval", "false"));
            CHECK_ATTACK_INTERVAL = Boolean.parseBoolean(set.getProperty("CheckAttackInterval", "false"));
            CHECK_SPELL_INTERVAL = Boolean.parseBoolean(set.getProperty("CheckSpellInterval", "false"));
            ENCOUNTER_LV = Integer.parseInt(set.getProperty("encounter_lv", "20"));
            KILLRED = Boolean.parseBoolean(set.getProperty("kill_red", "false"));
            CLANDEL = Boolean.parseBoolean(set.getProperty("clanadel", "false"));
            CLANTITLE = Boolean.parseBoolean(set.getProperty("clanatitle", "false"));
            CLANCOUNT = Integer.parseInt(set.getProperty("clancount", "100"));
            LIGHT = Boolean.parseBoolean(set.getProperty("light", "false"));
            HPBAR = Boolean.parseBoolean(set.getProperty("hpbar", "false"));
            SHOPINFO = Boolean.parseBoolean(set.getProperty("shopinfo", "false"));
            HOMEHPR = Integer.parseInt(set.getProperty("homehpr", "100"));
            HOMEMPR = Integer.parseInt(set.getProperty("homempr", "100"));
            INNHPR = Integer.parseInt(set.getProperty("innhpr", "10"));
            INNMPR = Integer.parseInt(set.getProperty("innmpr", "10"));
            CASTLEHPR = Integer.parseInt(set.getProperty("castlehpr", "10"));
            CASTLEMPR = Integer.parseInt(set.getProperty("castlempr", "10"));
            FORESTHPR = Integer.parseInt(set.getProperty("foresthpr", "10"));
            FORESTMPR = Integer.parseInt(set.getProperty("forestmpr", "10"));
            SET_GLOBAL = Integer.parseInt(set.getProperty("set_global", "100"));
            SET_GLOBAL_COUNT = Integer.parseInt(set.getProperty("set_global_count", "100"));
            SET_GLOBAL_TIME = Integer.parseInt(set.getProperty("set_global_time", "5"));
            skillId_ITEM = Integer.parseInt(set.getProperty("skillId_item", "100"));
            skillId_ITEM_COUNT = Integer.parseInt(set.getProperty("skillId_item_count", "100"));
            chanskill_ITEM = Integer.parseInt(set.getProperty("chanskill_item", "100"));
            chanskill_ITEM_COUNT = Integer.parseInt(set.getProperty("chanskill_item_count", "100"));
            pcskill_ITEM = Integer.parseInt(set.getProperty("pcskill_item", "100"));
            pcskill_ITEM_COUNT = Integer.parseInt(set.getProperty("pcskill_item_count", "100"));
            WAR_DOLL = Boolean.parseBoolean(set.getProperty("war_doll", "true"));
            WAR_Hier = Boolean.parseBoolean(set.getProperty("war_hier", "true"));
            WAR_summon = Boolean.parseBoolean(set.getProperty("war_summon", "true"));
            Class_Id = Boolean.parseBoolean(set.getProperty("ClassId", "true"));
            Xljnet = Boolean.parseBoolean(set.getProperty("Xljnet", "false"));
            PET_MAX_LEVEL = Integer.parseInt(set.getProperty("PetMaxLevel", "0"));
            MAX_LEVEL = Integer.parseInt(set.getProperty("maxLevel", "99"));
            GUI = Boolean.parseBoolean(set.getProperty("GUI", "true"));
            GM_OVERHEARD = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD", "false"));
            GM_OVERHEARD0 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD0", "false"));
            GM_OVERHEARD4 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD4", "false"));
            GM_OVERHEARD11 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD11", "false"));
            GM_OVERHEARD13 = Boolean.parseBoolean(set.getProperty("GM_OVERHEARD13", "false"));
            Gamesleep = Integer.parseInt(set.getProperty("Gamesleep", "30"));
            SuccessBoard = Boolean.parseBoolean(set.getProperty("SuccessBoard", "false"));
            WeaponOverSafeBoard = Byte.parseByte(set.getProperty("WeaponOverSafeBoard", "2"));
            ArmorOverSafeBoard = Byte.parseByte(set.getProperty("ArmorOverSafeBoard", "2"));
            Success_Board = Boolean.parseBoolean(set.getProperty("Success_Board", "false"));
            Weapon_Over = Byte.parseByte(set.getProperty("Weapon_Over", "2"));
            Armor_Over = Byte.parseByte(set.getProperty("Armor_Over", "2"));
            Weapon_EnchantLevel = Byte.parseByte(set.getProperty("Weapon_EnchantLevel", "2"));
            Armor_EnchantLevel = Byte.parseByte(set.getProperty("Armor_EnchantLevel", "2"));
            Weapon_Over_SafeBoard = Byte.parseByte(set.getProperty("Weapon_Over_SafeBoard", "2"));
            Armor_Over_SafeBoard = Byte.parseByte(set.getProperty("Armor_Over_SafeBoard", "2"));
            pc_is = Boolean.parseBoolean(set.getProperty("pcis", "false"));
            item_getLogName = Boolean.parseBoolean(set.getProperty("item_getLogName", "false"));
            skill_getName = Boolean.parseBoolean(set.getProperty("skill_getName", "false"));
            ALT_WARPUNISHMENT = Boolean.parseBoolean(set.getProperty("WarPunishment", "false"));
            Pc_Level = Integer.parseInt(set.getProperty("Pc_Level", "50"));
            if (set.getProperty("GiveItemList") != null) {
                for (String str : set.getProperty("GiveItemList").split(",")) {
                    GIVE_ITEM_LIST.add(Integer.valueOf(Integer.parseInt(str)));
                }
            }
            SHOCK = toIntArray(set.getProperty("SHOCK", ""), ",");
            GASH_SHOP_ITEM_ID = Integer.parseInt(set.getProperty("GSItemID", "44070"));
            ONLINE_GIFT_LEVEL = Short.parseShort(set.getProperty("onlinegiftlevel", "5"));
            SET_ITEM = Integer.parseInt(set.getProperty("set_item", "100"));
            SET_ITEM_COUNT = Integer.parseInt(set.getProperty("set_item_count", "100"));
            SET_ITEM_Party = Integer.parseInt(set.getProperty("set_item_party", "100"));
            SET_ITEM_COUNT_Party = Integer.parseInt(set.getProperty("set_item_count_party", "100"));
            THUNDER_GRAB_RND = Integer.parseInt(set.getProperty("THUNDER_GRAB_RND", "10"));
            THUNDER_GRAB_INT = Double.parseDouble(set.getProperty("THUNDER_GRAB_RND_INT", "1.5"));
            THUNDER_GRAB_MR = Double.parseDouble(set.getProperty("THUNDER_GRAB_RND_MR", "1.5"));
            Enchant = Integer.parseInt(set.getProperty("enchant", "3"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: "+LIANG);
        }finally {
            set.clear();
        }
    }

    public static int[] toIntArray(String text, String type) {
        StringTokenizer st = new StringTokenizer(text, type);
        int[] iReturn = new int[st.countTokens()];
        for (int i = 0; i < iReturn.length; i++) {
            iReturn[i] = Integer.parseInt(st.nextToken());
        }
        return iReturn;
    }
}
