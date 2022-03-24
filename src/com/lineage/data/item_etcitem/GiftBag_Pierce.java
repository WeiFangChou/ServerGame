package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;

public class GiftBag_Pierce extends ItemExecutor {
    private GiftBag_Pierce() {
    }

    public static ItemExecutor get() {
        return new GiftBag_Pierce();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int item_id;
        int count = 1;
        switch ((int) (Math.random() * 100.0d)) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                item_id = L1SkillId.ENTANGLE;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                item_id = 69;
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                item_id = 153;
                break;
            case 16:
            case 17:
            case 18:
            case 19:
                item_id = 71;
                break;
            case 20:
            case 25:
            case 30:
            case 35:
            default:
                item_id = 20210;
                break;
            case 21:
            case 22:
            case 23:
            case 24:
                item_id = 154;
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                item_id = 72;
                break;
            case 31:
            case 32:
            case 33:
            case 34:
                item_id = 159;
                break;
            case 36:
            case 37:
            case 38:
            case 39:
                item_id = 77;
                break;
            case 40:
            case 45:
            case 50:
            case 55:
                item_id = 20132;
                break;
            case 41:
            case 42:
            case 43:
            case 44:
                item_id = 161;
                break;
            case 46:
            case 47:
            case 48:
            case 49:
                item_id = 80;
                break;
            case 51:
            case 52:
            case 53:
            case 54:
                item_id = L1SkillId.NATURES_TOUCH;
                break;
            case 56:
            case 57:
            case 58:
            case 59:
                item_id = 75;
                break;
            case 60:
            case 65:
            case 70:
            case 90:
                item_id = 20180;
                break;
            case 61:
            case 62:
            case 63:
            case 64:
                item_id = L1SkillId.IRON_SKIN;
                break;
            case 66:
            case 67:
            case 68:
            case 69:
                item_id = 81;
                break;
            case 71:
            case 72:
            case 73:
            case 74:
                item_id = 40739;
                count = L1SkillId.STATUS_BRAVE;
                break;
            case 75:
            case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
            case ActionCodes.ACTION_ChainswordDamage /*{ENCODED_INT: 85}*/:
            case 95:
                item_id = 20070;
                break;
            case 76:
            case 77:
            case L1SkillId.ABSOLUTE_BARRIER /*{ENCODED_INT: 78}*/:
            case 79:
                item_id = 40738;
                count = L1SkillId.STATUS_BRAVE;
                break;
            case 81:
            case 82:
            case 83:
            case 84:
                item_id = 40740;
                count = 500;
                break;
            case 86:
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
            case 88:
            case L1SkillId.BOUNCE_ATTACK /*{ENCODED_INT: 89}*/:
                item_id = 162;
                break;
            case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
            case 92:
            case OpcodesServer.S_OPCODE_POISON /*{ENCODED_INT: 93}*/:
            case 94:
                item_id = OpcodesServer.S_OPCODE_SELECTTARGET;
                break;
            case OpcodesClient.C_OPCODE_TITLE /*{ENCODED_INT: 96}*/:
            case 97:
            case L1SkillId.ENCHANT_VENOM /*{ENCODED_INT: 98}*/:
            case 99:
                item_id = 20032;
                break;
        }
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, item_id, (long) count);
    }
}
