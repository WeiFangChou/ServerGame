package com.lineage.data.npc.mob;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.data.quest.DragonKnightLv30_1;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DK30_OrcEmissaryA extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(DK30_OrcEmissaryA.class);

    private DK30_OrcEmissaryA() {
    }

    public static NpcExecutor get() {
        return new DK30_OrcEmissaryA();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        boolean isTak = false;
        try {
            if (pc.getTempCharGfx() == 6984) {
                isTak = true;
            }
            if (!isTak || pc.isCrown() || pc.isKnight() || pc.isElf() || pc.isWizard() || pc.isDarkelf()) {
                return;
            }
            if (!pc.isDragonKnight()) {
                pc.isIllusionist();
            } else if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id())) {
                npc.onTalkAction(pc);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "spy_orc1"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean isCloseList;
        L1ItemInstance item;
        if (pc.isDragonKnight()) {
            if (pc.getQuest().isStart(DragonKnightLv30_1.QUEST.get_id()) && cmd.equalsIgnoreCase("request flute of spy") && (item = pc.getInventory().checkItemX(49223, 1)) != null) {
                pc.getInventory().removeItem(item, 1);
                CreateNewItem.getQuestItem(pc, npc, 49222, 1);
            }
            isCloseList = true;
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
