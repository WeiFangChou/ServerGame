package com.lineage.server.model.Instance;

import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.utils.CalcExp;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ScarecrowInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1ScarecrowInstance.class);
    private static final long serialVersionUID = 1;

    public L1ScarecrowInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) {
        try {
            L1AttackMode attack = new L1AttackPc(player, this);
            if (attack.calcHit()) {
                if (player.getLevel() < 5) {
                    ArrayList<L1PcInstance> targetList = new ArrayList<>();
                    targetList.add(player);
                    ArrayList<Integer> hateList = new ArrayList<>();
                    hateList.add(1);
                    CalcExp.calcExp(player, getId(), targetList, hateList, (long) ((int) getExp()));
                }
                if (getHeading() < 7) {
                    setHeading(getHeading() + 1);
                } else {
                    setHeading(0);
                }
                broadcastPacketAll(new S_ChangeHeading(this));
            }
            attack.action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance l1pcinstance) {
    }

    public void onFinalAction() {
    }

    public void doFinalAction() {
    }
}
