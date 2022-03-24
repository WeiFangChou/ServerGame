package com.lineage.data.cmd;

import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Skill_Check {
    public static void check(L1PcInstance pc, L1ItemInstance item, int skillid, int magicLv, int attribute) throws Exception {
        if (CharSkillReading.get().spellCheck(pc.getId(), skillid)) {
            pc.sendPackets(new S_ServerMessage(79));
        } else if (skillid != 0) {
            new Skill_Studying().magic(pc, skillid, magicLv, attribute, item.getId());
        }
    }
}
