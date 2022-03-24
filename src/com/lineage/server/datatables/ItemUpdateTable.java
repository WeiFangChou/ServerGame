package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.templates.L1ItemUpdate;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemUpdateTable {
    private static ItemUpdateTable _instance;
    private static final Log _log = LogFactory.getLog(ItemUpdateTable.class);
    private static Map<Integer, ArrayList<L1ItemUpdate>> _updateMap = new HashMap();

    public static ItemUpdateTable get() {
        if (_instance == null) {
            _instance = new ItemUpdateTable();
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
            pstm = con.prepareStatement("SELECT * FROM `server_item_update` ORDER BY `id`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int item_id = rs.getInt("itemid");
                if (ItemTable.get().getTemplate(item_id) == null) {
                    _log.error("物品升級資料錯誤: 沒有這個編號的道具:" + item_id);
                    delete(id);
                } else {
                    String[] needids_tmp = rs.getString("needids").replaceAll(" ", "").split(",");
                    String[] needcounts_tmp = rs.getString("needcounts").replaceAll(" ", "").split(",");
                    if (needids_tmp.length != needcounts_tmp.length) {
                        _log.error("物品升級資料錯誤: 交換物品需要道具數量不吻合" + item_id);
                    } else {
                        int toid = rs.getInt("toid");
                        if (ItemTable.get().getTemplate(toid) == null) {
                            _log.error("物品升級資料錯誤: 沒有這個編號的對象道具:" + toid);
                            delete(id);
                        } else {
                            int[] needids = new int[needids_tmp.length];
                            for (int i = 0; i < needids_tmp.length; i++) {
                                needids[i] = Integer.parseInt(needids_tmp[i]);
                            }
                            int[] needcounts = new int[needcounts_tmp.length];
                            for (int i2 = 0; i2 < needcounts_tmp.length; i2++) {
                                needcounts[i2] = Integer.parseInt(needcounts_tmp[i2]);
                            }
                            L1ItemUpdate tmp = new L1ItemUpdate();
                            tmp.set_item_id(item_id);
                            tmp.set_toid(toid);
                            tmp.set_needids(needids);
                            tmp.set_needcounts(needcounts);
                            ArrayList<L1ItemUpdate> value = _updateMap.get(Integer.valueOf(item_id));
                            if (value == null) {
                                value = new ArrayList<>();
                                value.add(tmp);
                            } else {
                                value.add(tmp);
                            }
                            _updateMap.put(Integer.valueOf(item_id), value);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入物品升級資料數量: " + _updateMap.size() + "(" + timer.get() + "ms)");
    }

    public static void delete(int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `server_item_update` WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public ArrayList<L1ItemUpdate> get(int key) {
        return _updateMap.get(Integer.valueOf(key));
    }

    public Map<Integer, ArrayList<L1ItemUpdate>> map() {
        return _updateMap;
    }
}
