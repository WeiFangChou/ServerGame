package com.lineage.server.model;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.datatables.ShopTable;
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
import william.L1Config;

public class L1CountQuest {
    public static final int QUEST_END = 255;
    private static final Log _log = LogFactory.getLog(L1CountQuest.class);
    private L1PcInstance _owner = null;
    private Map<Integer, Integer> _quest = null;

    public L1CountQuest(L1PcInstance owner) {
        this._owner = owner;
    }

    public L1PcInstance get_owner() {
        return this._owner;
    }

    public int get_step(int quest_id) {
        delete();
        if (this._quest == null) {
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                this._quest = Maps.newHashMap();
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("SELECT * FROM character_account_count WHERE account_name=?");
                pstm.setString(1, this._owner.getAccountName());
                rs = pstm.executeQuery();
                while (rs.next()) {
                    this._quest.put(new Integer(rs.getInt(2)), new Integer(rs.getInt(3)));
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
        Integer step = this._quest.get(new Integer(quest_id));
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
            if (this._quest.get(new Integer(quest_id)) == null) {
                pstm = con.prepareStatement("INSERT INTO character_account_count SET account_name = ?, quest_id = ?, quest_step = ?");
                pstm.setString(1, this._owner.getAccountName());
                pstm.setInt(2, quest_id);
                pstm.setInt(3, step);
                pstm.execute();
            } else {
                pstm = con.prepareStatement("UPDATE character_account_count SET quest_step = ? WHERE account_name = ? AND quest_id = ?");
                pstm.setInt(1, step);
                pstm.setString(2, this._owner.getAccountName());
                pstm.setInt(3, quest_id);
                pstm.execute();
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }
        this._quest.put(new Integer(quest_id), new Integer(step));
    }

    private static void delete() {
        if (L1Config._4008) {
            for (Integer num : ShopTable._DailyItem.keySet()) {
                deleteData(num.intValue());
            }
            for (Integer num2 : ShopCnTable._DailyCnItem.keySet()) {
                deleteData(num2.intValue());
            }
        }
    }

    public static void deleteData(int quest_id) {
        Connection co = null;
        PreparedStatement pm = null;
        try {
            co = DatabaseFactory.get().getConnection();
            pm = co.prepareStatement("DELETE FROM `character_account_count` WHERE `quest_id`=?");
            pm.setInt(1, quest_id);
            pm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pm);
            SQLUtil.close(co);
        }
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
}
