package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigDarkElfSkill {
    public static int ARMOR_BRAKE0 = 0;
    public static int ARMOR_BRAKE1 = 0;
    public static int ARMOR_BRAKE2 = 0;
    public static int ARMOR_BRAKE3 = 0;
    public static double ARMOR_BRAKE_DMG = 0.0d;
    private static final String ConfigSkillDarkElf = "./config/【黑暗妖精】技能設置.properties";

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(ConfigSkillDarkElf));
            set.load(is);
            is.close();
            ARMOR_BRAKE0 = Integer.parseInt(set.getProperty("ARMOR_BRAKE0", "10"));
            ARMOR_BRAKE1 = Integer.parseInt(set.getProperty("ARMOR_BRAKE1", "10"));
            ARMOR_BRAKE2 = Integer.parseInt(set.getProperty("ARMOR_BRAKE2", "10"));
            ARMOR_BRAKE3 = Integer.parseInt(set.getProperty("ARMOR_BRAKE3", "10"));
            ARMOR_BRAKE_DMG = Double.parseDouble(set.getProperty("ARMOR_BRAKE_DMG", "45"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/【黑暗妖精】技能設置.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
        }
    }
}
