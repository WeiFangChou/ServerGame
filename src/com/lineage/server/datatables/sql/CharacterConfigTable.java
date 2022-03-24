package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharacterConfigStorage;
import com.lineage.server.templates.L1Config;
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

public class CharacterConfigTable implements CharacterConfigStorage {
    private static final Map<Integer, L1Config> _configList = new HashMap();
    private static final Log _log = LogFactory.getLog(CharacterConfigTable.class);

    @Override // com.lineage.server.datatables.storage.CharacterConfigStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_config`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("object_id");
                if (CharObjidTable.get().isChar(objid) != null) {
                    L1Config l1Configl = new L1Config();
                    l1Configl.setObjid(objid);
                    l1Configl.setLength(rs.getInt("length"));
                    l1Configl.setData(rs.getBytes("data"));
                    _configList.put(Integer.valueOf(objid), l1Configl);
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
        _log.info("載入人物快速鍵紀錄資料數量: " + _configList.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_config` WHERE `object_id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharacterConfigStorage
    public L1Config get(int objectId) {
        return _configList.get(Integer.valueOf(objectId));
    }

    @Override // com.lineage.server.datatables.storage.CharacterConfigStorage
    public void storeCharacterConfig(int objectId, int length, byte[] data) {
        L1Config configl = new L1Config();
        configl.setObjid(objectId);
        configl.setLength(length);
        configl.setData(data);
        _configList.put(Integer.valueOf(objectId), configl);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `character_config` SET `object_id`=?,`length`=?,`data`=?");
            int i = 0 + 1;
            pstm.setInt(i, configl.getObjid());
            int i2 = i + 1;
            pstm.setInt(i2, configl.getLength());
            pstm.setBytes(i2 + 1, configl.getData());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharacterConfigStorage
    public void updateCharacterConfig(int objectId, int length, byte[] data) {
        L1Config configl = _configList.get(Integer.valueOf(objectId));
        configl.setObjid(objectId);
        configl.setLength(length);
        configl.setData(data);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_config` SET `length`=?,`data`=? WHERE `object_id`=?");
            int i = 0 + 1;
            pstm.setInt(i, configl.getLength());
            int i2 = i + 1;
            pstm.setBytes(i2, configl.getData());
            pstm.setInt(i2 + 1, configl.getObjid());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
