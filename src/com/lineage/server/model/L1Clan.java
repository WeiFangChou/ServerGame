package com.lineage.server.model;

import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.world.World;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Clan {
    public static final int ALLIANCE_CLAN_RANK_ATTEND = 5;
    public static final int ALLIANCE_CLAN_RANK_GUARDIAN = 6;
    public static final int CLAN_RANK_GUARDIAN = 3;
    public static final int CLAN_RANK_PRINCE = 4;
    public static final int CLAN_RANK_PROBATION = 1;
    public static final int CLAN_RANK_PUBLIC = 2;
    public static final int NORMAL_CLAN_RANK_ATTEND = 8;
    public static final int NORMAL_CLAN_RANK_GENERAL = 7;
    public static final int NORMAL_CLAN_RANK_GUARDIAN = 9;
    public static final int NORMAL_CLAN_RANK_PRINCE = 10;
    private static final Log _log = LogFactory.getLog(L1Clan.class);
    private int _castleId;
    private int _clanId;
    private String _clanName;
    private boolean _clanskill = false;
    private final L1DwarfForClanInventory _dwarfForClan = new L1DwarfForClanInventory(this);
    private int _houseId;
    private int _leaderId;
    private String _leaderName;
    private final Lock _lock = new ReentrantLock(true);
    private int _maxuser;
    private final ArrayList<String> _membersNameList = new ArrayList<>();
    String[] _rank = {"", "", "一般", "副君主", "聯盟君主", "修習騎士", "守護騎士", "一般", "修習騎士", "守護騎士", "聯盟君主"};
    private Timestamp _skilltime = null;
    private int _warehouse = 0;

    public int getClanId() {
        return this._clanId;
    }

    public void setClanId(int clan_id) {
        this._clanId = clan_id;
    }

    public String getClanName() {
        return this._clanName;
    }

    public void setClanName(String clan_name) {
        this._clanName = clan_name;
    }

    public int getLeaderId() {
        return this._leaderId;
    }

    public void setLeaderId(int leader_id) {
        this._leaderId = leader_id;
    }

    public String getLeaderName() {
        return this._leaderName;
    }

    public void setLeaderName(String leader_name) {
        this._leaderName = leader_name;
    }

    public int getCastleId() {
        return this._castleId;
    }

    public void setCastleId(int hasCastle) {
        this._castleId = hasCastle;
    }

    public int getHouseId() {
        return this._houseId;
    }

    public void setHouseId(int hasHideout) {
        this._houseId = hasHideout;
    }

    public void CheckClan_Exp20(L1PcInstance leavePc) {
        L1PcInstance[] clanMembers = getOnlineClanMember();
        int OnlineMemberSize = getOnlineClanMemberSize();
        if (leavePc != null && leavePc.hasSkillEffect(L1SkillId.CLAN_EXP20)) {
            leavePc.removeNoTimerSkillEffect(L1SkillId.CLAN_EXP20);
            leavePc.set_expadd(-20);
            leavePc.sendPackets(new S_PacketBox(165, 0));
        }
        if (OnlineMemberSize >= 3) {
            for (L1PcInstance clanMember : clanMembers) {
                if (!clanMember.hasSkillEffect(L1SkillId.CLAN_EXP20)) {
                    clanMember.setSkillEffect(L1SkillId.CLAN_EXP20, 0);
                    clanMember.set_expadd(20);
                    clanMember.sendPackets(new S_PacketBox(165, 1));
                }
            }
            return;
        }
        for (L1PcInstance clanMember2 : clanMembers) {
            if (clanMember2.hasSkillEffect(L1SkillId.CLAN_EXP20)) {
                clanMember2.removeNoTimerSkillEffect(L1SkillId.CLAN_EXP20);
                clanMember2.set_expadd(-20);
                clanMember2.sendPackets(new S_PacketBox(165, 0));
            }
        }
    }

    public void addMemberName(String member_name) {
        this._lock.lock();
        try {
            if (!this._membersNameList.contains(member_name)) {
                this._membersNameList.add(member_name);
            }
            CheckClan_Exp20(null);
        } finally {
            this._lock.unlock();
        }
    }

    public void delMemberName(String member_name) {
        this._lock.lock();
        try {
            if (this._membersNameList.contains(member_name)) {
                this._membersNameList.remove(member_name);
            }
            CheckClan_Exp20(World.get().getPlayer(member_name));
        } finally {
            this._lock.unlock();
        }
    }

    public int getOnlineClanMemberSize() {
        int count = 0;
        try {
            Iterator<String> it = this._membersNameList.iterator();
            while (it.hasNext()) {
                if (World.get().getPlayer(it.next()) != null) {
                    count++;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return count;
    }

    public int getAllMembersSize() {
        try {
            return this._membersNameList.size();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public void sendPacketsAll(ServerBasePacket packet) {
        try {
            for (Object nameobj : this._membersNameList.toArray()) {
                L1PcInstance pc = World.get().getPlayer((String) nameobj);
                if (pc != null) {
                    pc.sendPackets(packet);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1PcInstance[] getOnlineClanMember() {
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<L1PcInstance> onlineMembers = new ArrayList<>();
        try {
            temp.addAll(this._membersNameList);
            Iterator<String> it = temp.iterator();
            while (it.hasNext()) {
                L1PcInstance pc = World.get().getPlayer(it.next());
                if (pc != null && !onlineMembers.contains(pc)) {
                    onlineMembers.add(pc);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return (L1PcInstance[]) onlineMembers.toArray(new L1PcInstance[onlineMembers.size()]);
    }

    public StringBuilder getOnlineMembersFP() {
        ArrayList<String> temp = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);
            Iterator<String> it = temp.iterator();
            while (it.hasNext()) {
                String name = it.next();
                if (World.get().getPlayer(name) != null) {
                    result.append(String.valueOf(name) + " ");
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    public StringBuilder getAllMembersFP() {
        ArrayList<String> temp = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);
            Iterator<String> it = temp.iterator();
            while (it.hasNext()) {
                result.append(String.valueOf(it.next()) + " ");
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    public StringBuilder getOnlineMembersFPWithRank() {
        ArrayList<String> temp = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);
            Iterator<String> it = temp.iterator();
            while (it.hasNext()) {
                String name = it.next();
                L1PcInstance pc = World.get().getPlayer(name);
                if (pc != null) {
                    result.append(String.valueOf(name) + getRankString(pc) + " ");
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    public StringBuilder getAllMembersFPWithRank() {
        ArrayList<String> temp = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        try {
            temp.addAll(this._membersNameList);
            Iterator<String> it = temp.iterator();
            while (it.hasNext()) {
                String name = it.next();
                if (CharacterTable.get().restoreCharacter(name) != null) {
                    result.append(String.valueOf(name) + " ");
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    private String getRankString(L1PcInstance pc) {
        if (pc == null || pc.getClanRank() <= 0) {
            return "";
        }
        return this._rank[pc.getClanRank()];
    }

    public String[] getAllMembers() {
        return (String[]) this._membersNameList.toArray(new String[this._membersNameList.size()]);
    }

    public L1DwarfForClanInventory getDwarfForClanInventory() {
        return this._dwarfForClan;
    }

    public int getWarehouseUsingChar() {
        return this._warehouse;
    }

    public void setWarehouseUsingChar(int objid) {
        this._warehouse = objid;
    }

    public void set_clanskill(boolean boolean1) {
        this._clanskill = boolean1;
    }

    public boolean isClanskill() {
        return this._clanskill;
    }

    public void set_skilltime(Timestamp skilltime) {
        this._skilltime = skilltime;
    }

    public Timestamp get_skilltime() {
        return this._skilltime;
    }

    public int getOnlineMaxUser() {
        return this._maxuser;
    }

    public void setOnlineMaxUser(int i) {
        this._maxuser = i;
    }
}
