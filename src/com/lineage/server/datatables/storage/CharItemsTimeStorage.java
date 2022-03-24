package com.lineage.server.datatables.storage;

import java.sql.Timestamp;

public interface CharItemsTimeStorage {
    void addTime(int i, Timestamp timestamp);

    void load();

    void updateTime(int i, Timestamp timestamp);
}
