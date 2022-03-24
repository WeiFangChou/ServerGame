package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.ServerGmCommandStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerGmCommandTable implements ServerGmCommandStorage {
    private static final Log _log = LogFactory.getLog(ServerGmCommandTable.class);

    @Override // com.lineage.server.datatables.storage.ServerGmCommandStorage
    public void create(L1PcInstance pc, String cmd) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `other_gmcommand` SET `gmobjid`=?,`gmname`=?,`cmd`=?,`time`=?");
            if (pc == null) {
                int i = 0 + 1;
                ps.setInt(i, 0);
                int i2 = i + 1;
                ps.setString(i2, "--視窗命令--");
                int i3 = i2 + 1;
                ps.setString(i3, cmd);
                ps.setTimestamp(i3 + 1, lastactive);
            } else {
                int i4 = 0 + 1;
                ps.setInt(i4, pc.getId());
                int i5 = i4 + 1;
                ps.setString(i5, pc.getName());
                int i6 = i5 + 1;
                ps.setString(i6, cmd);
                ps.setTimestamp(i6 + 1, lastactive);
                _log.info("建立GM指令使用紀錄: " + pc.getName() + " " + cmd);
            }
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
