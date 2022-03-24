package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1Day_Signature;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Day_Signature {
    private static final Map<Integer, L1Day_Signature> _DaySignature = Maps.newHashMap();
    private static Day_Signature _instance;
    private static final Log _log = LogFactory.getLog(Day_Signature.class);

    public static Day_Signature get() {
        if (_instance == null) {
            _instance = new Day_Signature();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `每日簽到領取`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("流水號");
                L1Day_Signature Day = new L1Day_Signature();
                Day.setDay(rs.getInt("日期"));
                Day.setMsg(rs.getString("顯示文字"));
                Day.setItem(rs.getString("物品編號"));
                Day.setEnchant(rs.getString("物品加成"));
                Day.setCount(rs.getString("物品數量"));
                Day.setMakeUp(rs.getInt("補簽物品編號"));
                Day.setMakeUpC(rs.getInt("補簽物品數量"));
                _DaySignature.put(Integer.valueOf(id), Day);
                i++;
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入每日簽到領取設置數量: " + i + "(" + timer.get() + "ms)");
    }

    public int DaySize() {
        return _DaySignature.size();
    }

    public L1Day_Signature getDay(int id) {
        return _DaySignature.get(Integer.valueOf(id));
    }
}
