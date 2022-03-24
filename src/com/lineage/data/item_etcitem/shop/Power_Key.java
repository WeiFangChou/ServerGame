package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Power_Key extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Power_Key.class);

    private Power_Key() {
    }

    public static ItemExecutor get() {
        return new Power_Key();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            L1ItemInstance tgitem = pc.getInventory().getItem(data[0]);
            if (tgitem != null) {
                int itemid = data[0];
                if (tgitem.getItem().getType() != 16) {
                    return;
                }
                if (!ItemBoxTable.get().is_key(tgitem.getItemId(), itemid)) {
                    pc.sendPackets(new S_ServerMessage(79));
                } else if (pc.getInventory().removeItem(item, 1) == 1) {
                    ItemBoxTable.get().get_key(pc, tgitem, itemid);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
