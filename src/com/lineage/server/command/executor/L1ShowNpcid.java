package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TrapInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ShowNpcid implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1ShowNpcid.class);

    private L1ShowNpcid() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1ShowNpcid();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            for (L1Object object : pc.getKnownObjects()) {
                if (object instanceof L1ItemInstance) {
                    L1ItemInstance tg = (L1ItemInstance) object;
                    pc.sendPackets(new S_Chat(tg, "ItemId:" + tg.getItemId(), 0));
                } else if (object instanceof L1PcInstance) {
                    L1PcInstance tg2 = (L1PcInstance) object;
                    pc.sendPackets(new S_Chat(tg2, "Objid:" + tg2.getId(), 0));
                } else if (object instanceof L1TrapInstance) {
                    L1TrapInstance tg3 = (L1TrapInstance) object;
                    pc.sendPackets(new S_Chat(object, "XY:" + tg3.getX() + "/" + tg3.getY(), 0));
                } else if (object instanceof L1PetInstance) {
                    pc.sendPackets(new S_Chat(object, "tg: Pet", 0));
                } else if (object instanceof L1SummonInstance) {
                    pc.sendPackets(new S_Chat(object, "tg: Summon", 0));
                } else if (object instanceof L1DollInstance) {
                    pc.sendPackets(new S_Chat(object, "Over Time:" + ((L1DollInstance) object).get_time(), 0));
                } else if (object instanceof L1EffectInstance) {
                    pc.sendPackets(new S_Chat(object, "tg: Effect", 0));
                } else if (object instanceof L1MonsterInstance) {
                    L1MonsterInstance tg4 = (L1MonsterInstance) object;
                    pc.sendPackets(new S_Chat(object, "NpcId：→" + tg4.getName() + "←/→" + tg4.getNpcId() + "←", 0));
                } else if (object instanceof L1NpcInstance) {
                    L1NpcInstance tg5 = (L1NpcInstance) object;
                    pc.sendPackets(new S_Chat(object, "NpcId：→" + tg5.getName() + "←/→" + tg5.getNpcId() + "←", 0));
                }
            }
        } catch (Exception e) {
            _log.error("錯誤的GM指令格式: " + getClass().getSimpleName() + " 執行的GM:" + pc.getName());
            pc.sendPackets(new S_ServerMessage(261));
        }
    }
}
