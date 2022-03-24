package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.EWLv40_1;
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

public class Npc_Roi extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Roi.class);

    private Npc_Roi() {
    }

    public static NpcExecutor get() {
        return new Npc_Roi();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 7;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (npc.getMaster() != null) {
                if (npc.getMaster().equals(pc)) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi2"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi"));
                }
            } else if (pc.getQuest().isEnd(EWLv40_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi"));
            } else if (pc.getLevel() >= EWLv40_1.QUEST.get_questlevel()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi1"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (npc.getMaster() == null) {
            if (pc.getQuest().isEnd(EWLv40_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi"));
                return;
            }
            if (pc.getLevel() < EWLv40_1.QUEST.get_questlevel()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "roi"));
            } else if (cmd.equalsIgnoreCase("start")) {
                new L1FollowerInstance(NpcTable.get().getTemplate(EWLv40_1._roi2id), npc, pc);
                QuestClass.get().startQuest(pc, EWLv40_1.QUEST.get_id());
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
            if (npc.getNpcId() == 70957 && pc != null) {
                Iterator<L1Object> it = World.get().getVisibleObjects(npc).iterator();
                while (it.hasNext()) {
                    L1Object object = it.next();
                    if (object instanceof L1NpcInstance) {
                        L1NpcInstance tgnpc = (L1NpcInstance) object;
                        if (tgnpc.getNpcTemplate().get_npcId() == 70964 && npc.getLocation().getTileLineDistance(pc.getLocation()) < 3 && tgnpc.getLocation().getTileLineDistance(pc.getLocation()) < 3) {
                            pc.getQuest().set_end(EWLv40_1.QUEST.get_id());
                            CreateNewItem.getQuestItem(pc, npc, 41003, 1);
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
