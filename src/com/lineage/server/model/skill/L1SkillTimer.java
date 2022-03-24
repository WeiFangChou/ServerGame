package com.lineage.server.model.skill;

public interface L1SkillTimer {
    void begin();

    void end();

    int getRemainingTime();

    void kill();
}
