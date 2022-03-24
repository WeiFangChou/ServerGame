package com.lineage.server.clientpackets;

import com.lineage.data.ItemClass;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemBoxTable;
import com.lineage.server.datatables.Item_Box_Table;
import com.lineage.server.model.L1ItemDelay;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBoxItemLv;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1EtcItem;
import com.lineage.server.utils.CheckUtil;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ItemUSe extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ItemUSe.class);

    public C_ItemUSe() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (pc.isGhost()) {
                return;
            }

            if (pc.isDead()) {
                return;
            }

            if (pc.isTeleport()) {
                pc.setTeleport(false);
                pc.sendPackets(new S_Paralysis(7, false));
                return;
            }

            int itemObjid = this.readD();
            L1ItemInstance useItem = pc.getInventory().getItem(itemObjid);
            if (useItem == null) {
                return;
            }

            useItem.set_char_objid(pc.getId());
            boolean isStop = false;
            pc.getMapId();
            if (!pc.getMap().isUsableItem()) {
                pc.sendPackets(new S_ServerMessage(563));
                isStop = true;
            }

            if (pc.isParalyzedX() && !isStop) {
                isStop = true;
            }

            if (!isStop) {
                switch (pc.getMapId()) {
                    case 22:
                        switch (useItem.getItemId()) {
                            case 30:
                            case 40017:
                                break;
                            default:
                                pc.sendPackets(new S_ServerMessage(563));
                                isStop = true;
                        }
                }
            }

            if (!CheckUtil.getUseItemAll(pc) && !isStop) {
                isStop = true;
            }

            if (pc.isPrivateShop() && !isStop) {
                isStop = true;
            }

            int use_type = useItem.getItem().getUseType();
            if (isStop) {
                pc.setTeleport(false);
                pc.sendPackets(new S_Paralysis(7, false));
                return;
            }

            boolean isClass = false;
            String className = useItem.getItem().getclassname();
            if (!className.equals("0")) {
                isClass = true;
            }

            if (pc.getCurrentHp() <= 0) {
                return;
            }

            int delay_id = 0;
            if (useItem.getItem().getType2() == 0) {
                delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
                if (pc.hasItemDelay(502)) {
                    return;
                }
            }

            if (delay_id != 0 && pc.hasItemDelay(delay_id)) {
                pc.sendPackets(new S_Paralysis(7, false));
                return;
            }

            if (useItem.getCount() <= 0L) {
                pc.sendPackets(new S_ServerMessage(329, useItem.getLogName()));
                return;
            }

            int min = useItem.getItem().getMinLevel();
            int max = useItem.getItem().getMaxLevel();
            S_PacketBoxItemLv toUser;
            if (min != 0 && min > pc.getLevel()) {
                if (min < 50) {
                    toUser = new S_PacketBoxItemLv(min, 0);
                    pc.sendPackets(toUser);
                } else {
                    S_ServerMessage toUserMsg = new S_ServerMessage(318, String.valueOf(min));
                    pc.sendPackets(toUserMsg);
                }

                return;
            }

            if (max != 0 && max < pc.getLevel()) {
                toUser = new S_PacketBoxItemLv(0, max);
                pc.sendPackets(toUser);
                return;
            }

            boolean isDelayEffect = false;
            int itemId;
            if (useItem.getItem().getType2() == 0) {
                itemId = ((L1EtcItem) useItem.getItem()).get_delayEffect();
                if (itemId > 0) {
                    isDelayEffect = true;
                    Timestamp lastUsed = useItem.getLastUsed();
                    if (lastUsed != null) {
                        Calendar cal = Calendar.getInstance();
                        long useTime = (cal.getTimeInMillis() - lastUsed.getTime()) / 1000L;
                        if (useTime <= (long) itemId) {
                            useTime = ((long) itemId - useTime) / 60L;
                            String useTimeS = useItem.getLogName() + " " + useTime;
                            pc.sendPackets(new S_ServerMessage(1139, useTimeS));
                            return;
                        }
                    }
                }
            }

            int[] newData;
            switch (use_type) {
                case -12:
                case -5:
                case -1:
                    pc.sendPackets(new S_ServerMessage(74, useItem.getLogName()));
                    break;
                case -11:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -10:
                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -9:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -8:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readC(), this.readC()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var50) {
                            return;
                        }
                    }
                    break;
                case -7:
                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -6:
                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -4:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case -3:
                    pc.getInventory().setSting(useItem.getItemId());
                    pc.sendPackets(new S_ServerMessage(452, useItem.getLogName()));
                    break;
                case -2:
                    pc.getInventory().setArrow(useItem.getItemId());
                    pc.sendPackets(new S_ServerMessage(452, useItem.getLogName()));
                    break;
                case 0:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 1:
                    if (pc.hasItemDelay(500)) {
                        return;
                    }

                    if (this.useItem(pc, useItem)) {
                        this.useWeapon(pc, useItem);
                    }
                    break;
                case 2:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 37:
                case 40:
                case 43:
                case 44:
                case 45:
                case 47:
                case 48:
                    if (pc.hasItemDelay(501)) {
                        return;
                    }

                    if (this.useItem(pc, useItem)) {
                        this.useArmor(pc, useItem);
                    }
                    break;
                case 3:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                case 4:
                    break;
                case 5:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD(), this.readH(), this.readH()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var44) {
                            return;
                        }
                    }
                    break;
                case 6:
                    if (!L1BuffUtil.getUseItemTeleport(pc)) {
                        pc.setTeleport(false);
                        pc.sendPackets(new S_Paralysis(7, false));
                        pc.sendPackets(new S_SystemMessage("雙腳被捆綁的狀態下無法瞬間移動。"));
                        return;
                    }

                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        try {
                            newData = new int[]{this.readD(), this.readH()};
                            ItemClass.get().item(newData, pc, useItem);
                            pc.sendPackets(new S_Paralysis(7, false));
                        } catch (Exception var54) {
                            return;
                        }
                    }
                    break;
                case 7:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var41) {
                            return;
                        }
                    }
                    break;
                case 8:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var53) {
                            return;
                        }
                    }
                    break;
                case 9:
                    if (!L1BuffUtil.getUseItemTeleport(pc)) {
                        pc.setTeleport(false);
                        pc.sendPackets(new S_Paralysis(7, false));
                        pc.sendPackets(new S_SystemMessage("雙腳被捆綁的狀態下無法瞬間移動。"));
                        return;
                    }

                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 10:
                    if (useItem.getRemainingTime() <= 0 && useItem.getItemId() != 40004) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 11:
                case 41:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                default:
                    _log.info("未處理的物品分類: " + use_type);
                    break;
                case 12:
                case 31:
                case 33:
                case 35:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readH()};
                            pc.setText(this.readS());
                            pc.setTextByte(this.readByte());
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var43) {
                            return;
                        }
                    }
                    break;
                case 13:
                case 32:
                case 34:
                case 36:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 14:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var52) {
                            return;
                        }
                    }
                    break;
                case 15:
                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 16:
                    if (isClass) {
                        String cmd = this.readS();
                        pc.setText(cmd);
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 17:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD(), this.readH(), this.readH()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var49) {
                            return;
                        }
                    }
                    break;
                case 26:
                case 27:
                case 46:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var47) {
                            return;
                        }
                    }
                    break;
                case 28:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readC()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var45) {
                            return;
                        }
                    }
                    break;
                case 29:
                    if (!L1BuffUtil.getUseItemTeleport(pc)) {
                        pc.setTeleport(false);
                        pc.sendPackets(new S_Paralysis(7, false));
                        pc.sendPackets(new S_SystemMessage("雙腳被捆綁的狀態下無法瞬間移動。"));
                        return;
                    }

                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        try {
                            newData = new int[]{this.readD(), this.readH()};
                            ItemClass.get().item(newData, pc, useItem);
                            pc.sendPackets(new S_Paralysis(7, false));
                        } catch (Exception var40) {
                            return;
                        }
                    }
                    break;
                case 30:
                    if (isClass) {
                        try {
                            itemId = this.readD();
                            newData = new int[]{itemId};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var42) {
                            return;
                        }
                    }
                    break;
                case 38:
                    if (!CheckUtil.getUseItem(pc)) {
                        return;
                    }

                    if (isClass) {
                        ItemClass.get().item((int[]) null, pc, useItem);
                    }
                    break;
                case 39:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD(), this.readH(), this.readH()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var48) {
                            return;
                        }
                    }
                    break;
                case 42:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readH(), this.readH(), 0};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var51) {
                            return;
                        }
                    }
                    break;
                case 55:
                    if (isClass) {
                        try {
                            newData = new int[]{this.readD()};
                            ItemClass.get().item(newData, pc, useItem);
                        } catch (Exception var46) {
                            return;
                        }
                    }
            }

            if (useItem.getItem().getType2() == 0 && use_type == 0) {
                itemId = useItem.getItem().getItemId();
                switch (itemId) {
                    case 40630:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "diegodiary"));
                        break;
                    case 40663:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "sonsletter"));
                        break;
                    case 40701:
                        if (pc.getQuest().get_step(23) == 1) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "firsttmap"));
                        } else if (pc.getQuest().get_step(23) == 2) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapa"));
                        } else if (pc.getQuest().get_step(23) == 3) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapb"));
                        } else if (pc.getQuest().get_step(23) == 4) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "secondtmapc"));
                        } else if (pc.getQuest().get_step(23) == 5) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapd"));
                        } else if (pc.getQuest().get_step(23) == 6) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmape"));
                        } else if (pc.getQuest().get_step(23) == 7) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapf"));
                        } else if (pc.getQuest().get_step(23) == 8) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapg"));
                        } else if (pc.getQuest().get_step(23) == 9) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmaph"));
                        } else if (pc.getQuest().get_step(23) == 10) {
                            pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "thirdtmapi"));
                        }
                        break;
                    case 41007:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll"));
                        break;
                    case 41009:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "erisscroll2"));
                        break;
                    case 41060:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "nonames"));
                        break;
                    case 41061:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kames"));
                        break;
                    case 41062:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bakumos"));
                        break;
                    case 41063:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "bukas"));
                        break;
                    case 41064:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "huwoomos"));
                        break;
                    case 41065:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "noas"));
                        break;
                    case 41317:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rarson"));
                        break;
                    case 41318:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "kuen"));
                        break;
                    case 41329:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "anirequest"));
                        break;
                    case 41340:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "tion"));
                        break;
                    case 41356:
                        pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "rparum3"));
                }
            }

            ArrayList list;
            if (useItem.getItem().getType2() == 0 && useItem.getItem().getType() == 9) {
                if (pc.getInventory().getSize() >= 160) {
                    pc.sendPackets(new S_ServerMessage(263));
                    return;
                }

                if (pc.getInventory().getWeight240() >= 180) {
                    pc.sendPackets(new S_ServerMessage(82));
                    return;
                }

                list = Item_Box_Table.get().get(pc, useItem);
                if (list == null) {
                    Item_Box_Table.get().get(pc, useItem);
                }
            }

            if (useItem.getItem().getType2() == 0 && useItem.getItem().getType() == 16) {
                if (pc.getInventory().getSize() >= 160) {
                    pc.sendPackets(new S_ServerMessage(263));
                    return;
                }

                if (pc.getInventory().getWeight240() >= 180) {
                    pc.sendPackets(new S_ServerMessage(82));
                    return;
                }

                list = ItemBoxTable.get().get(pc, useItem);
                if (list == null) {
                    ItemBoxTable.get().get_all(pc, useItem);
                }
            }

            if (isDelayEffect) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                useItem.setLastUsed(ts);
                pc.getInventory().updateItem(useItem, 32);
                pc.getInventory().saveItem(useItem, 32);
            }

            try {
                L1ItemDelay.onItemUse(client, useItem);
            } catch (Exception var39) {
                _log.error("分類道具使用延遲異常:" + useItem.getItemId(), var39);
            }
        } catch (Exception var55) {
            return;
        } finally {
            this.over();
        }

    }

    private boolean useItem(L1PcInstance pc, L1ItemInstance useItem) {
        boolean isEquipped = false;
        if (pc.isCrown()) {
            if (useItem.getItem().isUseRoyal()) {
                isEquipped = true;
            }
        } else if (pc.isKnight()) {
            if (useItem.getItem().isUseKnight()) {
                isEquipped = true;
            }
        } else if (pc.isElf()) {
            if (useItem.getItem().isUseElf()) {
                isEquipped = true;
            }
        } else if (pc.isWizard()) {
            if (useItem.getItem().isUseMage()) {
                isEquipped = true;
            }
        } else if (pc.isDarkelf()) {
            if (useItem.getItem().isUseDarkelf()) {
                isEquipped = true;
            }
        } else if (pc.isDragonKnight()) {
            if (useItem.getItem().isUseDragonknight()) {
                isEquipped = true;
            }
        } else if (pc.isIllusionist() && useItem.getItem().isUseIllusionist()) {
            isEquipped = true;
        }

        if (!isEquipped) {
            pc.sendPackets(new S_ServerMessage(264));
        }

        return isEquipped;
    }

    private void useArmor(L1PcInstance pc, L1ItemInstance armor) {
        int type = armor.getItem().getType();
        L1PcInventory pcInventory = pc.getInventory();
        boolean equipeSpace;
        int count;
        if (type == 9) {
            if (!armor.isEquipped()) {
                if (pcInventory.getEquippedCountByItemId(armor.getItemId()) >= 2) {
                    pc.sendPackets(new S_ServerMessage(3278));
                    return;
                }

                if (pcInventory.getEquippedCountByActivityItem() >= 2) {
                    pc.sendPackets(new S_ServerMessage(3279));
                    return;
                }
            }

            count = 1;
            if ((pc.getRingsExpansion() & 1) == 1) {
                ++count;
            }

            if ((pc.getRingsExpansion() & 2) == 2) {
                ++count;
            }

            equipeSpace = pcInventory.getTypeEquipped(2, 9) <= count;
        } else {
            equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
        }

        if (equipeSpace && !armor.isEquipped()) {
            count = pc.getTempCharGfx();
            if (!L1PolyMorph.isEquipableArmor(count, type)) {
                return;
            }

            if (type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1 || type == 7 && pcInventory.getTypeEquipped(2, 13) >= 1) {
                pc.sendPackets(new S_ServerMessage(124));
                return;
            }

            if (type == 7 && pc.getWeapon() != null && pc.getWeapon().getItem().isTwohandedWeapon()) {
                pc.sendPackets(new S_ServerMessage(129));
                return;
            }

            if (type == 3 && pcInventory.getTypeEquipped(2, 4) >= 1) {
                pc.sendPackets(new S_ServerMessage(126, "$224", "$225"));
                return;
            }

            if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) {
                pc.sendPackets(new S_ServerMessage(126, "$224", "$226"));
                return;
            }

            if (type == 2 && pcInventory.getTypeEquipped(2, 4) >= 1) {
                pc.sendPackets(new S_ServerMessage(126, "$226", "$225"));
                return;
            }

            if (pc.hasSkillEffect(78)) {
                pc.killSkillEffectTimer(78);
                pc.startHpRegeneration();
                pc.startMpRegeneration();
            }

            pcInventory.setEquipped(armor, true);
        } else {
            if (!armor.isEquipped()) {
                if (armor.getItem().getUseType() == 23) {
                    pc.sendPackets(new S_SystemMessage("你已經戴著四個戒指。"));
                    return;
                }

                pc.sendPackets(new S_ServerMessage(124));
                return;
            }

            if (armor.getItem().getBless() == 2) {
                pc.sendPackets(new S_ServerMessage(150));
                return;
            }

            if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) {
                pc.sendPackets(new S_ServerMessage(127));
                return;
            }

            if ((type == 2 || type == 3) && pcInventory.getTypeEquipped(2, 4) >= 1) {
                pc.sendPackets(new S_ServerMessage(127));
                return;
            }

            if (type == 7 && pc.hasSkillEffect(90)) {
                pc.removeSkillEffect(90);
            }

            pcInventory.setEquipped(armor, false);
        }

        pc.setCurrentHp(pc.getCurrentHp());
        pc.setCurrentMp(pc.getCurrentMp());
        pc.sendPackets(new S_OwnCharAttrDef(pc));
        pc.sendPackets(new S_OwnCharStatus(pc));
        pc.sendPackets(new S_SPMR(pc));
    }

    private void useWeapon(L1PcInstance pc, L1ItemInstance weapon) {
        switch (weapon.getItemId()) {
            case 65:
            case 133:
            case 191:
            case 192:
                if (pc.getMapId() != 2000 && !pc.getWeapon().equals(weapon)) {
                    pc.sendPackets(new S_ServerMessage(563));
                    return;
                }
                break;
            default:
                if (pc.hasSkillEffect(4007)) {
                    pc.sendPackets(new S_ServerMessage(563));
                    return;
                }
        }

        L1PcInventory pcInventory = pc.getInventory();
        if (pc.getWeapon() == null || !pc.getWeapon().equals(weapon)) {
            int weapon_type = weapon.getItem().getType();
            int polyid = pc.getTempCharGfx();
            if (!L1PolyMorph.isEquipableWeapon(polyid, weapon_type)) {
                return;
            }

            if (weapon.getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(2, 7) >= 1) {
                pc.sendPackets(new S_ServerMessage(128));
                return;
            }
        }

        if (pc.hasSkillEffect(78)) {
            pc.killSkillEffectTimer(78);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }

        if (pc.getWeapon() != null) {
            if (pc.getWeapon().getItem().getBless() == 2) {
                pc.sendPackets(new S_ServerMessage(150));
                return;
            }

            if (pc.getWeapon().equals(weapon)) {
                pcInventory.setEquipped(pc.getWeapon(), false, false, false);
                return;
            }

            pcInventory.setEquipped(pc.getWeapon(), false, false, true);
        }

        if (weapon.getItem().getBless() == 2) {
            pc.sendPackets(new S_ServerMessage(149, weapon.getLogName()));
        }

        pcInventory.setEquipped(weapon, true, false, false);
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
