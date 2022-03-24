package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.L1WeaponSkill;
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

public class WeaponSkillTable {
    private static WeaponSkillTable _instance;
    private static final Log _log = LogFactory.getLog(WeaponSkillTable.class);
    private static final Map<Integer, L1WeaponSkill> _weaponIdIndex = new HashMap();

    public static WeaponSkillTable get() {
        if (_instance == null) {
            _instance = new WeaponSkillTable();
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
            pstm = con.prepareStatement("SELECT * FROM `weapon_skill`");
            rs = pstm.executeQuery();
            fillWeaponSkillTable(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入技能武器資料數量: " + _weaponIdIndex.size() + "(" + timer.get() + "ms)");
    }

    private void fillWeaponSkillTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            int weaponId = rs.getInt("weapon_id");
            _weaponIdIndex.put(Integer.valueOf(weaponId), new L1WeaponSkill(weaponId, rs.getInt("probability"), rs.getInt("fix_damage"), rs.getInt("random_damage"), rs.getInt("area"), rs.getInt("skill_id"), rs.getInt("skill_time"), rs.getInt("effect_id"), rs.getInt("effect_target"), rs.getBoolean("arrow_type"), rs.getInt("attr")));
        }
    }

    public L1WeaponSkill getTemplate(int weaponId) {
        return _weaponIdIndex.get(Integer.valueOf(weaponId));
    }
}
