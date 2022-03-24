package com.lineage.server.model.Instance;

import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1MonsterGuardInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1MonsterGuardInstance.class);
    private static final long serialVersionUID = 1;

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_NPCPack(this));
                if (getCurrentHp() > 0) {
                    onNpcAI();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void searchTarget() {
        L1PcInstance targetPlayer = searchTarget(this);
        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
            return;
        }
        this.ISASCAPE = false;
    }

    private L1PcInstance searchTarget(L1MonsterGuardInstance npc) {
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(npc).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if (pc.getCurrentHp() > 0 && !pc.isDead() && !pc.isGhost() && !pc.isGm() && npc.get_showId() == pc.get_showId()) {
                if (pc.getClan() == null || pc.getClan().getCastleId() != 2) {
                    boolean isCheck = false;
                    if (!pc.isInvisble()) {
                        isCheck = true;
                    }
                    if (npc.getNpcTemplate().is_agrocoi()) {
                        isCheck = true;
                    }
                    if (!isCheck) {
                        continue;
                    } else if (pc.hasSkillEffect(67) && npc.getNpcTemplate().is_agrososc()) {
                        return pc;
                    } else {
                        if (npc.getNpcTemplate().is_agro()) {
                            return pc;
                        }
                        if (npc.getNpcTemplate().is_agrogfxid1() >= 0 && pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid1()) {
                            return pc;
                        }
                        if (npc.getNpcTemplate().is_agrogfxid2() >= 0 && pc.getGfxId() == npc.getNpcTemplate().is_agrogfxid2()) {
                            return pc;
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void setLink(L1Character cha) {
        if (get_showId() == cha.get_showId() && cha != null && this._hateList.isEmpty()) {
            this._hateList.add(cha, 0);
            checkTarget();
        }
    }

    public L1MonsterGuardInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            setActived(false);
            startAI();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) throws Exception {
        if (this.ATTACK != null) {
            this.ATTACK.attack(pc, this);
        }
        if (getCurrentHp() > 0 && !isDead()) {
            L1AttackMode attack = new L1AttackPc(pc, this);
            if (attack.calcHit()) {
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentHp(int i) {
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentMp(int i) {
    }
}
