package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1House;
import java.util.Map;

public interface HouseStorage {
    L1House getHouseTable(int i);

    Map<Integer, L1House> getHouseTableList();

    void load();

    void updateHouse(L1House l1House);
}
