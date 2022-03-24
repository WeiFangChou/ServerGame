package com.lineage.server.templates;

import com.lineage.DatabaseFactory;
import com.lineage.data.event.GamblingSet;
import com.lineage.data.event.gambling.GamblingNpc;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.datatables.lock.GamblingReading;
import com.lineage.server.datatables.lock.ServerCnInfoReading;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PcOtherList {
    private static final Log _log = LogFactory.getLog(L1PcOtherList.class);
    public ArrayList<Integer> ATKNPC = new ArrayList<>();
    public Map<Integer, L1ItemInstance> DELIST = new HashMap();
    public Map<Integer, L1Quest> QUESTMAP = new HashMap();
    public Map<Integer, int[]> SHOPLIST = new HashMap();
    public Map<Integer, L1ShopS> SHOPXMAP = new HashMap();
    private Map<Integer, L1ShopItem> _cnList = new HashMap();
    private Map<Integer, L1ItemInstance> _cnSList = new HashMap();
    private Map<Integer, GamblingNpc> _gamList = new HashMap();
    private Map<Integer, L1Gambling> _gamSellList = new HashMap();
    private Map<Integer, L1IllusoryInstance> _illusoryList = new HashMap();
    private int[] _is;
    private L1PcInstance _pc;
    private Map<Integer, String[]> _shiftingList = new HashMap();
    private Map<Integer, L1ItemInstance> _sitemList = new HashMap();
    private Map<Integer, Integer> _sitemList2 = new HashMap();
    private Map<Integer, L1TeleportLoc> _teleport = new HashMap();
    private Map<Integer, Integer> _uplevelList = new HashMap();

    public L1PcOtherList(L1PcInstance pc) {
        this._pc = pc;
    }

    public void clearAll() {
        try {
            ListMapUtil.clear(this.DELIST);
            ListMapUtil.clear(this._cnList);
            ListMapUtil.clear(this._cnSList);
            ListMapUtil.clear(this._gamList);
            ListMapUtil.clear(this._gamSellList);
            ListMapUtil.clear(this._illusoryList);
            ListMapUtil.clear(this._teleport);
            ListMapUtil.clear(this._uplevelList);
            ListMapUtil.clear(this._shiftingList);
            ListMapUtil.clear(this._sitemList);
            ListMapUtil.clear(this._sitemList2);
            ListMapUtil.clear(this.QUESTMAP);
            ListMapUtil.clear(this.SHOPXMAP);
            ListMapUtil.clear((ArrayList<?>) this.ATKNPC);
            ListMapUtil.clear(this.SHOPLIST);
            this.DELIST = null;
            this._cnList = null;
            this._cnSList = null;
            this._gamList = null;
            this._gamSellList = null;
            this._illusoryList = null;
            this._teleport = null;
            this._uplevelList = null;
            this._shiftingList = null;
            this._sitemList = null;
            this._sitemList2 = null;
            this.QUESTMAP = null;
            this.SHOPXMAP = null;
            this.ATKNPC = null;
            this.SHOPLIST = null;
            this._is = null;
            this._pc = null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public Map<Integer, Integer> get_sitemList2() {
        return this._sitemList2;
    }

    public void add_sitemList2(Integer key, Integer value) {
        this._sitemList2.put(key, value);
    }

    public void clear_sitemList2() {
        this._sitemList2.clear();
    }

    public Map<Integer, L1ItemInstance> get_sitemList() {
        return this._sitemList;
    }

    public void add_sitemList(Integer key, L1ItemInstance value) {
        this._sitemList.put(key, value);
    }

    public void clear_sitemList() {
        this._sitemList.clear();
    }

    public Map<Integer, String[]> get_shiftingList() {
        return this._shiftingList;
    }

    public void add_shiftingList(Integer key, String[] value) {
        this._shiftingList.put(key, value);
    }

    public void remove_shiftingList(Integer key) {
        this._shiftingList.remove(key);
    }

    public void set_shiftingList() {
        try {
            this._shiftingList.clear();
            Connection conn = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                conn = DatabaseFactory.get().getConnection();
                pstm = conn.prepareStatement("SELECT * FROM `characters` WHERE `account_name`=?");
                pstm.setString(1, this._pc.getAccountName());
                rs = pstm.executeQuery();
                int key = 0;
                while (rs.next()) {
                    int objid = rs.getInt("objid");
                    String name = rs.getString("char_name");
                    if (!name.equalsIgnoreCase(this._pc.getName())) {
                        key++;
                        add_shiftingList(Integer.valueOf(key), new String[]{String.valueOf(objid), name});
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(conn);
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    public Map<Integer, L1IllusoryInstance> get_illusoryList() {
        return this._illusoryList;
    }

    public void addIllusoryList(Integer key, L1IllusoryInstance value) {
        this._illusoryList.put(key, value);
    }

    public void removeIllusoryList(Integer key) {
        try {
            if (this._illusoryList != null && this._illusoryList.get(key) != null) {
                this._illusoryList.remove(key);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void teleport(HashMap<Integer, L1TeleportLoc> teleportMap) {
        try {
            ListMapUtil.clear(this._teleport);
            this._teleport.putAll(teleportMap);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public Map<Integer, L1TeleportLoc> teleportMap() {
        return this._teleport;
    }

    public void sellall(Map<Integer, Integer> sellallMap) {
        int getprice = 0;
        int totalprice = 0;
        try {
            for (Integer integer : sellallMap.keySet()) {
                L1ItemInstance item = this._pc.getInventory().getItem(integer.intValue());
                if (item != null && item.getBless() < 128) {
                    int price = ShopTable.get().getPrice(item.getItemId());
                    Integer count = sellallMap.get(integer);
                    if (price < 200000 || count.intValue() <= 9999) {
                        totalprice += count.intValue() * price;
                        if (!RangeInt.includes(totalprice, 0, 2000000000)) {
                            this._pc.sendPackets(new S_SystemMessage("總共販賣價格無法超過 20億金幣。"));
                            return;
                        } else if (this._pc.getInventory().removeItem(integer.intValue(), (long) count.intValue()) == ((long) count.intValue())) {
                            getprice += count.intValue() * price;
                            String pcinfo = "玩家";
                            if (this._pc.isGm()) {
                                pcinfo = "管理者";
                            }
                            WriteLogTxt.Recording("NPC商人賣出紀錄", "IP：(" + ((Object) this._pc.getNetConnection().getIp()) + ")帳號：【" + this._pc.getAccountName() + "】" + pcinfo + "：【" + this._pc.getName() + "】賣出物品：【(+" + item.getEnchantLevel() + ")" + item.getName() + "(" + count + ")】獲得金幣：【" + getprice + "】");
                        }
                    } else {
                        this._pc.sendPackets(new S_SystemMessage("販賣數量無法超過 9999個。"));
                        return;
                    }
                }
            }
            if (getprice > 0) {
                L1ItemInstance item2 = ItemTable.get().createItem(L1ItemId.ADENA);
                item2.setCount((long) getprice);
                this._pc.getInventory().storeItem(item2);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void clear() {
        try {
            ListMapUtil.clear(this._cnList);
            ListMapUtil.clear(this._gamList);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void set_gamSellList(Map<Integer, L1Gambling> sellList) {
        try {
            ListMapUtil.clear(this._gamSellList);
            this._gamSellList.putAll(sellList);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void get_sellGam(int objid, int count) {
        int outcount;
        try {
            L1Gambling element = this._gamSellList.get(Integer.valueOf(objid));
            if (element != null) {
                long countx = ((long) (element.get_rate() * ((double) GamblingSet.GAMADENA))) * ((long) count);
                if (this._pc.getInventory().removeItem(objid, (long) count) == ((long) count) && (outcount = element.get_outcount() - count) >= 0) {
                    element.set_outcount(outcount);
                    GamblingReading.get().updateGambling(element.get_id(), outcount);
                    L1ItemInstance item = ItemTable.get().createItem(GamblingSet.ADENAITEM);
                    item.setCount(countx);
                    createNewItem(item);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void add_gamList(GamblingNpc element, int index) {
        this._gamList.put(new Integer(index), element);
    }

    public void get_buyGam(Map<Integer, Integer> gamMap) {
        try {
            for (Integer integer : gamMap.keySet()) {
                get_gamItem(integer.intValue(), gamMap.get(integer).intValue());
            }
            ListMapUtil.clear(this._gamList);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void get_gamItem(int index, int count) {
        if (count > 0) {
            try {
                GamblingNpc element = this._gamList.get(Integer.valueOf(index));
                if (element != null) {
                    int npcid = element.get_npc().getNpcId();
                    int no = GamblingTime.get_gamblingNo();
                    long adena = (long) (GamblingSet.GAMADENA * count);
                    long srcCount = this._pc.getInventory().countItems(GamblingSet.ADENAITEM);
                    if (srcCount >= adena) {
                        L1ItemInstance item = ItemTable.get().createItem(40309);
                        if (this._pc.getInventory().checkAddItem(item, (long) count) == 0) {
                            this._pc.getInventory().consumeItem(GamblingSet.ADENAITEM, adena);
                            item.setCount((long) count);
                            item.setGamNo(String.valueOf(no) + "-" + npcid);
                            createNewItem(item);
                            element.add_adena(adena);
                            return;
                        }
                        this._pc.sendPackets(new S_ServerMessage(270));
                        return;
                    }
                    L1Item item2 = ItemTable.get().getTemplate(GamblingSet.ADENAITEM);
                    this._pc.sendPackets(new S_ServerMessage(337, String.valueOf(item2.getName()) + "(" + (adena - srcCount) + ")"));
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public void add_cnSList(L1ItemInstance shopItem, int index) {
        this._cnSList.put(new Integer(index), shopItem);
    }

    public void get_buyCnS(Map<Integer, Integer> cnMap) {
        try {
            for (Integer integer : cnMap.keySet()) {
                if (cnMap.get(integer).intValue() > 0) {
                    L1ItemInstance element = this._cnSList.get(Integer.valueOf(integer.intValue()));
                    L1ShopS shopS = DwarfShopReading.get().getShopS(element.getId());
                    if (!(element == null || shopS == null || shopS.get_end() != 0 || shopS.get_item() == null)) {
                        if (this._pc.getInventory().checkItemX(44070, (long) shopS.get_adena()) == null) {
                            this._pc.sendPackets(new S_ServerMessage(337, "天寶"));
                        } else {
                            shopS.set_end(1);
                            shopS.set_item(null);
                            DwarfShopReading.get().updateShopS(shopS);
                            DwarfShopReading.get().deleteItem(element.getId());
                            this._pc.getInventory().consumeItem(44070, (long) shopS.get_adena());
                            this._pc.getInventory().storeTradeItem(element);
                            this._pc.sendPackets(new S_ServerMessage(403, element.getLogName()));
                        }
                    }
                }
            }
            ListMapUtil.clear(this._cnList);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void add_cnList(L1ShopItem shopItem, int index) {
        this._cnList.put(new Integer(index), shopItem);
    }

    public void get_buyCn(Map<Integer, Integer> cnMap) {
        L1ShopItem element;
        try {
            for (Integer integer : cnMap.keySet()) {
                int index = integer.intValue();
                int count = cnMap.get(integer).intValue();
                if (count > 0 && (element = this._cnList.get(Integer.valueOf(index))) != null) {
                    get_cnItem(element, count);
                }
            }
            ListMapUtil.clear(this._cnList);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void get_cnItem(L1ShopItem element, int count) {
        if (this._pc.get_temp_adena() != 0) {
            try {
                int itemid_cn = this._pc.get_temp_adena();
                int itemid = element.getItemId();
                int getCount = count;
                if (element.getPackCount() > 1) {
                    getCount = element.getPackCount() * count;
                }
                int enchantlevel = element.getEnchantLevel();
                int adenaCount = element.getPrice() * count;
                if (this._pc.getInventory().checkAddItem(element.getItem(), (long) getCount) == 0) {
                    long srcCount = this._pc.getInventory().countItems(itemid_cn);
                    if (srcCount < ((long) adenaCount) || !this._pc.getInventory().consumeItem(itemid_cn, (long) adenaCount)) {
                        this._pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(itemid_cn).getName()) + "(" + (((long) adenaCount) - srcCount) + ")"));
                        return;
                    }
                    L1Item itemtmp = ItemTable.get().getTemplate(itemid);
                    String pcinfo = "玩家";
                    if (this._pc.isGm()) {
                        pcinfo = "管理者";
                    }
                    WriteLogTxt.Recording("購入商城紀錄", "IP：(" + ((Object) this._pc.getNetConnection().getIp()) + ")帳號：【" + this._pc.getAccountName() + "】" + pcinfo + "：【" + this._pc.getName() + "】購買物品：【(+" + enchantlevel + ")" + itemtmp.getName() + "(" + count + ")】消費金額：【" + adenaCount + "】");
                    toGmMsg(itemtmp, adenaCount);
                    if (itemtmp.isStackable()) {
                        L1ItemInstance item = ItemTable.get().createItem(itemid);
                        item.setCount((long) getCount);
                        item.setEnchantLevel(enchantlevel);
                        item.setIdentified(true);
                        createNewItem(item);
                        return;
                    }
                    for (int i = 0; i < getCount; i++) {
                        L1ItemInstance item2 = ItemTable.get().createItem(itemid);
                        item2.setEnchantLevel(enchantlevel);
                        item2.setIdentified(true);
                        createNewItem(item2);
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void toGmMsg(L1Item itemtmp, int adenaCount) {
        try {
            ServerCnInfoReading.get().create(this._pc, itemtmp, adenaCount);
            for (L1PcInstance tgpc : World.get().getAllPlayers()) {
                if (tgpc.isGm()) {
                    StringBuilder topc = new StringBuilder();
                    topc.append("人物:" + this._pc.getName() + " 買入:" + itemtmp.getName() + " 花費:" + adenaCount);
                    tgpc.sendPackets(new S_ServerMessage(166, topc.toString()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void createNewItem(L1ItemInstance item) {
        try {
            this._pc.getInventory().storeItem(item);
            this._pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void add_levelList(int key, int value) {
        this._uplevelList.put(Integer.valueOf(key), Integer.valueOf(value));
    }

    public Map<Integer, Integer> get_uplevelList() {
        return this._uplevelList;
    }

    public Integer get_uplevelList(int key) {
        return this._uplevelList.get(Integer.valueOf(key));
    }

    public void clear_uplevelList() {
        ListMapUtil.clear(this._uplevelList);
    }

    public void set_newPcOriginal(int[] is) {
        this._is = is;
    }

    public int[] get_newPcOriginal() {
        return this._is;
    }
}
