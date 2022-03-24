package com.lineage.server.model.monitor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.world.World;

public abstract class L1PcMonitor implements Runnable {
    protected int _id;

    public abstract void execTask(L1PcInstance l1PcInstance);

    public L1PcMonitor(int oId) {
        this._id = oId;
    }

    public final void run() {
        L1PcInstance pc = (L1PcInstance) World.get().findObject(this._id);
        if (pc != null && pc.getNetConnection() != null) {
            execTask(pc);
        }
    }
}
