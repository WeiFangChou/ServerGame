package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.drop.SetDrop;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DropTable {
    private static DropTable _instance;
    private static final Log _log = LogFactory.getLog(DropTable.class);
    private Map<Integer, ArrayList<L1Drop>> droplistMapcopy = new HashMap();

    public static DropTable get() {
        if (_instance == null) {
            _instance = new DropTable();
        }
        return _instance;
    }

    public static void init() {
        _instance = new DropTable();
    }

    public void load() {
        new SetDrop().addDropMap(allDropList());
    }

    private Map<Integer, ArrayList<L1Drop>> allDropList() {
        PerformanceTimer timer = new PerformanceTimer();
        Map<Integer, ArrayList<L1Drop>> droplistMap = new HashMap<>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `droplist`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int mobId = rs.getInt("mobId");
                int itemId = rs.getInt("itemId");
                int min = rs.getInt("min");
                int max = rs.getInt("max");
                int chance = rs.getInt("chance");
                if (check_item(itemId, mobId, rs.getString("note"), chance)) {
                    L1Drop drop = new L1Drop(mobId, itemId, min, max, chance);
                    ArrayList<L1Drop> dropList = droplistMap.get(Integer.valueOf(drop.getMobid()));
                    if (dropList == null) {
                        dropList = new ArrayList<>();
                        droplistMap.put(new Integer(drop.getMobid()), dropList);
                    }
                    dropList.add(drop);
                }
            }
            this.droplistMapcopy.putAll(droplistMap);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入掉落物品資料數量: " + droplistMap.size() + "(" + timer.get() + "ms)");
        return droplistMap;
    }

    private boolean check_item(int itemId, int mobId, String note, int chance) {
        L1Item itemTemplate = ItemTable.get().getTemplate(itemId);
        if (itemTemplate == null) {
            errorItem(itemId);
            return false;
        } else if (note.contains("→")) {
            return true;
        } else {
            updata_name(itemTemplate.getName(), itemId, mobId, chance);
            return true;
        }
    }

    private void updata_name(String itemname, int itemId, int mobId, int chance) {
        String blessed;
        Connection cn = null;
        PreparedStatement ps = null;
        String npcname = NpcTable.get().getNpcName(mobId);
        double dropchance = ((double) chance) / 10000.0d;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
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
            ps = cn.prepareStatement("UPDATE `droplist` SET `note`=? WHERE `itemId`=? AND `mobId`=?");
            int i = 0 + 1;
            ps.setString(i, String.valueOf(npcname) + "→：" + blessed + itemname + "→機率：" + nf.format(dropchance) + "%");
            int i2 = i + 1;
            ps.setInt(i2, itemId);
            ps.setInt(i2 + 1, mobId);
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
            pstm = con.prepareStatement("DELETE FROM `droplist` WHERE `itemId`=?");
            pstm.setInt(1, itemid);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public ArrayList<L1Drop> getdroplist(int mobid) {
        return this.droplistMapcopy.get(Integer.valueOf(mobid));
    }
}
