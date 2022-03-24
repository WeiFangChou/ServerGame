package com.lineage.server.model.skill;

import com.lineage.server.datatables.MobSkillTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1MobSkill;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SkillDelayforMob {
    private static final Log _log = LogFactory.getLog(L1SkillDelayforMob.class);

    public static void onSkillUse(L1NpcInstance npc, int time, int idx, int skilltype) {
        try {
            L1MobSkill mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
            mobSkillTemplate.setSkillDelayIdx(idx, true);
            if (skilltype != 0) {
                mobSkillTemplate.setSkillDelayType(skilltype, true);
            }
            GeneralThreadPool.get().schedule(new SkillDelayTimer(npc, idx, skilltype), (long) time);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    /* access modifiers changed from: package-private */
    public static class SkillDelayTimer implements Runnable {
        private int _idx;
        private L1NpcInstance _npc;
        private int _skilltype;

        public SkillDelayTimer(L1NpcInstance npc, int idx, int skilltype) {
            this._npc = npc;
            this._idx = idx;
            this._skilltype = skilltype;
        }

        public void run() {
            stopDelayTimer(this._idx, this._skilltype);
        }

        public void stopDelayTimer(int idx, int skilltype) {
            L1MobSkill mobSkillTemplate = MobSkillTable.getInstance().getTemplate(this._npc.getNpcTemplate().get_npcId());
            mobSkillTemplate.setSkillDelayIdx(idx, false);
            if (skilltype != 0) {
                mobSkillTemplate.setSkillDelayType(skilltype, false);
            }
        }
    }
}
