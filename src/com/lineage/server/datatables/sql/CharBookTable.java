package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactoryLogin;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.CharBookStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Bookmarks;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1BookMark;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharBookTable implements CharBookStorage {
    private static final Map<Integer, ArrayList<L1BookMark>> _bookmarkMap = new HashMap();
    private static final Log _log = LogFactory.getLog(CharBookTable.class);

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `character_teleport`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int char_id = rs.getInt("char_id");
                if (CharObjidTable.get().isChar(char_id) != null) {
                    String name = rs.getString("name");
                    int locx = rs.getInt("locx");
                    int locy = rs.getInt("locy");
                    int mapid = rs.getShort("mapid");
                    L1BookMark bookmark = new L1BookMark();
                    bookmark.setId(id);
                    bookmark.setCharId(char_id);
                    bookmark.setName(name);
                    bookmark.setLocX(locx);
                    bookmark.setLocY(locy);
                    bookmark.setMapId(mapid);
                    addMap(char_id, bookmark);
                } else {
                    delete(char_id);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入記憶座標紀錄資料數量: " + _bookmarkMap.size() + "(" + timer.get() + "ms)");
    }

    private static void delete(int objid) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactoryLogin.get().getConnection();
            ps = cn.prepareStatement("DELETE FROM `character_teleport` WHERE `char_id`=?");
            ps.setInt(1, objid);
            ps.execute();
            _bookmarkMap.remove(Integer.valueOf(objid));
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private static void addMap(int objId, L1BookMark bookmark) {
        ArrayList<L1BookMark> bookmarks = _bookmarkMap.get(Integer.valueOf(objId));
        if (L1TownLocation.isGambling(bookmark.getLocX(), bookmark.getLocY(), bookmark.getMapId())) {
            deleteBookmark(bookmark.getId());
        } else if (bookmarks == null) {
            ArrayList<L1BookMark> bookmarks2 = new ArrayList<>();
            bookmarks2.add(bookmark);
            _bookmarkMap.put(Integer.valueOf(objId), bookmarks2);
        } else {
            bookmarks.add(bookmark);
        }
    }

    private static void deleteBookmark(int id) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `character_teleport` WHERE `id`=?");
            pstm.setInt(1, id);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public ArrayList<L1BookMark> getBookMarks(L1PcInstance pc) {
        return _bookmarkMap.get(Integer.valueOf(pc.getId()));
    }

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public L1BookMark getBookMark(L1PcInstance pc, int i) {
        ArrayList<L1BookMark> bookmarks = _bookmarkMap.get(Integer.valueOf(pc.getId()));
        if (bookmarks != null) {
            int x = i & 65535;
            int y = (i >> 16) & 65535;
            Iterator<L1BookMark> it = bookmarks.iterator();
            while (it.hasNext()) {
                L1BookMark book = it.next();
                if (book.getLocX() == x && book.getLocY() == y) {
                    return book;
                }
            }
        }
        return null;
    }

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public void deleteBookmark(L1PcInstance pc, String s) {
        ArrayList<L1BookMark> bookmarks = _bookmarkMap.get(Integer.valueOf(pc.getId()));
        if (bookmarks != null) {
            Iterator<L1BookMark> it = bookmarks.iterator();
            while (it.hasNext()) {
                L1BookMark book = it.next();
                if (book.getName().equalsIgnoreCase(s)) {
                    Connection con = null;
                    PreparedStatement pstm = null;
                    try {
                        con = DatabaseFactoryLogin.get().getConnection();
                        pstm = con.prepareStatement("DELETE FROM `character_teleport` WHERE `id`=?");
                        pstm.setInt(1, book.getId());
                        pstm.execute();
                        bookmarks.remove(book);
                    } catch (SQLException e) {
                        _log.error(e.getLocalizedMessage(), e);
                    } finally {
                        SQLUtil.close(pstm);
                        SQLUtil.close(con);
                    }
                }
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public void addBookmark(L1PcInstance pc, String s) {
        if (!pc.getMap().isMarkable()) {
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.ILLUSION_DIA_GOLEM));
            return;
        }
        ArrayList<L1BookMark> bookmarks = _bookmarkMap.get(Integer.valueOf(pc.getId()));
        if (bookmarks == null) {
            bookmarks = new ArrayList<>();
            _bookmarkMap.put(Integer.valueOf(pc.getId()), bookmarks);
        }
        if (bookmarks.size() > 60) {
            pc.sendPackets(new S_ServerMessage(676));
            return;
        }
        Iterator<L1BookMark> it = bookmarks.iterator();
        while (it.hasNext()) {
            if (it.next().getName().equalsIgnoreCase(s)) {
                pc.sendPackets(new S_ServerMessage(327));
                return;
            }
        }
        L1BookMark book = new L1BookMark();
        book.setId(IdFactory.get().nextId());
        book.setCharId(pc.getId());
        book.setName(s);
        book.setLocX(pc.getX());
        book.setLocY(pc.getY());
        book.setMapId(pc.getMapId());
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO `character_teleport` SET `id`=?,`char_id`=?,`name`=?,`locx`=?,`locy`=?,`mapid`=?");
            pstm.setInt(1, book.getId());
            pstm.setInt(2, book.getCharId());
            pstm.setString(3, book.getName());
            pstm.setInt(4, book.getLocX());
            pstm.setInt(5, book.getLocY());
            pstm.setInt(6, book.getMapId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        bookmarks.add(book);
        pc.sendPackets(new S_Bookmarks(s, book.getMapId(), book.getLocX(), book.getLocY(), book.getId()));
    }

    @Override // com.lineage.server.datatables.storage.CharBookStorage
    public void updateBookmarkName(L1BookMark bookmark) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactoryLogin.get().getConnection();
            pstm = con.prepareStatement("UPDATE `character_teleport` SET `name`=? WHERE `id`=?");
            pstm.setString(1, bookmark.getName());
            pstm.setInt(2, bookmark.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
