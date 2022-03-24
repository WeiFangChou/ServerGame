package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserColorU extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserColorU.class);

    private UserColorU() {
    }

    public static ItemExecutor get() {
        return new UserColorU();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getLawful() <= 0) {
            pc.getInventory().removeItem(item, 1);
            pc.setLawful(32767);
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));
            try {
                pc.save();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(79));
        }
    }
}
