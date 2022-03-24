package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1UserSkillTmp;
import java.util.ArrayList;

public interface CharSkillStorage {
    void load();

    void setAuto(int i, int i2, int i3);

    ArrayList<L1UserSkillTmp> skills(int i);

    boolean spellCheck(int i, int i2);

    void spellLost(int i, int i2);

    void spellMastery(int i, int i2, String str, int i3, int i4);
}
