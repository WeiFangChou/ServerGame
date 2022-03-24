package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Pual extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Pual.class);

    private Npc_Pual() {
    }

    public static NpcExecutor get() {
        return new Npc_Pual();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pual1"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request halpas symbol")) {
            if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                L1ItemInstance item = pc.getInventory().checkItemX(49215, 1);
                if (item != null) {
                    pc.getInventory().removeItem(item, 1);
                    CreateNewItem.getQuestItem(pc, npc, 49223, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(337, "$5731(1)"));
                }
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("request chainsword extinctioner")) {
            L1ItemInstance item1 = pc.getInventory().checkItemX(49230, 1);
            if (item1 != null) {
                pc.getInventory().removeItem(item1);
            }
            isCloseList = getItem(pc, new int[]{49228, 40494, 40779, 40052}, new int[]{1, 100, 10, 3}, new int[]{273}, new int[]{1});
        } else if (cmd.equalsIgnoreCase("request lump of steel")) {
            int[] items = {40899, 40408, L1ItemId.ADENA};
            int[] counts = {5, 5, 500};
            int[] gitems = {40779};
            int[] gcounts = {1};
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
                isCloseList = true;
            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));
            } else if (xcount < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("a1")) {
            int[] items2 = {40899, 40408, L1ItemId.ADENA};
            int[] counts2 = {5, 5, 500};
            int[] gitems2 = {40779};
            int[] gcounts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("request chainsword destroyer")) {
            isCloseList = getItem(pc, new int[]{17, 40406, 40503, 40393, L1ItemId.ADENA}, new int[]{1, 20, 20, 1, 1000000}, new int[]{273}, new int[]{1});
        } else if (cmd.equalsIgnoreCase("request guarder of ancient archer")) {
            isCloseList = getItem(pc, new int[]{20140, 40445, 40504, 40505, 40521, 40495, L1ItemId.ADENA}, new int[]{1, 3, 20, 50, 20, 50, 1000000}, new int[]{21105}, new int[]{1});
        } else if (cmd.equalsIgnoreCase("request guarder of ancient champion")) {
            isCloseList = getItem(pc, new int[]{20143, 40445, L1ItemId.ADENA}, new int[]{1, 5, 1000000}, new int[]{21106}, new int[]{1});
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private boolean getItem(L1PcInstance pc, int[] items, int[] counts, int[] gitems, int[] gcounts) {
        if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
            CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
        }
        return true;
    }
}
