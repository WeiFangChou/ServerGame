package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Recall implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Recall.class);

    private L1Recall() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Recall();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        Collection<L1PcInstance> targets = null;
        try {
            if (arg.equalsIgnoreCase("all")) {
                targets = World.get().getAllPlayers();
            } else {
                Collection<L1PcInstance> targets2 = new ArrayList<>();
                try {
                    L1PcInstance tg = World.get().getPlayer(arg);
                    if (tg == null) {
                        pc.sendPackets(new S_PacketBoxGm(pc, 2));
                        return;
                    } else {
                        targets2.add(tg);
                        targets = targets2;
                    }
                } catch (Exception e) {
                    _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
                    pc.sendPackets(new S_ServerMessage(261));
                }
            }
            for (L1PcInstance target : targets) {
                if (!target.isGm()) {
                    L1Teleport.teleportToTargetFront(target, pc, 2);
                    target.sendPackets(new S_SystemMessage("管理者召喚。"));
                }
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
