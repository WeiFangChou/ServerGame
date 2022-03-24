package com.lineage.server.model.Instance;

import com.lineage.echo.OpcodesClient;
import com.lineage.server.datatables.BlessDystem;
import com.lineage.server.datatables.BlessSystem;
import com.lineage.server.datatables.DollPowerTable;
import com.lineage.server.datatables.NewEnchantDystem;
import com.lineage.server.datatables.NewEnchantSystem;
import com.lineage.server.datatables.PetItemTable;
import com.lineage.server.model.doll.L1DollExecutor;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.templates.L1BlessDystem;
import com.lineage.server.templates.L1BlessSystem;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1NewEnchantDystem;
import com.lineage.server.templates.L1NewEnchantSystem;
import com.lineage.server.templates.L1PetItem;
import com.lineage.server.templates.L1SuperCard;
import com.lineage.server.utils.BinaryOutputStream;
import java.util.Iterator;

public class L1ItemStatus {
    private final L1Item _item;
    private final L1ItemInstance _itemInstance;
    private final L1ItemPower _itemPower;
    private final BinaryOutputStream _os;
    private boolean _statusx;

    public L1ItemStatus(L1ItemInstance itemInstance) {
        this._itemInstance = itemInstance;
        this._item = itemInstance.getItem();
        this._os = new BinaryOutputStream();
        this._itemPower = new L1ItemPower(this._itemInstance);
    }

    public L1ItemStatus(L1Item template) {
        this._itemInstance = new L1ItemInstance();
        this._itemInstance.setItem(template);
        this._item = template;
        this._os = new BinaryOutputStream();
        this._itemPower = new L1ItemPower(this._itemInstance);
    }

    public BinaryOutputStream getStatusBytes() {
        switch (this._item.getUseType()) {
            case OpcodesClient.C_OPCODE_HOTEL_ENTER:
                L1PetItem petItem = PetItemTable.get().getTemplate(this._item.getItemId());
                if (petItem.isWeapom()) {
                    return petweapon(petItem);
                }
                return petarmor(petItem);
            case OpcodesClient.C_OPCODE_SECURITYSTATUSSET:
            case OpcodesClient.C_OPCODE_SECURITYSTATUS:
            case OpcodesClient.C_OPCODE_WARTIMESET:
            case OpcodesClient.C_OPCODE_SOLDIERGIVEOK:
            case OpcodesClient.C_OPCODE_SOLDIERGIVE:
            case OpcodesClient.C_OPCODE_SOLDIERBUY:
            case OpcodesClient.C_OPCODE_HORUNOK:
            case OpcodesClient.C_OPCODE_HORUN:
            case -1:
            case 0:
            case 3:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 39:
            case 42:
            case 46:
            case 55:
                if (this._item.getclassname().equalsIgnoreCase("doll.Magic_Doll")) {
                    return etcitem_doll();
                }
                return etcitem();
            case -3:
            case OpcodesClient.C_OPCODE_HIRESOLDIER:
                return arrow();
            case 1:
                return weapon();
            case 2:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 25:
                return armor();
            case 4:
            case 11:
            case 41:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            default:
                return null;
            case 10:
                return lightitem();
            case 23:
            case 24:
            case 37:
            case 40:
                return accessories();
            case 38:
                return fooditem();
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
                return accessories2();
        }
    }

