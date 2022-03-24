package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.LogEnchantStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogEnchantTable implements LogEnchantStorage {
    private static final Log _log = LogFactory.getLog(LogEnchantTable.class);

    @Override // com.lineage.server.datatables.storage.LogEnchantStorage
    public void failureEnchant(L1PcInstance pc, L1ItemInstance item) {
        int i = 0;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `other_enchant` SET `id`=?,`item_id`=?,`char_id`=?,`item_name`=?,`count`=?,`is_equipped`=?,`enchantlvl`=?,`is_id`=?,`durability`=?,`charge_count`=?,`remaining_time`=?,`last_used`=?,`bless`=?,`attr_enchant_kind`=?,`attr_enchant_level`=?,`datetime`=SYSDATE(),`ipmac`=?");
            int i2 = 0 + 1;
            ps.setInt(i2, item.getId());
            int i3 = i2 + 1;
            ps.setInt(i3, item.getItem().getItemId());
            int i4 = i3 + 1;
            ps.setInt(i4, pc.getId());
            int i5 = i4 + 1;
            ps.setString(i5, item.getItem().getName());
            int i6 = i5 + 1;
            ps.setLong(i6, item.getCount());
            int i7 = i6 + 1;
            ps.setInt(i7, 0);
            int i8 = i7 + 1;
            ps.setInt(i8, item.getEnchantLevel());
            int i9 = i8 + 1;
            if (item.isIdentified()) {
                i = 1;
            }
            ps.setInt(i9, i);
            int i10 = i9 + 1;
            ps.setInt(i10, item.get_durability());
            int i11 = i10 + 1;
            ps.setInt(i11, item.getChargeCount());
            int i12 = i11 + 1;
            ps.setInt(i12, item.getRemainingTime());
            int i13 = i12 + 1;
            ps.setTimestamp(i13, item.getLastUsed());
            int i14 = i13 + 1;
            ps.setInt(i14, item.getBless());
            int i15 = i14 + 1;
            ps.setInt(i15, item.getAttrEnchantKind());
            int i16 = i15 + 1;
            ps.setInt(i16, item.getAttrEnchantLevel());
            StringBuilder ip = pc.getNetConnection().getIp();
            ps.setString(i16 + 1, ((Object) ip) + "/" + ((Object) pc.getNetConnection().getMac()));
            ps.execute();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
