package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollPowerTable {
    private static final ArrayList<String> _checkList = new ArrayList<>();
    private static final HashMap<Integer, L1DollExecutor> _classList = new HashMap<>();
    private static DollPowerTable _instance;
    private static final Log _log = LogFactory.getLog(DollPowerTable.class);
    private static final HashMap<Integer, L1Doll> _powerMap = new HashMap<>();

    public static DollPowerTable get() {
        if (_instance == null) {
            _instance = new DollPowerTable();
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
            pstm = con.prepareStatement("SELECT * FROM `etcitem_doll_power`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String classname = rs.getString("classname");
                int type1 = rs.getInt("type1");
                int type2 = rs.getInt("type2");
                int type3 = rs.getInt("type3");
                String note = rs.getString("note");
                String ch = String.valueOf(classname) + "=" + type1 + "=" + type2 + "=" + type3;
                if (_checkList.lastIndexOf(ch) == -1) {
                    _checkList.add(ch);
                    addList(id, classname, type1, type2, type3, note);
                } else {
                    _log.error("娃娃能力設置重複:id=" + id + " type1=" + type1 + " type2=" + type2 + " type3=" + type3);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
            _checkList.clear();
        }
        _log.info("載入娃娃能力資料數量: " + _classList.size() + "(" + timer.get() + "ms)");
        setDollType();
    }

    private void setDollType() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `etcitem_doll_type`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int itemid = rs.getInt("itemid");
                String powers = rs.getString("powers").replaceAll(" ", "");
                String need = rs.getString("need").replaceAll(" ", "");
                String count = rs.getString("count").replaceAll(" ", "");
                int time = rs.getInt("time");
                int gfxid = rs.getInt("gfxid");
                String nameid = rs.getString("nameid");
                boolean iserr = false;
                ArrayList<L1DollExecutor> powerList = new ArrayList<>();
                if (powers != null && !powers.equals("")) {
                    String[] set1 = powers.split(",");
                    int length = set1.length;
                    for (int i = 0; i < length; i++) {
                        String string = set1[i];
                        L1DollExecutor e = _classList.get(Integer.valueOf(Integer.parseInt(string)));
                        if (e != null) {
                            powerList.add(e);
                        } else {
                            _log.error("娃娃能力取回錯誤-沒有這個編號:" + string);
                            iserr = true;
                        }
                    }
                }
                int[] needs = null;
                if (need != null && !need.equals("")) {
                    String[] set2 = need.split(",");
                    needs = new int[set2.length];
                    for (int i2 = 0; i2 < set2.length; i2++) {
                        int itemid_n = Integer.parseInt(set2[i2]);
                        if (ItemTable.get().getTemplate(itemid_n) == null) {
                            _log.error("物品資訊取回錯誤-沒有這個編號:" + itemid_n);
                            iserr = true;
                        }
                        needs[i2] = itemid_n;
                    }
                }
                int[] counts = null;
                if (count != null && !count.equals("")) {
                    String[] set3 = count.split(",");
                    counts = new int[set3.length];
                    if (set3.length != needs.length) {
                        _log.error("物品資訊對應錯誤-長度不吻合: itemid:" + itemid);
                        iserr = true;
                    }
                    for (int i3 = 0; i3 < set3.length; i3++) {
                        counts[i3] = Integer.parseInt(set3[i3]);
                    }
                }
                if (!iserr) {
                    L1Doll doll_power = new L1Doll();
                    doll_power.set_itemid(itemid);
                    doll_power.setPowerList(powerList);
                    doll_power.set_need(needs);
                    doll_power.set_counts(counts);
                    doll_power.set_time(time);
                    doll_power.set_gfxid(gfxid);
                    doll_power.set_nameid(nameid);
                    _powerMap.put(Integer.valueOf(itemid), doll_power);
                }
            }
        } catch (SQLException e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        } catch (Exception e3) {
            _log.error(e3.getLocalizedMessage(), e3);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
            _classList.clear();
        }
        _log.info("載入娃娃能力資料數量: " + _classList.size() + "(" + timer.get() + "ms)");
    }

    private void addList(int powerid, String className, int int1, int int2, int int3, String note) {
        if (!className.equals("0")) {
            try {
                L1DollExecutor exe = (L1DollExecutor) Class.forName("com.lineage.server.model.doll." + className).getMethod("get", new Class[0]).invoke(null, new Object[0]);
                exe.set_power(int1, int2, int3);
                exe.set_note(note);
                _classList.put(new Integer(powerid), exe);
            } catch (ClassNotFoundException e) {
                _log.error("發生[娃娃能力檔案]錯誤, 檢查檔案是否存在:" + className + " 娃娃能力編號:" + powerid);
            } catch (IllegalArgumentException e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            } catch (IllegalAccessException e3) {
                _log.error(e3.getLocalizedMessage(), e3);
            } catch (InvocationTargetException e4) {
                _log.error(e4.getLocalizedMessage(), e4);
            } catch (SecurityException e5) {
                _log.error(e5.getLocalizedMessage(), e5);
            } catch (NoSuchMethodException e6) {
                _log.error(e6.getLocalizedMessage(), e6);
            }
        }
    }

    public L1Doll get_type(int key) {
        return _powerMap.get(Integer.valueOf(key));
    }

    public HashMap<Integer, L1Doll> map() {
        return _powerMap;
    }
}
