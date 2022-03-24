package com.lineage.server.command.executor;

import com.lineage.server.EchoServerTimer;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Port implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Port.class);

    private L1Port() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Port();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String cmd = st.nextToken();
            int key = Integer.valueOf(st.nextToken()).intValue();
            if (cmd.equalsIgnoreCase("stop")) {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 關閉 指定監聽端口。");
                EchoServerTimer.get().stopPort(key);
            } else if (cmd.equalsIgnoreCase("start")) {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 開啟 指定監聽端口。");
                EchoServerTimer.get().startPort(key);
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
