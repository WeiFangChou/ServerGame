package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigCharSetting {
    private static final String CHAR_SETTINGS_CONFIG_FILE = "./config/charsettings.properties";
    public static int DARKELF_MAX_HP;
    public static int DARKELF_MAX_MP;
    public static int DRAGONKNIGHT_MAX_HP;
    public static int DRAGONKNIGHT_MAX_MP;
    public static int ELF_MAX_HP;
    public static int ELF_MAX_MP;
    public static int ILLUSIONIST_MAX_HP;
    public static int ILLUSIONIST_MAX_MP;
    public static int KNIGHT_MAX_HP;
    public static int KNIGHT_MAX_MP;
    public static int PRINCE_MAX_HP;
    public static int PRINCE_MAX_MP;
    public static int WIZARD_MAX_HP;
    public static int WIZARD_MAX_MP;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(CHAR_SETTINGS_CONFIG_FILE));
            set.load(is);
            is.close();
            PRINCE_MAX_HP = Integer.parseInt(set.getProperty("PrinceMaxHP", "1000"));
            PRINCE_MAX_MP = Integer.parseInt(set.getProperty("PrinceMaxMP", "800"));
            KNIGHT_MAX_HP = Integer.parseInt(set.getProperty("KnightMaxHP", "1400"));
            KNIGHT_MAX_MP = Integer.parseInt(set.getProperty("KnightMaxMP", "600"));
            ELF_MAX_HP = Integer.parseInt(set.getProperty("ElfMaxHP", "1000"));
            ELF_MAX_MP = Integer.parseInt(set.getProperty("ElfMaxMP", "900"));
            WIZARD_MAX_HP = Integer.parseInt(set.getProperty("WizardMaxHP", "800"));
            WIZARD_MAX_MP = Integer.parseInt(set.getProperty("WizardMaxMP", "1200"));
            DARKELF_MAX_HP = Integer.parseInt(set.getProperty("DarkelfMaxHP", "1000"));
            DARKELF_MAX_MP = Integer.parseInt(set.getProperty("DarkelfMaxMP", "900"));
            DRAGONKNIGHT_MAX_HP = Integer.parseInt(set.getProperty("DragonKnightMaxHP", "1400"));
            DRAGONKNIGHT_MAX_MP = Integer.parseInt(set.getProperty("DragonKnightMaxMP", "600"));
            ILLUSIONIST_MAX_HP = Integer.parseInt(set.getProperty("IllusionistMaxHP", "900"));
            ILLUSIONIST_MAX_MP = Integer.parseInt(set.getProperty("IllusionistMaxMP", "1100"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/charsettings.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
        }
    }
}
