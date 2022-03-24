package com.lineage.server.model.map;

import com.lineage.server.model.Instance.L1DoorInstance;
import com.lineage.server.model.Instance.L1GuardInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1V1Map extends L1Map {
    private static final byte BITFLAG_IS_IMPASSABLE = Byte.MIN_VALUE;
    private static final byte[] HEADING_TABLE_X;
    private static final byte[] HEADING_TABLE_Y;
    private static final Log _log = LogFactory.getLog(L1V1Map.class);
    private boolean _isEnabledDeathPenalty;
    private boolean _isEscapable;
    private int _isMapteleport;
    private boolean _isMarkable;
    private boolean _isRecallPets;
    private boolean _isTakePets;
    private boolean _isTeleportable;
    private boolean _isUnderwater;
    private boolean _isUsableItem;
    private boolean _isUsableSkill;
    private boolean _isUsePainwand;
    private boolean _isUseResurrection;
    private byte[][] _map;
    private int _mapId;
    private int _worldBottomRightX;
    private int _worldBottomRightY;
    private int _worldTopLeftX;
    private int _worldTopLeftY;

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
    }

    protected L1V1Map() {
    }

    public L1V1Map(int mapId, byte[][] map, int worldTopLeftX, int worldTopLeftY, boolean underwater, boolean markable, boolean teleportable, boolean escapable, boolean useResurrection, boolean usePainwand, boolean enabledDeathPenalty, boolean takePets, boolean recallPets, boolean usableItem, boolean usableSkill, int Mapteleport) {
        this._mapId = mapId;
        this._map = map;
        this._worldTopLeftX = worldTopLeftX;
        this._worldTopLeftY = worldTopLeftY;
        this._worldBottomRightX = (map.length + worldTopLeftX) - 1;
        this._worldBottomRightY = (map[0].length + worldTopLeftY) - 1;
        this._isUnderwater = underwater;
        this._isMarkable = markable;
        this._isTeleportable = teleportable;
        this._isEscapable = escapable;
        this._isUseResurrection = useResurrection;
        this._isUsePainwand = usePainwand;
        this._isEnabledDeathPenalty = enabledDeathPenalty;
        this._isTakePets = takePets;
        this._isRecallPets = recallPets;
        this._isUsableItem = usableItem;
        this._isUsableSkill = usableSkill;
        this._isMapteleport = Mapteleport;
    }

    public L1V1Map(L1V1Map map) {
        this._mapId = map._mapId;
        this._map = new byte[map._map.length][];
        for (int i = 0; i < map._map.length; i++) {
            this._map[i] = (byte[]) map._map[i].clone();
        }
        this._worldTopLeftX = map._worldTopLeftX;
        this._worldTopLeftY = map._worldTopLeftY;
        this._worldBottomRightX = map._worldBottomRightX;
        this._worldBottomRightY = map._worldBottomRightY;
    }

    private int accessTile(int x, int y) {
        if (!isInMap(x, y)) {
            return 0;
        }
        return this._map[x - this._worldTopLeftX][y - this._worldTopLeftY];
    }

    private int accessOriginalTile(int x, int y) {
        return accessTile(x, y) & 127;
    }

    private void setTile(int x, int y, int tile) {
        if (isInMap(x, y)) {
            this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = (byte) tile;
        }
    }

    public byte[][] getRawTiles() {
        return this._map;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getId() {
        return this._mapId;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getX() {
        return this._worldTopLeftX;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getY() {
        return this._worldTopLeftY;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getWidth() {
        return (this._worldBottomRightX - this._worldTopLeftX) + 1;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getHeight() {
        return (this._worldBottomRightY - this._worldTopLeftY) + 1;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getTile(int x, int y) {
        if ((this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] & BITFLAG_IS_IMPASSABLE) != 0) {
            return 300;
        }
        return accessOriginalTile(x, y);
    }

    @Override // com.lineage.server.model.map.L1Map
    public int getOriginalTile(int x, int y) {
        return accessOriginalTile(x, y);
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isInMap(Point pt) {
        return isInMap(pt.getX(), pt.getY());
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isInMap(int x, int y) {
        if (this._mapId == 4) {
            if (x < 32520 || y < 32070) {
                return false;
            }
            if (y < 32190 && x < 33950) {
                return false;
            }
        }
        if (this._worldTopLeftX > x || x > this._worldBottomRightX || this._worldTopLeftY > y || y > this._worldBottomRightY) {
            return false;
        }
        return true;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(Point pt, L1Character cha) {
        return isPassable(pt.getX(), pt.getY(), cha);
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(int x, int y, L1Character cha) {
        return isPassable(x, y + -1, 4, cha) || isPassable(x + 1, y, 6, cha) || isPassable(x, y + 1, 0, cha) || isPassable(x + -1, y, 2, cha);
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(Point pt, int heading, L1Character cha) {
        return isPassable(pt.getX(), pt.getY(), heading, cha);
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassable(int x, int y, int heading, L1Character cha) {
        try {
            int tile1 = accessTile(x, y);
            int tile2 = accessTile(x + HEADING_TABLE_X[heading], y + HEADING_TABLE_Y[heading]);
            if (tile2 == 0 || (tile2 & -128) == -128) {
                return false;
            }
            if (cha != null) {
                switch (this._mapId) {
                    case 0:
                        return set_map(tile2, 1);
                    case 4:
                    case 57:
                    case 58:
                    case 68:
                    case 69:
                    case 70:
                    case 303:
                    case 430:
                    case 440:
                    case 445:
                    case 480:
                    case 613:
                    case 621:
                    case 630:
                        switch (tile2) {
                            case 12:
                            case 21:
                            case 26:
                            case 28:
                            case 29:
                            case 44:
                                return false;
                            default:
                                return set_map(tile2, 8);
                        }
                    default:
                        return set_map(tile2, 3);
                }
            } else {
                switch (heading) {
                    case 0:
                        return (tile1 & 2) == 2;
                    case 1:
                        return (accessTile(x, y + -1) & 1) == 1 || (accessTile(x + 1, y) & 2) == 2;
                    case 2:
                        return (tile1 & 1) == 1;
                    case 3:
                        return (accessTile(x, y + 1) & 1) == 1;
                    case 4:
                        return (tile2 & 2) == 2;
                    case 5:
                        return (tile2 & 1) == 1 || (tile2 & 2) == 2;
                    case 6:
                        return (tile2 & 1) == 1;
                    case 7:
                        return (accessTile(x + -1, y) & 2) == 2;
                    default:
                        return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isPassableDna(int x, int y, int heading) {
        try {
            int tile2 = accessTile(x + HEADING_TABLE_X[heading], y + HEADING_TABLE_Y[heading]);
            if (tile2 == 0) {
                return false;
            }
            switch (this._mapId) {
                case 0:
                    return set_map(tile2, 1);
                case 4:
                case 57:
                case 58:
                case 68:
                case 69:
                case 70:
                case 303:
                case 430:
                case 440:
                case 445:
                case 480:
                case 613:
                case 621:
                case 630:
                    switch (tile2) {
                        case 12:
                        case 21:
                        case 26:
                        case 28:
                        case 29:
                        case 44:
                            return false;
                        default:
                            return set_map(tile2, 8);
                    }
                default:
                    return set_map(tile2, 3);
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean set_map(int tile2, int i) {
        return (tile2 & i) != 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isDoorPassable(int x, int y, int heading, L1NpcInstance npc) {
        if (heading == -1) {
            return false;
        }
        try {
            int tile2 = accessTile(x + HEADING_TABLE_X[heading], y + HEADING_TABLE_Y[heading]);
            if (npc != null) {
                if (tile2 != 3) {
                    return true;
                }
                if (npc.is_now_target() == null) {
                    return false;
                }
                Iterator<L1Object> it = World.get().getVisibleObjects(npc, 2).iterator();
                while (it.hasNext()) {
                    L1Object object = it.next();
                    if (object instanceof L1DoorInstance) {
                        L1DoorInstance door = (L1DoorInstance) object;
                        switch (door.getDoorId()) {
                            case 6006:
                            case 6007:
                            case 10000:
                            case 10001:
                            case 10002:
                            case 10003:
                            case 10004:
                            case 10005:
                            case 10006:
                            case 10007:
                            case 10008:
                            case 10009:
                            case 10010:
                            case 10011:
                            case 10012:
                            case 10013:
                            case 10015:
                            case 10016:
                            case 10017:
                            case 10019:
                            case 10020:
                            case 10036:
                                return false;
                            default:
                                if (door.getOpenStatus() != 29) {
                                    return true;
                                }
                                if (npc instanceof L1GuardInstance) {
                                    door.open();
                                    return true;
                                } else if (door.getKeeperId() == 0) {
                                    door.open();
                                    return true;
                                } else {
                                    continue;
                                }
                        }
                    }
                }
                return false;
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    @Override // com.lineage.server.model.map.L1Map
    public void setPassable(Point pt, boolean isPassable) {
        setPassable(pt.getX(), pt.getY(), isPassable, 2);
    }

    @Override // com.lineage.server.model.map.L1Map
    public void setPassable(int x, int y, boolean isPassable, int door) {
        switch (door) {
            case 0:
                set_door_0(x, y, isPassable);
                return;
            case 1:
                set_door_1(x, y, isPassable);
                return;
            default:
                if (isPassable) {
                    setTile(x, y, (short) (accessTile(x, y) & 127));
                    return;
                } else {
                    setTile(x, y, (short) (accessTile(x, y) | -128));
                    return;
                }
        }
    }

    private void set_door_0(int x, int y, boolean isPassable) {
        if (isPassable) {
            try {
                this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = 47;
            } catch (Exception e) {
                _log.error("X:" + x + " Y:" + y + " MAP:" + this._mapId, e);
            }
        } else {
            this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = 3;
            this._map[(x - 1) - this._worldTopLeftX][y - this._worldTopLeftY] = 3;
            this._map[(x + 1) - this._worldTopLeftX][y - this._worldTopLeftY] = 3;
        }
    }

    private void set_door_1(int x, int y, boolean isPassable) {
        if (isPassable) {
            try {
                this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = 47;
            } catch (Exception e) {
                _log.error("X:" + x + " Y:" + y + " MAP:" + this._mapId, e);
            }
        } else {
            this._map[x - this._worldTopLeftX][y - this._worldTopLeftY] = 3;
            this._map[x - this._worldTopLeftX][(y - 1) - this._worldTopLeftY] = 3;
            this._map[x - this._worldTopLeftX][(y + 1) - this._worldTopLeftY] = 3;
        }
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isSafetyZone(Point pt) {
        return isSafetyZone(pt.getX(), pt.getY());
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isSafetyZone(int x, int y) {
        return (accessOriginalTile(x, y) & 48) == 16;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isCombatZone(Point pt) {
        return isCombatZone(pt.getX(), pt.getY());
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isCombatZone(int x, int y) {
        return (accessOriginalTile(x, y) & 48) == 32;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isNormalZone(Point pt) {
        return isNormalZone(pt.getX(), pt.getY());
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isNormalZone(int x, int y) {
        return (accessOriginalTile(x, y) & 48) == 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(Point pt) {
        return isArrowPassable(pt.getX(), pt.getY());
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(int x, int y) {
        return (accessOriginalTile(x, y) & 14) != 0;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(Point pt, int heading) {
        return isArrowPassable(pt.getX(), pt.getY(), heading);
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isArrowPassable(int x, int y, int heading) {
        try {
            int tile2 = accessTile(x + HEADING_TABLE_X[heading], y + HEADING_TABLE_Y[heading]);
            switch (tile2) {
                case 0:
                case 3:
                    return false;
                case 1:
                case 2:
                default:
                    if ((tile2 & 12) != 0) {
                        return true;
                    }
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUnderwater() {
        return this._isUnderwater;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isMarkable() {
        return this._isMarkable;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isTeleportable() {
        return this._isTeleportable;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isEscapable() {
        return this._isEscapable;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUseResurrection() {
        return this._isUseResurrection;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsePainwand() {
        return this._isUsePainwand;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isEnabledDeathPenalty() {
        return this._isEnabledDeathPenalty;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isTakePets() {
        return this._isTakePets;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isRecallPets() {
        return this._isRecallPets;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsableItem() {
        return this._isUsableItem;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isUsableSkill() {
        return this._isUsableSkill;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int isMapteleport() {
        return this._isMapteleport;
    }

    @Override // com.lineage.server.model.map.L1Map
    public boolean isFishingZone(int x, int y) {
        return accessOriginalTile(x, y) == 28;
    }

    @Override // com.lineage.server.model.map.L1Map
    public int isExistDoor(int x, int y) {
        try {
            return this._map[x - this._worldTopLeftX][y - this._worldTopLeftY];
        } catch (Exception e) {
            return 0;
        }
    }

    @Override // com.lineage.server.model.map.L1Map
    public String toString(Point pt) {
        return new StringBuilder().append(getOriginalTile(pt.getX(), pt.getY())).toString();
    }
}
