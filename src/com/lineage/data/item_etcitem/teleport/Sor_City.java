package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import java.util.Random;

public class Sor_City extends ItemExecutor {
    private final Random _random = new Random();

    public static ItemExecutor get() {
        return new Sor_City();
    }

    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int[][] locs = null;
        int itemId = item.getItemId();
        switch (itemId) {
            case 40082:
                locs = new int[][] { { 32782, 32756, 68 }, { 32785, 32764, 68 }, { 32771, 32761, 68 } };
                break;
            case 40101:
                locs = new int[][] { { 32673, 32856, 69 }, { 32684, 32853, 69 }, { 32679, 32871, 69 } };
                break;
            case 40085:
                locs = new int[][] { { 32580, 32931 }, { 32586, 32942 }, { 32571, 32943 } };
                break;
            case 40080:
                locs = new int[][] { { 32612, 32734, 4 }, { 32602, 32734, 4 }, { 32608, 32726, 4 } };
                break;
            case 40122:
                locs = new int[][] { { 33050, 32780, 4 }, { 33059, 32785, 4 }, { 33047, 32789, 4 } };
                break;
            case 40115:
                locs = new int[][] { { 32622, 33179, 4 }, { 32618, 33187, 4 }, { 32606, 33187, 4 } };
                break;
            case 40125:
                locs = new int[][] { { 32715, 32448, 4 }, { 32732, 32459, 4 }, { 32738, 32441, 4 } };
                break;
            case 40114:
                locs = new int[][] { { 33051, 32337, 4 }, { 33054, 32340, 4 }, { 33058, 32335, 4 } };
                break;
            case 40117:
                locs = new int[][] { { 33080, 33392, 4 }, { 33090, 33399, 4 }, { 33073, 33407, 4 } };
                break;
            case 40081:
                locs = new int[][] { { 33442, 32797, 4 }, { 33446, 32807, 4 }, { 33430, 32809, 4 } };
                break;
            case 40123:
                locs = new int[][] { { 33605, 33259, 4 }, { 33616, 33242, 4 }, { 33602, 33236, 4 } };
                break;
            case 40103:
                locs = new int[][] { { 34061, 32276, 4 }, { 34051, 32278, 4 }, { 34051, 32293, 4 } };
                break;
            case 40116:
                locs = new int[][] { { 33705, 32504, 4 }, { 33707, 32493, 4 }, { 33718, 32504, 4 } };
                break;
            case 40102:
                locs = new int[][] { { 33966, 33253, 4 }, { 33973, 33246, 4 }, { 33962, 33254, 4 } };
                break;
            case 40845:
                locs = new int[][] { { 32864, 32895, 304 } };
                break;
            case 40083:
                locs = new int[][] { { 32549, 32801, 400 } };
                break;
            case 40120:
                locs = new int[][] { { 32571, 32673, 400 } };
                break;
            case 40118:
                locs = new int[][] { { 32601, 32908, 400 } };
                break;
            case 40084:
                locs = new int[][] { { 32723, 32851, 320 } };
                break;
        }
        if (pc.getMap().isEscapable()) {
            if (locs != null) {
                int[] loc = locs[this._random.nextInt(locs.length)];
                pc.getInventory().removeItem(item, 1L);
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                L1Teleport.teleport(pc, loc[0], loc[1], loc[2], 5, true);
            } else {
                pc.getInventory().removeItem(item, 1L);
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                L1Teleport.teleport(pc, 33080, 33392, 4, 5, true);
            }
        } else {
            pc.sendPackets((ServerBasePacket)new S_ServerMessage(276));
            pc.sendPackets((ServerBasePacket)new S_Paralysis(7, false));
        }
    }
}
