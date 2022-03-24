package com.lineage.server.model;

import com.lineage.config.ConfigOther;
import com.lineage.data.QuestClass;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.QuestTable;
import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.datatables.sql.SpawnBossTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.model.skill.skillmode.SUMMON_MONSTER;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_GreenMessage;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_PC_Xljnet;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShopSellListCnX;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.templates.L1Quest;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ActionPc {
    private static final Log _log = LogFactory.getLog(L1ActionPc.class);
    private static final int[] skillIds = {26, 42, 43, 48, 151, L1SkillId.NATURES_TOUCH, 148, 115, 117};
    private final L1PcInstance _pc;

    public L1ActionPc(L1PcInstance pc) {
        this._pc = pc;
    }

    public L1PcInstance get_pc() {
        return this._pc;
    }

    public void action(String cmd, long amount) {
        int war_type;
        try {
            if (cmd.matches("[0-9]+")) {
                this._pc.get_other().set_gmHtml(null);
                if (this._pc.isSummonMonster()) {
                    summonMonster(this._pc, cmd);
                    this._pc.setShapeChange(false);
                    this._pc.setSummonMonster(false);
                }
            } else if (this._pc.isShapeChange()) {
                this._pc.get_other().set_gmHtml(null);
                int awakeSkillId = this._pc.getAwakeSkillId();
                if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
                    this._pc.sendPackets(new S_ServerMessage(1384));
                    return;
                }
                L1PolyMorph.handleCommands(this._pc, cmd);
                this._pc.setShapeChange(false);
                this._pc.setSummonMonster(false);
            } else if (this._pc.get_other().get_gmHtml() != null) {
                this._pc.get_other().get_gmHtml().action(cmd);
            } else {
                this._pc.get_other().set_gmHtml(null);
                if (cmd.equalsIgnoreCase("power")) {
                    if (this._pc.power()) {
                        this._pc.sendPackets(new S_Bonusstats(this._pc.getId()));
                    }
                } else if (cmd.equalsIgnoreCase("shop")) {
                    this._pc.sendPackets(new S_ShopSellListCnX(this._pc, this._pc.getId()));
                } else if (cmd.equalsIgnoreCase("index")) {
                    this._pc.isWindows();
                } else if (cmd.equalsIgnoreCase("locerr1")) {
                    this._pc.set_unfreezingTime(10);
                } else if (cmd.equalsIgnoreCase("locerr2")) {
                    if (this._pc.hasSkillEffect(123457)) {
                        this._pc.sendPackets(new S_SystemMessage("請稍後在使用。"));
                    } else if (this._pc.isParalyzed() || this._pc.isSleeped()) {
                        this._pc.sendPackets(new S_SystemMessage("負面效果中無法使用此功能。"));
                    } else if (L1CastleLocation.checkInAllWarArea(this._pc.getX(), this._pc.getY(), this._pc.getMapId())) {
                        this._pc.sendPackets(new S_SystemMessage("在攻城區內無法使用。"));
                    } else if (this._pc.getMapId() == 666) {
                        this._pc.sendPackets(new S_SystemMessage("笨蛋！地獄中是無法使用此指令的！"));
                    } else {
                        this._pc.setSkillEffect(123457, L1SkillId.SEXP11);
                        L1Teleport.teleport(this._pc, this._pc.getX(), this._pc.getY(), this._pc.getMapId(), this._pc.getHeading(), false);
                        this._pc.sendPackets(new S_SystemMessage("系統已幫您排除錯位以及卡點的狀況。"));
                    }
                } else if (cmd.equalsIgnoreCase("attack_view_on")) {
                    this._pc.set_send_weapon_dmg_gfxid(true);
                    this._pc.sendPackets(new S_SystemMessage("顯示傷害開啟。"));
                } else if (cmd.equalsIgnoreCase("attack_view_off")) {
                    this._pc.set_send_weapon_dmg_gfxid(false);
                    this._pc.sendPackets(new S_SystemMessage("顯示傷害關閉。"));
                } else if (cmd.equalsIgnoreCase("party_view_on")) {
                    if (!this._pc.isInParty()) {
                        this._pc.sendPackets(new S_ServerMessage(425));
                        return;
                    }
                    this._pc.isShowDrop(0);
                    this._pc.sendPackets(new S_SystemMessage("隊伍掉落訊息開啟。"));
                } else if (cmd.equalsIgnoreCase("party_view_off")) {
                    if (!this._pc.isInParty()) {
                        this._pc.sendPackets(new S_ServerMessage(425));
                        return;
                    }
                    this._pc.isShowDrop(1);
                    this._pc.sendPackets(new S_SystemMessage("隊伍掉落訊息關閉。"));
                } else if (cmd.equalsIgnoreCase("bosschaxun")) {
                    String[] htmldata = new String[L1SkillId.AQUA_PROTECTER];
                    int i = 0;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                        for (L1Spawn spawn : SpawnBossTable.get_bossSpawnTable().values()) {
                            if (i > 98) {
                                break;
                            } else if (spawn.getMessage() == 1) {
                                if (Calendar.getInstance().after(spawn.get_nextSpawnTime())) {
                                    htmldata[i] = String.valueOf(spawn.getName()) + "\n 已刷新";
                                } else {
                                    htmldata[i] = String.valueOf(spawn.getName()) + "\n" + sdf.format(spawn.get_nextSpawnTime().getTime());
                                }
                                i++;
                            }
                        }
                        this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "serverboss", htmldata));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (cmd.equalsIgnoreCase("clanparty")) {
                    if (this._pc.getClanid() == 0) {
                        this._pc.sendPackets(new S_SystemMessage("你還沒有血盟"));
                        return;
                    }
                    if (this._pc.isInParty()) {
                        if (!this._pc.getParty().isLeader(this._pc)) {
                            this._pc.sendPackets(new S_SystemMessage("你不是隊長"));
                            return;
                        } else if (!this._pc.getParty().isVacancy()) {
                            this._pc.sendPackets(new S_SystemMessage("你的隊伍已經滿員"));
                            return;
                        }
                    }
                    Iterator<L1PcInstance> it = World.get().getVisiblePlayer(this._pc).iterator();
                    while (it.hasNext()) {
                        L1PcInstance tagpc = it.next();
                        if (tagpc.getClanid() == this._pc.getClanid() && !tagpc.isInParty()) {
                            tagpc.setPartyID(this._pc.getId());
                            tagpc.sendPackets(new S_Message_YN(953, this._pc.getName()));
                        }
                    }
                    this._pc.sendPackets(new S_SystemMessage("已成功向周圍血盟成員發送組隊邀請!!"));
                } else if (cmd.equalsIgnoreCase("addskill")) {
                    if (this._pc.getInventory().checkItem(ConfigOther.skillId_ITEM, (long) ConfigOther.skillId_ITEM_COUNT)) {
                        this._pc.getInventory().consumeItem(ConfigOther.skillId_ITEM, (long) ConfigOther.skillId_ITEM_COUNT);
                        for (L1PcInstance targetpc : World.get().getAllPlayers()) {
                            if (!targetpc.isPrivateShop() && targetpc.getNetConnection() != null) {
                                int[] iArr = skillIds;
                                for (int skillId : iArr) {
                                    if (skillId == 148 && targetpc.isElf()) {
                                        skillId = 149;
                                    }
                                    new L1SkillUse().handleCommands(targetpc, skillId, targetpc.getId(), targetpc.getX(), targetpc.getY(), 1800, 4);
                                }
                            }
                        }
                        StringBuilder msg = new StringBuilder();
                        msg.append("\\f=土豪玩家【\\f2");
                        msg.append(this._pc.getName());
                        msg.append("\\f=】為全服在線玩家加狀態");
                        World.get().broadcastPacketToAll(new S_GreenMessage(msg.toString()));
                        return;
                    }
                    this._pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(ConfigOther.skillId_ITEM).getName()) + "(" + ((long) ConfigOther.skillId_ITEM_COUNT) + ")"));
                } else if (cmd.equalsIgnoreCase("addchanskill")) {
                    if (this._pc.getClanid() == 0 || this._pc.getClan() == null) {
                        this._pc.sendPackets(new S_SystemMessage("你還沒有加入血盟"));
                    } else if (this._pc.getInventory().checkItem(ConfigOther.chanskill_ITEM, (long) ConfigOther.chanskill_ITEM_COUNT)) {
                        this._pc.getInventory().consumeItem(ConfigOther.chanskill_ITEM, (long) ConfigOther.chanskill_ITEM_COUNT);
                        L1PcInstance[] onlineClanMember = this._pc.getClan().getOnlineClanMember();
                        for (L1PcInstance targetchanpc : onlineClanMember) {
                            if (!targetchanpc.isPrivateShop() && targetchanpc.getNetConnection() != null) {
                                int[] iArr2 = skillIds;
                                for (int skillId2 : iArr2) {
                                    if (skillId2 == 148 && targetchanpc.isElf()) {
                                        skillId2 = 149;
                                    }
                                    new L1SkillUse().handleCommands(targetchanpc, skillId2, targetchanpc.getId(), targetchanpc.getX(), targetchanpc.getY(), 1800, 4);
                                }
                            }
                        }
                        StringBuilder msg2 = new StringBuilder();
                        msg2.append("\\f=【\\f2");
                        msg2.append(this._pc.getClanname());
                        msg2.append("\\f=】血盟的土豪<\\f2");
                        msg2.append(this._pc.getName());
                        msg2.append("\\f=>為其在線成員加狀態");
                        World.get().broadcastPacketToAll(new S_GreenMessage(msg2.toString()));
                    } else {
                        this._pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(ConfigOther.chanskill_ITEM).getName()) + "(" + ((long) ConfigOther.chanskill_ITEM_COUNT) + ")"));
                    }
                } else if (cmd.equalsIgnoreCase("pcskill")) {
                    if (this._pc.getInventory().checkItem(ConfigOther.pcskill_ITEM, (long) ConfigOther.pcskill_ITEM_COUNT)) {
                        this._pc.getInventory().consumeItem(ConfigOther.pcskill_ITEM, (long) ConfigOther.pcskill_ITEM_COUNT);
                        if (!(this._pc.isPrivateShop() || this._pc.getNetConnection() == null)) {
                            int[] iArr3 = skillIds;
                            for (int skillId3 : iArr3) {
                                if (skillId3 == 148 && this._pc.isElf()) {
                                    skillId3 = 149;
                                }
                                new L1SkillUse().handleCommands(this._pc, skillId3, this._pc.getId(), this._pc.getX(), this._pc.getY(), 1800, 4);
                            }
                            return;
                        }
                        return;
                    }
                    this._pc.sendPackets(new S_ServerMessage(337, String.valueOf(ItemTable.get().getTemplate(ConfigOther.pcskill_ITEM).getName()) + "(" + ((long) ConfigOther.pcskill_ITEM_COUNT) + ")"));
                } else if (cmd.equalsIgnoreCase("noparty")) {
                    if (this._pc.isInParty()) {
                        if (!this._pc.getParty().isLeader(this._pc)) {
                            this._pc.sendPackets(new S_SystemMessage("你不是隊長。"));
                            return;
                        } else if (!this._pc.getParty().isVacancy()) {
                            this._pc.sendPackets(new S_SystemMessage("你的隊伍已經滿員。"));
                            return;
                        }
                    }
                    Iterator<L1PcInstance> it2 = World.get().getVisiblePlayer(this._pc).iterator();
                    while (it2.hasNext()) {
                        L1PcInstance tagpc2 = it2.next();
                        if (!tagpc2.isInParty()) {
                            tagpc2.setPartyID(this._pc.getId());
                            tagpc2.sendPackets(new S_Message_YN(953, this._pc.getName()));
                        }
                    }
                    this._pc.sendPackets(new S_SystemMessage("已成功向周圍發送組隊邀請!!"));
                } else if (cmd.equalsIgnoreCase("mazuskill")) {
                    if (this._pc.hasSkillEffect(L1SkillId.MAZU_SKILL)) {
                        this._pc.sendPackets(new S_ServerMessage("媽祖祝福效果時間尚有" + this._pc.getSkillEffectTimeSec(L1SkillId.MAZU_SKILL) + "秒。"));
                    }
                } else if (cmd.equalsIgnoreCase("exp_time")) {
                    L1BuffUtil.cancelExpSkill(this._pc);
                } else if (cmd.equalsIgnoreCase("showemblem")) {
                    for (int castle_id = 1; castle_id < 8; castle_id++) {
                        if (this._pc.isCrown() && ServerWarExecutor.get().isNowWar(castle_id)) {
                            this._pc.sendPackets(new S_SystemMessage("攻城戰期間無法使用顯示盟徽。"));
                            return;
                        }
                    }
                    if (this._pc.isShowEmblem()) {
                        war_type = 3;
                        this._pc.setShowEmblem(false);
                    } else {
                        war_type = 1;
                        this._pc.setShowEmblem(true);
                    }
                    if (this._pc.getClanid() != 0) {
                        if (new File("emblem/" + String.valueOf(this._pc.getClanid())).exists()) {
                            this._pc.sendPackets(new S_War(war_type, this._pc.getClanname(), ""));
                        }
                    }
                    Collection<L1Clan> clans = WorldClan.get().getAllClans();
                    if (!(clans == null || clans.isEmpty())) {
                        for (L1Clan clan : clans) {
                            if (clan.getClanId() != this._pc.getClanid()) {
                                if (new File("emblem/" + String.valueOf(clan.getClanId())).exists()) {
                                    this._pc.sendPackets(new S_War(war_type, this._pc.getClanname(), clan.getClanName()));
                                }
                            }
                        }
                    }
                } else if (cmd.equalsIgnoreCase("loadweaponqiehuan")) {
                    loadWeaponQieHuan();
                } else if (cmd.startsWith("weapon_index")) {
                    selWeaponQieHuan(cmd);
                } else if (cmd.equalsIgnoreCase("eq_chakan")) {
                    if (this._pc.get_tuokui_objId() != 0) {
                        L1Object target = World.get().findObject(this._pc.get_tuokui_objId());
                        this._pc.set_tuokui_objId(0);
                        if (target == null) {
                            this._pc.sendPackets(new S_SystemMessage("玩家已經不在線上。"));
                        } else if (target instanceof L1PcInstance) {
                            L1PcInstance tagpc3 = (L1PcInstance) target;
                            List<L1ItemInstance> items = tagpc3.getInventory().getItems();
                            List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<>();
                            for (L1ItemInstance item1 : items) {
                                if (item1.isEquipped()) {
                                    itemsx.add(item1);
                                }
                            }
                            this._pc.sendPackets(new S_PC_Xljnet(tagpc3, 0, itemsx));
                        }
                    }
                } else if (cmd.equalsIgnoreCase("qt")) {
                    showStartQuest(this._pc, this._pc.getId());
                } else if (cmd.equalsIgnoreCase("quest")) {
                    showQuest(this._pc, this._pc.getId());
                } else if (cmd.equalsIgnoreCase("questa")) {
                    showQuestAll(this._pc, this._pc.getId());
                } else if (cmd.equalsIgnoreCase("i")) {
                    L1Quest quest = QuestTable.get().getTemplate(this._pc.getTempID());
                    this._pc.setTempID(0);
                    if (quest != null) {
                        QuestClass.get().showQuest(this._pc, quest.get_id());
                    }
                } else if (cmd.equalsIgnoreCase("d")) {
                    L1Quest quest2 = QuestTable.get().getTemplate(this._pc.getTempID());
                    this._pc.setTempID(0);
                    if (quest2 == null) {
                        return;
                    }
                    if (this._pc.getQuest().isEnd(quest2.get_id())) {
                        questDel(quest2);
                    } else if (!this._pc.getQuest().isStart(quest2.get_id())) {
                        this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_q_not6"));
                    } else {
                        questDel(quest2);
                    }
                } else if (cmd.equalsIgnoreCase("dy")) {
                    L1Quest quest3 = QuestTable.get().getTemplate(this._pc.getTempID());
                    this._pc.setTempID(0);
                    if (quest3 == null) {
                        return;
                    }
                    if (this._pc.getQuest().isEnd(quest3.get_id())) {
                        isDel(quest3);
                    } else if (!this._pc.getQuest().isStart(quest3.get_id())) {
                        this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_q_not6"));
                    } else {
                        isDel(quest3);
                    }
                } else if (cmd.equalsIgnoreCase("up")) {
                    new L1ActionShowHtml(this._pc).showQuestMap(this._pc.get_other().get_page() - 1);
                } else if (cmd.equalsIgnoreCase("dn")) {
                    new L1ActionShowHtml(this._pc).showQuestMap(this._pc.get_other().get_page() + 1);
                } else if (cmd.equalsIgnoreCase("q0")) {
                    showPage((this._pc.get_other().get_page() * 10) + 0);
                } else if (cmd.equalsIgnoreCase("q1")) {
                    showPage((this._pc.get_other().get_page() * 10) + 1);
                } else if (cmd.equalsIgnoreCase("q2")) {
                    showPage((this._pc.get_other().get_page() * 10) + 2);
                } else if (cmd.equalsIgnoreCase("q3")) {
                    showPage((this._pc.get_other().get_page() * 10) + 3);
                } else if (cmd.equalsIgnoreCase("q4")) {
                    showPage((this._pc.get_other().get_page() * 10) + 4);
                } else if (cmd.equalsIgnoreCase("q5")) {
                    showPage((this._pc.get_other().get_page() * 10) + 5);
                } else if (cmd.equalsIgnoreCase("q6")) {
                    showPage((this._pc.get_other().get_page() * 10) + 6);
                } else if (cmd.equalsIgnoreCase("q7")) {
                    showPage((this._pc.get_other().get_page() * 10) + 7);
                } else if (cmd.equalsIgnoreCase("q8")) {
                    showPage((this._pc.get_other().get_page() * 10) + 8);
                } else if (cmd.equalsIgnoreCase("q9")) {
                    showPage((this._pc.get_other().get_page() * 10) + 9);
                }
            }
        } catch (Exception e2) {
            _log.error(e2.getLocalizedMessage(), e2);
        }
    }

    private void questDel(L1Quest quest) {
        String over;
        try {
            if (quest.is_del()) {
                this._pc.setTempID(quest.get_id());
                if (this._pc.getQuest().isEnd(quest.get_id())) {
                    over = "完成任務";
                } else {
                    over = String.valueOf(this._pc.getQuest().get_step(quest.get_id())) + " / " + quest.get_difficulty();
                }
                this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qi2", new String[]{quest.get_questname(), Integer.toString(quest.get_questlevel()), over}));
                return;
            }
            this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_q_not5"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void isDel(L1Quest quest) {
        try {
            if (quest.is_del()) {
                QuestClass.get().stopQuest(this._pc, quest.get_id());
                CharacterQuestReading.get().delQuest(this._pc.getId(), quest.get_id());
                this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qi3", new String[]{quest.get_questname(), Integer.toString(quest.get_questlevel())}));
                return;
            }
            this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_q_not5"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void showStartQuest(L1PcInstance pc, int objid) {
        int key = 0;
        try {
            pc.get_otherList().QUESTMAP.clear();
            int i = QuestTable.MINQID;
            int key2 = 0;
            while (i <= QuestTable.MAXQID) {
                L1Quest value = QuestTable.get().getTemplate(i);
                if (value != null) {
                    if (pc.getQuest().isEnd(value.get_id())) {
                        key = key2;
                    } else if (pc.getQuest().isStart(value.get_id())) {
                        key = key2 + 1;
                        pc.get_otherList().QUESTMAP.put(Integer.valueOf(key2), value);
                    }
                    i++;
                    key2 = key;
                }
                key = key2;
                i++;
                key2 = key;
            }
            if (pc.get_otherList().QUESTMAP.size() <= 0) {
                pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not7"));
            } else {
                new L1ActionShowHtml(pc).showQuestMap(0);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void showQuest(L1PcInstance pc, int objid) {
        int key = 0;
        try {
            pc.get_otherList().QUESTMAP.clear();
            int i = QuestTable.MINQID;
            int key2 = 0;
            while (i <= QuestTable.MAXQID) {
                L1Quest value = QuestTable.get().getTemplate(i);
                if (value != null && pc.getLevel() >= value.get_questlevel()) {
                    if (pc.getQuest().isEnd(value.get_id())) {
                        key = key2;
                    } else if (pc.getQuest().isStart(value.get_id())) {
                        key = key2;
                    } else if (value.check(pc)) {
                        key = key2 + 1;
                        pc.get_otherList().QUESTMAP.put(Integer.valueOf(key2), value);
                    }
                    i++;
                    key2 = key;
                }
                key = key2;
                i++;
                key2 = key;
            }
            if (pc.get_otherList().QUESTMAP.size() <= 0) {
                pc.sendPackets(new S_NPCTalkReturn(objid, "y_q_not4"));
            } else {
                new L1ActionShowHtml(pc).showQuestMap(0);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void showQuestAll(L1PcInstance pc, int objid) {
        try {
            pc.get_otherList().QUESTMAP.clear();
            int key = 0;
            for (int i = QuestTable.MINQID; i <= QuestTable.MAXQID; i++) {
                L1Quest value = QuestTable.get().getTemplate(i);
                if (value == null || !value.check(pc)) {
                    key = key;
                } else {
                    key++;
                    pc.get_otherList().QUESTMAP.put(Integer.valueOf(key), value);
                }
            }
            new L1ActionShowHtml(pc).showQuestMap(0);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void showPage(int key) {
        String over;
        try {
            L1Quest quest = this._pc.get_otherList().QUESTMAP.get(Integer.valueOf(key));
            this._pc.setTempID(quest.get_id());
            if (this._pc.getQuest().isEnd(quest.get_id())) {
                over = "完成任務";
            } else {
                over = String.valueOf(this._pc.getQuest().get_step(quest.get_id())) + " / " + quest.get_difficulty();
            }
            this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "y_qi1", new String[]{quest.get_questname(), Integer.toString(quest.get_questlevel()), over, ""}));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void selWeaponQieHuan(String cmd) {
        int weaponObjId;
        try {
            int index = Integer.parseInt(cmd.substring(12));
            if (index >= 3 && index <= 17 && (weaponObjId = this._pc.getWeaponItemObjId(index)) > 0 && this._pc.getInventory().getItem(weaponObjId) != null) {
                if (index >= 3 && index <= 7) {
                    this._pc.setWeaponItemObjId(weaponObjId, 0);
                } else if (index >= 8 && index <= 12) {
                    this._pc.setWeaponItemObjId(weaponObjId, 1);
                } else if (index >= 13 && index <= 17) {
                    this._pc.setWeaponItemObjId(weaponObjId, 2);
                }
                loadWeaponQieHuan();
            }
        } catch (Exception ignored) {
        }
    }

    private void loadWeaponQieHuan() {
        String[] datas = new String[18];
        for (int i = 0; i < 18; i++) {
            int weaponObjId = this._pc.getWeaponItemObjId(i);
            if (weaponObjId > 0) {
                L1ItemInstance weaponItem = this._pc.getInventory().getItem(weaponObjId);
                if (weaponItem != null) {
                    datas[i] = weaponItem.getLogName();
                } else {
                    datas[i] = "空";
                }
            } else {
                datas[i] = "空";
            }
        }
        this._pc.sendPackets(new S_NPCTalkReturn(this._pc.getId(), "serverweapon", datas));
    }

    private void summonMonster(L1PcInstance pc, String s) {
        try {
            new SUMMON_MONSTER().start(pc, s);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
