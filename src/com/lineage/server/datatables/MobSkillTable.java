package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1MobSkill;
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

public class MobSkillTable {
    private static MobSkillTable _instance;
    private static final Log _log = LogFactory.getLog(MobSkillTable.class);
    private static final Map<Integer, L1MobSkill> _mobskills = new HashMap();
    private final boolean _initialized = true;

    public static MobSkillTable getInstance() {
        if (_instance == null) {
            _instance = new MobSkillTable();
        }
        return _instance;
    }

    public boolean isInitialized() {
        return this._initialized;
    }

    private MobSkillTable() {
        loadMobSkillData();
    }

    private void loadMobSkillData() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm1 = null;
        PreparedStatement pstm2 = null;
        ResultSet rs1 = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm1 = con.prepareStatement("SELECT mobid,count(*) as cnt FROM mobskill group by mobid");
            pstm2 = con.prepareStatement("SELECT * FROM mobskill where mobid = ? order by mobid,actNo");
            rs1 = pstm1.executeQuery();
            while (rs1.next()) {
                int mobid = rs1.getInt("mobid");
                int count = rs1.getInt("cnt");
                ResultSet rs2 = null;
                try {
                    pstm2.setInt(1, mobid);
                    L1MobSkill mobskill = new L1MobSkill(count);
                    mobskill.set_mobid(mobid);
                    rs2 = pstm2.executeQuery();
                    while (rs2.next()) {
                        int actNo = rs2.getInt("actNo");
                        mobskill.setMobName(rs2.getString("mobname"));
                        mobskill.setType(actNo, rs2.getInt("type"));
                        mobskill.setTriggerRandom(actNo, rs2.getInt("TriRnd"));
                        mobskill.setTriggerHp(actNo, rs2.getInt("TriHp"));
                        mobskill.setTriggerCompanionHp(actNo, rs2.getInt("TriCompanionHp"));
                        mobskill.setTriggerRange(actNo, rs2.getInt("TriRange"));
                        mobskill.setTriggerCount(actNo, rs2.getInt("TriCount"));
                        mobskill.setChangeTarget(actNo, rs2.getInt("ChangeTarget"));
                        mobskill.setRange(actNo, rs2.getInt("Range"));
                        mobskill.setAreaWidth(actNo, rs2.getInt("AreaWidth"));
                        mobskill.setAreaHeight(actNo, rs2.getInt("AreaHeight"));
                        mobskill.setLeverage(actNo, rs2.getInt("Leverage"));
                        mobskill.setSkillId(actNo, rs2.getInt("SkillId"));
                        mobskill.setGfxid(actNo, rs2.getInt("Gfxid"));
                        mobskill.setActid(actNo, rs2.getInt("Actid"));
                        mobskill.setSummon(actNo, rs2.getInt("SummonId"));
                        mobskill.setSummonMin(actNo, rs2.getInt("SummonMin"));
                        mobskill.setSummonMax(actNo, rs2.getInt("SummonMax"));
                        mobskill.setPolyId(actNo, rs2.getInt("PolyId"));
                        mobskill.setReuseDelay(actNo, rs2.getInt("reuseDelay"));
                    }
                    _mobskills.put(new Integer(mobid), mobskill);
                } catch (SQLException e1) {
                    _log.error(e1.getLocalizedMessage(), e1);
                } finally {
                    SQLUtil.close(rs2);
                }
            }
            _log.info("載入MOB技能資料數量: " + _mobskills.size() + "(" + timer.get() + "ms)");
        } catch (SQLException e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } finally {
            SQLUtil.close(rs1);
            SQLUtil.close(pstm1);
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }
    }

    public L1MobSkill getTemplate(int id) {
        return _mobskills.get(Integer.valueOf(id));
    }
}
