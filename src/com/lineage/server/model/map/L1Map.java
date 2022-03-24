package com.lineage.server.model.map;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.types.Point;

public abstract class L1Map {
    private static L1NullMap _nullMap = new L1NullMap();

    public abstract int getHeight();

    public abstract int getId();

    public abstract int getOriginalTile(int i, int i2);

    public abstract int getTile(int i, int i2);

    public abstract int getWidth();

    public abstract int getX();

    public abstract int getY();

    public abstract boolean isArrowPassable(int i, int i2);

    public abstract boolean isArrowPassable(int i, int i2, int i3);

    public abstract boolean isArrowPassable(Point point);

    public abstract boolean isArrowPassable(Point point, int i);

    public abstract boolean isCombatZone(int i, int i2);

    public abstract boolean isCombatZone(Point point);

    public abstract boolean isDoorPassable(int i, int i2, int i3, L1NpcInstance l1NpcInstance);

    public abstract boolean isEnabledDeathPenalty();

    public abstract boolean isEscapable();

    public abstract int isExistDoor(int i, int i2);

    public abstract boolean isFishingZone(int i, int i2);

    public abstract boolean isInMap(int i, int i2);

    public abstract boolean isInMap(Point point);

    public abstract int isMapteleport();

    public abstract boolean isMarkable();

    public abstract boolean isNormalZone(int i, int i2);

    public abstract boolean isNormalZone(Point point);

    public abstract boolean isPassable(int i, int i2, int i3, L1Character l1Character);

    public abstract boolean isPassable(int i, int i2, L1Character l1Character);

    public abstract boolean isPassable(Point point, int i, L1Character l1Character);

    public abstract boolean isPassable(Point point, L1Character l1Character);

    public abstract boolean isPassableDna(int i, int i2, int i3);

    public abstract boolean isRecallPets();

    public abstract boolean isSafetyZone(int i, int i2);

    public abstract boolean isSafetyZone(Point point);

    public abstract boolean isTakePets();

    public abstract boolean isTeleportable();

    public abstract boolean isUnderwater();

    public abstract boolean isUsableItem();

    public abstract boolean isUsableSkill();

    public abstract boolean isUsePainwand();

    public abstract boolean isUseResurrection();

    public abstract void setPassable(int i, int i2, boolean z, int i3);

    public abstract void setPassable(Point point, boolean z);

    public abstract String toString(Point point);

    protected L1Map() {
    }

    public static L1Map newNull() {
        return _nullMap;
    }

    public boolean isNull() {
        return false;
    }
}
