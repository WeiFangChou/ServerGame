package com.lineage.data.npc.quest;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.ALv15_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Lyra extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Lyra.class);

    private Npc_Lyra() {
    }

    public static NpcExecutor get() {
        return new Npc_Lyra();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        boolean isOrg = false;
        try {
            if (pc.getTempCharGfx() == 3906) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3860) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3864) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3866) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3869) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 3868) {
                isOrg = true;
            }
            if (pc.getTempCharGfx() == 2323) {
                isOrg = true;
            }
            if (isOrg) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra11"));
            } else if (pc.getQuest().isEnd(ALv15_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra1"));
            } else if (pc.getLevel() < ALv15_1.QUEST.get_questlevel()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyra1"));
            } else if (pc.getQuest().isStart(ALv15_1.QUEST.get_id())) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev3"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev1"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("contract1")) {
            QuestClass.get().startQuest(pc, ALv15_1.QUEST.get_id());
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev2"));
        } else if (cmd.equalsIgnoreCase("contract1yes")) {
            getAdena(pc, npc);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev5"));
        } else if (cmd.equalsIgnoreCase("contract1no")) {
            if (pc.getQuest().isStart(ALv15_1.QUEST.get_id())) {
                getAdena(pc, npc);
                QuestClass.get().endQuest(pc, ALv15_1.QUEST.get_id());
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "lyraev4"));
            } else {
                getAdena(pc, npc);
                isCloseList = true;
            }
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private void getAdena(L1PcInstance pc, L1NpcInstance npc) throws Exception {
        long adena = 0;
        L1ItemInstance item1 = pc.getInventory().findItemId(40131);
        if (item1 != null) {
            adena = 0 + (30 * pc.getInventory().removeItem(item1));
        }
        L1ItemInstance item2 = pc.getInventory().findItemId(40132);
        if (item2 != null) {
            adena += 100 * pc.getInventory().removeItem(item2);
        }
        L1ItemInstance item3 = pc.getInventory().findItemId(40133);
        if (item3 != null) {
            adena += 50 * pc.getInventory().removeItem(item3);
        }
        L1ItemInstance item4 = pc.getInventory().findItemId(40134);
        if (item4 != null) {
            adena += 50 * pc.getInventory().removeItem(item4);
        }
        L1ItemInstance item5 = pc.getInventory().findItemId(40135);
        if (item5 != null) {
            adena += 200 * pc.getInventory().removeItem(item5);
        }
        if (adena > 0) {
            CreateNewItem.createNewItem(pc, (int) L1ItemId.ADENA, adena);
        }
    }
}
