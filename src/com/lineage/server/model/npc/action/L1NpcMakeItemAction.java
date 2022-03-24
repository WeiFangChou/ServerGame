package com.lineage.server.model.npc.action;

import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.Item_Make_Table;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1ObjectAmount;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.serverpackets.S_HowManyMake;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.IterableElementList;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcMakeItemAction extends L1NpcXmlAction {
    private final List<L1ObjectAmount<Integer>> _materials = new ArrayList();
    private final List<L1ObjectAmount<Integer>> _items = new ArrayList();
    private final boolean _isAmountInputable;
    private final L1NpcAction _actionOnSucceed;
    private final L1NpcAction _actionOnFail;

    public L1NpcMakeItemAction(Element element) {
        super(element);
        this._isAmountInputable = L1NpcXmlParser.getBoolAttribute(element, "AmountInputable", true);
        NodeList list = element.getChildNodes();
        Iterator var4 = (new IterableElementList(list)).iterator();

        Element elem;
        while(var4.hasNext()) {
            elem = (Element)var4.next();
            int id;
            long amount;
            int enchant;
            if (elem.getNodeName().equalsIgnoreCase("Material")) {
                id = Integer.valueOf(elem.getAttribute("ItemId"));
                amount = Long.valueOf(elem.getAttribute("Amount"));
                enchant = 0;

                try {
                    enchant = Integer.valueOf(elem.getAttribute("Enchant"));
                } catch (Exception var10) {
                }

                this._materials.add(new L1ObjectAmount(id, amount, enchant));
            } else if (elem.getNodeName().equalsIgnoreCase("Item")) {
                id = Integer.valueOf(elem.getAttribute("ItemId"));
                amount = Long.valueOf(elem.getAttribute("Amount"));
                enchant = 0;

                try {
                    enchant = Integer.valueOf(elem.getAttribute("Enchant"));
                } catch (Exception var11) {
                }

                this._items.add(new L1ObjectAmount(id, amount, enchant));
            }
        }

        if (!this._items.isEmpty() && !this._materials.isEmpty()) {
            elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Succeed");
            this._actionOnSucceed = elem == null ? null : new L1NpcListedAction(elem);
            elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
            this._actionOnFail = elem == null ? null : new L1NpcListedAction(elem);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean makeItems(L1PcInstance pc, String npcName, long amount) throws Exception {
        if (amount > 0L && amount < 1000L) {
            boolean isEnoughMaterials = true;
            Iterator var7 = this._materials.iterator();

            while(var7.hasNext()) {
                L1ObjectAmount<Integer> material = (L1ObjectAmount)var7.next();
                if (!pc.getInventory().productionList((Integer)material.getObject(), material.getEnchant(), material.getAmount() * amount)) {
                    L1Item temp = ItemTable.get().getTemplate((Integer)material.getObject());
                    if (material.getEnchant() != 0) {
                        pc.sendPackets(new S_ServerMessage(337, (material.getEnchant() < 0 ? material.getEnchant() : "+" + material.getEnchant()) + " " + temp.getName() + "(" + (material.getAmount() * amount - pc.getInventory().countItems(temp.getItemId())) + ")"));
                    } else {
                        pc.sendPackets(new S_ServerMessage(337, temp.getName() + "(" + (material.getAmount() * amount - pc.getInventory().countItems(temp.getItemId())) + ")"));
                    }

                    isEnoughMaterials = false;
                }
            }

            if (!isEnoughMaterials) {
                return false;
            } else {
                int countToCreate = 0;
                int weight = 0;
                Iterator var9 = this._items.iterator();

                long _CountToCreate;
                do {
                    L1ObjectAmount makingItem;
                    if (!var9.hasNext()) {
                        if (pc.getInventory().getSize() + countToCreate > 180) {
                            pc.sendPackets(new S_ServerMessage(263));
                            return false;
                        }

                        if (pc.getMaxWeight() < (double)(pc.getInventory().getWeight() + weight)) {
                            pc.sendPackets(new S_ServerMessage(82));
                            return false;
                        }

                        var9 = this._materials.iterator();

                        while(var9.hasNext()) {
                            makingItem = (L1ObjectAmount)var9.next();
                            if (makingItem.getEnchant() != 0) {
                                pc.getInventory().isProductionList((Integer)makingItem.getObject(), makingItem.getAmount() * amount, makingItem.getEnchant());
                            } else {
                                pc.getInventory().consumeItem((Integer)makingItem.getObject(), makingItem.getAmount() * amount);
                            }
                        }

                        var9 = this._items.iterator();

                        while(var9.hasNext()) {
                            makingItem = (L1ObjectAmount)var9.next();
                            L1ItemInstance item = pc.getInventory().storeItem((Integer)makingItem.getObject(), makingItem.getAmount() * amount);
                            if (item != null) {
                                String itemName = ItemTable.get().getTemplate((Integer)makingItem.getObject()).getName();
                                if (makingItem.getAmount() * amount > 1L) {
                                    itemName = itemName + " (" + makingItem.getAmount() * amount + ")";
                                }

                                if (makingItem.getEnchant() != 0) {
                                    itemName = (makingItem.getEnchant() < 0 ? makingItem.getEnchant() : "+" + makingItem.getEnchant()) + " " + itemName;
                                }

                                pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                if (Item_Make_Table.get().contains((Integer)makingItem.getObject())) {
                                    World.get().broadcastPacketToAll(new S_SystemMessage("恭喜玩家：【" + pc.getName() + "】製造取得【" + item.getName() + "(" + item.getCount() + ")】。"));
                                }

                                if (Item_Make_Table.get().contains((Integer)makingItem.getObject())) {
                                    WriteLogTxt.Recording("製造道具紀錄", "(" + pc.getNetConnection().getIp() + ")" + "玩家:【" + pc.getName() + "】 " + "ID:【 " + npcName + "】" + "【 +" + item.getEnchantLevel() + " " + item.getName() + "(" + item.getCount() + ")】" + "道具ID：【" + item.getId() + "】");
                                }
                            }
                        }

                        return true;
                    }

                    makingItem = (L1ObjectAmount)var9.next();
                    L1Item temp = ItemTable.get().getTemplate((Integer)makingItem.getObject());
                    if (temp.isStackable()) {
                        if (!pc.getInventory().checkItem((Integer)makingItem.getObject())) {
                            ++countToCreate;
                        }
                    } else {
                        countToCreate = (int)((long)countToCreate + makingItem.getAmount() * amount);
                    }

                    weight = (int)((long)weight + (long)temp.getWeight() * makingItem.getAmount() * amount / 1000L);
                    _CountToCreate = (long)countToCreate;
                } while(_CountToCreate >= 0L && _CountToCreate <= 1000L);

                return false;
            }
        } else {
            return false;
        }
    }

    private long countNumOfMaterials(L1PcInventory inv) {
        long count = 9223372036854775807L;

        long numOfSet;
        for(Iterator var5 = this._materials.iterator(); var5.hasNext(); count = Math.min(count, numOfSet)) {
            L1ObjectAmount<Integer> material = (L1ObjectAmount)var5.next();
            numOfSet = inv.countItems((Integer)material.getObject()) / material.getAmount();
        }

        return count;
    }

    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) throws Exception {
        long numOfMaterials = this.countNumOfMaterials(pc.getInventory());
        if (1L < numOfMaterials && this._isAmountInputable) {
            pc.sendPackets(new S_HowManyMake(obj.getId(), (int)numOfMaterials, actionName));
            return null;
        } else {
            return this.executeWithAmount(actionName, pc, obj, 1L);
        }
    }

    public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, long amount) throws Exception {
        L1NpcInstance npc = (L1NpcInstance)obj;
        L1NpcHtml result = null;
        if (this.makeItems(pc, npc.getNameId(), amount)) {
            if (this._actionOnSucceed != null) {
                result = this._actionOnSucceed.execute(actionName, pc, obj, new byte[0]);
            }
        } else if (this._actionOnFail != null) {
            result = this._actionOnFail.execute(actionName, pc, obj, new byte[0]);
        }

        return result == null ? L1NpcHtml.HTML_CLOSE : result;
    }

    public void execute(String actionName, String npcid) {
    }
}
