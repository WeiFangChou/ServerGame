package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.Map;

public interface EzpayStorage3 {
    Map<Integer, int[]> ezpayInfo(String str);

    void insertItem(String str, L1ItemInstance l1ItemInstance);

    boolean update(String str, int i, String str2, String str3);
}
