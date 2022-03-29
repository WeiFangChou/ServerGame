package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Party {
    public static final int MAXPT = 8;
    private static final Log _log = LogFactory.getLog(L1Party.class);
    private boolean _isLeader = false;
    private L1PcInstance _leader = null;
    private final ConcurrentHashMap<Integer, L1PcInstance> _membersList = new ConcurrentHashMap<>();

    public void addMember(L1PcInstance pc) {
        int key = 1;
        if (pc == null) {
            try {
                throw new NullPointerException();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else if (this._membersList.size() != 8 && !this._membersList.contains(pc)) {
            boolean hpUpdate = false;
            if (this._membersList.isEmpty()) {
                setLeader(pc);
            } else {
                hpUpdate = true;
            }
            while (this._membersList.get(Integer.valueOf(key)) != null) {
                key++;
            }
            this._membersList.put(Integer.valueOf(key), pc);
            pc.setParty(this);
            for (L1PcInstance member : this._membersList.values()) {
                if (!member.equals(this._leader)) {
                    member.sendPackets(new S_PacketBoxParty(member, this));
                } else if (this._isLeader) {
                    member.sendPackets(new S_PacketBoxParty(pc));
                }
                member.sendPackets(new S_PacketBoxParty(this, member));
            }
            if (!this._isLeader) {
                this._isLeader = true;
            }
            if (hpUpdate) {
                createMiniHp(pc);
            }
        }
    }

    private void removeMember(L1PcInstance pc) {
        int removeKey = -1;
        try {
            for (Integer num : this._membersList.keySet()) {
                int key = num.intValue();
                if (pc.equals(this._membersList.get(Integer.valueOf(key)))) {
                    removeKey = key;
                }
            }
            if (removeKey != -1) {
                this._membersList.remove(Integer.valueOf(removeKey));
                pc.setParty(null);
                if (!this._membersList.isEmpty()) {
                    deleteMiniHp(pc);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1PcInstance partyUser() {
        L1PcInstance user = null;
        for (L1PcInstance pc : this._membersList.values()) {
            if (user == null && !this._leader.equals(pc)) {
                user = pc;
            }
        }
        return user;
    }

    public int partyUserInMap(int mapid) {
        int i = 0;
        if (this._membersList.isEmpty()) {
            return 0;
        }
        if (this._membersList.size() <= 0) {
            return 0;
        }
        for (L1PcInstance tgpc : this._membersList.values()) {
            if (tgpc.getMapId() == mapid) {
                i++;
            }
        }
        return i;
    }

    public boolean isVacancy() {
        return this._membersList.size() < 8;
    }

    public int getVacancy() {
        return 8 - this._membersList.size();
    }

    public boolean isMember(L1PcInstance pc) {
        for (L1PcInstance tgpc : this._membersList.values()) {
            if (pc.equals(tgpc)) {
                return true;
            }
        }
        return false;
    }

    public void setLeader(L1PcInstance pc) {
        this._leader = pc;
    }

    public L1PcInstance getLeader() {
        return this._leader;
    }

    public int getLeaderID() {
        return this._leader.getId();
    }

    public boolean isLeader(L1PcInstance pc) {
        return pc.getId() == this._leader.getId();
    }

    public String getMembersNameList() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this._membersList.isEmpty() || this._membersList.size() <= 0) {
            return null;
        }
        Iterator<L1PcInstance> it = this._membersList.values().iterator();
        while (it.hasNext()) {
            stringBuilder.append(String.valueOf(it.next().getName()) + " ");
        }
        return stringBuilder.toString();
    }

    private void createMiniHp(L1PcInstance pc) {
        if (!this._membersList.isEmpty() && this._membersList.size() > 0) {
            for (L1PcInstance member : this._membersList.values()) {
                member.sendPackets(new S_HPMeter(pc.getId(), (pc.getCurrentHp() * 100) / pc.getMaxHp()));
                pc.sendPackets(new S_HPMeter(member.getId(), (member.getCurrentHp() * 100) / member.getMaxHp()));
            }
        }
    }

    private void deleteMiniHp(L1PcInstance pc) {
        if (!this._membersList.isEmpty() && this._membersList.size() > 0) {
            for (L1PcInstance member : this._membersList.values()) {
                pc.sendPackets(new S_HPMeter(member.getId(), 255));
                member.sendPackets(new S_HPMeter(pc.getId(), 255));
            }
        }
    }

    public void updateMiniHP(L1PcInstance pc) {
        if (!this._membersList.isEmpty() && this._membersList.size() > 0) {
            for (L1PcInstance member : this._membersList.values()) {
                member.sendPackets(new S_HPMeter(pc.getId(), (pc.getCurrentHp() * 100) / pc.getMaxHp()));
            }
        }
    }

    public void breakup() {
        if (!this._membersList.isEmpty() && this._membersList.size() > 0) {
            for (L1PcInstance member : this._membersList.values()) {
                removeMember(member);
                member.sendPackets(new S_ServerMessage(418));
            }
        }
    }

    public void leaveMember(L1PcInstance pc) {
        if (isLeader(pc)) {
            breakup();
        } else if (getNumOfMembers() == 2) {
            removeMember(pc);
            L1PcInstance leader = getLeader();
            removeMember(leader);
            sendLeftMessage(pc, pc);
            sendLeftMessage(leader, pc);
            leader.sendPackets(new S_ServerMessage(418));
            pc.sendPackets(new S_ServerMessage(418));
        } else {
            removeMember(pc);
            for (L1PcInstance member : this._membersList.values()) {
                sendLeftMessage(member, pc);
            }
            sendLeftMessage(pc, pc);
        }
    }

    public void kickMember(L1PcInstance pc) {
        if (getNumOfMembers() == 2) {
            removeMember(pc);
            L1PcInstance leader = getLeader();
            removeMember(leader);
            sendLeftMessage(pc, pc);
            sendLeftMessage(leader, pc);
            leader.sendPackets(new S_ServerMessage(418));
            pc.sendPackets(new S_ServerMessage(418));
        } else {
            removeMember(pc);
            for (L1PcInstance member : this._membersList.values()) {
                sendLeftMessage(member, pc);
            }
            sendLeftMessage(pc, pc);
        }
        pc.sendPackets(new S_ServerMessage(419));
    }

    public ConcurrentHashMap<Integer, L1PcInstance> partyUsers() {
        return this._membersList;
    }

    public int getNumOfMembers() {
        return this._membersList.size();
    }

    private void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
        sendTo.sendPackets(new S_ServerMessage(420, left.getName()));
    }

    public void msgToAll() {
        for (L1PcInstance member : this._membersList.values()) {
            if (!member.equals(this._leader)) {
                member.sendPackets(new S_PacketBoxParty(this._leader.getId(), this._leader.getName()));
            }
        }
    }
}
