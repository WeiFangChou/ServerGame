package com.lineage.server.model.Instance;

import com.lineage.echo.OpcodesClient;
import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.datatables.lock.TownReading;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.gametime.L1GameTimeClock;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_NPCPack_M;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.WorldClan;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1MerchantInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1MerchantInstance.class);
    private static final long serialVersionUID = 1;

    public L1MerchantInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_M(this));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            new L1AttackPc(pc, this).action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            setActived(false);
            startAI();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) throws Exception {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        int npcid = getNpcTemplate().get_npcId();
        L1PcQuest quest = player.getQuest();
        String htmlid = null;
        String[] htmldata = null;
        int pcX = player.getX();
        int pcY = player.getY();
        int npcX = getX();
        int npcY = getY();
        if (this.WORK == null && getNpcTemplate().getChangeHead()) {
            if (pcX == npcX && pcY < npcY) {
                setHeading(0);
            } else if (pcX > npcX && pcY < npcY) {
                setHeading(1);
            } else if (pcX > npcX && pcY == npcY) {
                setHeading(2);
            } else if (pcX > npcX && pcY > npcY) {
                setHeading(3);
            } else if (pcX == npcX && pcY > npcY) {
                setHeading(4);
            } else if (pcX < npcX && pcY > npcY) {
                setHeading(5);
            } else if (pcX < npcX && pcY == npcY) {
                setHeading(6);
            } else if (pcX < npcX && pcY < npcY) {
                setHeading(7);
            }
            broadcastPacketAll(new S_ChangeHeading(this));
        }
        if (talking != null) {
            if (npcid == 70841) {
                htmlid = player.isElf() ? "luudielE1" : player.isDarkelf() ? "luudielCE1" : "luudiel1";
            } else if (npcid == 70087) {
                if (player.isDarkelf()) {
                    htmlid = "sedia";
                }
            } else if (npcid == 70099) {
                if (!quest.isEnd(11) && player.getLevel() > 13) {
                    htmlid = "kuper1";
                }
            } else if (npcid == 70796) {
                if (!quest.isEnd(11) && player.getLevel() > 13) {
                    htmlid = "dunham1";
                }
            } else if (npcid == 70011) {
                int time = L1GameTimeClock.getInstance().currentTime().getSeconds() % 86400;
                if (time < 21600 || time > 72000) {
                    htmlid = "shipEvI6";
                }
            } else if (npcid == 70553) {
                if (!checkHasCastle(player, 1)) {
                    htmlid = "ishmael7";
                } else if (checkClanLeader(player)) {
                    htmlid = "ishmael1";
                } else {
                    htmlid = "ishmael6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70822) {
                if (!checkHasCastle(player, 2)) {
                    htmlid = "seghem7";
                } else if (checkClanLeader(player)) {
                    htmlid = "seghem1";
                } else {
                    htmlid = "seghem6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70784) {
                if (!checkHasCastle(player, 3)) {
                    htmlid = "othmond7";
                } else if (checkClanLeader(player)) {
                    htmlid = "othmond1";
                } else {
                    htmlid = "othmond6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70623) {
                if (!checkHasCastle(player, 4)) {
                    htmlid = "orville7";
                } else if (checkClanLeader(player)) {
                    htmlid = "orville1";
                } else {
                    htmlid = "orville6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70880) {
                if (!checkHasCastle(player, 5)) {
                    htmlid = "fisher7";
                } else if (checkClanLeader(player)) {
                    htmlid = "fisher1";
                } else {
                    htmlid = "fisher6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70665) {
                if (!checkHasCastle(player, 6)) {
                    htmlid = "potempin7";
                } else if (checkClanLeader(player)) {
                    htmlid = "potempin1";
                } else {
                    htmlid = "potempin6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 70721) {
                if (!checkHasCastle(player, 7)) {
                    htmlid = "timon7";
                } else if (checkClanLeader(player)) {
                    htmlid = "timon1";
                } else {
                    htmlid = "timon6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 81155) {
                if (!checkHasCastle(player, 8)) {
                    htmlid = "olle7";
                } else if (checkClanLeader(player)) {
                    htmlid = "olle1";
                } else {
                    htmlid = "olle6";
                    htmldata = new String[]{player.getName()};
                }
            } else if (npcid == 80057) {
                switch (player.getKarmaLevel()) {
                    case OpcodesClient.C_OPCODE_SOLDIERGIVEOK:
                        htmlid = "cyk8";
                        break;
                    case OpcodesClient.C_OPCODE_SOLDIERGIVE:
                        htmlid = "cyk7";
                        break;
                    case OpcodesClient.C_OPCODE_SOLDIERBUY:
                        htmlid = "cyk6";
                        break;
                    case OpcodesClient.C_OPCODE_HORUNOK:
                        htmlid = "cyk5";
                        break;
                    case OpcodesClient.C_OPCODE_HORUN:
                        htmlid = "cyk4";
                        break;
                    case -3:
                        htmlid = "cyk3";
                        break;
                    case OpcodesClient.C_OPCODE_HIRESOLDIER:
                        htmlid = "cyk2";
                        break;
                    case -1:
                        htmlid = "cyk1";
                        break;
                    case 0:
                        htmlid = "alfons1";
                        break;
                    case 1:
                        htmlid = "cbk1";
                        break;
                    case 2:
                        htmlid = "cbk2";
                        break;
                    case 3:
                        htmlid = "cbk3";
                        break;
                    case 4:
                        htmlid = "cbk4";
                        break;
                    case 5:
                        htmlid = "cbk5";
                        break;
                    case 6:
                        htmlid = "cbk6";
                        break;
                    case 7:
                        htmlid = "cbk7";
                        break;
                    case 8:
                        htmlid = "cbk8";
                        break;
                    default:
                        htmlid = "alfons1";
                        break;
                }
            } else if (npcid == 80058) {
                int level = player.getLevel();
                htmlid = level <= 44 ? "cpass03" : (level > 51 || 45 > level) ? "cpass01" : "cpass02";
            } else if (npcid == 80059) {
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) {
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40917)) {
                    htmlid = "wpass14";
                } else if (player.getInventory().checkItem(40912) || player.getInventory().checkItem(40910) || player.getInventory().checkItem(40911)) {
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40909)) {
                    int count = getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40913, (long) count)) {
                        createRuler(player, 1, count);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else {
                    htmlid = player.getInventory().checkItem(40913) ? "wpass08" : "wpass05";
                }
            } else if (npcid == 80060) {
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) {
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40920)) {
                    htmlid = "wpass13";
                } else if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40910) || player.getInventory().checkItem(40911)) {
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40912)) {
                    int count2 = getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40916, (long) count2)) {
                        try {
                            createRuler(player, 8, count2);
                            htmlid = "wpass06";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        htmlid = "wpass03";
                    }
                } else {
                    htmlid = player.getInventory().checkItem(40916) ? "wpass08" : "wpass05";
                }
            } else if (npcid == 80061) {
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) {
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40918)) {
                    htmlid = "wpass11";
                } else if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player.getInventory().checkItem(40911)) {
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40910)) {
                    int count3 = getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40914, (long) count3)) {
                        createRuler(player, 4, count3);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else {
                    htmlid = player.getInventory().checkItem(40914) ? "wpass08" : "wpass05";
                }
            } else if (npcid == 80062) {
                if (player.getKarmaLevel() > 0) {
                    htmlid = "cpass03";
                } else if (player.getInventory().checkItem(40921)) {
                    htmlid = "wpass02";
                } else if (player.getInventory().checkItem(40919)) {
                    htmlid = "wpass12";
                } else if (player.getInventory().checkItem(40909) || player.getInventory().checkItem(40912) || player.getInventory().checkItem(40910)) {
                    htmlid = "wpass04";
                } else if (player.getInventory().checkItem(40911)) {
                    int count4 = getNecessarySealCount(player);
                    if (player.getInventory().checkItem(40915, (long) count4)) {
                        createRuler(player, 2, count4);
                        htmlid = "wpass06";
                    } else {
                        htmlid = "wpass03";
                    }
                } else {
                    htmlid = player.getInventory().checkItem(40915) ? "wpass08" : "wpass05";
                }
            } else if (npcid == 80065) {
                htmlid = player.getKarmaLevel() < 3 ? "uturn0" : "uturn1";
            } else if (npcid == 80047) {
                htmlid = player.getKarmaLevel() > -3 ? "uhelp1" : "uhelp2";
            } else if (npcid == 80049) {
                htmlid = player.getKarma() <= -10000000 ? "betray11" : "betray12";
            } else if (npcid == 80050) {
                htmlid = player.getKarmaLevel() > -1 ? "meet103" : "meet101";
            } else if (npcid == 80053) {
                int karmaLevel = player.getKarmaLevel();
                if (karmaLevel == 0) {
                    htmlid = "aliceyet";
                } else if (karmaLevel >= 1) {
                    htmlid = (player.getInventory().checkItem(196) || player.getInventory().checkItem(197) || player.getInventory().checkItem(198) || player.getInventory().checkItem(199) || player.getInventory().checkItem(200) || player.getInventory().checkItem(201) || player.getInventory().checkItem(202) || player.getInventory().checkItem(203)) ? "alice_gd" : "gd";
                } else if (karmaLevel <= -1) {
                    if (!player.getInventory().checkItem(40991)) {
                        htmlid = player.getInventory().checkItem(196) ? karmaLevel <= -2 ? "Mate_2" : "alice_1" : player.getInventory().checkItem(197) ? karmaLevel <= -3 ? "Mate_3" : "alice_2" : player.getInventory().checkItem(198) ? karmaLevel <= -4 ? "Mate_4" : "alice_3" : player.getInventory().checkItem(199) ? karmaLevel <= -5 ? "Mate_5" : "alice_4" : player.getInventory().checkItem(200) ? karmaLevel <= -6 ? "Mate_6" : "alice_5" : player.getInventory().checkItem(201) ? karmaLevel <= -7 ? "Mate_7" : "alice_6" : player.getInventory().checkItem(202) ? karmaLevel <= -8 ? "Mate_8" : "alice_7" : player.getInventory().checkItem(203) ? "alice_8" : "alice_no";
                    } else if (karmaLevel <= -1) {
                        htmlid = "Mate_1";
                    }
                }
            } else if (npcid == 80055) {
                int amuletLevel = 0;
                if (player.getInventory().checkItem(20358)) {
                    amuletLevel = 1;
                } else if (player.getInventory().checkItem(20359)) {
                    amuletLevel = 2;
                } else if (player.getInventory().checkItem(20360)) {
                    amuletLevel = 3;
                } else if (player.getInventory().checkItem(20361)) {
                    amuletLevel = 4;
                } else if (player.getInventory().checkItem(20362)) {
                    amuletLevel = 5;
                } else if (player.getInventory().checkItem(20363)) {
                    amuletLevel = 6;
                } else if (player.getInventory().checkItem(20364)) {
                    amuletLevel = 7;
                } else if (player.getInventory().checkItem(20365)) {
                    amuletLevel = 8;
                }
                htmlid = player.getKarmaLevel() == -1 ? amuletLevel >= 1 ? "uamuletd" : "uamulet1" : player.getKarmaLevel() == -2 ? amuletLevel >= 2 ? "uamuletd" : "uamulet2" : player.getKarmaLevel() == -3 ? amuletLevel >= 3 ? "uamuletd" : "uamulet3" : player.getKarmaLevel() == -4 ? amuletLevel >= 4 ? "uamuletd" : "uamulet4" : player.getKarmaLevel() == -5 ? amuletLevel >= 5 ? "uamuletd" : "uamulet5" : player.getKarmaLevel() == -6 ? amuletLevel >= 6 ? "uamuletd" : "uamulet6" : player.getKarmaLevel() == -7 ? amuletLevel >= 7 ? "uamuletd" : "uamulet7" : player.getKarmaLevel() == -8 ? amuletLevel >= 8 ? "uamuletd" : "uamulet8" : "uamulet0";
            } else if (npcid == 80056) {
                htmlid = player.getKarma() <= -10000000 ? "infamous11" : "infamous12";
            } else if (npcid == 80064) {
                htmlid = player.getKarmaLevel() < 1 ? "meet003" : "meet001";
            } else if (npcid == 80066) {
                htmlid = player.getKarma() >= 10000000 ? "betray01" : "betray02";
            } else if (npcid == 80071) {
                int earringLevel = 0;
                if (player.getInventory().checkItem(21020)) {
                    earringLevel = 1;
                } else if (player.getInventory().checkItem(21021)) {
                    earringLevel = 2;
                } else if (player.getInventory().checkItem(21022)) {
                    earringLevel = 3;
                } else if (player.getInventory().checkItem(21023)) {
                    earringLevel = 4;
                } else if (player.getInventory().checkItem(21024)) {
                    earringLevel = 5;
                } else if (player.getInventory().checkItem(21025)) {
                    earringLevel = 6;
                } else if (player.getInventory().checkItem(21026)) {
                    earringLevel = 7;
                } else if (player.getInventory().checkItem(21027)) {
                    earringLevel = 8;
                }
                htmlid = player.getKarmaLevel() == 1 ? earringLevel >= 1 ? "lringd" : "lring1" : player.getKarmaLevel() == 2 ? earringLevel >= 2 ? "lringd" : "lring2" : player.getKarmaLevel() == 3 ? earringLevel >= 3 ? "lringd" : "lring3" : player.getKarmaLevel() == 4 ? earringLevel >= 4 ? "lringd" : "lring4" : player.getKarmaLevel() == 5 ? earringLevel >= 5 ? "lringd" : "lring5" : player.getKarmaLevel() == 6 ? earringLevel >= 6 ? "lringd" : "lring6" : player.getKarmaLevel() == 7 ? earringLevel >= 7 ? "lringd" : "lring7" : player.getKarmaLevel() == 8 ? earringLevel >= 8 ? "lringd" : "lring8" : "lring0";
            } else if (npcid == 80072) {
                int karmaLevel2 = player.getKarmaLevel();
                htmlid = karmaLevel2 == 1 ? "lsmith0" : karmaLevel2 == 2 ? "lsmith1" : karmaLevel2 == 3 ? "lsmith2" : karmaLevel2 == 4 ? "lsmith3" : karmaLevel2 == 5 ? "lsmith4" : karmaLevel2 == 6 ? "lsmith5" : karmaLevel2 == 7 ? "lsmith7" : karmaLevel2 == 8 ? "lsmith8" : "";
            } else if (npcid == 80074) {
                htmlid = player.getKarma() >= 10000000 ? "infamous01" : "infamous02";
            } else if (npcid == 80104) {
                if (!player.isCrown()) {
                    htmlid = "horseseller4";
                }
            } else if (npcid == 70528) {
                htmlid = talkToTownmaster(player, 1);
            } else if (npcid == 70546) {
                htmlid = talkToTownmaster(player, 6);
            } else if (npcid == 70567) {
                htmlid = talkToTownmaster(player, 3);
            } else if (npcid == 70815) {
                htmlid = talkToTownmaster(player, 4);
            } else if (npcid == 70774) {
                htmlid = talkToTownmaster(player, 5);
            } else if (npcid == 70799) {
                htmlid = talkToTownmaster(player, 2);
            } else if (npcid == 70594) {
                htmlid = talkToTownmaster(player, 7);
            } else if (npcid == 70860) {
                htmlid = talkToTownmaster(player, 8);
            } else if (npcid == 70654) {
                htmlid = talkToTownmaster(player, 9);
            } else if (npcid == 70748) {
                htmlid = talkToTownmaster(player, 10);
            } else if (npcid == 70534) {
                htmlid = talkToTownadviser(player, 1);
            } else if (npcid == 70556) {
                htmlid = talkToTownadviser(player, 6);
            } else if (npcid == 70572) {
                htmlid = talkToTownadviser(player, 3);
            } else if (npcid == 70830) {
                htmlid = talkToTownadviser(player, 4);
            } else if (npcid == 70788) {
                htmlid = talkToTownadviser(player, 5);
            } else if (npcid == 70806) {
                htmlid = talkToTownadviser(player, 2);
            } else if (npcid == 70631) {
                htmlid = talkToTownadviser(player, 7);
            } else if (npcid == 70876) {
                htmlid = talkToTownadviser(player, 8);
            } else if (npcid == 70663) {
                htmlid = talkToTownadviser(player, 9);
            } else if (npcid == 70761) {
                htmlid = talkToTownadviser(player, 10);
            } else if (npcid == 70506) {
                htmlid = talkToRuba(player);
            } else if (npcid == 71026) {
                if (player.getLevel() < 10) {
                    htmlid = "en0113";
                } else if (player.getLevel() >= 10 && player.getLevel() < 25) {
                    htmlid = "en0111";
                } else if (player.getLevel() > 25) {
                    htmlid = "en0112";
                }
            } else if (npcid == 71027) {
                if (player.getLevel() < 10) {
                    htmlid = "en0283";
                } else if (player.getLevel() >= 10 && player.getLevel() < 25) {
                    htmlid = "en0281";
                } else if (player.getLevel() > 25) {
                    htmlid = "en0282";
                }
            } else if (npcid == 70512) {
                if (player.getLevel() >= 25) {
                    htmlid = "jpe0102";
                }
            } else if (npcid == 70514) {
                if (player.getLevel() >= 25) {
                    htmlid = "jpe0092";
                }
            } else if (npcid == 71038) {
                htmlid = player.getInventory().checkItem(41060) ? (player.getInventory().checkItem(41090) || player.getInventory().checkItem(41091) || player.getInventory().checkItem(41092)) ? "orcfnoname7" : "orcfnoname8" : "orcfnoname1";
            } else if (npcid == 71040) {
                htmlid = player.getInventory().checkItem(41060) ? player.getInventory().checkItem(41065) ? (player.getInventory().checkItem(41086) || player.getInventory().checkItem(41087) || player.getInventory().checkItem(41088) || player.getInventory().checkItem(41089)) ? "orcfnoa6" : "orcfnoa5" : "orcfnoa2" : "orcfnoa1";
            } else if (npcid == 71041) {
                htmlid = player.getInventory().checkItem(41060) ? player.getInventory().checkItem(41064) ? (player.getInventory().checkItem(41081) || player.getInventory().checkItem(41082) || player.getInventory().checkItem(41083) || player.getInventory().checkItem(41084) || player.getInventory().checkItem(41085)) ? "orcfhuwoomo2" : "orcfhuwoomo8" : "orcfhuwoomo1" : "orcfhuwoomo5";
            } else if (npcid == 71042) {
                htmlid = player.getInventory().checkItem(41060) ? player.getInventory().checkItem(41062) ? (player.getInventory().checkItem(41071) || player.getInventory().checkItem(41072) || player.getInventory().checkItem(41073) || player.getInventory().checkItem(41074) || player.getInventory().checkItem(41075)) ? "orcfbakumo2" : "orcfbakumo8" : "orcfbakumo1" : "orcfbakumo5";
            } else if (npcid == 71043) {
                htmlid = player.getInventory().checkItem(41060) ? player.getInventory().checkItem(41063) ? (player.getInventory().checkItem(41076) || player.getInventory().checkItem(41077) || player.getInventory().checkItem(41078) || player.getInventory().checkItem(41079) || player.getInventory().checkItem(41080)) ? "orcfbuka2" : "orcfbuka8" : "orcfbuka1" : "orcfbuka5";
            } else if (npcid == 71044) {
                htmlid = player.getInventory().checkItem(41060) ? player.getInventory().checkItem(41061) ? (player.getInventory().checkItem(41066) || player.getInventory().checkItem(41067) || player.getInventory().checkItem(41068) || player.getInventory().checkItem(41069) || player.getInventory().checkItem(41070)) ? "orcfkame2" : "orcfkame8" : "orcfkame1" : "orcfkame5";
            } else if (npcid == 71055) {
                if (player.getQuest().get_step(30) == 3) {
                    htmlid = "lukein13";
                } else if (player.getQuest().get_step(23) == 255 && player.getQuest().get_step(30) == 2 && player.getInventory().checkItem(40631)) {
                    htmlid = "lukein10";
                } else if (player.getQuest().get_step(23) == 255) {
                    htmlid = "lukein0";
                } else if (player.getQuest().get_step(23) == 11) {
                    if (player.getInventory().checkItem(40716)) {
                        htmlid = "lukein9";
                    }
                } else if (player.getQuest().get_step(23) >= 1 && player.getQuest().get_step(23) <= 10) {
                    htmlid = "lukein8";
                }
            } else if (npcid == 71063) {
                if (player.getQuest().get_step(24) != 255 && player.getQuest().get_step(23) == 1) {
                    htmlid = "maptbox";
                }
            } else if (npcid == 71064) {
                if (player.getQuest().get_step(23) == 2) {
                    htmlid = talkToSecondtbox(player);
                }
            } else if (npcid == 71065) {
                if (player.getQuest().get_step(23) == 3) {
                    htmlid = talkToSecondtbox(player);
                }
            } else if (npcid == 71066) {
                if (player.getQuest().get_step(23) == 4) {
                    htmlid = talkToSecondtbox(player);
                }
            } else if (npcid == 71067) {
                if (player.getQuest().get_step(23) == 5) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71068) {
                if (player.getQuest().get_step(23) == 6) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71069) {
                if (player.getQuest().get_step(23) == 7) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71070) {
                if (player.getQuest().get_step(23) == 8) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71071) {
                if (player.getQuest().get_step(23) == 9) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71072) {
                if (player.getQuest().get_step(23) == 10) {
                    htmlid = talkToThirdtbox(player);
                }
            } else if (npcid == 71056) {
                if (player.getQuest().get_step(30) == 4) {
                    htmlid = player.getInventory().checkItem(40631) ? "SIMIZZ11" : "SIMIZZ0";
                } else if (player.getQuest().get_step(27) == 2) {
                    htmlid = "SIMIZZ0";
                } else if (player.getQuest().get_step(27) == 255) {
                    htmlid = "SIMIZZ15";
                } else if (player.getQuest().get_step(27) == 1) {
                    htmlid = "SIMIZZ6";
                }
            } else if (npcid == 71057) {
                if (player.getQuest().get_step(28) == 255) {
                    htmlid = "doil4b";
                }
            } else if (npcid == 71059) {
                htmlid = player.getQuest().get_step(29) == 255 ? "rudian1c" : player.getQuest().get_step(29) == 1 ? "rudian7" : player.getQuest().get_step(28) == 255 ? "rudian1b" : "rudian1a";
            } else if (npcid == 71060) {
                if (player.getQuest().get_step(30) == 255) {
                    htmlid = "resta1e";
                } else if (player.getQuest().get_step(27) == 255) {
                    htmlid = "resta14";
                } else if (player.getQuest().get_step(30) == 4) {
                    htmlid = "resta13";
                } else if (player.getQuest().get_step(30) == 3) {
                    htmlid = "resta11";
                    player.getQuest().set_step(30, 4);
                } else if (player.getQuest().get_step(30) == 2) {
                    htmlid = "resta16";
                } else if ((player.getQuest().get_step(27) == 2 && player.getQuest().get_step(31) == 1) || player.getInventory().checkItem(40647)) {
                    htmlid = "resta1a";
                } else if (player.getQuest().get_step(31) == 1 || player.getInventory().checkItem(40647)) {
                    htmlid = "resta1c";
                } else if (player.getQuest().get_step(27) == 2) {
                    htmlid = "resta1b";
                }
            } else if (npcid == 71061) {
                if (player.getQuest().get_step(31) == 255) {
                    htmlid = "cadmus1c";
                } else if (player.getQuest().get_step(31) == 3) {
                    htmlid = "cadmus8";
                } else if (player.getQuest().get_step(31) == 2) {
                    htmlid = "cadmus1a";
                } else if (player.getQuest().get_step(28) == 255) {
                    htmlid = "cadmus1b";
                }
            } else if (npcid == 71036) {
                if (player.getQuest().get_step(32) == 255) {
                    htmlid = "kamyla26";
                } else if (player.getQuest().get_step(32) == 4 && player.getInventory().checkItem(40717)) {
                    htmlid = "kamyla15";
                } else if (player.getQuest().get_step(32) == 4) {
                    htmlid = "kamyla14";
                } else if (player.getQuest().get_step(32) == 3 && player.getInventory().checkItem(40630)) {
                    htmlid = "kamyla12";
                } else if (player.getQuest().get_step(32) == 3) {
                    htmlid = "kamyla11";
                } else if (player.getQuest().get_step(32) == 2 && player.getInventory().checkItem(40644)) {
                    htmlid = "kamyla9";
                } else if (player.getQuest().get_step(32) == 1) {
                    htmlid = "kamyla8";
                } else if (player.getQuest().get_step(31) == 255 && player.getInventory().checkItem(40621)) {
                    htmlid = "kamyla1";
                }
            } else if (npcid == 71089) {
                if (player.getQuest().get_step(32) == 2) {
                    htmlid = "francu12";
                }
            } else if (npcid == 71090) {
                if (player.getQuest().get_step(33) == 1 && player.getInventory().checkItem(40620)) {
                    htmlid = "jcrystal2";
                } else if (player.getQuest().get_step(33) == 1) {
                    htmlid = "jcrystal3";
                }
            } else if (npcid == 71091) {
                if (player.getQuest().get_step(33) == 2 && player.getInventory().checkItem(40654)) {
                    htmlid = "jcrystall2";
                }
            } else if (npcid == 71074) {
                if (player.getQuest().get_step(34) == 255) {
                    htmlid = "lelder0";
                } else if (player.getQuest().get_step(34) == 3 && player.getInventory().checkItem(40634)) {
                    htmlid = "lelder12";
                } else if (player.getQuest().get_step(34) == 3) {
                    htmlid = "lelder11";
                } else if (player.getQuest().get_step(34) == 2 && player.getInventory().checkItem(40633)) {
                    htmlid = "lelder7";
                } else if (player.getQuest().get_step(34) == 2) {
                    htmlid = "lelder7b";
                } else if (player.getQuest().get_step(34) == 1) {
                    htmlid = "lelder7b";
                } else if (player.getLevel() >= 40) {
                    htmlid = "lelder1";
                }
            } else if (npcid == 71076) {
                if (player.getQuest().get_step(34) == 255) {
                    htmlid = "ylizardb";
                }
            } else if (npcid == 80079) {
                if (player.getQuest().get_step(35) == 255 && !player.getInventory().checkItem(41312)) {
                    htmlid = "keplisha6";
                } else if (player.getInventory().checkItem(41314)) {
                    htmlid = "keplisha3";
                } else if (player.getInventory().checkItem(41313)) {
                    htmlid = "keplisha2";
                } else if (player.getInventory().checkItem(41312)) {
                    htmlid = "keplisha4";
                }
            } else if (npcid == 80102) {
                if (player.getInventory().checkItem(41329)) {
                    htmlid = "fillis3";
                }
            } else if (npcid == 71167) {
                if (player.getTempCharGfx() == 3887) {
                    htmlid = "frim1";
                }
            } else if (npcid == 71141) {
                if (player.getTempCharGfx() == 3887) {
                    htmlid = "moumthree1";
                }
            } else if (npcid == 71142) {
                if (player.getTempCharGfx() == 3887) {
                    htmlid = "moumtwo1";
                }
            } else if (npcid == 71145) {
                if (player.getTempCharGfx() == 3887) {
                    htmlid = "moumone1";
                }
            } else if (npcid == 81200) {
                if (player.getInventory().checkItem(21069) || player.getInventory().checkItem(21074)) {
                    htmlid = "c_belt";
                }
            } else if (npcid == 80076) {
                if (player.getInventory().checkItem(41058)) {
                    htmlid = "voyager8";
                } else if (player.getInventory().checkItem(49082) || player.getInventory().checkItem(49083)) {
                    htmlid = (player.getInventory().checkItem(41038) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039) || player.getInventory().checkItem(41039)) ? "voyager9" : "voyager7";
                } else if (player.getInventory().checkItem(49082) || player.getInventory().checkItem(49083) || player.getInventory().checkItem(49084) || player.getInventory().checkItem(49085) || player.getInventory().checkItem(49086) || player.getInventory().checkItem(49087) || player.getInventory().checkItem(49088) || player.getInventory().checkItem(49089) || player.getInventory().checkItem(49090) || player.getInventory().checkItem(49091)) {
                    htmlid = "voyager7";
                }
            } else if (npcid == 80048) {
                int level2 = player.getLevel();
                htmlid = level2 <= 44 ? "entgate3" : (level2 < 45 || level2 > 51) ? "entgate" : "entgate2";
            } else if (npcid == 71168) {
                if (player.getInventory().checkItem(41028)) {
                    htmlid = "dantes1";
                }
            } else if (npcid == 80067) {
                if (player.getQuest().get_step(36) == 255) {
                    htmlid = "minicod10";
                } else if (player.getKarmaLevel() >= 1) {
                    htmlid = "minicod07";
                } else if (player.getQuest().get_step(36) == 1 && player.getTempCharGfx() == 6034) {
                    htmlid = "minicod03";
                } else if (player.getQuest().get_step(36) == 1 && player.getTempCharGfx() != 6034) {
                    htmlid = "minicod05";
                } else if (player.getQuest().get_step(37) == 255 || player.getInventory().checkItem(41121) || player.getInventory().checkItem(41122)) {
                    htmlid = "minicod01";
                } else if (player.getInventory().checkItem(41130) && player.getInventory().checkItem(41131)) {
                    htmlid = "minicod06";
                } else if (player.getInventory().checkItem(41130)) {
                    htmlid = "minicod02";
                }
            } else if (npcid == 81202) {
                if (player.getQuest().get_step(37) == 255) {
                    htmlid = "minitos10";
                } else if (player.getKarmaLevel() <= -1) {
                    htmlid = "minitos07";
                } else if (player.getQuest().get_step(37) == 1 && player.getTempCharGfx() == 6035) {
                    htmlid = "minitos03";
                } else if (player.getQuest().get_step(37) == 1 && player.getTempCharGfx() != 6035) {
                    htmlid = "minitos05";
                } else if (player.getQuest().get_step(36) == 255 || player.getInventory().checkItem(41130) || player.getInventory().checkItem(41131)) {
                    htmlid = "minitos01";
                } else if (player.getInventory().checkItem(41121) && player.getInventory().checkItem(41122)) {
                    htmlid = "minitos06";
                } else if (player.getInventory().checkItem(41121)) {
                    htmlid = "minitos02";
                }
            } else if (npcid == 81208) {
                if (player.getInventory().checkItem(41129) || player.getInventory().checkItem(41138)) {
                    htmlid = "minibrob04";
                } else if ((player.getInventory().checkItem(41126) && player.getInventory().checkItem(41127) && player.getInventory().checkItem(41128)) || (player.getInventory().checkItem(41135) && player.getInventory().checkItem(41136) && player.getInventory().checkItem(41137))) {
                    htmlid = "minibrob02";
                }
            } else if (npcid == 50113) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orena14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orena0";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orena2";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orena3";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orena4";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orena5";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orena6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orena7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orena8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orena9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orena10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orena11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orena12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orena13";
                }
            } else if (npcid == 50112) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenb14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenb0";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenb2";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenb3";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenb4";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenb5";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenb6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenb7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenb8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenb9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenb10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenb11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenb12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenb13";
                }
            } else if (npcid == 50111) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenc14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenc1";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenc0";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenc3";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenc4";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenc5";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenc6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenc7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenc8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenc9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenc10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenc11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenc12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenc13";
                }
            } else if (npcid == 50116) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orend14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orend3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orend1";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orend0";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orend4";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orend5";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orend6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orend7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orend8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orend9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orend10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orend11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orend12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orend13";
                }
            } else if (npcid == 50117) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orene14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orene3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orene4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orene1";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orene0";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orene5";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orene6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orene7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orene8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orene9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orene10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orene11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orene12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orene13";
                }
            } else if (npcid == 50119) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenf14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenf3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenf4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenf5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenf1";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenf0";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenf6";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenf7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenf8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenf9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenf10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenf11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenf12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenf13";
                }
            } else if (npcid == 50121) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "oreng14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "oreng3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "oreng4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "oreng5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "oreng6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "oreng1";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "oreng0";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "oreng7";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "oreng8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "oreng9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "oreng10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "oreng11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "oreng12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "oreng13";
                }
            } else if (npcid == 50114) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenh14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenh3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenh4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenh5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenh6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenh7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenh1";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenh0";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenh8";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenh9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenh10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenh11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenh12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenh13";
                }
            } else if (npcid == 50120) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "oreni14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "oreni3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "oreni4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "oreni5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "oreni6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "oreni7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "oreni8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "oreni1";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "oreni0";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "oreni9";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "oreni10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "oreni11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "oreni12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "oreni13";
                }
            } else if (npcid == 50122) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenj14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenj3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenj4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenj5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenj6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenj7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenj8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenj9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenj1";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenj0";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenj10";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenj11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenj12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenj13";
                }
            } else if (npcid == 50123) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenk14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenk3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenk4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenk5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenk6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenk7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenk8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenk9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenk10";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenk1";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenk0";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenk11";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenk12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenk13";
                }
            } else if (npcid == 50125) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenl14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenl3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenl4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenl5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenl6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenl7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenl8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenl9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenl10";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenl11";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenl1";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenl0";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenl12";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenl13";
                }
            } else if (npcid == 50124) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenm14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenm3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenm4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenm5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenm6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenm7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenm8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenm9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenm10";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenm11";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenm12";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenm1";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenm0";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenm13";
                }
            } else if (npcid == 50126) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "orenn14";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "orenn3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "orenn4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "orenn5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "orenn6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "orenn7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "orenn8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "orenn9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "orenn10";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "orenn11";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "orenn12";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "orenn13";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "orenn1";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "orenn0";
                }
            } else if (npcid == 50115) {
                if (player.getQuest().get_step(39) == 255) {
                    htmlid = "oreno0";
                } else if (player.getQuest().get_step(39) == 1) {
                    htmlid = "oreno3";
                } else if (player.getQuest().get_step(39) == 2) {
                    htmlid = "oreno4";
                } else if (player.getQuest().get_step(39) == 3) {
                    htmlid = "oreno5";
                } else if (player.getQuest().get_step(39) == 4) {
                    htmlid = "oreno6";
                } else if (player.getQuest().get_step(39) == 5) {
                    htmlid = "oreno7";
                } else if (player.getQuest().get_step(39) == 6) {
                    htmlid = "oreno8";
                } else if (player.getQuest().get_step(39) == 7) {
                    htmlid = "oreno9";
                } else if (player.getQuest().get_step(39) == 8) {
                    htmlid = "oreno10";
                } else if (player.getQuest().get_step(39) == 9) {
                    htmlid = "oreno11";
                } else if (player.getQuest().get_step(39) == 10) {
                    htmlid = "oreno12";
                } else if (player.getQuest().get_step(39) == 11) {
                    htmlid = "oreno13";
                } else if (player.getQuest().get_step(39) == 12) {
                    htmlid = "oreno14";
                } else if (player.getQuest().get_step(39) == 13) {
                    htmlid = "oreno1";
                }
            } else if (npcid == 70838) {
                if (player.isCrown() || player.isKnight() || player.isWizard() || player.isDragonKnight() || player.isIllusionist()) {
                    htmlid = "nerupam1";
                } else if (player.isDarkelf() && player.getLawful() <= -1) {
                    htmlid = "nerupaM2";
                } else if (player.isDarkelf()) {
                    htmlid = "nerupace1";
                } else if (player.isElf()) {
                    htmlid = "nerupae1";
                }
            } else if (npcid == 80099) {
                if (player.getQuest().get_step(41) == 1) {
                    htmlid = player.getInventory().checkItem(41325, serialVersionUID) ? "rarson8" : "rarson10";
                } else if (player.getQuest().get_step(41) == 2) {
                    htmlid = (!player.getInventory().checkItem(41317, serialVersionUID) || !player.getInventory().checkItem(41315, serialVersionUID)) ? "rarson19" : "rarson13";
                } else if (player.getQuest().get_step(41) == 3) {
                    htmlid = "rarson14";
                } else if (player.getQuest().get_step(41) == 4) {
                    htmlid = !player.getInventory().checkItem(41326, serialVersionUID) ? "rarson18" : player.getInventory().checkItem(41326, serialVersionUID) ? "rarson11" : "rarson17";
                } else if (player.getQuest().get_step(41) >= 5) {
                    htmlid = "rarson1";
                }
            } else if (npcid == 80101) {
                if (player.getQuest().get_step(41) != 4) {
                    htmlid = (player.getQuest().get_step(41) != 2 || !player.getInventory().checkItem(41317, serialVersionUID)) ? "kuen1" : "kuen3";
                } else if (player.getInventory().checkItem(41315, serialVersionUID) && player.getInventory().checkItem(40494, 30) && player.getInventory().checkItem(41317, serialVersionUID)) {
                    htmlid = "kuen4";
                } else if (player.getInventory().checkItem(41316, serialVersionUID)) {
                    htmlid = "kuen1";
                } else if (!player.getInventory().checkItem(41316)) {
                    player.getQuest().set_step(41, 1);
                }
            } else if (npcid == 70088 && !player.isDarkelf()) {
                htmlid = "scwaty2";
            }
            if (htmlid != null) {
                if (htmldata != null) {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
                } else {
                    player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
                }
            } else if (player.getLawful() < -1000) {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
            set_stop_time(10);
            setRest(true);
        }
    }

    private static String talkToTownadviser(L1PcInstance pc, int town_id) {
        if (pc.getHomeTownId() != town_id || !TownReading.get().isLeader(pc, town_id)) {
            return "secretary2";
        }
        return "secretary1";
    }

    private static String talkToTownmaster(L1PcInstance pc, int town_id) {
        if (pc.getHomeTownId() == town_id) {
            return "hometown";
        }
        return "othertown";
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) {
    }

    public void doFinalAction(L1PcInstance player) {
    }

    private boolean checkHasCastle(L1PcInstance player, int castle_id) {
        L1Clan clan;
        if (player.getClanid() == 0 || (clan = WorldClan.get().getClan(player.getClanname())) == null || clan.getCastleId() != castle_id) {
            return false;
        }
        return true;
    }

    private boolean checkClanLeader(L1PcInstance player) {
        L1Clan clan;
        if (!player.isCrown() || (clan = WorldClan.get().getClan(player.getClanname())) == null || player.getId() != clan.getLeaderId()) {
            return false;
        }
        return true;
    }

    private int getNecessarySealCount(L1PcInstance pc) {
        int rulerCount = 0;
        if (pc.getInventory().checkItem(40917)) {
            rulerCount = 0 + 1;
        }
        if (pc.getInventory().checkItem(40920)) {
            rulerCount++;
        }
        if (pc.getInventory().checkItem(40918)) {
            rulerCount++;
        }
        if (pc.getInventory().checkItem(40919)) {
            rulerCount++;
        }
        if (rulerCount == 0) {
            return 10;
        }
        if (rulerCount == 1) {
            return 100;
        }
        if (rulerCount == 2) {
            return 200;
        }
        if (rulerCount == 3) {
            return 500;
        }
        return 10;
    }

    private void createRuler(L1PcInstance pc, int attr, int sealCount) throws Exception {
        int rulerId = 0;
        int protectionId = 0;
        int sealId = 0;
        if (attr == 1) {
            rulerId = 40917;
            protectionId = 40909;
            sealId = 40913;
        } else if (attr == 2) {
            rulerId = 40919;
            protectionId = 40911;
            sealId = 40915;
        } else if (attr == 4) {
            rulerId = 40918;
            protectionId = 40910;
            sealId = 40914;
        } else if (attr == 8) {
            rulerId = 40920;
            protectionId = 40912;
            sealId = 40916;
        }
        pc.getInventory().consumeItem(protectionId, serialVersionUID);
        pc.getInventory().consumeItem(sealId, (long) sealCount);
        L1ItemInstance item = pc.getInventory().storeItem(rulerId, serialVersionUID);
        if (item != null) {
            pc.sendPackets(new S_ServerMessage(143, getNpcTemplate().get_name(), item.getLogName()));
        }
    }

    private String talkToRuba(L1PcInstance pc) {
        if (pc.isCrown() || pc.isWizard()) {
            return "en0101";
        }
        if (pc.isKnight() || pc.isElf() || pc.isDarkelf()) {
            return "en0102";
        }
        return "";
    }

    private String talkToSecondtbox(L1PcInstance pc) {
        if (pc.getQuest().get_step(24) != 255 || !pc.getInventory().checkItem(40701)) {
            return "maptbox0";
        }
        return "maptboxa";
    }

    private String talkToThirdtbox(L1PcInstance pc) {
        if (pc.getQuest().get_step(25) != 255 || !pc.getInventory().checkItem(40701)) {
            return "maptbox0";
        }
        return "maptboxd";
    }
}
