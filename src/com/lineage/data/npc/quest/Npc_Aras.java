package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ElfLv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Aras extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Aras.class);

    private Npc_Aras() {
    }

    public static NpcExecutor get() {
        return new Npc_Aras();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getLawful() < -500) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras12"));
            } else if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else if (pc.isElf()) {
                if (pc.getQuest().isEnd(ElfLv15_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras9"));
                } else if (pc.getLevel() >= ElfLv15_1.QUEST.get_questlevel()) {
                    switch (pc.getQuest().get_step(ElfLv15_1.QUEST.get_id())) {
                        case 0:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras7"));
                            return;
                        case 1:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras1"));
                            return;
                        case 2:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras3"));
                            return;
                        case 3:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras10"));
                            return;
                        case 4:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras13"));
                            return;
                        case 5:
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras8"));
                            return;
                        default:
                            return;
                    }
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras11"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (pc.isElf()) {
            switch (pc.getQuest().get_step(ElfLv15_1.QUEST.get_id())) {
                case 1:
                    if (cmd.equalsIgnoreCase("A")) {
                        L1ItemInstance item = pc.getInventory().checkItemX(40637, 1);
                        if (item != null) {
                            pc.getInventory().removeItem(item, 1);
                        }
                        CreateNewItem.createNewItem(pc, 40664, 1);
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 2);
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras2"));
                        break;
                    }
                    break;
                case 2:
                    try {
                        if (cmd.matches("[0-9]+")) {
                            status2(pc, npc, Integer.valueOf(cmd).intValue());
                            break;
                        }
                    } catch (Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                        break;
                    }
                    break;
                case 3:
                    if (cmd.equalsIgnoreCase("B")) {
                        L1ItemInstance item2 = pc.getInventory().checkItemX(40664, 1);
                        if (item2 != null) {
                            pc.getInventory().removeItem(item2, 1);
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras13"));
                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras14"));
                        }
                        CreateNewItem.createNewItem(pc, 40665, 1);
                        pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 4);
                        break;
                    }
                    break;
            }
        }
        if (0 != 0) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private void status2(L1PcInstance pc, L1NpcInstance npc, int intValue) throws Exception {
        switch (intValue) {
            case 1:
                getItem(pc, npc, 40684, 40699);
                return;
            case 2:
                getItem(pc, npc, 40683, 40698);
                return;
            case 3:
                getItem(pc, npc, 40679, 40693);
                return;
            case 4:
                getItem(pc, npc, 40682, 40697);
                return;
            case 5:
                getItem(pc, npc, 40681, 40695);
                return;
            case 6:
                getItem(pc, npc, 40680, 40694);
                return;
            case 7:
                if (CreateNewItem.checkNewItem(pc, new int[]{40684, 40683, 40679, 40682, 40681, 40680}, new int[]{1, 1, 1, 1, 1, 1}) < 1) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras5"));
                    return;
                }
                CreateNewItem.createNewItem(pc, new int[]{40684, 40683, 40679, 40682, 40681, 40680}, new int[]{1, 1, 1, 1, 1, 1}, new int[]{40699, 40698, 40693, 40697, 40695, 40694}, 1, new int[]{1, 1, 1, 1, 1, 1});
                pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 3);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras10"));
                return;
            default:
                return;
        }
    }

    private void getItem(L1PcInstance pc, L1NpcInstance npc, int srcid, int getid) throws Exception {
        if (CreateNewItem.checkNewItem(pc, srcid, 1) < 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras5"));
            return;
        }
        CreateNewItem.createNewItem(pc, srcid, 1, getid, 1);
        if (checkItem(pc)) {
            pc.getQuest().set_step(ElfLv15_1.QUEST.get_id(), 3);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras10"));
            return;
        }
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aras4"));
    }

    private boolean checkItem(L1PcInstance pc) {
        int i = 0;
        for (int itemid : new int[]{40699, 40698, 40693, 40697, 40695, 40694}) {
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
