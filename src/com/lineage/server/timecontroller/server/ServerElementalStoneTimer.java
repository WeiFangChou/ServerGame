package com.lineage.server.timecontroller.server;

import com.lineage.config.ConfigAlt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.L1GroundInventory;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1WorldMap;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerElementalStoneTimer extends TimerTask {
    private static final int ELEMENTAL_STONE_ID = 40515;
    private static final int ELVEN_FOREST_MAPID = 4;
    private static final int FIRST_X = 32911;
    private static final int FIRST_Y = 32210;
    private static final int INTERVAL = 3;
    private static final int LAST_X = 33141;
    private static final int LAST_Y = 32500;
    private static final int MAX_COUNT = ConfigAlt.ELEMENTAL_STONE_AMOUNT;
    private static final int SLEEP_TIME = 30;
    private static final Log _log = LogFactory.getLog(ServerElementalStoneTimer.class);
    private final L1Object _dummy = new L1Object();
    private ArrayList<L1GroundInventory> _itemList = new ArrayList<>(MAX_COUNT);
    private final L1Map _map = L1WorldMap.get().getMap(ELVEN_FOREST_MAPID);
    private Random _random = new Random();
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 30000L, 30000L);
    }

    private boolean canPut(L1Location loc) {
        this._dummy.setMap(loc.getMap());
        this._dummy.setX(loc.getX());
        this._dummy.setY(loc.getY());
        if (World.get().getVisiblePlayer(this._dummy).size() > 0) {
            return false;
        }
        return true;
    }

    private Point nextPoint() {
        return new Point(this._random.nextInt(230) + FIRST_X, this._random.nextInt(290) + FIRST_Y);
    }

    private void removeItemsPickedUp() {
        int i = 0;
        while (i < this._itemList.size()) {
            if (!this._itemList.get(i).checkItem(ELEMENTAL_STONE_ID)) {
                this._itemList.remove(i);
                i--;
            }
            i++;
        }
    }

    private void putElementalStone(L1Location loc) {
        L1GroundInventory gInventory = World.get().getInventory(loc);
        L1ItemInstance item = ItemTable.get().createItem(ELEMENTAL_STONE_ID);
        item.setEnchantLevel(0);
        item.setCount(1);
        gInventory.storeItem(item);
        this._itemList.add(gInventory);
    }

    public void run() {
        try {
            removeItemsPickedUp();
            while (this._itemList.size() < MAX_COUNT) {
                L1Location loc = new L1Location(nextPoint(), this._map);
                if (canPut(loc)) {
                    putElementalStone(loc);
                    Thread.sleep(3000);
                }
            }
        } catch (Throwable e) {
            _log.error("元素石生成時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerElementalStoneTimer().start();
        }
    }
}
