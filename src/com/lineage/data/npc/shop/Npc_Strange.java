package com.lineage.data.npc.shop;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ShopSellListCn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Strange extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Strange.class);
    private String _htmlid = null;
    private int _itemid = 0;

    private Npc_Strange() {
    }

    public static NpcExecutor get() {
        return new Npc_Strange();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        if (this._htmlid != null) {
            pc.set_temp_adena(this._itemid);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), this._htmlid));
            return;
        }
        pc.set_temp_adena(this._itemid);
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_shop"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (cmd.equalsIgnoreCase("a")) {
            pc.sendPackets(new S_ShopSellListCn(pc, npc));
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void set_set(String[] set) {
        try {
            this._itemid = Integer.parseInt(set[1]);
        } catch (Exception e) {
            _log.error("NPC專屬貨幣設置錯誤:檢查CLASSNAME為Npc_Strange的NPC設置!");
        }
        try {
            this._htmlid = set[2];
        } catch (Exception ignored) {
        }
    }
}
