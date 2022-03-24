package com.eric;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.Timer;
import java.util.TimerTask;

public class RandomMobDeleteTimer extends TimerTask {
    private L1NpcInstance[] _npc;
    private int _randomMobId;

    public RandomMobDeleteTimer(int randomMobId, L1NpcInstance[] npc) {
        this._randomMobId = randomMobId;
        this._npc = npc;
    }

    public void run() {
        RandomMobTable.spawn(this._randomMobId);
        for (L1NpcInstance npc : this._npc) {
            npc.deleteMe();
        }
        cancel();
    }

    public void begin() {
        Timer timer = new Timer();
        if (RandomMobTable.getInstance().getTimeSecondToDelete(this._randomMobId) > 0) {
            timer.schedule(this, (long) (RandomMobTable.getInstance().getTimeSecondToDelete(this._randomMobId) * L1SkillId.STATUS_BRAVE));
        }
    }
}
