package com.lineage.data.item_etcitem.poweritem;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import java.util.Iterator;

public class WeaponQieHuan extends ItemExecutor {
    private WeaponQieHuan() {
    }

    public static ItemExecutor get() {
        return new WeaponQieHuan();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (pc.getWeaponItemObjId(0) <= 0 || pc.getWeaponItemObjId(1) <= 0) {
            int n1 = 3;
            int n2 = 8;
            int n3 = 13;
            for (L1ItemInstance weaponitem : pc.getInventory().getItems()) {
                L1Item item2 = weaponitem.getItem();
                if (item2.getType2() == 1) {
                    if ((item2.getType1() == 4 || item2.getType1() == 46 || item2.getType() == 7) && n1 < 8) {
                        pc.setWeaponItemObjId(weaponitem.getId(), n1);
                        n1++;
                    } else if (item2.getType1() == 50 || (item2.getType() == 16 && n2 < 13)) {
                        pc.setWeaponItemObjId(weaponitem.getId(), n2);
                        n2++;
                    }
                } else if (item2.getType2() == 2 && item2.getType() == 7 && n3 < 18) {
                    pc.setWeaponItemObjId(weaponitem.getId(), n3);
                    n3++;
                }
                if (n1 >= 8 && n2 >= 13 && n3 >= 18) {
                    break;
                }
            }
            pc.getAction().action("loadweaponqiehuan", 0);
        } else if (pc.getWeapon() == null) {
            UseWeapon(pc, pc.getWeaponItemObjId(0));
            if (pc.getWeaponItemObjId(2) > 0) {
                UseArmor(pc, pc.getWeaponItemObjId(2));
            }
        } else if (pc.getWeapon().getItem().getType1() == 4 || pc.getWeapon().getItem().getType1() == 46 || pc.getWeapon().getItem().getType() == 7) {
            Iterator<L1ItemInstance> it = pc.getInventory().getItems().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                L1ItemInstance armor = it.next();
                if (armor.isEquipped() && armor.getItem().getType2() == 2 && armor.getItem().getType() == 7) {
                    pc.getInventory().setEquipped(armor, false);
                    break;
                }
            }
            UseWeapon(pc, pc.getWeaponItemObjId(1));
        } else if (pc.getWeapon().getItem().getType1() == 50 || pc.getWeapon().getItem().getType() == 16) {
            UseWeapon(pc, pc.getWeaponItemObjId(0));
            if (pc.getWeaponItemObjId(2) > 0) {
                UseArmor(pc, pc.getWeaponItemObjId(2));
            }
        }
    }

    private void UseArmor(L1PcInstance activeChar, int armorObjId) {
        boolean equipeSpace;
        L1PcInventory pcInventory = activeChar.getInventory();
        L1ItemInstance armor = pcInventory.getItem(armorObjId);
        if (armor != null) {
            int type = armor.getItem().getType();
            if (type == 9) {
                equipeSpace = pcInventory.getTypeEquipped(2, 9) <= 1;
            } else {
                equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
            }
            if (!equipeSpace || armor.isEquipped()) {
                if (!armor.isEquipped()) {
                    activeChar.sendPackets(new S_ServerMessage(124));
                } else if (armor.getBless() == 2) {
                    activeChar.sendPackets(new S_ServerMessage(150));
                    return;
                } else if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) {
                    activeChar.sendPackets(new S_ServerMessage(127));
                    return;
                } else if ((type == 2 || type == 3) && pcInventory.getTypeEquipped(2, 4) >= 1) {
                    activeChar.sendPackets(new S_ServerMessage(127));
                    return;
                } else {
                    pcInventory.setEquipped(armor, false);
                }
            } else if (!L1PolyMorph.isEquipableArmor(activeChar.getTempCharGfx(), type)) {
                return;
            } else {
                if (type == 7 && activeChar.getWeapon() != null && activeChar.getWeapon().getItem().isTwohandedWeapon()) {
                    activeChar.sendPackets(new S_ServerMessage(129));
                    return;
                } else if (type == 3 && pcInventory.getTypeEquipped(2, 4) >= 1) {
                    activeChar.sendPackets(new S_ServerMessage(126, "$224", "$225"));
                    return;
                } else if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) {
                    activeChar.sendPackets(new S_ServerMessage(126, "$224", "$226"));
                    return;
                } else if (type != 2 || pcInventory.getTypeEquipped(2, 4) < 1) {
                    cancelAbsoluteBarrier(activeChar);
                    pcInventory.setEquipped(armor, true);
                } else {
                    activeChar.sendPackets(new S_ServerMessage(126, "$226", "$225"));
                    return;
                }
            }
            activeChar.setCurrentHp(activeChar.getCurrentHp());
            activeChar.setCurrentMp(activeChar.getCurrentMp());
            activeChar.sendPackets(new S_OwnCharAttrDef(activeChar));
            activeChar.sendPackets(new S_OwnCharStatus(activeChar));
            activeChar.sendPackets(new S_SPMR(activeChar));
        }
    }

    private void cancelAbsoluteBarrier(L1PcInstance pc) {
        if (pc.hasSkillEffect(78)) {
            pc.killSkillEffectTimer(78);
            pc.startHpRegeneration();
            pc.startMpRegeneration();
        }
    }

    private void UseWeapon(L1PcInstance activeChar, int weaponObjId) {
        L1PcInventory pcInventory = activeChar.getInventory();
        L1ItemInstance weapon = pcInventory.getItem(weaponObjId);
        if (weapon != null) {
            if (activeChar.getWeapon() == null || !activeChar.getWeapon().equals(weapon)) {
                if (!L1PolyMorph.isEquipableWeapon(activeChar.getTempCharGfx(), weapon.getItem().getType())) {
                    return;
                }
                if (weapon.getItem().isTwohandedWeapon() && pcInventory.getTypeEquipped(2, 7) >= 1) {
                    activeChar.sendPackets(new S_ServerMessage(128));
                    return;
                }
            }
            cancelAbsoluteBarrier(activeChar);
            if (activeChar.getWeapon() != null) {
                if (activeChar.getWeapon().getBless() == 2) {
                    activeChar.sendPackets(new S_ServerMessage(150));
                    return;
                } else if (activeChar.getWeapon().equals(weapon)) {
                    pcInventory.setEquipped(activeChar.getWeapon(), false, false, false);
                    return;
                } else {
                    pcInventory.setEquipped(activeChar.getWeapon(), false, false, true);
                }
            }
            if (weapon.getItemId() == 200002) {
                activeChar.sendPackets(new S_ServerMessage(149, weapon.getLogName()));
            }
            pcInventory.setEquipped(weapon, true, false, false);
            if (activeChar.getWeapon() == null) {
                return;
            }
            if (activeChar.getWeapon().getItem().getType1() == 4 || activeChar.getWeapon().getItem().getType1() == 46 || activeChar.getWeapon().getItem().getType() == 7) {
                activeChar.sendPackets(new S_SystemMessage("成功切換到單手武器:" + activeChar.getWeapon().getName() + "。"));
            } else if (activeChar.getWeapon().getItem().getType1() == 50 || activeChar.getWeapon().getItem().getType() == 16) {
                activeChar.sendPackets(new S_SystemMessage("成功切換到雙手武器:" + activeChar.getWeapon().getName() + "。"));
            }
        }
    }
}
