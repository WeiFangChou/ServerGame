package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Loc implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Loc.class);

    private L1Loc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Loc();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int locx = pc.getX();
            int locy = pc.getY();
            int mapid = pc.getMapId();
            int gab = pc.getMap().getOriginalTile(locx, locy);
            int h = pc.getHeading();
            Object[] objArr = new Object[6];
            objArr[0] = locx;
            objArr[1] = locy;
            objArr[2] = mapid;
            objArr[3] = h;
            objArr[4] = gab;
            objArr[5] = (pc.getMap().isCombatZone(locx, locy) ? "戰鬥區域" : "") + (pc.getMap().isSafetyZone(locx, locy) ? "安全區域" : "") + (pc.getMap().isNormalZone(locx, locy) ? "一般區域" : "");
            pc.sendPackets(new S_SystemMessage(String.format("座標 (%d, %d, %d, %d) %d %s", objArr)));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
