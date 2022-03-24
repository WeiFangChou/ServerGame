package com.lineage.data.item_etcitem;

import com.lineage.config.ConfigRate;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class Mystical_Ption extends ItemExecutor {
    private Mystical_Ption() {
    }

    public static ItemExecutor get() {
        return new Mystical_Ption();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
        if (tgItem != null) {
            Random random = new Random();
            int earing2Id = tgItem.getItem().getItemId();
            int potion1 = 0;
            int potion2 = 0;
            if (earing2Id < 41173 || 41184 < earing2Id) {
                pc.sendPackets(new S_ServerMessage(79));
                return;
            }
            if (itemId == 40926) {
                potion1 = OpcodesClient.C_OPCODE_ARROWATTACK;
                potion2 = OpcodesClient.C_OPCODE_ENTERPORTAL;
            } else if (itemId == 40927) {
                potion1 = OpcodesClient.C_OPCODE_ENTERPORTAL;
                potion2 = 251;
            } else if (itemId == 40928) {
                potion1 = 251;
                potion2 = 253;
            } else if (itemId == 40929) {
                potion1 = 253;
                potion2 = 255;
            }
            if (earing2Id < itemId + potion1 || itemId + potion2 < earing2Id) {
                pc.sendPackets(new S_ServerMessage(79));
            } else if (random.nextInt(99) + 1 < ConfigRate.CREATE_CHANCE_MYSTERIOUS) {
                CreateNewItem.createNewItem(pc, earing2Id - 12, 1);
                pc.getInventory().removeItem(tgItem, 1);
                pc.getInventory().removeItem(item, 1);
            } else {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.AQUA_PROTECTER, tgItem.getName()));
                pc.getInventory().removeItem(item, 1);
            }
        }
    }
}
