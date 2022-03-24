package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1WilliamTypeIdOrginal;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TypeIdOrginal {
    private static final HashMap<Integer, L1WilliamTypeIdOrginal> _gfxIdIndex = new HashMap<>();
    private static TypeIdOrginal _instance;
    private static final Log _log = LogFactory.getLog(TypeIdOrginal.class);

    public static TypeIdOrginal get() {
        if (_instance == null) {
            _instance = new TypeIdOrginal();
        }
        return _instance;
    }

    private TypeIdOrginal() {
        loadGfxIdOrginal();
    }

    public void loadGfxIdOrginal() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.get().getConnection();
            pstm = conn.prepareStatement("SELECT * FROM 職業傷害系統");
            rs = pstm.executeQuery();
            fillWeaponSkill(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
        _log.info("載入職業傷害設定: " + _gfxIdIndex.size() + "(" + timer.get() + "ms)");
    }

    private void fillWeaponSkill(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int gfxId = rs.getInt("職業");
            _gfxIdIndex.put(Integer.valueOf(gfxId), new L1WilliamTypeIdOrginal(gfxId, (double) rs.getInt("傷害值")));
        }
    }

    public L1WilliamTypeIdOrginal getTemplate(int gfxId) {
        return _gfxIdIndex.get(Integer.valueOf(gfxId));
    }

    public L1WilliamTypeIdOrginal[] getGfxIdList() {
        return (L1WilliamTypeIdOrginal[]) _gfxIdIndex.values().toArray(new L1WilliamTypeIdOrginal[_gfxIdIndex.size()]);
    }
}
