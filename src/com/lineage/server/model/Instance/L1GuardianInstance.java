package com.lineage.server.model.Instance;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.drop.DropShare;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CalcExp;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GuardianInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1GuardianInstance.class);
    private static final long serialVersionUID = 1;
    private int _configtime = ConfigAlt.GDROPITEM_TIME;
    private L1GuardianInstance _npc = this;
    private Random _random = new Random();

    public L1GuardianInstance(L1Npc template) {
        super(template);
        if (!isDropitems()) {
            doGDropItem(0);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void searchTarget() {
        L1PcInstance targetPlayer = searchTarget(this);
        if (targetPlayer != null) {
            this._hateList.add(targetPlayer, 0);
            this._target = targetPlayer;
        }
    }

    private static L1PcInstance searchTarget(L1GuardianInstance npc) {
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(npc).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            if (pc.getCurrentHp() > 0 && !pc.isDead() && !pc.isGm() && !pc.isGhost() && npc.get_showId() == pc.get_showId()) {
                if (!pc.isInvisble() || npc.getNpcTemplate().is_agrocoi()) {
                    if (!pc.isElf()) {
                        npc.wideBroadcastPacket(new S_NpcChatShouting(npc, "$804"));
                        return pc;
                    } else if (pc.isElf() && pc.isWantedForElf()) {
                        npc.wideBroadcastPacket(new S_NpcChat(npc, "$815"));
                        return pc;
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

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            setActived(false);
            startAI();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) throws Exception {
        int chance = this._random.nextInt(100) + 1;
        int PT_Npc_id = getNpcTemplate().get_npcId();
        if (player.getType() == 2 && player.getCurrentWeapon() == 0 && player.isElf()) {
            L1AttackMode attack = new L1AttackPc(player, this);
            if (attack.calcHit()) {
                try {
                    String npcName = getNpcTemplate().get_name();
                    L1Item item40499 = ItemTable.get().getTemplate(40499);
                    L1Item item40503 = ItemTable.get().getTemplate(40503);
                    L1Item item40505 = ItemTable.get().getTemplate(40505);
                    L1Item item40506 = ItemTable.get().getTemplate(40506);
                    L1Item item40507 = ItemTable.get().getTemplate(40507);
                    L1Item item40519 = ItemTable.get().getTemplate(40519);
                    switch (PT_Npc_id) {
                        case 70846:
                            if (!this._inventory.checkItem(40507)) {
                                player.sendPackets(new S_ServerMessage(337, item40507.getName()));
                                break;
                            } else {
                                String itemName = item40503.getName();
                                long itemCount = this._inventory.countItems(40507);
                                if (itemCount > serialVersionUID) {
                                    itemName = String.valueOf(itemName) + " (" + itemCount + ")";
                                }
                                this._inventory.consumeItem(40507, itemCount);
                                player.getInventory().storeItem(40503, itemCount);
                                player.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                break;
                            }
                        case 70848:
                            if (this._inventory.checkItem(40499)) {
                                String itemName2 = item40505.getName();
                                long itemCount2 = this._inventory.countItems(40499);
                                if (itemCount2 > serialVersionUID) {
                                    itemName2 = String.valueOf(itemName2) + " (" + itemCount2 + ")";
                                }
                                this._inventory.consumeItem(40499, itemCount2);
                                player.getInventory().storeItem(40505, itemCount2);
                                player.sendPackets(new S_ServerMessage(143, npcName, itemName2));
                                if (!isDropitems()) {
                                    doGDropItem(3);
                                }
                            }
                            if (!this._inventory.checkItem(40507)) {
                                if (this._inventory.checkItem(40506) && !this._inventory.checkItem(40507)) {
                                    if (chance > 10) {
                                        player.sendPackets(new S_ServerMessage(337, item40499.getName()));
                                        break;
                                    } else {
                                        String itemName3 = item40506.getName();
                                        this._inventory.consumeItem(40506, serialVersionUID);
                                        player.getInventory().storeItem(40506, serialVersionUID);
                                        player.sendPackets(new S_ServerMessage(143, npcName, itemName3));
                                        break;
                                    }
                                } else {
                                    if (!forDropitems()) {
                                        setDropItems(false);
                                        doGDropItem(this._configtime);
                                    }
                                    if (chance <= 70 && chance >= 40) {
                                        broadcastPacketAll(new S_NpcChat(this._npc, "$822"));
                                        break;
                                    } else {
                                        player.sendPackets(new S_ServerMessage(337, item40499.getName()));
                                        break;
                                    }
                                }
                            } else if (chance > 25) {
                                player.sendPackets(new S_ServerMessage(337, item40499.getName()));
                                break;
                            } else {
                                this._inventory.consumeItem(40507, 6);
                                player.getInventory().storeItem(40507, 6);
                                player.sendPackets(new S_ServerMessage(143, npcName, String.valueOf(item40507.getName()) + " (6)"));
                                break;
                            }

                        case 70850:
                            if (this._inventory.checkItem(40519)) {
                                if (chance <= 30) {
                                    this._inventory.consumeItem(40519, 5);
                                    player.getInventory().storeItem(40519, 5);
                                    player.sendPackets(new S_ServerMessage(143, npcName, String.valueOf(item40519.getName()) + " (5)"));
                                    break;
                                }
                            } else {
                                if (!forDropitems()) {
                                    setDropItems(false);
                                    doGDropItem(this._configtime);
                                }
                                if (chance <= 80 && chance >= 40) {
                                    broadcastPacketAll(new S_NpcChat(this._npc, "$824"));
                                    break;
                                }
                            }
                            break;
                    }
                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
                attack.calcDamage();
                attack.calcStaffOfMana();
                attack.addChaserAttack();
            }
            attack.action();
            attack.commit();
        } else if (getCurrentHp() > 0 && !isDead()) {
            L1AttackMode attack2 = new L1AttackPc(player, this);
            if (attack2.calcHit()) {
                attack2.calcDamage();
                attack2.calcStaffOfMana();
                attack2.addChaserAttack();
            }
            attack2.action();
            attack2.commit();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        L1NpcInstance target = (L1NpcInstance) World.get().findObject(getId());
        if (talking != null) {
            int pcx = player.getX();
            int pcy = player.getY();
            int npcx = target.getX();
            int npcy = target.getY();
            if (pcx == npcx && pcy < npcy) {
                setHeading(0);
            } else if (pcx > npcx && pcy < npcy) {
                setHeading(1);
            } else if (pcx > npcx && pcy == npcy) {
                setHeading(2);
            } else if (pcx > npcx && pcy > npcy) {
                setHeading(3);
            } else if (pcx == npcx && pcy > npcy) {
                setHeading(4);
            } else if (pcx < npcx && pcy > npcy) {
                setHeading(5);
            } else if (pcx < npcx && pcy == npcy) {
                setHeading(6);
            } else if (pcx < npcx && pcy < npcy) {
                setHeading(7);
            }
            broadcastPacketAll(new S_ChangeHeading(this));
            if (player.getLawful() < -1000) {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
            set_stop_time(10);
            setRest(true);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
        this.ISASCAPE = false;
        if ((attacker instanceof L1PcInstance) && damage > 0) {
            L1PcInstance pc = (L1PcInstance) attacker;
            if (pc.getType() != 2 || pc.getCurrentWeapon() != 0) {
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
                    serchLink(pc, getNpcTemplate().get_family());
                    if (damage > 0) {
                        pc.setPetTarget(this);
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
                } else if (!isDead()) {
                    setDead(true);
                    setStatus(8);
                    GeneralThreadPool.get().execute(new Death(attacker));
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

    @Override // com.lineage.server.model.L1Character
    public void setCurrentMp(int i) {
        int currentMp = Math.min(i, (int) getMaxMp());
        if (getCurrentMp() != currentMp) {
            setCurrentMpDirect(currentMp);
        }
    }

    class Death implements Runnable {
        L1Character _lastAttacker;

        public Death(L1Character lastAttacker) {
            this._lastAttacker = lastAttacker;
        }

        public void run() {
            L1GuardianInstance.this.setDeathProcessing(true);
            L1GuardianInstance.this.setCurrentHpDirect(0);
            L1GuardianInstance.this.setDead(true);
            L1GuardianInstance.this.setStatus(8);
            int targetobjid = L1GuardianInstance.this.getId();
            L1GuardianInstance.this.getMap().setPassable(L1GuardianInstance.this.getLocation(), true);
            L1GuardianInstance.this.broadcastPacketAll(new S_DoActionGFX(targetobjid, 8));
            L1PcInstance player = null;
            if (this._lastAttacker instanceof L1PcInstance) {
                player = (L1PcInstance) this._lastAttacker;
            } else if (this._lastAttacker instanceof L1PetInstance) {
                player = (L1PcInstance) ((L1PetInstance) this._lastAttacker).getMaster();
            } else if (this._lastAttacker instanceof L1SummonInstance) {
                player = (L1PcInstance) ((L1SummonInstance) this._lastAttacker).getMaster();
            } else if (this._lastAttacker instanceof L1IllusoryInstance) {
                player = (L1PcInstance) ((L1IllusoryInstance) this._lastAttacker).getMaster();
            } else if (this._lastAttacker instanceof L1EffectInstance) {
                player = (L1PcInstance) ((L1EffectInstance) this._lastAttacker).getMaster();
            }
            if (player != null) {
                CalcExp.calcExp(player, targetobjid, L1GuardianInstance.this._hateList.toTargetArrayList(), L1GuardianInstance.this._hateList.toHateArrayList(), L1GuardianInstance.this.getExp());
                try {
                    new DropShare().dropShare(L1GuardianInstance.this._npc, L1GuardianInstance.this._dropHateList.toTargetArrayList(), L1GuardianInstance.this._dropHateList.toHateArrayList());
                } catch (Exception e) {
                    L1GuardianInstance._log.error(e.getLocalizedMessage(), e);
                }
                player.addKarma((int) (((double) L1GuardianInstance.this.getKarma()) * ConfigRate.RATE_KARMA));
            }
            L1GuardianInstance.this.setDeathProcessing(false);
            L1GuardianInstance.this.setKarma(0);
            L1GuardianInstance.this.setExp(0);
            L1GuardianInstance.this.allTargetClear();
            L1GuardianInstance.this.startDeleteTimer(ConfigAlt.NPC_DELETION_TIME);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) {
    }

    public void doFinalAction(L1PcInstance player) {
    }

    public void doGDropItem(int timer) {
        GeneralThreadPool.get().schedule(new GDropItemTask(this, null), (long) (60000 * timer));
    }

    /* access modifiers changed from: private */
    public class GDropItemTask implements Runnable {
        int npcId;

        private GDropItemTask() {
            this.npcId = L1GuardianInstance.this.getNpcTemplate().get_npcId();
        }

        /* synthetic */ GDropItemTask(L1GuardianInstance l1GuardianInstance, GDropItemTask gDropItemTask) {
            this();
        }

        public void run() {
            try {
                if (L1GuardianInstance.this._configtime <= 0 || L1GuardianInstance.this.isDropitems()) {
                    L1GuardianInstance.this.giveDropItems(false);
                    return;
                }
                if (this.npcId == 70848 && !L1GuardianInstance.this._inventory.checkItem(40505) && !L1GuardianInstance.this._inventory.checkItem(40506) && !L1GuardianInstance.this._inventory.checkItem(40507)) {
                    L1GuardianInstance.this._inventory.storeItem(40506, L1GuardianInstance.serialVersionUID);
                    L1GuardianInstance.this._inventory.storeItem(40507, 66);
                    L1GuardianInstance.this._inventory.storeItem(40505, 8);
                }
                if (this.npcId == 70850 && !L1GuardianInstance.this._inventory.checkItem(40519)) {
                    L1GuardianInstance.this._inventory.storeItem(40519, 30);
                }
                L1GuardianInstance.this.setDropItems(true);
                L1GuardianInstance.this.giveDropItems(true);
                L1GuardianInstance.this.doGDropItem(L1GuardianInstance.this._configtime);
            } catch (Exception e) {
                L1GuardianInstance._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
