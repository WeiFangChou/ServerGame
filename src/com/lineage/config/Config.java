package com.lineage.config;

import com.lineage.list.Announcements;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    public static String[] AUTORESTART = null;
    public static int AUTOSAVE_INTERVAL = 0;
    public static int AUTOSAVE_INTERVAL_INVENTORY = 0;
    public static boolean AUTO_CREATE_ACCOUNTS = false;
    public static final int AVer = 2010083002;
    public static String CHAT_SERVER_HOST_NAME = null;
    public static int CHAT_SERVER_PORT = 0;
    public static int CLIENT_LANGUAGE = 0;
    public static String CLIENT_LANGUAGE_CODE;
    public static final int CVer = 120913200;
    public static boolean DEBUG = false;
    public static String GAME_SERVER_HOST_NAME = null;
    public static String GAME_SERVER_PORT = null;
    public static boolean ISUBUNTU = false;
    public static String[] LANGUAGE_CODE_ARRAY = {"UTF8", "EUCKR", "UTF8", "BIG5", "SJIS", "GBK"};
    public static boolean LOGINS_TO_AUTOENTICATION = false;
    public static int MAX_ONLINE_USERS = 10;
    public static boolean NEWS = false;
    public static final int NVer = 120913201;
    public static int PC_RECOGNIZE_RANGE = 0;
    public static int RESTART_LOGIN = 0;
    public static String RSA_KEY_E = null;
    public static String RSA_KEY_N = null;
    public static String SERVERNAME = null;
    public static int SERVERNO = 0;
    private static final String SERVER_CONFIG_FILE = "./config/server.properties";
    public static final String SRCVER = "Lineage3.63C";
    public static final int SVer = 120913203;
    public static String TIME_ZONE = null;
    public static final int Time = 490882;
    public static final String VER = "3.63C";

    public static void load() throws Exception {
        Properties pack = new Properties();
        try {
            InputStream is = new FileInputStream(new File("./config/pack.properties"));
            pack.load(is);
            is.close();
            LOGINS_TO_AUTOENTICATION = Boolean.parseBoolean(pack.getProperty("Autoentication", "false"));
            RSA_KEY_E = pack.getProperty("RSA_KEY_E", "0");
            RSA_KEY_N = pack.getProperty("RSA_KEY_N", "0");
        } catch (Exception e) {
            System.err.println("沒有找到登入器加密設置檔案: ./config/pack.properties");
        } finally {
            pack.clear();
        }
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(SERVER_CONFIG_FILE));
            set.load(is);
            is.close();
            SERVERNO = Integer.parseInt(set.getProperty("ServerNo", "1"));
            GAME_SERVER_HOST_NAME = set.getProperty("GameserverHostname", "*");
            GAME_SERVER_PORT = set.getProperty("GameserverPort", "2000-2001");
            CLIENT_LANGUAGE = Integer.parseInt(set.getProperty("ClientLanguage", "3"));
            CLIENT_LANGUAGE_CODE = LANGUAGE_CODE_ARRAY[CLIENT_LANGUAGE];
            String tmp = set.getProperty("AutoRestart", "");
            if (!tmp.equalsIgnoreCase("null")) {
                AUTORESTART = tmp.split(",");
            }
            TIME_ZONE = set.getProperty("TimeZone", "CST");
            AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(set.getProperty("AutoCreateAccounts", "true"));
            MAX_ONLINE_USERS = Integer.parseInt(set.getProperty("MaximumOnlineUsers", "30"));
            AUTOSAVE_INTERVAL = Integer.parseInt(set.getProperty("AutosaveInterval", "1200"), 10);
            AUTOSAVE_INTERVAL /= 60;
            if (AUTOSAVE_INTERVAL <= 0) {
                AUTOSAVE_INTERVAL = 20;
            }
            AUTOSAVE_INTERVAL_INVENTORY = Integer.parseInt(set.getProperty("AutosaveIntervalOfInventory", "300"), 10);
            AUTOSAVE_INTERVAL_INVENTORY /= 60;
            if (AUTOSAVE_INTERVAL_INVENTORY <= 0) {
                AUTOSAVE_INTERVAL_INVENTORY = 5;
            }
            PC_RECOGNIZE_RANGE = Integer.parseInt(set.getProperty("PcRecognizeRange", "13"));
            RESTART_LOGIN = Integer.parseInt(set.getProperty("restartlogin", "30"));
            NEWS = Boolean.parseBoolean(set.getProperty("News", "false"));
            if (NEWS) {
                Announcements.get().load();
            }
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: "+SERVER_CONFIG_FILE);
        }finally {
            set.clear();
        }
    }
}
