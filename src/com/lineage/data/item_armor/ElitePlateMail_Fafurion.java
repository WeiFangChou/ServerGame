package com.lineage.data.item_armor;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ElitePlateMail_Fafurion extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(ElitePlateMail_Fafurion.class);
    private int _hp_max = 0;
    private int _hp_min = 0;

    /* renamed from: _r */
    private int f4_r = 0;

    private ElitePlateMail_Fafurion() {
    }

    public static ItemExecutor get() {
        return new ElitePlateMail_Fafurion();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                switch (data[0]) {
                    case 0:
                        pc.set_elitePlateMail_Fafurion(0, 0, 0);
                        return;
                    case 1:
                        pc.set_elitePlateMail_Fafurion(this.f4_r, this._hp_min, this._hp_max);
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
            this.f4_r = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
        try {
            this._hp_min = Integer.parseInt(set[2]);
        } catch (Exception ignored) {
        }
        try {
            this._hp_max = Integer.parseInt(set[3]);
        } catch (Exception ignored) {
        }
    }
}
