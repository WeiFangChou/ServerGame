package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1SkillItem;
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

public class SkillsItemTable {
    private static SkillsItemTable _instance;
    private static final Log _log = LogFactory.getLog(SkillsItemTable.class);
    private static final Map<Integer, L1SkillItem> _skills = new HashMap();

    public static SkillsItemTable get() {
        if (_instance == null) {
            _instance = new SkillsItemTable();
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
            pstm = con.prepareStatement("SELECT * FROM `skills_item`");
            rs = pstm.executeQuery();
            itemTable(rs);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入購買技能 材料 設置資料數量: " + _skills.size() + "(" + timer.get() + "ms)");
    }

    private void itemTable(ResultSet rs) throws SQLException {
        while (rs.next()) {
            L1SkillItem skillItem = new L1SkillItem();
            int skill_id = rs.getInt("skill_id");
            skillItem.set_skill_id(skill_id);
            skillItem.set_name(rs.getString("name"));
            String items = rs.getString("itemids");
            if (items == null) {
                skillItem.set_items(null);
            } else {
                String[] itemsX = items.split(",");
                int[] items_x = new int[itemsX.length];
                for (int i = 0; i < itemsX.length; i++) {
                    items_x[i] = Integer.parseInt(itemsX[i]);
                }
                skillItem.set_items(items_x);
            }
            String counts = rs.getString("counts");
            if (counts == null) {
                skillItem.set_counts(null);
            } else {
                String[] countsX = counts.split(",");
                int[] counts_x = new int[countsX.length];
                for (int i2 = 0; i2 < countsX.length; i2++) {
                    counts_x[i2] = Integer.parseInt(countsX[i2]);
                }
                skillItem.set_counts(counts_x);
            }
            _skills.put(new Integer(skill_id), skillItem);
            if (skillItem.get_items() == null) {
                _skills.remove(new Integer(skill_id));
            } else if (skillItem.get_items().length != skillItem.get_counts().length) {
                _log.error("購買技能 材料 設置資料異常 技能編號: " + skill_id);
                _skills.remove(new Integer(skill_id));
            }
        }
    }

    public L1SkillItem getTemplate(int i) {
        return _skills.get(new Integer(i));
    }
}
