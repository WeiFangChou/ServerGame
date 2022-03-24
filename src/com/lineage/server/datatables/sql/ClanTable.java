package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.storage.ClanStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.WorldClan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClanTable implements ClanStorage {
    private static final Log _log = LogFactory.getLog(ClanTable.class);
    private final Map<Integer, L1Clan> _clans = new HashMap();

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `clan_data` ORDER BY `clan_id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                L1Clan clan = new L1Clan();
                int clan_id = rs.getInt("clan_id");
                clan.setClanId(clan_id);
                clan.setClanName(rs.getString("clan_name"));
                clan.setLeaderId(rs.getInt("leader_id"));
                clan.setLeaderName(rs.getString("leader_name"));
                clan.setCastleId(rs.getInt("hascastle"));
                clan.setHouseId(rs.getInt("hashouse"));
                boolean clanskill = rs.getBoolean("clanskill");
                if (clanskill) {
                    clan.set_clanskill(clanskill);
                    clan.set_skilltime(rs.getTimestamp("skilltime"));
                }
                WorldClan.get().storeClan(clan);
                this._clans.put(Integer.valueOf(clan_id), clan);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入血盟資料資料數量: " + this._clans.size() + "(" + timer.get() + "ms)");
        Collection<L1Clan> AllClan = WorldClan.get().getAllClans();
        for (L1Clan clan2 : AllClan) {
            Connection cn2 = null;
            PreparedStatement ps2 = null;
            ResultSet rs2 = null;
            try {
                cn2 = DatabaseFactory.get().getConnection();
                ps2 = cn2.prepareStatement("SELECT `char_name` FROM `characters` WHERE `ClanID`=?");
                ps2.setInt(1, clan2.getClanId());
                rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    clan2.addMemberName(rs2.getString("char_name"));
                }
            } catch (SQLException e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            } finally {
                SQLUtil.close(rs2);
                SQLUtil.close(ps2);
                SQLUtil.close(cn2);
            }
        }
        for (L1Clan clan3 : AllClan) {
            clan3.getDwarfForClanInventory().loadItems();
        }
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public void addDeClan(Integer integer, L1Clan l1Clan) {
        WorldClan.get().storeClan(l1Clan);
        this._clans.put(integer, l1Clan);
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public L1Clan createClan(L1PcInstance player, String clan_name) {
        for (L1Clan oldClans : WorldClan.get().getAllClans()) {
            if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
                return null;
            }
        }
        L1Clan clan = new L1Clan();
        clan.setClanId(IdFactory.get().nextId());
        clan.setClanName(clan_name);
        clan.setLeaderId(player.getId());
        clan.setLeaderName(player.getName());
        clan.setCastleId(0);
        clan.setHouseId(0);
        clan.set_clanskill(false);
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `clan_data` SET `clan_id`=?,`clan_name`=?,`leader_id`=?,`leader_name`=?,`hascastle`=?,`hashouse`=?,`clanskill`=?,`skilltime`=?");
            int i = 0 + 1;
            ps.setInt(i, clan.getClanId());
            int i2 = i + 1;
            ps.setString(i2, clan.getClanName());
            int i3 = i2 + 1;
            ps.setInt(i3, clan.getLeaderId());
            int i4 = i3 + 1;
            ps.setString(i4, clan.getLeaderName());
            int i5 = i4 + 1;
            ps.setInt(i5, clan.getCastleId());
            int i6 = i5 + 1;
            ps.setInt(i6, clan.getHouseId());
            int i7 = i6 + 1;
            ps.setBoolean(i7, clan.isClanskill());
            ps.setTimestamp(i7 + 1, clan.get_skilltime());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        WorldClan.get().storeClan(clan);
        this._clans.put(Integer.valueOf(clan.getClanId()), clan);
        player.setClanid(clan.getClanId());
        player.setClanname(clan.getClanName());
        player.setClanRank(4);
        clan.addMemberName(player.getName());
        try {
            player.save();
            return clan;
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
            return clan;
        }
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public void updateClan(L1Clan clan) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE clan_data SET `clan_id`=?,`leader_id`=?,`leader_name`=?,`hascastle`=?,`hashouse`=?,`clanskill`=?,`skilltime`=? WHERE `clan_name`=?");
            int i = 0 + 1;
            ps.setInt(i, clan.getClanId());
            int i2 = i + 1;
            ps.setInt(i2, clan.getLeaderId());
            int i3 = i2 + 1;
            ps.setString(i3, clan.getLeaderName());
            int i4 = i3 + 1;
            ps.setInt(i4, clan.getCastleId());
            int i5 = i4 + 1;
            ps.setInt(i5, clan.getHouseId());
            int i6 = i5 + 1;
            ps.setBoolean(i6, clan.isClanskill());
            int i7 = i6 + 1;
            ps.setTimestamp(i7, clan.get_skilltime());
            ps.setString(i7 + 1, clan.getClanName());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public void deleteClan(String clan_name) {
        L1Clan clan = WorldClan.get().getClan(clan_name);
        if (clan != null) {
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                cn = DatabaseFactory.get().getConnection();
                ps = cn.prepareStatement("DELETE FROM `clan_data` WHERE `clan_name`=?");
                ps.setString(1, clan_name);
                ps.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(cn);
            }
            clan.getDwarfForClanInventory().clearItems();
            clan.getDwarfForClanInventory().deleteAllItems();
            WorldClan.get().removeClan(clan);
            this._clans.remove(Integer.valueOf(clan.getClanId()));
        }
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public L1Clan getTemplate(int clan_id) {
        return this._clans.get(Integer.valueOf(clan_id));
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public Map<Integer, L1Clan> get_clans() {
        return this._clans;
    }

    @Override // com.lineage.server.datatables.storage.ClanStorage
    public void updateClanOnlineMaxUser(L1Clan clan) {
    }
}
