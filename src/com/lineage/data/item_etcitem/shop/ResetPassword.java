package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResetPassword extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(ResetPassword.class);
    public static final Random _random = new Random();

    private ResetPassword() {
    }

    public static ItemExecutor get() {
        return new ResetPassword();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (item != null && pc != null) {
            try {
                pc.repass(1);
                pc.getInventory().removeItem(item, 1);
                pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "y_pass_01", new String[]{"請輸入您的舊密碼"}));
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
