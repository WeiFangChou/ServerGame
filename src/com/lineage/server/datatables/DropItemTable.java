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

public final class DropItemTable {
    private static final Map<Integer, DropItemData> _dropItem = new HashMap();
    private static DropItemTable _instance;
    private static final Log _log = LogFactory.getLog(DropItemTable.class);

    /* access modifiers changed from: private */
    public class DropItemData {
        public double dropAmount;
        public double dropRate;

        private DropItemData() {
            this.dropRate = 1.0d;
            this.dropAmount = 1.0d;
        }

        /* synthetic */ DropItemData(DropItemTable dropItemTable, DropItemData dropItemData) {
            this();
        }
    }

    public static DropItemTable get() {
        if (_instance == null) {
            _instance = new DropItemTable();
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
            pstm = con.prepareStatement("SELECT * FROM `drop_item`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int itemid = rs.getInt("item_id");
                if (ItemTable.get().getTemplate(itemid) == null) {
                    _log.error("掉落物品機率資料錯誤: 沒有這個編號的道具:" + itemid);
                    errorItem(itemid);
                } else {
                    DropItemData data = new DropItemData(this, null);
                    data.dropRate = rs.getDouble("drop_rate");
                    data.dropAmount = rs.getDouble("drop_amount");
                    _dropItem.put(new Integer(itemid), data);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入掉落物品機率資料數量: " + _dropItem.size() + "(" + timer.get() + "ms)");
    }

    private static void errorItem(int itemid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `drop_item` WHERE `item_id`=?");
            pstm.setInt(1, itemid);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public double getDropRate(int itemId) {
        DropItemData data = _dropItem.get(Integer.valueOf(itemId));
        if (data == null) {
            return 1.0d;
        }
        return data.dropRate;
    }

    public double getDropAmount(int itemId) {
        DropItemData data = _dropItem.get(Integer.valueOf(itemId));
        if (data == null) {
            return 1.0d;
        }
        return data.dropAmount;
    }
}
