package com.lineage.data.npc.mob;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.utils.L1SpawnUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MobSpawn extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(MobSpawn.class);
    private int _npcid = 0;

    private MobSpawn() {
    }

    public static NpcExecutor get() {
        return new MobSpawn();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 8;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public L1PcInstance death(L1Character lastAttacker, L1NpcInstance npc) {
        try {
            L1PcInstance pc = CheckUtil.checkAtkPc(lastAttacker);
            if (pc == null || this._npcid == 0) {
                return pc;
            }
            if (NpcTable.get().getTemplate(this._npcid) == null) {
                _log.error("召喚NPC編號: " + this._npcid + " 不存在資料庫中!(mob.MobSpawn)");
                return pc;
            }
            L1NpcInstance newnpc = L1SpawnUtil.spawnT(this._npcid, npc.getX(), npc.getY(), npc.getMapId(), npc.getHeading(), 300);
            newnpc.onNpcAI();
            newnpc.startChat(0);
            return pc;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void set_set(String[] set) {
        try {
            this._npcid = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
