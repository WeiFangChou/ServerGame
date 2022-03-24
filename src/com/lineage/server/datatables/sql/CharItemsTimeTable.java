package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CharItemsTimeStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharItemsTimeTable implements CharItemsTimeStorage {
    private static final Log _log = LogFactory.getLog(CharBookTable.class);

    @Override // com.lineage.server.datatables.storage.CharItemsTimeStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int size = 0;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_items_time`");
            rs = ps.executeQuery();
            while (rs.next()) {
                addValue(rs.getInt("itemr_obj_id"), rs.getTimestamp("usertime"));
                size++;
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入物品使用期限資料數量: " + size + "(" + timer.get() + "ms)");
    }

    private static void addValue(int itemr_obj_id, Timestamp usertime) {
        L1Object obj = World.get().findObject(itemr_obj_id);
        boolean isError = true;
        if (obj != null && (obj instanceof L1ItemInstance)) {
            ((L1ItemInstance) obj).set_time(usertime);
            isError = false;
        }
        if (isError) {
            delete(itemr_obj_id);
        }
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_items_time` WHERE `itemr_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsTimeStorage
    public void addTime(int itemr_obj_id, Timestamp usertime) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_items_time` SET `itemr_obj_id`=?,`usertime`=?");
            int i = 0 + 1;
            ps.setInt(i, itemr_obj_id);
            ps.setTimestamp(i + 1, usertime);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharItemsTimeStorage
    public void updateTime(int itemr_obj_id, Timestamp usertime) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("UPDATE `character_items_time` SET `usertime`=? WHERE `itemr_obj_id`=?");
            int i = 0 + 1;
            ps.setTimestamp(i, usertime);
            ps.setInt(i + 1, itemr_obj_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
