package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.OtherUserToPcStorage;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OtherUserToPcTable implements OtherUserToPcStorage {
    private static final Log _log = LogFactory.getLog(OtherUserToPcTable.class);

    @Override // com.lineage.server.datatables.storage.OtherUserToPcStorage
    public void add(String itemname, long itemcount, int pcobjid, String pcname, int srcpcobjid, String srcpcname) {
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `other_topc` SET `itemname`=?,`itemcount`=?,`pcobjid`=?,`pcname`=?,`srcpcobjid`=?,`srcpcname`=?,`datetime`=SYSDATE()");
            int i = 0 + 1;
            ps.setString(i, itemname);
            int i2 = i + 1;
            ps.setLong(i2, itemcount);
            int i3 = i2 + 1;
            ps.setInt(i3, pcobjid);
            int i4 = i3 + 1;
            ps.setString(i4, pcname);
            int i5 = i4 + 1;
            ps.setInt(i5, srcpcobjid);
            ps.setString(i5 + 1, srcpcname);
            ps.execute();
        } catch (Exception e) {
            SqlError.isError(_log, e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }
}
