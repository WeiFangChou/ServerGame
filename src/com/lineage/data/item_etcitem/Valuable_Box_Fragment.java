package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Valuable_Box_Fragment extends ItemExecutor {
    private Valuable_Box_Fragment() {
    }

    public static ItemExecutor get() {
        return new Valuable_Box_Fragment();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        switch (item.getItemId()) {
            case 49093:
                if (pc.getInventory().checkItem(49094, 1)) {
                    pc.getInventory().consumeItem(49093, 1);
                    pc.getInventory().consumeItem(49094, 1);
                    CreateNewItem.createNewItem(pc, 49095, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49094:
                if (pc.getInventory().checkItem(49093, 1)) {
                    pc.getInventory().consumeItem(49093, 1);
                    pc.getInventory().consumeItem(49094, 1);
                    CreateNewItem.createNewItem(pc, 49095, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49097:
                if (pc.getInventory().checkItem(49098, 1)) {
                    pc.getInventory().consumeItem(49097, 1);
                    pc.getInventory().consumeItem(49098, 1);
                    CreateNewItem.createNewItem(pc, 49099, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49098:
                if (pc.getInventory().checkItem(49097, 1)) {
                    pc.getInventory().consumeItem(49097, 1);
                    pc.getInventory().consumeItem(49098, 1);
                    CreateNewItem.createNewItem(pc, 49099, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49269:
                if (pc.getInventory().checkItem(49270, 1)) {
                    pc.getInventory().consumeItem(49270, 1);
                    pc.getInventory().consumeItem(49269, 1);
                    CreateNewItem.createNewItem(pc, 49274, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49270:
                if (pc.getInventory().checkItem(49269, 1)) {
                    pc.getInventory().consumeItem(49269, 1);
                    pc.getInventory().consumeItem(49270, 1);
                    CreateNewItem.createNewItem(pc, 49274, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49271:
                if (pc.getInventory().checkItem(49272, 1)) {
                    pc.getInventory().consumeItem(49272, 1);
                    pc.getInventory().consumeItem(49271, 1);
                    CreateNewItem.createNewItem(pc, 49275, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            case 49272:
                if (pc.getInventory().checkItem(49271, 1)) {
                    pc.getInventory().consumeItem(49271, 1);
                    pc.getInventory().consumeItem(49272, 1);
                    CreateNewItem.createNewItem(pc, 49275, 1);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(79));
                return;
            default:
                return;
        }
    }
}
