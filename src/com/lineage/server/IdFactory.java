package com.lineage.server;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.lock.ServerReading;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IdFactory {
    private static IdFactory _instance;
    private static final Log _log = LogFactory.getLog(IdFactory.class);
    private Object _monitor = new Object();
    private AtomicInteger _nextId;

    public static IdFactory get() {
        if (_instance == null) {
            _instance = new IdFactory();
        }
        return _instance;
    }

    public int nextId() {
        int andIncrement;
        synchronized (this._monitor) {
            andIncrement = this._nextId.getAndIncrement();
        }
        return andIncrement;
    }

    public int maxId() {
        int i;
        synchronized (this._monitor) {
            i = this._nextId.get();
        }
        return i;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT MAX(id)+1 AS NEXTID FROM (SELECT `id` FROM `character_items` UNION ALL SELECT `id` FROM `character_warehouse` UNION ALL SELECT `id` FROM `character_elf_warehouse` UNION ALL SELECT `id` FROM `clan_warehouse` UNION ALL SELECT `id` FROM `character_shopinfo` UNION ALL SELECT `objid` AS `id` FROM `characters` UNION ALL SELECT `clan_id` AS `id` FROM `clan_data` UNION ALL SELECT `id` FROM `character_teleport` UNION ALL SELECT `id` FROM `character_mail` UNION ALL SELECT `objid` AS `id` FROM `character_pets`) t");
            rs = ps.executeQuery();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt("nextid");
            }
            if (id < ServerReading.get().minId()) {
                id = ServerReading.get().minId();
            }
            if (id < ServerReading.get().maxId()) {
                id = ServerReading.get().maxId();
            }
            this._nextId = new AtomicInteger(id);
            _log.info("載入已用最大id編號: " + id + "(" + timer.get() + "ms)");
        } catch (SQLException e) {
            _log.error("id數據加載異常!", e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }
}
