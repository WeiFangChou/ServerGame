package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactoryLogin;
import com.lineage.server.datatables.storage.EzpayStorage3;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EzpayTable3 implements EzpayStorage3 {
    private static final Map<String, CopyOnWriteArrayList<L1ItemInstance>> _itemList = new ConcurrentHashMap();
    private static final Log _log = LogFactory.getLog(EzpayTable3.class);

    @Override // com.lineage.server.datatables.storage.EzpayStorage3
    public Map<Integer, int[]> ezpayInfo(String loginName) {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, int[]> list = new HashMap<>();
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `即時發放獎勵` WHERE `指定發送玩家帳號`=? ORDER BY `流水號`");
            ps.setString(1, loginName.toLowerCase());
            rs = ps.executeQuery();
            while (rs.next()) {
                int[] value = new int[3];
                int out = rs.getInt("是否已送出");
                int ready = rs.getInt("是否給予開放");
                if (out == 0 && ready == 1) {
                    int key = rs.getInt("流水號");
                    int p_id = rs.getInt("商品道具編號");
                    int count = rs.getInt("商品數量");
                    value[0] = key;
                    value[1] = p_id;
                    value[2] = count;
                    list.put(Integer.valueOf(key), value);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
        return list;
    }

    @Override // com.lineage.server.datatables.storage.EzpayStorage3
    public void insertItem(String account_name, L1ItemInstance item) {
        _log.warn("帳號:" + account_name + " 加入即時發放獎勵數據:" + item.getItem().getName() + " OBJID:" + item.getId());
        addItem(account_name, item);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `即時發放獎勵` SET `流水號`=?,`指定發送玩家帳號`=?,`商品道具編號`= ?,`商品名稱`=?,`商品數量`=?");
            int i = 0 + 1;
            ps.setInt(i, item.getId());
            int i2 = i + 1;
            ps.setString(i2, account_name);
            int i3 = i2 + 1;
            ps.setInt(i3, item.getItemId());
            int i4 = i3 + 1;
            ps.setString(i4, item.getItem().getName());
            ps.setLong(i4 + 1, item.getCount());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    private static void addItem(String account_name, L1ItemInstance item) {
        CopyOnWriteArrayList<L1ItemInstance> list = _itemList.get(account_name);
        if (list == null) {
            list = new CopyOnWriteArrayList<>();
            if (!list.contains(item)) {
                list.add(item);
            }
        } else if (!list.contains(item)) {
            list.add(item);
        }
        if (World.get().findObject(item.getId()) == null) {
            World.get().storeObject(item);
        }
        _itemList.put(account_name, list);
    }

    private boolean is_holding(String loginName, int id) {
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactoryLogin.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `即時發放獎勵` WHERE `指定發送玩家帳號`=? AND `流水號`=?");
            ps.setString(1, loginName.toLowerCase());
            ps.setInt(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("是否已送出") != 0) {
                    SQLUtil.close(ps);
                    SQLUtil.close(co);
                    SQLUtil.close(rs);
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return true;
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
            SQLUtil.close(rs);
        }
    }

    @Override // com.lineage.server.datatables.storage.EzpayStorage3
    public boolean update(String loginName, int id, String pcname, String ip) {
        if (!is_holding(loginName, id)) {
            return false;
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `即時發放獎勵` SET `是否已送出`=1,`領取人名稱`=?,`領取時間`=?,`領取人ip`=? WHERE `流水號`=? AND `指定發送玩家帳號`=?");
            pstm.setString(1, pcname);
            pstm.setTimestamp(2, lastactive);
            pstm.setString(3, ip);
            pstm.setInt(4, id);
            pstm.setString(5, loginName);
            pstm.execute();
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
