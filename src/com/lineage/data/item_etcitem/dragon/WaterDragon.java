package com.lineage.data.item_etcitem.dragon;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WaterDragon extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(WaterDragon.class);

    private WaterDragon() {
    }

    public static ItemExecutor get() {
        return new WaterDragon();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item != null && pc != null) {
            int time = L1BuffUtil.cancelDragon(pc);
            if (time != -1) {
                pc.sendPackets(new S_ServerMessage(1139, String.valueOf(time / 60)));
                return;
            }
            pc.getInventory().removeItem(item, 1);
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7467));
            SkillMode mode = L1SkillMode.get().getSkill(L1SkillId.DRAGON3);
            if (mode != null) {
                try {
                    mode.start(pc, (L1Character) null, (L1Magic) null, 1200);
                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }
}
