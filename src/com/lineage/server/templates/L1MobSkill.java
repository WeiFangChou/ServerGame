package com.lineage.server.templates;

public class L1MobSkill implements Cloneable {
    public static final int TYPE_NONE = 0;
    public static final int TYPE_PHYSICAL_ATTACK = 1;
    public static final int TYPE_MAGIC_ATTACK = 2;
    public static final int TYPE_SUMMON = 3;
    public static final int TYPE_POLY = 4;
    public static final int AHTHARAS_1 = 5;
    public static final int AHTHARAS_2 = 6;
    public static final int AHTHARAS_3 = 7;
    public static final int CHANGE_TARGET_NO = 0;
    public static final int CHANGE_TARGET_ME = 2;
    public static final int CHANGE_TARGET_RANDOM = 3;
    private final int skillSize;
    private boolean[] isSkillDelayType;
    private boolean[] isSkillDelayIdx;
    private int mobid;
    private String mobName;
    private int[] type;
    private int[] triRnd;
    int[] triHp;
    int[] triCompanionHp;
    int[] triRange;
    int[] triCount;
    int[] changeTarget;
    int[] range;
    int[] areaWidth;
    int[] areaHeight;
    int[] leverage;
    int[] skillId;
    int[] gfxid;
    int[] actid;
    int[] summon;
    int[] summonMin;
    int[] summonMax;
    int[] polyId;
    int[] reuseDelay;

    public L1MobSkill clone() {
        try {
            return (L1MobSkill)super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new InternalError(var2.getMessage());
        }
    }

    public int getSkillSize() {
        return this.skillSize;
    }

    public L1MobSkill(int sSize) {
        this.skillSize = sSize;
        this.type = new int[this.skillSize];
        this.triRnd = new int[this.skillSize];
        this.triHp = new int[this.skillSize];
        this.triCompanionHp = new int[this.skillSize];
        this.triRange = new int[this.skillSize];
        this.triCount = new int[this.skillSize];
        this.changeTarget = new int[this.skillSize];
        this.range = new int[this.skillSize];
        this.areaWidth = new int[this.skillSize];
        this.areaHeight = new int[this.skillSize];
        this.leverage = new int[this.skillSize];
        this.skillId = new int[this.skillSize];
        this.gfxid = new int[this.skillSize];
        this.actid = new int[this.skillSize];
        this.summon = new int[this.skillSize];
        this.summonMin = new int[this.skillSize];
        this.summonMax = new int[this.skillSize];
        this.polyId = new int[this.skillSize];
        this.reuseDelay = new int[this.skillSize];
        this.isSkillDelayIdx = new boolean[this.skillSize];
        this.isSkillDelayType = new boolean[this.skillSize];
    }

    public void setSkillDelayType(int type, boolean flag) {
        if (type >= 0 && type < this.getSkillSize()) {
            this.isSkillDelayType[type] = flag;
        }
    }

    public boolean isSkillDelayType(int type) {
        return type >= 0 && type < this.getSkillSize() ? this.isSkillDelayType[type] : false;
    }

    public void setSkillDelayIdx(int idx, boolean flag) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.isSkillDelayIdx[idx] = flag;
        }
    }

    public boolean isSkillDelayIdx(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.isSkillDelayIdx[idx] : false;
    }

    public int get_mobid() {
        return this.mobid;
    }

    public void set_mobid(int i) {
        this.mobid = i;
    }

    public String getMobName() {
        return this.mobName;
    }

    public void setMobName(String s) {
        this.mobName = s;
    }

    public int getType(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.type[idx] : 0;
    }

    public void setType(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.type[idx] = i;
        }
    }

    public int getTriggerRandom(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.triRnd[idx] : 0;
    }

    public void setTriggerRandom(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.triRnd[idx] = i;
        }
    }

    public int getTriggerHp(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.triHp[idx] : 0;
    }

    public void setTriggerHp(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.triHp[idx] = i;
        }
    }

    public int getTriggerCompanionHp(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.triCompanionHp[idx] : 0;
    }

    public void setTriggerCompanionHp(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.triCompanionHp[idx] = i;
        }
    }

    public int getTriggerRange(int idx) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            Math.abs(idx);
            return this.triRange[idx];
        } else {
            return 0;
        }
    }

    public void setTriggerRange(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.triRange[idx] = i;
        }
    }

    public boolean isTriggerDistance(int idx, int distance) {
        int triggerRange = this.getTriggerRange(idx);
        return triggerRange < 0 && distance <= Math.abs(triggerRange) || triggerRange > 0 && distance >= triggerRange;
    }

    public int getTriggerCount(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.triCount[idx] : 0;
    }

    public void setTriggerCount(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.triCount[idx] = i;
        }
    }

    public int getChangeTarget(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.changeTarget[idx] : 0;
    }

    public void setChangeTarget(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.changeTarget[idx] = i;
        }
    }

    public int getRange(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.range[idx] : 0;
    }

    public void setRange(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.range[idx] = i;
        }
    }

    public int getAreaWidth(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.areaWidth[idx] : 0;
    }

    public void setAreaWidth(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.areaWidth[idx] = i;
        }
    }

    public int getAreaHeight(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.areaHeight[idx] : 0;
    }

    public void setAreaHeight(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.areaHeight[idx] = i;
        }
    }

    public int getLeverage(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.leverage[idx] : 0;
    }

    public void setLeverage(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.leverage[idx] = i;
        }
    }

    public int getSkillId(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.skillId[idx] : 0;
    }

    public void setSkillId(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.skillId[idx] = i;
        }
    }

    public int getGfxid(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.gfxid[idx] : 0;
    }

    public void setGfxid(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.gfxid[idx] = i;
        }
    }

    public int getActid(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.actid[idx] : 0;
    }

    public void setActid(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.actid[idx] = i;
        }
    }

    public int getSummon(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.summon[idx] : 0;
    }

    public void setSummon(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.summon[idx] = i;
        }
    }

    public int getSummonMin(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.summonMin[idx] : 0;
    }

    public void setSummonMin(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.summonMin[idx] = i;
        }
    }

    public int getSummonMax(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.summonMax[idx] : 0;
    }

    public void setSummonMax(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.summonMax[idx] = i;
        }
    }

    public int getPolyId(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.polyId[idx] : 0;
    }

    public void setPolyId(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.polyId[idx] = i;
        }
    }

    public int getReuseDelay(int idx) {
        return idx >= 0 && idx < this.getSkillSize() ? this.reuseDelay[idx] : 0;
    }

    public void setReuseDelay(int idx, int i) {
        if (idx >= 0 && idx < this.getSkillSize()) {
            this.reuseDelay[idx] = i;
        }
    }
}
