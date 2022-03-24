package com.lineage.server.model;

import com.lineage.server.datatables.PolyTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PolyMorph {
    private static final int AMULET_EQUIP = 2;
    private static final int ARMOR_EQUIP = 16;
    private static final int AXE_EQUIP = 8;
    private static final int BELT_EQUIP = 64;
    private static final int BOOTS_EQUIP = 1024;
    private static final int BOW_EQUIP = 256;
    private static final int CHAINSWORD_EQUIP = 1024;
    private static final int CLAW_EQUIP = 128;
    private static final int CLOAK_EQUIP = 32;
    private static final int DAGGER_EQUIP = 1;
    private static final int EARRING_EQUIP = 4;
    private static final int EDORYU_EQUIP = 64;
    private static final int GLOVE_EQUIP = 256;
    private static final int GUARDER_EQUIP = 2048;
    private static final int HELM_EQUIP = 1;
    private static final int KIRINGKU_EQUIP = 512;
    public static final int MORPH_BY_GM = 2;
    public static final int MORPH_BY_ITEMMAGIC = 1;
    public static final int MORPH_BY_KEPLISHA = 8;
    public static final int MORPH_BY_LOGIN = 0;
    public static final int MORPH_BY_NPC = 4;
    private static final int RING_EQUIP = 512;
    private static final int SHIELD_EQUIP = 128;
    private static final int SPEAR_EQUIP = 16;
    private static final int STAFF_EQUIP = 32;
    private static final int SWORD_EQUIP = 2;
    private static final int TSHIRT_EQUIP = 8;
    private static final int TWOHANDSWORD_EQUIP = 4;
    private static final Log _log = LogFactory.getLog(L1PolyMorph.class);
    private static final Map<Integer, Integer> armorFlgMap = new HashMap();
    private static final Map<Integer, Integer> weaponFlgMap = new HashMap();
    private int _armorEquipFlg;
    private boolean _canUseSkill;
    private int _causeFlg;
    private int _id;
    private int _minLevel;
    private String _name;
    private int _polyId;
    private int _weaponEquipFlg;

    static {
        weaponFlgMap.put(1, 2);
        weaponFlgMap.put(2, 1);
        weaponFlgMap.put(3, 4);
        weaponFlgMap.put(4, 256);
        weaponFlgMap.put(5, 16);
        weaponFlgMap.put(6, 8);
        weaponFlgMap.put(7, 32);
        weaponFlgMap.put(8, 256);
        weaponFlgMap.put(9, 256);
        weaponFlgMap.put(10, 256);
        weaponFlgMap.put(11, 128);
        weaponFlgMap.put(12, 64);
        weaponFlgMap.put(13, 256);
        weaponFlgMap.put(14, 16);
        weaponFlgMap.put(15, 8);
        weaponFlgMap.put(16, 32);
        weaponFlgMap.put(17, 512);
        weaponFlgMap.put(18, 1024);
        armorFlgMap.put(1, 1);
        armorFlgMap.put(2, 16);
        armorFlgMap.put(3, 8);
        armorFlgMap.put(4, 32);
        armorFlgMap.put(5, 256);
        armorFlgMap.put(6, 1024);
        armorFlgMap.put(7, 128);
        armorFlgMap.put(8, 2);
        armorFlgMap.put(9, 512);
        armorFlgMap.put(10, 64);
        armorFlgMap.put(12, 4);
        armorFlgMap.put(13, 2048);
    }

    public L1PolyMorph(int id, String name, int polyId, int minLevel, int weaponEquipFlg, int armorEquipFlg, boolean canUseSkill, int causeFlg) {
        this._id = id;
        this._name = name;
        this._polyId = polyId;
        this._minLevel = minLevel;
        this._weaponEquipFlg = weaponEquipFlg;
        this._armorEquipFlg = armorEquipFlg;
        this._canUseSkill = canUseSkill;
        this._causeFlg = causeFlg;
    }

    public int getId() {
        return this._id;
    }

    public String getName() {
        return this._name;
    }

    public int getPolyId() {
        return this._polyId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public int getWeaponEquipFlg() {
        return this._weaponEquipFlg;
    }

    public int getArmorEquipFlg() {
        return this._armorEquipFlg;
    }

    public boolean canUseSkill() {
        return this._canUseSkill;
    }

    public int getCauseFlg() {
        return this._causeFlg;
    }

    public static void handleCommands(L1PcInstance pc, String s) {
        if (pc != null && !pc.isDead()) {
            L1PolyMorph poly = PolyTable.get().getTemplate(s);
            if (poly == null && !s.equals("none")) {
                return;
            }
            if (s.equals("none")) {
                if (pc.getTempCharGfx() != 6034 && pc.getTempCharGfx() != 6035) {
                    pc.removeSkillEffect(67);
                    pc.sendPackets(new S_CloseList(pc.getId()));
                }
            } else if (pc.getLevel() < poly.getMinLevel() && !pc.isGm()) {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.DRAGON_SKIN));
            } else if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
                pc.sendPackets(new S_ServerMessage((int) L1SkillId.DRAGON_SKIN));
            } else {
                doPoly(pc, poly.getPolyId(), 7200, 1);
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        }
    }

    public static void doPoly(L1Character cha, int polyId, int timeSecs, int cause) {
        if (cha != null) {
            try {
                if (cha.isDead() || cha.hasItemDelay(L1ItemDelay.POLY)) {
                    return;
                }
                if (cha instanceof L1PcInstance) {
                    L1PcInstance pc = (L1PcInstance) cha;
                    if (pc.getMapId() == 5300) {
                        pc.sendPackets(new S_ServerMessage(1170));
                    } else if (pc.getMapId() == 9000) {
                        pc.sendPackets(new S_ServerMessage(1170));
                    } else if (pc.getMapId() == 9100) {
                        pc.sendPackets(new S_ServerMessage(1170));
                    } else if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.DRAGON_SKIN));
                    } else if (!isMatchCause(polyId, cause)) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.DRAGON_SKIN));
                    } else {
                        pc.killSkillEffectTimer(67);
                        pc.setSkillEffect(67, timeSecs * L1SkillId.STATUS_BRAVE);
                        if (pc.getTempCharGfx() != polyId) {
                            L1ItemInstance weapon = pc.getWeapon();
                            boolean weaponTakeoff = weapon != null && !isEquipableWeapon(polyId, weapon.getItem().getType());
                            pc.setTempCharGfx(polyId);
                            pc.sendPackets(new S_ChangeShape(pc, polyId, weaponTakeoff));
                            if (!pc.isGmInvis() && !pc.isInvisble()) {
                                pc.broadcastPacketAll(new S_ChangeShape(pc, polyId));
                            }
                            pc.getInventory().takeoffEquip(polyId);
                            if (pc.getWeapon() != null) {
                                pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                            }
                        }
                        pc.sendPackets(new S_PacketBox(35, timeSecs));
                    }
                } else if (cha instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) cha;
                    mob.killSkillEffectTimer(67);
                    mob.setSkillEffect(67, timeSecs * L1SkillId.STATUS_BRAVE);
                    if (mob.getTempCharGfx() != polyId) {
                        mob.setTempCharGfx(polyId);
                        mob.broadcastPacketAll(new S_ChangeShape(mob, polyId));
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public static void undoPoly(L1Character cha) {
        try {
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                int classId = pc.getClassId();
                pc.setTempCharGfx(classId);
                pc.sendPacketsAll(new S_ChangeShape(pc, classId));
                if (pc.getWeapon() != null) {
                    pc.sendPacketsAll(new S_CharVisualUpdate(pc));
                }
            } else if (cha instanceof L1MonsterInstance) {
                L1MonsterInstance mob = (L1MonsterInstance) cha;
                mob.setTempCharGfx(0);
                mob.broadcastPacketAll(new S_ChangeShape(mob, mob.getGfxId()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean isEquipableWeapon(int polyId, int weaponType) {
        Integer flg;
        try {
            L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            if (poly == null || (flg = weaponFlgMap.get(Integer.valueOf(weaponType))) == null || (poly.getWeaponEquipFlg() & flg.intValue()) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return true;
        }
    }

    public static boolean isEquipableArmor(int polyId, int armorType) {
        Integer flg;
        try {
            L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            if (poly == null || (flg = armorFlgMap.get(Integer.valueOf(armorType))) == null || (poly.getArmorEquipFlg() & flg.intValue()) != 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return true;
        }
    }

    public static boolean isMatchCause(int polyId, int cause) {
        try {
            L1PolyMorph poly = PolyTable.get().getTemplate(polyId);
            return poly == null || cause == 0 || (poly.getCauseFlg() & cause) != 0;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
