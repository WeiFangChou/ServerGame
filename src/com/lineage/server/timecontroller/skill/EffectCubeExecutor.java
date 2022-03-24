package com.lineage.server.timecontroller.skill;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1EffectType;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Cube;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EffectCubeExecutor {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType;
    private static EffectCubeExecutor _instance;
    private static final Log _log = LogFactory.getLog(EffectCubeExecutor.class);

    static /* synthetic */ int[] $SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType() {
        int[] iArr = $SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType;
        if (iArr == null) {
            iArr = new int[L1EffectType.values().length];
            try {
                iArr[L1EffectType.isCubeBurn.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[L1EffectType.isCubeEruption.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[L1EffectType.isCubeHarmonize.ordinal()] = 5;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[L1EffectType.isCubeShock.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[L1EffectType.isFirewall.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[L1EffectType.isOther.ordinal()] = 6;
            } catch (NoSuchFieldError ignored) {
            }
            $SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType = iArr;
        }
        return iArr;
    }

    protected static EffectCubeExecutor get() {
        if (_instance == null) {
            _instance = new EffectCubeExecutor();
        }
        return _instance;
    }

    /* access modifiers changed from: protected */
    public void cubeBurn(L1EffectInstance effect) {
        try {
            Iterator<L1Object> it = World.get().getVisibleObjects(effect, 3).iterator();
            while (it.hasNext()) {
                L1Object objects = it.next();
                if (objects != null) {
                    if (objects instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance) objects;
                        if (!pc.isDead() && effect.get_showId() == pc.get_showId() && effect.getMaster() != null) {
                            L1PcInstance user = (L1PcInstance) effect.getMaster();
                            if (pc.getId() == user.getId()) {
                                cubeToAlly(pc, effect);
                            } else if (pc.getClanid() != 0 && user.getClanid() == pc.getClanid()) {
                                cubeToAlly(pc, effect);
                            } else if (pc.isInParty() && pc.getParty().isMember(user)) {
                                cubeToAlly(pc, effect);
                            } else if (pc.isSafetyZone()) {
                                boolean isNowWar = false;
                                int castleId = L1CastleLocation.getCastleIdByArea(pc);
                                if (castleId > 0) {
                                    isNowWar = ServerWarExecutor.get().isNowWar(castleId);
                                }
                                if (isNowWar) {
                                    cubeToEnemy(pc, effect);
                                }
                            } else {
                                cubeToEnemy(pc, effect);
                            }
                        }
                    } else if (objects instanceof L1MonsterInstance) {
                        L1MonsterInstance mob = (L1MonsterInstance) objects;
                        if (!mob.isDead()) {
                            cubeToEnemy(mob, effect);
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error("Npc L1Effect幻術師技能(立方)狀態送出時間軸發生異常", e);
            effect.deleteMe();
        }
    }

    private static void cubeToAlly(L1Character cha, L1EffectInstance effect) {
        int castGfx = SkillsTable.get().getTemplate(effect.getSkillId()).getCastGfx();
        int skillid = cubeToAllyId(effect.effectType());
        if (!cha.hasSkillEffect(skillid)) {
            switch ($SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType()[effect.effectType().ordinal()]) {
                case 2:
                    cha.addFire(30);
                    break;
                case 3:
                    cha.addEarth(30);
                    break;
                case 4:
                    cha.addWind(30);
                    break;
            }
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_OwnCharAttrDef(pc));
                pc.sendPackets(new S_SkillSound(pc.getId(), castGfx));
            }
            cha.broadcastPacketX10(new S_SkillSound(cha.getId(), castGfx));
            cha.setSkillEffect(skillid, L1EffectInstance.CUBE_TIME);
            if (effect.effectType() == L1EffectType.isCubeHarmonize) {
                new L1Cube(effect, cha, L1SkillId.STATUS_CUBE_BALANCE).begin();
            }
        }
    }

    private static void cubeToEnemy(L1Character cha, L1EffectInstance effect) {
        int castGfx2 = SkillsTable.get().getTemplate(effect.getSkillId()).getCastGfx2();
        int skillid = cubeToEnemyId(effect.effectType());
        if (!cha.hasSkillEffect(skillid)) {
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc = (L1PcInstance) cha;
                pc.sendPackets(new S_SkillSound(pc.getId(), castGfx2));
            }
            cha.broadcastPacketX10(new S_SkillSound(cha.getId(), castGfx2));
            cha.setSkillEffect(skillid, L1EffectInstance.CUBE_TIME);
            new L1Cube(effect, cha, skillid).begin();
        }
    }

    private static int cubeToAllyId(L1EffectType effectType) {
        switch ($SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType()[effectType.ordinal()]) {
            case 2:
                return L1SkillId.STATUS_CUBE_IGNITION_TO_ALLY;
            case 3:
                return L1SkillId.STATUS_CUBE_QUAKE_TO_ALLY;
            case 4:
                return L1SkillId.STATUS_CUBE_SHOCK_TO_ALLY;
            case 5:
                return L1SkillId.STATUS_CUBE_BALANCE;
            default:
                return 0;
        }
    }

    private static int cubeToEnemyId(L1EffectType effectType) {
        switch ($SWITCH_TABLE$com$lineage$server$model$Instance$L1EffectType()[effectType.ordinal()]) {
            case 2:
                return L1SkillId.STATUS_CUBE_IGNITION_TO_ENEMY;
            case 3:
                return L1SkillId.STATUS_CUBE_QUAKE_TO_ENEMY;
            case 4:
                return L1SkillId.STATUS_CUBE_SHOCK_TO_ENEMY;
            case 5:
                return L1SkillId.STATUS_CUBE_BALANCE;
            default:
                return 0;
        }
    }
}
