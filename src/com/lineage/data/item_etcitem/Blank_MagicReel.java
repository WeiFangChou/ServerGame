package com.lineage.data.item_etcitem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Skills;

public class Blank_MagicReel extends ItemExecutor {
    private Blank_MagicReel() {
    }

    public static ItemExecutor get() {
        return new Blank_MagicReel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int blanksc_skillid = data[0];
        int itemId = item.getItemId();
        if (!pc.isWizard()) {
            pc.sendPackets(new S_ServerMessage(264));
        } else if ((itemId != 40090 || blanksc_skillid > 7) && ((itemId != 40091 || blanksc_skillid > 15) && ((itemId != 40092 || blanksc_skillid > 22) && ((itemId != 40093 || blanksc_skillid > 31) && (itemId != 40094 || blanksc_skillid > 39))))) {
            pc.sendPackets(new S_ServerMessage(591));
        } else {
            L1ItemInstance spellsc = ItemTable.get().createItem(40859 + blanksc_skillid);
            if (spellsc != null && pc.getInventory().checkAddItem(spellsc, 1) == 0) {
                L1Skills l1skills = SkillsTable.get().getTemplate(blanksc_skillid + 1);
                if (pc.getCurrentHp() <= l1skills.getHpConsume()) {
                    pc.sendPackets(new S_ServerMessage(279));
                } else if (pc.getCurrentMp() < l1skills.getMpConsume()) {
                    pc.sendPackets(new S_ServerMessage(278));
                } else if (l1skills.getItemConsumeId() == 0 || pc.getInventory().checkItem(l1skills.getItemConsumeId(), (long) l1skills.getItemConsumeCount())) {
                    pc.setCurrentHp(pc.getCurrentHp() - l1skills.getHpConsume());
                    pc.setCurrentMp(pc.getCurrentMp() - l1skills.getMpConsume());
                    int lawful = pc.getLawful() + l1skills.getLawful();
                    if (lawful > 32767) {
                        lawful = 32767;
                    }
                    if (lawful < -32767) {
                        lawful = -32767;
                    }
                    pc.setLawful(lawful);
                    if (l1skills.getItemConsumeId() != 0) {
                        pc.getInventory().consumeItem(l1skills.getItemConsumeId(), (long) l1skills.getItemConsumeCount());
                    }
                    pc.getInventory().removeItem(item, 1);
                    pc.getInventory().storeItem(spellsc);
                } else {
                    pc.sendPackets(new S_ServerMessage(299));
                }
            }
        }
    }
}
