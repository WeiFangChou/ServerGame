package com.lineage.server.clientpackets;

import com.eric.gui.J_Main;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.quest.CrownLv45_1;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ClanUpdate;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_Resurrection;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Trade;
import com.lineage.server.templates.L1House;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import java.util.regex.Matcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Attr extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Attr.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int cost;
        int maxMember;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isTeleport()) {
                    over();
                    return;
                }
                int mode = readH();
                if (mode == 0) {
                    readD();
                    mode = readH();
                }
                switch (mode) {
                    case 97:
                        int c = readC();
                        L1PcInstance joinPc = (L1PcInstance) World.get().findObject(pc.getTempID());
                        pc.setTempID(0);
                        if (joinPc != null) {
                            if (c == 0) {
                                joinPc.sendPackets(new S_ServerMessage(96, pc.getName()));
                                break;
                            } else if (c == 1) {
                                int clan_id = pc.getClanid();
                                String clanName = pc.getClanname();
                                L1Clan clan = WorldClan.get().getClan(clanName);
                                if (clan != null) {
                                    int charisma = pc.getCha();
                                    boolean lv45quest = false;
                                    if (pc.getQuest().isEnd(CrownLv45_1.QUEST.get_id())) {
                                        lv45quest = true;
                                    }
                                    if (pc.getLevel() >= 50) {
                                        if (lv45quest) {
                                            maxMember = charisma * 9;
                                        } else {
                                            maxMember = charisma * 3;
                                        }
                                    } else if (lv45quest) {
                                        maxMember = charisma * 6;
                                    } else {
                                        maxMember = charisma * 2;
                                    }
                                    if (ConfigOther.CLANCOUNT != 0) {
                                        maxMember = ConfigOther.CLANCOUNT;
                                    }
                                    if (joinPc.getClanid() != 0) {
                                        if (ConfigAlt.CLAN_ALLIANCE) {
                                            changeClan(client, pc, joinPc, maxMember);
                                            break;
                                        } else {
                                            joinPc.sendPackets(new S_ServerMessage(89));
                                            break;
                                        }
                                    } else if (maxMember > clan.getAllMembers().length) {
                                        for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
                                            clanMembers.sendPackets(new S_ServerMessage(94, joinPc.getName()));
                                        }
                                        joinPc.setClanid(clan_id);
                                        joinPc.setClanname(clanName);
                                        joinPc.setClanRank(8);
                                        joinPc.save();
                                        joinPc.sendPackets(new S_ClanUpdate(joinPc.getId(), joinPc.getClanname(), joinPc.getClanRank()));
                                        clan.addMemberName(joinPc.getName());
                                        for (L1PcInstance clanMembers2 : clan.getOnlineClanMember()) {
                                            clanMembers2.sendPackets(new S_ClanUpdate(joinPc.getId(), joinPc.getClanname(), joinPc.getClanRank()));
                                        }
                                        joinPc.sendPackets(new S_ServerMessage(95, clanName));
                                        break;
                                    } else {
                                        joinPc.sendPackets(new S_ServerMessage(188, pc.getName()));
                                        over();
                                        return;
                                    }
                                }
                            }
                        }
                        break;
                    case OpcodesServer.S_OPCODE_INVLIST /*{ENCODED_INT: 180}*/:
                        readC();
                        String polys = readS();
                        if (pc.isShapeChange()) {
                            L1PolyMorph.handleCommands(pc, polys);
                            pc.setShapeChange(false);
                            break;
                        }
                        break;
                    case 217:
                    case OpcodesClient.C_OPCODE_BOARDBACK:
                    case 222:
                        int c2 = readC();
                        L1PcInstance enemyLeader = (L1PcInstance) World.get().findObject(pc.getTempID());
                        if (enemyLeader != null) {
                            pc.setTempID(0);
                            String clanName2 = pc.getClanname();
                            String enemyClanName = enemyLeader.getClanname();
                            if (c2 == 0) {
                                if (mode != 217) {
                                    if (mode == 221 || mode == 222) {
                                        enemyLeader.sendPackets(new S_ServerMessage((int) OpcodesClient.C_OPCODE_CHANGECHAR, clanName2));
                                        break;
                                    }
                                } else {
                                    enemyLeader.sendPackets(new S_ServerMessage((int) OpcodesClient.C_OPCODE_CHARRESET, clanName2));
                                    break;
                                }
                            } else if (c2 == 1) {
                                if (mode != 217) {
                                    if (mode == 221 || mode == 222) {
                                        Iterator<L1War> it = WorldWar.get().getWarList().iterator();
                                        while (true) {
                                            if (it.hasNext()) {
                                                L1War war = it.next();
                                                if (war.checkClanInWar(clanName2)) {
                                                    if (mode != 221) {
                                                        if (mode == 222) {
                                                            war.ceaseWar(enemyClanName, clanName2);
                                                            break;
                                                        }
                                                    } else {
                                                        war.surrenderWar(enemyClanName, clanName2);
                                                        break;
                                                    }
                                                }
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    new L1War().handleCommands(2, enemyClanName, clanName2);
                                    break;
                                }
                            }
                        } else {
                            over();
                            return;
                        }
                        break;
                    case OpcodesServer.S_OPCODE_PINKNAME /*{ENCODED_INT: 252}*/:
                        int c3 = readC();
                        L1PcInstance trading_partner = (L1PcInstance) World.get().findObject(pc.getTradeID());
                        if (trading_partner != null) {
                            if (c3 != 0) {
                                if (c3 == 1) {
                                    pc.sendPackets(new S_Trade(trading_partner.getName()));
                                    trading_partner.sendPackets(new S_Trade(pc.getName()));
                                    break;
                                }
                            } else {
                                trading_partner.sendPackets(new S_ServerMessage(253, pc.getName()));
                                pc.setTradeID(0);
                                trading_partner.setTradeID(0);
                                break;
                            }
                        }
                        break;
                    case 321:
                        int c4 = readC();
                        L1PcInstance resusepc1 = (L1PcInstance) World.get().findObject(pc.getTempID());
                        pc.setTempID(0);
                        if (!(resusepc1 == null || c4 == 0 || c4 != 1)) {
                            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 230));
                            pc.resurrect(pc.getMaxHp() / 2);
                            pc.setCurrentHp(pc.getMaxHp() / 2);
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                            pc.stopPcDeleteTimer();
                            pc.sendPacketsAll(new S_Resurrection(pc, (L1Character) resusepc1, 0));
                            pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                            break;
                        }
                    case 322:
                        int c5 = readC();
                        L1PcInstance resusepc2 = (L1PcInstance) World.get().findObject(pc.getTempID());
                        pc.setTempID(0);
                        if (!(resusepc2 == null || c5 == 0 || c5 != 1)) {
                            pc.sendPacketsX8(new S_SkillSound(pc.getId(), 230));
                            pc.resurrect(pc.getMaxHp());
                            pc.setCurrentHp(pc.getMaxHp());
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                            pc.stopPcDeleteTimer();
                            pc.sendPacketsAll(new S_Resurrection(pc, (L1Character) resusepc2, 0));
                            pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                            if (pc.getExpRes() == 1 && pc.isGres() && pc.isGresValid()) {
                                pc.resExp();
                                pc.setExpRes(0);
                                pc.setGres(false);
                                break;
                            }
                        }
                    case 325:
                        readC();
                        String petName = readS();
                        if (pc.is_rname()) {
                            String name = Matcher.quoteReplacement(petName).replaceAll("\\s", "").replaceAll("　", "");
                            String name2 = String.valueOf(name.substring(0, 1).toUpperCase()) + name.substring(1);
                            for (String ban : C_CreateChar.BANLIST) {
                                if (name2.indexOf(ban) != -1) {
                                    name2 = "";
                                }
                            }
                            if (name2.length() == 0) {
                                pc.sendPackets(new S_ServerMessage(53));
                                over();
                                return;
                            } else if (!C_CreateChar.isInvalidName(name2)) {
                                pc.sendPackets(new S_ServerMessage(53));
                                over();
                                return;
                            } else if (CharObjidTable.get().charObjid(name2) != 0) {
                                pc.sendPackets(new S_ServerMessage(58));
                                over();
                                return;
                            } else {
                                World.get().removeObject(pc);
                                pc.getInventory().consumeItem(41227, 1);
                                WriteLogTxt.Recording("角色更名記錄", "角色OBJID：<" + pc.getId() + ">修改前名字為：【" + pc.getName() + "】，修改後名字為：【" + name2 + "】");
                                J_Main.getInstance().delPlayerTable(pc.getName());
                                pc.setName(name2);
                                J_Main.getInstance().addPlayerTable(pc.getAccountName(), name2, pc.getNetConnection().getIp());
                                CharObjidTable.get().reChar(pc.getId(), name2);
                                CharacterTable.get().newCharName(pc.getId(), name2);
                                World.get().storeObject(pc);
                                pc.sendPacketsAll(new S_ChangeName(pc, true));
                                pc.sendPackets(new S_ServerMessage(166, "由於人物名稱異動!請重新登入遊戲!將於5秒後強制斷線!"));
                                new KickPc(this, pc, null).start_cmd();
                            }
                        } else {
                            pc.setTempID(0);
                            renamePet((L1PetInstance) World.get().findObject(pc.getTempID()), petName);
                        }
                        pc.rename(false);
                        break;
                    case 479:
                        if (readC() == 1) {
                            String s = readS();
                            if (pc.getLevel() - 50 > pc.getBonusStats()) {
                                if (s.equalsIgnoreCase("str")) {
                                    if (pc.getBaseStr() < ConfigAlt.POWER) {
                                        pc.addBaseStr(1);
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "， 請重試一次。"));
                                    }
                                } else if (s.equalsIgnoreCase("dex")) {
                                    if (pc.getBaseDex() < ConfigAlt.POWER) {
                                        pc.addBaseDex(1);
                                        pc.resetBaseAc();
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "，請重試一次。"));
                                    }
                                } else if (s.equalsIgnoreCase("con")) {
                                    if (pc.getBaseCon() < ConfigAlt.POWER) {
                                        pc.addBaseCon(1);
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "，請重試一次。"));
                                    }
                                } else if (s.equalsIgnoreCase("int")) {
                                    if (pc.getBaseInt() < ConfigAlt.POWER) {
                                        pc.addBaseInt(1);
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "，請重試一次。"));
                                    }
                                } else if (s.equalsIgnoreCase("wis")) {
                                    if (pc.getBaseWis() < ConfigAlt.POWER) {
                                        pc.addBaseWis(1);
                                        pc.resetBaseMr();
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "， 請重試一次。"));
                                    }
                                } else if (s.equalsIgnoreCase("cha")) {
                                    if (pc.getBaseCha() < ConfigAlt.POWER) {
                                        pc.addBaseCha(1);
                                        pc.setBonusStats(pc.getBonusStats() + 1);
                                        pc.sendPackets(new S_OwnCharStatus2(pc));
                                        pc.sendPackets(new S_CharVisualUpdate(pc));
                                        pc.save();
                                    } else {
                                        pc.sendPackets(new S_ServerMessage("屬性最大值只能到" + ConfigAlt.POWER + "，請重試一次。"));
                                    }
                                }
                                if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getBonusStats() && pc.getBaseStr() + pc.getBaseDex() + pc.getBaseCon() + pc.getBaseInt() + pc.getBaseWis() + pc.getBaseCha() < ConfigAlt.POWER * 6) {
                                    pc.sendPackets(new S_Bonusstats(pc.getId()));
                                    break;
                                }
                            } else {
                                over();
                                return;
                            }
                        }
                        break;
                    case 512:
                        readC();
                        String houseName = readS();
                        int houseId = pc.getTempID();
                        pc.setTempID(0);
                        if (houseName.length() <= 16) {
                            L1House house = HouseReading.get().getHouseTable(houseId);
                            house.setHouseName(houseName);
                            HouseReading.get().updateHouse(house);
                            break;
                        } else {
                            pc.sendPackets(new S_ServerMessage(513));
                            break;
                        }
                    case 630:
                        int c6 = readC();
                        L1PcInstance fightPc = (L1PcInstance) World.get().findObject(pc.getFightId());
                        if (c6 != 0) {
                            if (c6 == 1) {
                                fightPc.sendPackets(new S_PacketBox(5, fightPc.getFightId(), fightPc.getId()));
                                pc.sendPackets(new S_PacketBox(5, pc.getFightId(), pc.getId()));
                                break;
                            }
                        } else {
                            pc.setFightId(0);
                            fightPc.setFightId(0);
                            fightPc.sendPackets(new S_ServerMessage(631, pc.getName()));
                            break;
                        }
                        break;
                    case 653:
                        int c7 = readC();
                        L1PcInstance target653 = (L1PcInstance) World.get().findObject(pc.getPartnerId());
                        if (c7 != 0) {
                            if (c7 == 1) {
                                if (target653 != null) {
                                    target653.setPartnerId(0);
                                    target653.save();
                                    target653.sendPackets(new S_ServerMessage(662));
                                } else {
                                    CharacterTable.get();
                                    CharacterTable.updatePartnerId(pc.getPartnerId());
                                }
                            }
                            pc.setPartnerId(0);
                            pc.save();
                            pc.sendPackets(new S_ServerMessage(662));
                            break;
                        } else {
                            over();
                            return;
                        }
                    case 654:
                        int c8 = readC();
                        L1PcInstance partner = (L1PcInstance) World.get().findObject(pc.getTempID());
                        pc.setTempID(0);
                        if (partner != null) {
                            if (c8 != 0) {
                                if (c8 == 1) {
                                    pc.setPartnerId(partner.getId());
                                    pc.save();
                                    pc.sendPackets(new S_ServerMessage(790));
                                    pc.sendPackets(new S_ServerMessage(655, partner.getName()));
                                    partner.setPartnerId(pc.getId());
                                    partner.save();
                                    partner.sendPackets(new S_ServerMessage(790));
                                    partner.sendPackets(new S_ServerMessage(655, pc.getName()));
                                    break;
                                }
                            } else {
                                partner.sendPackets(new S_ServerMessage(656, pc.getName()));
                                break;
                            }
                        }
                        break;
                    case 729:
                        int c9 = readC();
                        if (c9 != 0 && c9 == 1) {
                            callClan(pc);
                            break;
                        }
                    case 738:
                        int c10 = readC();
                        if (c10 != 0 && c10 == 1 && pc.getExpRes() == 1) {
                            int level = pc.getLevel();
                            int lawful = pc.getLawful();
                            if (level < 45) {
                                cost = level * level * 100;
                            } else {
                                cost = level * level * 200;
                            }
                            if (lawful >= 0) {
                                cost /= 2;
                            }
                            if (pc.getInventory().consumeItem(L1ItemId.ADENA, (long) cost)) {
                                pc.resExp();
                                pc.setExpRes(0);
                                break;
                            } else {
                                pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                                break;
                            }
                        }
                    case 748:
                        int c11 = readC();
                        if (c11 != 0 && c11 == 1) {
                            L1Teleport.teleport(pc, pc.getTeleportX(), pc.getTeleportY(), pc.getTeleportMapId(), 5, true);
                            break;
                        }
                    case 951:
                        int c12 = readC();
                        L1PcInstance chatPc = (L1PcInstance) World.get().findObject(pc.getPartyID());
                        if (chatPc != null) {
                            if (c12 != 0) {
                                if (c12 == 1) {
                                    if (chatPc.isInChatParty()) {
                                        if (!chatPc.getChatParty().isVacancy() && !chatPc.isGm()) {
                                            chatPc.sendPackets(new S_ServerMessage(417));
                                            break;
                                        } else {
                                            chatPc.getChatParty().addMember(pc);
                                            break;
                                        }
                                    } else {
                                        L1ChatParty chatParty = new L1ChatParty();
                                        chatParty.addMember(chatPc);
                                        chatParty.addMember(pc);
                                        chatPc.sendPackets(new S_ServerMessage(424, pc.getName()));
                                        break;
                                    }
                                }
                            } else {
                                chatPc.sendPackets(new S_ServerMessage(423, pc.getName()));
                                pc.setPartyID(0);
                                break;
                            }
                        }
                        break;
                    case 953:
                        int c13 = readC();
                        L1PcInstance target = (L1PcInstance) World.get().findObject(pc.getPartyID());
                        if (target != null) {
                            if (c13 != 0) {
                                if (c13 == 1) {
                                    if (target.isInParty()) {
                                        if (target.getParty().isVacancy()) {
                                            target.getParty().addMember(pc);
                                            break;
                                        } else {
                                            target.sendPackets(new S_ServerMessage(417));
                                            break;
                                        }
                                    } else {
                                        L1Party party = new L1Party();
                                        party.addMember(target);
                                        party.addMember(pc);
                                        target.sendPackets(new S_ServerMessage(424, pc.getName()));
                                        break;
                                    }
                                }
                            } else {
                                target.sendPackets(new S_ServerMessage(423, pc.getName()));
                                pc.setPartyID(0);
                                break;
                            }
                        }
                        break;
                    case 1415:
                        int c14 = readC();
                        if (c14 != 0 && c14 == 1) {
                            int locX = pc.getTeleportX();
                            int locY = pc.getTeleportY();
                            short mapId = pc.getTeleportMapId();
                            pc.getInventory().consumeItem(ConfigOther.SET_ITEM, (long) ConfigOther.SET_ITEM_COUNT);
                            L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
                            break;
                        }
                    case 1416:
                        int c15 = readC();
                        if (c15 != 0 && c15 == 1) {
                            int locX2 = pc.getTeleportX();
                            int locY2 = pc.getTeleportY();
                            short mapId2 = pc.getTeleportMapId();
                            pc.getInventory().consumeItem(ConfigOther.SET_ITEM_Party, (long) ConfigOther.SET_ITEM_COUNT_Party);
                            L1Teleport.teleport(pc, locX2, locY2, mapId2, 5, true);
                            break;
                        }
                }
                over();
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void changeClan(ClientExecutor clientthread, L1PcInstance pc, L1PcInstance joinPc, int maxMember) {
        L1PcInstance[] clanMember;
        int clanId = pc.getClanid();
        String clanName = pc.getClanname();
        L1Clan clan = WorldClan.get().getClan(clanName);
        int clanNum = clan.getAllMembers().length;
        int oldClanId = joinPc.getClanid();
        String oldClanName = joinPc.getClanname();
        L1Clan oldClan = WorldClan.get().getClan(oldClanName);
        String[] oldClanMemberName = oldClan.getAllMembers();
        int oldClanNum = oldClanMemberName.length;
        if (clan != null && oldClan != null && joinPc.isCrown() && joinPc.getId() == oldClan.getLeaderId()) {
            if (maxMember < clanNum + oldClanNum) {
                joinPc.sendPackets(new S_ServerMessage(188, pc.getName()));
                return;
            }
            for (L1PcInstance l1PcInstance : clan.getOnlineClanMember()) {
                l1PcInstance.sendPackets(new S_ServerMessage(94, joinPc.getName()));
            }
            for (int i = 0; i < oldClanMemberName.length; i++) {
                L1PcInstance oldClanMember = World.get().getPlayer(oldClanMemberName[i]);
                if (oldClanMember != null) {
                    oldClanMember.setClanid(clanId);
                    oldClanMember.setClanname(clanName);
                    if (oldClanMember.getId() == joinPc.getId()) {
                        oldClanMember.setClanRank(3);
                    } else {
                        oldClanMember.setClanRank(5);
                    }
                    try {
                        oldClanMember.save();
                    } catch (Exception e) {
                        _log.error(e.getLocalizedMessage(), e);
                    }
                    clan.addMemberName(oldClanMember.getName());
                    oldClanMember.sendPackets(new S_ServerMessage(95, clanName));
                } else {
                    try {
                        L1PcInstance offClanMember = CharacterTable.get().restoreCharacter(oldClanMemberName[i]);
                        offClanMember.setClanid(clanId);
                        offClanMember.setClanname(clanName);
                        offClanMember.setClanRank(1);
                        offClanMember.save();
                        clan.addMemberName(offClanMember.getName());
                    } catch (Exception e2) {
                        _log.error(e2.getLocalizedMessage(), e2);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
            ClanEmblemReading.get().deleteIcon(oldClanId);
            ClanReading.get().deleteClan(oldClanName);
        }
    }

    private static void renamePet(L1PetInstance pet, String name) throws Exception {
        if (pet == null || name == null) {
            throw new NullPointerException();
        }
        L1Pet petTemplate = PetReading.get().getTemplate(pet.getItemObjId());
        if (petTemplate == null) {
            throw new NullPointerException();
        }
        L1PcInstance pc = (L1PcInstance) pet.getMaster();
        if (PetReading.get().isNameExists(name)) {
            pc.sendPackets(new S_ServerMessage(327));
            return;
        }
        if (!pet.getName().equalsIgnoreCase(NpcTable.get().getTemplate(pet.getNpcId()).get_name())) {
            pc.sendPackets(new S_ServerMessage(326));
            return;
        }
        pet.setName(name);
        petTemplate.set_name(name);
        PetReading.get().storePet(petTemplate);
        pc.getInventory().updateItem(pc.getInventory().getItem(pet.getItemObjId()));
        pc.sendPacketsAll(new S_ChangeName(pet.getId(), name));
    }

    private void callClan(L1PcInstance pc) {
        L1PcInstance callClanPc = (L1PcInstance) World.get().findObject(pc.getTempID());
        pc.setTempID(0);
        if (pc.isParalyzedX() || callClanPc == null) {
            return;
        }
        if (!pc.getMap().isEscapable() && !pc.isGm()) {
            pc.sendPackets(new S_ServerMessage(647));
        } else if (pc.getId() == callClanPc.getCallClanId()) {
            boolean isInWarArea = false;
            int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);
            if (castleId != 0) {
                isInWarArea = true;
                if (ServerWarExecutor.get().isNowWar(castleId)) {
                    isInWarArea = false;
                }
            }
            short mapId = callClanPc.getMapId();
            if ((mapId != 0 && mapId != 4 && mapId != 304) || isInWarArea) {
                pc.sendPackets(new S_ServerMessage(629));
            } else if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
                pc.sendPackets(new S_ServerMessage(629));
            } else {
                int[] HEADING_TABLE_X = new int[8];
                HEADING_TABLE_X[1] = 1;
                HEADING_TABLE_X[2] = 1;
                HEADING_TABLE_X[3] = 1;
                HEADING_TABLE_X[5] = -1;
                HEADING_TABLE_X[6] = -1;
                HEADING_TABLE_X[7] = -1;
                int[] HEADING_TABLE_Y = new int[8];
                HEADING_TABLE_Y[0] = -1;
                HEADING_TABLE_Y[1] = -1;
                HEADING_TABLE_Y[3] = 1;
                HEADING_TABLE_Y[4] = 1;
                HEADING_TABLE_Y[5] = 1;
                HEADING_TABLE_Y[7] = -1;
                L1Map map = callClanPc.getMap();
                int locX = callClanPc.getX();
                int locY = callClanPc.getY();
                int heading = callClanPc.getCallClanHeading();
                int locX2 = locX + HEADING_TABLE_X[heading];
                int locY2 = locY + HEADING_TABLE_Y[heading];
                int heading2 = (heading + 4) % 4;
                boolean isExsistCharacter = false;
                Iterator<L1Object> it = World.get().getVisibleObjects(callClanPc, 1).iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    L1Object object = it.next();
                    if (object instanceof L1Character) {
                        L1Character cha = (L1Character) object;
                        if (cha.getX() == locX2 && cha.getY() == locY2 && cha.getMapId() == mapId) {
                            isExsistCharacter = true;
                            break;
                        }
                    }
                }
                if (!(locX2 == 0 && locY2 == 0) && map.isPassable(locX2, locY2, (L1Character) null) && !isExsistCharacter) {
                    L1Teleport.teleport(pc, locX2, locY2, mapId, heading2, true, 3);
                } else {
                    pc.sendPackets(new S_ServerMessage(627));
                }
            }
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }

    private class KickPc implements Runnable {
        private ClientExecutor _client;

        private KickPc(L1PcInstance pc) {
            this._client = pc.getNetConnection();
        }

        /* synthetic */ KickPc(C_Attr c_Attr, L1PcInstance l1PcInstance, KickPc kickPc) {
            this(l1PcInstance);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void start_cmd() {
            GeneralThreadPool.get().execute(this);
        }

        public void run() {
            try {
                Thread.sleep(5000);
                this._client.kick();
            } catch (InterruptedException e) {
                C_Attr._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
