package com.lineage.server.timecontroller.pc;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.list.OnlineUser;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.thread.PcOtherThreadPool;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PcAutoSaveInventoryTimer */
public class PcAutoSaveInventoryTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PcAutoSaveInventoryTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 60000, 60000);
    }

    public void run() {
        try {
            Collection<ClientExecutor> allClien = OnlineUser.get().all();
            if (!allClien.isEmpty()) {
                for (ClientExecutor client : allClien) {
                    int time = client.get_saveInventory();
                    if (time != -1) {
                        save(client, Integer.valueOf(time - 1));
                    }
                }
            }
        } catch (Exception e) {
            _log.error("背包物品自動保存時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcAutoSaveInventoryTimer().start();
        }
    }

    private void save(ClientExecutor client, Integer time) {
        try {
            if (client.get_socket() != null) {
                if (time.intValue() > 0) {
                    client.set_saveInventory(time.intValue());
                    return;
                }
                client.set_saveInventory(Config.AUTOSAVE_INTERVAL_INVENTORY);
                L1PcInstance pc = client.getActiveChar();
                if (pc != null) {
                    pc.saveInventory();
                }
            }
        } catch (Exception e) {
            _log.debug("執行背包資料存檔處理異常 帳號:" + client.getAccountName());
        }
    }
}
