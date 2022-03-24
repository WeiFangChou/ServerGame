package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;

public class Disintoxicat_Potion extends ItemExecutor {
    private Disintoxicat_Potion() {
    }

    public static ItemExecutor get() {
        return new Disintoxicat_Potion();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.hasSkillEffect(71)) {
            pc.sendPackets(new S_ServerMessage(698));
            return;
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 192));
        pc.getInventory().removeItem(item, 1);
        pc.curePoison();
    }
}
