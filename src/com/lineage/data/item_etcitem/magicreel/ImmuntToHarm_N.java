package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

public class ImmuntToHarm_N extends ItemExecutor {
    private ImmuntToHarm_N() {
    }

    public static ItemExecutor get() {
        return new ImmuntToHarm_N();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc != null && item != null) {
            if (pc.getLevel() > 60) {
                pc.sendPackets(new S_ServerMessage(285));
            } else if (pc.getInventory().removeItem(item, 1) >= 1) {
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                new L1SkillUse().handleCommands(pc, 68, pc.getId(), 0, 0, 0, 2);
            }
        }
    }
}
