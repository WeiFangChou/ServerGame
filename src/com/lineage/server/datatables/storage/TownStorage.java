package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Town;

public interface TownStorage {
    void addSalesMoney(int i, int i2);

    void clearHomeTownID();

    int getPay(int i);

    L1Town getTownTable(int i);

    L1Town[] getTownTableList();

    boolean isLeader(L1PcInstance l1PcInstance, int i);

    void load();

    String totalContribution(int i);

    void updateSalesMoneyYesterday();

    void updateTaxRate();
}
