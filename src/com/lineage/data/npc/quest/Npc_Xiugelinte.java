package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Xiugelinte extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Xiugelinte.class);

    private Npc_Xiugelinte() {
    }

    public static NpcExecutor get() {
        return new Npc_Xiugelinte();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint1"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            if (!cmd.equalsIgnoreCase("0")) {
                return;
            }
            if (pc.getInventory().checkItem(49335)) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint4"));
                return;
            }
            L1ItemInstance item = pc.getInventory().checkItemX(L1ItemId.ADENA, 1000);
            if (item != null) {
                pc.getInventory().removeItem(item, 1000);
                CreateNewItem.createNewItem(pc, 49335, 1);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint2"));
                return;
            }
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hugrint3"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
