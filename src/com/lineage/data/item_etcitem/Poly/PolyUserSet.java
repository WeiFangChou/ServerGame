package com.lineage.data.item_etcitem.Poly;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PolyUserSet extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(PolyUserSet.class);
    private int _polyid = -1;
    private int _time = 1800;

    private PolyUserSet() {
    }

    public static ItemExecutor get() {
        return new PolyUserSet();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
            pc.sendPackets(new S_ServerMessage(1384));
        } else if (this._polyid == -1) {
            _log.error("自定義變身捲軸 設定錯誤: " + item.getItemId() + " 沒有變身代號!");
        } else {
            pc.getInventory().removeItem(item, 1);
            L1PolyMorph.doPoly(pc, this._polyid, this._time, 1);
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._polyid = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
        try {
            this._time = Integer.parseInt(set[2]);
        } catch (Exception ignored) {
        }
    }
}
