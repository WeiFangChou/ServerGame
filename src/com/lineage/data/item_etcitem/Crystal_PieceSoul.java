package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Crystal_PieceSoul extends ItemExecutor {
    private Crystal_PieceSoul() {
    }

    public static ItemExecutor get() {
        return new Crystal_PieceSoul();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        boolean notUse = false;
        switch (item.getItemId()) {
            case 40576:
                if (!pc.isElf()) {
                    notUse = true;
                    break;
                }
                break;
            case 40577:
                if (!pc.isWizard()) {
                    notUse = true;
                    break;
                }
                break;
            case 40578:
                if (!pc.isKnight()) {
                    notUse = true;
                    break;
                }
                break;
        }
        if (notUse) {
            pc.sendPackets(new S_ServerMessage(264));
            return;
        }
        String itenName = item.getLogName();
        if (pc.castleWarResult()) {
            pc.sendPackets(new S_ServerMessage(403, itenName));
        } else if (pc.getMapId() == 303) {
            pc.sendPackets(new S_ServerMessage(403, itenName));
        } else {
            pc.death(null);
            pc.getInventory().removeItem(item, 1);
            CreateNewItem.createNewItem(pc, item.getItemId() - 3, 1);
        }
    }
}
