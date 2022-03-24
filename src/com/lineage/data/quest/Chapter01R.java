package com.lineage.data.quest;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Party;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Chapter01R implements Runnable {
    private static final Log _log = LogFactory.getLog(Chapter01R.class);
    public boolean DOOR_1;
    public boolean DOOR_2;
    public boolean DOOR_4;
    public boolean DOOR_4OPEN;

    public Chapter01R(L1Party party, int qid, int i) {
    }

    public void startR() {
        GeneralThreadPool.get().execute(this);
    }

    public void run() {
    }

    public int get_time() {
        return 0;
    }

    public void boss_a_death() {
    }

    public void boss_b_death() {
    }

    public void down_count(L1NpcInstance npc) {
    }
}
