package com.lineage.server.model;

import com.lineage.config.ConfigOther;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1ItemDelay;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.poison.L1Poison;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillStop;
import com.lineage.server.model.skill.L1SkillTimer;
import com.lineage.server.model.skill.L1SkillTimerCreator;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_Light;
import com.lineage.server.serverpackets.S_PetCtrlMenu;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Character extends L1Object {
    protected static final byte[] HEADING_TABLE_X;
    protected static final byte[] HEADING_TABLE_Y;
    private static final Log _log = LogFactory.getLog(L1Character.class);
    private static final long serialVersionUID = 1;
    private int _ac = 10;
    private int _addAttrKind;
    private int _bowDmgup = 0;
    private int _bowHitup = 0;
    private int _braveSpeed;
    private short _cha = 0;
    private int _chaLightSize;
    private short _con = 0;
    private int _currentHp;
    private int _currentMp;
    private short _dex = 0;
    private int _dmgup = 0;
    private int _dodge_down = 0;
    private int _dodge_up = 0;
    private final Map<Integer, L1DollInstance> _dolls = new HashMap();
    private int _earth = 0;
    private long _exp;
    private int _fire = 0;
    private final Map<Integer, L1FollowerInstance> _followerlist = new HashMap();
    private int _gfxid;
    private int _heading;
    private int _hitup = 0;
    private int _innKeyId;
    private int _innRoomNumber;
    private short _int = 0;
    private boolean _isDead;
    private boolean _isHall;
    private int _isShowDrop = 0;
    private boolean _isSkillDelay = false;
    private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap();
    private int _karma;
    private final List<L1Object> _knownObjects = new CopyOnWriteArrayList();
    private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList();
    private int _lawful;
    private int _level = 1;
    private int _maxHp = 0;
    private short _maxMp = 0;
    private int _moveSpeed;
    private int _mr = 0;
    private String _name;
    private int _ownLightSize;
    L1Paralysis _paralysis;
    private boolean _paralyzed;
    private final Map<Integer, L1NpcInstance> _petlist = new HashMap();
    private L1Poison _poison = null;
    private int _registBlind = 0;
    private int _registFreeze = 0;
    private int _registSleep = 0;
    private int _registStone = 0;
    private int _registStun = 0;
    private int _registSustain = 0;
    private byte _ringsExpansion;
    private int _secHp = -1;
    private boolean _send_weapon_dmg_gfxid = true;
    private final HashMap<Integer, L1SkillTimer> _skillEffect = new HashMap<>();
    private boolean _sleeped;
    private int _sp = 0;
    private int _status;
    private short _str = 0;
    private int _tempCharGfx;
    private int _temp_adena = 0;
    private String _title;
    private int _tmp;
    private int _tmp_mr;
    private int _trueAc = 0;
    private int _trueBowDmgup = 0;
    private int _trueBowHitup = 0;
    private short _trueCha = 0;
    private short _trueCon = 0;
    private short _trueDex = 0;
    private int _trueDmgup = 0;
    private int _trueEarth = 0;
    private int _trueFire = 0;
    private int _trueHitup = 0;
    private short _trueInt = 0;
    private int _trueMaxHp = 0;
    private int _trueMaxMp = 0;
    private int _trueMr = 0;
    private int _trueRegistBlind = 0;
    private int _trueRegistFreeze = 0;
    private int _trueRegistSleep = 0;
    private int _trueRegistStone = 0;
    private int _trueRegistStun = 0;
    private int _trueRegistSustain = 0;
    private short _trueStr = 0;
    private int _trueWater = 0;
    private int _trueWind = 0;
    private short _trueWis = 0;
    private int _water = 0;
    private int _wind = 0;
    private short _wis = 0;

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
    }

    public void resurrect(int hp) {
        try {
            if (isDead()) {
                if (hp <= 0) {
                    hp = 1;
                }
                setDead(false);
                setCurrentHp(hp);
                setStatus(0);
                L1PolyMorph.undoPoly(this);
                Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
                while (it.hasNext()) {
                    L1PcInstance pc = it.next();
                    pc.sendPackets(new S_RemoveObject(this));
                    pc.removeKnownObject(this);
                    pc.updateObject();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void broadcastPacketHP(L1PcInstance pc) {
        try {
            if (this._secHp != getCurrentHp()) {
                this._secHp = getCurrentHp();
                pc.sendPackets(new S_HPMeter(this));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getCurrentHp() {
        return this._currentHp;
    }

    public void setCurrentHp(int i) {
        this._currentHp = i;
        if (this._currentHp >= getMaxHp()) {
            this._currentHp = getMaxHp();
        }
    }

    public void setCurrentHpDirect(int i) {
        this._currentHp = i;
    }

    public int getCurrentMp() {
        return this._currentMp;
    }

    public void setCurrentMp(int i) {
        this._currentMp = i;
        if (this._currentMp >= getMaxMp()) {
            this._currentMp = getMaxMp();
        }
    }

    public void setCurrentMpDirect(int i) {
        this._currentMp = i;
    }

    public boolean isSleeped() {
        return this._sleeped;
    }

    public void setSleeped(boolean sleeped) {
        this._sleeped = sleeped;
    }

    public boolean isParalyzedX() {
        if (!hasSkillEffect(50) && !hasSkillEffect(80) && !hasSkillEffect(194) && !hasSkillEffect(157) && !hasSkillEffect(87) && !hasSkillEffect(208) && !hasSkillEffect(33)) {
            return false;
        }
        return true;
    }

    public boolean isParalyzed() {
        return this._paralyzed;
    }

    public void setParalyzed(boolean paralyzed) {
        this._paralyzed = paralyzed;
    }

    public L1Paralysis getParalysis() {
        return this._paralysis;
    }

    public void setParalaysis(L1Paralysis p) {
        this._paralysis = p;
    }

    public void cureParalaysis() {
        if (this._paralysis != null) {
            this._paralysis.cure();
        }
    }

    public void broadcastPacketAll(ServerBasePacket packet) {
        try {
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                if (pc.get_showId() == get_showId()) {
                    pc.sendPackets(packet);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void broadcastPacketX10(ServerBasePacket packet) {
        try {
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this, 10).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                if (pc.get_showId() == get_showId()) {
                    pc.sendPackets(packet);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void broadcastPacketX8(ServerBasePacket packet) {
        try {
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this, 8).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                if (pc.get_showId() == get_showId()) {
                    pc.sendPackets(packet);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void broadcastPacketXR(ServerBasePacket packet, int r) {
        try {
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this, r).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                if (pc.get_showId() == get_showId()) {
                    pc.sendPackets(packet);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void wideBroadcastPacket(ServerBasePacket packet) {
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this, 50).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            if (pc.get_showId() == get_showId()) {
                pc.sendPackets(packet);
            }
        }
    }

    public void broadcastPacketExceptTargetSight(ServerBasePacket packet, L1Character target) {
        boolean isX8 = false;
        if (ServerWarExecutor.get().checkCastleWar() > 0) {
            isX8 = true;
        }
        if (isX8) {
            Iterator<L1PcInstance> it = World.get().getVisiblePlayerExceptTargetSight(this, target, 8).iterator();
            while (it.hasNext()) {
                it.next().sendPackets(packet);
            }
            return;
        }
        Iterator<L1PcInstance> it2 = World.get().getVisiblePlayerExceptTargetSight(this, target).iterator();
        while (it2.hasNext()) {
            it2.next().sendPackets(packet);
        }
    }

    public int[] getFrontLoc() {
        int x = getX();
        int y = getY();
        int heading = getHeading();
        return new int[]{x + HEADING_TABLE_X[heading], y + HEADING_TABLE_Y[heading]};
    }

    public int targetDirection(int tx, int ty) {
        float dis_x = (float) Math.abs(getX() - tx);
        float dis_y = (float) Math.abs(getY() - ty);
        float dis = Math.max(dis_x, dis_y);
        if (dis == 0.0f) {
            return getHeading();
        }
        int avg_x = (int) Math.floor((double) ((dis_x / dis) + 0.59f));
        int avg_y = (int) Math.floor((double) ((dis_y / dis) + 0.59f));
        int dir_x = 0;
        int dir_y = 0;
        if (getX() < tx) {
            dir_x = 1;
        }
        if (getX() > tx) {
            dir_x = -1;
        }
        if (getY() < ty) {
            dir_y = 1;
        }
        if (getY() > ty) {
            dir_y = -1;
        }
        if (avg_x == 0) {
            dir_x = 0;
        }
        if (avg_y == 0) {
            dir_y = 0;
        }
        switch (dir_x) {
            case -1:
                switch (dir_y) {
                    case -1:
                        return 7;
                    case 0:
                        return 6;
                    case 1:
                        return 5;
                }
            case 0:
                switch (dir_y) {
                    case -1:
                        return 0;
                    case 1:
                        return 4;
                }
            case 1:
                switch (dir_y) {
                    case -1:
                        return 1;
                    case 0:
                        return 2;
                    case 1:
                        return 3;
                }
        }
        return getHeading();
    }

    public boolean glanceCheck(int tx, int ty) {
        L1Map map = getMap();
        int chx = getX();
        int chy = getY();
        for (int i = 0; i < 15 && ((chx != tx || chy != ty) && ((chx + 1 != tx || chy - 1 != ty) && ((chx + 1 != tx || chy != ty) && ((chx + 1 != tx || chy + 1 != ty) && ((chx != tx || chy + 1 != ty) && ((chx - 1 != tx || chy + 1 != ty) && ((chx - 1 != tx || chy != ty) && ((chx - 1 != tx || chy - 1 != ty) && (chx != tx || chy - 1 != ty))))))))); i++) {
            if (!map.isArrowPassable(chx, chy, targetDirection(tx, ty))) {
                return false;
            }
            if (chx < tx) {
                if (chy == ty) {
                    chx++;
                } else if (chy > ty) {
                    chx++;
                    chy--;
                } else if (chy < ty) {
                    chx++;
                    chy++;
                }
            } else if (chx == tx) {
                if (chy < ty) {
                    chy++;
                } else if (chy > ty) {
                    chy--;
                }
            } else if (chx > tx) {
                if (chy == ty) {
                    chx--;
                } else if (chy < ty) {
                    chx--;
                    chy++;
                } else if (chy > ty) {
                    chx--;
                    chy--;
                }
            }
        }
        return true;
    }

    public boolean isAttackPosition(int x, int y, int range) {
        if (range >= 7) {
            if (getLocation().getTileDistance(new Point(x, y)) > range) {
                return false;
            }
        } else if (getLocation().getTileLineDistance(new Point(x, y)) > range) {
            return false;
        }
        return glanceCheck(x, y);
    }

    public L1Inventory getInventory() {
        return null;
    }

    private void addSkillEffect(int skillId, int timeMillis) {
        L1SkillTimer timer = null;
        if (timeMillis > 0) {
            timer = L1SkillTimerCreator.create(this, skillId, timeMillis);
            timer.begin();
        }
        this._skillEffect.put(Integer.valueOf(skillId), timer);
    }

    public void setSkillEffect(int skillId, int timeMillis) {
        if (hasSkillEffect(skillId)) {
            int remainingTimeMills = getSkillEffectTimeSec(skillId) * L1SkillId.STATUS_BRAVE;
            if (remainingTimeMills < 0) {
                return;
            }
            if (remainingTimeMills < timeMillis || timeMillis == 0) {
                killSkillEffectTimer(skillId);
                addSkillEffect(skillId, timeMillis);
                return;
            }
            return;
        }
        addSkillEffect(skillId, timeMillis);
    }

    public void removeSkillEffect(int skillId) {
        L1SkillTimer timer = this._skillEffect.remove(Integer.valueOf(skillId));
        if (timer != null) {
            timer.end();
        }
    }

    public void removeNoTimerSkillEffect(int skillId) {
        this._skillEffect.remove(Integer.valueOf(skillId));
        L1SkillStop.stopSkill(this, skillId);
    }

    public void clearAllSkill() {
        for (L1SkillTimer timer : this._skillEffect.values()) {
            if (timer != null) {
                timer.end();
            }
        }
        this._skillEffect.clear();
    }

    public void killSkillEffectTimer(int skillId) {
        L1SkillTimer timer = this._skillEffect.remove(Integer.valueOf(skillId));
        if (timer != null) {
            timer.kill();
        }
    }

    public void clearSkillEffectTimer() {
        for (L1SkillTimer timer : this._skillEffect.values()) {
            if (timer != null) {
                timer.kill();
            }
        }
        this._skillEffect.clear();
    }

    public boolean hasSkillEffect(int skillId) {
        return this._skillEffect.containsKey(Integer.valueOf(skillId));
    }

    public Set<Integer> getSkillEffect() {
        return this._skillEffect.keySet();
    }

    public boolean getSkillisEmpty() {
        return this._skillEffect.isEmpty();
    }

    public int getSkillEffectTimeSec(int skillId) {
        L1SkillTimer timer = this._skillEffect.get(Integer.valueOf(skillId));
        if (timer == null) {
            return -1;
        }
        return timer.getRemainingTime();
    }

    public void setSkillDelay(boolean flag) {
        this._isSkillDelay = flag;
    }

    public boolean isSkillDelay() {
        return this._isSkillDelay;
    }

    public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
        this._itemdelay.put(Integer.valueOf(delayId), timer);
    }

    public void removeItemDelay(int delayId) {
        this._itemdelay.remove(Integer.valueOf(delayId));
    }

    public boolean hasItemDelay(int delayId) {
        return this._itemdelay.containsKey(Integer.valueOf(delayId));
    }

    public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
        return this._itemdelay.get(Integer.valueOf(delayId));
    }

    public void addPet(L1NpcInstance npc) {
        this._petlist.put(Integer.valueOf(npc.getId()), npc);
        sendPetCtrlMenu(npc, true);
    }

    public void removePet(L1NpcInstance npc) {
        this._petlist.remove(Integer.valueOf(npc.getId()));
        sendPetCtrlMenu(npc, false);
    }

    public Map<Integer, L1NpcInstance> getPetList() {
        return this._petlist;
    }

    private final void sendPetCtrlMenu(L1NpcInstance npc, boolean type) {
        if (npc instanceof L1PetInstance) {
            L1PetInstance pet = (L1PetInstance) npc;
            L1Character cha = pet.getMaster();
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_PetCtrlMenu(pc, pet, type));
                if (type) {
                    pc.sendPackets(new S_HPMeter(pet));
                }
            }
        } else if (npc instanceof L1SummonInstance) {
            L1SummonInstance summon = (L1SummonInstance) npc;
            L1Character cha2 = summon.getMaster();
            if (cha2 instanceof L1PcInstance) {
                L1PcInstance pc2 = (L1PcInstance) cha2;
                pc2.sendPackets(new S_PetCtrlMenu(pc2, summon, type));
                if (type) {
                    pc2.sendPackets(new S_HPMeter(summon));
                }
            }
        }
    }

    public void addDoll(L1DollInstance doll) {
        this._dolls.put(Integer.valueOf(doll.getItemObjId()), doll);
    }

    public void removeDoll(L1DollInstance doll) {
        this._dolls.remove(Integer.valueOf(doll.getItemObjId()));
    }

    public L1DollInstance getDoll(int key) {
        return this._dolls.get(Integer.valueOf(key));
    }

    public Map<Integer, L1DollInstance> getDolls() {
        return this._dolls;
    }

    public void addFollower(L1FollowerInstance follower) {
        this._followerlist.put(Integer.valueOf(follower.getId()), follower);
    }

    public void removeFollower(L1FollowerInstance follower) {
        this._followerlist.remove(Integer.valueOf(follower.getId()));
    }

    public Map<Integer, L1FollowerInstance> getFollowerList() {
        return this._followerlist;
    }

    public void setPoison(L1Poison poison) {
        this._poison = poison;
    }

    public void curePoison() {
        if (this._poison != null) {
            this._poison.cure();
        }
    }

    public L1Poison getPoison() {
        return this._poison;
    }

    public void setPoisonEffect(int effectId) {
        broadcastPacketX8(new S_Poison(getId(), effectId));
    }

    public int getZoneType() {
        if (getMap().isSafetyZone(getLocation())) {
            return 1;
        }
        if (getMap().isCombatZone(getLocation())) {
            return -1;
        }
        return 0;
    }

    public boolean isSafetyZone() {
        if (getMap().isSafetyZone(getLocation())) {
            return true;
        }
        return false;
    }

    public boolean isCombatZone() {
        if (getMap().isCombatZone(getLocation())) {
            return true;
        }
        return false;
    }

    public boolean isNoneZone() {
        return getZoneType() == 0;
    }

    public long getExp() {
        return this._exp;
    }

    public void setExp(long exp) {
        this._exp = exp;
    }

    public boolean knownsObject(L1Object obj) {
        return this._knownObjects.contains(obj);
    }

    public List<L1Object> getKnownObjects() {
        return this._knownObjects;
    }

    public List<L1PcInstance> getKnownPlayers() {
        return this._knownPlayer;
    }

    public void addKnownObject(L1Object obj) {
        if (!this._knownObjects.contains(obj)) {
            this._knownObjects.add(obj);
            if (obj instanceof L1PcInstance) {
                this._knownPlayer.add((L1PcInstance) obj);
            }
        }
    }

    public void removeKnownObject(L1Object obj) {
        this._knownObjects.remove(obj);
        if (obj instanceof L1PcInstance) {
            this._knownPlayer.remove(obj);
        }
    }

    public void removeAllKnownObjects() {
        this._knownObjects.clear();
        this._knownPlayer.clear();
    }

    public String getName() {
        return this._name;
    }

    public void setName(String s) {
        this._name = s;
    }

    public synchronized int getLevel() {
        return this._level;
    }

    public synchronized void setLevel(int level) {
        this._level = level;
    }

    public int getMaxHp() {
        return this._maxHp;
    }

    public void setMaxHp(int hp) {
        this._trueMaxHp = hp;
        this._maxHp = RangeInt.ensure(this._trueMaxHp, 1, 500000);
        this._currentHp = Math.min(this._currentHp, this._maxHp);
    }

    public void addMaxHp(int i) {
        setMaxHp(this._trueMaxHp + i);
    }

    public short getMaxMp() {
        return this._maxMp;
    }

    public void setMaxMp(int mp) {
        this._trueMaxMp = mp;
        this._maxMp = (short) RangeInt.ensure(this._trueMaxMp, 0, 32767);
        this._currentMp = Math.min(this._currentMp, (int) this._maxMp);
    }

    public void addMaxMp(int i) {
        setMaxMp(this._trueMaxMp + i);
    }

    public int getAc() {
        int ac = this._ac;
        if (this instanceof L1PcInstance) {
            switch (((L1PcInstance) this).guardianEncounter()) {
                case 0:
                    ac -= 2;
                    break;
                case 1:
                    ac -= 4;
                    break;
                case 2:
                    ac -= 6;
                    break;
            }
        }
        return RangeInt.ensure(ac, -211, 44);
    }

    public void setAc(int i) {
        this._trueAc = i;
        this._ac = RangeInt.ensure(i, -211, 44);
    }

    public void addAc(int i) {
        setAc(this._trueAc + i);
    }

    public short getStr() {
        return this._str;
    }

    public void setStr(int i) {
        this._trueStr = (short) i;
        this._str = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addStr(int i) {
        setStr(this._trueStr + i);
    }

    public short getCon() {
        return this._con;
    }

    public void setCon(int i) {
        this._trueCon = (short) i;
        this._con = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addCon(int i) {
        setCon(this._trueCon + i);
    }

    public short getDex() {
        return this._dex;
    }

    public void setDex(int i) {
        this._trueDex = (short) i;
        this._dex = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addDex(int i) {
        setDex(this._trueDex + i);
    }

    public short getCha() {
        return this._cha;
    }

    public void setCha(int i) {
        this._trueCha = (short) i;
        this._cha = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addCha(int i) {
        setCha(this._trueCha + i);
    }

    public short getInt() {
        return this._int;
    }

    public void setInt(int i) {
        this._trueInt = (short) i;
        this._int = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addInt(int i) {
        setInt(this._trueInt + i);
    }

    public short getWis() {
        return this._wis;
    }

    public void setWis(int i) {
        this._trueWis = (short) i;
        this._wis = (short) RangeInt.ensure(i, 1, OpcodesServer.S_OPCODE_SHOWSHOPBUYLIST);
    }

    public void addWis(int i) {
        setWis(this._trueWis + i);
    }

    public int getWind() {
        return this._wind;
    }

    public void addWind(int i) {
        this._trueWind += i;
        if (this._trueWind >= 127) {
            this._wind = 127;
        } else if (this._trueWind <= -128) {
            this._wind = -128;
        } else {
            this._wind = this._trueWind;
        }
    }

    public int getWater() {
        return this._water;
    }

    public void addWater(int i) {
        this._trueWater += i;
        if (this._trueWater >= 127) {
            this._water = 127;
        } else if (this._trueWater <= -128) {
            this._water = -128;
        } else {
            this._water = this._trueWater;
        }
    }

    public int getFire() {
        return this._fire;
    }

    public void addFire(int i) {
        this._trueFire += i;
        if (this._trueFire >= 127) {
            this._fire = 127;
        } else if (this._trueFire <= -128) {
            this._fire = -128;
        } else {
            this._fire = this._trueFire;
        }
    }

    public int getEarth() {
        return this._earth;
    }

    public void addEarth(int i) {
        this._trueEarth += i;
        if (this._trueEarth >= 127) {
            this._earth = 127;
        } else if (this._trueEarth <= -128) {
            this._earth = -128;
        } else {
            this._earth = this._trueEarth;
        }
    }

    public int getAddAttrKind() {
        return this._addAttrKind;
    }

    public void setAddAttrKind(int i) {
        this._addAttrKind = i;
    }

    public int getRegistStun() {
        return this._registStun;
    }

    public void addRegistStun(int i) {
        this._trueRegistStun += i;
        if (this._trueRegistStun > 127) {
            this._registStun = 127;
        } else if (this._trueRegistStun < -128) {
            this._registStun = -128;
        } else {
            this._registStun = this._trueRegistStun;
        }
    }

    public int getRegistStone() {
        return this._registStone;
    }

    public void addRegistStone(int i) {
        this._trueRegistStone += i;
        if (this._trueRegistStone > 127) {
            this._registStone = 127;
        } else if (this._trueRegistStone < -128) {
            this._registStone = -128;
        } else {
            this._registStone = this._trueRegistStone;
        }
    }

    public int getRegistSleep() {
        return this._registSleep;
    }

    public void addRegistSleep(int i) {
        this._trueRegistSleep += i;
        if (this._trueRegistSleep > 127) {
            this._registSleep = 127;
        } else if (this._trueRegistSleep < -128) {
            this._registSleep = -128;
        } else {
            this._registSleep = this._trueRegistSleep;
        }
    }

    public int getRegistFreeze() {
        return this._registFreeze;
    }

    public void add_regist_freeze(int i) {
        this._trueRegistFreeze += i;
        if (this._trueRegistFreeze > 127) {
            this._registFreeze = 127;
        } else if (this._trueRegistFreeze < -128) {
            this._registFreeze = -128;
        } else {
            this._registFreeze = this._trueRegistFreeze;
        }
    }

    public int getRegistSustain() {
        return this._registSustain;
    }

    public void addRegistSustain(int i) {
        this._trueRegistSustain += i;
        if (this._trueRegistSustain > 127) {
            this._registSustain = 127;
        } else if (this._trueRegistSustain < -128) {
            this._registSustain = -128;
        } else {
            this._registSustain = this._trueRegistSustain;
        }
    }

    public int getRegistBlind() {
        return this._registBlind;
    }

    public void addRegistBlind(int i) {
        this._trueRegistBlind += i;
        if (this._trueRegistBlind > 127) {
            this._registBlind = 127;
        } else if (this._trueRegistBlind < -128) {
            this._registBlind = -128;
        } else {
            this._registBlind = this._trueRegistBlind;
        }
    }

    public int getDmgup() {
        return this._dmgup;
    }

    public void addDmgup(int i) {
        this._trueDmgup += i;
        if (this._trueDmgup >= 127) {
            this._dmgup = 127;
        } else if (this._trueDmgup <= -128) {
            this._dmgup = -128;
        } else {
            this._dmgup = this._trueDmgup;
        }
    }

    public int getBowDmgup() {
        return this._bowDmgup;
    }

    public void addBowDmgup(int i) {
        this._trueBowDmgup += i;
        if (this._trueBowDmgup >= 127) {
            this._bowDmgup = 127;
        } else if (this._trueBowDmgup <= -128) {
            this._bowDmgup = -128;
        } else {
            this._bowDmgup = this._trueBowDmgup;
        }
    }

    public int getHitup() {
        return this._hitup;
    }

    public void addHitup(int i) {
        this._trueHitup += i;
        if (this._trueHitup >= 127) {
            this._hitup = 127;
        } else if (this._trueHitup <= -128) {
            this._hitup = -128;
        } else {
            this._hitup = this._trueHitup;
        }
    }

    public int getBowHitup() {
        return this._bowHitup;
    }

    public void addBowHitup(int i) {
        this._trueBowHitup += i;
        if (this._trueBowHitup >= 127) {
            this._bowHitup = 127;
        } else if (this._trueBowHitup <= -128) {
            this._bowHitup = -128;
        } else {
            this._bowHitup = this._trueBowHitup;
        }
    }

    public int getMr() {
        if (hasSkillEffect(153)) {
            return this._mr >> 2;
        }
        return this._mr;
    }

    public int getTrueMr() {
        return this._trueMr;
    }

    public void addMr(int i) {
        this._trueMr += i;
        if (this._trueMr <= 0) {
            this._mr = 0;
        } else {
            this._mr = this._trueMr;
        }
    }

    public int getSp() {
        return getTrueSp() + this._sp;
    }

    public int getTrueSp() {
        return getMagicLevel() + getMagicBonus();
    }

    public void addSp(int i) {
        this._sp += i;
    }

    public boolean isDead() {
        return this._isDead;
    }

    public void setDead(boolean flag) {
        this._isDead = flag;
    }

    public int getStatus() {
        return this._status;
    }

    public void setStatus(int i) {
        this._status = i;
    }

    public String getTitle() {
        return this._title;
    }

    public void setTitle(String s) {
        this._title = s;
    }

    public int getLawful() {
        return this._lawful;
    }

    public void setLawful(int i) {
        this._lawful = i;
    }

    public synchronized void addLawful(int i) {
        this._lawful += i;
        if (this._lawful > 32767) {
            this._lawful = 32767;
        } else if (this._lawful < -32768) {
            this._lawful = -32768;
        }
    }

    public int getHeading() {
        return this._heading;
    }

    public void setHeading(int i) {
        this._heading = i;
    }

    public int getMoveSpeed() {
        return this._moveSpeed;
    }

    public void setMoveSpeed(int i) {
        this._moveSpeed = i;
    }

    public int getBraveSpeed() {
        return this._braveSpeed;
    }

    public void setBraveSpeed(int i) {
        this._braveSpeed = i;
    }

    public int getTempCharGfx() {
        return this._tempCharGfx;
    }

    public void setTempCharGfx(int i) {
        this._tempCharGfx = i;
    }

    public int getGfxId() {
        return this._gfxid;
    }

    public void setGfxId(int i) {
        this._gfxid = i;
    }

    public int getMagicLevel() {
        return getLevel() >> 2;
    }

    public int getMagicBonus() {
        switch (getInt()) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return -2;
            case 6:
            case 7:
            case 8:
                return -1;
            case 9:
            case 10:
            case 11:
                return 0;
            case 12:
            case 13:
            case 14:
                return 1;
            case 15:
            case 16:
            case 17:
                return 2;
            case 18:
                return 3;
            case 19:
                return 4;
            case 20:
                return 5;
            case 21:
                return 6;
            case 22:
                return 7;
            case 23:
                return 8;
            case 24:
                return 9;
            case 25:
            case 26:
            case 27:
                return 10;
            case 28:
            case 29:
            case 30:
                return 11;
            case 31:
            case 32:
            case 33:
                return 12;
            case 34:
            case 35:
            case 36:
                return 13;
            case 37:
            case 38:
            case 39:
                return 14;
            case 40:
            case 41:
            case 42:
            case 43:
                return 15;
            default:
                return getInt() - 28;
        }
    }

    public boolean isInvisble() {
        return hasSkillEffect(60) || hasSkillEffect(97);
    }

    public void healHp(int pt) {
        setCurrentHp(getCurrentHp() + pt);
    }

    public int getKarma() {
        return this._karma;
    }

    public void setKarma(int karma) {
        this._karma = karma;
    }

    public void setMr(int i) {
        this._trueMr = i;
        if (this._trueMr <= 0) {
            this._mr = 0;
        } else {
            this._mr = this._trueMr;
        }
    }

    public void turnOnOffLight() {
        int itemlightSize;
        int lightSize = 0;
        if (this instanceof L1NpcInstance) {
            lightSize = ((L1NpcInstance) this).getLightSize();
        }
        for (L1ItemInstance item : getInventory().getItems()) {
            if (item.getItem().getType2() == 0 && item.getItem().getType() == 2 && (itemlightSize = item.getItem().getLightRange()) != 0 && item.isNowLighting() && itemlightSize > lightSize) {
                lightSize = itemlightSize;
            }
        }
        if (hasSkillEffect(2)) {
            lightSize = 14;
        }
        if (this instanceof L1PcInstance) {
            if (ConfigOther.LIGHT) {
                lightSize = 20;
            }
            L1PcInstance pc = (L1PcInstance) this;
            pc.sendPackets(new S_Light(pc.getId(), lightSize));
        }
        if (!isInvisble()) {
            broadcastPacketAll(new S_Light(getId(), lightSize));
        }
        setOwnLightSize(lightSize);
        setChaLightSize(lightSize);
    }

    public int getChaLightSize() {
        if (isInvisble()) {
            return 0;
        }
        if (ConfigOther.LIGHT) {
            return 14;
        }
        return this._chaLightSize;
    }

    public void setChaLightSize(int i) {
        this._chaLightSize = i;
    }

    public int getOwnLightSize() {
        if (isInvisble()) {
            return 0;
        }
        if (ConfigOther.LIGHT) {
            return 14;
        }
        return this._ownLightSize;
    }

    public void setOwnLightSize(int i) {
        this._ownLightSize = i;
    }

    public int get_tmp() {
        return this._tmp;
    }

    public void set_tmp(int tmp) {
        this._tmp = tmp;
    }

    public int get_tmp_mr() {
        return this._tmp_mr;
    }

    public void set_tmp_mr(int tmp) {
        this._tmp_mr = tmp;
    }

    public int get_dodge() {
        return this._dodge_up;
    }

    public void add_dodge(int i) {
        this._dodge_up += i;
        if (this._dodge_up > 10) {
            this._dodge_up = 10;
        } else if (this._dodge_up < 0) {
            this._dodge_up = 0;
        }
    }

    public int get_dodge_down() {
        return this._dodge_down;
    }

    public void add_dodge_down(int i) {
        this._dodge_down += i;
        if (this._dodge_down > 10) {
            this._dodge_down = 10;
        } else if (this._dodge_down < 0) {
            this._dodge_down = 0;
        }
    }

    public final byte getRingsExpansion() {
        return this._ringsExpansion;
    }

    public final void setRingsExpansion(byte i) {
        this._ringsExpansion = i;
    }

    public int getInnRoomNumber() {
        return this._innRoomNumber;
    }

    public void setInnRoomNumber(int i) {
        this._innRoomNumber = i;
    }

    public boolean checkRoomOrHall() {
        return this._isHall;
    }

    public void setHall(boolean i) {
        this._isHall = i;
    }

    public int getInnKeyId() {
        return this._innKeyId;
    }

    public void setInnKeyId(int i) {
        this._innKeyId = i;
    }

    public void set_temp_adena(int itemid) {
        this._temp_adena = itemid;
    }

    public int get_temp_adena() {
        return this._temp_adena;
    }

    public void set_send_weapon_dmg_gfxid(boolean flag) {
        this._send_weapon_dmg_gfxid = flag;
    }

    public boolean is_send_weapon_dmg_gfxid() {
        return this._send_weapon_dmg_gfxid;
    }

    public int setShowDrop() {
        return this._isShowDrop;
    }

    public void isShowDrop(int isShowDrop) {
        this._isShowDrop = isShowDrop;
    }
}
