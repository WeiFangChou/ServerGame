package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Up_hm1 extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Up_hm1.class);

    private Up_hm1() {
    }

    public static ItemExecutor get() {
        return new Up_hm1();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        pc.getInventory().removeItem(item, 1);
        pc.get_other().set_addmp(pc.get_other().get_addmp() + 50);
        pc.get_other().set_addhp(pc.get_other().get_addhp() + 500);
        pc.addMaxHp(500);
        pc.setCurrentHpDirect(pc.getMaxHp());
        pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
        if (pc.isInParty()) {
            pc.getParty().updateMiniHP(pc);
        }
        pc.addMaxMp(50);
        pc.setCurrentHpDirect(pc.getMaxMp());
        pc.sendPackets(new S_MPUpdate(pc));
        try {
            pc.save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
