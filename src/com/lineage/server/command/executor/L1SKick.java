package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SKick implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1SKick.class);

    private L1SKick() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SKick();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 解除卡點。");
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
        L1PcInstance target = World.get().getPlayer(arg);
        if (target != null) {
            String info = "進行 人物:" + target.getName() + " 解除卡點作業。";
            if (pc == null) {
                _log.warn(info);
            } else {
                pc.sendPackets(new S_SystemMessage(info));
            }
            target.setX(33080);
            target.setY(33392);
            target.setMap( 4);
            target.getNetConnection().kick();
        } else if (pc == null) {
            _log.warn(String.valueOf(arg) + " 不在線上。");
        } else {
            pc.sendPackets(new S_ServerMessage(73, arg));
        }
    }
}
