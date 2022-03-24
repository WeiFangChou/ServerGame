package com.lineage.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OutErrorMsg {
    private static final Log _log = LogFactory.getLog(OutErrorMsg.class);

    public static void put(String className, String string, Throwable t) {
        _log.error(string);
        StringBuilder putInfo = new StringBuilder();
        putInfo.append(String.valueOf(string) + "###");
        StackTraceElement[] locations = t.getStackTrace();
        int length = locations.length;
        for (int i = 0; i < length; i++) {
            putInfo.append("   " + locations[i].toString() + "###");
        }
        overOut(className, putInfo);
    }

    public static void put(int oid, String string) {
        _log.error(string);
        StringBuilder putInfo = new StringBuilder();
        putInfo.append(String.valueOf(string) + "/" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        overOut(String.valueOf(oid), putInfo);
    }

    private static void overOut(String name, StringBuilder string) {
        try {
            File file = new File("./" + name + ".txt");
            file.createNewFile();
            OutputStreamWriter printWriter = new OutputStreamWriter(new FileOutputStream(file, true), "utf-8");
            for (String txt : string.toString().split("###")) {
                printWriter.write(txt);
                printWriter.write("\r\n");
            }
            printWriter.write("\r\n");
            printWriter.flush();
            printWriter.close();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
