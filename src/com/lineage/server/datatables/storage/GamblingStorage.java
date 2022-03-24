package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Gambling;

public interface GamblingStorage {
    void add(L1Gambling l1Gambling);

    L1Gambling getGambling(int i);

    L1Gambling getGambling(String str);

    void load();

    int maxId();

    void updateGambling(int i, int i2);

    int[] winCount(int i);
}
