package com.lineage.server.command.executor;

import com.lineage.config.ConfigRate;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Db implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Db.class);

    private L1Db() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Db();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int rateXp = Integer.parseInt(new StringTokenizer(arg).nextToken(), 10);
            String msgid = null;
            if (pc == null) {
                _log.warn("系統命令執行: " + cmdName + " 變更經驗值倍率" + arg);
            }
            if (((int) ConfigRate.RATE_XP) != rateXp) {
                if (ConfigRate.RATE_XP < ((double) rateXp)) {
                    ConfigRate.RATE_XP = (double) rateXp;
                    msgid = "\\fY伺服器變更經驗值為" + ConfigRate.RATE_XP + "倍，大家請把握時間升級！";
                } else if (ConfigRate.RATE_XP > ((double) rateXp)) {
                    ConfigRate.RATE_XP = (double) rateXp;
                    msgid = "\\fY伺服器變更經驗值為" + ConfigRate.RATE_XP + "倍，祝大家遊戲愉快！";
                }
                if (msgid != null) {
                    World.get().broadcastPacketToAll(new S_ServerMessage(msgid));
                }
                if (pc == null) {
                    _log.warn("目前經驗倍率變更為: " + rateXp);
                }
            } else if (pc == null) {
                _log.warn("目前經驗倍率已經是: " + rateXp);
            } else {
                pc.sendPackets(new S_ServerMessage(166, "目前經驗倍率已經是:" + rateXp));
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
