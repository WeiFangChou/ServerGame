package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;

public abstract class SkillMode {
    public abstract int start(L1NpcInstance l1NpcInstance, L1Character l1Character, L1Magic l1Magic, int i) throws Exception;

    public abstract int start(L1PcInstance l1PcInstance, L1Character l1Character, L1Magic l1Magic, int i) throws Exception;

    public abstract void start(L1PcInstance l1PcInstance, Object obj) throws Exception;

    public abstract void stop(L1Character l1Character) throws Exception;
}
