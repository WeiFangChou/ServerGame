package com.lineage.data.item_etcitem.xljnet;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;
import william.Item_Super;
import william.L1William_Super;

public class Item_Super_Wake extends ItemExecutor {
    private int chance_Super = 0;

    private Item_Super_Wake() {
    }

    public static ItemExecutor get() {
        return new Item_Super_Wake();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
        if (tgItem != null) {
            if (pc.getInventory().getSize() >= 179) {
                pc.sendPackets(new S_ServerMessage(263));
            } else if (pc.getInventory().getWeight240() >= 197) {
                pc.sendPackets(new S_ServerMessage(110));
            } else if (tgItem.isEquipped()) {
                pc.sendPackets(new S_ServerMessage(166, String.valueOf(tgItem.getLogName()) + "使用中"));
            } else {
                L1William_Super Super_Item = Item_Super.getInstance().getTemplate(tgItem.getItem().getItemId());
                if (Super_Item != null) {
                    int ItemId = Super_Item.getItem_Id();
                    int Super_itemId = Super_Item.getSuperitemId();
                    int SuperCount = Super_Item.getSuperCount();
                    if (tgItem == null) {
                        return;
                    }
                    if (this.chance_Super >= Random.nextInt(100) + 1) {
                        pc.getInventory().removeItem(item, 1);
                        pc.getInventory().removeItem(tgItem, 1);
                        L1ItemInstance item1 = ItemTable.get().createItem(ItemId);
                        item1.setIdentified(true);
                        item1.setCount(1);
                        pc.getInventory().storeItem(item1);
                        pc.sendPackets(new S_ServerMessage(166, "【" + item1.getLogName() + "】覺醒成功"));
                        switch (Super_Item.getMessage()) {
                            case 1:
                                World.get().broadcastPacketToAll(new S_ServerMessage("恭喜【" + pc.getName() + "】使用【" + item.getName() + "】覺醒成功【 " + item1.getName() + "】。"));
                                return;
                            case 2:
                                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】使用【" + item.getName() + "】覺醒成功【 " + item1.getName() + "】。"));
                                return;
                            case 3:
                                World.get().broadcastPacketToAll(new S_ServerMessage("恭喜【" + pc.getName() + "】使用【" + item.getName() + "】覺醒成功【 " + item1.getName() + "】。"));
                                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】使用【" + item.getName() + "】覺醒成功【 " + item1.getName() + "】。"));
                                return;
                            default:
                                return;
                        }
                    } else if (Super_itemId != 0) {
                        pc.getInventory().storeItem(Super_itemId, (long) SuperCount);
                        L1Item temp = ItemTable.get().getTemplate(Super_itemId);
                        pc.getInventory().removeItem(item, 1);
                        pc.sendPackets(new S_ServerMessage(166, "【" + tgItem.getLogName() + "】覺醒失敗"));
                        pc.sendPackets(new S_SystemMessage("退回" + temp.getName() + "(" + SuperCount + ")。"));
                        WriteLogTxt.Recording("覺醒失敗退回，獲得紀錄", "玩家:【" + pc.getName() + "】 " + "地點:【 " + pc.getLocation() + "】獲得" + "【 " + temp.getName() + "(" + SuperCount + ")】");
                    }
                }
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        if (set.length > 1) {
            try {
                this.chance_Super = Integer.parseInt(set[1]);
            } catch (Exception ignored) {
            }
        }
    }
}
