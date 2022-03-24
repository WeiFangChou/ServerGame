package com.lineage.data.npc.shop;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.event.ShopXSet;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemRestrictionsTable;
import com.lineage.server.datatables.ShopXTable;
import com.lineage.server.datatables.lock.DwarfShopReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_CnSRetrieve;
import com.lineage.server.serverpackets.S_CnSShopSellList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ShopS;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_ShopX extends NpcExecutor {
    private static final int _adenaid = 44070;
    private static final int _count = 200;
    private static final Log _log = LogFactory.getLog(Npc_ShopX.class);

    private Npc_ShopX() {
    }

    public static NpcExecutor get() {
        return new Npc_ShopX();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.get_other().set_item(null);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_1", new String[]{String.valueOf(ShopXSet.ADENA), String.valueOf(ShopXSet.DATE), String.valueOf(ShopXSet.MIN), String.valueOf(ShopXSet.MAX)}));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (cmd.equalsIgnoreCase("s")) {
            cmd_s1(pc, npc);
        } else if (cmd.equalsIgnoreCase("i")) {
            pc.sendPackets(new S_CnSShopSellList(pc, npc));
        } else if (cmd.equalsIgnoreCase("l")) {
            cmd_1(pc, npc);
        } else if (cmd.equalsIgnoreCase("ma")) {
            cmd_ma(pc, npc, amount);
        } else if (cmd.equals("up")) {
            showPage(pc, npc.getId(), pc.get_other().get_page() - 1);
        } else if (cmd.equals("dn")) {
            showPage(pc, npc.getId(), pc.get_other().get_page() + 1);
        } else if (cmd.equals("over")) {
            cmd_over(pc, npc);
        } else if (cmd.equals("no")) {
            pc.setTempID(0);
            pc.sendPackets(new S_CloseList(pc.getId()));
        } else if (cmd.matches("[0-9]+")) {
            update(pc, npc, Integer.parseInt(String.valueOf(pc.get_other().get_page()) + cmd));
        }
    }

    public static void cmd_1(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.get_otherList().SHOPXMAP.clear();
            Map<Integer, L1ShopS> temp = DwarfShopReading.get().getShopSMap(pc.getId());
            if (temp != null) {
                pc.get_otherList().SHOPXMAP.putAll(temp);
                temp.clear();
            }
            showPage(pc, npc.getId(), 0);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void cmd_ma(L1PcInstance pc, L1NpcInstance npc, long amount) {
        try {
            L1ItemInstance itemT = pc.getInventory().checkItemX(_adenaid, (long) ShopXSet.ADENA);
            boolean isError = false;
            if (itemT == null) {
                pc.sendPackets(new S_ServerMessage(337, "天寶"));
                isError = true;
            }
            if (amount < ((long) ShopXSet.MIN)) {
                isError = true;
            }
            if (amount > ((long) ShopXSet.MAX)) {
                isError = true;
            }
            if (isError) {
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.get_other().set_item(null);
                return;
            }
            L1ItemInstance item = pc.get_other().get_item();
            if (item == null) {
                pc.sendPackets(new S_CloseList(pc.getId()));
                return;
            }
            if (item.isEquipped()) {
                isError = true;
            }
            if (!item.isIdentified()) {
                isError = true;
            }
            if (item.getItem().getMaxUseTime() != 0) {
                isError = true;
            }
            if (ShopXTable.get().getTemplate(item.getItem().getItemId()) != null) {
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
                pc.sendPackets(new S_CloseList(pc.getId()));
                pc.get_other().set_item(null);
                return;
            }
            pc.get_other().set_item(null);
            Timestamp overTime = new Timestamp(System.currentTimeMillis() + ((long) (ShopXSet.DATE * 24 * 60 * 60 * L1SkillId.STATUS_BRAVE)));
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_4", new String[]{item.getLogName(), String.valueOf(amount), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(overTime)}));
            L1ShopS shopS = new L1ShopS();
            shopS.set_id(DwarfShopReading.get().nextId());
            shopS.set_item_obj_id(item.getId());
            shopS.set_user_obj_id(pc.getId());
            shopS.set_adena((int) amount);
            shopS.set_overtime(overTime);
            shopS.set_end(0);
            shopS.set_none(item.getNumberedName_to_String());
            shopS.set_item(item);
            pc.getInventory().removeItem(itemT, (long) ShopXSet.ADENA);
            pc.getInventory().removeItem(item);
            DwarfShopReading.get().insertItem(item.getId(), item, shopS);
            try {
                pc.save();
                pc.saveInventory();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    public static void cmd_over(L1PcInstance pc, L1NpcInstance npc) {
        try {
            L1ShopS shopS = DwarfShopReading.get().getShopS(pc.getTempID());
            pc.setTempID(0);
            shopS.set_end(3);
            shopS.set_item(null);
            DwarfShopReading.get().updateShopS(shopS);
            pc.sendPackets(new S_CloseList(pc.getId()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void cmd_s1(L1PcInstance pc, L1NpcInstance npc) {
        try {
            Map<Integer, L1ShopS> allShopS = DwarfShopReading.get().allShopS();
            if (allShopS.size() >= 200) {
                pc.sendPackets(new S_ServerMessage(75));
                pc.sendPackets(new S_CloseList(pc.getId()));
            } else if (pc.getInventory().checkItemX(_adenaid, (long) ShopXSet.ADENA) == null) {
                pc.sendPackets(new S_ServerMessage(337, "天寶"));
                pc.sendPackets(new S_CloseList(pc.getId()));
            } else {
                List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<>();
                for (L1ItemInstance item : pc.getInventory().getItems()) {
                    if (ShopXTable.get().getTemplate(item.getItem().getItemId()) == null && !item.isEquipped() && item.isIdentified() && item.getItem().getMaxUseTime() == 0 && item.get_time() == null && item.getGamNo() == null && item.getEnchantLevel() >= 0) {
                        if ((item.getItem().getMaxChargeCount() == 0 || item.getChargeCount() > 0) && !ItemRestrictionsTable.RESTRICTIONS.contains(Integer.valueOf(item.getItemId()))) {
                            itemsx.add(item);
                        }
                    }
                }
                pc.sendPackets(new S_CnSRetrieve(pc, npc.getId(), itemsx));
                allShopS.clear();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void cmd_s2(L1PcInstance pc, L1NpcInstance npc) {
        try {
            Map<Integer, L1ShopS> allShopS = DwarfShopReading.get().allShopS();
            if (allShopS.size() >= 200) {
                pc.sendPackets(new S_ServerMessage(75));
                pc.sendPackets(new S_CloseList(pc.getId()));
            } else if (pc.getInventory().checkItemX(_adenaid, (long) ShopXSet.ADENA) == null) {
                pc.sendPackets(new S_ServerMessage(337, "天寶"));
                pc.sendPackets(new S_CloseList(pc.getId()));
            } else {
                List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<>();
                for (L1ItemInstance item : pc.getInventory().getItems()) {
                    if (ShopXTable.get().getTemplate(item.getItem().getItemId()) == null && item.getItem().isTradable() && !item.isEquipped() && item.isIdentified() && item.getItem().getMaxUseTime() == 0 && item.get_time() == null && item.getGamNo() == null && item.getEnchantLevel() >= 0) {
                        if ((item.getItem().getMaxChargeCount() == 0 || item.getChargeCount() > 0) && !ItemRestrictionsTable.RESTRICTIONS.contains(Integer.valueOf(item.getItemId()))) {
                            itemsx.add(item);
                        }
                    }
                }
                pc.sendPackets(new S_CnSRetrieve(pc, npc.getId(), itemsx));
                allShopS.clear();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void update(L1PcInstance pc, L1NpcInstance npc, int key) {
        Map<Integer, L1ShopS> list = pc.get_otherList().SHOPXMAP;
        pc.setTempID(0);
        L1ShopS shopS = list.get(Integer.valueOf(key));
        switch (shopS.get_end()) {
            case 0:
                pc.setTempID(shopS.get_item_obj_id());
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_x_5", new String[]{String.valueOf(shopS.get_item().getLogName()) + "(" + shopS.get_item().getCount() + ")", String.valueOf(shopS.get_adena()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(shopS.get_overtime())}));
                break;
            case 1:
                shopS.set_end(2);
                DwarfShopReading.get().updateShopS(shopS);
                CreateNewItem.createNewItem(pc, (int) _adenaid, (long) shopS.get_adena());
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;
            case 2:
                pc.sendPackets(new S_ServerMessage(166, "該託售物品收入已領回"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;
            case 3:
                shopS.set_end(4);
                shopS.set_item(null);
                DwarfShopReading.get().updateShopS(shopS);
                L1ItemInstance item = DwarfShopReading.get().allItems().get(Integer.valueOf(shopS.get_item_obj_id()));
                DwarfShopReading.get().deleteItem(shopS.get_item_obj_id());
                pc.getInventory().storeTradeItem(item);
                pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;
            case 4:
                pc.sendPackets(new S_ServerMessage(166, "該託售物品已領回"));
                pc.sendPackets(new S_CloseList(pc.getId()));
                break;
        }
        pc.get_other().set_item(null);
    }

    public static void showPage(L1PcInstance pc, int npcobjid, int page) {
        Map<Integer, L1ShopS> list = pc.get_otherList().SHOPXMAP;
        if (list != null) {
            int allpage = list.size() / 10;
            if (page > allpage || page < 0) {
                page = 0;
            }
            if (list.size() % 10 != 0) {
                allpage++;
            }
            pc.get_other().set_page(page);
            int or = page * 10;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.valueOf(page + 1) + "/" + allpage + ",");
            for (int key = or; key < or + 10; key++) {
                L1ShopS shopS = list.get(Integer.valueOf(key));
                if (shopS != null) {
                    stringBuilder.append(String.valueOf(shopS.get_none()) + " / " + shopS.get_adena() + ",");
                    switch (shopS.get_end()) {
                        case 0:
                            stringBuilder.append("出售中,");
                            continue;
                        case 1:
                            stringBuilder.append("已售出未領回,");
                            continue;
                        case 2:
                            stringBuilder.append("已售出已領回,");
                            continue;
                        case 3:
                            stringBuilder.append("未售出未領回,");
                            continue;
                        case 4:
                            stringBuilder.append("未售出已領回,");
                            continue;
                    }
                } else {
                    stringBuilder.append(" ,");
                }
            }
            if (allpage >= page + 1) {
                pc.sendPackets(new S_NPCTalkReturn(npcobjid, "y_x_2", stringBuilder.toString().split(",")));
            } else {
                pc.sendPackets(new S_ServerMessage(166, "沒有可以顯示的項目"));
            }
        }
    }
}
