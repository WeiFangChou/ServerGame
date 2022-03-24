package com.lineage.server.timecontroller.skill;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldDragonKnight;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Skill_Awake_Timer extends TimerTask {
    private static final Log _log = LogFactory.getLog(Skill_Awake_Timer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1PcInstance> allPc = WorldDragonKnight.get().all();
            if (!allPc.isEmpty()) {
                for (L1PcInstance tgpc : allPc) {
                    if (!tgpc.isDead() && tgpc.isMpReductionActiveByAwake()) {
                        int time = tgpc.get_awakeMprTime() - 1;
                        if (time <= 0) {
                            decreaseMp(tgpc);
                            tgpc.set_awakeMprTime(4);
                        } else {
                            tgpc.set_awakeMprTime(time);
                        }
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("龍騎士覺醒技能MP自然減少處理異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new Skill_Awake_Timer().start();
        }
    }

    public static void decreaseMp(L1PcInstance tgpc) {
        try {
            int newMp = tgpc.getCurrentMp() - 8;
            if (newMp < 0) {
                newMp = 0;
                stop(tgpc);
            }
            tgpc.setCurrentMp(newMp);
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void stop(L1PcInstance pc) {
        switch (pc.getAwakeSkillId()) {
            case 185:
                pc.addMaxHp(-127);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) {
                    pc.getParty().updateMiniHP(pc);
                }
                pc.addAc(12);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                break;
            case 190:
                pc.addMr(-30);
                pc.addWind(-30);
                pc.addWater(-30);
                pc.addFire(-30);
                pc.addEarth(-30);
                pc.sendPackets(new S_SPMR(pc));
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                break;
            case 195:
                pc.addStr(-5);
                pc.addCon(-5);
                pc.addDex(-5);
                pc.addCha(-5);
                pc.addInt(-5);
                pc.addWis(-5);
                pc.sendPackets(new S_OwnCharStatus2(pc));
                break;
        }
        pc.setAwakeSkillId(0);
        L1BuffUtil.undoPoly(pc);
        pc.stopMpReductionByAwake();
    }
}
