package com.lineage.server.model.poison;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_SkillIconPoison;

public class L1SilencePoison extends L1Poison {
    private final L1Character _target;

    public static boolean doInfection(L1Character cha) {
        if (!L1Poison.isValidTarget(cha)) {
            return false;
        }
        cha.setPoison(new L1SilencePoison(cha));
        return true;
    }

    private L1SilencePoison(L1Character cha) {
        this._target = cha;
        doInfection();
    }

    private void doInfection() {
        this._target.setPoisonEffect(1);
        sendMessageIfPlayer(this._target, 310);
        this._target.setSkillEffect(L1SkillId.STATUS_POISON_SILENCE, 0);
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public int getEffectId() {
        return 1;
    }

    @Override // com.lineage.server.model.poison.L1Poison
    public void cure() {
        this._target.setPoisonEffect(0);
        sendMessageIfPlayer(this._target, 311);
        if (this._target instanceof L1PcInstance) {
            ((L1PcInstance) this._target).sendPackets(new S_SkillIconPoison(0, 0));
        }
        this._target.killSkillEffectTimer(L1SkillId.STATUS_POISON_SILENCE);
        this._target.setPoison(null);
    }
}
