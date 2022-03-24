package com.lineage.server.model;

import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Cube extends TimerTask {
    private static final Log _log = LogFactory.getLog(L1Cube.class);
    private final L1Character _cha;
    private final L1Character _effect;
    private ScheduledFuture<?> _future = null;
    private final int _skillId;
    private int _timeCounter = 0;

    public L1Cube(L1Character effect, L1Character cha, int skillId) {
        this._effect = effect;
        this._cha = cha;
        this._skillId = skillId;
    }

    public void run() {
        try {
            if (this._cha.isDead()) {
                stop();
            } else if (!this._cha.hasSkillEffect(this._skillId)) {
                stop();
            } else {
                this._timeCounter++;
                giveEffect();
            }
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void begin() {
        this._future = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 900L, 1000L);
    }

    public void stop() {
        if (this._future != null) {
            this._future.cancel(false);
        }
    }

    public void giveEffect() throws Exception {
        switch (this._skillId) {
            case L1SkillId.STATUS_CUBE_IGNITION_TO_ENEMY /*{ENCODED_INT: 1019}*/:
                if (this._timeCounter % 4 == 0 && !this._cha.hasSkillEffect(L1SkillId.STATUS_FREEZE) && !this._cha.hasSkillEffect(78) && !this._cha.hasSkillEffect(50) && !this._cha.hasSkillEffect(80) && !this._cha.hasSkillEffect(194) && !this._cha.hasSkillEffect(157)) {
                    if (this._cha instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance) this._cha;
                        pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 2));
                        pc.receiveDamage(this._effect, 10.0d, false, true);
                        return;
                    } else if (this._cha instanceof L1MonsterInstance) {
                        L1MonsterInstance mob = (L1MonsterInstance) this._cha;
                        mob.broadcastPacketX10(new S_DoActionGFX(mob.getId(), 2));
                        mob.receiveDamage(this._effect, 10);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case L1SkillId.STATUS_CUBE_QUAKE_TO_ALLY /*{ENCODED_INT: 1020}*/:
            case L1SkillId.STATUS_CUBE_SHOCK_TO_ALLY /*{ENCODED_INT: 1022}*/:
            case 1024:
            default:
                return;
            case L1SkillId.STATUS_CUBE_QUAKE_TO_ENEMY /*{ENCODED_INT: 1021}*/:
                if (this._timeCounter % 4 == 0 && !this._cha.hasSkillEffect(L1SkillId.STATUS_FREEZE) && !this._cha.hasSkillEffect(78) && !this._cha.hasSkillEffect(50) && !this._cha.hasSkillEffect(80) && !this._cha.hasSkillEffect(194) && !this._cha.hasSkillEffect(157)) {
                    if (this._cha instanceof L1PcInstance) {
                        L1PcInstance pc2 = (L1PcInstance) this._cha;
                        pc2.setSkillEffect(L1SkillId.STATUS_FREEZE, L1SkillId.STATUS_BRAVE);
                        pc2.sendPackets(new S_Paralysis(6, true));
                        return;
                    } else if (this._cha instanceof L1MonsterInstance) {
                        L1MonsterInstance mob2 = (L1MonsterInstance) this._cha;
                        mob2.setSkillEffect(L1SkillId.STATUS_FREEZE, L1SkillId.STATUS_BRAVE);
                        mob2.setParalyzed(true);
                        return;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case L1SkillId.STATUS_CUBE_SHOCK_TO_ENEMY /*{ENCODED_INT: 1023}*/:
                this._cha.setSkillEffect(1024, L1SkillId.STATUS_FREEZE);
                return;
            case L1SkillId.STATUS_CUBE_BALANCE /*{ENCODED_INT: 1025}*/:
                if (this._timeCounter % 4 == 0) {
                    int newMp = this._cha.getCurrentMp() + 5;
                    if (newMp < 0) {
                        newMp = 0;
                    }
                    this._cha.setCurrentMp(newMp);
                }
                if (this._timeCounter % 5 != 0) {
                    return;
                }
                if (this._cha instanceof L1PcInstance) {
                    ((L1PcInstance) this._cha).receiveDamage(this._effect, 25.0d, false, true);
                    return;
                } else if (this._cha instanceof L1MonsterInstance) {
                    ((L1MonsterInstance) this._cha).receiveDamage(this._effect, 25);
                    return;
                } else {
                    return;
                }
        }
    }
}
