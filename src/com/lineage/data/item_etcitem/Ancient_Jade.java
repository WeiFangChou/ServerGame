package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;

public class Ancient_Jade extends ItemExecutor {
    private Ancient_Jade() {
    }

    public static ItemExecutor get() {
        return new Ancient_Jade();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int characterSlot;
        if (pc == null || item == null) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        L1Account account = pc.getNetConnection().getAccount();
        if (account == null) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        int characterSlot2 = account.get_character_slot();
        if (ConfigAlt.DEFAULT_CHARACTER_SLOT + characterSlot2 >= 8) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        pc.getInventory().removeItem(item, 1);
        if (characterSlot2 < 0) {
            characterSlot = 0;
        } else {
            characterSlot = characterSlot2 + 1;
        }
        account.set_character_slot(characterSlot);
        AccountReading.get().updateCharacterSlot(pc.getAccountName(), characterSlot);
    }
}
