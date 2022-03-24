package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Magic {
    private static final Log _log = LogFactory.getLog(L1Magic.class);
    private L1MagicMode _magicMode;

    public L1Magic(L1Character attacker, L1Character target) {
        if (attacker != null) {
            try {
                if (attacker instanceof L1PcInstance) {
                    this._magicMode = new L1MagicPc((L1PcInstance) attacker, target);
                } else {
                    this._magicMode = new L1MagicNpc((L1NpcInstance) attacker, target);
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public void setLeverage(int i) {
        this._magicMode.setLeverage(i);
    }

    public void commit(int damage, int drainMana) throws Exception {
        this._magicMode.commit(damage, drainMana);
    }

    public boolean calcProbabilityMagic(int skillId) {
        return this._magicMode.calcProbabilityMagic(skillId);
    }

    public int calcMagicDamage(int skillId) throws Exception {
        return this._magicMode.calcMagicDamage(skillId);
    }

    public int calcHealing(int skillId) {
        return this._magicMode.calcHealing(skillId);
    }
}
