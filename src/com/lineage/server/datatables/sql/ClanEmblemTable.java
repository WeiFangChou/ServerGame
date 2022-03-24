package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.storage.ClanEmblemStorage;
import com.lineage.server.templates.L1EmblemIcon;
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

public class ClanEmblemTable implements ClanEmblemStorage {
    private static final Map<Integer, L1EmblemIcon> _iconList = new HashMap();
    private static final Log _log = LogFactory.getLog(ClanEmblemTable.class);

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `clan_emblem`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                int clanid = rs.getInt("clan_id");
                if (ClanReading.get().getTemplate(clanid) != null) {
                    byte[] icon = rs.getBytes("emblemicon");
                    int update = rs.getInt("update");
                    L1EmblemIcon emblemIcon = new L1EmblemIcon();
                    emblemIcon.set_clanid(clanid);
                    emblemIcon.set_clanIcon(icon);
                    emblemIcon.set_update(update);
                    _iconList.put(Integer.valueOf(clanid), emblemIcon);
                } else {
                    deleteIcon(clanid);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入盟輝圖檔紀錄資料數量: " + _iconList.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public L1EmblemIcon get(int clan_id) {
        return _iconList.get(Integer.valueOf(clan_id));
    }

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public void add(int clan_id, byte[] icon) {
        L1EmblemIcon emblemIcon = new L1EmblemIcon();
        emblemIcon.set_clanid(clan_id);
        emblemIcon.set_clanIcon(icon);
        emblemIcon.set_update(0);
        _iconList.put(Integer.valueOf(clan_id), emblemIcon);
    }

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public void deleteIcon(int clan_id) {
        _iconList.remove(Integer.valueOf(clan_id));
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `clan_emblem` WHERE `clan_id`=?");
            ps.setInt(1, clan_id);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public L1EmblemIcon storeClanIcon(int clan_id, byte[] icon) {
        L1EmblemIcon emblemIcon = new L1EmblemIcon();
        emblemIcon.set_clanid(clan_id);
        emblemIcon.set_clanIcon(icon);
        emblemIcon.set_update(0);
        _iconList.put(Integer.valueOf(clan_id), emblemIcon);
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `clan_emblem` SET `clan_id`=?,`emblemicon`=?,`update`=?");
            int i = 0 + 1;
            pstm.setInt(i, clan_id);
            int i2 = i + 1;
            pstm.setBytes(i2, icon);
            pstm.setInt(i2 + 1, 0);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        return emblemIcon;
    }

    @Override // com.lineage.server.datatables.storage.ClanEmblemStorage
    public void updateClanIcon(L1EmblemIcon emblemIcon) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE `clan_emblem` SET `emblemicon`=?,`update`=? WHERE `clan_id`=?");
            int i = 0 + 1;
            pstm.setBytes(i, emblemIcon.get_clanIcon());
            int i2 = i + 1;
            pstm.setInt(i2, emblemIcon.get_update());
            pstm.setInt(i2 + 1, emblemIcon.get_clanid());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
