package com.lineage.server.command.executor;

import com.lineage.server.datatables.CommandsTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Command;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CommandHelp implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1CommandHelp.class);

    private L1CommandHelp() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1CommandHelp();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        ArrayList<L1Command> list = new ArrayList<>();
        for (L1Command command : CommandsTable.get().getList()) {
            if (pc == null) {
                if (command.isSystem()) {
                    list.add(command);
                }
            } else if (command.getLevel() <= pc.getAccessLevel()) {
                list.add(command);
            }
        }
        Iterator<L1Command> it = list.iterator();
        while (it.hasNext()) {
            L1Command command2 = it.next();
            if (pc == null) {
                _log.info("可用命令: " + command2.getName() + ": " + command2.get_note());
            } else {
                pc.sendPackets(new S_ServerMessage(166, String.valueOf(command2.getName()) + ": " + command2.get_note()));
            }
        }
        if (pc == null) {
            _log.info("可用命令: c: 對遊戲中玩家廣播公告.");
        }
    }
}
