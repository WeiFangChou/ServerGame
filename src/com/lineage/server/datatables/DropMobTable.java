package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.templates.L1DropMob;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DropMobTable {
    private static DropMobTable _instance;
    private static final Log _log = LogFactory.getLog(DropMobTable.class);

    public static DropMobTable get() {
        if (_instance == null) {
            _instance = new DropMobTable();
        }
        return _instance;
    }

    private DropMobTable() {
        new SetDrop().addDropMob(allMobDropList());
    }

    private Map<Integer, L1DropMob> allMobDropList() {
        Map<Integer, L1DropMob> _droplistMob = new HashMap<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `droplist_mob`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int itemId = rs.getInt("itemid");
                int min = rs.getInt("min");
                int max = rs.getInt("max");
                int chance = rs.getInt("chance");
                if (check_item(itemId, rs.getString("note"))) {
                    L1DropMob drop = new L1DropMob();
                    drop.setMin(min);
                    drop.setMax(max);
                    drop.setChance(chance);
                    _droplistMob.put(Integer.valueOf(itemId), drop);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入掉落物品資料數量(全部怪物): " + _droplistMob.size());
        return _droplistMob;
    }

    private boolean check_item(int itemId, String note) {
        L1Item itemTemplate = ItemTable.get().getTemplate(itemId);
        if (itemTemplate == null) {
            errorItem(itemId);
            _log.info("全部怪物掉落錯誤物品->" + itemId);
            return false;
        } else if (note.contains("=>")) {
            return true;
        } else {
            updata_name(itemTemplate.getName(), itemId);
            return true;
        }
    }

    private void updata_name(String itemname, int itemId) {
        String blessed;
        Connection cn = null;
        PreparedStatement ps = null;
        L1Item dropitem = ItemTable.get().getTemplate(itemId);
        if (dropitem.getBless() == 1) {
            blessed = "";
        } else if (dropitem.getBless() == 0) {
            blessed = "祝福→";
        } else {
            blessed = "詛咒→";
        }
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `droplist_mob` SET `note`=? WHERE `itemid`=? ");
            int i = 0 + 1;
            ps.setString(i, "=>" + blessed + itemname);
            ps.setInt(i + 1, itemId);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void errorItem(int itemid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `droplist_mob` WHERE `itemid`=?");
            pstm.setInt(1, itemid);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
