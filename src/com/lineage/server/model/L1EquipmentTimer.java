package com.lineage.server.model;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.TimerTask;

public class L1EquipmentTimer extends TimerTask {
    private final L1ItemInstance _item;
    private final L1PcInstance _pc;

    public L1EquipmentTimer(L1PcInstance pc, L1ItemInstance item) {
        this._pc = pc;
        this._item = item;
    }

    public void run() {
        if (this._item.getRemainingTime() - 1 > 0) {
            this._item.setRemainingTime(this._item.getRemainingTime() - 1);
            this._pc.getInventory().updateItem(this._item, 256);
            if (this._pc.getOnlineStatus() == 0) {
                cancel();
                return;
            }
            return;
        }
        try {
            this._pc.getInventory().removeItem(this._item, 1);
            cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
