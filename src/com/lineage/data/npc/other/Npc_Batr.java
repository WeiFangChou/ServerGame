package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Batr extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Batr.class);

    private Npc_Batr() {
    }

    public static NpcExecutor get() {
        return new Npc_Batr();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "batr1"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request sapphire kiringku")) {
            int[] items = {40054, 49205, 49181};
            int[] counts = {3, 1, 1};
            int[] gitems = {270};
            int[] gcounts = {1};
            if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "batr4"));
            } else {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("request obsidian kiringku")) {
            int[] items2 = {40052, 40053, 40054, 40055, 40520, 49092, L1ItemId.ADENA};
            int[] counts2 = {10, 10, 10, 10, 30, 2, 1000000};
            int[] gitems2 = {271};
            int[] gcounts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) < 1) {
                isCloseList = true;
            } else {
                CreateNewItem.createNewItem(pc, items2, counts2, gitems2, 1, gcounts2);
                isCloseList = true;
            }
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
