package com.lineage.server.datatables.storage;

import java.util.Map;

public interface CharacterQuestStorage {
    void delQuest(int i, int i2);

    Map<Integer, Integer> get(int i);

    void load();

    void storeQuest(int i, int i2, int i3);

    void updateQuest(int i, int i2, int i3);
}
