package com.lineage.server.model.skillUse;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;

public abstract class L1SkillMode {
    public abstract int start(L1NpcInstance l1NpcInstance, L1Character l1Character, int i, int i2, int i3, String str) throws Exception;

    public abstract int start(L1PcInstance l1PcInstance, L1Character l1Character, int i, int i2, int i3, String str) throws Exception;

    public abstract void stop(L1Character l1Character) throws Exception;

    public void useMode(L1Character user, int skillid) {
    }

    public void useLoc(L1Character user, L1Character targ, int skillid) {
    }
}
