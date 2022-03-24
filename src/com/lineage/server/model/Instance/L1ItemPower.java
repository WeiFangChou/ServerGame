package com.lineage.server.model.Instance;

import java.util.HashMap;
import java.util.Map;

public class L1ItemPower {
    public static final Map<Integer, Integer> MR2 = new HashMap();
    private final L1ItemInstance _itemInstance;

    public static void load() {
        MR2.put(20011, new Integer(1));
        MR2.put(120011, new Integer(1));
        MR2.put(20110, new Integer(1));
        MR2.put(21108, new Integer(1));
        MR2.put(20056, new Integer(2));
        MR2.put(120056, new Integer(2));
        MR2.put(70092, new Integer(3));
        MR2.put(70034, new Integer(1));
        MR2.put(30328, new Integer(1));
        MR2.put(30329, new Integer(1));
        MR2.put(30330, new Integer(1));
        MR2.put(30331, new Integer(1));
    }

    protected L1ItemPower(L1ItemInstance itemInstance) {
        this._itemInstance = itemInstance;
    }

    /* access modifiers changed from: protected */
    public int getMr() {
        int mr = this._itemInstance.getItem().get_mdef();
        Integer integer = MR2.get(Integer.valueOf(this._itemInstance.getItemId()));
        if (integer != null) {
            return mr + (this._itemInstance.getEnchantLevel() * integer.intValue());
        }
        return mr;
    }

    /* access modifiers changed from: protected */
    public void greater(L1PcInstance owner, boolean equipment) {
        int level = this._itemInstance.getEnchantLevel();
        if (level > 0) {
            if (equipment) {
                switch (this._itemInstance.getItem().get_greater()) {
                    case 0:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addEarth(1);
                                owner.addWind(1);
                                owner.addWater(1);
                                owner.addFire(1);
                                return;
                            case 2:
                                owner.addEarth(2);
                                owner.addWind(2);
                                owner.addWater(2);
                                owner.addFire(2);
                                return;
                            case 3:
                                owner.addEarth(3);
                                owner.addWind(3);
                                owner.addWater(3);
                                owner.addFire(3);
                                return;
                            case 4:
                                owner.addEarth(4);
                                owner.addWind(4);
                                owner.addWater(4);
                                owner.addFire(4);
                                return;
                            case 5:
                                owner.addEarth(5);
                                owner.addWind(5);
                                owner.addWater(5);
                                owner.addFire(5);
                                return;
                            case 6:
                                owner.addEarth(6);
                                owner.addWind(6);
                                owner.addWater(6);
                                owner.addFire(6);
                                owner.addHpr(1);
                                owner.addMpr(1);
                                return;
                            case 7:
                                owner.addEarth(10);
                                owner.addWind(10);
                                owner.addWater(10);
                                owner.addFire(10);
                                owner.addHpr(3);
                                owner.addMpr(3);
                                return;
                            default:
                                owner.addEarth(15);
                                owner.addWind(15);
                                owner.addWater(15);
                                owner.addFire(15);
                                owner.addHpr(3);
                                owner.addMpr(3);
                                return;
                        }
                    case 1:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addMaxHp(5);
                                return;
                            case 2:
                                owner.addMaxHp(10);
                                return;
                            case 3:
                                owner.addMaxHp(15);
                                return;
                            case 4:
                                owner.addMaxHp(20);
                                return;
                            case 5:
                                owner.addMaxHp(25);
                                return;
                            case 6:
                                owner.addMaxHp(30);
                                owner.addMr(2);
                                return;
                            case 7:
                                owner.addMaxHp(40);
                                owner.addMr(7);
                                return;
                            default:
                                owner.addMaxHp(level + 40);
                                owner.addMr(level + 12);
                                return;
                        }
                    case 2:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addMaxMp(3);
                                return;
                            case 2:
                                owner.addMaxMp(6);
                                return;
                            case 3:
                                owner.addMaxMp(9);
                                return;
                            case 4:
                                owner.addMaxMp(12);
                                return;
                            case 5:
                                owner.addMaxMp(15);
                                return;
                            case 6:
                                owner.addMaxMp(25);
                                owner.addSp(1);
                                return;
                            case 7:
                                owner.addMaxMp(40);
                                owner.addSp(2);
                                return;
                            default:
                                owner.addMaxMp(level + 40);
                                owner.addSp(3);
                                return;
                        }
                    default:
                        return;
                }
            } else {
                switch (this._itemInstance.getItem().get_greater()) {
                    case 0:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addEarth(-1);
                                owner.addWind(-1);
                                owner.addWater(-1);
                                owner.addFire(-1);
                                return;
                            case 2:
                                owner.addEarth(-2);
                                owner.addWind(-2);
                                owner.addWater(-2);
                                owner.addFire(-2);
                                return;
                            case 3:
                                owner.addEarth(-3);
                                owner.addWind(-3);
                                owner.addWater(-3);
                                owner.addFire(-3);
                                return;
                            case 4:
                                owner.addEarth(-4);
                                owner.addWind(-4);
                                owner.addWater(-4);
                                owner.addFire(-4);
                                return;
                            case 5:
                                owner.addEarth(-5);
                                owner.addWind(-5);
                                owner.addWater(-5);
                                owner.addFire(-5);
                                return;
                            case 6:
                                owner.addEarth(-6);
                                owner.addWind(-6);
                                owner.addWater(-6);
                                owner.addFire(-6);
                                owner.addHpr(-1);
                                owner.addMpr(-1);
                                return;
                            case 7:
                                owner.addEarth(-10);
                                owner.addWind(-10);
                                owner.addWater(-10);
                                owner.addFire(-10);
                                owner.addHpr(-3);
                                owner.addMpr(-3);
                                return;
                            default:
                                owner.addEarth(-15);
                                owner.addWind(-15);
                                owner.addWater(-15);
                                owner.addFire(-15);
                                owner.addHpr(-3);
                                owner.addMpr(-3);
                                return;
                        }
                    case 1:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addMaxHp(-5);
                                return;
                            case 2:
                                owner.addMaxHp(-10);
                                return;
                            case 3:
                                owner.addMaxHp(-15);
                                return;
                            case 4:
                                owner.addMaxHp(-20);
                                return;
                            case 5:
                                owner.addMaxHp(-25);
                                return;
                            case 6:
                                owner.addMaxHp(-30);
                                owner.addMr(-2);
                                return;
                            case 7:
                                owner.addMaxHp(-40);
                                owner.addMr(-7);
                                return;
                            default:
                                owner.addMaxHp(-(level + 40));
                                owner.addMr(-(level + 12));
                                return;
                        }
                    case 2:
                        switch (level) {
                            case 0:
                                return;
                            case 1:
                                owner.addMaxMp(-3);
                                return;
                            case 2:
                                owner.addMaxMp(-6);
                                return;
                            case 3:
                                owner.addMaxMp(-9);
                                return;
                            case 4:
                                owner.addMaxMp(-12);
                                return;
                            case 5:
                                owner.addMaxMp(-15);
                                return;
                            case 6:
                                owner.addMaxMp(-25);
                                owner.addSp(-1);
                                return;
                            case 7:
                                owner.addMaxMp(-40);
                                owner.addSp(-2);
                                return;
                            default:
                                owner.addMaxMp(-(level + 40));
                                owner.addSp(-3);
                                return;
                        }
                    default:
                        return;
                }
            }
        }
    }
}
