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

public class Item_Poly_Table {
    private static final ArrayList<Integer> _idList = new ArrayList<>();
    private static Item_Poly_Table _instance;
    private static final Log _log = LogFactory.getLog(Item_Poly_Table.class);

    public static Item_Poly_Table get() {
        if (_instance == null) {
            _instance = new Item_Poly_Table();
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
            pstm = con.prepareStatement("SELECT * FROM `變身合成公告系統`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int item_id = rs.getInt("道具編號");
                if (!_idList.contains(Integer.valueOf(item_id))) {
                    _idList.add(Integer.valueOf(item_id));
                }
                if (!rs.getString("名稱").contains("=>")) {
                    updata_name(item_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入變身合成公告數量: " + _idList.size() + "(" + timer.get() + "ms)");
    }

    private static void updata_name(int item_id) {
        Connection cn = null;
        PreparedStatement ps = null;
        String itemname = ItemTable.get().getTemplate(item_id).getName();
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `變身合成公告系統` SET `名稱`=? WHERE `道具編號`=?");
            int i = 0 + 1;
            ps.setString(i, "公告->" + itemname);
            ps.setInt(i + 1, item_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public boolean contains(int item_id) {
        return _idList.contains(Integer.valueOf(item_id));
    }
}
