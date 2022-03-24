package com.lineage.server.command.executor;

import com.lineage.commons.system.LanSecurityManager;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.IpReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PowerKick implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1PowerKick.class);

    private L1PowerKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1PowerKick();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        String gmName;
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 封鎖IP/MAC。");
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
        if (arg.indexOf("remove") != -1) {
            StringTokenizer st = new StringTokenizer(arg);
            st.nextToken();
            String ipaddr = st.nextToken();
            boolean isBan = false;
            if (LanSecurityManager.BANIPMAP.containsKey(arg)) {
                isBan = true;
            }
            if (!isBan) {
                IpReading.get().remove(ipaddr);
                _log.warn("系統命令執行: " + cmdName + " " + ipaddr + " 解除封鎖IP/MAC。");
            }
        } else if (arg.lastIndexOf(".") == -1) {
            L1PcInstance target = World.get().getPlayer(arg);
            if (target != null) {
                ClientExecutor targetClient = target.getNetConnection();
                String ipaddr2 = targetClient.getIp().toString();
                if (ipaddr2 != null && !LanSecurityManager.BANIPMAP.containsKey(ipaddr2)) {
                    IpReading.get().add(ipaddr2.toString(), String.valueOf(gmName) + ":L1PowerKick 封鎖IP");
                }
                if (pc != null) {
                    pc.sendPackets(new S_SystemMessage(String.valueOf(target.getName()) + " 封鎖IP/MAC。"));
                }
                targetClient.kick();
            } else if (pc == null) {
                _log.error("指令異常: 這個命令必須輸入正確人物名稱 或是 IP/MAC位置才能執行。");
            } else {
                pc.sendPackets(new S_PacketBoxGm(pc, 6));
            }
        } else if (!LanSecurityManager.BANIPMAP.containsKey(arg)) {
            IpReading.get().add(arg.toString(), String.valueOf(gmName) + ":L1PowerKick 封鎖IP");
        }
    }
}
