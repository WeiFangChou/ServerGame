package com.lineage.server.command.executor;

import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ReloadShop implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ReloadShop.class);

    private L1ReloadShop() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ReloadShop();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance paramL1PcInstance, String paramString1, String paramString2) {
        ShopTable.get().restshop();
        ShopCnTable.get().restshopCn();
        paramL1PcInstance.sendPackets(new S_SystemMessage("[shop]+[shop_cn]+[shop_rates]資料庫已重讀完成!"));
    }
}
