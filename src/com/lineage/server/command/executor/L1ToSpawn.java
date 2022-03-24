package com.lineage.server.command.executor;

import com.lineage.server.datatables.EventSpawnTable;
import com.lineage.server.datatables.NpcSpawnTable;
import com.lineage.server.datatables.QuesttSpawnTable;
import com.lineage.server.datatables.SpawnTable;
import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Spawn;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ToSpawn implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ToSpawn.class);

    private L1ToSpawn() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ToSpawn();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int id = Integer.parseInt(new StringTokenizer(arg).nextToken());
            L1Spawn spawn = NpcSpawnTable.get().getTemplate(id);
            if (spawn == null) {
                spawn = SpawnTable.get().getTemplate(id);
            }
            if (spawn == null) {
                spawn = EventSpawnTable.get().getTemplate(id);
            }
            if (spawn == null) {
                spawn = SpawnBossReading.get().getTemplate(id);
            }
            if (spawn == null) {
                spawn = QuesttSpawnTable.get().getTemplate(id);
            }
            if (spawn != null) {
                L1Teleport.teleport(pc, spawn.getTmpLocX(), spawn.getTmpLocY(), spawn.getTmpMapid(), 5, false);
                pc.sendPackets(new S_SystemMessage("移動至指定召喚編號: " + id));
                return;
            }
            pc.sendPackets(new S_SystemMessage("沒有這個編號的召喚點: " + id));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName(), e);
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
