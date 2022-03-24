package william.Poly;

import com.lineage.DatabaseFactory;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.utils.SQLUtil;
import com.lineage.server.utils.collections.Maps;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NpcPower {
    private static NpcPower _instance;
    private static final Log _log = LogFactory.getLog(NpcPower.class);
    private static final Map<Integer, NpcPower> _npcpower = Maps.newHashMap();
    private int _addac;
    private int _addblind;
    private int _addbowdmg;
    private int _addbowhit;
    private int _addcha;
    private int _addcon;
    private int _adddex;
    private int _adddmg;
    private int _adddodge;
    private int _addearth;
    private int _addfire;
    private int _addfreeze;
    private int _addgiftcount;
    private int _addgiftid;
    private int _addhit;
    private int _addhp;
    private int _addhpr;
    private int _addint;
    private int _addmp;
    private int _addmpr;
    private int _addmr;
    private int _addreduction;
    private int _addsleep;
    private int _addsp;
    private int _addstone;
    private int _addstr;
    private int _addsturn;
    private int _addsustain;
    private int _addwater;
    private int _addwind;
    private int _addwis;
    private String _enchant;
    private int _id;
    private String _itemid;
    private String _note;
    private int _quest;

    public static NpcPower get() {
        if (_instance == null) {
            _instance = new NpcPower();
        }
        return _instance;
    }

    public int getQuest() {
        return this._quest;
    }

    public void setQuest(int quest) {
        this._quest = quest;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getPItemId() {
        return this._itemid;
    }

    public void setPItemId(String itemid) {
        this._itemid = itemid;
    }

    public String getNote() {
        return this._note;
    }

    public void setNote(String note) {
        this._note = note;
    }

    public String getPEnchant() {
        return this._enchant;
    }

    public void setPEnchant(String enchant) {
        this._enchant = enchant;
    }

    public int getPHp() {
        return this._addhp;
    }

    public void setPHp(int hp) {
        this._addhp = hp;
    }

    public int getPMp() {
        return this._addmp;
    }

    public void setPMp(int mp) {
        this._addmp = mp;
    }

    public int getPHpr() {
        return this._addhpr;
    }

    public void setPHpr(int hpr) {
        this._addhpr = hpr;
    }

    public int getPMpr() {
        return this._addmpr;
    }

    public void setPMpr(int mpr) {
        this._addmpr = mpr;
    }

    public int getPDmg() {
        return this._adddmg;
    }

    public void setPDmg(int dmg) {
        this._adddmg = dmg;
    }

    public int getPBowDmg() {
        return this._addbowdmg;
    }

    public void setPBowDmg(int bowdmg) {
        this._addbowdmg = bowdmg;
    }

    public int getPHit() {
        return this._addhit;
    }

    public void setPHit(int hit) {
        this._addhit = hit;
    }

    public int getPBowHit() {
        return this._addbowhit;
    }

    public void setPBowHit(int bowhit) {
        this._addbowhit = bowhit;
    }

    public int getPAc() {
        return this._addac;
    }

    public void setPAc(int ac) {
        this._addac = ac;
    }

    public int getPReduction() {
        return this._addreduction;
    }

    public void setPReduction(int reduction) {
        this._addreduction = reduction;
    }

    public int getPDodge() {
        return this._adddodge;
    }

    public void setPDodge(int dodge) {
        this._adddodge = dodge;
    }

    public int getPSp() {
        return this._addsp;
    }

    public void setPSp(int sp) {
        this._addsp = sp;
    }

    public int getPMr() {
        return this._addmr;
    }

    public void setPMr(int mr) {
        this._addmr = mr;
    }

    public int getPStr() {
        return this._addstr;
    }

    public void setPStr(int str) {
        this._addstr = str;
    }

    public int getPDex() {
        return this._adddex;
    }

    public void setPDex(int dex) {
        this._adddex = dex;
    }

    public int getPCon() {
        return this._addcon;
    }

    public void setPCon(int con) {
        this._addcon = con;
    }

    public int getPInt() {
        return this._addint;
    }

    public void setPInt(int Int) {
        this._addint = Int;
    }

    public int getPWis() {
        return this._addwis;
    }

    public void setPWis(int wis) {
        this._addwis = wis;
    }

    public int getPCha() {
        return this._addcha;
    }

    public void setPCha(int cha) {
        this._addcha = cha;
    }

    public int getPFire() {
        return this._addfire;
    }

    public void setPFire(int fire) {
        this._addfire = fire;
    }

    public int getPWind() {
        return this._addwind;
    }

    public void setPWind(int wind) {
        this._addwind = wind;
    }

    public int getPEarth() {
        return this._addearth;
    }

    public void setPEarth(int earth) {
        this._addearth = earth;
    }

    public int getPWater() {
        return this._addwater;
    }

    public void setPWater(int water) {
        this._addwater = water;
    }

    public int getPFreeze() {
        return this._addfreeze;
    }

    public void setPFreeze(int freeze) {
        this._addfreeze = freeze;
    }

    public int getPSturn() {
        return this._addsturn;
    }

    public void setPSturn(int sturn) {
        this._addsturn = sturn;
    }

    public int getPStone() {
        return this._addstone;
    }

    public void setPStone(int stone) {
        this._addstone = stone;
    }

    public int getPSleep() {
        return this._addsleep;
    }

    public void setPSleep(int sleep) {
        this._addsleep = sleep;
    }

    public int getPSustain() {
        return this._addsustain;
    }

    public void setPSustain(int sustain) {
        this._addsustain = sustain;
    }

    public int getPGiftId() {
        return this._addgiftid;
    }

    public void setPGiftId(int gift) {
        this._addgiftid = gift;
    }

    public int getPBlind() {
        return this._addblind;
    }

    public void setPBlind(int blind) {
        this._addblind = blind;
    }

    public int getPGiftCount() {
        return this._addgiftcount;
    }

    public void setPGiftCount(int count) {
        this._addgiftcount = count;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `道具兌換能力系統`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String note = rs.getString("note");
                int quest = rs.getInt("quest");
                int hp = rs.getInt("AddHp");
                int mp = rs.getInt("AddMp");
                int hpr = rs.getInt("AddHpr");
                int mpr = rs.getInt("AddMpr");
                int dmg = rs.getInt("AddDmg");
                int bowdmg = rs.getInt("AddBowDmg");
                int hit = rs.getInt("AddHit");
                int bowhit = rs.getInt("AddBowHit");
                int ac = rs.getInt("AddAc");
                int reduction = rs.getInt("AddReduction");
                int dodge = rs.getInt("AddDodge");
                int sp = rs.getInt("AddSp");
                int mr = rs.getInt("AddMr");
                int str = rs.getInt("AddStr");
                int dex = rs.getInt("AddDex");
                int con = rs.getInt("AddCon");
                int Int = rs.getInt("AddInt");
                int wis = rs.getInt("AddWis");
                int cha = rs.getInt("AddCha");
                int fire = rs.getInt("AddFire");
                int wind = rs.getInt("AddWind");
                int earth = rs.getInt("AddEarth");
                int water = rs.getInt("AddWater");
                int freeze = rs.getInt("AddFreeze");
                int sturn = rs.getInt("AddSturn");
                int stone = rs.getInt("AddStone");
                int sleep = rs.getInt("AddSleep");
                int sustain = rs.getInt("AddSustain");
                int blind = rs.getInt("AddBlind");
                int giftid = rs.getInt("GiftId");
                int giftc = rs.getInt("GiftCount");
                NpcPower power = new NpcPower();
                power.setId(id);
                power.setPItemId(rs.getString("ItemId").replaceAll(" ", ""));
                power.setPEnchant(rs.getString("Enchant").replaceAll(" ", ""));
                power.setNote(note);
                power.setQuest(quest);
                power.setPHp(hp);
                power.setPMp(mp);
                power.setPHpr(hpr);
                power.setPMpr(mpr);
                power.setPDmg(dmg);
                power.setPBowDmg(bowdmg);
                power.setPHit(hit);
                power.setPBowHit(bowhit);
                power.setPAc(ac);
                power.setPReduction(reduction);
                power.setPDodge(dodge);
                power.setPSp(sp);
                power.setPMr(mr);
                power.setPStr(str);
                power.setPDex(dex);
                power.setPCon(con);
                power.setPInt(Int);
                power.setPWis(wis);
                power.setPCha(cha);
                power.setPFire(fire);
                power.setPWind(wind);
                power.setPEarth(earth);
                power.setPWater(water);
                power.setPFreeze(freeze);
                power.setPSturn(sturn);
                power.setPStone(stone);
                power.setPSleep(sleep);
                power.setPSustain(sustain);
                power.setPBlind(blind);
                power.setPGiftId(giftid);
                power.setPGiftCount(giftc);
                _npcpower.put(Integer.valueOf(id), power);
                i++;
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入道具轉換能力系統: " + i + "(" + timer.get() + "ms)");
    }

    public int NpcPowerSize() {
        return _npcpower.size();
    }

    public NpcPower getPower(int id) {
        return _npcpower.get(Integer.valueOf(id));
    }
}
