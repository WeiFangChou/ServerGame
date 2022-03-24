package com.lineage.server.command.executor;

import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.datatables.Item_Box_Table;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ReloadEtcitem_box implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ReloadEtcitem_box.class);

    private L1ReloadEtcitem_box() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ReloadEtcitem_box();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance paramL1PcInstance, String paramString1, String paramString2) {
        ItemBoxTable.get().load();
        Item_Box_Table.get().load();
        paramL1PcInstance.sendPackets(new S_SystemMessage("[┌etcitem_box┬etcitem_box_key┬etcitem_box_questit┬emetcitem_boxs┐]資料庫已重讀完成!"));
    }
}
