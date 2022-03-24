package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigKill {
    public static boolean KILLLEVEL = false;
    public static final Map<Integer, String> KILL_TEXT_LIST = new HashMap();
    private static final String _kill_text = "./config/kill_desc.txt";
    private static final Log _log = LogFactory.getLog(ConfigKill.class);

    public static void load() throws ConfigErrorException {
        try {
            InputStream is = new FileInputStream(new File(_kill_text));
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            LineNumberReader lnr = new LineNumberReader(isr);
            boolean isWhile = false;
            int i = 1;
            while (true) {
                String desc = lnr.readLine();
                if (desc == null) {
                    is.close();
                    isr.close();
                    lnr.close();
                    return;
                } else if (!isWhile) {
                    isWhile = true;
                } else if (desc.trim().length() != 0 && !desc.startsWith("#")) {
                    if (desc.startsWith("KILLLEVEL")) {
                        KILLLEVEL = Boolean.parseBoolean(desc.replaceAll(" ", "").substring(10));
                    } else {
                        KILL_TEXT_LIST.put(new Integer(i), desc);
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            _log.error("設置檔案遺失: ./config/kill_desc.txt");
        }
    }
}
