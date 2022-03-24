package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillSound;

public class Water_Essence extends ItemExecutor {
    private Water_Essence() {
    }

    public static ItemExecutor get() {
        return new Water_Essence();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        useBlessOfEva(pc, item.getItemId());
        pc.getInventory().removeItem(item, 1);
    }

    private void useBlessOfEva(L1PcInstance pc, int item_id) {
        int time;
        if (pc.hasSkillEffect(71)) {
            pc.sendPackets(new S_ServerMessage(698));
            return;
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        if (item_id == 40032) {
            time = 1800;
        } else if (item_id == 40041) {
            time = 300;
        } else if (item_id == 41344) {
            time = 2100;
        } else {
            return;
        }
        if (pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH) && (time = time + pc.getSkillEffectTimeSec(L1SkillId.STATUS_UNDERWATER_BREATH)) > 3600) {
            time = 3600;
        }
        pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), time));
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 190));
        pc.setSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH, time * L1SkillId.STATUS_BRAVE);
    }
}
