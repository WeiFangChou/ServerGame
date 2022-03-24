package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ResolventTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Dissolution extends ItemExecutor {
    private Dissolution() {
    }

    public static ItemExecutor get() {
        return new Dissolution();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance item1 = pc.getInventory().getItem(data[0]);
        if (item1 != null) {
            useResolvent(pc, item1, item);
        }
    }

    private void useResolvent(L1PcInstance pc, L1ItemInstance item, L1ItemInstance resolvent) throws Exception {
        Random _random = new Random();
        if (item == null || resolvent == null) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        if (item.getItem().getType2() == 1 || item.getItem().getType2() == 2) {
            if (item.getEnchantLevel() != 0) {
                pc.sendPackets(new S_ServerMessage(1161));
                return;
            } else if (item.isEquipped()) {
                pc.sendPackets(new S_ServerMessage(1161));
                return;
            }
        }
        long crystalCount = (long) ResolventTable.get().getCrystalCount(item.getItem().getItemId());
        if (crystalCount == 0) {
            pc.sendPackets(new S_ServerMessage(1161));
            return;
        }
        int rnd = _random.nextInt(100) + 1;
        if (rnd >= 1 && rnd <= 50) {
            crystalCount = 0;
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.NATURES_TOUCH, item.getName()));
        } else if (rnd >= 51 && rnd <= 90) {
            crystalCount *= 1;
        } else if (rnd >= 91 && rnd <= 100) {
            crystalCount = (long) (((double) crystalCount) * 1.5d);
            pc.getInventory().storeItem(41246, (long) (((double) crystalCount) * 1.5d));
        }
        if (crystalCount != 0) {
            CreateNewItem.createNewItem(pc, 41246, crystalCount);
        }
        pc.getInventory().removeItem(item, 1);
        pc.getInventory().removeItem(resolvent, 1);
    }
}
