package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ItemTime;
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

public class ItemTimeTable {
    public static final Map<Integer, L1ItemTime> TIME = new HashMap();
    private static ItemTimeTable _instance;
    private static final Log _log = LogFactory.getLog(ItemTimeTable.class);

    public static ItemTimeTable get() {
        if (_instance == null) {
            _instance = new ItemTimeTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_item_time`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int key = rs.getInt("itemid");
                TIME.put(Integer.valueOf(key), new L1ItemTime(rs.getInt("remain_time")));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入物品可用時間限制: " + TIME.size() + "(" + timer.get() + "ms)");
    }
}
