package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1BlessSystem;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BlessSystem {
    private static final Map<Integer, L1BlessSystem> _enchantlist = new HashMap();
    private static BlessSystem _instance;
    private static final Log _log = LogFactory.getLog(BlessSystem.class);

    public static BlessSystem get() {
        if (_instance == null) {
            _instance = new BlessSystem();
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
            pm = co.prepareStatement("SELECT * FROM `祝福武器能力系統`");
            rs = pm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("序號");
                L1BlessSystem en = new L1BlessSystem();
                en.setWeaponId(rs.getInt("武器編號"));
                en.setHp(rs.getInt("血量"));
                en.setMp(rs.getInt("魔量"));
                en.setStr(rs.getInt("力量"));
                en.setDex(rs.getInt("敏捷"));
                en.setCon(rs.getInt("體質"));
                en.setWis(rs.getInt("精神"));
                en.setInt(rs.getInt("智力"));
                en.setCha(rs.getInt("魅力"));
                en.setMr(rs.getInt("魔防"));
                en.setSp(rs.getInt("魔攻"));
                en.setFire(rs.getInt("火屬性"));
                en.setWater(rs.getInt("水屬性"));
                en.setWind(rs.getInt("風屬性"));
                en.setEarth(rs.getInt("地屬性"));
                en.setFreeze(rs.getInt("寒冰耐性"));
                en.setStone(rs.getInt("石化耐性"));
                en.setSleep(rs.getInt("睡眠耐性"));
                en.setBlind(rs.getInt("暗黑耐性"));
                en.setStun(rs.getInt("昏迷耐性"));
                en.setSustain(rs.getInt("支撑耐性"));
                en.setHpr(rs.getInt("血量回復量"));
                en.setMpr(rs.getInt("魔量回復量"));
                en.setExp(rs.getInt("經驗值"));
                en.setBamage(rs.getInt("傷害減免"));
                en.setHit(rs.getInt("近距離命中"));
                en.setDmg(rs.getInt("近距離傷害"));
                en.setBowHit(rs.getInt("遠距離命中"));
                en.setBowDmg(rs.getInt("遠距離傷害"));
                en.setDmgSmall(rs.getInt("攻擊對小怪"));
                en.setDmgLarge(rs.getInt("攻擊對大怪"));
                _enchantlist.put(Integer.valueOf(id), en);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
        _log.info("載入祝福武器能力資料數量: " + _enchantlist.size() + "(" + timer.get() + "ms)");
    }

    public static L1BlessSystem get(int weaponId) {
        try {
            if (_enchantlist.size() > 0) {
                for (int i = 0; i <= _enchantlist.size(); i++) {
                    L1BlessSystem list = _enchantlist.get(Integer.valueOf(i));
                    if (list != null && list.getWeaponId() == weaponId) {
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
