package william.Poly;

import com.lineage.DatabaseFactory;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_SPMR;
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

public class Npc_PowerLog {
    private static Npc_PowerLog _instance;
    private static final Log _log = LogFactory.getLog(Npc_PowerLog.class);
    private static final Map<Integer, Npc_PowerLog> _powerMap = Maps.newHashMap();
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
    private int _id;

    public static Npc_PowerLog get() {
        if (_instance == null) {
            _instance = new Npc_PowerLog();
        }
        return _instance;
    }

    public int getId() {
        return this._id;
    }

    public void setId(int id) {
        this._id = id;
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

    public int getPBlind() {
        return this._addblind;
    }

    public void setPBlind(int blind) {
        this._addblind = blind;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("SELECT * FROM `道具兌換能力系統log`");
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("CharId");
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
                Npc_PowerLog power = new Npc_PowerLog();
                power.setId(id);
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
                _powerMap.put(Integer.valueOf(id), power);
                i++;
            }
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
        _log.info("載入道具轉換能力系統紀錄: " + _powerMap.size() + "(" + timer.get() + "ms)");
    }

    public void storeOther(int objId, NpcPower NP) {
        if (_powerMap.get(Integer.valueOf(objId)) == null) {
            addNewOther(objId, NP);
            Npc_PowerLog power = new Npc_PowerLog();
            power.setId(objId);
            power.setPHp(NP.getPHp());
            power.setPMp(NP.getPMp());
            power.setPHpr(NP.getPHpr());
            power.setPMpr(NP.getPMpr());
            power.setPDmg(NP.getPDmg());
            power.setPBowDmg(NP.getPBowDmg());
            power.setPHit(NP.getPHit());
            power.setPBowHit(NP.getPBowHit());
            power.setPAc(NP.getPAc());
            power.setPReduction(NP.getPReduction());
            power.setPDodge(NP.getPDodge());
            power.setPSp(NP.getPSp());
            power.setPMr(NP.getPMr());
            power.setPStr(NP.getPStr());
            power.setPDex(NP.getPDex());
            power.setPCon(NP.getPCon());
            power.setPInt(NP.getPInt());
            power.setPWis(NP.getPWis());
            power.setPCha(NP.getPCha());
            power.setPFire(NP.getPFire());
            power.setPWind(NP.getPWind());
            power.setPEarth(NP.getPEarth());
            power.setPWater(NP.getPWater());
            power.setPFreeze(NP.getPFreeze());
            power.setPSturn(NP.getPSturn());
            power.setPStone(NP.getPStone());
            power.setPSleep(NP.getPSleep());
            power.setPSustain(NP.getPSustain());
            power.setPBlind(NP.getPBlind());
            _powerMap.put(Integer.valueOf(objId), power);
            return;
        }
        updateOther(objId, NP);
    }

