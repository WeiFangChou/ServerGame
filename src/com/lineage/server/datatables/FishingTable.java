package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Fishing;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FishingTable {
    private static final Map<Integer, L1Fishing> _fishingMap = new HashMap();
    private static FishingTable _instance;
    public static final Log _log = LogFactory.getLog(FishingTable.class);
    private static Random _random = new Random();

    public static FishingTable get() {
        if (_instance == null) {
            _instance = new FishingTable();
        }
        return _instance;
    }

    private FishingTable() {
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `server_fishing`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("itemid");
                int randomint = rs.getInt("randomint");
                int random = rs.getInt("random");
                int count = rs.getInt("count");
                int bait = rs.getInt("bait");
                if (ItemTable.get().getTemplate(key) == null) {
                    _log.error("漁獲資料錯誤: 沒有這個編號的道具:" + key);
                    delete(key);
                } else if (count > 0) {
                    L1Fishing value = new L1Fishing();
                    value.set_itemid(key);
                    value.set_randomint(randomint);
                    value.set_random(random);
                    value.set_count(count);
                    value.set_bait(bait);
                    _fishingMap.put(Integer.valueOf(key), value);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入漁獲資料數量: " + _fishingMap.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_fishing` WHERE `itemid`=?");
            ps.setInt(1, item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public L1Fishing get_item() {
        try {
            Object[] objs = _fishingMap.values().toArray();
            return (L1Fishing) objs[_random.nextInt(objs.length)];
        } catch (Exception e) {
            return null;
        }
    }
}
