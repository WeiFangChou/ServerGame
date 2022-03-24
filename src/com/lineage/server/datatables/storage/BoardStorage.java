package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;
import java.util.Map;

public interface BoardStorage {
    void deleteTopic(int i);

    Map<Integer, L1Board> getBoardMap();

    L1Board getBoardTable(int i);

    L1Board[] getBoardTableList();

    int getMaxId();

    void load();

    void writeTopic(L1PcInstance l1PcInstance, String str, String str2, String str3);
}
