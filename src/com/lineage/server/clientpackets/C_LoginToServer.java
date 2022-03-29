package com.lineage.server.clientpackets;

import com.eric.gui.J_Main;
import com.lineage.config.ConfigOther;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.GetBackRestartTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharBookConfigReading;
import com.lineage.server.datatables.lock.CharBookReading;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.lock.CharacterConfigReading;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1ClanMatching;
import com.lineage.server.model.L1War;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_AddSkill;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_BookMarkList;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_CharResetInfo;
import com.lineage.server.serverpackets.S_Emblem;
import com.lineage.server.serverpackets.S_EnterGame;
import com.lineage.server.serverpackets.S_InitialAbilityGrowth;
import com.lineage.server.serverpackets.S_InvList;
import com.lineage.server.serverpackets.S_Karma;
import com.lineage.server.serverpackets.S_MapID;
import com.lineage.server.serverpackets.S_NewMaster;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharPack;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxConfig;
import com.lineage.server.serverpackets.S_PacketBoxIcon1;
import com.lineage.server.serverpackets.S_PacketBoxProtection;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.serverpackets.S_Weather;
import com.lineage.server.templates.L1BookConfig;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.templates.L1Config;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.templates.L1GetBackRestart;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1UserSkillTmp;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldSummons;
import com.lineage.server.world.WorldWar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.Poly.CardBookCmd;

