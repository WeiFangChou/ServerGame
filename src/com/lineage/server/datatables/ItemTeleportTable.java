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

public class ItemTeleportTable {
    private static ItemTeleportTable _instance;
    private static final Log _log = LogFactory.getLog(ItemTeleportTable.class);
    private static final Map<Integer, int[]> _teleportList = new HashMap();

    public static ItemTeleportTable get() {
        if (_instance == null) {
            _instance = new ItemTeleportTable();
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
            pstm = con.prepareStatement("SELECT * FROM `道具傳送捲軸(專用)`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int item_id = rs.getInt("道具編號");
                int locx = rs.getInt("locx");
                int locy = rs.getInt("locy");
                int mapid = rs.getInt("mapid");
                int time = rs.getInt("time");
                int No = rs.getInt("是否開放");
                _teleportList.put(new Integer(item_id), new int[]{locx, locy, mapid, time, No});
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入傳送捲軸傳送點定義數量: " + _teleportList.size() + "(" + timer.get() + "ms)");
    }

    public int[] getLoc(int item_id) {
        return _teleportList.get(Integer.valueOf(item_id));
    }
}
