package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.SprTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_SystemMessage;
import java.util.EnumMap;

public class AcceleratorChecker {

    /* renamed from: $SWITCH_TABLE$com$lineage$server$clientpackets$AcceleratorChecker$ACT_TYPE */
    private static /* synthetic */ int[] f12xa04bda95 = null;
    private static double CHECK_MOVESTRICTNESS = (((double) (ConfigOther.CHECK_MOVE_STRICTNESS - 5)) / 100.0d);
    private static double CHECK_STRICTNESS = (((double) (ConfigOther.CHECK_STRICTNESS - 5)) / 100.0d);
    private static final double HASTE_RATE = 0.745d;
    private static final int INJUSTICE_COUNT_LIMIT = ConfigOther.INJUSTICE_COUNT;
    private static final int JUSTICE_COUNT_LIMIT = ConfigOther.JUSTICE_COUNT;
    public static final int R_DETECTED = 1;
    public static final int R_DISPOSED = 2;
    public static final int R_OK = 0;
    private static final double WAFFLE_RATE = 0.874d;
    private final EnumMap<ACT_TYPE, Long> _actTimers = new EnumMap<>(ACT_TYPE.class);
    private final L1PcInstance _pc;
    private int move_injusticeCount;
    private int move_justiceCount;
    private int moveresult = 0;

    public enum ACT_TYPE {
        MOVE,
        ATTACK,
        SPELL_DIR,
        SPELL_NODIR
    }

    /* renamed from: $SWITCH_TABLE$com$lineage$server$clientpackets$AcceleratorChecker$ACT_TYPE */
    static /* synthetic */ int[] m0xa04bda95() {
        int[] iArr = f12xa04bda95;
        if (iArr == null) {
            iArr = new int[ACT_TYPE.values().length];
            try {
                iArr[ACT_TYPE.ATTACK.ordinal()] = 2;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[ACT_TYPE.MOVE.ordinal()] = 1;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[ACT_TYPE.SPELL_DIR.ordinal()] = 3;
            } catch (NoSuchFieldError ignored) {
            }
            try {
                iArr[ACT_TYPE.SPELL_NODIR.ordinal()] = 4;
            } catch (NoSuchFieldError ignored) {
            }
            f12xa04bda95 = iArr;
        }
        return iArr;
    }

    public AcceleratorChecker(L1PcInstance pc) {
        this._pc = pc;
        this.move_injusticeCount = 0;
        this.move_justiceCount = 0;
        long now = System.currentTimeMillis();
        ACT_TYPE[] values = ACT_TYPE.values();
        for (ACT_TYPE each : values) {
            this._actTimers.put(each, Long.valueOf(now));
            EnumMap<ACT_TYPE, Long> _checkTimers = new EnumMap<>(ACT_TYPE.class);
            _checkTimers.put(each, Long.valueOf(now));
        }
    }

    public static void Setspeed() {
        CHECK_STRICTNESS = ((double) (ConfigOther.CHECK_STRICTNESS - 5)) / 100.0d;
        CHECK_MOVESTRICTNESS = ((double) (ConfigOther.CHECK_MOVE_STRICTNESS - 5)) / 100.0d;
    }

    public int checkInterval(ACT_TYPE type) {
        int attackresult = 0;
        switch (m0xa04bda95()[type.ordinal()]) {
            case 1:
                long movenow = System.currentTimeMillis();
                long moveinterval = movenow - this._actTimers.get(type).longValue();
                int moverightInterval = getRightInterval(type);
                moveinterval = (long) (((double) moveinterval) * CHECK_MOVESTRICTNESS);
                if (0 < moveinterval && moveinterval < ((long) moverightInterval)) {
                    this.move_injusticeCount++;
                    this.move_justiceCount = 0;
                    if (this.move_injusticeCount >= INJUSTICE_COUNT_LIMIT) {
                        doPunishment();
                        this.moveresult = 2;
                    } else {
                        this.moveresult = 1;
                    }
                } else if (moveinterval >= ((long) moverightInterval)) {
                    this.move_justiceCount++;
                    if (this.move_justiceCount >= JUSTICE_COUNT_LIMIT) {
                        this.move_injusticeCount = 0;
                        this.move_justiceCount = 0;
                    }
                    this.moveresult = 0;
                }
                this._actTimers.put(type, Long.valueOf(movenow));
                return this.moveresult;
            default:
                long attacknow = System.currentTimeMillis();
                long attackinterval = attacknow - this._actTimers.get(type).longValue();
                int attackrightInterval = getRightInterval(type);
                attackinterval = (long) (((double) attackinterval) * CHECK_STRICTNESS);
                if (0 < attackinterval && attackinterval < ((long) attackrightInterval)) {
                    attackresult = 2;
                } else if (attackinterval >= ((long) attackrightInterval)) {
                    attackresult = 0;
                } else {
                    attackresult = 2;
                }
                this._actTimers.put(type, Long.valueOf(attacknow));
                return attackresult;
        }
    }

