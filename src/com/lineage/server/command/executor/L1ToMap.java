package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ToMap implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ToMap.class);

    private L1ToMap() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ToMap();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            Integer mapId = Integer.valueOf(Integer.parseInt(arg));
            L1Map mapData = L1WorldMap.get().getMap(mapId.shortValue());
            if (mapData == null) {
                _log.error("指定地圖不存在" + mapId);
                return;
            }
            int x = mapData.getX();
            int y = mapData.getY();
            L1Location newLocation = new L1Location(x + (mapData.getHeight() / 2), y + (mapData.getWidth() / 2), mapId.intValue()).randomLocation(200, true);
            L1Teleport.teleport(pc, newLocation.getX(), newLocation.getY(), mapId.shortValue(), 5, true);
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
