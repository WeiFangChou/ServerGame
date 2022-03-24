package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1HpBar implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1HpBar.class);

    private L1HpBar() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1HpBar();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        if (arg.equalsIgnoreCase("on")) {
            pc.setSkillEffect(L1SkillId.GMSTATUS_HPBAR, 0);
        } else if (arg.equalsIgnoreCase("off")) {
            pc.removeSkillEffect(L1SkillId.GMSTATUS_HPBAR);
            for (L1Object obj : pc.getKnownObjects()) {
                if (pc.isGmHpBarTarget(obj)) {
                    pc.sendPackets(new S_HPMeter(obj.getId(), 255));
                }
            }
        } else {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
