package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTeleportTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SOR_UserSet extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(SOR_UserSet.class);

    private SOR_UserSet() {
    }

    public static ItemExecutor get() {
        return new SOR_UserSet();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int[] loc = ItemTeleportTable.get().getLoc(item.getItemId());
        if (loc != null) {
            int locX = loc[0];
            int locY = loc[1];
            short mapId = (short) loc[2];
            int No = loc[4];
            if (!pc.getMap().isEscapable()) {
                pc.sendPackets(new S_ServerMessage(276));
                pc.sendPackets(new S_Paralysis(7, false));
            } else if (No == 0) {
                pc.sendPackets(new S_ServerMessage(String.valueOf(item.getName()) + "未開放。"));
                return;
            } else {
                pc.getInventory().removeItem(item, 1);
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                GeneralThreadPool.get().schedule(new TeleportRunnable(pc, locX, locY, mapId), 0);
            }
            int time = loc[3];
            if (time > 0) {
                pc.get_other().set_usemap(mapId);
                ServerUseMapTimer.put(pc, time);
                pc.sendPackets(new S_ServerMessage("使用時間限制:" + time + "秒"));
            }
        }
    }

    private class TeleportRunnable implements Runnable {
        private int _locX = 0;
        private int _locY = 0;
        private int _mapid = 0;
        private final L1PcInstance _pc;

        public TeleportRunnable(L1PcInstance pc, int x, int y, int mapid) {
            this._pc = pc;
            this._locX = x;
            this._locY = y;
            this._mapid = mapid;
        }

        public void run() {
            int newX;
            int newY;
            try {
                L1Map map = L1WorldMap.get().getMap((short) this._mapid);
                int tryCount = 0;
                int i = this._locX;
                int i2 = this._locY;
                while (true) {
                    tryCount++;
                    newX = (this._locX + ((int) (Math.random() * ((double) 10)))) - ((int) (Math.random() * ((double) 10)));
                    newY = (this._locY + ((int) (Math.random() * ((double) 10)))) - ((int) (Math.random() * ((double) 10)));
                    if (!map.isPassable(newX, newY, this._pc)) {
                        Thread.sleep(1);
                        if (tryCount >= 5) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (tryCount >= 5) {
                    L1Teleport.teleport(this._pc, this._locX, this._locY, (short) this._mapid, this._pc.getHeading(), true);
                } else {
                    L1Teleport.teleport(this._pc, newX, newY, (short) this._mapid, this._pc.getHeading(), true);
                }
            } catch (InterruptedException e) {
                SOR_UserSet._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