public class C_LoginToServer extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_LoginToServer.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            String loginName = client.getAccountName();
            if (client.getActiveChar() != null) {
                _log.error("帳號重複登入人物: " + loginName + "強制中斷連線");
                client.kick();
                return;
            }
            String charName = readS();
            L1PcInstance pc = L1PcInstance.load(charName);
            if (pc == null || !loginName.equals(pc.getAccountName())) {
                _log.info("無效登入要求: " + charName + " 帳號(" + loginName + ", " + ((Object) client.getIp()) + ")");
                client.kick();
                over();
                return;
            }
            if (ConfigOther.GUI) {
                J_Main.getInstance().addPlayerTable(loginName, charName, client.getIp());
            }
            _log.info("登入遊戲: " + charName + " 帳號(" + loginName + ", " + ((Object) client.getIp()) + ")");
            pc.setNetConnection(client);
            pc.setPacketOutput(client.out());
            int currentHpAtLoad = pc.getCurrentHp();
            int currentMpAtLoad = pc.getCurrentMp();
            client.set_error(0);
            pc.clearSkillMastery();
            pc.setOnlineStatus(1);
            CharacterTable.updateOnlineStatus(pc);
            World.get().storeObject(pc);
            pc.setNetConnection(client);
            pc.setPacketOutput(client.out());
            client.setActiveChar(pc);
            getOther(pc);
            pc.sendPackets(new S_EnterGame());
            pc.sendPackets(new S_InitialAbilityGrowth(pc));
            items(pc);
            bookmarks(pc);
            backRestart(pc);
            getFocus(pc);
            pc.sendVisualEffectAtLogin();
            skills(pc);
            for (L1ItemInstance item : pc.getInventory().getItems()) {
                if (item.isEquipped()) {
                    pc.getInventory().toSlotPacket(pc, item);
                }
            }
            pc.turnOnOffLight();
            ClanMatching(pc);
            if (pc.getCurrentHp() > 0) {
                pc.setDead(false);
                pc.setStatus(0);
            } else {
                pc.setDead(true);
                pc.setStatus(8);
            }
            L1Config config = CharacterConfigReading.get().get(pc.getId());
            if (config != null) {
                pc.sendPackets(new S_PacketBoxConfig(config));
            }
            serchSummon(pc);
            ServerWarExecutor.get().checkCastleWar(pc);
            if (william.L1Config._2226 && !pc.hasSkillEffect(L1SkillId.AI_1) && !pc.isGm()) {
                pc.setSkillEffect(L1SkillId.AI_1, 1800000);
            }
            war(pc);
            marriage(pc);
            if (currentHpAtLoad > pc.getCurrentHp()) {
                pc.setCurrentHp(currentHpAtLoad);
            }
            if (currentMpAtLoad > pc.getCurrentMp()) {
                pc.setCurrentMp(currentMpAtLoad);
            }
            buff(pc);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
            pc.startObjectAutoUpdate();
            crown(pc);
            pc.save();
            if (pc.getHellTime() > 0) {
                pc.beginHell(false);
            }
            pc.sendPackets(new S_CharResetInfo(pc));
            pc.getQuest().load();
            if (pc.get_food() >= 225) {
                pc.set_h_time(Calendar.getInstance().getTimeInMillis() / 1000);
            }
            if (pc.getLevel() <= 20) {
                pc.sendPackets(new S_PacketBoxProtection(6, 1));
            }
            CardBookCmd.CardSetTable(pc);
            over();
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void crown(L1PcInstance pc) {
        try {
            Map<Integer, L1Clan> map = L1CastleLocation.mapCastle();
            for (Integer key : map.keySet()) {
                L1Clan clan = map.get(key);
                if (clan != null) {
                    if (key.equals(2)) {
                        pc.sendPackets(new S_CastleMaster(8, clan.getLeaderId()));
                    } else {
                        pc.sendPackets(new S_CastleMaster(key.intValue(), clan.getLeaderId()));
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void getFocus(L1PcInstance pc) {
        try {
            pc.set_showId(-1);
            World.get().addVisibleObject(pc);
            pc.sendPackets(new S_OwnCharStatus(pc));
            pc.sendPackets(new S_MapID(pc, pc.getMapId(), pc.getMap().isUnderwater()));
            pc.sendPackets(new S_OwnCharPack(pc));
            ArrayList<L1PcInstance> otherPc = World.get().getVisiblePlayer(pc);
            if (otherPc.size() > 0) {
                Iterator<L1PcInstance> it = otherPc.iterator();
                while (it.hasNext()) {
                    it.next().sendPackets(new S_OtherCharPacks(pc));
                }
            }
            if (pc.power()) {
                pc.sendPackets(new S_Bonusstats(pc.getId()));
            }
            pc.sendPackets(new S_SPMR(pc));
            pc.sendPackets(new S_PacketBoxIcon1(true, pc.get_dodge()));
            pc.sendPackets(new S_Karma(pc));
            pc.sendPackets(new S_Weather(World.get().getWeather()));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void marriage(L1PcInstance pc) {
        L1PcInstance partner;
        try {
            if (pc.getPartnerId() != 0 && (partner = (L1PcInstance) World.get().findObject(pc.getPartnerId())) != null && partner.getPartnerId() != 0 && pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
                pc.sendPackets(new S_ServerMessage(548));
                partner.sendPackets(new S_ServerMessage(549));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void getOther(L1PcInstance pc) throws Exception {
        try {
            pc.set_otherList(new L1PcOtherList(pc));
            pc.addMaxHp(pc.get_other().get_addhp());
            pc.addMaxMp(pc.get_other().get_addmp());
            OnlineGiftSet.add(pc);
            int time = pc.get_other().get_usemapTime();
            if (time > 0) {
                ServerUseMapTimer.put(pc, time);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void war(L1PcInstance pc) {
        try {
            if (pc.getClanid() != 0) {
                L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (clan == null) {
                    pc.setClanid(0);
                    pc.setClanname("");
                    pc.setClanRank(0);
                    pc.save();
                } else if (pc.getClanid() == clan.getClanId() && pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
                    L1PcInstance[] clanMembers = clan.getOnlineClanMember();
                    for (L1PcInstance clanMember : clanMembers) {
                        if (clanMember.getId() != pc.getId()) {
                            clanMember.sendPackets(new S_ServerMessage(843, pc.getName()));
                        }
                    }
                    clan.CheckClan_Exp20(null);
                    L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clan.getClanId());
                    if (emblemIcon != null) {
                        pc.sendPackets(new S_Emblem(emblemIcon));
                    }
                    for (L1War war : WorldWar.get().getWarList()) {
                        if (war.checkClanInWar(pc.getClanname())) {
                            String enemy_clan_name = war.getEnemyClanName(pc.getClanname());
                            if (enemy_clan_name != null) {
                                pc.sendPackets(new S_War(8, pc.getClanname(), enemy_clan_name));
                                return;
                            }
                            return;
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void backRestart(L1PcInstance pc) {
        try {
            L1GetBackRestart gbr = GetBackRestartTable.get().getGetBackRestart(pc.getMapId());
            if (gbr != null) {
                pc.setX(gbr.getLocX());
                pc.setY(gbr.getLocY());
                pc.setMap(gbr.getMapId());
            }
            int castle_id = L1CastleLocation.getCastleIdByArea(pc);
            if (castle_id > 0 && ServerWarExecutor.get().isNowWar(castle_id)) {
                L1Clan clan = WorldClan.get().getClan(pc.getClanname());
                if (clan == null) {
                    int[] iArr = new int[3];
                    int[] loc = L1CastleLocation.getGetBackLoc(castle_id);
                    pc.setX(loc[0]);
                    pc.setY(loc[1]);
                    pc.setMap( loc[2]);
                } else if (clan.getCastleId() != castle_id) {
                    int[] iArr2 = new int[3];
                    int[] loc2 = L1CastleLocation.getGetBackLoc(castle_id);
                    pc.setX(loc2[0]);
                    pc.setY(loc2[1]);
                    pc.setMap( loc2[2]);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void items(L1PcInstance pc) {
        try {
            CharacterTable.restoreInventory(pc);
            if (pc.getInventory().getItems().size() > 0) {
                pc.sendPackets(new S_InvList(pc.getInventory().getItems()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void bookmarks(L1PcInstance pc) {
        try {
            L1BookConfig bookconfig = CharBookConfigReading.get().get(pc.getId());
            if (bookconfig != null) {
                pc.sendPackets(new S_BookMarkList(pc, bookconfig));
                return;
            }
            ArrayList<L1BookMark> bookList = CharBookReading.get().getBookMarks(pc);
            if (bookList == null || bookList.size() <= 0) {
                pc.sendPackets(new S_BookMarkList());
            } else {
                pc.sendPackets(new S_BookMarkList(bookList));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void skills(L1PcInstance pc) {
        try {
            ArrayList<L1UserSkillTmp> skillList = CharSkillReading.get().skills(pc.getId());
            int[] skills = new int[28];
            if (skillList != null && skillList.size() > 0) {
                Iterator<L1UserSkillTmp> it = skillList.iterator();
                while (it.hasNext()) {
                    L1Skills skill = SkillsTable.get().getTemplate(it.next().get_skill_id());
                    int skillLevel = skill.getSkillLevel() - 1;
                    skills[skillLevel] = skills[skillLevel] + skill.getId();
                }
                pc.sendPackets(new S_AddSkill(pc, skills));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void ClanMatching(L1PcInstance pc) {
        L1ClanMatching cml = L1ClanMatching.getInstance();
        if (pc.getClanid() != 0) {
            switch (pc.getClanRank()) {
                case 3:
                case 4:
                case 6:
                case 9:
                case 10:
                    cml.loadClanMatchingApcList_Crown(pc);
                    if (!pc.getInviteList().isEmpty()) {
                        pc.sendPackets(new S_ServerMessage(3246));
                        return;
                    }
                    return;
                case 5:
                case 7:
                case 8:
                default:
                    return;
            }
        } else if (!pc.isCrown()) {
            cml.loadClanMatchingApcList_User(pc);
            if (!cml.getMatchingList().isEmpty()) {
                pc.sendPackets(new S_ServerMessage(3245));
            }
        } else {
            pc.sendPackets(new S_ServerMessage(3247));
        }
    }

    private void serchSummon(L1PcInstance pc) {
        try {
            Collection<L1SummonInstance> summons = WorldSummons.get().all();
            if (summons.size() > 0) {
                for (L1SummonInstance summon : summons) {
                    if (summon.getMaster().getId() == pc.getId()) {
                        summon.setMaster(pc);
                        pc.addPet(summon);
                        S_NewMaster packet = new S_NewMaster(pc.getName(), summon);
                        Iterator<L1PcInstance> it = World.get().getVisiblePlayer(summon).iterator();
                        while (it.hasNext()) {
                            it.next().sendPackets(packet);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void buff(L1PcInstance pc) {
        try {
            CharBuffReading.get().buff(pc);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
