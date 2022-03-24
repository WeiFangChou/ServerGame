package com.lineage.server.model.Instance;

import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1War;
import com.lineage.server.model.L1WarSpawn;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldNpc;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1TowerInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1TowerInstance.class);
    private static final long serialVersionUID = 1;
    private int _castle_id;
    private int _crackStatus;

    public L1TowerInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack(this));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) {
        try {
            if (getCurrentHp() > 0 && !isDead()) {
                L1AttackMode attack = new L1AttackPc(player, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                }
                attack.action();
                attack.commit();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        boolean isDamage = false;
        if (this._castle_id == 0) {
            if (isSubTower()) {
                this._castle_id = 7;
            } else {
                this._castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
            }
        }
        if (this._castle_id > 0 && ServerWarExecutor.get().isNowWar(this._castle_id)) {
            if (this._castle_id == 7 && !isSubTower()) {
                int subTowerDeadCount = 0;
                for (L1NpcInstance npc : WorldNpc.get().all()) {
                    if (npc instanceof L1TowerInstance) {
                        L1TowerInstance tower = (L1TowerInstance) npc;
                        if (tower.isSubTower() && tower.isDead()) {
                            subTowerDeadCount++;
                        }
                    }
                }
                if (subTowerDeadCount < 4) {
                    return;
                }
            }
            isDamage = true;
        }
        if (isDamage) {
            L1PcInstance pc = null;
            if (attacker instanceof L1PcInstance) {
                pc = (L1PcInstance) attacker;
            } else if (attacker instanceof L1PetInstance) {
                pc = (L1PcInstance) ((L1PetInstance) attacker).getMaster();
            } else if (attacker instanceof L1SummonInstance) {
                pc = (L1PcInstance) ((L1SummonInstance) attacker).getMaster();
            } else if (attacker instanceof L1IllusoryInstance) {
                pc = (L1PcInstance) ((L1IllusoryInstance) attacker).getMaster();
            } else {
                boolean z = attacker instanceof L1EffectInstance;
            }
            if (pc != null) {
                boolean existDefenseClan = false;
                Iterator<L1Clan> iter = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter.hasNext()) {
                        if (iter.next().getCastleId() == this._castle_id) {
                            existDefenseClan = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                boolean isProclamation = false;
                Iterator<L1War> it = WorldWar.get().getWarList().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    L1War war = it.next();
                    if (this._castle_id == war.getCastleId()) {
                        isProclamation = war.checkClanInWar(pc.getClanname());
                        break;
                    }
                }
                if (existDefenseClan && !isProclamation) {
                    return;
                }
                if (getCurrentHp() > 0 && !isDead()) {
                    int newHp = getCurrentHp() - damage;
                    if (newHp <= 0 && !isDead()) {
                        setCurrentHpDirect(0);
                        setDead(true);
                        setStatus(35);
                        this._crackStatus = 0;
                        GeneralThreadPool.get().execute(new Death(this, attacker));
                    }
                    if (newHp > 0) {
                        setCurrentHp(newHp);
                        if ((getMaxHp() * 1) / 4 > getCurrentHp()) {
                            if (this._crackStatus != 3) {
                                broadcastPacketAll(new S_DoActionGFX(getId(), 34));
                                setStatus(34);
                                this._crackStatus = 3;
                            }
                        } else if ((getMaxHp() * 2) / 4 > getCurrentHp()) {
                            if (this._crackStatus != 2) {
                                broadcastPacketAll(new S_DoActionGFX(getId(), 33));
                                setStatus(33);
                                this._crackStatus = 2;
                            }
                        } else if ((getMaxHp() * 3) / 4 > getCurrentHp() && this._crackStatus != 1) {
                            broadcastPacketAll(new S_DoActionGFX(getId(), 32));
                            setStatus(32);
                            this._crackStatus = 1;
                        }
                    }
                } else if (!isDead()) {
                    setDead(true);
                    setStatus(35);
                    GeneralThreadPool.get().execute(new Death(this, attacker));
                }
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

    private class Death implements Runnable {
        private L1Character _lastattacker;
        private L1TowerInstance _tower = null;

        public Death(L1TowerInstance tower, L1Character attacker) {
            this._lastattacker = attacker;
            this._tower = tower;
        }

        public void run() {
            this._tower.setCurrentHpDirect(0);
            this._tower.setDead(true);
            this._tower.setStatus(35);
            this._tower.getMap().setPassable(this._tower.getLocation(), true);
            this._tower.broadcastPacketAll(new S_DoActionGFX(this._tower.getId(), 35));
            if (!this._tower.isSubTower()) {
                new L1WarSpawn().SpawnCrown(this._tower._castle_id);
            }
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void deleteMe() {
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
    }

    public boolean isSubTower() {
        return getNpcTemplate().get_npcId() == 81190 || getNpcTemplate().get_npcId() == 81191 || getNpcTemplate().get_npcId() == 81192 || getNpcTemplate().get_npcId() == 81193;
    }
}
