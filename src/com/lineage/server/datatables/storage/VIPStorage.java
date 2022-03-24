package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import java.sql.Timestamp;
import java.util.Map;

public interface VIPStorage {
    void delOther(int i);

    Timestamp getOther(L1PcInstance l1PcInstance);

    void load();

    Map<Integer, Timestamp> map();

    void storeOther(int i, Timestamp timestamp);
}
