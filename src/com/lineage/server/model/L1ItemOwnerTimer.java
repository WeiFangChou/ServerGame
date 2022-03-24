package com.lineage.server.model;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Timer;
import java.util.TimerTask;

public class L1ItemOwnerTimer extends TimerTask {
    private final L1ItemInstance _item;
    private final int _timeMillis;

    public L1ItemOwnerTimer(L1ItemInstance item, int timeMillis) {
        this._item = item;
        this._timeMillis = timeMillis;
    }

    public void run() {
        this._item.setItemOwnerId(0);
        cancel();
    }

    public void begin() {
        new Timer().schedule(this, (long) this._timeMillis);
    }
}
