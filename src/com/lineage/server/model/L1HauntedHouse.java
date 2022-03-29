package com.lineage.server.model;

import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class L1HauntedHouse {
    public static final int STATUS_NONE = 0;
    public static final int STATUS_PLAYING = 2;
    public static final int STATUS_READY = 1;
    private static L1HauntedHouse _instance;
    private int _goalCount = 0;
    private int _hauntedHouseStatus = 0;
    private final ArrayList<L1PcInstance> _members = new ArrayList<>();
    private int _winnersCount = 0;

    public static L1HauntedHouse getInstance() {
        if (_instance == null) {
            _instance = new L1HauntedHouse();
        }
        return _instance;
    }

    private void readyHauntedHouse() {
        setHauntedHouseStatus(1);
        new L1HauntedHouseReadyTimer().begin();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startHauntedHouse() {
        setHauntedHouseStatus(2);
        int membersCount = getMembersCount();
        if (membersCount <= 4) {
            setWinnersCount(1);
        } else if (5 >= membersCount && membersCount <= 7) {
            setWinnersCount(2);
        } else if (8 >= membersCount && membersCount <= 10) {
            setWinnersCount(3);
        }
        L1PcInstance[] membersArray = getMembersArray();
        for (L1PcInstance pc : membersArray) {
            new L1SkillUse().handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 1);
            L1PolyMorph.doPoly(pc, 6284, 300, 4);
        }
        for (L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.open();
                }
            }
        }
    }

    public void endHauntedHouse() {
        setHauntedHouseStatus(0);
        setWinnersCount(0);
        setGoalCount(0);
        L1PcInstance[] membersArray = getMembersArray();
        for (L1PcInstance pc : membersArray) {
            if (pc.getMapId() == 5140) {
                new L1SkillUse().handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 1);
                L1Teleport.teleport(pc, 32624, 32813,  4, 5, true);
            }
        }
        clearMembers();
        for (L1Object object : World.get().getObject()) {
            if (object instanceof L1DoorInstance) {
                L1DoorInstance door = (L1DoorInstance) object;
                if (door.getMapId() == 5140) {
                    door.close();
                }
            }
        }
    }

    public void removeRetiredMembers() {
        L1PcInstance[] temp = getMembersArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getMapId() != 5140) {
                removeMember(temp[i]);
            }
        }
    }

    public void sendMessage(int type, String msg) {
        for (L1PcInstance pc : getMembersArray()) {
            pc.sendPackets(new S_ServerMessage(type, msg));
        }
    }

    public void addMember(L1PcInstance pc) {
        if (!this._members.contains(pc)) {
            this._members.add(pc);
        }
        if (getMembersCount() == 1 && getHauntedHouseStatus() == 0) {
            readyHauntedHouse();
        }
    }

    public void removeMember(L1PcInstance pc) {
        this._members.remove(pc);
    }

    public void clearMembers() {
        this._members.clear();
    }

    public boolean isMember(L1PcInstance pc) {
        return this._members.contains(pc);
    }

    public L1PcInstance[] getMembersArray() {
        return (L1PcInstance[]) this._members.toArray(new L1PcInstance[this._members.size()]);
    }

    public int getMembersCount() {
        return this._members.size();
    }

    private void setHauntedHouseStatus(int i) {
        this._hauntedHouseStatus = i;
    }

    public int getHauntedHouseStatus() {
        return this._hauntedHouseStatus;
    }

    private void setWinnersCount(int i) {
        this._winnersCount = i;
    }

    public int getWinnersCount() {
        return this._winnersCount;
    }

    public void setGoalCount(int i) {
        this._goalCount = i;
    }

    public int getGoalCount() {
        return this._goalCount;
    }

    public class L1HauntedHouseReadyTimer extends TimerTask {
        public L1HauntedHouseReadyTimer() {
        }

        public void run() {
            L1HauntedHouse.this.startHauntedHouse();
            new L1HauntedHouseTimer().begin();
        }

        public void begin() {
            new Timer().schedule(this, 90000);
        }
    }

    public class L1HauntedHouseTimer extends TimerTask {
        public L1HauntedHouseTimer() {
        }

        public void run() {
            L1HauntedHouse.this.endHauntedHouse();
            cancel();
        }

        public void begin() {
            new Timer().schedule(this, 300000);
        }
    }
}
