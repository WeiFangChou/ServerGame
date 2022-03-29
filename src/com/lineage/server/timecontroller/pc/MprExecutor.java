package com.lineage.server.timecontroller.pc;

import com.lineage.config.ConfigOther;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.types.Point;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.MprExecutor */
public class MprExecutor {
    private static MprExecutor _instance;
    private static final Log _log = LogFactory.getLog(MprExecutor.class);
    private static final Map<Integer, Integer> _mapId = new HashMap();
    private static final Map<Integer, Integer> _skill = new HashMap();
    private static final Map<Integer, Integer> _wis = new HashMap();

    protected static MprExecutor get() {
        if (_instance == null) {
            _instance = new MprExecutor();
        }
        return _instance;
    }

    private MprExecutor() {
        _skill.put(32, 5);
        _skill.put(Integer.valueOf((int) L1SkillId.CONCENTRATION), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_1_2_N), 3);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_1_2_S), 3);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_2_4_N), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_2_4_S), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_3_5_N), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.COOKING_3_5_S), 2);
        _skill.put(Integer.valueOf((int) L1SkillId.ADLV80_1), 3);
        _skill.put(4010, 3);
        _mapId.put(16384, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(16384, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(16896, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(17408, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(17920, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(18432, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(18944, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(19968, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(19456, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(20480, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(20992, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(21504, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(22016, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(22528, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(23040, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(23552, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(24064, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(24576, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(25088, Integer.valueOf(ConfigOther.INNMPR));
        _mapId.put(15, Integer.valueOf(ConfigOther.CASTLEMPR));
        _mapId.put(29, Integer.valueOf(ConfigOther.CASTLEMPR));
        _mapId.put(52, Integer.valueOf(ConfigOther.CASTLEMPR));
        _mapId.put(64, Integer.valueOf(ConfigOther.CASTLEMPR));
        _mapId.put(300, Integer.valueOf(ConfigOther.CASTLEMPR));
        _wis.put(0, 1);
        _wis.put(1, 1);
        _wis.put(2, 1);
        _wis.put(3, 1);
        _wis.put(4, 1);
        _wis.put(5, 1);
        _wis.put(6, 1);
        _wis.put(7, 1);
        _wis.put(8, 1);
        _wis.put(9, 1);
        _wis.put(10, 1);
        _wis.put(11, 1);
        _wis.put(12, 1);
        _wis.put(13, 1);
        _wis.put(14, 1);
        _wis.put(15, 2);
        _wis.put(16, 2);
    }

    /* access modifiers changed from: protected */
    public boolean check(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.isDead() || tgpc.isTeleport() || tgpc.getCurrentMp() >= tgpc.getMaxMp()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void checkRegenMp(L1PcInstance tgpc) {
        try {
            tgpc.set_mpRegenType(tgpc.mpRegenType() + tgpc.getMpRegenState());
            tgpc.setRegenState(4);
            if (tgpc.isRegenMp()) {
                regenMp(tgpc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void regenMp(L1PcInstance tgpc) {
        int baseMpr;
        tgpc.set_mpRegenType(0);
        if (tgpc.getMapId() != 201) {
            Integer wis = _wis.get( tgpc.getWis());
            if (wis != null) {
                baseMpr = wis.intValue();
            } else {
                wis = Integer.valueOf(tgpc.getWis());
                baseMpr = 3;
            }
            if (!tgpc.getSkillisEmpty() && tgpc.getSkillEffect().size() > 0) {
                try {
                    Object[] array = tgpc.getSkillEffect().toArray();
                    for (Object key : array) {
                        if (!((Integer) key).equals(Integer.valueOf((int) L1SkillId.STATUS_BLUE_POTION))) {
                            Integer integer = _skill.get(key);
                            if (integer != null) {
                                baseMpr += integer.intValue();
                            }
                        } else if (wis.intValue() < 11) {
                            baseMpr++;
                        } else {
                            baseMpr += tgpc.getWis() - 10;
                        }
                    }
                } catch (ConcurrentModificationException ignored) {
                } catch (Exception e2) {
                    _log.error(e2.getLocalizedMessage(), e2);
                }
            }
            if (L1HouseLocation.isInHouse(tgpc.getX(), tgpc.getY(), tgpc.getMapId())) {
                baseMpr += ConfigOther.HOMEMPR;
            }
            if (L1HouseLocation.isInHouse(tgpc.getMapId())) {
                baseMpr += ConfigOther.HOMEMPR;
            }
            Integer rmp = _mapId.get(new Integer(tgpc.getMapId()));
            if (rmp != null) {
                baseMpr += rmp.intValue();
            }
            if (tgpc.isElf() && tgpc.getMapId() == 4 && tgpc.getLocation().isInScreen(new Point(33055, 32336))) {
                baseMpr += ConfigOther.FORESTMPR;
            }
            if (tgpc.getOriginalMpr() > 0) {
                baseMpr += tgpc.getOriginalMpr();
            }
            tgpc.setCurrentMp(Math.max(tgpc.getCurrentMp() + baseMpr + tgpc.getInventory().mpRegenPerTick() + tgpc.getMpr(), 0));
        }
    }
}
