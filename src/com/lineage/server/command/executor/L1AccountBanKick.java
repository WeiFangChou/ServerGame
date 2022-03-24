package com.lineage.server.command.executor;

import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AccountBanKick implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1AccountBanKick.class);

    private L1AccountBanKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1AccountBanKick();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        String gmName;
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " 帳號封鎖:" + arg);
                gmName = "系統命令";
            } catch (Exception e) {
                if (pc == null) {
                    _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                    return;
                }
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }
        } else {
            gmName = String.valueOf(pc.getName()) + "命令";
        }
        L1PcInstance target = World.get().getPlayer(arg);
        if (target != null) {
            String info = String.valueOf(target.getName()) + " 該人物帳號完成封鎖。";
            if (pc == null) {
                _log.warn(info);
            } else {
                pc.sendPackets(new S_SystemMessage(info));
            }
            start(target, String.valueOf(gmName) + ":L1AccountBanKick 封鎖帳號");
        } else if (AccountReading.get().isAccount(arg)) {
            IpReading.get().add(arg, String.valueOf(gmName) + ":L1AccountBanKick 封鎖帳號");
        } else if (pc == null) {
            _log.error("指令異常: 這個命令必須輸入正確帳號名稱才能執行。");
        } else {
            pc.sendPackets(new S_PacketBoxGm(pc, 7));
        }
    }

    private void start(L1PcInstance target, String info) {
        IpReading.get().add(target.getAccountName(), info);
        target.getNetConnection().kick();
    }
}
