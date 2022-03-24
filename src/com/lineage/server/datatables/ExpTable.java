package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Exp;
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

public final class ExpTable {
    public static long MAX_EXP = 0;
    public static int MAX_LEVEL = 0;
    private static final Map<Integer, L1Exp> _expList = new HashMap();
    private static ExpTable _instance;
    private static final Log _log = LogFactory.getLog(ExpTable.class);

    public static ExpTable get() {
        if (_instance == null) {
            _instance = new ExpTable();
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
            pstm = con.prepareStatement("SELECT * FROM `exp`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int level = rs.getInt("level");
                long exp = rs.getLong("exp");
                double expPenalty = rs.getDouble("expPenalty");
                if (level > MAX_LEVEL) {
                    MAX_LEVEL = level;
                }
                if (exp > MAX_EXP) {
                    MAX_EXP = exp;
                }
                L1Exp l1exp = new L1Exp();
                l1exp.set_level(level);
                l1exp.set_exp(exp);
                l1exp.set_expPenalty(expPenalty);
                _expList.put(new Integer(level), l1exp);
            }
            _log.info("載入經驗質設置資料數量: " + _expList.size() + "(" + timer.get() + "ms)");
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        MAX_LEVEL--;
    }

    public static long getExpByLevel(int level) {
        L1Exp l1exp = _expList.get(new Integer(level - 1));
        if (l1exp != null) {
            return l1exp.get_exp();
        }
        return 0;
    }

    public static long getNeedExpNextLevel(int level) {
        return getExpByLevel(level + 1) - getExpByLevel(level);
    }

    public static int getLevelByExp(long exp) {
        int level = 1;
        for (Integer integer : _expList.keySet()) {
            if (integer.intValue() != 0) {
                long upe = _expList.get(integer).get_exp();
                if (exp >= _expList.get(Integer.valueOf(integer.intValue() - 1)).get_exp() && exp < upe) {
                    level = integer.intValue();
                }
            }
        }
        return Math.min(level, MAX_LEVEL);
    }

    public static int getExpPercentage(int level, long exp) {
        return (int) (100.0d * (((double) (exp - getExpByLevel(level))) / ((double) getNeedExpNextLevel(level))));
    }

    public static double getPenaltyRate(int level) {
        L1Exp l1exp = _expList.get(new Integer(level));
        if (l1exp != null) {
            return 1.0d / l1exp.get_expPenalty();
        }
        return 1.0d;
    }
}
