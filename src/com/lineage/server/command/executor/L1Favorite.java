package com.lineage.server.command.executor;

import com.lineage.server.command.GMCommands;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Favorite implements L1CommandExecutor {
    private static final Map<Integer, String> _faviCom = new HashMap();
    private static final Log _log = LogFactory.getLog(L1Favorite.class);

    private L1Favorite() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Favorite();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            if (!_faviCom.containsKey(Integer.valueOf(pc.getId()))) {
                _faviCom.put(Integer.valueOf(pc.getId()), "");
            }
            String faviCom = _faviCom.get(Integer.valueOf(pc.getId()));
            if (arg.startsWith("set")) {
                StringTokenizer st = new StringTokenizer(arg);
                st.nextToken();
                if (!st.hasMoreTokens()) {
                    pc.sendPackets(new S_SystemMessage("紀錄質不能為空白"));
                    return;
                }
                StringBuilder cmd = new StringBuilder();
                String temp = st.nextToken();
                if (temp.equalsIgnoreCase(cmdName)) {
                    pc.sendPackets(new S_SystemMessage(String.valueOf(cmdName) + " 紀錄質異常。"));
                    return;
                }
                cmd.append(String.valueOf(temp) + " ");
                while (st.hasMoreTokens()) {
                    cmd.append(String.valueOf(st.nextToken()) + " ");
                }
                String faviCom2 = cmd.toString().trim();
                _faviCom.put(Integer.valueOf(pc.getId()), faviCom2);
                pc.sendPackets(new S_SystemMessage(String.valueOf(faviCom2) + " 指令紀錄完成!"));
            } else if (arg.startsWith("show")) {
                pc.sendPackets(new S_SystemMessage("目前紀錄的指令: " + faviCom));
            } else if (faviCom.isEmpty()) {
                pc.sendPackets(new S_SystemMessage("目前無紀錄指令!"));
            } else {
                StringBuilder cmd2 = new StringBuilder();
                StringTokenizer st2 = new StringTokenizer(arg);
                StringTokenizer st22 = new StringTokenizer(faviCom);
                while (st22.hasMoreTokens()) {
                    String temp2 = st22.nextToken();
                    if (temp2.startsWith("%")) {
                        cmd2.append(String.valueOf(st2.nextToken()) + " ");
                    } else {
                        cmd2.append(String.valueOf(temp2) + " ");
                    }
                }
                while (st2.hasMoreTokens()) {
                    cmd2.append(String.valueOf(st2.nextToken()) + " ");
                }
                pc.sendPackets(new S_SystemMessage(((Object) cmd2) + " 指令執行。"));
                GMCommands.getInstance().handleCommands(pc, cmd2.toString());
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
