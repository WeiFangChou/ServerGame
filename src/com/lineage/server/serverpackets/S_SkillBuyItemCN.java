package com.lineage.server.serverpackets;

import com.lineage.list.PcLvSkillList;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class S_SkillBuyItemCN extends ServerBasePacket {
    private static final Log _log = LogFactory.getLog(S_SkillBuyItemCN.class);
    private byte[] _byte = null;

    public S_SkillBuyItemCN(L1PcInstance pc, L1NpcInstance npc) {
        ArrayList<Integer> skillList = null;
        if (pc.isCrown()) {
            skillList = PcLvSkillList.isCrown(pc);
        } else if (pc.isKnight()) {
            skillList = PcLvSkillList.isKnight(pc);
        } else if (pc.isElf()) {
            skillList = PcLvSkillList.isElf(pc);
        } else if (pc.isWizard()) {
            skillList = PcLvSkillList.isWizard(pc);
        } else if (pc.isDarkelf()) {
            skillList = PcLvSkillList.isDarkelf(pc);
        } else if (pc.isDragonKnight()) {
            skillList = PcLvSkillList.isDragonKnight(pc);
        } else if (pc.isIllusionist()) {
            skillList = PcLvSkillList.isIllusionist(pc);
        }
        ArrayList<Integer> newSkillList = new ArrayList<>();
        Iterator<Integer> it = skillList.iterator();
        while (it.hasNext()) {
            Integer integer = it.next();
            if (!CharSkillReading.get().spellCheck(pc.getId(), integer.intValue() + 1)) {
                newSkillList.add(integer);
            }
        }
        if (newSkillList.size() <= 0) {
            writeC(L1SkillId.ELEMENTAL_FIRE);
            writeH(0);
            return;
        }
        try {
            writeC(L1SkillId.ELEMENTAL_FIRE);
            writeH(newSkillList.size());
            Iterator<Integer> it2 = newSkillList.iterator();
            while (it2.hasNext()) {
                writeD(it2.next().intValue());
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
