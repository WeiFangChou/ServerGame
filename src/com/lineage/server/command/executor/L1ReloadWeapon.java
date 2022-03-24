package com.lineage.server.command.executor;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.WeaponSkillTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ReloadWeapon implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ReloadWeapon.class);

    private L1ReloadWeapon() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ReloadWeapon();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance paramL1PcInstance, String paramString1, String paramString2) throws Throwable {
        ItemTable.get().load();
        WeaponSkillTable.get().load();
        paramL1PcInstance.sendPackets(new S_SystemMessage("[weapon]+[weapon_skill]資料庫已重讀完成!"));
    }
}
