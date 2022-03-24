package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.GamblingStorage;
import com.lineage.server.templates.L1Gambling;
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

public class GamblingTable implements GamblingStorage {
    private static final Map<String, L1Gambling> _gamblingList = new HashMap();
    private static final Log _log = LogFactory.getLog(GamblingTable.class);

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_gambling` ORDER BY `id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                L1Gambling gambling = new L1Gambling();
                int id = rs.getInt("id");
                long adena = rs.getLong("adena");
                double rate = rs.getDouble("rate");
                String gamblingno = rs.getString("gamblingno");
                int outcount = rs.getInt("outcount");
                gambling.set_id(id);
                gambling.set_adena(adena);
                gambling.set_rate(rate);
                gambling.set_gamblingno(gamblingno);
                gambling.set_outcount(outcount);
                _gamblingList.put(gamblingno, gambling);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入奇岩賭場紀錄資料數量: " + _gamblingList.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public L1Gambling getGambling(String key) {
        return _gamblingList.get(key);
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public L1Gambling getGambling(int key) {
        for (L1Gambling gambling : _gamblingList.values()) {
            if (gambling.get_id() == key) {
                return gambling;
            }
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public void add(L1Gambling gambling) {
        Connection co = null;
        PreparedStatement ps = null;
        int id = gambling.get_id();
        String gamblingno = gambling.get_gamblingno();
        _gamblingList.put(gamblingno, gambling);
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `character_gambling` SET `id`=?,`adena`=?,`rate`=?,`gamblingno`=?,`outcount`=?,`r`=?");
            int i = 0 + 1;
            ps.setInt(i, id);
            int i2 = i + 1;
            ps.setLong(i2, gambling.get_adena());
            int i3 = i2 + 1;
            ps.setDouble(i3, gambling.get_rate());
            int i4 = i3 + 1;
            ps.setString(i4, gamblingno);
            int i5 = i4 + 1;
            ps.setInt(i5, gambling.get_outcount());
            ps.setInt(i5 + 1, gambling.get_outcount());
            ps.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public void updateGambling(int id, int outcount) {
        System.out.println("更新賭場紀錄" + id + "-" + outcount);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("UPDATE `character_gambling` SET `outcount`=? WHERE `id`=?");
            int i = 0 + 1;
            ps.setInt(i, outcount);
            ps.setInt(i + 1, id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public int[] winCount(int npcid) {
        int size = _gamblingList.size();
        int winCount = 0;
        for (L1Gambling gambling : _gamblingList.values()) {
            String no = gambling.get_gamblingno();
            if (npcid == Integer.parseInt(no.substring(no.indexOf("-") + 1))) {
                winCount++;
            }
        }
        return new int[]{size, winCount};
    }

    @Override // com.lineage.server.datatables.storage.GamblingStorage
    public int maxId() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_gambling` ORDER BY `id`");
            rs = ps.executeQuery();
            int id = 0;
            while (rs.next()) {
                id = rs.getInt("id") + 1;
            }
            if (id < 100) {
                id = 100;
            }
            return id;
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
            return 100;
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
