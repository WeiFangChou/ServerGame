package com.lineage.server.command.executor;

import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Spr implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Spr.class);

    private L1Spr() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Spr();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int sprid = Integer.parseInt(arg);
            int attack = SprTable.get().getAttackSpeed(sprid, 1);
            String info = "sprid:" + sprid + "\n\r passispeed:" + SprTable.get().getMoveSpeed(sprid, 0) + "\n\r atkspeed:" + attack + "\n\r dmg:" + SprTable.get().getDmg(sprid) + "\n\r atk_magic_speed:" + SprTable.get().getDirSpellSpeed(sprid) + "\n\r sub_magic_speed:" + SprTable.get().getNodirSpellSpeed(sprid) + "\n\r sub_magic_speed30:" + SprTable.get().getDirSpellSpeed30(sprid);
            if (pc == null) {
                _log.warn("系統命令執行: spr" + sprid + "\n\r" + info);
            } else {
                pc.sendPackets(new S_ServerMessage(166, info));
            }
        } catch (Exception e) {
            if (pc == null) {
                _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                return;
            }
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
