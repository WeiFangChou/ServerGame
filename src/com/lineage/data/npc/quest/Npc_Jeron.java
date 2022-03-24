package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv45_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Jeron extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Jeron.class);

    private Npc_Jeron() {
    }

    public static NpcExecutor get() {
        return new Npc_Jeron();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.getQuest().isEnd(ALv45_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
            } else if (pc.getLevel() >= ALv45_1.QUEST.get_questlevel()) {
                switch (pc.getQuest().get_step(ALv45_1.QUEST.get_id())) {
                    case 0:
                    case 1:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
                        return;
                    case 2:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron1"));
                        return;
                    case 3:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron7"));
                        return;
                    default:
                        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
                        return;
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("A")) {
            if (CreateNewItem.checkNewItem(pc, new int[]{41340}, new int[]{1}) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron10"));
                return;
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron2"));
        } else if (cmd.equalsIgnoreCase("B")) {
            int[] items = {40308};
            int[] counts = {1000000};
            if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron8"));
                return;
            }
            int[] gitems = {41341};
            int[] gcounts = {1};
            L1ItemInstance item = pc.getInventory().checkItemX(41340, 1);
            if (item != null) {
                pc.getInventory().removeItem(item, 1);
            }
            CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 3);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron6"));
        } else if (cmd.equalsIgnoreCase("C")) {
            int[] items2 = {41342};
            int[] counts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron9"));
                return;
            }
            int[] gitems2 = {41341};
            int[] gcounts2 = {1};
            L1ItemInstance item2 = pc.getInventory().checkItemX(41340, 1);
            if (item2 != null) {
                pc.getInventory().removeItem(item2, 1);
            }
            CreateNewItem.createNewItem(pc, items2, counts2, gitems2, 1, gcounts2);
            pc.getQuest().set_step(ALv45_1.QUEST.get_id(), 3);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jeron5"));
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
