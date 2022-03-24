package com.lineage.data.event;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.lock.AccountBankReading;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.templates.L1Bank;
import com.lineage.server.templates.L1Event;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.RangeLong;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BankSet extends EventExecutor {
    public static long BANKMAX;
    public static double BANK_INTEREST = 0.01d;
    public static long BANK_INTEREST_OVER;
    public static int BANK_ITEMID;
    public static int BANK_TIME = 1;
    private static final Log _log = LogFactory.getLog(BankSet.class);

    private BankSet() {
    }

    public static EventExecutor get() {
        return new BankSet();
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(L1Event event) {
        try {
            String[] set = event.get_eventother().split(",");
            try {
                BANKMAX = Long.parseLong(set[0]);
            } catch (Exception e) {
                BANKMAX = 1900000000;
                _log.error("未設定銀行儲蓄上限(使用預設19億)");
            }
            try {
                BANK_INTEREST_OVER = Long.parseLong(set[1]);
            } catch (Exception e2) {
                BANK_INTEREST_OVER = 2000000000;
                _log.error("未設定利息及現存款總額(使用預設20億)");
            }
            try {
                BANK_ITEMID = Integer.parseInt(set[2]);
            } catch (Exception e3) {
                BANK_ITEMID = L1ItemId.ADENA;
                _log.error("未設定存款物品編號(使用預設40308)");
            }
            try {
                BANK_TIME = Integer.parseInt(set[3]);
            } catch (Exception e4) {
                BANK_TIME = 60;
                _log.error("未設銀行計算利息時間(使用預設60分鐘)");
            }
            try {
                BANK_INTEREST = Double.parseDouble(set[4]);
            } catch (Exception e5) {
                BANK_INTEREST = 0.01d;
                _log.error("未設銀行利息比率(使用預設0.01%)");
            }
            _log.info("銀行設置\n 帳戶儲蓄上限: " + ((Object) RangeLong.scount(BANKMAX)) + " \n 存款總額限制: " + ((Object) RangeLong.scount(BANK_INTEREST_OVER)) + "\n 存款物品編號: " + ((Object) RangeLong.scount(BANK_INTEREST_OVER)) + "\n 利息計算時間: " + BANK_TIME + "\n 利息比率: " + BANK_INTEREST);
            AccountBankReading.get().load();
            new BankTimer(this, null).start();
        } catch (Exception e6) {
            _log.error(e6.getLocalizedMessage(), e6);
        }
    }

    private class BankTimer extends TimerTask {
        private ScheduledFuture<?> _timer;

        private BankTimer() {
        }

        /* synthetic */ BankTimer(BankSet bankSet, BankTimer bankTimer) {
            this();
        }

        public void start() {
            int timeMillis = BankSet.BANK_TIME * 60 * L1SkillId.STATUS_BRAVE;
            this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, (long) timeMillis, (long) timeMillis);
        }

        public void run() {
            try {
                Map<String, L1Bank> map = AccountBankReading.get().map();
                if (!map.isEmpty()) {
                    for (String key : map.keySet()) {
                        L1Bank bank = map.get(key);
                        if (bank.isLan() && !bank.isEmpty()) {
                            long nwecount = bank.get_adena_count() + Math.round(((double) bank.get_adena_count()) * BankSet.BANK_INTEREST);
                            bank.set_adena_count(nwecount);
                            AccountBankReading.get().updateAdena(bank.get_account_name(), nwecount);
                            Thread.sleep(5);
                        }
                    }
                }
            } catch (Exception e) {
                BankSet._log.error("銀行管理員時間軸異常重啟", e);
                GeneralThreadPool.get().cancel(this._timer, false);
                new BankTimer().start();
            }
        }
    }
}
