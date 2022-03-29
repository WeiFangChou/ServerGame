package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Move implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Move.class);

    private L1Move() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Move();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int mapid;
        try {
            StringTokenizer st = new StringTokenizer(arg);
            int locx = Integer.parseInt(st.nextToken());
            int locy = Integer.parseInt(st.nextToken());
            if (st.hasMoreTokens()) {
                mapid = Short.parseShort(st.nextToken());
            } else {
                mapid = pc.getMapId();
            }
            L1Teleport.teleport(pc, locx, locy, mapid, 5, false);
            pc.sendPackets(new S_SystemMessage("座標 " + locx + ", " + locy + ", " + ((int) mapid) + " 執行移動。"));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
