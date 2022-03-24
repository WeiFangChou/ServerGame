package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_GreenMessage;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.utils.Teleportation;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Teleport {
    public static final int ADVANCED_MASS_TELEPORT = 2;
    public static final int CALL_CLAN = 3;
    public static final int CHANGE_POSITION = 1;
    public static final int[] EFFECT_SPR = {L1SkillId.EXOTIC_VITALIZE, 2235, 2236, 2281};
    public static final int[] EFFECT_TIME = {280, 440, 440, 1120};
    public static final int TELEPORT = 0;
    private static final Log _log = LogFactory.getLog(L1Teleport.class);

    private L1Teleport() {
    }

    public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable) {
        teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head, effectable, 0);
    }

    public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable, int skillType) {
        teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head, effectable, skillType);
    }

    public static void teleport(L1PcInstance pc, int x, int y, short mapid, int head, boolean effectable) {
        teleport(pc, x, y, mapid, head, effectable, 0);
    }

    public static void teleport(L1PcInstance pc, int x, int y, short mapId, int head, boolean effectable, int skillType) {
        if (pc.getTradeID() != 0) {
            new L1Trade().tradeCancel(pc);
        }
        pc.setPetModel();
        if (effectable && skillType >= 0 && skillType <= EFFECT_SPR.length) {
            pc.sendPackets(new S_ChangeName(pc, false));
            pc.sendPacketsX8(new S_SkillSound(pc.getId(), EFFECT_SPR[skillType]));
            try {
                Thread.sleep((long) ((int) (((double) EFFECT_TIME[skillType]) * 0.7d)));
            } catch (Exception ignored) {
            }
        }
        pc.setTeleportX(x);
        pc.setTeleportY(y);
        pc.setTeleportMapId(mapId);
        pc.setTeleportHeading(head);
        Teleportation.teleportation(pc);
    }

    public static void teleportToTargetFront(L1Character cha, L1Character target, int distance) {
        int locX = target.getX();
        int locY = target.getY();
        int heading = target.getHeading();
        L1Map map = target.getMap();
        short mapId = target.getMapId();
        switch (heading) {
            case 0:
                locY -= distance;
                break;
            case 1:
                locX += distance;
                locY -= distance;
                break;
            case 2:
                locX += distance;
                break;
            case 3:
                locX += distance;
                locY += distance;
                break;
            case 4:
                locY += distance;
                break;
            case 5:
                locX -= distance;
                locY += distance;
                break;
            case 6:
                locX -= distance;
                break;
            case 7:
                locX -= distance;
                locY -= distance;
                break;
        }
        if (!map.isPassable(locX, locY, (L1Character) null)) {
            return;
        }
        if (cha instanceof L1PcInstance) {
            teleport((L1PcInstance) cha, locX, locY, mapId, cha.getHeading(), true);
        } else if (cha instanceof L1NpcInstance) {
            ((L1NpcInstance) cha).teleport(locX, locY, cha.getHeading());
        }
    }

    public static void teleportToTargetFront_clan(L1Character cha, L1Character target, String item_id, String mapName, int distance, boolean chkitem) {
        int locX = target.getX();
        int locY = target.getY();
        int heading = target.getHeading();
        L1Map map = target.getMap();
        short mapId = target.getMapId();
        if (chkitem) {
            switch (heading) {
                case 0:
                    locY -= distance;
                    break;
                case 1:
                    locX += distance;
                    locY -= distance;
                    break;
                case 2:
                    locX += distance;
                    break;
                case 3:
                    locX += distance;
                    locY += distance;
                    break;
                case 4:
                    locY += distance;
                    break;
                case 5:
                    locX -= distance;
                    locY += distance;
                    break;
                case 6:
                    locX -= distance;
                    break;
                case 7:
                    locX -= distance;
                    locY -= distance;
                    break;
            }
            if (!map.isPassable(locX, locY, (L1Character) null)) {
                return;
            }
            if (cha instanceof L1PcInstance) {
                L1PcInstance objpc = (L1PcInstance) cha;
                String clanName = objpc.getClanname();
                objpc.setTeleportX(locX);
                objpc.setTeleportY(locY);
                objpc.setTeleportMapId(mapId);
                World.get().broadcastPacketToAll_sing(new S_GreenMessage("【" + clanName + "】\\f=玩家：【\\f3" + target.getName() + "\\f=】\\f=地點：【\\f2" + mapName + "】\\f=使用：【\\f2" + item_id + "\\f=】\\f>呼叫血盟成員前往支援。"), objpc);
                objpc.sendPackets(new S_Message_YN(1415, target.getName()));
            } else if (cha instanceof L1NpcInstance) {
                ((L1NpcInstance) cha).teleport(locX, locY, cha.getHeading());
            }
        }
    }

    public static void teleportToTargetFront_Party(L1Character cha, L1Character target, String item_id, String mapName, int distance, boolean chkitem) {
        int locX = target.getX();
        int locY = target.getY();
        int heading = target.getHeading();
        L1Map map = target.getMap();
        short mapId = target.getMapId();
        if (chkitem) {
            switch (heading) {
                case 0:
                    locY -= distance;
                    break;
                case 1:
                    locX += distance;
                    locY -= distance;
                    break;
                case 2:
                    locX += distance;
                    break;
                case 3:
                    locX += distance;
                    locY += distance;
                    break;
                case 4:
                    locY += distance;
                    break;
                case 5:
                    locX -= distance;
                    locY += distance;
                    break;
                case 6:
                    locX -= distance;
                    break;
                case 7:
                    locX -= distance;
                    locY -= distance;
                    break;
            }
            if (!map.isPassable(locX, locY, (L1Character) null)) {
                return;
            }
            if (cha instanceof L1PcInstance) {
                L1PcInstance objpc = (L1PcInstance) cha;
                objpc.setTeleportX(locX);
                objpc.setTeleportY(locY);
                objpc.setTeleportMapId(mapId);
                World.get().broadcastPacketToAll_sing(new S_GreenMessage("\\f=玩家：【\\f3" + target.getName() + "\\f=】\\f=地點：【\\f2" + mapName + "】\\f=使用：【\\f2" + item_id + "\\f=】\\f>呼叫隊伍成員前往支援。"), objpc);
                objpc.sendPackets(new S_Message_YN(1416, target.getName()));
            } else if (cha instanceof L1NpcInstance) {
                ((L1NpcInstance) cha).teleport(locX, locY, cha.getHeading());
            }
        }
    }

    public static void randomTeleport(L1PcInstance pc, boolean effectable) {
        try {
            L1Location newLocation = pc.getLocation().randomLocation(200, true);
            teleport(pc, newLocation.getX(), newLocation.getY(), (short) newLocation.getMapId(), 5, effectable);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
