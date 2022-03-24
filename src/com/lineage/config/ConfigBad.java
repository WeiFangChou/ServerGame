package com.lineage.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigBad {
    public static final ArrayList<String> BAD_TEXT_LIST = new ArrayList<>();
    private static final String _bad_text = "./data/badtext.txt";
    private static final Log _log = LogFactory.getLog(ConfigBad.class);

    public static void load() throws ConfigErrorException {
        try {
            InputStream is = new FileInputStream(new File(_bad_text));
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            LineNumberReader lnr = new LineNumberReader(isr);
            boolean isWhile = false;
            while (true) {
                String desc = lnr.readLine();
                if (desc == null) {
                    is.close();
                    isr.close();
                    lnr.close();
                    return;
                } else if (!isWhile) {
                    isWhile = true;
                } else if (desc.trim().length() != 0 && !desc.startsWith("#") && !BAD_TEXT_LIST.contains(desc)) {
                    BAD_TEXT_LIST.add(desc);
                }
            }
        } catch (Exception e) {
            _log.error("設置檔案遺失: ./data/badtext.txt");
        }
    }
}
