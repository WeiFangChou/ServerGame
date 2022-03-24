package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerHomeTownTime;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1HomeTown implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1HomeTown.class);

    private L1HomeTown() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1HomeTown();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) throws Exception {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 啟用貢獻度系統。");
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
        String para1 = new StringTokenizer(arg).nextToken();
        if (para1.equalsIgnoreCase("daily")) {
            ServerHomeTownTime.getInstance().dailyProc();
        } else if (para1.equalsIgnoreCase("monthly")) {
            ServerHomeTownTime.getInstance().monthlyProc();
        } else {
            throw new Exception();
        }
    }
}
