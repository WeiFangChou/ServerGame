package com.lineage.server.timecontroller.pc;

import com.lineage.config.ConfigOther;
import com.lineage.data.quest.DarkElfLv50_1;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.types.Point;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.HprExecutor */
public class HprExecutor {
    private static HprExecutor _instance;
    private static final Log _log = LogFactory.getLog(HprExecutor.class);
    private static final Map<Integer, Integer> _mapIdD = new HashMap();
    private static final Map<Integer, Integer> _mapIdU = new HashMap();
    private static final Map<Integer, Integer> _skill = new HashMap();

    protected static HprExecutor get() {
        if (_instance == null) {
            _instance = new HprExecutor();
        }
        return _instance;
    }

    private HprExecutor() {
        _skill.put(Integer.valueOf((int) L1SkillId.NATURES_TOUCH), 15);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_1_5_N), 3);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_1_5_S), 3);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_2_4_N), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_2_4_S), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_3_6_N), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_3_6_S), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.ADLV80_1), 3);
        _skill.put(4010, 3);
        _mapIdU.put(16384, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(16896, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(17408, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(17920, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(18432, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(18944, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(19968, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(19456, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(20480, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(20992, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(21504, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(22016, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(22528, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(23040, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(23552, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(24064, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(24576, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(25088, Integer.valueOf(ConfigOther.INNHPR));
        _mapIdU.put(15, Integer.valueOf(ConfigOther.CASTLEHPR));
        _mapIdU.put(29, Integer.valueOf(ConfigOther.CASTLEHPR));
        _mapIdU.put(52, Integer.valueOf(ConfigOther.CASTLEHPR));
        _mapIdU.put(64, Integer.valueOf(ConfigOther.CASTLEHPR));
        _mapIdU.put(300, Integer.valueOf(ConfigOther.CASTLEHPR));
        _mapIdD.put(410, -10);
        _mapIdD.put(2000, -10);
        _mapIdD.put(Integer.valueOf((int) DarkElfLv50_1.MAPID), -10);
    }

    /* access modifiers changed from: protected */
    public boolean check(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isDead() || tgpc.isTeleport()) {
                return false;
            }
            if (_mapIdD.get(new Integer(tgpc.getMapId())) != null) {
                return true;
            }
            if (isUnderwater(tgpc)) {
                return true;
            }
            return tgpc.getCurrentHp() < tgpc.getMaxHp();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void checkRegenHp(L1PcInstance tgpc) {
        try {
            tgpc.set_hpRegenType(tgpc.hpRegenType() + tgpc.getHpRegenState());
            tgpc.setRegenState(4);
            if (tgpc.isRegenHp()) {
                regenHp(tgpc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void regenHp(L1PcInstance tgpc) {
        tgpc.set_hpRegenType(0);
        int maxBonus = 1;
        if (tgpc.getLevel() > 11 && tgpc.getCon() >= 14) {
            maxBonus = Math.min(tgpc.getCon() - 12, 14);
        }
        int equipHpr = tgpc.getInventory().hpRegenPerTick() + tgpc.getHpr();
        int bonus = new Random().nextInt(maxBonus) + 1;
        if (!tgpc.getSkillisEmpty() && tgpc.getSkillEffect().size() > 0) {
            try {
                for (Object key : tgpc.getSkillEffect().toArray()) {
                    Integer integer = _skill.get(key);
                    if (integer != null) {
                        bonus += integer.intValue();
                    }
                }
            } catch (ConcurrentModificationException ignored) {
            } catch (Exception e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            }
        }
        if (L1HouseLocation.isInHouse(tgpc.getX(), tgpc.getY(), tgpc.getMapId())) {
            bonus += ConfigOther.HOMEHPR;
        }
        if (L1HouseLocation.isInHouse(tgpc.getMapId())) {
            bonus += ConfigOther.HOMEHPR;
        }
        Integer rhp = _mapIdU.get(new Integer(tgpc.getMapId()));
        if (rhp != null) {
            bonus += rhp.intValue();
        }
        if (tgpc.isElf() && tgpc.getMapId() == 4 && tgpc.getLocation().isInScreen(new Point(33055, 32336))) {
            bonus += ConfigOther.FORESTHPR;
        }
        if (tgpc.getOriginalHpr() > 0) {
            bonus += tgpc.getOriginalHpr();
        }
        boolean inLifeStream = false;
        if (isPlayerInLifeStream(tgpc)) {
            inLifeStream = true;
            bonus += 3;
        }
        int newHp = Math.max(tgpc.getCurrentHp() + bonus + equipHpr, 1);
        if (isUnderwater(tgpc)) {
            newHp -= 20;
        }
        Integer dhp = _mapIdD.get(new Integer(tgpc.getMapId()));
        if (dhp != null && !inLifeStream) {
            int bonus2 = bonus + dhp.intValue();
        }
        tgpc.setCurrentHp(Math.max(newHp, 0));
    }

    private static boolean isUnderwater(L1PcInstance pc) {
        if (pc.getInventory().checkEquipped(20207) || pc.hasSkillEffect(L1SkillId.STATUS_UNDERWATER_BREATH)) {
            return false;
        }
        if (!pc.getInventory().checkEquipped(21048) || !pc.getInventory().checkEquipped(21049) || !pc.getInventory().checkEquipped(21050)) {
            return pc.getMap().isUnderwater();
        }
        return false;
    }

    private static boolean isPlayerInLifeStream(L1PcInstance pc) {
        for (L1Object object : pc.getKnownObjects()) {
            if (object instanceof L1EffectInstance) {
                L1EffectInstance effect = (L1EffectInstance) object;
                if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(pc.getLocation()) < 4) {
                    return true;
                }
            }
        }
        return false;
    }
}
