package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Herbert extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Herbert.class);
    private static Random _random = new Random();

    private Npc_Herbert() {
    }

    public static NpcExecutor get() {
        return new Npc_Herbert();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "herbert2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "herbert1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("request t-shirt")) {
            isCloseList = getItem(pc, new int[]{40456, 40455, 40457, L1ItemId.ADENA}, new int[]{3, 2, 10, 30000}, new int[]{20085}, new int[]{1});
        } else if (cmd.equalsIgnoreCase("request cloak of magic resistance")) {
            isCloseList = getItem(pc, new int[]{40456, 40455, 40457, L1ItemId.ADENA}, new int[]{10, 2, 1, L1SkillId.STATUS_BRAVE}, new int[]{20056}, new int[]{1});
        } else if (cmd.equalsIgnoreCase("request cloak of protection")) {
            isCloseList = getItem(pc, new int[]{40456, 40455, 40457, L1ItemId.ADENA}, new int[]{5, 5, 10, 20000}, new int[]{20063}, new int[]{1});
        } else {
            cmd.equalsIgnoreCase("request sixth goods of war");
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private boolean getItem(L1PcInstance pc, int[] items, int[] counts, int[] gitems, int[] gcounts) {
        if (CreateNewItem.checkNewItem(pc, items, counts) >= 1) {
            CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
        }
        return true;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 20;
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
            this._point = new Point[]{new Point(33504, 32777), new Point(33501, 32777)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Herbert npc_Herbert, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Herbert._random.nextInt(this._point.length);
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
                }
            } catch (Exception e) {
                Npc_Herbert._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
