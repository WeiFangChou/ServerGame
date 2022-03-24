package com.lineage.data.item_armor;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MaiteSi_Earring extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(MaiteSi_Earring.class);
    private int _up_hp_potion = 0;

    private MaiteSi_Earring() {
    }

    public static ItemExecutor get() {
        return new MaiteSi_Earring();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                switch (data[0]) {
                    case 0:
                        pc.set_up_hp_potion(pc.get_up_hp_potion() - this._up_hp_potion);
                        return;
                    case 1:
                        pc.set_up_hp_potion(pc.get_up_hp_potion() + this._up_hp_potion);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._up_hp_potion = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
