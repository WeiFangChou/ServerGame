package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.data.quest.DragonKnightLv50_1;
import com.lineage.data.quest.IllusionistLv50_1;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1QuestUser;
import com.lineage.server.world.WorldQuest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ThoughtPieceTime extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(ThoughtPieceTime.class);

    private ThoughtPieceTime() {
    }

    public static ItemExecutor get() {
        return new ThoughtPieceTime();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.isDragonKnight()) {
            if (pc.getQuest().isStart(DragonKnightLv50_1.QUEST.get_id())) {
                pc.getInventory().removeItem(item, 1);
                staraQuest(pc, DragonKnightLv50_1.QUEST.get_id(), 2004);
                return;
            }
            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
        } else if (!pc.isIllusionist()) {
            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
        } else if (pc.getQuest().isStart(IllusionistLv50_1.QUEST.get_id())) {
            pc.getInventory().removeItem(item, 1);
            staraQuest(pc, IllusionistLv50_1.QUEST.get_id(), 2004);
        } else {
            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "cot_ep1st"));
        }
    }

    private void staraQuest(L1PcInstance pc, int questid, int mapid) {
        try {
            int showId = WorldQuest.get().nextId();
            if (QuestMapTable.get().getTemplate(mapid) == -1) {
            }
            L1QuestUser quest = WorldQuest.get().put(showId, mapid, questid, pc);
            if (quest == null) {
                _log.error("副本設置過程發生異常!!");
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            Integer time = QuestMapTable.get().getTime(mapid);
            if (time != null) {
                quest.set_time(time.intValue());
            }
            L1Teleport.teleport(pc, 32729, 32831, (short) mapid, 2, true);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
