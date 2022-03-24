package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Color implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Color.class);

    private L1Color() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Color();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            GeneralThreadPool.get().execute(new ColorTimeController(pc));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }

    private class ColorTimeController implements Runnable {
        int _mode = 0;
        private L1PcInstance _pc;

        public ColorTimeController(L1PcInstance pc) {
            this._pc = pc;
        }

        public void run() {
            while (this._pc.isGm() && this._pc.getOnlineStatus() == 1 && this._pc.getNetConnection() != null) {
                try {
                    if (!this._pc.isGm()) {
                        this._pc.sendPacketsAll(new S_ChangeName(this._pc.getId(), this._pc.getName()));
                        return;
                    }
                    this._mode++;
                    if (this._mode > 10) {
                        this._mode = 0;
                    }
                    this._pc.sendPacketsAll(new S_ChangeName(this._pc.getId(), this._pc.getName(), this._mode));
                    this._pc.sendPacketsX8(new S_SkillSound(this._pc.getId(), 5288));
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    L1Color._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
