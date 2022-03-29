package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_Paralysis;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Transmission_Reel extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Transmission_Reel.class);

    private Transmission_Reel() {
    }

    public static ItemExecutor get() {
        return new Transmission_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        boolean isTeleport = false;
        try {
            int locx = 0;
            int locy = 0;
            int mapid = 0;
            switch (item.getItemId()) {
                case 40289:
                    locx = 32631;
                    locy = 32935;
                    mapid = 111;
                    isTeleport = true;
                    break;
                case 40290:
                    locx = 32631;
                    locy = 32935;
                    mapid = 121;
                    isTeleport = true;
                    break;
                case 40291:
                    locx = 32631;
                    locy = 32935;
                    mapid = 131;
                    isTeleport = true;
                    break;
                case 40292:
                    locx = 32631;
                    locy = 32935;
                    mapid = 141;
                    isTeleport = true;
                    break;
                case 40293:
                    locx = 32669;
                    locy = 32814;
                    mapid = 151;
                    isTeleport = true;
                    break;
                case 40294:
                    locx = 32669;
                    locy = 32814;
                    mapid = 161;
                    isTeleport = true;
                    break;
                case 40295:
                    locx = 32669;
                    locy = 32814;
                    mapid = 171;
                    isTeleport = true;
                    break;
                case 40296:
                    locx = 32669;
                    locy = 32814;
                    mapid = 181;
                    isTeleport = true;
                    break;
                case 40297:
                    locx = 32669;
                    locy = 32814;
                    mapid = 191;
                    isTeleport = true;
                    break;
            }
            if (isTeleport) {
                L1Teleport.teleport(pc, locx, locy, mapid, 5, true);
            } else {
                pc.sendPackets(new S_Paralysis(7, false));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