    private BinaryOutputStream etcitem_doll() {
        L1Doll doll = DollPowerTable.get().get_type(this._item.getItemId());
        this._os.writeC(39);
        this._os.writeS("詳細數值如下");
        if (!doll.getPowerList().isEmpty()) {
            Iterator<L1DollExecutor> it = doll.getPowerList().iterator();
            while (it.hasNext()) {
                L1DollExecutor power = it.next();
                if (power.get_note() != null) {
                    String msg = power.get_note();
                    this._os.writeC(39);
                    this._os.writeS(msg);
                    this._os.writeC(23);
                    this._os.writeC(this._item.getMaterial());
                    this._os.writeD(this._itemInstance.getWeight());
                }
            }
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream arrow() {
        this._os.writeC(1);
        this._os.writeC(this._item.getDmgSmall());
        this._os.writeC(this._item.getDmgLarge());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream fooditem() {
        this._os.writeC(21);
        this._os.writeH(this._item.getFoodVolume());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream lightitem() {
        this._os.writeC(22);
        this._os.writeH(this._item.getLightRange());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream armor() {
        this._os.writeC(19);
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);
        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());
        this._os.writeD(this._itemInstance.getWeight());
        if (this._itemInstance.getEnchantLevel() != 0) {
            this._os.writeC(2);
            this._os.writeC(this._itemInstance.getEnchantLevel());
        }
        if (this._itemInstance.get_durability() != 0) {
            this._os.writeC(3);
            this._os.writeC(this._itemInstance.get_durability());
        }
        int pw_s1 = this._item.get_addstr();
        int pw_s2 = this._item.get_adddex();
        int pw_s3 = this._item.get_addcon();
        int pw_s4 = this._item.get_addwis();
        int pw_s5 = this._item.get_addint();
        int pw_s6 = this._item.get_addcha();
        int pw_sHp = this._item.get_addhp();
        int pw_sMp = this._item.get_addmp();
        int pw_sMr = this._itemPower.getMr();
        int pw_sSp = this._item.get_addsp();
        int pw_sDg = this._item.getDmgModifierByArmor();
        int pw_sHi = this._item.getHitModifierByArmor();
        int pw_d4_1 = this._item.get_defense_fire();
        int pw_d4_2 = this._item.get_defense_water();
        int pw_d4_3 = this._item.get_defense_wind();
        int pw_d4_4 = this._item.get_defense_earth();
        int pw_k6_1 = this._item.get_regist_freeze();
        int pw_k6_2 = this._item.get_regist_stone();
        int pw_k6_3 = this._item.get_regist_sleep();
        int pw_k6_4 = this._item.get_regist_blind();
        int pw_k6_5 = this._item.get_regist_stun();
        int pw_k6_6 = this._item.get_regist_sustain();
        int pw_sHpr = this._item.get_addhpr();
        int pw_sMpr = this._item.get_addmpr();
        int bamag = this._item.getDamageReduction();
        if (pw_sHi != 0) {
            this._os.writeC(5);
            this._os.writeC(pw_sHi);
        }
        if (pw_sDg != 0) {
            this._os.writeC(6);
            this._os.writeC(pw_sDg);
        }
        int bit = (this._item.isUseRoyal() ? 1 : 0) | (this._item.isUseKnight() ? 2 : 0) | (this._item.isUseElf() ? 4 : 0) | (this._item.isUseMage() ? 8 : 0) | (this._item.isUseDarkelf() ? 16 : 0) | (this._item.isUseDragonknight() ? 32 : 0);
        int i = this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(7);
        this._os.writeC(bit | i);
        int itemId = this._itemInstance.getItem().getItemId();
        if (!(this._item.getType2() != 2 || itemId == 20028 || itemId == 20082 || itemId == 20173 || itemId == 20206 || itemId == 20232 || itemId == 20126 || itemId == 20081)) {
            this._os.writeC(39);
            this._os.writeS("安定值: " + this._item.get_safeenchant());
        }
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(24);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }
        if (this._item.getBowDmgModifierByArmor() != 0) {
            this._os.writeC(35);
            this._os.writeC(this._item.getBowDmgModifierByArmor());
        }
        if (bamag != 0) {
            this._os.writeC(39);
            this._os.writeS("傷害減免:+ " + bamag);
        }
        int s6_1 = 0;
        int s6_2 = 0;
        int s6_3 = 0;
        int s6_4 = 0;
        int s6_5 = 0;
        int s6_6 = 0;
        int aH_1 = 0;
        int aM_1 = 0;
        int aMR_1 = 0;
        int aSP_1 = 0;
        int aSS_1 = 0;
        int d4_1 = 0;
        int d4_2 = 0;
        int d4_3 = 0;
        int d4_4 = 0;
        int k6_1 = 0;
        int k6_2 = 0;
        int k6_3 = 0;
        int k6_4 = 0;
        int k6_5 = 0;
        int k6_6 = 0;
        if (this._itemInstance.isMatch()) {
            s6_1 = this._item.get_mode()[0];
            s6_2 = this._item.get_mode()[1];
            s6_3 = this._item.get_mode()[2];
            s6_4 = this._item.get_mode()[3];
            s6_5 = this._item.get_mode()[4];
            s6_6 = this._item.get_mode()[5];
            aH_1 = this._item.get_mode()[6];
            aM_1 = this._item.get_mode()[7];
            aMR_1 = this._item.get_mode()[8];
            aSP_1 = this._item.get_mode()[9];
            aSS_1 = this._item.get_mode()[10];
            d4_1 = this._item.get_mode()[11];
            d4_2 = this._item.get_mode()[12];
            d4_3 = this._item.get_mode()[13];
            d4_4 = this._item.get_mode()[14];
            k6_1 = this._item.get_mode()[15];
            k6_2 = this._item.get_mode()[16];
            k6_3 = this._item.get_mode()[17];
            k6_4 = this._item.get_mode()[18];
            k6_5 = this._item.get_mode()[19];
            k6_6 = this._item.get_mode()[20];
        }
        int addstr = pw_s1 + s6_1;
        if (addstr != 0) {
            this._os.writeC(8);
            this._os.writeC(addstr);
        }
        int adddex = pw_s2 + s6_2;
        if (adddex != 0) {
            this._os.writeC(9);
            this._os.writeC(adddex);
        }
        int addcon = pw_s3 + s6_3;
        if (addcon != 0) {
            this._os.writeC(10);
            this._os.writeC(addcon);
        }
        int addwis = pw_s4 + s6_4;
        if (addwis != 0) {
            this._os.writeC(11);
            this._os.writeC(addwis);
        }
        int addint = pw_s5 + s6_5;
        if (addint != 0) {
            this._os.writeC(12);
            this._os.writeC(addint);
        }
        int addcha = pw_s6 + s6_6;
        if (addcha != 0) {
            this._os.writeC(13);
            this._os.writeC(addcha);
        }
        int addhp = pw_sHp + aH_1;
        if (addhp != 0) {
            this._os.writeC(14);
            this._os.writeH(addhp);
        }
        int addmp = pw_sMp + aM_1;
        if (addmp != 0) {
            if (addmp <= 120) {
                this._os.writeC(32);
                this._os.writeC(addmp);
            } else {
                this._os.writeC(39);
                this._os.writeS("魔力上限 +" + addmp);
            }
        }
        if (pw_sHpr != 0) {
            this._os.writeC(37);
            this._os.writeC(pw_sHpr);
        }
        if (pw_sMpr != 0) {
            this._os.writeC(38);
            this._os.writeC(pw_sMpr);
        }
        int addmr = pw_sMr + aMR_1;
        if (addmr != 0) {
            this._os.writeC(15);
            this._os.writeH(addmr);
        }
        int addsp = pw_sSp + aSP_1;
        if (addsp != 0) {
            this._os.writeC(17);
            this._os.writeC(addsp);
        }
        boolean haste = this._item.isHasteItem();
        if (aSS_1 == 1) {
            haste = true;
        }
        if (haste) {
            this._os.writeC(18);
        }
        int fire = pw_d4_1 + d4_1;
        if (fire != 0) {
            this._os.writeC(27);
            this._os.writeC(fire);
        }
        int water = pw_d4_2 + d4_2;
        if (water != 0) {
            this._os.writeC(28);
            this._os.writeC(water);
        }
        int wind = pw_d4_3 + d4_3;
        if (wind != 0) {
            this._os.writeC(29);
            this._os.writeC(wind);
        }
        int earth = pw_d4_4 + d4_4;
        if (earth != 0) {
            this._os.writeC(30);
            this._os.writeC(earth);
        }
        boolean isOut = false;
        int freeze = pw_k6_1 + k6_1;
        if (freeze != 0) {
            if (addmr != 0 && 0 == 0) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(freeze);
            this._os.writeC(33);
            this._os.writeC(1);
        }
        int stone = pw_k6_2 + k6_2;
        if (stone != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(stone);
            this._os.writeC(33);
            this._os.writeC(2);
        }
        int sleep = pw_k6_3 + k6_3;
        if (sleep != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(sleep);
            this._os.writeC(33);
            this._os.writeC(3);
        }
        int blind = pw_k6_4 + k6_4;
        if (blind != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(blind);
            this._os.writeC(33);
            this._os.writeC(4);
        }
        int stun = pw_k6_5 + k6_5;
        if (stun != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(stun);
            this._os.writeC(33);
            this._os.writeC(5);
        }
        int sustain = pw_k6_6 + k6_6;
        if (sustain != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
            }
            this._os.writeC(15);
            this._os.writeH(sustain);
            this._os.writeC(33);
            this._os.writeC(6);
        }
        L1NewEnchantDystem Encdant = NewEnchantDystem.get(this._item.getItemId(), this._itemInstance.getEnchantLevel() - this._item.get_safeenchant());
        if (Encdant != null) {
            if (!(Encdant.getHp() == 0 && Encdant.getMp() == 0 && Encdant.getStr() == 0 && Encdant.getDex() == 0 && Encdant.getCon() == 0 && Encdant.getWis() == 0 && Encdant.getInt() == 0 && Encdant.getCha() == 0 && Encdant.getMr() == 0 && Encdant.getSp() == 0 && Encdant.getFire() == 0 && Encdant.getWater() == 0 && Encdant.getWind() == 0 && Encdant.getEarth() == 0 && Encdant.getFreeze() == 0 && Encdant.getStone() == 0 && Encdant.getSleep() == 0 && Encdant.getBlind() == 0 && Encdant.getStun() == 0 && Encdant.getSustain() == 0 && Encdant.getExp() == 0 && Encdant.getBamage() == 0 && Encdant.getHit() == 0 && Encdant.getDmg() == 0 && Encdant.getBowHit() == 0 && Encdant.getBowDmg() == 0)) {
                this._os.writeC(39);
                this._os.writeS("<強化能力>：↓");
            }
            if (Encdant.getHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(Encdant.getHp());
            }
            int add_mp = Encdant.getMp();
            if (add_mp != 0) {
                if (add_mp <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(add_mp);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + add_mp);
                }
            }
            if (Encdant.getStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(Encdant.getStr());
            }
            if (Encdant.getDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(Encdant.getDex());
            }
            if (Encdant.getCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(Encdant.getCon());
            }
            if (Encdant.getWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(Encdant.getWis());
            }
            if (Encdant.getInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(Encdant.getInt());
            }
            if (Encdant.getCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(Encdant.getCha());
            }
            if (Encdant.getMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getMr());
            }
            if (Encdant.getSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(Encdant.getSp());
            }
            if (Encdant.getFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(Encdant.getFire());
            }
            if (Encdant.getWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(Encdant.getWater());
            }
            if (Encdant.getWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(Encdant.getWind());
            }
            if (Encdant.getEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(Encdant.getEarth());
            }
            if (Encdant.getFreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getFreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (Encdant.getStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (Encdant.getSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (Encdant.getBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (Encdant.getStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (Encdant.getSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(Encdant.getSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (Encdant.getExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + Encdant.getExp());
            }
            if (Encdant.getBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + Encdant.getBamage());
            }
            if (Encdant.getHit() != 0) {
                this._os.writeC(48);
                this._os.writeC(Encdant.getHit());
            }
            if (Encdant.getDmg() != 0) {
                this._os.writeC(47);
                this._os.writeC(Encdant.getDmg());
            }
            if (Encdant.getBowHit() != 0) {
                this._os.writeC(24);
                this._os.writeC(Encdant.getBowHit());
            }
            if (Encdant.getBowDmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(Encdant.getBowDmg());
            }
        }
        L1BlessDystem Bless = BlessDystem.get(this._item.getItemId());
        if (Bless != null) {
            if (!(Bless.getHp() == 0 && Bless.getMp() == 0 && Bless.getStr() == 0 && Bless.getDex() == 0 && Bless.getCon() == 0 && Bless.getWis() == 0 && Bless.getInt() == 0 && Bless.getCha() == 0 && Bless.getMr() == 0 && Bless.getSp() == 0 && Bless.getFire() == 0 && Bless.getWater() == 0 && Bless.getWind() == 0 && Bless.getEarth() == 0 && Bless.getFreeze() == 0 && Bless.getStone() == 0 && Bless.getSleep() == 0 && Bless.getBlind() == 0 && Bless.getStun() == 0 && Bless.getSustain() == 0 && Bless.getHpr() == 0 && Bless.getMpr() == 0 && Bless.getExp() == 0 && Bless.getBamage() == 0 && Bless.getHit() == 0 && Bless.getDmg() == 0 && Bless.getBowHit() == 0 && Bless.getBowDmg() == 0 && Bless.getAC() == 0 && Bless.getWeight() == 0)) {
                this._os.writeC(39);
                this._os.writeS("<祝福能力>：↓");
            }
            if (Bless.getHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(Bless.getHp());
            }
            int add_mp2 = Bless.getMp();
            if (add_mp2 != 0) {
                if (add_mp2 <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(add_mp2);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + add_mp2);
                }
            }
            if (Bless.getStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(Bless.getStr());
            }
            if (Bless.getDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(Bless.getDex());
            }
            if (Bless.getCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(Bless.getCon());
            }
            if (Bless.getWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(Bless.getWis());
            }
            if (Bless.getInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(Bless.getInt());
            }
            if (Bless.getCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(Bless.getCha());
            }
            if (Bless.getMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getMr());
            }
            if (Bless.getSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(Bless.getSp());
            }
            if (Bless.getFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(Bless.getFire());
            }
            if (Bless.getWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(Bless.getWater());
            }
            if (Bless.getWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(Bless.getWind());
            }
            if (Bless.getEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(Bless.getEarth());
            }
            if (Bless.getFreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getFreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (Bless.getStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (Bless.getSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (Bless.getBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (Bless.getStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (Bless.getSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (Bless.getHpr() != 0) {
                this._os.writeC(37);
                this._os.writeC(Bless.getHpr());
            }
            if (Bless.getMpr() != 0) {
                this._os.writeC(38);
                this._os.writeC(Bless.getMpr());
            }
            if (Bless.getExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + Bless.getExp());
            }
            if (Bless.getBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + Bless.getBamage());
            }
            if (Bless.getHit() != 0) {
                this._os.writeC(48);
                this._os.writeC(Bless.getHit());
            }
            if (Bless.getDmg() != 0) {
                this._os.writeC(47);
                this._os.writeC(Bless.getDmg());
            }
            if (Bless.getBowHit() != 0) {
                this._os.writeC(24);
                this._os.writeC(Bless.getBowHit());
            }
            if (Bless.getBowDmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(Bless.getBowDmg());
            }
            if (Bless.getAC() != 0) {
                this._os.writeC(39);
                this._os.writeS("防禦 -" + Bless.getAC());
            }
            if (Bless.getWeight() != 0) {
                this._os.writeC(39);
                this._os.writeS("負重值:" + Bless.getWeight());
            }
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream accessories() {
        this._os.writeC(19);
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);
        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());
        this._os.writeD(this._itemInstance.getWeight());
        int pw_s1 = this._item.get_addstr();
        int pw_s2 = this._item.get_adddex();
        int pw_s3 = this._item.get_addcon();
        int pw_s4 = this._item.get_addwis();
        int pw_s5 = this._item.get_addint();
        int pw_s6 = this._item.get_addcha();
        int pw_sHp = this._item.get_addhp();
        int pw_sMp = this._item.get_addmp();
        int pw_sMr = this._itemPower.getMr();
        int pw_sSp = this._item.get_addsp();
        int pw_sDg = this._item.getDmgModifierByArmor();
        int pw_sHi = this._item.getHitModifierByArmor();
        int pw_d4_1 = this._item.get_defense_fire();
        int pw_d4_2 = this._item.get_defense_water();
        int pw_d4_3 = this._item.get_defense_wind();
        int pw_d4_4 = this._item.get_defense_earth();
        int pw_k6_1 = this._item.get_regist_freeze();
        int pw_k6_2 = this._item.get_regist_stone();
        int pw_k6_3 = this._item.get_regist_sleep();
        int pw_k6_4 = this._item.get_regist_blind();
        int pw_k6_5 = this._item.get_regist_stun();
        int pw_k6_6 = this._item.get_regist_sustain();
        int pw_sHpr = this._item.get_addhpr();
        int pw_sMpr = this._item.get_addmpr();
        int bamag = this._item.getDamageReduction();
        if (pw_sHi != 0) {
            this._os.writeC(5);
            this._os.writeC(pw_sHi);
        }
        if (pw_sDg != 0) {
            this._os.writeC(6);
            this._os.writeC(pw_sDg);
        }
        int bit = (this._item.isUseRoyal() ? 1 : 0) | (this._item.isUseKnight() ? 2 : 0) | (this._item.isUseElf() ? 4 : 0) | (this._item.isUseMage() ? 8 : 0) | (this._item.isUseDarkelf() ? 16 : 0) | (this._item.isUseDragonknight() ? 32 : 0);
        int i = this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(7);
        this._os.writeC(bit | i);
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(24);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }
        if (this._item.getBowDmgModifierByArmor() != 0) {
            this._os.writeC(35);
            this._os.writeC(this._item.getBowDmgModifierByArmor());
        }
        if (bamag != 0) {
            this._os.writeC(39);
            this._os.writeS("傷害減免:+ " + bamag);
        }
        int s6_1 = 0;
        int s6_2 = 0;
        int s6_3 = 0;
        int s6_4 = 0;
        int s6_5 = 0;
        int s6_6 = 0;
        int aH_1 = 0;
        int aM_1 = 0;
        int aMR_1 = 0;
        int aSP_1 = 0;
        int aSS_1 = 0;
        int d4_1 = 0;
        int d4_2 = 0;
        int d4_3 = 0;
        int d4_4 = 0;
        int k6_1 = 0;
        int k6_2 = 0;
        int k6_3 = 0;
        int k6_4 = 0;
        int k6_5 = 0;
        int k6_6 = 0;
        if (this._itemInstance.isMatch()) {
            s6_1 = this._item.get_mode()[0];
            s6_2 = this._item.get_mode()[1];
            s6_3 = this._item.get_mode()[2];
            s6_4 = this._item.get_mode()[3];
            s6_5 = this._item.get_mode()[4];
            s6_6 = this._item.get_mode()[5];
            aH_1 = this._item.get_mode()[6];
            aM_1 = this._item.get_mode()[7];
            aMR_1 = this._item.get_mode()[8];
            aSP_1 = this._item.get_mode()[9];
            aSS_1 = this._item.get_mode()[10];
            d4_1 = this._item.get_mode()[11];
            d4_2 = this._item.get_mode()[12];
            d4_3 = this._item.get_mode()[13];
            d4_4 = this._item.get_mode()[14];
            k6_1 = this._item.get_mode()[15];
            k6_2 = this._item.get_mode()[16];
            k6_3 = this._item.get_mode()[17];
            k6_4 = this._item.get_mode()[18];
            k6_5 = this._item.get_mode()[19];
            k6_6 = this._item.get_mode()[20];
        }
        int addstr = pw_s1 + s6_1;
        if (addstr != 0) {
            this._os.writeC(8);
            this._os.writeC(addstr);
        }
        int adddex = pw_s2 + s6_2;
        if (adddex != 0) {
            this._os.writeC(9);
            this._os.writeC(adddex);
        }
        int addcon = pw_s3 + s6_3;
        if (addcon != 0) {
            this._os.writeC(10);
            this._os.writeC(addcon);
        }
        int addwis = pw_s4 + s6_4;
        if (addwis != 0) {
            this._os.writeC(11);
            this._os.writeC(addwis);
        }
        int addint = pw_s5 + s6_5;
        if (addint != 0) {
            this._os.writeC(12);
            this._os.writeC(addint);
        }
        int addcha = pw_s6 + s6_6;
        if (addcha != 0) {
            this._os.writeC(13);
            this._os.writeC(addcha);
        }
        int addhp = greater()[4] + pw_sHp + aH_1;
        if (addhp != 0) {
            this._os.writeC(14);
            this._os.writeH(addhp);
        }
        int addmp = greater()[5] + pw_sMp + aM_1;
        if (addmp != 0) {
            if (addmp <= 120) {
                this._os.writeC(32);
                this._os.writeC(addmp);
            } else {
                this._os.writeC(39);
                this._os.writeS("魔力上限 +" + addmp);
            }
        }
        if (pw_sHpr != 0) {
            this._os.writeC(37);
            this._os.writeC(pw_sHpr);
        }
        if (pw_sMpr != 0) {
            this._os.writeC(38);
            this._os.writeC(pw_sMpr);
        }
        int addmr = greater()[6] + pw_sMr + aMR_1;
        if (addmr != 0) {
            this._os.writeC(15);
            this._os.writeH(addmr);
        }
        int addsp = greater()[7] + pw_sSp + aSP_1;
        if (addsp != 0) {
            this._os.writeC(17);
            this._os.writeC(addsp);
        }
        boolean haste = this._item.isHasteItem();
        if (aSS_1 == 1) {
            haste = true;
        }
        if (haste) {
            this._os.writeC(18);
        }
        int defense_fire = greater()[0] + pw_d4_1 + d4_1;
        if (defense_fire != 0) {
            this._os.writeC(27);
            this._os.writeC(defense_fire);
        }
        int defense_water = greater()[1] + pw_d4_2 + d4_2;
        if (defense_water != 0) {
            this._os.writeC(28);
            this._os.writeC(defense_water);
        }
        int defense_wind = greater()[2] + pw_d4_3 + d4_3;
        if (defense_wind != 0) {
            this._os.writeC(29);
            this._os.writeC(defense_wind);
        }
        int defense_earth = greater()[3] + pw_d4_4 + d4_4;
        if (defense_earth != 0) {
            this._os.writeC(30);
            this._os.writeC(defense_earth);
        }
        boolean isOut = false;
        int freeze = pw_k6_1 + k6_1;
        if (freeze != 0) {
            if (addmr != 0 && 0 == 0) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(freeze);
            this._os.writeC(33);
            this._os.writeC(1);
        }
        int stone = pw_k6_2 + k6_2;
        if (stone != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(stone);
            this._os.writeC(33);
            this._os.writeC(2);
        }
        int sleep = pw_k6_3 + k6_3;
        if (sleep != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(sleep);
            this._os.writeC(33);
            this._os.writeC(3);
        }
        int blind = pw_k6_4 + k6_4;
        if (blind != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(blind);
            this._os.writeC(33);
            this._os.writeC(4);
        }
        int stun = pw_k6_5 + k6_5;
        if (stun != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(stun);
            this._os.writeC(33);
            this._os.writeC(5);
        }
        int sustain = pw_k6_6 + k6_6;
        if (sustain != 0) {
            if (addmr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
            }
            this._os.writeC(15);
            this._os.writeH(sustain);
            this._os.writeC(33);
            this._os.writeC(6);
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream accessories2() {
        this._os.writeC(19);
        int ac = this._item.get_ac();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);
        this._os.writeC(this._item.getMaterial());
        this._os.writeC(this._item.get_greater());
        this._os.writeD(this._itemInstance.getWeight());
        int pw_s1 = this._item.get_addstr();
        int pw_s2 = this._item.get_adddex();
        int pw_s3 = this._item.get_addcon();
        int pw_s4 = this._item.get_addwis();
        int pw_s5 = this._item.get_addint();
        int pw_s6 = this._item.get_addcha();
        int pw_sHp = this._item.get_addhp();
        int pw_sMp = this._item.get_addmp();
        int pw_sMr = this._itemPower.getMr();
        int pw_sSp = this._item.get_addsp();
        int pw_sDg = this._item.getDmgModifierByArmor();
        int pw_sHi = this._item.getHitModifierByArmor();
        int pw_d4_1 = this._item.get_defense_fire();
        int pw_d4_2 = this._item.get_defense_water();
        int pw_d4_3 = this._item.get_defense_wind();
        int pw_d4_4 = this._item.get_defense_earth();
        int pw_k6_1 = this._item.get_regist_freeze();
        int pw_k6_2 = this._item.get_regist_stone();
        int pw_k6_3 = this._item.get_regist_sleep();
        int pw_k6_4 = this._item.get_regist_blind();
        int pw_k6_5 = this._item.get_regist_stun();
        int pw_k6_6 = this._item.get_regist_sustain();
        int pw_sHpr = this._item.get_addhpr();
        int pw_sMpr = this._item.get_addmpr();
        int bamag = this._item.getDamageReduction();
        if (pw_sHi != 0) {
            this._os.writeC(5);
            this._os.writeC(pw_sHi);
        }
        if (pw_sDg != 0) {
            this._os.writeC(6);
            this._os.writeC(pw_sDg);
        }
        if (bamag != 0) {
            this._os.writeC(39);
            this._os.writeS("傷害減免:+ " + bamag);
        }
        int bit = (this._item.isUseRoyal() ? 1 : 0) | (this._item.isUseKnight() ? 2 : 0) | (this._item.isUseElf() ? 4 : 0) | (this._item.isUseMage() ? 8 : 0) | (this._item.isUseDarkelf() ? 16 : 0) | (this._item.isUseDragonknight() ? 32 : 0);
        int i = this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(7);
        this._os.writeC(bit | i);
        if (this._item.getBowHitModifierByArmor() != 0) {
            this._os.writeC(24);
            this._os.writeC(this._item.getBowHitModifierByArmor());
        }
        if (this._item.getBowDmgModifierByArmor() != 0) {
            this._os.writeC(35);
            this._os.writeC(this._item.getBowDmgModifierByArmor());
        }
        if (pw_s1 != 0) {
            this._os.writeC(8);
            this._os.writeC(pw_s1);
        }
        if (pw_s2 != 0) {
            this._os.writeC(9);
            this._os.writeC(pw_s2);
        }
        if (pw_s3 != 0) {
            this._os.writeC(10);
            this._os.writeC(pw_s3);
        }
        if (pw_s4 != 0) {
            this._os.writeC(11);
            this._os.writeC(pw_s4);
        }
        if (pw_s5 != 0) {
            this._os.writeC(12);
            this._os.writeC(pw_s5);
        }
        if (pw_s6 != 0) {
            this._os.writeC(13);
            this._os.writeC(pw_s6);
        }
        if (pw_sHp != 0) {
            this._os.writeC(14);
            this._os.writeH(pw_sHp);
        }
        if (pw_sMp != 0) {
            if (pw_sMp <= 120) {
                this._os.writeC(32);
                this._os.writeC(pw_sMp);
            } else {
                this._os.writeC(39);
                this._os.writeS("魔力上限 +" + pw_sMp);
            }
        }
        if (pw_sHpr != 0) {
            this._os.writeC(37);
            this._os.writeC(pw_sHpr);
        }
        if (pw_sMpr != 0) {
            this._os.writeC(38);
            this._os.writeC(pw_sMpr);
        }
        if (pw_sMr != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_sMr);
        }
        if (pw_sSp != 0) {
            this._os.writeC(17);
            this._os.writeC(pw_sSp);
        }
        if (this._item.isHasteItem()) {
            this._os.writeC(18);
        }
        if (pw_d4_1 != 0) {
            this._os.writeC(27);
            this._os.writeC(pw_d4_1);
        }
        if (pw_d4_2 != 0) {
            this._os.writeC(28);
            this._os.writeC(pw_d4_2);
        }
        if (pw_d4_3 != 0) {
            this._os.writeC(29);
            this._os.writeC(pw_d4_3);
        }
        if (pw_d4_4 != 0) {
            this._os.writeC(30);
            this._os.writeC(pw_d4_4);
        }
        boolean isOut = false;
        if (pw_k6_1 != 0) {
            if (pw_sMr != 0 && 0 == 0) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_1);
            this._os.writeC(33);
            this._os.writeC(1);
        }
        if (pw_k6_2 != 0) {
            if (pw_sMr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_2);
            this._os.writeC(33);
            this._os.writeC(2);
        }
        if (pw_k6_3 != 0) {
            if (pw_sMr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_3);
            this._os.writeC(33);
            this._os.writeC(3);
        }
        if (pw_k6_4 != 0) {
            if (pw_sMr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_4);
            this._os.writeC(33);
            this._os.writeC(4);
        }
        if (pw_k6_5 != 0) {
            if (pw_sMr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
                isOut = true;
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_5);
            this._os.writeC(33);
            this._os.writeC(5);
        }
        if (pw_k6_6 != 0) {
            if (pw_sMr != 0 && !isOut) {
                this._os.writeC(33);
                this._os.writeC(L1SkillId.ILLUSION_DIA_GOLEM);
            }
            this._os.writeC(15);
            this._os.writeH(pw_k6_6);
            this._os.writeC(33);
            this._os.writeC(6);
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream weapon() {
        this._os.writeC(1);
        this._os.writeC(this._item.getDmgSmall());
        this._os.writeC(this._item.getDmgLarge());
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (this._itemInstance.getEnchantLevel() != 0) {
            this._os.writeC(2);
            this._os.writeC(this._itemInstance.getEnchantLevel());
        }
        if (this._itemInstance.get_durability() != 0) {
            this._os.writeC(3);
            this._os.writeC(this._itemInstance.get_durability());
        }
        if (this._item.isTwohandedWeapon()) {
            this._os.writeC(4);
        }
        int get_addstr = this._item.get_addstr();
        int get_adddex = this._item.get_adddex();
        int get_addcon = this._item.get_addcon();
        int get_addwis = this._item.get_addwis();
        int get_addint = this._item.get_addint();
        int get_addcha = this._item.get_addcha();
        int get_addhp = this._item.get_addhp();
        int get_addmp = this._item.get_addmp();
        int mr = this._itemPower.getMr();
        int addWeaponSp = this._item.get_addsp();
        int addDmgModifier = this._item.getDmgModifier();
        int addHitModifier = this._item.getHitModifier();
        int BowHit = this._item.getBowHitModifierByArmor();
        int BowDmg = this._item.getBowDmgModifierByArmor();
        int pw_d4_1 = this._item.get_defense_fire();
        int pw_d4_2 = this._item.get_defense_water();
        int pw_d4_3 = this._item.get_defense_wind();
        int pw_d4_4 = this._item.get_defense_earth();
        int pw_k6_1 = this._item.get_regist_freeze();
        int pw_k6_2 = this._item.get_regist_stone();
        int pw_k6_3 = this._item.get_regist_sleep();
        int pw_k6_4 = this._item.get_regist_blind();
        int pw_k6_5 = this._item.get_regist_stun();
        int pw_k6_6 = this._item.get_regist_sustain();
        int pw_sHpr = this._item.get_addhpr();
        int pw_sMpr = this._item.get_addmpr();
        if (addHitModifier != 0) {
            this._os.writeC(5);
            this._os.writeC(addHitModifier);
        }
        if (addDmgModifier != 0) {
            this._os.writeC(6);
            this._os.writeC(addDmgModifier);
        }
        int bit = (this._item.isUseRoyal() ? 1 : 0) | (this._item.isUseKnight() ? 2 : 0) | (this._item.isUseElf() ? 4 : 0) | (this._item.isUseMage() ? 8 : 0) | (this._item.isUseDarkelf() ? 16 : 0) | (this._item.isUseDragonknight() ? 32 : 0);
        int i = this._item.isUseIllusionist() ? 64 : 0;
        this._os.writeC(7);
        this._os.writeC(bit | i);
        if (this._item.get_safeenchant() >= 0) {
            this._os.writeC(39);
            this._os.writeS("安定值: " + this._item.get_safeenchant());
        }
        if (BowHit != 0) {
            this._os.writeC(24);
            this._os.writeC(BowHit);
        }
        if (BowDmg != 0) {
            this._os.writeC(35);
            this._os.writeC(BowDmg);
        }
        if (this._itemInstance.getItemId() == 126 || this._itemInstance.getItemId() == 127 || this._itemInstance.getItemId() == 100127 || this._itemInstance.getItemId() == 100126) {
            this._os.writeC(16);
        }
        if (this._itemInstance.getItemId() == 262 || this._itemInstance.getItemId() == 100262) {
            this._os.writeC(34);
        }
        if (get_addstr != 0) {
            this._os.writeC(8);
            this._os.writeC(get_addstr);
        }
        if (get_adddex != 0) {
            this._os.writeC(9);
            this._os.writeC(get_adddex);
        }
        if (get_addcon != 0) {
            this._os.writeC(10);
            this._os.writeC(get_addcon);
        }
        if (get_addwis != 0) {
            this._os.writeC(11);
            this._os.writeC(get_addwis);
        }
        if (get_addint != 0) {
            this._os.writeC(12);
            this._os.writeC(get_addint);
        }
        if (get_addcha != 0) {
            this._os.writeC(13);
            this._os.writeC(get_addcha);
        }
        if (get_addhp != 0) {
            this._os.writeC(14);
            this._os.writeH(get_addhp);
        }
        if (get_addmp != 0) {
            if (get_addmp <= 120) {
                this._os.writeC(32);
                this._os.writeC(get_addmp);
            } else {
                this._os.writeC(39);
                this._os.writeS("魔力上限 +" + get_addmp);
            }
        }
        if (pw_sHpr != 0) {
            this._os.writeC(37);
            this._os.writeC(pw_sHpr);
        }
        if (pw_sMpr != 0) {
            this._os.writeC(38);
            this._os.writeC(pw_sMpr);
        }
        if (mr != 0) {
            this._os.writeC(15);
            this._os.writeH(mr);
        }
        if (addWeaponSp != 0) {
            this._os.writeC(17);
            this._os.writeC(addWeaponSp);
        }
        if (this._item.isHasteItem()) {
            this._os.writeC(18);
        }
        if (pw_d4_1 != 0) {
            this._os.writeC(27);
            this._os.writeC(pw_d4_1);
        }
        if (pw_d4_2 != 0) {
            this._os.writeC(28);
            this._os.writeC(pw_d4_2);
        }
        if (pw_d4_3 != 0) {
            this._os.writeC(29);
            this._os.writeC(pw_d4_3);
        }
        if (pw_d4_4 != 0) {
            this._os.writeC(30);
            this._os.writeC(pw_d4_4);
        }
        if (pw_k6_1 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_1);
            this._os.writeC(33);
            this._os.writeC(1);
        }
        if (pw_k6_2 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_2);
            this._os.writeC(33);
            this._os.writeC(2);
        }
        if (pw_k6_3 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_3);
            this._os.writeC(33);
            this._os.writeC(3);
        }
        if (pw_k6_4 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_4);
            this._os.writeC(33);
            this._os.writeC(4);
        }
        if (pw_k6_5 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_5);
            this._os.writeC(33);
            this._os.writeC(5);
        }
        if (pw_k6_6 != 0) {
            this._os.writeC(15);
            this._os.writeH(pw_k6_6);
            this._os.writeC(33);
            this._os.writeC(6);
        }
        L1NewEnchantSystem Enchant = NewEnchantSystem.get(this._item.getItemId(), this._itemInstance.getEnchantLevel() - this._item.get_safeenchant());
        if (Enchant != null) {
            if (!(Enchant.getHp() == 0 && Enchant.getMp() == 0 && Enchant.getStr() == 0 && Enchant.getDex() == 0 && Enchant.getCon() == 0 && Enchant.getWis() == 0 && Enchant.getInt() == 0 && Enchant.getCha() == 0 && Enchant.getMr() == 0 && Enchant.getSp() == 0 && Enchant.getFire() == 0 && Enchant.getWater() == 0 && Enchant.getWind() == 0 && Enchant.getEarth() == 0 && Enchant.getFreeze() == 0 && Enchant.getStone() == 0 && Enchant.getSleep() == 0 && Enchant.getBlind() == 0 && Enchant.getStun() == 0 && Enchant.getSustain() == 0 && Enchant.getExp() == 0 && Enchant.getBamage() == 0 && Enchant.getHit() == 0 && Enchant.getDmg() == 0 && Enchant.getBowHit() == 0 && Enchant.getBowDmg() == 0)) {
                this._os.writeC(39);
                this._os.writeS("<強化能力>：↓");
            }
            if (Enchant.getHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(Enchant.getHp());
            }
            int addmp = Enchant.getMp();
            if (addmp != 0) {
                if (addmp <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(addmp);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + addmp);
                }
            }
            if (Enchant.getStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(Enchant.getStr());
            }
            if (Enchant.getDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(Enchant.getDex());
            }
            if (Enchant.getCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(Enchant.getCon());
            }
            if (Enchant.getWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(Enchant.getWis());
            }
            if (Enchant.getInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(Enchant.getInt());
            }
            if (Enchant.getCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(Enchant.getCha());
            }
            if (Enchant.getMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getMr());
            }
            if (Enchant.getSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(Enchant.getSp());
            }
            if (Enchant.getFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(Enchant.getFire());
            }
            if (Enchant.getWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(Enchant.getWater());
            }
            if (Enchant.getWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(Enchant.getWind());
            }
            if (Enchant.getEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(Enchant.getEarth());
            }
            if (Enchant.getFreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getFreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (Enchant.getStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (Enchant.getSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (Enchant.getBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (Enchant.getStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (Enchant.getSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(Enchant.getSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (Enchant.getExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + Enchant.getExp());
            }
            if (Enchant.getBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + Enchant.getBamage());
            }
            if (Enchant.getHit() != 0) {
                this._os.writeC(48);
                this._os.writeC(Enchant.getHit());
            }
            if (Enchant.getDmg() != 0) {
                this._os.writeC(47);
                this._os.writeC(Enchant.getDmg());
            }
            if (Enchant.getBowHit() != 0) {
                this._os.writeC(24);
                this._os.writeC(Enchant.getBowHit());
            }
            if (Enchant.getBowDmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(Enchant.getBowDmg());
            }
        }
        L1BlessSystem Bless = BlessSystem.get(this._item.getItemId());
        if (Bless != null) {
            if (!(Bless.getHp() == 0 && Bless.getMp() == 0 && Bless.getStr() == 0 && Bless.getDex() == 0 && Bless.getCon() == 0 && Bless.getWis() == 0 && Bless.getInt() == 0 && Bless.getCha() == 0 && Bless.getMr() == 0 && Bless.getSp() == 0 && Bless.getFire() == 0 && Bless.getWater() == 0 && Bless.getWind() == 0 && Bless.getEarth() == 0 && Bless.getFreeze() == 0 && Bless.getStone() == 0 && Bless.getSleep() == 0 && Bless.getBlind() == 0 && Bless.getStun() == 0 && Bless.getSustain() == 0 && Bless.getHpr() == 0 && Bless.getMpr() == 0 && Bless.getExp() == 0 && Bless.getBamage() == 0 && Bless.getHit() == 0 && Bless.getDmg() == 0 && Bless.getBowHit() == 0 && Bless.getBowDmg() == 0 && Bless.getDmgSmall() == 0 && Bless.getDmgLarge() == 0)) {
                this._os.writeC(39);
                this._os.writeS("<祝福能力>：↓");
            }
            if (Bless.getHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(Bless.getHp());
            }
            int addmp2 = Bless.getMp();
            if (addmp2 != 0) {
                if (addmp2 <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(addmp2);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + addmp2);
                }
            }
            if (Bless.getStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(Bless.getStr());
            }
            if (Bless.getDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(Bless.getDex());
            }
            if (Bless.getCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(Bless.getCon());
            }
            if (Bless.getWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(Bless.getWis());
            }
            if (Bless.getInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(Bless.getInt());
            }
            if (Bless.getCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(Bless.getCha());
            }
            if (Bless.getMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getMr());
            }
            if (Bless.getSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(Bless.getSp());
            }
            if (Bless.getFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(Bless.getFire());
            }
            if (Bless.getWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(Bless.getWater());
            }
            if (Bless.getWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(Bless.getWind());
            }
            if (Bless.getEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(Bless.getEarth());
            }
            if (Bless.getFreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getFreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (Bless.getStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (Bless.getSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (Bless.getBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (Bless.getStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (Bless.getSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(Bless.getSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (Bless.getHpr() != 0) {
                this._os.writeC(37);
                this._os.writeC(Bless.getHpr());
            }
            if (Bless.getMpr() != 0) {
                this._os.writeC(38);
                this._os.writeC(Bless.getMpr());
            }
            if (Bless.getExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + Bless.getExp());
            }
            if (Bless.getBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + Bless.getBamage());
            }
            if (Bless.getHit() != 0) {
                this._os.writeC(48);
                this._os.writeC(Bless.getHit());
            }
            if (Bless.getDmg() != 0) {
                this._os.writeC(47);
                this._os.writeC(Bless.getDmg());
            }
            if (Bless.getBowHit() != 0) {
                this._os.writeC(24);
                this._os.writeC(Bless.getBowHit());
            }
            if (Bless.getBowDmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(Bless.getBowDmg());
            }
            if (!(Bless.getDmgSmall() == 0 && Bless.getDmgLarge() == 0)) {
                this._os.writeC(39);
                this._os.writeS("攻擊力 " + Bless.getDmgSmall() + "/ " + Bless.getDmgLarge());
            }
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream etcitem() {
        if (this._item.getItemId() == 40312) {
            this._os.writeC(39);
            this._os.writeS("旅館編號:" + this._itemInstance.getInnKeyName());
            this._os.writeC(39);
            this._os.writeS("到期時間:(" + this._itemInstance.getDueTime() + ")");
        }
        this._os.writeC(23);
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        L1SuperCard card = null;
        L1Item item = this._itemInstance.getItem();
        if (item != null) {
            card = item.getCard();
        }
        if (card != null) {
            if (card.getExtraHit() != 0) {
                this._os.writeC(5);
                this._os.writeC(card.getExtraHit());
            }
            if (card.getExtraDmg() != 0) {
                this._os.writeC(6);
                this._os.writeC(card.getExtraDmg());
            }
            if (card.getExtraStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(card.getExtraStr());
            }
            if (card.getExtraDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(card.getExtraDex());
            }
            if (card.getExtraCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(card.getExtraCon());
            }
            if (card.getExtraWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(card.getExtraWis());
            }
            if (card.getExtraInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(card.getExtraInt());
            }
            if (card.getExtraCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(card.getExtraCha());
            }
            if (card.getExtraHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(card.getExtraHp());
            }
            int addmp = card.getExtraMp();
            if (addmp != 0) {
                if (addmp <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(addmp);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + addmp);
                }
            }
            if (card.getExtraBowhit() != 0) {
                this._os.writeC(24);
                this._os.writeC(card.getExtraBowhit());
            }
            if (card.getExtraSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(card.getExtraSp());
            }
            if (card.getExtraFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(card.getExtraFire());
            }
            if (card.getExtraWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(card.getExtraWater());
            }
            if (card.getExtraWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(card.getExtraWind());
            }
            if (card.getExtraEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(card.getExtraEarth());
            }
            if (card.getExtrafreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtrafreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (card.getExtraStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (card.getExtraSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (card.getExtraBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (card.getExtraStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (card.getExtraSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (card.getExtraBowdmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(card.getExtraBowdmg());
            }
            if (card.getExtraExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + card.getExtraExp());
            }
            if (card.getExtraBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + card.getExtraBamage());
            }
            if (card.getExtraMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getExtraMr());
            }
            if (card.getExtraHpr() != 0) {
                this._os.writeC(37);
                this._os.writeC(card.getExtraHpr());
            }
            if (card.getExtraMpr() != 0) {
                this._os.writeC(38);
                this._os.writeC(card.getExtraMpr());
            }
        }
        if (card != null) {
            if (!(card.getWakeHp() == 0 && card.getWakeMp() == 0 && card.getWakeHpr() == 0 && card.getWakeMpr() == 0 && card.getWakeStr() == 0 && card.getWakeDex() == 0 && card.getWakeInt() == 0 && card.getWakeCon() == 0 && card.getWakeWis() == 0 && card.getWakeCha() == 0 && card.getWakeMr() == 0 && card.getWakeSp() == 0 && card.getWakeHit() == 0 && card.getWakeBowhit() == 0 && card.getWakeDmg() == 0 && card.getWakeBowdmg() == 0 && card.getWakeExp() == 0 && card.getWakeWeight() == 0 && card.getWakeEarth() == 0 && card.getWakeWind() == 0 && card.getWakeWater() == 0 && card.getWakeFire() == 0 && card.getWakeStun() == 0 && card.getWakeStone() == 0 && card.getWakeSleep() == 0 && card.getWakefreeze() == 0 && card.getWakeSustain() == 0 && card.getWakeBlind() == 0 && card.getWakeBamage() == 0 && card.getWakeMr() == 0)) {
                this._os.writeC(39);
                this._os.writeS("<覺醒>：↓");
            }
            if (card.getWakeHit() != 0) {
                this._os.writeC(48);
                this._os.writeC(card.getWakeHit());
            }
            if (card.getWakeDmg() != 0) {
                this._os.writeC(47);
                this._os.writeC(card.getWakeDmg());
            }
            if (card.getWakeStr() != 0) {
                this._os.writeC(8);
                this._os.writeC(card.getWakeStr());
            }
            if (card.getWakeDex() != 0) {
                this._os.writeC(9);
                this._os.writeC(card.getWakeDex());
            }
            if (card.getWakeCon() != 0) {
                this._os.writeC(10);
                this._os.writeC(card.getWakeCon());
            }
            if (card.getWakeWis() != 0) {
                this._os.writeC(11);
                this._os.writeC(card.getWakeWis());
            }
            if (card.getWakeInt() != 0) {
                this._os.writeC(12);
                this._os.writeC(card.getWakeInt());
            }
            if (card.getWakeCha() != 0) {
                this._os.writeC(13);
                this._os.writeC(card.getWakeCha());
            }
            if (card.getWakeHp() != 0) {
                this._os.writeC(14);
                this._os.writeH(card.getWakeHp());
            }
            int addmp2 = card.getWakeMp();
            if (addmp2 != 0) {
                if (addmp2 <= 120) {
                    this._os.writeC(32);
                    this._os.writeC(addmp2);
                } else {
                    this._os.writeC(39);
                    this._os.writeS("魔力上限 +" + addmp2);
                }
            }
            if (card.getWakeBowhit() != 0) {
                this._os.writeC(24);
                this._os.writeC(card.getWakeBowhit());
            }
            if (card.getWakeSp() != 0) {
                this._os.writeC(17);
                this._os.writeC(card.getWakeSp());
            }
            if (card.getWakeFire() != 0) {
                this._os.writeC(27);
                this._os.writeC(card.getWakeFire());
            }
            if (card.getWakeWater() != 0) {
                this._os.writeC(28);
                this._os.writeC(card.getWakeWater());
            }
            if (card.getWakeWind() != 0) {
                this._os.writeC(29);
                this._os.writeC(card.getWakeWind());
            }
            if (card.getWakeEarth() != 0) {
                this._os.writeC(30);
                this._os.writeC(card.getWakeEarth());
            }
            if (card.getWakefreeze() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakefreeze());
                this._os.writeC(33);
                this._os.writeC(1);
            }
            if (card.getWakeStone() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeStone());
                this._os.writeC(33);
                this._os.writeC(2);
            }
            if (card.getWakeSleep() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeSleep());
                this._os.writeC(33);
                this._os.writeC(3);
            }
            if (card.getWakeBlind() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeBlind());
                this._os.writeC(33);
                this._os.writeC(4);
            }
            if (card.getWakeStun() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeStun());
                this._os.writeC(33);
                this._os.writeC(5);
            }
            if (card.getWakeSustain() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeSustain());
                this._os.writeC(33);
                this._os.writeC(6);
            }
            if (card.getWakeBowdmg() != 0) {
                this._os.writeC(35);
                this._os.writeC(card.getWakeBowdmg());
            }
            if (card.getWakeExp() != 0) {
                this._os.writeC(39);
                this._os.writeS("經驗值:" + card.getWakeExp());
            }
            if (card.getWakeBamage() != 0) {
                this._os.writeC(39);
                this._os.writeS("傷害減免:" + card.getWakeBamage());
            }
            if (card.getWakeHpr() != 0) {
                this._os.writeC(37);
                this._os.writeC(card.getWakeHpr());
            }
            if (card.getWakeMpr() != 0) {
                this._os.writeC(38);
                this._os.writeC(card.getWakeMpr());
            }
            if (card.getWakeMr() != 0) {
                this._os.writeC(15);
                this._os.writeH(card.getWakeMr());
            }
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream petarmor(L1PetItem petItem) {
        this._os.writeC(19);
        int ac = petItem.getAddAc();
        if (ac < 0) {
            ac = Math.abs(ac);
        }
        this._os.writeC(ac);
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (petItem.getHitModifier() != 0) {
            this._os.writeC(5);
            this._os.writeC(petItem.getHitModifier());
        }
        if (petItem.getDamageModifier() != 0) {
            this._os.writeC(6);
            this._os.writeC(petItem.getDamageModifier());
        }
        if (petItem.isHigher()) {
            this._os.writeC(7);
            this._os.writeC(128);
        }
        if (petItem.getAddStr() != 0) {
            this._os.writeC(8);
            this._os.writeC(petItem.getAddStr());
        }
        if (petItem.getAddDex() != 0) {
            this._os.writeC(9);
            this._os.writeC(petItem.getAddDex());
        }
        if (petItem.getAddCon() != 0) {
            this._os.writeC(10);
            this._os.writeC(petItem.getAddCon());
        }
        if (petItem.getAddWis() != 0) {
            this._os.writeC(11);
            this._os.writeC(petItem.getAddWis());
        }
        if (petItem.getAddInt() != 0) {
            this._os.writeC(12);
            this._os.writeC(petItem.getAddInt());
        }
        if (petItem.getAddHp() != 0) {
            this._os.writeC(14);
            this._os.writeH(petItem.getAddHp());
        }
        if (petItem.getAddMp() != 0) {
            this._os.writeC(32);
            this._os.writeC(petItem.getAddMp());
        }
        if (petItem.getAddMr() != 0) {
            this._os.writeC(15);
            this._os.writeH(petItem.getAddMr());
        }
        if (petItem.getAddSp() != 0) {
            this._os.writeC(17);
            this._os.writeC(petItem.getAddSp());
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private BinaryOutputStream petweapon(L1PetItem petItem) {
        this._os.writeC(1);
        this._os.writeC(0);
        this._os.writeC(0);
        this._os.writeC(this._item.getMaterial());
        this._os.writeD(this._itemInstance.getWeight());
        if (petItem.isHigher()) {
            this._os.writeC(7);
            this._os.writeC(128);
        }
        if (petItem.getAddStr() != 0) {
            this._os.writeC(8);
            this._os.writeC(petItem.getAddStr());
        }
        if (petItem.getAddDex() != 0) {
            this._os.writeC(9);
            this._os.writeC(petItem.getAddDex());
        }
        if (petItem.getAddCon() != 0) {
            this._os.writeC(10);
            this._os.writeC(petItem.getAddCon());
        }
        if (petItem.getAddWis() != 0) {
            this._os.writeC(11);
            this._os.writeC(petItem.getAddWis());
        }
        if (petItem.getAddInt() != 0) {
            this._os.writeC(12);
            this._os.writeC(petItem.getAddInt());
        }
        if (petItem.getAddHp() != 0) {
            this._os.writeC(14);
            this._os.writeH(petItem.getAddHp());
        }
        if (petItem.getAddMp() != 0) {
            this._os.writeC(32);
            this._os.writeC(petItem.getAddMp());
        }
        if (petItem.getAddMr() != 0) {
            this._os.writeC(15);
            this._os.writeH(petItem.getAddMr());
        }
        if (this._statusx) {
            if (!this._item.isTradable()) {
                this._os.writeC(39);
                this._os.writeS("無法交易");
            }
            if (this._item.isCantDelete()) {
                this._os.writeC(39);
                this._os.writeS("無法刪除");
            }
            if (this._item.get_safeenchant() < 0) {
                this._os.writeC(39);
                this._os.writeS("無法強化");
            }
        }
        return this._os;
    }

    private int[] greater() {
        int level = this._itemInstance.getEnchantLevel();
        int[] rint = new int[10];
        switch (this._itemInstance.getItem().get_greater()) {
            case 0:
                switch (level) {
                    case 0:
                        return rint;
                    case 1:
                        int[] rint2 = new int[10];
                        rint2[0] = 1;
                        rint2[1] = 1;
                        rint2[2] = 1;
                        rint2[3] = 1;
                        return rint2;
                    case 2:
                        int[] rint3 = new int[10];
                        rint3[0] = 2;
                        rint3[1] = 2;
                        rint3[2] = 2;
                        rint3[3] = 2;
                        return rint3;
                    case 3:
                        int[] rint4 = new int[10];
                        rint4[0] = 3;
                        rint4[1] = 3;
                        rint4[2] = 3;
                        rint4[3] = 3;
                        return rint4;
                    case 4:
                        int[] rint5 = new int[10];
                        rint5[0] = 4;
                        rint5[1] = 4;
                        rint5[2] = 4;
                        rint5[3] = 4;
                        return rint5;
                    case 5:
                        int[] rint6 = new int[10];
                        rint6[0] = 5;
                        rint6[1] = 5;
                        rint6[2] = 5;
                        rint6[3] = 5;
                        return rint6;
                    case 6:
                        int[] rint7 = new int[10];
                        rint7[0] = 6;
                        rint7[1] = 6;
                        rint7[2] = 6;
                        rint7[3] = 6;
                        rint7[8] = 1;
                        rint7[9] = 1;
                        return rint7;
                    case 7:
                        int[] rint8 = new int[10];
                        rint8[0] = 10;
                        rint8[1] = 10;
                        rint8[2] = 10;
                        rint8[3] = 10;
                        rint8[8] = 3;
                        rint8[9] = 3;
                        return rint8;
                    default:
                        int[] rint9 = new int[10];
                        rint9[0] = 15;
                        rint9[1] = 15;
                        rint9[2] = 15;
                        rint9[3] = 15;
                        rint9[8] = 3;
                        rint9[9] = 3;
                        return rint9;
                }
            case 1:
                switch (level) {
                    case 0:
                        return rint;
                    case 1:
                        int[] rint10 = new int[10];
                        rint10[4] = 5;
                        return rint10;
                    case 2:
                        int[] rint11 = new int[10];
                        rint11[4] = 10;
                        return rint11;
                    case 3:
                        int[] rint12 = new int[10];
                        rint12[4] = 15;
                        return rint12;
                    case 4:
                        int[] rint13 = new int[10];
                        rint13[4] = 20;
                        return rint13;
                    case 5:
                        int[] rint14 = new int[10];
                        rint14[4] = 25;
                        return rint14;
                    case 6:
                        int[] rint15 = new int[10];
                        rint15[4] = 30;
                        rint15[6] = 2;
                        return rint15;
                    case 7:
                        int[] rint16 = new int[10];
                        rint16[4] = 40;
                        rint16[6] = 7;
                        return rint16;
                    default:
                        int[] rint17 = new int[10];
                        rint17[4] = 40;
                        rint17[6] = 12;
                        return rint17;
                }
            case 2:
                switch (level) {
                    case 0:
                        return rint;
                    case 1:
                        int[] rint18 = new int[10];
                        rint18[5] = 3;
                        return rint18;
                    case 2:
                        int[] rint19 = new int[10];
                        rint19[5] = 6;
                        return rint19;
                    case 3:
                        int[] rint20 = new int[10];
                        rint20[5] = 9;
                        return rint20;
                    case 4:
                        int[] rint21 = new int[10];
                        rint21[5] = 12;
                        return rint21;
                    case 5:
                        int[] rint22 = new int[10];
                        rint22[5] = 15;
                        return rint22;
                    case 6:
                        int[] rint23 = new int[10];
                        rint23[5] = 25;
                        rint23[7] = 1;
                        return rint23;
                    case 7:
                        int[] rint24 = new int[10];
                        rint24[5] = 40;
                        rint24[7] = 2;
                        return rint24;
                    default:
                        int[] rint25 = new int[10];
                        rint25[5] = 40;
                        rint25[7] = 3;
                        return rint25;
                }
            default:
                return rint;
        }
    }
}
