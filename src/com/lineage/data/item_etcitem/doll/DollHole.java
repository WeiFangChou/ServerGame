package com.lineage.data.item_etcitem.doll;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DollHole extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(DollHole.class);

    /* renamed from: _a */
    private int f5_a = 0;

    public static ItemExecutor get() {
        return new DollHole();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        switch (this.f5_a) {
            case 1:
                if (pc.getDollhole() != 1 && pc.getDollhole() != 2) {
                    pc.setDollhole(1);
                    pc.sendPackets(new S_SystemMessage("你擴充了第二隻娃娃空間了。"));
                    pc.getInventory().removeItem(item, 1);
                    break;
                } else {
                    pc.sendPackets(new S_SystemMessage("您已經擴充過第二隻娃娃空間了。"));
                    return;
                }
            case 2:
                if (pc.getDollhole() != 2) {
                    if (pc.getDollhole() != 0) {
                        pc.setDollhole(2);
                        pc.sendPackets(new S_SystemMessage("你擴充了第三隻娃娃空間了。"));
                        pc.getInventory().removeItem(item, 1);
                        break;
                    } else {
                        pc.sendPackets(new S_SystemMessage("你還沒擴充第二隻娃娃，無法擴充第三隻娃娃。"));
                        return;
                    }
                } else {
                    pc.sendPackets(new S_SystemMessage("您已經擴充過第三隻娃娃空間了。"));
                    return;
                }
        }
        try {
            pc.save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this.f5_a = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }
}
