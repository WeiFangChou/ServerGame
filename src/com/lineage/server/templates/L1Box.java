package com.lineage.server.templates;

import com.lineage.server.model.Instance.L1PcInstance;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1Box {
    private static final int _int1 = 1;
    private static final int _int2 = 2;
    private static final int _int3 = 4;
    private static final int _int4 = 8;
    private static final int _int5 = 16;
    private static final int _int6 = 32;
    private static final int _int7 = 64;
    private static final Log _log = LogFactory.getLog(L1Box.class);
    private int _box_item_id;
    private int _get_item_id;
    private boolean _isCrown;
    private boolean _isDarkelf;
    private boolean _isDragonKnight;
    private boolean _isElf;
    private boolean _isIllusionist;
    private boolean _isKnight;
    private boolean _isWizard;
    private int _max_count;
    private int _min_count;
    private int _out;
    private int _random;
    private int _remain_time;
    private int _use_type = 127;

    public int get_box_item_id() {
        return this._box_item_id;
    }

    public void set_box_item_id(int box_item_id) {
        this._box_item_id = box_item_id;
    }

    public int get_item_id() {
        return this._get_item_id;
    }

    public void set_get_item_id(int get_item_id) {
        this._get_item_id = get_item_id;
    }

    public int get_random() {
        return this._random;
    }

    public void set_random(int random) {
        this._random = random;
    }

    public int get_min_count() {
        return this._min_count;
    }

    public void set_min_count(int min_count) {
        this._min_count = min_count;
    }

    public int get_max_count() {
        return this._max_count;
    }

    public void set_max_count(int max_count) {
        this._max_count = max_count;
    }

    public int is_out() {
        return this._out;
    }

    public void set_out(int out) {
        this._out = out;
    }

    public int get_remain_time() {
        return this._remain_time;
    }

    public void set_remain_time(int remain_time) {
        this._remain_time = remain_time;
    }

    public int get_use_type() {
        return this._use_type;
    }

    public void set_use_type(int use_type) {
        this._use_type = use_type;
        if (use_type >= 64) {
            use_type -= 64;
            this._isIllusionist = true;
        }
        if (use_type >= 32) {
            use_type -= 32;
            this._isDragonKnight = true;
        }
        if (use_type >= 16) {
            use_type -= 16;
            this._isDarkelf = true;
        }
        if (use_type >= 8) {
            use_type -= 8;
            this._isWizard = true;
        }
        if (use_type >= 4) {
            use_type -= 4;
            this._isElf = true;
        }
        if (use_type >= 2) {
            use_type -= 2;
            this._isKnight = true;
        }
        if (use_type >= 1) {
            use_type--;
            this._isCrown = true;
        }
        if (use_type > 0) {
            _log.error("寶箱可用職業設定錯誤:餘數大於0 編號:" + this._box_item_id + "/" + this._get_item_id);
        }
    }

    public boolean is_use(L1PcInstance pc) {
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
            return false;
        }
    }
}
