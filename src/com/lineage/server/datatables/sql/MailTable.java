package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.config.Config;
import com.lineage.server.IdFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.storage.MailStorage;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Mail;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailTable implements MailStorage {
    private static final Map<Integer, L1Mail> _allMail = new HashMap();
    private static final Log _log = LogFactory.getLog(MailTable.class);

    @Override // com.lineage.server.datatables.storage.MailStorage
    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM `character_mail`");
            rs = pstm.executeQuery();
            while (rs.next()) {
                L1Mail mail = new L1Mail();
                int id = rs.getInt("id");
                mail.setId(id);
                mail.setType(rs.getInt("type"));
                mail.setSenderName(rs.getString("sender"));
                String receiver = rs.getString("receiver");
                mail.setReceiverName(receiver);
                mail.setDate(rs.getString("date"));
                mail.setReadStatus(rs.getInt("read_status"));
                mail.setSubject(rs.getBytes("subject"));
                mail.setContent(rs.getBytes("content"));
                if (CharObjidTable.get().charObjid(receiver) != 0) {
                    _allMail.put(Integer.valueOf(id), mail);
                } else {
                    deleteMail(receiver);
                }
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
        _log.info("載入信件資料數量: " + _allMail.size() + "(" + timer.get() + "ms)");
    }

    private void deleteMail(String receiver) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("DELETE FROM `character_mail` WHERE `receiver`=?");
            pstm.setString(1, receiver);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public void setReadStatus(int mailId) {
        L1Mail mail = _allMail.get(Integer.valueOf(mailId));
        if (mail != null) {
            mail.setReadStatus(1);
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                rs = con.createStatement().executeQuery("SELECT * FROM `character_mail` WHERE `id`=" + mailId);
                if (rs != null && rs.next()) {
                    pstm = con.prepareStatement("UPDATE `character_mail` SET `read_status`=? WHERE `id`=" + mailId);
                    pstm.setInt(1, 1);
                    pstm.execute();
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public void setMailType(int mailId, int type) {
        L1Mail mail = _allMail.get(Integer.valueOf(mailId));
        if (mail != null) {
            mail.setType(type);
            Connection con = null;
            PreparedStatement pstm = null;
            ResultSet rs = null;
            try {
                con = DatabaseFactory.get().getConnection();
                rs = con.createStatement().executeQuery("SELECT * FROM `character_mail` WHERE `id`=" + mailId);
                if (rs != null && rs.next()) {
                    pstm = con.prepareStatement("UPDATE `character_mail` SET `type`=? WHERE `id`=" + mailId);
                    pstm.setInt(1, type);
                    pstm.execute();
                }
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public void deleteMail(int mailId) {
        if (_allMail.remove(Integer.valueOf(mailId)) != null) {
            Connection con = null;
            PreparedStatement pstm = null;
            try {
                con = DatabaseFactory.get().getConnection();
                pstm = con.prepareStatement("DELETE FROM `character_mail` WHERE `id`=?");
                pstm.setInt(1, mailId);
                pstm.execute();
            } catch (SQLException e) {
                _log.error(e.getLocalizedMessage(), e);
            } finally {
                SQLUtil.close(pstm);
                SQLUtil.close(con);
            }
        }
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public void writeMail(int type, String receiver, L1PcInstance writer, byte[] text) {
        String date = new SimpleDateFormat("yy/MM/dd").format(Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE)).getTime());
        int spacePosition1 = 0;
        int spacePosition2 = 0;
        int i = 0;
        while (true) {
            if (i >= text.length) {
                break;
            }
            if (text[i] == 0 && text[i + 1] == 0) {
                if (spacePosition1 != 0) {
                    if (spacePosition1 != 0 && 0 == 0) {
                        spacePosition2 = i;
                        break;
                    }
                } else {
                    spacePosition1 = i;
                }
            }
            i += 2;
        }
        int subjectLength = spacePosition1 + 2;
        int contentLength = spacePosition2 - spacePosition1;
        if (contentLength <= 0) {
            contentLength = 1;
        }
        byte[] subject = new byte[subjectLength];
        byte[] content = new byte[contentLength];
        System.arraycopy(text, 0, subject, 0, subjectLength);
        System.arraycopy(text, subjectLength, content, 0, contentLength);
        Connection con = null;
        PreparedStatement pstm2 = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm2 = con.prepareStatement("INSERT INTO `character_mail` SET `id`=?,`type`=?,`sender`=?,`receiver`=?,`date`=?,`read_status`=?,`subject`=?,`content`=?");
            int id = IdFactory.get().nextId();
            pstm2.setInt(1, id);
            pstm2.setInt(2, type);
            pstm2.setString(3, writer.getName());
            pstm2.setString(4, receiver);
            pstm2.setString(5, date);
            pstm2.setInt(6, 0);
            pstm2.setBytes(7, subject);
            pstm2.setBytes(8, content);
            pstm2.execute();
            L1Mail mail = new L1Mail();
            mail.setId(id);
            mail.setType(type);
            mail.setSenderName(writer.getName());
            mail.setReceiverName(receiver);
            mail.setDate(date);
            mail.setSubject(subject);
            mail.setContent(content);
            mail.setReadStatus(0);
            _allMail.put(Integer.valueOf(id), mail);
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm2);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public Map<Integer, L1Mail> getAllMail() {
        return _allMail;
    }

    @Override // com.lineage.server.datatables.storage.MailStorage
    public L1Mail getMail(int mailId) {
        return _allMail.get(Integer.valueOf(mailId));
    }
}
