package com.lineage.data.item_etcitem.event;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Item_Mazu extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Item_Mazu.class);

    private Item_Mazu() {
    }

    public static ItemExecutor get() {
        return new Item_Mazu();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                if (pc.hasSkillEffect(L1SkillId.MAZU_SKILL)) {
                    pc.sendPackets(new S_ServerMessage("媽祖祝福效果時間尚有" + pc.getSkillEffectTimeSec(L1SkillId.MAZU_SKILL) + "秒。"));
                    return;
                }
                pc.getInventory().removeItem(item, 1);
                pc.setSkillEffect(L1SkillId.MAZU_SKILL, 2400000);
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7321));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
