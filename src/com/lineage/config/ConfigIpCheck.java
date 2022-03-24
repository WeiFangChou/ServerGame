package com.lineage.config;

import com.lineage.commons.system.LanSecurityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigIpCheck {
    public static int COUNT = 0;
    public static boolean IPCHECK = false;
    public static boolean IPCHECKPACK = false;
    public static boolean ISONEIP = false;
    public static int ONETIMEMILLIS = 0;
    public static boolean SETDB = false;
    public static int TIMEMILLIS = 0;
    public static boolean UFW = false;
    private static final String _ipcheck = "./config/ipcheck.properties";

    public static void load() throws ConfigErrorException {
        Properties set = new Properties();
        try {
            InputStream is = new FileInputStream(new File(_ipcheck));
            set.load(is);
            is.close();
            IPCHECKPACK = Boolean.parseBoolean(set.getProperty("IPCHECKPACK", "false"));
            if (IPCHECKPACK) {
                new LanSecurityManager().stsrt_cmd_tmp();
            }
            IPCHECK = Boolean.parseBoolean(set.getProperty("IPCHECK", "false"));
            TIMEMILLIS = Integer.parseInt(set.getProperty("TIMEMILLIS", "1000"));
            COUNT = Integer.parseInt(set.getProperty("COUNT", "10"));
            SETDB = Boolean.parseBoolean(set.getProperty("SETDB", "false"));
            UFW = Boolean.parseBoolean(set.getProperty("UFW", "true"));
            ISONEIP = Boolean.parseBoolean(set.getProperty("ISONEIP", "false"));
            ONETIMEMILLIS = Integer.parseInt(set.getProperty("ONETIMEMILLIS", "20000"));
            if (ONETIMEMILLIS != 0) {
                new LanSecurityManager().stsrt_cmd();
            }
            set.clear();
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/ipcheck.properties");
        } catch (Throwable th) {
            set.clear();
            throw th;
        }
    }
}
