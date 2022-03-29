package com.lineage.server.clientpackets;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.*;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.*;
import com.lineage.server.model.Instance.*;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.*;
import com.lineage.server.templates.*;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;

import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_NPCAction extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_NPCAction.class);
    private static Random _random = new Random();

    public C_NPCAction() {
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

                int objid = this.readD();
                String s = this.readS();
                int[] materials = null;
                int[] counts = null;
                int[] createitem = null;
                int[] createcount = null;
                String htmlid = null;
                String success_htmlid = null;
                String failure_htmlid = null;
                String[] htmldata = null;
                L1Object obj = World.get().findObject(objid);
                if (obj == null) {
                    _log.error("該OBJID編號的 NPC已經不存在世界中: " + objid);
                    return;
                }

                int i;
                int cost;
                if (obj instanceof L1NpcInstance) {
                    L1NpcInstance tmp = (L1NpcInstance)obj;
                    String s2 = null;

                    try {
                        if (tmp.getNpcTemplate().get_classname().equalsIgnoreCase("other.Npc_AuctionBoard")) {
                            s2 = this.readS();
                        } else if (tmp.getNpcTemplate().get_classname().equalsIgnoreCase("other.Npc_Board")) {
                            s2 = this.readS();
                        }
                    } catch (Exception var36) {
                    }

                    if (obj instanceof L1PetInstance) {
                        L1PetInstance npc = (L1PetInstance)obj;
                        pc.getActionPet().action(npc, s);
                        return;
                    }

                    if (obj instanceof L1SummonInstance) {
                        L1SummonInstance npc = (L1SummonInstance)obj;
                        pc.getActionSummon().action(npc, s);
                        return;
                    }

                    L1NpcInstance npc = (L1NpcInstance)obj;
                    i = Math.abs(pc.getX() - npc.getX());
                    cost = Math.abs(pc.getY() - npc.getY());
                    if (i > 3 || cost > 3) {
                        return;
                    }

                    if (npc.ACTION != null) {
                        if (s2 != null && s2.length() > 0) {
                            npc.ACTION.action(pc, npc, s + "," + s2, 0L);
                            return;
                        }

                        npc.ACTION.action(pc, npc, s, 0L);
                        return;
                    }

                    npc.onFinalAction(pc, s);
                } else if (obj instanceof L1PcInstance) {
                    L1PcInstance target = (L1PcInstance)obj;
                    target.getAction().action(s, 0L);
                    return;
                }

                L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
                if (action != null) {
                    L1NpcHtml result = action.execute(s, pc, obj, this.readByte());
                    if (result != null) {
                        pc.sendPackets(new S_NPCTalkReturn(obj.getId(), result));
                    }

                    return;
                }

                int htmlB;
                String itemName;
                L1ItemInstance item;
                boolean insn;
                String npcName;
                if (s.equalsIgnoreCase("buy")) {
                    try {
                        pc.sendPackets(new S_ShopSellList(objid));
                    } catch (Exception var35) {
                    }
                } else {
                    int html;
                    if (s.equalsIgnoreCase("sell")) {
                        html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                        if (html != 70523 && html != 70805) {
                            if (html != 70537 && html != 70807) {
                                if (html != 70525 && html != 70804) {
                                    if (html != 50527 && html != 50505 && html != 50519 && html != 50545 && html != 50531 && html != 50529 && html != 50516 && html != 50538 && html != 50518 && html != 50509 && html != 50536 && html != 50520 && html != 50543 && html != 50526 && html != 50512 && html != 50510 && html != 50504 && html != 50525 && html != 50534 && html != 50540 && html != 50515 && html != 50513 && html != 50528 && html != 50533 && html != 50542 && html != 50511 && html != 50501 && html != 50503 && html != 50508 && html != 50514 && html != 50532 && html != 50544 && html != 50524 && html != 50535 && html != 50521 && html != 50517 && html != 50537 && html != 50539 && html != 50507 && html != 50530 && html != 50502 && html != 50506 && html != 50522 && html != 50541 && html != 50523 && html != 50620 && html != 50623 && html != 50619 && html != 50621 && html != 50622 && html != 50624 && html != 50617 && html != 50614 && html != 50618 && html != 50616 && html != 50615 && html != 50626 && html != 50627 && html != 50628 && html != 50629 && html != 50630 && html != 50631) {
                                        pc.sendPackets(new S_ShopBuyList(objid, pc));
                                    } else {
                                        String sellHouseMessage = this.sellHouse(pc, objid, html);
                                        if (sellHouseMessage != null) {
                                            htmlid = sellHouseMessage;
                                        }
                                    }
                                } else {
                                    htmlid = "lien2";
                                }
                            } else {
                                htmlid = "farlin2";
                            }
                        } else {
                            htmlid = "ladar2";
                        }
                    } else if (s.equalsIgnoreCase("retrieve")) {
                        if (pc.getLevel() >= 5) {
                            html = pc.getDwarfInventory().getItems().size();
                            if (html > 0) {
                                htmlB = client.getAccount().get_warehouse();
                                if (htmlB != -256) {
                                    pc.sendPackets(new S_ServerMessage(834));
                                    return;
                                }

                                pc.sendPackets(new S_RetrieveList(objid, pc));
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(objid, "noitemret"));
                            }
                        }
                    } else if (s.equalsIgnoreCase("retrieve-elven")) {
                        if (pc.getLevel() >= 5 && pc.isElf()) {
                            html = pc.getDwarfForElfInventory().getSize();
                            if (html > 0) {
                                htmlB = client.getAccount().get_warehouse();
                                if (htmlB != -256) {
                                    pc.sendPackets(new S_ServerMessage(834));
                                    return;
                                }

                                pc.sendPackets(new S_RetrieveElfList(objid, pc));
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(objid, "noitemret"));
                            }
                        }
                    } else if (s.equalsIgnoreCase("retrieve-pledge")) {
                        if (pc.getLevel() >= 5) {
                            if (pc.getClanid() == 0) {
                                pc.sendPackets(new S_ServerMessage(208));
                                return;
                            }

                            html = pc.getClan().getDwarfForClanInventory().getSize();
                            if (html > 0) {
                                htmlB = pc.getClanRank();
                                switch(htmlB) {
                                    case 2:
                                    case 3:
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                    case 9:
                                        if (pc.getTitle().equalsIgnoreCase("")) {
                                            pc.sendPackets(new S_ServerMessage(728));
                                            return;
                                        }
                                    case 4:
                                    case 10:
                                        break;
                                    default:
                                        pc.sendPackets(new S_ServerMessage(728));
                                        return;
                                }

                                i = client.getAccount().get_warehouse();
                                if (i != -256) {
                                    pc.sendPackets(new S_ServerMessage(834));
                                    return;
                                }

                                pc.sendPackets(new S_RetrievePledgeList(objid, pc));
                            } else {
                                pc.sendPackets(new S_NPCTalkReturn(objid, "noitemret"));
                            }
                        }
                    } else {
                        L1NpcInstance npc;

                        if (s.equalsIgnoreCase("get")) {
                            npc = (L1NpcInstance)obj;
                            htmlB = npc.getNpcTemplate().get_npcId();
                            if (htmlB != 70099 && htmlB != 70796) {
                                if (htmlB == 70528 || htmlB == 70546 || htmlB == 70567 || htmlB == 70594 || htmlB == 70654 || htmlB == 70748 || htmlB == 70774 || htmlB == 70799 || htmlB == 70815 || htmlB == 70860) {
                                    pc.getHomeTownId();
                                }
                            } else {
                                item = pc.getInventory().storeItem(20081, 1L);
                                npcName = npc.getNpcTemplate().get_name();
                                itemName = item.getItem().getName();
                                pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                pc.getQuest().set_end(11);
                                htmlid = "";
                            }
                        } else {
                            boolean isHall;
                            int roomNumber;
                            byte roomCount;

                            L1Inn inn;
                            Timestamp dueTime;
                            Calendar cal;
                            long checkDueTime;
                            boolean canRent;
                            boolean findRoom;
                            boolean isRented;
                            if (s.equalsIgnoreCase("room")) {
                                npc = (L1NpcInstance)obj;
                                htmlB = npc.getNpcTemplate().get_npcId();
                                canRent = false;
                                findRoom = false;
                                isRented = false;
                                isHall = false;
                                roomNumber = 0;
                                roomCount = 0;

                                for(i = 0; i < 16; ++i) {
                                    inn = InnTable.getInstance().getTemplate(htmlB, i);
                                    if (inn != null) {
                                        if (inn.isHall()) {
                                            isHall = true;
                                        }

                                        dueTime = inn.getDueTime();
                                        cal = Calendar.getInstance();
                                        checkDueTime = (cal.getTimeInMillis() - dueTime.getTime()) / 1000L;
                                        if (inn.getLodgerId() == pc.getId() && checkDueTime < 0L) {
                                            isRented = true;
                                            break;
                                        }

                                        if (pc.getInventory().checkItem(40312, 1L)) {
                                            isRented = true;
                                            break;
                                        }

                                        if (!findRoom && !isRented) {
                                            if (checkDueTime >= 0L) {
                                                canRent = true;
                                                findRoom = true;
                                                roomNumber = inn.getRoomNumber();
                                            } else if (!inn.isHall()) {
                                                ++roomCount;
                                            }
                                        }
                                    }
                                }

                                if (isRented) {
                                    if (isHall) {
                                        htmlid = "inn15";
                                    } else {
                                        htmlid = "inn5";
                                    }
                                } else if (roomCount >= 12) {
                                    htmlid = "inn6";
                                } else if (canRent) {
                                    pc.setInnRoomNumber(roomNumber);
                                    pc.setHall(false);
                                    pc.sendPackets(new S_HowManyKey(npc, 300, 1, 8, "inn2"));
                                }
                            } else if (s.equalsIgnoreCase("hall") && obj instanceof L1MerchantInstance) {
                                if (pc.isCrown()) {
                                    npc = (L1NpcInstance)obj;
                                    htmlB = npc.getNpcTemplate().get_npcId();
                                    canRent = false;
                                    findRoom = false;
                                    isRented = false;
                                    isHall = false;
                                    roomNumber = 0;
                                    roomCount = 0;

                                    for(i = 0; i < 16; ++i) {
                                        inn = InnTable.getInstance().getTemplate(htmlB, i);
                                        if (inn != null) {
                                            if (inn.isHall()) {
                                                isHall = true;
                                            }

                                            dueTime = inn.getDueTime();
                                            cal = Calendar.getInstance();
                                            checkDueTime = (cal.getTimeInMillis() - dueTime.getTime()) / 1000L;
                                            if (inn.getLodgerId() == pc.getId() && checkDueTime < 0L) {
                                                isRented = true;
                                                break;
                                            }

                                            if (pc.getInventory().checkItem(40312, 1L)) {
                                                isRented = true;
                                                break;
                                            }

                                            if (!findRoom && !isRented) {
                                                if (checkDueTime >= 0L) {
                                                    canRent = true;
                                                    findRoom = true;
                                                    roomNumber = inn.getRoomNumber();
                                                } else if (inn.isHall()) {
                                                    ++roomCount;
                                                }
                                            }
                                        }
                                    }

                                    if (isRented) {
                                        if (isHall) {
                                            htmlid = "inn15";
                                        } else {
                                            htmlid = "inn5";
                                        }
                                    } else if (roomCount >= 4) {
                                        htmlid = "inn16";
                                    } else if (canRent) {
                                        pc.setInnRoomNumber(roomNumber);
                                        pc.setHall(true);
                                        pc.sendPackets(new S_HowManyKey(npc, 300, 1, 16, "inn12"));
                                    }
                                } else {
                                    htmlid = "inn10";
                                }
                            } else {


                                if (s.equalsIgnoreCase("return")) {
                                    npc = (L1NpcInstance)obj;
                                    htmlB = npc.getNpcTemplate().get_npcId();
                                    i = 0;
                                    findRoom = false;

                                    for(i = 0; i < 16; ++i) {
                                        inn = InnTable.getInstance().getTemplate(htmlB, i);
                                        if (inn != null && inn.getLodgerId() == pc.getId()) {
                                            dueTime = inn.getDueTime();
                                            if (dueTime != null) {
                                                cal = Calendar.getInstance();
                                                if ((cal.getTimeInMillis() - dueTime.getTime()) / 1000L < 0L) {
                                                    findRoom = true;
                                                    i += 60;
                                                }
                                            }

                                            Timestamp ts = new Timestamp(System.currentTimeMillis());
                                            inn.setDueTime(ts);
                                            inn.setLodgerId(0);
                                            inn.setKeyId(0);
                                            inn.setHall(false);
                                            InnTable.getInstance().updateInn(inn);
                                            break;
                                        }
                                    }

                                    Iterator var67 = pc.getInventory().getItems().iterator();

                                    while(var67.hasNext()) {
                                        item = (L1ItemInstance)var67.next();
                                        if (item.getInnNpcId() == htmlB) {
                                            i = (int)((long)i + 20L * item.getCount());
                                            InnKeyTable.DeleteKey(item);
                                            pc.getInventory().removeItem(item);
                                            findRoom = true;
                                        }
                                    }

                                    if (findRoom) {
                                        htmldata = new String[]{npc.getName(), String.valueOf(i)};
                                        htmlid = "inn20";
                                        pc.getInventory().storeItem(40308, (long)i);
                                    } else {
                                        htmlid = "";
                                    }
                                } else {

                                    if (s.equalsIgnoreCase("enter")) {
                                        npc = (L1NpcInstance)obj;
                                        htmlB = npc.getNpcTemplate().get_npcId();
                                        Iterator var54 = pc.getInventory().getItems().iterator();

                                        label5784:
                                        while(true) {
                                            while(true) {
                                                do {
                                                    if (!var54.hasNext()) {
                                                        break label5784;
                                                    }

                                                    item = (L1ItemInstance)var54.next();
                                                } while(item.getInnNpcId() != htmlB);

                                                for(i = 0; i < 16; ++i) {
                                                    inn = InnTable.getInstance().getTemplate(htmlB, i);
                                                    if (inn.getKeyId() == item.getKeyId()) {
                                                        dueTime = item.getDueTime();
                                                        if (dueTime != null) {
                                                            cal = Calendar.getInstance();
                                                            if ((cal.getTimeInMillis() - dueTime.getTime()) / 1000L < 0L) {
                                                                int[] data = null;
                                                                switch(htmlB) {
                                                                    case 70012:
                                                                        data = new int[]{32745, 32803, 16384, 32743, 32808, 16896};
                                                                        break;
                                                                    case 70019:
                                                                        data = new int[]{32743, 32803, 17408, 32744, 32807, 17920};
                                                                        break;
                                                                    case 70031:
                                                                        data = new int[]{32744, 32803, 18432, 32744, 32807, 18944};
                                                                        break;
                                                                    case 70054:
                                                                        data = new int[]{32744, 32803, 23552, 32744, 32807, 24064};
                                                                        break;
                                                                    case 70065:
                                                                        data = new int[]{32744, 32803, 19456, 32744, 32807, 19968};
                                                                        break;
                                                                    case 70070:
                                                                        data = new int[]{32744, 32803, 20480, 32744, 32807, 20992};
                                                                        break;
                                                                    case 70075:
                                                                        data = new int[]{32744, 32803, 21504, 32744, 32807, 22016};
                                                                        break;
                                                                    case 70084:
                                                                        data = new int[]{32744, 32803, 22528, 32744, 32807, 23040};
                                                                        break;
                                                                    case 70096:
                                                                        data = new int[]{32744, 32803, 24576, 32744, 32807, 25088};
                                                                }

                                                                if (!item.checkRoomOrHall()) {
                                                                    pc.set_showId(item.getKeyId());
                                                                    L1Teleport.teleport(pc, data[0], data[1], (short)data[2], 6, false);
                                                                } else {
                                                                    pc.set_showId(item.getKeyId());
                                                                    L1Teleport.teleport(pc, data[3], data[4], (short)data[5], 6, false);
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else if (s.equalsIgnoreCase("openigate")) {
                                        npc = (L1NpcInstance)obj;
                                        this.openCloseGate(pc, npc.getNpcTemplate().get_npcId(), true);
                                        htmlid = "";
                                    } else if (s.equalsIgnoreCase("closeigate")) {
                                        npc = (L1NpcInstance)obj;
                                        this.openCloseGate(pc, npc.getNpcTemplate().get_npcId(), false);
                                        htmlid = "";
                                    } else if (s.equalsIgnoreCase("askwartime")) {
                                        npc = (L1NpcInstance)obj;
                                        if (npc.getNpcTemplate().get_npcId() == 60514) {
                                            htmldata = this.makeWarTimeStrings(1);
                                            htmlid = "ktguard7";
                                        } else if (npc.getNpcTemplate().get_npcId() == 60560) {
                                            htmldata = this.makeWarTimeStrings(2);
                                            htmlid = "orcguard7";
                                        } else if (npc.getNpcTemplate().get_npcId() == 60552) {
                                            htmldata = this.makeWarTimeStrings(3);
                                            htmlid = "wdguard7";
                                        } else if (npc.getNpcTemplate().get_npcId() != 60524 && npc.getNpcTemplate().get_npcId() != 60525 && npc.getNpcTemplate().get_npcId() != 60529) {
                                            if (npc.getNpcTemplate().get_npcId() == 70857) {
                                                htmldata = this.makeWarTimeStrings(5);
                                                htmlid = "heguard7";
                                            } else if (npc.getNpcTemplate().get_npcId() != 60530 && npc.getNpcTemplate().get_npcId() != 60531) {
                                                if (npc.getNpcTemplate().get_npcId() != 60533 && npc.getNpcTemplate().get_npcId() != 60534) {
                                                    if (npc.getNpcTemplate().get_npcId() == 81156) {
                                                        htmldata = this.makeWarTimeStrings(8);
                                                        htmlid = "dfguard3";
                                                    }
                                                } else {
                                                    htmldata = this.makeWarTimeStrings(7);
                                                    htmlid = "adguard7";
                                                }
                                            } else {
                                                htmldata = this.makeWarTimeStrings(6);
                                                htmlid = "dcguard7";
                                            }
                                        } else {
                                            htmldata = this.makeWarTimeStrings(4);
                                            htmlid = "grguard7";
                                        }
                                    } else {
                                        L1Clan clan;
                                        L1Castle l1castle;
                                        if (s.equalsIgnoreCase("inex")) {
                                            clan = WorldClan.get().getClan(pc.getClanname());
                                            if (clan != null) {
                                                htmlB = clan.getCastleId();
                                                if (htmlB != 0) {
                                                    l1castle = CastleReading.get().getCastleTable(htmlB);
                                                    pc.sendPackets(new S_ServerMessage(309, l1castle.getName(), String.valueOf(l1castle.getPublicMoney())));
                                                    htmlid = "";
                                                }
                                            }
                                        } else if (s.equalsIgnoreCase("tax")) {
                                            pc.sendPackets(new S_TaxRate(pc.getId()));
                                        } else if (s.equalsIgnoreCase("withdrawal")) {
                                            clan = WorldClan.get().getClan(pc.getClanname());
                                            if (clan != null) {
                                                htmlB = clan.getCastleId();
                                                if (htmlB != 0) {
                                                    l1castle = CastleReading.get().getCastleTable(htmlB);
                                                    pc.sendPackets(new S_Drawal(pc.getId(), l1castle.getPublicMoney()));
                                                }
                                            }
                                        } else if (s.equalsIgnoreCase("cdeposit")) {
                                            pc.sendPackets(new S_Deposit(pc.getId()));
                                        } else if (!s.equalsIgnoreCase("employ") && !s.equalsIgnoreCase("arrange")) {
                                            if (s.equalsIgnoreCase("castlegate")) {
                                                this.repairGate(pc);
                                                htmlid = "";
                                            } else {

                                                if (s.equalsIgnoreCase("encw")) {
                                                    if (pc.getWeapon() == null) {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    } else {
                                                        Iterator var53 = pc.getInventory().getItems().iterator();

                                                        while(var53.hasNext()) {
                                                            item = (L1ItemInstance)var53.next();
                                                            if (pc.getWeapon().equals(item)) {
                                                                L1SkillUse l1skilluse = new L1SkillUse();
                                                                l1skilluse.handleCommands(pc, 12, item.getId(), 0, 0, 0, 2);
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    htmlid = "";
                                                } else if (s.equalsIgnoreCase("enca")) {
                                                    item = pc.getInventory().getItemEquipped(2, 2);
                                                    if (item != null) {
                                                        L1SkillUse l1skilluse = new L1SkillUse();
                                                        l1skilluse.handleCommands(pc, 21, item.getId(), 0, 0, 0, 2);
                                                    } else {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    }

                                                    htmlid = "";
                                                } else if (s.equalsIgnoreCase("depositnpc")) {
                                                    Object[] petList = pc.getPetList().values().toArray();
                                                    Object[] var66 = petList;
                                                    cost = petList.length;

                                                    for(i = 0; i < cost; ++i) {
                                                        Object petObject = var66[i];
                                                        if (petObject instanceof L1PetInstance) {
                                                            L1PetInstance pet = (L1PetInstance)petObject;
                                                            pet.collect(true);
                                                            pc.removePet(pet);
                                                            pet.setDead(true);
                                                            pet.deleteMe();
                                                        }
                                                    }

                                                    htmlid = "";
                                                } else if (s.equalsIgnoreCase("withdrawnpc")) {
                                                    pc.sendPackets(new S_PetList(objid, pc));
                                                } else if (!s.equalsIgnoreCase("open") && !s.equalsIgnoreCase("close")) {
                                                    if (s.equalsIgnoreCase("expel")) {
                                                        npc = (L1NpcInstance)obj;
                                                        this.expelOtherClan(pc, npc.getNpcTemplate().get_npcId());
                                                        htmlid = "";
                                                    } else if (s.equalsIgnoreCase("pay")) {
                                                        npc = (L1NpcInstance)obj;
                                                        htmldata = this.makeHouseTaxStrings(pc, npc);
                                                        htmlid = "agpay";
                                                    } else if (s.equalsIgnoreCase("payfee")) {
                                                        npc = (L1NpcInstance)obj;
                                                        this.payFee(pc, npc);
                                                        htmlid = "";
                                                    } else {

                                                        L1House house;
                                                        if (s.equalsIgnoreCase("name")) {
                                                            clan = WorldClan.get().getClan(pc.getClanname());
                                                            if (clan != null) {
                                                                htmlB = clan.getHouseId();
                                                                if (htmlB != 0) {
                                                                    house = HouseReading.get().getHouseTable(htmlB);
                                                                    cost = house.getKeeperId();
                                                                    npc = (L1NpcInstance)obj;
                                                                    if (npc.getNpcTemplate().get_npcId() == cost) {
                                                                        pc.setTempID(htmlB);
                                                                        pc.sendPackets(new S_Message_YN(512));
                                                                    }
                                                                }
                                                            }

                                                            htmlid = "";
                                                        } else if (!s.equalsIgnoreCase("rem")) {
                                                            int[] loc;
                                                            if (!s.equalsIgnoreCase("tel0") && !s.equalsIgnoreCase("tel1") && !s.equalsIgnoreCase("tel2") && !s.equalsIgnoreCase("tel3")) {
                                                                if (s.equalsIgnoreCase("upgrade")) {
                                                                    clan = WorldClan.get().getClan(pc.getClanname());
                                                                    if (clan != null) {
                                                                        htmlB = clan.getHouseId();
                                                                        if (htmlB != 0) {
                                                                            house = HouseReading.get().getHouseTable(htmlB);
                                                                            cost = house.getKeeperId();
                                                                            npc = (L1NpcInstance)obj;
                                                                            if (npc.getNpcTemplate().get_npcId() == cost) {
                                                                                if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
                                                                                    if (house.isPurchaseBasement()) {
                                                                                        pc.sendPackets(new S_ServerMessage(1135));
                                                                                    } else if (pc.getInventory().consumeItem(40308, 5000000L)) {
                                                                                        house.setPurchaseBasement(true);
                                                                                        HouseReading.get().updateHouse(house);
                                                                                        pc.sendPackets(new S_ServerMessage(1099));
                                                                                    } else {
                                                                                        pc.sendPackets(new S_ServerMessage(189));
                                                                                    }
                                                                                } else {
                                                                                    pc.sendPackets(new S_ServerMessage(518));
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("hall") && obj instanceof L1HousekeeperInstance) {
                                                                    clan = WorldClan.get().getClan(pc.getClanname());
                                                                    if (clan != null) {
                                                                        htmlB = clan.getHouseId();
                                                                        if (htmlB != 0) {
                                                                            house = HouseReading.get().getHouseTable(htmlB);
                                                                            cost = house.getKeeperId();
                                                                            npc = (L1NpcInstance)obj;
                                                                            if (npc.getNpcTemplate().get_npcId() == cost) {
                                                                                if (house.isPurchaseBasement()) {
                                                                                    loc = new int[3];
                                                                                    loc = L1HouseLocation.getBasementLoc(htmlB);
                                                                                    L1Teleport.teleport(pc, loc[0], loc[1], (short)loc[2], 5, true);
                                                                                } else {
                                                                                    pc.sendPackets(new S_ServerMessage(1098));
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("fire")) {
                                                                    if (pc.isElf()) {
                                                                        if (pc.getElfAttr() != 0) {
                                                                            return;
                                                                        }

                                                                        pc.setElfAttr(2);
                                                                        pc.save();
                                                                        pc.sendPackets(new S_PacketBox(15, 1));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("water")) {
                                                                    if (pc.isElf()) {
                                                                        if (pc.getElfAttr() != 0) {
                                                                            return;
                                                                        }

                                                                        pc.setElfAttr(4);
                                                                        pc.save();
                                                                        pc.sendPackets(new S_PacketBox(15, 2));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("air")) {
                                                                    if (pc.isElf()) {
                                                                        if (pc.getElfAttr() != 0) {
                                                                            return;
                                                                        }

                                                                        pc.setElfAttr(8);
                                                                        pc.save();
                                                                        pc.sendPackets(new S_PacketBox(15, 3));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("earth")) {
                                                                    if (pc.isElf()) {
                                                                        if (pc.getElfAttr() != 0) {
                                                                            return;
                                                                        }

                                                                        pc.setElfAttr(1);
                                                                        pc.save();
                                                                        pc.sendPackets(new S_PacketBox(15, 4));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("init")) {
                                                                    if (pc.isElf()) {
                                                                        if (pc.getElfAttr() == 0) {
                                                                            return;
                                                                        }

                                                                        for(html = 129; html <= 176; ++html) {
                                                                            L1Skills l1skills1 = SkillsTable.get().getTemplate(html);
                                                                            i = l1skills1.getAttr();
                                                                            if (i != 0) {
                                                                                CharSkillReading.get().spellLost(pc.getId(), l1skills1.getSkillId());
                                                                            }
                                                                        }

                                                                        if (pc.hasSkillEffect(147)) {
                                                                            pc.removeSkillEffect(147);
                                                                        }

                                                                        pc.sendPackets(new S_DelSkill(pc, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 248, 252, 252, 255, 0, 0, 0, 0, 0, 0));
                                                                        pc.setElfAttr(0);
                                                                        pc.save();
                                                                        pc.sendPackets(new S_ServerMessage(678));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("exp")) {
                                                                    if (pc.getExpRes() == 1) {
                                                                        insn = false;
                                                                        htmlB = pc.getLevel();
                                                                        i = pc.getLawful();
                                                                        if (htmlB < 45) {
                                                                            html = htmlB * htmlB * 100;
                                                                        } else {
                                                                            html = htmlB * htmlB * 200;
                                                                        }

                                                                        if (i >= 0) {
                                                                            html /= 2;
                                                                        }

                                                                        pc.sendPackets(new S_Message_YN(738, String.valueOf(html)));
                                                                    } else {
                                                                        pc.sendPackets(new S_ServerMessage(739));
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("ent")) {
                                                                    this.watchUb(pc, 50038);
                                                                    html = ((L1NpcInstance)obj).getNpcId();
                                                                    if (html != 80085 && html != 80086 && html != 80087) {
                                                                        if (html != 50038 && html != 50042 && html != 50029 && html != 50019 && html != 50062) {
                                                                            htmlid = this.enterUb(pc, html);
                                                                        } else {
                                                                            htmlid = this.watchUb(pc, html);
                                                                        }
                                                                    } else {
                                                                        htmlid = this.enterHauntedHouse(pc);
                                                                    }
                                                                } else if (s.equalsIgnoreCase("par")) {
                                                                    htmlid = this.enterUb(pc, ((L1NpcInstance)obj).getNpcId());
                                                                } else if (s.equalsIgnoreCase("info")) {
                                                                    html = ((L1NpcInstance)obj).getNpcId();
                                                                    if (html != 80085 && html != 80086 && html != 80087) {
                                                                        htmlid = "colos2";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("sco")) {
                                                                    npc = (L1NpcInstance)obj;
                                                                    this.UbRank(pc, npc);
                                                                } else if (s.equalsIgnoreCase("haste")) {
                                                                    npc = (L1NpcInstance)obj;
                                                                    htmlB = npc.getNpcTemplate().get_npcId();
                                                                    if (htmlB == 70514) {
                                                                        pc.sendPackets(new S_ServerMessage(183));
                                                                        pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 1600));
                                                                        pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                                                                        pc.sendPacketsX8(new S_SkillSound(pc.getId(), 755));
                                                                        pc.setMoveSpeed(1);
                                                                        pc.setSkillEffect(1001, 1600000);
                                                                        htmlid = "";
                                                                    }
                                                                } else if (s.equalsIgnoreCase("skeleton nbmorph")) {
                                                                    this.poly(client, 2374);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("lycanthrope nbmorph")) {
                                                                    this.poly(client, 3874);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("shelob nbmorph")) {
                                                                    this.poly(client, 95);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("ghoul nbmorph")) {
                                                                    this.poly(client, 3873);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("ghast nbmorph")) {
                                                                    this.poly(client, 3875);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("atuba orc nbmorph")) {
                                                                    this.poly(client, 3868);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("skeleton axeman nbmorph")) {
                                                                    this.poly(client, 2376);
                                                                    htmlid = "";
                                                                } else if (s.equalsIgnoreCase("troll nbmorph")) {
                                                                    this.poly(client, 3878);
                                                                    htmlid = "";
                                                                } else {

                                                                    if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71038) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41060, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfnoname9";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41060, 1L)) {
                                                                            htmlid = "orcfnoname11";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71039) {
                                                                        if (s.equalsIgnoreCase("teleportURL")) {
                                                                            htmlid = "orcfbuwoo2";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71040) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41065, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfnoa4";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41065, 1L)) {
                                                                            htmlid = "orcfnoa7";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71041) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41064, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfhuwoomo4";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41064, 1L)) {
                                                                            htmlid = "orcfhuwoomo6";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71042) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41062, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfbakumo4";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41062, 1L)) {
                                                                            htmlid = "orcfbakumo6";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71043) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41063, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfbuka4";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41063, 1L)) {
                                                                            htmlid = "orcfbuka6";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71044) {
                                                                        if (s.equalsIgnoreCase("A")) {
                                                                            npc = (L1NpcInstance)obj;
                                                                            item = pc.getInventory().storeItem(41061, 1L);
                                                                            npcName = npc.getNpcTemplate().get_name();
                                                                            itemName = item.getItem().getName();
                                                                            pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                            htmlid = "orcfkame4";
                                                                        } else if (s.equalsIgnoreCase("Z") && pc.getInventory().consumeItem(41061, 1L)) {
                                                                            htmlid = "orcfkame6";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71078) {
                                                                        if (s.equalsIgnoreCase("teleportURL")) {
                                                                            htmlid = "usender2";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71080) {
                                                                        if (s.equalsIgnoreCase("teleportURL")) {
                                                                            htmlid = "amisoo2";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80048) {
                                                                        if (s.equalsIgnoreCase("2")) {
                                                                            htmlid = "";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80049) {
                                                                        if (s.equalsIgnoreCase("1") && pc.getKarma() <= -10000000) {
                                                                            pc.setKarma(1000000);
                                                                            pc.sendPackets(new S_ServerMessage(1078));
                                                                            htmlid = "betray13";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80050) {
                                                                        if (s.equalsIgnoreCase("1")) {
                                                                            htmlid = "meet105";
                                                                        } else if (s.equalsIgnoreCase("2")) {
                                                                            if (pc.getInventory().checkItem(40718)) {
                                                                                htmlid = "meet106";
                                                                            } else {
                                                                                htmlid = "meet110";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("a")) {
                                                                            if (pc.getInventory().consumeItem(40718, 1L)) {
                                                                                pc.addKarma((int)(-100.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1079));
                                                                                htmlid = "meet107";
                                                                            } else {
                                                                                htmlid = "meet104";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("b")) {
                                                                            if (pc.getInventory().consumeItem(40718, 10L)) {
                                                                                pc.addKarma((int)(-1000.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1079));
                                                                                htmlid = "meet108";
                                                                            } else {
                                                                                htmlid = "meet104";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("c")) {
                                                                            if (pc.getInventory().consumeItem(40718, 100L)) {
                                                                                pc.addKarma((int)(-10000.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1079));
                                                                                htmlid = "meet109";
                                                                            } else {
                                                                                htmlid = "meet104";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("d")) {
                                                                            if (!pc.getInventory().checkItem(40615) && !pc.getInventory().checkItem(40616)) {
                                                                                L1Teleport.teleport(pc, 32683, 32895, (short)608, 5, true);
                                                                            } else {
                                                                                htmlid = "";
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80052) {
                                                                        if (s.equalsIgnoreCase("a")) {
                                                                            if (pc.hasSkillEffect(4003)) {
                                                                                pc.removeSkillEffect(4003);
                                                                            }

                                                                            if (pc.hasSkillEffect(4007)) {
                                                                                pc.removeSkillEffect(4007);
                                                                            }

                                                                            if (pc.hasSkillEffect(4006)) {
                                                                                pc.sendPackets(new S_ServerMessage(79));
                                                                            } else {
                                                                                pc.setSkillEffect(4005, 1500000);
                                                                                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7246));
                                                                                pc.sendPackets(new S_ServerMessage(1127));
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80053) {
                                                                        html = pc.getKarmaLevel();
                                                                        if (s.equalsIgnoreCase("a")) {
                                                                            htmlB = 0;
                                                                            int[] aliceMaterialIdList = new int[]{40991, 196, 197, 198, 199, 200, 201, 202, 203};
                                                                            int[] var75 = aliceMaterialIdList;
                                                                            i = aliceMaterialIdList.length;

                                                                            for(i = 0; i < i; ++i) {
                                                                                cost = var75[i];
                                                                                if (pc.getInventory().checkItem(cost)) {
                                                                                    htmlB = cost;
                                                                                    break;
                                                                                }
                                                                            }

                                                                            if (htmlB == 0) {
                                                                                htmlid = "alice_no";
                                                                            } else if (htmlB == 40991) {
                                                                                if (html <= -1) {
                                                                                    materials = new int[]{40995, 40718, 40991};
                                                                                    counts = new int[]{100, 100, 1};
                                                                                    createitem = new int[]{196};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_1";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "aliceyet";
                                                                                }
                                                                            } else if (htmlB == 196) {
                                                                                if (html <= -2) {
                                                                                    materials = new int[]{40997, 40718, 196};
                                                                                    counts = new int[]{100, 100, 1};
                                                                                    createitem = new int[]{197};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_2";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_1";
                                                                                }
                                                                            } else if (htmlB == 197) {
                                                                                if (html <= -3) {
                                                                                    materials = new int[]{40990, 40718, 197};
                                                                                    counts = new int[]{100, 100, 1};
                                                                                    createitem = new int[]{198};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_3";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_2";
                                                                                }
                                                                            } else if (htmlB == 198) {
                                                                                if (html <= -4) {
                                                                                    materials = new int[]{40994, 40718, 198};
                                                                                    counts = new int[]{50, 100, 1};
                                                                                    createitem = new int[]{199};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_4";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_3";
                                                                                }
                                                                            } else if (htmlB == 199) {
                                                                                if (html <= -5) {
                                                                                    materials = new int[]{40993, 40718, 199};
                                                                                    counts = new int[]{50, 100, 1};
                                                                                    createitem = new int[]{200};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_5";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_4";
                                                                                }
                                                                            } else if (htmlB == 200) {
                                                                                if (html <= -6) {
                                                                                    materials = new int[]{40998, 40718, 200};
                                                                                    counts = new int[]{50, 100, 1};
                                                                                    createitem = new int[]{201};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_6";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_5";
                                                                                }
                                                                            } else if (htmlB == 201) {
                                                                                if (html <= -7) {
                                                                                    materials = new int[]{40996, 40718, 201};
                                                                                    counts = new int[]{10, 100, 1};
                                                                                    createitem = new int[]{202};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_7";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_6";
                                                                                }
                                                                            } else if (htmlB == 202) {
                                                                                if (html <= -8) {
                                                                                    materials = new int[]{40992, 40718, 202};
                                                                                    counts = new int[]{10, 100, 1};
                                                                                    createitem = new int[]{203};
                                                                                    createcount = new int[]{1};
                                                                                    success_htmlid = "alice_8";
                                                                                    failure_htmlid = "alice_no";
                                                                                } else {
                                                                                    htmlid = "alice_7";
                                                                                }
                                                                            } else if (htmlB == 203) {
                                                                                htmlid = "alice_8";
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80055) {
                                                                        npc = (L1NpcInstance)obj;
                                                                        htmlid = this.getYaheeAmulet(pc, npc, s);
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80056) {
                                                                        npc = (L1NpcInstance)obj;
                                                                        if (pc.getKarma() <= -10000000) {
                                                                            this.getBloodCrystalByKarma(pc, npc, s);
                                                                        }

                                                                        htmlid = "";
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80063) {
                                                                        if (s.equalsIgnoreCase("a")) {
                                                                            if (pc.getInventory().checkItem(40921)) {
                                                                                L1Teleport.teleport(pc, 32674, 32832, (short)603, 2, true);
                                                                            } else {
                                                                                htmlid = "gpass02";
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80064) {
                                                                        if (s.equalsIgnoreCase("1")) {
                                                                            htmlid = "meet005";
                                                                        } else if (s.equalsIgnoreCase("2")) {
                                                                            if (pc.getInventory().checkItem(40678)) {
                                                                                htmlid = "meet006";
                                                                            } else {
                                                                                htmlid = "meet010";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("a")) {
                                                                            if (pc.getInventory().consumeItem(40678, 1L)) {
                                                                                pc.addKarma((int)(100.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1078));
                                                                                htmlid = "meet007";
                                                                            } else {
                                                                                htmlid = "meet004";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("b")) {
                                                                            if (pc.getInventory().consumeItem(40678, 10L)) {
                                                                                pc.addKarma((int)(1000.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1078));
                                                                                htmlid = "meet008";
                                                                            } else {
                                                                                htmlid = "meet004";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("c")) {
                                                                            if (pc.getInventory().consumeItem(40678, 100L)) {
                                                                                pc.addKarma((int)(10000.0D * ConfigRate.RATE_KARMA));
                                                                                pc.sendPackets(new S_ServerMessage(1078));
                                                                                htmlid = "meet009";
                                                                            } else {
                                                                                htmlid = "meet004";
                                                                            }
                                                                        } else if (s.equalsIgnoreCase("d")) {
                                                                            if (!pc.getInventory().checkItem(40909) && !pc.getInventory().checkItem(40910) && !pc.getInventory().checkItem(40911) && !pc.getInventory().checkItem(40912) && !pc.getInventory().checkItem(40913) && !pc.getInventory().checkItem(40914) && !pc.getInventory().checkItem(40915) && !pc.getInventory().checkItem(40916) && !pc.getInventory().checkItem(40917) && !pc.getInventory().checkItem(40918) && !pc.getInventory().checkItem(40919) && !pc.getInventory().checkItem(40920) && !pc.getInventory().checkItem(40921)) {
                                                                                L1Teleport.teleport(pc, 32674, 32832, (short)602, 2, true);
                                                                            } else {
                                                                                htmlid = "";
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80066) {
                                                                        if (s.equalsIgnoreCase("1") && pc.getKarma() >= 10000000) {
                                                                            pc.setKarma(-1000000);
                                                                            pc.sendPackets(new S_ServerMessage(1079));
                                                                            htmlid = "betray03";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80071) {
                                                                        npc = (L1NpcInstance)obj;
                                                                        htmlid = this.getBarlogEarring(pc, npc, s);
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80073) {
                                                                        if (s.equalsIgnoreCase("a")) {
                                                                            if (pc.hasSkillEffect(4003)) {
                                                                                pc.removeSkillEffect(4003);
                                                                            }

                                                                            if (pc.hasSkillEffect(4007)) {
                                                                                pc.removeSkillEffect(4007);
                                                                            }

                                                                            if (pc.hasSkillEffect(4005)) {
                                                                                pc.sendPackets(new S_ServerMessage(79));
                                                                            } else {
                                                                                pc.setSkillEffect(4006, 1020000);
                                                                                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7247));
                                                                                pc.sendPackets(new S_ServerMessage(1127));
                                                                            }
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80072) {
                                                                        html = pc.getKarmaLevel();
                                                                        if (s.equalsIgnoreCase("0")) {
                                                                            htmlid = "lsmitha";
                                                                        } else if (s.equalsIgnoreCase("1")) {
                                                                            htmlid = "lsmithb";
                                                                        } else if (s.equalsIgnoreCase("2")) {
                                                                            htmlid = "lsmithc";
                                                                        } else if (s.equalsIgnoreCase("3")) {
                                                                            htmlid = "lsmithd";
                                                                        } else if (s.equalsIgnoreCase("4")) {
                                                                            htmlid = "lsmithe";
                                                                        } else if (s.equalsIgnoreCase("5")) {
                                                                            htmlid = "lsmithf";
                                                                        } else if (s.equalsIgnoreCase("6")) {
                                                                            htmlid = "";
                                                                        } else if (s.equalsIgnoreCase("7")) {
                                                                            htmlid = "lsmithg";
                                                                        } else if (s.equalsIgnoreCase("8")) {
                                                                            htmlid = "lsmithh";
                                                                        } else if (s.equalsIgnoreCase("a") && html >= 1) {
                                                                            materials = new int[]{20158, 40669, 40678};
                                                                            counts = new int[]{1, 50, 100};
                                                                            createitem = new int[]{20083};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithaa";
                                                                        } else if (s.equalsIgnoreCase("b") && html >= 2) {
                                                                            materials = new int[]{20144, 40672, 40678};
                                                                            counts = new int[]{1, 50, 100};
                                                                            createitem = new int[]{20131};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithbb";
                                                                        } else if (s.equalsIgnoreCase("c") && html >= 3) {
                                                                            materials = new int[]{20075, 40671, 40678};
                                                                            counts = new int[]{1, 50, 100};
                                                                            createitem = new int[]{20069};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithcc";
                                                                        } else if (s.equalsIgnoreCase("d") && html >= 4) {
                                                                            materials = new int[]{20183, 40674, 40678};
                                                                            counts = new int[]{1, 20, 100};
                                                                            createitem = new int[]{20179};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithdd";
                                                                        } else if (s.equalsIgnoreCase("e") && html >= 5) {
                                                                            materials = new int[]{20190, 40674, 40678};
                                                                            counts = new int[]{1, 40, 100};
                                                                            createitem = new int[]{20209};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithee";
                                                                        } else if (s.equalsIgnoreCase("f") && html >= 6) {
                                                                            materials = new int[]{20078, 40674, 40678};
                                                                            counts = new int[]{1, 5, 100};
                                                                            createitem = new int[]{20290};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithff";
                                                                        } else if (s.equalsIgnoreCase("g") && html >= 7) {
                                                                            materials = new int[]{20078, 40670, 40678};
                                                                            counts = new int[]{1, 1, 100};
                                                                            createitem = new int[]{20261};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithgg";
                                                                        } else if (s.equalsIgnoreCase("h") && html >= 8) {
                                                                            materials = new int[]{40719, 40673, 40678};
                                                                            counts = new int[]{1, 1, 100};
                                                                            createitem = new int[]{20031};
                                                                            createcount = new int[]{1};
                                                                            success_htmlid = "";
                                                                            failure_htmlid = "lsmithhh";
                                                                        }
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80074) {
                                                                        npc = (L1NpcInstance)obj;
                                                                        if (pc.getKarma() >= 10000000) {
                                                                            this.getSoulCrystalByKarma(pc, npc, s);
                                                                        }

                                                                        htmlid = "";
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80057) {
                                                                        htmlid = this.karmaLevelToHtmlId(pc.getKarmaLevel());
                                                                        htmldata = new String[]{String.valueOf(pc.getKarmaPercent())};
                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 80059 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 80060 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 80061 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 80062) {
                                                                        if (!s.equalsIgnoreCase("pandora6") && !s.equalsIgnoreCase("cold6") && !s.equalsIgnoreCase("balsim3") && !s.equalsIgnoreCase("mellin3") && !s.equalsIgnoreCase("glen3")) {
                                                                            L1Town town;
                                                                            if (s.equalsIgnoreCase("set")) {
                                                                                if (obj instanceof L1NpcInstance) {
                                                                                    html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                                                                                    htmlB = L1TownLocation.getTownIdByNpcid(html);
                                                                                    if (htmlB >= 1 && htmlB <= 10) {
                                                                                        if (pc.getHomeTownId() == -1) {
                                                                                            pc.sendPackets(new S_ServerMessage(759));
                                                                                            htmlid = "";
                                                                                        } else if (pc.getHomeTownId() > 0) {
                                                                                            if (pc.getHomeTownId() != htmlB) {
                                                                                                town = TownReading.get().getTownTable(pc.getHomeTownId());
                                                                                                if (town != null) {
                                                                                                    pc.sendPackets(new S_ServerMessage(758, town.get_name()));
                                                                                                }

                                                                                                htmlid = "";
                                                                                            } else {
                                                                                                htmlid = "";
                                                                                            }
                                                                                        } else if (pc.getHomeTownId() == 0) {
                                                                                            if (pc.getLevel() < 10) {
                                                                                                pc.sendPackets(new S_ServerMessage(757));
                                                                                                htmlid = "";
                                                                                            } else {
                                                                                                i = pc.getLevel();
                                                                                                cost = i * i * 10;
                                                                                                if (pc.getInventory().consumeItem(40308, cost)) {
                                                                                                    pc.setHomeTownId(htmlB);
                                                                                                    pc.setContribution(0);
                                                                                                    pc.save();
                                                                                                } else {
                                                                                                    pc.sendPackets(new S_ServerMessage(337, "$4"));
                                                                                                }

                                                                                                htmlid = "";
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if (s.equalsIgnoreCase("clear")) {
                                                                                if (obj instanceof L1NpcInstance) {
                                                                                    html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                                                                                    htmlB = L1TownLocation.getTownIdByNpcid(html);
                                                                                    if (htmlB > 0) {
                                                                                        if (pc.getHomeTownId() > 0) {
                                                                                            if (pc.getHomeTownId() == htmlB) {
                                                                                                pc.setHomeTownId(-1);
                                                                                                pc.setContribution(0);
                                                                                                pc.save();
                                                                                            } else {
                                                                                                pc.sendPackets(new S_ServerMessage(756));
                                                                                            }
                                                                                        }

                                                                                        htmlid = "";
                                                                                    }
                                                                                }
                                                                            } else if (s.equalsIgnoreCase("ask")) {
                                                                                if (obj instanceof L1NpcInstance) {
                                                                                    html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                                                                                    htmlB = L1TownLocation.getTownIdByNpcid(html);
                                                                                    if (htmlB >= 1 && htmlB <= 10) {
                                                                                        town = TownReading.get().getTownTable(htmlB);
                                                                                        itemName = town.get_leader_name();
                                                                                        if (itemName != null && itemName.length() != 0) {
                                                                                            htmlid = "owner";
                                                                                            htmldata = new String[]{itemName};
                                                                                        } else {
                                                                                            htmlid = "noowner";
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70534 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70556 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70572 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70631 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70663 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70761 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70788 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70806 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70830 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 70876) {
                                                                                if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 70512) {
                                                                                    if (s.equalsIgnoreCase("0") || s.equalsIgnoreCase("fullheal")) {
                                                                                        html = _random.nextInt(21) + 70;
                                                                                        pc.setCurrentHp(pc.getCurrentHp() + html);
                                                                                        pc.sendPackets(new S_PacketBoxHpMsg());
                                                                                        pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                                                                                        pc.sendPackets(new S_HPUpdate(pc));
                                                                                        htmlid = "";
                                                                                    }
                                                                                } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71037) {
                                                                                    if (s.equalsIgnoreCase("0")) {
                                                                                        pc.setCurrentHp(pc.getMaxHp());
                                                                                        pc.setCurrentMp(pc.getMaxMp());
                                                                                        pc.sendPackets(new S_PacketBoxHpMsg());
                                                                                        pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                                                                                        pc.sendPackets(new S_HPUpdate(pc));
                                                                                        pc.sendPackets(new S_MPUpdate(pc));
                                                                                    }
                                                                                } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71030) {
                                                                                    if (s.equalsIgnoreCase("fullheal")) {
                                                                                        if (pc.getInventory().checkItem(40308, 5L)) {
                                                                                            pc.getInventory().consumeItem(40308, 5L);
                                                                                            pc.setCurrentHp(pc.getMaxHp());
                                                                                            pc.setCurrentMp(pc.getMaxMp());
                                                                                            pc.sendPackets(new S_PacketBoxHpMsg());
                                                                                            pc.sendPackets(new S_SkillSound(pc.getId(), 830));
                                                                                            pc.sendPackets(new S_HPUpdate(pc));
                                                                                            pc.sendPackets(new S_MPUpdate(pc));
                                                                                            if (pc.isInParty()) {
                                                                                                pc.getParty().updateMiniHP(pc);
                                                                                            }
                                                                                        } else {
                                                                                            pc.sendPackets(new S_ServerMessage(337, "$4"));
                                                                                        }
                                                                                    }
                                                                                } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71002) {
                                                                                    if (s.equalsIgnoreCase("0") && pc.getLevel() <= 13) {
                                                                                        L1SkillUse skillUse = new L1SkillUse();
                                                                                        skillUse.handleCommands(pc, 44, pc.getId(), pc.getX(), pc.getY(), 0, 3, (L1NpcInstance)obj);
                                                                                        htmlid = "";
                                                                                    }
                                                                                } else {

                                                                                    int[] item_amounts;
                                                                                    int[] item_ids;
                                                                                    if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71025) {
                                                                                        if (s.equalsIgnoreCase("0")) {
                                                                                            item_ids = new int[]{41225};
                                                                                            item_amounts = new int[]{1};

                                                                                            for(i = 0; i < item_ids.length; ++i) {
                                                                                                item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                            }

                                                                                            htmlid = "jpe0083";
                                                                                        }
                                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71055) {
                                                                                        if (s.equalsIgnoreCase("0")) {
                                                                                            item_ids = new int[]{40701};
                                                                                            item_amounts = new int[]{1};

                                                                                            for(i = 0; i < item_ids.length; ++i) {
                                                                                                item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                            }

                                                                                            pc.getQuest().set_step(23, 1);
                                                                                            htmlid = "lukein8";
                                                                                        } else if (s.equalsIgnoreCase("1")) {
                                                                                            pc.getQuest().set_step(23, 11);
                                                                                            htmlid = "lukein0";
                                                                                            item_ids = new int[]{20269};
                                                                                            item_amounts = new int[]{1};

                                                                                            for(i = 0; i < item_ids.length; ++i) {
                                                                                                item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                            }

                                                                                            pc.getInventory().consumeItem(40716, 1L);
                                                                                            pc.getQuest().set_end(23);
                                                                                        } else if (s.equalsIgnoreCase("2")) {
                                                                                            htmlid = "lukein12";
                                                                                            pc.getQuest().set_step(30, 3);
                                                                                        }
                                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71063) {
                                                                                        if (s.equalsIgnoreCase("0")) {
                                                                                            materials = new int[]{40701};
                                                                                            counts = new int[]{1};
                                                                                            createitem = new int[]{40702};
                                                                                            createcount = new int[]{1};
                                                                                            htmlid = "maptbox1";
                                                                                            pc.getQuest().set_end(24);
                                                                                            item_ids = new int[]{1, 2, 3};
                                                                                            htmlB = _random.nextInt(item_ids.length);
                                                                                            i = item_ids[htmlB];
                                                                                            if (i == 1) {
                                                                                                pc.getQuest().set_step(23, 2);
                                                                                            } else if (i == 2) {
                                                                                                pc.getQuest().set_step(23, 3);
                                                                                            } else if (i == 3) {
                                                                                                pc.getQuest().set_step(23, 4);
                                                                                            }
                                                                                        }
                                                                                    } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71064 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71065 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71066) {
                                                                                        if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71067 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71068 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71069 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71070 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71071 && ((L1NpcInstance)obj).getNpcTemplate().get_npcId() != 71072) {
                                                                                            if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71056) {
                                                                                                if (s.equalsIgnoreCase("a")) {
                                                                                                    pc.getQuest().set_step(27, 1);
                                                                                                    htmlid = "SIMIZZ7";
                                                                                                } else if (s.equalsIgnoreCase("b")) {
                                                                                                    if (pc.getInventory().checkItem(40661) && pc.getInventory().checkItem(40662) && pc.getInventory().checkItem(40663)) {
                                                                                                        htmlid = "SIMIZZ8";
                                                                                                        pc.getQuest().set_step(27, 2);
                                                                                                        materials = new int[]{40661, 40662, 40663};
                                                                                                        counts = new int[]{1, 1, 1};
                                                                                                        createitem = new int[]{20044};
                                                                                                        createcount = new int[]{1};
                                                                                                    } else {
                                                                                                        htmlid = "SIMIZZ9";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("d")) {
                                                                                                    htmlid = "SIMIZZ12";
                                                                                                    pc.getQuest().set_step(27, 255);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71057) {
                                                                                                if (s.equalsIgnoreCase("3")) {
                                                                                                    htmlid = "doil4";
                                                                                                } else if (s.equalsIgnoreCase("6")) {
                                                                                                    htmlid = "doil6";
                                                                                                } else if (s.equalsIgnoreCase("1")) {
                                                                                                    if (pc.getInventory().checkItem(40714)) {
                                                                                                        htmlid = "doil8";
                                                                                                        materials = new int[]{40714};
                                                                                                        counts = new int[]{1};
                                                                                                        createitem = new int[]{40647};
                                                                                                        createcount = new int[]{1};
                                                                                                        pc.getQuest().set_step(28, 255);
                                                                                                    } else {
                                                                                                        htmlid = "doil7";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71059) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    htmlid = "rudian6";
                                                                                                    item_ids = new int[]{40700};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                    }

                                                                                                    pc.getQuest().set_step(29, 1);
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    if (pc.getInventory().checkItem(40710)) {
                                                                                                        htmlid = "rudian8";
                                                                                                        materials = new int[]{40700, 40710};
                                                                                                        counts = new int[]{1, 1};
                                                                                                        createitem = new int[]{40647};
                                                                                                        createcount = new int[]{1};
                                                                                                        pc.getQuest().set_step(29, 255);
                                                                                                    } else {
                                                                                                        htmlid = "rudian9";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71060) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    if (pc.getQuest().get_step(29) == 255) {
                                                                                                        htmlid = "resta6";
                                                                                                    } else {
                                                                                                        htmlid = "resta4";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    htmlid = "resta10";
                                                                                                    pc.getQuest().set_step(30, 2);
                                                                                                } else if (s.equalsIgnoreCase("D")) {
                                                                                                    htmlid = "resta15";
                                                                                                    pc.getQuest().set_end(30);
                                                                                                    item_ids = new int[]{40647};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                    }

                                                                                                    pc.getInventory().consumeItem(40631, 1L);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71061) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    if (pc.getInventory().checkItem(40647, 3L)) {
                                                                                                        htmlid = "cadmus6";
                                                                                                        pc.getInventory().consumeItem(40647, 3L);
                                                                                                        pc.getQuest().set_step(31, 2);
                                                                                                    } else {
                                                                                                        htmlid = "cadmus5";
                                                                                                        pc.getQuest().set_step(31, 1);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    pc.getQuest().set_end(31);
                                                                                                    htmlid = "cadmus1";
                                                                                                    item_ids = new int[]{40692};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                    }

                                                                                                    pc.getInventory().consumeItem(40711, 1L);
                                                                                                    pc.getQuest().set_step(31, 255);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71036) {
                                                                                                if (s.equalsIgnoreCase("a")) {
                                                                                                    htmlid = "kamyla7";
                                                                                                    pc.getQuest().set_step(32, 1);
                                                                                                } else if (s.equalsIgnoreCase("c")) {
                                                                                                    htmlid = "kamyla10";
                                                                                                    pc.getInventory().consumeItem(40644, 1L);
                                                                                                    pc.getQuest().set_step(32, 3);
                                                                                                } else if (s.equalsIgnoreCase("e")) {
                                                                                                    htmlid = "kamyla13";
                                                                                                    pc.getInventory().consumeItem(40630, 1L);
                                                                                                    pc.getQuest().set_step(32, 4);
                                                                                                } else if (s.equalsIgnoreCase("g")) {
                                                                                                    if (pc.getQuest().get_step(32) == 4 && pc.getInventory().checkItem(40717)) {
                                                                                                        htmlid = "kamyla25";
                                                                                                        item_ids = new int[]{33};
                                                                                                        item_amounts = new int[]{1};

                                                                                                        for(i = 0; i < item_ids.length; ++i) {
                                                                                                            item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(40717, 1L);
                                                                                                        pc.getQuest().set_end(32);
                                                                                                        pc.getQuest().set_end(33);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("h")) {
                                                                                                    if (pc.getQuest().get_step(32) == 4 && pc.getInventory().checkItem(40717)) {
                                                                                                        htmlid = "kamyla25";
                                                                                                        item_ids = new int[]{79};
                                                                                                        item_amounts = new int[]{1};

                                                                                                        for(i = 0; i < item_ids.length; ++i) {
                                                                                                            item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(40717, 1L);
                                                                                                        pc.getQuest().set_end(32);
                                                                                                        pc.getQuest().set_end(33);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("i")) {
                                                                                                    if (pc.getQuest().get_step(32) == 4 && pc.getInventory().checkItem(40717)) {
                                                                                                        htmlid = "kamyla25";
                                                                                                        item_ids = new int[]{178};
                                                                                                        item_amounts = new int[]{1};

                                                                                                        for(i = 0; i < item_ids.length; ++i) {
                                                                                                            item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(40717, 1L);
                                                                                                        pc.getQuest().set_end(32);
                                                                                                        pc.getQuest().set_end(33);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("k")) {
                                                                                                    if (pc.getQuest().get_step(32) == 4 && pc.getInventory().checkItem(40717)) {
                                                                                                        htmlid = "kamyla25";
                                                                                                        item_ids = new int[]{20234};
                                                                                                        item_amounts = new int[]{1};

                                                                                                        for(i = 0; i < item_ids.length; ++i) {
                                                                                                            item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(40717, 1L);
                                                                                                        pc.getQuest().set_end(32);
                                                                                                        pc.getQuest().set_end(33);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("j")) {
                                                                                                    if (pc.getQuest().get_step(32) == 4 && pc.getInventory().checkItem(40717)) {
                                                                                                        htmlid = "kamyla25";
                                                                                                        item_ids = new int[]{118};
                                                                                                        item_amounts = new int[]{1};

                                                                                                        for(i = 0; i < item_ids.length; ++i) {
                                                                                                            item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(40717, 1L);
                                                                                                        pc.getQuest().set_end(32);
                                                                                                        pc.getQuest().set_end(33);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("b")) {
                                                                                                    if (pc.getQuest().get_step(32) == 1) {
                                                                                                        L1Teleport.teleport(pc, 32679, 32742, (short)482, 5, true);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("d")) {
                                                                                                    if (pc.getQuest().get_step(32) == 3) {
                                                                                                        L1Teleport.teleport(pc, 32736, 32800, (short)483, 5, true);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("f") && pc.getQuest().get_step(32) == 4) {
                                                                                                    L1Teleport.teleport(pc, 32746, 32807, (short)484, 5, true);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71089) {
                                                                                                if (s.equalsIgnoreCase("a")) {
                                                                                                    htmlid = "francu10";
                                                                                                    item_ids = new int[]{40644};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        pc.getQuest().set_step(32, 2);
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71090) {
                                                                                                if (s.equalsIgnoreCase("a")) {
                                                                                                    htmlid = "";
                                                                                                    item_ids = new int[]{246, 247, 248, 249, 40660};
                                                                                                    item_amounts = new int[]{1, 1, 1, 1, 5};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], (long)item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        pc.getQuest().set_step(33, 1);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("b")) {
                                                                                                    if (!pc.getInventory().checkEquipped(246) && !pc.getInventory().checkEquipped(247) && !pc.getInventory().checkEquipped(248) && !pc.getInventory().checkEquipped(249)) {
                                                                                                        if (pc.getInventory().checkItem(40660)) {
                                                                                                            htmlid = "jcrystal4";
                                                                                                        } else {
                                                                                                            pc.getInventory().consumeItem(246, 1L);
                                                                                                            pc.getInventory().consumeItem(247, 1L);
                                                                                                            pc.getInventory().consumeItem(248, 1L);
                                                                                                            pc.getInventory().consumeItem(249, 1L);
                                                                                                            pc.getInventory().consumeItem(40620, 1L);
                                                                                                            pc.getQuest().set_step(33, 2);
                                                                                                            L1Teleport.teleport(pc, 32801, 32895, (short)483, 4, true);
                                                                                                        }
                                                                                                    } else {
                                                                                                        htmlid = "jcrystal5";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("c")) {
                                                                                                    if (!pc.getInventory().checkEquipped(246) && !pc.getInventory().checkEquipped(247) && !pc.getInventory().checkEquipped(248) && !pc.getInventory().checkEquipped(249)) {
                                                                                                        pc.getInventory().checkItem(40660);
                                                                                                        item = pc.getInventory().findItemId(40660);
                                                                                                        long sc = item.getCount();
                                                                                                        if (sc > 0L) {
                                                                                                            pc.getInventory().consumeItem(40660, sc);
                                                                                                        }

                                                                                                        pc.getInventory().consumeItem(246, 1L);
                                                                                                        pc.getInventory().consumeItem(247, 1L);
                                                                                                        pc.getInventory().consumeItem(248, 1L);
                                                                                                        pc.getInventory().consumeItem(249, 1L);
                                                                                                        pc.getInventory().consumeItem(40620, 1L);
                                                                                                        pc.getQuest().set_step(33, 0);
                                                                                                        L1Teleport.teleport(pc, 32736, 32800, (short)483, 4, true);
                                                                                                    } else {
                                                                                                        htmlid = "jcrystal5";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71091) {
                                                                                                if (s.equalsIgnoreCase("a")) {
                                                                                                    htmlid = "";
                                                                                                    pc.getInventory().consumeItem(40654, 1L);
                                                                                                    pc.getQuest().set_step(33, 255);
                                                                                                    L1Teleport.teleport(pc, 32744, 32927, (short)483, 4, true);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71074) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    htmlid = "lelder5";
                                                                                                    pc.getQuest().set_step(34, 1);
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    htmlid = "lelder10";
                                                                                                    pc.getInventory().consumeItem(40633, 1L);
                                                                                                    pc.getQuest().set_step(34, 3);
                                                                                                } else if (s.equalsIgnoreCase("C")) {
                                                                                                    htmlid = "lelder13";
                                                                                                    pc.getQuest().get_step(34);
                                                                                                    materials = new int[]{40634};
                                                                                                    counts = new int[]{1};
                                                                                                    createitem = new int[]{20167};
                                                                                                    createcount = new int[]{1};
                                                                                                    pc.getQuest().set_step(34, 255);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80079) {
                                                                                                if (s.equalsIgnoreCase("0")) {
                                                                                                    if (!pc.getInventory().checkItem(41312)) {
                                                                                                        item = pc.getInventory().storeItem(41312, 1L);
                                                                                                        if (item != null) {
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                            pc.getQuest().set_step(35, 255);
                                                                                                        }

                                                                                                        htmlid = "keplisha7";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("1")) {
                                                                                                    if (!pc.getInventory().checkItem(41314)) {
                                                                                                        if (pc.getInventory().checkItem(40308, 1000L)) {
                                                                                                            materials = new int[]{40308, 41313};
                                                                                                            counts = new int[]{1000, 1};
                                                                                                            createitem = new int[]{41314};
                                                                                                            createcount = new int[]{1};
                                                                                                            html = _random.nextInt(3) + 1;
                                                                                                            htmlB = _random.nextInt(100) + 1;
                                                                                                            switch(html) {
                                                                                                                case 1:
                                                                                                                    htmlid = "horosa" + htmlB;
                                                                                                                    break;
                                                                                                                case 2:
                                                                                                                    htmlid = "horosb" + htmlB;
                                                                                                                    break;
                                                                                                                case 3:
                                                                                                                    htmlid = "horosc" + htmlB;
                                                                                                            }
                                                                                                        } else {
                                                                                                            htmlid = "keplisha8";
                                                                                                        }
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("2")) {
                                                                                                    if (pc.getTempCharGfx() != pc.getClassId()) {
                                                                                                        htmlid = "keplisha9";
                                                                                                    } else if (pc.getInventory().checkItem(41314)) {
                                                                                                        pc.getInventory().consumeItem(41314, 1L);
                                                                                                        html = _random.nextInt(9) + 1;
                                                                                                        htmlB = 6180 + _random.nextInt(64);
                                                                                                        this.polyByKeplisha(client, htmlB);
                                                                                                        switch(html) {
                                                                                                            case 1:
                                                                                                                htmlid = "horomon11";
                                                                                                                break;
                                                                                                            case 2:
                                                                                                                htmlid = "horomon12";
                                                                                                                break;
                                                                                                            case 3:
                                                                                                                htmlid = "horomon13";
                                                                                                                break;
                                                                                                            case 4:
                                                                                                                htmlid = "horomon21";
                                                                                                                break;
                                                                                                            case 5:
                                                                                                                htmlid = "horomon22";
                                                                                                                break;
                                                                                                            case 6:
                                                                                                                htmlid = "horomon23";
                                                                                                                break;
                                                                                                            case 7:
                                                                                                                htmlid = "horomon31";
                                                                                                                break;
                                                                                                            case 8:
                                                                                                                htmlid = "horomon32";
                                                                                                                break;
                                                                                                            case 9:
                                                                                                                htmlid = "horomon33";
                                                                                                        }
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("3")) {
                                                                                                    if (pc.getInventory().checkItem(41312)) {
                                                                                                        pc.getInventory().consumeItem(41312, 1L);
                                                                                                        htmlid = "";
                                                                                                    }

                                                                                                    if (pc.getInventory().checkItem(41313)) {
                                                                                                        pc.getInventory().consumeItem(41313, 1L);
                                                                                                        htmlid = "";
                                                                                                    }

                                                                                                    if (pc.getInventory().checkItem(41314)) {
                                                                                                        pc.getInventory().consumeItem(41314, 1L);
                                                                                                        htmlid = "";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80084) {
                                                                                                if (s.equalsIgnoreCase("q")) {
                                                                                                    if (pc.getInventory().checkItem(41356, 1L)) {
                                                                                                        htmlid = "rparum4";
                                                                                                    } else {
                                                                                                        item = pc.getInventory().storeItem(41356, 1L);
                                                                                                        if (item != null) {
                                                                                                            pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        }

                                                                                                        htmlid = "rparum3";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80105) {
                                                                                                if (s.equalsIgnoreCase("c") && pc.isCrown() && pc.getInventory().checkItem(20383, 1L)) {
                                                                                                    if (pc.getInventory().checkItem(40308, 100000L)) {
                                                                                                        item = pc.getInventory().findItemId(20383);
                                                                                                        if (item != null && item.getChargeCount() != 50) {
                                                                                                            item.setChargeCount(50);
                                                                                                            pc.getInventory().updateItem(item, 128);
                                                                                                            pc.getInventory().consumeItem(40308, 100000L);
                                                                                                            htmlid = "";
                                                                                                        }
                                                                                                    } else {
                                                                                                        pc.sendPackets(new S_ServerMessage(337, "$4"));
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71126) {
                                                                                                if (s.equalsIgnoreCase("B")) {
                                                                                                    if (pc.getInventory().checkItem(41007, 1L)) {
                                                                                                        htmlid = "eris10";
                                                                                                    } else {
                                                                                                        npc = (L1NpcInstance)obj;
                                                                                                        item = pc.getInventory().storeItem(41007, 1L);
                                                                                                        npcName = npc.getNpcTemplate().get_name();
                                                                                                        itemName = item.getItem().getName();
                                                                                                        pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                                                        htmlid = "eris6";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("C")) {
                                                                                                    if (pc.getInventory().checkItem(41009, 1L)) {
                                                                                                        htmlid = "eris10";
                                                                                                    } else {
                                                                                                        npc = (L1NpcInstance)obj;
                                                                                                        item = pc.getInventory().storeItem(41009, 1L);
                                                                                                        npcName = npc.getNpcTemplate().get_name();
                                                                                                        itemName = item.getItem().getName();
                                                                                                        pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                                                        htmlid = "eris8";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("A")) {
                                                                                                    if (pc.getInventory().checkItem(41007, 1L)) {
                                                                                                        if (pc.getInventory().checkItem(40969, 20L)) {
                                                                                                            htmlid = "eris18";
                                                                                                            materials = new int[]{40969, 41007};
                                                                                                            counts = new int[]{20, 1};
                                                                                                            createitem = new int[]{41008};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else {
                                                                                                            htmlid = "eris5";
                                                                                                        }
                                                                                                    } else {
                                                                                                        htmlid = "eris2";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("E")) {
                                                                                                    if (pc.getInventory().checkItem(41010, 1L)) {
                                                                                                        htmlid = "eris19";
                                                                                                    } else {
                                                                                                        htmlid = "eris7";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("D")) {
                                                                                                    if (pc.getInventory().checkItem(41010, 1L)) {
                                                                                                        htmlid = "eris19";
                                                                                                    } else if (pc.getInventory().checkItem(41009, 1L)) {
                                                                                                        if (pc.getInventory().checkItem(40959, 1L)) {
                                                                                                            htmlid = "eris17";
                                                                                                            materials = new int[]{40959, 41009};
                                                                                                            counts = new int[]{1, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40960, 1L)) {
                                                                                                            htmlid = "eris16";
                                                                                                            materials = new int[]{40960, 41009};
                                                                                                            counts = new int[]{1, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40961, 1L)) {
                                                                                                            htmlid = "eris15";
                                                                                                            materials = new int[]{40961, 41009};
                                                                                                            counts = new int[]{1, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40962, 1L)) {
                                                                                                            htmlid = "eris14";
                                                                                                            materials = new int[]{40962, 41009};
                                                                                                            counts = new int[]{1, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40635, 10L)) {
                                                                                                            htmlid = "eris12";
                                                                                                            materials = new int[]{40635, 41009};
                                                                                                            counts = new int[]{10, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40638, 10L)) {
                                                                                                            htmlid = "eris11";
                                                                                                            materials = new int[]{40638, 41009};
                                                                                                            counts = new int[]{10, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40642, 10L)) {
                                                                                                            htmlid = "eris13";
                                                                                                            materials = new int[]{40642, 41009};
                                                                                                            counts = new int[]{10, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else if (pc.getInventory().checkItem(40667, 10L)) {
                                                                                                            htmlid = "eris13";
                                                                                                            materials = new int[]{40667, 41009};
                                                                                                            counts = new int[]{10, 1};
                                                                                                            createitem = new int[]{41010};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else {
                                                                                                            htmlid = "eris8";
                                                                                                        }
                                                                                                    } else {
                                                                                                        htmlid = "eris7";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80076) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    item_ids = new int[]{49082, 49083};
                                                                                                    htmlB = _random.nextInt(item_ids.length);
                                                                                                    i = item_ids[htmlB];

                                                                                                    if (i == 49082) {
                                                                                                        htmlid = "voyager6a";
                                                                                                        npc = (L1NpcInstance)obj;
                                                                                                        item = pc.getInventory().storeItem(i, 1L);
                                                                                                        npcName = npc.getNpcTemplate().get_name();
                                                                                                        itemName = item.getItem().getName();
                                                                                                        pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                                                    } else if (i == 49083) {
                                                                                                        htmlid = "voyager6b";
                                                                                                        npc = (L1NpcInstance)obj;
                                                                                                        item = pc.getInventory().storeItem(i, 1L);
                                                                                                        npcName = npc.getNpcTemplate().get_name();
                                                                                                        itemName = item.getItem().getName();
                                                                                                        pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71128) {
                                                                                                if (s.equals("A")) {
                                                                                                    if (pc.getInventory().checkItem(41010, 1L)) {
                                                                                                        htmlid = "perita2";
                                                                                                    } else {
                                                                                                        htmlid = "perita3";
                                                                                                    }
                                                                                                } else if (s.equals("p")) {
                                                                                                    if (pc.getInventory().checkItem(40987, 1L) && pc.getInventory().checkItem(40988, 1L) && pc.getInventory().checkItem(40989, 1L)) {
                                                                                                        htmlid = "perita43";
                                                                                                    } else if (pc.getInventory().checkItem(40987, 1L) && pc.getInventory().checkItem(40989, 1L)) {
                                                                                                        htmlid = "perita44";
                                                                                                    } else if (pc.getInventory().checkItem(40987, 1L) && pc.getInventory().checkItem(40988, 1L)) {
                                                                                                        htmlid = "perita45";
                                                                                                    } else if (pc.getInventory().checkItem(40988, 1L) && pc.getInventory().checkItem(40989, 1L)) {
                                                                                                        htmlid = "perita47";
                                                                                                    } else if (pc.getInventory().checkItem(40987, 1L)) {
                                                                                                        htmlid = "perita46";
                                                                                                    } else if (pc.getInventory().checkItem(40988, 1L)) {
                                                                                                        htmlid = "perita49";
                                                                                                    } else if (pc.getInventory().checkItem(40987, 1L)) {
                                                                                                        htmlid = "perita48";
                                                                                                    } else {
                                                                                                        htmlid = "perita50";
                                                                                                    }
                                                                                                } else if (s.equals("q")) {
                                                                                                    if (pc.getInventory().checkItem(41173, 1L) && pc.getInventory().checkItem(41174, 1L) && pc.getInventory().checkItem(41175, 1L)) {
                                                                                                        htmlid = "perita54";
                                                                                                    } else if (pc.getInventory().checkItem(41173, 1L) && pc.getInventory().checkItem(41175, 1L)) {
                                                                                                        htmlid = "perita55";
                                                                                                    } else if (pc.getInventory().checkItem(41173, 1L) && pc.getInventory().checkItem(41174, 1L)) {
                                                                                                        htmlid = "perita56";
                                                                                                    } else if (pc.getInventory().checkItem(41174, 1L) && pc.getInventory().checkItem(41175, 1L)) {
                                                                                                        htmlid = "perita58";
                                                                                                    } else if (pc.getInventory().checkItem(41174, 1L)) {
                                                                                                        htmlid = "perita57";
                                                                                                    } else if (pc.getInventory().checkItem(41175, 1L)) {
                                                                                                        htmlid = "perita60";
                                                                                                    } else if (pc.getInventory().checkItem(41176, 1L)) {
                                                                                                        htmlid = "perita59";
                                                                                                    } else {
                                                                                                        htmlid = "perita61";
                                                                                                    }
                                                                                                } else if (s.equals("s")) {
                                                                                                    if (pc.getInventory().checkItem(41161, 1L) && pc.getInventory().checkItem(41162, 1L) && pc.getInventory().checkItem(41163, 1L)) {
                                                                                                        htmlid = "perita62";
                                                                                                    } else if (pc.getInventory().checkItem(41161, 1L) && pc.getInventory().checkItem(41163, 1L)) {
                                                                                                        htmlid = "perita63";
                                                                                                    } else if (pc.getInventory().checkItem(41161, 1L) && pc.getInventory().checkItem(41162, 1L)) {
                                                                                                        htmlid = "perita64";
                                                                                                    } else if (pc.getInventory().checkItem(41162, 1L) && pc.getInventory().checkItem(41163, 1L)) {
                                                                                                        htmlid = "perita66";
                                                                                                    } else if (pc.getInventory().checkItem(41161, 1L)) {
                                                                                                        htmlid = "perita65";
                                                                                                    } else if (pc.getInventory().checkItem(41162, 1L)) {
                                                                                                        htmlid = "perita68";
                                                                                                    } else if (pc.getInventory().checkItem(41163, 1L)) {
                                                                                                        htmlid = "perita67";
                                                                                                    } else {
                                                                                                        htmlid = "perita69";
                                                                                                    }
                                                                                                } else if (s.equals("B")) {
                                                                                                    if (pc.getInventory().checkItem(40651, 10L) && pc.getInventory().checkItem(40643, 10L) && pc.getInventory().checkItem(40618, 10L) && pc.getInventory().checkItem(40645, 10L) && pc.getInventory().checkItem(40676, 10L) && pc.getInventory().checkItem(40442, 5L) && pc.getInventory().checkItem(40051, 1L)) {
                                                                                                        htmlid = "perita7";
                                                                                                        materials = new int[]{40651, 40643, 40618, 40645, 40676, 40442, 40051};
                                                                                                        counts = new int[]{10, 10, 10, 10, 20, 5, 1};
                                                                                                        createitem = new int[]{40925};
                                                                                                        createcount = new int[]{1};
                                                                                                    } else {
                                                                                                        htmlid = "perita8";
                                                                                                    }
                                                                                                } else if (!s.equals("G") && !s.equals("h") && !s.equals("i")) {
                                                                                                    if (!s.equals("H") && !s.equals("j") && !s.equals("k")) {
                                                                                                        if (!s.equals("I") && !s.equals("l") && !s.equals("m")) {
                                                                                                            if (!s.equals("J") && !s.equals("n") && !s.equals("o")) {
                                                                                                                if (s.equals("K")) {
                                                                                                                    int earinga = 0;
                                                                                                                    int earingb = 0;
                                                                                                                    if (!pc.getInventory().checkEquipped(21014) && !pc.getInventory().checkEquipped(21006) && !pc.getInventory().checkEquipped(21007)) {
                                                                                                                        if (pc.getInventory().checkItem(21014, 1L)) {
                                                                                                                            earinga = 21014;
                                                                                                                            earingb = 'ꃘ';
                                                                                                                        } else if (pc.getInventory().checkItem(21006, 1L)) {
                                                                                                                            earinga = 21006;
                                                                                                                            earingb = 'ꃙ';
                                                                                                                        } else if (pc.getInventory().checkItem(21007, 1L)) {
                                                                                                                            earinga = 21007;
                                                                                                                            earingb = 'ꃚ';
                                                                                                                        } else {
                                                                                                                            htmlid = "perita36";
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        htmlid = "perita36";
                                                                                                                    }

                                                                                                                    if (earinga > 0) {
                                                                                                                        materials = new int[]{earinga};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{earingb};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    }
                                                                                                                } else if (s.equals("L")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21015)) {
                                                                                                                        htmlid = "perita22";
                                                                                                                    } else if (pc.getInventory().checkItem(21015, 1L)) {
                                                                                                                        materials = new int[]{21015};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41179};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita22";
                                                                                                                    }
                                                                                                                } else if (s.equals("M")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21016)) {
                                                                                                                        htmlid = "perita26";
                                                                                                                    } else if (pc.getInventory().checkItem(21016, 1L)) {
                                                                                                                        materials = new int[]{21016};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41182};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita26";
                                                                                                                    }
                                                                                                                } else if (s.equals("b")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21009)) {
                                                                                                                        htmlid = "perita39";
                                                                                                                    } else if (pc.getInventory().checkItem(21009, 1L)) {
                                                                                                                        materials = new int[]{21009};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41180};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita39";
                                                                                                                    }
                                                                                                                } else if (s.equals("d")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21012)) {
                                                                                                                        htmlid = "perita41";
                                                                                                                    } else if (pc.getInventory().checkItem(21012, 1L)) {
                                                                                                                        materials = new int[]{21012};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41183};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita41";
                                                                                                                    }
                                                                                                                } else if (s.equals("a")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21008)) {
                                                                                                                        htmlid = "perita38";
                                                                                                                    } else if (pc.getInventory().checkItem(21008, 1L)) {
                                                                                                                        materials = new int[]{21008};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41181};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita38";
                                                                                                                    }
                                                                                                                } else if (s.equals("c")) {
                                                                                                                    if (pc.getInventory().checkEquipped(21010)) {
                                                                                                                        htmlid = "perita40";
                                                                                                                    } else if (pc.getInventory().checkItem(21010, 1L)) {
                                                                                                                        materials = new int[]{21010};
                                                                                                                        counts = new int[]{1};
                                                                                                                        createitem = new int[]{41184};
                                                                                                                        createcount = new int[]{1};
                                                                                                                    } else {
                                                                                                                        htmlid = "perita40";
                                                                                                                    }
                                                                                                                }
                                                                                                            } else if (pc.getInventory().checkItem(40651, 30L) && pc.getInventory().checkItem(40643, 30L) && pc.getInventory().checkItem(40618, 30L) && pc.getInventory().checkItem(40645, 30L) && pc.getInventory().checkItem(40676, 30L) && pc.getInventory().checkItem(40675, 20L) && pc.getInventory().checkItem(40052, 1L) && pc.getInventory().checkItem(40051, 1L)) {
                                                                                                                htmlid = "perita33";
                                                                                                                materials = new int[]{40651, 40643, 40618, 40645, 40676, 40675, 40052, 40051};
                                                                                                                counts = new int[]{30, 30, 30, 30, 30, 20, 1, 1};
                                                                                                                createitem = new int[]{40928};
                                                                                                                createcount = new int[]{1};
                                                                                                            } else {
                                                                                                                htmlid = "perita34";
                                                                                                            }
                                                                                                        } else if (pc.getInventory().checkItem(40651, 20L) && pc.getInventory().checkItem(40643, 20L) && pc.getInventory().checkItem(40618, 20L) && pc.getInventory().checkItem(40645, 20L) && pc.getInventory().checkItem(40676, 30L) && pc.getInventory().checkItem(40675, 10L) && pc.getInventory().checkItem(40050, 3L) && pc.getInventory().checkItem(40051, 1L)) {
                                                                                                            htmlid = "perita31";
                                                                                                            materials = new int[]{40651, 40643, 40618, 40645, 40676, 40675, 40050, 40051};
                                                                                                            counts = new int[]{20, 20, 20, 20, 30, 10, 3, 1};
                                                                                                            createitem = new int[]{40928};
                                                                                                            createcount = new int[]{1};
                                                                                                        } else {
                                                                                                            htmlid = "perita32";
                                                                                                        }
                                                                                                    } else if (pc.getInventory().checkItem(40651, 10L) && pc.getInventory().checkItem(40643, 10L) && pc.getInventory().checkItem(40618, 10L) && pc.getInventory().checkItem(40645, 10L) && pc.getInventory().checkItem(40676, 20L) && pc.getInventory().checkItem(40675, 10L) && pc.getInventory().checkItem(40048, 3L) && pc.getInventory().checkItem(40051, 1L)) {
                                                                                                        htmlid = "perita29";
                                                                                                        materials = new int[]{40651, 40643, 40618, 40645, 40676, 40675, 40048, 40051};
                                                                                                        counts = new int[]{10, 10, 10, 10, 20, 10, 3, 1};
                                                                                                        createitem = new int[]{40927};
                                                                                                        createcount = new int[]{1};
                                                                                                    } else {
                                                                                                        htmlid = "perita30";
                                                                                                    }
                                                                                                } else if (pc.getInventory().checkItem(40651, 5L) && pc.getInventory().checkItem(40643, 5L) && pc.getInventory().checkItem(40618, 5L) && pc.getInventory().checkItem(40645, 5L) && pc.getInventory().checkItem(40676, 5L) && pc.getInventory().checkItem(40675, 5L) && pc.getInventory().checkItem(40049, 3L) && pc.getInventory().checkItem(40051, 1L)) {
                                                                                                    htmlid = "perita27";
                                                                                                    materials = new int[]{40651, 40643, 40618, 40645, 40676, 40675, 40049, 40051};
                                                                                                    counts = new int[]{5, 5, 5, 5, 10, 10, 3, 1};
                                                                                                    createitem = new int[]{40926};
                                                                                                    createcount = new int[]{1};
                                                                                                } else {
                                                                                                    htmlid = "perita28";
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71129) {
                                                                                                if (s.equals("Z")) {
                                                                                                    htmlid = "rumtis2";
                                                                                                } else if (s.equals("Y")) {
                                                                                                    if (pc.getInventory().checkItem(41010, 1L)) {
                                                                                                        htmlid = "rumtis3";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis4";
                                                                                                    }
                                                                                                } else if (s.equals("q")) {
                                                                                                    htmlid = "rumtis92";
                                                                                                } else if (s.equals("A")) {
                                                                                                    if (pc.getInventory().checkItem(41161, 1L)) {
                                                                                                        htmlid = "rumtis6";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("B")) {
                                                                                                    if (pc.getInventory().checkItem(41164, 1L)) {
                                                                                                        htmlid = "rumtis7";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("C")) {
                                                                                                    if (pc.getInventory().checkItem(41167, 1L)) {
                                                                                                        htmlid = "rumtis8";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("T")) {
                                                                                                    if (pc.getInventory().checkItem(41167, 1L)) {
                                                                                                        htmlid = "rumtis9";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("w")) {
                                                                                                    if (pc.getInventory().checkItem(41162, 1L)) {
                                                                                                        htmlid = "rumtis14";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("x")) {
                                                                                                    if (pc.getInventory().checkItem(41165, 1L)) {
                                                                                                        htmlid = "rumtis15";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("y")) {
                                                                                                    if (pc.getInventory().checkItem(41168, 1L)) {
                                                                                                        htmlid = "rumtis16";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("z")) {
                                                                                                    if (pc.getInventory().checkItem(41171, 1L)) {
                                                                                                        htmlid = "rumtis17";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("U")) {
                                                                                                    if (pc.getInventory().checkItem(41163, 1L)) {
                                                                                                        htmlid = "rumtis10";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("V")) {
                                                                                                    if (pc.getInventory().checkItem(41166, 1L)) {
                                                                                                        htmlid = "rumtis11";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("W")) {
                                                                                                    if (pc.getInventory().checkItem(41169, 1L)) {
                                                                                                        htmlid = "rumtis12";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("X")) {
                                                                                                    if (pc.getInventory().checkItem(41172, 1L)) {
                                                                                                        htmlid = "rumtis13";
                                                                                                    } else {
                                                                                                        htmlid = "rumtis101";
                                                                                                    }
                                                                                                } else if (s.equals("D") || s.equals("E") || s.equals("F") || s.equals("G")) {
                                                                                                    insn = false;
                                                                                                    boolean bacn = false;
                                                                                                    int me = 0;
                                                                                                    int mr = 0;
                                                                                                    int mj = 0;
                                                                                                    int an = 0;
                                                                                                    int men = 0;
                                                                                                    int mrn = 0;
                                                                                                    int mjn = 0;
                                                                                                    int ann = 0;
                                                                                                    if (pc.getInventory().checkItem(40959, 1L) && pc.getInventory().checkItem(40960, 1L) && pc.getInventory().checkItem(40961, 1L) && pc.getInventory().checkItem(40962, 1L)) {
                                                                                                        insn = true;
                                                                                                        me = '\u9fff';
                                                                                                        mr = 'ꀀ';
                                                                                                        mj = 'ꀁ';
                                                                                                        an = 'ꀂ';
                                                                                                        men = 1;
                                                                                                        mrn = 1;
                                                                                                        mjn = 1;
                                                                                                        ann = 1;
                                                                                                    } else if (pc.getInventory().checkItem(40642, 10L) && pc.getInventory().checkItem(40635, 10L) && pc.getInventory().checkItem(40638, 10L) && pc.getInventory().checkItem(40667, 10L)) {
                                                                                                        bacn = true;
                                                                                                        me = '黂';
                                                                                                        mr = '麻';
                                                                                                        mj = '麾';
                                                                                                        an = '黛';
                                                                                                        men = 10;
                                                                                                        mrn = 10;
                                                                                                        mjn = 10;
                                                                                                        ann = 10;
                                                                                                    }

                                                                                                    if (pc.getInventory().checkItem(40046, 1L) && pc.getInventory().checkItem(40618, 5L) && pc.getInventory().checkItem(40643, 5L) && pc.getInventory().checkItem(40645, 5L) && pc.getInventory().checkItem(40651, 5L) && pc.getInventory().checkItem(40676, 5L)) {
                                                                                                        if (!insn && !bacn) {
                                                                                                            htmlid = "rumtis18";
                                                                                                        } else {
                                                                                                            htmlid = "rumtis60";
                                                                                                            materials = new int[]{me, mr, mj, an, 40046, 40618, 40643, 40651, 40676};
                                                                                                            counts = new int[]{men, mrn, mjn, ann, 1, 5, 5, 5, 5, 5};
                                                                                                            createitem = new int[]{40926};
                                                                                                            createcount = new int[]{1};
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71119) {
                                                                                                if (s.equalsIgnoreCase("request las history book")) {
                                                                                                    materials = new int[]{41019, 41020, 41021, 41022, 41023, 41024, 41025, 41026};
                                                                                                    counts = new int[]{1, 1, 1, 1, 1, 1, 1, 1};
                                                                                                    createitem = new int[]{41027};
                                                                                                    createcount = new int[]{1};
                                                                                                    htmlid = "";
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71170) {
                                                                                                if (s.equalsIgnoreCase("request las weapon manual")) {
                                                                                                    materials = new int[]{41027};
                                                                                                    counts = new int[]{1};
                                                                                                    createitem = new int[]{40965};
                                                                                                    createcount = new int[]{1};
                                                                                                    htmlid = "";
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71168) {
                                                                                                if (s.equalsIgnoreCase("a") && pc.getInventory().checkItem(41028, 1L)) {
                                                                                                    L1Teleport.teleport(pc, 32648, 32921, (short)535, 6, true);
                                                                                                    pc.getInventory().consumeItem(41028, 1L);
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80067) {
                                                                                                if (s.equalsIgnoreCase("n")) {
                                                                                                    htmlid = "";
                                                                                                    this.poly(client, 6034);
                                                                                                    item_ids = new int[]{41132, 41133, 41134};
                                                                                                    item_amounts = new int[]{1, 1, 1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        pc.getQuest().set_step(36, 1);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("d")) {
                                                                                                    htmlid = "minicod09";
                                                                                                    pc.getInventory().consumeItem(41130, 1L);
                                                                                                    pc.getInventory().consumeItem(41131, 1L);
                                                                                                } else if (s.equalsIgnoreCase("k")) {
                                                                                                    htmlid = "";
                                                                                                    pc.getInventory().consumeItem(41132, 1L);
                                                                                                    pc.getInventory().consumeItem(41133, 1L);
                                                                                                    pc.getInventory().consumeItem(41134, 1L);
                                                                                                    pc.getInventory().consumeItem(41135, 1L);
                                                                                                    pc.getInventory().consumeItem(41136, 1L);
                                                                                                    pc.getInventory().consumeItem(41137, 1L);
                                                                                                    pc.getInventory().consumeItem(41138, 1L);
                                                                                                    pc.getQuest().set_step(36, 0);
                                                                                                } else if (s.equalsIgnoreCase("e")) {
                                                                                                    if (pc.getQuest().get_step(36) != 255 && pc.getKarmaLevel() < 1) {
                                                                                                        if (pc.getInventory().checkItem(41138)) {
                                                                                                            htmlid = "";
                                                                                                            pc.addKarma((int)(1600.0D * ConfigRate.RATE_KARMA));
                                                                                                            pc.getInventory().consumeItem(41130, 1L);
                                                                                                            pc.getInventory().consumeItem(41131, 1L);
                                                                                                            pc.getInventory().consumeItem(41138, 1L);
                                                                                                            pc.getQuest().set_step(36, 255);
                                                                                                        } else {
                                                                                                            htmlid = "minicod04";
                                                                                                        }
                                                                                                    } else {
                                                                                                        htmlid = "";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("g")) {
                                                                                                    htmlid = "";
                                                                                                    item_ids = new int[]{41130};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 81202) {
                                                                                                if (s.equalsIgnoreCase("n")) {
                                                                                                    htmlid = "";
                                                                                                    this.poly(client, 6035);
                                                                                                    item_ids = new int[]{41123, 41124, 41125};
                                                                                                    item_amounts = new int[]{1, 1, 1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                        pc.getQuest().set_step(37, 1);
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("d")) {
                                                                                                    htmlid = "minitos09";
                                                                                                    pc.getInventory().consumeItem(41121, 1L);
                                                                                                    pc.getInventory().consumeItem(41122, 1L);
                                                                                                } else if (s.equalsIgnoreCase("k")) {
                                                                                                    htmlid = "";
                                                                                                    pc.getInventory().consumeItem(41123, 1L);
                                                                                                    pc.getInventory().consumeItem(41124, 1L);
                                                                                                    pc.getInventory().consumeItem(41125, 1L);
                                                                                                    pc.getInventory().consumeItem(41126, 1L);
                                                                                                    pc.getInventory().consumeItem(41127, 1L);
                                                                                                    pc.getInventory().consumeItem(41128, 1L);
                                                                                                    pc.getInventory().consumeItem(41129, 1L);
                                                                                                    pc.getQuest().set_step(37, 0);
                                                                                                } else if (s.equalsIgnoreCase("e")) {
                                                                                                    if (pc.getQuest().get_step(37) != 255 && pc.getKarmaLevel() < 1) {
                                                                                                        if (pc.getInventory().checkItem(41129)) {
                                                                                                            htmlid = "";
                                                                                                            pc.addKarma((int)(-1600.0D * ConfigRate.RATE_KARMA));
                                                                                                            pc.getInventory().consumeItem(41121, 1L);
                                                                                                            pc.getInventory().consumeItem(41122, 1L);
                                                                                                            pc.getInventory().consumeItem(41129, 1L);
                                                                                                            pc.getQuest().set_step(37, 255);
                                                                                                        } else {
                                                                                                            htmlid = "minitos04";
                                                                                                        }
                                                                                                    } else {
                                                                                                        htmlid = "";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("g")) {
                                                                                                    htmlid = "";
                                                                                                    item_ids = new int[]{41121};
                                                                                                    item_amounts = new int[]{1};

                                                                                                    for(i = 0; i < item_ids.length; ++i) {
                                                                                                        item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                        pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71253) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    if (pc.getInventory().checkItem(49101, 100L)) {
                                                                                                        materials = new int[]{49101};
                                                                                                        counts = new int[]{100};
                                                                                                        createitem = new int[]{49092};
                                                                                                        createcount = new int[]{1};
                                                                                                        htmlid = "joegolem18";
                                                                                                    } else {
                                                                                                        htmlid = "joegolem19";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    if (pc.getInventory().checkItem(49101, 1L)) {
                                                                                                        pc.getInventory().consumeItem(49101, 1L);
                                                                                                        L1Teleport.teleport(pc, 33966, 33253, (short)4, 5, true);
                                                                                                        htmlid = "";
                                                                                                    } else {
                                                                                                        htmlid = "joegolem20";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 71255) {
                                                                                                if (s.equalsIgnoreCase("e")) {
                                                                                                    if (pc.getInventory().checkItem(49242, 1L)) {
                                                                                                        pc.getInventory().consumeItem(49242, 1L);
                                                                                                        L1Teleport.teleport(pc, 32735, 32831, (short)782, 2, true);
                                                                                                        htmlid = "";
                                                                                                    } else {
                                                                                                        htmlid = "tebegate3";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80099) {
                                                                                                if (s.equalsIgnoreCase("A")) {
                                                                                                    if (pc.getInventory().checkItem(40308, 300L)) {
                                                                                                        pc.getInventory().consumeItem(40308, 300L);
                                                                                                        pc.getInventory().storeItem(41315, 1L);
                                                                                                        pc.getQuest().set_step(41, 1);
                                                                                                        htmlid = "rarson16";
                                                                                                    } else if (!pc.getInventory().checkItem(40308, 300L)) {
                                                                                                        htmlid = "rarson7";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("B")) {
                                                                                                    if (pc.getQuest().get_step(41) == 1 && pc.getInventory().checkItem(41325, 1L)) {
                                                                                                        pc.getInventory().consumeItem(41325, 1L);
                                                                                                        pc.getInventory().storeItem(40308, 2000L);
                                                                                                        pc.getInventory().storeItem(41317, 1L);
                                                                                                        pc.getQuest().set_step(41, 2);
                                                                                                        htmlid = "rarson9";
                                                                                                    } else {
                                                                                                        htmlid = "rarson10";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("C")) {
                                                                                                    if (pc.getQuest().get_step(41) == 4 && pc.getInventory().checkItem(41326, 1L)) {
                                                                                                        pc.getInventory().storeItem(40308, 30000L);
                                                                                                        pc.getInventory().consumeItem(41326, 1L);
                                                                                                        htmlid = "rarson12";
                                                                                                        pc.getQuest().set_step(41, 5);
                                                                                                    } else {
                                                                                                        htmlid = "rarson17";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("D")) {
                                                                                                    if (pc.getQuest().get_step(41) > 1 && pc.getQuest().get_step(41) != 5) {
                                                                                                        if (pc.getQuest().get_step(41) >= 2 && pc.getQuest().get_step(41) <= 4) {
                                                                                                            if (pc.getInventory().checkItem(40308, 300L)) {
                                                                                                                pc.getInventory().consumeItem(40308, 300L);
                                                                                                                pc.getInventory().storeItem(41315, 1L);
                                                                                                                htmlid = "rarson16";
                                                                                                            } else if (!pc.getInventory().checkItem(40308, 300L)) {
                                                                                                                htmlid = "rarson7";
                                                                                                            }
                                                                                                        }
                                                                                                    } else if (pc.getInventory().checkItem(40308, 300L)) {
                                                                                                        pc.getInventory().consumeItem(40308, 300L);
                                                                                                        pc.getInventory().storeItem(41315, 1L);
                                                                                                        pc.getQuest().set_step(41, 1);
                                                                                                        htmlid = "rarson16";
                                                                                                    } else if (!pc.getInventory().checkItem(40308, 300L)) {
                                                                                                        htmlid = "rarson7";
                                                                                                    }
                                                                                                }
                                                                                            } else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 80101) {
                                                                                                if (s.equalsIgnoreCase("request letter of kuen")) {
                                                                                                    if (pc.getQuest().get_step(41) == 2 && pc.getInventory().checkItem(41317, 1L)) {
                                                                                                        pc.getInventory().consumeItem(41317, 1L);
                                                                                                        pc.getInventory().storeItem(41318, 1L);
                                                                                                        pc.getQuest().set_step(41, 3);
                                                                                                        htmlid = "";
                                                                                                    } else {
                                                                                                        htmlid = "";
                                                                                                    }
                                                                                                } else if (s.equalsIgnoreCase("request holy mithril dust")) {
                                                                                                    if (pc.getQuest().get_step(41) == 3 && pc.getInventory().checkItem(41315, 1L) && pc.getInventory().checkItem(40494, 30L) && pc.getInventory().checkItem(41318, 1L)) {
                                                                                                        pc.getInventory().consumeItem(41315, 1L);
                                                                                                        pc.getInventory().consumeItem(41318, 1L);
                                                                                                        pc.getInventory().consumeItem(40494, 30L);
                                                                                                        pc.getInventory().storeItem(41316, 1L);
                                                                                                        pc.getQuest().set_step(41, 4);
                                                                                                        htmlid = "";
                                                                                                    } else {
                                                                                                        htmlid = "";
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        } else if (s.equalsIgnoreCase("0")) {
                                                                                            htmlid = "maptbox2";
                                                                                            pc.getQuest().set_end(26);
                                                                                            item_ids = new int[]{40716};
                                                                                            item_amounts = new int[]{1};

                                                                                            for(i = 0; i < item_ids.length; ++i) {
                                                                                                item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
                                                                                                pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)obj).getNpcTemplate().get_name(), item.getItem().getName()));
                                                                                            }

                                                                                            pc.getInventory().consumeItem(40701, 1L);
                                                                                            pc.getQuest().set_step(23, 11);
                                                                                        }
                                                                                    } else if (s.equalsIgnoreCase("0")) {
                                                                                        materials = new int[]{40701};
                                                                                        counts = new int[]{1};
                                                                                        createitem = new int[]{40702};
                                                                                        createcount = new int[]{1};
                                                                                        htmlid = "maptbox1";
                                                                                        pc.getQuest().set_end(25);
                                                                                        item_ids = new int[]{1, 2, 3, 4, 5, 6};
                                                                                        htmlB = _random.nextInt(item_ids.length);
                                                                                        i = item_ids[htmlB];
                                                                                        if (i == 1) {
                                                                                            pc.getQuest().set_step(23, 5);
                                                                                        } else if (i == 2) {
                                                                                            pc.getQuest().set_step(23, 6);
                                                                                        } else if (i == 3) {
                                                                                            pc.getQuest().set_step(23, 7);
                                                                                        } else if (i == 4) {
                                                                                            pc.getQuest().set_step(23, 8);
                                                                                        } else if (i == 5) {
                                                                                            pc.getQuest().set_step(23, 9);
                                                                                        } else if (i == 6) {
                                                                                            pc.getQuest().set_step(23, 10);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            } else if (s.equalsIgnoreCase("r")) {
                                                                                if (obj instanceof L1NpcInstance) {
                                                                                    html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                                                                                    htmlB = L1TownLocation.getTownIdByNpcid(html);
                                                                                }
                                                                            } else if (!s.equalsIgnoreCase("t")) {
                                                                                s.equalsIgnoreCase("c");
                                                                            }
                                                                        } else {
                                                                            htmlid = s;
                                                                            html = ((L1NpcInstance)obj).getNpcTemplate().get_npcId();
                                                                            htmlB = L1CastleLocation.getCastleTaxRateByNpcId(html);
                                                                            htmldata = new String[]{String.valueOf(htmlB)};
                                                                        }
                                                                    } else {
                                                                        htmlid = this.talkToDimensionDoor(pc, (L1NpcInstance)obj, s);
                                                                    }
                                                                }
                                                            } else {
                                                                clan = WorldClan.get().getClan(pc.getClanname());
                                                                if (clan != null) {
                                                                    htmlB = clan.getHouseId();
                                                                    if (htmlB != 0) {
                                                                        house = HouseReading.get().getHouseTable(htmlB);
                                                                        cost = house.getKeeperId();
                                                                        npc = (L1NpcInstance)obj;
                                                                        if (npc.getNpcTemplate().get_npcId() == cost) {
                                                                            loc = new int[3];
                                                                            if (s.equalsIgnoreCase("tel0")) {
                                                                                loc = L1HouseLocation.getHouseTeleportLoc(htmlB, 0);
                                                                            } else if (s.equalsIgnoreCase("tel1")) {
                                                                                loc = L1HouseLocation.getHouseTeleportLoc(htmlB, 1);
                                                                            } else if (s.equalsIgnoreCase("tel2")) {
                                                                                loc = L1HouseLocation.getHouseTeleportLoc(htmlB, 2);
                                                                            } else if (s.equalsIgnoreCase("tel3")) {
                                                                                loc = L1HouseLocation.getHouseTeleportLoc(htmlB, 3);
                                                                            }

                                                                            L1Teleport.teleport(pc, loc[0], loc[1], (short)loc[2], 5, true);
                                                                        }
                                                                    }
                                                                }

                                                                htmlid = "";
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    npc = (L1NpcInstance)obj;
                                                    this.openCloseDoor(pc, npc, s);
                                                    htmlid = "";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (htmlid != null && htmlid.equalsIgnoreCase("colos2")) {
                    htmldata = this.makeUbInfoStrings(((L1NpcInstance)obj).getNpcTemplate().get_npcId());
                }

                if (createitem != null) {
                    insn = true;

                    for(htmlB = 0; htmlB < materials.length; ++htmlB) {
                        if (!pc.getInventory().checkItemNotEquipped(materials[htmlB], counts[htmlB])) {
                            L1Item temp = ItemTable.get().getTemplate(materials[htmlB]);
                            pc.sendPackets(new S_ServerMessage(337, temp.getName()));
                            insn = false;
                        }
                    }

                    if (insn) {
                        htmlB = 0;
                        i = 0;

                        for(cost = 0; cost < createitem.length; ++cost) {
                            L1Item temp = ItemTable.get().getTemplate(createitem[cost]);
                            if (temp.isStackable()) {
                                if (!pc.getInventory().checkItem(createitem[cost])) {
                                    ++htmlB;
                                }
                            } else {
                                htmlB += createcount[cost];
                            }

                            i += temp.getWeight() * createcount[cost] / 1000;
                        }

                        if (pc.getInventory().getSize() + htmlB > 180) {
                            pc.sendPackets(new S_ServerMessage(263));
                            return;
                        }

                        if (pc.getMaxWeight() < (double)(pc.getInventory().getWeight() + i)) {
                            pc.sendPackets(new S_ServerMessage(82));
                            return;
                        }

                        for(cost = 0; cost < materials.length; ++cost) {
                            pc.getInventory().consumeItem(materials[cost], counts[cost]);
                        }

                        for(cost = 0; cost < createitem.length; ++cost) {
                            item = pc.getInventory().storeItem(createitem[cost], createcount[cost]);
                            if (item != null) {
                                npcName = ItemTable.get().getTemplate(createitem[cost]).getName();
                                itemName = "";
                                if (obj instanceof L1NpcInstance) {
                                    itemName = ((L1NpcInstance)obj).getNpcTemplate().get_name();
                                }

                                if (createcount[cost] > 1) {
                                    pc.sendPackets(new S_ServerMessage(143, itemName, npcName + " (" + createcount[cost] + ")"));
                                } else {
                                    pc.sendPackets(new S_ServerMessage(143, itemName, npcName));
                                }
                            }
                        }

                        if (success_htmlid != null) {
                            pc.sendPackets(new S_NPCTalkReturn(objid, success_htmlid, htmldata));
                        }
                    } else if (failure_htmlid != null) {
                        pc.sendPackets(new S_NPCTalkReturn(objid, failure_htmlid, htmldata));
                    }
                }

                if (htmlid != null) {
                    pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
                    return;
                }
            } catch (Exception var37) {
            }

        } finally {
            this.over();
        }
    }

    private String karmaLevelToHtmlId(int level) {
        if (level != 0 && level >= -7 && 7 >= level) {
            String htmlid = "";
            if (level > 0) {
                htmlid = "vbk" + level;
            } else if (level < 0) {
                htmlid = "vyk" + Math.abs(level);
            }

            return htmlid;
        } else {
            return "";
        }
    }

    private String watchUb(L1PcInstance pc, int npcId) throws Exception {
        L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
        L1Location loc = ub.getLocation();
        if (pc.getInventory().consumeItem(40308, 100L)) {
            try {
                pc.save();
                pc.beginGhost(loc.getX(), loc.getY(), loc.getMapId(), true);
            } catch (Exception var6) {
                _log.error(var6.getLocalizedMessage(), var6);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(189));
        }

        return "";
    }

    private String enterUb(L1PcInstance pc, int npcId) {
        L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
        if (ub.isActive() && ub.canPcEnter(pc)) {
            if (ub.isNowUb()) {
                return "colos1";
            } else if (ub.getMembersCount() >= ub.getMaxPlayer()) {
                return "colos4";
            } else {
                ub.addMember(pc);
                L1Location loc = ub.getLocation().randomLocation(10, false);
                L1Teleport.teleport(pc, loc.getX(), loc.getY(), ub.getMapId(), 5, true);
                return "";
            }
        } else {
            return "colos2";
        }
    }

    private String enterHauntedHouse(L1PcInstance pc) {
        if (L1HauntedHouse.getInstance().getHauntedHouseStatus() == 2) {
            pc.sendPackets(new S_ServerMessage(1182));
            return "";
        } else if (L1HauntedHouse.getInstance().getMembersCount() >= 10) {
            pc.sendPackets(new S_ServerMessage(1184));
            return "";
        } else {
            L1HauntedHouse.getInstance().addMember(pc);
            L1Teleport.teleport(pc, 32722, 32830, 5140, 2, true);
            return "";
        }
    }

    private void poly(ClientExecutor clientthread, int polyId) throws Exception {
        L1PcInstance pc = clientthread.getActiveChar();
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId != 185 && awakeSkillId != 190 && awakeSkillId != 195) {
            if (pc.getInventory().checkItem(40308, 100L)) {
                pc.getInventory().consumeItem(40308, 100L);
                L1PolyMorph.doPoly(pc, polyId, 1800, 4);
            } else {
                pc.sendPackets(new S_ServerMessage(337, "$4"));
            }

        } else {
            pc.sendPackets(new S_ServerMessage(1384));
        }
    }

    private void polyByKeplisha(ClientExecutor clientthread, int polyId) throws Exception {
        L1PcInstance pc = clientthread.getActiveChar();
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId != 185 && awakeSkillId != 190 && awakeSkillId != 195) {
            if (pc.getInventory().checkItem(40308, 100L)) {
                pc.getInventory().consumeItem(40308, 100L);
                L1PolyMorph.doPoly(pc, polyId, 1800, 8);
            } else {
                pc.sendPackets(new S_ServerMessage(337, "$4"));
            }

        } else {
            pc.sendPackets(new S_ServerMessage(1384));
        }
    }

    private String sellHouse(L1PcInstance pc, int objectId, int npcId) {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan == null) {
            return "";
        } else {
            int houseId = clan.getHouseId();
            if (houseId == 0) {
                return "";
            } else {
                L1House house = HouseReading.get().getHouseTable(houseId);
                int keeperId = house.getKeeperId();
                if (npcId != keeperId) {
                    return "";
                } else if (!pc.isCrown()) {
                    pc.sendPackets(new S_ServerMessage(518));
                    return "";
                } else if (pc.getId() != clan.getLeaderId()) {
                    pc.sendPackets(new S_ServerMessage(518));
                    return "";
                } else if (house.isOnSale()) {
                    return "agonsale";
                } else {
                    pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
                    return null;
                }
            }
        }
    }

    private void openCloseDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            int houseId = clan.getHouseId();
            if (houseId != 0) {
                L1House house = HouseReading.get().getHouseTable(houseId);
                int keeperId = house.getKeeperId();
                if (npc.getNpcTemplate().get_npcId() == keeperId) {
                    L1DoorInstance door1 = null;
                    L1DoorInstance door2 = null;
                    L1DoorInstance door3 = null;
                    L1DoorInstance door4 = null;
                    L1DoorInstance[] var16;
                    int var15 = (var16 = DoorSpawnTable.get().getDoorList()).length;

                    for(int var14 = 0; var14 < var15; ++var14) {
                        L1DoorInstance door = var16[var14];
                        if (door.getKeeperId() == keeperId) {
                            if (door1 == null) {
                                door1 = door;
                            } else if (door2 == null) {
                                door2 = door;
                            } else if (door3 == null) {
                                door3 = door;
                            } else if (door4 == null) {
                                door4 = door;
                                break;
                            }
                        }
                    }

                    if (door1 != null) {
                        if (s.equalsIgnoreCase("open")) {
                            door1.open();
                        } else if (s.equalsIgnoreCase("close")) {
                            door1.close();
                        }
                    }

                    if (door2 != null) {
                        if (s.equalsIgnoreCase("open")) {
                            door2.open();
                        } else if (s.equalsIgnoreCase("close")) {
                            door2.close();
                        }
                    }

                    if (door3 != null) {
                        if (s.equalsIgnoreCase("open")) {
                            door3.open();
                        } else if (s.equalsIgnoreCase("close")) {
                            door3.close();
                        }
                    }

                    if (door4 != null) {
                        if (s.equalsIgnoreCase("open")) {
                            door4.open();
                        } else if (s.equalsIgnoreCase("close")) {
                            door4.close();
                        }
                    }
                }
            }
        }

    }

    private void openCloseGate(L1PcInstance pc, int keeperId, boolean isOpen) {
        boolean isNowWar = false;
        int pcCastleId = 0;
        if (pc.getClanid() != 0) {
            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (clan != null) {
                pcCastleId = clan.getCastleId();
            }
        }

        if (keeperId != 70656 && keeperId != 70549 && keeperId != 70985) {
            if (keeperId == 70600) {
                if (this.isExistDefenseClan(2) && pcCastleId != 2) {
                    return;
                }

                isNowWar = ServerWarExecutor.get().isNowWar(2);
            } else if (keeperId != 70778 && keeperId != 70987 && keeperId != 70687) {
                if (keeperId != 70817 && keeperId != 70800 && keeperId != 70988 && keeperId != 70990 && keeperId != 70989 && keeperId != 70991) {
                    if (keeperId != 70863 && keeperId != 70992 && keeperId != 70862) {
                        if (keeperId != 70995 && keeperId != 70994 && keeperId != 70993) {
                            if (keeperId == 70996) {
                                if (this.isExistDefenseClan(7) && pcCastleId != 7) {
                                    return;
                                }

                                isNowWar = ServerWarExecutor.get().isNowWar(7);
                            }
                        } else {
                            if (this.isExistDefenseClan(6) && pcCastleId != 6) {
                                return;
                            }

                            isNowWar = ServerWarExecutor.get().isNowWar(6);
                        }
                    } else {
                        if (this.isExistDefenseClan(5) && pcCastleId != 5) {
                            return;
                        }

                        isNowWar = ServerWarExecutor.get().isNowWar(5);
                    }
                } else {
                    if (this.isExistDefenseClan(4) && pcCastleId != 4) {
                        return;
                    }

                    isNowWar = ServerWarExecutor.get().isNowWar(4);
                }
            } else {
                if (this.isExistDefenseClan(3) && pcCastleId != 3) {
                    return;
                }

                isNowWar = ServerWarExecutor.get().isNowWar(3);
            }
        } else {
            if (this.isExistDefenseClan(1) && pcCastleId != 1) {
                return;
            }

            isNowWar = ServerWarExecutor.get().isNowWar(1);
        }

        L1DoorInstance[] var9;
        int var8 = (var9 = DoorSpawnTable.get().getDoorList()).length;

        for(int var7 = 0; var7 < var8; ++var7) {
            L1DoorInstance door = var9[var7];
            if (door.getKeeperId() == keeperId && (!isNowWar || door.getMaxHp() <= 1)) {
                if (isOpen) {
                    door.open();
                } else {
                    door.close();
                }
            }
        }

    }

    private boolean isExistDefenseClan(int castleId) {
        boolean isExistDefenseClan = false;
        Collection<L1Clan> allClans = WorldClan.get().getAllClans();
        Iterator iter = allClans.iterator();

        while(iter.hasNext()) {
            L1Clan clan = (L1Clan)iter.next();
            if (castleId == clan.getCastleId()) {
                isExistDefenseClan = true;
                break;
            }
        }

        return isExistDefenseClan;
    }

    private void expelOtherClan(L1PcInstance clanPc, int keeperId) {
        int houseId = 0;
        Collection<L1House> houseList = HouseReading.get().getHouseTableList().values();
        Iterator var6 = houseList.iterator();

        while(var6.hasNext()) {
            L1House house = (L1House)var6.next();
            if (house.getKeeperId() == keeperId) {
                houseId = house.getHouseId();
            }
        }

        if (houseId != 0) {
            int[] loc = new int[3];
            Iterator var7 = World.get().getAllPlayers().iterator();

            while(var7.hasNext()) {
                L1PcInstance pc = (L1PcInstance)var7.next();
                if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(), pc.getMapId()) && clanPc.getClanid() != pc.getClanid()) {
                    loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
                    if (pc != null) {
                        L1Teleport.teleport(pc, loc[0], loc[1], loc[2], 5, true);
                    }
                }
            }

        }
    }

    private void repairGate(L1PcInstance pc) {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            int castleId = clan.getCastleId();
            if (castleId != 0) {
                if (!ServerWarExecutor.get().isNowWar(castleId)) {
                    L1DoorInstance[] var7;
                    int var6 = (var7 = DoorSpawnTable.get().getDoorList()).length;

                    for(int var5 = 0; var5 < var6; ++var5) {
                        L1DoorInstance door = var7[var5];
                        if (L1CastleLocation.checkInWarArea(castleId, door)) {
                            door.repairGate();
                        }
                    }

                    pc.sendPackets(new S_ServerMessage(990));
                } else {
                    pc.sendPackets(new S_ServerMessage(991));
                }
            }
        }

    }

    private void payFee(L1PcInstance pc, L1NpcInstance npc) throws Exception {
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            int houseId = clan.getHouseId();
            if (houseId != 0) {
                L1House house = HouseReading.get().getHouseTable(houseId);
                int keeperId = house.getKeeperId();
                if (npc.getNpcTemplate().get_npcId() == keeperId) {
                    if (pc.getInventory().checkItem(40308, 2000L)) {
                        pc.getInventory().consumeItem(40308, 2000L);
                        TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
                        Calendar cal = Calendar.getInstance(tz);
                        cal.add(5, ConfigAlt.HOUSE_TAX_INTERVAL);
                        cal.set(12, 0);
                        cal.set(13, 0);
                        house.setTaxDeadline(cal);
                        HouseReading.get().updateHouse(house);
                    } else {
                        pc.sendPackets(new S_ServerMessage(189));
                    }
                }
            }
        }

    }

    private String[] makeHouseTaxStrings(L1PcInstance pc, L1NpcInstance npc) {
        String name = npc.getNpcTemplate().get_name();
        String[] result = new String[]{name, "2000", "1", "1", "00"};
        L1Clan clan = WorldClan.get().getClan(pc.getClanname());
        if (clan != null) {
            int houseId = clan.getHouseId();
            if (houseId != 0) {
                L1House house = HouseReading.get().getHouseTable(houseId);
                int keeperId = house.getKeeperId();
                if (npc.getNpcTemplate().get_npcId() == keeperId) {
                    Calendar cal = house.getTaxDeadline();
                    int month = cal.get(2) + 1;
                    int day = cal.get(5);
                    int hour = cal.get(11);
                    result = new String[]{name, "2000", String.valueOf(month), String.valueOf(day), String.valueOf(hour)};
                }
            }
        }

        return result;
    }

    private String[] makeWarTimeStrings(int castleId) {
        L1Castle castle = CastleReading.get().getCastleTable(castleId);
        if (castle == null) {
            return null;
        } else {
            Calendar warTime = castle.getWarTime();
            int year = warTime.get(1);
            int month = warTime.get(2) + 1;
            int day = warTime.get(5);
            int hour = warTime.get(11);
            int minute = warTime.get(12);
            String[] result;
            if (castleId == 2) {
                result = new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute)};
            } else {
                result = new String[]{"", String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute)};
            }

            return result;
        }
    }

    private String getYaheeAmulet(L1PcInstance pc, L1NpcInstance npc, String s) throws Exception {
        int[] amuletIdList = new int[]{20358, 20359, 20360, 20361, 20362, 20363, 20364, 20365};
        int amuletId = 0;
        L1ItemInstance item = null;
        String htmlid = null;
        if (s.equalsIgnoreCase("1")) {
            amuletId = amuletIdList[0];
        } else if (s.equalsIgnoreCase("2")) {
            amuletId = amuletIdList[1];
        } else if (s.equalsIgnoreCase("3")) {
            amuletId = amuletIdList[2];
        } else if (s.equalsIgnoreCase("4")) {
            amuletId = amuletIdList[3];
        } else if (s.equalsIgnoreCase("5")) {
            amuletId = amuletIdList[4];
        } else if (s.equalsIgnoreCase("6")) {
            amuletId = amuletIdList[5];
        } else if (s.equalsIgnoreCase("7")) {
            amuletId = amuletIdList[6];
        } else if (s.equalsIgnoreCase("8")) {
            amuletId = amuletIdList[7];
        }

        if (amuletId != 0) {
            item = pc.getInventory().storeItem(amuletId, 1L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            int[] var11 = amuletIdList;
            int var10 = amuletIdList.length;

            for(int var9 = 0; var9 < var10; ++var9) {
                int id = var11[var9];
                if (id == amuletId) {
                    break;
                }

                if (pc.getInventory().checkItem(id)) {
                    pc.getInventory().consumeItem(id, 1L);
                }
            }

            htmlid = "";
        }

        return htmlid;
    }

    private String getBarlogEarring(L1PcInstance pc, L1NpcInstance npc, String s) throws Exception {
        int[] earringIdList = new int[]{21020, 21021, 21022, 21023, 21024, 21025, 21026, 21027};
        int earringId = 0;
        L1ItemInstance item = null;
        String htmlid = null;
        if (s.equalsIgnoreCase("1")) {
            earringId = earringIdList[0];
        } else if (s.equalsIgnoreCase("2")) {
            earringId = earringIdList[1];
        } else if (s.equalsIgnoreCase("3")) {
            earringId = earringIdList[2];
        } else if (s.equalsIgnoreCase("4")) {
            earringId = earringIdList[3];
        } else if (s.equalsIgnoreCase("5")) {
            earringId = earringIdList[4];
        } else if (s.equalsIgnoreCase("6")) {
            earringId = earringIdList[5];
        } else if (s.equalsIgnoreCase("7")) {
            earringId = earringIdList[6];
        } else if (s.equalsIgnoreCase("8")) {
            earringId = earringIdList[7];
        }

        if (earringId != 0) {
            item = pc.getInventory().storeItem(earringId, 1L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            int[] var11 = earringIdList;
            int var10 = earringIdList.length;

            for(int var9 = 0; var9 < var10; ++var9) {
                int id = var11[var9];
                if (id == earringId) {
                    break;
                }

                if (pc.getInventory().checkItem(id)) {
                    pc.getInventory().consumeItem(id, 1L);
                }
            }

            htmlid = "";
        }

        return htmlid;
    }

    private String[] makeUbInfoStrings(int npcId) {
        L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
        return ub.makeUbInfoStrings();
    }

    private String talkToDimensionDoor(L1PcInstance pc, L1NpcInstance npc, String s) throws Exception {
        String htmlid = "";
        int protectionId = 0;
        int sealId = 0;
        int locX = 0;
        int locY = 0;
        int mapId = 0;
        if (npc.getNpcTemplate().get_npcId() == 80059) {
            protectionId = '鿍';
            sealId = '鿑';
            locX = '者';
            locY = '聃';
            mapId = 607;
        } else if (npc.getNpcTemplate().get_npcId() == 80060) {
            protectionId = '鿐';
            sealId = '鿔';
            locX = 32757;
            locY = '聊';
            mapId = 606;
        } else if (npc.getNpcTemplate().get_npcId() == 80061) {
            protectionId = '鿎';
            sealId = '鿒';
            locX = '耾';
            locY = '耶';
            mapId = 604;
        } else if (npc.getNpcTemplate().get_npcId() == 80062) {
            protectionId = '鿏';
            sealId = '鿓';
            locX = '聃';
            locY = '耶';
            mapId = 605;
        }

        if (s.equalsIgnoreCase("a")) {
            L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
            htmlid = "";
        } else {
            L1ItemInstance item;
            if (s.equalsIgnoreCase("b")) {
                item = pc.getInventory().storeItem(protectionId, 1L);
                if (item != null) {
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
                }

                htmlid = "";
            } else if (s.equalsIgnoreCase("c")) {
                htmlid = "wpass07";
            } else if (s.equalsIgnoreCase("d")) {
                if (pc.getInventory().checkItem(sealId)) {
                    item = pc.getInventory().findItemId(sealId);
                    pc.getInventory().consumeItem(sealId, item.getCount());
                }
            } else if (s.equalsIgnoreCase("e")) {
                htmlid = "";
            } else if (s.equalsIgnoreCase("f")) {
                if (pc.getInventory().checkItem(protectionId)) {
                    pc.getInventory().consumeItem(protectionId, 1L);
                }

                if (pc.getInventory().checkItem(sealId)) {
                    item = pc.getInventory().findItemId(sealId);
                    pc.getInventory().consumeItem(sealId, item.getCount());
                }

                htmlid = "";
            }
        }

        return htmlid;
    }

    private void getBloodCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
        L1ItemInstance item = null;
        if (s.equalsIgnoreCase("1")) {
            pc.addKarma((int)(500.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40718, 1L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1081));
        } else if (s.equalsIgnoreCase("2")) {
            pc.addKarma((int)(5000.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40718, 10L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1081));
        } else if (s.equalsIgnoreCase("3")) {
            pc.addKarma((int)(50000.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40718, 100L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1081));
        }

    }

    private void getSoulCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
        L1ItemInstance item = null;
        if (s.equalsIgnoreCase("1")) {
            pc.addKarma((int)(-500.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40678, 1L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1080));
        } else if (s.equalsIgnoreCase("2")) {
            pc.addKarma((int)(-5000.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40678, 10L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1080));
        } else if (s.equalsIgnoreCase("3")) {
            pc.addKarma((int)(-50000.0D * ConfigRate.RATE_KARMA));
            item = pc.getInventory().storeItem(40678, 100L);
            if (item != null) {
                pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
            }

            pc.sendPackets(new S_ServerMessage(1080));
        }

    }

    private void UbRank(L1PcInstance pc, L1NpcInstance npc) {
        L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npc.getNpcTemplate().get_npcId());
        String[] htmldata = null;
        htmldata = new String[11];
        htmldata[0] = npc.getNpcTemplate().get_name();
        String htmlid = "colos3";
        int i = 1;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;

        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM ub_rank WHERE ub_id=? order by score desc limit 10");
            pstm.setInt(1, ub.getUbId());

            for(rs = pstm.executeQuery(); rs.next(); ++i) {
                htmldata[i] = rs.getString(2) + " : " + rs.getInt(3);
            }
        } catch (SQLException var14) {
            _log.error(var14.getLocalizedMessage(), var14);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }

        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), htmlid, htmldata));
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
