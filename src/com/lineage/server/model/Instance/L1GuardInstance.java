package com.lineage.server.model.Instance;

import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GuardInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1GuardInstance.class);
    private static final long serialVersionUID = 1;

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void searchTarget() {
        L1PcInstance targetPlayer = searchTarget(this);
        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
        }
    }

    private static L1PcInstance searchTarget(L1GuardInstance npc) {
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(npc).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if (pc.getCurrentHp() > 0 && !pc.isDead() && !pc.isGm() && !pc.isGhost() && npc.get_showId() == pc.get_showId()) {
                if ((!pc.isInvisble() || npc.getNpcTemplate().is_agrocoi()) && pc.isWanted()) {
                    return pc;
                }
            }
        }
        return null;
    }

    public void setTarget(L1PcInstance targetPlayer) {
        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public boolean noTarget() {
        if (getLocation().getTileLineDistance(new Point(getHomeX(), getHomeY())) > 0) {
            if (this._npcMove != null) {
                int dir = this._npcMove.moveDirection(getHomeX(), getHomeY());
                if (dir != -1) {
                    this._npcMove.setDirectionMove(dir);
                    setSleepTime(calcSleepTime(getPassispeed(), 0));
                } else {
                    teleport(getHomeX(), getHomeY(), 1);
                }
            }
        } else if (World.get().getRecognizePlayer(this).size() == 0) {
            return true;
        }
        return false;
    }

    public L1GuardInstance(L1Npc template) {
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
    public void onAction(L1PcInstance pc) {
        try {
            if (isDead()) {
                return;
            }
            if (getCurrentHp() > 0) {
                L1AttackMode attack = new L1AttackPc(pc, this);
                if (attack.calcHit()) {
                    attack.calcDamage();
                    attack.calcStaffOfMana();
                    attack.addChaserAttack();
                }
                attack.action();
                attack.commit();
                return;
            }
            L1AttackMode attack2 = new L1AttackPc(pc, this);
            attack2.calcHit();
            attack2.action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        int npcid = getNpcTemplate().get_npcId();
        String htmlid = null;
        String[] htmldata = null;
        String clan_name = " ";
        String pri_name = " ";
        if (talking != null) {
            if (npcid == 70549 || npcid == 70985) {
                if (checkHasCastle(player, 1)) {
                    htmlid = "gateokeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70656) {
                if (checkHasCastle(player, 1)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70600 || npcid == 70986) {
                if (checkHasCastle(player, 2)) {
                    htmlid = "orckeeper";
                } else {
                    htmlid = "orckeeperop";
                }
            } else if (npcid == 70687 || npcid == 70987) {
                if (checkHasCastle(player, 3)) {
                    htmlid = "gateokeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70778) {
                if (checkHasCastle(player, 3)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70800 || npcid == 70988 || npcid == 70989 || npcid == 70990 || npcid == 70991) {
                if (checkHasCastle(player, 4)) {
                    htmlid = "gateokeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70817) {
                if (checkHasCastle(player, 4)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70862 || npcid == 70992) {
                if (checkHasCastle(player, 5)) {
                    htmlid = "gateokeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70863) {
                if (checkHasCastle(player, 5)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70993 || npcid == 70994) {
                if (checkHasCastle(player, 6)) {
                    htmlid = "gateokeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70995) {
                if (checkHasCastle(player, 6)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 70996) {
                if (checkHasCastle(player, 7)) {
                    htmlid = "gatekeeper";
                    htmldata = new String[]{player.getName()};
                } else {
                    htmlid = "gatekeeperop";
                }
            } else if (npcid == 60514) {
                Iterator<L1Clan> iter = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter.hasNext()) {
                        L1Clan clan = iter.next();
                        if (clan.getCastleId() == 1) {
                            clan_name = clan.getClanName();
                            pri_name = clan.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "ktguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 60560) {
                Iterator<L1Clan> iter2 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter2.hasNext()) {
                        L1Clan clan2 = iter2.next();
                        if (clan2.getCastleId() == 2) {
                            clan_name = clan2.getClanName();
                            pri_name = clan2.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "orcguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 60552) {
                Iterator<L1Clan> iter3 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter3.hasNext()) {
                        L1Clan clan3 = iter3.next();
                        if (clan3.getCastleId() == 3) {
                            clan_name = clan3.getClanName();
                            pri_name = clan3.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "wdguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 60524 || npcid == 60525 || npcid == 60529) {
                Iterator<L1Clan> iter4 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter4.hasNext()) {
                        L1Clan clan4 = iter4.next();
                        if (clan4.getCastleId() == 4) {
                            clan_name = clan4.getClanName();
                            pri_name = clan4.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "grguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 70857) {
                Iterator<L1Clan> iter5 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter5.hasNext()) {
                        L1Clan clan5 = iter5.next();
                        if (clan5.getCastleId() == 5) {
                            clan_name = clan5.getClanName();
                            pri_name = clan5.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "heguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 60530 || npcid == 60531) {
                Iterator<L1Clan> iter6 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter6.hasNext()) {
                        L1Clan clan6 = iter6.next();
                        if (clan6.getCastleId() == 6) {
                            clan_name = clan6.getClanName();
                            pri_name = clan6.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "dcguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 60533 || npcid == 60534) {
                Iterator<L1Clan> iter7 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter7.hasNext()) {
                        L1Clan clan7 = iter7.next();
                        if (clan7.getCastleId() == 7) {
                            clan_name = clan7.getClanName();
                            pri_name = clan7.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "adguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            } else if (npcid == 81156) {
                Iterator<L1Clan> iter8 = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter8.hasNext()) {
                        L1Clan clan8 = iter8.next();
                        if (clan8.getCastleId() == 8) {
                            clan_name = clan8.getClanName();
                            pri_name = clan8.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                htmlid = "ktguard6";
                htmldata = new String[]{getName(), clan_name, pri_name};
            }
            if (htmlid != null) {
                if (htmldata != null) {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
                }
            } else if (player.getLawful() < -1000) {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
        }
    }

    public void onFinalAction() {
    }

    public void doFinalAction() {
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        this.ISASCAPE = false;
        if (getCurrentHp() > 0 && !isDead()) {
            if (damage >= 0 && !(attacker instanceof L1EffectInstance)) {
                if (attacker instanceof L1IllusoryInstance) {
                    attacker = ((L1IllusoryInstance) attacker).getMaster();
                    setHate(attacker, damage);
                } else {
                    setHate(attacker, damage);
                }
            }
            if (damage > 0) {
                removeSkillEffect(66);
            }
            onNpcAI();
            if ((attacker instanceof L1PcInstance) && damage > 0) {
                ((L1PcInstance) attacker).setPetTarget(this);
            }
            int newHp = getCurrentHp() - damage;
            if (newHp <= 0 && !isDead()) {
                setCurrentHpDirect(0);
                setDead(true);
                setStatus(8);
                GeneralThreadPool.get().execute(new Death(attacker));
            }
            if (newHp > 0) {
                setCurrentHp(newHp);
            }
        } else if ((getCurrentHp() != 0 || isDead()) && !isDead()) {
            setDead(true);
            setStatus(8);
            GeneralThreadPool.get().execute(new Death(attacker));
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
            L1GuardInstance.this.setDeathProcessing(true);
            L1GuardInstance.this.setCurrentHpDirect(0);
            L1GuardInstance.this.setDead(true);
            L1GuardInstance.this.setStatus(8);
            L1GuardInstance.this.getMap().setPassable(L1GuardInstance.this.getLocation(), true);
            L1GuardInstance.this.broadcastPacketAll(new S_DoActionGFX(L1GuardInstance.this.getId(), 8));
            L1GuardInstance.this.startChat(1);
            L1GuardInstance.this.setDeathProcessing(false);
            L1GuardInstance.this.allTargetClear();
            L1GuardInstance.this.startDeleteTimer(ConfigAlt.NPC_DELETION_TIME);
        }
    }

    private boolean checkHasCastle(L1PcInstance pc, int castleId) {
        L1Clan clan;
        if (pc.getClanid() == 0 || (clan = WorldClan.get().getClan(pc.getClanname())) == null || clan.getCastleId() != castleId) {
            return false;
        }
        return true;
    }
}
