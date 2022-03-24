package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Valok extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Valok.class);

    private Npc_Valok() {
    }

    public static NpcExecutor get() {
        return new Npc_Valok();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 33;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.get_hardinR() != null) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_ep017"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "j_html05"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void spawn(L1NpcInstance npc) {
        GeneralThreadPool.get().execute(new ValokR(npc));
    }

    class ValokR implements Runnable {
        private final L1NpcInstance _npc;

        public ValokR(L1NpcInstance npc) {
            this._npc = npc;
        }

        public void run() {
            try {
                Thread.sleep(7000);
                this._npc.broadcastPacketAll(new S_NpcChat(this._npc, "$7675"));
            } catch (Exception e) {
                Npc_Valok._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