    private void addNewOther(int objId, NpcPower NP) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("INSERT INTO `道具兌換能力系統log` SET `CharId`=?,`AddHp`=?,`AddMp`=?,`AddHpr`=?,`AddMpr`=?,`AddDmg`=?,`AddBowDmg`=?,`AddHit`=?,`AddBowHit`=?,`AddAc`=?,`AddReduction`=?,`AddDodge`=?,`AddSp`=?,`AddMr`=?,`AddStr`=?,`AddDex`=?,`AddCon`=?,`AddInt`=?,`AddWis`=?,`AddCha`=?,`AddFire`=?,`AddWind`=?,`AddEarth`=?,`AddWater`=?,`AddFreeze`=?,`AddSturn`=?,`AddStone`=?,`AddSleep`=?,`AddSustain`=?,`Addblind`=?");
            int i = 0 + 1;
            ps.setInt(i, objId);
            int i2 = i + 1;
            ps.setInt(i2, NP.getPHp());
            int i3 = i2 + 1;
            ps.setInt(i3, NP.getPMp());
            int i4 = i3 + 1;
            ps.setInt(i4, NP.getPHpr());
            int i5 = i4 + 1;
            ps.setInt(i5, NP.getPMpr());
            int i6 = i5 + 1;
            ps.setInt(i6, NP.getPDmg());
            int i7 = i6 + 1;
            ps.setInt(i7, NP.getPBowDmg());
            int i8 = i7 + 1;
            ps.setInt(i8, NP.getPHit());
            int i9 = i8 + 1;
            ps.setInt(i9, NP.getPBowHit());
            int i10 = i9 + 1;
            ps.setInt(i10, NP.getPAc());
            int i11 = i10 + 1;
            ps.setInt(i11, NP.getPReduction());
            int i12 = i11 + 1;
            ps.setInt(i12, NP.getPDodge());
            int i13 = i12 + 1;
            ps.setInt(i13, NP.getPSp());
            int i14 = i13 + 1;
            ps.setInt(i14, NP.getPMr());
            int i15 = i14 + 1;
            ps.setInt(i15, NP.getPStr());
            int i16 = i15 + 1;
            ps.setInt(i16, NP.getPDex());
            int i17 = i16 + 1;
            ps.setInt(i17, NP.getPCon());
            int i18 = i17 + 1;
            ps.setInt(i18, NP.getPInt());
            int i19 = i18 + 1;
            ps.setInt(i19, NP.getPWis());
            int i20 = i19 + 1;
            ps.setInt(i20, NP.getPCha());
            int i21 = i20 + 1;
            ps.setInt(i21, NP.getPFire());
            int i22 = i21 + 1;
            ps.setInt(i22, NP.getPWind());
            int i23 = i22 + 1;
            ps.setInt(i23, NP.getPEarth());
            int i24 = i23 + 1;
            ps.setInt(i24, NP.getPWater());
            int i25 = i24 + 1;
            ps.setInt(i25, NP.getPFreeze());
            int i26 = i25 + 1;
            ps.setInt(i26, NP.getPSturn());
            int i27 = i26 + 1;
            ps.setInt(i27, NP.getPStone());
            int i28 = i27 + 1;
            ps.setInt(i28, NP.getPSleep());
            int i29 = i28 + 1;
            ps.setInt(i29, NP.getPSustain());
            ps.setInt(i29 + 1, NP.getPBlind());
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    private void updateOther(int objid, NpcPower NP) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            Npc_PowerLog value = _powerMap.get(Integer.valueOf(objid));
            int hp = value.getPHp();
            int mp = value.getPMp();
            int hpr = value.getPHpr();
            int mpr = value.getPMpr();
            int d = value.getPDmg();
            int bd = value.getPBowDmg();
            int h = value.getPHit();
            int bh = value.getPBowHit();
            int ac = value.getPAc();
            int rd = value.getPReduction();
            int dg = value.getPDodge();
            int sp = value.getPSp();
            int mr = value.getPMr();
            int s = value.getPStr();
            int de = value.getPDex();
            int co = value.getPCon();
            int in = value.getPInt();
            int wi = value.getPWis();
            int ch = value.getPCha();
            int fi = value.getPFire();
            int win = value.getPWind();
            int ea = value.getPEarth();
            int wa = value.getPWater();
            int fr = value.getPFreeze();
            int st = value.getPSturn();
            int so = value.getPStone();
            int sl = value.getPSleep();
            int su = value.getPSustain();
            int bl = value.getPBlind();
            Npc_PowerLog power = new Npc_PowerLog();
            power.setId(objid);
            power.setPHp(NP.getPHp() + hp);
            power.setPMp(NP.getPMp() + mp);
            power.setPHpr(NP.getPHpr() + hpr);
            power.setPMpr(NP.getPMpr() + mpr);
            power.setPDmg(NP.getPDmg() + d);
            power.setPBowDmg(NP.getPBowDmg() + bd);
            power.setPHit(NP.getPHit() + h);
            power.setPBowHit(NP.getPBowHit() + bh);
            power.setPAc(NP.getPAc() + ac);
            power.setPReduction(NP.getPReduction() + rd);
            power.setPDodge(NP.getPDodge() + dg);
            power.setPSp(NP.getPSp() + sp);
            power.setPMr(NP.getPMr() + mr);
            power.setPStr(NP.getPStr() + s);
            power.setPDex(NP.getPDex() + de);
            power.setPCon(NP.getPCon() + co);
            power.setPInt(NP.getPInt() + in);
            power.setPWis(NP.getPWis() + wi);
            power.setPCha(NP.getPCha() + ch);
            power.setPFire(NP.getPFire() + fi);
            power.setPWind(NP.getPWind() + wi);
            power.setPEarth(NP.getPEarth() + ea);
            power.setPWater(NP.getPWater() + wa);
            power.setPFreeze(NP.getPFreeze() + fr);
            power.setPSturn(NP.getPSturn() + st);
            power.setPStone(NP.getPStone() + st);
            power.setPSleep(NP.getPSleep() + sl);
            power.setPSustain(NP.getPSustain() + su);
            power.setPBlind(NP.getPBlind() + bl);
            _powerMap.put(Integer.valueOf(objid), power);
            cn = DatabaseFactory.get().getConnection();
            ps = cn.prepareStatement("UPDATE `道具兌換能力系統log` SET `AddHp`=?,`AddMp`=?,`AddHpr`=?,`AddMpr`=?,`AddDmg`=?,`AddBowDmg`=?,`AddHit`=?,`AddBowHit`=?,`AddAc`=?,`AddReduction`=?,`AddDodge`=?,`AddSp`=?,`AddMr`=?,`AddStr`=?,`AddDex`=?,`AddCon`=?,`AddInt`=?,`AddWis`=?,`AddCha`=?,`AddFire`=?,`AddWind`=?,`AddEarth`=?,`AddWater`=?,`AddFreeze`=?,`AddSturn`=?,`AddStone`=?,`AddSleep`=?,`AddSustain`=?,`Addblind`=? WHERE `CharId`=?");
            int i = 0 + 1;
            ps.setInt(i, NP.getPHp() + hp);
            int i2 = i + 1;
            ps.setInt(i2, NP.getPMp() + mp);
            int i3 = i2 + 1;
            ps.setInt(i3, NP.getPHpr() + hpr);
            int i4 = i3 + 1;
            ps.setInt(i4, NP.getPMpr() + mpr);
            int i5 = i4 + 1;
            ps.setInt(i5, NP.getPDmg() + d);
            int i6 = i5 + 1;
            ps.setInt(i6, NP.getPBowDmg() + bd);
            int i7 = i6 + 1;
            ps.setInt(i7, NP.getPHit() + h);
            int i8 = i7 + 1;
            ps.setInt(i8, NP.getPBowHit() + bh);
            int i9 = i8 + 1;
            ps.setInt(i9, NP.getPAc() + ac);
            int i10 = i9 + 1;
            ps.setInt(i10, NP.getPReduction() + rd);
            int i11 = i10 + 1;
            ps.setInt(i11, NP.getPDodge() + dg);
            int i12 = i11 + 1;
            ps.setInt(i12, NP.getPSp() + sp);
            int i13 = i12 + 1;
            ps.setInt(i13, NP.getPMr() + mr);
            int i14 = i13 + 1;
            ps.setInt(i14, NP.getPStr() + s);
            int i15 = i14 + 1;
            ps.setInt(i15, NP.getPDex() + de);
            int i16 = i15 + 1;
            ps.setInt(i16, NP.getPCon() + co);
            int i17 = i16 + 1;
            ps.setInt(i17, NP.getPInt() + in);
            int i18 = i17 + 1;
            ps.setInt(i18, NP.getPWis() + wi);
            int i19 = i18 + 1;
            ps.setInt(i19, NP.getPCha() + ch);
            int i20 = i19 + 1;
            ps.setInt(i20, NP.getPFire() + fi);
            int i21 = i20 + 1;
            ps.setInt(i21, NP.getPWind() + win);
            int i22 = i21 + 1;
            ps.setInt(i22, NP.getPEarth() + ea);
            int i23 = i22 + 1;
            ps.setInt(i23, NP.getPWater() + wa);
            int i24 = i23 + 1;
            ps.setInt(i24, NP.getPFreeze() + fr);
            int i25 = i24 + 1;
            ps.setInt(i25, NP.getPSturn() + st);
            int i26 = i25 + 1;
            ps.setInt(i26, NP.getPStone() + so);
            int i27 = i26 + 1;
            ps.setInt(i27, NP.getPSleep() + sl);
            int i28 = i27 + 1;
            ps.setInt(i28, NP.getPSustain() + su);
            int i29 = i28 + 1;
            ps.setInt(i29, NP.getPBlind() + bl);
            ps.setInt(i29 + 1, objid);
            ps.execute();
        } catch (SQLException e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            SQLUtil.close(ps);
            SQLUtil.close(cn);
        }
    }

    public final void effectBuff(L1PcInstance pc, int negative) {
        Npc_PowerLog value = _powerMap.get(Integer.valueOf(pc.getId()));
        if (value != null) {
            pc.addMaxHp(value.getPHp() * negative);
            pc.addMaxMp(value.getPMp() * negative);
            pc.addHpr(value.getPHpr() * negative);
            pc.addMpr(value.getPMpr() * negative);
            pc.addDmgup(value.getPDmg() * negative);
            pc.addBowDmgup(value.getPBowDmg() * negative);
            pc.addHitup(value.getPHit() * negative);
            pc.addBowHitup(value.getPBowHit() * negative);
            pc.addAc(value.getPAc() * negative);
            pc.addDamageReductionByArmor(value.getPReduction() * negative);
            pc.add_dodge(value.getPDodge() * negative);
            pc.addSp(value.getPSp() * negative);
            pc.addMr(value.getPMr() * negative);
            pc.addStr(value.getPStr() * negative);
            pc.addCon(value.getPCon() * negative);
            pc.addDex(value.getPDex() * negative);
            pc.addWis(value.getPWis() * negative);
            pc.addCha(value.getPCha() * negative);
            pc.addInt(value.getPInt() * negative);
            pc.addFire(value.getPFire() * negative);
            pc.addWind(value.getPWind() * negative);
            pc.addEarth(value.getPEarth() * negative);
            pc.addWater(value.getPWater() * negative);
            pc.add_regist_freeze(value.getPFreeze() * negative);
            pc.addRegistStun(value.getPSturn() * negative);
            pc.addRegistStone(value.getPStone() * negative);
            pc.addRegistSleep(value.getPSleep() * negative);
            pc.addRegistSustain(value.getPSustain() * negative);
            pc.addRegistBlind(value.getPBlind() * negative);
            pc.sendPackets(new S_SPMR(pc));
            pc.sendPackets(new S_OwnCharStatus(pc));
        }
    }

    public Npc_PowerLog getPower(int id) {
        return _powerMap.get(Integer.valueOf(id));
    }
}
