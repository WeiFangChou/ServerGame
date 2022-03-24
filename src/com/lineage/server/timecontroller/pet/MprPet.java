package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1NpcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MprPet {
    private static final Log _log = LogFactory.getLog(MprPet.class);

    public static boolean mpUpdate(L1NpcInstance npc, int time) {
        try {
            if (npc.getMaxHp() <= 0 || npc.getMaxMp() <= 0 || npc.isDead() || npc.destroyed() || npc.getCurrentHp() <= 0 || npc.getCurrentMp() >= npc.getMaxMp()) {
                return false;
            }
            int mprInterval = npc.getNpcTemplate().get_mprinterval();
            if (mprInterval <= 0) {
                mprInterval = 20;
            }
            if (time % mprInterval != 0) {
                return false;
            }
            int mpr = npc.getNpcTemplate().get_mpr();
            if (mpr <= 0) {
                mpr = 1;
            }
            mprInterval(npc, mpr);
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private static void mprInterval(L1NpcInstance npc, int mpr) {
        try {
            if (npc.isMpRegenerationX()) {
                npc.setCurrentMp(npc.getCurrentMp() + mpr);
            }
        } catch (Exception e) {
            _log.error("PET 執行回復MP發生異常", e);
            npc.deleteMe();
        }
    }
}
