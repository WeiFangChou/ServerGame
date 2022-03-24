package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ChangeWeather implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ChangeWeather.class);

    private L1ChangeWeather() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ChangeWeather();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (pc == null) {
            try {
                _log.warn("系統命令執行: " + cmdName + " " + arg + " 遊戲天氣控制。");
                _log.info("說明: 0 終止氣候。");
                _log.info("說明: 1~3 雪控制。");
                _log.info("說明: 17~19 雨控制");
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
        int weather = Integer.parseInt(new StringTokenizer(arg).nextToken());
        World.get().setWeather(weather);
        World.get().broadcastPacketToAll(new S_Weather(weather));
    }
}
