package com.lineage.server.model.Instance;

import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Door;
import com.lineage.server.serverpackets.S_DoorPack;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DoorInstance extends L1NpcInstance {
    public static final int NOT_PASS = 65;
    public static final int PASS = 0;
    private static final Log _log = LogFactory.getLog(L1DoorInstance.class);
    private static final long serialVersionUID = 1;
    private int _crackStatus;
    private int _direction = 0;
    private int _doorId = 0;
    private int _keeperId = 0;
    private int _leftEdgeLocation = 0;
    private int _openStatus = 29;
    private int _passable = 65;
    private int _rightEdgeLocation = 0;

    public L1DoorInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            if (getMaxHp() != 0 && getMaxHp() != 1 && getCurrentHp() > 0 && !isDead()) {
                L1AttackMode attack = new L1AttackPc(pc, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                    attack.addChaserAttack();
                }
                attack.action();
                attack.commit();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                if (getOpenStatus() == 28) {
                    setOpenStatus(28);
                    setPassable(0);
                } else {
                    setOpenStatus(29);
                    setPassable(65);
                }
                perceivedFrom.sendPackets(new S_DoorPack(this));
                sendDoorPacket();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void deleteMe() {
        try {
            setPassable(0);
            sendDoorPacket();
            this._destroyed = true;
            if (getInventory() != null) {
                getInventory().clearItems();
            }
            allTargetClear();
            this._master = null;
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            removeAllKnownObjects();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        if (getMaxHp() != 0 && getMaxHp() != 1) {
            if (getCurrentHp() > 0 && !isDead()) {
                int newHp = getCurrentHp() - damage;
                if (newHp <= 0 && !isDead()) {
                    setStatus(37);
                    GeneralThreadPool.get().execute(new Death(attacker));
                }
                if (newHp > 0) {
                    setCurrentHp(newHp);
                    if ((getMaxHp() * 1) / 6 > getCurrentHp()) {
                        if (this._crackStatus != 5) {
                            broadcastPacketAll(new S_DoActionGFX(getId(), 36));
                            setStatus(36);
                            this._crackStatus = 5;
                        }
                    } else if ((getMaxHp() * 2) / 6 > getCurrentHp()) {
                        if (this._crackStatus != 4) {
                            broadcastPacketAll(new S_DoActionGFX(getId(), 35));
                            setStatus(35);
                            this._crackStatus = 4;
                        }
                    } else if ((getMaxHp() * 3) / 6 > getCurrentHp()) {
                        if (this._crackStatus != 3) {
                            broadcastPacketAll(new S_DoActionGFX(getId(), 34));
                            setStatus(34);
                            this._crackStatus = 3;
                        }
                    } else if ((getMaxHp() * 4) / 6 > getCurrentHp()) {
                        if (this._crackStatus != 2) {
                            broadcastPacketAll(new S_DoActionGFX(getId(), 33));
                            setStatus(33);
                            this._crackStatus = 2;
                        }
                    } else if ((getMaxHp() * 5) / 6 > getCurrentHp() && this._crackStatus != 1) {
                        broadcastPacketAll(new S_DoActionGFX(getId(), 32));
                        setStatus(32);
                        this._crackStatus = 1;
                    }
                }
            } else if (!isDead()) {
                setStatus(37);
                GeneralThreadPool.get().execute(new Death(attacker));
            }
        }
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentHp(int i) {
        int currentHp = Math.min(i, getMaxHp());
        if (getCurrentHp() != currentHp) {
            setCurrentHpDirect(currentHp);
        }
    }

    class Death implements Runnable {
        L1Character _lastAttacker;

        public Death(L1Character lastAttacker) {
            this._lastAttacker = lastAttacker;
        }

        public void run() {
            L1DoorInstance.this.setCurrentHpDirect(0);
            L1DoorInstance.this.setDead(true);
            L1DoorInstance.this.getMap().setPassable(L1DoorInstance.this.getLocation(), true);
            L1DoorInstance.this.broadcastPacketAll(new S_DoActionGFX(L1DoorInstance.this.getId(), 37));
            L1DoorInstance.this.setPassable(0);
            L1DoorInstance.this.setOpenStatus(28);
            L1DoorInstance.this.setPassable(0);
            L1DoorInstance.this.sendDoorPacket();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendDoorPacket() {
        int entranceX = getEntranceX();
        int entranceY = getEntranceY();
        int leftEdgeLocation = getLeftEdgeLocation();
        int rightEdgeLocation = getRightEdgeLocation();
        if (rightEdgeLocation - leftEdgeLocation == 0) {
            sendPacket(entranceX, entranceY);
        } else if (getDirection() == 0) {
            for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
                sendPacket(x, entranceY);
            }
        } else {
            for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
                sendPacket(entranceX, y);
            }
        }
    }

    private void sendPacket(int x, int y) {
        broadcastPacketAll(new S_Door(x, y, getDirection(), getPassable()));
    }

    private void set_open() {
        int entranceX = getEntranceX();
        int entranceY = getEntranceY();
        int leftEdgeLocation = getLeftEdgeLocation();
        int rightEdgeLocation = getRightEdgeLocation();
        if (getDirection() == 0) {
            getMap().setPassable(entranceX, entranceY, true, 0);
            for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
                getMap().setPassable(x, entranceY, true, 0);
            }
            return;
        }
        getMap().setPassable(entranceX, entranceY, true, 1);
        for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
            getMap().setPassable(entranceX, y, true, 1);
        }
    }

    public void open() {
        if (!isDead() && getOpenStatus() == 29) {
            setOpenStatus(28);
            setPassable(0);
            broadcastPacketAll(new S_DoActionGFX(getId(), 28));
            sendDoorPacket();
            set_open();
        }
    }

    private void set_close() {
        int entranceX = getEntranceX();
        int entranceY = getEntranceY();
        int leftEdgeLocation = getLeftEdgeLocation();
        int rightEdgeLocation = getRightEdgeLocation();
        if (getDirection() == 0) {
            getMap().setPassable(entranceX, entranceY, false, 0);
            for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
                getMap().setPassable(x, entranceY, false, 0);
            }
            return;
        }
        getMap().setPassable(entranceX, entranceY, false, 1);
        for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
            getMap().setPassable(entranceX, y, false, 1);
        }
    }

    public void close() {
        if (!isDead() && getOpenStatus() == 28) {
            setOpenStatus(29);
            setPassable(65);
            broadcastPacketAll(new S_DoActionGFX(getId(), 29));
            sendDoorPacket();
            set_close();
        }
    }

    public void repairGate() {
        if (getMaxHp() > 1) {
            setDead(false);
            setCurrentHp(getMaxHp());
            setStatus(0);
            setCrackStatus(0);
            setOpenStatus(28);
            close();
        }
    }

    public int getDoorId() {
        return this._doorId;
    }

    public void setDoorId(int i) {
        this._doorId = i;
    }

    public int getDirection() {
        return this._direction;
    }

    public void setDirection(int i) {
        if (i == 0 || i == 1) {
            this._direction = i;
        }
    }

    public int getEntranceX() {
        if (getDirection() == 0) {
            return getX();
        }
        return getX() - 1;
    }

    public int getEntranceY() {
        if (getDirection() == 0) {
            return getY() + 1;
        }
        return getY();
    }

    public int getLeftEdgeLocation() {
        return this._leftEdgeLocation;
    }

    public void setLeftEdgeLocation(int i) {
        this._leftEdgeLocation = i;
    }

    public int getRightEdgeLocation() {
        return this._rightEdgeLocation;
    }

    public void setRightEdgeLocation(int i) {
        this._rightEdgeLocation = i;
    }

    public int getOpenStatus() {
        return this._openStatus;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setOpenStatus(int i) {
        if (i == 28 || i == 29) {
            this._openStatus = i;
        }
    }

    public int getPassable() {
        return this._passable;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setPassable(int i) {
        if (i == 0 || i == 65) {
            this._passable = i;
        }
    }

    public int getKeeperId() {
        return this._keeperId;
    }

    public void setKeeperId(int i) {
        this._keeperId = i;
    }

    private void setCrackStatus(int i) {
        this._crackStatus = i;
    }

    public static void openDoor() {
        L1DoorInstance[] allDoor = DoorSpawnTable.get().getDoorList();
        if (allDoor.length > 0) {
            for (L1DoorInstance door : allDoor) {
                switch (door.getDoorId()) {
                    case L1SkillId.SEXP20 /*{ENCODED_INT: 5001}*/:
                    case L1SkillId.SEXP13 /*{ENCODED_INT: 5002}*/:
                    case L1SkillId.SEXP15 /*{ENCODED_INT: 5003}*/:
                    case L1SkillId.SEXP17 /*{ENCODED_INT: 5004}*/:
                    case L1SkillId.REEXP20 /*{ENCODED_INT: 5005}*/:
                    case 5006:
                    case 5007:
                    case 5008:
                    case 5009:
                    case 5010:
                    case 6006:
                    case 6007:
                    case 10000:
                    case 10001:
                    case 10002:
                    case 10003:
                    case 10004:
                    case 10005:
                    case 10006:
                    case 10007:
                    case 10008:
                    case 10009:
                    case 10010:
                    case 10011:
                    case 10012:
                    case 10013:
                    case 10015:
                    case 10016:
                    case 10017:
                    case 10019:
                    case 10020:
                    case 10036:
                        door.set_close();
                        break;
                    default:
                        if (door.getMaxHp() <= 1) {
                            if (door.getKeeperId() != 0) {
                                door.set_close();
                                break;
                            } else {
                                door.open();
                                break;
                            }
                        } else {
                            door.set_close();
                            break;
                        }
                }
            }
        }
    }

    public static void openGam(boolean isOpen) {
        L1DoorInstance[] allDoor = DoorSpawnTable.get().getDoorList();
        if (allDoor.length > 0) {
            for (L1DoorInstance door : allDoor) {
                switch (door.getDoorId()) {
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                        if (isOpen) {
                            door.open();
                            break;
                        } else {
                            door.close();
                            break;
                        }
                }
            }
        }
    }
}
