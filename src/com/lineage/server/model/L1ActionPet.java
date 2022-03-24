package com.lineage.server.model;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_SelectTarget;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ActionPet {
    private static final Log _log = LogFactory.getLog(L1ActionPet.class);
    private final L1PcInstance _pc;

    public L1ActionPet(L1PcInstance pc) {
        this._pc = pc;
    }

    public L1PcInstance get_pc() {
        return this._pc;
    }

    public void action(L1PetInstance npc, String action) {
        String status = null;
        try {
            if (action.equalsIgnoreCase("attackchr")) {
                int currentPetStatus = npc.getCurrentPetStatus();
                String type = "0";
                switch (currentPetStatus) {
                    case 0:
                    case 4:
                    case 6:
                    case 7:
                        type = "5";
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 8:
                        type = String.valueOf(currentPetStatus);
                        break;
                }
                npc.onFinalAction(this._pc, type);
                this._pc.sendPackets(new S_SelectTarget(npc.getId()));
            } else if (action.equalsIgnoreCase("aggressive")) {
                status = "1";
            } else if (action.equalsIgnoreCase("defensive")) {
                status = "2";
            } else if (action.equalsIgnoreCase("stay")) {
                status = "3";
            } else if (action.equalsIgnoreCase("extend")) {
                status = "4";
            } else if (action.equalsIgnoreCase("alert")) {
                status = "5";
            } else if (action.equalsIgnoreCase("dismiss")) {
                status = "6";
            } else if (action.equalsIgnoreCase("getitem")) {
                status = "8";
                npc.collection();
            } else if (action.equalsIgnoreCase("changename")) {
                this._pc.rename(false);
                this._pc.setTempID(npc.getId());
                this._pc.sendPackets(new S_Message_YN(325));
            }
            if (status != null) {
                npc.onFinalAction(this._pc, status);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
