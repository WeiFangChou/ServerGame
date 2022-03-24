package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1FollowerInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Endia extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Endia.class);

    private Npc_Endia() {
    }

    public static NpcExecutor get() {
        return new Npc_Endia();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 7;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (npc.getMaster() == null) {
                npc.onTalkAction(pc);
                if (pc.isCrown()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else if (pc.isKnight()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else if (pc.isElf()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else if (pc.isWizard()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else if (pc.isDarkelf()) {
                    if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                    } else if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                        switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                            case 1:
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq1"));
                                return;
                            default:
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                                return;
                        }
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                    }
                } else if (pc.isDragonKnight()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else if (pc.isIllusionist()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
                }
            } else if (npc.getMaster().equals(pc)) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq2"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (npc.getMaster() == null) {
            if (!pc.isDarkelf()) {
                isCloseList = true;
            } else if (pc.getQuest().isEnd(DarkElfLv50_1.QUEST.get_id())) {
                isCloseList = true;
            } else if (pc.getLevel() >= DarkElfLv50_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(DarkElfLv50_1.QUEST.get_id())) {
                    case 1:
                        if (cmd.equalsIgnoreCase("start")) {
                            new L1FollowerInstance(NpcTable.get().getTemplate(DarkElfLv50_1._endiaId), npc, pc);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "endiaq2"));
                            break;
                        }
                        break;
                    default:
                        isCloseList = true;
                        break;
                }
            } else {
                isCloseList = true;
            }
            if (isCloseList) {
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void attack(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (npc.getNpcId() == 71094 && pc != null) {
                Iterator<L1Object> it = World.get().getVisibleObjects(npc).iterator();
                while (it.hasNext()) {
                    L1Object object = it.next();
                    if (object instanceof L1NpcInstance) {
                        L1NpcInstance tgnpc = (L1NpcInstance) object;
                        if (tgnpc.getNpcTemplate().get_npcId() == 70811 && npc.getLocation().getTileLineDistance(pc.getLocation()) < 3 && tgnpc.getLocation().getTileLineDistance(pc.getLocation()) < 3) {
                            pc.getQuest().set_step(DarkElfLv50_1.QUEST.get_id(), 2);
                            CreateNewItem.getQuestItem(pc, npc, 40582, 1);
                            npc.setParalyzed(true);
                            npc.deleteMe();
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
