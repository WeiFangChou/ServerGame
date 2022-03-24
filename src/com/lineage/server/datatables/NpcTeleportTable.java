package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1TeleportLoc;
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

public class NpcTeleportTable {
    private static NpcTeleportTable _instance;
    private static final Log _log = LogFactory.getLog(NpcTeleportTable.class);
    private static final Map<Integer, Integer> _partyMap = new HashMap();
    private static final Map<Integer, HashMap<String, L1TeleportLoc>> _srcMap = new HashMap();
    private static final Map<String, HashMap<Integer, L1TeleportLoc>> _teleportLocList = new HashMap();
    private static final Map<Integer, Integer> _timeMap = new HashMap();

    public static NpcTeleportTable get() {
        if (_instance == null) {
            _instance = new NpcTeleportTable();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `npcaction_teleport` ORDER BY `id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String orderid = rs.getString("orderid");
                int locx = rs.getInt("locx");
                int locy = rs.getInt("locy");
                int mapid = rs.getInt("mapid");
                int itemid = rs.getInt("itemid");
                int price = rs.getInt("price");
                int time = rs.getInt("time");
                int user = rs.getInt("user");
                int min = rs.getInt("min");
                int max = rs.getInt("max");
                String note = rs.getString("note");
                L1TeleportLoc teleportLoc = new L1TeleportLoc();
                teleportLoc.set_id(id);
                teleportLoc.set_name(name);
                teleportLoc.set_orderid(orderid);
                teleportLoc.set_locx(locx);
                teleportLoc.set_locy(locy);
                teleportLoc.set_mapid(mapid);
                teleportLoc.set_itemid(itemid);
                teleportLoc.set_price(price);
                teleportLoc.set_user(user);
                teleportLoc.set_min(min);
                teleportLoc.set_max(max);
                if (name.equalsIgnoreCase(orderid)) {
                    String[] set = note.replace(" ", "").split(",");
                    int[] result = new int[set.length];
                    for (int i = 0; i < result.length; i++) {
                        result[i] = Integer.parseInt(set[i]);
                    }
                    int length = result.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        int npcid = result[i2];
                        L1Npc npc = NpcTable.get().getTemplate(npcid);
                        if (npc == null) {
                            del(id);
                        } else if (!npc.getImpl().equalsIgnoreCase("L1Teleporter")) {
                            del(id);
                        } else {
                            HashMap<String, L1TeleportLoc> list = _srcMap.get(Integer.valueOf(npcid));
                            if (list != null) {
                                list.put(orderid, teleportLoc);
                            } else {
                                list = new HashMap<>();
                                list.put(orderid, teleportLoc);
                            }
                            _srcMap.put(Integer.valueOf(npcid), list);
                        }
                    }
                } else if (price > 0) {
                    if (time != 0) {
                        _timeMap.put(Integer.valueOf(mapid), Integer.valueOf(time));
                    }
                    teleportLoc.set_time(time);
                    if (user != 0) {
                        _partyMap.put(Integer.valueOf(mapid), Integer.valueOf(user));
                    }
                    addList(orderid, teleportLoc);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入NPC傳送點設置數量: " + _teleportLocList.size() + "(" + timer.get() + "ms)");
        _log.info("載入時間地圖設置數量: " + _timeMap.size() + "(" + timer.get() + "ms)");
        _log.info("載入團隊地圖設置數量: " + _partyMap.size() + "(" + timer.get() + "ms)");
        _log.info("載入官方傳送點設置數量: " + _srcMap.size() + "(" + timer.get() + "ms)");
    }

    private void del(int id) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `npcaction_teleport` WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void addList(String orderid, L1TeleportLoc teleportLoc) {
        HashMap<Integer, L1TeleportLoc> map = _teleportLocList.get(orderid);
        if (map != null) {
            map.put(new Integer(map.size()), teleportLoc);
        } else {
            map = new HashMap<>();
            map.put(new Integer(0), teleportLoc);
        }
        _teleportLocList.put(orderid, map);
    }

    public Map<String, HashMap<Integer, L1TeleportLoc>> get_locs() {
        return _teleportLocList;
    }

    public Integer isPartyMap(int mapid) {
        return _partyMap.get(Integer.valueOf(mapid));
    }

    public Map<Integer, Integer> partyMaps() {
        return _partyMap;
    }

    public boolean isTimeMap(int mapid) {
        return _timeMap.get(Integer.valueOf(mapid)) != null;
    }

    public Map<Integer, Integer> timeMaps() {
        return _timeMap;
    }

    public HashMap<Integer, L1TeleportLoc> get_teles(String orderid) {
        if (_teleportLocList.get(orderid) != null) {
            return _teleportLocList.get(orderid);
        }
        return null;
    }

    public L1TeleportLoc get_loc(String orderid, int id) {
        HashMap<Integer, L1TeleportLoc> map = _teleportLocList.get(orderid);
        if (map != null) {
            return map.get(new Integer(id));
        }
        return null;
    }

    public boolean get_teleport(L1PcInstance pc, String orderid, int npcid) throws Exception {
        L1TeleportLoc t;
        HashMap<String, L1TeleportLoc> map = _srcMap.get(Integer.valueOf(npcid));
        if (map == null || (t = map.get(orderid)) == null) {
            return false;
        }
        if (t.get_price() > 0) {
            if (!pc.getInventory().checkItem(t.get_itemid(), (long) t.get_price())) {
                pc.sendPackets(new S_ServerMessage(337, ItemTable.get().getTemplate(t.get_itemid()).getName()));
                return true;
            }
            pc.getInventory().consumeItem(t.get_itemid(), (long) t.get_price());
        }
        L1Teleport.teleport(pc, t.get_locx(), t.get_locy(), (short) t.get_mapid(), 5, true);
        return true;
    }

    public void set(String orderid, int locx, int locy, int mapid, int price, String npcids) {
        if (npcids.equals("")) {
            npcids = "---";
        }
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `npcaction_teleport` SET `name`=?,`orderid`=?,`locx`=?,`locy`=?,`mapid`=?,`itemid`=?,`price`=?,`time`=?,`user`=?,`min`=?,`max`=?,`note`=?");
            int i = 0 + 1;
            ps.setString(i, orderid);
            int i2 = i + 1;
            ps.setString(i2, orderid);
            int i3 = i2 + 1;
            ps.setInt(i3, locx);
            int i4 = i3 + 1;
            ps.setInt(i4, locy);
            int i5 = i4 + 1;
            ps.setInt(i5, mapid);
            int i6 = i5 + 1;
            ps.setInt(i6, L1ItemId.ADENA);
            int i7 = i6 + 1;
            ps.setInt(i7, price);
            int i8 = i7 + 1;
            ps.setInt(i8, 0);
            int i9 = i8 + 1;
            ps.setInt(i9, 0);
            int i10 = i9 + 1;
            ps.setInt(i10, 0);
            int i11 = i10 + 1;
            ps.setInt(i11, 200);
            ps.setString(i11 + 1, npcids);
            ps.execute();
        } catch (SQLException e) {
            System.out.println("npcids:" + npcids);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
