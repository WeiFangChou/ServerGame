package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.L1SpawnUtil;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ILL implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ILL.class);
    private final Random _random = new Random();

    private L1ILL() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ILL();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            if (pc.get_otherList().get_illusoryList().size() < 1) {
                int count = this._random.nextInt(5) + 1;
                for (int i = 0; i < count; i++) {
                    L1Location loc = pc.getLocation().randomLocation(4, false);
                    L1IllusoryInstance spawnIll = L1SpawnUtil.spawn(pc, loc, pc.getHeading(), 30);
                    pc.get_otherList().addIllusoryList(Integer.valueOf(spawnIll.getId()), spawnIll);
                    pc.sendPacketsAll(new S_EffectLocation(loc, 5524));
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
