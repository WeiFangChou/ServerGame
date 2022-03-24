package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.ServerCnInfoStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerCnInfoTable implements ServerCnInfoStorage {
    private static final Log _log = LogFactory.getLog(ServerCnInfoTable.class);

    @Override // com.lineage.server.datatables.storage.ServerCnInfoStorage
    public void create(L1PcInstance pc, L1Item itemtmp, int count) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `other_cn_shop` SET `itemname`=?,`itemid`=?,`selling_price`=?,`time`=?,`pcobjid`=?");
            String pcinfo = "(玩家)";
            if (pc.isGm()) {
                pcinfo = "(管理者)";
            }
            int i = 0 + 1;
            pstm.setString(i, String.valueOf(itemtmp.getName()) + pcinfo);
            int i2 = i + 1;
            pstm.setInt(i2, itemtmp.getItemId());
            int i3 = i2 + 1;
            pstm.setInt(i3, count);
            int i4 = i3 + 1;
            pstm.setTimestamp(i4, lastactive);
            pstm.setInt(i4 + 1, pc.getId());
            pstm.execute();
            _log.info("建立天寶購買紀錄: " + pc.getName() + " " + itemtmp.getName() + " " + count);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
