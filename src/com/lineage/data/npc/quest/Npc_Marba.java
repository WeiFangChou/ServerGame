package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Marba extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Marba.class);

    private Npc_Marba() {
    }

    public static NpcExecutor get() {
        return new Npc_Marba();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getLawful() < -500) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba1"));
            } else if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else if (pc.isElf()) {
                if (pc.getQuest().isEnd(ElfLv15_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba15"));
                } else if (pc.getLevel() >= ElfLv15_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(ElfLv15_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba3"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba6"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba19"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba19"));
                            return;
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba17"));
                            return;
                        case 5:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba22"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba2"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isElf()) {
            switch (pc.getQuest().get_step(ElfLv15_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equals("A")) {
                        CreateNewItem.createNewItem(pc, 40637, 1);
                        QuestClass.get().startQuest(pc, ElfLv15_1.QUEST.get_id());
                        isCloseList = true;
                        break;
                    }
                    break;
                case 4:
                    if (cmd.equals("B")) {
                        L1ItemInstance item = pc.getInventory().checkItemX(40665, 1);
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);
                        }
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 5);
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba7"));
                        break;
                    }
                    break;
                case 5:
                    if (!cmd.equalsIgnoreCase("1")) {
                        if (!cmd.equalsIgnoreCase("2")) {
                            if (!cmd.equalsIgnoreCase("3")) {
                                if (!cmd.equalsIgnoreCase("4")) {
                                    if (!cmd.equalsIgnoreCase("5")) {
                                        if (cmd.equalsIgnoreCase("6")) {
                                            getItem(pc, npc, new int[]{40694, 40492, 40500, 40501, 40495, 40520}, new int[]{1, 1, 1, 1, 10, 35}, 20401);
                                            break;
                                        }
                                    } else {
                                        getItem(pc, npc, new int[]{40695, 40522, 40523, 40500, 40510, 40508}, new int[]{1, 5, 5, 5, 3, 50}, 20406);
                                        break;
                                    }
                                } else {
                                    getItem(pc, npc, new int[]{40697, 40522, 40523, 40500, 40510, 40495}, new int[]{1, 5, 5, 5, 3, 10}, 20409);
                                    break;
                                }
                            } else {
                                getItem(pc, npc, new int[]{40693, 40518, 40516, 40517, 40510, 40508}, new int[]{1, 5, 5, 5, 6, 100}, 20393);
                                break;
                            }
                        } else {
                            getItem(pc, npc, new int[]{40698, 40501, 40492, 40523, 40510, 40495}, new int[]{1, 3, 3, 3, 5, 10}, 20389);
                            break;
                        }
                    } else {
                        getItem(pc, npc, new int[]{40699, 40518, 40516, 40517, 40512, 40495}, new int[]{1, 3, 3, 3, 5, 10}, L1SkillId.ILLUSION_DIA_GOLEM);
                        break;
                    }
                    break;
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private void getItem(L1PcInstance pc, L1NpcInstance npc, int[] srcid, int[] srccount, int itemid) {
        if (CreateNewItem.checkNewItem(pc, srcid, srccount) < 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba16"));
            return;
        }
        CreateNewItem.createNewItem(pc, srcid, srccount, new int[]{itemid}, 1, new int[]{1});
        if (checkItem(pc)) {
            QuestClass.get().endQuest(pc, ElfLv15_1.QUEST.get_id());
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "marba21"));
        }
    }

    private boolean checkItem(L1PcInstance pc) {
        int i = 0;
        for (int itemid : new int[]{L1SkillId.ILLUSION_DIA_GOLEM, 20401, 20409, 20393, 20406, 20389}) {
            if (pc.getInventory().checkItemX(itemid, 1) != null) {
                i++;
            }
        }
        if (i >= 6) {
            return true;
        }
        return false;
    }
}
