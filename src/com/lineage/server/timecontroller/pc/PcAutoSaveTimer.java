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

/* renamed from: com.lineage.server.timecontroller.pc.PcAutoSaveTimer */
public class PcAutoSaveTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(PcAutoSaveTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 60000, 60000);
    }

    public void run() {
        try {
            Collection<ClientExecutor> allClien = OnlineUser.get().all();
            if (!allClien.isEmpty()) {
                for (ClientExecutor client : allClien) {
                    int time = client.get_savePc();
                    if (time != -1) {
                        save(client, Integer.valueOf(time - 1));
                    }
                }
            }
        } catch (Exception e) {
            _log.error("人物資料自動保存時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcAutoSaveTimer().start();
        }
    }

    private void save(ClientExecutor client, Integer time) {
        try {
            if (client.get_socket() != null) {
                if (time.intValue() > 0) {
                    client.set_savePc(time.intValue());
                    return;
                }
                client.set_savePc(Config.AUTOSAVE_INTERVAL);
                L1PcInstance pc = client.getActiveChar();
                if (pc != null) {
                    pc.save();
                }
            }
        } catch (Exception e) {
            _log.debug("執行人物資料存檔處理異常 帳號:" + client.getAccountName());
        }
    }
}
