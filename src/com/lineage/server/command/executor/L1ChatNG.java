package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ChatNG implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ChatNG.class);

    private L1ChatNG() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ChatNG();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            StringTokenizer st = new StringTokenizer(arg);
            String name = st.nextToken();
            int time = Integer.parseInt(st.nextToken());
            L1PcInstance tg = World.get().getPlayer(name);
            if (tg != null) {
                tg.setSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED, time * 60 * L1SkillId.STATUS_BRAVE);
                tg.sendPackets(new S_PacketBox(36, time * 60));
                tg.sendPackets(new S_ServerMessage(286, String.valueOf(time)));
                pc.sendPackets(new S_ServerMessage(287, name));
                return;
            }
            pc.sendPackets(new S_ServerMessage(73, name));
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
