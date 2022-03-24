package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Kill implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Kill.class);

    private L1Kill() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Kill();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 殺死指定人物。");
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
            target.setCurrentHp(0);
            target.death(null);
        } else if (pc == null) {
            _log.error("指令異常: 這個命令必須輸入人物名稱才能執行。");
        } else {
            pc.sendPackets(new S_PacketBoxGm(pc, 8));
        }
    }
}
