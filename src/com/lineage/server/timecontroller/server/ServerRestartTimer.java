package com.lineage.server.timecontroller.server;

import com.lineage.config.Config;
import com.lineage.server.Shutdown;
import com.lineage.server.thread.GeneralThreadPool;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerRestartTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerRestartTimer.class);
    private static Calendar _restart = null;
    private static final ArrayList<Calendar> _restartList = new ArrayList<>();
    private static String _restartTime = null;
    private static String _startTime = null;
    private static String _string = "yyyy/MM/dd HH:mm:ss";
    private ScheduledFuture<?> _timer;

    public static String get_restartTime() {
        return _restartTime;
    }

    public static String get_startTime() {
        return _startTime;
    }

    public static boolean isRtartTime() {
        if (_restart != null && _restart.getTimeInMillis() - System.currentTimeMillis() <= 600000) {
            return true;
        }
        return false;
    }

    private static Calendar timestampToCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    public void start() {
        int xh;
        if (Config.AUTORESTART != null) {
            Calendar cals = timestampToCalendar();
            if (_startTime == null) {
                _startTime = new SimpleDateFormat(_string).format(cals.getTime());
            }
            if (Config.AUTORESTART != null) {
                int HHi = Integer.parseInt(new SimpleDateFormat("HH").format(cals.getTime()));
                int mmi = Integer.parseInt(new SimpleDateFormat("mm").format(cals.getTime()));
                for (String hm : Config.AUTORESTART) {
                    String[] hm_b = hm.split(":");
                    String hh_b = hm_b[0];
                    String mm_b = hm_b[1];
                    int newHH = Integer.parseInt(hh_b);
                    int newMM = Integer.parseInt(mm_b);
                    Calendar cal = timestampToCalendar();
                    int xhh = newHH - HHi;
                    if (xhh > 0) {
                        xh = xhh;
                    } else {
                        xh = (24 - HHi) + newHH;
                    }
                    cal.add(10, xh);
                    cal.add(12, newMM - mmi);
                    _restartList.add(cal);
                }
                Iterator<Calendar> it = _restartList.iterator();
                while (it.hasNext()) {
                    Calendar tmpCal = it.next();
                    if (_restart == null) {
                        _restart = tmpCal;
                    } else if (tmpCal.before(_restart)) {
                        _restart = tmpCal;
                    }
                }
            }
            _restartTime = new SimpleDateFormat(_string).format(_restart.getTime());
            _log.warn("\n\r--------------------------------------------------\n\r       開機完成時間為:" + _startTime + "\n\r       設置關機時間為:" + _restartTime + "\n\r--------------------------------------------------");
            this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
        }
    }

    public void run() {
        try {
            startCommand();
        } catch (Exception e) {
            _log.error("自動重啟時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerRestartTimer().start();
        }
    }

    private void startCommand() {
        if (Config.AUTORESTART != null) {
            Calendar cals = Calendar.getInstance();
            cals.setTimeInMillis(System.currentTimeMillis());
            if (_restart.before(cals)) {
                Shutdown.getInstance().startShutdown(null, 300, true);
            }
        }
    }
}
