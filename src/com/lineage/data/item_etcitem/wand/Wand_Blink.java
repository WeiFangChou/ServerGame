package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import java.util.Iterator;

public class Wand_Blink extends ItemExecutor {
    private Wand_Blink() {
    }

    public static ItemExecutor get() {
        return new Wand_Blink();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        int targObjId = data[0];
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        if (pc.isInvisble()) {
            pc.delInvis();
        }
        pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), 17));
        if (item.getChargeCount() <= 0) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        L1Object target = World.get().findObject(targObjId);
        if (target != null) {
            int rnd = (int) (Math.random() * 100.0d);
            if (target == null || rnd < 50) {
                pc.sendPackets(new S_ServerMessage(280));
            } else {
                wandAction(pc, target);
            }
            item.setChargeCount(item.getChargeCount() - 1);
            pc.getInventory().updateItem(item, 128);
            return;
        }
        pc.sendPackets(new S_ServerMessage(79));
    }

    private void wandAction(L1PcInstance pc, L1Object ta) {
        int newX;
        int newY;
        int newX2;
        int newY2;
        short xy = (short) ((int) ((Math.random() * 3.0d) + 1.0d));
        if (ta instanceof L1PcInstance) {
            L1PcInstance targetPc = (L1PcInstance) ta;
            int target_x = pc.getX() - targetPc.getX();
            int target_y = pc.getY() - targetPc.getY();
            if (pc.getId() == targetPc.getId()) {
                pc.sendPackets(new S_ServerMessage(280));
            } else if (target_x > 4 || target_x < -4 || target_y > 4 || target_y < -4 || targetPc.isGm()) {
                pc.sendPackets(new S_ServerMessage(280));
            } else {
                switch (target_x) {
                    case OpcodesClient.C_OPCODE_HORUN /*{ENCODED_INT: -4}*/:
                    case -3:
                    case OpcodesClient.C_OPCODE_HIRESOLDIER /*{ENCODED_INT: -2}*/:
                    case -1:
                        newX2 = targetPc.getX() + xy;
                        break;
                    case 0:
                    default:
                        newX2 = targetPc.getX();
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newX2 = targetPc.getX() - xy;
                        break;
                }
                switch (target_y) {
                    case OpcodesClient.C_OPCODE_HORUN /*{ENCODED_INT: -4}*/:
                    case -3:
                    case OpcodesClient.C_OPCODE_HIRESOLDIER /*{ENCODED_INT: -2}*/:
                    case -1:
                        newY2 = targetPc.getY() + xy;
                        break;
                    case 0:
                    default:
                        newY2 = targetPc.getY();
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        newY2 = targetPc.getY() - xy;
                        break;
                }
                L1Map map = L1WorldMap.get().getMap(targetPc.getMapId());
                short mapId = targetPc.getMapId();
                int head = targetPc.getHeading();
                if (L1TownLocation.isGambling(newX2, newY2, mapId)) {
                    pc.sendPackets(new S_ServerMessage(280));
                } else if (!map.isInMap(newX2, newY2) || !map.isPassable(newX2, newY2, (L1Character) null)) {
                    pc.sendPackets(new S_ServerMessage(280));
                } else {
                    L1Teleport.teleport(targetPc, newX2, newY2, mapId, head, true);
                }
            }
        } else if (ta instanceof L1MonsterInstance) {
            L1MonsterInstance targetNpc = (L1MonsterInstance) ta;
            int target_x2 = pc.getX() - targetNpc.getX();
            int target_y2 = pc.getY() - targetNpc.getY();
            if (target_x2 > 4 || target_x2 < -4 || target_y2 > 4 || target_y2 < -4 || targetNpc.getLevel() >= 40) {
                pc.sendPackets(new S_ServerMessage(280));
                return;
            }
            switch (target_x2) {
                case OpcodesClient.C_OPCODE_HORUN /*{ENCODED_INT: -4}*/:
                case -3:
                case OpcodesClient.C_OPCODE_HIRESOLDIER /*{ENCODED_INT: -2}*/:
                case -1:
                    newX = targetNpc.getX() + xy;
                    break;
                case 0:
                default:
                    newX = targetNpc.getX();
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    newX = targetNpc.getX() - xy;
                    break;
            }
            switch (target_y2) {
                case OpcodesClient.C_OPCODE_HORUN /*{ENCODED_INT: -4}*/:
                case -3:
                case OpcodesClient.C_OPCODE_HIRESOLDIER /*{ENCODED_INT: -2}*/:
                case -1:
                    newY = targetNpc.getY() + xy;
                    break;
                case 0:
                default:
                    newY = targetNpc.getY();
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    newY = targetNpc.getY() - xy;
                    break;
            }
            L1Map map2 = L1WorldMap.get().getMap(targetNpc.getMapId());
            short mapId2 = targetNpc.getMapId();
            int head2 = targetNpc.getHeading();
            if (!map2.isInMap(newX, newY) || !map2.isPassable(newX, newY, targetNpc)) {
                pc.sendPackets(new S_ServerMessage(280));
            } else {
                teleport(targetNpc, newX, newY, mapId2, head2);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(280));
        }
    }

    private void teleport(L1MonsterInstance targetNpc, int x, int y, short map, int head) {
        World.get().moveVisibleObject(targetNpc, map);
        targetNpc.getMap().setPassable(targetNpc.getX(), targetNpc.getY(), true, 2);
        targetNpc.setX(x);
        targetNpc.setY(y);
        targetNpc.setMap(map);
        targetNpc.setHeading(head);
        targetNpc.getMap().setPassable(x, y, false, 2);
        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(targetNpc, 15).iterator();
        while (it.hasNext()) {
            L1PcInstance tgPc = it.next();
            if (tgPc != null) {
                tgPc.sendPackets(new S_SkillSound(targetNpc.getId(), L1SkillId.EXOTIC_VITALIZE));
                tgPc.sendPackets(new S_RemoveObject(targetNpc));
                tgPc.removeKnownObject(targetNpc);
                tgPc.updateObject();
            }
        }
    }
}
