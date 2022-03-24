package com.lineage.server;

import com.lineage.list.OnlineUser;
import com.lineage.server.command.GMCommands;
import com.lineage.server.utils.SystemUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CmdEcho {
    private static final Log _log = LogFactory.getLog(CmdEcho.class);
    private static final boolean _msg = false;
    private static final String _t1 = "\n\r--------------------------------------------------";
    private static final String _t2 = "\n\r--------------------------------------------------";

    public CmdEcho(long timer) {
        _log.warn("\n\r--------------------------------------------------" + (timer != 0 ? "\n       啟動指令視窗監聽器!!遊戲伺服器啟動完成!!\n       服務器啟動耗用時間: " + timer + "ms" : "\n       目前連線帳號: " + OnlineUser.get().size()) + "\n\r--------------------------------------------------");
        SystemUtil.printMemoryUsage(_log);
    }

    public void runCmd() {
        try {
            GMCommands.getInstance().handleCommands(new BufferedReader(new InputStreamReader(System.in)).readLine());
            reRunCmd();
        } catch (IOException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void reRunCmd() {
        new CmdEcho(0).runCmd();
    }
}
