package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Shop extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Shop.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    int mapId = pc.getMapId();
                    if (mapId == 340 || mapId == 350 || mapId == 360 || mapId == 370) {
                        ArrayList<L1PrivateShopSellList> sellList = pc.getSellList();
                        ArrayList<L1PrivateShopBuyList> buyList = pc.getBuyList();
                        boolean tradable = true;
                        int type = readC();
                        if (type == 0) {
                            int sellTotalCount = readH();
                            int i = 0;
                            while (true) {
                                if (i >= sellTotalCount) {
                                    break;
                                }
                                int sellObjectId = readD();
                                int sellPrice = Math.max(0, readD());
                                if (sellPrice <= 0) {
                                    _log.error("要求開設個人商店傳回金幣小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                                    break;
                                }
                                int sellCount = Math.max(0, readD());
                                if (sellCount <= 0) {
                                    _log.error("要求開設個人商店傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                                    break;
                                }
                                L1ItemInstance checkItem = pc.getInventory().getItem(sellObjectId);
                                if (!checkItem.getItem().isTradable()) {
                                    tradable = false;
                                    pc.sendPackets(new S_ServerMessage(1497));
                                }
                                if (checkItem.get_time() != null) {
                                    pc.sendPackets(new S_ServerMessage(1497));
                                    tradable = false;
                                }
                                if (checkItem.isEquipped()) {
                                    pc.sendPackets(new S_ServerMessage(141));
                                    over();
                                    return;
                                }
                                Object[] petlist = pc.getPetList().values().toArray();
                                int length = petlist.length;
                                for (int i2 = 0; i2 < length; i2++) {
                                    Object petObject = petlist[i2];
                                    if ((petObject instanceof L1PetInstance) && checkItem.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                                        pc.sendPackets(new S_ServerMessage(1187));
                                        over();
                                        return;
                                    }
                                }
                                if (pc.getDoll(checkItem.getId()) != null) {
                                    pc.sendPackets(new S_ServerMessage(1181));
                                    over();
                                    return;
                                }
                                L1PrivateShopSellList pssl = new L1PrivateShopSellList();
                                pssl.setItemObjectId(sellObjectId);
                                pssl.setSellPrice(sellPrice);
                                pssl.setSellTotalCount(sellCount);
                                sellList.add(pssl);
                                i++;
                            }
                            int buyTotalCount = readH();
                            int i3 = 0;
                            while (true) {
                                if (i3 >= buyTotalCount) {
                                    break;
                                }
                                int buyObjectId = readD();
                                int buyPrice = Math.max(0, readD());
                                if (buyPrice <= 0) {
                                    _log.error("要求買入道具傳回金幣小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                                    break;
                                }
                                int buyCount = Math.max(0, readD());
                                if (buyCount <= 0) {
                                    _log.error("要求買入道具傳回數量小於等於0: " + pc.getName() + pc.getNetConnection().kick());
                                    break;
                                }
                                L1ItemInstance checkItem2 = pc.getInventory().getItem(buyObjectId);
                                if (checkItem2.getCount() > 0) {
                                    if (!checkItem2.getItem().isTradable()) {
                                        tradable = false;
                                        pc.sendPackets(new S_ServerMessage(1497));
                                    }
                                    if (checkItem2.getBless() >= 128) {
                                        pc.sendPackets(new S_ServerMessage(1497));
                                        over();
                                        return;
                                    } else if (checkItem2.isEquipped()) {
                                        pc.sendPackets(new S_ServerMessage(141));
                                        over();
                                        return;
                                    } else {
                                        Object[] petlist2 = pc.getPetList().values().toArray();
                                        int length2 = petlist2.length;
                                        for (int i4 = 0; i4 < length2; i4++) {
                                            Object petObject2 = petlist2[i4];
                                            if ((petObject2 instanceof L1PetInstance) && checkItem2.getId() == ((L1PetInstance) petObject2).getItemObjId()) {
                                                pc.sendPackets(new S_ServerMessage(1187));
                                                over();
                                                return;
                                            }
                                        }
                                        if (pc.getDoll(checkItem2.getId()) != null) {
                                            pc.sendPackets(new S_ServerMessage(1181));
                                            over();
                                            return;
                                        }
                                        L1PrivateShopBuyList psbl = new L1PrivateShopBuyList();
                                        psbl.setItemObjectId(buyObjectId);
                                        psbl.setBuyPrice(buyPrice);
                                        psbl.setBuyTotalCount(buyCount);
                                        buyList.add(psbl);
                                    }
                                }
                                i3++;
                            }
                            if (!tradable) {
                                sellList.clear();
                                buyList.clear();
                                pc.setPrivateShop(false);
                                pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 3));
                                over();
                                return;
                            }
                            byte[] chat = readByte();
                            pc.setShopChat(chat);
                            pc.setPrivateShop(true);
                            pc.sendPacketsAll(new S_DoActionShop(pc.getId(), chat));
                        } else if (type == 1) {
                            sellList.clear();
                            buyList.clear();
                            pc.setPrivateShop(false);
                            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 3));
                        }
                        over();
                        return;
                    }
                    pc.sendPackets(new S_ServerMessage(876));
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
