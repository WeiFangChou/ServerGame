package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.Iterator;

public class Reactivating_Reel extends ItemExecutor {
    private Reactivating_Reel() {
    }

    public static ItemExecutor get() {
        return new Reactivating_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1Character target = (L1Character) World.get().findObject(data[0]);
        if (target != null && target.getId() != pc.getId()) {
            if (target.getCurrentHp() <= 0 || target.isDead()) {
                pc.getInventory().removeItem(item, 1);
                boolean isbless = false;
                if (item.getItem().getBless() == 0) {
                    isbless = true;
                }
                if (!target.isDead()) {
                    return;
                }
                if (target instanceof L1PcInstance) {
                    L1PcInstance targetPc = (L1PcInstance) target;
                    if (World.get().getVisiblePlayer(targetPc, 0).size() > 0) {
                        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(targetPc, 0).iterator();
                        while (it.hasNext()) {
                            if (!it.next().isDead()) {
                                pc.sendPackets(new S_ServerMessage(592));
                                return;
                            }
                        }
                    }
                    if (pc.getMap().isUseResurrection()) {
                        targetPc.setTempID(pc.getId());
                        if (!isbless) {
                            targetPc.sendPackets(new S_Message_YN(321));
                        } else {
                            targetPc.sendPackets(new S_Message_YN(322));
                        }
                    }
                } else if ((target instanceof L1NpcInstance) && !(target instanceof L1TowerInstance) && !(target instanceof L1DoorInstance) && !(target instanceof L1GuardInstance) && !(target instanceof L1MonsterInstance)) {
                    L1NpcInstance npc = (L1NpcInstance) target;
                    if (!npc.getNpcTemplate().isCantResurrect()) {
                        if ((npc instanceof L1PetInstance) && World.get().getVisiblePlayer(npc, 0).size() > 0) {
                            Iterator<L1PcInstance> it2 = World.get().getVisiblePlayer(npc, 0).iterator();
                            while (it2.hasNext()) {
                                if (!it2.next().isDead()) {
                                    pc.sendPackets(new S_ServerMessage(592));
                                    return;
                                }
                            }
                        }
                        if (npc.isDead()) {
                            if (isbless) {
                                npc.resurrect(npc.getMaxHp());
                            } else {
                                npc.resurrect(npc.getMaxHp() / 4);
                            }
                            npc.setResurrect(true);
                            npc.onNpcAI();
                        }
                    }
                }
            }
        }
    }
}
