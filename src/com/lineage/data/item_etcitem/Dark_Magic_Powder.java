package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Dark_Magic_Powder extends ItemExecutor {
    private Dark_Magic_Powder() {
    }

    public static ItemExecutor get() {
        return new Dark_Magic_Powder();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            Random _random = new Random();
            int historybookId = item1.getItem().getItemId();
            if (historybookId < 41011 || 41018 < historybookId) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            if (_random.nextInt(99) + 1 <= ConfigRate.CREATE_CHANCE_HISTORY_BOOK) {
                CreateNewItem.createNewItem(pc, historybookId + 8, 1);
            } else {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, item1.getName()));
            }
            pc.getInventory().removeItem(item1, 1);
            pc.getInventory().removeItem(item, 1);
        }
    }
}
