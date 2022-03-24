package com.lineage.data.item_etcitem.exp;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class REEXP20 extends ItemExecutor {
    private int _time = 1800;

    private REEXP20() {
    }

    public static ItemExecutor get() {
        return new REEXP20();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item)throws Exception {
        if (item != null && pc != null && L1BuffUtil.cancelExpSkill_3(pc)) {
            int time = this._time;
            if (pc.getInventory().removeItem(item, 1) == 1) {
                pc.setSkillEffect(L1SkillId.REEXP20, time * L1SkillId.STATUS_BRAVE);
                pc.sendPackets(new S_ServerMessage("經驗質提升200%(" + this._time + "秒)。"));
            }
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
