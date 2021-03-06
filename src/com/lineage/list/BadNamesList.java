package com.lineage.list;

import com.lineage.server.utils.StreamUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BadNamesList {
    private static BadNamesList _instance;
    private static final Log _log = LogFactory.getLog(BadNamesList.class);
    private ArrayList<String> _nameList = new ArrayList<>();

    public static BadNamesList get() {
        if (_instance == null) {
            _instance = new BadNamesList();
        }
        return _instance;
    }

    public void load() throws Exception {
        LineNumberReader lnr = null;
        try {
            LineNumberReader lnr2 = new LineNumberReader(new InputStreamReader(new FileInputStream(new File("data/badnames.txt")), "utf8"));
            boolean isWhile = false;
            while (true) {
                    String line = lnr2.readLine();
                    if (line == null) {
                        _log.info("載入禁止名稱數量: " + this._nameList.size());
                        StreamUtil.close(lnr2);
                        return;
                    } else if (!isWhile) {
                        isWhile = true;
                    } else if (line.trim().length() != 0 && !line.startsWith("#")) {
                        this._nameList.add(new StringTokenizer(line, ";").nextToken());
                    }
            }
        } catch (FileNotFoundException e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }finally {
            StreamUtil.close(lnr);
        }
    }

    public boolean isBadName(String name) {
        String checkName = name.toLowerCase();
        Iterator<String> it = this._nameList.iterator();
        while (it.hasNext()) {
            if (checkName.contains(it.next().toLowerCase())) {
                _log.info("新建人物名稱包含禁用字元: " + this._nameList.size());
                return true;
            }
        }
        return false;
    }

    public String[] getAllBadNames() {
        return (String[]) this._nameList.toArray(new String[this._nameList.size()]);
    }
}
