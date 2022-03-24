package com.lineage.server.clientpackets;

import com.lineage.data.event.ShopXSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.Shutdown;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.ItemUpdateTable;
import com.lineage.server.datatables.ShopTable;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.DwarfForClanReading;
import com.lineage.server.datatables.lock.DwarfForElfReading;
import com.lineage.server.datatables.lock.DwarfReading;
import com.lineage.server.datatables.lock.OtherUserBuyReading;
import com.lineage.server.datatables.lock.OtherUserSellReading;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1CnInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.shop.L1Shop;
import com.lineage.server.model.shop.L1ShopBuyOrderList;
import com.lineage.server.model.shop.L1ShopSellOrderList;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_CnsSell;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1ItemUpdate;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.timecontroller.event.GamblingTime;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Result extends ClientBasePacket {
    public static final Log _log = LogFactory.getLog(C_Result.class);
    public static final Random _random = new Random();

    public C_Result() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            try {
                this.read(decrypt);
                L1PcInstance pc = client.getActiveChar();
                if (pc.isGhost()) {
                    return;
                }

                if (pc.isDead()) {
                    return;
                }

                if (pc.isTeleport()) {
                    return;
                }

                if (pc.isPrivateShop()) {
                    return;
                }

                if (Shutdown.SHUTDOWN) {
                    pc.sendPackets(new S_SystemMessage("目前服務器準備關機狀態，無法使用交易功能。"));
                    return;
                }

                int npcObjectId = this.readD();
                int resultType = this.readC();
                int size = this.readC();
                int unknown = this.readC();
                int npcId = 0;
                boolean isPrivateShop = false;
                L1Object findObject = World.get().findObject(npcObjectId);
                if (findObject != null) {
                    boolean isStop = false;
                    if (findObject instanceof L1NpcInstance) {
                        L1NpcInstance targetNpc = (L1NpcInstance)findObject;
                        npcId = targetNpc.getNpcTemplate().get_npcId();
                        isStop = true;
                    } else if (findObject instanceof L1PcInstance) {
                        isPrivateShop = true;
                        isStop = true;
                    }

                    if (isStop) {
                        int diffLocX = Math.abs(pc.getX() - findObject.getX());
                        int diffLocY = Math.abs(pc.getY() - findObject.getY());
                        if (diffLocX > 5 || diffLocY > 5) {
                            return;
                        }
                    }
                }

                int level;
                L1PcInstance targetPc;
                switch(resultType) {
                    case 0:
                        if (size <= 0) {
                            return;
                        }

                        if (findObject instanceof L1MerchantInstance) {
                            switch(npcId) {
                                case 70535:
                                    this.mode_shopS(pc, size);
                                    return;
                                default:
                                    this.mode_buy(pc, npcId, size);
                                    return;
                            }
                        }

                        if (!(findObject instanceof L1GamblingInstance)) {
                            if (findObject instanceof L1CnInstance) {
                                this.mode_cn(pc, size, true);
                                return;
                            }

                            if (pc.equals(findObject)) {
                                this.mode_cn(pc, size, true);
                                return;
                            }

                            if (findObject instanceof L1PcInstance && isPrivateShop) {
                                targetPc = (L1PcInstance)findObject;
                                this.mode_buypc(pc, targetPc, size);
                                return;
                            }

                            return;
                        }

                        this.mode_gambling(pc, npcId, size, true);
                        return;
                    case 1:
                        if (size > 0) {
                            if (findObject instanceof L1MerchantInstance) {
                                switch(npcId) {
                                    case 99999:
                                        this.mode_sellall(pc, size);
                                        return;
                                    default:
                                        this.mode_sell(pc, npcId, size);
                                        return;
                                }
                            }

                            if (findObject instanceof L1GamblingInstance) {
                                this.mode_gambling(pc, npcId, size, false);
                                return;
                            }

                            if (findObject instanceof L1PcInstance && isPrivateShop) {
                                targetPc = (L1PcInstance)findObject;
                                this.mode_sellpc(pc, targetPc, size);
                                return;
                            }
                        }

                        return;
                    case 2:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_in(pc, npcId, size);
                                return;
                            }
                        }

                        return;
                    case 3:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_out(pc, npcId, size);
                                return;
                            }
                        }

                        return;
                    case 4:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_clan_in(pc, npcId, size);
                                return;
                            }
                        }

                        return;
                    case 5:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5) {
                                this.mode_warehouse_clan_out(pc, npcId, size);
                            } else {
                                L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                                if (clan != null) {
                                    clan.setWarehouseUsingChar(0);
                                    return;
                                }
                            }

                            return;
                        }
                        break;
                    case 6:
                    case 7:
                    case 11:
                    default:
                        return;
                    case 8:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5 && pc.isElf()) {
                                this.mode_warehouse_elf_in(pc, npcId, size);
                                return;
                            }
                        }

                        return;
                    case 9:
                        if (size > 0 && findObject instanceof L1DwarfInstance) {
                            level = pc.getLevel();
                            if (level >= 5 && pc.isElf()) {
                                this.mode_warehouse_elf_out(pc, npcId, size);
                                return;
                            }
                        }

                        return;
                    case 10:
                        if (size > 0) {
                            switch(npcId) {
                                case 91141:
                                case 91142:
                                case 91143:
                                    this.mode_update_item(pc, size, npcObjectId);
                            }
                        }

                        return;
                    case 12:
                        if (size > 0) {
                            switch(npcId) {
                                case 70535:
                                    this.mode_shop_item(pc, size, npcObjectId);
                                    return;
                            }
                        }
                }
            } catch (Exception var17) {
            }

        } finally {
            this.over();
        }
    }

    private void mode_update_item(L1PcInstance pc, int size, int npcObjectId) {
        try {
            if (size != 1) {
                pc.sendPackets(new S_ServerMessage("\\fR你只能選取一樣裝備用來升級。"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            int orderId = this.readD();
            int count = Math.max(0, this.readD());
            if (count != 1) {
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            L1ItemInstance item = pc.getInventory().getItem(orderId);
            ArrayList<L1ItemUpdate> items = ItemUpdateTable.get().get(item.getItemId());
            String[] names = new String[items.size()];

            for(int index = 0; index < items.size(); ++index) {
                int toid = ((L1ItemUpdate)items.get(index)).get_toid();
                L1Item tgitem = ItemTable.get().getTemplate(toid);
                if (tgitem != null) {
                    names[index] = tgitem.getName();
                }
            }

            pc.set_mode_id(orderId);
            pc.sendPackets(new S_NPCTalkReturn(npcObjectId, "y_up_i1", names));
        } catch (Exception var12) {
            _log.error("升級裝備物品數據異常: " + pc.getName());
        }

    }

    private void mode_shopS(L1PcInstance pc, int size) {
        try {
            Map<Integer, Integer> sellScoreMapMap = new HashMap();

            for(int i = 0; i < size; ++i) {
                int orderId = this.readD();
                int count = Math.max(0, this.readD());
                if (count <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    sellScoreMapMap.put(new Integer(orderId), new Integer(count));
                }
            }

            pc.get_otherList().get_buyCnS(sellScoreMapMap);
        } catch (Exception var7) {
            _log.error("購買人物託售物品數據異常: " + pc.getName());
        }

    }

    private void mode_shop_item(L1PcInstance pc, int size, int npcObjectId) {
        try {
            if (size == 1) {
                int objid = this.readD();
                L1Object object = pc.getInventory().getItem(objid);
                boolean isError = false;
                if (object instanceof L1ItemInstance) {
                    L1ItemInstance item = (L1ItemInstance)object;
                    if (item.isEquipped()) {
                        isError = true;
                    }

                    if (!item.isIdentified()) {
                        isError = true;
                    }

                    if (item.getItem().getMaxUseTime() != 0) {
                        isError = true;
                    }

                    if (item.get_time() != null) {
                        isError = true;
                    }

                    if (ShopXTable.get().getTemplate(item.getItem().getItemId()) != null) {
                        isError = true;
                    }

                    Object[] petlist = pc.getPetList().values().toArray();
                    Object[] var12 = petlist;
                    int var11 = petlist.length;

                    for(int var10 = 0; var10 < var11; ++var10) {
                        Object petObject = var12[var10];
                        if (petObject instanceof L1PetInstance) {
                            L1PetInstance pet = (L1PetInstance)petObject;
                            if (item.getId() == pet.getItemObjId()) {
                                isError = true;
                            }
                        }
                    }

                    if (pc.getDoll(item.getId()) != null) {
                        isError = true;
                    }

                    if (item.getGamNo() != null) {
                        isError = true;
                    }

                    if (item.getEnchantLevel() < 0) {
                        isError = true;
                    }

                    if (item.getItem().getMaxChargeCount() != 0 && item.getChargeCount() <= 0) {
                        isError = true;
                    }

                    if (isError) {
                        pc.sendPackets(new S_NPCTalkReturn(npcObjectId, "y_x_e1"));
                    } else {
                        L1ItemInstance itemT = pc.getInventory().checkItemX(44070, (long)ShopXSet.ADENA);
                        if (itemT == null) {
                            pc.sendPackets(new S_ServerMessage(337, "天寶"));
                            pc.sendPackets(new S_CloseList(pc.getId()));
                            return;
                        }

                        pc.get_other().set_item(item);
                        pc.sendPackets(new S_CnsSell(npcObjectId, "y_x_3", "ma"));
                    }
                }
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npcObjectId, "y_x_e"));
            }
        } catch (Exception var14) {
            _log.error("人物託售物品數據異常: " + pc.getName());
        }

    }

    private void mode_sellall(L1PcInstance pc, int size) {
        try {
            Map<Integer, Integer> sellallMap = new HashMap();

            for(int i = 0; i < size; ++i) {
                int objid = this.readD();
                int count = Math.max(0, this.readD());
                if (count <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    sellallMap.put(new Integer(objid), new Integer(count));
                }
            }

            pc.get_otherList().sellall(sellallMap);
        } catch (Exception var7) {
            _log.error("回收商人/買入玩家物品數據異常: " + pc.getName());
        }

    }

    private void mode_cn(L1PcInstance pc, int size, boolean isShop) {
        try {
            if (isShop) {
                Map<Integer, Integer> cnMap = new HashMap();

                for(int i = 0; i < size; ++i) {
                    int orderId = this.readD();
                    int count = Math.max(0, this.readD());
                    if (count <= 0) {
                        _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                    } else {
                        cnMap.put(new Integer(orderId), new Integer(count));
                    }
                }

                pc.get_otherList().get_buyCn(cnMap);
            }
        } catch (Exception var8) {
            _log.error("奇怪的商人買入物品數據異常: " + pc.getName());
        }

    }

    private void mode_gambling(L1PcInstance pc, int npcId, int size, boolean isShop) {
        int i;
        int orderId;
        if (isShop) {
            if (GamblingTime.isStart()) {
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }

            Map<Integer, Integer> gamMap = new HashMap();

            for(i = 0; i < size; ++i) {
                orderId = this.readD();
                int count = Math.max(0, this.readD());
                if (count <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    gamMap.put(new Integer(orderId), new Integer(count));
                }
            }

            pc.get_otherList().get_buyGam(gamMap);
        } else {
            for(i = 0; i < size; ++i) {
                i = this.readD();
                orderId = Math.max(0, this.readD());
                if (orderId <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    pc.get_otherList().get_sellGam(i, orderId);
                }
            }
        }

    }

    private void mode_warehouse_elf_out(L1PcInstance pc, int npcId, int size) throws Exception {
        int i = 0;

        while(i < size) {
            int objectId = this.readD();
            int count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求精靈倉庫取出傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                break;
            }

            L1ItemInstance item = pc.getDwarfForElfInventory().getItem(objectId);
            if (item == null) {
                _log.error("精靈倉庫取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (!DwarfForElfReading.get().getUserItems(pc.getAccountName(), objectId, count)) {
                _log.error("精靈倉庫取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (pc.getInventory().checkAddItem(item, (long)count) == 0) {
                if (pc.getInventory().consumeItem(40494, 2L)) {
                    pc.getDwarfForElfInventory().tradeItem(item, (long)count, pc.getInventory());
                    WriteLogTxt.Recording("精靈倉庫取出記錄", "帳號:" + pc.getAccountName() + " 取出血盟倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
                    ++i;
                    continue;
                }

                pc.sendPackets(new S_ServerMessage(337, "$767"));
                break;
            }

            pc.sendPackets(new S_ServerMessage(270));
            break;
        }

    }

    private void mode_warehouse_elf_in(L1PcInstance pc, int npcId, int size) throws Exception {
        for(int i = 0; i < size; ++i) {
            int objectId = this.readD();
            int count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求精靈倉庫存入傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                break;
            }

            L1Object object = pc.getInventory().getItem(objectId);
            if (object == null) {
                _log.error("人物背包資料取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (!CharItemsReading.get().getUserItems(pc.getId(), objectId, (long)count)) {
                _log.error("人物背包資料取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            L1ItemInstance item = (L1ItemInstance)object;
            if (!item.getItem().isTradable()) {
                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                break;
            }

            if (item.get_time() != null) {
                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                break;
            }

            Object[] petlist = pc.getPetList().values().toArray();
            Object[] var13 = petlist;
            int var12 = petlist.length;

            for(int var11 = 0; var11 < var12; ++var11) {
                Object petObject = var13[var11];
                if (petObject instanceof L1PetInstance) {
                    L1PetInstance pet = (L1PetInstance)petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        break;
                    }
                }
            }

            if (pc.getDoll(item.getId()) != null) {
                pc.sendPackets(new S_ServerMessage(1181));
                break;
            }

            if (pc.getDwarfForElfInventory().checkAddItemToWarehouse(item, (long)count, 0) == 1) {
                pc.sendPackets(new S_ServerMessage(75));
                break;
            }

            pc.getInventory().tradeItem(objectId, (long)count, pc.getDwarfForElfInventory());
            WriteLogTxt.Recording("精靈倉庫存入記錄", "帳號:" + pc.getAccountName() + " 角色" + pc.getName() + "取出精靈倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
        }

    }

    private void mode_warehouse_clan_out(L1PcInstance pc, int npcId, int size) {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());

        try {
            if (clan != null) {
                for(int i = 0; i < size; ++i) {
                    int objectId = this.readD();
                    int count = Math.max(0, this.readD());
                    if (count <= 0) {
                        _log.error("要求血盟倉庫取出傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                        break;
                    }

                    L1ItemInstance item = clan.getDwarfForClanInventory().getItem(objectId);
                    if (item == null) {
                        _log.error("血盟倉庫取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                        break;
                    }

                    if (!DwarfForClanReading.get().getUserItems(pc.getClanname(), objectId, count)) {
                        _log.error("血盟倉庫取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                        break;
                    }

                    if (pc.getInventory().checkAddItem(item, (long)count) != 0) {
                        pc.sendPackets(new S_ServerMessage(270));
                        break;
                    }

                    if (!pc.getInventory().consumeItem(40308, 30L)) {
                        pc.sendPackets(new S_ServerMessage(189));
                        break;
                    }

                    clan.getDwarfForClanInventory().tradeItem(item, (long)count, pc.getInventory());
                    Iterator var10 = World.get().getAllPlayers().iterator();

                    while(var10.hasNext()) {
                        L1PcInstance tgpc = (L1PcInstance)var10.next();
                        if (tgpc.getClanid() != 0 && tgpc.getClanid() == pc.getClanid()) {
                            tgpc.sendPackets(new S_SystemMessage("血盟成員:" + pc.getName() + " 從血盟倉庫取出:" + item.getNumberedName_to_String() + " 數量:" + count));
                        }
                    }

                    WriteLogTxt.Recording("血盟倉庫取出記錄", "帳號:" + pc.getAccountName() + " 角色" + pc.getName() + " 取出精靈倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
                }
            }
        } catch (Exception var14) {
            _log.error(var14.getLocalizedMessage(), var14);
        } finally {
            if (clan != null) {
                clan.setWarehouseUsingChar(0);
            }

        }

    }

    private void mode_warehouse_clan_in(L1PcInstance pc, int npcId, int size) {
        try {
            if (pc.getClanid() != 0) {
                for(int i = 0; i < size; ++i) {
                    int objectId = this.readD();
                    int count = Math.max(0, this.readD());
                    if (count <= 0) {
                        _log.error("要求血盟倉庫存入傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                        break;
                    }

                    L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                    L1Object object = pc.getInventory().getItem(objectId);
                    if (object == null) {
                        _log.error("人物背包資料取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                        break;
                    }

                    if (!CharItemsReading.get().getUserItems(pc.getId(), objectId, (long)count)) {
                        _log.error("人物背包資料取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                        break;
                    }

                    L1ItemInstance item = (L1ItemInstance)object;
                    if (!item.getItem().isTradable()) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        break;
                    }

                    if (item.get_time() != null) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        break;
                    }

                    if (ItemRestrictionsTable.RESTRICTIONS.contains(item.getItemId())) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        break;
                    }

                    Object[] petlist = pc.getPetList().values().toArray();
                    Object[] var14 = petlist;
                    int var13 = petlist.length;

                    for(int var12 = 0; var12 < var13; ++var12) {
                        Object petObject = var14[var12];
                        if (petObject instanceof L1PetInstance) {
                            L1PetInstance pet = (L1PetInstance)petObject;
                            if (item.getId() == pet.getItemObjId()) {
                                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                                break;
                            }
                        }
                    }

                    if (pc.getDoll(item.getId()) != null) {
                        pc.sendPackets(new S_ServerMessage(1181));
                        break;
                    }

                    if (clan != null) {
                        if (clan.getDwarfForClanInventory().checkAddItemToWarehouse(item, (long)count, 1) == 1) {
                            pc.sendPackets(new S_ServerMessage(75));
                            break;
                        }

                        pc.getInventory().tradeItem(objectId, (long)count, clan.getDwarfForClanInventory());
                        Iterator var18 = World.get().getAllPlayers().iterator();

                        while(var18.hasNext()) {
                            L1PcInstance tgpc = (L1PcInstance)var18.next();
                            if (tgpc.getClanid() != 0 && tgpc.getClanid() == pc.getClanid()) {
                                tgpc.sendPackets(new S_SystemMessage("血盟成員:" + pc.getName() + " 存入血盟倉庫:" + item.getNumberedName_to_String() + " 數量:" + count));
                            }
                        }

                        WriteLogTxt.Recording("血盟倉庫存入記錄", "帳號:" + pc.getAccountName() + " 角色" + pc.getName() + "存入血盟倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
                        if (clan != null) {
                            clan.setWarehouseUsingChar(0);
                        }
                    }
                }
            } else {
                pc.sendPackets(new S_ServerMessage(208));
            }
        } catch (Exception var16) {
            _log.error(var16.getLocalizedMessage(), var16);
        }

    }

    private void mode_warehouse_out(L1PcInstance pc, int npcId, int size) throws Exception {
        int i = 0;

        while(i < size) {
            int objectId = this.readD();
            int count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求個人倉庫取出傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                break;
            }

            L1ItemInstance item = pc.getDwarfInventory().getItem(objectId);
            if (item == null) {
                _log.error("個人倉庫取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (!DwarfReading.get().getUserItems(pc.getAccountName(), objectId, count)) {
                _log.error("個人倉庫取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (pc.getInventory().checkAddItem(item, (long)count) == 0) {
                if (pc.getInventory().consumeItem(40308, 30L)) {
                    pc.getDwarfInventory().tradeItem(item, (long)count, pc.getInventory());
                    WriteLogTxt.Recording("個人倉庫取出記錄", "帳號:" + pc.getAccountName() + " 角色" + pc.getName() + "取出倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
                    ++i;
                    continue;
                }

                pc.sendPackets(new S_ServerMessage(189));
                break;
            }

            pc.sendPackets(new S_ServerMessage(270));
            break;
        }

    }

    private void mode_warehouse_in(L1PcInstance pc, int npcId, int size) throws Exception {
        for(int i = 0; i < size; ++i) {
            int objectId = this.readD();
            int count = Math.max(0, this.readD());
            if (count <= 0) {
                _log.error("要求個人倉庫存入傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                break;
            }

            L1Object object = pc.getInventory().getItem(objectId);
            if (object == null) {
                _log.error("人物背包資料取出數據異常(物品為空): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            if (!CharItemsReading.get().getUserItems(pc.getId(), objectId, (long)count)) {
                _log.error("人物背包資料取出數據異常(該倉庫指定數據有誤): " + pc.getName() + "/" + pc.getNetConnection().hashCode());
                break;
            }

            L1ItemInstance item = (L1ItemInstance)object;
            if (item.getCount() <= 0L) {
                break;
            }

            if (!item.getItem().isTradable()) {
                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                break;
            }

            if (item.get_time() != null) {
                pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                break;
            }

            Object[] petlist = pc.getPetList().values().toArray();
            Object[] var13 = petlist;
            int var12 = petlist.length;

            for(int var11 = 0; var11 < var12; ++var11) {
                Object petObject = var13[var11];
                if (petObject instanceof L1PetInstance) {
                    L1PetInstance pet = (L1PetInstance)petObject;
                    if (item.getId() == pet.getItemObjId()) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        break;
                    }
                }
            }

            if (pc.getDoll(item.getId()) != null) {
                pc.sendPackets(new S_ServerMessage(1181));
                break;
            }

            if (pc.getDwarfInventory().checkAddItemToWarehouse(item, (long)count, 0) == 1) {
                pc.sendPackets(new S_ServerMessage(75));
                break;
            }

            pc.getInventory().tradeItem(objectId, (long)count, pc.getDwarfInventory());
            WriteLogTxt.Recording("個人倉庫存入記錄", "帳號:" + pc.getAccountName() + " 角色" + pc.getName() + "存入倉庫數據:+" + item.getEnchantLevel() + " " + item.getItem().getName() + "(" + item.getCount() + ")" + " OBJID:" + item.getId());
        }

    }

    private void mode_sellpc(L1PcInstance pc, L1PcInstance targetPc, int size) throws Exception {
        boolean[] isRemoveFromList = new boolean[8];
        if (!targetPc.isTradingInPrivateShop()) {
            targetPc.setTradingInPrivateShop(true);
            ArrayList<L1PrivateShopBuyList> buyList = targetPc.getBuyList();
            int i = 0;

            while(true) {
                label98: {
                    if (i < size) {
                        int itemObjectId = this.readD();
                        int count = this.readCH();
                        count = Math.max(0, count);
                        if (count <= 0) {
                            _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                        } else {
                            int order = this.readC();
                            L1ItemInstance item = pc.getInventory().getItem(itemObjectId);
                            if (item == null || item.get_time() != null) {
                                break label98;
                            }

                            L1PrivateShopBuyList psbl = (L1PrivateShopBuyList)buyList.get(order);
                            int buyItemObjectId = psbl.getItemObjectId();
                            long buyPrice = (long)psbl.getBuyPrice();
                            int buyTotalCount = psbl.getBuyTotalCount();
                            int buyCount = psbl.getBuyCount();
                            if (count > buyTotalCount - buyCount) {
                                count = buyTotalCount - buyCount;
                            }

                            if (item.isEquipped()) {
                                pc.sendPackets(new S_ServerMessage(905));
                                break label98;
                            }

                            L1ItemInstance srcItem = targetPc.getInventory().getItem(buyItemObjectId);
                            if (srcItem.get_time() != null) {
                                break label98;
                            }

                            if (item.getItemId() != srcItem.getItemId() || item.getEnchantLevel() != srcItem.getEnchantLevel()) {
                                _log.error("可能使用bug進行交易 人物名稱(賣出道具給予個人商店/交易條件不吻合): " + pc.getName() + " objid:" + pc.getId());
                                return;
                            }

                            if (targetPc.getInventory().checkAddItem(item, (long)count) == 0) {
                                for(int j = 0; j < count; ++j) {
                                    if (buyPrice * (long)j > 2000000000L) {
                                        targetPc.sendPackets(new S_ServerMessage(904, "2000000000"));
                                        return;
                                    }
                                }

                                if (targetPc.getInventory().checkItem(40308, (long)count * buyPrice)) {
                                    L1ItemInstance adena = targetPc.getInventory().findItemId(40308);
                                    if (adena != null) {
                                        if (item.getCount() < (long)count) {
                                            pc.sendPackets(new S_ServerMessage(989));
                                            _log.error("可能使用bug進行交易 人物名稱(賣出道具給予個人商店/交易數量不吻合): " + pc.getName() + " objid:" + pc.getId());
                                        } else {
                                            OtherUserSellReading.get().add(item.getItem().getName(), item.getId(), (int)buyPrice, (long)count, pc.getId(), pc.getName(), targetPc.getId(), targetPc.getName());
                                            targetPc.getInventory().tradeItem(adena, (long)count * buyPrice, pc.getInventory());
                                            pc.getInventory().tradeItem(item, (long)count, targetPc.getInventory());
                                            psbl.setBuyCount(count + buyCount);
                                            buyList.set(order, psbl);
                                            WriteLogTxt.Recording("玩家賣物品記錄", "IP(" + pc.getNetConnection().getIp() + ")" + "帳號：" + pc.getAccountName() + "的角色名稱:【" + pc.getName() + "】 " + "的" + "【+" + item.getEnchantLevel() + " " + item.getName() + "(" + count + ")" + "】" + " 以金幣：" + (long)count * buyPrice + "元 賣給了掛商店中的 IP：" + "(" + targetPc.getNetConnection().getIp() + ")" + "帳號：" + targetPc.getAccountName() + "的角色名稱:【" + targetPc.getName() + "】 ");
                                            if (psbl.getBuyCount() == psbl.getBuyTotalCount()) {
                                                isRemoveFromList[order] = true;
                                            }
                                        }
                                    }
                                    break label98;
                                }

                                targetPc.sendPackets(new S_ServerMessage(189));
                            } else {
                                pc.sendPackets(new S_ServerMessage(271));
                            }
                        }
                    }

                    for(i = 7; i >= 0; --i) {
                        if (isRemoveFromList[i]) {
                            buyList.remove(i);
                        }
                    }

                    targetPc.setTradingInPrivateShop(false);
                    return;
                }

                ++i;
            }
        }
    }

    private void mode_buypc(L1PcInstance pc, L1PcInstance targetPc, int size) throws Exception {
        boolean[] isRemoveFromList = new boolean[8];
        if (!targetPc.isTradingInPrivateShop()) {
            ArrayList<L1PrivateShopSellList> sellList = targetPc.getSellList();
            synchronized(sellList) {
                if (pc.getPartnersPrivateShopItemCount() == sellList.size()) {
                    targetPc.setTradingInPrivateShop(true);

                    int i;
                    for(i = 0; i < size; ++i) {
                        int order = this.readD();
                        int count = Math.max(0, this.readD());
                        if (count <= 0) {
                            _log.error("要求買入個人商店物品傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                            break;
                        }

                        L1PrivateShopSellList pssl = (L1PrivateShopSellList)sellList.get(order);
                        int itemObjectId = pssl.getItemObjectId();
                        int sellPrice = pssl.getSellPrice();
                        int sellTotalCount = pssl.getSellTotalCount();
                        int sellCount = pssl.getSellCount();
                        L1ItemInstance item = targetPc.getInventory().getItem(itemObjectId);
                        if (item != null && item.get_time() == null) {
                            if (count > sellTotalCount - sellCount) {
                                count = sellTotalCount - sellCount;
                            }

                            if (count > 0) {
                                if (pc.getInventory().checkAddItem(item, (long)count) != 0) {
                                    pc.sendPackets(new S_ServerMessage(270));
                                    break;
                                }

                                for(int j = 0; j < count; ++j) {
                                    if (sellPrice * j > 2000000000) {
                                        pc.sendPackets(new S_ServerMessage(904, "2000000000"));
                                        targetPc.setTradingInPrivateShop(false);
                                        return;
                                    }
                                }

                                int price = count * sellPrice;
                                L1ItemInstance adena = pc.getInventory().findItemId(40308);
                                if (adena == null) {
                                    pc.sendPackets(new S_ServerMessage(189));
                                } else if (adena.getCount() < (long)price) {
                                    pc.sendPackets(new S_ServerMessage(189));
                                } else if (targetPc != null) {
                                    if (item.getCount() < (long)count) {
                                        pc.sendPackets(new S_ServerMessage(989));
                                    } else {
                                        OtherUserBuyReading.get().add(item.getItem().getName(), item.getId(), sellPrice, (long)count, pc.getId(), pc.getName(), targetPc.getId(), targetPc.getName());
                                        targetPc.getInventory().tradeItem(item, (long)count, pc.getInventory());
                                        pc.getInventory().tradeItem(adena, (long)price, targetPc.getInventory());
                                        String message = item.getItem().getName() + " (" + count + ")";
                                        targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), message));
                                        pssl.setSellCount(count + sellCount);
                                        sellList.set(order, pssl);
                                        WriteLogTxt.Recording("玩家買物品記錄", "IP(" + targetPc.getNetConnection().getIp() + ")" + "帳號：" + targetPc.getAccountName() + "的角色名稱:【" + targetPc.getName() + "】 " + "的" + "【+" + item.getEnchantLevel() + " " + item.getName() + "(" + count + ")" + "】" + " 在個人商店中以金幣：" + price + "元 賣給了IP：" + "(" + pc.getNetConnection().getIp() + ")" + "帳號：" + pc.getAccountName() + "的角色名稱:【" + pc.getName() + "】 ");
                                        if (pssl.getSellCount() == pssl.getSellTotalCount()) {
                                            isRemoveFromList[order] = true;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for(i = 7; i >= 0; --i) {
                        if (isRemoveFromList[i]) {
                            sellList.remove(i);
                        }
                    }

                    targetPc.setTradingInPrivateShop(false);
                }
            }
        }
    }

    private void mode_sell(L1PcInstance pc, int npcId, int size) throws Exception {
        L1Shop shop = ShopTable.get().get(npcId);
        if (shop != null) {
            L1ShopSellOrderList orderList = shop.newSellOrderList(pc);

            for(int i = 0; i < size; ++i) {
                int objid = this.readD();
                int count = Math.max(0, this.readD());
                if (count <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    orderList.add(objid, count);
                }
            }

            shop.buyItems(orderList);
        } else {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }

    }

    private void mode_buy(L1PcInstance pc, int npcId, int size) throws Exception {
        L1Shop shop = ShopTable.get().get(npcId);
        if (shop != null) {
            L1ShopBuyOrderList orderList = shop.newBuyOrderList();

            for(int i = 0; i < size; ++i) {
                int orderId = this.readD();
                int count = Math.max(0, this.readD());
                if (count <= 0) {
                    _log.error("要求列表物品取得傳回數量小於等於0: " + pc.getName() + ":" + pc.getNetConnection().kick());
                } else {
                    orderList.add(orderId, count);
                }
            }

            shop.sellItems(pc, orderList);
        } else {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
