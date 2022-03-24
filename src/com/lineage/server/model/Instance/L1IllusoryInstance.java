package com.lineage.server.model.Instance;

import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Character;
import com.lineage.server.serverpackets.S_NPCPack_Ill;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.templates.L1Npc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1IllusoryInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1IllusoryInstance.class);
    private static final long serialVersionUID = 1;

    public L1IllusoryInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                if (getCurrentHp() > 0) {
                    perceivedFrom.sendPackets(new S_NPCPack_Ill(this));
                    onNpcAI();
                    if (getBraveSpeed() == 1) {
                        perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000));
                        return;
                    }
                    return;
                }
                perceivedFrom.sendPackets(new S_NPCPack_Ill(this));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void setLink(L1Character cha) {
        if (get_showId() == cha.get_showId()) {
            if ((!(cha instanceof L1PcInstance) || !cha.getMap().isSafetyZone(cha.getLocation())) && cha != null && this._hateList.isEmpty()) {
                this._hateList.add(cha, 0);
                checkTarget();
            }
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onNpcAI() {
        if (!isAiRunning()) {
            setActived(false);
            startAI();
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance pc) {
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            new L1AttackPc(pc, this).action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void receiveDamage(L1Character attacker, int damage) {
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentHp(int i) {
    }

    @Override // com.lineage.server.model.L1Character
    public void setCurrentMp(int i) {
    }
}
