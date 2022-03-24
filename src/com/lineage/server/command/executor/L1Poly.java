package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Poly implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Poly.class);

    private L1Poly() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Poly();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String name = st.nextToken();
            int polyid = Integer.parseInt(st.nextToken());
            L1PcInstance tg = World.get().getPlayer(name);
            if (tg == null) {
                pc.sendPackets(new S_ServerMessage(73, name));
                return;
            }
            try {
                L1PolyMorph.doPoly(tg, polyid, 7200, 2);
            } catch (Exception e) {
                pc.sendPackets(new S_SystemMessage(".poly [人物名稱] [外型代號]"));
            }
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
