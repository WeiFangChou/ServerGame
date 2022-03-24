package com.lineage.server.timecontroller.event;

import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldClan;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClanSkillTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ClanSkillTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 3600000L, 3600000L);
    }

    public void run() {
        try {
            Collection<L1Clan> allClan = WorldClan.get().getAllClans();
            if (!allClan.isEmpty()) {
                for (L1Clan clan : allClan) {
                    if (clan.isClanskill()) {
                        Timestamp skilltime = clan.get_skilltime();
                        if (skilltime == null) {
                            clan.set_clanskill(false);
                        } else {
                            if (skilltime.before(new Timestamp(System.currentTimeMillis()))) {
                                clan.set_clanskill(false);
                                clan.set_skilltime(null);
                                ClanReading.get().updateClan(clan);
                            }
                            Thread.sleep(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error("血盟技能計時時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ClanSkillTimer().start();
        }
    }
}
