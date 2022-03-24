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
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Eye_Lindvior extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Eye_Lindvior.class);

    private Eye_Lindvior() {
    }

    public static ItemExecutor get() {
        return new Eye_Lindvior();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                int time = L1BuffUtil.cancelDragon(pc);
                if (time != -1) {
                    pc.sendPackets(new S_ServerMessage(1139, String.valueOf(time / 60)));
                    return;
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7673));
                item.setLastUsed(new Timestamp(System.currentTimeMillis()));
                pc.getInventory().updateItem(item, 32);
                pc.getInventory().saveItem(item, 32);
                SkillMode mode = L1SkillMode.get().getSkill(L1SkillId.DRAGON4);
                if (mode != null) {
                    mode.start(pc, (L1Character) null, (L1Magic) null, 600);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
