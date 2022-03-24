package com.lineage.data.item_etcitem.html;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;

public class Lashistory extends ItemExecutor {
    private Lashistory() {
    }

    public static ItemExecutor get() {
        return new Lashistory();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        switch (item.getItem().getItemId()) {
            case 41019:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory1"));
                return;
            case 41020:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory2"));
                return;
            case 41021:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory3"));
                return;
            case 41022:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory4"));
                return;
            case 41023:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory5"));
                return;
            case 41024:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory6"));
                return;
            case 41025:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory7"));
                return;
            case 41026:
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "lashistory8"));
                return;
            default:
                return;
        }
    }
}
