package com.lineage.server.command.executor;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcSpawnTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1NpcSet implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1NpcSet.class);

    private L1NpcSet() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1NpcSet();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(Integer.parseInt(arg));
            if (npc == null) {
                pc.sendPackets(new S_SystemMessage("找不到該npc。"));
                return;
            }
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(pc.getMap());
            npc.setX(pc.getX());
            npc.setY(pc.getY());
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(pc.getHeading());
            npc.set_showId(pc.get_showId());
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            NpcSpawnTable.get().storeSpawn(pc, npc.getNpcTemplate());
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
