package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Sepia extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Sepia.class);

    private Npc_Sepia() {
    }

    public static NpcExecutor get() {
        return new Npc_Sepia();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (!pc.isCrown() && !pc.isKnight()) {
                if (pc.isElf()) {
                    if (!pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                        if (pc.getLevel() >= ElfLv45_1.QUEST.get_questlevel()) {
                            switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                                case 0:
                                case 1:
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sepia2"));
                                    return;
                                case 2:
                                case 3:
                                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sepia1"));
                                    return;
                                default:
                                    return;
                            }
                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sepia2"));
                        }
                    }
                } else if (!pc.isWizard() && !pc.isDarkelf() && !pc.isDragonKnight()) {
                    pc.isIllusionist();
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isElf()) {
            if (!pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                if (pc.getQuest().isStart(ElfLv45_1.QUEST.get_id())) {
                    switch (pc.getQuest().get_step(ElfLv45_1.QUEST.get_id())) {
                        case 2:
                        case 3:
                            if (cmd.equalsIgnoreCase("teleport sepia-dungen")) {
                                pc.getQuest().set_step(ElfLv45_1.QUEST.get_id(), 3);
                                staraQuest(pc, 302);
                                isCloseList = true;
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
            } else {
                return;
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    public static void staraQuest(L1PcInstance pc, int mapid) {
        try {
            int questid = ElfLv45_1.QUEST.get_id();
            L1QuestUser quest = WorldQuest.get().put(WorldQuest.get().nextId(), mapid, questid, pc);
            if (quest == null) {
                _log.error("副本設置過程發生異常!!");
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }
            L1Teleport.teleport(pc, 32745, 32872,  mapid, 0, true);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
