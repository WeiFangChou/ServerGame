package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Ress implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Ress.class);

    private L1Ress() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Ress();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int objid = pc.getId();
            pc.sendPacketsX8(new S_SkillSound(objid, 761));
            pc.setCurrentHp(pc.getMaxHp());
            pc.setCurrentMp(pc.getMaxMp());
            if (pc.isDead()) {
                pc.setTempID(objid);
                pc.sendPackets(new S_Message_YN(322));
            }
            Iterator<L1PcInstance> it = World.get().getVisiblePlayer(pc).iterator();
            while (it.hasNext()) {
                L1PcInstance tg = it.next();
                if (tg.isDead()) {
                    tg.setTempID(objid);
                    tg.sendPackets(new S_Message_YN(322));
                } else {
                    tg.setCurrentHp(tg.getMaxHp());
                    tg.setCurrentMp(tg.getMaxMp());
                }
            }
            Iterator<L1Object> it2 = World.get().getVisibleObjects(pc).iterator();
            while (it2.hasNext()) {
                L1Object obj = it2.next();
                if (obj instanceof L1PetInstance) {
                    L1PetInstance tg2 = (L1PetInstance) obj;
                    if (!tg2.isDead()) {
                        tg2.setCurrentHp(tg2.getMaxHp());
                        tg2.setCurrentMp(tg2.getMaxMp());
                    }
                }
                if (obj instanceof L1SummonInstance) {
                    L1SummonInstance tg3 = (L1SummonInstance) obj;
                    if (!tg3.isDead()) {
                        tg3.setCurrentHp(tg3.getMaxHp());
                        tg3.setCurrentMp(tg3.getMaxMp());
                    }
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
