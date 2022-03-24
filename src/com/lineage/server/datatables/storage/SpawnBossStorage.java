package com.lineage.server.datatables.storage;

import com.lineage.server.model.L1Spawn;
import java.util.Calendar;
import java.util.List;

public interface SpawnBossStorage {
    List<Integer> bossIds();

    L1Spawn getTemplate(int i);

    void load();

    void upDateNextSpawnTime(int i, Calendar calendar);
}
