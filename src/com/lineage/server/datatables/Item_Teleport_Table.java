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

public class Item_Teleport_Table {
    private static Item_Teleport_Table _instance;
    private static final Log _log = LogFactory.getLog(Item_Teleport_Table.class);
    private static final Map<Integer, int[]> _teleportList = new HashMap();

    public static Item_Teleport_Table get() {
        if (_instance == null) {
            _instance = new Item_Teleport_Table();
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
            pstm = con.prepareStatement("SELECT * FROM `道具傳送符(專用)`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int item_id = rs.getInt("道具編號");
                int locx = rs.getInt("locx");
                int locy = rs.getInt("locy");
                int mapid = rs.getInt("mapid");
                int time = rs.getInt("time");
                _teleportList.put(new Integer(item_id), new int[]{locx, locy, mapid, time});
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入傳送符傳送點定義數量: " + _teleportList.size() + "(" + timer.get() + "ms)");
    }

    public int[] getLoc(int item_id) {
        return _teleportList.get(Integer.valueOf(item_id));
    }
}
