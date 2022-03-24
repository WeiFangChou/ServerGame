package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ItemName;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1SuperCard;

public class SuperCard extends ItemExecutor {
    private SuperCard() {
    }

    public static ItemExecutor get() {
        return new SuperCard();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        L1SuperCard card;
        if (item != null && pc != null && (card = item.getItem().getCard()) != null) {
            L1ItemInstance stec = pc.getInventory().findEquippedSuperCardByType(card.getCardType());
            if (stec != null && item != stec) {
                pc.sendPackets(new S_SystemMessage("已有相同裝備使用中。"));
            } else if (item.isEquipped()) {
                pc.getInventory().setEquipped(item, false, false, false);
            } else {
                pc.getInventory().setEquipped(item, true, false, false);
                pc.sendPackets(new S_ItemName(item));
            }
        }
    }
}
