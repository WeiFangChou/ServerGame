package com.lineage.server.clientpackets;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigRate;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1HauntedHouse;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1UltimateBattle;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_SellHouse;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.templates.L1House;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public void start(byte[] param1, ClientExecutor param2) {
        // $FF: Couldn't be decompiled
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
                pc.beginGhost(loc.getX(), loc.getY(), (short)loc.getMapId(), true);
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
            L1Teleport.teleport(pc, 32722, 32830, (short)5140, 2, true);
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
                        L1Teleport.teleport(pc, loc[0], loc[1], (short)loc[2], 5, true);
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
        short mapId = 0;
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
