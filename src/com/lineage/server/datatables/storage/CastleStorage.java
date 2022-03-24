package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Castle;
import java.util.Map;

public interface CastleStorage {
    Map<Integer, L1Castle> getCastleMap();

    L1Castle getCastleTable(int i);

    L1Castle[] getCastleTableList();

    void load();

    void updateCastle(L1Castle l1Castle);
}
