package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.StringTokenizer;

public final class ConfigPoly {
    private static final String ALT_SETTINGS_FILE = "./config/變身卡合成設定.properties";
    public static int CONSUME2;
    public static int CONSUME3;
    public static int CONSUME4;
    public static int NeedCount2;
    public static int NeedCount3;
    public static int NeedCount4;
    public static int PolyLIST1;
    public static int PolyLIST2;
    public static int PolyLIST3;
    public static int[] Poly_LIST_1;
    public static int[] Poly_LIST_2;
    public static int[] Poly_LIST_3;
    public static int[] Poly_LIST_4;
    public static int[] Poly_LIST_5;
    public static int[] Poly_LIST_6;

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE));
            set.load(new InputStreamReader(is, "utf-8"));
            is.close();
            Poly_LIST_1 = toIntArray(set.getProperty("Poly_List1", ""), ",");
            Poly_LIST_2 = toIntArray(set.getProperty("Poly_List2", ""), ",");
            Poly_LIST_3 = toIntArray(set.getProperty("Poly_List3", ""), ",");
            Poly_LIST_4 = toIntArray(set.getProperty("Poly_List4", ""), ",");
            Poly_LIST_5 = toIntArray(set.getProperty("Poly_List5", ""), ",");
            Poly_LIST_6 = toIntArray(set.getProperty("Poly_List6", ""), ",");
            CONSUME2 = Integer.parseInt(set.getProperty("Consume2", "50"));
            CONSUME3 = Integer.parseInt(set.getProperty("Consume3", "50"));
            CONSUME4 = Integer.parseInt(set.getProperty("Consume4", "50"));
            PolyLIST1 = Integer.parseInt(set.getProperty("item_Id_1", "50"));
            PolyLIST2 = Integer.parseInt(set.getProperty("item_Id_2", "50"));
            PolyLIST3 = Integer.parseInt(set.getProperty("item_Id_3", "50"));
            NeedCount2 = Integer.parseInt(set.getProperty("Count2", "50"));
            NeedCount3 = Integer.parseInt(set.getProperty("Count3", "50"));
            NeedCount4 = Integer.parseInt(set.getProperty("Count4", "50"));
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/變身卡合成設定.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
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
