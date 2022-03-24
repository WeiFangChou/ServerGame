package com.lineage.server.command.executor;

import com.lineage.server.EchoServerTimer;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Echo implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Echo.class);

    private L1Echo() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Echo();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            if (arg.equalsIgnoreCase("stop")) {
                if (pc == null) {
                    _log.warn("系統命令執行: 全部端口關閉監聽!");
                } else {
                    pc.sendPackets(new S_ServerMessage(166, "全部端口關閉監聽!"));
                }
                EchoServerTimer.get().stopEcho();
                return;
            }
            _log.info("重新啟動服務器端口監聽!");
            EchoServerTimer.get().reStart();
            if (pc != null) {
                pc.sendPackets(new S_ServerMessage(166, "重新啟動服務器端口監聽!"));
            }
            _log.info("監聽端口設置作業完成!!");
        } catch (Exception e) {
            if (pc == null) {
                _log.error("錯誤的命令格式: " + getClass().getSimpleName());
            } else {
                _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                pc.sendPackets(new S_ServerMessage(261));
            }
        } finally {
            _log.info("監聽端口設置作業完成!!");
        }
    }
}
