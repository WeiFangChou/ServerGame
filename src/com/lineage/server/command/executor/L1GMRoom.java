package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1GMRoom implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1GMRoom.class);

    private L1GMRoom() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1GMRoom();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            switch (Integer.parseInt(arg)) {
                case 1:
                    L1Teleport.teleport(pc, 32737, 32796, (short) 99, 5, false);
                    return;
                case 2:
                    L1Teleport.teleport(pc, 32734, 32799, (short) 17100, 5, false);
                    return;
                case 3:
                    L1Teleport.teleport(pc, 32644, 32955, (short) 0, 5, false);
                    return;
                case 4:
                    L1Teleport.teleport(pc, 33429, 32814, (short) 4, 5, false);
                    return;
                case 5:
                    L1Teleport.teleport(pc, 32894, 32535, (short) 300, 5, false);
                    return;
                case 6:
                    L1Teleport.teleport(pc, 32679, 33169, (short) 4, 5, false);
                    return;
                case 7:
                    L1Teleport.teleport(pc, 32863, 32936, (short) 630, 5, false);
                    return;
                case 8:
                    L1Teleport.teleport(pc, 32734, 32802, (short) 360, 5, false);
                    return;
                case 9:
                    L1Teleport.teleport(pc, 32737, 32789, (short) 997, 5, false);
                    return;
                case 10:
                    L1Teleport.teleport(pc, 32959, 32874, (short) 68, 5, false);
                    return;
                case 11:
                    L1Teleport.teleport(pc, 32707, 32846, (short) 9000, 5, false);
                    return;
                default:
                    L1Teleport.teleport(pc, 32863, 32936, (short) 630, 5, false);
                    return;
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
