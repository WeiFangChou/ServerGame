package com.lineage.data.item_armor;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ElitePlateMail_Antharas extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(ElitePlateMail_Antharas.class);

    private ElitePlateMail_Antharas() {
    }

    public static ItemExecutor get() {
        return new ElitePlateMail_Antharas();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                switch (data[0]) {
                    case 0:
                        pc.set_venom_resist(-1);
                        return;
                    case 1:
                        pc.set_venom_resist(1);
                        return;
                    default:
                        return;
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
