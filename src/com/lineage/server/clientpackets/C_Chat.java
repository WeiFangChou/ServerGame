package com.lineage.server.clientpackets;

import com.eric.gui.J_Main;
import com.lineage.config.Config;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.command.GMCommands;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CharTitle;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ChatClan;
import com.lineage.server.serverpackets.S_ChatClanUnion;
import com.lineage.server.serverpackets.S_ChatParty;
import com.lineage.server.serverpackets.S_ChatParty2;
import com.lineage.server.serverpackets.S_ChatShouting;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.Random;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1Config;
import william.Plug_Question;

public class C_Chat extends ClientBasePacket {
    private static final String _check_pwd = "abcdefghijklmnopqrstuvwxyz0123456789!_=+-?.#";
    private static final Log _log = LogFactory.getLog(C_Chat.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (decrypt.length > 108) {
                _log.warn("人物:" + pc.getName() + "對話長度超過限制:" + client.getIp().toString());
                client.set_error(client.get_error() + 1);
                return;
            }
            boolean isStop = false;
            boolean errMessage = false;
            if (pc.hasSkillEffect(64) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(161) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
                isStop = true;
                errMessage = true;
            }
            if (isStop) {
                if (errMessage) {
                    pc.sendPackets(new S_ServerMessage(242));
                }
                over();
                return;
            }
            int chatType = readC();
            String chatText = readS();
            switch (chatType) {
                case 0:
                    if (pc.is_retitle()) {
                        re_title(pc, chatText.trim());
                        over();
                        return;
                    } else if (pc.is_repass() == 0) {
                        if (ConfigOther.GUI) {
                            J_Main.getInstance().addNormalChat(pc.getName(), chatText);
                        }
                        if (ConfigOther.GM_OVERHEARD0) {
                            for (L1Object visible : World.get().getAllPlayers()) {
                                if (visible instanceof L1PcInstance) {
                                    L1PcInstance GM = (L1PcInstance) visible;
                                    if (GM.isGm() && pc.getId() != GM.getId()) {
                                        GM.sendPackets(new S_SystemMessage("【一般】" + pc.getName() + ":" + chatText));
                                    }
                                }
                            }
                        }
                        if (chatText.equals("卡點")) {
                            if (pc.hasSkillEffect(123457)) {
                                pc.sendPackets(new S_SystemMessage("請稍後在使用。"));
                                over();
                                return;
                            } else if (L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(), pc.getMapId())) {
                                pc.sendPackets(new S_SystemMessage("在攻城區內無法使用。"));
                                over();
                                return;
                            } else if (pc.isParalyzed() || pc.isSleeped()) {
                                pc.sendPackets(new S_SystemMessage("負面效果中無法使用此功能。"));
                                over();
                                return;
                            } else if (pc.getMapId() == 666) {
                                pc.sendPackets(new S_SystemMessage("笨蛋！地獄中是無法使用此指令的！"));
                                over();
                                return;
                            } else {
                                pc.setSkillEffect(123457, 10000);
                                L1Teleport.teleport(pc, pc.getX() + Random.nextInt(1) + 0, pc.getY() + Random.nextInt(1) + 0, pc.getMapId(), pc.getHeading(), false, 1);
                                pc.sendPackets(new S_SystemMessage("系統已幫您排除錯位以及卡點的狀況。"));
                            }
                        }
                        re_ai(pc, chatText);
                        chatType_0(pc, chatText);
                        break;
                    } else {
                        re_repass(pc, chatText.trim());
                        over();
                        return;
                    }
                case 2:
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addNormalChat(pc.getName(), chatText);
                    }
                    chatType_2(pc, chatText);
                    break;
                case 4:
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addClanChat(pc.getName(), chatText);
                    }
                    if (ConfigOther.GM_OVERHEARD4) {
                        for (L1Object visible2 : World.get().getAllPlayers()) {
                            if (visible2 instanceof L1PcInstance) {
                                L1PcInstance GM2 = (L1PcInstance) visible2;
                                if (GM2.isGm() && pc.getId() != GM2.getId()) {
                                    GM2.sendPackets(new S_SystemMessage("【血盟】" + pc.getName() + ":" + chatText));
                                }
                            }
                        }
                    }
                    chatType_4(pc, chatText);
                    break;
                case 11:
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addTeamChat(pc.getName(), chatText);
                    }
                    if (ConfigOther.GM_OVERHEARD11) {
                        for (L1Object visible3 : World.get().getAllPlayers()) {
                            if (visible3 instanceof L1PcInstance) {
                                L1PcInstance GM3 = (L1PcInstance) visible3;
                                if (GM3.isGm() && pc.getId() != GM3.getId()) {
                                    GM3.sendPackets(new S_SystemMessage("【隊伍】" + pc.getName() + ":" + chatText));
                                }
                            }
                        }
                    }
                    chatType_11(pc, chatText);
                    break;
                case 13:
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addChat(pc.getName(), chatText);
                    }
                    if (ConfigOther.GM_OVERHEARD13) {
                        for (L1Object visible4 : World.get().getAllPlayers()) {
                            if (visible4 instanceof L1PcInstance) {
                                L1PcInstance GM4 = (L1PcInstance) visible4;
                                if (GM4.isGm() && pc.getId() != GM4.getId()) {
                                    GM4.sendPackets(new S_SystemMessage("【聯盟】" + pc.getName() + ":" + chatText));
                                }
                            }
                        }
                    }
                    chatType_13(pc, chatText);
                    break;
                case 14:
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addTeamChat(pc.getName(), chatText);
                    }
                    if (ConfigOther.GM_OVERHEARD11) {
                        for (L1Object visible5 : World.get().getAllPlayers()) {
                            if (visible5 instanceof L1PcInstance) {
                                L1PcInstance GM5 = (L1PcInstance) visible5;
                                if (GM5.isGm() && pc.getId() != GM5.getId()) {
                                    GM5.sendPackets(new S_SystemMessage("【聊天】" + pc.getName() + ":" + chatText));
                                }
                            }
                        }
                    }
                    chatType_14(pc, chatText);
                    break;
            }
            if (!pc.isGm()) {
                pc.checkChatInterval();
            }
            over();
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void re_repass(L1PcInstance pc, String password) {
        try {
            switch (pc.is_repass()) {
                case 1:
                    if (!pc.getNetConnection().getAccount().get_password().equals(password)) {
                        pc.sendPackets(new S_ServerMessage(1744));
                        return;
                    }
                    pc.repass(2);
                    pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01", new String[]{"請輸入您的新密碼"}));
                    return;
                case 2:
                    boolean iserr = false;
                    int i = 0;
                    while (true) {
                        if (i < password.length()) {
                            if (!_check_pwd.contains(password.substring(i, i + 1).toLowerCase())) {
                                pc.sendPackets(new S_ServerMessage(1742));
                                iserr = true;
                            } else {
                                i++;
                            }
                        }
                        if (password.length() > 13) {
                            pc.sendPackets(new S_ServerMessage(166, "密碼長度過長"));
                            iserr = true;
                        }
                        if (password.length() < 3) {
                            pc.sendPackets(new S_ServerMessage(166, "密碼長度過長"));
                            iserr = true;
                        }
                        if (!iserr) {
                            pc.setText(password);
                            pc.repass(3);
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01", new String[]{"請確認您的新密碼"}));
                            return;
                        }
                        return;
                    }

                case 3:
                    if (!pc.getText().equals(password)) {
                        pc.sendPackets(new S_ServerMessage(1982));
                        return;
                    }
                    pc.sendPackets(new S_CloseList(pc.getId()));
                    pc.sendPackets(new S_ServerMessage(1985));
                    AccountReading.get().updatePwd(pc.getAccountName(), password);
                    pc.setText(null);
                    pc.repass(0);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            pc.sendPackets(new S_CloseList(pc.getId()));
            pc.sendPackets(new S_ServerMessage(45));
            pc.setText(null);
            pc.repass(0);
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void re_title(L1PcInstance pc, String chatText) {
        try {
            String newchatText = chatText.trim();
            if (newchatText.isEmpty() || newchatText.length() <= 0) {
                pc.sendPackets(new S_ServerMessage("\\fU請輸入封號內容"));
                return;
            }
            if (newchatText.getBytes().length > (Config.LOGINS_TO_AUTOENTICATION ? 18 : 13)) {
                pc.sendPackets(new S_ServerMessage("\\fU封號長度過長"));
                return;
            }
            StringBuilder title = new StringBuilder();
            title.append(newchatText);
            pc.setTitle(title.toString());
            pc.sendPacketsAll(new S_CharTitle(pc.getId(), title));
            pc.save();
            pc.retitle(false);
            pc.sendPackets(new S_ServerMessage("\\fU封號變更完成"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void re_ai(L1PcInstance pc, String chatText) {
        if (chatText.startsWith(Plug_Question.getInstance().getTemplate(pc.getAi_Number()).getAnswers()) && pc.hasSkillEffect(L1SkillId.AI_2)) {
            pc.sendPackets(new S_ServerMessage("你可以繼續遊戲了。"));
            WriteLogTxt.Recording("AI_測試通過紀錄", "帳號：〈" + pc.getAccountName() + "〉IP：〈" + ((Object) pc.getNetConnection().getIp()) + "〉玩家：〈" + pc.getName() + "〉AI測試通過。");
            pc.killSkillEffectTimer(L1SkillId.AI_2);
            pc.setSkillEffect(L1SkillId.AI_1, (L1Config._2227 + Random.nextInt(10) + 1) * 60 * L1SkillId.STATUS_BRAVE);
        } else if (pc.hasSkillEffect(L1SkillId.AI_2)) {
            pc.killSkillEffectTimer(L1SkillId.AI_2);
            WriteLogTxt.Recording("AI_外掛斷線紀錄", "帳號：〈" + pc.getAccountName() + "〉IP：〈" + ((Object) pc.getNetConnection().getIp()) + "〉玩家：〈" + pc.getName() + "〉AI測試斷線。");
            pc.sendPackets(new S_Disconnect());
        }
    }

    private void chatType_14(L1PcInstance pc, String chatText) {
        if (pc.isInChatParty()) {
            S_ChatParty2 chatpacket = new S_ChatParty2(pc, chatText);
            L1PcInstance[] partyMembers = pc.getChatParty().getMembers();
            for (L1PcInstance listner : partyMembers) {
                if (!listner.getExcludingList().contains(pc.getName())) {
                    listner.sendPackets(chatpacket);
                }
            }
            if (ConfigRecord.LOGGING_CHAT_CHAT_PARTY) {
                LogChatReading.get().noTarget(pc, chatText, 14);
            }
        }
    }

    private void chatType_13(L1PcInstance pc, String chatText) {
        L1Clan clan;
        if (pc.getClanid() != 0 && (clan = WorldClan.get().getClan(pc.getClanname())) != null) {
            switch (pc.getClanRank()) {
                case 3:
                case 4:
                case 6:
                case 9:
                case 10:
                    S_ChatClanUnion chatpacket = new S_ChatClanUnion(pc, chatText);
                    L1PcInstance[] clanMembers = clan.getOnlineClanMember();
                    for (L1PcInstance listner : clanMembers) {
                        if (!listner.getExcludingList().contains(pc.getName())) {
                            switch (listner.getClanRank()) {
                                case 3:
                                case 4:
                                case 6:
                                case 9:
                                case 10:
                                    listner.sendPackets(chatpacket);
                                    continue;
                            }
                        }
                    }
                    if (ConfigRecord.LOGGING_CHAT_COMBINED) {
                        LogChatReading.get().noTarget(pc, chatText, 13);
                        return;
                    }
                    return;
                case 5:
                case 7:
                case 8:
                default:
                    return;
            }
        }
    }

    private void chatType_11(L1PcInstance pc, String chatText) {
        if (pc.isInParty()) {
            S_ChatParty chatpacket = new S_ChatParty(pc, chatText);
            ConcurrentHashMap<Integer, L1PcInstance> pcs = pc.getParty().partyUsers();
            if (!pcs.isEmpty() && pcs.size() > 0) {
                for (L1PcInstance listner : pcs.values()) {
                    if (!listner.getExcludingList().contains(pc.getName())) {
                        listner.sendPackets(chatpacket);
                    }
                }
                if (ConfigRecord.LOGGING_CHAT_PARTY) {
                    LogChatReading.get().noTarget(pc, chatText, 11);
                }
            }
        }
    }

    private void chatType_4(L1PcInstance pc, String chatText) {
        L1Clan clan;
        if (!(pc.getClanid() == 0 || (clan = WorldClan.get().getClan(pc.getClanname())) == null)) {
            S_ChatClan chatpacket = new S_ChatClan(pc, chatText);
            L1PcInstance[] clanMembers = clan.getOnlineClanMember();
            for (L1PcInstance listner : clanMembers) {
                if (!listner.getExcludingList().contains(pc.getName())) {
                    listner.sendPackets(chatpacket);
                }
            }
            if (ConfigRecord.LOGGING_CHAT_CLAN) {
                LogChatReading.get().noTarget(pc, chatText, 4);
            }
        }
    }

    private void chatType_2(L1PcInstance pc, String chatText) {
        if (!pc.isGhost()) {
            S_ChatShouting chatpacket = new S_ChatShouting(pc, chatText);
            pc.sendPackets(chatpacket);
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(pc, 50).iterator();
            while (it.hasNext()) {
                L1PcInstance listner = it.next();
                if (!listner.getExcludingList().contains(pc.getName()) && pc.get_showId() == listner.get_showId()) {
                    listner.sendPackets(chatpacket);
                }
            }
            if (ConfigRecord.LOGGING_CHAT_SHOUT) {
                LogChatReading.get().noTarget(pc, chatText, 2);
            }
            doppelShouting(pc, chatText);
        }
    }

    private void chatType_0(L1PcInstance pc, String chatText) {
        if (pc.isGhost() && !pc.isGm() && !pc.isMonitor()) {
            return;
        }
        if (pc.getAccessLevel() <= 0 || !chatText.startsWith(".")) {
            S_Chat chatpacket = new S_Chat(pc, chatText);
            pc.sendPackets(chatpacket);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(pc).iterator();
            while (it.hasNext()) {
                L1PcInstance listner = it.next();
                if (!listner.getExcludingList().contains(pc.getName()) && pc.get_showId() == listner.get_showId()) {
                    listner.sendPackets(chatpacket);
                }
            }
            if (ConfigRecord.LOGGING_CHAT_NORMAL) {
                LogChatReading.get().noTarget(pc, chatText, 0);
            }
            doppelGenerally(pc, chatText);
            return;
        }
        GMCommands.getInstance().handleCommands(pc, chatText.substring(1));
    }

    private void doppelGenerally(L1PcInstance pc, String chatText) {
        for (L1Object obj : pc.getKnownObjects()) {
            if (obj instanceof L1MonsterInstance) {
                L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
                    mob.broadcastPacketX8(new S_NpcChat(mob, chatText));
                }
            }
        }
    }

    private void doppelShouting(L1PcInstance pc, String chatText) {
        for (L1Object obj : pc.getKnownObjects()) {
            if (obj instanceof L1MonsterInstance) {
                L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
                    mob.broadcastPacketX8(new S_NpcChatShouting(mob, chatText));
                }
            }
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
