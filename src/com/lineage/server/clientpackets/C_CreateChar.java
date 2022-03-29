package com.lineage.server.clientpackets;

import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.list.BadNamesList;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.BeginnerTable;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_CharCreateStatus;
import com.lineage.server.serverpackets.S_NewCharPacket;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.world.World;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1WilliamSystemMessage;

public class C_CreateChar extends ClientBasePacket {
    public static final String[] BANLIST;
    private static final int[][] CLASS_LIST;
    private static final int[][] LOC_LIST = {new int[]{32684, 32870, 2005}, new int[]{32686, 32867, 2005}, new int[]{32691, 32864, 2005}, new int[]{32684, 32870, 2005}, new int[]{32686, 32867, 2005}, new int[]{32691, 32864, 2005}, new int[]{32691, 32864, 2005}};
    public static final int[] ORIGINAL_AMOUNT = {8, 4, 7, 16, 10, 6, 10};
    public static final int[] ORIGINAL_CHA = {13, 12, 9, 8, 9, 8, 8};
    public static final int[] ORIGINAL_CON = {10, 14, 12, 12, 8, 14, 12};
    public static final int[] ORIGINAL_DEX = {10, 12, 12, 7, 15, 11, 10};
    public static final int[] ORIGINAL_INT = {10, 8, 12, 12, 11, 11, 12};
    public static final int[] ORIGINAL_STR = {13, 16, 11, 8, 12, 13, 11};
    public static final int[] ORIGINAL_WIS = {11, 9, 12, 12, 10, 12, 12};
    private static final Log _log = LogFactory.getLog(C_CreateChar.class);

