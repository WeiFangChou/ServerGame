package com.lineage.server.model.shop;

import com.lineage.config.ConfigRate;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1TaxCalculator;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ShopItem;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class L1Shop {
    private final int _npcId;
    private final List<L1ShopItem> _purchasingItems;
    private final List<L1ShopItem> _sellingItems;

    public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
        if (sellingItems == null || purchasingItems == null) {
            throw new NullPointerException();
        }
        this._npcId = npcId;
        this._sellingItems = sellingItems;
        this._purchasingItems = purchasingItems;
    }

    public int getNpcId() {
        return this._npcId;
    }

    public List<L1ShopItem> getSellingItems() {
        return this._sellingItems;
    }

    private boolean isPurchaseableItem(L1ItemInstance item) {
        if (item != null && !item.isEquipped() && item.getEnchantLevel() == 0 && item.getBless() < 128) {
            return true;
        }
        return false;
    }

    private L1ShopItem getPurchasingItem(int itemId) {
        for (L1ShopItem shopItem : this._purchasingItems) {
            if (shopItem.getItemId() == itemId) {
                return shopItem;
            }
        }
        return null;
    }

    public L1AssessedItem assessItem(L1ItemInstance item) {
        L1ShopItem shopItem = getPurchasingItem(item.getItemId());
        if (shopItem == null) {
            return null;
        }
        return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
    }

    private int getAssessedPrice(L1ShopItem item) {
        return (int) ((((double) item.getPrice()) * ConfigRate.RATE_SHOP_PURCHASING_PRICE) / ((double) item.getPackCount()));
    }

    public List<L1AssessedItem> assessItems(L1PcInventory inv) {
        List<L1AssessedItem> result = new ArrayList<>();
        for (L1ShopItem item : this._purchasingItems) {
            L1ItemInstance[] findItemsId = inv.findItemsId(item.getItemId());
            for (L1ItemInstance targetItem : findItemsId) {
                if (isPurchaseableItem(targetItem)) {
                    result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
                }
            }
        }
        return result;
    }

    private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
        int price = orderList.getTotalPriceTaxIncluded();
        if (!RangeInt.includes(price, 0, 2000000000)) {
            pc.sendPackets(new S_ServerMessage(904, "2000000000"));
            return false;
        }
        for (L1ShopBuyOrder order : orderList.getList()) {
            int level = order.getItem().getlevel();
            int itemid = order.getItem().getItemId();
            int DailyBuyingCount = order.getItem().getDailyBuyingCount();
            if (DailyBuyingCount > 0) {
                if (pc.getLevel() < level) {
                    pc.sendPackets(new S_ServerMessage("每日購買限制【" + level + "】級。"));
                    return false;
                }
                int AlreadyBoughtCount = pc.getCount_Quest().get_step(itemid);
                int buyingcount = order.getCount();
                if (AlreadyBoughtCount >= DailyBuyingCount || buyingcount + AlreadyBoughtCount > DailyBuyingCount) {
                    pc.sendPackets(new S_ServerMessage("超過每日限制購買數量上限。"));
                    return false;
                }
                pc.getCount_Quest().set_step(itemid, buyingcount + AlreadyBoughtCount);
            }
        }
        if (!pc.getInventory().checkItem(L1ItemId.ADENA, (long) price)) {
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
            return false;
        }
        if (((double) (orderList.getTotalWeight() + (pc.getInventory().getWeight() * L1SkillId.STATUS_BRAVE))) > pc.getMaxWeight() * 1000.0d) {
            pc.sendPackets(new S_ServerMessage(82));
            return false;
        }
        int totalCount = pc.getInventory().getSize();
        for (L1ShopBuyOrder order2 : orderList.getList()) {
            L1Item temp = order2.getItem().getItem();
            if (!temp.isStackable()) {
                totalCount++;
            } else if (!pc.getInventory().checkItem(temp.getItemId())) {
                totalCount++;
            }
        }
        if (totalCount <= 180) {
            return true;
        }
        pc.sendPackets(new S_ServerMessage(263));
        return false;
    }

    private void payCastleTax(L1ShopBuyOrderList orderList) {
        L1TaxCalculator calc = orderList.getTaxCalculator();
        int price = orderList.getTotalPrice();
        int castleId = L1CastleLocation.getCastleIdByNpcid(this._npcId);
        int castleTax = calc.calcCastleTaxPrice(price);
        int nationalTax = calc.calcNationalTaxPrice(price);
        if (castleId == 7 || castleId == 8) {
            castleTax += nationalTax;
            nationalTax = 0;
        }
        if (castleId != 0 && castleTax > 0) {
            L1Castle castle = CastleReading.get().getCastleTable(castleId);
            synchronized (castle) {
                castle.setPublicMoney(castle.getPublicMoney() + ((long) castleTax));
                CastleReading.get().updateCastle(castle);
            }
            if (nationalTax > 0) {
                L1Castle aden = CastleReading.get().getCastleTable(7);
                synchronized (aden) {
                    aden.setPublicMoney(aden.getPublicMoney() + ((long) nationalTax));
                    CastleReading.get().updateCastle(aden);
                }
            }
        }
    }

    private void payDiadTax(L1ShopBuyOrderList orderList) {
        int diadTax = orderList.getTaxCalculator().calcDiadTaxPrice(orderList.getTotalPrice());
        if (diadTax > 0) {
            L1Castle castle = CastleReading.get().getCastleTable(8);
            synchronized (castle) {
                castle.setPublicMoney(castle.getPublicMoney() + ((long) diadTax));
                CastleReading.get().updateCastle(castle);
            }
        }
    }

    private void payTownTax(L1ShopBuyOrderList orderList) {
        int town_id;
        int price = orderList.getTotalPrice();
        if (!World.get().isProcessingContributionTotal() && (town_id = L1TownLocation.getTownIdByNpcid(this._npcId)) >= 1 && town_id <= 10) {
            TownReading.get().addSalesMoney(town_id, price);
        }
    }

    private void payTax(L1ShopBuyOrderList orderList) {
        payCastleTax(orderList);
        payTownTax(orderList);
        payDiadTax(orderList);
    }

    private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) throws Exception {
        int priceTax;
        if (orderList != null && (priceTax = orderList.getTotalPriceTaxIncluded()) > 0 && inv.consumeItem(L1ItemId.ADENA, (long) priceTax)) {
            for (L1ShopBuyOrder order : orderList.getList()) {
                int itemId = order.getItem().getItemId();
                long amount = (long) order.getCount();
                int deleteDay = order.getItem().get_remain_time();
                if (amount > 0) {
                    L1ItemInstance item = ItemTable.get().createItem(itemId);
                    if (deleteDay > 0) {
                        Timestamp ts = new Timestamp(System.currentTimeMillis() + ((long) (86400000 * deleteDay)));
                        item.set_time(ts);
                        CharItemsTimeReading.get().addTime(item.getId(), ts);
                    }
                    item.setCount(amount);
                    String npcname = NpcTable.get().getNpcName(this._npcId);
                    String pcinfo = "玩家";
                    if (inv.getOwner().isGm()) {
                        pcinfo = "管理者";
                    }
                    WriteLogTxt.Recording("NPC商人購買紀錄", pcinfo + "：【" + inv.getOwner().getName() + "】NPC：【" + npcname + "】" + "花費：【" + priceTax + "】金幣購入物品：【(+" + item.getEnchantLevel() + ")" + item.getName() + "(" + item.getCount() + ")】");
                    if (this._npcId == 70068 || this._npcId == 70020 || this._npcId == 70056) {
                        item.setIdentified(false);
                        Random random = new Random();
                        int chance = random.nextInt(100) + 1;
                        if (chance <= 15) {
                            item.setEnchantLevel(-2);
                        } else if (chance >= 16 && chance <= 30) {
                            item.setEnchantLevel(-1);
                        } else if (chance >= 31 && chance <= 70) {
                            item.setEnchantLevel(0);
                        } else if (chance >= 71 && chance <= 87) {
                            item.setEnchantLevel(random.nextInt(2) + 1);
                        } else if (chance >= 88 && chance <= 97) {
                            item.setEnchantLevel(random.nextInt(3) + 3);
                        } else if (chance >= 98 && chance <= 99) {
                            item.setEnchantLevel(6);
                        } else if (chance == 100) {
                            item.setEnchantLevel(7);
                        }
                    } else if (order.getItem().getEnchantLevel() != 0) {
                        item.setEnchantLevel(order.getItem().getEnchantLevel());
                    }
                    item.setIdentified(true);
                    inv.storeItem(item);
                }
            }
        }
    }

    public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) throws Exception {
        if (!orderList.isEmpty() && ensureSell(pc, orderList)) {
            sellItems(pc.getInventory(), orderList);
            payTax(orderList);
        }
    }

    public void buyItems(L1ShopSellOrderList orderList) throws Exception {
        L1PcInventory inv = orderList.getPc().getInventory();
        int totalPrice = 0;
        for (L1ShopSellOrder order : orderList.getList()) {
            totalPrice = (int) (((long) totalPrice) + (((long) order.getItem().getAssessedPrice()) * inv.removeItem(order.getItem().getTargetId(), (long) order.getCount())));
        }
        int totalPrice2 = RangeInt.ensure(totalPrice, 0, 2000000000);
        if (totalPrice2 > 0) {
            inv.storeItem(L1ItemId.ADENA, (long) totalPrice2);
        }
    }

    public L1ShopBuyOrderList newBuyOrderList() {
        return new L1ShopBuyOrderList(this);
    }

    public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
        return new L1ShopSellOrderList(this, pc);
    }
}
