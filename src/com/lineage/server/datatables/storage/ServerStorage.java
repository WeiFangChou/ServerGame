package com.lineage.server.datatables.storage;

public interface ServerStorage {
    void isStop();

    void load();

    int maxId();

    int minId();
}
