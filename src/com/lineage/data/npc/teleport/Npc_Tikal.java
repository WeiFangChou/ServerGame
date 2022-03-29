package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Npc_Tikal extends NpcExecutor {
    private Npc_Tikal() {
    }

    public static NpcExecutor get() {
        return new Npc_Tikal();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "tikalgate1"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (cmd.equals("e")) {
            L1ItemInstance item = pc.getInventory().checkItemX(49273, 1);
            if (item != null) {
                pc.getInventory().removeItem(item, 1);
                L1Teleport.teleport(pc, 32732, 32862,  784, 5, true);
                return;
            }
            pc.sendPackets(new S_ServerMessage(337, ItemTable.get().getTemplate(49273).getName()));
        }
    }
}
