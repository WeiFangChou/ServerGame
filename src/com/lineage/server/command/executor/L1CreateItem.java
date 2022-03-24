package com.lineage.server.command.executor;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CreateItem implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1CreateItem.class);

    private L1CreateItem() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CreateItem();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int itemid;
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String nameid = st.nextToken();
            long count = 1;
            if (st.hasMoreTokens()) {
                count = Long.parseLong(st.nextToken());
            }
            int enchant = 0;
            if (st.hasMoreTokens()) {
                enchant = Integer.parseInt(st.nextToken());
            }
            try {
                itemid = Integer.parseInt(nameid);
            } catch (NumberFormatException e) {
                itemid = ItemTable.get().findItemIdByNameWithoutSpace(nameid);
                if (itemid == 0) {
                    pc.sendPackets(new S_SystemMessage("沒有找到條件吻合的物品。"));
                    return;
                }
            }
            L1Item temp = ItemTable.get().getTemplate(itemid);
            if (temp == null) {
                pc.sendPackets(new S_SystemMessage("指定ID不存在"));
            } else if (temp.isStackable()) {
                L1ItemInstance item = ItemTable.get().createItem(itemid);
                item.setEnchantLevel(0);
                item.setCount(count);
                item.setIdentified(true);
                if (pc.getInventory().checkAddItem(item, count) == 0) {
                    pc.getInventory().storeItem(item);
                    pc.sendPackets(new S_ServerMessage(403, String.valueOf(item.getLogName()) + "(ID:" + itemid + ")"));
                }
            } else if (count > 10) {
                pc.sendPackets(new S_SystemMessage("不可以堆疊的物品一次創造數量禁止超過10"));
            } else {
                L1ItemInstance item2 = null;
                int createCount = 0;
                while (((long) createCount) < count) {
                    item2 = ItemTable.get().createItem(itemid);
                    item2.setEnchantLevel(enchant);
                    item2.setIdentified(true);
                    if (pc.getInventory().checkAddItem(item2, 1) != 0) {
                        break;
                    }
                    pc.getInventory().storeItem(item2);
                    createCount++;
                }
                if (createCount > 0) {
                    pc.sendPackets(new S_ServerMessage(403, String.valueOf(item2.getLogName()) + "(ID:" + itemid + ")"));
                }
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
