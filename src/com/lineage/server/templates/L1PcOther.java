package com.lineage.server.templates;

import com.lineage.server.command.GmHtml;
import com.lineage.server.model.Instance.L1ItemInstance;
import java.util.HashMap;
import java.util.Map;

public class L1PcOther {
    public static final String[] ADDNAME = {"あ", "ア", "い", "イ", "う", "ウ", "え", "エ", "お", "オ", "か", "カ", "き", "キ", "く", "ク", "け", "ケ", "こ", "コ", "さ", "サ", "し", "シ", "す", "ス", "せ", "セ", "そ", "ソ", "た", "タ", "ち", "チ", "つ", "ツ", "て", "テ", "と", "ト", "な", "ナ", "に", "ニ", "ぬ", "ヌ", "ね", "ネ", "の", "ノ", "は", "ハ", "ひ", "ヒ", "ふ", "フ", "へ", "ヘ", "ほ", "ホ", "ま", "マ", "み", "ミ", "む", "ム", "め", "メ", "も", "モ", "や", "ヤ", "ゆ", "ユ", "よ", "ヨ", "ら", "ラ", "り", "リ", "る", "ル", "れ", "レ", "ろ", "ロ", "わ", "ワ", "を", "ヲ", "ん", "ン"};
    public static final int CLEVLE0 = 1;
    public static final int CLEVLE1 = 2;
    public static final int CLEVLE10 = 1024;
    public static final int CLEVLE2 = 4;
    public static final int CLEVLE3 = 8;
    public static final int CLEVLE4 = 16;
    public static final int CLEVLE5 = 32;
    public static final int CLEVLE6 = 64;
    public static final int CLEVLE7 = 128;
    public static final int CLEVLE8 = 256;
    public static final int CLEVLE9 = 512;
    private static boolean _isStart = false;
    private static final Map<Integer, StringBuilder> _titleList = new HashMap();
    private int _addhp = 0;
    private int _addmp = 0;
    private int _clanskill = 0;
    private int _color = 1;
    private int _deathCount = 0;
    private GmHtml _gmHtml = null;
    private L1ItemInstance _item = null;
    private int _killCount = 0;
    private int _objid = 0;
    private int _page = 0;
    private int _score = 0;
    private boolean _shopSkill = false;
    private int _usemap = 0;
    private int _usemapTime = 0;

    public static void load() {
        if (!_isStart) {
            _titleList.put(1, new StringBuilder(""));
            _titleList.put(2, new StringBuilder("\\fD"));
            _titleList.put(4, new StringBuilder("\\f="));
            _titleList.put(8, new StringBuilder("\\fH"));
            _titleList.put(16, new StringBuilder("\\f_"));
            _titleList.put(32, new StringBuilder("\\f2"));
            _titleList.put(64, new StringBuilder("\\fF"));
            _titleList.put(128, new StringBuilder("\\fT"));
            _titleList.put(256, new StringBuilder("\\fE"));
            _titleList.put(512, new StringBuilder("\\f0"));
            _titleList.put(1024, new StringBuilder("\\f?"));
            _isStart = true;
        }
    }

    public void set_item(L1ItemInstance item) {
        this._item = item;
    }

    public L1ItemInstance get_item() {
        return this._item;
    }

    public void set_shopSkill(boolean shopSkill) {
        this._shopSkill = shopSkill;
    }

    public boolean get_shopSkill() {
        return this._shopSkill;
    }

    public void set_objid(int objid) {
        this._objid = objid;
    }

    public int get_objid() {
        return this._objid;
    }

    public void set_page(int page) {
        this._page = page;
    }

    public int get_page() {
        return this._page;
    }

    public void set_usemap(int usemap) {
        this._usemap = usemap;
    }

    public int get_usemap() {
        return this._usemap;
    }

    public void set_usemapTime(int usemapTime) {
        this._usemapTime = usemapTime;
    }

    public int get_usemapTime() {
        return this._usemapTime;
    }

    public void set_addhp(int addhp) {
        if (addhp < 0) {
            addhp = 0;
        }
        this._addhp = addhp;
    }

    public int get_addhp() {
        return this._addhp;
    }

    public void set_addmp(int addmp) {
        if (addmp < 0) {
            addmp = 0;
        }
        this._addmp = addmp;
    }

    public int get_addmp() {
        return this._addmp;
    }

    public void set_score(int score) {
        if (score < 0) {
            score = 0;
        }
        this._score = score;
    }

    public void add_score(int score) {
        if (score < 0) {
            score = 0;
        }
        this._score += score;
        if (this._score < 0) {
            this._score = 0;
        }
    }

    public int get_score() {
        return this._score;
    }

    public void set_color(int color) {
        this._color = color;
    }

    public boolean set_color(int color, int tg) {
        set_score(tg);
        this._color = color;
        return true;
    }

    public int get_color() {
        return this._color;
    }

    public String color() {
        StringBuilder stringBuilder = _titleList.get(Integer.valueOf(this._color));
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return "";
    }

    public void set_clanskill(int clanskill) {
        this._clanskill = clanskill;
    }

    public int get_clanskill() {
        return this._clanskill;
    }

    public void add_killCount(int i) {
        this._killCount += i;
    }

    public void set_killCount(int i) {
        this._killCount = i;
    }

    public int get_killCount() {
        return this._killCount;
    }

    public void add_deathCount(int i) {
        this._deathCount += i;
    }

    public void set_deathCount(int i) {
        this._deathCount = i;
    }

    public int get_deathCount() {
        return this._deathCount;
    }

    public GmHtml get_gmHtml() {
        return this._gmHtml;
    }

    public void set_gmHtml(GmHtml gmHtml) {
        this._gmHtml = gmHtml;
    }
}
