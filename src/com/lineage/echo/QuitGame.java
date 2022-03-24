package com.lineage.echo;

import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Trade;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuitGame {
    private static final Log _log = LogFactory.getLog(QuitGame.class);

    public static void quitGame(L1PcInstance pc) {
        Object[] petList;
        if (pc != null && pc.getOnlineStatus() != 0) {
            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null && clan.getWarehouseUsingChar() == pc.getId()) {
                clan.setWarehouseUsingChar(0);
            }
            if (!pc.getPetList().isEmpty() && (petList = pc.getPetList().values().toArray()) != null) {
                remove_pet(pc, petList);
            }
            try {
                if (!pc.getDolls().isEmpty()) {
                    for (Object obj : pc.getDolls().values().toArray()) {
                        L1DollInstance doll = (L1DollInstance) obj;
                        if (doll != null) {
                            doll.deleteDoll();
                        }
                    }
                    pc.getDolls().clear();
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
            try {
                if (!pc.get_otherList().get_illusoryList().isEmpty()) {
                    for (Object obj2 : pc.get_otherList().get_illusoryList().values().toArray()) {
                        L1IllusoryInstance ill = (L1IllusoryInstance) obj2;
                        if (ill != null) {
                            ill.deleteMe();
                        }
                    }
                }
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
            try {
                pc.get_otherList().clearAll();
            } catch (Exception e3) {
                _log.error(e3.getLocalizedMessage(), e3);
            }
            try {
                if (pc.isDead()) {
                    int[] loc = GetbackTable.GetBack_Location(pc, true);
                    pc.setX(loc[0]);
                    pc.setY(loc[1]);
                    pc.setMap((short) loc[2]);
                    pc.setCurrentHp(pc.getLevel());
                    if (pc.get_food() > 40) {
                        pc.set_food(40);
                    }
                }
            } catch (Exception e4) {
                _log.error(e4.getLocalizedMessage(), e4);
            }
            try {
                if (pc.getTradeID() != 0) {
                    new L1Trade().tradeCancel(pc);
                }
            } catch (Exception e5) {
                _log.error(e5.getLocalizedMessage(), e5);
            }
            try {
                if (pc.getFightId() != 0) {
                    pc.setFightId(0);
                    L1PcInstance fightPc = (L1PcInstance) World.get().findObject(pc.getFightId());
                    if (fightPc != null) {
                        fightPc.setFightId(0);
                        fightPc.sendPackets(new S_PacketBox(5, 0, 0));
                    }
                }
            } catch (Exception e6) {
                _log.error(e6.getLocalizedMessage(), e6);
            }
            try {
                if (pc.isInParty()) {
                    pc.getParty().leaveMember(pc);
                }
            } catch (Exception e7) {
                _log.error(e7.getLocalizedMessage(), e7);
            }
            try {
                if (pc.isInChatParty()) {
                    pc.getChatParty().leaveMember(pc);
                }
            } catch (Exception e8) {
                _log.error(e8.getLocalizedMessage(), e8);
            }
            try {
                if (!pc.getFollowerList().isEmpty()) {
                    Object[] followerList = pc.getFollowerList().values().toArray();
                    int length = followerList.length;
                    for (int i = 0; i < length; i++) {
                        L1FollowerInstance follower = (L1FollowerInstance) followerList[i];
                        follower.setParalyzed(true);
                        follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getHeading(), follower.getMapId());
                        follower.deleteMe();
                    }
                }
            } catch (Exception e9) {
                _log.error(e9.getLocalizedMessage(), e9);
            }
            try {
                pc.stopEtcMonitor();
                pc.setOnlineStatus(0);
                pc.save();
                pc.saveInventory();
                pc.logout();
            } catch (Exception e10) {
                _log.error(e10.getLocalizedMessage(), e10);
            }
        }
    }

    private static void remove_pet(L1PcInstance pc, Object[] petList) {
        try {
            for (Object obj : petList) {
                L1NpcInstance petObject = (L1NpcInstance) obj;
                if (petObject != null) {
                    if (petObject instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance) petObject;
                        pet.collect(true);
                        pet.setDead(true);
                        pc.removePet(pet);
                        pet.deleteMe();
                    }
                    if (petObject instanceof L1SummonInstance) {
                        L1SummonInstance summon = (L1SummonInstance) petObject;
                        new S_NewMaster(summon);
                        if (summon == null) {
                            continue;
                        } else if (!summon.destroyed()) {
                            if (summon.tamed()) {
                                summon.deleteMe();
                            } else {
                                summon.Death(null);
                            }
                        } else {
                            return;
                        }
                    } else {
                        continue;
                    }
                }
            }
            pc.getPetList().clear();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
