package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1UbSupplie;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Lists;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UbSupplies {
    private static UbSupplies _instance;
    private static Logger _log = Logger.getLogger(UbSupplies.class.getName());
    private final List<L1UbSupplie> _ubSupplies = Lists.newArrayList();

    private UbSupplies() throws Throwable {
        load();
    }

    public static UbSupplies getInstance() throws Throwable {
        if (_instance == null) {
            _instance = new UbSupplies();
        }
        return _instance;
    }

    public void load() throws Throwable {
        Throwable th;
        SQLException e;
        L1UbSupplie us = null;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("select * from ub_supplies ORDER BY ub_id,ub_round,ub_item_id");
            rs = pstm.executeQuery();
            while (rs.next()) {
                try {
                    us = new L1UbSupplie();
                    us.setUbId(rs.getInt("ub_id"));
                    us.setUbName(rs.getString("ub_name"));
                    us.setUbRound(rs.getInt("ub_round"));
                    us.setUbItemId(rs.getInt("ub_item_id"));
                    us.setUbItemStackCont(rs.getInt("ub_item_stackcont"));
                    us.setUbItemCont(rs.getInt("ub_item_cont"));
                    us.setUbItemBless(rs.getInt("ub_item_bless"));
                    this._ubSupplies.add(us);
                } catch (SQLException e2) {
                    e = e2;
                    try {
                        _log.log(Level.SEVERE, e.getLocalizedMessage(), (Throwable) e);
                        SQLUtil.close(rs);
                        SQLUtil.close(pstm);
                        SQLUtil.close(con);
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        SQLUtil.close(rs);
                        SQLUtil.close(pstm);
                        SQLUtil.close(con);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    SQLUtil.close(rs);
                    SQLUtil.close(pstm);
                    SQLUtil.close(con);
                    throw th;
                }
            }
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        } catch (SQLException e3) {
            e = e3;
        }
    }

    public List<L1UbSupplie> getUbSupplies(int id) {
        List<L1UbSupplie> temp = Lists.newArrayList();
        for (L1UbSupplie t : this._ubSupplies) {
            if (t.getUbId() == id) {
                temp.add(t);
            }
        }
        return temp;
    }
}
