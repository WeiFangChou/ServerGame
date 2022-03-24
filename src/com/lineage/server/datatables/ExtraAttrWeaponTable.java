package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1AttrWeapon;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class ExtraAttrWeaponTable {
    private static final Map<Integer, L1AttrWeapon> _attrList = new LinkedHashMap();
    private static ExtraAttrWeaponTable _instance;
    private static final Log _log = LogFactory.getLog(ExtraAttrWeaponTable.class);

    public static ExtraAttrWeaponTable getInstance() {
        if (_instance == null) {
            _instance = new ExtraAttrWeaponTable();
        }
        return _instance;
    }

    public final void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.get().getConnection();
            pstm = conn.prepareStatement("SELECT * FROM 屬性武器能力系統 ORDER BY 流水號");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("流水號");
                String name = rs.getString("顯示名稱");
                int stage = rs.getInt("屬性階段");
                _attrList.put(Integer.valueOf((id * 100) + stage), new L1AttrWeapon(name, stage, rs.getInt("發動機率 (1/1000)"), rs.getDouble("束縛時間(秒)"), rs.getDouble("傷害倍數"), rs.getInt("最大吸血量(隨機)"), rs.getInt("最大吸魔量(隨機)"), rs.getInt("範圍(格)"), rs.getInt("範圍傷害"), rs.getInt("特效")));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
        _log.info("載入屬性武器資料數量: " + _attrList.size() + "(" + timer.get() + "ms)");
    }

    public final L1AttrWeapon get(int id, int stage) {
        return _attrList.get(Integer.valueOf((id * 100) + stage));
    }
}
