package com.lineage.server.model;

import com.lineage.server.datatables.lock.SpawnBossReading;
import com.lineage.server.model.skill.L1SkillId;
import java.util.HashMap;

public class L1AttackList {
    protected static final HashMap<Integer, Integer> DEXD = new HashMap<>();
    protected static final HashMap<Integer, Integer> DEXH = new HashMap<>();
    protected static final HashMap<Integer, Integer[]> DNNPC = new HashMap<>();
    public static final HashMap<Integer, Double[]> MRDMG = new HashMap<>();
    protected static final HashMap<Integer, Boolean> NZONE = new HashMap<>();
    protected static final HashMap<Integer, Integer> PLNPC = new HashMap<>();
    protected static final HashMap<Integer, Integer> SKD1 = new HashMap<>();
    protected static final HashMap<Integer, Integer> SKD2 = new HashMap<>();
    public static final HashMap<Integer, Integer> SKD3 = new HashMap<>();
    public static final HashMap<Integer, Integer> SKM0 = new HashMap<>();
    protected static final HashMap<Integer, Integer> SKNPC = new HashMap<>();
    protected static final HashMap<Integer, Integer> SKU1 = new HashMap<>();
    protected static final HashMap<Integer, Integer> SKU2 = new HashMap<>();
    protected static final HashMap<Integer, Integer> STRD = new HashMap<>();
    protected static final HashMap<Integer, Integer> STRH = new HashMap<>();

