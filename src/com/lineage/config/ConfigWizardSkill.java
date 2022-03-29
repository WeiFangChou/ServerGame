package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigWizardSkill {
    public static int CANCELLATION_1 = 0;
    public static int CANCELLATION_2 = 0;
    public static int CANCELLATION_3 = 0;
    public static double CANCELLATION_INT = 0.0d;
    public static double CANCELLATION_MR = 0.0d;
    private static final String CONFIG_FILE = "./config/【法師】技能設置.properties";
    public static int CURSE_PARALYZE_1;
    public static int CURSE_PARALYZE_2;
    public static int CURSE_PARALYZE_3;
    public static double CURSE_PARALYZE_INT;
    public static double CURSE_PARALYZE_MR;
    public static int CURSE_READY_TIME;
    public static int CURSE_TIME;
    public static int DARKNESS_1;
    public static int DARKNESS_2;
    public static int DARKNESS_3;
    public static double DARKNESS_INT;
    public static double DARKNESS_MR;
    public static int DECAY_POTION_1;
    public static int DECAY_POTION_2;
    public static int DECAY_POTION_3;
    public static double DECAY_POTION_INT;
    public static double DECAY_POTION_MR;
    public static int FOG_OF_SLEEPING_1;
    public static int FOG_OF_SLEEPING_2;
    public static int FOG_OF_SLEEPING_3;
    public static double FOG_OF_SLEEPING_INT;
    public static double FOG_OF_SLEEPING_MR;
    public static int ICE_LANCE_1;
    public static int ICE_LANCE_2;
    public static int ICE_LANCE_3;
    public static double ICE_LANCE_INT;
    public static double ICE_LANCE_MR;
    public static int SILENCE_1;
    public static int SILENCE_2;
    public static int SILENCE_3;
    public static double SILENCE_INT;
    public static double SILENCE_MR;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(CONFIG_FILE));
            set.load(is);
            is.close();
            SILENCE_1 = Integer.parseInt(set.getProperty("SILENCE_1", "5"));
            SILENCE_2 = Integer.parseInt(set.getProperty("SILENCE_2", "10"));
            SILENCE_3 = Integer.parseInt(set.getProperty("SILENCE_3", "15"));
            SILENCE_INT = Double.parseDouble(set.getProperty("SILENCE_INT", "0"));
            SILENCE_MR = Double.parseDouble(set.getProperty("SILENCE_MR", "0"));
            CURSE_PARALYZE_1 = Integer.parseInt(set.getProperty("CURSE_PARALYZE_1", "5"));
            CURSE_PARALYZE_2 = Integer.parseInt(set.getProperty("CURSE_PARALYZE_2", "10"));
            CURSE_PARALYZE_3 = Integer.parseInt(set.getProperty("CURSE_PARALYZE_3", "15"));
            CURSE_PARALYZE_INT = Double.parseDouble(set.getProperty("CURSE_PARALYZE_INT", "0"));
            CURSE_PARALYZE_MR = Double.parseDouble(set.getProperty("CURSE_PARALYZE_MR", "0"));
            CURSE_READY_TIME = Integer.parseInt(set.getProperty("CURSE_READY_TIME", "5"));
            CURSE_TIME = Integer.parseInt(set.getProperty("CURSE_TIME", "4"));
            CANCELLATION_1 = Integer.parseInt(set.getProperty("CANCELLATION_1", "5"));
            CANCELLATION_2 = Integer.parseInt(set.getProperty("CANCELLATION_2", "10"));
            CANCELLATION_3 = Integer.parseInt(set.getProperty("CANCELLATION_3", "15"));
            CANCELLATION_INT = Double.parseDouble(set.getProperty("CANCELLATION_INT", "0"));
            CANCELLATION_MR = Double.parseDouble(set.getProperty("CANCELLATION_MR", "0"));
            ICE_LANCE_1 = Integer.parseInt(set.getProperty("ICE_LANCE_1", "5"));
            ICE_LANCE_2 = Integer.parseInt(set.getProperty("ICE_LANCE_2", "10"));
            ICE_LANCE_3 = Integer.parseInt(set.getProperty("ICE_LANCE_3", "15"));
            ICE_LANCE_INT = Double.parseDouble(set.getProperty("ICE_LANCE_INT", "0"));
            ICE_LANCE_MR = Double.parseDouble(set.getProperty("ICE_LANCE_MR", "0"));
            DECAY_POTION_1 = Integer.parseInt(set.getProperty("DECAY_POTION_1", "5"));
            DECAY_POTION_2 = Integer.parseInt(set.getProperty("DECAY_POTION_2", "10"));
            DECAY_POTION_3 = Integer.parseInt(set.getProperty("DECAY_POTION_3", "15"));
            DECAY_POTION_INT = Double.parseDouble(set.getProperty("DECAY_POTION_INT", "0"));
            DECAY_POTION_MR = Double.parseDouble(set.getProperty("DECAY_POTION_MR", "0"));
            FOG_OF_SLEEPING_1 = Integer.parseInt(set.getProperty("FOG_OF_SLEEPING_1", "5"));
            FOG_OF_SLEEPING_2 = Integer.parseInt(set.getProperty("FOG_OF_SLEEPING_2", "10"));
            FOG_OF_SLEEPING_3 = Integer.parseInt(set.getProperty("FOG_OF_SLEEPING_3", "15"));
            FOG_OF_SLEEPING_INT = Double.parseDouble(set.getProperty("FOG_OF_SLEEPING_INT", "0"));
            FOG_OF_SLEEPING_MR = Double.parseDouble(set.getProperty("FOG_OF_SLEEPING_MR", "0"));
            DARKNESS_1 = Integer.parseInt(set.getProperty("DARKNESS_1", "5"));
            DARKNESS_2 = Integer.parseInt(set.getProperty("DARKNESS_2", "10"));
            DARKNESS_3 = Integer.parseInt(set.getProperty("DARKNESS_3", "15"));
            DARKNESS_INT = Double.parseDouble(set.getProperty("DARKNESS_INT", "0"));
            DARKNESS_MR = Double.parseDouble(set.getProperty("DARKNESS_MR", "0"));

        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: "+CONFIG_FILE);
        }finally {
            set.clear();
        }
    }
}
