package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import com.lineage.server.timecontroller.event.ranking.RankingClanTimer;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;
import com.lineage.server.timecontroller.event.ranking.RankingKillTimer;
import com.lineage.server.timecontroller.event.ranking.RankingWealthTimer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Ranking4 extends EventExecutor {
    private static final Log _log = LogFactory.getLog(Ranking4.class);

    private Ranking4() {
    }

    public static EventExecutor get() {
        return new Ranking4();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            new RankingKillTimer().start();
            Thread.sleep(500);
            new RankingWealthTimer().start();
            Thread.sleep(500);
            new RankingHeroTimer().start();
            Thread.sleep(500);
            new RankingClanTimer().start();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
