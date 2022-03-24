package com.lineage.server.model.Instance;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_FollowerPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1FollowerInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1FollowerInstance.class);
    private static final long serialVersionUID = 1;

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public boolean noTarget() {
        if (this.ATTACK != null) {
            L1PcInstance pc = null;
            if (this._master instanceof L1PcInstance) {
                pc = (L1PcInstance) this._master;
            }
            this.ATTACK.attack(pc, this);
        } else {
            Iterator<L1Object> it = World.get().getVisibleObjects(this).iterator();
            while (it.hasNext()) {
                L1Object object = it.next();
                if (object instanceof L1NpcInstance) {
                    L1NpcInstance tgnpc = (L1NpcInstance) object;
                    if (tgnpc.getNpcTemplate().get_npcId() == 71061 && getNpcTemplate().get_npcId() == 71062) {
                        if (getLocation().getTileLineDistance(this._master.getLocation()) < 3) {
                            L1PcInstance pc2 = (L1PcInstance) this._master;
                            if (pc2.getX() >= 32448 && pc2.getX() <= 32452 && pc2.getY() >= 33048 && pc2.getY() <= 33052 && pc2.getMapId() == 440) {
                                setParalyzed(true);
                                if (!pc2.getInventory().checkItem(40711)) {
                                    createNewItem(pc2, 40711, serialVersionUID);
                                    pc2.getQuest().set_step(31, 3);
                                }
                                deleteMe();
                                return true;
                            }
                        } else {
                            continue;
                        }
                    } else if (tgnpc.getNpcTemplate().get_npcId() == 71074 && getNpcTemplate().get_npcId() == 71075 && getLocation().getTileLineDistance(this._master.getLocation()) < 3) {
                        L1PcInstance pc3 = (L1PcInstance) this._master;
                        if (pc3.getX() >= 32731 && pc3.getX() <= 32735 && pc3.getY() >= 32854 && pc3.getY() <= 32858 && pc3.getMapId() == 480) {
                            setParalyzed(true);
                            if (!pc3.getInventory().checkItem(40633)) {
                                createNewItem(pc3, 40633, serialVersionUID);
                                pc3.getQuest().set_step(34, 2);
                            }
                            deleteMe();
                            return true;
                        }
                    }
                }
            }
        }
        if (destroyed()) {
            return true;
        }
        if (this._master == null) {
            return true;
        }
        if (this._master.isDead() || getLocation().getTileLineDistance(this._master.getLocation()) > 13) {
            setParalyzed(true);
            spawn(getNpcTemplate().get_npcId(), getX(), getY(), getHeading(), getMapId());
            deleteMe();
            return true;
        } else if (this._master == null || this._master.getMapId() != getMapId() || getLocation().getTileLineDistance(this._master.getLocation()) <= 2 || this._npcMove == null) {
            return false;
        } else {
            this._npcMove.setDirectionMove(this._npcMove.moveDirection(this._master.getX(), this._master.getY()));
            setSleepTime(calcSleepTime(getPassispeed(), 0));
            return false;
        }
    }

    public L1FollowerInstance(L1Npc template, L1NpcInstance target, L1Character master) {
        super(template);
        if (!m3is(master)) {
            this._master = master;
            setId(IdFactoryNpc.get().nextId());
            set_showId(master.get_showId());
            setMaster(master);
            setX(target.getX());
            setY(target.getY());
            setMap(target.getMapId());
            setHeading(target.getHeading());
            setLightSize(target.getLightSize());
            target.setParalyzed(true);
            target.deleteMe();
            World.get().storeObject(this);
            World.get().addVisibleObject(this);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                onPerceive(it.next());
            }
            onNpcAI();
            master.addFollower(this);
        }
    }

    /* renamed from: is */
    private boolean m3is(L1Character master) {
        if (master.getFollowerList().size() > 0) {
            return true;
        }
        return false;
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            startAI();
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public synchronized void deleteMe() {
        this._master.removeFollower(this);
        setMaster(null);
        getMap().setPassable(getLocation(), true);
        super.deleteMe();
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            L1AttackMode attack = new L1AttackPc(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
            }
            attack.action();
            attack.commit();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
        if (!isDead()) {
            switch (getNpcTemplate().get_npcId()) {
                case 71062:
                    if (this._master.equals(player)) {
                        player.sendPackets(new S_NPCTalkReturn(getId(), "kamit2"));
                        return;
                    } else {
                        player.sendPackets(new S_NPCTalkReturn(getId(), "kamit1"));
                        return;
                    }
                case 71075:
                    if (this._master.equals(player)) {
                        player.sendPackets(new S_NPCTalkReturn(getId(), "llizard2"));
                        return;
                    } else {
                        player.sendPackets(new S_NPCTalkReturn(getId(), "llizard1a"));
                        return;
                    }
                default:
                    return;
            }
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_FollowerPack(this, perceivedFrom));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void createNewItem(L1PcInstance pc, int item_id, long count) {
        L1ItemInstance item = ItemTable.get().createItem(item_id);
        item.setCount(count);
        if (item != null) {
            if (pc.getInventory().checkAddItem(item, count) == 0) {
                pc.getInventory().storeItem(item);
            } else {
                item.set_showId(pc.get_showId());
                World.get().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
            }
            pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
        }
    }

    public void spawn(int npcId, int x, int y, int h, short m) {
        try {
            L1QuestInstance newnpc = (L1QuestInstance) L1SpawnUtil.spawn(npcId, x, y, m, h);
            newnpc.onNpcAI();
            newnpc.startChat(0);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        if (getMaxHp() <= 100) {
            return;
        }
        if (getCurrentHp() > 0 && !isDead()) {
            int newHp = getCurrentHp() - damage;
            if (newHp <= 0 && !isDead()) {
                setCurrentHpDirect(0);
                setDead(true);
                setStatus(8);
                GeneralThreadPool.get().execute(new Death(this, null));
            }
            if (newHp > 0) {
                setCurrentHp(newHp);
            }
        } else if (!isDead()) {
            setDead(true);
            setStatus(8);
            GeneralThreadPool.get().execute(new Death(this, null));
        }
    }

    private class Death implements Runnable {
        private Death() {
        }

        /* synthetic */ Death(L1FollowerInstance l1FollowerInstance, Death death) {
            this();
        }

        public void run() {
            try {
                L1FollowerInstance.this.getMap().setPassable(L1FollowerInstance.this.getLocation(), true);
                L1FollowerInstance.this.setDeathProcessing(true);
                L1FollowerInstance.this.broadcastPacketAll(new S_DoActionGFX(L1FollowerInstance.this.getId(), 8));
                L1FollowerInstance.this.setDeathProcessing(false);
                L1FollowerInstance.this.startDeleteTimer(10);
            } catch (Exception e) {
                L1FollowerInstance._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
