package com.lineage.data.npc.other;

import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ShopBuyList;
import com.lineage.server.serverpackets.S_ShopSellList;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Philip extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Philip.class);
    private static Random _random = new Random();

    private Npc_Philip() {
    }

    public static NpcExecutor get() {
        return new Npc_Philip();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "philip2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "philip1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (cmd.equalsIgnoreCase("buy")) {
            pc.sendPackets(new S_ShopSellList(npc.getId()));
        } else if (cmd.equalsIgnoreCase("sell")) {
            pc.sendPackets(new S_ShopBuyList(npc.getId(), pc));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 14;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        new Work(this, npc, null).getStart();
    }

    private class Work implements Runnable {
        private L1NpcInstance _npc;
        private NpcWorkMove _npcMove;
        private Point[] _point;
        private int _spr;

        private Work(L1NpcInstance npc) {
            this._point = new Point[]{new Point(33452, 32755), new Point(33451, 32757), new Point(33448, 32758)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Philip npc_Philip, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Philip._random.nextInt(this._point.length);
                if (!this._npc.getLocation().isSamePoint(this._point[t])) {
                    point = this._point[t];
                }
                boolean isWork = true;
                while (isWork) {
                    Thread.sleep((long) this._spr);
                    if (point != null) {
                        isWork = this._npcMove.actionStart(point);
                    } else {
                        isWork = false;
                    }
                    if (this._npc.getLocation().isSamePoint(this._point[2])) {
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        for (int i = 0; i < 3; i++) {
                            this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 7));
                            Thread.sleep((long) this._spr);
                        }
                    }
                }
            } catch (Exception e) {
                Npc_Philip._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
