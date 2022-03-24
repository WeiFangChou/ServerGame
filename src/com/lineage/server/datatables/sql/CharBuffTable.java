package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharBuffStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Cooking;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_PacketBoxThirdSpeed;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.templates.L1BuffTmp;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharBuffTable implements CharBuffStorage {
    private static final Map<Integer, ArrayList<L1BuffTmp>> _buffMap = new HashMap();
    private static final int[] _buffSkill = {2, 67, 43, 54, 3, 99, 151, 159, L1SkillId.IRON_SKIN, L1SkillId.STATUS_BRAVE, L1SkillId.STATUS_HASTE, L1SkillId.STATUS_ELFBRAVE, 52, 101, 150, 26, 42, 109, 110, L1SkillId.GLOWING_AURA, 115, 117, 148, 155, L1SkillId.BURNING_WEAPON, 149, L1SkillId.STORM_EYE, 166, L1SkillId.STATUS_BLUE_POTION, L1SkillId.STATUS_CHAT_PROHIBITED, L1SkillId.MAZU_SKILL, 3000, L1SkillId.COOKING_1_0_S, L1SkillId.COOKING_1_1_N, L1SkillId.COOKING_1_1_S, L1SkillId.COOKING_1_2_N, L1SkillId.COOKING_1_2_S, L1SkillId.COOKING_1_3_N, L1SkillId.COOKING_1_3_S, L1SkillId.COOKING_1_4_N, L1SkillId.COOKING_1_4_S, L1SkillId.COOKING_1_5_N, L1SkillId.COOKING_1_5_S, L1SkillId.COOKING_1_6_N, L1SkillId.COOKING_1_6_S, L1SkillId.COOKING_2_0_N, L1SkillId.COOKING_2_0_S, L1SkillId.COOKING_2_1_N, L1SkillId.COOKING_2_1_S, L1SkillId.COOKING_2_2_N, L1SkillId.COOKING_2_2_S, L1SkillId.COOKING_2_3_N, L1SkillId.COOKING_2_3_S, L1SkillId.COOKING_2_4_N, L1SkillId.COOKING_2_4_S, L1SkillId.COOKING_2_5_N, L1SkillId.COOKING_2_5_S, L1SkillId.COOKING_2_6_N, L1SkillId.COOKING_2_6_S, L1SkillId.COOKING_3_0_N, L1SkillId.COOKING_3_0_S, L1SkillId.COOKING_3_1_N, L1SkillId.COOKING_3_1_S, L1SkillId.COOKING_3_2_N, L1SkillId.COOKING_3_2_S, L1SkillId.COOKING_3_3_N, L1SkillId.COOKING_3_3_S, L1SkillId.COOKING_3_4_N, L1SkillId.COOKING_3_4_S, L1SkillId.COOKING_3_5_N, L1SkillId.COOKING_3_5_S, L1SkillId.COOKING_3_6_N, L1SkillId.COOKING_3_6_S, 4010, L1SkillId.EXP15, L1SkillId.EXP17, L1SkillId.EXP20, L1SkillId.EXP25, 6671, L1SkillId.EXP35, L1SkillId.EXP40, L1SkillId.EXP45, L1SkillId.EXP50, L1SkillId.EXP55, L1SkillId.EXP60, L1SkillId.EXP65, L1SkillId.EXP70, L1SkillId.EXP75, L1SkillId.EXP80, L1SkillId.SEXP13, L1SkillId.SEXP15, L1SkillId.SEXP17, L1SkillId.SEXP20, L1SkillId.REEXP20, 998};
    private static final Log _log = LogFactory.getLog(CharBuffTable.class);

    @Override // com.lineage.server.datatables.storage.CharBuffStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_buff`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int char_obj_id = rs.getInt("char_obj_id");
                if (CharObjidTable.get().isChar(char_obj_id) != null) {
                    int skill_id = rs.getInt("skill_id");
                    int remaining_time = rs.getInt("remaining_time");
                    int poly_id = rs.getInt("poly_id");
                    L1BuffTmp buffTmp = new L1BuffTmp();
                    buffTmp.set_char_obj_id(char_obj_id);
                    buffTmp.set_skill_id(skill_id);
                    buffTmp.set_remaining_time(remaining_time);
                    buffTmp.set_poly_id(poly_id);
                    addMap(char_obj_id, buffTmp);
                } else {
                    delete(char_obj_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入保留技能紀錄資料數量: " + _buffMap.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        ArrayList<L1BuffTmp> list = _buffMap.get(Integer.valueOf(objid));
        if (list != null) {
            list.clear();
        }
        _buffMap.remove(Integer.valueOf(objid));
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_buff` WHERE `char_obj_id`=?");
            ps.setInt(1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addMap(int objId, L1BuffTmp buffTmp) {
        ArrayList<L1BuffTmp> list = _buffMap.get(Integer.valueOf(objId));
        if (list == null) {
            ArrayList<L1BuffTmp> newlist = new ArrayList<>();
            newlist.add(buffTmp);
            _buffMap.put(Integer.valueOf(objId), newlist);
            return;
        }
        list.add(buffTmp);
    }

    @Override // com.lineage.server.datatables.storage.CharBuffStorage
    public void saveBuff(L1PcInstance pc) {
        int[] iArr = _buffSkill;
        for (int skillId : iArr) {
            int timeSec = pc.getSkillEffectTimeSec(skillId);
            if (timeSec > 0) {
                int polyId = -1;
                if (skillId == 67) {
                    polyId = pc.getTempCharGfx();
                }
                storeBuff(pc.getId(), skillId, timeSec, polyId);
            }
        }
        pc.clearSkillEffectTimer();
    }

    private void storeBuff(int objId, int skillId, int time, int polyId) {
        L1BuffTmp buffTmp = new L1BuffTmp();
        buffTmp.set_char_obj_id(objId);
        buffTmp.set_skill_id(skillId);
        buffTmp.set_remaining_time(time);
        buffTmp.set_poly_id(polyId);
        addMap(objId, buffTmp);
        storeBuffR(buffTmp);
    }

    @Override // com.lineage.server.datatables.storage.CharBuffStorage
    public void buff(L1PcInstance pc) {
        ArrayList<L1BuffTmp> list = _buffMap.get(Integer.valueOf(pc.getId()));
        if (list != null) {
            Iterator<L1BuffTmp> it = list.iterator();
            while (it.hasNext()) {
                L1BuffTmp buffTmp = it.next();
                int skill_id = buffTmp.get_skill_id();
                int remaining_time = buffTmp.get_remaining_time();
                int poly_id = buffTmp.get_poly_id();
                if (remaining_time > 0) {
                    if (poly_id == -1) {
                        switch (skill_id) {
                            case 998:
                                pc.sendPackets(new S_PacketBoxThirdSpeed(remaining_time));
                                pc.sendPacketsAll(new S_Liquor(pc.getId(), 8));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.STATUS_BRAVE /*{ENCODED_INT: 1000}*/:
                                pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time));
                                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 1, 0));
                                pc.setBraveSpeed(1);
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.STATUS_HASTE /*{ENCODED_INT: 1001}*/:
                                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remaining_time));
                                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                                pc.setMoveSpeed(1);
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.STATUS_BLUE_POTION /*{ENCODED_INT: 1002}*/:
                                pc.sendPackets(new S_PacketBox(34, remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.STATUS_ELFBRAVE /*{ENCODED_INT: 1016}*/:
                                pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time));
                                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 3, 0));
                                pc.setBraveSpeed(1);
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case 3000:
                            case L1SkillId.COOKING_1_1_N /*{ENCODED_INT: 3001}*/:
                            case L1SkillId.COOKING_1_2_N /*{ENCODED_INT: 3002}*/:
                            case L1SkillId.COOKING_1_3_N /*{ENCODED_INT: 3003}*/:
                            case L1SkillId.COOKING_1_4_N /*{ENCODED_INT: 3004}*/:
                            case L1SkillId.COOKING_1_5_N /*{ENCODED_INT: 3005}*/:
                            case L1SkillId.COOKING_1_6_N /*{ENCODED_INT: 3006}*/:
                            case L1SkillId.COOKING_1_7_N /*{ENCODED_INT: 3007}*/:
                            case L1SkillId.COOKING_1_0_S /*{ENCODED_INT: 3008}*/:
                            case L1SkillId.COOKING_1_1_S /*{ENCODED_INT: 3009}*/:
                            case L1SkillId.COOKING_1_2_S /*{ENCODED_INT: 3010}*/:
                            case L1SkillId.COOKING_1_3_S /*{ENCODED_INT: 3011}*/:
                            case L1SkillId.COOKING_1_4_S /*{ENCODED_INT: 3012}*/:
                            case L1SkillId.COOKING_1_5_S /*{ENCODED_INT: 3013}*/:
                            case L1SkillId.COOKING_1_6_S /*{ENCODED_INT: 3014}*/:
                            case L1SkillId.COOKING_1_7_S /*{ENCODED_INT: 3015}*/:
                            case L1SkillId.COOKING_2_0_N /*{ENCODED_INT: 3016}*/:
                            case L1SkillId.COOKING_2_1_N /*{ENCODED_INT: 3017}*/:
                            case L1SkillId.COOKING_2_2_N /*{ENCODED_INT: 3018}*/:
                            case L1SkillId.COOKING_2_3_N /*{ENCODED_INT: 3019}*/:
                            case L1SkillId.COOKING_2_4_N /*{ENCODED_INT: 3020}*/:
                            case L1SkillId.COOKING_2_5_N /*{ENCODED_INT: 3021}*/:
                            case L1SkillId.COOKING_2_6_N /*{ENCODED_INT: 3022}*/:
                            case L1SkillId.COOKING_2_7_N /*{ENCODED_INT: 3023}*/:
                            case L1SkillId.COOKING_2_0_S /*{ENCODED_INT: 3024}*/:
                            case L1SkillId.COOKING_2_1_S /*{ENCODED_INT: 3025}*/:
                            case L1SkillId.COOKING_2_2_S /*{ENCODED_INT: 3026}*/:
                            case L1SkillId.COOKING_2_3_S /*{ENCODED_INT: 3027}*/:
                            case L1SkillId.COOKING_2_4_S /*{ENCODED_INT: 3028}*/:
                            case L1SkillId.COOKING_2_5_S /*{ENCODED_INT: 3029}*/:
                            case L1SkillId.COOKING_2_6_S /*{ENCODED_INT: 3030}*/:
                            case L1SkillId.COOKING_2_7_S /*{ENCODED_INT: 3031}*/:
                            case L1SkillId.COOKING_3_0_N /*{ENCODED_INT: 3032}*/:
                            case L1SkillId.COOKING_3_1_N /*{ENCODED_INT: 3033}*/:
                            case L1SkillId.COOKING_3_2_N /*{ENCODED_INT: 3034}*/:
                            case L1SkillId.COOKING_3_3_N /*{ENCODED_INT: 3035}*/:
                            case L1SkillId.COOKING_3_4_N /*{ENCODED_INT: 3036}*/:
                            case L1SkillId.COOKING_3_5_N /*{ENCODED_INT: 3037}*/:
                            case L1SkillId.COOKING_3_6_N /*{ENCODED_INT: 3038}*/:
                            case L1SkillId.COOKING_3_7_N /*{ENCODED_INT: 3039}*/:
                            case L1SkillId.COOKING_3_0_S /*{ENCODED_INT: 3040}*/:
                            case L1SkillId.COOKING_3_1_S /*{ENCODED_INT: 3041}*/:
                            case L1SkillId.COOKING_3_2_S /*{ENCODED_INT: 3042}*/:
                            case L1SkillId.COOKING_3_3_S /*{ENCODED_INT: 3043}*/:
                            case L1SkillId.COOKING_3_4_S /*{ENCODED_INT: 3044}*/:
                            case L1SkillId.COOKING_3_5_S /*{ENCODED_INT: 3045}*/:
                            case L1SkillId.COOKING_3_6_S /*{ENCODED_INT: 3046}*/:
                            case 3047:
                                L1Cooking.eatCooking(pc, skill_id, remaining_time);
                                continue;
                            case L1SkillId.STATUS_CHAT_PROHIBITED /*{ENCODED_INT: 4002}*/:
                                pc.sendPackets(new S_PacketBox(36, remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case 4010:
                                pc.sendPackets(new S_ServerMessage("130%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.SEXP11 /*{ENCODED_INT: 5000}*/:
                                pc.sendPackets(new S_ServerMessage("110%倍經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.SEXP20 /*{ENCODED_INT: 5001}*/:
                                pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.SEXP13 /*{ENCODED_INT: 5002}*/:
                                pc.sendPackets(new S_ServerMessage("130%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.SEXP15 /*{ENCODED_INT: 5003}*/:
                                pc.sendPackets(new S_ServerMessage("150%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.SEXP17 /*{ENCODED_INT: 5004}*/:
                                pc.sendPackets(new S_ServerMessage("170%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.REEXP20 /*{ENCODED_INT: 5005}*/:
                                pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case L1SkillId.EXP20 /*{ENCODED_INT: 6666}*/:
                                pc.sendPackets(new S_ServerMessage("200%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP25 /*{ENCODED_INT: 6667}*/:
                                pc.sendPackets(new S_ServerMessage("250%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP15 /*{ENCODED_INT: 6669}*/:
                                pc.sendPackets(new S_ServerMessage("150%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP17 /*{ENCODED_INT: 6670}*/:
                                pc.sendPackets(new S_ServerMessage("170%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            case 6671:
                                pc.sendPackets(new S_ServerMessage("300%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP35 /*{ENCODED_INT: 6672}*/:
                                pc.sendPackets(new S_ServerMessage("350%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP40 /*{ENCODED_INT: 6673}*/:
                                pc.sendPackets(new S_ServerMessage("400%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP45 /*{ENCODED_INT: 6674}*/:
                                pc.sendPackets(new S_ServerMessage("450%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP50 /*{ENCODED_INT: 6675}*/:
                                pc.sendPackets(new S_ServerMessage("500%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP55 /*{ENCODED_INT: 6676}*/:
                                pc.sendPackets(new S_ServerMessage("550%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP60 /*{ENCODED_INT: 6677}*/:
                                pc.sendPackets(new S_ServerMessage("600%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP65 /*{ENCODED_INT: 6678}*/:
                                pc.sendPackets(new S_ServerMessage("650%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP70 /*{ENCODED_INT: 6679}*/:
                                pc.sendPackets(new S_ServerMessage("700%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP75 /*{ENCODED_INT: 6680}*/:
                                pc.sendPackets(new S_ServerMessage("750%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.EXP80 /*{ENCODED_INT: 6681}*/:
                                pc.sendPackets(new S_ServerMessage("800%經驗 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                pc.sendPackets(new S_PacketBoxCooking(pc, 32, remaining_time));
                                continue;
                            case L1SkillId.MAZU_SKILL /*{ENCODED_INT: 9621}*/:
                                pc.sendPackets(new S_ServerMessage("媽祖的祝福 剩餘時間(秒):" + remaining_time));
                                pc.setSkillEffect(skill_id, remaining_time * L1SkillId.STATUS_BRAVE);
                                continue;
                            default:
                                SkillMode mode = L1SkillMode.get().getSkill(skill_id);
                                if (mode == null) {
                                    new L1SkillUse().handleCommands(pc, skill_id, pc.getId(), pc.getX(), pc.getY(), remaining_time, 1);
                                    break;
                                } else {
                                    try {
                                        mode.start(pc, pc, (L1Magic) null, remaining_time);
                                        continue;
                                    } catch (Exception e) {
                                        _log.error(e.getLocalizedMessage(), e);
                                        break;
                                    }
                                }
                        }
                    } else {
                        L1PolyMorph.doPoly(pc, poly_id, remaining_time, 0);
                    }
                }
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.CharBuffStorage
    public void deleteBuff(L1PcInstance pc) {
        delete(pc.getId());
    }

    @Override // com.lineage.server.datatables.storage.CharBuffStorage
    public void deleteBuff(int objid) {
        delete(objid);
    }

    private static void storeBuffR(L1BuffTmp buffTmp) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `character_buff` SET `char_obj_id`=?,`skill_id`=?,`remaining_time`=?,`poly_id`=?");
            ps.setInt(1, buffTmp.get_char_obj_id());
            ps.setInt(2, buffTmp.get_skill_id());
            ps.setInt(3, buffTmp.get_remaining_time());
            ps.setInt(4, buffTmp.get_poly_id());
            ps.execute();
        } catch (SQLException ignored) {
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
