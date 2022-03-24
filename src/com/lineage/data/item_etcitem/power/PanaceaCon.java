package com.lineage.data.item_etcitem.power;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PanaceaCon extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(PanaceaCon.class);

    private PanaceaCon() {
    }

    public static ItemExecutor get() {
        return new PanaceaCon();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getBaseCon() >= ConfigAlt.POWERMEDICINE || pc.getElixirStats() >= ConfigAlt.MEDICINE) {
            if (pc.getBaseCon() >= ConfigAlt.POWERMEDICINE) {
                pc.sendPackets(new S_ServerMessage("Con能力值" + ConfigAlt.POWERMEDICINE + "以後不能喝萬能藥! "));
            }
            if (pc.getElixirStats() >= ConfigAlt.MEDICINE) {
                pc.sendPackets(new S_ServerMessage("萬能藥只能喝" + ConfigAlt.MEDICINE + "瓶"));
                return;
            }
            return;
        }
        pc.addBaseCon(1);
        pc.resetBaseAc();
        pc.setElixirStats(pc.getElixirStats() + 1);
        pc.getInventory().removeItem(item, 1);
        pc.sendPackets(new S_OwnCharStatus2(pc));
        try {
            pc.save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