    static {
        int[] iArr = new int[7];
        iArr[1] = 61;
        iArr[2] = 138;
        iArr[3] = 734;
        iArr[4] = 2786;
        iArr[5] = 6658;
        iArr[6] = 6671;
        CLASS_LIST = new int[][]{iArr, new int[]{1, 48, 37, L1PcInstance.CLASSID_WIZARD_FEMALE, L1PcInstance.CLASSID_DARK_ELF_FEMALE, L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE, L1PcInstance.CLASSID_ILLUSIONIST_FEMALE}};
        String[] strArr = new String[OpcodesServer.S_OPCODE_WAR];
        strArr[0] = "あ";
        strArr[1] = "ア";
        strArr[2] = "い";
        strArr[3] = "イ";
        strArr[4] = "う";
        strArr[5] = "ウ";
        strArr[6] = "え";
        strArr[7] = "エ";
        strArr[8] = "お";
        strArr[9] = "オ";
        strArr[10] = "か";
        strArr[11] = "カ";
        strArr[12] = "き";
        strArr[13] = "キ";
        strArr[14] = "く";
        strArr[15] = "ク";
        strArr[16] = "け";
        strArr[17] = "ケ";
        strArr[18] = "こ";
        strArr[19] = "コ";
        strArr[20] = "さ";
        strArr[21] = "サ";
        strArr[22] = "し";
        strArr[23] = "シ";
        strArr[24] = "す";
        strArr[25] = "ス";
        strArr[26] = "せ";
        strArr[27] = "セ";
        strArr[28] = "そ";
        strArr[29] = "ソ";
        strArr[30] = "た";
        strArr[31] = "タ";
        strArr[32] = "ち";
        strArr[33] = "チ";
        strArr[34] = "つ";
        strArr[35] = "ツ";
        strArr[36] = "て";
        strArr[37] = "テ";
        strArr[38] = "と";
        strArr[39] = "ト";
        strArr[40] = "な";
        strArr[41] = "ナ";
        strArr[42] = "に";
        strArr[43] = "ニ";
        strArr[44] = "ぬ";
        strArr[45] = "ヌ";
        strArr[46] = "ね";
        strArr[47] = "ネ";
        strArr[48] = "の";
        strArr[49] = "ノ";
        strArr[50] = "は";
        strArr[51] = "ハ";
        strArr[52] = "ひ";
        strArr[53] = "ヒ";
        strArr[54] = "ふ";
        strArr[55] = "フ";
        strArr[56] = "へ";
        strArr[57] = "ヘ";
        strArr[58] = "ほ";
        strArr[59] = "ホ";
        strArr[60] = "ま";
        strArr[61] = "マ";
        strArr[62] = "み";
        strArr[63] = "ミ";
        strArr[64] = "む";
        strArr[65] = "ム";
        strArr[66] = "め";
        strArr[67] = "メ";
        strArr[68] = "も";
        strArr[69] = "モ";
        strArr[70] = "や";
        strArr[71] = "ヤ";
        strArr[72] = "ゆ";
        strArr[73] = "ユ";
        strArr[74] = "よ";
        strArr[75] = "ヨ";
        strArr[76] = "ら";
        strArr[77] = "ラ";
        strArr[78] = "り";
        strArr[79] = "リ";
        strArr[80] = "る";
        strArr[81] = "ル";
        strArr[82] = "れ";
        strArr[83] = "レ";
        strArr[84] = "ろ";
        strArr[85] = "ロ";
        strArr[86] = "わ";
        strArr[87] = "ワ";
        strArr[88] = "を";
        strArr[89] = "ヲ";
        strArr[90] = "ん";
        strArr[91] = "ン";
        strArr[92] = "丶";
        strArr[93] = "";
        strArr[94] = "";
        strArr[95] = "-";
        strArr[96] = "/";
        strArr[97] = "+";
        strArr[98] = "*";
        strArr[99] = "?";
        strArr[100] = "!";
        strArr[101] = "@";
        strArr[102] = "#";
        strArr[103] = "$";
        strArr[104] = "%";
        strArr[105] = "^";
        strArr[106] = "&";
        strArr[107] = "(";
        strArr[108] = ")";
        strArr[109] = "[";
        strArr[110] = "]";
        strArr[111] = "<";
        strArr[112] = ">";
        strArr[113] = "{";
        strArr[114] = "}";
        strArr[115] = ";";
        strArr[116] = ":";
        strArr[117] = "'";
        strArr[118] = "\"";
        strArr[119] = ",";
        strArr[120] = ".";
        strArr[121] = "~";
        strArr[122] = "`";
        BANLIST = strArr;
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = new L1PcInstance();
            String name = Matcher.quoteReplacement(readS());
            int maxAmount = ConfigAlt.DEFAULT_CHARACTER_SLOT + client.getAccount().get_character_slot();
            String name2 = name.replaceAll("\\s", "").replaceAll("　", "");
            if (name2.length() == 0) {
                client.out().encrypt(new S_CharCreateStatus(9));
            } else if (!isInvalidName(name2)) {
                client.out().encrypt(new S_CharCreateStatus(9));
                over();
            } else if (CharObjidTable.get().charObjid(name2) != 0) {
                client.out().encrypt(new S_CharCreateStatus(6));
                over();
            } else {
                int countCharacters = client.getAccount().get_countCharacters();
                if (countCharacters >= maxAmount) {
                    client.out().encrypt(new S_CharCreateStatus(21));
                    over();
                    return;
                }
                pc.setName(name2);
                pc.setType(readC());
                pc.set_sex(readC());
                pc.addBaseStr((byte) readC());
                pc.addBaseDex((byte) readC());
                pc.addBaseCon((byte) readC());
                pc.addBaseWis((byte) readC());
                pc.addBaseCha((byte) readC());
                pc.addBaseInt((byte) readC());
                boolean isStatusError = false;
                int originalStr = ORIGINAL_STR[pc.getType()];
                int originalDex = ORIGINAL_DEX[pc.getType()];
                int originalCon = ORIGINAL_CON[pc.getType()];
                int originalWis = ORIGINAL_WIS[pc.getType()];
                int originalCha = ORIGINAL_CHA[pc.getType()];
                int originalInt = ORIGINAL_INT[pc.getType()];
                int originalAmount = ORIGINAL_AMOUNT[pc.getType()];
                if (pc.getBaseStr() < originalStr || pc.getBaseDex() < originalDex || pc.getBaseCon() < originalCon || pc.getBaseWis() < originalWis || pc.getBaseCha() < originalCha || pc.getBaseInt() < originalInt || pc.getBaseStr() > originalStr + originalAmount || pc.getBaseDex() > originalDex + originalAmount || pc.getBaseCon() > originalCon + originalAmount || pc.getBaseWis() > originalWis + originalAmount || pc.getBaseCha() > originalCha + originalAmount || pc.getBaseInt() > originalInt + originalAmount) {
                    isStatusError = true;
                }
                if (pc.getDex() + pc.getCha() + pc.getCon() + pc.getInt() + pc.getStr() + pc.getWis() != 75 || isStatusError) {
                    client.out().encrypt(new S_CharCreateStatus(21));
                    over();
                } else if (!ConfigOther.Class_Id || !(pc.getType() == 6 || pc.getType() == 5)) {
                    client.getAccount().set_countCharacters(countCharacters + 1);
                    client.out().encrypt(new S_CharCreateStatus(2));
                    initNewChar(client, pc);
                    over();
                } else {
                    client.out().encrypt(new S_CharCreateStatus(21));
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private static void initNewChar(ClientExecutor client, L1PcInstance pc) {
        try {
            L1Account account = client.getAccount();
            pc.setId(IdFactory.get().nextId());
            int classid = CLASS_LIST[pc.get_sex()][pc.getType()];
            pc.setClassId(classid);
            pc.setTempCharGfx(classid);
            pc.setGfxId(classid);
            int[] loc = LOC_LIST[pc.getType()];
            pc.setX(loc[0]);
            pc.setY(loc[1]);
            pc.setMap( loc[2]);
            pc.setHeading(0);
            pc.setLawful(0);
            int initHp = CalcInitHpMp.calcInitHp(pc);
            int initMp = CalcInitHpMp.calcInitMp(pc);
            pc.addBaseMaxHp( initHp);
            pc.setCurrentHp( initHp);
            pc.addBaseMaxMp( initMp);
            pc.setCurrentMp( initMp);
            pc.resetBaseAc();
            pc.setTitle("");
            pc.setClanid(0);
            if (ConfigOther.pc_is) {
                String msg = "";
                if (pc.isCrown()) {
                    msg = "王族";
                } else if (pc.isKnight()) {
                    msg = "騎士";
                } else if (pc.isElf()) {
                    msg = "妖精";
                } else if (pc.isWizard()) {
                    msg = "法師";
                } else if (pc.isDarkelf()) {
                    msg = "黑暗妖精";
                } else if (pc.isDragonKnight()) {
                    msg = "龍騎士";
                } else if (pc.isIllusionist()) {
                    msg = "幻術師";
                }
                for (L1PcInstance allpc : World.get().getAllPlayers()) {
                    if (allpc != null) {
                        allpc.sendPackets(new S_ServerMessage(L1WilliamSystemMessage.ShowMessage(11) + "：【" + pc.getName() + "】" + L1WilliamSystemMessage.ShowMessage(12) + "：【" + msg + "】。"));
                    }
                }
            }
            pc.setClanRank(0);
            pc.set_food(OpcodesClient.C_OPCODE_PLEDGE);
            if (account.get_access_level() >= 200) {
                pc.setAccessLevel( account.get_access_level());
                pc.setGm(true);
                pc.setMonitor(false);
            } else {
                pc.setAccessLevel( 0);
                pc.setGm(false);
                pc.setMonitor(false);
            }
            pc.setGmInvis(false);
            pc.setExp(0);
            pc.setHighLevel(0);
            pc.setStatus(0);
            pc.setClanname("");
            pc.setBonusStats(0);
            pc.setElixirStats(0);
            pc.resetBaseMr();
            pc.setElfAttr(0);
            pc.set_PKcount(0);
            pc.setPkCountForElf(0);
            pc.setExpRes(0);
            pc.setPartnerId(0);
            pc.setOnlineStatus(0);
            pc.setHomeTownId(0);
            pc.setContribution(0);
            pc.setBanned(false);
            pc.setKarma(0);
            if (pc.isWizard()) {
                int object_id = pc.getId();
                L1Skills l1skills = SkillsTable.get().getTemplate(4);
                String skill_name = l1skills.getName();
                CharSkillReading.get().spellMastery(object_id, l1skills.getSkillId(), skill_name, 0, 0);
            }
            pc.setAccountName(client.getAccountName());
            pc.refresh();
            client.out().encrypt(new S_NewCharPacket(pc));
            CharacterTable.get().storeNewCharacter(pc);
            CharacterTable.saveCharStatus(pc);
            BeginnerTable.get().giveItem(pc);
            CharObjidTable.get().addChar(pc.getId(), pc.getName());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean isInvalidName(String name) {
        try {
            for (String ban : BANLIST) {
                if (name.indexOf(ban) != -1) {
                    return false;
                }
            }
            if (BadNamesList.get().isBadName(name)) {
                return false;
            }
            int numOfNameBytes = name.getBytes(Config.CLIENT_LANGUAGE_CODE).length;
            if (5 < numOfNameBytes - name.length() || 12 < numOfNameBytes) {
                return false;
            }
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
