package com.lineage.server.model.doll;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Doll_Water extends L1DollExecutor {
    private static final Log _log = LogFactory.getLog(Doll_Water.class);
    private String _note;

    public static L1DollExecutor get() {
        return new Doll_Water();
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_power(int int1, int int2, int int3) {
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void set_note(String note) {
        this._note = note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public String get_note() {
        return this._note;
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void setDoll(L1PcInstance pc) {
        try {
            if (pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_UNDERWATER_BREATH);
            }
            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), -1));
            pc.setSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH, 0);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public void removeDoll(L1PcInstance pc) {
        try {
            pc.killSkillEffectTimer(L1SkillId.STATUS_UNDERWATER_BREATH);
            pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), 0));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.doll.L1DollExecutor
    public boolean is_reset() {
        return false;
    }
}
