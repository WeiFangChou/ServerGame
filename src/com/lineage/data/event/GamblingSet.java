package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.templates.L1Event;
import com.lineage.server.templates.L1Item;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GamblingSet extends EventExecutor {
    public static int ADENAITEM = L1ItemId.ADENA;
    public static int GAMADENA;
    public static String GAMADENANAME;
    public static int GAMADENATIME;
    private static final Log _log = LogFactory.getLog(GamblingSet.class);

    private GamblingSet() {
    }

    public static EventExecutor get() {
        return new GamblingSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            String[] set = event.get_eventother().split(",");
            try {
                GAMADENATIME = Integer.parseInt(set[0]);
            } catch (Exception e) {
                GAMADENATIME = 30;
                _log.error("未設定每場比賽間隔時間(分鐘)(使用預設30分鐘)");
            }
            try {
                GAMADENA = Integer.parseInt(set[1]);
            } catch (Exception e2) {
                GAMADENA = L1SkillId.SEXP11;
                _log.error("未設定奇岩賭場 下注額(每張賭票售價)(使用預設5000)");
            }
            try {
                ADENAITEM = Integer.parseInt(set[2]);
                L1Item item = ItemTable.get().getTemplate(ADENAITEM);
                if (item != null) {
                    GAMADENANAME = item.getName();
                }
            } catch (Exception e3) {
                ADENAITEM = L1ItemId.ADENA;
                GAMADENANAME = "$4";
                _log.error("未設定奇岩賭場 下注物品編號(使用預設40308)");
            }
            GamblingReading.get().load();
            new GamblingTime().start();
            spawnDoor();
        } catch (Exception e4) {
            _log.error(e4.getLocalizedMessage(), e4);
        }
    }

    private void spawnDoor() {
        int[] iArr = new int[10];
        iArr[0] = 51;
        iArr[1] = 1487;
        iArr[2] = 33521;
        iArr[3] = 32861;
        iArr[4] = 4;
        iArr[5] = 1;
        iArr[6] = 33523;
        iArr[7] = 33523;
        iArr[9] = -1;
        int[] iArr2 = new int[10];
        iArr2[0] = 52;
        iArr2[1] = 1487;
        iArr2[2] = 33519;
        iArr2[3] = 32863;
        iArr2[4] = 4;
        iArr2[5] = 1;
        iArr2[6] = 33523;
        iArr2[7] = 33523;
        iArr2[9] = -1;
        int[] iArr3 = new int[10];
        iArr3[0] = 53;
        iArr3[1] = 1487;
        iArr3[2] = 33517;
        iArr3[3] = 32865;
        iArr3[4] = 4;
        iArr3[5] = 1;
        iArr3[6] = 33523;
        iArr3[7] = 33523;
        iArr3[9] = -1;
        int[] iArr4 = new int[10];
        iArr4[0] = 54;
        iArr4[1] = 1487;
        iArr4[2] = 33515;
        iArr4[3] = 32867;
        iArr4[4] = 4;
        iArr4[5] = 1;
        iArr4[6] = 33523;
        iArr4[7] = 33523;
        iArr4[9] = -1;
        int[] iArr5 = new int[10];
        iArr5[0] = 55;
        iArr5[1] = 1487;
        iArr5[2] = 33513;
        iArr5[3] = 32869;
        iArr5[4] = 4;
        iArr5[5] = 1;
        iArr5[6] = 33523;
        iArr5[7] = 33523;
        iArr5[9] = -1;
        int[][] gamDoors = {iArr, iArr2, iArr3, iArr4, iArr5};
        for (int[] doorInfo : gamDoors) {
            L1DoorInstance door = (L1DoorInstance) NpcTable.get().newNpcInstance(81158);
            if (door != null) {
                door.setId(IdFactoryNpc.get().nextId());
                int id = doorInfo[0];
                int gfxid = doorInfo[1];
                int locx = doorInfo[2];
                int locy = doorInfo[3];
                int mapid = doorInfo[4];
                int direction = doorInfo[5];
                int left_edge_location = doorInfo[6];
                int right_edge_location = doorInfo[7];
                int hp = doorInfo[8];
                int keeper = doorInfo[9];
                door.setDoorId(id);
                door.setGfxId(gfxid);
                door.setX(locx);
                door.setY(locy);
                door.setMap( mapid);
                door.setHomeX(locx);
                door.setHomeY(locy);
                door.setDirection(direction);
                door.setLeftEdgeLocation(left_edge_location);
                door.setRightEdgeLocation(right_edge_location);
                door.setMaxHp(hp);
                door.setCurrentHp(hp);
                door.setKeeperId(keeper);
                World.get().storeObject(door);
                World.get().addVisibleObject(door);
            }
        }
    }
}
