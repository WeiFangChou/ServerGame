package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;

public class MagicReel_Weapon extends ItemExecutor {
    private MagicReel_Weapon() {
    }

    public static ItemExecutor get() {
        return new MagicReel_Weapon();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int targetID;
        L1ItemInstance tgItem;
        if (pc != null && item != null && (tgItem = pc.getInventory().getItem((targetID = data[0]))) != null) {
            if (tgItem.getItem().getType2() != 1) {
                pc.sendPackets(new S_ServerMessage(281));
            } else if (pc.getInventory().removeItem(item, 1) >= 1) {
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                new L1SkillUse().handleCommands(pc, item.getItemId() - 40858, targetID, 0, 0, 0, 2);
            }
        }
    }
}
