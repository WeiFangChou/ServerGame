package com.lineage.server.storage.mysql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.CharObjidTable;
import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.storage.CharacterStorage;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MySqlCharacterStorage implements CharacterStorage {
    private static final Log _log = LogFactory.getLog(MySqlCharacterStorage.class);

    @Override // com.lineage.server.storage.CharacterStorage
    public L1PcInstance loadCharacter(String charName) throws Throwable {
        Throwable th;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
            pstm.setString(1, charName);
            rs = pstm.executeQuery();
            if (!rs.next()) {
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
                return null;
            }
            L1PcInstance pc = new L1PcInstance();
            try {
                pc.setAccountName(rs.getString("account_name").toLowerCase());
                int objid = rs.getInt("objid");
                pc.setId(objid);
                pc.set_showId(-1);
                L1PcOther other = CharOtherReading.get().getOther(pc);
                if (other == null) {
                    other = new L1PcOther();
                    other.set_objid(objid);
                }
                pc.set_other(other);
                pc.setName(rs.getString("char_name"));
                pc.setHighLevel(rs.getInt("HighLevel"));
                pc.setExp(rs.getLong("Exp"));
                pc.addBaseMaxHp(rs.getShort("MaxHp"));
                short currentHp = rs.getShort("CurHp");
                if (currentHp < 1) {
                    currentHp = 1;
                }
                pc.setDead(false);
                pc.setCurrentHpDirect(currentHp);
                pc.setStatus(0);
                pc.addBaseMaxMp(rs.getShort("MaxMp"));
                pc.setCurrentMpDirect(rs.getShort("CurMp"));
                pc.addBaseStr(rs.getInt("Str"));
                pc.addBaseCon(rs.getInt("Con"));
                pc.addBaseDex(rs.getInt("Dex"));
                pc.addBaseCha(rs.getInt("Cha"));
                pc.addBaseInt(rs.getInt("Intel"));
                pc.addBaseWis(rs.getInt("Wis"));
                pc.setCurrentWeapon(rs.getInt("Status"));
                int classId = rs.getInt("Class");
                pc.setClassId(classId);
                pc.setTempCharGfx(classId);
                pc.setGfxId(classId);
                pc.set_sex(rs.getInt("Sex"));
                pc.setType(rs.getInt("Type"));
                int head = rs.getInt("Heading");
                if (head > 7) {
                    head = 0;
                }
                pc.setHeading(head);
                pc.setX(rs.getInt("locX"));
                pc.setY(rs.getInt("locY"));
                pc.setMap(rs.getShort("MapID"));
                pc.set_food(rs.getInt("Food"));
                pc.setLawful(rs.getInt("Lawful"));
                pc.setTitle(rs.getString("Title"));
                pc.setClanid(rs.getInt("ClanID"));
                pc.setClanname(rs.getString("Clanname"));
                pc.setClanRank(rs.getInt("ClanRank"));
                pc.setBonusStats(rs.getInt("BonusStatus"));
                pc.setElixirStats(rs.getInt("ElixirStatus"));
                pc.setElfAttr(rs.getInt("ElfAttr"));
                pc.set_PKcount(rs.getInt("PKcount"));
                pc.setPkCountForElf(rs.getInt("PkCountForElf"));
                pc.setExpRes(rs.getInt("ExpRes"));
                pc.setPartnerId(rs.getInt("PartnerID"));
                pc.setAccessLevel(rs.getShort("AccessLevel"));
                if (pc.getAccessLevel() >= 200) {
                    pc.setGm(true);
                    pc.setMonitor(false);
                } else if (pc.getAccessLevel() == 100) {
                    pc.setGm(false);
                    pc.setMonitor(true);
                } else {
                    pc.setGm(false);
                    pc.setMonitor(false);
                }
                pc.setOnlineStatus(rs.getInt("OnlineStatus"));
                pc.setHomeTownId(rs.getInt("HomeTownID"));
                pc.setContribution(rs.getInt("Contribution"));
                pc.setHellTime(rs.getInt("HellTime"));
                pc.setBanned(rs.getBoolean("Banned"));
                pc.setKarma(rs.getInt("Karma"));
                pc.setLastPk(rs.getTimestamp("LastPk"));
                pc.setLastPkForElf(rs.getTimestamp("LastPkForElf"));
                pc.setDeleteTime(rs.getTimestamp("DeleteTime"));
                pc.setOriginalStr(rs.getInt("OriginalStr"));
                pc.setOriginalCon(rs.getInt("OriginalCon"));
                pc.setOriginalDex(rs.getInt("OriginalDex"));
                pc.setOriginalCha(rs.getInt("OriginalCha"));
                pc.setOriginalInt(rs.getInt("OriginalInt"));
                pc.setOriginalWis(rs.getInt("OriginalWis"));
                pc.setRingsExpansion(rs.getByte("RingsExpansion"));
                pc.setDollhole(rs.getInt("doll_hole"));
                pc.refresh();
                pc.setMoveSpeed(0);
                pc.setBraveSpeed(0);
                pc.setGmInvis(false);
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
                return pc;
            } catch (SQLException e) {
                SQLException e1 = e;
                try {
                    _log.error(e1.getLocalizedMessage(), e1);
                    SQLUtil.close(rs);
                    SQLUtil.close(pstm);
                    SQLUtil.close(con);
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    SQLUtil.close(rs);
                    SQLUtil.close(pstm);
                    SQLUtil.close(con);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                SQLUtil.close(rs);
                SQLUtil.close(pstm);
                SQLUtil.close(con);
                throw th;
            }
        } catch (SQLException e2) {
            SQLException e = e2;
            _log.error(e.getLocalizedMessage(), e);
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
            return null;
        }
    }

    @Override // com.lineage.server.storage.CharacterStorage
    public void createCharacter(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("INSERT INTO characters SET account_name=?,objid=?,char_name=?,level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,Con=?,Dex=?,Cha=?,Intel=?,Wis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,PKcount=?,PkCountForElf=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,Pay=?,HellTime=?,Banned=?,Karma=?,LastPk=?,LastPkForElf=?,DeleteTime=?,CreateTime=?,doll_hole=?");
            int i = 0 + 1;
            pstm.setString(i, pc.getAccountName());
            int i2 = i + 1;
            pstm.setInt(i2, pc.getId());
            int i3 = i2 + 1;
            pstm.setString(i3, pc.getName());
            int i4 = i3 + 1;
            pstm.setInt(i4, pc.getLevel());
            int i5 = i4 + 1;
            pstm.setInt(i5, pc.getHighLevel());
            int i6 = i5 + 1;
            pstm.setLong(i6, pc.getExp());
            int i7 = i6 + 1;
            pstm.setInt(i7, pc.getBaseMaxHp());
            int hp = pc.getCurrentHp();
            if (hp < 1) {
                hp = 1;
            }
            int i8 = i7 + 1;
            pstm.setInt(i8, hp);
            int i9 = i8 + 1;
            pstm.setInt(i9, pc.getBaseMaxMp());
            int i10 = i9 + 1;
            pstm.setInt(i10, pc.getCurrentMp());
            int i11 = i10 + 1;
            pstm.setInt(i11, pc.getAc());
            int i12 = i11 + 1;
            pstm.setInt(i12, pc.getBaseStr());
            int i13 = i12 + 1;
            pstm.setInt(i13, pc.getBaseCon());
            int i14 = i13 + 1;
            pstm.setInt(i14, pc.getBaseDex());
            int i15 = i14 + 1;
            pstm.setInt(i15, pc.getBaseCha());
            int i16 = i15 + 1;
            pstm.setInt(i16, pc.getBaseInt());
            int i17 = i16 + 1;
            pstm.setInt(i17, pc.getBaseWis());
            int i18 = i17 + 1;
            pstm.setInt(i18, pc.getCurrentWeapon());
            int i19 = i18 + 1;
            pstm.setInt(i19, pc.getClassId());
            int i20 = i19 + 1;
            pstm.setInt(i20, pc.get_sex());
            int i21 = i20 + 1;
            pstm.setInt(i21, pc.getType());
            int i22 = i21 + 1;
            pstm.setInt(i22, pc.getHeading());
            int i23 = i22 + 1;
            pstm.setInt(i23, pc.getX());
            int i24 = i23 + 1;
            pstm.setInt(i24, pc.getY());
            int i25 = i24 + 1;
            pstm.setInt(i25, pc.getMapId());
            int i26 = i25 + 1;
            pstm.setInt(i26, pc.get_food());
            int i27 = i26 + 1;
            pstm.setInt(i27, pc.getLawful());
            int i28 = i27 + 1;
            pstm.setString(i28, pc.getTitle());
            int i29 = i28 + 1;
            pstm.setInt(i29, pc.getClanid());
            int i30 = i29 + 1;
            pstm.setString(i30, pc.getClanname());
            int i31 = i30 + 1;
            pstm.setInt(i31, pc.getClanRank());
            int i32 = i31 + 1;
            pstm.setInt(i32, pc.getBonusStats());
            int i33 = i32 + 1;
            pstm.setInt(i33, pc.getElixirStats());
            int i34 = i33 + 1;
            pstm.setInt(i34, pc.getElfAttr());
            int i35 = i34 + 1;
            pstm.setInt(i35, pc.get_PKcount());
            int i36 = i35 + 1;
            pstm.setInt(i36, pc.getPkCountForElf());
            int i37 = i36 + 1;
            pstm.setInt(i37, pc.getExpRes());
            int i38 = i37 + 1;
            pstm.setInt(i38, pc.getPartnerId());
            int i39 = i38 + 1;
            pstm.setShort(i39, pc.getAccessLevel());
            int i40 = i39 + 1;
            pstm.setInt(i40, pc.getOnlineStatus());
            int i41 = i40 + 1;
            pstm.setInt(i41, pc.getHomeTownId());
            int i42 = i41 + 1;
            pstm.setInt(i42, pc.getContribution());
            int i43 = i42 + 1;
            pstm.setInt(i43, 0);
            int i44 = i43 + 1;
            pstm.setInt(i44, pc.getHellTime());
            int i45 = i44 + 1;
            pstm.setBoolean(i45, pc.isBanned());
            int i46 = i45 + 1;
            pstm.setInt(i46, pc.getKarma());
            int i47 = i46 + 1;
            pstm.setTimestamp(i47, pc.getLastPk());
            int i48 = i47 + 1;
            pstm.setTimestamp(i48, pc.getLastPkForElf());
            int i49 = i48 + 1;
            pstm.setTimestamp(i49, pc.getDeleteTime());
            int i50 = i49 + 1;
            pstm.setInt(i50, Integer.parseInt(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(System.currentTimeMillis())).replace("-", "")));
            pstm.setInt(i50 + 1, pc.getDollhole());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.storage.CharacterStorage
    public void deleteCharacter(String accountName, String charName) throws Exception {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.get().getConnection();
            PreparedStatement pstm2 = con.prepareStatement("SELECT * FROM characters WHERE account_name=? AND char_name=?");
            pstm2.setString(1, accountName);
            pstm2.setString(2, charName);
            rs = pstm2.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("could not delete character");
            }
            SQLUtil.close(pstm2);
            int objid = CharObjidTable.get().charObjid(charName);
            if (objid != 0) {
                CharItemsReading.get().delUserItems(Integer.valueOf(objid));
            }
            PreparedStatement pstm3 = con.prepareStatement("DELETE FROM character_buddys WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm3.setString(1, charName);
            pstm3.execute();
            PreparedStatement pstm4 = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm4.setString(1, charName);
            pstm4.execute();
            PreparedStatement pstm5 = con.prepareStatement("DELETE FROM character_config WHERE object_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm5.setString(1, charName);
            pstm5.execute();
            PreparedStatement pstm6 = con.prepareStatement("DELETE FROM character_quests WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm6.setString(1, charName);
            pstm6.execute();
            PreparedStatement pstm7 = con.prepareStatement("DELETE FROM character_skills WHERE char_obj_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm7.setString(1, charName);
            pstm7.execute();
            PreparedStatement pstm8 = con.prepareStatement("DELETE FROM character_teleport WHERE char_id IN (SELECT objid FROM characters WHERE char_name = ?)");
            pstm8.setString(1, charName);
            pstm8.execute();
            pstm = con.prepareStatement("DELETE FROM characters WHERE char_name=?");
            pstm.setString(1, charName);
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }

    @Override // com.lineage.server.storage.CharacterStorage
    public void storeCharacter(L1PcInstance pc) {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = DatabaseFactory.get().getConnection();
            pstm = con.prepareStatement("UPDATE characters SET level=?,HighLevel=?,Exp=?,MaxHp=?,CurHp=?,MaxMp=?,CurMp=?,Ac=?,Str=?,Con=?,Dex=?,Cha=?,Intel=?,Wis=?,Status=?,Class=?,Sex=?,Type=?,Heading=?,LocX=?,LocY=?,MapID=?,Food=?,Lawful=?,Title=?,ClanID=?,Clanname=?,ClanRank=?,BonusStatus=?,ElixirStatus=?,ElfAttr=?,PKcount=?,PkCountForElf=?,ExpRes=?,PartnerID=?,AccessLevel=?,OnlineStatus=?,HomeTownID=?,Contribution=?,HellTime=?,Banned=?,Karma=?,LastPk=?,LastPkForElf=?,DeleteTime=?,doll_hole=? WHERE objid=?");
            int i = 0 + 1;
            pstm.setInt(i, pc.getLevel());
            int i2 = i + 1;
            pstm.setInt(i2, pc.getHighLevel());
            int i3 = i2 + 1;
            pstm.setLong(i3, pc.getExp());
            int i4 = i3 + 1;
            pstm.setInt(i4, pc.getBaseMaxHp());
            int hp = pc.getCurrentHp();
            if (hp < 1) {
                hp = 1;
            }
            int i5 = i4 + 1;
            pstm.setInt(i5, hp);
            int i6 = i5 + 1;
            pstm.setInt(i6, pc.getBaseMaxMp());
            int i7 = i6 + 1;
            pstm.setInt(i7, pc.getCurrentMp());
            int i8 = i7 + 1;
            pstm.setInt(i8, pc.getAc());
            int i9 = i8 + 1;
            pstm.setInt(i9, pc.getBaseStr());
            int i10 = i9 + 1;
            pstm.setInt(i10, pc.getBaseCon());
            int i11 = i10 + 1;
            pstm.setInt(i11, pc.getBaseDex());
            int i12 = i11 + 1;
            pstm.setInt(i12, pc.getBaseCha());
            int i13 = i12 + 1;
            pstm.setInt(i13, pc.getBaseInt());
            int i14 = i13 + 1;
            pstm.setInt(i14, pc.getBaseWis());
            int i15 = i14 + 1;
            pstm.setInt(i15, pc.getCurrentWeapon());
            int i16 = i15 + 1;
            pstm.setInt(i16, pc.getClassId());
            int i17 = i16 + 1;
            pstm.setInt(i17, pc.get_sex());
            int i18 = i17 + 1;
            pstm.setInt(i18, pc.getType());
            int i19 = i18 + 1;
            pstm.setInt(i19, pc.getHeading());
            int i20 = i19 + 1;
            pstm.setInt(i20, pc.getX());
            int i21 = i20 + 1;
            pstm.setInt(i21, pc.getY());
            int i22 = i21 + 1;
            pstm.setInt(i22, pc.getMapId());
            int i23 = i22 + 1;
            pstm.setInt(i23, pc.get_food());
            int i24 = i23 + 1;
            pstm.setInt(i24, pc.getLawful());
            int i25 = i24 + 1;
            pstm.setString(i25, pc.getTitle());
            int i26 = i25 + 1;
            pstm.setInt(i26, pc.getClanid());
            int i27 = i26 + 1;
            pstm.setString(i27, pc.getClanname());
            int i28 = i27 + 1;
            pstm.setInt(i28, pc.getClanRank());
            int i29 = i28 + 1;
            pstm.setInt(i29, pc.getBonusStats());
            int i30 = i29 + 1;
            pstm.setInt(i30, pc.getElixirStats());
            int i31 = i30 + 1;
            pstm.setInt(i31, pc.getElfAttr());
            int i32 = i31 + 1;
            pstm.setInt(i32, pc.get_PKcount());
            int i33 = i32 + 1;
            pstm.setInt(i33, pc.getPkCountForElf());
            int i34 = i33 + 1;
            pstm.setInt(i34, pc.getExpRes());
            int i35 = i34 + 1;
            pstm.setInt(i35, pc.getPartnerId());
            short leve = pc.getAccessLevel();
            if (leve >= 20000) {
                leve = 0;
            }
            int i36 = i35 + 1;
            pstm.setShort(i36, leve);
            int i37 = i36 + 1;
            pstm.setInt(i37, pc.getOnlineStatus());
            int i38 = i37 + 1;
            pstm.setInt(i38, pc.getHomeTownId());
            int i39 = i38 + 1;
            pstm.setInt(i39, pc.getContribution());
            int i40 = i39 + 1;
            pstm.setInt(i40, pc.getHellTime());
            int i41 = i40 + 1;
            pstm.setBoolean(i41, pc.isBanned());
            int i42 = i41 + 1;
            pstm.setInt(i42, pc.getKarma());
            int i43 = i42 + 1;
            pstm.setTimestamp(i43, pc.getLastPk());
            int i44 = i43 + 1;
            pstm.setTimestamp(i44, pc.getLastPkForElf());
            int i45 = i44 + 1;
            pstm.setTimestamp(i45, pc.getDeleteTime());
            int i46 = i45 + 1;
            pstm.setInt(i46, pc.getDollhole());
            pstm.setInt(i46 + 1, pc.getId());
            pstm.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(pstm);
            SQLUtil.close(con);
        }
    }
}
