package com.lineage.server.timecontroller;

import com.lineage.server.timecontroller.skill.EffectCubeBurnTimer;
import com.lineage.server.timecontroller.skill.EffectCubeEruptionTimer;
import com.lineage.server.timecontroller.skill.EffectCubeHarmonizeTimer;
import com.lineage.server.timecontroller.skill.EffectCubeShockTimer;
import com.lineage.server.timecontroller.skill.EffectFirewallTimer;
import com.lineage.server.timecontroller.skill.Skill_Awake_Timer;

public class StartTimer_Skill {
    public void start() {
        new Skill_Awake_Timer().start();
        new EffectFirewallTimer().start();
        new EffectCubeBurnTimer().start();
        new EffectCubeEruptionTimer().start();
        new EffectCubeShockTimer().start();
        new EffectCubeHarmonizeTimer().start();
    }
}
