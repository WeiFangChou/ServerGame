package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;

public class Sosc_PolyReel extends ItemExecutor {
    private Sosc_PolyReel() {
    }

    public static ItemExecutor get() {
        return new Sosc_PolyReel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        String text = pc.getText();
        if (text != null) {
            pc.setText(null);
            int time = 1800;
            if (item.getBless() == 0) {
                time = 2100;
            }
            if (item.getBless() == 128) {
                time = 2100;
            }
            int awakeSkillId = pc.getAwakeSkillId();
            if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
                pc.sendPackets(new S_ServerMessage(1384));
                return;
            }
            L1PolyMorph poly = PolyTable.get().getTemplate(text);
            if (poly != null || text.equals("")) {
                if (text.equals("")) {
                    pc.removeSkillEffect(67);
                } else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
                    L1PolyMorph.doPoly(pc, poly.getPolyId(), time, 1);
                }
                pc.getInventory().removeItem(item, 1);
                return;
            }
            pc.sendPackets(new S_ServerMessage((int) L1SkillId.DRAGON_SKIN));
        }
    }
}
