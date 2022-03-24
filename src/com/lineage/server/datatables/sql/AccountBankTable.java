package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.AccountBankStorage;
import com.lineage.server.templates.L1Bank;
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

public class AccountBankTable implements AccountBankStorage {
    private static final Log _log = LogFactory.getLog(AccountBankTable.class);
    private final Map<String, L1Bank> _bankNameList = new HashMap();

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_bank`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String account_name = rs.getString("account_name").toLowerCase();
                long adena_count = rs.getLong("adena_count");
                String pass = rs.getString("pass");
                L1Bank bank = new L1Bank();
                bank.set_account_name(account_name);
                bank.set_adena_count(adena_count);
                bank.set_pass(pass);
                this._bankNameList.put(account_name, bank);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        _log.info("載入已有銀行帳戶資料數量: " + this._bankNameList.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public L1Bank get(String account_name) {
        return this._bankNameList.get(account_name);
    }

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public Map<String, L1Bank> map() {
        return this._bankNameList;
    }

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public void create(String loginName, L1Bank bank) {
        if (this._bankNameList.get(loginName) == null) {
            this._bankNameList.put(loginName, bank);
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                Timestamp lastactive = new Timestamp(System.currentTimeMillis());
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("INSERT INTO `character_bank` SET `account_name`=?,`adena_count`=?,`pass`=?,`settime`=?");
                int i = 0 + 1;
                ps.setString(i, bank.get_account_name());
                int i2 = i + 1;
                ps.setInt(i2, 0);
                int i3 = i2 + 1;
                ps.setString(i3, bank.get_pass());
                ps.setTimestamp(i3 + 1, lastactive);
                ps.execute();
                _log.info("新銀行帳號建立: " + bank.get_account_name());
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public void updatePass(String loginName, String pwd) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_bank` SET `pass`=? WHERE `account_name`=?");
            pstm.setString(1, pwd);
            pstm.setString(2, loginName);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.AccountBankStorage
    public void updateAdena(String loginName, long adena) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_bank` SET `adena_count`=? WHERE `account_name`=?");
            pstm.setLong(1, adena);
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
