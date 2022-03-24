package com.lineage.server.templates;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.poison.L1DamagePoison;
import com.lineage.server.model.poison.L1ParalysisPoison;
import com.lineage.server.model.poison.L1SilencePoison;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.utils.Dice;
import com.lineage.server.utils.L1SpawnUtil;
import com.lineage.server.world.World;
import java.sql.ResultSet;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Trap {
    private static final Log _log = LogFactory.getLog(L1Trap.class);
    private int _base;
    private int _count;
    private int _damage;
    private int _delay;
    private Dice _dice;
    private int _diceCount;
    private int _gfxId;
    private int _id;
    private boolean _isDetectionable;
    private L1Location _loc;
    private int _npcId;
    private int _poisonType;
    private int _skillId;
    private int _skillTimeSeconds;
    private int _time;
    private int _trap;
    private String _type;

    public L1Trap(ResultSet rs) {
        try {
            this._id = rs.getInt("id");
            this._gfxId = rs.getInt("gfxId");
            this._isDetectionable = rs.getBoolean("isDetectionable");
            this._type = rs.getString("type");
            if (this._type.equalsIgnoreCase("L1DamageTrap")) {
                this._trap = 1;
                this._dice = new Dice(rs.getInt("dice"));
                this._base = rs.getInt("base");
                this._diceCount = rs.getInt("diceCount");
            } else if (this._type.equalsIgnoreCase("L1HealingTrap")) {
                this._trap = 2;
                this._dice = new Dice(rs.getInt("dice"));
                this._base = rs.getInt("base");
                this._diceCount = rs.getInt("diceCount");
            } else if (this._type.equalsIgnoreCase("L1MonsterTrap")) {
                this._trap = 3;
                this._npcId = rs.getInt("monsterNpcId");
                this._count = rs.getInt("monsterCount");
            } else if (this._type.equalsIgnoreCase("L1PoisonTrap")) {
                this._trap = 4;
                String poisonType = rs.getString("poisonType");
                if (poisonType.equalsIgnoreCase("d")) {
                    this._poisonType = 1;
                } else if (poisonType.equalsIgnoreCase("s")) {
                    this._poisonType = 2;
                } else if (poisonType.equalsIgnoreCase("p")) {
                    this._poisonType = 3;
                }
                this._delay = rs.getInt("poisonDelay");
                this._time = rs.getInt("poisonTime");
                this._damage = rs.getInt("poisonDamage");
            } else if (this._type.equalsIgnoreCase("L1SkillTrap")) {
                this._trap = 5;
                this._skillId = rs.getInt("skillId");
                this._skillTimeSeconds = rs.getInt("skillTimeSeconds");
            } else if (this._type.equalsIgnoreCase("L1TeleportTrap")) {
                this._trap = 6;
                this._loc = new L1Location(rs.getInt("teleportX"), rs.getInt("teleportY"), rs.getInt("teleportMapId"));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public String getType() {
        return String.valueOf(this._type) + "(" + this._trap + "-" + this._id + ")";
    }

    public int getId() {
        return this._id;
    }

    public int getGfxId() {
        return this._gfxId;
    }

    private void sendEffect(L1Object trapObj) {
        try {
            if (getGfxId() != 0) {
                S_EffectLocation effect = new S_EffectLocation(trapObj.getLocation(), getGfxId());
                Iterator<L1PcInstance> it = World.get().getRecognizePlayer(trapObj).iterator();
                while (it.hasNext()) {
                    it.next().sendPackets(effect);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void onTrod(L1PcInstance trodFrom, L1Object trapObj) {
        switch (this._trap) {
            case 0:
                _log.error("陷阱的處理未定義: " + this._id);
                return;
            case 1:
                onType1(trodFrom, trapObj);
                return;
            case 2:
                onType2(trodFrom, trapObj);
                return;
            case 3:
                onType3(trodFrom, trapObj);
                return;
            case 4:
                onType4(trodFrom, trapObj);
                return;
            case 5:
                onType5(trodFrom, trapObj);
                return;
            case 6:
                onType6(trodFrom, trapObj);
                return;
            default:
                return;
        }
    }

    public void onDetection(L1PcInstance caster, L1Object trapObj) {
        if (this._isDetectionable) {
            sendEffect(trapObj);
        }
    }

    private void onType1(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 1 && this._base > 0) {
                sendEffect(trapObj);
                trodFrom.receiveDamage(trodFrom, (double) (this._dice.roll(this._diceCount) + this._base), false, true);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void onType2(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 2 && this._base > 0) {
                sendEffect(trapObj);
                trodFrom.healHp(this._dice.roll(this._diceCount) + this._base);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void onType3(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 3 && this._npcId > 0) {
                sendEffect(trapObj);
                for (int i = 0; i < this._count; i++) {
                    L1SpawnUtil.spawn(this._npcId, trodFrom.getLocation().randomLocation(5, false)).setLink(trodFrom);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void onType4(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 4) {
                sendEffect(trapObj);
                switch (this._poisonType) {
                    case 1:
                        L1DamagePoison.doInfection(trodFrom, trodFrom, this._time, this._damage);
                        return;
                    case 2:
                        L1SilencePoison.doInfection(trodFrom);
                        return;
                    case 3:
                        L1ParalysisPoison.doInfection(trodFrom, this._delay, this._time);
                        return;
                    default:
                        return;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void onType5(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 5 && this._skillId > 0) {
                sendEffect(trapObj);
                new L1SkillUse().handleCommands(trodFrom, this._skillId, trodFrom.getId(), trodFrom.getX(), trodFrom.getY(), this._skillTimeSeconds, 4);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void onType6(L1PcInstance trodFrom, L1Object trapObj) {
        try {
            if (this._trap == 6 && this._loc != null) {
                sendEffect(trapObj);
                L1Teleport.teleport(trodFrom, this._loc.getX(), this._loc.getY(), (short) this._loc.getMapId(), 5, true, 2);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
