package com.lineage.data.item_etcitem.quest;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

public class Holy_WaterEva extends ItemExecutor {
    private Holy_WaterEva() {
    }

    public static ItemExecutor get() {
        return new Holy_WaterEva();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.hasSkillEffect(L1SkillId.STATUS_HOLY_WATER) || pc.hasSkillEffect(L1SkillId.STATUS_HOLY_MITHRIL_POWDER)) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        if (pc.hasSkillEffect(L1SkillId.STATUS_HOLY_MITHRIL_POWDER)) {
            pc.removeSkillEffect(L1SkillId.STATUS_HOLY_MITHRIL_POWDER);
        }
        pc.setSkillEffect(L1SkillId.STATUS_HOLY_WATER_OF_EVA, 900000);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));
        pc.sendPackets(new S_ServerMessage(1140));
        pc.getInventory().removeItem(item, 1);
    }
}
