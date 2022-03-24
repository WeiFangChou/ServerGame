package com.lineage.server.model;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Cooking {
    private static final Log _log = LogFactory.getLog(L1Cooking.class);

    private L1Cooking() {
    }

    public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) throws Exception {
        int cookingId;
        int cookingId2;
        int cookingId3;
        int cookingId4;
        int cookingId5;
        int cookingId6;
        int cookingId7;
        int cookingId8;
        int cookingId9;
        int cookingId10;
        int cookingId11;
        int cookingId12;
        int cookingId13;
        int cookingId14;
        int cookingId15;
        int cookingId16;
        int cookingId17;
        int cookingId18;
        int cookingId19;
        int cookingId20;
        int cookingId21;
        int cookingId22;
        int cookingId23;
        int cookingId24;
        int dessertId;
        int cookingId25;
        int itemId = item.getItem().getItemId();
        switch (itemId) {
            case 41284:
            case 41292:
            case 49056:
            case 49064:
            case 49251:
            case 49259:
                if (pc.get_food() != 225) {
                    pc.sendPackets(new S_ServerMessage(74, item.getNumberedName(1, true)));
                    return;
                }
                break;
        }
        if (((itemId >= 41277 && itemId <= 41283) || ((itemId >= 41285 && itemId <= 41291) || ((itemId >= 49049 && itemId <= 49055) || ((itemId >= 49057 && itemId <= 49063) || ((itemId >= 49244 && itemId <= 49250) || (itemId >= 49252 && itemId <= 49258)))))) && (cookingId25 = pc.getCookingId()) != 0) {
            pc.removeSkillEffect(cookingId25);
        }
        if ((itemId == 41284 || itemId == 41292 || itemId == 49056 || itemId == 49064 || itemId == 49251 || itemId == 49259) && (dessertId = pc.getDessertId()) != 0) {
            pc.removeSkillEffect(dessertId);
        }
        if (itemId == 41277 || itemId == 41285) {
            if (itemId == 41277) {
                cookingId = 3000;
            } else {
                cookingId = L1SkillId.COOKING_1_0_S;
            }
            eatCooking(pc, cookingId, 900);
        } else if (itemId == 41278 || itemId == 41286) {
            if (itemId == 41278) {
                cookingId2 = L1SkillId.COOKING_1_1_N;
            } else {
                cookingId2 = L1SkillId.COOKING_1_1_S;
            }
            eatCooking(pc, cookingId2, 900);
        } else if (itemId == 41279 || itemId == 41287) {
            if (itemId == 41279) {
                cookingId3 = L1SkillId.COOKING_1_2_N;
            } else {
                cookingId3 = L1SkillId.COOKING_1_2_S;
            }
            eatCooking(pc, cookingId3, 900);
        } else if (itemId == 41280 || itemId == 41288) {
            if (itemId == 41280) {
                cookingId4 = L1SkillId.COOKING_1_3_N;
            } else {
                cookingId4 = L1SkillId.COOKING_1_3_S;
            }
            eatCooking(pc, cookingId4, 900);
        } else if (itemId == 41281 || itemId == 41289) {
            if (itemId == 41281) {
                cookingId5 = L1SkillId.COOKING_1_4_N;
            } else {
                cookingId5 = L1SkillId.COOKING_1_4_S;
            }
            eatCooking(pc, cookingId5, 900);
        } else if (itemId == 41282 || itemId == 41290) {
            if (itemId == 41282) {
                cookingId6 = L1SkillId.COOKING_1_5_N;
            } else {
                cookingId6 = L1SkillId.COOKING_1_5_S;
            }
            eatCooking(pc, cookingId6, 900);
        } else if (itemId == 41283 || itemId == 41291) {
            if (itemId == 41283) {
                cookingId7 = L1SkillId.COOKING_1_6_N;
            } else {
                cookingId7 = L1SkillId.COOKING_1_6_S;
            }
            eatCooking(pc, cookingId7, 900);
        } else if (itemId == 41284 || itemId == 41292) {
            if (itemId == 41284) {
                cookingId8 = L1SkillId.COOKING_1_7_N;
            } else {
                cookingId8 = L1SkillId.COOKING_1_7_S;
            }
            eatCooking(pc, cookingId8, 900);
        } else if (itemId == 49049 || itemId == 49057) {
            if (itemId == 49049) {
                cookingId9 = L1SkillId.COOKING_2_0_N;
            } else {
                cookingId9 = L1SkillId.COOKING_2_0_S;
            }
            eatCooking(pc, cookingId9, 900);
        } else if (itemId == 49050 || itemId == 49058) {
            if (itemId == 49050) {
                cookingId10 = L1SkillId.COOKING_2_1_N;
            } else {
                cookingId10 = L1SkillId.COOKING_2_1_S;
            }
            eatCooking(pc, cookingId10, 900);
        } else if (itemId == 49051 || itemId == 49059) {
            if (itemId == 49051) {
                cookingId11 = L1SkillId.COOKING_2_2_N;
            } else {
                cookingId11 = L1SkillId.COOKING_2_2_S;
            }
            eatCooking(pc, cookingId11, 900);
        } else if (itemId == 49052 || itemId == 49060) {
            if (itemId == 49052) {
                cookingId12 = L1SkillId.COOKING_2_3_N;
            } else {
                cookingId12 = L1SkillId.COOKING_2_3_S;
            }
            eatCooking(pc, cookingId12, 900);
        } else if (itemId == 49053 || itemId == 49061) {
            if (itemId == 49053) {
                cookingId13 = L1SkillId.COOKING_2_4_N;
            } else {
                cookingId13 = L1SkillId.COOKING_2_4_S;
            }
            eatCooking(pc, cookingId13, 900);
        } else if (itemId == 49054 || itemId == 49062) {
            if (itemId == 49054) {
                cookingId14 = L1SkillId.COOKING_2_5_N;
            } else {
                cookingId14 = L1SkillId.COOKING_2_5_S;
            }
            eatCooking(pc, cookingId14, 900);
        } else if (itemId == 49055 || itemId == 49063) {
            if (itemId == 49055) {
                cookingId15 = L1SkillId.COOKING_2_6_N;
            } else {
                cookingId15 = L1SkillId.COOKING_2_6_S;
            }
            eatCooking(pc, cookingId15, 900);
        } else if (itemId == 49056 || itemId == 49064) {
            if (itemId == 49056) {
                cookingId16 = L1SkillId.COOKING_2_7_N;
            } else {
                cookingId16 = L1SkillId.COOKING_2_7_S;
            }
            eatCooking(pc, cookingId16, 900);
        } else if (itemId == 49244 || itemId == 49252) {
            if (itemId == 49244) {
                cookingId17 = L1SkillId.COOKING_3_0_N;
            } else {
                cookingId17 = L1SkillId.COOKING_3_0_S;
            }
            eatCooking(pc, cookingId17, 900);
        } else if (itemId == 49245 || itemId == 49253) {
            if (itemId == 49245) {
                cookingId18 = L1SkillId.COOKING_3_1_N;
            } else {
                cookingId18 = L1SkillId.COOKING_3_1_S;
            }
            eatCooking(pc, cookingId18, 900);
        } else if (itemId == 49246 || itemId == 49254) {
            if (itemId == 49246) {
                cookingId19 = L1SkillId.COOKING_3_2_N;
            } else {
                cookingId19 = L1SkillId.COOKING_3_2_S;
            }
            eatCooking(pc, cookingId19, 900);
        } else if (itemId == 49247 || itemId == 49255) {
            if (itemId == 49247) {
                cookingId20 = L1SkillId.COOKING_3_3_N;
            } else {
                cookingId20 = L1SkillId.COOKING_3_3_S;
            }
            eatCooking(pc, cookingId20, 900);
        } else if (itemId == 49248 || itemId == 49256) {
            if (itemId == 49248) {
                cookingId21 = L1SkillId.COOKING_3_4_N;
            } else {
                cookingId21 = L1SkillId.COOKING_3_4_S;
            }
            eatCooking(pc, cookingId21, 900);
        } else if (itemId == 49249 || itemId == 49257) {
            if (itemId == 49249) {
                cookingId22 = L1SkillId.COOKING_3_5_N;
            } else {
                cookingId22 = L1SkillId.COOKING_3_5_S;
            }
            eatCooking(pc, cookingId22, 900);
        } else if (itemId == 49250 || itemId == 49258) {
            if (itemId == 49250) {
                cookingId23 = L1SkillId.COOKING_3_6_N;
            } else {
                cookingId23 = L1SkillId.COOKING_3_6_S;
            }
            eatCooking(pc, cookingId23, 900);
        } else if (itemId == 49251 || itemId == 49259) {
            if (itemId == 49251) {
                cookingId24 = L1SkillId.COOKING_3_7_N;
            } else {
                cookingId24 = 3047;
            }
            eatCooking(pc, cookingId24, 900);
        }
        pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1, true)));
        pc.getInventory().removeItem(item, 1);
    }

    public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
        int cookingType = 0;
        switch (cookingId) {
            case 3000:
            case L1SkillId.COOKING_1_0_S /*{ENCODED_INT: 3008}*/:
                cookingType = 0;
                pc.addWind(10);
                pc.addWater(10);
                pc.addFire(10);
                pc.addEarth(10);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                break;
            case L1SkillId.COOKING_1_1_N /*{ENCODED_INT: 3001}*/:
            case L1SkillId.COOKING_1_1_S /*{ENCODED_INT: 3009}*/:
                cookingType = 1;
                pc.addMaxHp(30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) {
                    pc.getParty().updateMiniHP(pc);
                    break;
                }
                break;
            case L1SkillId.COOKING_1_2_N /*{ENCODED_INT: 3002}*/:
            case L1SkillId.COOKING_1_2_S /*{ENCODED_INT: 3010}*/:
                cookingType = 2;
                break;
            case L1SkillId.COOKING_1_3_N /*{ENCODED_INT: 3003}*/:
            case L1SkillId.COOKING_1_3_S /*{ENCODED_INT: 3011}*/:
                cookingType = 3;
                pc.addAc(-1);
                pc.sendPackets(new S_OwnCharStatus(pc));
                break;
            case L1SkillId.COOKING_1_4_N /*{ENCODED_INT: 3004}*/:
            case L1SkillId.COOKING_1_4_S /*{ENCODED_INT: 3012}*/:
                cookingType = 4;
                pc.addMaxMp(20);
                pc.sendPackets(new S_MPUpdate(pc));
                break;
            case L1SkillId.COOKING_1_5_N /*{ENCODED_INT: 3005}*/:
            case L1SkillId.COOKING_1_5_S /*{ENCODED_INT: 3013}*/:
                cookingType = 5;
                break;
            case L1SkillId.COOKING_1_6_N /*{ENCODED_INT: 3006}*/:
            case L1SkillId.COOKING_1_6_S /*{ENCODED_INT: 3014}*/:
                cookingType = 6;
                pc.addMr(5);
                pc.sendPackets(new S_SPMR(pc));
                break;
            case L1SkillId.COOKING_1_7_N /*{ENCODED_INT: 3007}*/:
            case L1SkillId.COOKING_1_7_S /*{ENCODED_INT: 3015}*/:
                cookingType = 7;
                break;
            case L1SkillId.COOKING_2_0_N /*{ENCODED_INT: 3016}*/:
            case L1SkillId.COOKING_2_0_S /*{ENCODED_INT: 3024}*/:
                cookingType = 8;
                break;
            case L1SkillId.COOKING_2_1_N /*{ENCODED_INT: 3017}*/:
            case L1SkillId.COOKING_2_1_S /*{ENCODED_INT: 3025}*/:
                cookingType = 9;
                pc.addMaxHp(30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) {
                    pc.getParty().updateMiniHP(pc);
                }
                pc.addMaxMp(30);
                pc.sendPackets(new S_MPUpdate(pc));
                break;
            case L1SkillId.COOKING_2_2_N /*{ENCODED_INT: 3018}*/:
            case L1SkillId.COOKING_2_2_S /*{ENCODED_INT: 3026}*/:
                cookingType = 10;
                pc.addAc(-2);
                pc.sendPackets(new S_OwnCharStatus(pc));
                break;
            case L1SkillId.COOKING_2_3_N /*{ENCODED_INT: 3019}*/:
            case L1SkillId.COOKING_2_3_S /*{ENCODED_INT: 3027}*/:
                cookingType = 11;
                break;
            case L1SkillId.COOKING_2_4_N /*{ENCODED_INT: 3020}*/:
            case L1SkillId.COOKING_2_4_S /*{ENCODED_INT: 3028}*/:
                cookingType = 12;
                break;
            case L1SkillId.COOKING_2_5_N /*{ENCODED_INT: 3021}*/:
            case L1SkillId.COOKING_2_5_S /*{ENCODED_INT: 3029}*/:
                cookingType = 13;
                pc.addMr(10);
                pc.sendPackets(new S_SPMR(pc));
                break;
            case L1SkillId.COOKING_2_6_N /*{ENCODED_INT: 3022}*/:
            case L1SkillId.COOKING_2_6_S /*{ENCODED_INT: 3030}*/:
                cookingType = 14;
                pc.addSp(1);
                pc.sendPackets(new S_SPMR(pc));
                break;
            case L1SkillId.COOKING_2_7_N /*{ENCODED_INT: 3023}*/:
            case L1SkillId.COOKING_2_7_S /*{ENCODED_INT: 3031}*/:
                cookingType = 15;
                break;
            case L1SkillId.COOKING_3_0_N /*{ENCODED_INT: 3032}*/:
            case L1SkillId.COOKING_3_0_S /*{ENCODED_INT: 3040}*/:
                cookingType = 16;
                break;
            case L1SkillId.COOKING_3_1_N /*{ENCODED_INT: 3033}*/:
            case L1SkillId.COOKING_3_1_S /*{ENCODED_INT: 3041}*/:
                cookingType = 17;
                pc.addMaxHp(50);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) {
                    pc.getParty().updateMiniHP(pc);
                }
                pc.addMaxMp(50);
                pc.sendPackets(new S_MPUpdate(pc));
                break;
            case L1SkillId.COOKING_3_2_N /*{ENCODED_INT: 3034}*/:
            case L1SkillId.COOKING_3_2_S /*{ENCODED_INT: 3042}*/:
                cookingType = 18;
                break;
            case L1SkillId.COOKING_3_3_N /*{ENCODED_INT: 3035}*/:
            case L1SkillId.COOKING_3_3_S /*{ENCODED_INT: 3043}*/:
                cookingType = 19;
                pc.addAc(-3);
                pc.sendPackets(new S_OwnCharStatus(pc));
                break;
            case L1SkillId.COOKING_3_4_N /*{ENCODED_INT: 3036}*/:
            case L1SkillId.COOKING_3_4_S /*{ENCODED_INT: 3044}*/:
                cookingType = 20;
                pc.addMr(15);
                pc.sendPackets(new S_SPMR(pc));
                pc.addWind(10);
                pc.addWater(10);
                pc.addFire(10);
                pc.addEarth(10);
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                break;
            case L1SkillId.COOKING_3_5_N /*{ENCODED_INT: 3037}*/:
            case L1SkillId.COOKING_3_5_S /*{ENCODED_INT: 3045}*/:
                cookingType = 21;
                pc.addSp(2);
                pc.sendPackets(new S_SPMR(pc));
                break;
            case L1SkillId.COOKING_3_6_N /*{ENCODED_INT: 3038}*/:
            case L1SkillId.COOKING_3_6_S /*{ENCODED_INT: 3046}*/:
                cookingType = 22;
                pc.addMaxHp(30);
                pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
                if (pc.isInParty()) {
                    pc.getParty().updateMiniHP(pc);
                    break;
                }
                break;
            case L1SkillId.COOKING_3_7_N /*{ENCODED_INT: 3039}*/:
            case 3047:
                cookingType = 23;
                break;
        }
        try {
            pc.sendPackets(new S_PacketBoxCooking(pc, cookingType, time));
            pc.setSkillEffect(cookingId, time * L1SkillId.STATUS_BRAVE);
            if ((cookingId >= 3000 && cookingId <= 3006) || ((cookingId >= 3008 && cookingId <= 3014) || ((cookingId >= 3016 && cookingId <= 3022) || ((cookingId >= 3024 && cookingId <= 3030) || ((cookingId >= 3032 && cookingId <= 3038) || (cookingId >= 3040 && cookingId <= 3046)))))) {
                pc.setCookingId(cookingId);
            } else if (cookingId == 3007 || cookingId == 3015 || cookingId == 3023 || cookingId == 3031 || cookingId == 3039 || cookingId == 3047) {
                pc.setDessertId(cookingId);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
