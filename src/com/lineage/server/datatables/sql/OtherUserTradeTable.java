package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherUserTradeStorage;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OtherUserTradeTable implements OtherUserTradeStorage {
    private static final Log _log = LogFactory.getLog(OtherUserTradeTable.class);

    @Override // com.lineage.server.datatables.storage.OtherUserTradeStorage
    public void add(String itemname, int itemobjid, int itemadena, long itemcount, int pcobjid, String pcname, int srcpcobjid, String srcpcname) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_pctrade` SET `itemname`=?,`itemobjid`=?,`itemadena`=?,`itemcount`=?,`pcobjid`=?,`pcname`=?,`srcpcobjid`=?,`srcpcname`=?,`datetime`=SYSDATE()");
            int i = 0 + 1;
            ps.setString(i, itemname);
            int i2 = i + 1;
            ps.setInt(i2, itemobjid);
            int i3 = i2 + 1;
            ps.setInt(i3, itemadena);
            int i4 = i3 + 1;
            ps.setLong(i4, itemcount);
            int i5 = i4 + 1;
            ps.setInt(i5, pcobjid);
            int i6 = i5 + 1;
            ps.setString(i6, "移入人物:" + pcname);
            int i7 = i6 + 1;
            ps.setInt(i7, srcpcobjid);
            ps.setString(i7 + 1, "移出人物:" + srcpcname);
            ps.execute();
        } catch (Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
