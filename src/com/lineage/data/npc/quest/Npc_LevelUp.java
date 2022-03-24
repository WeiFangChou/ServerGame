package com.lineage.data.npc.quest;

import com.lineage.config.ConfigAlt;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.clientpackets.C_CreateChar;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus2;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_LevelUp extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_LevelUp.class);

    private Npc_LevelUp() {
    }

    public static NpcExecutor get() {
        return new Npc_LevelUp();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX1_1", (String[]) null));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        String[] info = null;
        if (cmd.equalsIgnoreCase("c")) {
            if (pc.getInventory().checkItemX(49142, 1) != null) {
                pc.get_otherList().clear_uplevelList();
                info = showInfo(pc, 2);
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", (String[]) null));
            }
        } else if (cmd.equalsIgnoreCase("s1")) {
            info = showInfoX(pc, 1);
        } else if (cmd.equalsIgnoreCase("s2")) {
            info = showInfoX(pc, 2);
        } else if (cmd.equalsIgnoreCase("d1")) {
            info = showInfoX(pc, 3);
        } else if (cmd.equalsIgnoreCase("d2")) {
            info = showInfoX(pc, 4);
        } else if (cmd.equalsIgnoreCase("c1")) {
            info = showInfoX(pc, 5);
        } else if (cmd.equalsIgnoreCase("c2")) {
            info = showInfoX(pc, 6);
        } else if (cmd.equalsIgnoreCase("w1")) {
            info = showInfoX(pc, 7);
        } else if (cmd.equalsIgnoreCase("w2")) {
            info = showInfoX(pc, 8);
        } else if (cmd.equalsIgnoreCase("i1")) {
            info = showInfoX(pc, 9);
        } else if (cmd.equalsIgnoreCase("i2")) {
            info = showInfoX(pc, 10);
        } else if (cmd.equalsIgnoreCase("h1")) {
            info = showInfoX(pc, 11);
        } else if (cmd.equalsIgnoreCase("h2")) {
            info = showInfoX(pc, 12);
        } else if (cmd.equalsIgnoreCase("x")) {
            if (pc.getInventory().checkItemX(49142, 1) != null) {
                int elixirStats = pc.get_otherList().get_uplevelList(0).intValue();
                if (elixirStats > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4", new String[]{String.valueOf(elixirStats)}));
                    return;
                }
                stopSkill(pc);
                info = showInfo(pc, 0);
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", (String[]) null));
            }
        } else if (cmd.equalsIgnoreCase("b")) {
            if (pc.getInventory().checkItemX(49142, 1) != null) {
                int elixirStats2 = pc.get_otherList().get_uplevelList(0).intValue();
                if (elixirStats2 > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4", new String[]{String.valueOf(elixirStats2)}));
                    return;
                }
                stopSkill(pc);
                info = showInfo(pc, 1);
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", (String[]) null));
            }
        } else if (cmd.equalsIgnoreCase("d")) {
            L1ItemInstance item = pc.getInventory().checkItemX(49142, 1);
            if (item != null) {
                int elixirStats3 = pc.get_otherList().get_uplevelList(0).intValue();
                if (elixirStats3 > 0) {
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX4", new String[]{String.valueOf(elixirStats3)}));
                    return;
                }
                stopSkill(pc);
                pc.getInventory().removeItem(item, 1);
                int hp = CalcInitHpMp.calcInitHp(pc);
                int mp = CalcInitHpMp.calcInitMp(pc);
                int baseStr = pc.getBaseStr();
                int baseDex = pc.getBaseDex();
                int baseCon = pc.getBaseCon();
                int baseWis = pc.getBaseWis();
                int baseInt = pc.getBaseInt();
                int baseCha = pc.getBaseCha();
                pc.addBaseStr(-baseStr);
                pc.addBaseDex(-baseDex);
                pc.addBaseCon(-baseCon);
                pc.addBaseWis(-baseWis);
                pc.addBaseInt(-baseInt);
                pc.addBaseCha(-baseCha);
                int originalStr = pc.get_otherList().get_uplevelList(1).intValue();
                int originalDex = pc.get_otherList().get_uplevelList(2).intValue();
                int originalCon = pc.get_otherList().get_uplevelList(3).intValue();
                int originalWis = pc.get_otherList().get_uplevelList(4).intValue();
                int originalInt = pc.get_otherList().get_uplevelList(5).intValue();
                int originalCha = pc.get_otherList().get_uplevelList(6).intValue();
                int addStr = pc.get_otherList().get_uplevelList(7).intValue();
                int addDex = pc.get_otherList().get_uplevelList(8).intValue();
                int addCon = pc.get_otherList().get_uplevelList(9).intValue();
                int addWis = pc.get_otherList().get_uplevelList(10).intValue();
                int addInt = pc.get_otherList().get_uplevelList(11).intValue();
                int addCha = pc.get_otherList().get_uplevelList(12).intValue();
                pc.addBaseStr((byte) ((addStr + originalStr) - 1));
                pc.addBaseDex((byte) ((addDex + originalDex) - 1));
                pc.addBaseCon((byte) ((addCon + originalCon) - 1));
                pc.addBaseWis((byte) ((addWis + originalWis) - 1));
                pc.addBaseInt((byte) ((addInt + originalInt) - 1));
                pc.addBaseCha((byte) ((addCha + originalCha) - 1));
                pc.setOriginalStr(pc.get_otherList().get_newPcOriginal()[0]);
                pc.setOriginalDex(pc.get_otherList().get_newPcOriginal()[1]);
                pc.setOriginalCon(pc.get_otherList().get_newPcOriginal()[2]);
                pc.setOriginalWis(pc.get_otherList().get_newPcOriginal()[3]);
                pc.setOriginalInt(pc.get_otherList().get_newPcOriginal()[4]);
                pc.setOriginalCha(pc.get_otherList().get_newPcOriginal()[5]);
                pc.addMr(0 - pc.getMr());
                pc.addDmgup(0 - pc.getDmgup());
                pc.addHitup(0 - pc.getHitup());
                pc.addBaseMaxHp((short) (hp - pc.getBaseMaxHp()));
                pc.addBaseMaxMp((short) (mp - pc.getBaseMaxMp()));
                if (pc.getOriginalAc() > 0) {
                    pc.addAc(pc.getOriginalAc());
                }
                if (pc.getOriginalMr() > 0) {
                    pc.addMr(0 - pc.getOriginalMr());
                }
                pc.refresh();
                setHpMp(pc);
                pc.setCurrentHp(pc.getMaxHp());
                pc.setCurrentMp(pc.getMaxMp());
                try {
                    CharacterTable.saveCharStatus(pc);
                    pc.sendPackets(new S_OwnCharStatus2(pc));
                    pc.sendPackets(new S_OwnCharAttrDef(pc));
                    pc.sendPackets(new S_OwnCharStatus(pc));
                    pc.sendPackets(new S_SPMR(pc));
                    pc.save();
                } catch (Exception e) {
                    _log.error(e.getLocalizedMessage(), e);
                }
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 7625));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_E", (String[]) null));
            }
            pc.sendPackets(new S_CloseList(pc.getId()));
            pc.get_otherList().clear_uplevelList();
        }
        if (info != null) {
            switch (pc.get_otherList().get_uplevelList(13).intValue()) {
                case 0:
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX2", info));
                    return;
                case 1:
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "yuleX2_1", info));
                    return;
                case 2:
                    pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "ySrc_1", info));
                    return;
                default:
                    return;
            }
        }
    }

    public static void setHpMp(L1PcInstance pc) {
        for (int i = 0; i < pc.getLevel(); i++) {
            short randomHp = CalcStat.calcStatHp(pc.getType(), pc.getBaseMaxHp(), pc.getBaseCon(), pc.getOriginalHpup());
            short randomMp = CalcStat.calcStatMp(pc.getType(), pc.getBaseMaxMp(), pc.getBaseWis(), pc.getOriginalMpup());
            pc.addBaseMaxHp(randomHp);
            pc.addBaseMaxMp(randomMp);
        }
    }

    public static String[] showInfoX(L1PcInstance pc, int mode) {
        int addCha = 0;
        int addCha2 = 0;
        int addInt = 0;
        int addInt2= 0;
        int addWis= 0;
        int addWis2= 0;
        int addCon= 0;
        int addCon2= 0;
        int addDex= 0;
        int addDex2= 0;
        int addStr= 0;
        int addStr2= 0;
        int[] max = new int[6];
        switch (pc.get_otherList().get_uplevelList(13).intValue()) {
            case 0:
                max = new int[]{ConfigAlt.POWER, ConfigAlt.POWER, ConfigAlt.POWER, ConfigAlt.POWER, ConfigAlt.POWER, ConfigAlt.POWER};
                break;
            case 1:
                max = new int[]{ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE, ConfigAlt.POWERMEDICINE};
                break;
            case 2:
                switch (pc.getType()) {
                    case 0:
                        max = new int[]{20, 18, 18, 18, 18, 18};
                        break;
                    case 1:
                        max = new int[]{20, 16, 18, 13, 12, 16};
                        break;
                    case 2:
                        max = new int[]{18, 18, 18, 18, 18, 16};
                        break;
                    case 3:
                        max = new int[]{20, 14, 18, 18, 18, 18};
                        break;
                    case 4:
                        max = new int[]{18, 18, 18, 18, 18, 18};
                        break;
                    case 5:
                        max = new int[]{19, 16, 18, 17, 17, 14};
                        break;
                    case 6:
                        max = new int[]{19, 16, 18, 18, 18, 18};
                        break;
                }
        }
        int elixirStats = pc.get_otherList().get_uplevelList(0).intValue();
        int originalStr = pc.get_otherList().get_uplevelList(1).intValue();
        int originalDex = pc.get_otherList().get_uplevelList(2).intValue();
        int originalCon = pc.get_otherList().get_uplevelList(3).intValue();
        int originalWis = pc.get_otherList().get_uplevelList(4).intValue();
        int originalInt = pc.get_otherList().get_uplevelList(5).intValue();
        int originalCha = pc.get_otherList().get_uplevelList(6).intValue();
        int addStr3 = pc.get_otherList().get_uplevelList(7).intValue();
        int addDex3 = pc.get_otherList().get_uplevelList(8).intValue();
        int addCon3 = pc.get_otherList().get_uplevelList(9).intValue();
        int addWis3 = pc.get_otherList().get_uplevelList(10).intValue();
        int addInt3 = pc.get_otherList().get_uplevelList(11).intValue();
        int addCha3 = pc.get_otherList().get_uplevelList(12).intValue();
        switch (mode) {
            case 1:
                elixirStats--;
                if (elixirStats >= 0 && (addStr2 = addStr3 + 1) <= max[0] - originalStr && addStr2 >= 0) {
                    pc.get_otherList().add_levelList(7, addStr2);
                    break;
                } else {
                    return null;
                }
            case 2:
                elixirStats++;
                if (elixirStats >= 0 && addStr3 - 1 <= max[0] - originalStr && addStr >= 0) {
                    pc.get_otherList().add_levelList(7, addStr);
                    break;
                } else {
                    return null;
                }
            case 3:
                elixirStats--;
                if (elixirStats >= 0 && (addDex2 = addDex3 + 1) <= max[1] - originalDex && addDex2 >= 0) {
                    pc.get_otherList().add_levelList(8, addDex2);
                    break;
                } else {
                    return null;
                }

            case 4:
                elixirStats++;
                if (elixirStats >= 0 && addDex3 - 1 <= max[1] - originalDex && addDex >= 0) {
                    pc.get_otherList().add_levelList(8, addDex);
                    break;
                } else {
                    return null;
                }
            case 5:
                elixirStats--;
                if (elixirStats >= 0 && (addCon2 = addCon3 + 1) <= max[2] - originalCon && addCon2 >= 0) {
                    pc.get_otherList().add_levelList(9, addCon2);
                    break;
                } else {
                    return null;
                }

            case 6:
                elixirStats++;
                if (elixirStats >= 0 && addCon3 - 1 <= max[2] - originalCon && addCon >= 0) {
                    pc.get_otherList().add_levelList(9, addCon);
                    break;
                } else {
                    return null;
                }
            case 7:
                elixirStats--;
                if (elixirStats >= 0 && (addWis2 = addWis3 + 1) <= max[3] - originalWis && addWis2 >= 0) {
                    pc.get_otherList().add_levelList(10, addWis2);
                    break;
                } else {
                    return null;
                }
            case 8:
                elixirStats++;
                if (elixirStats >= 0 && addWis3 - 1 <= max[3] - originalWis && addWis >= 0) {
                    pc.get_otherList().add_levelList(10, addWis);
                    break;
                } else {
                    return null;
                }

            case 9:
                elixirStats--;
                if (elixirStats >= 0 && (addInt2 = addInt3 + 1) <= max[4] - originalInt && addInt2 >= 0) {
                    pc.get_otherList().add_levelList(11, addInt2);
                    break;
                } else {
                    return null;
                }
            case 10:
                elixirStats++;
                if (elixirStats >= 0 && addInt3 - 1 <= max[4] - originalInt && addInt >= 0) {
                    pc.get_otherList().add_levelList(11, addInt);
                    break;
                } else {
                    return null;
                }

            case 11:
                elixirStats--;
                if (elixirStats >= 0 && (addCha2 = addCha3 + 1) <= max[5] - originalCha && addCha2 >= 0) {
                    pc.get_otherList().add_levelList(12, addCha2);
                    break;
                } else {
                    return null;
                }
            case 12:
                elixirStats++;
                if (elixirStats >= 0 && addCha3 - 1 <= max[5] - originalCha && addCha >= 0) {
                    pc.get_otherList().add_levelList(12, addCha);
                    break;
                } else {
                    return null;
                }

        }
        pc.get_otherList().add_levelList(0, elixirStats);
        return info(pc);
    }

    public static String[] showInfo(L1PcInstance pc, int mode) {
        int elixirStats = 0;
        int type = pc.getType();
        int originalStr = C_CreateChar.ORIGINAL_STR[type];
        int originalDex = C_CreateChar.ORIGINAL_DEX[type];
        int originalCon = C_CreateChar.ORIGINAL_CON[type];
        int originalWis = C_CreateChar.ORIGINAL_WIS[type];
        int originalInt = C_CreateChar.ORIGINAL_INT[type];
        int originalCha = C_CreateChar.ORIGINAL_CHA[type];
        switch (mode) {
            case 0:
                if (pc.getBonusStats() > 0) {
                    elixirStats = 0 + pc.getBonusStats();
                }
                pc.get_otherList().add_levelList(13, 0);
                break;
            case 1:
                if (pc.getElixirStats() > 0) {
                    elixirStats = 0 + pc.getElixirStats();
                }
                originalStr = pc.get_otherList().get_uplevelList(1).intValue();
                originalDex = pc.get_otherList().get_uplevelList(2).intValue();
                originalCon = pc.get_otherList().get_uplevelList(3).intValue();
                originalWis = pc.get_otherList().get_uplevelList(4).intValue();
                originalInt = pc.get_otherList().get_uplevelList(5).intValue();
                originalCha = pc.get_otherList().get_uplevelList(6).intValue();
                pc.get_otherList().add_levelList(13, 1);
                break;
            case 2:
                elixirStats = C_CreateChar.ORIGINAL_AMOUNT[type];
                pc.get_otherList().add_levelList(13, 2);
                break;
        }
        pc.get_otherList().add_levelList(0, elixirStats);
        switch (mode) {
            case 0:
                int addStrS = pc.get_otherList().get_uplevelList(7).intValue();
                int addDexS = pc.get_otherList().get_uplevelList(8).intValue();
                int addConS = pc.get_otherList().get_uplevelList(9).intValue();
                int addWisS = pc.get_otherList().get_uplevelList(10).intValue();
                int addIntS = pc.get_otherList().get_uplevelList(11).intValue();
                int addChaS = pc.get_otherList().get_uplevelList(12).intValue();
                pc.get_otherList().add_levelList(1, originalStr + addStrS);
                pc.get_otherList().add_levelList(2, originalDex + addDexS);
                pc.get_otherList().add_levelList(3, originalCon + addConS);
                pc.get_otherList().add_levelList(4, originalWis + addWisS);
                pc.get_otherList().add_levelList(5, originalInt + addIntS);
                pc.get_otherList().add_levelList(6, originalCha + addChaS);
                pc.get_otherList().set_newPcOriginal(new int[]{originalStr + addStrS, originalDex + addDexS, originalCon + addConS, originalWis + addWisS, originalInt + addIntS, originalCha + addChaS});
                break;
            case 1:
                int addStrR = pc.get_otherList().get_uplevelList(7).intValue();
                int addDexR = pc.get_otherList().get_uplevelList(8).intValue();
                int addConR = pc.get_otherList().get_uplevelList(9).intValue();
                int addWisR = pc.get_otherList().get_uplevelList(10).intValue();
                int addIntR = pc.get_otherList().get_uplevelList(11).intValue();
                int addChaR = pc.get_otherList().get_uplevelList(12).intValue();
                pc.get_otherList().add_levelList(1, originalStr + addStrR);
                pc.get_otherList().add_levelList(2, originalDex + addDexR);
                pc.get_otherList().add_levelList(3, originalCon + addConR);
                pc.get_otherList().add_levelList(4, originalWis + addWisR);
                pc.get_otherList().add_levelList(5, originalInt + addIntR);
                pc.get_otherList().add_levelList(6, originalCha + addChaR);
                break;
            case 2:
                pc.get_otherList().add_levelList(1, originalStr);
                pc.get_otherList().add_levelList(2, originalDex);
                pc.get_otherList().add_levelList(3, originalCon);
                pc.get_otherList().add_levelList(4, originalWis);
                pc.get_otherList().add_levelList(5, originalInt);
                pc.get_otherList().add_levelList(6, originalCha);
                break;
        }
        pc.get_otherList().add_levelList(7, 0);
        pc.get_otherList().add_levelList(8, 0);
        pc.get_otherList().add_levelList(9, 0);
        pc.get_otherList().add_levelList(10, 0);
        pc.get_otherList().add_levelList(11, 0);
        pc.get_otherList().add_levelList(12, 0);
        return info(pc);
    }

    private static String[] info(L1PcInstance pc) {
        String[] info;
        Object valueOf;
        Object valueOf2;
        int p1 = pc.get_otherList().get_uplevelList(0).intValue();
        int p2 = pc.get_otherList().get_uplevelList(1).intValue();
        int p3 = pc.get_otherList().get_uplevelList(7).intValue();
        int p4 = pc.get_otherList().get_uplevelList(2).intValue();
        int p5 = pc.get_otherList().get_uplevelList(8).intValue();
        int p6 = pc.get_otherList().get_uplevelList(3).intValue();
        int p7 = pc.get_otherList().get_uplevelList(9).intValue();
        int p8 = pc.get_otherList().get_uplevelList(4).intValue();
        int p9 = pc.get_otherList().get_uplevelList(10).intValue();
        int p10 = pc.get_otherList().get_uplevelList(5).intValue();
        int p11 = pc.get_otherList().get_uplevelList(11).intValue();
        int p12 = pc.get_otherList().get_uplevelList(6).intValue();
        int p13 = pc.get_otherList().get_uplevelList(12).intValue();
        if (pc.get_otherList().get_uplevelList(13).intValue() == 2) {
            int type = pc.getType();
            int elixirStats = C_CreateChar.ORIGINAL_AMOUNT[type];
            String nameid = "";
            switch (type) {
                case 0:
                    nameid = "$1127";
                    break;
                case 1:
                    nameid = "$1128";
                    break;
                case 2:
                    nameid = "$1129";
                    break;
                case 3:
                    nameid = "$1130";
                    break;
                case 4:
                    nameid = "$2503";
                    break;
                case 5:
                    nameid = "$5889";
                    break;
                case 6:
                    nameid = "$5890";
                    break;
            }
            info = new String[15];
            info[0] = nameid;
            info[1] = String.valueOf(elixirStats);
            info[2] = String.valueOf(p1);
            info[3] = String.valueOf(p2 < 10 ? "0" + p2 : Integer.valueOf(p2));
            info[4] = String.valueOf(p3 < 10 ? "0" + p3 : Integer.valueOf(p3));
            info[5] = String.valueOf(p4 < 10 ? "0" + p4 : Integer.valueOf(p4));
            info[6] = String.valueOf(p5 < 10 ? "0" + p5 : Integer.valueOf(p5));
            info[7] = String.valueOf(p6 < 10 ? "0" + p6 : Integer.valueOf(p6));
            info[8] = String.valueOf(p7 < 10 ? "0" + p7 : Integer.valueOf(p7));
            info[9] = String.valueOf(p8 < 10 ? "0" + p8 : Integer.valueOf(p8));
            info[10] = String.valueOf(p9 < 10 ? "0" + p9 : Integer.valueOf(p9));
            info[11] = String.valueOf(p10 < 10 ? "0" + p10 : Integer.valueOf(p10));
            info[12] = String.valueOf(p11 < 10 ? "0" + p11 : Integer.valueOf(p11));
            info[13] = String.valueOf(p12 < 10 ? "0" + p12 : Integer.valueOf(p12));
            if (p13 < 10) {
                valueOf2 = "0" + p13;
            } else {
                valueOf2 = Integer.valueOf(p13);
            }
            info[14] = String.valueOf(valueOf2);
        } else {
            info = new String[13];
            info[0] = String.valueOf(p1);
            info[1] = String.valueOf(p2 < 10 ? "0" + p2 : Integer.valueOf(p2));
            info[2] = String.valueOf(p3 < 10 ? "0" + p3 : Integer.valueOf(p3));
            info[3] = String.valueOf(p4 < 10 ? "0" + p4 : Integer.valueOf(p4));
            info[4] = String.valueOf(p5 < 10 ? "0" + p5 : Integer.valueOf(p5));
            info[5] = String.valueOf(p6 < 10 ? "0" + p6 : Integer.valueOf(p6));
            info[6] = String.valueOf(p7 < 10 ? "0" + p7 : Integer.valueOf(p7));
            info[7] = String.valueOf(p8 < 10 ? "0" + p8 : Integer.valueOf(p8));
            info[8] = String.valueOf(p9 < 10 ? "0" + p9 : Integer.valueOf(p9));
            info[9] = String.valueOf(p10 < 10 ? "0" + p10 : Integer.valueOf(p10));
            info[10] = String.valueOf(p11 < 10 ? "0" + p11 : Integer.valueOf(p11));
            info[11] = String.valueOf(p12 < 10 ? "0" + p12 : Integer.valueOf(p12));
            if (p13 < 10) {
                valueOf = "0" + p13;
            } else {
                valueOf = Integer.valueOf(p13);
            }
            info[12] = String.valueOf(valueOf);
        }
        return info;
    }

    public static void stopSkill(L1PcInstance pc) {
        pc.getInventory().takeoffEquip(945);
        for (int skillNum = 1; skillNum <= 220; skillNum++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum) || pc.isDead()) {
                pc.removeSkillEffect(skillNum);
            }
        }
        pc.curePoison();
        pc.cureParalaysis();
        for (int skillNum2 = 998; skillNum2 <= 1026; skillNum2++) {
            pc.removeSkillEffect(skillNum2);
        }
        for (int skillNum3 = 3000; skillNum3 <= 3047; skillNum3++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum3)) {
                pc.removeSkillEffect(skillNum3);
            }
        }
        if (pc.getHasteItemEquipped() > 0) {
            pc.setMoveSpeed(0);
            pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
        }
        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
    }
}
