package com.lineage.server.timecontroller.pet;

import com.lineage.server.model.Instance.L1NpcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HprPet {
    private static final Log _log = LogFactory.getLog(HprPet.class);

    public static boolean hpUpdate(L1NpcInstance npc, int time) {
        try {
            if (npc.getMaxHp() <= 0 || npc.getCurrentHp() <= 0 || npc.isDead() || npc.destroyed() || npc.getCurrentHp() >= npc.getMaxHp()) {
                return false;
            }
            int hprInterval = npc.getNpcTemplate().get_hprinterval();
            if (hprInterval <= 0) {
                hprInterval = 20;
            }
            if (time % hprInterval != 0) {
                return false;
            }
            int hpr = npc.getNpcTemplate().get_hpr();
            if (hpr <= 0) {
                hpr = 1;
            }
            hprInterval(npc, hpr);
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    private static void hprInterval(L1NpcInstance npc, int hpr) {
        try {
            if (npc.isHpRegenerationX()) {
                npc.setCurrentHp(npc.getCurrentHp() + hpr);
            }
        } catch (Exception e) {
            _log.error("PET 執行回復HP發生異常", e);
            npc.deleteMe();
        }
    }
}
