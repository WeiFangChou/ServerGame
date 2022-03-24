package com.lineage.data.npc.other;

import com.lineage.data.QuestClass;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.KnightLv15_1;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Aanon extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Aanon.class);

    private Npc_Aanon() {
    }

    public static NpcExecutor get() {
        return new Npc_Aanon();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 19;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            if (pc.isCrown()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            } else if (pc.isKnight()) {
                if (pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
                } else if (pc.getLevel() < KnightLv15_1.QUEST.get_questlevel()) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
                } else if (pc.getQuest().get_step(KnightLv15_1.QUEST.get_id()) == 2) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon4"));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
                }
            } else if (pc.isElf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            } else if (pc.isWizard()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            } else if (pc.isDarkelf()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            } else if (pc.isDragonKnight()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            } else if (pc.isIllusionist()) {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon8"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (pc.isKnight() && cmd.equalsIgnoreCase("request hood of red knight")) {
            if (!pc.getQuest().isEnd(KnightLv15_1.QUEST.get_id())) {
                if (pc.getQuest().get_step(KnightLv15_1.QUEST.get_id()) != 2) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "aanon1"));
                } else if (CreateNewItem.checkNewItem(pc, new int[]{40540, 40601, 20005}, new int[]{1, 1, 1}) < 1) {
                    isCloseList = true;
                } else {
                    CreateNewItem.createNewItem(pc, new int[]{40540, 40601, 20005}, new int[]{1, 1, 1}, new int[]{20027}, 1, new int[]{1});
                    QuestClass.get().endQuest(pc, KnightLv15_1.QUEST.get_id());
                    isCloseList = true;
                }
            } else {
                return;
            }
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int workTime() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void work(L1NpcInstance npc) {
        if (npc.getStatus() != 4) {
            npc.setStatus(4);
            npc.broadcastPacketAll(new S_NPCPack(npc));
        }
        new Work(this, npc, null).getStart();
    }

    private class Work implements Runnable {
        private L1NpcInstance _npc;
        private int _spr;

        private Work(L1NpcInstance npc) {
            this._npc = npc;
            this._spr = SprTable.get().getMoveSpeed(npc.getTempCharGfx(), 0);
        }

        /* synthetic */ Work(Npc_Aanon npc_Aanon, L1NpcInstance l1NpcInstance, Work work) {
            this(l1NpcInstance);
        }

        public void getStart() {
            GeneralThreadPool.get().schedule(this, 10);
        }

        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    this._npc.broadcastPacketX8(new S_DoActionGFX(this._npc.getId(), 30));
                    Thread.sleep((long) this._spr);
                } catch (Exception e) {
                    Npc_Aanon._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
