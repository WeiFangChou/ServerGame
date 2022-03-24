package com.lineage.data.item_etcitem.shop;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_SkillSound;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserSex extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(UserSex.class);

    private UserSex() {
    }

    public static ItemExecutor get() {
        return new UserSex();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int newSex;
        if (item != null && pc != null) {
            int sex = pc.get_sex();
            pc.getInventory().removeItem(item, 1);
            int newType = -1;
            if (sex == 0) {
                newSex = 1;
                if (pc.isCrown()) {
                    newType = 1;
                } else if (pc.isKnight()) {
                    newType = 48;
                } else if (pc.isElf()) {
                    newType = 37;
                } else if (pc.isWizard()) {
                    newType = L1PcInstance.CLASSID_WIZARD_FEMALE;
                } else if (pc.isDarkelf()) {
                    newType = L1PcInstance.CLASSID_DARK_ELF_FEMALE;
                } else if (pc.isDragonKnight()) {
                    newType = L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE;
                } else if (pc.isIllusionist()) {
                    newType = L1PcInstance.CLASSID_ILLUSIONIST_FEMALE;
                }
            } else {
                newSex = 0;
                if (pc.isCrown()) {
                    newType = 0;
                } else if (pc.isKnight()) {
                    newType = 61;
                } else if (pc.isElf()) {
                    newType = 138;
                } else if (pc.isWizard()) {
                    newType = L1PcInstance.CLASSID_WIZARD_MALE;
                } else if (pc.isDarkelf()) {
                    newType = L1PcInstance.CLASSID_DARK_ELF_MALE;
                } else if (pc.isDragonKnight()) {
                    newType = L1PcInstance.CLASSID_DRAGON_KNIGHT_MALE;
                } else if (pc.isIllusionist()) {
                    newType = 6671;
                }
            }
            try {
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 196));
                Thread.sleep(50);
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 197));
                Thread.sleep(50);
                pc.sendPacketsX8(new S_SkillSound(pc.getId(), 198));
                pc.sendPacketsAll(new S_ChangeShape(pc, newType));
                if (pc.getWeapon() != null) {
                    pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                }
                pc.set_sex(newSex);
                pc.setClassId(newType);
                pc.save();
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
