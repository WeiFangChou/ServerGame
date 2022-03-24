package com.lineage.server.command.executor;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.L1SpawnUtil;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SpawnCmd implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1SpawnCmd.class);

    private L1SpawnCmd() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1SpawnCmd();
    }

    private void sendErrorMessage(L1PcInstance pc, String cmdName) {
        pc.sendPackets(new S_SystemMessage(String.valueOf(cmdName) + " npcid|name [數量] [範圍]。"));
    }

    private int parseNpcId(String nameId) {
        try {
            return Integer.parseInt(nameId);
        } catch (NumberFormatException e) {
            return NpcTable.get().findNpcIdByNameWithoutSpace(nameId);
        }
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String nameId = tok.nextToken();
            int count = 1;
            if (tok.hasMoreTokens()) {
                count = Integer.parseInt(tok.nextToken());
            }
            int randomrange = 0;
            if (tok.hasMoreTokens()) {
                randomrange = Integer.parseInt(tok.nextToken(), 10);
            }
            int npcid = parseNpcId(nameId);
            L1Npc npc = NpcTable.get().getTemplate(npcid);
            if (npc == null) {
                pc.sendPackets(new S_SystemMessage("找不到該npc。"));
            } else if (count > 1000) {
                pc.sendPackets(new S_SystemMessage("一次召喚數量不能超過1000。"));
            } else {
                if (count > 100) {
                    GeneralThreadPool.get().execute(new SpawnRunnable(this, pc, npcid, randomrange, count, null));
                } else {
                    for (int i = 0; i < count; i++) {
                        L1SpawnUtil.spawn(pc, npcid, randomrange, 0);
                    }
                }
                pc.sendPackets(new S_SystemMessage(String.format("%s(%d) (%d) 召喚。 (範圍:%d)", npc.get_name(), Integer.valueOf(npcid), Integer.valueOf(count), Integer.valueOf(randomrange))));
            }
        } catch (NoSuchElementException e) {
            sendErrorMessage(pc, cmdName);
        } catch (NumberFormatException e2) {
            sendErrorMessage(pc, cmdName);
        } catch (Exception e3) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }

    private class SpawnRunnable implements Runnable {
        private int _count;
        private int _npcid;
        private L1PcInstance _pc;
        private int _randomrange;

        private SpawnRunnable(L1PcInstance pc, int npcid, int randomrange, int count) {
            this._pc = pc;
            this._npcid = npcid;
            this._randomrange = randomrange;
            this._count = count;
        }

        /* synthetic */ SpawnRunnable(L1SpawnCmd l1SpawnCmd, L1PcInstance l1PcInstance, int i, int i2, int i3, SpawnRunnable spawnRunnable) {
            this(l1PcInstance, i, i2, i3);
        }

        public void run() {
            for (int i = 0; i < this._count; i++) {
                try {
                    L1SpawnUtil.spawn(this._pc, this._npcid, this._randomrange, 0);
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    L1SpawnCmd._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
