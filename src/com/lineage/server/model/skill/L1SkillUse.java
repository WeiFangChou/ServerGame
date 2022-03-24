package com.lineage.server.model.skill;

import com.lineage.config.ConfigOther;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.Instance.L1CrownInstance;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1DwarfInstance;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1FieldObjectInstance;
import com.lineage.server.model.Instance.L1FurnitureInstance;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1HousekeeperInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MerchantInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.Instance.L1TeleporterInstance;
import com.lineage.server.model.Instance.L1TowerInstance;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_Chat;
import com.lineage.server.serverpackets.S_Dexup;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxIconAura;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RangeSkill;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillIconShield;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_Sound;
import com.lineage.server.serverpackets.S_Strup;
import com.lineage.server.serverpackets.S_UseAttackSkill;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldTrap;
import com.lineage.server.world.WorldWar;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SkillUse {
    private static final Log _log = LogFactory.getLog(L1SkillUse.class);
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOGIN = 1;
    public static final int TYPE_SPELLSC = 2;
    public static final int TYPE_NPCBUFF = 3;
    public static final int TYPE_GMBUFF = 4;
    private L1Skills _skill;
    private int _skillId;
    private int _getBuffDuration;
    private int _shockStunDuration;
    private int _getBuffIconDuration;
    private int _targetID;
    private int _mpConsume = 0;
    private int _hpConsume = 0;
    private int _targetX = 0;
    private int _targetY = 0;
    private int _dmg = 0;
    private int _skillTime = 0;
    private int _type = 0;
    private boolean _isPK = false;
    private int _bookmarkId = 0;
    private int _itemobjid = 0;
    private boolean _checkedUseSkill = false;
    private int _leverage = 10;
    private boolean _isFreeze = false;
    private boolean _isCounterMagic = true;
    private boolean _isGlanceCheckFail = false;
    private L1Character _user = null;
    private L1PcInstance _player = null;
    private L1NpcInstance _npc = null;
    private L1Character _target = null;
    private L1NpcInstance _targetNpc = null;
    private int _calcType;
    private static final int PC_PC = 1;
    private static final int PC_NPC = 2;
    private static final int NPC_PC = 3;
    private static final int NPC_NPC = 4;
    private ArrayList<TargetStatus> _targetList;
    private static final int[] EXCEPT_COUNTER_MAGIC = new int[]{1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60, 61, 63, 67, 68, 69, 72, 73, 75, 78, 79, 87, 88, 89, 90, 91, 97, 98, 99, 100, 101, 102, 104, 105, 106, 107, 109, 110, 111, 113, 114, 115, 116, 117, 118, 129, 130, 131, 132, 134, 137, 138, 146, 147, 148, 149, 150, 151, 155, 156, 158, 159, 161, 163, 164, 165, 166, 168, 169, 170, 171, 175, 176, 181, 185, 190, 195, 201, 204, 209, 211, 10026, 10027, 214, 216, 219, 10028, 10029};
    private static final int[][] REPEATEDSKILLS = new int[][]{{148, 149, 155, 156, 163, 166}, {3, 99, 151, 159, 168}, {52, 101, 150, 1000, 1016, 1017, 186}, {43, 54, 1001}, {26, 110}, {42, 109}, {114, 115}};

    public L1SkillUse() {
    }

    public void setLeverage(int i) {
        this._leverage = i;
    }

    public int getLeverage() {
        return this._leverage;
    }

    private boolean isCheckedUseSkill() {
        return this._checkedUseSkill;
    }

    private void setCheckedUseSkill(boolean flg) {
        this._checkedUseSkill = flg;
    }

    public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, int time, int type, L1Character attacker) {
        return this.checkUseSkill(player, skillid, target_id, x, y, time, type, attacker, 0, 0);
    }

    public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, int time, int type, L1Character attacker, int actid, int gfxid) {
        this.setCheckedUseSkill(true);
        this._targetList = new ArrayList();
        this._skill = SkillsTable.get().getTemplate(skillid);
        if (this._skill == null) {
            return false;
        } else {
            this._skillId = skillid;
            this._targetX = x;
            this._targetY = y;
            this._skillTime = time;
            this._type = type;
            boolean checkedResult = true;
            if (attacker == null) {
                this._player = player;
                this._user = this._player;
            } else {
                this._npc = (L1NpcInstance)attacker;
                this._user = this._npc;
            }

            if (this._skill.getTarget().equals("none")) {
                this._targetID = this._user.getId();
                this._targetX = this._user.getX();
                this._targetY = this._user.getY();
            } else {
                this._targetID = target_id;
            }

            switch(type) {
                case 0:
                    checkedResult = this.isNormalSkillUsable();
                case 1:
                default:
                    break;
                case 2:
                case 3:
                    checkedResult = true;
            }

            if (!checkedResult) {
                return false;
            } else if (this._skillId != 58 && this._skillId != 63) {
                L1Object object = World.get().findObject(this._targetID);
                if (object instanceof L1ItemInstance) {
                    return false;
                } else {
                    if (this._user instanceof L1PcInstance) {
                        if (object instanceof L1PcInstance) {
                            this._calcType = 1;
                        } else {
                            this._calcType = 2;
                            this._targetNpc = (L1NpcInstance)object;
                        }
                    } else if (this._user instanceof L1NpcInstance) {
                        if (object instanceof L1PcInstance) {
                            this._calcType = 3;
                        } else if (this._skill.getTarget().equals("none")) {
                            this._calcType = 3;
                        } else {
                            this._calcType = 4;
                            this._targetNpc = (L1NpcInstance)object;
                        }
                    }

                    switch(this._skillId) {
                        case 5:
                        case 69:
                            this._bookmarkId = target_id;
                            break;
                        case 12:
                        case 21:
                        case 73:
                        case 100:
                        case 107:
                            this._itemobjid = target_id;
                    }

                    this._target = (L1Character)object;
                    if (!(this._target instanceof L1MonsterInstance) && this._skill.getTarget().equals("attack") && this._user.getId() != target_id) {
                        this._isPK = true;
                    }

                    if (!(object instanceof L1Character)) {
                        checkedResult = false;
                    }

                    this.makeTargetList();
                    if (this._targetList.size() == 0 && this._user instanceof L1NpcInstance) {
                        checkedResult = false;
                    }

                    return checkedResult;
                }
            } else {
                return true;
            }
        }
    }

    private boolean isNormalSkillUsable() {
        if (this._user instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance)this._user;
            if (!this.isAttrAgrees()) {
                return false;
            }

            if (this._skillId == 147 && pc.getElfAttr() == 0) {
                pc.sendPackets(new S_ServerMessage(280));
                return false;
            }

            if (this._skillId == 77 && pc.getLawful() < 500) {
                pc.sendPackets(new S_ServerMessage(352, "$967"));
                return false;
            }

            if (this._skillId == 205 || this._skillId == 210 || this._skillId == 215 || this._skillId == 220) {
                boolean isNearSameCube = false;

                Iterator var5 = World.get().getVisibleObjects(pc, 3).iterator();

                label110: {
                    int gfxId;
                    do {
                        L1Object obj;
                        do {
                            if (!var5.hasNext()) {
                                break label110;
                            }

                            obj = (L1Object)var5.next();
                        } while(!(obj instanceof L1EffectInstance));

                        L1EffectInstance effect = (L1EffectInstance)obj;
                        gfxId = effect.getGfxId();
                    } while((this._skillId != 205 || gfxId != 6706) && (this._skillId != 210 || gfxId != 6712) && (this._skillId != 215 || gfxId != 6718) && (this._skillId != 220 || gfxId != 6724));

                    isNearSameCube = true;
                }

                if (isNearSameCube) {
                    pc.sendPackets(new S_ServerMessage(1412));
                    return false;
                }
            }

            if (pc.getAwakeSkillId() == 185 && this._skillId != 185 && this._skillId != 184 && this._skillId != 189 && this._skillId != 194 || pc.getAwakeSkillId() == 190 && this._skillId != 190 && this._skillId != 184 && this._skillId != 189 && this._skillId != 194 || pc.getAwakeSkillId() == 195 && this._skillId != 195 && this._skillId != 184 && this._skillId != 189 && this._skillId != 194) {
                pc.sendPackets(new S_ServerMessage(1385));
                return false;
            }

            if (!this.isItemConsume() && !this._player.isGm()) {
                this._player.sendPackets(new S_ServerMessage(299));
                return false;
            }
        } else if (this._user instanceof L1NpcInstance && this._user.hasSkillEffect(64)) {
            this._user.removeSkillEffect(64);
            return false;
        }

        return this.isHPMPConsume();
    }

    public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, int timeSecs, int type) {
        this.handleCommands(player, skillId, targetId, x, y, timeSecs, type, (L1Character)null);
    }

    public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, int timeSecs, int type, L1Character attacker) {
        try {
            if (!this.isCheckedUseSkill()) {
                boolean isUseSkill = this.checkUseSkill(player, skillId, targetId, x, y, timeSecs, type, attacker);
                if (!isUseSkill) {
                    this.failSkill();
                    return;
                }
            }

            switch(type) {
                case 0:
                    if (!this._isGlanceCheckFail || this._skill.getArea() > 0 || this._skill.getTarget().equals("none")) {
                        this.runSkill();
                        this.useConsume();
                        this.sendGrfx(true);
                        this.sendFailMessageHandle();
                        this.setDelay();
                    }
                    break;
                case 1:
                    this.runSkill();
                    break;
                case 2:
                    this.runSkill();
                    this.sendGrfx(true);
                    break;
                case 3:
                    this.runSkill();
                    this.sendGrfx(true);
                    break;
                case 4:
                    this.runSkill();
                    this.sendGrfx(false);
            }

            this.setCheckedUseSkill(false);
        } catch (Exception var10) {
            _log.error(var10.getLocalizedMessage(), var10);
        }

    }

    private void failSkill() {
        this.setCheckedUseSkill(false);
        switch(this._skillId) {
            case 5:
            case 69:
            case 131:
                this._player.sendPackets(new S_Paralysis(7, false));
            default:
        }
    }

    private boolean isTarget(L1Character cha) throws Exception {
        if (cha == null) {
            return false;
        } else if (this._user.get_showId() != cha.get_showId()) {
            return false;
        } else {
            if (this._npc != null) {
                if (this._npc.isHate(cha)) {
                    return true;
                }

                if (this._npc instanceof L1PetInstance && cha instanceof L1MonsterInstance) {
                    return true;
                }

                if (this._npc instanceof L1SummonInstance && cha instanceof L1MonsterInstance) {
                    return true;
                }
            }

            if (!CheckUtil.checkAttackSkill(cha)) {
                return false;
            } else {
                boolean flg = false;
                if (!(cha instanceof L1DoorInstance) || cha.getMaxHp() != 0 && cha.getMaxHp() != 1) {
                    if (cha instanceof L1DollInstance) {
                        return false;
                    } else {
                        L1PcInstance xpc;
                        if (cha instanceof L1PcInstance) {
                            xpc = (L1PcInstance)cha;
                            if (xpc.isGhost() || xpc.isGmInvis()) {
                                return false;
                            }
                        }

                        if (this._calcType == 3 && (cha instanceof L1PcInstance || cha instanceof L1PetInstance || cha instanceof L1SummonInstance)) {
                            flg = true;
                        }

                        if (this._calcType == 2 && cha instanceof L1PcInstance && cha.isSafetyZone()) {
                            return false;
                        } else if (this._calcType != 2 || !(this._target instanceof L1NpcInstance) || this._target instanceof L1PetInstance || this._target instanceof L1SummonInstance || !(cha instanceof L1PetInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PcInstance)) {
                            if (this._calcType == 2 && this._target instanceof L1NpcInstance && !(this._target instanceof L1GuardInstance) && cha instanceof L1GuardInstance) {
                                return false;
                            } else if ((this._skill.getTarget().equals("attack") || this._skill.getType() == 64) && this._calcType == 3 && !(cha instanceof L1PetInstance) && !(cha instanceof L1SummonInstance) && !(cha instanceof L1PcInstance)) {
                                return false;
                            } else if ((this._skill.getTarget().equals("attack") || this._skill.getType() == 64) && this._calcType == 4 && this._user instanceof L1MonsterInstance && cha instanceof L1MonsterInstance) {
                                return false;
                            } else if (this._skill.getTarget().equals("none") && this._skill.getType() == 64 && (cha instanceof L1CrownInstance || cha instanceof L1DwarfInstance || cha instanceof L1EffectInstance || cha instanceof L1FieldObjectInstance || cha instanceof L1FurnitureInstance || cha instanceof L1HousekeeperInstance || cha instanceof L1MerchantInstance || cha instanceof L1TeleporterInstance)) {
                                return false;
                            } else if (this._skill.getType() == 64 && cha.getId() == this._user.getId()) {
                                return false;
                            } else if (cha.getId() == this._user.getId() && this._skillId == 49) {
                                return false;
                            } else if (((this._skill.getTargetTo() & 1) == 1 || (this._skill.getTargetTo() & 4) == 4 || (this._skill.getTargetTo() & 8) == 8) && cha.getId() == this._user.getId() && this._skillId != 49) {
                                return true;
                            } else {
                                L1SummonInstance summon;
                                L1PetInstance pet;
                                if (this._user instanceof L1PcInstance && (this._skill.getTarget().equals("attack") || this._skill.getType() == 64) && !this._isPK) {
                                    if (cha instanceof L1SummonInstance) {
                                        summon = (L1SummonInstance)cha;
                                        if (this._player.getId() == summon.getMaster().getId()) {
                                            return false;
                                        }
                                    } else if (cha instanceof L1PetInstance) {
                                        pet = (L1PetInstance)cha;
                                        if (this._player.getId() == pet.getMaster().getId()) {
                                            return false;
                                        }
                                    }
                                }

                                if ((this._skill.getTarget().equals("attack") || this._skill.getType() == 64) && !(cha instanceof L1MonsterInstance) && !this._isPK && this._target instanceof L1PcInstance) {
                                    xpc = null;

                                    try {
                                        xpc = (L1PcInstance)cha;
                                    } catch (Exception var6) {
                                        return false;
                                    }

                                    if (this._skillId != 72 || xpc.getZoneType() == 1 || !cha.hasSkillEffect(60) && !cha.hasSkillEffect(97)) {
                                        if (this._player.getClanid() != 0 && xpc.getClanid() != 0) {
                                            Iterator var5 = WorldWar.get().getWarList().iterator();

                                            while(var5.hasNext()) {
                                                L1War war = (L1War)var5.next();
                                                if (war.checkClanInWar(this._player.getClanname()) && war.checkClanInSameWar(this._player.getClanname(), xpc.getClanname()) && L1CastleLocation.checkInAllWarArea(xpc.getX(), xpc.getY(), xpc.getMapId())) {
                                                    return true;
                                                }
                                            }
                                        }

                                        return false;
                                    } else {
                                        return true;
                                    }
                                } else if (!this._user.glanceCheck(cha.getX(), cha.getY()) && !this._skill.isThrough() && this._skill.getType() != 2 && this._skill.getType() != 32) {
                                    this._isGlanceCheckFail = true;
                                    return false;
                                } else if (cha.hasSkillEffect(50) && (this._skillId == 50 || this._skillId == 80 || this._skillId == 194)) {
                                    return false;
                                } else if (!cha.hasSkillEffect(80) || this._skillId != 50 && this._skillId != 80 && this._skillId != 194) {
                                    if (cha.hasSkillEffect(194) && (this._skillId == 50 || this._skillId == 80 || this._skillId == 194)) {
                                        return false;
                                    } else if (cha.hasSkillEffect(157) && this._skillId == 157) {
                                        return false;
                                    } else if (!(cha instanceof L1MonsterInstance) && (this._skillId == 36 || this._skillId == 41)) {
                                        return false;
                                    } else if (cha.isDead() && this._skillId != 41 && this._skillId != 61 && this._skillId != 75 && this._skillId != 165) {
                                        return false;
                                    } else if (cha.isDead() || this._skillId != 41 && this._skillId != 61 && this._skillId != 75 && this._skillId != 165) {
                                        if (!(cha instanceof L1TowerInstance) && !(cha instanceof L1DoorInstance) || this._skillId != 41 && this._skillId != 61 && this._skillId != 75 && this._skillId != 165) {
                                            if (cha instanceof L1PcInstance) {
                                                xpc = (L1PcInstance)cha;
                                                if (xpc.hasSkillEffect(78)) {
                                                    switch(this._skillId) {
                                                        case 13:
                                                        case 20:
                                                        case 26:
                                                        case 27:
                                                        case 29:
                                                        case 37:
                                                        case 40:
                                                        case 42:
                                                        case 44:
                                                        case 47:
                                                        case 48:
                                                        case 56:
                                                        case 64:
                                                        case 66:
                                                        case 68:
                                                        case 69:
                                                        case 71:
                                                        case 72:
                                                        case 76:
                                                        case 151:
                                                        case 152:
                                                        case 153:
                                                            return true;
                                                        default:
                                                            return false;
                                                    }
                                                }
                                            }

                                            if (cha instanceof L1NpcInstance) {
                                                int hiddenStatus = ((L1NpcInstance)cha).getHiddenStatus();
                                                switch(hiddenStatus) {
                                                    case 1:
                                                        switch(this._skillId) {
                                                            case 13:
                                                            case 72:
                                                                return true;
                                                            default:
                                                                return false;
                                                        }
                                                    case 2:
                                                        return false;
                                                }
                                            }

                                            if ((this._skill.getTargetTo() & 1) == 1 && cha instanceof L1PcInstance) {
                                                flg = true;
                                            } else if ((this._skill.getTargetTo() & 2) == 2 && (cha instanceof L1MonsterInstance || cha instanceof L1NpcInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance)) {
                                                flg = true;
                                            } else if ((this._skill.getTargetTo() & 16) == 16 && this._user instanceof L1PcInstance) {
                                                if (cha instanceof L1SummonInstance) {
                                                    summon = (L1SummonInstance)cha;
                                                    if (summon.getMaster() != null && this._player.getId() == summon.getMaster().getId()) {
                                                        flg = true;
                                                    }
                                                }

                                                if (cha instanceof L1PetInstance) {
                                                    pet = (L1PetInstance)cha;
                                                    if (pet.getMaster() != null && this._player.getId() == pet.getMaster().getId()) {
                                                        flg = true;
                                                    }
                                                }
                                            }

                                            if (this._calcType == 1 && cha instanceof L1PcInstance) {
                                                xpc = (L1PcInstance)cha;
                                                if ((this._skill.getTargetTo() & 4) == 4 && (this._player.getClanid() != 0 && this._player.getClanid() == xpc.getClanid() || this._player.isGm())) {
                                                    return true;
                                                }

                                                if ((this._skill.getTargetTo() & 8) == 8 && (this._player.getParty().isMember(xpc) || this._player.isGm())) {
                                                    return true;
                                                }
                                            }

                                            return flg;
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        return false;
                                    }
                                } else {
                                    return false;
                                }
                            }
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        }
    }

    private boolean isParty(L1NpcInstance npc, L1Character cha) {
        if (npc.getMaster() == null) {
            return false;
        } else if (npc.isHate(cha)) {
            return false;
        } else {
            int masterId = npc.getMaster().getId();
            if (cha instanceof L1PcInstance) {
                return cha.getId() == masterId;
            } else if (cha instanceof L1PetInstance) {
                L1PetInstance tgPet = (L1PetInstance)cha;
                return tgPet.getMaster() != null && tgPet.getMaster().getId() == masterId;
            } else if (cha instanceof L1SummonInstance) {
                L1SummonInstance tgSu = (L1SummonInstance)cha;
                return tgSu.getMaster() != null && tgSu.getMaster().getId() == masterId;
            } else {
                return false;
            }
        }
    }

    private void makeTargetList() {
        try {
            if (this._type == 1) {
                this._targetList.add(new TargetStatus(this._user));
                return;
            }

            if (this._skill.getTargetTo() == 0 && (this._skill.getType() & 64) != 64) {
                this._targetList.add(new TargetStatus(this._user));
                return;
            }

            if (this._skill.getRanged() != -1) {
                if (this._user.getLocation().getTileLineDistance(this._target.getLocation()) > this._skill.getRanged()) {
                    return;
                }
            } else if (!this._user.getLocation().isInScreen(this._target.getLocation())) {
                return;
            }

            if (!this.isTarget(this._target) && !this._skill.getTarget().equals("none")) {
                return;
            }

            ArrayList objects;
            L1Object tgobj;
            Iterator var3;
            L1Character cha;
            switch(this._calcType) {
                case 17:
                case 194:
                    objects = World.get().getVisibleLineObjects(this._user, this._target);
                    var3 = objects.iterator();

                    while(var3.hasNext()) {
                        tgobj = (L1Object)var3.next();
                        if (tgobj != null && tgobj instanceof L1Character) {
                            cha = (L1Character)tgobj;
                            if (this.isTarget(cha)) {
                                this._targetList.add(new TargetStatus(cha));
                            }
                        }
                    }

                    objects.clear();
                    return;
            }

            if (this._skill.getArea() != 0) {
                if (!this._skill.getTarget().equals("none")) {
                    this._targetList.add(new TargetStatus(this._target));
                }

                if (this._skillId != 49 && !this._skill.getTarget().equals("attack") && this._skill.getType() != 64) {
                    this._targetList.add(new TargetStatus(this._user));
                }

                if (this._skill.getArea() == -1) {
                    objects = World.get().getVisibleObjects(this._user);
                } else {
                    objects = World.get().getVisibleObjects(this._target, this._skill.getArea());
                }

                var3 = objects.iterator();

                while(true) {
                    L1MonsterInstance mob;
                    do {
                        do {
                            do {
                                if (!var3.hasNext()) {
                                    return;
                                }

                                tgobj = (L1Object)var3.next();
                            } while(tgobj == null);
                        } while(!(tgobj instanceof L1Character));

                        if (!(tgobj instanceof L1MonsterInstance)) {
                            break;
                        }

                        mob = (L1MonsterInstance)tgobj;
                    } while(mob.getNpcId() == 45166 || mob.getNpcId() == 45167);

                    cha = (L1Character)tgobj;
                    if (this.isTarget(cha)) {
                        this._targetList.add(new TargetStatus(cha));
                    }
                }
            }

            if (!this._user.glanceCheck(this._target.getX(), this._target.getY()) && (this._skill.getType() & 64) == 64 && this._skillId != 10026 && this._skillId != 10027 && this._skillId != 10028 && this._skillId != 10029) {
                this._targetList.add(new TargetStatus(this._target, false));
                return;
            }

            this._targetList.add(new TargetStatus(this._target));
        } catch (Exception var5) {
        }

    }

    private void sendHappenMessage(L1PcInstance pc) {
        int msgID = this._skill.getSysmsgIdHappen();
        if (msgID > 0) {
            pc.sendPackets(new S_ServerMessage(msgID));
        }

    }

    private void sendFailMessageHandle() {
        if (this._skill.getType() != 64 && !this._skill.getTarget().equals("none") && this._targetList.size() == 0) {
            this.sendFailMessage();
        }

    }

    private void sendFailMessage() {
        int msgID = this._skill.getSysmsgIdFail();
        if (msgID > 0 && this._user instanceof L1PcInstance) {
            this._player.sendPackets(new S_ServerMessage(msgID));
        }

    }

    private boolean isAttrAgrees() {
        int magicattr = this._skill.getAttr();
        if (this._user instanceof L1NpcInstance) {
            return true;
        } else {
            return this._skill.getSkillLevel() < 17 || this._skill.getSkillLevel() > 22 || magicattr == 0 || magicattr == this._player.getElfAttr() || this._player.isGm();
        }
    }

    private boolean isHPMPConsume() {
        this._mpConsume = this._skill.getMpConsume();
        this._hpConsume = this._skill.getHpConsume();

        int currentMp;
        int currentHp;
        if (this._user instanceof L1NpcInstance) {
            currentMp = this._npc.getCurrentMp();
            currentHp = this._npc.getCurrentHp();
        } else {
            currentMp = this._player.getCurrentMp();
            currentHp = this._player.getCurrentHp();
            if (this._player.getInt() > 12 && this._skillId > 8 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 13 && this._skillId > 16 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 14 && this._skillId > 23 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 15 && this._skillId > 32 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 16 && this._skillId > 40 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 17 && this._skillId > 48 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 18 && this._skillId > 56 && this._skillId <= 80) {
                --this._mpConsume;
            }

            if (this._player.getInt() > 12 && this._skillId >= 87 && this._skillId <= 91) {
                this._mpConsume -= this._player.getInt() - 12;
            }

            if (this._skillId == 26 && this._player.getInventory().checkEquipped(20013)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 43 && this._player.getInventory().checkEquipped(20013)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 1 && this._player.getInventory().checkEquipped(20014)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 19 && this._player.getInventory().checkEquipped(20014)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 12 && this._player.getInventory().checkEquipped(20015)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 13 && this._player.getInventory().checkEquipped(20015)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 42 && this._player.getInventory().checkEquipped(20015)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 43 && this._player.getInventory().checkEquipped(20008)) {
                this._mpConsume >>= 1;
            }

            if (this._skillId == 54 && this._player.getInventory().checkEquipped(20023)) {
                this._mpConsume >>= 1;
            }

            if (this._skill.getMpConsume() > 0) {
                this._mpConsume = Math.max(this._mpConsume, 1);
            }

            if (this._player.getOriginalMagicConsumeReduction() > 0) {
                this._mpConsume -= this._player.getOriginalMagicConsumeReduction();
            }
        }

        if (currentHp < this._hpConsume + 1) {
            if (this._user instanceof L1PcInstance) {
                this._player.sendPackets(new S_ServerMessage(279));
            }

            return false;
        } else if (currentMp < this._mpConsume) {
            if (this._user instanceof L1PcInstance) {
                this._player.sendPackets(new S_ServerMessage(278));
                if (this._player.isGm()) {
                    this._player.setCurrentMp(this._player.getMaxMp());
                }
            }

            return false;
        } else {
            return true;
        }
    }

    private boolean isItemConsume() {
        int itemConsume = this._skill.getItemConsumeId();
        int itemConsumeCount = this._skill.getItemConsumeCount();
        if (itemConsume == 0) {
            return true;
        } else {
            return this._player.getInventory().checkItem(itemConsume, (long)itemConsumeCount);
        }
    }

    private void useConsume() throws Exception {
        int lawful;
        int itemConsume;
        if (this._user instanceof L1NpcInstance) {
            lawful = this._npc.getCurrentHp() - this._hpConsume;
            this._npc.setCurrentHp(lawful);
            itemConsume = this._npc.getCurrentMp() - this._mpConsume;
            this._npc.setCurrentMp(itemConsume);
        } else {
            if (this.isHPMPConsume()) {
                if (this._skillId == 108) {
                    this._player.setCurrentHp(1);
                    this._player.setCurrentMp(0);
                } else {
                    lawful = this._player.getCurrentHp() - this._hpConsume;
                    this._player.setCurrentHp(lawful);
                    itemConsume = this._player.getCurrentMp() - this._mpConsume;
                    this._player.setCurrentMp(itemConsume);
                }
            }

            lawful = this._player.getLawful() + this._skill.getLawful();
            if (lawful > 32767) {
                lawful = 32767;
            }

            if (lawful < -32767) {
                lawful = -32767;
            }

            this._player.setLawful(lawful);
            itemConsume = this._skill.getItemConsumeId();
            int itemConsumeCount = this._skill.getItemConsumeCount();
            if (itemConsume != 0) {
                this._player.getInventory().consumeItem(itemConsume, (long)itemConsumeCount);
            }
        }
    }

    private void addMagicList(L1Character cha, boolean repetition) {
        if (this._skillTime == 0) {
            this._getBuffDuration = this._skill.getBuffDuration() * 1000;
            if (this._skill.getBuffDuration() == 0) {
                if (this._skillId == 60) {
                    cha.setSkillEffect(60, 0);
                }

                return;
            }
        } else {
            this._getBuffDuration = this._skillTime * 1000;
        }

        if (this._skillId == 87) {
            this._getBuffDuration = this._shockStunDuration;
        }

        if (this._skillId != 11) {
            if (this._skillId != 33 && this._skillId != 4001) {
                if (this._skillId != 67) {
                    if (this._skillId != 21 && this._skillId != 8 && this._skillId != 12 && this._skillId != 48 && this._skillId != 107) {
                        if (this._skillId != 50 && this._skillId != 80 && this._skillId != 194 || this._isFreeze) {
                            SkillMode mode = L1SkillMode.get().getSkill(this._skillId);
                            if (mode == null) {
                                cha.setSkillEffect(this._skillId, this._getBuffDuration);
                            }

                            if (cha instanceof L1PcInstance && repetition) {
                                L1PcInstance pc = (L1PcInstance)cha;
                                this.sendIcon(pc);
                            }

                        }
                    }
                }
            }
        }
    }

    private void sendIcon(L1PcInstance pc) {
        if (this._skillTime == 0) {
            this._getBuffIconDuration = this._skill.getBuffDuration();
        } else {
            this._getBuffIconDuration = this._skillTime;
        }

        switch(this._skillId) {
            case 3:
                pc.sendPackets(new S_SkillIconShield(5, this._getBuffIconDuration));
                break;
            case 26:
                pc.sendPackets(new S_Dexup(pc, 5, this._getBuffIconDuration));
                break;
            case 29:
            case 76:
            case 152:
                pc.sendPackets(new S_SkillHaste(pc.getId(), 2, this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 2, 0));
                break;
            case 42:
                pc.sendPackets(new S_Strup(pc, 5, this._getBuffIconDuration));
                break;
            case 43:
            case 54:
                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                break;
            case 52:
            case 101:
            case 150:
                pc.sendPackets(new S_SkillBrave(pc.getId(), 4, this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 4, 0));
                break;
            case 68:
                pc.sendPackets(new S_PacketBox(40, this._getBuffIconDuration));
                break;
            case 99:
                pc.sendPackets(new S_SkillIconShield(3, this._getBuffIconDuration));
                break;
            case 109:
                pc.sendPackets(new S_Strup(pc, 2, this._getBuffIconDuration));
                break;
            case 110:
                pc.sendPackets(new S_Dexup(pc, 2, this._getBuffIconDuration));
                break;
            case 112:
                pc.sendPackets(new S_PacketBoxIconAura(119, this._getBuffIconDuration));
                break;
            case 114:
                pc.sendPackets(new S_PacketBoxIconAura(113, this._getBuffIconDuration));
                break;
            case 115:
                pc.sendPackets(new S_PacketBoxIconAura(114, this._getBuffIconDuration));
                break;
            case 117:
                pc.sendPackets(new S_PacketBoxIconAura(116, this._getBuffIconDuration));
                break;
            case 148:
                pc.sendPackets(new S_PacketBoxIconAura(147, this._getBuffIconDuration));
                break;
            case 149:
                pc.sendPackets(new S_PacketBoxIconAura(148, this._getBuffIconDuration));
                break;
            case 151:
                pc.sendPackets(new S_SkillIconShield(6, this._getBuffIconDuration));
                break;
            case 155:
                pc.sendPackets(new S_PacketBoxIconAura(154, this._getBuffIconDuration));
                break;
            case 156:
                pc.sendPackets(new S_PacketBoxIconAura(155, this._getBuffIconDuration));
                break;
            case 159:
                pc.sendPackets(new S_SkillIconShield(7, this._getBuffIconDuration));
                break;
            case 163:
                pc.sendPackets(new S_PacketBoxIconAura(162, this._getBuffIconDuration));
                break;
            case 166:
                pc.sendPackets(new S_PacketBoxIconAura(165, this._getBuffIconDuration));
                break;
            case 168:
                pc.sendPackets(new S_SkillIconShield(10, this._getBuffIconDuration));
                break;
            case 186:
                pc.sendPackets(new S_SkillBrave(pc.getId(), 6, this._getBuffIconDuration));
                pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 6, 0));
        }

        pc.sendPackets(new S_OwnCharStatus(pc));
    }

    private void sendGrfx(boolean isSkillAction) {
        int actionId = this._skill.getActionId();
        int castgfx = this._skill.getCastGfx();
        if (castgfx != 0) {
            int targetid;
            if (this._user instanceof L1PcInstance) {
                if (this._skillId == 58 || this._skillId == 63) {
                    L1PcInstance pc = (L1PcInstance)this._user;
                    if (this._skillId == 58) {
                        pc.setHeading(pc.targetDirection(this._targetX, this._targetY));
                        pc.sendPacketsAll(new S_ChangeHeading(pc));
                    }

                    pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), actionId));
                    return;
                }

                targetid = this._target.getId();
                L1PcInstance pc;
                if (this._skillId == 87) {
                    if (this._targetList.size() == 0) {
                        return;
                    }

                    if (this._target instanceof L1PcInstance) {
                        pc = (L1PcInstance)this._target;
                        pc.sendPacketsAll(new S_SkillSound(pc.getId(), 4434));
                    } else if (this._target instanceof L1NpcInstance) {
                        this._target.broadcastPacketX10(new S_SkillSound(this._target.getId(), 4434));
                    }

                    return;
                }

                if (this._skillId == 2) {
                    pc = (L1PcInstance)this._target;
                    pc.sendPackets(new S_Sound(145));
                }

                if (this._targetList.size() == 0 && !this._skill.getTarget().equals("none")) {
                    int tempchargfx = this._player.getTempCharGfx();
                    switch(tempchargfx) {
                        case 5727:
                        case 5730:
                            actionId = 19;
                        case 5728:
                        case 5729:
                        case 5731:
                        case 5732:
                        case 5734:
                        case 5735:
                        default:
                            break;
                        case 5733:
                        case 5736:
                            actionId = 1;
                    }

                    if (isSkillAction) {
                        this._player.sendPacketsX10(new S_DoActionGFX(this._player.getId(), actionId));
                    }

                    return;
                }

                if (this._skill.getTarget().equals("attack") && this._skillId != 18) {
                    if (this.isPcSummonPet(this._target) && (this._player.isSafetyZone() || this._target.isSafetyZone() || this._player.checkNonPvP(this._player, this._target))) {
                        this._player.sendPacketsX10(new S_UseAttackSkill(this._player, 0, castgfx, this._targetX, this._targetY, actionId, this._dmg));
                        return;
                    }

                    if (this._skill.getArea() == 0) {
                        this._player.sendPacketsX10(new S_UseAttackSkill(this._player, targetid, castgfx, this._targetX, this._targetY, actionId, this._dmg));
                    } else {
                        this._player.sendPacketsX10(new S_RangeSkill(this._player, this._targetList, castgfx, actionId, 8));
                    }
                } else if (this._skill.getTarget().equals("none") && this._skill.getType() == 64) {
                    this._player.sendPacketsX10(new S_RangeSkill(this._player, this._targetList, castgfx, actionId, 0));
                } else {
                    if (this._skillId != 5 && this._skillId != 69 && this._skillId != 131) {
                        if (isSkillAction) {
                            this._player.sendPacketsX10(new S_DoActionGFX(this._player.getId(), this._skill.getActionId()));
                        }

                        if (this._skillId != 31 && this._skillId != 91 && this._skillId != 134) {
                            if (this._skillId == 113) {
                                return;
                            }

                            if (this._skillId != 185 && this._skillId != 190 && this._skillId != 195) {
                                this._player.sendPacketsX10(new S_SkillSound(targetid, castgfx));
                            } else {
                                if (this._skillId != this._player.getAwakeSkillId()) {
                                    return;
                                }

                                this._player.sendPacketsX10(new S_SkillSound(targetid, castgfx));
                            }
                        } else {
                            this._player.sendPacketsX10(new S_SkillSound(targetid, castgfx));
                        }
                    }

                    Iterator var6 = this._targetList.iterator();

                    while(var6.hasNext()) {
                        TargetStatus ts = (TargetStatus)var6.next();
                        L1Character cha = ts.getTarget();
                        if (cha instanceof L1PcInstance) {
                            pc = (L1PcInstance)cha;
                            pc.sendPackets(new S_OwnCharStatus(pc));
                        }
                    }
                }
            } else if (this._user instanceof L1NpcInstance) {
                targetid = this._target.getId();
                if (this._user instanceof L1MerchantInstance) {
                    this._user.broadcastPacketX10(new S_SkillSound(targetid, castgfx));
                    return;
                }

                if (this._targetList.size() == 0 && !this._skill.getTarget().equals("none")) {
                    this._user.broadcastPacketX10(new S_DoActionGFX(this._user.getId(), this._skill.getActionId()));
                    return;
                }

                if (this._skill.getTarget().equals("attack") && this._skillId != 18) {
                    if (this._skill.getArea() == 0) {
                        this._user.broadcastPacketX10(new S_UseAttackSkill(this._user, targetid, castgfx, this._targetX, this._targetY, actionId, this._dmg));
                    } else {
                        this._user.broadcastPacketX10(new S_RangeSkill(this._user, this._targetList, castgfx, actionId, 8));
                    }
                } else if (this._skill.getTarget().equals("none") && this._skill.getType() == 64) {
                    this._user.broadcastPacketX10(new S_RangeSkill(this._user, this._targetList, castgfx, actionId, 0));
                } else if (this._skillId != 5 && this._skillId != 69 && this._skillId != 131) {
                    this._user.broadcastPacketX10(new S_DoActionGFX(this._user.getId(), this._skill.getActionId()));
                    this._user.broadcastPacketX10(new S_SkillSound(targetid, castgfx));
                }
            }

        }
    }

    private void deleteRepeatedSkills(L1Character cha) {
        int[][] var5;
        int var4 = (var5 = REPEATEDSKILLS).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            int[] skills = var5[var3];
            int[] var9 = skills;
            int var8 = skills.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                int id = var9[var7];
                if (id == this._skillId) {
                    this.stopSkillList(cha, skills);
                }
            }
        }

    }

    private void stopSkillList(L1Character cha, int[] repeat_skill) {
        int[] var6 = repeat_skill;
        int var5 = repeat_skill.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            int skillId = var6[var4];
            if (skillId != this._skillId) {
                cha.removeSkillEffect(skillId);
            }
        }

    }

    private void setDelay() {
        if (this._skill.getReuseDelay() > 0) {
            L1SkillDelay.onSkillUse(this._user, this._skill.getReuseDelay());
        }

    }

    private void runSkill() throws Exception {
        L1PcInstance ts;
        switch(this._skillId) {
            case 58:
                L1SpawnUtil.doSpawnFireWall(this._user, this._targetX, this._targetY);
                return;
            case 63:
                L1SpawnUtil.spawnEffect(81169, this._skill.getBuffDuration(), this._targetX, this._targetY, this._user.getMapId(), this._user, 0);
                return;
            case 112:
                if (this._calcType == 1) {
                    ts = (L1PcInstance)this._target;
                    L1PinkName.onAction(ts, this._user);
                    this._target.setSkillEffect(112, 8000);
                }

                return;
            case 205:
                L1SpawnUtil.spawnEffect(80149, this._skill.getBuffDuration(), this._targetX, this._targetY, this._user.getMapId(), this._user, this._skillId);
                return;
            case 210:
                L1SpawnUtil.spawnEffect(80150, this._skill.getBuffDuration(), this._targetX, this._targetY, this._user.getMapId(), this._user, this._skillId);
                return;
            case 215:
                L1SpawnUtil.spawnEffect(80151, this._skill.getBuffDuration(), this._targetX, this._targetY, this._user.getMapId(), this._user, this._skillId);
                return;
            case 220:
                L1SpawnUtil.spawnEffect(80152, this._skill.getBuffDuration(), this._targetX, this._targetY, this._user.getMapId(), this._user, this._skillId);
                return;
            default:
                int[] var4;
                int drainMana = (var4 = EXCEPT_COUNTER_MAGIC).length;

                for(int var2 = 0; var2 < drainMana; ++var2) {
                    int skillId = var4[var2];
                    if (this._skillId == skillId) {
                        this._isCounterMagic = false;
                        break;
                    }
                }

                if (this._skillId == 87 && this._user instanceof L1PcInstance) {
                    this._target.onAction(this._player);
                }

                if (this.isTargetCalc(this._target)) {
                    try {
                        L1Character cha = null;
                        drainMana = 0;
                        boolean isSuccess = false;
                        Iterator iter = this._targetList.iterator();

                        while(true) {
                            while(iter.hasNext()) {

                                int heal = 0;
                                int undeadType = 0;
                                TargetStatus targetStatus = (TargetStatus)iter.next();
                                cha = targetStatus.getTarget();
                                if (this._npc != null) {
                                    if (this._npc instanceof L1PetInstance && this.isParty(this._npc, cha)) {
                                        targetStatus.isCalc(false);
                                        this._dmg = 0;
                                        continue;
                                    }

                                    if (this._npc instanceof L1SummonInstance && this.isParty(this._npc, cha)) {
                                        targetStatus.isCalc(false);
                                        this._dmg = 0;
                                        continue;
                                    }
                                }

                                if (targetStatus.isCalc() && this.isTargetCalc(cha)) {
                                    L1Magic magic = new L1Magic(this._user, cha);
                                    magic.setLeverage(this.getLeverage());
                                    if (cha instanceof L1MonsterInstance) {
                                        undeadType = ((L1MonsterInstance)cha).getNpcTemplate().get_undead();
                                    }

                                    if ((this._skill.getType() == 4 || this._skill.getType() == 1) && this.isTargetFailure(cha)) {
                                        iter.remove();
                                    } else {
                                        if (cha instanceof L1PcInstance) {
                                            if (this._skillTime == 0) {
                                                this._getBuffIconDuration = this._skill.getBuffDuration();
                                            } else {
                                                this._getBuffIconDuration = this._skillTime;
                                            }
                                        }

                                        this.deleteRepeatedSkills(cha);
                                        switch(this._skill.getType()) {
                                            case 1:
                                            case 4:
                                                isSuccess = magic.calcProbabilityMagic(this._skillId);
                                                if (this._type == 4) {
                                                    isSuccess = true;
                                                }

                                                if (cha.hasSkillEffect(153) && this._skillId != 153) {
                                                    cha.removeSkillEffect(153);
                                                }

                                                if (this._skillId != 66 || this._skillId != 212 || this._skillId != 103) {
                                                    cha.removeSkillEffect(66);
                                                    cha.removeSkillEffect(212);
                                                    cha.removeSkillEffect(103);
                                                }

                                                if (!isSuccess) {
                                                    if ((this._skillId == 66 || this._skillId == 212 || this._skillId == 103) && cha instanceof L1PcInstance) {
                                                        L1PcInstance pc = (L1PcInstance)cha;
                                                        pc.sendPackets(new S_ServerMessage(297));
                                                    }

                                                    iter.remove();
                                                    continue;
                                                }

                                                if (this.isUseCounterMagic(cha)) {
                                                    iter.remove();
                                                    continue;
                                                }
                                                break;
                                            case 16:
                                                this._dmg = -1 * magic.calcHealing(this._skillId);
                                                if (cha.hasSkillEffect(170)) {
                                                    this._dmg <<= 1;
                                                }

                                                if (cha.hasSkillEffect(173)) {
                                                    this._dmg >>= 1;
                                                }

                                                if (cha.hasSkillEffect(4012)) {
                                                    this._dmg >>= 1;
                                                }

                                                if (cha.hasSkillEffect(4013)) {
                                                    this._dmg *= -1;
                                                }
                                                break;
                                            case 64:
                                                if (this._user.getId() != cha.getId()) {
                                                    if (this.isUseCounterMagic(cha)) {
                                                        iter.remove();
                                                        continue;
                                                    }

                                                    this._dmg = magic.calcMagicDamage(this._skillId);
                                                    cha.removeSkillEffect(153);
                                                }
                                        }

                                        SkillMode mode = L1SkillMode.get().getSkill(this._skillId);
                                        if (mode != null) {
                                            if (this._user instanceof L1PcInstance) {
                                                switch(this._skillId) {
                                                    case 5:
                                                    case 69:
                                                        this._dmg = mode.start(this._player, cha, magic, this._bookmarkId);
                                                        break;
                                                    case 116:
                                                    case 118:
                                                        this._dmg = mode.start(this._player, cha, magic, this._targetID);
                                                        break;
                                                    default:
                                                        this._dmg = mode.start(this._player, cha, magic, this._getBuffIconDuration);
                                                }
                                            }

                                            if (this._user instanceof L1NpcInstance) {
                                                this._dmg = mode.start(this._npc, cha, magic, this._getBuffIconDuration);
                                            }
                                        } else if (cha.hasSkillEffect(this._skillId)) {
                                            this.addMagicList(cha, true);
                                            continue;
                                        }

                                        L1NpcInstance npc;
                                        int i;
                                        int weaponDamage;
                                        L1PcInstance pc;
                                        L1ItemInstance item;
                                        Random rad;
                                        if (this._skillId == 13) {
                                            if (cha instanceof L1NpcInstance) {
                                                npc = (L1NpcInstance)cha;
                                                i = npc.getHiddenStatus();
                                                if (i == 1) {
                                                    npc.appearOnGround(this._player);
                                                }
                                            }
                                        } else if (this._skillId == 72) {
                                            if (cha instanceof L1PcInstance) {
                                                this._dmg = magic.calcMagicDamage(this._skillId);
                                            } else if (cha instanceof L1NpcInstance) {
                                                npc = (L1NpcInstance)cha;
                                                i = npc.getHiddenStatus();
                                                if (i == 1) {
                                                    npc.appearOnGround(this._player);
                                                } else {
                                                    this._dmg = 0;
                                                }
                                            } else {
                                                this._dmg = 0;
                                            }
                                        } else if (this._skillId != 1 && this._skillId != 19 && this._skillId != 35 && this._skillId != 57 && this._skillId != 49 && this._skillId != 158 && this._skillId != 164) {
                                            if (this._skillId != 10 && this._skillId != 28) {
                                                if (this._skillId != 10026 && this._skillId != 10027 && this._skillId != 10028 && this._skillId != 10029) {
                                                    if (this._skillId == 10057) {
                                                        L1Teleport.teleportToTargetFront(cha, this._user, 1);
                                                    } else if (this._skillId != 29 && this._skillId != 76 && this._skillId != 152) {
                                                        if (this._skillId == 11) {
                                                            L1DamagePoison.doInfection(this._user, cha, 3000, 5);
                                                        } else if (this._skillId == 47) {
                                                            if (cha instanceof L1PcInstance) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addDmgup(-5);
                                                                pc.addHitup(-1);
                                                            }
                                                        } else if (this._skillId == 56) {
                                                            if (cha instanceof L1PcInstance) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addDmgup(-6);
                                                                pc.addAc(12);
                                                            }
                                                        } else if (this._skillId != 50 && this._skillId != 80 && this._skillId != 194) {
                                                            if (this._skillId == 157) {
                                                                if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    pc.sendPacketsAll(new S_Poison(pc.getId(), 2));
                                                                    pc.sendPackets(new S_Paralysis(4, true));
                                                                } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
                                                                    npc = (L1NpcInstance)cha;
                                                                    npc.broadcastPacketAll(new S_Poison(npc.getId(), 2));
                                                                    npc.setParalyzed(true);
                                                                }
                                                            } else if (this._skillId == 18) {
                                                                if (undeadType == 1 || undeadType == 3) {
                                                                    this._dmg = cha.getCurrentHp();
                                                                }
                                                            } else if (this._skillId == 39) {
                                                                rad = new Random();
                                                                i = rad.nextInt(10) + 5;
                                                                drainMana = i + this._user.getInt() / 2;
                                                                if (cha.getCurrentMp() < drainMana) {
                                                                    drainMana = cha.getCurrentMp();
                                                                }
                                                            } else if (this._skillId == 27) {
                                                                if (this._calcType != 1 && this._calcType != 3) {
                                                                    ((L1NpcInstance)cha).setWeaponBreaked(true);
                                                                } else if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    item = pc.getWeapon();
                                                                    if (item != null) {
                                                                        Random random = new Random();
                                                                        weaponDamage = random.nextInt(this._user.getInt() / 3) + 1;
                                                                        pc.sendPackets(new S_ServerMessage(268, item.getLogName()));
                                                                        pc.getInventory().receiveDamage(item, weaponDamage);
                                                                    }
                                                                }
                                                            } else if (this._skillId == 66) {
                                                                if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    pc.sendPackets(new S_Paralysis(3, true));
                                                                }

                                                                cha.setSleeped(true);
                                                            } else if (this._skillId == 183) {
                                                                if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    pc.addAc(15);
                                                                }
                                                            } else if (this._skillId == 193 && cha instanceof L1PcInstance) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addStr(-5);
                                                                pc.addInt(-5);
                                                            }
                                                        } else {
                                                            this._isFreeze = magic.calcProbabilityMagic(this._skillId);
                                                            if (this._isFreeze) {
                                                                if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    L1SpawnUtil.spawnEffect(81168, this._skill.getBuffDuration(), cha.getX(), cha.getY(), cha.getMapId(), this._user, 0);
                                                                    pc.sendPacketsAll(new S_Poison(pc.getId(), 2));
                                                                    pc.sendPackets(new S_Paralysis(4, true));
                                                                } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
                                                                    npc = (L1NpcInstance)cha;
                                                                    L1SpawnUtil.spawnEffect(81168, this._skill.getBuffDuration(), cha.getX(), cha.getY(), cha.getMapId(), this._user, 0);
                                                                    npc.broadcastPacketAll(new S_Poison(npc.getId(), 2));
                                                                    npc.setParalyzed(true);
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (cha instanceof L1PcInstance) {
                                                            pc = (L1PcInstance)cha;
                                                            if (pc.getHasteItemEquipped() > 0) {
                                                                continue;
                                                            }
                                                        }

                                                        if (cha.getBraveSpeed() == 5) {
                                                            continue;
                                                        }

                                                        switch(cha.getMoveSpeed()) {
                                                            case 0:
                                                                if (cha instanceof L1PcInstance) {
                                                                    pc = (L1PcInstance)cha;
                                                                    pc.sendPackets(new S_SkillHaste(pc.getId(), 2, this._getBuffIconDuration));
                                                                }

                                                                cha.broadcastPacketAll(new S_SkillHaste(cha.getId(), 2, this._getBuffIconDuration));
                                                                cha.setMoveSpeed(2);
                                                                break;
                                                            case 1:
                                                                int skillNum = 0;
                                                                if (cha.hasSkillEffect(43)) {
                                                                    skillNum = 43;
                                                                } else if (cha.hasSkillEffect(54)) {
                                                                    skillNum = 54;
                                                                } else if (cha.hasSkillEffect(1001)) {
                                                                    skillNum = 1001;
                                                                }

                                                                if (skillNum != 0) {
                                                                    cha.removeSkillEffect(skillNum);
                                                                    cha.removeSkillEffect(this._skillId);
                                                                    cha.setMoveSpeed(0);
                                                                    continue;
                                                                }
                                                        }
                                                    }
                                                } else if (this._user instanceof L1NpcInstance) {
                                                    this._user.broadcastPacketX8(new S_NpcChat(this._npc, "$3717"));
                                                } else {
                                                    this._player.broadcastPacketX8(new S_Chat(this._player, "$3717"));
                                                }
                                            } else {
                                                heal = this._dmg;
                                            }
                                        } else if (this._user instanceof L1PcInstance) {
                                            cha.removeSkillEffect(170);
                                        }

                                        int brave;
                                        int charisma;
                                        if (this._calcType == 1 || this._calcType == 3) {
                                            if (this._skillId == 73) {
                                                pc = (L1PcInstance)cha;
                                                item = pc.getInventory().getItem(this._itemobjid);
                                                if (item != null && item.getItem().getType2() == 1) {
                                                    charisma = item.getItem().getType2();
                                                    weaponDamage = item.getItem().get_safeenchant();
                                                    brave = item.getEnchantLevel();
                                                    String item_name = item.getName();
                                                    if (weaponDamage < 0) {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    } else if (weaponDamage == 0) {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    } else if (charisma == 1 && brave == 0) {
                                                        if (!item.isIdentified()) {
                                                            pc.sendPackets(new S_ServerMessage(161, item_name, "$245", "$247"));
                                                        } else {
                                                            item_name = "+0 " + item_name;
                                                            pc.sendPackets(new S_ServerMessage(161, "+0 " + item_name, "$245", "$247"));
                                                        }

                                                        item.setEnchantLevel(1);
                                                        pc.getInventory().updateItem(item, 4);
                                                    } else {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    }
                                                } else {
                                                    pc.sendPackets(new S_ServerMessage(79));
                                                }
                                            } else if (this._skillId == 100) {
                                                pc = (L1PcInstance)cha;
                                                Random random = new Random();
                                                item = pc.getInventory().getItem(this._itemobjid);
                                                if (item != null) {
                                                    weaponDamage = (int)(10.0D + (double)pc.getLevel() * 0.8D + (double)(pc.getWis() - 6) * 1.2D);
                                                    brave = (int)((double)weaponDamage / 2.1D);
                                                    int wise = (int)((double)brave / 2.0D);
                                                    int kayser = (int)((double)wise / 1.9D);
                                                    int chance = random.nextInt(100) + 1;
                                                    if (item.getItem().getItemId() == 40320) {
                                                        pc.getInventory().removeItem(item, 1L);
                                                        if (weaponDamage >= chance) {
                                                            pc.getInventory().storeItem(40321, 1L);
                                                            pc.sendPackets(new S_ServerMessage(403, "$2475"));
                                                        } else {
                                                            pc.sendPackets(new S_ServerMessage(280));
                                                        }
                                                    } else if (item.getItem().getItemId() == 40321) {
                                                        pc.getInventory().removeItem(item, 1L);
                                                        if (brave >= chance) {
                                                            pc.getInventory().storeItem(40322, 1L);
                                                            pc.sendPackets(new S_ServerMessage(403, "$2476"));
                                                        } else {
                                                            pc.sendPackets(new S_ServerMessage(280));
                                                        }
                                                    } else if (item.getItem().getItemId() == 40322) {
                                                        pc.getInventory().removeItem(item, 1L);
                                                        if (wise >= chance) {
                                                            pc.getInventory().storeItem(40323, 1L);
                                                            pc.sendPackets(new S_ServerMessage(403, "$2477"));
                                                        } else {
                                                            pc.sendPackets(new S_ServerMessage(280));
                                                        }
                                                    } else if (item.getItem().getItemId() == 40323) {
                                                        pc.getInventory().removeItem(item, 1L);
                                                        if (kayser >= chance) {
                                                            pc.getInventory().storeItem(40324, 1L);
                                                            pc.sendPackets(new S_ServerMessage(403, "$2478"));
                                                        } else {
                                                            pc.sendPackets(new S_ServerMessage(280));
                                                        }
                                                    }
                                                }
                                            } else if (this._skillId == 78) {
                                                pc = (L1PcInstance)cha;
                                                pc.stopHpRegeneration();
                                                pc.stopMpRegeneration();
                                            }

                                            if (this._skillId != 2) {
                                                if (this._skillId == 114) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addHitup(5);
                                                    pc.addBowHitup(5);
                                                    pc.addMr(20);
                                                    pc.sendPackets(new S_SPMR(pc));
                                                    pc.sendPackets(new S_PacketBoxIconAura(113, this._getBuffIconDuration));
                                                } else if (this._skillId == 115) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addAc(-8);
                                                    pc.sendPackets(new S_PacketBoxIconAura(114, this._getBuffIconDuration));
                                                } else if (this._skillId == 117) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addDmgup(5);
                                                    pc.sendPackets(new S_PacketBoxIconAura(116, this._getBuffIconDuration));
                                                } else if (this._skillId == 3) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addAc(-2);
                                                    pc.sendPackets(new S_SkillIconShield(5, this._getBuffIconDuration));
                                                } else if (this._skillId == 99) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addMr(5);
                                                    pc.sendPackets(new S_SPMR(pc));
                                                    pc.sendPackets(new S_SkillIconShield(3, this._getBuffIconDuration));
                                                } else if (this._skillId == 110) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addDex(2);
                                                    pc.sendPackets(new S_Dexup(pc, 2, this._getBuffIconDuration));
                                                } else if (this._skillId == 109) {
                                                    pc = (L1PcInstance)cha;
                                                    pc.addStr(2);
                                                    pc.sendPackets(new S_Strup(pc, 2, this._getBuffIconDuration));
                                                } else if (this._skillId == 107) {
                                                    pc = (L1PcInstance)cha;
                                                    item = pc.getInventory().getItem(this._itemobjid);
                                                    if (item != null && item.getItem().getType2() == 1) {
                                                        item.setSkillWeaponEnchant(pc, this._skillId, this._skill.getBuffDuration() * 1000);
                                                        pc.sendPackets(new S_PacketBox(154, 2951, this._skill.getBuffDuration(), true));
                                                    } else {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    }
                                                } else if (this._skillId == 12) {
                                                    pc = (L1PcInstance)cha;
                                                    item = pc.getInventory().getItem(this._itemobjid);
                                                    if (item != null && item.getItem().getType2() == 1) {
                                                        pc.sendPackets(new S_ServerMessage(161, item.getLogName(), "$245", "$247"));
                                                        item.setSkillWeaponEnchant(pc, this._skillId, this._skill.getBuffDuration() * 1000);
                                                        pc.sendPackets(new S_PacketBox(154, 747, this._skill.getBuffDuration(), true));
                                                    } else {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                    }
                                                } else if (this._skillId != 8 && this._skillId != 48) {
                                                    if (this._skillId == 21) {
                                                        pc = (L1PcInstance)cha;
                                                        item = pc.getInventory().getItem(this._itemobjid);
                                                        if (item != null && item.getItem().getType2() == 2 && item.getItem().getType() == 2) {
                                                            pc.sendPackets(new S_ServerMessage(161, item.getLogName(), "$245", "$247"));
                                                            item.setSkillArmorEnchant(pc, this._skillId, this._skill.getBuffDuration() * 1000);
                                                        } else {
                                                            pc.sendPackets(new S_ServerMessage(79));
                                                        }
                                                    } else if (this._skillId == 159) {
                                                        pc = (L1PcInstance)cha;
                                                        pc.addAc(-7);
                                                        pc.sendPackets(new S_SkillIconShield(7, this._getBuffIconDuration));
                                                    } else if (this._skillId == 129) {
                                                        pc = (L1PcInstance)cha;
                                                        pc.addMr(10);
                                                        pc.sendPackets(new S_SPMR(pc));
                                                    } else if (this._skillId == 137) {
                                                        pc = (L1PcInstance)cha;
                                                        pc.addWis(3);
                                                        pc.resetBaseMr();
                                                    } else if (this._skillId == 138) {
                                                        pc = (L1PcInstance)cha;
                                                        pc.addWind(10);
                                                        pc.addWater(10);
                                                        pc.addFire(10);
                                                        pc.addEarth(10);
                                                        pc.sendPackets(new S_OwnCharAttrDef(pc));
                                                    } else if (this._skillId == 147) {
                                                        pc = (L1PcInstance)cha;
                                                        i = pc.getElfAttr();
                                                        if (i == 1) {
                                                            pc.addEarth(50);
                                                        } else if (i == 2) {
                                                            pc.addFire(50);
                                                        } else if (i == 4) {
                                                            pc.addWater(50);
                                                        } else if (i == 8) {
                                                            pc.addWind(50);
                                                        }
                                                    } else if (this._skillId != 60 && this._skillId != 97) {
                                                        if (this._skillId == 168) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addAc(-10);
                                                            pc.sendPackets(new S_SkillIconShield(10, this._getBuffIconDuration));
                                                        } else if (this._skillId == 151) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addAc(-6);
                                                            pc.sendPackets(new S_SkillIconShield(6, this._getBuffIconDuration));
                                                        } else if (this._skillId == 42) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addStr(5);
                                                            pc.sendPackets(new S_Strup(pc, 5, this._getBuffIconDuration));
                                                        } else if (this._skillId == 26) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addDex(5);
                                                            pc.sendPackets(new S_Dexup(pc, 5, this._getBuffIconDuration));
                                                        } else if (this._skillId == 148) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addDmgup(4);
                                                            pc.sendPackets(new S_PacketBoxIconAura(147, this._getBuffIconDuration));
                                                        } else if (this._skillId == 155) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addDmgup(4);
                                                            pc.sendPackets(new S_PacketBoxIconAura(154, this._getBuffIconDuration));
                                                        } else if (this._skillId == 163) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addDmgup(6);
                                                            pc.addHitup(3);
                                                            pc.sendPackets(new S_PacketBoxIconAura(162, this._getBuffIconDuration));
                                                        } else if (this._skillId == 149) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addBowHitup(6);
                                                            pc.sendPackets(new S_PacketBoxIconAura(148, this._getBuffIconDuration));
                                                        } else if (this._skillId == 156) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addBowHitup(2);
                                                            pc.addBowDmgup(3);
                                                            pc.sendPackets(new S_PacketBoxIconAura(155, this._getBuffIconDuration));
                                                        } else if (this._skillId == 166) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addBowDmgup(5);
                                                            pc.addBowHitup(-1);
                                                            pc.sendPackets(new S_PacketBoxIconAura(165, this._getBuffIconDuration));
                                                        } else if (this._skillId == 55) {
                                                            pc = (L1PcInstance)cha;
                                                            pc.addAc(10);
                                                            pc.addDmgup(5);
                                                            pc.addHitup(2);
                                                        } else if (this._skillId == 54) {
                                                            pc = (L1PcInstance)cha;
                                                            if (pc.getHasteItemEquipped() > 0) {
                                                                continue;
                                                            }

                                                            if (pc.getMoveSpeed() != 2) {
                                                                pc.setDrink(false);
                                                                pc.setMoveSpeed(1);
                                                                pc.sendPackets(new S_SkillHaste(pc.getId(), 1, this._getBuffIconDuration));
                                                                pc.broadcastPacketAll(new S_SkillHaste(pc.getId(), 1, 0));
                                                            } else {
                                                                int skillNum = 0;
                                                                if (pc.hasSkillEffect(29)) {
                                                                    skillNum = 29;
                                                                } else if (pc.hasSkillEffect(76)) {
                                                                    skillNum = 76;
                                                                } else if (pc.hasSkillEffect(152)) {
                                                                    skillNum = 152;
                                                                }

                                                                if (skillNum != 0) {
                                                                    pc.removeSkillEffect(skillNum);
                                                                    pc.removeSkillEffect(54);
                                                                    pc.setMoveSpeed(0);
                                                                    continue;
                                                                }
                                                            }
                                                        } else if (this._skillId != 52 && this._skillId != 101 && this._skillId != 150) {
                                                            if (this._skillId == 204) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addDmgup(4);
                                                                pc.addHitup(4);
                                                            } else if (this._skillId == 209) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addSp(2);
                                                                pc.sendPackets(new S_SPMR(pc));
                                                            } else if (this._skillId == 214) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addAc(-20);
                                                            } else if (this._skillId == 216) {
                                                                pc = (L1PcInstance)cha;
                                                                pc.addStr(1);
                                                                pc.addCon(1);
                                                                pc.addDex(1);
                                                                pc.addWis(1);
                                                                pc.addInt(1);
                                                            } else if (this._skillId == 192) {
                                                                rad = new Random();
                                                                i = rad.nextInt(100) + 1;
                                                                pc = (L1PcInstance)cha;
                                                                if ((double)i <= (double)ConfigOther.THUNDER_GRAB_RND + (double)pc.getInt() * ConfigOther.THUNDER_GRAB_INT - (double)this._target.getMr() * ConfigOther.THUNDER_GRAB_MR && !cha.hasSkillEffect(4017)) {
                                                                    cha.setSkillEffect(4017, 4000);
                                                                    if (cha instanceof L1PcInstance) {
                                                                        pc.sendPackets(new S_Paralysis(6, true));
                                                                    } else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
                                                                        L1NpcInstance tgnpc = (L1NpcInstance)cha;
                                                                        tgnpc.setParalyzed(true);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            pc = (L1PcInstance)cha;
                                                            pc.setBraveSpeed(4);
                                                            pc.sendPackets(new S_SkillBrave(pc.getId(), 4, this._getBuffIconDuration));
                                                            pc.broadcastPacketAll(new S_SkillBrave(pc.getId(), 4, 0));
                                                        }
                                                    } else {
                                                        pc = (L1PcInstance)cha;
                                                        pc.sendPackets(new S_Invis(pc.getId(), 1));
                                                        pc.broadcastPacketAll(new S_RemoveObject(pc));
                                                    }
                                                } else {
                                                    if (!(cha instanceof L1PcInstance)) {
                                                        return;
                                                    }

                                                    pc = (L1PcInstance)cha;
                                                    if (pc.getWeapon() == null) {
                                                        pc.sendPackets(new S_ServerMessage(79));
                                                        return;
                                                    }

                                                    Iterator var32 = pc.getInventory().getItems().iterator();

                                                    while(var32.hasNext()) {
                                                        item = (L1ItemInstance)var32.next();
                                                        if (pc.getWeapon().equals(item)) {
                                                            pc.sendPackets(new S_ServerMessage(161, item.getLogName(), "$245", "$247"));
                                                            item.setSkillWeaponEnchant(pc, this._skillId, this._skill.getBuffDuration() * 1000);
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (this._calcType == 2 || this._calcType == 4) {
                                            Object pet;
                                            L1SummonInstance summon;
                                            Object[] var40;
                                            Object[] petlist;
                                            int petcost;
                                            if (this._skillId == 36 && ((L1MonsterInstance)cha).getNpcTemplate().isTamable()) {
                                                petcost = 0;
                                                petlist = this._user.getPetList().values().toArray();
                                                var40 = petlist;
                                                brave = petlist.length;

                                                for(weaponDamage = 0; weaponDamage < brave; ++weaponDamage) {
                                                    pet = var40[weaponDamage];
                                                    petcost += ((L1NpcInstance)pet).getPetcost();
                                                }

                                                charisma = this._user.getCha();
                                                if (this._player.isElf()) {
                                                    charisma += 12;
                                                } else if (this._player.isWizard()) {
                                                    charisma += 6;
                                                }

                                                charisma -= petcost;
                                                if (charisma >= 6) {
                                                    summon = new L1SummonInstance(this._targetNpc, this._user, false);
                                                    this._target = summon;
                                                } else {
                                                    this._player.sendPackets(new S_ServerMessage(319));
                                                }
                                            } else if (this._skillId == 41) {
                                                petcost = 0;
                                                petlist = this._user.getPetList().values().toArray();
                                                var40 = petlist;
                                                brave = petlist.length;

                                                for(weaponDamage = 0; weaponDamage < brave; ++weaponDamage) {
                                                    pet = var40[weaponDamage];
                                                    petcost += ((L1NpcInstance)pet).getPetcost();
                                                }

                                                charisma = this._user.getCha();
                                                if (this._player.isElf()) {
                                                    charisma += 12;
                                                } else if (this._player.isWizard()) {
                                                    charisma += 6;
                                                }

                                                charisma -= petcost;
                                                if (charisma >= 6) {
                                                    summon = new L1SummonInstance(this._targetNpc, this._user, true);
                                                    this._target = summon;
                                                } else {
                                                    this._player.sendPackets(new S_ServerMessage(319));
                                                }
                                            } else if (this._skillId == 23) {
                                                if (cha instanceof L1MonsterInstance) {
                                                    L1Npc npcTemp = ((L1MonsterInstance)cha).getNpcTemplate();
                                                    i = npcTemp.get_weakAttr();
                                                    if ((i & 1) == 1) {
                                                        cha.broadcastPacketX8(new S_SkillSound(cha.getId(), 2169));
                                                    }

                                                    if ((i & 2) == 2) {
                                                        cha.broadcastPacketX8(new S_SkillSound(cha.getId(), 2167));
                                                    }

                                                    if ((i & 4) == 4) {
                                                        cha.broadcastPacketX8(new S_SkillSound(cha.getId(), 2166));
                                                    }

                                                    if ((i & 8) == 8) {
                                                        cha.broadcastPacketX8(new S_SkillSound(cha.getId(), 2168));
                                                    }
                                                }
                                            } else if (this._skillId == 145) {
                                                if (cha instanceof L1SummonInstance) {
                                                    summon = (L1SummonInstance)cha;
                                                    summon.broadcastPacketX10(new S_SkillSound(summon.getId(), 2245));
                                                    summon.returnToNature();
                                                } else if (this._user instanceof L1PcInstance) {
                                                    this._player.sendPackets(new S_ServerMessage(79));
                                                }
                                            }
                                        }

                                        if (this._skill.getType() == 16 && this._calcType == 2 && undeadType == 1) {
                                            this._dmg *= -1;
                                        }

                                        if (this._skill.getType() == 16 && this._calcType == 2 && undeadType == 3) {
                                            this._dmg = 0;
                                        }

                                        if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && this._dmg < 0) {
                                            this._dmg = 0;
                                        }

                                        if (this._dmg != 0 || drainMana != 0) {
                                            magic.commit(this._dmg, drainMana);
                                        }

                                        if (heal > 0) {
                                            if (heal + this._user.getCurrentHp() > this._user.getMaxHp()) {
                                                this._user.setCurrentHp(this._user.getMaxHp());
                                            } else {
                                                this._user.setCurrentHp(heal + this._user.getCurrentHp());
                                            }
                                        }

                                        if (cha instanceof L1PcInstance) {
                                            pc = (L1PcInstance)cha;
                                            pc.turnOnOffLight();
                                            pc.sendPackets(new S_OwnCharAttrDef(pc));
                                            pc.sendPackets(new S_OwnCharStatus(pc));
                                            this.sendHappenMessage(pc);
                                        }

                                        this.addMagicList(cha, false);
                                        if (cha instanceof L1PcInstance) {
                                            pc = (L1PcInstance)cha;
                                            pc.turnOnOffLight();
                                        }
                                    }
                                } else {
                                    targetStatus.isCalc(false);
                                }
                            }

                            if (this._skillId == 13 || this._skillId == 72) {
                                this.detection(this._player);
                            }
                            break;
                        }
                    } catch (Exception var18) {
                        _log.error(var18.getLocalizedMessage(), var18);
                    }

                }
        }
    }

    private void detection(L1PcInstance pc) {
        if (!pc.isGmInvis() && pc.isInvisble()) {
            pc.delInvis();
            pc.beginInvisTimer();
        }

        Iterator var3 = World.get().getVisiblePlayer(pc).iterator();

        while(var3.hasNext()) {
            L1PcInstance tgt = (L1PcInstance)var3.next();
            if (!tgt.isGmInvis() && tgt.isInvisble()) {
                tgt.delInvis();
            }
        }

        WorldTrap.get().onDetection(pc);
    }

    private boolean isTargetCalc(L1Character cha) {
        if (this._skill.getTarget().equals("attack") && this._skillId != 18 && this.isPcSummonPet(cha) && (this._player.isSafetyZone() || cha.isSafetyZone() || this._player.checkNonPvP(this._player, cha))) {
            return false;
        } else {
            switch(this._skillId) {
                case 66:
                    if (this._user.getId() == cha.getId()) {
                        return false;
                    }
                    break;
                case 69:
                    if (this._user.getId() != cha.getId()) {
                        return false;
                    }
                    break;
                case 76:
                    if (this._user.getId() == cha.getId()) {
                        return false;
                    }

                    if (cha instanceof L1SummonInstance) {
                        L1SummonInstance summon = (L1SummonInstance)cha;
                        if (this._user.getId() == summon.getMaster().getId()) {
                            return false;
                        }
                    } else if (cha instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance)cha;
                        if (this._user.getId() == pet.getMaster().getId()) {
                            return false;
                        }
                    }
            }

            return true;
        }
    }

    private boolean isPcSummonPet(L1Character cha) {
        switch(this._calcType) {
            case 1:
                return true;
            case 2:
                if (cha instanceof L1SummonInstance) {
                    L1SummonInstance summon = (L1SummonInstance)cha;
                    if (summon.isExsistMaster()) {
                        return true;
                    }
                }

                if (cha instanceof L1PetInstance) {
                    return true;
                }

                return false;
            default:
                return false;
        }
    }

    private boolean isTargetFailure(L1Character cha) {
        boolean isTU = false;
        boolean isErase = false;
        boolean isManaDrain = false;
        int undeadType = 0;
        if (!(cha instanceof L1TowerInstance) && !(cha instanceof L1DoorInstance)) {
            if (cha instanceof L1PcInstance) {
                if (this._calcType == 1 && this._player.checkNonPvP(this._player, cha)) {
                    L1PcInstance pc = (L1PcInstance)cha;
                    return this._player.getId() != pc.getId() && (pc.getClanid() == 0 || this._player.getClanid() != pc.getClanid());
                } else {
                    return false;
                }
            } else {
                if (cha instanceof L1MonsterInstance) {
                    isTU = ((L1MonsterInstance)cha).getNpcTemplate().get_IsTU();
                }

                if (cha instanceof L1MonsterInstance) {
                    isErase = ((L1MonsterInstance)cha).getNpcTemplate().get_IsErase();
                }

                if (cha instanceof L1MonsterInstance) {
                    undeadType = ((L1MonsterInstance)cha).getNpcTemplate().get_undead();
                }

                if (cha instanceof L1MonsterInstance) {
                    isManaDrain = true;
                }

                return this._skillId == 18 && (undeadType == 0 || undeadType == 2) || this._skillId == 18 && !isTU || (this._skillId == 153 || this._skillId == 29 || this._skillId == 39 || this._skillId == 76 || this._skillId == 152 || this._skillId == 167) && !isErase || this._skillId == 39 && !isManaDrain;
            }
        } else {
            return true;
        }
    }

    private boolean isUseCounterMagic(L1Character cha) {
        if (this._isCounterMagic && cha.hasSkillEffect(31)) {
            cha.removeSkillEffect(31);
            int castgfx2 = SkillsTable.get().getTemplate(31).getCastGfx2();
            cha.broadcastPacketAll(new S_SkillSound(cha.getId(), castgfx2));
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance)cha;
                pc.sendPackets(new S_SkillSound(pc.getId(), castgfx2));
            }

            return true;
        } else {
            return false;
        }
    }
}
