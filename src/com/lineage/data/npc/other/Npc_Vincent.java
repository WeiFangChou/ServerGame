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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Vincent extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Vincent.class);

    private Npc_Vincent() {
    }

    public static NpcExecutor get() {
        return new Npc_Vincent();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "vincent2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "vincent1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("sell")) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ladar2"));
        } else if (cmd.equalsIgnoreCase("request adena2")) {
            long xcount = CreateNewItem.checkNewItem(pc, new int[]{40405}, new int[]{1});
            if (xcount > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "A1"));
            } else {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("A1")) {
            isCloseList = getItem(pc, new int[]{40405}, new int[]{1}, new int[]{40308}, new int[]{2}, amount);
        } else if (cmd.equalsIgnoreCase("request adena30")) {
            long xcount2 = CreateNewItem.checkNewItem(pc, new int[]{40406}, new int[]{1});
            if (xcount2 > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount2, "A2"));
            } else {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("A2")) {
            isCloseList = getItem(pc, new int[]{40406}, new int[]{1}, new int[]{40308}, new int[]{30}, amount);
        } else if (cmd.equalsIgnoreCase("request hard leather")) {
            long xcount3 = CreateNewItem.checkNewItem(pc, new int[]{40405}, new int[]{20});
            if (xcount3 > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount3, "A3"));
            } else {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("A3")) {
            isCloseList = getItem(pc, new int[]{40405}, new int[]{20}, new int[]{40406}, new int[]{1}, amount);
        } else if (cmd.equalsIgnoreCase("request leather cap")) {
            isCloseList = getItem(pc, new int[]{40405, 40408}, new int[]{5, 1}, new int[]{20001}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather sandal")) {
            isCloseList = getItem(pc, new int[]{40405, 40408}, new int[]{6, 2}, new int[]{20193}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather vest")) {
            isCloseList = getItem(pc, new int[]{40405}, new int[]{10}, new int[]{20090}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather shield")) {
            isCloseList = getItem(pc, new int[]{40405}, new int[]{7}, new int[]{20219}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather boots")) {
            isCloseList = getItem(pc, new int[]{20212, 40406, 40408, L1ItemId.ADENA}, new int[]{1, 10, 10, 300}, new int[]{20192}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather helmet")) {
            isCloseList = getItem(pc, new int[]{20043, 20001, 40406, 40408}, new int[]{1, 1, 5, 5}, new int[]{20002}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request hard leather vest")) {
            isCloseList = getItem(pc, new int[]{20148, 40406, 40408}, new int[]{1, 15, 15}, new int[]{20145}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request leather vest with belt")) {
            isCloseList = getItem(pc, new int[]{20090, 40778}, new int[]{1, 1}, new int[]{20120}, new int[]{1}, 1);
        } else if (cmd.equalsIgnoreCase("request belt")) {
            long xcount4 = CreateNewItem.checkNewItem(pc, new int[]{40406, 40408}, new int[]{5, 2});
            if (xcount4 > 0) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount4, "A4"));
            } else {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("A4")) {
            isCloseList = getItem(pc, new int[]{40406, 40408}, new int[]{5, 2}, new int[]{40778}, new int[]{1}, amount);
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private boolean getItem(L1PcInstance pc, int[] items, int[] counts, int[] gitems, int[] gcounts, long amount) {
        try {
            if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {
                return true;
            }
            CreateNewItem.createNewItem(pc, items, counts, gitems, amount, gcounts);
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return true;
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 35;
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
            this._point = new Point[]{new Point(33480, 32777), new Point(33476, 32777)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Vincent npc_Vincent, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                if (!this._npc.getLocation().isSamePoint(this._point[1])) {
                    point = this._point[1];
                }
                boolean isWork1 = true;
                while (isWork1) {
                    Thread.sleep((long) this._spr);
                    if (point != null) {
                        isWork1 = this._npcMove.actionStart(point);
                    } else {
                        isWork1 = false;
                    }
                }
                this._npc.setHeading(6);
                this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 7));
                Thread.sleep(2000);
                if (!this._npc.getLocation().isSamePoint(this._point[0])) {
                    point = this._point[0];
                }
                boolean isWork2 = true;
                while (isWork2) {
                    Thread.sleep((long) this._spr);
                    if (point != null) {
                        isWork2 = this._npcMove.actionStart(point);
                    } else {
                        isWork2 = false;
                    }
                }
                this._npc.setHeading(4);
                this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
            } catch (Exception e) {
                Npc_Vincent._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
