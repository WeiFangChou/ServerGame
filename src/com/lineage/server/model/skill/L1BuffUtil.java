package com.lineage.server.model.skill;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1BuffUtil {
    private static final Log _log = LogFactory.getLog(L1BuffUtil.class);

    public static boolean stopPotion(L1PcInstance pc) {
        if (!pc.hasSkillEffect(71)) {
            return true;
        }
        pc.sendPackets(new S_ServerMessage(698));
        return false;
    }

    public static boolean getUseItemTeleport(L1PcInstance pc) {
        if (!pc.hasSkillEffect(157) && !pc.hasSkillEffect(L1SkillId.SHOCK_SKIN)) {
            return true;
        }
        return false;
    }

    public static void cancelAbsoluteBarrier(L1PcInstance pc) {
        if (pc.hasSkillEffect(78)) {
            pc.killSkillEffectTimer(78);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }
    }

    public static void thirdSpeed(L1PcInstance pc) {
        if (pc.hasSkillEffect(998)) {
            pc.killSkillEffectTimer(998);
        }
        pc.setSkillEffect(998, 600000);
        pc.sendPackets(new S_SkillSound(pc.getId(), 8031));
        pc.broadcastPacketX10(new S_SkillSound(pc.getId(), 8031));
        pc.sendPackets(new S_Liquor(pc.getId(), 8));
        pc.broadcastPacketX10(new S_Liquor(pc.getId(), 8));
        pc.sendPackets(new S_ServerMessage(1065));
    }

    public static void hasteStart(L1PcInstance pc) {
        try {
            if (pc.hasSkillEffect(43)) {
                pc.killSkillEffectTimer(43);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }
            if (pc.hasSkillEffect(54)) {
                pc.killSkillEffectTimer(54);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_HASTE);
                pc.sendPacketsAll(new S_SkillHaste(pc.getId(), 0, 0));
                pc.setMoveSpeed(0);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void haste(L1PcInstance pc, int timeMillis) {
        try {
            hasteStart(pc);
            pc.setSkillEffect(L1SkillId.STATUS_HASTE, timeMillis);
            int objId = pc.getId();
            pc.sendPackets(new S_SkillHaste(objId, 1, timeMillis / L1SkillId.STATUS_BRAVE));
            pc.broadcastPacketAll(new S_SkillHaste(objId, 1, 0));
            pc.sendPacketsX8(new S_SkillSound(objId, L1SkillId.MORTAL_BODY));
            pc.setMoveSpeed(1);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void braveStart(L1PcInstance pc) {
        try {
            if (pc.hasSkillEffect(52)) {
                pc.killSkillEffectTimer(52);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(101)) {
                pc.killSkillEffectTimer(101);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(150)) {
                pc.killSkillEffectTimer(150);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_BRAVE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_BRAVE);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_ELFBRAVE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_ELFBRAVE);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE)) {
                pc.killSkillEffectTimer(L1SkillId.STATUS_RIBRAVE);
                pc.setBraveSpeed(0);
            }
            if (pc.hasSkillEffect(L1SkillId.BLOODLUST)) {
                pc.killSkillEffectTimer(L1SkillId.BLOODLUST);
                pc.sendPacketsAll(new S_SkillBrave(pc.getId(), 0, 0));
                pc.setBraveSpeed(0);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void brave(L1PcInstance pc, int timeMillis) {
        try {
            braveStart(pc);
            pc.setSkillEffect(L1SkillId.STATUS_BRAVE, timeMillis);
            int objId = pc.getId();
            pc.sendPackets(new S_SkillBrave(objId, 1, timeMillis / L1SkillId.STATUS_BRAVE));
            pc.broadcastPacketAll(new S_SkillBrave(objId, 1, 0));
            pc.sendPacketsX8(new S_SkillSound(objId, 751));
            pc.setBraveSpeed(1);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void doPoly(L1PcInstance pc) {
        try {
            int polyId = 0;
            switch (pc.getAwakeSkillId()) {
                case 185:
                    polyId = 9362;
                    break;
                case 190:
                    polyId = 9364;
                    break;
                case 195:
                    polyId = 9363;
                    break;
            }
            if (pc.hasSkillEffect(67)) {
                pc.killSkillEffectTimer(67);
            }
            pc.setTempCharGfx(polyId);
            pc.sendPacketsAll(new S_ChangeShape(pc, polyId));
            if (pc.getWeapon() != null) {
                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static void undoPoly(L1PcInstance pc) {
        try {
            int classId = pc.getClassId();
            pc.setTempCharGfx(classId);
            pc.sendPacketsAll(new S_ChangeShape(pc, classId));
            if (pc.getWeapon() != null) {
                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean cancelExpSkill(L1PcInstance pc) {
        if (pc.hasSkillEffect(L1SkillId.COOKING_1_7_N)) {
            pc.removeSkillEffect(L1SkillId.COOKING_1_7_N);
        }
        if (pc.hasSkillEffect(L1SkillId.COOKING_1_7_S)) {
            pc.removeSkillEffect(L1SkillId.COOKING_1_7_S);
        }
        if (pc.hasSkillEffect(L1SkillId.COOKING_2_7_N)) {
            pc.removeSkillEffect(L1SkillId.COOKING_2_7_N);
        }
        if (pc.hasSkillEffect(L1SkillId.COOKING_2_7_S)) {
            pc.removeSkillEffect(L1SkillId.COOKING_2_7_S);
        }
        if (pc.hasSkillEffect(L1SkillId.COOKING_3_7_N)) {
            pc.removeSkillEffect(L1SkillId.COOKING_3_7_N);
        }
        if (pc.hasSkillEffect(3047)) {
            pc.removeSkillEffect(3047);
        }
        if (pc.hasSkillEffect(4010)) {
            pc.sendPackets(new S_ServerMessage("130%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(4010)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP15)) {
            pc.sendPackets(new S_ServerMessage("150%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP15)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP17)) {
            pc.sendPackets(new S_ServerMessage("170%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP17)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP20)) {
            pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP20)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP25)) {
            pc.sendPackets(new S_ServerMessage("250%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP25)));
            return false;
        } else if (pc.hasSkillEffect(6671)) {
            pc.sendPackets(new S_ServerMessage("300%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(6671)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP35)) {
            pc.sendPackets(new S_ServerMessage("350%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP35)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP40)) {
            pc.sendPackets(new S_ServerMessage("400%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP40)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP45)) {
            pc.sendPackets(new S_ServerMessage("450%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP45)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP50)) {
            pc.sendPackets(new S_ServerMessage("500%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP50)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP55)) {
            pc.sendPackets(new S_ServerMessage("550%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP55)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP60)) {
            pc.sendPackets(new S_ServerMessage("600%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP60)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP65)) {
            pc.sendPackets(new S_ServerMessage("650%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP65)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP70)) {
            pc.sendPackets(new S_ServerMessage("700%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP70)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.EXP75)) {
            pc.sendPackets(new S_ServerMessage("750%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP75)));
            return false;
        } else if (!pc.hasSkillEffect(L1SkillId.EXP80)) {
            return true;
        } else {
            pc.sendPackets(new S_ServerMessage("800%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.EXP80)));
            return false;
        }
    }

    public static boolean cancelExpSkill_2(L1PcInstance pc) {
        if (pc.hasSkillEffect(L1SkillId.SEXP11)) {
            pc.sendPackets(new S_ServerMessage("110%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.SEXP11)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.SEXP13)) {
            pc.sendPackets(new S_ServerMessage("130%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.SEXP13)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.SEXP15)) {
            pc.sendPackets(new S_ServerMessage("150%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.SEXP15)));
            return false;
        } else if (pc.hasSkillEffect(L1SkillId.SEXP17)) {
            pc.sendPackets(new S_ServerMessage("170%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.SEXP17)));
            return false;
        } else if (!pc.hasSkillEffect(L1SkillId.SEXP20)) {
            return true;
        } else {
            pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.SEXP20)));
            return false;
        }
    }

    public static boolean cancelExpSkill_3(L1PcInstance pc) {
        if (!pc.hasSkillEffect(L1SkillId.REEXP20)) {
            return true;
        }
        pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + pc.getSkillEffectTimeSec(L1SkillId.REEXP20)));
        return false;
    }

    public static int cancelDragon(L1PcInstance pc) {
        if (pc.hasSkillEffect(L1SkillId.DRAGON1)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON1);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON2)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON2);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON3)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON3);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON4)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON4);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON5)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON5);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON6)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON6);
        }
        if (pc.hasSkillEffect(L1SkillId.DRAGON7)) {
            return pc.getSkillEffectTimeSec(L1SkillId.DRAGON7);
        }
        return -1;
    }
}
