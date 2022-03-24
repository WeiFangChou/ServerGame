package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.templates.L1Pet;

public interface PetStorage {
    void deletePet(int i);

    L1Pet[] getPetTableList();

    L1Pet getTemplate(int i);

    L1Pet getTemplateX(int i);

    boolean isNameExists(String str);

    void load();

    void storeNewPet(L1NpcInstance l1NpcInstance, int i, int i2);

    void storePet(L1Pet l1Pet);
}
