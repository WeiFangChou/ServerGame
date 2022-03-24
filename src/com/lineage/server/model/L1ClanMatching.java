package com.lineage.server.model;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ClanMatching {
    private static L1ClanMatching _instance;
    private static final Log _log = LogFactory.getLog(L1ClanMatching.class);
    private ArrayList<ClanMatchingList> _list = new ArrayList<>();

    public static L1ClanMatching getInstance() {
        if (_instance == null) {
            _instance = new L1ClanMatching();
        }
        return _instance;
    }

    public void writeClanMatching(String clanname, String text, int htype) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO clan_matching_list SET clanname = ?, text = ?, type = ?");
            pstm.setString(1, clanname);
            pstm.setString(2, text);
            pstm.setInt(3, htype);
            addMatching(new ClanMatchingList(clanname, text, htype));
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void updateClanMatching(String clanname, String text, int htype) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE clan_matching_list SET text = ?, type = ? WHERE clanname = ?");
            pstm.setString(1, text);
            pstm.setInt(2, htype);
            pstm.setString(3, clanname);
            ClanMatchingList CML = getClanMatchingList(clanname);
            CML._text = text;
            CML._type = htype;
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatching(L1Clan clan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_list WHERE clanname=?");
            pstm.setString(1, clan.getClanName());
            pstm.execute();
            removeMatching(clan.getClanName());
            L1PcInstance[] onlineClanMember = clan.getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.getInviteList().clear();
                        break;
                }
            }
            for (L1PcInstance player : World.get().getAllPlayers()) {
                if (player.getClanid() == 0 && player.getCMAList().contains(clan.getClanName())) {
                    player.removeCMAList(clan.getClanName());
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatching(String clanname) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_list WHERE clanname=?");
            pstm.setString(1, clanname);
            pstm.execute();
            removeMatching(clanname);
            for (L1PcInstance player : World.get().getAllPlayers()) {
                if (player.getClanid() == 0 && player.getCMAList().contains(clanname)) {
                    player.removeCMAList(clanname);
                }
            }
            L1PcInstance[] onlineClanMember = WorldClan.get().getClan(clanname).getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.getInviteList().clear();
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatching(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_list WHERE clanname=?");
            pstm.setString(1, pc.getClanname());
            pstm.execute();
            removeMatching(pc.getClanname());
            L1PcInstance[] onlineClanMember = pc.getClan().getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.getInviteList().clear();
                        break;
                }
            }
            for (L1PcInstance player : World.get().getAllPlayers()) {
                if (player.getClanid() == 0 && player.getCMAList().contains(pc.getClanname())) {
                    player.removeCMAList(pc.getClanname());
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void loadClanMatching() {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM clan_matching_list");
            rs = pstm.executeQuery();
            while (rs.next()) {
                addMatching(new ClanMatchingList(rs.getString("clanname"), rs.getString("text"), rs.getInt("type")));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void writeClanMatchingApcList_User(L1PcInstance pc, L1Clan clan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO clan_matching_apclist SET pc_name=?, pc_objid=?, clan_name=?, clan_id=?");
            pstm.setString(1, pc.getName());
            pstm.setInt(2, pc.getId());
            pstm.setString(3, clan.getClanName());
            pstm.setInt(4, clan.getClanId());
            pstm.execute();
            pc.addCMAList(clan.getClanName());
            L1PcInstance[] onlineClanMember = clan.getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.addInviteList(pc.getName());
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void loadClanMatchingApcList_User(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM clan_matching_apclist WHERE pc_name = ?");
            pstm.setString(1, pc.getName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                pc.addCMAList(rs.getString("clan_name"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void loadClanMatchingApcList_Crown(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM clan_matching_apclist WHERE clan_name = ?");
            pstm.setString(1, pc.getClanname());
            rs = pstm.executeQuery();
            while (rs.next()) {
                pc.addInviteList(rs.getString("pc_name"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE pc_name=?");
            pstm.setString(1, pc.getName());
            pstm.execute();
            pc.getCMAList().clear();
            L1PcInstance[] onlineClanMember = pc.getClan().getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.removeInviteList(pc.getName());
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(int clanid) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE clan_id=?");
            pstm.setInt(1, clanid);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(L1Clan clan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE clan_name=?");
            pstm.setString(1, clan.getClanName());
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(String clanname) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE clan_name=?");
            pstm.setString(1, clanname);
            pstm.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(L1Clan clan, String charname) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE clan_id=? AND clan_name=?");
            pstm.setInt(1, clan.getClanId());
            pstm.setString(2, clan.getClanName());
            pstm.execute();
            L1PcInstance[] onlineClanMember = clan.getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.removeInviteList(charname);
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(L1PcInstance pc, int objid, L1Clan clan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE pc_objid=? AND clan_name=?");
            pstm.setInt(1, objid);
            pstm.setString(2, clan.getClanName());
            pstm.execute();
            pc.removeCMAList(clan.getClanName());
            String pcname = pc.getName();
            L1PcInstance[] onlineClanMember = clan.getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.removeInviteList(pcname);
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void deleteClanMatchingApcList(L1PcInstance pc, L1Clan clan) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM clan_matching_apclist WHERE pc_name=? AND clan_name=?");
            pstm.setString(1, pc.getName());
            pstm.setString(2, clan.getClanName());
            pstm.execute();
            pc.removeCMAList(clan.getClanName());
            L1PcInstance[] onlineClanMember = clan.getOnlineClanMember();
            for (L1PcInstance clanuser : onlineClanMember) {
                switch (clanuser.getClanRank()) {
                    case 3:
                    case 4:
                    case 6:
                    case 9:
                    case 10:
                        clanuser.removeInviteList(pc.getName());
                        break;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    public void addMatching(ClanMatchingList list) {
        if (!this._list.contains(list)) {
            this._list.add(list);
        }
    }

    public void removeMatching(String clanname) {
        if (isClanMatchingList(clanname)) {
            this._list.remove(getClanMatchingList(clanname));
        }
    }

    public ArrayList<ClanMatchingList> getMatchingList() {
        return this._list;
    }

    public boolean isClanMatchingList(String clanname) {
        for (int i = 0; i < this._list.size(); i++) {
            if (this._list.get(i)._clanname.equalsIgnoreCase(clanname)) {
                return true;
            }
        }
        return false;
    }

    public ClanMatchingList getClanMatchingList(String clanname) {
        for (int i = 0; i < this._list.size(); i++) {
            if (this._list.get(i)._clanname.equalsIgnoreCase(clanname)) {
                return this._list.get(i);
            }
        }
        return null;
    }

    public static class ClanMatchingList {
        public String _clanname = null;
        public String _text = null;
        public int _type = 0;

        public ClanMatchingList(String clanname, String text, int type) {
            this._clanname = clanname;
            this._text = text;
            this._type = type;
        }
    }
}
