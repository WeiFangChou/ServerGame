package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ChatParty {
    private static final Log _log = LogFactory.getLog(L1ChatParty.class);
    private L1PcInstance _leader = null;
    private final List<L1PcInstance> _membersList = new ArrayList();

    public void addMember(L1PcInstance pc) {
        if (pc == null) {
            try {
                throw new NullPointerException();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else if (!this._membersList.contains(pc)) {
            if (this._membersList.isEmpty()) {
                setLeader(pc);
            }
            this._membersList.add(pc);
            pc.setChatParty(this);
        }
    }

    private void removeMember(L1PcInstance pc) {
        try {
            if (this._membersList.contains(pc)) {
                this._membersList.remove(pc);
                pc.setChatParty(null);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean isVacancy() {
        return this._membersList.size() < 8;
    }

    public int getVacancy() {
        return 8 - this._membersList.size();
    }

    public boolean isMember(L1PcInstance pc) {
        return this._membersList.contains(pc);
    }

    private void setLeader(L1PcInstance pc) {
        this._leader = pc;
    }

    public L1PcInstance getLeader() {
        return this._leader;
    }

    public boolean isLeader(L1PcInstance pc) {
        return pc.getId() == this._leader.getId();
    }

    public String getMembersNameList() {
        String _result = new String("");
        Iterator<L1PcInstance> it = this._membersList.iterator();
        while (it.hasNext()) {
            _result = String.valueOf(_result) + it.next().getName() + " ";
        }
        return _result;
    }

    private void breakup() {
        try {
            L1PcInstance[] members = getMembers();
            for (L1PcInstance member : members) {
                removeMember(member);
                member.sendPackets(new S_ServerMessage(418));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void leaveMember(L1PcInstance pc) {
        try {
            L1PcInstance[] members = getMembers();
            if (isLeader(pc)) {
                breakup();
            } else if (getNumOfMembers() == 2) {
                removeMember(pc);
                L1PcInstance leader = getLeader();
                removeMember(leader);
                sendLeftMessage(pc, pc);
                sendLeftMessage(leader, pc);
            } else {
                removeMember(pc);
                for (L1PcInstance member : members) {
                    sendLeftMessage(member, pc);
                }
                sendLeftMessage(pc, pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void kickMember(L1PcInstance pc) {
        try {
            if (getNumOfMembers() == 2) {
                removeMember(pc);
                removeMember(getLeader());
            } else {
                removeMember(pc);
            }
            pc.sendPackets(new S_ServerMessage(419));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public L1PcInstance[] getMembers() {
        return (L1PcInstance[]) this._membersList.toArray(new L1PcInstance[this._membersList.size()]);
    }

    public int getNumOfMembers() {
        return this._membersList.size();
    }

    private void sendLeftMessage(L1PcInstance sendTo, L1PcInstance left) {
        try {
            sendTo.sendPackets(new S_ServerMessage(420, left.getName()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
