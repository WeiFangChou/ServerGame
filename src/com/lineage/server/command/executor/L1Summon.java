package com.lineage.server.command.executor;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Npc;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Summon implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1Summon.class);

    private L1Summon() {
    }

    public static L1Summon getInstance() {
        return new L1Summon();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        int npcid;
        try {
            StringTokenizer tok = new StringTokenizer(arg);
            String nameid = tok.nextToken();
            try {
                npcid = Integer.parseInt(nameid);
            } catch (NumberFormatException e) {
                npcid = NpcTable.get().findNpcIdByNameWithoutSpace(nameid);
                if (npcid <= 0) {
                    pc.sendPackets(new S_ServerMessage(166, "錯誤的NPCID: " + npcid));
                    return;
                }
            }
            int count = 1;
            if (tok.hasMoreTokens()) {
                count = Integer.parseInt(tok.nextToken());
            }
            if (count > 5) {
                pc.sendPackets(new S_SystemMessage("寵物召喚數量一次禁止超過5隻。"));
                return;
            }
            L1Npc npc = NpcTable.get().getTemplate(npcid);
            for (int i = 0; i < count; i++) {
                new L1PetInstance(npc, pc).setPetcost(0);
            }
            pc.sendPackets(new S_ServerMessage(166, String.valueOf(NpcTable.get().getTemplate(npcid).get_name()) + "(ID:" + npcid + ") 數量:" + count + " 完成召喚"));
        } catch (Exception e2) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
