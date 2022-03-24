package com.lineage.data.item_etcitem.poweritem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;
import william.ItemBlessarmor;
import william.L1William_Bless_armor;

public class Item_Bless_armor extends ItemExecutor {
    private int chance_Bless = 0;

    private Item_Bless_armor() {
    }

    public static ItemExecutor get() {
        return new Item_Bless_armor();
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
                pc.sendPackets(new S_ServerMessage(166, String.valueOf(tgItem.getLogName()) + " 裝備中，無法物品施放祝福"));
            } else if (tgItem.getItem().getType2() == 1) {
                pc.sendPackets(new S_ServerMessage(166, "無法物品施放祝福"));
            } else if (tgItem.getItem().getType() == 8 || tgItem.getItem().getType() == 9 || tgItem.getItem().getType() == 10 || tgItem.getItem().getType() == 11 || tgItem.getItem().getType() == 12) {
                pc.sendPackets(new S_ServerMessage(166, "無法物品施放祝福"));
            } else if (tgItem.getBless() == 0) {
                pc.sendPackets(new S_ServerMessage(166, "無法對【" + tgItem.getLogName() + "】施放祝福"));
            } else {
                L1William_Bless_armor Bless_armor_Item = ItemBlessarmor.getInstance().getTemplate(tgItem.getItem().getItemId());
                if (Bless_armor_Item != null) {
                    int ItemId = Bless_armor_Item.getItem_Id();
                    int enchant_level = tgItem.getEnchantLevel();
                    if (tgItem == null) {
                        return;
                    }
                    if (this.chance_Bless >= Random.nextInt(100) + 1) {
                        pc.getInventory().removeItem(item, 1);
                        pc.getInventory().removeItem(tgItem, 1);
                        L1ItemInstance item1 = ItemTable.get().createItem(ItemId);
                        item1.setIdentified(true);
                        item1.setCount(1);
                        item1.setEnchantLevel(enchant_level);
                        pc.getInventory().storeItem(item1);
                        pc.sendPackets(new S_ServerMessage(166, "【" + item1.getLogName() + "】瞬間散發祝福的光芒"));
                        switch (Bless_armor_Item.getMessage()) {
                            case 1:
                                World.get().broadcastPacketToAll(new S_ServerMessage("恭喜【" + pc.getName() + "】使用【" + item.getName() + "】讓【+" + item1.getEnchantLevel() + " " + item1.getName() + "】散發出祝福的光芒。"));
                                return;
                            case 2:
                                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】使用【" + item.getName() + "】讓【+" + item1.getEnchantLevel() + " " + item1.getName() + "】散發出祝福的光芒。"));
                                return;
                            case 3:
                                World.get().broadcastPacketToAll(new S_ServerMessage("恭喜【" + pc.getName() + "】使用【" + item.getName() + "】讓【+" + item1.getEnchantLevel() + " " + item1.getName() + "】散發出祝福的光芒。"));
                                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】使用【" + item.getName() + "】讓【+" + item1.getEnchantLevel() + " " + item1.getName() + "】散發出祝福的光芒。"));
                                return;
                            default:
                                return;
                        }
                    } else {
                        pc.sendPackets(new S_ServerMessage(166, "【" + tgItem.getLogName() + "】瞬間散發失敗的黑暗"));
                        pc.getInventory().removeItem(item, 1);
                    }
                }
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        if (set.length > 1) {
            try {
                this.chance_Bless = Integer.parseInt(set[1]);
            } catch (Exception ignored) {
            }
        }
    }
}
