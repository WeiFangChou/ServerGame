package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigKnightSkill {
    private static final String CONFIG_FILE = "./config/【騎士】技能設置.properties";
    public static int ImpactHalo1;
    public static int ImpactHalo2;
    public static int ImpactHalo3;
    public static int[] SHOCK_STUN_TIMER;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(CONFIG_FILE));
            set.load(is);
            is.close();
            String[] s = set.getProperty("SHOCK_STUN_TIMER", "").split(",");
            SHOCK_STUN_TIMER = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                SHOCK_STUN_TIMER[i] = Integer.parseInt(s[i]);
            }
            ImpactHalo1 = Integer.parseInt(set.getProperty("Impact_Halo_1", "1"));
            ImpactHalo2 = Integer.parseInt(set.getProperty("Impact_Halo_2", "1"));
            ImpactHalo3 = Integer.parseInt(set.getProperty("Impact_Halo_3", "1"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/【騎士】技能設置.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
        }
    }
}
