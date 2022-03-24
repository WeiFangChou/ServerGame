package com.lineage.server.model.skill;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxCooking;
import com.lineage.server.serverpackets.S_PacketBoxIconAura;
import com.lineage.server.serverpackets.S_PacketBoxWaterLife;
import com.lineage.server.serverpackets.S_PacketBoxWisdomPotion;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillIconBlessOfEva;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.types.Point;
import com.lineage.server.utils.RandomArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1WilliamSystemMessage;
import william.Plug_Question;

public class L1SkillStop {
    private static final Log _log = LogFactory.getLog(L1SkillStop.class);

    public static void stopSkill(L1Character cha, int skillId) {
        try {
            SkillMode mode = L1SkillMode.get().getSkill(skillId);
            if (mode == null) {
                switch (skillId) {
                    case 2:
                        if ((cha instanceof L1PcInstance) && !cha.isInvisble()) {
                            ((L1PcInstance) cha).turnOnOffLight();
                            break;
                        }
                    case 3:
                        cha.addAc(2);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_SkillIconShield(5, 0));
                            break;
                        }
                        break;
                    case 12:
                        cha.addDmgup(-2);
                        break;
                    case 21:
                        cha.addAc(3);
                        break;
                    case 26:
                        cha.addDex(-5);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc = (L1PcInstance) cha;
                            pc.sendPackets(new S_Dexup(pc, 5, 0));
                            break;
                        }
                        break;
                    case 29:
                    case 76:
                    case L1SkillId.ENTANGLE:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc2 = (L1PcInstance) cha;
                            pc2.sendPacketsAll(new S_SkillHaste(pc2.getId(), 0, 0));
                        }
                        cha.setMoveSpeed(0);
                        break;
                    case 42:
                        cha.addStr(-5);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc3 = (L1PcInstance) cha;
                            pc3.sendPackets(new S_Strup(pc3, 5, 0));
                            break;
                        }
                        break;
                    case 47:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc4 = (L1PcInstance) cha;
                            pc4.addDmgup(5);
                            pc4.addHitup(1);
                            break;
                        }
                        break;
                    case 48:
                        cha.addDmgup(-2);
                        cha.addHitup(-2);
                        cha.addBowHitup(-2);
                        break;
                    case 50:
                    case L1SkillId.FREEZING_BLIZZARD:
                    case 194:
                        if (!(cha instanceof L1PcInstance)) {
                            if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
                                L1NpcInstance npc = (L1NpcInstance) cha;
                                npc.broadcastPacketAll(new S_Poison(npc.getId(), 0));
                                npc.setParalyzed(false);
                                break;
                            }
                        } else {
                            L1PcInstance pc5 = (L1PcInstance) cha;
                            pc5.sendPacketsAll(new S_Poison(pc5.getId(), 0));
                            pc5.sendPackets(new S_Paralysis(4, false));
                            break;
                        }
                    case 52:
                    case 101:
                    case 150:
                        cha.setBraveSpeed(0);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc6 = (L1PcInstance) cha;
                            pc6.sendPacketsAll(new S_SkillBrave(pc6.getId(), 0, 0));
                            break;
                        }
                        break;
                    case 54:
                        cha.setMoveSpeed(0);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc7 = (L1PcInstance) cha;
                            pc7.sendPacketsAll(new S_SkillHaste(pc7.getId(), 0, 0));
                            break;
                        }
                        break;
                    case 55:
                        cha.addAc(-10);
                        cha.addDmgup(-5);
                        cha.addHitup(-2);
                        break;
                    case 56:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc8 = (L1PcInstance) cha;
                            pc8.addDmgup(6);
                            pc8.addAc(-12);
                            break;
                        }
                        break;
                    case 66:
                        cha.setSleeped(false);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc9 = (L1PcInstance) cha;
                            pc9.sendPackets(new S_Paralysis(3, false));
                            pc9.sendPackets(new S_OwnCharStatus(pc9));
                            break;
                        }
                        break;
                    case 67:
                        L1PolyMorph.undoPoly(cha);
                        break;
                    case L1SkillId.ABSOLUTE_BARRIER:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc10 = (L1PcInstance) cha;
                            pc10.startHpRegeneration();
                            pc10.startMpRegeneration();
                            break;
                        }
                        break;
                    case 97:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).delBlindHiding();
                            break;
                        }
                        break;
                    case 99:
                        cha.addMr(-5);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc11 = (L1PcInstance) cha;
                            pc11.sendPackets(new S_SPMR(pc11));
                            pc11.sendPackets(new S_SkillIconShield(3, 0));
                            break;
                        }
                        break;
                    case 107:
                        cha.addDmgup(-5);
                        break;
                    case 109:
                        cha.addStr(-2);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc12 = (L1PcInstance) cha;
                            pc12.sendPackets(new S_Strup(pc12, 2, 0));
                            break;
                        }
                        break;
                    case 110:
                        cha.addDex(-2);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc13 = (L1PcInstance) cha;
                            pc13.sendPackets(new S_Dexup(pc13, 2, 0));
                            break;
                        }
                        break;
                    case L1SkillId.GLOWING_AURA:
                        cha.addHitup(-5);
                        cha.addBowHitup(-5);
                        cha.addMr(-20);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc14 = (L1PcInstance) cha;
                            pc14.sendPackets(new S_SPMR(pc14));
                            pc14.sendPackets(new S_PacketBoxIconAura(113, 0));
                            break;
                        }
                        break;
                    case 115:
                        cha.addAc(8);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(L1SkillId.GLOWING_AURA, 0));
                            break;
                        }
                        break;
                    case 117:
                        cha.addDmgup(-5);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(116, 0));
                            break;
                        }
                        break;
                    case 129:
                        cha.addMr(-10);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc15 = (L1PcInstance) cha;
                            pc15.sendPackets(new S_SPMR(pc15));
                            break;
                        }
                        break;
                    case 137:
                        cha.addWis(-3);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).resetBaseMr();
                            break;
                        }
                        break;
                    case 138:
                        cha.addWind(-10);
                        cha.addWater(-10);
                        cha.addFire(-10);
                        cha.addEarth(-10);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc16 = (L1PcInstance) cha;
                            pc16.sendPackets(new S_OwnCharAttrDef(pc16));
                            break;
                        }
                        break;
                    case L1SkillId.ELEMENTAL_PROTECTION:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc17 = (L1PcInstance) cha;
                            int attr = pc17.getElfAttr();
                            if (attr == 1) {
                                cha.addEarth(-50);
                            } else if (attr == 2) {
                                cha.addFire(-50);
                            } else if (attr == 4) {
                                cha.addWater(-50);
                            } else if (attr == 8) {
                                cha.addWind(-50);
                            }
                            pc17.sendPackets(new S_OwnCharAttrDef(pc17));
                            break;
                        }
                        break;
                    case 148:
                        cha.addDmgup(-4);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(L1SkillId.ELEMENTAL_PROTECTION, 0));
                            break;
                        }
                        break;
                    case 149:
                        cha.addBowHitup(-6);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(148, 0));
                            break;
                        }
                        break;
                    case 151:
                        cha.addAc(6);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_SkillIconShield(6, 0));
                            break;
                        }
                        break;
                    case 155:
                        cha.addDmgup(-4);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(154, 0));
                            break;
                        }
                        break;
                    case L1SkillId.STORM_EYE:
                        cha.addBowHitup(-2);
                        cha.addBowDmgup(-3);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(155, 0));
                            break;
                        }
                        break;
                    case 157:
                        if (!(cha instanceof L1PcInstance)) {
                            if ((cha instanceof L1MonsterInstance) || (cha instanceof L1SummonInstance) || (cha instanceof L1PetInstance)) {
                                L1NpcInstance npc2 = (L1NpcInstance) cha;
                                npc2.broadcastPacketAll(new S_Poison(npc2.getId(), 0));
                                npc2.setParalyzed(false);
                                break;
                            }
                        } else {
                            L1PcInstance pc18 = (L1PcInstance) cha;
                            pc18.sendPacketsAll(new S_Poison(pc18.getId(), 0));
                            pc18.sendPackets(new S_Paralysis(4, false));
                            break;
                        }
                    case 159:
                        cha.addAc(7);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_SkillIconShield(7, 0));
                            break;
                        }
                        break;
                    case L1SkillId.BURNING_WEAPON:
                        cha.addDmgup(-6);
                        cha.addHitup(-3);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(162, 0));
                            break;
                        }
                        break;
                    case 166:
                        cha.addBowDmgup(-5);
                        cha.addBowHitup(1);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxIconAura(165, 0));
                            break;
                        }
                        break;
                    case L1SkillId.IRON_SKIN:
                        cha.addAc(10);
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_SkillIconShield(10, 0));
                            break;
                        }
                        break;
                    case 170:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxWaterLife());
                            break;
                        }
                        break;
                    case L1SkillId.GUARD_BRAKE:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).addAc(-15);
                            break;
                        }
                        break;
                    case 193:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc19 = (L1PcInstance) cha;
                            pc19.addStr(5);
                            pc19.addInt(5);
                            break;
                        }
                        break;
                    case 204:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc20 = (L1PcInstance) cha;
                            pc20.addDmgup(-4);
                            pc20.addHitup(-4);
                            break;
                        }
                        break;
                    case 209:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc21 = (L1PcInstance) cha;
                            pc21.addSp(-2);
                            pc21.sendPackets(new S_SPMR(pc21));
                            break;
                        }
                        break;
                    case L1SkillId.ILLUSION_DIA_GOLEM:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).addAc(20);
                            break;
                        }
                        break;
                    case 216:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc22 = (L1PcInstance) cha;
                            pc22.addStr(-1);
                            pc22.addCon(-1);
                            pc22.addDex(-1);
                            pc22.addWis(-1);
                            pc22.addInt(-1);
                            break;
                        }
                        break;
                    case 998:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc23 = (L1PcInstance) cha;
                            pc23.sendPacketsAll(new S_Liquor(pc23.getId(), 0));
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_BRAVE:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc24 = (L1PcInstance) cha;
                            pc24.sendPacketsAll(new S_SkillBrave(pc24.getId(), 0, 0));
                        }
                        cha.setBraveSpeed(0);
                        break;
                    case L1SkillId.STATUS_HASTE:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc25 = (L1PcInstance) cha;
                            pc25.sendPacketsAll(new S_SkillHaste(pc25.getId(), 0, 0));
                        }
                        cha.setMoveSpeed(0);
                        break;
                    case L1SkillId.STATUS_UNDERWATER_BREATH:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc26 = (L1PcInstance) cha;
                            pc26.sendPackets(new S_SkillIconBlessOfEva(pc26.getId(), 0));
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_WISDOM_POTION:
                        if (cha instanceof L1PcInstance) {
                            cha.addSp(-2);
                            ((L1PcInstance) cha).sendPackets(new S_PacketBoxWisdomPotion(0));
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_POISON:
                        cha.curePoison();
                        break;
                    case L1SkillId.STATUS_ELFBRAVE:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc27 = (L1PcInstance) cha;
                            pc27.sendPacketsAll(new S_SkillBrave(pc27.getId(), 0, 0));
                        }
                        cha.setBraveSpeed(0);
                        break;
                    case L1SkillId.STATUS_RIBRAVE:
                        if (!(cha instanceof L1PcInstance)) {
                            cha.setBraveSpeed(0);
                            break;
                        } else {
                            L1PcInstance pc28 = (L1PcInstance) cha;
                            pc28.setBraveSpeed(0);
                            pc28.sendPacketsAll(new S_SkillHaste(pc28.getId(), 0, 0));
                            break;
                        }
                    case L1SkillId.STATUS_CUBE_IGNITION_TO_ALLY:
                        cha.addFire(-30);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc29 = (L1PcInstance) cha;
                            pc29.sendPackets(new S_OwnCharAttrDef(pc29));
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_CUBE_QUAKE_TO_ALLY:
                        cha.addEarth(-30);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc30 = (L1PcInstance) cha;
                            pc30.sendPackets(new S_OwnCharAttrDef(pc30));
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_CUBE_SHOCK_TO_ALLY:
                        cha.addWind(-30);
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc31 = (L1PcInstance) cha;
                            pc31.sendPackets(new S_OwnCharAttrDef(pc31));
                            break;
                        }
                        break;
                    case 3000:
                    case L1SkillId.COOKING_1_0_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc32 = (L1PcInstance) cha;
                            pc32.addWind(-10);
                            pc32.addWater(-10);
                            pc32.addFire(-10);
                            pc32.addEarth(-10);
                            pc32.sendPackets(new S_OwnCharAttrDef(pc32));
                            pc32.sendPackets(new S_PacketBoxCooking(pc32, 0, 0));
                            pc32.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_1_N:
                    case L1SkillId.COOKING_1_1_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc33 = (L1PcInstance) cha;
                            pc33.addMaxHp(-30);
                            pc33.sendPackets(new S_HPUpdate(pc33.getCurrentHp(), pc33.getMaxHp()));
                            if (pc33.isInParty()) {
                                pc33.getParty().updateMiniHP(pc33);
                            }
                            pc33.sendPackets(new S_PacketBoxCooking(pc33, 1, 0));
                            pc33.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_2_N:
                    case L1SkillId.COOKING_1_2_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc34 = (L1PcInstance) cha;
                            pc34.sendPackets(new S_PacketBoxCooking(pc34, 2, 0));
                            pc34.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_3_N:
                    case L1SkillId.COOKING_1_3_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc35 = (L1PcInstance) cha;
                            pc35.addAc(1);
                            pc35.sendPackets(new S_PacketBoxCooking(pc35, 3, 0));
                            pc35.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_4_N:
                    case L1SkillId.COOKING_1_4_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc36 = (L1PcInstance) cha;
                            pc36.addMaxMp(-20);
                            pc36.sendPackets(new S_MPUpdate(pc36.getCurrentMp(), pc36.getMaxMp()));
                            pc36.sendPackets(new S_PacketBoxCooking(pc36, 4, 0));
                            pc36.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_5_N:
                    case L1SkillId.COOKING_1_5_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc37 = (L1PcInstance) cha;
                            pc37.sendPackets(new S_PacketBoxCooking(pc37, 5, 0));
                            pc37.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_6_N:
                    case L1SkillId.COOKING_1_6_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc38 = (L1PcInstance) cha;
                            pc38.addMr(-5);
                            pc38.sendPackets(new S_SPMR(pc38));
                            pc38.sendPackets(new S_PacketBoxCooking(pc38, 6, 0));
                            pc38.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_1_7_N:
                    case L1SkillId.COOKING_1_7_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc39 = (L1PcInstance) cha;
                            pc39.sendPackets(new S_PacketBoxCooking(pc39, 7, 0));
                            pc39.setDessertId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_0_N:
                    case L1SkillId.COOKING_2_0_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc40 = (L1PcInstance) cha;
                            pc40.sendPackets(new S_PacketBoxCooking(pc40, 8, 0));
                            pc40.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_1_N:
                    case L1SkillId.COOKING_2_1_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc41 = (L1PcInstance) cha;
                            pc41.addMaxHp(-30);
                            pc41.sendPackets(new S_HPUpdate(pc41.getCurrentHp(), pc41.getMaxHp()));
                            if (pc41.isInParty()) {
                                pc41.getParty().updateMiniHP(pc41);
                            }
                            pc41.addMaxMp(-30);
                            pc41.sendPackets(new S_MPUpdate(pc41.getCurrentMp(), pc41.getMaxMp()));
                            pc41.sendPackets(new S_PacketBoxCooking(pc41, 9, 0));
                            pc41.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_2_N:
                    case L1SkillId.COOKING_2_2_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc42 = (L1PcInstance) cha;
                            pc42.addAc(2);
                            pc42.sendPackets(new S_PacketBoxCooking(pc42, 10, 0));
                            pc42.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_3_N:
                    case L1SkillId.COOKING_2_3_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc43 = (L1PcInstance) cha;
                            pc43.sendPackets(new S_PacketBoxCooking(pc43, 11, 0));
                            pc43.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_4_N:
                    case L1SkillId.COOKING_2_4_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc44 = (L1PcInstance) cha;
                            pc44.sendPackets(new S_PacketBoxCooking(pc44, 12, 0));
                            pc44.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_5_N:
                    case L1SkillId.COOKING_2_5_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc45 = (L1PcInstance) cha;
                            pc45.addMr(-10);
                            pc45.sendPackets(new S_SPMR(pc45));
                            pc45.sendPackets(new S_PacketBoxCooking(pc45, 13, 0));
                            pc45.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_6_N:
                    case L1SkillId.COOKING_2_6_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc46 = (L1PcInstance) cha;
                            pc46.addSp(-1);
                            pc46.sendPackets(new S_SPMR(pc46));
                            pc46.sendPackets(new S_PacketBoxCooking(pc46, 14, 0));
                            pc46.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_2_7_N:
                    case L1SkillId.COOKING_2_7_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc47 = (L1PcInstance) cha;
                            pc47.sendPackets(new S_PacketBoxCooking(pc47, 15, 0));
                            pc47.setDessertId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_0_N:
                    case L1SkillId.COOKING_3_0_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc48 = (L1PcInstance) cha;
                            pc48.sendPackets(new S_PacketBoxCooking(pc48, 16, 0));
                            pc48.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_1_N:
                    case L1SkillId.COOKING_3_1_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc49 = (L1PcInstance) cha;
                            pc49.addMaxHp(-50);
                            pc49.sendPackets(new S_HPUpdate(pc49.getCurrentHp(), pc49.getMaxHp()));
                            if (pc49.isInParty()) {
                                pc49.getParty().updateMiniHP(pc49);
                            }
                            pc49.addMaxMp(-50);
                            pc49.sendPackets(new S_MPUpdate(pc49.getCurrentMp(), pc49.getMaxMp()));
                            pc49.sendPackets(new S_PacketBoxCooking(pc49, 17, 0));
                            pc49.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_2_N:
                    case L1SkillId.COOKING_3_2_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc50 = (L1PcInstance) cha;
                            pc50.sendPackets(new S_PacketBoxCooking(pc50, 18, 0));
                            pc50.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_3_N:
                    case L1SkillId.COOKING_3_3_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc51 = (L1PcInstance) cha;
                            pc51.addAc(3);
                            pc51.sendPackets(new S_PacketBoxCooking(pc51, 19, 0));
                            pc51.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_4_N:
                    case L1SkillId.COOKING_3_4_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc52 = (L1PcInstance) cha;
                            pc52.addMr(-15);
                            pc52.sendPackets(new S_SPMR(pc52));
                            pc52.addWind(-10);
                            pc52.addWater(-10);
                            pc52.addFire(-10);
                            pc52.addEarth(-10);
                            pc52.sendPackets(new S_OwnCharAttrDef(pc52));
                            pc52.sendPackets(new S_PacketBoxCooking(pc52, 20, 0));
                            pc52.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_5_N:
                    case L1SkillId.COOKING_3_5_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc53 = (L1PcInstance) cha;
                            pc53.addSp(-2);
                            pc53.sendPackets(new S_SPMR(pc53));
                            pc53.sendPackets(new S_PacketBoxCooking(pc53, 21, 0));
                            pc53.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_6_N:
                    case L1SkillId.COOKING_3_6_S:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc54 = (L1PcInstance) cha;
                            pc54.addMaxHp(-30);
                            pc54.sendPackets(new S_HPUpdate(pc54.getCurrentHp(), pc54.getMaxHp()));
                            if (pc54.isInParty()) {
                                pc54.getParty().updateMiniHP(pc54);
                            }
                            pc54.sendPackets(new S_PacketBoxCooking(pc54, 22, 0));
                            pc54.setCookingId(0);
                            break;
                        }
                        break;
                    case L1SkillId.COOKING_3_7_N:
                    case 3047:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc55 = (L1PcInstance) cha;
                            pc55.sendPackets(new S_PacketBoxCooking(pc55, 23, 0));
                            pc55.setDessertId(0);
                            break;
                        }
                        break;
                    case L1SkillId.STATUS_CHAT_PROHIBITED:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage(288));
                            break;
                        }
                        break;
                    case 4010:
                    case L1SkillId.EXP20:
                    case L1SkillId.EXP25:
                    case L1SkillId.EXP15:
                    case L1SkillId.EXP17:
                    case 6671:
                    case L1SkillId.EXP35:
                    case L1SkillId.EXP40:
                    case L1SkillId.EXP45:
                    case L1SkillId.EXP50:
                    case L1SkillId.EXP55:
                    case L1SkillId.EXP60:
                    case L1SkillId.EXP65:
                    case L1SkillId.EXP70:
                    case L1SkillId.EXP75:
                    case L1SkillId.EXP80:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.SEXP20:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.SEXP13:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.SEXP15:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.SEXP17:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.REEXP20:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_ServerMessage("經驗加倍效果結束。"));
                            break;
                        }
                        break;
                    case L1SkillId.AI_1:
                        if (cha instanceof L1PcInstance) {
                            L1PcInstance pc56 = (L1PcInstance) cha;
                            if (!pc56.isPrivateShop() && !pc56.isSafetyZone() && !L1CastleLocation.checkInAllWarArea(pc56.getX(), pc56.getY(), pc56.getMapId()) && !L1HouseLocation.isInHouse(pc56.getX(), pc56.getY(), pc56.getMapId())) {
                                if (pc56.getMapId() != 4 || !pc56.getLocation().isInScreen(new Point(33055, 32336))) {
                                    pc56.setAi_Number(RandomArrayList.getInt(Plug_Question.getInstance().getQuestionList().length));
                                    Plug_Question question = Plug_Question.getInstance().getTemplate(pc56.getAi_Number());
                                    String[] j = {"\\fA", "\\fN", "\\f9", "\\fP", "\\fU", "\\fW", "\\fO", "\\fY", "\\fH", "\\f1", "\\f4", "\\f7", "\\f5", "\\fQ", "\\fI", "\\fR", "\\fX", "\\fE", "\\fZ", "\\fG", "\\f2", "\\f3", "\\f6", "\\f8", "\\fC", "\\fV", "\\fJ", "\\fD", "\\fK", "\\fL", "\\fF", "\\fM", "\\fT", "\\fB", "\\fS"};
                                    int r = RandomArrayList.getInt(j.length);
                                    pc56.sendPackets(new S_BlueMessage(166, String.format(String.valueOf(j[r]) + L1WilliamSystemMessage.ShowMessage(13), String.valueOf(j[r]) + question.getQuestion())));
                                    pc56.sendPackets(new S_SystemMessage(String.format(String.valueOf(j[r]) + L1WilliamSystemMessage.ShowMessage(14), String.valueOf(j[r]) + question.getQuestion())));
                                    pc56.setSkillEffect(L1SkillId.AI_2, 600000);
                                    break;
                                } else {
                                    return;
                                }
                            } else {
                                return;
                            }
                        }
                        break;
                    case L1SkillId.AI_2:
                        if (cha instanceof L1PcInstance) {
                            ((L1PcInstance) cha).sendPackets(new S_Disconnect());
                            break;
                        }
                        break;
                }
            } else {
                mode.stop(cha);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc57 = (L1PcInstance) cha;
            sendStopMessage(pc57, skillId);
            pc57.sendPackets(new S_OwnCharStatus(pc57));
        }
    }

    private static void sendStopMessage(L1PcInstance charaPc, int skillid) {
        int msgID;
        L1Skills l1skills = SkillsTable.get().getTemplate(skillid);
        if (l1skills != null && charaPc != null && (msgID = l1skills.getSysmsgIdStop()) > 0) {
            charaPc.sendPackets(new S_ServerMessage(msgID));
        }
    }
}
