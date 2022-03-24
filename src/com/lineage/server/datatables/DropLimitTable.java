package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DropLimitTable {
    private static final HashMap<Integer, int[]> _drop_limit_List = new HashMap<>();
    private static DropLimitTable _instance;
    private static final Log _log = LogFactory.getLog(DropLimitTable.class);
    private static int _restart;

    public static DropLimitTable get() {
        if (_instance == null) {
            _instance = new DropLimitTable();
        }
        return _instance;
    }

    private DropLimitTable() {
        cieanCount();
        load();
    }

    private static Calendar timestampToCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    private void load() {
        int xh;
        PerformanceTimer timer = new PerformanceTimer();
        if (Config.AUTORESTART != null) {
            Calendar cals = timestampToCalendar();
            int HHi = Integer.parseInt(new SimpleDateFormat("HH").format(cals.getTime()));
            int mmi = Integer.parseInt(new SimpleDateFormat("mm").format(cals.getTime()));
            ArrayList<Calendar> _restartList = new ArrayList<>();
            Calendar restart = null;
            int i = 0;
            String[] strArr = Config.AUTORESTART;
            int length = strArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                String[] hm_b = strArr[i2].split(":");
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
                if (restart == null) {
                    restart = tmpCal;
                } else {
                    if (tmpCal.before(restart)) {
                        restart = tmpCal;
                    }
                    i++;
                }
            }
            _restart = i + 1;
        }
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `drop_limit` WHERE `restart`=?");
            ps.setInt(1, _restart);
            rs = ps.executeQuery();
            while (rs.next()) {
                int item_id = rs.getInt("item_id");
                _drop_limit_List.put(Integer.valueOf(item_id), new int[]{rs.getInt("count_max"), 0});
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入掉落物品限制資料數量: " + _drop_limit_List.size() + "(" + timer.get() + "ms)");
    }

    public boolean checkItemId(int item_id, long count) {
        if (_drop_limit_List.isEmpty() || !_drop_limit_List.containsKey(Integer.valueOf(item_id))) {
            return false;
        }
        int c = (int) count;
        int[] data = _drop_limit_List.get(Integer.valueOf(item_id));
        if (data[0] < data[1] + c) {
            return true;
        }
        data[1] = data[1] + c;
        _drop_limit_List.put(Integer.valueOf(item_id), data);
        updateCount(item_id, data[1]);
        return false;
    }

    private void updateCount(int item_id, int count) {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("UPDATE `drop_limit` SET `count_add`=? WHERE `restart`=? AND `item_id`=?");
            pm.setInt(1, count);
            pm.setInt(2, _restart);
            pm.setInt(3, item_id);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }

    private void cieanCount() {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("UPDATE `drop_limit` SET `count_add`= 0 WHERE `count_add`>0");
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }
}
