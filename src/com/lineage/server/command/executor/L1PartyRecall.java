package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PartyRecall implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1PartyRecall.class);

    private L1PartyRecall() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1PartyRecall();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            L1PcInstance target = World.get().getPlayer(arg);
            if (target != null) {
                L1Party party = target.getParty();
                if (party != null) {
                    int x = pc.getX();
                    int y = pc.getY() + 2;
                    short map = pc.getMapId();
                    ConcurrentHashMap<Integer, L1PcInstance> pcs = party.partyUsers();
                    if (!pcs.isEmpty() && pcs.size() > 0) {
                        for (L1PcInstance pc2 : pcs.values()) {
                            try {
                                L1Teleport.teleport(pc2, x, y, map, 5, true);
                                pc2.sendPackets(new S_SystemMessage("管理員召喚!"));
                            } catch (Exception e) {
                                _log.error("隊伍召喚異常", e);
                            }
                        }
                        return;
                    }
                    return;
                }
                pc.sendPackets(new S_SystemMessage(String.valueOf(arg) + " 不是一個隊伍成員!"));
                return;
            }
            pc.sendPackets(new S_PacketBoxGm(pc, 3));
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
