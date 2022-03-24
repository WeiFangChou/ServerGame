package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Trap;
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

public class TrapTable {
    private static TrapTable _instance;
    private static final Log _log = LogFactory.getLog(TrapTable.class);
    private static final Map<Integer, L1Trap> _traps = new HashMap();

    public static TrapTable get() {
        if (_instance == null) {
            _instance = new TrapTable();
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
            pstm = con.prepareStatement("SELECT * FROM `trap`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1Trap trap = new L1Trap(rs);
                _traps.put(Integer.valueOf(trap.getId()), trap);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入陷阱資料數量: " + _traps.size() + "(" + timer.get() + "ms)");
    }

    public static void reload() {
        _instance = new TrapTable();
        _traps.clear();
        _instance.load();
    }

    public L1Trap getTemplate(int id) {
        return _traps.get(Integer.valueOf(id));
    }
}
