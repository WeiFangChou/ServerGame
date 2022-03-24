package com.lineage.server.model.map;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.types.Point;

/* access modifiers changed from: package-private */
/* compiled from: L1Map */
public class L1NullMap extends L1Map {
    @Override // com.lineage.server.model.map.L1Map
    public int getId() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getX() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getY() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getWidth() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getHeight() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getTile(int x, int y) {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getOriginalTile(int x, int y) {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isInMap(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isInMap(Point pt) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(int x, int y, L1Character cha) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(Point pt, L1Character cha) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(int x, int y, int heading, L1Character cha) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassableDna(int x, int y, int heading) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(Point pt, int heading, L1Character cha) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isDoorPassable(int x, int y, int heading, L1NpcInstance npc) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public void setPassable(int x, int y, boolean isPassable, int door) {
    }

    @Override // com.lineage.server.model.map.L1Map
    public void setPassable(Point pt, boolean isPassable) {
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isSafetyZone(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isSafetyZone(Point pt) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isCombatZone(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isCombatZone(Point pt) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isNormalZone(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isNormalZone(Point pt) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(Point pt) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(int x, int y, int heading) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(Point pt, int heading) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUnderwater() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isMarkable() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isTeleportable() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isEscapable() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUseResurrection() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsePainwand() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isEnabledDeathPenalty() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isTakePets() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isRecallPets() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsableItem() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsableSkill() {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isFishingZone(int x, int y) {
        return false;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int isExistDoor(int x, int y) {
        return 3;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int isMapteleport() {
        return 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public String toString(Point pt) {
        return "null";
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isNull() {
        return true;
    }
}
