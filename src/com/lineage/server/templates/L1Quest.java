package com.lineage.server.templates;

import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Quest {
    private static final int _int1 = 1;
    private static final int _int2 = 2;
    private static final int _int3 = 4;
    private static final int _int4 = 8;
    private static final int _int5 = 16;
    private static final int _int6 = 32;
    private static final int _int7 = 64;
    private static final Log _log = LogFactory.getLog(L1Quest.class);
    private boolean _del;
    private int _difficulty;
    private int _id;
    private boolean _isCrown;
    private boolean _isDarkelf;
    private boolean _isDragonKnight;
    private boolean _isElf;
    private boolean _isIllusionist;
    private boolean _isKnight;
    private boolean _isWizard;
    private String _note;
    private String _questclass;
    private int _questlevel;
    private String _questname;
    private boolean _queststart;

    public int get_id() {
        return this._id;
    }

    public void set_id(int _id2) {
        this._id = _id2;
    }

    public String get_questname() {
        return this._questname;
    }

    public void set_questname(String _questname2) {
        this._questname = _questname2;
    }

    public String get_questclass() {
        return this._questclass;
    }

    public void set_questclass(String _questclass2) {
        this._questclass = _questclass2;
    }

    public boolean is_queststart() {
        return this._queststart;
    }

    public void set_queststart(boolean _queststart2) {
        this._queststart = _queststart2;
    }

    public int get_questlevel() {
        return this._questlevel;
    }

    public void set_questlevel(int _questlevel2) {
        this._questlevel = _questlevel2;
    }

    public void set_difficulty(int _difficulty2) {
        this._difficulty = _difficulty2;
    }

    public int get_difficulty() {
        return this._difficulty;
    }

    public void set_note(String _note2) {
        this._note = _note2;
    }

    public String get_note() {
        return this._note;
    }

    public void set_questuser(int questuser) {
        if (questuser >= 64) {
            questuser -= 64;
            try {
                this._isIllusionist = true;
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                return;
            }
        }
        if (questuser >= 32) {
            questuser -= 32;
            this._isDragonKnight = true;
        }
        if (questuser >= 16) {
            questuser -= 16;
            this._isDarkelf = true;
        }
        if (questuser >= 8) {
            questuser -= 8;
            this._isWizard = true;
        }
        if (questuser >= 4) {
            questuser -= 4;
            this._isElf = true;
        }
        if (questuser >= 2) {
            questuser -= 2;
            this._isKnight = true;
        }
        if (questuser >= 1) {
            questuser--;
            this._isCrown = true;
        }
        if (questuser > 0) {
            _log.error("任務可執行職業設定錯誤:餘數大於0 編號:" + this._id);
        }
    }

    public boolean check(L1PcInstance pc) {
        try {
            if (pc.isCrown() && this._isCrown) {
                return true;
            }
            if (pc.isKnight() && this._isKnight) {
                return true;
            }
            if (pc.isElf() && this._isElf) {
                return true;
            }
            if (pc.isWizard() && this._isWizard) {
                return true;
            }
            if (pc.isDarkelf() && this._isDarkelf) {
                return true;
            }
            if (pc.isDragonKnight() && this._isDragonKnight) {
                return true;
            }
            if (!pc.isIllusionist() || !this._isIllusionist) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return  false;
        }
    }

    public void set_del(boolean del) {
        this._del = del;
    }

    public boolean is_del() {
        return this._del;
    }
}
