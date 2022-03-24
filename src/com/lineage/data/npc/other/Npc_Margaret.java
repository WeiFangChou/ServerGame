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

public class Npc_Margaret extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Margaret.class);
    private static Random _random = new Random();

    private Npc_Margaret() {
    }

    public static NpcExecutor get() {
        return new Npc_Margaret();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "margaret2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "margaret1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (cmd.equalsIgnoreCase("buy")) {
            pc.sendPackets(new S_ShopSellList(npc.getId()));
        } else if (cmd.equalsIgnoreCase("sell")) {
            pc.sendPackets(new S_ShopBuyList(npc.getId(), pc));
        } else {
            cmd.equalsIgnoreCase("request first goods of war");
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 17;
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
            this._point = new Point[]{new Point(33449, 32752), new Point(33451, 32750), new Point(33449, 32750)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Margaret npc_Margaret, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Margaret._random.nextInt(this._point.length);
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
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 17));
                    } else if (this._npc.getLocation().isSamePoint(this._point[0])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 18));
                    }
                }
            } catch (Exception e) {
                Npc_Margaret._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
