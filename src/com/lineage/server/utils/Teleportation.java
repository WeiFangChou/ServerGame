package com.lineage.server.utils;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_NPCPack_Doll;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_NPCPack_Summon;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_PacketBoxWindShackle;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Teleportation {
    private static final Log _log = LogFactory.getLog(Teleportation.class);
    private static Random _random = new Random();

    private Teleportation() {
    }

    public static void teleportation(L1PcInstance pc) {
        if (pc != null) {
            try {
                if (!(pc.getOnlineStatus() == 0 || pc.getNetConnection() == null || pc.isDead() || pc.isTeleport())) {
                    if (pc.getTradeID() != 0) {
                        new L1Trade().tradeCancel(pc);
                    }
                    pc.getMap().setPassable(pc.getLocation(), true);
                    int x = pc.getTeleportX();
                    int y = pc.getTeleportY();
                    short mapId = pc.getTeleportMapId();
                    int head = pc.getTeleportHeading();
                    if (!L1WorldMap.get().getMap(mapId).isInMap(x, y) && !pc.isGm()) {
                        x = pc.getX();
                        y = pc.getY();
                        mapId = pc.getMapId();
                    }
                    L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                    if (clan != null && clan.getWarehouseUsingChar() == pc.getId()) {
                        clan.setWarehouseUsingChar(0);
                    }
                    World.get().moveVisibleObject(pc, mapId);
                    pc.setLocation(x, y, mapId);
                    pc.setHeading(head);
                    pc.setOleLocX(x);
                    pc.setOleLocY(y);
                    pc.sendPackets(new S_MapID(pc, pc.getMapId(), pc.getMap().isUnderwater()));
                    if (!pc.isGhost() && !pc.isGmInvis() && !pc.isInvisble()) {
                        pc.broadcastPacketAll(new S_OtherCharPacks(pc));
                    }
                    if (pc.isReserveGhost()) {
                        pc.endGhost();
                    }
                    pc.sendPackets(new S_OwnCharPack(pc));
                    pc.removeAllKnownObjects();
                    pc.sendVisualEffectAtTeleport();
                    pc.updateObject();
                    pc.sendPackets(new S_CharVisualUpdate(pc));
                    pc.killSkillEffectTimer(32);
                    pc.setCallClanId(0);
                    HashSet<L1PcInstance> subjects = new HashSet<>();
                    subjects.add(pc);
                    if (!pc.isGhost()) {
                        if (pc.getMap().isTakePets()) {
                            for (L1NpcInstance petNpc : pc.getPetList().values()) {
                                L1Location loc = pc.getLocation().randomLocation(3, false);
                                int nx = loc.getX();
                                int ny = loc.getY();
                                if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134) {
                                    nx = (32799 + _random.nextInt(5)) - 3;
                                    ny = (32864 + _random.nextInt(5)) - 3;
                                }
                                petNpc.set_showId(pc.get_showId());
                                teleport(petNpc, nx, ny, mapId, head);
                                if (petNpc instanceof L1SummonInstance) {
                                    pc.sendPackets(new S_NPCPack_Summon((L1SummonInstance) petNpc, pc));
                                } else if (petNpc instanceof L1PetInstance) {
                                    pc.sendPackets(new S_NPCPack_Pet((L1PetInstance) petNpc, pc));
                                }
                                Iterator<L1PcInstance> it = World.get().getVisiblePlayer(petNpc).iterator();
                                while (it.hasNext()) {
                                    L1PcInstance visiblePc = it.next();
                                    visiblePc.removeKnownObject(petNpc);
                                    if (visiblePc.get_showId() == petNpc.get_showId()) {
                                        subjects.add(visiblePc);
                                    }
                                }
                            }
                        }
                        if (!pc.getDolls().isEmpty()) {
                            L1Location loc2 = pc.getLocation().randomLocation(3, false);
                            int nx2 = loc2.getX();
                            int ny2 = loc2.getY();
                            Object[] dolls = pc.getDolls().values().toArray();
                            int length = dolls.length;
                            for (int i = 0; i < length; i++) {
                                L1DollInstance doll = (L1DollInstance) dolls[i];
                                teleport(doll, nx2, ny2, mapId, head);
                                pc.sendPackets(new S_NPCPack_Doll(doll, pc));
                                doll.set_showId(pc.get_showId());
                                Iterator<L1PcInstance> it2 = World.get().getVisiblePlayer(doll).iterator();
                                while (it2.hasNext()) {
                                    L1PcInstance visiblePc2 = it2.next();
                                    visiblePc2.removeKnownObject(doll);
                                    if (visiblePc2.get_showId() == doll.get_showId()) {
                                        subjects.add(visiblePc2);
                                    }
                                }
                            }
                        }
                    }
                    Iterator<L1PcInstance> it3 = subjects.iterator();
                    while (it3.hasNext()) {
                        it3.next().updateObject();
                    }
                    Integer time = ServerUseMapTimer.MAP.get(pc);
                    if (time != null) {
                        ServerUseMapTimer.put(pc, time.intValue());
                    }
                    pc.setTeleport(false);
                    if (pc.hasSkillEffect(167)) {
                        pc.sendPackets(new S_PacketBoxWindShackle(pc.getId(), pc.getSkillEffectTimeSec(167)));
                    }
                    if (pc.getMapId() <= 10000 && pc.getInnKeyId() != 0) {
                        pc.setInnKeyId(0);
                    }
                    pc.getMap().setPassable(pc.getLocation(), false);
                    pc.getPetModel();
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
        try {
            World.get().moveVisibleObject(npc, map);
            L1WorldMap.get().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true, 2);
            npc.setX(x);
            npc.setY(y);
            npc.setMap(map);
            npc.setHeading(head);
            L1WorldMap.get().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false, 2);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
