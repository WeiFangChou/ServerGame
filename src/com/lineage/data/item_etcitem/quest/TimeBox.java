package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimeBox extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(TimeBox.class);

    private TimeBox() {
    }

    public static ItemExecutor get() {
        return new TimeBox();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            CreateNewItem.createNewItem(pc, 49313, 2);
            item.setLastUsed(new Timestamp(System.currentTimeMillis()));
            pc.getInventory().updateItem(item, 32);
            pc.getInventory().saveItem(item, 32);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
