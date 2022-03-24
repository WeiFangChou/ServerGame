package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CheckUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CH_BossB extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(CH_BossB.class);

    private CH_BossB() {
    }

    public static NpcExecutor get() {
        return new CH_BossB();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 40;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || pc.get_hardinR() == null) {
                return pc;
            }
            pc.get_hardinR().boss_b_death();
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void spawn(L1NpcInstance npc) {
        try {
            switch (npc.getNpcId()) {
                case 91295:
                default:
                    return;
                case 91296:
                    GeneralThreadPool.get().execute(new BossBR(npc));
                    return;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    class BossBR implements Runnable {
        private final L1NpcInstance _npc;

        public BossBR(L1NpcInstance npc) {
            this._npc = npc;
        }

        public void run() {
            try {
                if (!this._npc.isDead()) {
                    Thread.sleep(4000);
                    this._npc.broadcastPacketAll(new S_NpcChat(this._npc, "$7588"));
                }
                if (!this._npc.isDead()) {
                    Thread.sleep(4000);
                    this._npc.broadcastPacketAll(new S_NpcChat(this._npc, "$7591"));
                }
                if (!this._npc.isDead()) {
                    Thread.sleep(4000);
                    this._npc.broadcastPacketAll(new S_NpcChat(this._npc, "$7593"));
                }
            } catch (Exception e) {
                CH_BossB._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
