package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.thread.GeneralThreadPool;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Chaser extends TimerTask {
    private static final Log _log = LogFactory.getLog(L1Chaser.class);
    private static final Random _random = new Random();
    private final L1Character _cha;
    private ScheduledFuture<?> _future = null;
    private final L1PcInstance _pc;
    private int _timeCounter = 0;

    public L1Chaser(L1PcInstance pc, L1Character cha) {
        this._cha = cha;
        this._pc = pc;
    }

    public void run() {
        try {
            if (this._cha == null || this._cha.isDead()) {
                stop();
                return;
            }
            attack();
            this._timeCounter++;
            if (this._timeCounter >= 3) {
                stop();
            }
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void begin() {
        this._future = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 0L, 1000L);
    }

    public void stop() {
        if (this._future != null) {
            this._future.cancel(false);
        }
    }

    private void attack() throws Exception {
        double damage = getDamage(this._pc, this._cha);
        if (this._cha.getCurrentHp() - ((int) damage) <= 0 && this._cha.getCurrentHp() != 1) {
            damage = (double) (this._cha.getCurrentHp() - 1);
        } else if (this._cha.getCurrentHp() == 1) {
            damage = 0.0d;
        }
        this._pc.sendPacketsAll(new S_EffectLocation(this._cha.getX(), this._cha.getY(), 7025));
        if (this._cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) this._cha;
            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 2));
            pc.receiveDamage(this._pc, damage, false, false);
        } else if (this._cha instanceof L1NpcInstance) {
            L1NpcInstance npc = (L1NpcInstance) this._cha;
            npc.broadcastPacketX10(new S_DoActionGFX(npc.getId(), 2));
            npc.receiveDamage(this._pc, (int) damage);
        }
    }

    private double getDamage(L1PcInstance pc, L1Character cha) {
        double coefficientB;
        double coefficientC;
        int spByItem = pc.getSp() - pc.getTrueSp();
        int intel = pc.getInt();
        double coefficientA = 1.0d + (0.09375d * ((double) ((pc.getInt() + spByItem) - 12)));
        if (coefficientA < 1.0d) {
            coefficientA = 1.0d;
        }
        if (intel > 25) {
            coefficientB = 1.625d;
        } else if (intel <= 12) {
            coefficientB = 0.78d;
        } else {
            coefficientB = ((double) intel) * 0.065d;
        }
        if (intel > 25) {
            coefficientC = 25.0d;
        } else if (intel <= 12) {
            coefficientC = 12.0d;
        } else {
            coefficientC = (double) intel;
        }
        double bsk = 0.0d;
        if (pc.hasSkillEffect(55)) {
            bsk = 0.1d;
        }
        return L1WeaponSkill.calcDamageReduction(pc, cha, ((((((double) ((_random.nextInt(6) + 1) + 7)) * (1.0d + bsk)) * coefficientA) * coefficientB) / 10.5d) * coefficientC * 2.0d, 0);
    }
}
