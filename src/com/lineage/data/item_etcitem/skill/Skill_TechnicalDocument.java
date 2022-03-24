package com.lineage.data.item_etcitem.skill;

import com.lineage.data.cmd.Skill_Check;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_TechnicalDocument extends ItemExecutor {
    private Skill_TechnicalDocument() {
    }

    public static ItemExecutor get() {
        return new Skill_TechnicalDocument();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            if (!pc.isKnight()) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            int itemId = item.getItemId();
            int skillid = 0;
            int attribute = 0;
            int magicLv = 0;
            if (itemId == 40164) {
                skillid = 87;
                attribute = 5;
                magicLv = 31;
            } else if (itemId == 40165) {
                skillid = 88;
                attribute = 5;
                magicLv = 31;
            } else if (itemId == 40166) {
                skillid = 89;
                attribute = 5;
                magicLv = 32;
            } else if (itemId == 41147) {
                skillid = 90;
                attribute = 5;
                magicLv = 31;
            } else if (itemId == 41148) {
                skillid = 91;
                attribute = 5;
                magicLv = 31;
            }
            Skill_Check.check(pc, item, skillid, magicLv, attribute);
        }
    }
}
