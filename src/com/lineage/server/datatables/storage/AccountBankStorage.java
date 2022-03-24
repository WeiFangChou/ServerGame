package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1Bank;
import java.util.Map;

public interface AccountBankStorage {
    void create(String str, L1Bank l1Bank);

    L1Bank get(String str);

    void load();

    Map<String, L1Bank> map();

    void updateAdena(String str, long j);

    void updatePass(String str, String str2);
}
