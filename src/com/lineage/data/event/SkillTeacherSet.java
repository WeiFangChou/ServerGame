package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import java.util.HashMap;
import java.util.Map;

public class SkillTeacherSet extends EventExecutor {
    public static final Map<Integer, Integer> RESKILLLIST = new HashMap();

    private SkillTeacherSet() {
    }

    public static EventExecutor get() {
        return new SkillTeacherSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        String[] set = event.get_eventother().split(",");
        for (String string : set) {
            RESKILLLIST.put(Integer.valueOf(Integer.parseInt(string) - 1), Integer.valueOf(Integer.parseInt(string)));
        }
    }
}
