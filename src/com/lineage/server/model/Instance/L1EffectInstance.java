package com.lineage.server.model.Instance;

import com.lineage.server.serverpackets.S_NPCPack_Eff;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1EffectInstance extends L1NpcInstance {
    public static final int CUBE_INTERVAL = 500;
    public static final int CUBE_TIME = 8000;
    public static final int FW_DAMAGE_INTERVAL = 1650;
    public static final int OTHER = 500;
    private static final Log _log = LogFactory.getLog(L1EffectInstance.class);
    private static final long serialVersionUID = 1;
    private L1EffectType _effectType;
    private int _skillId;

    public L1EffectInstance(L1Npc template) {
        super(template);
        switch (getNpcTemplate().get_npcId()) {
            case 80149:
                this._effectType = L1EffectType.isCubeBurn;
                return;
            case 80150:
                this._effectType = L1EffectType.isCubeEruption;
                return;
            case 80151:
                this._effectType = L1EffectType.isCubeShock;
                return;
            case 80152:
                this._effectType = L1EffectType.isCubeHarmonize;
                return;
            case 81157:
                this._effectType = L1EffectType.isFirewall;
                return;
            default:
                this._effectType = L1EffectType.isOther;
                return;
        }
    }

    public L1EffectType effectType() {
        return this._effectType;
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_NPCPack_Eff(this));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void deleteMe() {
        try {
            this._destroyed = true;
            if (getInventory() != null) {
                getInventory().clearItems();
            }
            allTargetClear();
            this._master = null;
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                L1PcInstance pc = it.next();
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }
            removeAllKnownObjects();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setSkillId(int i) {
        this._skillId = i;
    }

    public int getSkillId() {
        return this._skillId;
    }
}
