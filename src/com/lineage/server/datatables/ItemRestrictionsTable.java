package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemRestrictionsTable {
    public static final ArrayList<Integer> RESTRICTIONS = new ArrayList<>();
    private static ItemRestrictionsTable _instance;
    private static final Log _log = LogFactory.getLog(ItemRestrictionsTable.class);

    public static ItemRestrictionsTable get() {
        if (_instance == null) {
            _instance = new ItemRestrictionsTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_item_restrictions`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                RESTRICTIONS.add(Integer.valueOf(rs.getInt("itemid")));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入交易限制道具: " + RESTRICTIONS.size() + "(" + timer.get() + "ms)");
    }
}
