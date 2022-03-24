package com.lineage.server.datatables.sql;

import com.lineage.DatabaseFactory;
import com.lineage.server.datatables.storage.CharShiftingStorage;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Item;
import com.lineage.server.utils.SQLUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CharShiftingTable implements CharShiftingStorage {
    private static final Log _log = LogFactory.getLog(CharShiftingTable.class);

    @Override // com.lineage.server.datatables.storage.CharShiftingStorage
    public void newShifting(L1PcInstance pc, int tgId, String tgName, int srcObjid, L1Item srcItem, L1ItemInstance newItem, int mode) {
        int i;
        int i2;
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `other_shifting` SET `srcObjid`=?,`srcItemid`=?,`srcName`=?,`newObjid`=?,`newItemid`=?,`newName`=?,`enchantLevel`=?,`attrEnchant`=?,`weaponSkill`=?,`pcObjid`=?,`pcName`=?,`tgPcObjid`=?,`tgPcName`=?,`time`=?,`note`=?");
            if (srcItem != null) {
                int i3 = 0 + 1;
                ps.setInt(i3, srcObjid);
                int i4 = i3 + 1;
                ps.setInt(i4, srcItem.getItemId());
                i = i4 + 1;
                ps.setString(i, srcItem.getName());
            } else {
                int i5 = 0 + 1;
                ps.setInt(i5, 0);
                int i6 = i5 + 1;
                ps.setInt(i6, 0);
                i = i6 + 1;
                ps.setString(i, "");
            }
            int i7 = i + 1;
            ps.setInt(i7, newItem.getId());
            int i8 = i7 + 1;
            ps.setInt(i8, newItem.getItemId());
            int i9 = i8 + 1;
            ps.setString(i9, newItem.getItem().getName());
            int i10 = i9 + 1;
            ps.setInt(i10, newItem.getEnchantLevel());
            int i11 = i10 + 1;
            ps.setString(i11, String.valueOf(newItem.getAttrEnchantKind()) + "/" + newItem.getAttrEnchantLevel());
            StringBuilder cnSkillInfo = new StringBuilder();
            if (cnSkillInfo.length() > 0) {
                i2 = i11 + 1;
                ps.setString(i2, cnSkillInfo.toString());
            } else {
                i2 = i11 + 1;
                ps.setString(i2, "無效果");
            }
            int i12 = i2 + 1;
            ps.setInt(i12, pc.getId());
            int i13 = i12 + 1;
            ps.setString(i13, pc.getName());
            Timestamp lastactive = new Timestamp(System.currentTimeMillis());
            switch (mode) {
                case 0:
                    int i14 = i13 + 1;
                    ps.setInt(i14, 0);
                    int i15 = i14 + 1;
                    ps.setString(i15, "");
                    int i16 = i15 + 1;
                    ps.setTimestamp(i16, lastactive);
                    ps.setString(i16 + 1, "交換裝備");
                    break;
                case 1:
                    int i17 = i13 + 1;
                    ps.setInt(i17, 0);
                    int i18 = i17 + 1;
                    ps.setString(i18, "");
                    int i19 = i18 + 1;
                    ps.setTimestamp(i19, lastactive);
                    ps.setString(i19 + 1, "裝備升級");
                    break;
                case 2:
                    int i20 = i13 + 1;
                    ps.setInt(i20, tgId);
                    int i21 = i20 + 1;
                    ps.setString(i21, tgName);
                    int i22 = i21 + 1;
                    ps.setTimestamp(i22, lastactive);
                    ps.setString(i22 + 1, "轉移裝備");
                    break;
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
