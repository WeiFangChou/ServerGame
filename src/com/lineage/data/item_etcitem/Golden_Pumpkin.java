package com.lineage.data.item_etcitem;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.ActionCodes;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;

public class Golden_Pumpkin extends ItemExecutor {
    private Golden_Pumpkin() {
    }

    public static ItemExecutor get() {
        return new Golden_Pumpkin();
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
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
                item_id = 40524;
                count = 10;
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
                item_id = 40043;
                count = 10;
                break;
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
                item_id = 40068;
                count = 30;
                break;
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
                item_id = 40039;
                count = 10;
                break;
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
                item_id = 40040;
                count = 10;
                break;
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
                item_id = 40031;
                count = 10;
                break;
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
                item_id = 20342;
                break;
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                item_id = 20079;
                break;
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
                item_id = 20047;
                break;
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                item_id = 20086;
                break;
            case L1SkillId.ABSOLUTE_BARRIER /*{ENCODED_INT: 78}*/:
            case 79:
            case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
            case 81:
            case 82:
                item_id = 20087;
                break;
            case 83:
            case 84:
            case ActionCodes.ACTION_ChainswordDamage /*{ENCODED_INT: 85}*/:
            case 86:
            case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                item_id = 20088;
                break;
            case 88:
                item_id = 100029;
                break;
            case L1SkillId.BOUNCE_ATTACK /*{ENCODED_INT: 89}*/:
                item_id = 100037;
                break;
            case 90:
                item_id = 100042;
                break;
            case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                item_id = 100049;
                break;
            case 92:
                item_id = 100074;
                break;
            case OpcodesServer.S_OPCODE_POISON /*{ENCODED_INT: 93}*/:
                item_id = 120280;
                break;
            case 94:
                item_id = 120309;
                break;
            case 95:
                item_id = 120310;
                break;
            case OpcodesClient.C_OPCODE_TITLE /*{ENCODED_INT: 96}*/:
                item_id = 120311;
                break;
            case 97:
                item_id = 120317;
                break;
            case L1SkillId.ENCHANT_VENOM /*{ENCODED_INT: 98}*/:
                item_id = 120320;
                break;
            default:
                item_id = 120321;
                break;
        }
        pc.getInventory().removeItem(item, 1);
        CreateNewItem.createNewItem(pc, item_id, (long) count);
    }
}
