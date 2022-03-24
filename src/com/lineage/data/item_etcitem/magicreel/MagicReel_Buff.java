package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;

public class MagicReel_Buff extends ItemExecutor {
    private MagicReel_Buff() {
    }

    public static ItemExecutor get() {
        return new MagicReel_Buff();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc != null && item != null) {
            int targetID = data[0];
            if (targetID == 0) {
                pc.sendPackets(new S_ServerMessage(281));
                return;
            }
            L1Object target = World.get().findObject(targetID);
            if (target == null) {
                pc.sendPackets(new S_ServerMessage(281));
                return;
            }
            int spellsc_x = target.getX();
            int spellsc_y = target.getY();
            if (pc.getInventory().removeItem(item, 1) >= 1) {
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                new L1SkillUse().handleCommands(pc, item.getItemId() - 40858, targetID, spellsc_x, spellsc_y, 0, 2);
            }
        }
    }
}
