package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1WilliamGfxIdOrginal;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GfxIdOrginal {
    private static final HashMap<Integer, L1WilliamGfxIdOrginal> _gfxIdIndex = new HashMap<>();
    private static GfxIdOrginal _instance;
    private static final Log _log = LogFactory.getLog(GfxIdOrginal.class);

    public static GfxIdOrginal get() {
        if (_instance == null) {
            _instance = new GfxIdOrginal();
        }
        return _instance;
    }

    private GfxIdOrginal() {
        loadGfxIdOrginal();
    }

    public void loadGfxIdOrginal() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.get().getConnection();
            pstm = conn.prepareStatement("SELECT * FROM 變身魔法特效");
            rs = pstm.executeQuery();
            fillWeaponSkill(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
        _log.info("載入變身魔法特效設定: " + _gfxIdIndex.size() + "(" + timer.get() + "ms)");
    }

    private void fillWeaponSkill(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int gfxId = rs.getInt("變身編號");
            _gfxIdIndex.put(Integer.valueOf(gfxId), new L1WilliamGfxIdOrginal(gfxId, rs.getInt("箭矢特效"), rs.getInt("飛刀特效")));
        }
    }

    public L1WilliamGfxIdOrginal getTemplate(int gfxId) {
        return _gfxIdIndex.get(Integer.valueOf(gfxId));
    }

    public L1WilliamGfxIdOrginal[] getGfxIdList() {
        return (L1WilliamGfxIdOrginal[]) _gfxIdIndex.values().toArray(new L1WilliamGfxIdOrginal[_gfxIdIndex.size()]);
    }
}
