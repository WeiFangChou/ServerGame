package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Account;

public interface AccountStorage {
    L1Account create(String str, String str2, String str3, String str4, String str5);

    L1Account getAccount(String str);

    int getPoint(String str) throws Throwable;

    boolean isAccount(String str);

    boolean isAccountUT(String str);

    void load();

    void setPoint(String str, int i);

    void updateCharacterSlot(String str, int i);

    void updateFirstPay(String str, int i);

    void updateLan();

    void updateLan(String str, boolean z);

    void updateLastActive(L1Account l1Account);

    void updatePwd(String str, String str2);

    void updateWarehouse(String str, int i);
}
