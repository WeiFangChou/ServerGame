package com.lineage.server.datatables.storage;

import com.lineage.server.templates.L1EmblemIcon;

public interface ClanEmblemStorage {
    void add(int i, byte[] bArr);

    void deleteIcon(int i);

    L1EmblemIcon get(int i);

    void load();

    L1EmblemIcon storeClanIcon(int i, byte[] bArr);

    void updateClanIcon(L1EmblemIcon l1EmblemIcon);
}
