package com.lineage.server.storage;

import com.lineage.server.model.Instance.L1PcInstance;

public interface CharacterStorage {
    void createCharacter(L1PcInstance l1PcInstance) throws Exception;

    void deleteCharacter(String str, String str2) throws Exception;

    L1PcInstance loadCharacter(String str) throws Throwable;

    void storeCharacter(L1PcInstance l1PcInstance) throws Exception;
}
