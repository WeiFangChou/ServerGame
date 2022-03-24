package com.lineage.server.command.executor;

import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1KillNpc implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1KillNpc.class);

    private L1KillNpc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1KillNpc();
    }

    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            if (pc.is_isKill()) {
                pc.sendPackets(new S_ServerMessage(166, "Kill Npc : Off"));
                pc.set_isKill(false);
            } else {
                pc.sendPackets(new S_ServerMessage(166, "Kill Npc : On"));
                pc.set_isKill(true);
                L1KillNpc.Kill kill = new L1KillNpc.Kill(pc);
                GeneralThreadPool.get().execute(kill);
            }
        } catch (Exception var5) {
            _log.error("錯誤的GM指令格式: " + this.getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }

    }

    private class Kill implements Runnable {
        private final L1PcInstance _pc;

        private Kill(L1PcInstance pc) {
            this._pc = pc;
        }

        public void run() {
            label23:
            while(true) {
                try {
                    if (this._pc.is_isKill()) {
                        Thread.sleep(1000L);
                        Iterator var2 = this._pc.getKnownObjects().iterator();

                        while(true) {
                            if (!var2.hasNext()) {
                                continue label23;
                            }

                            L1Object obj = (L1Object)var2.next();
                            if (obj instanceof L1MonsterInstance) {
                                L1MonsterInstance mob = (L1MonsterInstance)obj;
                                int hp = mob.getMaxHp() + 1000;
                                mob.receiveDamage(this._pc, hp);
                            }
                        }
                    }
                } catch (InterruptedException var5) {
                    L1KillNpc._log.error(var5.getLocalizedMessage(), var5);
                }

                return;
            }
        }
    }
}
