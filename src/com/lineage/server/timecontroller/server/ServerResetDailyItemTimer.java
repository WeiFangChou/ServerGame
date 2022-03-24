package com.lineage.server.timecontroller.server;

import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.ShopCnTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.sql.CharacterQuestTable;
import com.lineage.server.model.L1CountQuest;
import com.lineage.server.model.L1Map_Quest;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1Config;
import william.L1SystemMessageTable;

public class ServerResetDailyItemTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerResetDailyItemTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, (long) 600000, (long) 600000);
    }

    public void run() {
        try {
            if (!L1Config._4008) {
                ResetDailyItem();
            }
        } catch (Exception e) {
            _log.error("重置道具購買數量上限時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerResetDailyItemTimer().start();
        }
    }

    public static void ResetDailyItem() {
        int[] s;
        try {
            Calendar now_cal = Calendar.getInstance();
            Calendar reset_cal = L1Config._4009;
            long nowtime = now_cal.getTimeInMillis();
            long resettime = reset_cal.getTimeInMillis();
            if (nowtime - resettime >= 86400000) {
                for (Integer num : ShopTable._DailyItem.keySet()) {
                    L1CountQuest.deleteData(num.intValue());
                }
                for (Integer num2 : ShopCnTable._DailyCnItem.keySet()) {
                    L1CountQuest.deleteData(num2.intValue());
                }
                for (int i : ConfigOther.SHOCK) {
                    L1Map_Quest.deleteData(i);
                }
                _log.info("每日系統重置已完成。");
                while (nowtime - resettime >= 86400000) {
                    resettime += 86400000;
                }
                CharacterQuestTable.deleteData(900001);
                Calendar next_reset_cal = Calendar.getInstance();
                next_reset_cal.setTimeInMillis(resettime);
                L1Config._4009 = next_reset_cal;
                L1SystemMessageTable.get().updateResetTime(L1SkillId.ADLV80_1, next_reset_cal);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
