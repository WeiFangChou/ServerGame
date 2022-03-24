package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharBookConfigStorage;
import com.lineage.server.templates.L1BookConfig;
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

public class CharBookConfigTable implements CharBookConfigStorage {
    private static final Map<Integer, L1BookConfig> _configList = new HashMap();
    private static final Map<Integer, L1BookConfig> _configList2 = new HashMap();
    private static final Log _log = LogFactory.getLog(CharBookConfigTable.class);

    @Override // com.lineage.server.datatables.storage.CharBookConfigStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_bookconfig`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("object_id");
                if (CharObjidTable.get().isChar(objid) != null) {
                    L1BookConfig l1BookConfigl = new L1BookConfig();
                    l1BookConfigl.setObjid(objid);
                    l1BookConfigl.setData(rs.getBytes("data"));
                    _configList.put(Integer.valueOf(objid), l1BookConfigl);
                } else {
                    delete(objid);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入人物記憶座標設置紀錄資料數量: " + _configList.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_bookconfig` WHERE `object_id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharBookConfigStorage
    public L1BookConfig get(int objectId) {
        return _configList.get(Integer.valueOf(objectId));
    }

    @Override // com.lineage.server.datatables.storage.CharBookConfigStorage
    public void storeCharacterBookConfig(int objectId, byte[] data) {
        L1BookConfig configl = new L1BookConfig();
        configl.setObjid(objectId);
        configl.setData(data);
        _configList.put(Integer.valueOf(objectId), configl);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `character_bookconfig` SET `object_id`=?,`data`=?");
            int i = 0 + 1;
            pstm.setInt(i, configl.getObjid());
            pstm.setBytes(i + 1, configl.getData());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharBookConfigStorage
    public void updateCharacterConfig(int objectId, byte[] data) {
        L1BookConfig configl = _configList.get(Integer.valueOf(objectId));
        configl.setObjid(objectId);
        configl.setData(data);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_bookconfig` SET `data`=? WHERE `object_id`=?");
            int i = 0 + 1;
            pstm.setBytes(i, configl.getData());
            pstm.setInt(i + 1, configl.getObjid());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static Map<Integer, L1BookConfig> getBookConfig(boolean local) {
        if (local) {
            return _configList;
        }
        return _configList2;
    }
}
