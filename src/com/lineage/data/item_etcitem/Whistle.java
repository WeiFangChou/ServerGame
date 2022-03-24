package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_Sound;

public class Whistle extends ItemExecutor {
    private Whistle() {
    }

    public static ItemExecutor get() {
        return new Whistle();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        pc.sendPacketsX8(new S_Sound(437));
        Object[] petList = pc.getPetList().values().toArray();
        for (Object petObject : petList) {
            if (petObject instanceof L1PetInstance) {
                ((L1PetInstance) petObject).call();
            }
        }
    }
}
