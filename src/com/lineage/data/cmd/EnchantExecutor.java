package com.lineage.data.cmd;

import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.Random;

public abstract class EnchantExecutor {
    public abstract void failureEnchant(L1PcInstance l1PcInstance, L1ItemInstance l1ItemInstance) throws Exception;

    public abstract void successEnchant(L1PcInstance l1PcInstance, L1ItemInstance l1ItemInstance, int i) throws Exception;

    public int randomELevel(L1ItemInstance item, int bless) {
        switch (bless) {
            case 0:
            case 128:
                if (item.getBless() >= 3) {
                    return 0;
                }
                int i = new Random().nextInt(100) + 1;
                if (item.getItem().get_safeenchant() == 0) {
                    if (i < 50) {
                        return 1;
                    }
                    if (i >= 50 && i <= 80) {
                        return 2;
                    }
                    if (i < 81 || i > 100) {
                        return 0;
                    }
                    return 3;
                } else if (item.getEnchantLevel() <= 2) {
                    if (i < 32) {
                        return 1;
                    }
                    if (i >= 32 && i <= 76) {
                        return 2;
                    }
                    if (i < 77 || i > 100) {
                        return 0;
                    }
                    return 3;
                } else if (item.getEnchantLevel() < 3 || item.getEnchantLevel() > 5 || i >= 35) {
                    return 1;
                } else {
                    return 2;
                }
            case 1:
            case 129:
                if (item.getBless() < 3) {
                    return 1;
                }
                return 0;
            case 2:
            case L1SkillId.BODY_TO_MIND /*{ENCODED_INT: 130}*/:
                if (item.getBless() < 3) {
                    return -1;
                }
                return 0;
            case 3:
            case 131:
                if (item.getBless() == 3) {
                    return 1;
                }
                return 0;
            default:
                return 0;
        }
    }
}
