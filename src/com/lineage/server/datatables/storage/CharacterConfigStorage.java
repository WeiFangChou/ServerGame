package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Config;

public interface CharacterConfigStorage {
    L1Config get(int i);

    void load();

    void storeCharacterConfig(int i, int i2, byte[] bArr);

    void updateCharacterConfig(int i, int i2, byte[] bArr);
}
