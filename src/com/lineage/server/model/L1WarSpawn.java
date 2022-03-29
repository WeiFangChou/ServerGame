package com.lineage.server.model;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1WarSpawn {
    private static L1WarSpawn _instance;
    private static final Log _log = LogFactory.getLog(L1WarSpawn.class);

    public static L1WarSpawn getInstance() {
        if (_instance == null) {
            _instance = new L1WarSpawn();
        }
        return _instance;
    }

    public void spawnTower(int castleId) {
        int npcId = 81111;
        if (castleId == 7) {
            npcId = 81189;
        }
        L1Npc l1npc = NpcTable.get().getTemplate(npcId);
        int[] iArr = new int[3];
        int[] loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1],  loc[2]);
        if (castleId == 7) {
            spawnSubTower();
        }
    }

    private void spawnSubTower() {
        int[] iArr = new int[3];
        for (int i = 1; i <= 4; i++) {
            L1Npc l1npc = NpcTable.get().getTemplate(81189 + i);
            int[] loc = L1CastleLocation.getSubTowerLoc(i);
            SpawnWarObject(l1npc, loc[0], loc[1],  loc[2]);
        }
    }

    public void SpawnCrown(int castleId) {
        L1Npc l1npc = NpcTable.get().getTemplate(81125);
        int[] iArr = new int[3];
        int[] loc = L1CastleLocation.getTowerLoc(castleId);
        SpawnWarObject(l1npc, loc[0], loc[1],  loc[2]);
    }

    public void SpawnFlag(int castleId) {
        L1Npc l1npc = NpcTable.get().getTemplate(81122);
        int[] iArr = new int[5];
        int[] loc = L1CastleLocation.getWarArea(castleId);
        int locx1 = loc[0];
        int locx2 = loc[1];
        int locy1 = loc[2];
        int locy2 = loc[3];
        int mapid =  loc[4];
        for (int x = locx1; x <= locx2; x += 8) {
            SpawnWarObject(l1npc, x, locy1, mapid);
        }
        for (int y = locy1; y <= locy2; y += 8) {
            SpawnWarObject(l1npc, locx2, y, mapid);
        }
        for (int x2 = locx2; x2 >= locx1; x2 -= 8) {
            SpawnWarObject(l1npc, x2, locy2, mapid);
        }
        for (int y2 = locy2; y2 >= locy1; y2 -= 8) {
            SpawnWarObject(l1npc, locx1, y2, mapid);
        }
        switch (castleId) {
        }
    }

    private void SpawnWarObject(L1Npc l1npc, int locx, int locy, int mapid) {
        if (l1npc != null) {
            try {
                L1NpcInstance npc = NpcTable.get().newNpcInstance(l1npc);
                npc.setId(IdFactoryNpc.get().nextId());
                npc.setX(locx);
                npc.setY(locy);
                npc.setHomeX(locx);
                npc.setHomeY(locy);
                npc.setHeading(0);
                npc.setMap(mapid);
                World.get().storeObject(npc);
                World.get().addVisibleObject(npc);
                for (L1PcInstance pc : World.get().getAllPlayers()) {
                    npc.addKnownObject(pc);
                    pc.addKnownObject(npc);
                    pc.sendPacketsAll(new S_NPCPack(npc));
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
