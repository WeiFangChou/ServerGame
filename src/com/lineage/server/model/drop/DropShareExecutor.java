package com.lineage.server.model.drop;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;
import java.util.ArrayList;

public interface DropShareExecutor {
    void dropShare(L1NpcInstance l1NpcInstance, ArrayList<L1Character> arrayList, ArrayList<Integer> arrayList2);
}
