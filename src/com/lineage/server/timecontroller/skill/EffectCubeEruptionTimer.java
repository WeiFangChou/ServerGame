package com.lineage.server.timecontroller.skill;

import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.WorldEffect;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EffectCubeEruptionTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(EffectCubeEruptionTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 500L, 500L);
    }

    public void run() {
        try {
            Collection<L1EffectInstance> allNpc = WorldEffect.get().all();
            if (!allNpc.isEmpty()) {
                for (L1EffectInstance effect : allNpc) {
                    if (effect.effectType() == L1EffectType.isCubeEruption) {
                        EffectCubeExecutor.get().cubeBurn(effect);
                        Thread.sleep(1);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Npc L1Effect幻術師技能(立方：地裂)狀態送出時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new EffectCubeEruptionTimer().start();
        }
    }
}
