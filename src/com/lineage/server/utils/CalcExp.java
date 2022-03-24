package com.lineage.server.utils;

import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.MapExpTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_NPCPack_Pet;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.world.World;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CalcExp {
    private static final Log _log = LogFactory.getLog(CalcExp.class);

    private CalcExp() {
    }

    public static void calcExp(L1PcInstance srcpc, int targetid, ArrayList<?> acquisitorList, ArrayList<Integer> hateList, long exp) {
        double party_level = 0.0d;
        double dist = 0.0d;
        try {
            L1Object object = World.get().findObject(targetid);
            if (object instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance) object;
                long acquire_exp = 0;
                int acquire_lawful = 0;
                long party_exp = 0;
                int party_lawful = 0;
                long totalHateExp = 0;
                int totalHateLawful = 0;
                if (acquisitorList.size() == hateList.size()) {
                    for (int i = hateList.size() - 1; i >= 0; i--) {
                        L1Character acquisitor = (L1Character) acquisitorList.get(i);
                        int hate = hateList.get(i).intValue();
                        boolean isRemove = false;
                        if (acquisitor instanceof L1IllusoryInstance) {
                            isRemove = true;
                        }
                        if (acquisitor instanceof L1EffectInstance) {
                            isRemove = true;
                        }
                        if (isRemove) {
                            if (acquisitor != null) {
                                acquisitorList.remove(i);
                                hateList.remove(i);
                            }
                        } else if (acquisitor == null || acquisitor.isDead()) {
                            acquisitorList.remove(i);
                            hateList.remove(i);
                        } else {
                            totalHateExp += (long) hate;
                            if (acquisitor instanceof L1PcInstance) {
                                totalHateLawful += hate;
                            }
                        }
                    }
                    if (!(totalHateExp == 0 || object == null || (npc instanceof L1PetInstance) || (npc instanceof L1SummonInstance))) {
                        if (!World.get().isProcessingContributionTotal() && srcpc.getHomeTownId() > 0) {
                            srcpc.addContribution(npc.getLevel() / 10);
                        }
                        int lawful = npc.getLawful();
                        if (srcpc.isInParty()) {
                            long partyHateExp = 0;
                            int partyHateLawful = 0;
                            for (int i2 = hateList.size() - 1; i2 >= 0; i2--) {
                                L1Character acquisitor2 = (L1Character) acquisitorList.get(i2);
                                int hate2 = hateList.get(i2).intValue();
                                if (acquisitor2 instanceof L1PcInstance) {
                                    L1PcInstance pc = (L1PcInstance) acquisitor2;
                                    if (pc == srcpc) {
                                        partyHateExp += (long) hate2;
                                        partyHateLawful += hate2;
                                    } else if (srcpc.getParty().isMember(pc)) {
                                        partyHateExp += (long) hate2;
                                        partyHateLawful += hate2;
                                    } else {
                                        if (totalHateExp > 0) {
                                            acquire_exp = (((long) hate2) * exp) / totalHateExp;
                                        }
                                        if (totalHateLawful > 0) {
                                            acquire_lawful = (lawful * hate2) / totalHateLawful;
                                        }
                                        addExp(pc, acquire_exp, acquire_lawful);
                                    }
                                } else if (acquisitor2 instanceof L1PetInstance) {
                                    L1PetInstance pet = (L1PetInstance) acquisitor2;
                                    L1PcInstance master = (L1PcInstance) pet.getMaster();
                                    if (master == srcpc) {
                                        partyHateExp += (long) hate2;
                                    } else if (srcpc.getParty().isMember(master)) {
                                        partyHateExp += (long) hate2;
                                    } else {
                                        if (totalHateExp > 0) {
                                            acquire_exp = (((long) hate2) * exp) / totalHateExp;
                                        }
                                        addExpPet(pet, acquire_exp);
                                    }
                                } else if (acquisitor2 instanceof L1SummonInstance) {
                                    L1PcInstance master2 = (L1PcInstance) ((L1SummonInstance) acquisitor2).getMaster();
                                    if (master2 == srcpc) {
                                        partyHateExp += (long) hate2;
                                    } else if (srcpc.getParty().isMember(master2)) {
                                        partyHateExp += (long) hate2;
                                    }
                                }
                            }
                            if (totalHateExp > 0) {
                                party_exp = (exp * partyHateExp) / totalHateExp;
                            }
                            if (totalHateLawful > 0) {
                                party_lawful = (lawful * partyHateLawful) / totalHateLawful;
                            }
                            double pri_bonus = 0.0d;
                            L1PcInstance leader = srcpc.getParty().getLeader();
                            if (leader.isCrown() && (srcpc.knownsObject(leader) || srcpc.equals(leader))) {
                                pri_bonus = 0.059d;
                            }
                            Object[] pcs = srcpc.getParty().partyUsers().values().toArray();
                            double pt_bonus = 0.0d;
                            int length = pcs.length;
                            for (int i3 = 0; i3 < length; i3++) {
                                Object obj = pcs[i3];
                                if (obj instanceof L1PcInstance) {
                                    L1PcInstance each = (L1PcInstance) obj;
                                    if (!each.isDead()) {
                                        if (srcpc.knownsObject(each) || srcpc.equals(each)) {
                                            party_level += (double) (each.getLevel() * each.getLevel());
                                        }
                                        if (srcpc.knownsObject(each)) {
                                            pt_bonus += 0.04d;
                                        }
                                    }
                                }
                            }
                            long party_exp2 = (long) (((double) party_exp) * (1.0d + pt_bonus + pri_bonus));
                            if (party_level > 0.0d) {
                                dist = ((double) (srcpc.getLevel() * srcpc.getLevel())) / party_level;
                            }
                            long member_exp = (long) (((double) party_exp2) * dist);
                            int member_lawful = (int) (((double) party_lawful) * dist);
                            long ownHateExp = 0;
                            for (int i4 = hateList.size() - 1; i4 >= 0; i4--) {
                                L1Character acquisitor3 = (L1Character) acquisitorList.get(i4);
                                int hate3 = hateList.get(i4).intValue();
                                if (acquisitor3 instanceof L1PcInstance) {
                                    if (((L1PcInstance) acquisitor3) == srcpc) {
                                        ownHateExp += (long) hate3;
                                    }
                                } else if (acquisitor3 instanceof L1PetInstance) {
                                    if (((L1PcInstance) ((L1PetInstance) acquisitor3).getMaster()) == srcpc) {
                                        ownHateExp += (long) hate3;
                                    }
                                } else if ((acquisitor3 instanceof L1SummonInstance) && ((L1PcInstance) ((L1SummonInstance) acquisitor3).getMaster()) == srcpc) {
                                    ownHateExp += (long) hate3;
                                }
                            }
                            if (ownHateExp != 0) {
                                for (int i5 = hateList.size() - 1; i5 >= 0; i5--) {
                                    L1Character acquisitor4 = (L1Character) acquisitorList.get(i5);
                                    int hate4 = hateList.get(i5).intValue();
                                    if (acquisitor4 instanceof L1PcInstance) {
                                        L1PcInstance pc2 = (L1PcInstance) acquisitor4;
                                        if (pc2 == srcpc) {
                                            if (ownHateExp > 0) {
                                                acquire_exp = (((long) hate4) * member_exp) / ownHateExp;
                                            }
                                            addExp(pc2, acquire_exp, member_lawful);
                                        }
                                    } else if (acquisitor4 instanceof L1PetInstance) {
                                        L1PetInstance pet2 = (L1PetInstance) acquisitor4;
                                        if (((L1PcInstance) pet2.getMaster()) == srcpc) {
                                            if (ownHateExp > 0) {
                                                acquire_exp = (((long) hate4) * member_exp) / ownHateExp;
                                            }
                                            addExpPet(pet2, acquire_exp);
                                        }
                                    } else {
                                        boolean z = acquisitor4 instanceof L1SummonInstance;
                                    }
                                }
                            } else {
                                addExp(srcpc, member_exp, member_lawful);
                            }
                            int length2 = pcs.length;
                            for (int i6 = 0; i6 < length2; i6++) {
                                Object obj2 = pcs[i6];
                                if (obj2 instanceof L1PcInstance) {
                                    L1PcInstance tgpc = (L1PcInstance) obj2;
                                    if (!tgpc.isDead() && !tgpc.isDead() && srcpc.knownsObject(tgpc)) {
                                        if (party_level > 0.0d) {
                                            dist = ((double) (tgpc.getLevel() * tgpc.getLevel())) / party_level;
                                        }
                                        long member_exp2 = (long) ((int) (((double) party_exp2) * dist));
                                        int member_lawful2 = (int) (((double) party_lawful) * dist);
                                        long ownHateExp2 = 0;
                                        for (int i7 = hateList.size() - 1; i7 >= 0; i7--) {
                                            L1Character acquisitor5 = (L1Character) acquisitorList.get(i7);
                                            int hate5 = hateList.get(i7).intValue();
                                            if (acquisitor5 instanceof L1PcInstance) {
                                                if (((L1PcInstance) acquisitor5) == tgpc) {
                                                    ownHateExp2 += (long) hate5;
                                                }
                                            } else if (acquisitor5 instanceof L1PetInstance) {
                                                if (((L1PcInstance) ((L1PetInstance) acquisitor5).getMaster()) == tgpc) {
                                                    ownHateExp2 += (long) hate5;
                                                }
                                            } else if ((acquisitor5 instanceof L1SummonInstance) && ((L1PcInstance) ((L1SummonInstance) acquisitor5).getMaster()) == tgpc) {
                                                ownHateExp2 += (long) hate5;
                                            }
                                        }
                                        if (ownHateExp2 != 0) {
                                            for (int i8 = hateList.size() - 1; i8 >= 0; i8--) {
                                                L1Character acquisitor6 = (L1Character) acquisitorList.get(i8);
                                                int hate6 = hateList.get(i8).intValue();
                                                if (acquisitor6 instanceof L1PcInstance) {
                                                    L1PcInstance pc3 = (L1PcInstance) acquisitor6;
                                                    if (pc3 == tgpc) {
                                                        if (ownHateExp2 > 0) {
                                                            acquire_exp = (((long) hate6) * member_exp2) / ownHateExp2;
                                                        }
                                                        addExp(pc3, acquire_exp, member_lawful2);
                                                    }
                                                } else if (acquisitor6 instanceof L1PetInstance) {
                                                    L1PetInstance pet3 = (L1PetInstance) acquisitor6;
                                                    if (((L1PcInstance) pet3.getMaster()) == tgpc) {
                                                        if (ownHateExp2 > 0) {
                                                            acquire_exp = (((long) hate6) * member_exp2) / ownHateExp2;
                                                        }
                                                        addExpPet(pet3, acquire_exp);
                                                    }
                                                } else {
                                                    boolean z2 = acquisitor6 instanceof L1SummonInstance;
                                                }
                                            }
                                        } else {
                                            addExp(tgpc, member_exp2, member_lawful2);
                                        }
                                    }
                                }
                            }
                            return;
                        }
                        for (int i9 = hateList.size() - 1; i9 >= 0; i9--) {
                            L1Character acquisitor7 = (L1Character) acquisitorList.get(i9);
                            int hate7 = hateList.get(i9).intValue();
                            long acquire_exp2 = (((long) hate7) * exp) / totalHateExp;
                            if ((acquisitor7 instanceof L1PcInstance) && totalHateLawful > 0) {
                                acquire_lawful = (lawful * hate7) / totalHateLawful;
                            }
                            if (acquisitor7 instanceof L1PcInstance) {
                                addExp((L1PcInstance) acquisitor7, acquire_exp2, acquire_lawful);
                            } else if (acquisitor7 instanceof L1PetInstance) {
                                addExpPet((L1PetInstance) acquisitor7, acquire_exp2);
                            } else {
                                boolean z3 = acquisitor7 instanceof L1SummonInstance;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void addExp(L1PcInstance pc, long exp, int lawful) {
        try {
            pc.addLawful(((int) (((double) lawful) * ConfigRate.RATE_LA)) * -1);
            if (pc.getLevel() < ExpTable.MAX_LEVEL) {
                double addExp = (double) exp;
                double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
                if (exppenalty < 1.0d) {
                    addExp *= exppenalty;
                }
                if (ConfigRate.RATE_XP > 1.0d) {
                    addExp *= ConfigRate.RATE_XP;
                }
                double addExp2 = addExp * add(pc);
                if (addExp2 < 0.0d) {
                    addExp2 = 0.0d;
                } else if (addExp2 > 3.6065092E7d) {
                    addExp2 = 3.6065092E7d;
                }
                pc.addExp((long) addExp2);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static double add(L1PcInstance pc) {
        double add_exp = 1.0d;
        try {
            if (pc.hasSkillEffect(L1SkillId.MAZU_SKILL)) {
                add_exp = 1.0d + 1.0d;
            }
            if (pc.getExpPoint() > 0) {
                add_exp += ((double) pc.getExpPoint()) / 100.0d;
            }
            double foodBonus = 0.0d;
            if (pc.hasSkillEffect(L1SkillId.COOKING_1_7_N) || pc.hasSkillEffect(L1SkillId.COOKING_1_7_S)) {
                foodBonus = 0.01d;
            }
            if (pc.hasSkillEffect(L1SkillId.COOKING_2_7_N) || pc.hasSkillEffect(L1SkillId.COOKING_2_7_S)) {
                foodBonus = 0.02d;
            }
            if (pc.hasSkillEffect(L1SkillId.COOKING_3_7_N) || pc.hasSkillEffect(3047)) {
                foodBonus = 0.03d;
            }
            if (pc.hasSkillEffect(L1SkillId.SEXP11)) {
                foodBonus = 0.1d;
            }
            if (pc.hasSkillEffect(4010)) {
                foodBonus = 0.3d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP15)) {
                foodBonus = 0.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP17)) {
                foodBonus = 0.7d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP20)) {
                foodBonus = 1.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP25)) {
                foodBonus = 1.5d;
            }
            if (pc.hasSkillEffect(6671)) {
                foodBonus = 2.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP35)) {
                foodBonus = 2.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP40)) {
                foodBonus = 3.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP45)) {
                foodBonus = 3.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP50)) {
                foodBonus = 4.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP55)) {
                foodBonus = 4.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP60)) {
                foodBonus = 5.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP65)) {
                foodBonus = 5.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP70)) {
                foodBonus = 6.0d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP75)) {
                foodBonus = 6.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.EXP80)) {
                foodBonus = 7.0d;
            }
            if (foodBonus > 0.0d) {
                add_exp += foodBonus;
            }
            double add_exp2 = add_exp + pc.getExpAdd();
            double s2_exp = 0.0d;
            if (pc.hasSkillEffect(L1SkillId.SEXP13)) {
                s2_exp = 0.3d;
            }
            if (pc.hasSkillEffect(L1SkillId.SEXP15)) {
                s2_exp = 0.5d;
            }
            if (pc.hasSkillEffect(L1SkillId.SEXP17)) {
                s2_exp = 0.7d;
            }
            if (pc.hasSkillEffect(L1SkillId.SEXP20)) {
                s2_exp = 1.0d;
            }
            if (s2_exp > 0.0d) {
                add_exp2 += s2_exp;
            }
            int mapid = pc.getMapId();
            if (MapExpTable.get().get_level(mapid, pc.getLevel())) {
                return add_exp2 + (MapExpTable.get().get_exp(mapid).doubleValue() - 1.0d);
            }
            return add_exp2;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 1.0d;
        }
    }

    private static void addExpPet(L1PetInstance pet, long exp) {
        L1Pet petTemplate;
        try {
            if (pet.getPetType() != null) {
                L1PcInstance pc = (L1PcInstance) pet.getMaster();
                int petItemObjId = pet.getItemObjId();
                int levelBefore = pet.getLevel();
                long totalExp = (long) ((((double) (10 * exp)) * ConfigRate.RATE_PET_XP) + ((double) pet.getExp()));
                long maxExp = ExpTable.getExpByLevel(ConfigOther.PET_MAX_LEVEL);
                if (totalExp > maxExp) {
                    totalExp = maxExp - 1;
                }
                pet.setExp(totalExp);
                pet.setLevel(ExpTable.getLevelByExp(totalExp));
                int expPercentage = ExpTable.getExpPercentage(pet.getLevel(), totalExp);
                int gap = pet.getLevel() - levelBefore;
                for (int i = 1; i <= gap; i++) {
                    RangeInt hpUpRange = pet.getPetType().getHpUpRange();
                    RangeInt mpUpRange = pet.getPetType().getMpUpRange();
                    pet.addMaxHp(hpUpRange.randomValue());
                    pet.addMaxMp(mpUpRange.randomValue());
                }
                pet.setExpPercent(expPercentage);
                pc.sendPackets(new S_NPCPack_Pet(pet, pc));
                if (!(gap == 0 || (petTemplate = PetReading.get().getTemplate(petItemObjId)) == null)) {
                    petTemplate.set_exp((int) pet.getExp());
                    petTemplate.set_level(pet.getLevel());
                    petTemplate.set_hp(pet.getMaxHp());
                    petTemplate.set_mp(pet.getMaxMp());
                    PetReading.get().storePet(petTemplate);
                    pc.sendPackets(new S_ServerMessage(320, pet.getName()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