    private void doPunishment() {
        int punishment_type = Math.abs(ConfigOther.PUNISHMENT_TYPE);
        int punishment_time = Math.abs(ConfigOther.PUNISHMENT_TIME);
        int punishment_mapid = Math.abs(ConfigOther.PUNISHMENT_MAP_ID);
        if (!this._pc.isGm()) {
            int x = this._pc.getX();
            int y = this._pc.getY();
            int mapid = this._pc.getMapId();
            switch (punishment_type) {
                case 0:
                    this._pc.sendPackets(new S_SystemMessage("加速器檢測警告" + punishment_time + "秒後強制驅離。"));
                    try {
                        Thread.sleep((long) (punishment_time * L1SkillId.STATUS_BRAVE));
                    } catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                    }
                    this._pc.sendPackets(new S_Disconnect());
                    WriteLogTxt.Recording("加速器斷線紀錄", "帳號:  【" + this._pc.getAccountName() + "】 " + ",玩家: 【" + this._pc.getName() + "】 " + ",IP(" + ((Object) this._pc.getNetConnection().getIp()) + ")" + ",地點: " + this._pc.getLocation());
                    return;
                case 1:
                    this._pc.sendPackets(new S_Paralysis(6, true));
                    this._pc.sendPackets(new S_SystemMessage("加速器檢測警告" + punishment_time + "秒後解除您的行動。"));
                    try {
                        Thread.sleep((long) (punishment_time * L1SkillId.STATUS_BRAVE));
                    } catch (Exception e2) {
                        System.out.println(e2.getLocalizedMessage());
                    }
                    this._pc.sendPackets(new S_Paralysis(6, false));
                    WriteLogTxt.Recording("加速器斷線紀錄", "帳號:  【" + this._pc.getAccountName() + "】 " + ",玩家: 【" + this._pc.getName() + "】 " + ",IP(" + ((Object) this._pc.getNetConnection().getIp()) + ")" + ",地點: " + this._pc.getLocation());
                    return;
                case 2:
                    L1Teleport.teleport(this._pc, 32698, 32857, (short) punishment_mapid, 5, false);
                    this._pc.sendPackets(new S_SystemMessage("加速器檢測警告" + punishment_time + "秒後傳送到地獄。"));
                    try {
                        Thread.sleep((long) (punishment_time * L1SkillId.STATUS_BRAVE));
                    } catch (Exception e3) {
                        System.out.println(e3.getLocalizedMessage());
                    }
                    L1Teleport.teleport(this._pc, x, y, (short) mapid, 5, false);
                    WriteLogTxt.Recording("加速器斷線紀錄", "帳號:  【" + this._pc.getAccountName() + "】 " + ",玩家: 【" + this._pc.getName() + "】 " + ",IP(" + ((Object) this._pc.getNetConnection().getIp()) + ")" + ",地點: " + this._pc.getLocation());
                    return;
                case 3:
                    int[] Head = new int[8];
                    Head[1] = 1;
                    Head[2] = 2;
                    Head[3] = 3;
                    Head[4] = 4;
                    Head[5] = 5;
                    Head[6] = 6;
                    Head[7] = 7;
                    int[] X = {x, x - 1, x - 1, x - 1, x, x + 1, x + 1, x + 1};
                    int[] Y = {y + 1, y + 1, y, y - 1, y - 1, y - 1, y, y + 1};
                    for (int i = 0; i < Head.length; i++) {
                        if (this._pc.getHeading() == Head[i]) {
                            L1Teleport.teleport(this._pc, X[i], Y[i], (short) mapid, this._pc.getHeading(), false);
                            this._pc.sendPackets(new S_SystemMessage("加速器檢測。"));
                        }
                        try {
                            Thread.sleep((long) (punishment_time * L1SkillId.STATUS_BRAVE));
                        } catch (Exception e4) {
                            System.out.println(e4.getLocalizedMessage());
                        }
                    }
                    return;
                default:
                    return;
            }
        } else if (ConfigOther.DEBUG_MODE) {
            this._pc.sendPackets(new S_SystemMessage("遊戲管理員在遊戲中使用加速器檢測中。"));
            this.move_injusticeCount = 0;
        }
    }

    private int getRightInterval(ACT_TYPE type) {
        int interval;
        switch (m0xa04bda95()[type.ordinal()]) {
            case 1:
                interval = SprTable.get().getMoveSpeed(this._pc.getTempCharGfx(), this._pc.getCurrentWeapon());
                break;
            case 2:
                interval = SprTable.get().getAttackSpeed(this._pc.getTempCharGfx(), this._pc.getCurrentWeapon() + 1);
                break;
            case 3:
                interval = SprTable.get().getDirSpellSpeed(this._pc.getTempCharGfx());
                break;
            case 4:
                interval = SprTable.get().getNodirSpellSpeed(this._pc.getTempCharGfx());
                break;
            default:
                return 0;
        }
        switch (this._pc.getMoveSpeed()) {
            case 1:
                interval = (int) (((double) interval) * HASTE_RATE);
                break;
            case 2:
                interval = (int) (((double) interval) / HASTE_RATE);
                break;
        }
        switch (this._pc.getBraveSpeed()) {
            case 1:
                interval = (int) (((double) interval) * HASTE_RATE);
                break;
            case 3:
                interval = (int) (((double) interval) * WAFFLE_RATE);
                break;
            case 4:
                if (type.equals(ACT_TYPE.MOVE)) {
                    interval = (int) (((double) interval) * HASTE_RATE);
                    break;
                }
                break;
            case 6:
                if (type.equals(ACT_TYPE.ATTACK) && this._pc.isFastAttackable()) {
                    interval = (int) (((double) interval) * 0.65113d);
                    break;
                }
        }
        if (this._pc.hasSkillEffect(L1SkillId.STATUS_RIBRAVE) && type.equals(ACT_TYPE.MOVE)) {
            interval = (int) (((double) interval) * WAFFLE_RATE);
        }
        if (this._pc.hasSkillEffect(998)) {
            interval = (int) (((double) interval) * WAFFLE_RATE);
        }
        if (this._pc.hasSkillEffect(167) && !type.equals(ACT_TYPE.MOVE)) {
            interval /= 2;
        }
        if (this._pc.getMapId() == 5143) {
            return (int) (((double) interval) * 0.1d);
        }
        return interval;
    }
}
