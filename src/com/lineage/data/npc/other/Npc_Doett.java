package com.lineage.data.npc.other;

import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Doett extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Doett.class);
    private static Work _work = null;

    private Npc_Doett() {
    }

    public static NpcExecutor get() {
        return new Npc_Doett();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 17;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            npc.setHeading(npc.targetDirection(pc.getX(), pc.getY()));
            npc.broadcastPacketAll(new S_ChangeHeading(npc));
            if (_work != null) {
                _work.stopMove();
            }
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));
            } else if (pc.isElf()) {
                if (pc.getLawful() < 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettec1"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doette1"));
                }
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM1"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM2"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM3"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "doettM4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 30;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        new Work(this, npc, null).getStart();
    }

    /* access modifiers changed from: private */
    public class Work implements Runnable {
        boolean _isStop;
        private final int[][] _loc;
        private final L1NpcInstance _npc;
        private final NpcWorkMove _npcMove;
        private final Random _random;
        private final int _spr;

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void stopMove() {
            this._isStop = true;
        }

        private Work(L1NpcInstance npc) {
            this._random = new Random();
            this._loc = new int[][]{new int[]{33066, 32311}, new int[]{33065, 32319}, new int[]{33056, 32326}, new int[]{33044, 32323}, new int[]{33051, 32314}};
            this._isStop = false;
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Doett npc_Doett, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            Npc_Doett._work = this;
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            try {
                int[] loc = this._loc[this._random.nextInt(this._loc.length)];
                Point tgloc = new Point(loc[0], loc[1]);
                boolean isMove = true;
                while (isMove) {
                    Thread.sleep((long) this._spr);
                    if (this._isStop) {
                        break;
                    }
                    if (tgloc != null) {
                        isMove = this._npcMove.actionStart(tgloc);
                    }
                    if (this._npc.getLocation().isSamePoint(tgloc)) {
                        isMove = false;
                    }
                }
            } catch (Exception e) {
                Npc_Doett._log.error(e.getLocalizedMessage(), e);
            } finally {
                Npc_Doett._work = null;
            }
        }
    }
}
