package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ToPC implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ToPC.class);

    private L1ToPC() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ToPC();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            L1PcInstance target = World.get().getPlayer(arg);
            if (target != null) {
                L1Teleport.teleport(pc, target.getX(), target.getY(), target.getMapId(), 5, false);
                pc.set_showId(target.get_showId());
                pc.sendPackets(new S_ServerMessage(166, "移動座標至指定人物身邊: " + arg));
                return;
            }
            pc.sendPackets(new S_PacketBoxGm(pc, 1));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
