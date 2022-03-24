package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import java.sql.Timestamp;

public class Unlimited_Quiver extends ItemExecutor {
    private Unlimited_Quiver() {
    }

    public static ItemExecutor get() {
        return new Unlimited_Quiver();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        int count = L1SkillId.STATUS_BRAVE;
        switch ((int) (Math.random() * 100.0d)) {
            case 0:
            case OpcodesClient.C_OPCODE_TITLE /*{ENCODED_INT: 96}*/:
            case 97:
            case L1SkillId.ENCHANT_VENOM /*{ENCODED_INT: 98}*/:
            case 99:
                item_id = 40748;
                break;
            case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
            case 81:
            case 82:
            case 83:
            case 84:
            case ActionCodes.ACTION_ChainswordDamage /*{ENCODED_INT: 85}*/:
            case 86:
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
            case 88:
            case L1SkillId.BOUNCE_ATTACK /*{ENCODED_INT: 89}*/:
                item_id = 40746;
                break;
            case 90:
            case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
            case 92:
            case OpcodesServer.S_OPCODE_POISON /*{ENCODED_INT: 93}*/:
            case 94:
            case 95:
                item_id = 40747;
                break;
            default:
                item_id = 40744;
                count = L1SkillId.STATUS_FREEZE;
                break;
        }
        CreateNewItem.createNewItem(pc, item_id, (long) count);
        item.setLastUsed(new Timestamp(System.currentTimeMillis()));
        pc.getInventory().updateItem(item, 32);
        pc.getInventory().saveItem(item, 32);
    }
}
