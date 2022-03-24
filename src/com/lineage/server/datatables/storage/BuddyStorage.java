package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1BuddyTmp;
import java.util.ArrayList;

public interface BuddyStorage {
    void addBuddy(int i, int i2, String str);

    void load();

    void removeBuddy(int i, String str);

    ArrayList<L1BuddyTmp> userBuddy(int i);
}
