package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Jason extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Jason.class);
    private static Random _random = new Random();

    private Npc_Jason() {
    }

    public static NpcExecutor get() {
        return new Npc_Jason();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jason2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "jason1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request timber")) {
            int[] items = {42502};
            int[] counts = {4};
            int[] gitems = {42503};
            int[] gcounts = {1};
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
                isCloseList = true;
            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));
            } else if (xcount < 1) {
                isCloseList = true;
            }
        } else if (cmd.equals("a1")) {
            int[] items2 = {42502};
            int[] counts2 = {4};
            int[] gitems2 = {42503};
            int[] gcounts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("request blank box")) {
            int[] items3 = {42502, L1ItemId.ADENA};
            int[] counts3 = {5, 500};
            int[] gitems3 = {42504};
            int[] gcounts3 = {1};
            long xcount2 = CreateNewItem.checkNewItem(pc, items3, counts3);
            if (xcount2 == 1) {
                CreateNewItem.createNewItem(pc, items3, counts3, gitems3, 1, gcounts3);
                isCloseList = true;
            } else if (xcount2 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount2, "a2"));
            } else if (xcount2 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equals("a2")) {
            int[] items4 = {42502, L1ItemId.ADENA};
            int[] counts4 = {5, 500};
            int[] gitems4 = {42504};
            int[] gcounts4 = {1};
            if (CreateNewItem.checkNewItem(pc, items4, counts4) >= amount) {
                CreateNewItem.createNewItem(pc, items4, counts4, gitems4, amount, gcounts4);
            }
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 25;
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
            this._point = new Point[]{new Point(33449, 32763), new Point(33449, 32762), new Point(33450, 32764), new Point(33452, 32765)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Jason npc_Jason, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Jason._random.nextInt(this._point.length);
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
                    if (this._npc.getLocation().isSamePoint(this._point[1])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 18));
                    } else if (this._npc.getLocation().isSamePoint(this._point[2])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 17));
                    }
                }
            } catch (Exception e) {
                Npc_Jason._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
