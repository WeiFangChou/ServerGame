package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherUserBuyStorage;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OtherUserBuyTable implements OtherUserBuyStorage {
    private static final Log _log = LogFactory.getLog(OtherUserBuyTable.class);

    @Override // com.lineage.server.datatables.storage.OtherUserBuyStorage
    public void add(String itemname, int itemobjid, int itemadena, long itemcount, int pcobjid, String pcname, int srcpcobjid, String srcpcname) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_pcbuy` SET `itemname`=?,`itemobjid`=?,`itemadena`=?,`itemcount`=?,`pcobjid`=?,`pcname`=?,`srcpcobjid`=?,`srcpcname`=?,`datetime`=SYSDATE()");
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
            ps.setString(i6, String.valueOf(pcname) + "(買家)");
            int i7 = i6 + 1;
            ps.setInt(i7, srcpcobjid);
            ps.setString(i7 + 1, String.valueOf(srcpcname) + "(賣家-商店)");
            ps.execute();
        } catch (Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
