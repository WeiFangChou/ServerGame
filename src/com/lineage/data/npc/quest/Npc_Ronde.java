package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DarkElfLv15_2;
import com.lineage.data.quest.DarkElfLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Ronde extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Ronde.class);

    private Npc_Ronde() {
    }

    public static NpcExecutor get() {
        return new Npc_Ronde();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else if (pc.isDarkelf()) {
                if (!pc.getQuest().isEnd(DarkElfLv15_2.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                } else if (pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde5"));
                } else if (pc.getLevel() >= DarkElfLv30_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(DarkElfLv30_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde1"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde2"));
                            return;
                        case 2:
                            if (pc.getInventory().checkItem(40596, 1)) {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde4"));
                                return;
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde3"));
                                return;
                            }
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde7"));
                }
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde6"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (!pc.isDarkelf()) {
            isCloseList = true;
        } else if (!pc.getQuest().isEnd(DarkElfLv30_1.QUEST.get_id())) {
            switch (pc.getQuest().get_step(DarkElfLv30_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("quest 13 ronde2")) {
                        QuestClass.get().startQuest(pc, DarkElfLv30_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde2"));
                        break;
                    }
                    break;
                case 1:
                    if (cmd.equalsIgnoreCase("request close list of assassination")) {
                        if (CreateNewItem.checkNewItem(pc, new int[]{40554}, new int[]{1}) < 1) {
                            isCloseList = true;
                            break;
                        } else {
                            CreateNewItem.createNewItem(pc, new int[]{40554}, new int[]{1}, new int[]{40556}, 1, new int[]{1});
                            pc.getQuest().set_step(DarkElfLv30_1.QUEST.get_id(), 2);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde3"));
                            break;
                        }
                    }
                    break;
                case 2:
                    if (!cmd.equalsIgnoreCase("quest 15 ronde4")) {
                        if (cmd.equalsIgnoreCase("request rondebag")) {
                            if (CreateNewItem.checkNewItem(pc, new int[]{40596}, new int[]{1}) < 1) {
                                isCloseList = true;
                                break;
                            } else {
                                CreateNewItem.createNewItem(pc, new int[]{40596}, new int[]{1}, new int[]{40545}, 1, new int[]{1});
                                QuestClass.get().endQuest(pc, DarkElfLv30_1.QUEST.get_id());
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde5"));
                                for (int itemId : new int[]{40557, 40558, 40559, 40560, 40561, 40562, 40563}) {
                                    L1ItemInstance item = pc.getInventory().checkItemX(itemId, 1);
                                    if (item != null) {
                                        pc.getInventory().removeItem(item, 1);
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        if (pc.hasSkillEffect(L1SkillId.CKEW_LV50)) {
                            pc.removeSkillEffect(L1SkillId.CKEW_LV50);
                        }
                        if (pc.hasSkillEffect(L1SkillId.STATUS_CURSE_YAHEE)) {
                            pc.removeSkillEffect(L1SkillId.STATUS_CURSE_YAHEE);
                        }
                        if (pc.hasSkillEffect(L1SkillId.STATUS_CURSE_BARLOG)) {
                            pc.removeSkillEffect(L1SkillId.STATUS_CURSE_BARLOG);
                        }
                        pc.setSkillEffect(L1SkillId.DE_LV30, 1500000);
                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7245));
                        pc.sendPackets(new S_ServerMessage(1454));
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ronde4"));
                        break;
                    }
                    break;
                default:
                    isCloseList = true;
                    break;
            }
        } else {
            return;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