    public static void load() {
        for (Integer bossid : SpawnBossReading.get().bossIds()) {
            Integer[] ids = {new Integer(66), new Integer(50), new Integer(33), new Integer(157), new Integer(80), new Integer(194)};
            if (DNNPC.get(bossid) == null) {
                DNNPC.put(bossid, ids);
            }
        }
        for (int mr = 0; mr < 255; mr++) {
            double mrFloor = 0.0d;
            double mrCoefficient = 0.0d;
            if (mr == 0) {
                mrFloor = 1.0d;
                mrCoefficient = 1.0d;
            } else if (mr > 0 && mr <= 50) {
                mrFloor = 2.0d;
                mrCoefficient = 1.0d;
            } else if (mr > 50 && mr <= 100) {
                mrFloor = 3.0d;
                mrCoefficient = 0.9d;
            } else if (mr > 100 && mr <= 120) {
                mrFloor = 4.0d;
                mrCoefficient = 0.9d;
            } else if (mr > 120 && mr <= 140) {
                mrFloor = 5.0d;
                mrCoefficient = 0.8d;
            } else if (mr > 140 && mr <= 160) {
                mrFloor = 6.0d;
                mrCoefficient = 0.8d;
            } else if (mr > 160 && mr <= 180) {
                mrFloor = 7.0d;
                mrCoefficient = 0.7d;
            } else if (mr > 180 && mr <= 200) {
                mrFloor = 8.0d;
                mrCoefficient = 0.7d;
            } else if (mr > 200 && mr <= 220) {
                mrFloor = 9.0d;
                mrCoefficient = 0.6d;
            } else if (mr > 220 && mr <= 240) {
                mrFloor = 10.0d;
                mrCoefficient = 0.6d;
            } else if (mr > 240) {
                mrFloor = 11.0d;
                mrCoefficient = 0.5d;
            }
            MRDMG.put(new Integer(mr), new Double[]{Double.valueOf(mrFloor), Double.valueOf(mrCoefficient)});
        }
        NZONE.put(new Integer(27), false);
        NZONE.put(new Integer(29), false);
        NZONE.put(new Integer(33), false);
        NZONE.put(new Integer(39), false);
        NZONE.put(new Integer(40), false);
        NZONE.put(new Integer(47), false);
        NZONE.put(new Integer(56), false);
        NZONE.put(new Integer(44), false);
        NZONE.put(new Integer(71), false);
        NZONE.put(new Integer(76), false);
        NZONE.put(new Integer((int) L1SkillId.ENTANGLE), false);
        NZONE.put(new Integer(153), false);
        NZONE.put(new Integer(157), false);
        NZONE.put(new Integer(161), false);
        NZONE.put(new Integer(167), false);
        NZONE.put(new Integer(174), false);
        NZONE.put(new Integer(87), false);
        NZONE.put(new Integer(66), false);
        NZONE.put(new Integer(50), false);
        NZONE.put(new Integer(80), false);
        NZONE.put(new Integer(194), false);
        NZONE.put(new Integer(173), false);
        NZONE.put(new Integer(133), false);
        NZONE.put(new Integer(145), false);
        NZONE.put(new Integer(64), false);
        NZONE.put(new Integer(193), false);
        NZONE.put(new Integer(188), false);
        NZONE.put(new Integer((int) L1SkillId.GUARD_BRAKE), false);
        SKD1.put(new Integer((int) L1SkillId.COOKING_2_0_N), new Integer(1));
        SKD1.put(new Integer((int) L1SkillId.COOKING_2_0_S), new Integer(1));
        SKD1.put(new Integer((int) L1SkillId.COOKING_2_0_N), new Integer(1));
        SKD1.put(new Integer((int) L1SkillId.COOKING_2_0_N), new Integer(1));
        SKD2.put(new Integer((int) L1SkillId.COOKING_2_3_N), new Integer(1));
        SKD2.put(new Integer((int) L1SkillId.COOKING_2_3_S), new Integer(1));
        SKD2.put(new Integer((int) L1SkillId.COOKING_3_0_N), new Integer(1));
        SKD2.put(new Integer((int) L1SkillId.COOKING_3_0_S), new Integer(1));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_0_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_1_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_2_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_3_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_4_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_5_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_6_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_0_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_1_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_2_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_3_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_4_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_5_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_6_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_0_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_1_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_2_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_3_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_4_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_5_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_3_6_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_1_7_S), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.COOKING_2_7_S), new Integer(-5));
        SKD3.put(new Integer(3047), new Integer(-5));
        SKD3.put(new Integer((int) L1SkillId.DRAGON_SKIN), new Integer(-10));
        SKD3.put(new Integer(211), new Integer(-15));
        SKD3.put(new Integer(68), new Integer(68));
        SKM0.put(new Integer(78), new Integer(0));
        SKM0.put(new Integer(50), new Integer(0));
        SKM0.put(new Integer(80), new Integer(0));
        SKM0.put(new Integer(194), new Integer(0));
        SKM0.put(new Integer(157), new Integer(0));
        SKU1.put(new Integer((int) L1SkillId.COOKING_2_0_N), new Integer(1));
        SKU1.put(new Integer((int) L1SkillId.COOKING_2_0_S), new Integer(1));
        SKU1.put(new Integer((int) L1SkillId.COOKING_3_2_N), new Integer(2));
        SKU1.put(new Integer((int) L1SkillId.COOKING_3_2_S), new Integer(2));
        SKU2.put(new Integer((int) L1SkillId.COOKING_2_3_N), new Integer(1));
        SKU2.put(new Integer((int) L1SkillId.COOKING_2_3_S), new Integer(1));
        SKU2.put(new Integer((int) L1SkillId.COOKING_3_0_N), new Integer(1));
        SKU2.put(new Integer((int) L1SkillId.COOKING_3_0_S), new Integer(1));
        SKNPC.put(new Integer(45912), new Integer((int) L1SkillId.STATUS_HOLY_WATER));
        SKNPC.put(new Integer(45913), new Integer((int) L1SkillId.STATUS_HOLY_WATER));
        SKNPC.put(new Integer(45914), new Integer((int) L1SkillId.STATUS_HOLY_WATER));
        SKNPC.put(new Integer(45915), new Integer((int) L1SkillId.STATUS_HOLY_WATER));
        SKNPC.put(new Integer(45916), new Integer((int) L1SkillId.STATUS_HOLY_MITHRIL_POWDER));
        SKNPC.put(new Integer(45941), new Integer((int) L1SkillId.STATUS_HOLY_WATER_OF_EVA));
        SKNPC.put(new Integer(45752), new Integer((int) L1SkillId.STATUS_CURSE_BARLOG));
        SKNPC.put(new Integer(45753), new Integer((int) L1SkillId.STATUS_CURSE_BARLOG));
        SKNPC.put(new Integer(45675), new Integer((int) L1SkillId.STATUS_CURSE_YAHEE));
        SKNPC.put(new Integer(81082), new Integer((int) L1SkillId.STATUS_CURSE_YAHEE));
        SKNPC.put(new Integer(45625), new Integer((int) L1SkillId.STATUS_CURSE_YAHEE));
        SKNPC.put(new Integer(45674), new Integer((int) L1SkillId.STATUS_CURSE_YAHEE));
        SKNPC.put(new Integer(45685), new Integer((int) L1SkillId.STATUS_CURSE_YAHEE));
        SKNPC.put(new Integer(87000), new Integer((int) L1SkillId.CKEW_LV50));
        SKNPC.put(new Integer(45020), new Integer((int) L1SkillId.I_LV30));
        PLNPC.put(new Integer(46069), new Integer(6035));
        PLNPC.put(new Integer(46070), new Integer(6035));
        PLNPC.put(new Integer(46071), new Integer(6035));
        PLNPC.put(new Integer(46072), new Integer(6035));
        PLNPC.put(new Integer(46073), new Integer(6035));
        PLNPC.put(new Integer(46074), new Integer(6035));
        PLNPC.put(new Integer(46075), new Integer(6035));
        PLNPC.put(new Integer(46076), new Integer(6035));
        PLNPC.put(new Integer(46077), new Integer(6035));
        PLNPC.put(new Integer(46078), new Integer(6035));
        PLNPC.put(new Integer(46079), new Integer(6035));
        PLNPC.put(new Integer(46080), new Integer(6035));
        PLNPC.put(new Integer(46081), new Integer(6035));
        PLNPC.put(new Integer(46082), new Integer(6035));
        PLNPC.put(new Integer(46083), new Integer(6035));
        PLNPC.put(new Integer(46084), new Integer(6035));
        PLNPC.put(new Integer(46085), new Integer(6035));
        PLNPC.put(new Integer(46086), new Integer(6035));
        PLNPC.put(new Integer(46087), new Integer(6035));
        PLNPC.put(new Integer(46088), new Integer(6035));
        PLNPC.put(new Integer(46089), new Integer(6035));
        PLNPC.put(new Integer(46090), new Integer(6035));
        PLNPC.put(new Integer(46091), new Integer(6035));
        PLNPC.put(new Integer(46092), new Integer(6034));
        PLNPC.put(new Integer(46093), new Integer(6034));
        PLNPC.put(new Integer(46094), new Integer(6034));
        PLNPC.put(new Integer(46095), new Integer(6034));
        PLNPC.put(new Integer(46096), new Integer(6034));
        PLNPC.put(new Integer(46097), new Integer(6034));
        PLNPC.put(new Integer(46098), new Integer(6034));
        PLNPC.put(new Integer(46099), new Integer(6034));
        PLNPC.put(new Integer(46100), new Integer(6034));
        PLNPC.put(new Integer(46100), new Integer(6034));
        PLNPC.put(new Integer(46101), new Integer(6034));
        PLNPC.put(new Integer(46102), new Integer(6034));
        PLNPC.put(new Integer(46103), new Integer(6034));
        PLNPC.put(new Integer(46104), new Integer(6034));
        PLNPC.put(new Integer(46105), new Integer(6034));
        PLNPC.put(new Integer(46106), new Integer(6034));
        int strH = 0 + 1;
        STRH.put(new Integer(strH), new Integer(-2));
        int strH2 = strH + 1;
        STRH.put(new Integer(strH2), new Integer(-2));
        int strH3 = strH2 + 1;
        STRH.put(new Integer(strH3), new Integer(-2));
        int strH4 = strH3 + 1;
        STRH.put(new Integer(strH4), new Integer(-2));
        int strH5 = strH4 + 1;
        STRH.put(new Integer(strH5), new Integer(-2));
        int strH6 = strH5 + 1;
        STRH.put(new Integer(strH6), new Integer(-2));
        int strH7 = strH6 + 1;
        STRH.put(new Integer(strH7), new Integer(-2));
        int strH8 = strH7 + 1;
        STRH.put(new Integer(strH8), new Integer(-2));
        int strH9 = strH8 + 1;
        STRH.put(new Integer(strH9), new Integer(-1));
        int strH10 = strH9 + 1;
        STRH.put(new Integer(strH10), new Integer(-1));
        int strH11 = strH10 + 1;
        STRH.put(new Integer(strH11), new Integer(0));
        int strH12 = strH11 + 1;
        STRH.put(new Integer(strH12), new Integer(0));
        int strH13 = strH12 + 1;
        STRH.put(new Integer(strH13), new Integer(1));
        int strH14 = strH13 + 1;
        STRH.put(new Integer(strH14), new Integer(1));
        int strH15 = strH14 + 1;
        STRH.put(new Integer(strH15), new Integer(2));
        int strH16 = strH15 + 1;
        STRH.put(new Integer(strH16), new Integer(2));
        int strH17 = strH16 + 1;
        STRH.put(new Integer(strH17), new Integer(3));
        int strH18 = strH17 + 1;
        STRH.put(new Integer(strH18), new Integer(3));
        int strH19 = strH18 + 1;
        STRH.put(new Integer(strH19), new Integer(4));
        int strH20 = strH19 + 1;
        STRH.put(new Integer(strH20), new Integer(4));
        int strH21 = strH20 + 1;
        STRH.put(new Integer(strH21), new Integer(5));
        int strH22 = strH21 + 1;
        STRH.put(new Integer(strH22), new Integer(5));
        int strH23 = strH22 + 1;
        STRH.put(new Integer(strH23), new Integer(5));
        int strH24 = strH23 + 1;
        STRH.put(new Integer(strH24), new Integer(6));
        int strH25 = strH24 + 1;
        STRH.put(new Integer(strH25), new Integer(6));
        int strH26 = strH25 + 1;
        STRH.put(new Integer(strH26), new Integer(6));
        int strH27 = strH26 + 1;
        STRH.put(new Integer(strH27), new Integer(7));
        int strH28 = strH27 + 1;
        STRH.put(new Integer(strH28), new Integer(7));
        int strH29 = strH28 + 1;
        STRH.put(new Integer(strH29), new Integer(7));
        int strH30 = strH29 + 1;
        STRH.put(new Integer(strH30), new Integer(8));
        int strH31 = strH30 + 1;
        STRH.put(new Integer(strH31), new Integer(8));
        int strH32 = strH31 + 1;
        STRH.put(new Integer(strH32), new Integer(8));
        int strH33 = strH32 + 1;
        STRH.put(new Integer(strH33), new Integer(9));
        int strH34 = strH33 + 1;
        STRH.put(new Integer(strH34), new Integer(9));
        int strH35 = strH34 + 1;
        STRH.put(new Integer(strH35), new Integer(9));
        int strH36 = strH35 + 1;
        STRH.put(new Integer(strH36), new Integer(10));
        int strH37 = strH36 + 1;
        STRH.put(new Integer(strH37), new Integer(10));
        int strH38 = strH37 + 1;
        STRH.put(new Integer(strH38), new Integer(10));
        int strH39 = strH38 + 1;
        STRH.put(new Integer(strH39), new Integer(11));
        int strH40 = strH39 + 1;
        STRH.put(new Integer(strH40), new Integer(11));
        int strH41 = strH40 + 1;
        STRH.put(new Integer(strH41), new Integer(11));
        int strH42 = strH41 + 1;
        STRH.put(new Integer(strH42), new Integer(12));
        int strH43 = strH42 + 1;
        STRH.put(new Integer(strH43), new Integer(12));
        int strH44 = strH43 + 1;
        STRH.put(new Integer(strH44), new Integer(12));
        int strH45 = strH44 + 1;
        STRH.put(new Integer(strH45), new Integer(13));
        int strH46 = strH45 + 1;
        STRH.put(new Integer(strH46), new Integer(13));
        int strH47 = strH46 + 1;
        STRH.put(new Integer(strH47), new Integer(13));
        int strH48 = strH47 + 1;
        STRH.put(new Integer(strH48), new Integer(14));
        int strH49 = strH48 + 1;
        STRH.put(new Integer(strH49), new Integer(14));
        int strH50 = strH49 + 1;
        STRH.put(new Integer(strH50), new Integer(14));
        int strH51 = strH50 + 1;
        STRH.put(new Integer(strH51), new Integer(15));
        int strH52 = strH51 + 1;
        STRH.put(new Integer(strH52), new Integer(15));
        int strH53 = strH52 + 1;
        STRH.put(new Integer(strH53), new Integer(15));
        int strH54 = strH53 + 1;
        STRH.put(new Integer(strH54), new Integer(16));
        int strH55 = strH54 + 1;
        STRH.put(new Integer(strH55), new Integer(16));
        int strH56 = strH55 + 1;
        STRH.put(new Integer(strH56), new Integer(16));
        int strH57 = strH56 + 1;
        STRH.put(new Integer(strH57), new Integer(17));
        int strH58 = strH57 + 1;
        STRH.put(new Integer(strH58), new Integer(17));
        int strH59 = strH58 + 1;
        STRH.put(new Integer(strH59), new Integer(17));
        STRH.put(new Integer(strH59 + 1), new Integer(18));
        int dexH = 0 + 1;
        DEXH.put(new Integer(dexH), new Integer(-2));
        int dexH2 = dexH + 1;
        DEXH.put(new Integer(dexH2), new Integer(-2));
        int dexH3 = dexH2 + 1;
        DEXH.put(new Integer(dexH3), new Integer(-2));
        int dexH4 = dexH3 + 1;
        DEXH.put(new Integer(dexH4), new Integer(-2));
        int dexH5 = dexH4 + 1;
        DEXH.put(new Integer(dexH5), new Integer(-2));
        int dexH6 = dexH5 + 1;
        DEXH.put(new Integer(dexH6), new Integer(-2));
        int dexH7 = dexH6 + 1;
        DEXH.put(new Integer(dexH7), new Integer(-1));
        int dexH8 = dexH7 + 1;
        DEXH.put(new Integer(dexH8), new Integer(-1));
        int dexH9 = dexH8 + 1;
        DEXH.put(new Integer(dexH9), new Integer(0));
        int dexH10 = dexH9 + 1;
        DEXH.put(new Integer(dexH10), new Integer(0));
        int dexH11 = dexH10 + 1;
        DEXH.put(new Integer(dexH11), new Integer(1));
        int dexH12 = dexH11 + 1;
        DEXH.put(new Integer(dexH12), new Integer(1));
        int dexH13 = dexH12 + 1;
        DEXH.put(new Integer(dexH13), new Integer(2));
        int dexH14 = dexH13 + 1;
        DEXH.put(new Integer(dexH14), new Integer(2));
        int dexH15 = dexH14 + 1;
        DEXH.put(new Integer(dexH15), new Integer(3));
        int dexH16 = dexH15 + 1;
        DEXH.put(new Integer(dexH16), new Integer(3));
        int dexH17 = dexH16 + 1;
        DEXH.put(new Integer(dexH17), new Integer(4));
        int dexH18 = dexH17 + 1;
        DEXH.put(new Integer(dexH18), new Integer(4));
        int dexH19 = dexH18 + 1;
        DEXH.put(new Integer(dexH19), new Integer(5));
        int dexH20 = dexH19 + 1;
        DEXH.put(new Integer(dexH20), new Integer(6));
        int dexH21 = dexH20 + 1;
        DEXH.put(new Integer(dexH21), new Integer(7));
        int dexH22 = dexH21 + 1;
        DEXH.put(new Integer(dexH22), new Integer(8));
        int dexH23 = dexH22 + 1;
        DEXH.put(new Integer(dexH23), new Integer(9));
        int dexH24 = dexH23 + 1;
        DEXH.put(new Integer(dexH24), new Integer(10));
        int dexH25 = dexH24 + 1;
        DEXH.put(new Integer(dexH25), new Integer(11));
        int dexH26 = dexH25 + 1;
        DEXH.put(new Integer(dexH26), new Integer(12));
        int dexH27 = dexH26 + 1;
        DEXH.put(new Integer(dexH27), new Integer(13));
        int dexH28 = dexH27 + 1;
        DEXH.put(new Integer(dexH28), new Integer(14));
        int dexH29 = dexH28 + 1;
        DEXH.put(new Integer(dexH29), new Integer(15));
        int dexH30 = dexH29 + 1;
        DEXH.put(new Integer(dexH30), new Integer(16));
        int dexH31 = dexH30 + 1;
        DEXH.put(new Integer(dexH31), new Integer(17));
        int dexH32 = dexH31 + 1;
        DEXH.put(new Integer(dexH32), new Integer(18));
        int dexH33 = dexH32 + 1;
        DEXH.put(new Integer(dexH33), new Integer(19));
        int dexH34 = dexH33 + 1;
        DEXH.put(new Integer(dexH34), new Integer(19));
        int dexH35 = dexH34 + 1;
        DEXH.put(new Integer(dexH35), new Integer(19));
        int dexH36 = dexH35 + 1;
        DEXH.put(new Integer(dexH36), new Integer(20));
        int dexH37 = dexH36 + 1;
        DEXH.put(new Integer(dexH37), new Integer(20));
        int dexH38 = dexH37 + 1;
        DEXH.put(new Integer(dexH38), new Integer(20));
        int dexH39 = dexH38 + 1;
        DEXH.put(new Integer(dexH39), new Integer(21));
        int dexH40 = dexH39 + 1;
        DEXH.put(new Integer(dexH40), new Integer(21));
        int dexH41 = dexH40 + 1;
        DEXH.put(new Integer(dexH41), new Integer(21));
        int dexH42 = dexH41 + 1;
        DEXH.put(new Integer(dexH42), new Integer(22));
        int dexH43 = dexH42 + 1;
        DEXH.put(new Integer(dexH43), new Integer(22));
        int dexH44 = dexH43 + 1;
        DEXH.put(new Integer(dexH44), new Integer(22));
        int dexH45 = dexH44 + 1;
        DEXH.put(new Integer(dexH45), new Integer(23));
        int dexH46 = dexH45 + 1;
        DEXH.put(new Integer(dexH46), new Integer(23));
        int dexH47 = dexH46 + 1;
        DEXH.put(new Integer(dexH47), new Integer(23));
        int dexH48 = dexH47 + 1;
        DEXH.put(new Integer(dexH48), new Integer(24));
        int dexH49 = dexH48 + 1;
        DEXH.put(new Integer(dexH49), new Integer(24));
        int dexH50 = dexH49 + 1;
        DEXH.put(new Integer(dexH50), new Integer(24));
        int dexH51 = dexH50 + 1;
        DEXH.put(new Integer(dexH51), new Integer(25));
        int dexH52 = dexH51 + 1;
        DEXH.put(new Integer(dexH52), new Integer(25));
        int dexH53 = dexH52 + 1;
        DEXH.put(new Integer(dexH53), new Integer(25));
        int dexH54 = dexH53 + 1;
        DEXH.put(new Integer(dexH54), new Integer(26));
        int dexH55 = dexH54 + 1;
        DEXH.put(new Integer(dexH55), new Integer(26));
        int dexH56 = dexH55 + 1;
        DEXH.put(new Integer(dexH56), new Integer(26));
        int dexH57 = dexH56 + 1;
        DEXH.put(new Integer(dexH57), new Integer(27));
        int dexH58 = dexH57 + 1;
        DEXH.put(new Integer(dexH58), new Integer(27));
        int dexH59 = dexH58 + 1;
        DEXH.put(new Integer(dexH59), new Integer(27));
        DEXH.put(new Integer(dexH59 + 1), new Integer(28));
        int dmgStr = -6;
        for (int str = 0; str <= 22; str++) {
            if (str % 2 == 1) {
                dmgStr++;
            }
            STRD.put(new Integer(str), new Integer(dmgStr));
        }
        for (int str2 = 23; str2 <= 28; str2++) {
            if (str2 % 3 == 2) {
                dmgStr++;
            }
            STRD.put(new Integer(str2), new Integer(dmgStr));
        }
        for (int str3 = 29; str3 <= 32; str3++) {
            if (str3 % 2 == 1) {
                dmgStr++;
            }
            STRD.put(new Integer(str3), new Integer(dmgStr));
        }
        for (int str4 = 33; str4 <= 34; str4++) {
            dmgStr++;
            STRD.put(new Integer(str4), new Integer(dmgStr));
        }
        for (int str5 = 35; str5 <= 254; str5++) {
            if (str5 % 4 == 1) {
                dmgStr++;
            }
            STRD.put(new Integer(str5), new Integer(dmgStr));
        }
        for (int dex = 0; dex <= 14; dex++) {
            DEXD.put(new Integer(dex), new Integer(0));
        }
        DEXD.put(new Integer(15), new Integer(1));
        DEXD.put(new Integer(16), new Integer(2));
        DEXD.put(new Integer(17), new Integer(3));
        DEXD.put(new Integer(18), new Integer(4));
        DEXD.put(new Integer(19), new Integer(4));
        DEXD.put(new Integer(20), new Integer(4));
        DEXD.put(new Integer(21), new Integer(5));
        DEXD.put(new Integer(22), new Integer(5));
        DEXD.put(new Integer(23), new Integer(5));
        int dmgDex = 5;
        for (int dex2 = 24; dex2 <= 35; dex2++) {
            if (dex2 % 3 == 1) {
                dmgDex++;
            }
            DEXD.put(new Integer(dex2), new Integer(dmgDex));
        }
        for (int dex3 = 36; dex3 <= 127; dex3++) {
            if (dex3 % 4 == 1) {
                dmgDex++;
            }
            DEXD.put(new Integer(dex3), new Integer(dmgDex));
        }
    }
}
