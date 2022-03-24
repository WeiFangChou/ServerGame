package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharOtherStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1PcOther;
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

public class CharOtherTable implements CharOtherStorage {
    private static final Log _log = LogFactory.getLog(CharOtherTable.class);
    private static final Map<Integer, L1PcOther> _otherMap = new HashMap();

    @Override // com.lineage.server.datatables.storage.CharOtherStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_other`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int char_obj_id = rs.getInt("char_obj_id");
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    int hpup = rs.getInt("hpup");
                    int mpup = rs.getInt("mpup");
                    int score = rs.getInt("score");
                    int color = rs.getInt("color");
                    int usemap = rs.getInt("usemap");
                    int usemaptime = rs.getInt("usemaptime");
                    int clanskill = rs.getInt("clanskill");
                    int killCount = rs.getInt("killCount");
                    int deathCount = rs.getInt("deathCount");
                    L1PcOther other = new L1PcOther();
                    other.set_objid(char_obj_id);
                    other.set_addhp(hpup);
                    other.set_addmp(mpup);
                    other.set_score(score);
                    other.set_color(color);
                    if (usemaptime <= 0) {
                        other.set_usemap(-1);
                        other.set_usemapTime(0);
                    } else {
                        other.set_usemap(usemap);
                        other.set_usemapTime(usemaptime);
                    }
                    other.set_clanskill(clanskill);
                    other.set_killCount(killCount);
                    other.set_deathCount(deathCount);
                    addMap(char_obj_id, other);
                } else {
                    delete(char_obj_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入額外紀錄資料數量: " + _otherMap.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_other` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();
            _otherMap.remove(Integer.valueOf(objid));
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addMap(int objId, L1PcOther other) {
        if (_otherMap.get(Integer.valueOf(objId)) == null) {
            _otherMap.put(Integer.valueOf(objId), other);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharOtherStorage
    public L1PcOther getOther(L1PcInstance pc) {
        return _otherMap.get(Integer.valueOf(pc.getId()));
    }

    @Override // com.lineage.server.datatables.storage.CharOtherStorage
    public void storeOther(int objId, L1PcOther other) {
        if (_otherMap.get(Integer.valueOf(objId)) == null) {
            addMap(objId, other);
            addNewOther(other);
            return;
        }
        updateOther(other);
    }

    private void updateOther(L1PcOther other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            int hpup = other.get_addhp();
            int mpup = other.get_addmp();
            int score = other.get_score();
            int color = other.get_color();
            int usemap = other.get_usemap();
            int usemapTime = other.get_usemapTime();
            int clanskill = other.get_clanskill();
            int killCount = other.get_killCount();
            int deathCount = other.get_deathCount();
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_other` SET `hpup`=?,`mpup`=?,`score`=?,`color`=?,`usemap`=?,`usemaptime`=?,`clanskill`=?,`killCount`=?,`deathCount`=? WHERE `char_obj_id`=?");
            int i = 0 + 1;
            ps.setInt(i, hpup);
            int i2 = i + 1;
            ps.setInt(i2, mpup);
            int i3 = i2 + 1;
            ps.setInt(i3, score);
            int i4 = i3 + 1;
            ps.setInt(i4, color);
            int i5 = i4 + 1;
            ps.setInt(i5, usemap);
            int i6 = i5 + 1;
            ps.setInt(i6, usemapTime);
            int i7 = i6 + 1;
            ps.setInt(i7, clanskill);
            int i8 = i7 + 1;
            ps.setInt(i8, killCount);
            int i9 = i8 + 1;
            ps.setInt(i9, deathCount);
            ps.setInt(i9 + 1, other.get_objid());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void addNewOther(L1PcOther other) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            int oid = other.get_objid();
            int hpup = other.get_addhp();
            int mpup = other.get_addmp();
            int score = other.get_score();
            int color = other.get_color();
            int usemap = other.get_usemap();
            int usemapTime = other.get_usemapTime();
            int clanskill = other.get_clanskill();
            int killCount = other.get_killCount();
            int deathCount = other.get_deathCount();
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_other` SET `char_obj_id`=?,`hpup`=?,`mpup`=?,`score`=?,`color`=?,`usemap`=?,`usemaptime`=?,`clanskill`=?,`killCount`=?,`deathCount`=?");
            int i = 0 + 1;
            ps.setInt(i, oid);
            int i2 = i + 1;
            ps.setInt(i2, hpup);
            int i3 = i2 + 1;
            ps.setInt(i3, mpup);
            int i4 = i3 + 1;
            ps.setInt(i4, score);
            int i5 = i4 + 1;
            ps.setInt(i5, color);
            int i6 = i5 + 1;
            ps.setInt(i6, usemap);
            int i7 = i6 + 1;
            ps.setInt(i7, usemapTime);
            int i8 = i7 + 1;
            ps.setInt(i8, clanskill);
            int i9 = i8 + 1;
            ps.setInt(i9, killCount);
            ps.setInt(i9 + 1, deathCount);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharOtherStorage
    public void tam() {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            for (L1PcOther other : _otherMap.values()) {
                other.set_killCount(0);
                other.set_deathCount(0);
            }
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `character_other` SET `killCount`='0' AND `deathCount`='0'");
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
