package com.lineage.data.item_etcitem.Poly;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_ServerMessage;

public class XiaNa_Change_Reel extends ItemExecutor {
    private XiaNa_Change_Reel() {
    }

    public static ItemExecutor get() {
        return new XiaNa_Change_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc.getMapId() == 5300) {
            pc.sendPackets(new S_ServerMessage(1170));
        } else if (pc.getMapId() == 9000) {
            pc.sendPackets(new S_ServerMessage(1170));
        } else if (pc.getMapId() == 9100) {
            pc.sendPackets(new S_ServerMessage(1170));
        } else {
            usePolyPotion(pc, item.getItemId());
            pc.getInventory().removeItem(item, 1);
        }
    }

    private void usePolyPotion(L1PcInstance pc, int itemId) {
        int awakeSkillId = pc.getAwakeSkillId();
        if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
            pc.sendPackets(new S_ServerMessage(1384));
            return;
        }
        int polyId = 0;
        if (itemId == 49149 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6822;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6823;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6824;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6825;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6826;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6827;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6828;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6829;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6830;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6831;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7139;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7140;
        } else if (itemId == 49149 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7141;
        } else if (itemId == 49149 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7142;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6832;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6833;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6834;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6835;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6836;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6837;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6838;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6839;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6840;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6841;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7143;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7144;
        } else if (itemId == 49150 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7145;
        } else if (itemId == 49150 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7146;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6842;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6843;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6844;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6845;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6846;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6847;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6848;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6849;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6850;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6851;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7147;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7148;
        } else if (itemId == 49151 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7149;
        } else if (itemId == 49151 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7150;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6852;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6853;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6854;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6855;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6856;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6857;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6858;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6859;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6860;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6861;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7151;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7152;
        } else if (itemId == 49152 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7153;
        } else if (itemId == 49152 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7154;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6862;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6863;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6864;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6865;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6866;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6867;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6868;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6869;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6870;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6871;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7155;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7156;
        } else if (itemId == 49153 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7157;
        } else if (itemId == 49153 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7158;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6872;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6873;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6874;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6875;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6876;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6877;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6878;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6879;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6880;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6881;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7159;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7160;
        } else if (itemId == 49154 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7161;
        } else if (itemId == 49154 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7162;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isCrown()) {
            polyId = 6882;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isCrown()) {
            polyId = 6883;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isKnight()) {
            polyId = 6884;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isKnight()) {
            polyId = 6885;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isElf()) {
            polyId = 6886;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isElf()) {
            polyId = 6887;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isWizard()) {
            polyId = 6888;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isWizard()) {
            polyId = 6889;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isDarkelf()) {
            polyId = 6890;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isDarkelf()) {
            polyId = 6891;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isDragonKnight()) {
            polyId = 7163;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isDragonKnight()) {
            polyId = 7164;
        } else if (itemId == 49155 && pc.get_sex() == 0 && pc.isIllusionist()) {
            polyId = 7165;
        } else if (itemId == 49155 && pc.get_sex() == 1 && pc.isIllusionist()) {
            polyId = 7166;
        }
        L1PolyMorph.doPoly(pc, polyId, 1800, 1);
    }
}
