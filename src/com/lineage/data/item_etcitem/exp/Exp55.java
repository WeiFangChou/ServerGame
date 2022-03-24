package com.lineage.data.item_etcitem.exp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Exp55 extends ItemExecutor {
    private int _time = 1800;

    private Exp55() {
    }

    public static ItemExecutor get() {
        return new Exp55();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null && L1BuffUtil.cancelExpSkill(pc)) {
            int time = this._time;
            pc.setSkillEffect(L1SkillId.EXP55, time * L1SkillId.STATUS_BRAVE);
            pc.getInventory().removeItem(item, 1);
            pc.sendPackets(new S_ServerMessage("經驗質提升550%(" + this._time + "秒)。"));
            pc.sendPackets(new S_PacketBoxCooking(pc, 32, time));
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._time = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
