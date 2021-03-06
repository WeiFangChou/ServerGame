package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1GetBackRestart;
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

public class GetBackRestartTable {
    private static final Map<Integer, L1GetBackRestart> _getbackrestart = new HashMap();
    private static GetBackRestartTable _instance;
    private static final Log _log = LogFactory.getLog(GetBackRestartTable.class);

    public static GetBackRestartTable get() {
        if (_instance == null) {
            _instance = new GetBackRestartTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `getback_restart`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1GetBackRestart gbr = new L1GetBackRestart();
                int area = rs.getInt("area");
                gbr.setArea(area);
                gbr.setLocX(rs.getInt("locx"));
                gbr.setLocY(rs.getInt("locy"));
                gbr.setMapId(rs.getShort("mapid"));
                _getbackrestart.put(new Integer(area), gbr);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入回城座標資料數量: " + _getbackrestart.size() + "(" + timer.get() + "ms)");
    }

    public void add(int area, int locx, int locy, int map) {
        if (_getbackrestart.get(new Integer(area)) == null) {
            L1GetBackRestart gbr = new L1GetBackRestart();
            gbr.setArea(area);
            gbr.setLocX(locx);
            gbr.setLocY(locy);
            gbr.setMapId( map);
            _getbackrestart.put(new Integer(area), gbr);
        }
    }

    public L1GetBackRestart getGetBackRestart(int mapid) {
        L1GetBackRestart tmp = _getbackrestart.get(new Integer(mapid));
        if (tmp == null) {
            return null;
        }
        return tmp;
    }

    public L1GetBackRestart[] getGetBackRestartTableList() {
        return (L1GetBackRestart[]) _getbackrestart.values().toArray(new L1GetBackRestart[_getbackrestart.size()]);
    }
}
