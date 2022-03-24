package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PetMatch;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_PetWar extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_PetWar.class);

    private Npc_PetWar() {
    }

    public static NpcExecutor get() {
        return new Npc_PetWar();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "petmatcher"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            String[] temp = cmd.split(",");
            int objid2 = Integer.valueOf(temp[2]).intValue();
            if (pc.getPetList().values().toArray().length > 0) {
                pc.sendPackets(new S_ServerMessage(1187));
            } else if (!L1PetMatch.getInstance().enterPetMatch(pc, objid2)) {
                pc.sendPackets(new S_ServerMessage(1182));
            } else {
                temp[0].equalsIgnoreCase("ent");
                if (0 != 0) {
                    pc.sendPackets(new S_CloseList(pc.getId()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
