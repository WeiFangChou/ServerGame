package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactoryLogin;
import com.lineage.config.Config;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.storage.ServerStorage;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerTable implements ServerStorage {
    private static final Log _log = LogFactory.getLog(ServerTable.class);
    private static int _srcminId = 10000;
    private int _maxId;
    private int _minId;

    @Override // com.lineage.server.datatables.storage.ServerStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `server_info`");
            rs = ps.executeQuery();
            boolean isInfo = false;
            while (rs.next()) {
                if (Config.SERVERNO == rs.getInt("id")) {
                    isInfo = true;
                    int minid = rs.getInt("minid");
                    int maxid = rs.getInt("maxid");
                    this._minId = minid;
                    if (this._minId < _srcminId) {
                        this._minId = _srcminId;
                    }
                    this._maxId = maxid;
                    set_start();
                }
            }
            if (!isInfo) {
                createServer();
                this._minId = _srcminId;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("載入服務器存檔資料完成  (" + timer.get() + "ms)");
    }

    private static void createServer() {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `server_info` SET `id`=?,`minid`=?,`maxid`=?,`start`=?");
            int i = 0 + 1;
            ps.setInt(i, Config.SERVERNO);
            int i2 = i + 1;
            ps.setInt(i2, _srcminId);
            int i3 = i2 + 1;
            ps.setInt(i3, 0);
            ps.setBoolean(i3 + 1, true);
            ps.execute();
            _log.info("新服務器資料表建立 - 編號:" + Config.SERVERNO);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void set_start() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `server_info` SET `start`=? WHERE `id`=?");
            int i = 0 + 1;
            pstm.setBoolean(i, true);
            pstm.setInt(i + 1, Config.SERVERNO);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.ServerStorage
    public void isStop() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `server_info` SET `maxid`=?,`start`=? WHERE `id`=?");
            int i = 0 + 1;
            pstm.setInt(i, IdFactory.get().maxId());
            int i2 = i + 1;
            pstm.setBoolean(i2, false);
            pstm.setInt(i2 + 1, Config.SERVERNO);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.ServerStorage
    public int minId() {
        return this._minId;
    }

    @Override // com.lineage.server.datatables.storage.ServerStorage
    public int maxId() {
        return this._maxId;
    }
}
