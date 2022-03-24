package com.lineage.server.command.executor;

import com.lineage.server.Shutdown;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Shutdown implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Shutdown.class);

    private L1Shutdown() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Shutdown();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 秒後關閉伺服器。");
            } catch (Exception e) {
                if (pc == null) {
                    _log.error("錯誤的命令格式: " + getClass().getSimpleName());
                    return;
                }
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
                return;
            }
        }
        if (arg.equalsIgnoreCase("now")) {
            Shutdown.getInstance().startShutdown(pc, 5, true);
        } else if (arg.equalsIgnoreCase("abort")) {
            Shutdown.getInstance().abort(pc);
        } else {
            Shutdown.getInstance().startShutdown(pc, Math.max(5, Integer.parseInt(arg)), true);
        }
    }
}
