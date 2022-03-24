package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.VIPStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VIPTable implements VIPStorage {
    private static final Log _log = LogFactory.getLog(VIPTable.class);
    private static final Map<Integer, Timestamp> _vipMap = new ConcurrentHashMap();

    @Override // com.lineage.server.datatables.storage.VIPStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_vip`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int char_obj_id = rs.getInt("char_obj_id");
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    addMap(char_obj_id, rs.getTimestamp("overtime"));
                } else {
                    delete(char_obj_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入VIP資料數量: " + _vipMap.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_vip` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();
            _vipMap.remove(Integer.valueOf(objid));
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addMap(int objId, Timestamp overtime) {
        if (_vipMap.get(Integer.valueOf(objId)) == null) {
            _vipMap.put(Integer.valueOf(objId), overtime);
        }
    }

    @Override // com.lineage.server.datatables.storage.VIPStorage
    public Map<Integer, Timestamp> map() {
        return _vipMap;
    }

    @Override // com.lineage.server.datatables.storage.VIPStorage
    public Timestamp getOther(L1PcInstance pc) {
        return _vipMap.get(Integer.valueOf(pc.getId()));
    }

    @Override // com.lineage.server.datatables.storage.VIPStorage
    public void storeOther(int key, Timestamp value) {
        if (_vipMap.get(Integer.valueOf(key)) == null) {
            addMap(key, value);
            addNewOther(key, value);
            return;
        }
        updateOther(key, value);
        _vipMap.put(Integer.valueOf(key), value);
    }

    @Override // com.lineage.server.datatables.storage.VIPStorage
    public void delOther(int key) {
        if (_vipMap.remove(Integer.valueOf(key)) != null) {
            delete(key);
        }
    }

    private static void updateOther(int objId, Timestamp other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_vip` SET `overtime`=? WHERE `char_obj_id`=?");
            int i = 0 + 1;
            ps.setTimestamp(i, other);
            ps.setInt(i + 1, objId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addNewOther(int objId, Timestamp other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_vip` SET `char_obj_id`=?,`overtime`=?");
            int i = 0 + 1;
            ps.setInt(i, objId);
            ps.setTimestamp(i + 1, other);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
