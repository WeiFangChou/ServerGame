package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public final class ConfigAlt {
    public static boolean ALT_ATKMSG = false;
    public static int ALT_ITEM_DELETION_RANGE = 0;
    public static int ALT_ITEM_DELETION_TIME = 0;
    public static String ALT_ITEM_DELETION_TYPE = null;
    public static boolean ALT_NONPVP = false;
    public static boolean ALT_PUNISHMENT = false;
    public static int ALT_RATE_OF_DUTY = 0;
    private static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";
    public static int ALT_WAR_INTERVAL = 0;
    public static int ALT_WAR_INTERVAL_UNIT = 0;
    public static int ALT_WAR_TIME = 0;
    public static int ALT_WAR_TIME_UNIT = 0;
    public static double ALT_WHO_COUNT = 0.0d;
    public static int ALT_WHO_TYPE = 0;
    public static byte AUTO_LOOT = 0;
    public static int CHAR_BOOK_INIT_COUNT = 0;
    public static int CHAR_BOOK_MAX_CHARGE = 0;
    public static boolean CLAN_ALLIANCE = false;
    public static int DEFAULT_CHARACTER_SLOT = 0;
    public static boolean DELETE_CHARACTER_AFTER_7DAYS = false;
    public static int DELETE_CHARACTER_AFTER_LV = 0;
    public static boolean DORP_ITEM = false;
    public static int ELEMENTAL_STONE_AMOUNT = 0;
    public static int GDROPITEM_TIME = 0;
    public static short GLOBAL_CHAT_LEVEL = 0;
    public static int HOUSE_TAX_INTERVAL = 0;
    public static int LOOTING_RANGE = 0;
    public static int MAX_CLAN_WAREHOUSE_ITEM = 0;
    public static int MAX_DOLL_COUNT = 0;
    public static final int MAX_NPC = 35;
    public static int MAX_NPC_ITEM;
    public static int MAX_PERSONAL_WAREHOUSE_ITEM;
    public static int MEDICINE;
    public static int NPC_DELETION_TIME;
    public static int POWER;
    public static int POWERMEDICINE;
    public static boolean SPAWN_HOME_POINT;
    public static int SPAWN_HOME_POINT_COUNT;
    public static int SPAWN_HOME_POINT_DELAY;
    public static int SPAWN_HOME_POINT_RANGE;
    public static short WHISPER_CHAT_LEVEL;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE));
            set.load(new InputStreamReader(is, "utf-8"));
            is.close();
            GLOBAL_CHAT_LEVEL = Short.parseShort(set.getProperty("GlobalChatLevel", "30"));
            WHISPER_CHAT_LEVEL = Short.parseShort(set.getProperty("WhisperChatLevel", "5"));
            AUTO_LOOT = Byte.parseByte(set.getProperty("AutoLoot", "2"));
            LOOTING_RANGE = Integer.parseInt(set.getProperty("LootingRange", "3"));
            ALT_NONPVP = Boolean.parseBoolean(set.getProperty("NonPvP", "true"));
            ALT_PUNISHMENT = Boolean.parseBoolean(set.getProperty("Punishment", "true"));
            CLAN_ALLIANCE = Boolean.parseBoolean(set.getProperty("ClanAlliance", "true"));
            ALT_ITEM_DELETION_TYPE = set.getProperty("ItemDeletionType", "auto");
            ALT_ITEM_DELETION_TIME = Integer.parseInt(set.getProperty("ItemDeletionTime", "10"));
            if (ALT_ITEM_DELETION_TIME > 60) {
                ALT_ITEM_DELETION_TIME = 60;
            }
            ALT_ITEM_DELETION_RANGE = Integer.parseInt(set.getProperty("ItemDeletionRange", "5"));
            ALT_WHO_TYPE = Integer.parseInt(set.getProperty("Who_type", "0"));
            ALT_WHO_COUNT = Double.parseDouble(set.getProperty("WhoCommandcount", "1.0"));
            if (ALT_WHO_COUNT < 1.0d) {
                ALT_WHO_COUNT = 1.0d;
            }
            String strWar = set.getProperty("WarTime", "2h");
            if (strWar.indexOf("d") >= 0) {
                ALT_WAR_TIME_UNIT = 5;
                strWar = strWar.replace("d", "");
            } else if (strWar.indexOf("h") >= 0) {
                ALT_WAR_TIME_UNIT = 11;
                strWar = strWar.replace("h", "");
            } else if (strWar.indexOf("m") >= 0) {
                ALT_WAR_TIME_UNIT = 12;
                strWar = strWar.replace("m", "");
            }
            ALT_WAR_TIME = Integer.parseInt(strWar);
            String strWar2 = set.getProperty("WarInterval", "4d");
            if (strWar2.indexOf("d") >= 0) {
                ALT_WAR_INTERVAL_UNIT = 5;
                strWar2 = strWar2.replace("d", "");
            } else if (strWar2.indexOf("h") >= 0) {
                ALT_WAR_INTERVAL_UNIT = 11;
                strWar2 = strWar2.replace("h", "");
            } else if (strWar2.indexOf("m") >= 0) {
                ALT_WAR_INTERVAL_UNIT = 12;
                strWar2 = strWar2.replace("m", "");
            }
            ALT_WAR_INTERVAL = Integer.parseInt(strWar2);
            SPAWN_HOME_POINT = Boolean.parseBoolean(set.getProperty("SpawnHomePoint", "true"));
            SPAWN_HOME_POINT_COUNT = Integer.parseInt(set.getProperty("SpawnHomePointCount", "2"));
            SPAWN_HOME_POINT_DELAY = Integer.parseInt(set.getProperty("SpawnHomePointDelay", "100"));
            SPAWN_HOME_POINT_RANGE = Integer.parseInt(set.getProperty("SpawnHomePointRange", "8"));
            ELEMENTAL_STONE_AMOUNT = Integer.parseInt(set.getProperty("ElementalStoneAmount", "300"));
            HOUSE_TAX_INTERVAL = Integer.parseInt(set.getProperty("HouseTaxInterval", "10"));
            MAX_DOLL_COUNT = Integer.parseInt(set.getProperty("MaxDollCount", "1"));
            MAX_NPC_ITEM = Integer.parseInt(set.getProperty("MaxNpcItem", "8"));
            MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty("MaxPersonalWarehouseItem", "100"));
            MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(set.getProperty("MaxClanWarehouseItem", "200"));
            DELETE_CHARACTER_AFTER_LV = Integer.parseInt(set.getProperty("DeleteCharacterAfterLV", "60"));
            DELETE_CHARACTER_AFTER_7DAYS = Boolean.parseBoolean(set.getProperty("DeleteCharacterAfter7Days", "True"));
            NPC_DELETION_TIME = Integer.parseInt(set.getProperty("NpcDeletionTime", "10"));
            DEFAULT_CHARACTER_SLOT = Integer.parseInt(set.getProperty("DefaultCharacterSlot", "4"));
            MEDICINE = Integer.parseInt(set.getProperty("Medicine", "20"));
            POWER = Integer.parseInt(set.getProperty("Power", "35"));
            POWERMEDICINE = Integer.parseInt(set.getProperty("MedicinePower", "45"));
            DORP_ITEM = Boolean.parseBoolean(set.getProperty("dorpitem", "true"));
            CHAR_BOOK_INIT_COUNT = Integer.parseInt(set.getProperty("CharBookInitCount", "60"));
            CHAR_BOOK_MAX_CHARGE = Integer.parseInt(set.getProperty("CharBookMaxCharge", "4"));
            GDROPITEM_TIME = Integer.parseInt(set.getProperty("GDropItemTime", "15"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/altsettings.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
        }
    }
}
