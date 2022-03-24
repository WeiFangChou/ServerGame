package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
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

public class Npc_Moria extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Moria.class);
    private static Random _random = new Random();

    private Npc_Moria() {
    }

    public static NpcExecutor get() {
        return new Npc_Moria();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else if (pc.isKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria1"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "moria4"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isWizard()) {
            if (cmd.equalsIgnoreCase("request magician dress")) {
                isCloseList = getItem(pc, new int[]{40054, 40318, 40457, 40455}, new int[]{1, 25, 2, 4}, new int[]{20111}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request magician cap")) {
                isCloseList = getItem(pc, new int[]{40051, 40318, 40457, 40456, 40455}, new int[]{2, 20, 1, 1, 1}, new int[]{20012}, new int[]{1});
            }
        }
        if (cmd.equalsIgnoreCase("request swap potion")) {
            int[] items = {40443, 40397, 40398, 40399, 40400, L1ItemId.POTION_OF_EMOTION_WISDOM};
            int[] counts = {5, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, 100};
            int[] gitems = {49015};
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
        } else if (cmd.equalsIgnoreCase("a1")) {
            int[] items2 = {40443, 40397, 40398, 40399, 40400, L1ItemId.POTION_OF_EMOTION_WISDOM};
            int[] counts2 = {5, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_BRAVE, 100};
            int[] gitems2 = {49015};
            int[] gcounts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
            }
            isCloseList = true;
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
        return 18;
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
            this._point = new Point[]{new Point(33405, 32836), new Point(33406, 32833), new Point(33404, 32834)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Moria npc_Moria, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Moria._random.nextInt(this._point.length);
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
                    if (this._npc.getLocation().isSamePoint(this._point[0])) {
                        this._npc.setHeading(6);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 17));
                    }
                }
            } catch (Exception e) {
                Npc_Moria._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
