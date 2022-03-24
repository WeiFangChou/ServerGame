package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxHpMsg;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.Random;

public class Fish extends ItemExecutor {
    private Fish() {
    }

    public static ItemExecutor get() {
        return new Fish();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        Random random = new Random();
        int getItemId = 0;
        int getCount = 0;
        switch (itemId) {
            case 41298:
                UseHeallingPotion(pc, 4, L1SkillId.SHOCK_SKIN);
                break;
            case 41299:
                UseHeallingPotion(pc, 15, 194);
                break;
            case 41300:
                UseHeallingPotion(pc, 35, 197);
                break;
            case 41301:
                int chance1 = random.nextInt(10);
                if (chance1 < 0 || chance1 >= 5) {
                    if (chance1 < 5 || chance1 >= 9) {
                        if (chance1 >= 9) {
                            int gemChance = random.nextInt(3);
                            if (gemChance != 0) {
                                if (gemChance != 1) {
                                    if (gemChance == 2) {
                                        getItemId = 40053;
                                        getCount = 1;
                                        break;
                                    }
                                } else {
                                    getItemId = 40049;
                                    getCount = 1;
                                    break;
                                }
                            } else {
                                getItemId = 40045;
                                getCount = 1;
                                break;
                            }
                        }
                    } else {
                        getItemId = L1ItemId.CONDENSED_POTION_OF_HEALING;
                        getCount = 1;
                        break;
                    }
                } else {
                    UseHeallingPotion(pc, 15, L1SkillId.SHOCK_SKIN);
                    break;
                }
                break;
            case 41302:
                int chance2 = random.nextInt(3);
                if (chance2 < 0 || chance2 >= 5) {
                    if (chance2 < 5 || chance2 >= 9) {
                        if (chance2 >= 9) {
                            int gemChance2 = random.nextInt(3);
                            if (gemChance2 != 0) {
                                if (gemChance2 != 1) {
                                    if (gemChance2 == 2) {
                                        getItemId = 40055;
                                        getCount = 1;
                                        break;
                                    }
                                } else {
                                    getItemId = 40051;
                                    getCount = 1;
                                    break;
                                }
                            } else {
                                getItemId = 40047;
                                getCount = 1;
                                break;
                            }
                        }
                    } else {
                        getItemId = L1ItemId.POTION_OF_GREATER_HASTE_SELF;
                        getCount = 1;
                        break;
                    }
                } else {
                    UseHeallingPotion(pc, 15, L1SkillId.SHOCK_SKIN);
                    break;
                }
                break;
            case 41303:
                int chance3 = random.nextInt(3);
                if (chance3 < 0 || chance3 >= 5) {
                    if (chance3 < 5 || chance3 >= 9) {
                        if (chance3 >= 9) {
                            int gemChance3 = random.nextInt(3);
                            if (gemChance3 != 0) {
                                if (gemChance3 != 1) {
                                    if (gemChance3 == 2) {
                                        getItemId = 40054;
                                        getCount = 1;
                                        break;
                                    }
                                } else {
                                    getItemId = 40050;
                                    getCount = 1;
                                    break;
                                }
                            } else {
                                getItemId = 40046;
                                getCount = 1;
                                break;
                            }
                        }
                    } else {
                        getItemId = L1ItemId.POTION_OF_MANA;
                        getCount = 1;
                        break;
                    }
                } else {
                    UseHeallingPotion(pc, 15, L1SkillId.SHOCK_SKIN);
                    break;
                }
                break;
            case 41304:
                int chance = random.nextInt(3);
                if (chance < 0 || chance >= 5) {
                    if (chance < 5 || chance >= 9) {
                        if (chance >= 9) {
                            int gemChance4 = random.nextInt(3);
                            if (gemChance4 != 0) {
                                if (gemChance4 != 1) {
                                    if (gemChance4 == 2) {
                                        getItemId = 40052;
                                        getCount = 1;
                                        break;
                                    }
                                } else {
                                    getItemId = 40048;
                                    getCount = 1;
                                    break;
                                }
                            } else {
                                getItemId = 40044;
                                getCount = 1;
                                break;
                            }
                        }
                    } else {
                        getItemId = L1ItemId.CONDENSED_POTION_OF_GREATER_HEALING;
                        getCount = 1;
                        break;
                    }
                } else {
                    UseHeallingPotion(pc, 15, L1SkillId.SHOCK_SKIN);
                    break;
                }
                break;
        }
        pc.getInventory().removeItem(item, 1);
        if (getCount != 0) {
            CreateNewItem.createNewItem(pc, getItemId, (long) getCount);
        }
    }

    private void UseHeallingPotion(L1PcInstance pc, int healHp, int gfxid) {
        Random _random = new Random();
        if (pc.hasSkillEffect(71)) {
            pc.sendPackets(new S_ServerMessage(698));
            return;
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        pc.sendPacketsX8(new S_SkillSound(pc.getId(), gfxid));
        int healHp2 = (int) (((double) healHp) * ((_random.nextGaussian() / 5.0d) + 1.0d));
        if (pc.get_up_hp_potion() > 0) {
            healHp2 += (pc.get_up_hp_potion() * healHp2) / 100;
        }
        if (pc.hasSkillEffect(173)) {
            healHp2 >>= 1;
        }
        if (pc.hasSkillEffect(L1SkillId.ADLV80_2_2)) {
            healHp2 >>= 1;
        }
        if (pc.hasSkillEffect(L1SkillId.ADLV80_2_1)) {
            healHp2 *= -1;
        }
        if (healHp2 > 0) {
            pc.sendPackets(new S_PacketBoxHpMsg());
        }
        pc.setCurrentHp(pc.getCurrentHp() + healHp2);
    }
}
