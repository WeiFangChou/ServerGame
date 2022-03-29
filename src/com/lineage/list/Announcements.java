package com.lineage.list;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.StreamUtil;
import com.lineage.server.world.World;
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

public class Announcements {
    private static Announcements _instance;
    private static final Log _log = LogFactory.getLog(Announcements.class);
    private final ArrayList<String> _announcements = new ArrayList<>();

    public static Announcements get() {
        if (_instance == null) {
            _instance = new Announcements();
        }
        return _instance;
    }

    public void load() throws Exception {
        LineNumberReader lnr = null;
        try {
            LineNumberReader lnr2 = new LineNumberReader(new InputStreamReader(new FileInputStream(new File("data/announcements.txt")), "utf8"));
            boolean isWhile = false;
            while (true) {
                    String line = lnr2.readLine();
                    if (line == null) {
                        _log.info("載入公告事項數量: " + this._announcements.size());
                        StreamUtil.close(lnr2);
                        return;
                    } else if (!isWhile) {
                        isWhile = true;
                    } else if (line.trim().length() != 0 && !line.startsWith("#")) {
                        this._announcements.add(new StringTokenizer(line, "\r\n").nextToken());
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

    public void showAnnouncements(L1PcInstance showTo) {
        Iterator<String> it = this._announcements.iterator();
        while (it.hasNext()) {
            showTo.sendPackets(new S_SystemMessage(it.next()));
        }
    }

    public void announceToAll(String msg) {
        World.get().broadcastServerMessage(msg);
    }

    public ArrayList<String> list() {
        return this._announcements;
    }
}
