package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigDescs {
    private static final Map<Integer, String> _show_desc = new HashMap();
    private static final String _show_desc_file = "./config/show_desc.txt";

    public static void load() throws ConfigErrorException {
        try {
            InputStream is = new FileInputStream(new File(_show_desc_file));
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
                    if (desc.startsWith("SERVER_NAME")) {
                        Config.SERVERNAME = desc.replaceAll(" ", "").substring(12);
                    } else {
                        _show_desc.put(new Integer(i), desc);
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            throw new ConfigErrorException("設置檔案遺失: ./config/show_desc.txt");
        }
    }

    public static String getShow(int nameid) {
        try {
            return _show_desc.get(new Integer(nameid));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int get_show_size() {
        return _show_desc.size();
    }
}
