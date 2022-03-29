package com.lineage.data.event.gambling;

import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.types.Point;
import com.lineage.server.utils.L1SpawnUtil;
import java.lang.reflect.Array;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GamblingNpc implements Runnable {
    private static final byte[] HEADING_TABLE_X;
    private static final byte[] HEADING_TABLE_Y;
    private static final Log _log = LogFactory.getLog(GamblingNpc.class);

    /* renamed from: xx */
    private static final int[][] f3xx = ((int[][]) Array.newInstance(Integer.TYPE, 5, 1));
    private long _adena;
    private Gambling _gambling;
    private boolean _isOver = false;
    private Point _loc;
    private L1NpcInstance _npc;
    private Random _random = new Random();
    private double _rate;
    private int _sId = 1;
    private Point[] _tgLoc;
    private int _xId;

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

    public GamblingNpc(Gambling gambling) {
        this._gambling = gambling;
    }

    public Gambling get_gambling() {
        return this._gambling;
    }

    public void set_rate(double rate) {
        this._rate = rate;
    }

    public double get_rate() {
        return this._rate;
    }

    public void add_adena(long adena) {
        this._adena += adena;
    }

    public long get_adena() {
        return this._adena;
    }

    public void delNpc() {
        this._npc.deleteMe();
    }

    public L1NpcInstance get_npc() {
        return this._npc;
    }

    public int get_xId() {
        return this._xId;
    }

    public void showNpc(int npcid, int i) {
        int[] gfxids;
        this._xId = i;
        if (NpcTable.get().getTemplate(npcid) != null) {
            this._tgLoc = GamblingConfig.TGLOC[i];
            int x = this._tgLoc[0].getX();
            int y = this._tgLoc[0].getY();
            if (GamblingConfig.ISGFX) {
                gfxids = GamblingConfig.GFX[i];
            } else {
                gfxids = GamblingConfig.GFXD[i];
            }
            this._npc = L1SpawnUtil.spawn(npcid, x, y,  4, 6, gfxids[this._random.nextInt(gfxids.length)]);
        }
    }

    public void getStart() {
        GeneralThreadPool.get().schedule(this, 10);
    }

    public void run() {
        try {
            this._loc = this._tgLoc[1];
            while (!this._isOver) {
                int _randomTime;
                if (this._xId == this._gambling.WIN) {
                    _randomTime = 150;
                } else {
                    _randomTime = 190;
                }
                int ss = 190;
                if (this._random.nextInt(100) < 25) {
                    ss = 150;
                }
                Thread.sleep((long) (ss + this._random.nextInt(_randomTime)));
                actionStart();
            }
        } catch (InterruptedException e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void setDirectionMove(int heading) {
        if (heading >= 0) {
            int locx = this._npc.getX();
            int locy = this._npc.getY();
            int locx2 = locx + HEADING_TABLE_X[heading];
            int locy2 = locy + HEADING_TABLE_Y[heading];
            this._npc.setHeading(heading);
            this._npc.setX(locx2);
            this._npc.setY(locy2);
            this._npc.broadcastPacketAll(new S_MoveCharPacket(this._npc));
        }
    }

    private void actionStart() {
        int x = this._loc.getX();
        int y = this._loc.getY();
        try {
            setDirectionMove(this._npc.targetDirection(x, y));
            if (this._npc.getLocation().getTileLineDistance(this._loc) < 2) {
                this._loc = this._tgLoc[this._sId];
                this._sId++;
            }
        } catch (Exception e) {
            if (this._gambling.get_oneNpc() == null) {
                this._gambling.set_oneNpc(this);
                int[] iArr = f3xx[this._xId];
                iArr[0] = iArr[0] + 1;
            }
            setDirectionMove(this._npc.targetDirection(x, y));
            this._isOver = true;
        }
    }
}
