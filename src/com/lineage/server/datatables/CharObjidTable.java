package com.lineage.server.datatables;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharObjidTable {
    private static CharObjidTable _instance;
    private static final Log _log = LogFactory.getLog(CharObjidTable.class);
    private static final Map<Integer, String> _objClanList = new HashMap();
    private static final Map<Integer, String> _objList = new HashMap();
    private static final Map<Integer, String> _pc_objList = new HashMap();

    public static CharObjidTable get() {
        if (_instance == null) {
            _instance = new CharObjidTable();
        }
        return _instance;
    }

    public void load() {
        loadPc();
        loadClan();
    }

    private void loadClan() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `clan_data`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int clan_id = rs.getInt("clan_id");
                _objClanList.put(Integer.valueOf(clan_id), rs.getString("clan_name"));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void loadPc() {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `characters`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int objid = rs.getInt("objid");
                _objList.put(Integer.valueOf(objid), rs.getString("char_name"));
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public void reChar(int objid, String char_name) {
        _objList.put(Integer.valueOf(objid), char_name);
    }

    public void addChar(int objid, String char_name) {
        if (charObjid(char_name) == 0) {
            _objList.put(Integer.valueOf(objid), char_name);
        }
    }

    public String isChar(int objid) {
        String username = _objList.get(Integer.valueOf(objid));
        if (username != null) {
            return username;
        }
        return null;
    }

    public String accounts_isChar(int accounts) {
        String username = _objList.get(Integer.valueOf(accounts));
        if (username != null) {
            return username;
        }
        return null;
    }

    public int charObjid(String char_name) {
        for (Integer integer : _objList.keySet()) {
            if (char_name.equalsIgnoreCase(_objList.get(integer))) {
                return integer.intValue();
            }
        }
        return 0;
    }

    public void charRemove(String char_name) {
        int objid = charObjid(char_name);
        if (_objList.get(Integer.valueOf(objid)) != null) {
            _objList.remove(Integer.valueOf(objid));
        }
    }

    public void addClan(int clan_id, String clan_name) {
        if (clanObjid(clan_name) == 0) {
            _objClanList.put(Integer.valueOf(clan_id), clan_name);
        }
    }

    public boolean isClan(int clan_id) {
        if (_objClanList.get(Integer.valueOf(clan_id)) != null) {
            return true;
        }
        return false;
    }

    public int clanObjid(String clan_name) {
        for (Integer integer : _objClanList.keySet()) {
            if (clan_name.equalsIgnoreCase(_objClanList.get(integer))) {
                return integer.intValue();
            }
        }
        return 0;
    }

    public boolean checkPcObjId(int objid) {
        return _pc_objList.containsKey(Integer.valueOf(objid));
    }
}
