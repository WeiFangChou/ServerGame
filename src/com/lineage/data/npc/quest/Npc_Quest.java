package com.lineage.data.npc.quest;

import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Quest extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Quest.class);

    private Npc_Quest() {
    }

    public static NpcExecutor get() {
        return new Npc_Quest();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.isWindows();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        pc.getAction().action(cmd, 0);
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return OpcodesServer.S_OPCODE_STRUP;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        new Work(this, npc, null).getStart();
    }

    private class Work implements Runnable {
        private final L1NpcInstance _npc;
        private final NpcWorkMove _npcMove;
        private final int _spr;

        private Work(L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Quest npc_Quest, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            try {
                Point home = new Point(this._npc.getX(), this._npc.getY());
                Point tgloc = new Point(this._npc.getX() + 5, this._npc.getY() + 5);
                boolean isMove = true;
                while (isMove) {
                    Thread.sleep((long) this._spr);
                    if (tgloc != null) {
                        isMove = this._npcMove.actionStart(tgloc);
                    }
                    if (this._npc.getLocation().isSamePoint(tgloc)) {
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 1));
                        Thread.sleep(1500);
                    }
                }
                boolean isMove2 = true;
                while (isMove2) {
                    Thread.sleep((long) this._spr);
                    if (home != null) {
                        isMove2 = this._npcMove.actionStart(home);
                    }
                    if (this._npc.getLocation().isSamePoint(home)) {
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 18));
                    }
                }
            } catch (Exception e) {
                Npc_Quest._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
