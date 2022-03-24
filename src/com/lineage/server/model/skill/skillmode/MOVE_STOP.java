package com.lineage.server.model.skill.skillmode;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MOVE_STOP extends SkillMode {
    private static final Log _log = LogFactory.getLog(MOVE_STOP.class);

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        try {
            if (cha.hasSkillEffect(L1SkillId.MOVE_STOP)) {
                return 0;
            }
            cha.setSkillEffect(L1SkillId.MOVE_STOP, integer * L1SkillId.STATUS_BRAVE);
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Paralysis(6, true));
                pc.sendPacketsAll(new S_Poison(pc.getId(), 2));
                return 0;
            } else if (!(cha instanceof L1MonsterInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PetInstance)) {
                return 0;
            } else {
                L1NpcInstance tgnpc = (L1NpcInstance) cha;
                tgnpc.broadcastPacketAll(new S_Poison(tgnpc.getId(), 2));
                tgnpc.setParalyzed(true);
                return 0;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        try {
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_Paralysis(6, false));
                pc.sendPacketsAll(new S_Poison(pc.getId(), 0));
            } else if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
                L1NpcInstance tgnpc = (L1NpcInstance) cha;
                tgnpc.broadcastPacketAll(new S_Poison(tgnpc.getId(), 0));
                tgnpc.setParalyzed(false);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
