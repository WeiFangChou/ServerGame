package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TownSetTable {
    private static TownSetTable _instance;
    private static final Log _log = LogFactory.getLog(TownSetTable.class);
    private static final Map<Integer, Integer> _townnpclist = new HashMap();

    public static TownSetTable get() {
        if (_instance == null) {
            _instance = new TownSetTable();
        }
        return _instance;
    }

    public void reload() {
        _townnpclist.clear();
        load();
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM npctown");
            rs = ps.executeQuery();
            while (rs.next()) {
                int npcId = rs.getInt("npcid");
                _townnpclist.put(new Integer(npcId), Integer.valueOf(rs.getInt("townid")));
                if (!rs.getString("名稱").contains("=>")) {
                    updata_name(npcId);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入NPC村莊資料數量: " + _townnpclist.size() + "(" + timer.get() + "ms)");
    }

    private static void updata_name(int npcId) {
        Connection cn = null;
        PreparedStatement ps = null;
        String npcname = NpcTable.get().getNpcName(npcId);
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `npctown` SET `名稱`=? WHERE `npcid`=?");
            int i = 0 + 1;
            ps.setString(i, "名稱->" + npcname);
            ps.setInt(i + 1, npcId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public int getTownid(int npcid) {
        if (_townnpclist.containsKey(Integer.valueOf(npcid))) {
            return _townnpclist.get(Integer.valueOf(npcid)).intValue();
        }
        return 14;
    }
}
