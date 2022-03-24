package com.lineage.data.cmd;

import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.lock.LogEnchantReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1ItemPower;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_HelpMessage;
import com.lineage.server.serverpackets.S_ItemStatus;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnchantArmor extends EnchantExecutor {
    private static final Log _log = LogFactory.getLog(EnchantArmor.class);

    @Override // com.lineage.data.cmd.EnchantExecutor
    public void failureEnchant(L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (item.getproctect()) {
            if (item.isEquipped()) {
                pc.addAc(item.getEnchantLevel());
            }
            item.setEnchantLevel(0);
            pc.sendPackets(new S_ItemStatus(item));
            pc.getInventory().updateItem(item, 4);
            pc.getInventory().saveItem(item, 4);
            item.setproctect(false);
            pc.sendPackets(new S_ServerMessage(String.valueOf(item.getName()) + " 因為保護效果，歸零了。"));
        } else if (item.getproctect0()) {
            if (item.isEquipped()) {
                pc.addAc(item.getEnchantLevel());
            }
            item.setEnchantLevel(item.getEnchantLevel() - 1);
            pc.sendPackets(new S_ItemStatus(item));
            pc.getInventory().updateItem(item, 4);
            pc.getInventory().saveItem(item, 4);
            item.setproctect0(false);
            pc.sendPackets(new S_ServerMessage("+" + item.getEnchantLevel() + " " + item.getName() + " 因為保護效果，降階了。"));
        } else if (item.getproctect1()) {
            if (item.isEquipped()) {
                pc.addAc(item.getEnchantLevel());
            }
            pc.sendPackets(new S_ItemStatus(item));
            pc.getInventory().updateItem(item, 4);
            pc.getInventory().saveItem(item, 4);
            item.setproctect1(false);
            pc.sendPackets(new S_ServerMessage("+" + item.getEnchantLevel() + " " + item.getName() + " 因為保護效果，保留了。"));
        } else {
            StringBuilder s = new StringBuilder();
            if (ConfigRecord.LOGGING_BAN_ENCHANT) {
                LogEnchantReading.get().failureEnchant(pc, item);
            }
            if (!item.isIdentified()) {
                s.append(item.getName());
            } else {
                s.append(item.getLogName());
            }
            pc.sendPackets(new S_ServerMessage(164, s.toString(), "$252"));
            pc.getInventory().removeItem(item, item.getCount());
            if (ConfigOther.Success_Board && item.getItem().getType2() == 2 && item.getEnchantLevel() >= item.getItem().get_safeenchant() + ConfigOther.Armor_Over) {
                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "玩家【" + pc.getName() + "】把【 " + s.toString() + "】給點爆了。"));
                World.get().broadcastPacketToAll(new S_HelpMessage("玩家【" + pc.getName() + "】把【 " + s.toString() + "】給點爆了。"));
            }
            if (item.getItem().getType2() == 2 && item.getEnchantLevel() >= item.getItem().get_safeenchant() + ConfigOther.Armor_Over_SafeBoard) {
                WriteLogTxt.Recording("裝備點爆了紀錄", "玩家【 " + pc.getName() + " 】" + "把" + "【 + " + item.getEnchantLevel() + " " + item.getName() + " 】給點爆了，" + "序號：【" + item.getId() + "】帳號：【" + pc.getAccountName() + "】");
            }
        }
    }

    @Override // com.lineage.data.cmd.EnchantExecutor
    public void successEnchant(L1PcInstance pc, L1ItemInstance item, int i) throws Exception {
        Integer integer;
        if (item.getproctect()) {
            item.setproctect(false);
            pc.sendPackets(new S_SystemMessage("裝備保護卷軸的力量消失了。"));
        }
        if (item.getproctect0()) {
            item.setproctect0(false);
            pc.sendPackets(new S_SystemMessage("裝備保護卷軸的力量消失了。"));
        }
        if (item.getproctect1()) {
            item.setproctect1(false);
            pc.sendPackets(new S_SystemMessage("裝備保護卷軸的力量消失了。"));
        }
        StringBuilder s = new StringBuilder();
        StringBuilder sa = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        if (!item.isIdentified()) {
            s.append(item.getName());
        } else if (item.getEnchantLevel() > 0) {
            s.append("+" + item.getEnchantLevel() + " " + item.getName());
        } else if (item.getEnchantLevel() < 0) {
            s.append(String.valueOf(item.getEnchantLevel()) + " " + item.getName());
        } else {
            s.append(item.getName());
        }
        switch (i) {
            case -1:
                sa.append("$246");
                sb.append("$247");
                break;
            case 0:
                pc.sendPackets(new S_ServerMessage(L1SkillId.AQUA_PROTECTER, s.toString(), "$252", "$248"));
                return;
            case 1:
                sa.append("$252");
                sb.append("$247");
                break;
            case 2:
            case 3:
                sa.append("$252");
                sb.append("$248");
                break;
        }
        pc.sendPackets(new S_ServerMessage(161, s.toString(), sa.toString(), sb.toString()));
        int oldEnchantLvl = item.getEnchantLevel();
        int newEnchantLvl = oldEnchantLvl + i;
        int enchant_level = item.getEnchantLevel() + i;
        int safe_enchant = item.getItem().get_safeenchant();
        if (oldEnchantLvl != newEnchantLvl) {
            item.setEnchantLevel(newEnchantLvl);
            pc.getInventory().updateItem(item, 4);
            pc.getInventory().saveItem(item, 4);
            if (ConfigOther.SuccessBoard && newEnchantLvl > ConfigOther.ArmorOverSafeBoard + safe_enchant) {
                World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜【" + pc.getName() + "】的【" + s.toString() + " → +" + enchant_level + "】。 "));
                World.get().broadcastPacketToAll(new S_HelpMessage("恭喜【" + pc.getName() + "】的【" + s.toString() + " → +" + enchant_level + "】。 "));
            }
            if (item.getItem().getType2() == 2 && newEnchantLvl > item.getItem().get_safeenchant() + ConfigOther.Armor_EnchantLevel) {
                WriteLogTxt.Recording("裝備強化成功紀錄", "玩家【 " + pc.getName() + " 】" + "的" + "【 " + s.toString() + " → +" + enchant_level + " " + item.getName() + " 】強化成功，" + "序號：【" + item.getId() + "】帳號：【" + pc.getAccountName() + "】");
            }
            if (item.isEquipped()) {
                switch (item.getItem().getUseType()) {
                    case 2:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 25:
                        pc.addAc(-i);
                        if (!(item.getMr() == 0 || (integer = L1ItemPower.MR2.get(Integer.valueOf(item.getItemId()))) == null)) {
                            pc.addMr(integer.intValue() * i);
                            pc.sendPackets(new S_SPMR(pc));
                            break;
                        }
                    case 23:
                    case 24:
                    case 37:
                    case 40:
                        if (item.getItem().get_greater() != 3) {
                            item.greater(pc, true);
                            break;
                        }
                        break;
                }
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
        }
    }
}
