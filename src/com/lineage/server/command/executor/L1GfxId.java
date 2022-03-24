package com.lineage.server.command.executor;

import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GfxId implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1GfxId.class);

    private L1GfxId() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GfxId();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int count;
        try {
            StringTokenizer st = new StringTokenizer(arg);
            int gfxid = Integer.parseInt(st.nextToken(), 10);
            try {
                count = Integer.parseInt(st.nextToken(), 10);
            } catch (Exception e) {
                count = 1;
            }
            if (count == 1) {
                spawn(pc, NpcTable.get().newNpcInstance(50000), gfxid, 0, count);
                return;
            }
            for (int i = 0; i < count; i++) {
                spawn(pc, NpcTable.get().newNpcInstance(50000), gfxid, i, count);
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }

    private void spawn(L1PcInstance pc, L1NpcInstance npc, int gfxid, int i, int count) {
        if (npc != null) {
            int tempgfxid = gfxid + i;
            npc.setId(IdFactoryNpc.get().nextId());
            npc.setGfxId(tempgfxid);
            npc.setTempCharGfx(tempgfxid);
            npc.setNameId("GFXID:" + npc.getGfxId());
            npc.setMap(pc.getMapId());
            npc.setX(pc.getX() + (i * 2));
            npc.setY(pc.getY() + (i * 2));
            npc.setHomeX(npc.getX());
            npc.setHomeY(npc.getY());
            npc.setHeading(4);
            npc.setGfxidInStatus(tempgfxid);
            npc.set_spawnTime(300);
            World.get().storeObject(npc);
            World.get().addVisibleObject(npc);
            startDelMe(npc, count);
        }
    }

    private void startDelMe(L1NpcInstance npc, int count) {
        GeneralThreadPool.get().execute(new DelMe(this, npc, count, null));
    }

    /* access modifiers changed from: private */
    public class DelMe implements Runnable {
        private int _isTest;
        private L1NpcInstance _npc;

        private DelMe(L1NpcInstance npc, int mode) {
            this._isTest = 0;
            this._npc = npc;
            this._isTest = mode;
        }

        /* synthetic */ DelMe(L1GfxId l1GfxId, L1NpcInstance l1NpcInstance, int i, DelMe delMe) {
            this(l1NpcInstance, i);
        }

        public void run() {
            int i = 0;
            while (!this._npc.isDead()) {
                try {
                    Thread.sleep(2000);
                    i++;
                    if (this._isTest != 1) {
                        this._npc.broadcastPacketX10(new S_NpcChat(this._npc, " "));
                    } else if (i != 8 && i <= 40) {
                        this._npc.broadcastPacketX10(new S_DoActionGFX(this._npc.getId(), i));
                        this._npc.broadcastPacketX10(new S_NpcChat(this._npc, "ACID: " + i));
                    } else if (i >= 41 && i <= 48) {
                        this._npc.setHeading(i - 41);
                        this._npc.broadcastPacketX10(new S_ChangeHeading(this._npc));
                        this._npc.broadcastPacketX10(new S_NpcChat(this._npc, "HEAD: " + i));
                    }
                } catch (InterruptedException e) {
                    L1GfxId._log.error(e.getLocalizedMessage(), e);
                    return;
                }
            }
        }
    }
}
