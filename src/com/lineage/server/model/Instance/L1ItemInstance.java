package com.lineage.server.model.Instance;

import com.lineage.echo.OpcodesClient;
import com.lineage.server.datatables.ExtraAttrWeaponTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.L1EquipmentTimer;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1AttrWeapon;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.templates.L1SuperCard;
import com.lineage.server.utils.RangeLong;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1ItemInstance extends L1Object {
    private static final Log _log = LogFactory.getLog(L1ItemInstance.class);
    private static final long serialVersionUID = 1;
    private int _acByMagic;
    private int _attrEnchantKind;
    private int _attrEnchantLevel;
    private int _bless;
    private int _char_objid;
    private int _chargeCount;
    private long _count;
    private int _dmgByMagic;
    private Timestamp _dueTime;
    private int _durability;
    private int _enchantLevel;
    private L1EquipmentTimer _equipmentTimer;
    private String _gamno;
    private int _hitByMagic;
    private int _holyDmgByMagic;
    private int _innNpcId;
    private boolean _isEquipped;
    private boolean _isEquippedTemp;
    private boolean _isHall;
    private boolean _isIdentified;
    private boolean _isMatch;
    private boolean _isNowLighting;
    private boolean _isRunning;
    private L1Item _item;
    private int _itemId;
    private int _itemOwnerId;
    private int _keyId;
    private String _killDeathName;
    private final LastStatus _lastStatus;
    private Timestamp _lastUsed;
    private int _lastWeight;
    private L1PcInstance _pc;
    private int _remainingTime;
    private Timestamp _time;
    private EnchantTimer _timer;
    private boolean proctect;
    private boolean proctect0;
    private boolean proctect1;

    public L1ItemInstance() {
        this._isEquipped = false;
        this._isEquippedTemp = false;
        this._isIdentified = false;
        this._isRunning = false;
        this._gamno = null;
        this._lastUsed = null;
        this._lastStatus = new LastStatus();
        this._acByMagic = 0;
        this._dmgByMagic = 0;
        this._holyDmgByMagic = 0;
        this._hitByMagic = 0;
        this._itemOwnerId = 0;
        this._isNowLighting = false;
        this._isMatch = false;
        this._char_objid = -1;
        this._time = null;
        this._keyId = 0;
        this._innNpcId = 0;
        this._killDeathName = null;
        this.proctect = false;
        this.proctect0 = false;
        this.proctect1 = false;
        this._count = serialVersionUID;
        this._enchantLevel = 0;
    }

    public L1ItemInstance(L1Item item, long count) {
        this();
        setItem(item);
        setCount(count);
    }

    public boolean isIdentified() {
        return this._isIdentified;
    }

    public void setIdentified(boolean identified) {
        this._isIdentified = identified;
    }

    public String getName() {
        return this._item.getName();
    }

    public String getName2() {
        return this._item.getName();
    }

    public long getCount() {
        return this._count;
    }

    public void setCount(long count) {
        this._count = count;
    }

    public String getGamNo() {
        return this._gamno;
    }

    public void setGamNo(String gamno) {
        this._gamno = gamno;
    }

    public boolean isEquipped() {
        return this._isEquipped;
    }

    public void setEquipped(boolean equipped) {
        this._isEquipped = equipped;
    }

    public L1Item getItem() {
        return this._item;
    }

    public void setItem(L1Item item) {
        this._item = item;
        this._itemId = item.getItemId();
    }

    public int getItemId() {
        return this._itemId;
    }

    public void setItemId(int itemId) {
        this._itemId = itemId;
    }

    public boolean isStackable() {
        return this._item.isStackable();
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) {
    }

    public int getEnchantLevel() {
        return this._enchantLevel;
    }

    public void setEnchantLevel(int enchantLevel) {
        this._enchantLevel = enchantLevel;
    }

    public int get_gfxid() {
        return this._item.getGfxId();
    }

    public int get_durability() {
        return this._durability;
    }

    public int getChargeCount() {
        return this._chargeCount;
    }

    public void setChargeCount(int i) {
        this._chargeCount = i;
    }

    public int getRemainingTime() {
        return this._remainingTime;
    }

    public void setRemainingTime(int i) {
        this._remainingTime = i;
    }

    public void setLastUsed(Timestamp t) {
        this._lastUsed = t;
    }

    public Timestamp getLastUsed() {
        return this._lastUsed;
    }

    public int getLastWeight() {
        return this._lastWeight;
    }

    public void setLastWeight(int weight) {
        this._lastWeight = weight;
    }

    public void setBless(int i) {
        this._bless = i;
    }

    public int getBless() {
        return this._bless;
    }

    public void setAttrEnchantKind(int i) {
        this._attrEnchantKind = i;
    }

    public int getAttrEnchantKind() {
        return this._attrEnchantKind;
    }

    public void setAttrEnchantLevel(int i) {
        this._attrEnchantLevel = i;
    }

    public int getAttrEnchantLevel() {
        return this._attrEnchantLevel;
    }

    public void set_durability(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 127) {
            i = 127;
        }
        this._durability = i;
    }

    public int getWeight() {
        if (getItem().getWeight() == 0) {
            return 0;
        }
        return (int) Math.max((getCount() * ((long) getItem().getWeight())) / 1000, (long) serialVersionUID);
    }

    public class LastStatus {
        public int attrEnchantKind;
        public int attrEnchantLevel;
        public int bless;
        public int chargeCount;
        public long count;
        public int durability;
        public int enchantLevel;
        public boolean isEquipped = false;
        public boolean isIdentified = true;
        public int itemId;
        public Timestamp lastUsed = null;
        public int remainingTime;

        public LastStatus() {
        }

        public void updateAll() {
            this.count = L1ItemInstance.this.getCount();
            this.itemId = L1ItemInstance.this.getItemId();
            this.isEquipped = L1ItemInstance.this.isEquipped();
            this.isIdentified = L1ItemInstance.this.isIdentified();
            this.enchantLevel = L1ItemInstance.this.getEnchantLevel();
            this.durability = L1ItemInstance.this.get_durability();
            this.chargeCount = L1ItemInstance.this.getChargeCount();
            this.remainingTime = L1ItemInstance.this.getRemainingTime();
            this.lastUsed = L1ItemInstance.this.getLastUsed();
            this.bless = L1ItemInstance.this.getBless();
            this.attrEnchantKind = L1ItemInstance.this.getAttrEnchantKind();
            this.attrEnchantLevel = L1ItemInstance.this.getAttrEnchantLevel();
        }

        public void updateCount() {
            this.count = L1ItemInstance.this.getCount();
        }

        public void updateItemId() {
            this.itemId = L1ItemInstance.this.getItemId();
        }

        public void updateEquipped() {
            this.isEquipped = L1ItemInstance.this.isEquipped();
        }

        public void updateIdentified() {
            this.isIdentified = L1ItemInstance.this.isIdentified();
        }

        public void updateEnchantLevel() {
            this.enchantLevel = L1ItemInstance.this.getEnchantLevel();
        }

        public void updateDuraility() {
            this.durability = L1ItemInstance.this.get_durability();
        }

        public void updateChargeCount() {
            this.chargeCount = L1ItemInstance.this.getChargeCount();
        }

        public void updateRemainingTime() {
            this.remainingTime = L1ItemInstance.this.getRemainingTime();
        }

        public void updateLastUsed() {
            this.lastUsed = L1ItemInstance.this.getLastUsed();
        }

        public void updateBless() {
            this.bless = L1ItemInstance.this.getBless();
        }

        public void updateAttrEnchantKind() {
            this.attrEnchantKind = L1ItemInstance.this.getAttrEnchantKind();
        }

        public void updateAttrEnchantLevel() {
            this.attrEnchantLevel = L1ItemInstance.this.getAttrEnchantLevel();
        }
    }

    public LastStatus getLastStatus() {
        return this._lastStatus;
    }

    public int getRecordingColumns() {
        int column = 0;
        if (getCount() != this._lastStatus.count) {
            column = 0 + 16;
        }
        if (getItemId() != this._lastStatus.itemId) {
            column += 64;
        }
        if (isEquipped() != this._lastStatus.isEquipped) {
            column += 8;
        }
        if (getEnchantLevel() != this._lastStatus.enchantLevel) {
            column += 4;
        }
        if (get_durability() != this._lastStatus.durability) {
            column++;
        }
        if (getChargeCount() != this._lastStatus.chargeCount) {
            column += 128;
        }
        if (getLastUsed() != this._lastStatus.lastUsed) {
            column += 32;
        }
        if (isIdentified() != this._lastStatus.isIdentified) {
            column += 2;
        }
        if (getRemainingTime() != this._lastStatus.remainingTime) {
            column += 256;
        }
        if (getBless() != this._lastStatus.bless) {
            column += 512;
        }
        if (getAttrEnchantKind() != this._lastStatus.attrEnchantKind) {
            column += 1024;
        }
        if (getAttrEnchantLevel() != this._lastStatus.attrEnchantLevel) {
            return column + L1PcInventory.COL_ATTR_ENCHANT_LEVEL;
        }
        return column;
    }

    public String getNumberedViewName(long count) {
        StringBuilder name = new StringBuilder(getNumberedName(count, true));
        if (this._time != null) {
            name.append("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this._time) + "]");
        }
        if (isEquipped()) {
            L1SuperCard card = null;
            L1Item item = getItem();
            if (item != null) {
                card = item.getCard();
            }
            if (card != null) {
                name.append(" ($117)");
            }
        }
        switch (this._item.getUseType()) {
            case OpcodesClient.C_OPCODE_HOTEL_ENTER:
                if (isEquipped()) {
                    name.append(" ($117)");
                    break;
                }
                break;
            case OpcodesClient.C_OPCODE_HORUN:
                L1Pet pet = PetReading.get().getTemplate(getId());
                if (pet != null) {
                    name.append("[Lv." + pet.get_level() + "]" + pet.get_name() + " HP" + pet.get_hp() + " " + NpcTable.get().getTemplate(pet.get_npcid()).get_nameid());
                    break;
                }
                break;
            case 1:
                if (isEquipped()) {
                    name.append(" ($9)");
                    break;
                }
                break;
            case 2:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 37:
            case 40:
            case 43:
            case 44:
            case 45:
            case 47:
            case 48:
                if (isEquipped()) {
                    name.append(" ($117)");
                    break;
                }
                break;
            case 10:
                if (isNowLighting()) {
                    name.append(" ($10)");
                }
                switch (this._item.getItemId()) {
                    case 40001:
                    case 40002:
                        if (getRemainingTime() <= 0) {
                            name.append(" ($11)");
                            break;
                        }
                        break;
                }
            default:
                if (isEquippedTemp()) {
                    name.append(" ($117)");
                    break;
                }
                break;
        }
        return name.toString();
    }

    public String getViewName() {
        return getNumberedViewName(this._count);
    }

    public String getLogName() {
        return getNumberedName(this._count, true);
    }

    public String getAllName() {
        return getNumberedName(this._count, false);
    }

    public String getNumberedName(long count, boolean mode) {
        StringBuilder name = new StringBuilder();
        if (isIdentified()) {
            switch (this._item.getUseType()) {
                case 1:
                    if (getAttrEnchantLevel() > 0) {
                        name.append(((Object) attrEnchantLevel()) + " ");
                    }
                    if (getEnchantLevel() < 0) {
                        if (getEnchantLevel() < 0) {
                            name.append(String.valueOf(String.valueOf(getEnchantLevel())) + " ");
                            break;
                        }
                    } else {
                        name.append("+" + getEnchantLevel() + " ");
                        break;
                    }
                    break;
                case 2:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 37:
                case 40:
                    if (getEnchantLevel() < 0) {
                        if (getEnchantLevel() < 0) {
                            name.append(String.valueOf(String.valueOf(getEnchantLevel())) + " ");
                            break;
                        }
                    } else {
                        name.append("+" + getEnchantLevel() + " ");
                        break;
                    }
                    break;
            }
        }
        if (mode) {
            name.append(this._item.getIdentifiedNameId());
        } else {
            name.append(this._item.getUnidentifiedNameId());
        }
        if (this._item.getUseType() == -5) {
            name.append("\\f_[" + getGamNo() + "]");
        }
        if (isIdentified()) {
            if (getItem().getMaxChargeCount() <= 0) {
                switch (this._item.getItemId()) {
                    case 20383:
                        name.append(" (" + getChargeCount() + ")");
                        break;
                }
            } else {
                name.append(" (" + getChargeCount() + ")");
            }
            if (getItem().getMaxUseTime() > 0 && getItem().getType2() != 0) {
                name.append(" (" + getRemainingTime() + ")");
            }
        }
        if (getItem().getItemId() == 40312 && getKeyId() != 0) {
            name.append(getInnKeyName());
        }
        if (count > serialVersionUID) {
            if (count < 1000000000) {
                name.append(" (" + count + ")");
            } else {
                name.append(" (" + ((Object) RangeLong.scount(count)) + ")");
            }
        }
        return name.toString();
    }

    private StringBuilder attrEnchantLevel() {
        StringBuilder attrEnchant = new StringBuilder();
        L1AttrWeapon attrWeapon = ExtraAttrWeaponTable.getInstance().get(getAttrEnchantKind(), getAttrEnchantLevel());
        if (attrWeapon != null) {
            attrEnchant.append(attrWeapon.getName());
        }
        return attrEnchant;
    }

    public byte[] getStatusBytes() {
        return new L1ItemStatus(this).getStatusBytes().getBytes();
    }

    public int getMr() {
        return new L1ItemPower(this).getMr();
    }

    public void greater(L1PcInstance owner, boolean equipment) {
        new L1ItemPower(this).greater(owner, equipment);
    }

    /* access modifiers changed from: package-private */
    public class EnchantTimer extends TimerTask {
        public EnchantTimer() {
        }

        public void run() {
            try {
                int type = L1ItemInstance.this.getItem().getType();
                int type2 = L1ItemInstance.this.getItem().getType2();
                int objid = L1ItemInstance.this.getId();
                if (L1ItemInstance.this._pc != null && L1ItemInstance.this._pc.getInventory().getItem(objid) != null && type == 2 && type2 == 2 && L1ItemInstance.this.isEquipped()) {
                    L1ItemInstance.this._pc.addAc(3);
                    L1ItemInstance.this._pc.sendPackets(new S_OwnCharStatus(L1ItemInstance.this._pc));
                }
                L1ItemInstance.this.setAcByMagic(0);
                L1ItemInstance.this.setDmgByMagic(0);
                L1ItemInstance.this.setHolyDmgByMagic(0);
                L1ItemInstance.this.setHitByMagic(0);
                L1ItemInstance.this._pc.sendPackets(new S_ServerMessage(308, L1ItemInstance.this.getLogName()));
                L1ItemInstance.this._isRunning = false;
                L1ItemInstance.this._timer = null;
            } catch (Exception e) {
                L1ItemInstance._log.warn("EnchantTimer: " + L1ItemInstance.this.getItemId());
            }
        }
    }

    public int getAcByMagic() {
        return this._acByMagic;
    }

    public void setAcByMagic(int i) {
        this._acByMagic = i;
    }

    public int getDmgByMagic() {
        return this._dmgByMagic;
    }

    public void setDmgByMagic(int i) {
        this._dmgByMagic = i;
    }

    public int getHolyDmgByMagic() {
        return this._holyDmgByMagic;
    }

    public void setHolyDmgByMagic(int i) {
        this._holyDmgByMagic = i;
    }

    public int getHitByMagic() {
        return this._hitByMagic;
    }

    public void setHitByMagic(int i) {
        this._hitByMagic = i;
    }

    public void setSkillArmorEnchant(L1PcInstance pc, int skillId, int skillTime) {
        int type = getItem().getType();
        int type2 = getItem().getType2();
        if (this._isRunning) {
            this._timer.cancel();
            int objid = getId();
            if (pc != null && pc.getInventory().getItem(objid) != null && type == 2 && type2 == 2 && isEquipped()) {
                pc.addAc(3);
                pc.sendPackets(new S_OwnCharStatus(pc));
            }
            setAcByMagic(0);
            this._isRunning = false;
            this._timer = null;
        }
        if (type == 2 && type2 == 2 && isEquipped()) {
            pc.addAc(-3);
            pc.sendPackets(new S_OwnCharStatus(pc));
        }
        setAcByMagic(3);
        this._pc = pc;
        this._char_objid = this._pc.getId();
        this._timer = new EnchantTimer();
        new Timer().schedule(this._timer, (long) skillTime);
        this._isRunning = true;
    }

    public void setSkillWeaponEnchant(L1PcInstance pc, int skillId, int skillTime) {
        if (getItem().getType2() == 1) {
            if (this._isRunning) {
                this._timer.cancel();
                setDmgByMagic(0);
                setHolyDmgByMagic(0);
                setHitByMagic(0);
                this._isRunning = false;
                this._timer = null;
            }
            switch (skillId) {
                case 8:
                    setHolyDmgByMagic(1);
                    setHitByMagic(1);
                    break;
                case 12:
                    setDmgByMagic(2);
                    break;
                case 48:
                    setDmgByMagic(2);
                    setHitByMagic(2);
                    break;
                case 107:
                    setDmgByMagic(5);
                    break;
            }
            this._pc = pc;
            this._char_objid = this._pc.getId();
            this._timer = new EnchantTimer();
            new Timer().schedule(this._timer, (long) skillTime);
            this._isRunning = true;
        }
    }

    public int getItemOwnerId() {
        return this._itemOwnerId;
    }

    public void setItemOwnerId(int i) {
        this._itemOwnerId = i;
    }

    public void startEquipmentTimer(L1PcInstance pc) {
        if (getRemainingTime() > 0) {
            this._equipmentTimer = new L1EquipmentTimer(pc, this);
            new Timer(true).scheduleAtFixedRate(this._equipmentTimer, 1000, 1000);
        }
    }

    public void stopEquipmentTimer(L1PcInstance pc) {
        if (getRemainingTime() > 0) {
            this._equipmentTimer.cancel();
            this._equipmentTimer = null;
        }
    }

    public boolean isNowLighting() {
        return this._isNowLighting;
    }

    public void setNowLighting(boolean flag) {
        this._isNowLighting = flag;
    }

    public boolean isEquippedTemp() {
        return this._isEquippedTemp;
    }

    public void set_isEquippedTemp(boolean isEquippedTemp) {
        this._isEquippedTemp = isEquippedTemp;
    }

    public void setIsMatch(boolean isMatch) {
        this._isMatch = isMatch;
    }

    public boolean isMatch() {
        return this._isMatch;
    }

    public void set_char_objid(int char_objid) {
        this._char_objid = char_objid;
    }

    public int get_char_objid() {
        return this._char_objid;
    }

    public void set_time(Timestamp time) {
        this._time = time;
    }

    public Timestamp get_time() {
        return this._time;
    }

    public boolean isRunning() {
        return this._timer != null;
    }

    public String getNumberedName_to_String() {
        L1AttrWeapon attrWeapon;
        StringBuilder name = new StringBuilder();
        switch (this._item.getUseType()) {
            case 1:
                int attrEnchantLevel = getAttrEnchantLevel();
                if (attrEnchantLevel > 0 && (attrWeapon = ExtraAttrWeaponTable.getInstance().get(getAttrEnchantKind(), attrEnchantLevel)) != null) {
                    name.append(attrWeapon.getName());
                }
                if (getEnchantLevel() < 0) {
                    if (getEnchantLevel() < 0) {
                        name.append(String.valueOf(String.valueOf(getEnchantLevel())) + " ");
                        break;
                    }
                } else {
                    name.append("+" + getEnchantLevel() + " ");
                    break;
                }
                break;
            case 2:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 37:
            case 40:
                if (getEnchantLevel() < 0) {
                    if (getEnchantLevel() < 0) {
                        name.append(String.valueOf(String.valueOf(getEnchantLevel())) + " ");
                        break;
                    }
                } else {
                    name.append("+" + getEnchantLevel() + " ");
                    break;
                }
                break;
        }
        name.append(this._item.getName());
        if (getItem().getMaxChargeCount() <= 0) {
            switch (this._item.getItemId()) {
                case 20383:
                    name.append(" (" + getChargeCount() + ")");
                    break;
            }
        } else {
            name.append(" (" + getChargeCount() + ")");
        }
        long count = getCount();
        if (count > serialVersionUID) {
            if (count < 1000000000) {
                name.append(" (" + count + ")");
            } else {
                name.append(" (" + ((Object) RangeLong.scount(count)) + ")");
            }
        }
        return name.toString();
    }

    public int getKeyId() {
        return this._keyId;
    }

    public void setKeyId(int i) {
        this._keyId = i;
    }

    public int getInnNpcId() {
        return this._innNpcId;
    }

    public void setInnNpcId(int i) {
        this._innNpcId = i;
    }

    public boolean checkRoomOrHall() {
        return this._isHall;
    }

    public void setHall(boolean i) {
        this._isHall = i;
    }

    public Timestamp getDueTime() {
        return this._dueTime;
    }

    public void setDueTime(Timestamp i) {
        this._dueTime = i;
    }

    public String getInnKeyName() {
        StringBuilder name = new StringBuilder();
        name.append(" #");
        String chatText = String.valueOf(getKeyId());
        String s1 = "";
        int i = 0;
        while (i < chatText.length() && i < 5) {
            s1 = String.valueOf(s1) + String.valueOf(chatText.charAt(i));
            i++;
        }
        name.append(s1);
        for (int i2 = 0; i2 < chatText.length(); i2++) {
            if (i2 % 2 == 0) {
                s1 = String.valueOf(chatText.charAt(i2));
            } else {
                name.append(Integer.toHexString(Integer.valueOf(String.valueOf(s1) + String.valueOf(chatText.charAt(i2))).intValue()).toLowerCase());
            }
        }
        return name.toString();
    }

    public void setKillDeathName(String kname) {
        this._killDeathName = kname;
    }

    public String getKillDeathName() {
        return this._killDeathName;
    }

    public boolean getproctect() {
        return this.proctect;
    }

    public void setproctect(boolean i) {
        this.proctect = i;
    }

    public boolean getproctect0() {
        return this.proctect0;
    }

    public void setproctect0(boolean i) {
        this.proctect0 = i;
    }

    public boolean getproctect1() {
        return this.proctect1;
    }

    public void setproctect1(boolean i) {
        this.proctect1 = i;
    }
}
