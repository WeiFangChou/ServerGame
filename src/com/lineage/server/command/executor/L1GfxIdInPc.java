package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GfxIdInPc implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1GfxIdInPc.class);

    private L1GfxIdInPc() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GfxIdInPc();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            try {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), Integer.parseInt(new StringTokenizer(arg).nextToken(), 10)));
            } catch (Exception e) {
                pc.sendPackets(new S_ServerMessage(261));
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
