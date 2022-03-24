package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactoryLogin;
import com.lineage.commons.system.LanSecurityManager;
import com.lineage.config.Config;
import com.lineage.config.ConfigIpCheck;
import com.lineage.server.datatables.storage.IpStorage;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IpTable implements IpStorage {
    private static final Log _log = LogFactory.getLog(IpTable.class);

    private void ufwDeny(String key) {
        try {
            if (ConfigIpCheck.UFW) {
                BufferedReader input = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("sudo ufw deny from " + key).getInputStream()));
                while (true) {
                    String line = input.readLine();
                    if (line != null) {
                        _log.info("Linux 系統命令執行: 防火牆" + line);
                    } else {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void ufwDeleteDeny(String key) {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("sudo ufw delete deny from " + key).getInputStream()));
            while (true) {
                String line = input.readLine();
                if (line != null) {
                    _log.info("Linux 系統命令執行: 防火牆" + line);
                } else {
                    return;
                }
            }
        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.datatables.storage.IpStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `ban_ip`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String key = rs.getString("ip");
                if (key.lastIndexOf(".") != -1) {
                    if (!LanSecurityManager.BANIPMAP.containsKey(key)) {
                        LanSecurityManager.BANIPMAP.put(key, 100);
                    }
                } else if (!LanSecurityManager.BANNAMEMAP.containsKey(key)) {
                    LanSecurityManager.BANNAMEMAP.put(key, 100);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入禁止登入IP資料數量: " + LanSecurityManager.BANIPMAP.size() + "(" + timer.get() + "ms)");
        _log.info("載入禁止登入NAME資料數量: " + LanSecurityManager.BANNAMEMAP.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.IpStorage
    public void add(String key, String info) {
        boolean isBan = false;
        if (key.lastIndexOf(".") != -1) {
            if (!LanSecurityManager.BANIPMAP.containsKey(key)) {
                LanSecurityManager.BANIPMAP.put(key, 100);
                isBan = true;
                if (Config.ISUBUNTU) {
                    ufwDeny(key);
                }
            }
        } else if (!LanSecurityManager.BANNAMEMAP.containsKey(key)) {
            LanSecurityManager.BANNAMEMAP.put(key, 100);
            isBan = true;
        }
        if (check(key)) {
            isBan = false;
        }
        if (isBan) {
            Connection con = null;
            PreparedStatement pstm = null;
            try {
                con = DatabaseFactoryLogin.get().getConnection();
                pstm = con.prepareStatement("INSERT INTO `ban_ip` SET `ip`=?,`info`=?,`datetime`=SYSDATE()");
                int i = 0 + 1;
                pstm.setString(i, key);
                pstm.setString(i + 1, info);
                pstm.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.IpStorage
    public void remove(String key) {
        boolean isBan = false;
        if (key.lastIndexOf(".") != -1) {
            if (LanSecurityManager.BANIPMAP.containsKey(key)) {
                LanSecurityManager.BANIPMAP.remove(key);
                isBan = true;
                if (Config.ISUBUNTU) {
                    _log.info("******Linux 系統命令執行**************************");
                    ufwDeleteDeny(key);
                    _log.info("******Linux 系統命令完成**************************");
                }
            }
        } else if (LanSecurityManager.BANNAMEMAP.containsKey(key)) {
            LanSecurityManager.BANNAMEMAP.remove(key);
            isBan = true;
        }
        if (!check(key)) {
            isBan = false;
        }
        if (isBan) {
            Connection con = null;
            PreparedStatement pstm = null;
            try {
                con = DatabaseFactoryLogin.get().getConnection();
                pstm = con.prepareStatement("DELETE FROM `ban_ip` WHERE `ip`=?");
                pstm.setString(1, key);
                pstm.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    private boolean check(String key) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `ban_ip` WHERE `ip` LIKE '%" + key + "%'");
            rs = ps.executeQuery();
            if (!rs.next()) {
                return false;
            }
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
            return true;
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
            return false;
        }
    }
}
