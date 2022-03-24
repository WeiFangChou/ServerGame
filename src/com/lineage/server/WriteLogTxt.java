package com.lineage.server;

import com.lineage.server.model.TimeInform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WriteLogTxt {
    private static final Log _log = LogFactory.getLog(WriteLogTxt.class);

    public static void Recording(String name, String info) {
        try {
            String date = " " + new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String path = "記錄存檔\\記錄" + date;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            FileOutputStream fos = new FileOutputStream(String.valueOf(path) + "\\" + name + date + ".txt", true);
            fos.write((String.valueOf(info) + " 時間：" + TimeInform.getNowTime(3, 0) + "\r\n").getBytes());
            fos.close();
        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
