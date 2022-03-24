package com.lineage.server.model.Instance;

import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1BowInstance extends L1NpcInstance {
    private static final long serialVersionUID = 1L;
    private static final Log _log = LogFactory.getLog(L1BowInstance.class);
    private int _bowid = 66;
    private int _time = 1000;
    private int _dmg = 15;
    private int _out_x = 0;
    private int _out_y = 0;
    private boolean _start = true;

    public L1BowInstance(L1Npc template) {
        super(template);
    }

    public void set_info(int bowid, int h, int dmg, int time) {
        this._bowid = bowid;
        this._dmg = dmg;
        this._time = time;
    }

    public int get_dmg() {
        return this._dmg;
    }

    public void set_dmg(int dmg) {
        this._dmg = dmg;
    }

    public int get_time() {
        return this._time;
    }

    public void set_time(int time) {
        this._time = time;
    }

    public int get_bowid() {
        return this._bowid;
    }

    public void set_bowid(int bowid) {
        this._bowid = bowid;
    }

    public boolean get_start() {
        return this._start;
    }

    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (this._out_x == 0 && this._out_y == 0) {
                this.set_atkLoc();
            }

            if (!this._start) {
                this._start = true;
            }
        } catch (Exception var3) {
            _log.error(var3.getLocalizedMessage(), var3);
        }

    }

    public void deleteMe() {
        try {
            this._destroyed = true;
            World.get().removeVisibleObject(this);
            World.get().removeObject(this);
            Iterator var2 = World.get().getRecognizePlayer(this).iterator();

            while(var2.hasNext()) {
                L1PcInstance pc = (L1PcInstance)var2.next();
                pc.removeKnownObject(this);
                pc.sendPackets(new S_RemoveObject(this));
            }

            this.removeAllKnownObjects();
        } catch (Exception var3) {
            _log.error(var3.getLocalizedMessage(), var3);
        }

    }

    public void atkTrag() {
        try {
            int out_x = this._out_x;
            int out_y = this._out_y;
            int tgid = 0;
            L1Character tg = this.checkTg();
            if (tg != null) {
                tgid = tg.getId();
                switch(this.getHeading()) {
                    case 0:
                        out_y = tg.getY();
                    case 1:
                    case 3:
                    case 5:
                    default:
                        break;
                    case 2:
                        out_x = tg.getX();
                        break;
                    case 4:
                        out_y = tg.getY();
                        break;
                    case 6:
                        out_x = tg.getX();
                }

                if (tg instanceof L1PcInstance) {
                    L1PcInstance trag = (L1PcInstance)tg;
                    trag.receiveDamage((L1Character)null, (double)this._dmg, false, true);
                } else if (tg instanceof L1PetInstance) {
                    L1PetInstance trag = (L1PetInstance)tg;
                    trag.receiveDamage((L1Character)null, this._dmg);
                } else if (tg instanceof L1SummonInstance) {
                    L1SummonInstance trag = (L1SummonInstance)tg;
                    trag.receiveDamage((L1Character)null, this._dmg);
                } else if (tg instanceof L1MonsterInstance) {
                    L1MonsterInstance trag = (L1MonsterInstance)tg;
                    trag.receiveDamage((L1Character)null, this._dmg);
                }
            }

            this.broadcastPacketAll(new S_UseArrowSkill(this, tgid, this._bowid, out_x, out_y, this._dmg));
        } catch (Exception var6) {
            _log.error(var6.getLocalizedMessage(), var6);
        }

    }

    public boolean checkPc() {
        try {
            if (World.get().getRecognizePlayer(this).size() <= 0) {
                this._start = false;
                return false;
            }
        } catch (Exception var2) {
            _log.error(var2.getLocalizedMessage(), var2);
        }

        return true;
    }

    private L1Character checkTg() {
        ArrayList<L1Object> tgs = World.get().getVisibleObjects(this, -1);
        Iterator var3 = tgs.iterator();

        while(var3.hasNext()) {
            L1Object object = (L1Object)var3.next();
            if (object instanceof L1Character) {
                L1Character cha = (L1Character)object;
                boolean isCheck = false;
                if (cha instanceof L1PcInstance) {
                    isCheck = true;
                } else if (cha instanceof L1PetInstance) {
                    isCheck = true;
                } else if (cha instanceof L1SummonInstance) {
                    isCheck = true;
                } else if (cha instanceof L1MonsterInstance) {
                    isCheck = true;
                }

                if (isCheck) {
                    switch(this.getHeading()) {
                        case 0:
                            if (object.getX() == this.getX() && object.getY() <= this.getY() && object.getY() >= this._out_y) {
                                return cha;
                            }
                        case 1:
                        case 3:
                        case 5:
                        default:
                            break;
                        case 2:
                            if (object.getX() >= this.getX() && object.getX() <= this._out_x && object.getY() == this.getY()) {
                                return cha;
                            }
                            break;
                        case 4:
                            if (object.getX() == this.getX() && object.getY() >= this.getY() && object.getY() <= this._out_y) {
                                return cha;
                            }
                            break;
                        case 6:
                            if (object.getX() <= this.getX() && object.getX() >= this._out_x && object.getY() == this.getY()) {
                                return cha;
                            }
                    }
                }
            }
        }

        return null;
    }

    private void set_atkLoc() {
        try {
            boolean test;
            int x;
            int y;
            test = true;
            x = this.getX();
            y = this.getY();
            int gab;
            label49:
            switch(this.getHeading()) {
                case 0:
                    while(test) {
                        gab = this.getMap().getOriginalTile(x, y--);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                case 1:
                case 3:
                case 5:
                default:
                    break;
                case 2:
                    while(true) {
                        if (!test) {
                            break label49;
                        }

                        gab = this.getMap().getOriginalTile(x++, y);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                case 4:
                    while(true) {
                        if (!test) {
                            break label49;
                        }

                        gab = this.getMap().getOriginalTile(x, y++);
                        if (gab == 0) {
                            test = false;
                        }
                    }
                case 6:
                    while(test) {
                        gab = this.getMap().getOriginalTile(x--, y);
                        if (gab == 0) {
                            test = false;
                        }
                    }
            }

            if (!test) {
                this._out_x = x;
                this._out_y = y;
            }
        } catch (Exception var5) {
            _log.error(var5.getLocalizedMessage(), var5);
        }

    }
}
