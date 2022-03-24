package com.lineage.server.model;

import com.lineage.config.ConfigAlt;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DeleteItemOnGround {
    private static final Log _log = LogFactory.getLog(L1DeleteItemOnGround.class);
    private DeleteTimer _deleteTimer;

    /* access modifiers changed from: private */
    public class DeleteTimer implements Runnable {
        public DeleteTimer() {
        }

        public void run() {
            int time = ((ConfigAlt.ALT_ITEM_DELETION_TIME * 60) * L1SkillId.STATUS_BRAVE) - 10000;
            while (true) {
                try {
                    Thread.sleep((long) time);
                    World.get().broadcastPacketToAll(new S_ServerMessage("地上的物品,10秒後將清除。"));
                    try {
                        Thread.sleep(10000);
                        L1DeleteItemOnGround.this.deleteItem();
                        World.get().broadcastPacketToAll(new S_ServerMessage("地上的物品,已被系統清除。"));
                    } catch (Exception exception) {
                        L1DeleteItemOnGround._log.info("L1DeleteItemOnGround error: " + exception);
                        return;
                    }
                } catch (Exception exception2) {
                    L1DeleteItemOnGround._log.info("L1DeleteItemOnGround error: " + exception2);
                    return;
                }
            }
        }
    }

    public void initialize() {
        if (ConfigAlt.ALT_ITEM_DELETION_TYPE.equalsIgnoreCase("auto")) {
            this._deleteTimer = new DeleteTimer();
            GeneralThreadPool.get().execute(this._deleteTimer);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void deleteItem() throws Exception {
        int numOfDeleted = 0;
        for (L1Object obj : World.get().getObject()) {
            if (obj instanceof L1ItemInstance) {
                L1ItemInstance item = (L1ItemInstance) obj;
                if (!(item.getX() == 0 && item.getY() == 0) && item.getItem().getItemId() != 40515 && !L1HouseLocation.isInHouse(item.getX(), item.getY(), item.getMapId()) && World.get().getVisiblePlayer(item, ConfigAlt.ALT_ITEM_DELETION_RANGE).isEmpty()) {
                    World.get().getInventory(item.getX(), item.getY(), item.getMapId()).removeItem(item);
                    numOfDeleted++;
                }
            }
        }
        _log.info("地上自動刪除。刪除數件: " + numOfDeleted);
    }
}
