package com.lineage.server.clientpackets;

import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Door extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Door.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    readH();
                    readH();
                    int objectId = readD();
                    L1Object obj = World.get().findObject(objectId);
                    if (!(obj instanceof L1DoorInstance)) {
                        if (obj instanceof L1NpcInstance) {
                            L1NpcInstance npc = (L1NpcInstance) obj;
                            switch (npc.getNpcId()) {
                                case 70918:
                                    openDeLv50(pc, npc);
                                    break;
                            }
                        }
                    } else {
                        L1DoorInstance door = (L1DoorInstance) World.get().findObject(objectId);
                        if (door == null) {
                            over();
                            return;
                        } else if (door.getDoorId() < 5001 || door.getDoorId() > 5010) {
                            switch (door.getDoorId()) {
                                case 6006:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40163, 1)) {
                                            door.open();
                                            new CloseTimer(door).begin();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 6007:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40313, 1)) {
                                            door.open();
                                            new CloseTimer(door).begin();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 10000:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40581, 1)) {
                                            door.open();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 10001:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40594, 1)) {
                                            door.open();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 10002:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40604, 1)) {
                                            door.open();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 10003:
                                case 10005:
                                case 10006:
                                case 10007:
                                case 10008:
                                case 10009:
                                case 10010:
                                case 10011:
                                case 10012:
                                case 10013:
                                case 10019:
                                case 10036:
                                    break;
                                case 10004:
                                    if (door.getOpenStatus() != 28) {
                                        if (pc.getInventory().consumeItem(40543, 1)) {
                                            door.open();
                                            break;
                                        }
                                    } else {
                                        over();
                                        return;
                                    }
                                    break;
                                case 10015:
                                    if (pc.get_hardinR().DOOR_1) {
                                        if (door.getOpenStatus() != 28) {
                                            if (door.getOpenStatus() == 29) {
                                                door.open();
                                                break;
                                            }
                                        } else {
                                            door.close();
                                            break;
                                        }
                                    }
                                    break;
                                case 10016:
                                    if (pc.get_hardinR().DOOR_2) {
                                        if (door.getOpenStatus() != 28) {
                                            if (door.getOpenStatus() == 29) {
                                                door.open();
                                                break;
                                            }
                                        } else {
                                            door.close();
                                            break;
                                        }
                                    }
                                    break;
                                case 10017:
                                    if (pc.get_hardinR().DOOR_2) {
                                        if (door.getOpenStatus() != 28) {
                                            if (door.getOpenStatus() == 29) {
                                                door.open();
                                                break;
                                            }
                                        } else {
                                            door.close();
                                            break;
                                        }
                                    }
                                    break;
                                case 10020:
                                    if (pc.get_hardinR().DOOR_4) {
                                        if (door.getOpenStatus() != 28) {
                                            if (door.getOpenStatus() == 29) {
                                                door.open();
                                                pc.get_hardinR().DOOR_4OPEN = true;
                                                break;
                                            }
                                        } else {
                                            door.close();
                                            break;
                                        }
                                    }
                                    break;
                                default:
                                    if (!isExistKeeper(pc, door.getKeeperId())) {
                                        if (door.getOpenStatus() != 28) {
                                            if (door.getOpenStatus() == 29) {
                                                door.open();
                                                break;
                                            }
                                        } else {
                                            door.close();
                                            break;
                                        }
                                    }
                                    break;
                            }
                        } else {
                            over();
                            return;
                        }
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void openDeLv50(L1PcInstance pc, L1NpcInstance npc) throws Exception {
        L1ItemInstance item = pc.getInventory().checkItemX(40600, 1);
        if (item != null) {
            pc.getInventory().removeItem(item, 1);
            HashMap<Integer, L1Object> mapList = new HashMap<>();
            mapList.putAll(World.get().getVisibleObjects(DarkElfLv50_1.MAPID));
            npc.setStatus(28);
            npc.broadcastPacketAll(new S_DoActionGFX(npc.getId(), 28));
            for (L1Object tgobj : mapList.values()) {
                if (tgobj instanceof L1NpcInstance) {
                    L1NpcInstance tgnpc = (L1NpcInstance) tgobj;
                    if (tgnpc.getNpcId() == 70905 && tgnpc.get_showId() == npc.get_showId()) {
                        tgnpc.deleteMe();
                    }
                }
            }
            mapList.clear();
        }
    }

    private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
        int houseId;
        if (keeperId == 0 || pc.isGm()) {
            return false;
        }
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan == null || (houseId = clan.getHouseId()) == 0 || keeperId != HouseReading.get().getHouseTable(houseId).getKeeperId()) {
            return true;
        }
        return false;
    }

    public class CloseTimer extends TimerTask {
        private L1DoorInstance _door;

        public CloseTimer(L1DoorInstance door) {
            this._door = door;
        }

        public void run() {
            if (this._door.getOpenStatus() == 28) {
                this._door.close();
            }
        }

        public void begin() {
            new Timer().schedule(this, 5000);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
