package com.lineage.server.clientpackets;

import com.lineage.config.Config;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.InnKeyTable;
import com.lineage.server.datatables.InnTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcActionTable;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.sql.AuctionBoardTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.templates.L1House;
import com.lineage.server.templates.L1Inn;
import com.lineage.server.world.World;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Amount extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Amount.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) throws Exception {
        String s1;
        String s2;
        L1Inventory inventory;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    int objectId = readD();
                    int amount = Math.max(0, readD());
                    if (amount <= 0) {
                        over();
                        return;
                    }
                    readC();
                    String s = readS();
                    if (amount > Integer.MAX_VALUE) {
                        amount = Integer.MAX_VALUE;
                    }
                    L1NpcInstance npc = (L1NpcInstance) World.get().findObject(objectId);
                    if (npc == null) {
                        over();
                        return;
                    }
                    try {
                        StringTokenizer stringtokenizer = new StringTokenizer(s);
                        s1 = stringtokenizer.nextToken();
                        s2 = stringtokenizer.nextToken();
                    } catch (NoSuchElementException e) {
                        s1 = "";
                        s2 = "";
                    }
                    if (s1.equalsIgnoreCase("agapply")) {
                        String pcName = pc.getName();
                        for (L1AuctionBoardTmp board : AuctionBoardReading.get().getAuctionBoardTableList().values()) {
                            if (pcName.equalsIgnoreCase(board.getBidder())) {
                                pc.sendPackets(new S_ServerMessage(523));
                                over();
                                return;
                            }
                        }
                        L1AuctionBoardTmp board2 = AuctionBoardReading.get().getAuctionBoardTable(Integer.valueOf(s2).intValue());
                        if (board2 != null) {
                            long nowPrice = board2.getPrice();
                            int nowBidderId = board2.getBidderId();
                            if (pc.getInventory().checkItemX(L1ItemId.ADENA, (long) amount) == null) {
                                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                            } else if (pc.getInventory().consumeItem(L1ItemId.ADENA, (long) amount)) {
                                board2.setPrice((long) amount);
                                board2.setBidder(pcName);
                                board2.setBidderId(pc.getId());
                                AuctionBoardReading.get().updateAuctionBoard(board2);
                                if (nowBidderId != 0) {
                                    L1PcInstance bidPc = (L1PcInstance) World.get().findObject(nowBidderId);
                                    if (bidPc != null) {
                                        bidPc.getInventory().storeItem(L1ItemId.ADENA, nowPrice);
                                        bidPc.sendPackets(new S_ServerMessage(525, String.valueOf(nowPrice)));
                                    } else {
                                        CharItemsReading.get().getAdenaCount(nowBidderId, nowPrice);
                                    }
                                }
                            } else {
                                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                            }
                        }
                    } else if (s1.equalsIgnoreCase("agsell")) {
                        if (npc.getNpcId() != 70535 || npc.ACTION == null) {
                            int houseId = Integer.valueOf(s2).intValue();
                            AuctionBoardTable boardTable = new AuctionBoardTable();
                            L1AuctionBoardTmp board3 = new L1AuctionBoardTmp();
                            if (board3 != null) {
                                board3.setHouseId(houseId);
                                L1House house = HouseReading.get().getHouseTable(houseId);
                                board3.setHouseName(house.getHouseName());
                                board3.setHouseArea(house.getHouseArea());
                                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
                                cal.add(5, 5);
                                cal.set(12, 0);
                                cal.set(13, 0);
                                board3.setDeadline(cal);
                                board3.setPrice((long) amount);
                                board3.setLocation(house.getLocation());
                                board3.setOldOwner(pc.getName());
                                board3.setOldOwnerId(pc.getId());
                                board3.setBidder("");
                                board3.setBidderId(0);
                                boardTable.insertAuctionBoard(board3);
                                house.setOnSale(true);
                                house.setPurchaseBasement(true);
                                HouseReading.get().updateHouse(house);
                            }
                        } else if (amount <= 0) {
                            over();
                            return;
                        } else {
                            npc.ACTION.action(pc, npc, s, (long) amount);
                            over();
                            return;
                        }
                    }
                    int npcId = npc.getNpcId();
                    if (npcId == 70070 || npcId == 70019 || npcId == 70075 || npcId == 70012 || npcId == 70031 || npcId == 70084 || npcId == 70065 || npcId == 70054 || npcId == 70096) {
                        if (pc.getInventory().checkItem(L1ItemId.ADENA, (long) (amount * 300))) {
                            L1Inn inn = InnTable.getInstance().getTemplate(npcId, pc.getInnRoomNumber());
                            if (inn != null) {
                                Timestamp dueTime = inn.getDueTime();
                                if (dueTime == null || (Calendar.getInstance().getTimeInMillis() - dueTime.getTime()) / 1000 >= 0) {
                                    Timestamp ts = new Timestamp(System.currentTimeMillis() + 14400000);
                                    L1ItemInstance item = ItemTable.get().createItem(40312);
                                    if (item != null) {
                                        item.setKeyId(item.getId());
                                        item.setInnNpcId(npcId);
                                        item.setHall(pc.checkRoomOrHall());
                                        item.setDueTime(ts);
                                        item.setCount((long) amount);
                                        inn.setKeyId(item.getKeyId());
                                        inn.setLodgerId(pc.getId());
                                        inn.setHall(pc.checkRoomOrHall());
                                        inn.setDueTime(ts);
                                        InnTable.getInstance().updateInn(inn);
                                        pc.getInventory().consumeItem(L1ItemId.ADENA, (long) (amount * 300));
                                        if (pc.getInventory().checkAddItem(item, (long) amount) == 0) {
                                            inventory = pc.getInventory();
                                        } else {
                                            inventory = World.get().getInventory(pc.getLocation());
                                        }
                                        inventory.storeItem(item);
                                        if (InnKeyTable.checkey(item)) {
                                            InnKeyTable.DeleteKey(item);
                                            InnKeyTable.StoreKey(item);
                                        } else {
                                            InnKeyTable.StoreKey(item);
                                        }
                                        String itemName = String.valueOf(item.getItem().getName()) + item.getInnKeyName();
                                        if (amount > 1) {
                                            itemName = String.valueOf(itemName) + " (" + amount + ")";
                                        }
                                        pc.sendPackets(new S_ServerMessage(143, npc.getName(), itemName));
                                        pc.sendPackets(new S_NPCTalkReturn(npcId, "inn4", new String[]{npc.getName()}));
                                    }
                                } else {
                                    pc.sendPackets(new S_NPCTalkReturn(npcId, ""));
                                    over();
                                    return;
                                }
                            }
                        } else {
                            pc.sendPackets(new S_NPCTalkReturn(npcId, "inn3", new String[]{npc.getName()}));
                        }
                    } else if (npc.ACTION == null) {
                        L1NpcAction action = NpcActionTable.getInstance().get(s, pc, npc);
                        if (action != null) {
                            L1NpcHtml result = action.executeWithAmount(s, pc, npc, (long) amount);
                            if (result != null) {
                                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), result));
                            }
                            over();
                            return;
                        }
                    } else if (amount <= 0) {
                        over();
                        return;
                    } else {
                        npc.ACTION.action(pc, npc, s, (long) amount);
                        over();
                        return;
                    }
                    over();
                }
            }
        } catch (Exception ignored) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
