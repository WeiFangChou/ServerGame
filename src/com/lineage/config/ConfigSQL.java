package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigSQL {
    public static String DB_DRIVER = null;
    public static String DB_LOGIN = null;
    public static String DB_LOGIN_LOGIN = null;
    public static String DB_PASSWORD = null;
    public static String DB_PASSWORD_LOGIN = null;
    public static String DB_URL1 = null;
    public static String DB_URL1_LOGIN = null;
    public static String DB_URL2 = null;
    public static String DB_URL2_LOGIN = null;
    public static String DB_URL3 = null;
    public static String DB_URL3_LOGIN = null;
    private static final String SQL_CONFIG = "./config/sql.properties";

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            File SQLFile = new File(SQL_CONFIG);

            InputStream is = new FileInputStream(SQLFile);

            set.load(is);
            is.close();

            DB_DRIVER = set.getProperty("Driver", "com.mysql.jdbc.Driver");
            DB_URL1_LOGIN = set.getProperty("URL1_LOGIN", "jdbc:mysql://localhost:3306/");
            DB_URL2_LOGIN = set.getProperty("URL2_LOGIN", "l1jsrc");
            DB_URL3_LOGIN = set.getProperty("URL3_LOGIN", "?useUnicode=true&characterEncoding=UTF8");
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //jdbc:mysql://localhost:3306/l1jsrc
            DB_LOGIN_LOGIN = set.getProperty("Login_LOGIN", "lineage");
            DB_PASSWORD_LOGIN = set.getProperty("Password_LOGIN", "lineage2022");
            DB_URL1 = set.getProperty("URL1", "jdbc:mysql://localhost:3306/");
            DB_URL2 = set.getProperty("URL2", "l1jsrc");
            DB_URL3 = set.getProperty("URL3", "?useUnicode=true&characterEncoding=UTF8");
            DB_LOGIN = set.getProperty("Login", "lineage");
            DB_PASSWORD = set.getProperty("Password", "lineage2022");

        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: "+SQL_CONFIG );
        }finally {
            set.clear();
        }
    }
}
