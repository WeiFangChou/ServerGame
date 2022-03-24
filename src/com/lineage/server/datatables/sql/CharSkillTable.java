package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharSkillStorage;
import com.lineage.server.templates.L1UserSkillTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharSkillTable implements CharSkillStorage {
    private static final Log _log = LogFactory.getLog(CharSkillTable.class);
    private static final Map<Integer, ArrayList<L1UserSkillTmp>> _skillMap = new HashMap();

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `character_skills`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int char_obj_id = rs.getInt("char_obj_id");
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    int skill_id = rs.getInt("skill_id");
                    String skill_name = rs.getString("skill_name");
                    int is_active = rs.getInt("is_active");
                    int activetimeleft = rs.getInt("activetimeleft");
                    L1UserSkillTmp userSkill = new L1UserSkillTmp();
                    userSkill.set_char_obj_id(char_obj_id);
                    userSkill.set_skill_id(skill_id);
                    userSkill.set_skill_name(skill_name);
                    userSkill.set_is_active(is_active);
                    userSkill.set_activetimeleft(activetimeleft);
                    addMap(char_obj_id, userSkill);
                } else {
                    deleteBuff(char_obj_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("載入人物技能紀錄資料數量: " + _skillMap.size() + "(" + timer.get() + "ms)");
    }

    private static void deleteBuff(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_skills` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addMap(int objId, L1UserSkillTmp skillTmp) {
        ArrayList<L1UserSkillTmp> list = _skillMap.get(Integer.valueOf(objId));
        if (list == null) {
            list = new ArrayList<>();
            list.add(skillTmp);
        } else {
            list.add(skillTmp);
        }
        _skillMap.put(Integer.valueOf(objId), list);
    }

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public ArrayList<L1UserSkillTmp> skills(int playerobjid) {
        return _skillMap.get(Integer.valueOf(playerobjid));
    }

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public void spellMastery(int playerobjid, int skillid, String skillname, int active, int time) {
        if (!spellCheck(playerobjid, skillid)) {
            L1UserSkillTmp userSkill = new L1UserSkillTmp();
            userSkill.set_char_obj_id(playerobjid);
            userSkill.set_skill_id(skillid);
            userSkill.set_skill_name(skillname);
            userSkill.set_is_active(active);
            userSkill.set_activetimeleft(time);
            addMap(playerobjid, userSkill);
            Connection co = null;
            PreparedStatement ps = null;
            try {
                co = DatabaseFactory.get().getConnection();
                ps = co.prepareStatement("INSERT INTO `character_skills` SET `char_obj_id`=?,`skill_id`=?,`skill_name`=?,`is_active`=?,`activetimeleft`=?");
                ps.setInt(1, userSkill.get_char_obj_id());
                ps.setInt(2, userSkill.get_skill_id());
                ps.setString(3, userSkill.get_skill_name());
                ps.setInt(4, userSkill.get_is_active());
                ps.setInt(5, userSkill.get_activetimeleft());
                ps.execute();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(co);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public void spellLost(int playerobjid, int skillid) {
        ArrayList<L1UserSkillTmp> list = _skillMap.get(Integer.valueOf(playerobjid));
        L1UserSkillTmp del = null;
        if (list != null) {
            Iterator<L1UserSkillTmp> it = list.iterator();
            while (it.hasNext()) {
                L1UserSkillTmp userSkillTmp = it.next();
                if (userSkillTmp.get_skill_id() == skillid) {
                    del = userSkillTmp;
                }
            }
        }
        if (del != null) {
            list.remove(del);
            Connection co = null;
            PreparedStatement ps = null;
            try {
                co = DatabaseFactory.get().getConnection();
                ps = co.prepareStatement("DELETE FROM `character_skills` WHERE `char_obj_id`=? AND `skill_id`=?");
                ps.setInt(1, playerobjid);
                ps.setInt(2, skillid);
                ps.execute();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(co);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public boolean spellCheck(int playerobjid, int skillid) {
        ArrayList<L1UserSkillTmp> list = _skillMap.get(Integer.valueOf(playerobjid));
        if (list != null) {
            Iterator<L1UserSkillTmp> it = list.iterator();
            while (it.hasNext()) {
                if (it.next().get_skill_id() == skillid) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.lineage.server.datatables.storage.CharSkillStorage
    public void setAuto(int mode, int objid, int skillid) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("UPDATE `character_skills` SET `is_active`=? WHERE `char_obj_id`=? AND `skill_id`=?");
            ps.setInt(1, mode);
            ps.setInt(2, objid);
            ps.setInt(3, skillid);
            ps.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
