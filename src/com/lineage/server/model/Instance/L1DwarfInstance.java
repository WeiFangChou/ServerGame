package com.lineage.server.model.Instance;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.serverpackets.S_NPCPack_D;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DwarfInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1DwarfInstance.class);
    private static final long serialVersionUID = 1;

    public L1DwarfInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            perceivedFrom.addKnownObject(this);
            perceivedFrom.sendPackets(new S_NPCPack_D(this));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            new L1AttackPc(pc, this).action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance pc) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        int npcId = getNpcTemplate().get_npcId();
        String htmlid = null;
        if (talking != null) {
            if (npcId != 60028) {
                htmlid = "storage";
            } else if (!pc.isElf()) {
                htmlid = "elCE1";
            }
            if (htmlid != null) {
                pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
            } else if (pc.getLevel() < 5) {
                pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
        }
    }

    private int getTemplateid() {
        return 0;
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance pc, String Action) {
        getTemplateid();
        if (!Action.equalsIgnoreCase("retrieve") && Action.equalsIgnoreCase("retrieve-pledge") && pc.getClanname().equalsIgnoreCase(" ")) {
            pc.sendPackets(new S_ServerMessage(208));
        }
    }
}
