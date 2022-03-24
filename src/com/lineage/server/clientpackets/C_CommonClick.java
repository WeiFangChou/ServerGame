package com.lineage.server.clientpackets;

import com.lineage.DatabaseFactory;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1Clan;
import com.lineage.server.serverpackets.S_CharAmount;
import com.lineage.server.serverpackets.S_CharPacks;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.world.WorldClan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_CommonClick extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_CommonClick.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            deleteCharacter(client);
            int amountOfChars = client.getAccount().get_countCharacters();
            client.out().encrypt(new S_CharAmount(amountOfChars, client));
            if (amountOfChars > 0) {
                sendCharPacks(client);
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void deleteCharacter(ClientExecutor client) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.get().getConnection();
            pstm = conn.prepareStatement("SELECT * FROM `characters` WHERE `account_name`=? ORDER BY `objid`");
            pstm.setString(1, client.getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                String name = rs.getString("char_name");
                String clanname = rs.getString("Clanname");
                Timestamp deleteTime = rs.getTimestamp("DeleteTime");
                if (deleteTime != null && ((Calendar.getInstance().getTimeInMillis() - deleteTime.getTime()) / 1000) / 3600 >= 0) {
                    L1Clan clan = WorldClan.get().getClan(clanname);
                    if (clan != null) {
                        clan.delMemberName(name);
                    }
                    client.getAccount().set_countCharacters(client.getAccount().get_countCharacters() - 1);
                    CharObjidTable.get().charRemove(name);
                    CharacterTable.get().deleteCharacter(client.getAccountName(), name);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
    }

    private void sendCharPacks(ClientExecutor client) {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            conn = DatabaseFactory.get().getConnection();
            pstm = conn.prepareStatement("SELECT * FROM `characters` WHERE `account_name`=? ORDER BY `objid`");
            pstm.setString(1, client.getAccountName());
            rs = pstm.executeQuery();
            while (rs.next()) {
                String name = rs.getString("char_name");
                String clanname = rs.getString("Clanname");
                int type = rs.getInt("Type");
                byte sex = rs.getByte("Sex");
                int lawful = rs.getInt("Lawful");
                int currenthp = rs.getInt("CurHp");
                if (currenthp < 1) {
                    currenthp = 1;
                } else if (currenthp > 32767) {
                    currenthp = 32767;
                }
                int currentmp = rs.getInt("CurMp");
                if (currentmp < 1) {
                    currentmp = 1;
                } else if (currentmp > 32767) {
                    currentmp = 32767;
                }
                int lvl = rs.getInt("level");
                if (lvl < 1) {
                    lvl = 1;
                }
                client.out().encrypt(new S_CharPacks(name, clanname, type, sex, lawful, currenthp, currentmp, rs.getInt("Ac"), lvl, rs.getInt("Str"), rs.getInt("Dex"), rs.getInt("Con"), rs.getInt("Wis"), rs.getInt("Cha"), rs.getInt("Intel"), rs.getInt("CreateTime")));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(conn);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
