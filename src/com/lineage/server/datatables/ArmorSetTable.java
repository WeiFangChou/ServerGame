package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.templates.L1ArmorSets;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ArmorSetTable {
    private static final ArrayList<L1ArmorSets> _armorSetList = new ArrayList<>();
    private static ArmorSetTable _instance;
    private static final Log _log = LogFactory.getLog(ArmorSetTable.class);

    public static ArmorSetTable get() {
        if (_instance == null) {
            _instance = new ArmorSetTable();
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
            pstm = con.prepareStatement("SELECT * FROM `armor_set`");
            rs = pstm.executeQuery();
            fillTable(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入套裝設置數量: " + _armorSetList.size() + "(" + timer.get() + "ms)");
        ArmorSet.load();
    }

    private void fillTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            L1ArmorSets as = new L1ArmorSets();
            as.setId(rs.getInt("id"));
            as.setSets(rs.getString("sets"));
            as.setPolyId(rs.getInt("polyid"));
            as.setAc(rs.getInt("ac"));
            as.setHp(rs.getInt("hp"));
            as.setMp(rs.getInt("mp"));
            as.setHpr(rs.getInt("hpr"));
            as.setMpr(rs.getInt("mpr"));
            as.setMr(rs.getInt("mr"));
            as.setStr(rs.getInt("str"));
            as.setDex(rs.getInt("dex"));
            as.setCon(rs.getInt("con"));
            as.setWis(rs.getInt("wis"));
            as.setCha(rs.getInt("cha"));
            as.setIntl(rs.getInt("intl"));
            as.setDefenseWater(rs.getInt("defense_water"));
            as.setDefenseWind(rs.getInt("defense_wind"));
            as.setDefenseFire(rs.getInt("defense_fire"));
            as.setDefenseEarth(rs.getInt("defense_earth"));
            as.set_regist_stun(rs.getInt("regist_stun"));
            as.set_regist_stone(rs.getInt("regist_stone"));
            as.set_regist_sleep(rs.getInt("regist_sleep"));
            as.set_regist_freeze(rs.getInt("regist_freeze"));
            as.set_regist_sustain(rs.getInt("regist_sustain"));
            as.set_regist_blind(rs.getInt("regist_blind"));
            as.set_modifier_dmg(rs.getInt("modifier_dmg"));
            as.set_reduction_dmg(rs.getInt("reduction_dmg"));
            as.set_magic_modifier_dmg(rs.getInt("magic_modifier_dmg"));
            as.set_magic_reduction_dmg(rs.getInt("magic_reduction_dmg"));
            as.set_bow_modifier_dmg(rs.getInt("bow_modifier_dmg"));
            String gfx = rs.getString("gfx");
            if (gfx != null && !gfx.equals("")) {
                String[] gfxs = gfx.replaceAll(" ", "").split(",");
                int[] out = new int[gfxs.length];
                for (int i = 0; i < gfxs.length; i++) {
                    out[i] = Integer.parseInt(gfxs[i]);
                }
                as.set_gfxs(out);
            }
            _armorSetList.add(as);
        }
    }

    public L1ArmorSets[] getAllList() {
        return (L1ArmorSets[]) _armorSetList.toArray(new L1ArmorSets[_armorSetList.size()]);
    }
}
