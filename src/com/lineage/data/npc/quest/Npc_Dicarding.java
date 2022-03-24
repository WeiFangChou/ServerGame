package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.CKEWLv50_1;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.data.quest.CrownLv50_1;
import com.lineage.data.quest.ElfLv45_1;
import com.lineage.data.quest.ElfLv50_1;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.data.quest.KnightLv50_1;
import com.lineage.data.quest.WizardLv45_1;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Dicarding extends NpcExecutor {
    private static int[] _itemIds = {40742, 49165, 49166, 49167, 49168, 49239, 65, 133, L1SkillId.MORTAL_BODY, 192};
    private static final Log _log = LogFactory.getLog(Npc_Dicarding.class);

    private Npc_Dicarding() {
    }

    public static NpcExecutor get() {
        return new Npc_Dicarding();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                isCrown(pc, npc);
            } else if (pc.isKnight()) {
                isKnight(pc, npc);
            } else if (pc.isElf()) {
                isElf(pc, npc);
            } else if (pc.isWizard()) {
                isWizard(pc, npc);
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void isWizard(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw15"));
            } else if (pc.getQuest().isEnd(WizardLv50_1.QUEST.get_id())) {
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw6"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw10"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw11"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw13"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
            } else if (!pc.getQuest().isEnd(WizardLv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.getLevel() >= WizardLv50_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                    case 0:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw1"));
                        return;
                    case 1:
                    case 2:
                        if (pc.getInventory().checkItemX(49164, 1) != null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw5"));
                            return;
                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw4"));
                            return;
                        }
                    default:
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void isElf(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge17"));
            } else if (pc.getQuest().isEnd(ElfLv50_1.QUEST.get_id())) {
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge10"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge12"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge13"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge15"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
            } else if (!pc.getQuest().isEnd(ElfLv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.getLevel() >= ElfLv50_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                    case 0:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge1"));
                        return;
                    case 1:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge4"));
                        return;
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge5"));
                        return;
                    case 3:
                    case 4:
                    case 5:
                        if (pc.getInventory().checkItemX(49163, 1) != null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge9"));
                            return;
                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge8"));
                            return;
                        }
                    default:
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void isKnight(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk16"));
            } else if (pc.getQuest().isEnd(KnightLv50_1.QUEST.get_id())) {
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk10"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk11"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk12"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk14"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
            } else if (!pc.getQuest().isEnd(KnightLv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.getLevel() >= KnightLv50_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(KnightLv50_1.QUEST.get_id())) {
                    case 0:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk1"));
                        return;
                    case 1:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk4"));
                        return;
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk6"));
                        return;
                    case 3:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk9"));
                        return;
                    default:
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void isCrown(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(CKEWLv50_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp15"));
            } else if (pc.getQuest().isEnd(CrownLv50_1.QUEST.get_id())) {
                if (pc.getLevel() >= CKEWLv50_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(CKEWLv50_1.QUEST.get_id())) {
                        case 0:
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp10"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp11"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp13"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
                }
            } else if (!pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            } else if (pc.getLevel() >= CrownLv50_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                    case 0:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp1"));
                        return;
                    case 1:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp4"));
                        return;
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp8"));
                        return;
                    default:
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicarding"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (pc.isCrown()) {
            switch (pc.getQuest().get_step(CrownLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("f")) {
                        QuestClass.get().startQuest(pc, CrownLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp3"));
                        break;
                    }
                    break;
                case 1:
                    if (cmd.equalsIgnoreCase("e")) {
                        if (pc.getInventory().checkItemX(49159, 1) == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp4a"));
                            break;
                        } else {
                            pc.getQuest().set_step(CrownLv50_1.QUEST.get_id(), 2);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp7"));
                            L1PolyMorph.doPoly(pc, 4261, 1800, 1);
                            break;
                        }
                    }
                    break;
                case 2:
                    if (cmd.equalsIgnoreCase("c")) {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp9"));
                        L1PolyMorph.doPoly(pc, 4261, 1800, 1);
                        break;
                    }
                    break;
            }
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("b")) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp12"));
                } else if (cmd.equalsIgnoreCase("a")) {
                    L1ItemInstance item = pc.getInventory().checkItemX(49241, 1);
                    if (item != null) {
                        QuestClass.get().endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp14"));
                        pc.getInventory().removeItem(item, 1);
                        CreateNewItem.getQuestItem(pc, npc, 51, 1);
                        delItem(pc);
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingp11"));
                    }
                }
            }
        } else if (pc.isKnight()) {
            switch (pc.getQuest().get_step(KnightLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("g")) {
                        QuestClass.get().startQuest(pc, KnightLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk3"));
                        break;
                    }
                    break;
                case 1:
                    if (cmd.equalsIgnoreCase("h")) {
                        L1ItemInstance item2 = pc.getInventory().checkItemX(49160, 1);
                        if (item2 == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk8"));
                            break;
                        } else {
                            pc.getQuest().set_step(KnightLv50_1.QUEST.get_id(), 2);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk5"));
                            pc.getInventory().removeItem(item2, 1);
                            break;
                        }
                    }
                    break;
                case 2:
                    if (cmd.equalsIgnoreCase("i")) {
                        pc.getQuest().set_step(KnightLv50_1.QUEST.get_id(), 3);
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk7"));
                        break;
                    }
                    break;
                case 3:
                    if (cmd.equalsIgnoreCase("j")) {
                        L1ItemInstance item3 = pc.getInventory().checkItemX(49161, 10);
                        if (item3 == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk9a"));
                            break;
                        } else {
                            QuestClass.get().endQuest(pc, KnightLv50_1.QUEST.get_id());
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk10"));
                            pc.getInventory().removeItem(item3);
                            QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                            break;
                        }
                    }
                    break;
            }
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("k")) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk13"));
                } else if (cmd.equalsIgnoreCase("l")) {
                    L1ItemInstance item4 = pc.getInventory().checkItemX(49241, 1);
                    if (item4 != null) {
                        QuestClass.get().endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk15"));
                        pc.getInventory().removeItem(item4, 1);
                        CreateNewItem.getQuestItem(pc, npc, 56, 1);
                        delItem(pc);
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingk12"));
                    }
                }
            }
        } else if (pc.isElf()) {
            switch (pc.getQuest().get_step(ElfLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("m")) {
                        QuestClass.get().startQuest(pc, ElfLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge3"));
                        break;
                    }
                    break;
                case 1:
                    if (cmd.equalsIgnoreCase("n")) {
                        L1ItemInstance item5 = pc.getInventory().checkItemX(49162, 1);
                        if (item5 == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge3"));
                            break;
                        } else {
                            pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 2);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge5"));
                            pc.getInventory().removeItem(item5, 1);
                            break;
                        }
                    }
                    break;
                case 2:
                    if (cmd.equalsIgnoreCase("o")) {
                        pc.getQuest().set_step(ElfLv50_1.QUEST.get_id(), 3);
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge7"));
                        break;
                    }
                    break;
                case 3:
                case 4:
                case 5:
                    if (cmd.equalsIgnoreCase("p")) {
                        L1ItemInstance item6 = pc.getInventory().checkItemX(49163, 1);
                        if (item6 == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge8"));
                            break;
                        } else {
                            QuestClass.get().endQuest(pc, ElfLv50_1.QUEST.get_id());
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge10"));
                            pc.getInventory().removeItem(item6, 1);
                            QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                            break;
                        }
                    }
                    break;
            }
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("q")) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge14"));
                } else if (cmd.equalsIgnoreCase("y")) {
                    L1ItemInstance item7 = pc.getInventory().checkItemX(49241, 1);
                    if (item7 != null) {
                        QuestClass.get().endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge16"));
                        pc.getInventory().removeItem(item7, 1);
                        CreateNewItem.getQuestItem(pc, npc, 184, 1);
                        delItem(pc);
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge13"));
                    }
                } else if (cmd.equalsIgnoreCase("s")) {
                    L1ItemInstance item8 = pc.getInventory().checkItemX(49241, 1);
                    if (item8 != null) {
                        QuestClass.get().endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge16"));
                        pc.getInventory().removeItem(item8, 1);
                        CreateNewItem.getQuestItem(pc, npc, 50, 1);
                        delItem(pc);
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardinge13"));
                    }
                }
            }
        } else if (pc.isWizard()) {
            switch (pc.getQuest().get_step(WizardLv50_1.QUEST.get_id())) {
                case 0:
                    if (cmd.equalsIgnoreCase("t")) {
                        QuestClass.get().startQuest(pc, WizardLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw3"));
                        break;
                    }
                    break;
                case 1:
                case 2:
                    if (cmd.equalsIgnoreCase("u")) {
                        L1ItemInstance item9 = pc.getInventory().checkItemX(49164, 1);
                        if (item9 == null) {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw4"));
                            break;
                        } else {
                            QuestClass.get().endQuest(pc, WizardLv50_1.QUEST.get_id());
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw6"));
                            pc.getInventory().removeItem(item9, 1);
                            break;
                        }
                    }
                    break;
                case 255:
                    if (cmd.equalsIgnoreCase("v")) {
                        QuestClass.get().startQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw9"));
                        break;
                    }
                    break;
            }
            if (pc.getQuest().isStart(CKEWLv50_1.QUEST.get_id())) {
                if (cmd.equalsIgnoreCase("w")) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw12"));
                } else if (cmd.equalsIgnoreCase("x")) {
                    L1ItemInstance item10 = pc.getInventory().checkItemX(49241, 1);
                    if (item10 != null) {
                        QuestClass.get().endQuest(pc, CKEWLv50_1.QUEST.get_id());
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw14"));
                        pc.getInventory().removeItem(item10, 1);
                        CreateNewItem.getQuestItem(pc, npc, 20225, 1);
                        delItem(pc);
                    } else {
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dicardingw11"));
                    }
                }
            }
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private void delItem(L1PcInstance pc) throws Exception {
        for (int itemId : _itemIds) {
            L1ItemInstance reitem = pc.getInventory().findItemId(itemId);
            if (reitem != null) {
                if (reitem.isEquipped()) {
                    pc.getInventory().setEquipped(reitem, false, false, false);
                }
                pc.sendPackets(new S_ServerMessage(165, reitem.getName()));
                pc.getInventory().removeItem(reitem);
            }
        }
    }
}
