package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.BoardStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Board;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BoardTable implements BoardStorage {
    private static final Map<Integer, L1Board> _boards = new HashMap();
    private static final Log _log = LogFactory.getLog(BoardTable.class);
    private static int _maxid = 0;

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection co = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("SELECT * FROM `server_board` ORDER BY `id`");
            rs = ps.executeQuery();
            while (rs.next()) {
                L1Board board = new L1Board();
                int id = rs.getInt("id");
                if (id > _maxid) {
                    _maxid = id;
                }
                board.set_id(id);
                board.set_name(rs.getString("name"));
                board.set_date(rs.getString("date"));
                board.set_title(rs.getString("title"));
                board.set_content(rs.getString("content"));
                _boards.put(Integer.valueOf(board.get_id()), board);
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
        _log.info("載入佈告欄資料數量: " + _boards.size() + "(" + timer.get() + "ms)");
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public Map<Integer, L1Board> getBoardMap() {
        return _boards;
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public L1Board[] getBoardTableList() {
        return (L1Board[]) _boards.values().toArray(new L1Board[_boards.size()]);
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public L1Board getBoardTable(int houseId) {
        return _boards.get(Integer.valueOf(houseId));
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public int getMaxId() {
        return _maxid;
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public void writeTopic(L1PcInstance pc, String date, String title, String content) {
        L1Board board = new L1Board();
        int i = _maxid + 1;
        _maxid = i;
        board.set_id(i);
        board.set_name(pc.getName());
        board.set_date(date);
        board.set_title(title);
        board.set_content(content);
        _boards.put(Integer.valueOf(board.get_id()), board);
        Connection co = null;
        PreparedStatement ps = null;
        try {
            co = DatabaseFactory.get().getConnection();
            ps = co.prepareStatement("INSERT INTO `server_board` SET `id`=?,`name`=?,`date`=?,`title`=?,`content`=?");
            ps.setInt(1, board.get_id());
            ps.setString(2, board.get_name());
            ps.setString(3, board.get_date());
            ps.setString(4, board.get_title());
            ps.setString(5, board.get_content());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close((ResultSet) null);
            SQLUtil.close(ps);
            SQLUtil.close(co);
        }
    }

    @Override // com.lineage.server.datatables.storage.BoardStorage
    public void deleteTopic(int number) {
        L1Board board = _boards.get(Integer.valueOf(number));
        if (board != null) {
            Connection co = null;
            PreparedStatement ps = null;
            try {
                co = DatabaseFactory.get().getConnection();
                ps = co.prepareStatement("DELETE FROM `server_board` WHERE `id`=?");
                ps.setInt(1, board.get_id());
                ps.execute();
                _boards.remove(Integer.valueOf(number));
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(ps);
                SQLUtil.close(co);
            }
        }
    }
}
