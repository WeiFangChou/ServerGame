package com.lineage.server.model.drop;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1DropMap;
import com.lineage.server.templates.L1DropMob;
import java.util.ArrayList;
import java.util.Map;

public interface SetDropExecutor {
    void addDropMap(Map<Integer, ArrayList<L1Drop>> map);

    void addDropMapX(Map<Integer, ArrayList<L1DropMap>> map);

    void addDropMob(Map<Integer, L1DropMob> map);

    void setDrop(L1NpcInstance l1NpcInstance, L1Inventory l1Inventory);

    void setDrop(L1NpcInstance l1NpcInstance, L1Inventory l1Inventory, double d);
}
