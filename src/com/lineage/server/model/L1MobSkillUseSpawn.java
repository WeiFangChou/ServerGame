package com.lineage.server.model;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NPCPack;
import com.lineage.server.world.World;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1MobSkillUseSpawn {
    private static final Log _log = LogFactory.getLog(L1MobSkillUseSpawn.class);
    private Random _rnd = new Random();

    public void mobspawn(L1Character attacker, L1Character target, int summonId, int count) {
        for (int i = 0; i < count; i++) {
            mobspawn(attacker, target, summonId);
        }
    }

    public L1MonsterInstance mobspawnX(L1Character attacker, L1Character target, int summonId) {
        return mobspawn(attacker, target, summonId);
    }

    private L1MonsterInstance mobspawn(L1Character attacker, L1Character target, int summonId) {
        try {
            L1NpcInstance mob = NpcTable.get().newNpcInstance(summonId);
            if (mob == null) {
                _log.error("NPC召喚技能 目標NPCID設置異常 異常編號: " + summonId);
                return null;
            }
            mob.setId(IdFactoryNpc.get().nextId());
            L1Location loc = attacker.getLocation().randomLocation(6, false);
            int heading = this._rnd.nextInt(8);
            mob.setX(loc.getX());
            mob.setY(loc.getY());
            mob.setHomeX(loc.getX());
            mob.setHomeY(loc.getY());
            mob.setMap(attacker.getMapId());
            mob.setHeading(heading);
            mob.set_showId(attacker.get_showId());
            World.get().storeObject(mob);
            World.get().addVisibleObject(mob);
            L1MonsterInstance newnpc = (L1MonsterInstance) World.get().findObject(mob.getId());
            newnpc.set_storeDroped(true);
            switch (mob.getNpcId()) {
                case 45061:
                case 45161:
                case 45181:
                case 45455:
                    newnpc.broadcastPacketAll(new S_DoActionGFX(newnpc.getId(), 11));
                    newnpc.setStatus(13);
                    newnpc.broadcastPacketAll(new S_NPCPack(newnpc));
                    newnpc.broadcastPacketAll(new S_DoActionGFX(newnpc.getId(), 4));
                    newnpc.setStatus(0);
                    newnpc.broadcastPacketAll(new S_NPCPack(newnpc));
                    break;
            }
            newnpc.onNpcAI();
            newnpc.startChat(0);
            if (target != null) {
                newnpc.setLink(target);
            }
            if (newnpc != null) {
                return newnpc;
            }
            return null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public void mobspawnSrc(L1Character attacker, L1Character target, int summonId) {
        try {
            L1NpcInstance npc = NpcTable.get().newNpcInstance(summonId);
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setMap(attacker.getMapId());
            npc.setX(attacker.getX());
            npc.setY(attacker.getY());
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(attacker.getHeading());
            npc.set_showId(attacker.get_showId());
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            npc.turnOnOffLight();
            npc.startChat(0);
            npc.setLink(target);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
