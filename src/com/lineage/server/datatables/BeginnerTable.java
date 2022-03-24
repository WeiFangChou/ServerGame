package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Beginner;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeginnerTable {
    private static final Map<String, ArrayList<L1Beginner>> _beginnerList = new HashMap();
    private static BeginnerTable _instance;
    public static final Log _log = LogFactory.getLog(BeginnerTable.class);

    public static BeginnerTable get() {
        if (_instance == null) {
            _instance = new BeginnerTable();
        }
        return _instance;
    }

    private BeginnerTable() {
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        int i = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `beginner`");
            rs = ps.executeQuery();
            while (rs.next()) {
                String activate = rs.getString("activate").toUpperCase();
                int itemid = rs.getInt("item_id");
                int count = rs.getInt("count");
                int enchantlvl = rs.getInt("enchantlvl");
                int charge_count = rs.getInt("charge_count");
                int time = rs.getInt("time");
                if (count > 0) {
                    L1Beginner beginner = new L1Beginner();
                    beginner.set_activate(activate);
                    beginner.set_itemid(itemid);
                    beginner.set_count(count);
                    beginner.set_enchantlvl(enchantlvl);
                    beginner.set_charge_count(charge_count);
                    beginner.set_time(time);
                    add(beginner);
                    i++;
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入新手物品資料數量: " + _beginnerList.size() + "/" + i + "(" + timer.get() + "ms)");
    }

    private void add(L1Beginner beginner) {
        String key = beginner.get_activate();
        ArrayList<L1Beginner> list = _beginnerList.get(key);
        if (list == null) {
            list = new ArrayList<>();
            list.add(beginner);
        } else {
            list.add(beginner);
        }
        _beginnerList.put(key, list);
    }

    public void giveItem(L1PcInstance pc) {
        String key = "A";
        if (pc.isCrown()) {
            key = "P";
        } else if (pc.isKnight()) {
            key = "K";
        } else if (pc.isElf()) {
            key = "E";
        } else if (pc.isWizard()) {
            key = "W";
        } else if (pc.isDarkelf()) {
            key = "D";
        } else if (pc.isDragonKnight()) {
            key = "R";
        } else if (pc.isIllusionist()) {
            key = "I";
        }
        ArrayList<L1Beginner> list = _beginnerList.get(key);
        if (list != null && !list.isEmpty()) {
            Iterator<L1Beginner> it = list.iterator();
            while (it.hasNext()) {
                get_item(pc.getId(), it.next());
            }
        }
        ArrayList<L1Beginner> listAll = _beginnerList.get("A");
        if (listAll != null && !listAll.isEmpty()) {
            Iterator<L1Beginner> it2 = listAll.iterator();
            while (it2.hasNext()) {
                get_item(pc.getId(), it2.next());
            }
        }
    }

    private void get_item(int objid, L1Beginner beginner) {
        try {
            L1ItemInstance item = ItemTable.get().createItem(beginner.get_itemid());
            if (item != null) {
                item.setCount((long) beginner.get_count());
                item.setEnchantLevel(beginner.get_enchantlvl());
                item.setChargeCount(beginner.get_charge_count());
                item.setBless(item.getBless());
                item.setIdentified(true);
                CharItemsReading.get().storeItem(objid, item);
                if (beginner.get_time() > 0) {
                    Timestamp ts = new Timestamp((((long) (beginner.get_time() * 60 * 60)) * 1000) + System.currentTimeMillis());
                    item.set_time(ts);
                    CharItemsTimeReading.get().addTime(item.getId(), ts);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
