package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.DatabaseFactoryLogin;
import com.lineage.config.Config;
import com.lineage.server.datatables.storage.AccountStorage;
import com.lineage.server.templates.L1Account;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AccountTable implements AccountStorage {
    private static final Log _log = LogFactory.getLog(AccountTable.class);
    private final Map<String, String> _loginNameList = new HashMap();

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `accounts`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String login = rs.getString("login").toLowerCase();
                this._loginNameList.put(login, login);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("載入已有帳戶名稱資料數量: " + this._loginNameList.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public boolean isAccountUT(String loginName) {
        return this._loginNameList.get(loginName) != null;
    }

    /* JADX INFO: finally extract failed */
    @Override // com.lineage.server.datatables.storage.AccountStorage
    public L1Account create(String loginName, String pwd, String ip, String host, String spwd) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            _log.error("L1Account Create");
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            L1Account value = new L1Account();
            value.set_login(loginName.toLowerCase());
            value.set_password(pwd);
            value.set_lastactive(lastactive);
            value.set_access_level(0);
            value.set_ip(ip);
            value.set_mac(host);
            value.set_character_slot(0);
            value.set_spw(spwd);
            value.set_warehouse(-256);
            value.set_countCharacters(0);
            value.set_isLoad(false);
            value.set_server_no(0);
            value.set_point(0);
            value.set_pay_first(0);
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `accounts` SET `login`=?,`password`=?,`lastactive`=?,`access_level`=?,`ip`=?,`host`=?,`character_slot`=?,`spw`=?,`server_no`=?,`累積金額`=?,`是否首儲`=?");
            int i = 0;
            ps.setString(++i, value.get_login().toLowerCase());
            ps.setString(++i, value.get_password());
            ps.setTimestamp(++i, value.get_lastactive());
            ps.setInt(++i, 0);
            ps.setString(++i, value.get_ip());
            ps.setString(++i, value.get_mac());
            ps.setInt(++i, 0);
            ps.setString(++i, value.get_spw());
            ps.setInt(++i, value.get_server_no());
            ps.execute();
            _log.info("新帳號建立: " + value.get_login());
            SQLUtil.close(ps);
            SQLUtil.close(cn);
            return value;
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Throwable th) {
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public boolean isAccount(String loginName) {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `accounts` WHERE `login`=?");
            ps.setString(1, loginName.toLowerCase());
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        return false;
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public L1Account getAccount(String loginName) {
        return getAccountInfo(loginName);
    }

    private L1Account getAccountInfo(String loginName) {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `accounts` WHERE `login`=?");
            ps.setString(1, loginName.toLowerCase());
            rs = ps.executeQuery();
            while (rs.next()) {
                String login = rs.getString("login").toLowerCase();
                String password = rs.getString("password");
                Timestamp lastactive = rs.getTimestamp("lastactive");
                int access_level = rs.getInt("access_level");
                String ip = rs.getString("ip");
                String host = rs.getString("host");
                int character_slot = rs.getInt("character_slot");
                String spw = rs.getString("spw");
                int warehouse = rs.getInt("warehouse");
                int server_no = rs.getInt("server_no");
                int point = rs.getInt("累積金額");
                int pay_first = rs.getInt("是否首儲");
                int countCharacters = getPlayers(login);
                L1Account value = new L1Account();
                value.set_login(login);
                value.set_password(password);
                value.set_lastactive(lastactive);
                value.set_access_level(access_level);
                value.set_ip(ip);
                value.set_mac(host);
                value.set_character_slot(character_slot);
                value.set_spw(spw);
                value.set_warehouse(warehouse);
                value.set_countCharacters(countCharacters);
                value.set_server_no(server_no);
                value.set_point(point);
                value.set_pay_first(pay_first);
                return value;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Throwable throwable) {
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        return null;
    }

    private static int getPlayers(String loginName) throws Exception {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `characters` WHERE `account_name`=?");
            ps.setString(1, loginName.toLowerCase());
            rs = ps.executeQuery();
            while (rs.next()) {i++;}
            return i;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        return 0;
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateWarehouse(String loginName, int pwd) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `warehouse`=? WHERE `login`=?");
            pstm.setInt(1, pwd);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateLastActive(L1Account account) {
        Connection con = null;
        PreparedStatement pstm = null;
        try{
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            con =DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `lastactive`=?,`ip`=?,`host`=? WHERE `login`=?");
            pstm.setTimestamp(1, lastactive);
            pstm.setString(2, account.get_ip());
            pstm.setString(3, account.get_mac());
            pstm.setString(4, account.get_login());
            pstm.execute();
        }catch (Exception e){
            _log.error(e.getLocalizedMessage(), e);
        }finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateCharacterSlot(String loginName, int count) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `character_slot`=? WHERE `login`=?");
            pstm.setInt(1, count);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updatePwd(String loginName, String newpwd) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `password`=? WHERE `login`=?");
            pstm.setString(1, newpwd);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateLan(String loginName, boolean islan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `server_no`=? WHERE `login`=?");
            if (islan) {
                pstm.setInt(1, Config.SERVERNO);
            } else {
                pstm.setInt(1, 0);
            }
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateLan() {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `server_no`=0");
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public static void changpassword(String login, String newPassword) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE accounts SET password=? WHERE login=?");
            pstm.setString(1, newPassword);
            pstm.setString(2, login);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void updatefp(String loginName, int fp) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `first_pay`=? WHERE `login`=?");
            pstm.setInt(1, 1);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public int getPoint(String loginName) throws Exception {
        Throwable th;
        Exception e;
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `accounts` WHERE `login`=?");
            ps.setString(1, loginName);
            rs = ps.executeQuery();
            while (rs.next()) {
                try {
                    i = rs.getInt("累積金額");
                } catch (Exception e2) {
                    e = e2;
                    try {
                        _log.error(e.getLocalizedMessage(), e);
                        SQLUtil.close(ps);
                        SQLUtil.close(co);
                        SQLUtil.close(rs);
                        return 0;
                    } catch (Throwable th2) {
                        th = th2;
                        SQLUtil.close(ps);
                        SQLUtil.close(co);
                        SQLUtil.close(rs);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    SQLUtil.close(ps);
                    SQLUtil.close(co);
                    SQLUtil.close(rs);
                    throw th;
                }
            }
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
            return i;
        } catch (Exception e3) {
            e = e3;
            return 0;
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void setPoint(String loginName, int point) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `累積金額`=? WHERE `login`=?");
            pstm.setInt(1, point);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountStorage
    public void updateFirstPay(String loginName, int count) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `accounts` SET `是否首儲`=? WHERE `login`=?");
            pstm.setInt(1, count);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
