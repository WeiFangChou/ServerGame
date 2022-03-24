package william;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_OwnCharPack;

public class L1PcUnlock {
    public static void Pc_Unlock(L1PcInstance pc) {
        if (pc != null) {
            pc.sendPackets(new S_OwnCharPack(pc));
            pc.removeAllKnownObjects();
            pc.updateObject();
            pc.sendVisualEffectAtTeleport();
            pc.sendPackets(new S_CharVisualUpdate(pc));
        }
    }
}
