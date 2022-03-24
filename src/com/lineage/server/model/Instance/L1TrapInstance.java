package com.lineage.server.model.Instance;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_Trap;
import com.lineage.server.templates.L1Trap;
import com.lineage.server.types.Point;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1TrapInstance extends L1Object {
    private static final Log _log = LogFactory.getLog(L1TowerInstance.class);
    private static final Random _random = new Random();
    private static final long serialVersionUID = 1;
    private final Point _baseLoc = new Point();
    private boolean _isEnable = true;
    private List<L1PcInstance> _knownPlayers = new CopyOnWriteArrayList();
    private final Point _rndPt = new Point();
    private int _span;
    private int _stop = 0;
    private final L1Trap _trap;

    public L1TrapInstance(int id, L1Trap trap, L1Location loc, Point rndPt, int span) {
        setId(id);
        this._trap = trap;
        getLocation().set(loc);
        this._baseLoc.set(loc);
        this._rndPt.set(rndPt);
        if (span > 0) {
            this._span = span / L1SkillId.STATUS_BRAVE;
        }
        resetLocation();
    }

    public L1Trap get_trap() {
        return this._trap;
    }

    public void set_stop(int _stop2) {
        this._stop = _stop2;
    }

    public int get_stop() {
        return this._stop;
    }

    public void resetLocation() {
        try {
            if (!(this._rndPt.getX() == 0 && this._rndPt.getY() == 0)) {
                enableTrap();
                for (int i = 0; i < 50; i++) {
                    int rndX = _random.nextInt(this._rndPt.getX() + 1) * (_random.nextBoolean() ? 1 : -1);
                    int nextInt = _random.nextInt(this._rndPt.getY() + 1);
                    int i2 = _random.nextBoolean() ? 1 : -1;
                    int rndX2 = rndX + this._baseLoc.getX();
                    int rndY = (nextInt * i2) + this._baseLoc.getY();
                    L1Map map = getLocation().getMap();
                    if (map.isInMap(rndX2, rndY) && map.isPassable(rndX2, rndY, (L1Character) null)) {
                        getLocation().set(rndX2, rndY);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getSpan() {
        return this._span;
    }

    public void enableTrap() {
        set_stop(0);
        this._isEnable = true;
    }

    public void disableTrap() {
        this._isEnable = false;
        for (L1PcInstance pc : this._knownPlayers) {
            pc.removeKnownObject(this);
            pc.sendPackets(new S_RemoveObject(this));
        }
        this._knownPlayers.clear();
    }

    public boolean isEnable() {
        return this._isEnable;
    }

    public void onTrod(L1PcInstance trodFrom) {
        this._trap.onTrod(trodFrom, this);
    }

    public void onDetection(L1PcInstance caster) {
        this._trap.onDetection(caster, this);
    }

    @Override // com.lineage.server.model.L1Object
    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.hasSkillEffect(2002)) {
                perceivedFrom.addKnownObject(this);
                perceivedFrom.sendPackets(new S_Trap(this, this._trap.getType()));
                this._knownPlayers.add(perceivedFrom);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
