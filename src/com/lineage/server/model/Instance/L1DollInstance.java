package com.lineage.server.model.Instance;

import com.lineage.config.ConfigOther;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.ActionCodes;
import com.lineage.server.IdFactoryNpc;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.doll.Doll_Skill;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.serverpackets.S_NPCPack_Doll;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1DollInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1DollInstance.class);
    private static Random _random = new Random();
    private static final long serialVersionUID = 1;
    private int _itemObjId;
    private int _olX = 0;
    private int _olY = 0;
    private boolean _power_doll = false;

    /* renamed from: _r */
    private int f13_r = -1;
    private int _skillid = -1;
    private int _srcBraveSpeed;
    private int _srcMoveSpeed;
    private int _time = 0;
    private L1Doll _type;

    public L1DollInstance(L1Npc template, L1PcInstance master, int itemObjId, L1Doll type) {
        super(template);
        try {
            setId(IdFactoryNpc.get().nextId());
            set_showId(master.get_showId());
            setItemObjId(itemObjId);
            this._type = type;
            setGfxId(type.get_gfxid());
            setTempCharGfx(type.get_gfxid());
            setNameId(type.get_nameid());
            set_time(type.get_time());
            setMaster(master);
            setX((master.getX() + _random.nextInt(5)) - 2);
            setY((master.getY() + _random.nextInt(5)) - 2);
            setMap(master.getMapId());
            setHeading(5);
            setLightSize(template.getLightSize());
            World.get().storeObject(this);
            World.get().addVisibleObject(this);
            Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
            while (it.hasNext()) {
                onPerceive(it.next());
            }
            master.addDoll(this);
            if (this._master instanceof L1PcInstance) {
                L1PcInstance masterpc = (L1PcInstance) this._master;
                if (!this._type.getPowerList().isEmpty()) {
                    Iterator<L1DollExecutor> it2 = this._type.getPowerList().iterator();
                    while (it2.hasNext()) {
                        L1DollExecutor p = it2.next();
                        if (p instanceof Doll_Skill) {
                            Doll_Skill vv = (Doll_Skill) p;
                            set_skill(vv.get_int()[0], vv.get_int()[1]);
                        } else {
                            p.setDoll(masterpc);
                        }
                        if (p.is_reset()) {
                            this._power_doll = true;
                        }
                    }
                }
                master.sendPackets(new S_PacketBox(56, type.get_time()));
            }
            broadcastPacketX10(new S_SkillSound(getId(), 5935));
            set_olX(getX());
            set_olY(getY());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void deleteDoll() {
        try {
            broadcastPacketX10(new S_SkillSound(getId(), 5936));
            if (this._master instanceof L1PcInstance) {
                L1PcInstance masterpc = (L1PcInstance) this._master;
                if (!this._type.getPowerList().isEmpty()) {
                    Iterator<L1DollExecutor> it = this._type.getPowerList().iterator();
                    while (it.hasNext()) {
                        it.next().removeDoll(masterpc);
                    }
                }
                masterpc.sendPackets(new S_PacketBox(56, 0));
            }
            this._master.removeDoll(this);
            deleteMe();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object, com.lineage.server.model.Instance.L1NpcInstance
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.get_showId() == get_showId()) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_NPCPack_Doll(this, perceivedFrom));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setNpcMoveSpeed() {
        try {
            if (!ConfigOther.WAR_DOLL) {
                for (int castle_id = 1; castle_id < 8; castle_id++) {
                    if (ServerWarExecutor.get().isNowWar(castle_id) && L1CastleLocation.checkInWarArea(castle_id, this)) {
                        deleteDoll();
                        return;
                    }
                }
            }
            if (this._master != null && this._master.isInvisble()) {
                deleteDoll();
            } else if (this._master.isDead()) {
                deleteDoll();
            } else {
                if (this._master.getMoveSpeed() != this._srcMoveSpeed) {
                    set_srcMoveSpeed(this._master.getMoveSpeed());
                    setMoveSpeed(this._srcMoveSpeed);
                }
                if (this._master.getBraveSpeed() != this._srcBraveSpeed) {
                    set_srcBraveSpeed(this._master.getBraveSpeed());
                    setBraveSpeed(this._srcBraveSpeed);
                }
                if (this._master == null || this._master.getMapId() != getMapId()) {
                    deleteDoll();
                } else if (getLocation().getTileLineDistance(this._master.getLocation()) > 2) {
                    int dir = targetDirection(this._master.getX(), this._master.getY());
                    Iterator<L1Object> it = World.get().getVisibleObjects(this, 1).iterator();
                    while (it.hasNext()) {
                        L1Object object = it.next();
                        if (dir >= 0 && dir <= 7) {
                            int locx = getX() + HEADING_TABLE_X[dir];
                            int locy = getY() + HEADING_TABLE_Y[dir];
                            if ((object instanceof L1DollInstance) && locx == object.getX() && locy == object.getY()) {
                                dir++;
                            }
                        }
                    }
                    if (dir >= 0 && dir <= 7) {
                        setDirectionMoveSrc(dir);
                        broadcastPacketAll(new S_MoveCharPacket(this));
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void set_srcMoveSpeed(int srcMoveSpeed) {
        try {
            this._srcMoveSpeed = srcMoveSpeed;
            broadcastPacketAll(new S_SkillHaste(getId(), this._srcMoveSpeed, 0));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void set_srcBraveSpeed(int srcBraveSpeed) {
        try {
            this._srcBraveSpeed = srcBraveSpeed;
            broadcastPacketAll(new S_SkillBrave(getId(), this._srcBraveSpeed, 0));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getItemObjId() {
        return this._itemObjId;
    }

    public void setItemObjId(int i) {
        this._itemObjId = i;
    }

    public int get_time() {
        return this._time;
    }

    public void set_time(int time) {
        this._time = time;
    }

    public void startDollSkill(L1Character target, double dmg) {
        try {
            if (this._skillid != -1 && _random.nextInt(L1SkillId.STATUS_BRAVE) <= this.f13_r) {
                L1Magic magic = new L1Magic(this._master, target);
                magic.commit(magic.calcMagicDamage(this._skillid), 0);
                L1Skills skill = SkillsTable.get().getTemplate(this._skillid);
                int castgfx = skill.getCastGfx();
                target.broadcastPacketX10(new S_SkillSound(target.getId(), castgfx));
                if (target instanceof L1PcInstance) {
                    L1PcInstance pc = (L1PcInstance) target;
                    pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
                }
                switch (this._skillid) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 8:
                    case 9:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 19:
                    case 20:
                    case 21:
                    case 23:
                    case 24:
                    case 26:
                    case 27:
                    case 31:
                    case 32:
                    case 33:
                    case 35:
                    case 36:
                    case 37:
                    case 39:
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 47:
                    case 48:
                    case 49:
                    case 51:
                    case 52:
                    case 53:
                    case 54:
                    case 55:
                    case 56:
                    case 57:
                    case 59:
                    case 60:
                    case 61:
                    case 62:
                    case 63:
                    case 64:
                    case 66:
                    case 67:
                    case 68:
                    case 69:
                    case 70:
                    case 71:
                    case 72:
                    case 73:
                    case 75:
                    case 76:
                    case L1SkillId.ABSOLUTE_BARRIER /*{ENCODED_INT: 78}*/:
                    case 79:
                    case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case ActionCodes.ACTION_ChainswordDamage:
                    case 86:
                    case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                    case 88:
                    case L1SkillId.BOUNCE_ATTACK /*{ENCODED_INT: 89}*/:
                    case 90:
                    case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                    case 92:
                    case OpcodesServer.S_OPCODE_POISON /*{ENCODED_INT: 93}*/:
                    case 94:
                    case 95:
                    case OpcodesClient.C_OPCODE_TITLE:
                    case 97:
                    case L1SkillId.ENCHANT_VENOM /*{ENCODED_INT: 98}*/:
                    case 99:
                    case 100:
                    case 101:
                    case L1SkillId.BURNING_SPIRIT /*{ENCODED_INT: 102}*/:
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 109:
                    case 110:
                    case 111:
                    case 112:
                    case 113:
                    case L1SkillId.GLOWING_AURA /*{ENCODED_INT: 114}*/:
                    case 115:
                    case 116:
                    case 117:
                    case L1SkillId.RUN_CLAN /*{ENCODED_INT: 118}*/:
                    case OpcodesServer.S_OPCODE_SHOWHTML /*{ENCODED_INT: 119}*/:
                    case OpcodesServer.S_OPCODE_STRUP /*{ENCODED_INT: 120}*/:
                    case 121:
                    case 122:
                    case OpcodesServer.S_OPCODE_WAR /*{ENCODED_INT: 123}*/:
                    case 124:
                    case 125:
                    case 126:
                    case 127:
                    case 128:
                    case 129:
                    case L1SkillId.BODY_TO_MIND /*{ENCODED_INT: 130}*/:
                    case 131:
                    case 133:
                    case 134:
                    case OpcodesServer.S_OPCODE_CHARLOCK /*{ENCODED_INT: 135}*/:
                    case 136:
                    case 137:
                    case 138:
                    case 139:
                    case OpcodesServer.S_OPCODE_LAWFUL /*{ENCODED_INT: 140}*/:
                    case 141:
                    case OpcodesServer.S_OPCODE_ATTACKPACKET /*{ENCODED_INT: 142}*/:
                    case 143:
                    case 144:
                    case 145:
                    case L1SkillId.BLOODY_SOUL /*{ENCODED_INT: 146}*/:
                    case L1SkillId.ELEMENTAL_PROTECTION /*{ENCODED_INT: 147}*/:
                    case 148:
                    case 149:
                    case 150:
                    case 151:
                    case L1SkillId.ENTANGLE /*{ENCODED_INT: 152}*/:
                    case 153:
                    case 154:
                    case 155:
                    case L1SkillId.STORM_EYE /*{ENCODED_INT: 156}*/:
                    case 157:
                    case L1SkillId.NATURES_TOUCH /*{ENCODED_INT: 158}*/:
                    case 159:
                    case L1SkillId.AQUA_PROTECTER /*{ENCODED_INT: 160}*/:
                    case 161:
                    case 162:
                    case L1SkillId.BURNING_WEAPON /*{ENCODED_INT: 163}*/:
                    case 164:
                    case 165:
                    case 166:
                    case 167:
                    case L1SkillId.IRON_SKIN /*{ENCODED_INT: 168}*/:
                    case L1SkillId.EXOTIC_VITALIZE /*{ENCODED_INT: 169}*/:
                    case 170:
                    case L1SkillId.ELEMENTAL_FIRE /*{ENCODED_INT: 171}*/:
                    case L1SkillId.STORM_WALK /*{ENCODED_INT: 172}*/:
                    case 173:
                    case 174:
                    case L1SkillId.SOUL_OF_FLAME /*{ENCODED_INT: 175}*/:
                    case L1SkillId.ADDITIONAL_FIRE /*{ENCODED_INT: 176}*/:
                    case OpcodesServer.S_OPCODE_SELECTTARGET /*{ENCODED_INT: 177}*/:
                    case 178:
                    case 179:
                    case OpcodesServer.S_OPCODE_INVLIST /*{ENCODED_INT: 180}*/:
                    case L1SkillId.DRAGON_SKIN /*{ENCODED_INT: 181}*/:
                    case 182:
                    case L1SkillId.GUARD_BRAKE /*{ENCODED_INT: 183}*/:
                    case 185:
                    case L1SkillId.BLOODLUST /*{ENCODED_INT: 186}*/:
                    case 188:
                    case L1SkillId.SHOCK_SKIN /*{ENCODED_INT: 189}*/:
                    case 190:
                    case L1SkillId.MORTAL_BODY /*{ENCODED_INT: 191}*/:
                    case 193:
                    case 195:
                    case 196:
                    case 197:
                    case 198:
                    case 199:
                    case 200:
                    case 201:
                    case 204:
                    case L1SkillId.CUBE_IGNITION /*{ENCODED_INT: 205}*/:
                    case L1SkillId.CONCENTRATION /*{ENCODED_INT: 206}*/:
                    case 209:
                    case 210:
                    case 211:
                    case 212:
                    case L1SkillId.ILLUSION_DIA_GOLEM /*{ENCODED_INT: 214}*/:
                    case L1SkillId.CUBE_SHOCK /*{ENCODED_INT: 215}*/:
                    case 216:
                    case 217:
                    case 218:
                    case L1SkillId.ILLUSION_AVATAR /*{ENCODED_INT: 219}*/:
                    default:
                        return;
                    case 4:
                    case 6:
                    case 7:
                    case 10:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 22:
                    case 25:
                    case 28:
                    case 30:
                    case 34:
                    case 38:
                    case 45:
                    case 46:
                    case 50:
                    case 58:
                    case 65:
                    case 74:
                    case 77:
                    case L1SkillId.FINAL_BURN /*{ENCODED_INT: 108}*/:
                    case L1SkillId.TRIPLE_ARROW /*{ENCODED_INT: 132}*/:
                    case 184:
                    case L1SkillId.FOE_SLAYER /*{ENCODED_INT: 187}*/:
                    case 192:
                    case 194:
                    case 202:
                    case 203:
                    case 207:
                    case 208:
                    case 213:
                        broadcastPacketAll(new S_DoActionGFX(getId(), 67));
                        return;
                    case 29:
                        switch (target.getMoveSpeed()) {
                            case 0:
                                if (target instanceof L1PcInstance) {
                                    L1PcInstance pc2 = (L1PcInstance) target;
                                    pc2.sendPackets(new S_SkillHaste(pc2.getId(), 2, skill.getBuffDuration()));
                                }
                                target.broadcastPacketAll(new S_SkillHaste(target.getId(), 2, skill.getBuffDuration()));
                                target.setMoveSpeed(2);
                                return;
                            case 1:
                                int skillNum = 0;
                                if (target.hasSkillEffect(43)) {
                                    skillNum = 43;
                                } else if (target.hasSkillEffect(54)) {
                                    skillNum = 54;
                                } else if (target.hasSkillEffect(L1SkillId.STATUS_HASTE)) {
                                    skillNum = L1SkillId.STATUS_HASTE;
                                }
                                if (skillNum != 0) {
                                    target.removeSkillEffect(skillNum);
                                    target.setMoveSpeed(0);
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void set_skill(int int1, int int2) {
        this._skillid = int1;
        if (this._skillid != -1) {
            setInt(this._master.getInt());
        }
        this.f13_r = int2;
    }

    public void set_olX(int x) {
        this._olX = x;
    }

    public int get_olX() {
        return this._olX;
    }

    public void set_olY(int y) {
        this._olY = y;
    }

    public int get_olY() {
        return this._olY;
    }

    public void startDollSkill() {
        if (!this._type.getPowerList().isEmpty() && (this._master instanceof L1PcInstance)) {
            L1PcInstance masterpc = (L1PcInstance) this._master;
            Iterator<L1DollExecutor> it = this._type.getPowerList().iterator();
            while (it.hasNext()) {
                L1DollExecutor p = it.next();
                if (p.is_reset()) {
                    p.setDoll(masterpc);
                }
            }
        }
    }

    public void show_action(int i) {
        if (!this._type.getPowerList().isEmpty()) {
            if (i == 1 && (this._master instanceof L1PcInstance)) {
                ((L1PcInstance) this._master).sendPacketsAll(new S_SkillSound(this._master.getId(), 6319));
            }
            if (i == 2 && (this._master instanceof L1PcInstance)) {
                ((L1PcInstance) this._master).sendPacketsAll(new S_SkillSound(this._master.getId(), 6320));
            }
            if (i == 3 && (this._master instanceof L1PcInstance)) {
                ((L1PcInstance) this._master).sendPacketsAll(new S_SkillSound(this._master.getId(), 6321));
            }
        }
    }

    public boolean is_power_doll() {
        return this._power_doll;
    }
}
