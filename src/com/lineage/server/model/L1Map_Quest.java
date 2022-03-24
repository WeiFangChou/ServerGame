package com.lineage.server.model;

import com.lineage.DatabaseFactory;
import com.lineage.config.ConfigOther;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Map_Quest {
    public static final int QUEST_END = 255;
    private static final Log _log = LogFactory.getLog(L1Map_Quest.class);
    private static L1PcInstance _owner = null;
    private static Map<Integer, Integer> _quest = null;

    public L1Map_Quest(L1PcInstance owner) {
        _owner = owner;
    }

    public L1PcInstance get_owner() {
        return _owner;
    }

    public int get_step(int quest_id) {
        delete();
        if (_quest == null) {
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                _quest = Maps.newHashMap();
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("SELECT * FROM character_account_map WHERE account_name=?");
                pstm.setString(1, _owner.getAccountName());
                rs = pstm.executeQuery();
                while (rs.next()) {
                    _quest.put(new Integer(rs.getInt(2)), new Integer(rs.getInt(3)));
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
        Integer step = _quest.get(new Integer(quest_id));
        if (step == null) {
            return 0;
        }
        return step.intValue();
    }

    public void set_step(int quest_id, int step) {
        PreparedStatement pstm;
        Connection con = null;
        PreparedStatement pstm2 = null;
        try {
            con = DatabaseFactory.get().getConnection();
            if (_quest.get(new Integer(quest_id)) == null) {
                pstm = con.prepareStatement("INSERT INTO character_account_map SET account_name = ?, quest_id = ?, quest_step = ?");
                pstm.setString(1, _owner.getAccountName());
                pstm.setInt(2, quest_id);
                pstm.setInt(3, step);
                pstm.execute();
            } else {
                pstm = con.prepareStatement("UPDATE character_account_map SET quest_step = ? WHERE account_name = ? AND quest_id = ?");
                pstm.setInt(1, step);
                pstm.setString(2, _owner.getAccountName());
                pstm.setInt(3, quest_id);
                pstm.execute();
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }
        _quest.put(new Integer(quest_id), new Integer(step));
    }

    public void add_step(int quest_id, int add) {
        set_step(quest_id, get_step(quest_id) + add);
    }

    public void set_end(int quest_id) {
        set_step(quest_id, 255);
    }

    public boolean isEnd(int quest_id) {
        if (get_step(quest_id) == 255) {
            return true;
        }
        return false;
    }

    private static void delete() {
        int[] s;
        for (int i : ConfigOther.SHOCK) {
            deleteData(i);
        }
    }

    public static void deleteData(int questid) {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_account_map` WHERE `quest_id`=?");
            pm.setInt(1, questid);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
    }
}
