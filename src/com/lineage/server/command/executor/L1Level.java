package com.lineage.server.command.executor;

import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.utils.RangeInt;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Level implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Level.class);

    private L1Level() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Level();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            int level = Integer.parseInt(new StringTokenizer(arg).nextToken());
            if (level != pc.getLevel()) {
                if (!RangeInt.includes(level, 1, ExpTable.MAX_LEVEL)) {
                    pc.sendPackets(new S_SystemMessage("範圍限制 1~" + ExpTable.MAX_LEVEL));
                } else {
                    pc.setExp(ExpTable.getExpByLevel(level));
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
