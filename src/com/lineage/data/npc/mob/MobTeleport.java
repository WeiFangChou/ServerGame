package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.CheckUtil;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MobTeleport extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(MobTeleport.class);
    private int _locx = 0;
    private int _locy = 0;
    private int _mapid = 0;
    private boolean _party = false;

    private MobTeleport() {
    }

    public static NpcExecutor get() {
        return new MobTeleport();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || this._locx == 0 || this._locy == 0) {
                return pc;
            }
            new M_teleport(this, pc, null).stsrt_cmd();
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void set_set(String[] set) {
        try {
            this._locx = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
        try {
            this._locy = Integer.parseInt(set[2]);
        } catch (Exception ignored) {
        }
        try {
            this._mapid = Integer.parseInt(set[3]);
        } catch (Exception ignored) {
        }
        try {
            this._party = Boolean.parseBoolean(set[4]);
        } catch (Exception ignored) {
        }
    }

    private class M_teleport implements Runnable {
        private final L1PcInstance _pc;

        private M_teleport(L1PcInstance pc) {
            this._pc = pc;
        }

        /* synthetic */ M_teleport(MobTeleport mobTeleport, L1PcInstance l1PcInstance, M_teleport m_teleport) {
            this(l1PcInstance);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void stsrt_cmd() throws IOException {
            GeneralThreadPool.get().execute(this);
        }

        public void run() {
            L1Party party;
            try {
                this._pc.sendPackets(new S_PacketBoxGree(1));
                this._pc.sendPackets(new S_PacketBoxGree("$ 5秒後將會被傳送!"));
                Thread.sleep(1000);
                this._pc.sendPackets(new S_PacketBoxGree("$ 4秒後將會被傳送!"));
                Thread.sleep(1000);
                this._pc.sendPackets(new S_PacketBoxGree("$ 3秒後將會被傳送!"));
                Thread.sleep(1000);
                this._pc.sendPackets(new S_PacketBoxGree("$ 2秒後將會被傳送!"));
                Thread.sleep(1000);
                this._pc.sendPackets(new S_PacketBoxGree("$ 1秒後將會被傳送!"));
                Thread.sleep(1000);
                this._pc.sendPackets(new S_PacketBoxGree("$ "));
                L1Teleport.teleport(this._pc, MobTeleport.this._locx, MobTeleport.this._locy,  MobTeleport.this._mapid, 5, true);
                if (MobTeleport.this._party && (party = this._pc.getParty()) != null) {
                    for (L1PcInstance otherPc : party.partyUsers().values()) {
                        if (otherPc.getId() != party.getLeaderID() && this._pc.getMapId() == otherPc.getMapId()) {
                            L1Teleport.teleport(otherPc, MobTeleport.this._locx, MobTeleport.this._locy,  MobTeleport.this._mapid, 5, true);
                        }
                    }
                }
            } catch (Exception e) {
                MobTeleport._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
