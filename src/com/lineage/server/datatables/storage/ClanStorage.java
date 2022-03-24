package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import java.util.Map;

public interface ClanStorage {
    void addDeClan(Integer num, L1Clan l1Clan);

    L1Clan createClan(L1PcInstance l1PcInstance, String str);

    void deleteClan(String str);

    L1Clan getTemplate(int i);

    Map<Integer, L1Clan> get_clans();

    void load();

    void updateClan(L1Clan l1Clan);

    void updateClanOnlineMaxUser(L1Clan l1Clan);
}
