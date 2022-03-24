package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

public class MagicReel_Short extends ItemExecutor {
    private MagicReel_Short() {
    }

    public static ItemExecutor get() {
        return new MagicReel_Short();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc != null && item != null) {
            if (pc.isInvisble() || pc.isInvisDelay()) {
                pc.sendPackets(new S_ServerMessage(281));
                return;
            }
            int targetID = data[0];
            int spellsc_x = data[1];
            int spellsc_y = data[2];
            if (targetID == pc.getId() || targetID == 0) {
                pc.sendPackets(new S_ServerMessage(281));
            } else if (pc.getInventory().removeItem(item, 1) >= 1) {
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                new L1SkillUse().handleCommands(pc, item.getItemId() - 40858, targetID, spellsc_x, spellsc_y, 0, 2);
            }
        }
    }
}
