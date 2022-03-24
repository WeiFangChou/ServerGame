package com.lineage.server.model.Instance;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.types.Point;
import com.lineage.server.world.World;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcMove extends NpcMoveExecutor {
    private static final Log _log = LogFactory.getLog(NpcMove.class);
    protected final int[][] DIR_TABLE;
    private int _ErrorCount;
    private int _heading;
    private final L1NpcInstance _npc;
    private int courceRange = 20;

    public NpcMove(L1NpcInstance npc) {
        int[] iArr = new int[2];
        iArr[1] = -1;
        int[] iArr2 = new int[2];
        iArr2[0] = 1;
        int[] iArr3 = new int[2];
        iArr3[1] = 1;
        int[] iArr4 = new int[2];
        iArr4[0] = -1;
        this.DIR_TABLE = new int[][]{iArr, new int[]{1, -1}, iArr2, new int[]{1, 1}, iArr3, new int[]{-1, 1}, iArr4, new int[]{-1, -1}};
        this._npc = npc;
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public void setDirectionMove(int dir) {
        if (dir >= 0) {
            int locx = this._npc.getX();
            int locy = this._npc.getY();
            int locx2 = locx + HEADING_TABLE_X[dir];
            int locy2 = locy + HEADING_TABLE_Y[dir];
            this._npc.setHeading(dir);
            if (!(this._npc instanceof L1DollInstance)) {
                this._npc.getMap().setPassable(this._npc.getLocation(), true);
            }
            this._npc.setX(locx2);
            this._npc.setY(locy2);
            if (!(this._npc instanceof L1DollInstance)) {
                this._npc.getMap().setPassable(this._npc.getLocation(), false);
            }
            this._npc.broadcastPacketAll(new S_MoveCharPacket(this._npc));
            if (this._npc.getMovementDistance() > 0 && (((this._npc instanceof L1GuardInstance) || (this._npc instanceof L1MerchantInstance) || (this._npc instanceof L1MonsterInstance)) && this._npc.getLocation().getLineDistance(new Point(this._npc.getHomeX(), this._npc.getHomeY())) > ((double) this._npc.getMovementDistance()))) {
                this._npc.teleport(this._npc.getHomeX(), this._npc.getHomeY(), this._npc.getHeading());
            }
            if (this._npc.getNpcTemplate().get_npcId() >= 45912 && this._npc.getNpcTemplate().get_npcId() <= 45916) {
                if (this._npc.getX() >= 32591 && this._npc.getX() <= 32644) {
                    return;
                }
                if ((this._npc.getY() < 32643 || this._npc.getY() > 32688) && this._npc.getMapId() == 4) {
                    this._npc.teleport(this._npc.getHomeX(), this._npc.getHomeY(), this._npc.getHeading());
                }
            }
        }
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public void clear() {
        this._ErrorCount = 0;
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public int moveDirection(int x, int y) {
        return moveDirection(x, y, this._npc.getLocation().getLineDistance(new Point(x, y)));
    }

    public int moveDirection(int x, int y, double d) {
        int dir;
        if (this._npc.hasSkillEffect(40) && d >= 2.0d) {
            return -1;
        }
        if (d > 30.0d) {
            return -1;
        }
        if (d > 20.0d) {
            dir = openDoor(checkObject(_targetDirection(this._npc.getHeading(), this._npc.getX(), this._npc.getY(), x, y)));
        } else {
            dir = openDoor(checkObject(_serchCource(x, y)));
            if (dir == -1) {
                this._ErrorCount++;
                if (this._ErrorCount < 20) {
                    dir = _targetDirection(this._npc.getHeading(), this._npc.getX(), this._npc.getY(), x, y);
                    if (!_exsistCharacterBetweenTarget(dir)) {
                        dir = openDoor(checkObject(dir));
                    }
                } else {
                    clear();
                    return -1;
                }
            } else {
                clear();
            }
        }
        return dir;
    }

    private static int _targetDirection(int h, int x, int y, int tx, int ty) {
        try {
            float dis_x = (float) Math.abs(x - tx);
            float dis_y = (float) Math.abs(y - ty);
            float dis = Math.max(dis_x, dis_y);
            if (dis == 0.0f) {
                return h;
            }
            int avg_x = (int) Math.floor((double) ((dis_x / dis) + 0.59f));
            int avg_y = (int) Math.floor((double) ((dis_y / dis) + 0.59f));
            int dir_x = 0;
            int dir_y = 0;
            if (x < tx) {
                dir_x = 1;
            }
            if (x > tx) {
                dir_x = -1;
            }
            if (y < ty) {
                dir_y = 1;
            }
            if (y > ty) {
                dir_y = -1;
            }
            if (avg_x == 0) {
                dir_x = 0;
            }
            if (avg_y == 0) {
                dir_y = 0;
            }
            switch (dir_x) {
                case -1:
                    switch (dir_y) {
                        case -1:
                            return 7;
                        case 0:
                            return 6;
                        case 1:
                            return 5;
                        default:
                            return h;
                    }
                case 0:
                    switch (dir_y) {
                        case -1:
                            return 0;
                        case 0:
                        default:
                            return h;
                        case 1:
                            return 4;
                    }
                case 1:
                    switch (dir_y) {
                        case -1:
                            return 1;
                        case 0:
                            return 2;
                        case 1:
                            return 3;
                        default:
                            return h;
                    }
                default:
                    return h;
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return h;
        }
    }

    private boolean _exsistCharacterBetweenTarget(int dir) {
        try {
            if (!(this._npc instanceof L1MonsterInstance)) {
                return false;
            }
            if (this._npc.is_now_target() == null) {
                return false;
            }
            int locX = this._npc.getX();
            int locY = this._npc.getY();
            int targetX = locX + HEADING_TABLE_X[dir];
            int targetY = locY + HEADING_TABLE_Y[dir];
            Iterator<L1Object> iter = World.get().getVisibleObjects(this._npc, 1).iterator();
            while (iter.hasNext()) {
                L1Object object = iter.next();
                boolean isCheck = false;
                if (object.getX() == targetX && object.getY() == targetY && object.getMapId() == this._npc.getMapId()) {
                    isCheck = true;
                }
                if (isCheck) {
                    boolean isHate = false;
                    if (object instanceof L1PcInstance) {
                        L1PcInstance pc = (L1PcInstance) object;
                        if (!pc.isGhost() && !pc.isGmInvis()) {
                            isHate = true;
                        }
                    } else if (object instanceof L1PetInstance) {
                        isHate = true;
                    } else if (object instanceof L1SummonInstance) {
                        isHate = true;
                    }
                    if (isHate) {
                        L1Character cha = (L1Character) object;
                        this._npc._hateList.add(cha, 0);
                        this._npc._target = cha;
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public int targetReverseDirection(int tx, int ty) {
        int dir = targetDirection(tx, ty) + 4;
        if (dir > 7) {
            return dir - 8;
        }
        return dir;
    }

    public int targetDirection(int tx, int ty) {
        float dis_x = (float) Math.abs(this._npc.getX() - tx);
        float dis_y = (float) Math.abs(this._npc.getY() - ty);
        float dis = Math.max(dis_x, dis_y);
        if (dis == 0.0f) {
            return getHeading();
        }
        int avg_x = (int) Math.floor((double) ((dis_x / dis) + 0.59f));
        int avg_y = (int) Math.floor((double) ((dis_y / dis) + 0.59f));
        int dir_x = 0;
        int dir_y = 0;
        if (this._npc.getX() < tx) {
            dir_x = 1;
        }
        if (this._npc.getX() > tx) {
            dir_x = -1;
        }
        if (this._npc.getY() < ty) {
            dir_y = 1;
        }
        if (this._npc.getY() > ty) {
            dir_y = -1;
        }
        if (avg_x == 0) {
            dir_x = 0;
        }
        if (avg_y == 0) {
            dir_y = 0;
        }
        if (dir_x == 1 && dir_y == -1) {
            return 1;
        }
        if (dir_x == 1 && dir_y == 0) {
            return 2;
        }
        if (dir_x == 1 && dir_y == 1) {
            return 3;
        }
        if (dir_x == 0 && dir_y == 1) {
            return 4;
        }
        if (dir_x == -1 && dir_y == 1) {
            return 5;
        }
        if (dir_x == -1 && dir_y == 0) {
            return 6;
        }
        if (dir_x == -1 && dir_y == -1) {
            return 7;
        }
        if (dir_x == 0 && dir_y == -1) {
            return 0;
        }
        return getHeading();
    }

    public int getHeading() {
        return this._heading;
    }

    public void setHeading(int i) {
        this._heading = i;
    }

    /* access modifiers changed from: protected */
    public int _serchCource(int x, int y) {
        int locCenter = this.courceRange + 1;
        int diff_x = x - locCenter;
        int diff_y = y - locCenter;
        int[] locBace = new int[4];
        locBace[0] = this._npc.getX() - diff_x;
        locBace[1] = this._npc.getY() - diff_y;
        int[] locNext = new int[4];
        int[] dirFront = new int[5];
        boolean[][] serchMap = (boolean[][]) Array.newInstance(Boolean.TYPE, (locCenter * 2) + 1, (locCenter * 2) + 1);
        LinkedList<int[]> queueSerch = new LinkedList<>();
        for (int j = (this.courceRange * 2) + 1; j > 0; j--) {
            for (int i = this.courceRange - Math.abs(locCenter - j); i >= 0; i--) {
                serchMap[j][locCenter + i] = true;
                serchMap[j][locCenter - i] = true;
            }
        }
        int[] firstCource = new int[8];
        firstCource[0] = 2;
        firstCource[1] = 4;
        firstCource[2] = 6;
        firstCource[4] = 1;
        firstCource[5] = 3;
        firstCource[6] = 5;
        firstCource[7] = 7;
        for (int i2 = 0; i2 < 8; i2++) {
            System.arraycopy(locBace, 0, locNext, 0, 4);
            _moveLocation(locNext, firstCource[i2]);
            if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
                return firstCource[i2];
            }
            if (serchMap[locNext[0]][locNext[1]]) {
                int tmpX = locNext[0] + diff_x;
                int tmpY = locNext[1] + diff_y;
                boolean found = false;
                if (i2 == 0) {
                    found = this._npc.getMap().isPassable(tmpX, tmpY + 1, i2, this._npc);
                } else if (i2 == 1) {
                    found = this._npc.getMap().isPassable(tmpX - 1, tmpY + 1, i2, this._npc);
                } else if (i2 == 2) {
                    found = this._npc.getMap().isPassable(tmpX - 1, tmpY, i2, this._npc);
                } else if (i2 == 3) {
                    found = this._npc.getMap().isPassable(tmpX - 1, tmpY - 1, i2, this._npc);
                } else if (i2 == 4) {
                    found = this._npc.getMap().isPassable(tmpX, tmpY - 1, i2, this._npc);
                } else if (i2 == 5) {
                    found = this._npc.getMap().isPassable(tmpX + 1, tmpY - 1, i2, this._npc);
                } else if (i2 == 6) {
                    found = this._npc.getMap().isPassable(tmpX + 1, tmpY, i2, this._npc);
                } else if (i2 == 7) {
                    found = this._npc.getMap().isPassable(tmpX + 1, tmpY + 1, i2, this._npc);
                }
                if (found) {
                    int[] locCopy = new int[4];
                    System.arraycopy(locNext, 0, locCopy, 0, 4);
                    locCopy[2] = firstCource[i2];
                    locCopy[3] = firstCource[i2];
                    queueSerch.add(locCopy);
                }
                serchMap[locNext[0]][locNext[1]] = false;
            }
        }
        while (queueSerch.size() > 0) {
            int[] locBace2 = queueSerch.removeFirst();
            _getFront(dirFront, locBace2[2]);
            for (int i3 = 4; i3 >= 0; i3--) {
                System.arraycopy(locBace2, 0, locNext, 0, 4);
                _moveLocation(locNext, dirFront[i3]);
                if (locNext[0] - locCenter == 0 && locNext[1] - locCenter == 0) {
                    return locNext[3];
                }
                if (serchMap[locNext[0]][locNext[1]]) {
                    int tmpX2 = locNext[0] + diff_x;
                    int tmpY2 = locNext[1] + diff_y;
                    boolean found2 = false;
                    if (i3 == 0) {
                        found2 = this._npc.getMap().isPassable(tmpX2, tmpY2 + 1, i3, this._npc);
                    } else if (i3 == 1) {
                        found2 = this._npc.getMap().isPassable(tmpX2 - 1, tmpY2 + 1, i3, this._npc);
                    } else if (i3 == 2) {
                        found2 = this._npc.getMap().isPassable(tmpX2 - 1, tmpY2, i3, this._npc);
                    } else if (i3 == 3) {
                        found2 = this._npc.getMap().isPassable(tmpX2 - 1, tmpY2 - 1, i3, this._npc);
                    } else if (i3 == 4) {
                        found2 = this._npc.getMap().isPassable(tmpX2, tmpY2 - 1, i3, this._npc);
                    }
                    if (found2) {
                        int[] locCopy2 = new int[4];
                        System.arraycopy(locNext, 0, locCopy2, 0, 4);
                        locCopy2[2] = dirFront[i3];
                        queueSerch.add(locCopy2);
                    }
                    serchMap[locNext[0]][locNext[1]] = false;
                }
            }
        }
        return -1;
    }

    private void _moveLocation(int[] ary, int dir) {
        ary[0] = ary[0] + this.DIR_TABLE[dir][0];
        ary[1] = ary[1] + this.DIR_TABLE[dir][1];
        ary[2] = dir;
    }

    private void _getFront(int[] ary, int d) {
        if (d == 1) {
            ary[4] = 2;
            ary[3] = 0;
            ary[2] = 1;
            ary[1] = 3;
            ary[0] = 7;
        } else if (d == 2) {
            ary[4] = 2;
            ary[3] = 4;
            ary[2] = 0;
            ary[1] = 1;
            ary[0] = 3;
        } else if (d == 3) {
            ary[4] = 2;
            ary[3] = 4;
            ary[2] = 1;
            ary[1] = 3;
            ary[0] = 5;
        } else if (d == 4) {
            ary[4] = 2;
            ary[3] = 4;
            ary[2] = 6;
            ary[1] = 3;
            ary[0] = 5;
        } else if (d == 5) {
            ary[4] = 4;
            ary[3] = 6;
            ary[2] = 3;
            ary[1] = 5;
            ary[0] = 7;
        } else if (d == 6) {
            ary[4] = 4;
            ary[3] = 6;
            ary[2] = 0;
            ary[1] = 5;
            ary[0] = 7;
        } else if (d == 7) {
            ary[4] = 6;
            ary[3] = 0;
            ary[2] = 1;
            ary[1] = 5;
            ary[0] = 7;
        } else if (d == 0) {
            ary[4] = 2;
            ary[3] = 6;
            ary[2] = 0;
            ary[1] = 1;
            ary[0] = 7;
        }
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public int checkObject(int h) {
        if (h >= 0 && h <= 7) {
            int x = this._npc.getX();
            int y = this._npc.getY();
            int h2 = _heading2[h];
            int h3 = _heading3[h];
            if (this._npc.getMap().isPassable(x, y, h, this._npc)) {
                return h;
            }
            if (this._npc.getMap().isPassable(x, y, h2, this._npc)) {
                return h2;
            }
            if (this._npc.getMap().isPassable(x, y, h3, this._npc)) {
                return h3;
            }
        }
        return -1;
    }

    @Override // com.lineage.server.model.Instance.NpcMoveExecutor
    public int openDoor(int h) {
        if (h == -1 || this._npc.getMap().isDoorPassable(this._npc.getX(), this._npc.getY(), h, this._npc)) {
            return h;
        }
        return -1;
    }

    public int get_ErrorCount() {
        return this._ErrorCount;
    }

    public void set_ErrorCount(int i) {
        this._ErrorCount = i;
    }
}
