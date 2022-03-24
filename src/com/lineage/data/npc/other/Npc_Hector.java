package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.cmd.NpcWorkMove;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
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

public class Npc_Hector extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Hector.class);
    private static Random _random = new Random();

    private Npc_Hector() {
    }

    public static NpcExecutor get() {
        return new Npc_Hector();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (pc.getLawful() < 0) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hector2"));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "hector1"));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (!cmd.equalsIgnoreCase("request fifth goods of war")) {
            if (cmd.equalsIgnoreCase("request iron gloves")) {
                isCloseList = getItem(pc, new int[]{20182, 40408, L1ItemId.ADENA}, new int[]{1, 150, 25000}, new int[]{20163}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request iron visor")) {
                isCloseList = getItem(pc, new int[]{20006, 40408, L1ItemId.ADENA}, new int[]{1, OpcodesServer.S_OPCODE_STRUP, 16500}, new int[]{20003}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request iron shield")) {
                isCloseList = getItem(pc, new int[]{20231, 40408, L1ItemId.ADENA}, new int[]{1, 200, 16000}, new int[]{20220}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request iron boots")) {
                isCloseList = getItem(pc, new int[]{20205, 40408, L1ItemId.ADENA}, new int[]{1, L1SkillId.AQUA_PROTECTER, L1EffectInstance.CUBE_TIME}, new int[]{20194}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request iron plate mail")) {
                isCloseList = getItem(pc, new int[]{20154, 40408, L1ItemId.ADENA}, new int[]{1, 450, 30000}, new int[]{20091}, new int[]{1});
            } else if (cmd.equalsIgnoreCase("request slim plate")) {
                int[] items = {40408, L1ItemId.ADENA};
                int[] counts = {10, 500};
                int[] gitems = {40526};
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
                int[] items2 = {40408, L1ItemId.ADENA};
                int[] counts2 = {10, 500};
                int[] gitems2 = {40526};
                int[] gcounts2 = {1};
                if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                    CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
                }
                isCloseList = true;
            } else if (cmd.equalsIgnoreCase("request lump of steel")) {
                int[] items3 = {40899, 40408, L1ItemId.ADENA};
                int[] counts3 = {5, 5, 500};
                int[] gitems3 = {40779};
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
            } else if (cmd.equalsIgnoreCase("a2")) {
                int[] items4 = {40899, 40408, L1ItemId.ADENA};
                int[] counts4 = {5, 5, 500};
                int[] gitems4 = {40779};
                int[] gcounts4 = {1};
                if (CreateNewItem.checkNewItem(pc, items4, counts4) >= amount) {
                    CreateNewItem.createNewItem(pc, items4, counts4, gitems4, amount, gcounts4);
                }
                isCloseList = true;
            }
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
        return 15;
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
            this._point = new Point[]{new Point(33471, 32775), new Point(33471, 32773), new Point(33468, 32774)};
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
            this._npcMove = new NpcWorkMove(npc);
        }

        /* synthetic */ Work(Npc_Hector npc_Hector, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            Point point = null;
            try {
                int t = Npc_Hector._random.nextInt(this._point.length);
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
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 7));
                    } else if (this._npc.getLocation().isSamePoint(this._point[1])) {
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 19));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 19));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 19));
                    } else if (this._npc.getLocation().isSamePoint(this._point[2])) {
                        this._npc.setHeading(4);
                        this._npc.broadcastPacketX8(new S_ChangeHeading(this._npc));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 17));
                        Thread.sleep((long) this._spr);
                        this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 18));
                    }
                }
            } catch (Exception e) {
                Npc_Hector._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
