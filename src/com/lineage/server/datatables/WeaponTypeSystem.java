package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1WeaponTypeSystem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WeaponTypeSystem {
    private static final Map<Integer, L1WeaponTypeSystem> _enchantlist = new HashMap();
    private static WeaponTypeSystem _instance;
    private static final Log _log = LogFactory.getLog(WeaponTypeSystem.class);

    public static WeaponTypeSystem get() {
        if (_instance == null) {
            _instance = new WeaponTypeSystem();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement pm = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("SELECT * FROM `武器爆擊系統`");
            rs = pm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("序號");
                L1WeaponTypeSystem en = new L1WeaponTypeSystem();
                en.setType(rs.getInt("武器類型"));
                en.setProbability(rs.getInt("發動機率 (1/1000)"));
                en.setDmg(rs.getDouble("傷害倍數"));
                en.setgfxid(rs.getInt("爆擊特效"));
                _enchantlist.put(Integer.valueOf(id), en);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
        _log.info("載入武器爆擊資料數量: " + _enchantlist.size() + "(" + timer.get() + "ms)");
    }

    public static L1WeaponTypeSystem get(int type) {
        try {
            if (_enchantlist.size() > 0) {
                for (int i = 0; i <= _enchantlist.size(); i++) {
                    L1WeaponTypeSystem list = _enchantlist.get(Integer.valueOf(i));
                    if (list != null && list.getType() == type) {
                        return list;
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }
}
