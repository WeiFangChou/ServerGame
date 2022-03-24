package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1BookConfig;

public interface CharBookConfigStorage {
    L1BookConfig get(int i);

    void load();

    void storeCharacterBookConfig(int i, byte[] bArr);

    void updateCharacterConfig(int i, byte[] bArr);
}
