package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;

public class Dice extends ItemExecutor {
    private Dice() {
    }

    public static ItemExecutor get() {
        return new Dice();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        Random _random = new Random();
        int gfxid = 0;
        switch (itemId) {
            case 40325:
                if (!pc.getInventory().checkItem(40318, 1)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    break;
                } else {
                    gfxid = _random.nextInt(2) + 3237;
                    break;
                }
            case 40326:
                if (!pc.getInventory().checkItem(40318, 1)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    break;
                } else {
                    gfxid = _random.nextInt(3) + 3229;
                    break;
                }
            case 40327:
                if (!pc.getInventory().checkItem(40318, 1)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    break;
                } else {
                    gfxid = _random.nextInt(4) + 3241;
                    break;
                }
            case 40328:
                if (!pc.getInventory().checkItem(40318, 1)) {
                    pc.sendPackets(new S_ServerMessage(79));
                    break;
                } else {
                    gfxid = _random.nextInt(6) + 3204;
                    break;
                }
        }
        if (gfxid != 0) {
            pc.getInventory().consumeItem(40318, 1);
            pc.sendPacketsAll(new S_SkillSound(pc.getId(), gfxid));
        }
    }
}
