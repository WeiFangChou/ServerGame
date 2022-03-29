package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigElfSkill {
    public static int AREA_OF_SILENCE_1 = 0;
    public static int AREA_OF_SILENCE_2 = 0;
    public static int AREA_OF_SILENCE_3 = 0;
    public static double AREA_OF_SILENCE_INT = 0.0d;
    public static double AREA_OF_SILENCE_MR = 0.0d;
    private static final String CONFIG_FILE = "./config/【妖精】技能設置.properties";
    public static int EARTH_BIND_1;
    public static int EARTH_BIND_2;
    public static int EARTH_BIND_3;
    public static double EARTH_BIND_INT;
    public static double EARTH_BIND_MR;
    public static int ENTANGLE_1;
    public static int ENTANGLE_2;
    public static int ENTANGLE_3;
    public static double ENTANGLE_INT;
    public static double ENTANGLE_MR;
    public static int ERASE_MAGIC_1;
    public static int ERASE_MAGIC_2;
    public static int ERASE_MAGIC_3;
    public static double ERASE_MAGIC_INT;
    public static double ERASE_MAGIC_MR;
    public static double POLLUTE_WATER_INT;
    public static double POLLUTE_WATER_MR;
    public static int POLLUTE_WATER_RND_1;
    public static int POLLUTE_WATER_RND_2;
    public static int POLLUTE_WATER_RND_3;
    public static int STRIKER_GALE_1;
    public static int STRIKER_GALE_2;
    public static int STRIKER_GALE_3;
    public static double STRIKER_GALE_INT;
    public static double STRIKER_GALE_MR;
    public static String TRIPLE_ARROW_GFX;
    public static int WIND_SHACKLE_1;
    public static int WIND_SHACKLE_2;
    public static int WIND_SHACKLE_3;
    public static double WIND_SHACKLE_INT;
    public static double WIND_SHACKLE_MR;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(CONFIG_FILE));
            set.load(is);
            is.close();
            STRIKER_GALE_1 = Integer.parseInt(set.getProperty("STRIKER_GALE_1", "5"));
            STRIKER_GALE_2 = Integer.parseInt(set.getProperty("STRIKER_GALE_2", "10"));
            STRIKER_GALE_3 = Integer.parseInt(set.getProperty("STRIKER_GALE_3", "15"));
            STRIKER_GALE_INT = Double.parseDouble(set.getProperty("STRIKER_GALE_INT", "0"));
            STRIKER_GALE_MR = Double.parseDouble(set.getProperty("STRIKER_GALE_MR", "0"));
            POLLUTE_WATER_RND_1 = Integer.parseInt(set.getProperty("POLLUTE_WATER_RND_1", "5"));
            POLLUTE_WATER_RND_2 = Integer.parseInt(set.getProperty("POLLUTE_WATER_RND_2", "10"));
            POLLUTE_WATER_RND_3 = Integer.parseInt(set.getProperty("POLLUTE_WATER_RND_3", "15"));
            POLLUTE_WATER_INT = Double.parseDouble(set.getProperty("POLLUTE_WATER_INT", "0"));
            POLLUTE_WATER_MR = Double.parseDouble(set.getProperty("POLLUTE_WATER_MR", "0"));
            EARTH_BIND_1 = Integer.parseInt(set.getProperty("EARTH_BIND_1", "5"));
            EARTH_BIND_2 = Integer.parseInt(set.getProperty("EARTH_BIND_2", "10"));
            EARTH_BIND_3 = Integer.parseInt(set.getProperty("EARTH_BIND_3", "15"));
            EARTH_BIND_INT = Double.parseDouble(set.getProperty("EARTH_BIND_INT", "0"));
            EARTH_BIND_MR = Double.parseDouble(set.getProperty("EARTH_BIND_MR", "0"));
            ERASE_MAGIC_1 = Integer.parseInt(set.getProperty("ERASE_MAGIC_1", "5"));
            ERASE_MAGIC_2 = Integer.parseInt(set.getProperty("ERASE_MAGIC_2", "10"));
            ERASE_MAGIC_3 = Integer.parseInt(set.getProperty("ERASE_MAGIC_3", "15"));
            ERASE_MAGIC_INT = Double.parseDouble(set.getProperty("ERASE_MAGIC_INT", "0"));
            ERASE_MAGIC_MR = Double.parseDouble(set.getProperty("ERASE_MAGIC_MR", "0"));
            AREA_OF_SILENCE_1 = Integer.parseInt(set.getProperty("AREA_OF_SILENCE_1", "5"));
            AREA_OF_SILENCE_2 = Integer.parseInt(set.getProperty("AREA_OF_SILENCE_2", "10"));
            AREA_OF_SILENCE_3 = Integer.parseInt(set.getProperty("AREA_OF_SILENCE_3", "15"));
            AREA_OF_SILENCE_INT = Double.parseDouble(set.getProperty("AREA_OF_SILENCE_INT", "0"));
            AREA_OF_SILENCE_MR = Double.parseDouble(set.getProperty("AREA_OF_SILENCE_MR", "0"));
            ENTANGLE_1 = Integer.parseInt(set.getProperty("ENTANGLE_1", "5"));
            ENTANGLE_2 = Integer.parseInt(set.getProperty("ENTANGLE_2", "10"));
            ENTANGLE_3 = Integer.parseInt(set.getProperty("ENTANGLE_3", "15"));
            ENTANGLE_INT = Double.parseDouble(set.getProperty("ENTANGLE_INT", "0"));
            ENTANGLE_MR = Double.parseDouble(set.getProperty("ENTANGLE_MR", "0"));
            WIND_SHACKLE_1 = Integer.parseInt(set.getProperty("WIND_SHACKLE_1", "5"));
            WIND_SHACKLE_2 = Integer.parseInt(set.getProperty("WIND_SHACKLE_2", "10"));
            WIND_SHACKLE_3 = Integer.parseInt(set.getProperty("WIND_SHACKLE_3", "15"));
            WIND_SHACKLE_INT = Double.parseDouble(set.getProperty("WIND_SHACKLE_INT", "0"));
            WIND_SHACKLE_MR = Double.parseDouble(set.getProperty("WIND_SHACKLE_MR", "0"));
            TRIPLE_ARROW_GFX = set.getProperty("TRIPLE_ARROW_GFX", "0");

        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: "+CONFIG_FILE);
        }finally {
            set.clear();
        }
    }
}
