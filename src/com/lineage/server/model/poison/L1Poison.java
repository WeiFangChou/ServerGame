package com.lineage.server.model.poison;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public abstract class L1Poison {
    public abstract void cure();

    public abstract int getEffectId();

    protected static boolean isValidTarget(L1Character cha) {
        if (cha == null || cha.getPoison() != null) {
            return false;
        }
        if (!(cha instanceof L1PcInstance)) {
            return true;
        }
        L1PcInstance player = (L1PcInstance) cha;
        return player.get_venom_resist() <= 0 && !player.hasSkillEffect(104) && !player.hasSkillEffect(L1SkillId.DRAGON5);
    }

    protected static void sendMessageIfPlayer(L1Character cha, int msgId) {
        if (cha instanceof L1PcInstance) {
            ((L1PcInstance) cha).sendPackets(new S_ServerMessage(msgId));
        }
    }
}
