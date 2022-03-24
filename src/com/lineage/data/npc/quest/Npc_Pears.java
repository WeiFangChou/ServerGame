package com.lineage.data.npc.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Pears extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Pears.class);

    private Npc_Pears() {
    }

    public static NpcExecutor get() {
        return new Npc_Pears();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.isDarkelf()) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "pears1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (pc.isDarkelf()) {
            if (cmd.equalsIgnoreCase("request silver sting knife")) {
                if (CreateNewItem.checkNewItem(pc, 40321, 1) >= 1) {
                    CreateNewItem.createNewItem(pc, 40321, 1, 40738, (int) L1SkillId.STATUS_BRAVE);
                }
            } else if (cmd.equalsIgnoreCase("request heavy sting knife")) {
                if (CreateNewItem.checkNewItem(pc, 40322, 1) >= 1) {
                    CreateNewItem.createNewItem(pc, 40322, 1, 40740, 2000);
                }
            } else if (cmd.equalsIgnoreCase("request pears itembag")) {
                if (CreateNewItem.checkNewItem(pc, 40323, 1) >= 1) {
                    CreateNewItem.createNewItem(pc, 40323, 1, 40715, 1);
                }
            } else if (cmd.equalsIgnoreCase("request jin gauntlet") && CreateNewItem.checkNewItem(pc, 40324, 1) >= 1) {
                CreateNewItem.createNewItem(pc, 40324, 1, 194, 1);
            }
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
