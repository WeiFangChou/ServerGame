package com.lineage.server.command.executor;

import com.lineage.data.item_armor.set.ArmorSet;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CreateItemSet implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1CreateItemSet.class);

    private L1CreateItemSet() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CreateItemSet();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int[] iArr = ArmorSet.getAllSet().get(Integer.valueOf(Integer.parseInt(arg))).get_ids();
            for (int itemid : iArr) {
                L1Item temp = ItemTable.get().getTemplate(itemid);
                if (temp != null) {
                    pc.getInventory().storeItem(itemid, 1);
                    pc.sendPackets(new S_ServerMessage(403, String.valueOf(temp.getName()) + "(ID:" + itemid + ")"));
                } else {
                    _log.error("找不到指定編號物品:" + itemid + " 套裝編號:" + arg + " 執行的GM:" + pc.getName());
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
