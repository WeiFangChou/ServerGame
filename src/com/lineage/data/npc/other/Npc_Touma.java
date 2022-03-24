package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Touma extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Touma.class);

    private Npc_Touma() {
    }

    public static NpcExecutor get() {
        return new Npc_Touma();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 17;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "touma1"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 20;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        if (npc.getStatus() != 4) {
            npc.setStatus(4);
            npc.broadcastPacketAll(new S_NPCPack(npc));
        }
        new Work(this, npc, null).getStart();
    }

    private class Work implements Runnable {
        private L1NpcInstance _npc;
        private int _spr;

        private Work(L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
        }

        /* synthetic */ Work(Npc_Touma npc_Touma, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 30));
                    Thread.sleep((long) this._spr);
                } catch (Exception e) {
                    Npc_Touma._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
