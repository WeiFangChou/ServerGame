package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Sainisi extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Sainisi.class);

    private Npc_Sainisi() {
    }

    public static NpcExecutor get() {
        return new Npc_Sainisi();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 1;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() == null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            } else if (pc.get_hardinR().get_time() > 0 && pc.get_hardinR().get_time() <= 72) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep003"));
            } else if (pc.get_hardinR().get_time() > 74 && pc.get_hardinR().get_time() <= 96) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep005"));
            } else if (pc.get_hardinR().get_time() > 96 && pc.get_hardinR().get_time() <= 104) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep006"));
            } else if (pc.get_hardinR().get_time() > 136 && pc.get_hardinR().get_time() <= 156) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep007"));
            } else if (pc.get_hardinR().get_time() <= 156 || pc.get_hardinR().get_time() > 170) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep004"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep008"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
