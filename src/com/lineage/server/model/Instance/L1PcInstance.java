package com.lineage.server.model.Instance;

import com.eric.gui.J_Main;
import com.lineage.config.Config;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigKill;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRate;
import com.lineage.data.event.OnlineGiftSet;
import com.lineage.data.quest.Chapter01R;
import com.lineage.echo.ClientExecutor;
import com.lineage.echo.EncryptExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.datatables.MapLevelTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.datatables.lock.CharBuffReading;
import com.lineage.server.datatables.lock.CharOtherReading;
import com.lineage.server.datatables.lock.CharSkillReading;
import com.lineage.server.datatables.sql.AccountTable;
import com.lineage.server.datatables.sql.CharacterTable;
import com.lineage.server.model.L1ActionPc;
import com.lineage.server.model.L1ActionPet;
import com.lineage.server.model.L1ActionSummon;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1ChatParty;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1CountQuest;
import com.lineage.server.model.L1DwarfForElfInventory;
import com.lineage.server.model.L1DwarfInventory;
import com.lineage.server.model.L1EquipmentSlot;
import com.lineage.server.model.L1ExcludingList;
import com.lineage.server.model.L1EzpayQuest;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.model.L1Karma;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1Map_Quest;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1PcQuest;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.L1War;
import com.lineage.server.model.TimeInform;
import com.lineage.server.model.classes.L1ClassFeature;
import com.lineage.server.model.monitor.L1PcInvisDelay;
import com.lineage.server.model.monitor.L1PcMonitor;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_BlueMessage;
import com.lineage.server.serverpackets.S_Bonusstats;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_DelSkill;
import com.lineage.server.serverpackets.S_Disconnect;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_Exp;
import com.lineage.server.serverpackets.S_Fishing;
import com.lineage.server.serverpackets.S_HPMeter;
import com.lineage.server.serverpackets.S_HPUpdate;
import com.lineage.server.serverpackets.S_Invis;
import com.lineage.server.serverpackets.S_KillMessage;
import com.lineage.server.serverpackets.S_Lawful;
import com.lineage.server.serverpackets.S_Liquor;
import com.lineage.server.serverpackets.S_MPUpdate;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_OtherCharPacks;
import com.lineage.server.serverpackets.S_OwnCharAttrDef;
import com.lineage.server.serverpackets.S_OwnCharStatus;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_PacketBoxParty;
import com.lineage.server.serverpackets.S_PacketBoxProtection;
import com.lineage.server.serverpackets.S_Poison;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_SPMR;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.serverpackets.ServerBasePacket;
import com.lineage.server.storage.mysql.MySqlCharacterStorage;
import com.lineage.server.templates.L1PcOther;
import com.lineage.server.templates.L1PcOtherList;
import com.lineage.server.templates.L1PrivateShopBuyList;
import com.lineage.server.templates.L1PrivateShopSellList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.templates.L1TradeItem;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.types.Point;
import com.lineage.server.utils.CalcInitHpMp;
import com.lineage.server.utils.CalcStat;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldQuest;
import com.lineage.server.world.WorldWar;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.Reward;

public class L1PcInstance extends L1Character {
    private static final Log _log = LogFactory.getLog(L1PcInstance.class);

    private static final long serialVersionUID = 1L;

    public static final int CLASSID_KNIGHT_MALE = 61;

    public static final int CLASSID_KNIGHT_FEMALE = 48;

    public static final int CLASSID_ELF_MALE = 138;

    public static final int CLASSID_ELF_FEMALE = 37;

    public static final int CLASSID_WIZARD_MALE = 734;

    public static final int CLASSID_WIZARD_FEMALE = 1186;

    public static final int CLASSID_DARK_ELF_MALE = 2786;

    public static final int CLASSID_DARK_ELF_FEMALE = 2796;

    public static final int CLASSID_PRINCE = 0;

    public static final int CLASSID_PRINCESS = 1;

    public static final int CLASSID_DRAGON_KNIGHT_MALE = 6658;

    public static final int CLASSID_DRAGON_KNIGHT_FEMALE = 6661;

    public static final int CLASSID_ILLUSIONIST_MALE = 6671;

    public static final int CLASSID_ILLUSIONIST_FEMALE = 6650;

    private static Random _random = new Random();

    private boolean _isKill = false;

    public boolean is_isKill() {
        return this._isKill;
    }

    public void set_isKill(boolean _isKill) {
        this._isKill = _isKill;
    }

    private short _hpr = 0;

    private short _trueHpr = 0;

    public short getHpr() {
        return this._hpr;
    }

    public void addHpr(int i) {
        this._trueHpr = (short)(this._trueHpr + i);
        this._hpr = (short)Math.max(0, this._trueHpr);
    }

    private short _mpr = 0;

    private short _trueMpr = 0;

    public short getMpr() {
        return this._mpr;
    }

    public void addMpr(int i) {
        this._trueMpr = (short)(this._trueMpr + i);
        this._mpr = (short)Math.max(0, this._trueMpr);
    }

    public short _originalHpr = 0;

    public short getOriginalHpr() {
        return this._originalHpr;
    }

    public short _originalMpr = 0;

    private boolean _mpRegenActive;

    private boolean _mpReductionActiveByAwake;

    private boolean _hpRegenActive;

    public short getOriginalMpr() {
        return this._originalMpr;
    }

    private int _hpRegenType = 0;

    private int _hpRegenState = 4;

    public int getHpRegenState() {
        return this._hpRegenState;
    }

    public void set_hpRegenType(int hpRegenType) {
        this._hpRegenType = hpRegenType;
    }

    public int hpRegenType() {
        return this._hpRegenType;
    }

    private int regenMax() {
        int[] lvlTable = {
                30, 25, 20, 16, 14, 12, 11, 10, 9, 3,
                2 };
        int regenLvl = Math.min(10, getLevel());
        if (30 <= getLevel() && isKnight())
            regenLvl = 11;
        return lvlTable[regenLvl - 1] << 2;
    }

    public boolean isRegenHp() {
        if (!this._hpRegenActive)
            return false;
        if (hasSkillEffect(169) || hasSkillEffect(176))
            return (this._hpRegenType >= regenMax());
        if (120 <= this._inventory.getWeight240())
            return false;
        if (this._food < 3)
            return false;
        return (this._hpRegenType >= regenMax());
    }

    private int _mpRegenType = 0;

    private int _mpRegenState = 4;

    public static final int REGENSTATE_NONE = 4;

    public static final int REGENSTATE_MOVE = 2;

    public static final int REGENSTATE_ATTACK = 1;

    public static final int INTERVAL_BY_AWAKE = 4;

    public int getMpRegenState() {
        return this._mpRegenState;
    }

    public void set_mpRegenType(int hpmpRegenType) {
        this._mpRegenType = hpmpRegenType;
    }

    public int mpRegenType() {
        return this._mpRegenType;
    }

    public boolean isRegenMp() {
        if (!this._mpRegenActive)
            return false;
        if (hasSkillEffect(169) || hasSkillEffect(176))
            return (this._mpRegenType >= 64);
        if (120 <= this._inventory.getWeight240())
            return false;
        if (this._food < 3)
            return false;
        return (this._mpRegenType >= 64);
    }

    public void setRegenState(int state) {
        this._mpRegenState = state;
        this._hpRegenState = state;
    }

    public void startHpRegeneration() {
        if (!this._hpRegenActive)
            this._hpRegenActive = true;
    }

    public void stopHpRegeneration() {
        if (this._hpRegenActive)
            this._hpRegenActive = false;
    }

    public boolean getHpRegeneration() {
        return this._hpRegenActive;
    }

    public void startMpRegeneration() {
        if (!this._mpRegenActive)
            this._mpRegenActive = true;
    }

    public void stopMpRegeneration() {
        if (this._mpRegenActive)
            this._mpRegenActive = false;
    }

    public boolean getMpRegeneration() {
        return this._mpRegenActive;
    }

    private int _awakeMprTime = 0;

    public int get_awakeMprTime() {
        return this._awakeMprTime;
    }

    public void set_awakeMprTime(int awakeMprTime) {
        this._awakeMprTime = awakeMprTime;
    }

    public void startMpReductionByAwake() {
        if (!this._mpReductionActiveByAwake) {
            set_awakeMprTime(4);
            this._mpReductionActiveByAwake = true;
        }
    }

    public void stopMpReductionByAwake() {
        if (this._mpReductionActiveByAwake) {
            set_awakeMprTime(0);
            this._mpReductionActiveByAwake = false;
        }
    }

    public boolean isMpReductionActiveByAwake() {
        return this._mpReductionActiveByAwake;
    }

    private int _awakeSkillId = 0;

    private int _old_lawful;

    public int getAwakeSkillId() {
        return this._awakeSkillId;
    }

    public void setAwakeSkillId(int i) {
        this._awakeSkillId = i;
    }

    public void startObjectAutoUpdate() {
        removeAllKnownObjects();
    }

    public void stopEtcMonitor() {
        set_ghostTime(-1);
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(false);
        stopMpReductionByAwake();
        if (ServerUseMapTimer.MAP.get(this) != null)
            ServerUseMapTimer.MAP.remove(this);
        OnlineGiftSet.remove(this);
        ListMapUtil.clear(this._skillList);
        ListMapUtil.clear(this._sellList);
        ListMapUtil.clear(this._buyList);
        ListMapUtil.clear(this._trade_items);
    }

    public int getLawfulo() {
        return this._old_lawful;
    }

    public void onChangeLawful() {
        if (this._old_lawful != getLawful()) {
            this._old_lawful = getLawful();
            sendPacketsAll((ServerBasePacket)new S_Lawful(this));
            lawfulUpdate();
        }
    }

    private boolean _jl1 = false;

    private boolean _jl2 = false;

    private boolean _jl3 = false;

    private boolean _el1 = false;

    private boolean _el2 = false;

    private boolean _el3 = false;

    private long _old_exp;

    private void lawfulUpdate() {
        int l = getLawful();
        if (l >= 10000 && l <= 19999) {
            if (!this._jl1) {
                overUpdate();
                this._jl1 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(0, 1));
                sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (l >= 20000 && l <= 29999) {
            if (!this._jl2) {
                overUpdate();
                this._jl2 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(1, 1));
                sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (l >= 30000 && l <= 39999) {
            if (!this._jl3) {
                overUpdate();
                this._jl3 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(2, 1));
                sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (l >= -19999 && l <= -10000) {
            if (!this._el1) {
                overUpdate();
                this._el1 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(3, 1));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (l >= -29999 && l <= -20000) {
            if (!this._el2) {
                overUpdate();
                this._el2 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(4, 1));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (l >= -39999 && l <= -30000) {
            if (!this._el3) {
                overUpdate();
                this._el3 = true;
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(5, 1));
                sendPackets((ServerBasePacket)new S_SPMR(this));
            }
        } else if (overUpdate()) {
            sendPackets((ServerBasePacket)new S_OwnCharAttrDef(this));
            sendPackets((ServerBasePacket)new S_SPMR(this));
        }
    }

    private boolean overUpdate() {
        if (this._jl1) {
            this._jl1 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(0, 0));
            return true;
        }
        if (this._jl2) {
            this._jl2 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(1, 0));
            return true;
        }
        if (this._jl3) {
            this._jl3 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(2, 0));
            return true;
        }
        if (this._el1) {
            this._el1 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(3, 0));
            return true;
        }
        if (this._el2) {
            this._el2 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(4, 0));
            return true;
        }
        if (this._el3) {
            this._el3 = false;
            sendPackets((ServerBasePacket)new S_PacketBoxProtection(5, 0));
            return true;
        }
        return false;
    }

    private boolean isEncounter() {
        if (getLevel() <= ConfigOther.ENCOUNTER_LV)
            return true;
        return false;
    }

    public int guardianEncounter() {
        if (this._jl1)
            return 0;
        if (this._jl2)
            return 1;
        if (this._jl3)
            return 2;
        if (this._el1)
            return 3;
        if (this._el2)
            return 4;
        if (this._el3)
            return 5;
        return -1;
    }

    public long getExpo() {
        return this._old_exp;
    }

    public void onChangeExp() {
        if (this._old_exp != getExp()) {
            this._old_exp = getExp();
            int level = ExpTable.getLevelByExp(getExp());
            int char_level = getLevel();
            int gap = level - char_level;
            if (gap == 0) {
                if (level <= 127) {
                    sendPackets((ServerBasePacket)new S_Exp(this));
                } else {
                    sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
                }
                return;
            }
            if (gap > 0) {
                levelUp(gap);
            } else if (gap < 0) {
                levelDown(gap);
            }
            if (getLevel() > 20) {
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(6, 0));
            } else {
                sendPackets((ServerBasePacket)new S_PacketBoxProtection(6, 1));
            }
        }
    }

    public void onPerceive(L1PcInstance perceivedFrom) {
        try {
            if (perceivedFrom.getMapId() >= 16384 && perceivedFrom.getMapId() <= 25088 &&
                    perceivedFrom.getInnKeyId() != getInnKeyId())
                return;
            if (isGmInvis() || isGhost() || isInvisble())
                return;
            if (perceivedFrom.get_showId() != get_showId())
                return;
            perceivedFrom.addKnownObject((L1Object)this);
            perceivedFrom.sendPackets((ServerBasePacket)new S_OtherCharPacks(this));
            if (isInParty() && getParty().isMember(perceivedFrom))
                perceivedFrom.sendPackets((ServerBasePacket)new S_HPMeter(this));
            if (this._isFishing)
                perceivedFrom.sendPackets((ServerBasePacket)new S_Fishing(getId(), 71, get_fishX(), get_fishY()));
            if (isPrivateShop()) {
                int mapId = getMapId();
                if (mapId != 340 && mapId != 350 && mapId != 360 && mapId != 370) {
                    getSellList().clear();
                    getBuyList().clear();
                    setPrivateShop(false);
                    sendPacketsAll((ServerBasePacket)new S_DoActionGFX(getId(), 3));
                } else {
                    perceivedFrom.sendPackets((ServerBasePacket)new S_DoActionShop(getId(), getShopChat()));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void removeOutOfRangeObjects() {
        for (L1Object known : getKnownObjects()) {
            if (known == null)
                continue;
            if (Config.PC_RECOGNIZE_RANGE == -1) {
                if (!getLocation().isInScreen((Point)known.getLocation())) {
                    removeKnownObject(known);
                    sendPackets((ServerBasePacket)new S_RemoveObject(known));
                }
                continue;
            }
            if (getLocation().getTileLineDistance((Point)known.getLocation()) > Config.PC_RECOGNIZE_RANGE) {
                removeKnownObject(known);
                sendPackets((ServerBasePacket)new S_RemoveObject(known));
            }
        }
    }

    public void updateObject() {
        if (getOnlineStatus() != 1)
            return;
        removeOutOfRangeObjects();
        for (L1Object visible : World.get().getVisibleObjects((L1Object)this, Config.PC_RECOGNIZE_RANGE)) {
            if (visible instanceof L1MerchantInstance) {
                if (!knownsObject(visible)) {
                    L1MerchantInstance npc = (L1MerchantInstance)visible;
                    npc.onPerceive(this);
                }
                continue;
            }
            if (visible instanceof L1DwarfInstance) {
                if (!knownsObject(visible)) {
                    L1DwarfInstance npc = (L1DwarfInstance)visible;
                    npc.onPerceive(this);
                }
                continue;
            }
            if (visible instanceof L1FieldObjectInstance) {
                if (!knownsObject(visible)) {
                    L1FieldObjectInstance npc = (L1FieldObjectInstance)visible;
                    npc.onPerceive(this);
                }
                continue;
            }
            if (visible.get_showId() != get_showId())
                continue;
            if (!knownsObject(visible)) {
                visible.onPerceive(this);
            } else if (visible instanceof L1NpcInstance) {
                L1NpcInstance npc = (L1NpcInstance)visible;
                if (getLocation().isInScreen((Point)npc.getLocation()) && npc.getHiddenStatus() != 0)
                    npc.approachPlayer(this);
            }
            if (isHpBarTarget(visible)) {
                L1Character cha = (L1Character)visible;
                cha.broadcastPacketHP(this);
            }
            if (hasSkillEffect(2001) &&
                    isGmHpBarTarget(visible)) {
                L1Character cha = (L1Character)visible;
                cha.broadcastPacketHP(this);
            }
        }
    }

    public boolean isHpBarTarget(L1Object obj) {
        switch (getMapId()) {
            case 400:
                if (obj instanceof L1FollowerInstance) {
                    L1FollowerInstance follower = (L1FollowerInstance)obj;
                    if (follower.getMaster().equals(this))
                        return true;
                }
                break;
        }
        return false;
    }

    public boolean isGmHpBarTarget(L1Object obj) {
        if (obj instanceof L1MonsterInstance)
            return true;
        if (obj instanceof L1PcInstance)
            return true;
        if (obj instanceof L1SummonInstance)
            return true;
        if (obj instanceof L1PetInstance)
            return true;
        if (obj instanceof L1FollowerInstance)
            return true;
        return false;
    }

    private void sendVisualEffect() {
        int poisonId = 0;
        if (getPoison() != null)
            poisonId = getPoison().getEffectId();
        if (getParalysis() != null)
            poisonId = getParalysis().getEffectId();
        if (poisonId != 0)
            sendPacketsAll((ServerBasePacket)new S_Poison(getId(), poisonId));
    }

    public void sendVisualEffectAtLogin() {
        sendVisualEffect();
    }

    private boolean _isCHAOTIC = false;

    public boolean isCHAOTIC() {
        return this._isCHAOTIC;
    }

    public void setCHAOTIC(boolean flag) {
        this._isCHAOTIC = flag;
    }

    public void sendVisualEffectAtTeleport() {
        if (isDrink())
            sendPackets((ServerBasePacket)new S_Liquor(getId()));
        if (isCHAOTIC())
            sendPackets((ServerBasePacket)new S_Liquor(getId(), 2));
        sendVisualEffect();
    }

    private ArrayList<Integer> _skillList = new ArrayList<>();

    private L1ClassFeature _classFeature;

    private int _PKcount;

    private int _PkCountForElf;

    private int _clanid;

    private String clanname;

    private int _clanRank;

    private byte _sex;

    private ArrayList<L1PrivateShopSellList> _sellList;

    private ArrayList<L1PrivateShopBuyList> _buyList;

    private byte[] _shopChat;

    private boolean _isPrivateShop;

    private boolean _isTradingInPrivateShop;

    private int _partnersPrivateShopItemCount;

    private EncryptExecutor _out;

    public void setSkillMastery(int skillid) {
        if (!this._skillList.contains(new Integer(skillid)))
            this._skillList.add(new Integer(skillid));
    }

    public void removeSkillMastery(int skillid) {
        if (this._skillList.contains(new Integer(skillid)))
            this._skillList.remove(new Integer(skillid));
    }

    public boolean isSkillMastery(int skillid) {
        return this._skillList.contains(new Integer(skillid));
    }

    public void clearSkillMastery() {
        this._skillList.clear();
    }

    public void setNpcSpeed() {
        try {
            if (!getDolls().isEmpty()) {
                byte b;
                int i;
                Object[] arrayOfObject;
                for (i = (arrayOfObject = getDolls().values().toArray()).length, b = 0; b < i; ) {
                    Object obj = arrayOfObject[b];
                    L1DollInstance doll = (L1DollInstance)obj;
                    if (doll != null)
                        doll.setNpcMoveSpeed();
                    b++;
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setCurrentHp(int i) {
        int currentHp = Math.min(i, getMaxHp());
        if (getCurrentHp() == currentHp)
            return;
        if (currentHp <= 0)
            if (isGm()) {
                currentHp = getMaxHp();
            } else if (!isDead()) {
                death((L1Character)null);
            }
        setCurrentHpDirect(currentHp);
        sendPackets((ServerBasePacket)new S_HPUpdate(currentHp, getMaxHp()));
        if (isInParty())
            getParty().updateMiniHP(this);
    }

    public void setCurrentMp(int i) {
        int currentMp = Math.min(i, getMaxMp());
        if (getCurrentMp() == currentMp)
            return;
        setCurrentMpDirect(currentMp);
        sendPackets((ServerBasePacket)new S_MPUpdate(currentMp, getMaxMp()));
    }

    public L1PcInventory getInventory() {
        return this._inventory;
    }

    public L1DwarfInventory getDwarfInventory() {
        return this._dwarf;
    }

    public L1DwarfForElfInventory getDwarfForElfInventory() {
        return this._dwarfForElf;
    }

    public boolean isGmInvis() {
        return this._gmInvis;
    }

    public void setGmInvis(boolean flag) {
        this._gmInvis = flag;
    }

    public int getCurrentWeapon() {
        return this._currentWeapon;
    }

    public void setCurrentWeapon(int i) {
        this._currentWeapon = i;
    }

    public int getType() {
        return this._type;
    }

    public void setType(int i) {
        this._type = i;
    }

    public short getAccessLevel() {
        return this._accessLevel;
    }

    public void setAccessLevel(short i) {
        this._accessLevel = i;
    }

    public int getClassId() {
        return this._classId;
    }

    public void setClassId(int i) {
        this._classId = i;
        this._classFeature = L1ClassFeature.newClassFeature(i);
    }

    public L1PcInstance() {
        this._classFeature = null;
        this._sellList = new ArrayList<>();
        this._buyList = new ArrayList<>();
        this._isPrivateShop = false;
        this._isTradingInPrivateShop = false;
        this._partnersPrivateShopItemCount = 0;
        this._oldTime = 0L;
        this._originalEr = 0;
        this._netConnection = null;
        this._karma = new L1Karma();
        this._isTeleport = false;
        this._isDrink = false;
        this._isGres = false;
        this._isPinkName = false;
        this._baseMaxHp = 0;
        this._baseMaxMp = 0;
        this._baseAc = 0;
        this._originalAc = 0;
        this._baseStr = 0;
        this._baseCon = 0;
        this._baseDex = 0;
        this._baseCha = 0;
        this._baseInt = 0;
        this._baseWis = 0;
        this._originalStr = 0;
        this._originalCon = 0;
        this._originalDex = 0;
        this._originalCha = 0;
        this._originalInt = 0;
        this._originalWis = 0;
        this._originalDmgup = 0;
        this._originalBowDmgup = 0;
        this._originalHitup = 0;
        this._originalBowHitup = 0;
        this._originalMr = 0;
        this._originalMagicHit = 0;
        this._originalMagicCritical = 0;
        this._originalMagicConsumeReduction = 0;
        this._originalMagicDamage = 0;
        this._originalHpup = 0;
        this._originalMpup = 0;
        this._baseDmgup = 0;
        this._baseBowDmgup = 0;
        this._baseHitup = 0;
        this._baseBowHitup = 0;
        this._baseMr = 0;
        this.invisDelayCounter = 0;
        this._invisTimerMonitor = new Object();
        this._ghost = false;
        this._ghostTime = -1;
        this._ghostCanTalk = true;
        this._isReserveGhost = false;
        this._ghostSaveLocX = 0;
        this._ghostSaveLocY = 0;
        this._ghostSaveMapId = 0;
        this._ghostSaveHeading = 0;
        this._weightUP = 1.0D;
        this._weightReduction = 0;
        this._originalStrWeightReduction = 0;
        this._originalConWeightReduction = 0;
        this._hasteItemEquipped = 0;
        this._damageReductionByArmor = 0;
        this._hitModifierByArmor = 0;
        this._dmgModifierByArmor = 0;
        this._bowHitModifierByArmor = 0;
        this._bowDmgModifierByArmor = 0;
        this._isFishing = false;
        this._fishX = -1;
        this._fishY = -1;
        this._cookingId = 0;
        this._dessertId = 0;
        this._excludingList = new L1ExcludingList();
        this._teleportX = 0;
        this._teleportY = 0;
        this._teleportMapId = 0;
        this._teleportHeading = 0;
        this._isCanWhisper = true;
        this._isShowTradeChat = true;
        this._isShowWorldChat = true;
        this._chatCount = 0;
        this._oldChatTimeInMillis = 0L;
        this._isInCharReset = false;
        this._tempLevel = 1;
        this._tempMaxLevel = 1;
        this._isSummonMonster = false;
        this._isShapeChange = false;
        this._textByte = null;
        this._target = null;
        this._dmgDown = 0;
        this._expadd = 0.0D;
        this._isFoeSlayer = false;
        this._weaknss = 0;
        this._weaknss_t = 0L;
        this._actionId = -1;
        this._hardin = null;
        this._unfreezingTime = 0;
        this._magic_modifier_dmg = 0;
        this._magic_reduction_dmg = 0;
        this._rname = false;
        this._retitle = false;
        this._repass = 0;
        this._trade_items = new ArrayList<>();
        this._mode_id = 0;
        this._check_item = false;
        this._global_time = 0L;
        this._doll_hpr = 0;
        this._doll_hpr_time = 0;
        this._doll_hpr_time_src = 0;
        this._doll_mpr = 0;
        this._doll_mpr_time = 0;
        this._doll_mpr_time_src = 0;
        this._doll_get = new int[2];
        this._doll_get_time = 0;
        this._doll_get_time_src = 0;
        this._spr_move_time = 0L;
        this._spr_attack_time = 0L;
        this._spr_skill_time = 0L;
        this._delete_time = 0;
        this._up_hp_potion = 0;
        this._elitePlateMail_Fafurion = 0;
        this._fafurion_hpmin = 0;
        this._fafurion_hpmax = 0;
        this._venom_resist = 0;
        this._speed = null;
        this._SummonId = 0;
        this._weaponObjIdList = new int[18];
        this._is_allclanbid = true;
        this._is_allCallClan = true;
        this._MatchingList = new ArrayList<>();
        this._CardId = 0;
        this._cmd = 0;
        this._InviteList = new ArrayList<>();
        this._cmalist = new ArrayList<>();
        this._earth_def = 0;
        this._toukuiname = "";
        this._tuokui_objId = 0;
        this._accessLevel = 0;
        this._currentWeapon = 0;
        this._inventory = new L1PcInventory(this);
        this._dwarf = new L1DwarfInventory(this);
        this._dwarfForElf = new L1DwarfForElfInventory(this);
        this._quest = new L1PcQuest(this);
        this._action = new L1ActionPc(this);
        this._actionPet = new L1ActionPet(this);
        this._actionSummon = new L1ActionSummon(this);
        this._equipSlot = new L1EquipmentSlot(this);
        this._count_quest = new L1CountQuest(this);
        this._ezpay_quest = new L1EzpayQuest(this);
        this._map_quest = new L1Map_Quest(this);
    }

    public L1ClassFeature getClassFeature() {
        return this._classFeature;
    }

    public synchronized long getExp() {
        return this._exp;
    }

    public synchronized void setExp(long i) {
        this._exp = i;
    }

    public int get_PKcount() {
        return this._PKcount;
    }

    public void set_PKcount(int i) {
        this._PKcount = i;
    }

    public int getPkCountForElf() {
        return this._PkCountForElf;
    }

    public void setPkCountForElf(int i) {
        this._PkCountForElf = i;
    }

    public int getClanid() {
        return this._clanid;
    }

    public void setClanid(int i) {
        this._clanid = i;
    }

    public String getClanname() {
        return this.clanname;
    }

    public void setClanname(String s) {
        this.clanname = s;
    }

    public L1Clan getClan() {
        return WorldClan.get().getClan(getClanname());
    }

    public int getClanRank() {
        return this._clanRank;
    }

    public void setClanRank(int i) {
        this._clanRank = i;
    }

    public byte get_sex() {
        return this._sex;
    }

    public void set_sex(int i) {
        this._sex = (byte)i;
    }

    public boolean isGm() {
        return this._gm;
    }

    public void setGm(boolean flag) {
        this._gm = flag;
    }

    public boolean isMonitor() {
        return this._monitor;
    }

    public void setMonitor(boolean flag) {
        this._monitor = flag;
    }

    private L1PcInstance getStat() {
        return null;
    }

    public void reduceCurrentHp(double d, L1Character l1character) {
        getStat().reduceCurrentHp(d, l1character);
    }

    private void notifyPlayersLogout(List<L1PcInstance> playersArray) {
        for (L1PcInstance player : playersArray) {
            if (player.knownsObject((L1Object)this)) {
                player.removeKnownObject((L1Object)this);
                player.sendPackets((ServerBasePacket)new S_RemoveObject((L1Object)this));
            }
        }
    }

    public void logout() {
        CharBuffReading.get().deleteBuff(this);
        CharBuffReading.get().saveBuff(this);
        getMap().setPassable((Point)getLocation(), true);
        if (getClanid() != 0) {
            L1Clan clan = WorldClan.get().getClan(getClanname());
            if (clan != null) {
                if (clan.getWarehouseUsingChar() == getId())
                    clan.setWarehouseUsingChar(0);
                clan.CheckClan_Exp20(null);
            }
        }
        notifyPlayersLogout(getKnownPlayers());
        if (get_showId() != -1)
            if (WorldQuest.get().isQuest(get_showId()))
                WorldQuest.get().remove(get_showId(), this);
        set_showId(-1);
        World.get().removeVisibleObject((L1Object)this);
        World.get().removeObject((L1Object)this);
        notifyPlayersLogout(World.get().getRecognizePlayer((L1Object)this));
        removeAllKnownObjects();
        stopHpRegeneration();
        stopMpRegeneration();
        setDead(true);
        setNetConnection((ClientExecutor)null);
        setPacketOutput((EncryptExecutor)null);
        if (ConfigOther.GUI)
            J_Main.getInstance().delPlayerTable(getName());
    }

    public ClientExecutor getNetConnection() {
        return this._netConnection;
    }

    public void setNetConnection(ClientExecutor clientthread) {
        this._netConnection = clientthread;
    }

    public boolean isInParty() {
        return (getParty() != null);
    }

    public L1Party getParty() {
        return this._party;
    }

    public void setParty(L1Party p) {
        this._party = p;
    }

    public boolean isInChatParty() {
        return (getChatParty() != null);
    }

    public L1ChatParty getChatParty() {
        return this._chatParty;
    }

    public void setChatParty(L1ChatParty cp) {
        this._chatParty = cp;
    }

    public int getPartyID() {
        return this._partyID;
    }

    public void setPartyID(int partyID) {
        this._partyID = partyID;
    }

    public int getTradeID() {
        return this._tradeID;
    }

    public void setTradeID(int tradeID) {
        this._tradeID = tradeID;
    }

    public void setTradeOk(boolean tradeOk) {
        this._tradeOk = tradeOk;
    }

    public boolean getTradeOk() {
        return this._tradeOk;
    }

    public int getTempID() {
        return this._tempID;
    }

    public void setTempID(int tempID) {
        this._tempID = tempID;
    }

    public boolean isTeleport() {
        return this._isTeleport;
    }

    public void setTeleport(boolean flag) {
        if (flag)
            setNowTarget((L1PcInstance)null);
        this._isTeleport = flag;
    }

    public boolean isDrink() {
        return this._isDrink;
    }

    public void setDrink(boolean flag) {
        this._isDrink = flag;
    }

    public boolean isGres() {
        return this._isGres;
    }

    public void setGres(boolean flag) {
        this._isGres = flag;
    }

    public boolean isPinkName() {
        return this._isPinkName;
    }

    public void setPinkName(boolean flag) {
        this._isPinkName = flag;
    }

    public ArrayList<L1PrivateShopSellList> getSellList() {
        return this._sellList;
    }

    public ArrayList<L1PrivateShopBuyList> getBuyList() {
        return this._buyList;
    }

    public void setShopChat(byte[] chat) {
        this._shopChat = chat;
    }

    public byte[] getShopChat() {
        return this._shopChat;
    }

    public boolean isPrivateShop() {
        return this._isPrivateShop;
    }

    public void setPrivateShop(boolean flag) {
        this._isPrivateShop = flag;
    }

    public boolean isTradingInPrivateShop() {
        return this._isTradingInPrivateShop;
    }

    public void setTradingInPrivateShop(boolean flag) {
        this._isTradingInPrivateShop = flag;
    }

    public int getPartnersPrivateShopItemCount() {
        return this._partnersPrivateShopItemCount;
    }

    public void setPartnersPrivateShopItemCount(int i) {
        this._partnersPrivateShopItemCount = i;
    }

    public void setPacketOutput(EncryptExecutor out) {
        this._out = out;
    }

    public void sendPackets(ServerBasePacket packet) {
        if (this._out == null)
            return;
        try {
            this._out.encrypt(packet);
        } catch (Exception e) {
            logout();
            close();
        }
    }

    public void sendPacketsAll(ServerBasePacket packet) {
        if (this._out == null)
            return;
        try {
            this._out.encrypt(packet);
            if (!isGmInvis() && !isInvisble())
                broadcastPacketAll(packet);
        } catch (Exception e) {
            logout();
            close();
        }
    }

    public void sendPacketsX8(ServerBasePacket packet) {
        if (this._out == null)
            return;
        try {
            this._out.encrypt(packet);
            if (!isGmInvis() && !isInvisble())
                broadcastPacketX8(packet);
        } catch (Exception e) {
            logout();
            close();
        }
    }

    public void sendPacketsX10(ServerBasePacket packet) {
        if (this._out == null)
            return;
        try {
            this._out.encrypt(packet);
            if (!isGmInvis() && !isInvisble())
                broadcastPacketX10(packet);
        } catch (Exception e) {
            logout();
            close();
        }
    }

    public void sendPacketsXR(ServerBasePacket packet, int r) {
        if (this._out == null)
            return;
        try {
            this._out.encrypt(packet);
            if (!isGmInvis() && !isInvisble())
                broadcastPacketXR(packet, r);
        } catch (Exception e) {
            logout();
            close();
        }
    }

    private void close() {
        try {
            getNetConnection().close();
        } catch (Exception ignored) {}
    }

    public void onAction(L1PcInstance attacker) throws Exception {
        if (attacker == null)
            return;
        if (isTeleport())
            return;
        if (isSafetyZone() || attacker.isSafetyZone()) {
            L1AttackPc l1AttackPc = new L1AttackPc(attacker, this);
            l1AttackPc.action();
            return;
        }
        if (checkNonPvP(this, attacker)) {
            L1AttackPc l1AttackPc = new L1AttackPc(attacker, this);
            l1AttackPc.action();
            return;
        }
        if (getCurrentHp() > 0 && !isDead()) {
            attacker.delInvis();
            boolean isCounterBarrier = false;
            L1AttackPc l1AttackPc = new L1AttackPc(attacker, this);
            if (l1AttackPc.calcHit()) {
                if (hasSkillEffect(91)) {
                    L1Magic magic = new L1Magic(this, attacker);
                    boolean isProbability = magic.calcProbabilityMagic(91);
                    boolean isShortDistance = l1AttackPc.isShortDistance();
                    if (isProbability && isShortDistance)
                        isCounterBarrier = true;
                }
                if (!isCounterBarrier) {
                    attacker.setPetTarget(this);
                    l1AttackPc.calcDamage();
                    l1AttackPc.calcStaffOfMana();
                    l1AttackPc.addChaserAttack();
                }
            }
            if (isCounterBarrier) {
                l1AttackPc.commitCounterBarrier();
            } else {
                l1AttackPc.action();
                l1AttackPc.commit();
            }
        }
    }

    public boolean checkNonPvP(L1PcInstance pc, L1Character target) {
        L1PcInstance targetpc = null;
        if (target instanceof L1PcInstance) {
            targetpc = (L1PcInstance)target;
        } else if (target instanceof L1PetInstance) {
            targetpc = (L1PcInstance)((L1PetInstance)target).getMaster();
        } else if (target instanceof L1SummonInstance) {
            targetpc = (L1PcInstance)((L1SummonInstance)target).getMaster();
        }
        if (targetpc == null)
            return false;
        if (!ConfigAlt.ALT_NONPVP) {
            if (getMap().isCombatZone((Point)getLocation()))
                return false;
            for (L1War war : WorldWar.get().getWarList()) {
                if (pc.getClanid() != 0 && targetpc.getClanid() != 0) {
                    boolean same_war = war.checkClanInSameWar(pc.getClanname(), targetpc.getClanname());
                    if (same_war)
                        return false;
                }
            }
            if (target instanceof L1PcInstance) {
                L1PcInstance targetPc = (L1PcInstance)target;
                if (isInWarAreaAndWarTime(pc, targetPc))
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean isInWarAreaAndWarTime(L1PcInstance pc, L1PcInstance target) {
        int castleId = L1CastleLocation.getCastleIdByArea(pc);
        int targetCastleId = L1CastleLocation.getCastleIdByArea(target);
        if (castleId != 0 && targetCastleId != 0 && castleId == targetCastleId && ServerWarExecutor.get().isNowWar(castleId))
            return true;
        return false;
    }

    private static boolean _debug = Config.DEBUG;

    public long _oldTime;

    public void setPetTarget(L1Character target) {
        if (target == null)
            return;
        if (target.isDead())
            return;
        Map<Integer, L1NpcInstance> petList = getPetList();
        try {
            if (!petList.isEmpty())
                for (Iterator<L1NpcInstance> iter = petList.values().iterator(); iter.hasNext(); ) {
                    L1NpcInstance pet = iter.next();
                    if (pet != null) {
                        if (pet instanceof L1PetInstance) {
                            L1PetInstance pets = (L1PetInstance)pet;
                            pets.setMasterTarget(target);
                            continue;
                        }
                        if (pet instanceof L1SummonInstance) {
                            L1SummonInstance summon = (L1SummonInstance)pet;
                            summon.setMasterTarget(target);
                        }
                    }
                }
        } catch (Exception e) {
            if (_debug)
                _log.error(e.getLocalizedMessage(), e);
        }
        Map<Integer, L1IllusoryInstance> illList = get_otherList().get_illusoryList();
        try {
            if (!illList.isEmpty())
                if (getId() != target.getId())
                    for (Iterator<L1IllusoryInstance> iter = illList.values().iterator(); iter.hasNext(); ) {
                        L1IllusoryInstance ill = iter.next();
                        if (ill != null)
                            ill.setLink(target);
                    }
        } catch (Exception e) {
            if (_debug)
                _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void delInvis() {
        if (hasSkillEffect(60)) {
            killSkillEffectTimer(60);
            sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacketAll((ServerBasePacket)new S_OtherCharPacks(this));
        }
        if (hasSkillEffect(97)) {
            killSkillEffectTimer(97);
            sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
            broadcastPacketAll((ServerBasePacket)new S_OtherCharPacks(this));
        }
    }

    public void delBlindHiding() {
        killSkillEffectTimer(97);
        sendPackets((ServerBasePacket)new S_Invis(getId(), 0));
        broadcastPacketAll((ServerBasePacket)new S_OtherCharPacks(this));
    }

    public void receiveDamage(L1Character attacker, double damage, int attr) throws Exception {
        int player_mr = getMr();
        int rnd = _random.nextInt(300) + 1;
        if (player_mr >= rnd)
            damage /= 2.0D;
        int resist = 0;
        switch (attr) {
            case 1:
                resist = getEarth();
                break;
            case 2:
                resist = getFire();
                break;
            case 4:
                resist = getWater();
                break;
            case 8:
                resist = getWind();
                break;
        }
        int resistFloor = (int)(0.32D * Math.abs(resist));
        if (resist >= 0) {
            resistFloor *= 1;
        } else {
            resistFloor *= -1;
        }
        double attrDeffence = resistFloor / 32.0D;
        double coefficient = 1.0D - attrDeffence + 0.09375D;
        if (coefficient > 0.0D)
            damage *= coefficient;
        receiveDamage(attacker, damage, false, false);
    }

    public void receiveManaDamage(L1Character attacker, int mpDamage) {
        if (mpDamage > 0 && !isDead()) {
            delInvis();
            if (attacker instanceof L1PcInstance)
                L1PinkName.onAction(this, attacker);
            if (attacker instanceof L1PcInstance && ((L1PcInstance)attacker).isPinkName())
                for (L1Object object : World.get().getVisibleObjects((L1Object)attacker)) {
                    if (object instanceof L1GuardInstance) {
                        L1GuardInstance guard = (L1GuardInstance)object;
                        guard.setTarget((L1PcInstance)attacker);
                    }
                }
            int newMp = getCurrentMp() - mpDamage;
            if (newMp > getMaxMp())
                newMp = getMaxMp();
            newMp = Math.max(newMp, 0);
            setCurrentMp(newMp);
        }
    }

    private static final Map<Long, Double> _magicDamagerList = new HashMap<>();

    private int _originalEr;

    private ClientExecutor _netConnection;

    private int _classId;

    private int _type;

    private long _exp;

    private final L1Karma _karma;

    private boolean _gm;

    private boolean _monitor;

    private boolean _gmInvis;

    private short _accessLevel;

    private int _currentWeapon;

    private final L1PcInventory _inventory;

    private final L1DwarfInventory _dwarf;

    private final L1DwarfForElfInventory _dwarfForElf;

    private L1ItemInstance _weapon;

    private L1Party _party;

    private L1ChatParty _chatParty;

    private int _partyID;

    private int _tradeID;

    private boolean _tradeOk;

    private int _tempID;

    private boolean _isTeleport;

    private boolean _isDrink;

    private boolean _isGres;

    private boolean _isPinkName;

    private L1PcQuest _quest;

    private L1CountQuest _count_quest;

    private L1EzpayQuest _ezpay_quest;

    private L1Map_Quest _map_quest;

    private L1ActionPc _action;

    private L1ActionPet _actionPet;

    private L1ActionSummon _actionSummon;

    private L1EquipmentSlot _equipSlot;

    private String _accountName;

    private short _baseMaxHp;

    private short _baseMaxMp;

    private int _baseAc;

    private int _originalAc;

    private int _baseStr;

    private int _baseCon;

    private int _baseDex;

    private int _baseCha;

    private int _baseInt;

    private int _baseWis;

    private int _originalStr;

    private int _originalCon;

    private int _originalDex;

    private int _originalCha;

    private int _originalInt;

    private int _originalWis;

    private int _originalDmgup;

    private int _originalBowDmgup;

    private int _originalHitup;

    private int _originalBowHitup;

    private int _originalMr;

    private int _originalMagicHit;

    private int _originalMagicCritical;

    private int _originalMagicConsumeReduction;

    private int _originalMagicDamage;

    private int _originalHpup;

    private int _originalMpup;

    private int _baseDmgup;

    private int _baseBowDmgup;

    private int _baseHitup;

    private int _baseBowHitup;

    private int _baseMr;

    private int _advenHp;

    private int _advenMp;

    private int _highLevel;

    private int _bonusStats;

    private int _elixirStats;

    private int _elfAttr;

    private int _expRes;

    private int _partnerId;

    private int _onlineStatus;

    private int _homeTownId;

    private int _contribution;

    private int _hellTime;

    private boolean _banned;

    private int _food;

    private int invisDelayCounter;

    private Object _invisTimerMonitor;

    private static final long DELAY_INVIS = 3000L;

    public double tempExp;

    private boolean _ghost;

    private int _ghostTime;

    private boolean _ghostCanTalk;

    private boolean _isReserveGhost;

    private int _ghostSaveLocX;

    private int _ghostSaveLocY;

    private short _ghostSaveMapId;

    private int _ghostSaveHeading;

    private Timestamp _lastPk;

    private Timestamp _lastPkForElf;

    private Timestamp _deleteTime;

    private double _weightUP;

    private int _weightReduction;

    private int _originalStrWeightReduction;

    private int _originalConWeightReduction;

    private int _hasteItemEquipped;

    private int _damageReductionByArmor;

    private int _hitModifierByArmor;

    private int _dmgModifierByArmor;

    private int _bowHitModifierByArmor;

    private int _bowDmgModifierByArmor;

    private boolean _gresValid;

    private boolean _isFishing;

    private int _fishX;

    private int _fishY;

    private int _cookingId;

    private int _dessertId;

    private final L1ExcludingList _excludingList;

    private int _teleportX;

    private int _teleportY;

    private short _teleportMapId;

    private int _teleportHeading;

    private int _tempCharGfxAtDead;

    private boolean _isCanWhisper;

    private boolean _isShowTradeChat;

    private boolean _isShowWorldChat;

    private int _fightId;

    private byte _chatCount;

    private long _oldChatTimeInMillis;

    private int _callClanId;

    private int _callClanHeading;

    private boolean _isInCharReset;

    private int _tempLevel;

    private int _tempMaxLevel;

    private boolean _isSummonMonster;

    private boolean _isShapeChange;

    private String _text;

    private byte[] _textByte;

    private L1PcOther _other;

    private L1PcOtherList _otherList;

    private int _oleLocX;

    private int _oleLocY;

    private L1PcInstance _target;

    private int _dmgDown;

    private long _h_time;

    private int _int1;

    private int _int2;

    private int _evasion;

    private double _expadd;

    private int _dd1;

    private int _dd2;

    private boolean _isFoeSlayer;

    private int _weaknss;

    private long _weaknss_t;

    private int _actionId;

    private Chapter01R _hardin;

    private int _unfreezingTime;

    private int _magic_modifier_dmg;

    private int _magic_reduction_dmg;

    private boolean _rname;

    private boolean _retitle;

    private int _repass;

    private ArrayList<L1TradeItem> _trade_items;

    private int _mode_id;

    private boolean _check_item;

    private long _global_time;

    private int _doll_hpr;

    private int _doll_hpr_time;

    private int _doll_hpr_time_src;

    private int _doll_mpr;

    private int _doll_mpr_time;

    private int _doll_mpr_time_src;

    private int[] _doll_get;

    private int _doll_get_time;

    private int _doll_get_time_src;

    private String _board_title;

    private String _board_content;

    private long _spr_move_time;

    private long _spr_attack_time;

    private long _spr_skill_time;

    private int _delete_time;

    private int _up_hp_potion;

    private int _elitePlateMail_Fafurion;

    private int _fafurion_hpmin;

    private int _fafurion_hpmax;

    int _venom_resist;

    private AcceleratorChecker _speed;

    private int killCount;

    private int _SummonId;

    private int _super;

    private int _ai_number;

    private int _points;

    private final int[] _weaponObjIdList;

    private boolean _is_allclanbid;

    private boolean _is_allCallClan;

    private ArrayList<String> _MatchingList;

    private int _expPoint;

    private int _CardId;

    private int _card;

    private int _cmd;

    private int _dollhole;

    private ArrayList<String> _InviteList;

    private ArrayList<String> _cmalist;

    private int _earth_def;

    private boolean _showemblem;

    private int _donus;

    private int _bonus;

    private String _toukuiname;

    private int _tuokui_objId;

    private int _ubscore;

    public static void load() {
        double newdmg = 100.0D;
        for (long i = 2000L; i > 0L; i--) {
            if (i % 100L == 0L)
                newdmg -= 3.33D;
            _magicDamagerList.put(Long.valueOf(i), Double.valueOf(newdmg));
        }
    }

    public double isMagicDamager(double damage) {
        long nowTime = System.currentTimeMillis();
        long interval = nowTime - this._oldTime;
        double newdmg = 0.0D;
        if (damage < 0.0D) {
            newdmg = damage;
        } else {
            Double tmpnewdmg = _magicDamagerList.get(Long.valueOf(interval));
            if (tmpnewdmg != null) {
                newdmg = damage * tmpnewdmg.doubleValue() / 100.0D;
            } else {
                newdmg = damage;
            }
            newdmg = Math.max(newdmg, 0.0D);
            this._oldTime = nowTime;
        }
        return newdmg;
    }

    public void receiveDamage(L1Character attacker, double damage, boolean isMagicDamage, boolean isCounterBarrier) throws Exception {
        if (getCurrentHp() > 0 && !isDead()) {
            if (attacker != null) {
                if (attacker != this && !(attacker instanceof L1EffectInstance) && !knownsObject((L1Object)attacker) && attacker.getMapId() == getMapId())
                    attacker.onPerceive(this);
                if (isMagicDamage)
                    damage = isMagicDamager(damage);
                L1PcInstance attackPc = null;
                L1NpcInstance attackNpc = null;
                if (attacker instanceof L1PcInstance) {
                    attackPc = (L1PcInstance)attacker;
                } else if (attacker instanceof L1NpcInstance) {
                    attackNpc = (L1NpcInstance)attacker;
                }
                if (damage > 0.0D) {
                    delInvis();
                    removeSkillEffect(66);
                    if (attackPc != null) {
                        L1PinkName.onAction(this, attackPc);
                        if (attackPc.isPinkName())
                            for (L1Object object : World.get().getVisibleObjects((L1Object)attacker)) {
                                if (object instanceof L1GuardInstance) {
                                    L1GuardInstance guard = (L1GuardInstance)object;
                                    guard.setTarget((L1PcInstance)attacker);
                                }
                            }
                    }
                }
                if (!isCounterBarrier) {
                    if (hasSkillEffect(191) && getId() != attacker.getId()) {
                        int rnd = _random.nextInt(100) + 1;
                        if (damage > 0.0D && rnd <= 18) {
                            int dmg = attacker.getLevel();
                            if (attackPc != null) {
                                attackPc.sendPacketsX10((ServerBasePacket)new S_DoActionGFX(attackPc.getId(), 2));
                                attackPc.receiveDamage(this, dmg, false, true);
                            } else if (attackNpc != null) {
                                attackNpc.broadcastPacketX10((ServerBasePacket)new S_DoActionGFX(attackNpc.getId(), 2));
                                attackNpc.receiveDamage(this, dmg);
                            }
                        }
                    }
                    if (attacker.hasSkillEffect(218) && getId() != attacker.getId()) {
                        attacker.killSkillEffectTimer(218);
                        int hpup = get_other().get_addhp();
                        int nowDamage = (getMaxHp() - getCurrentHp() - hpup) / 4;
                        if (nowDamage > 0)
                            if (attackPc != null) {
                                attackPc.sendPacketsX10((ServerBasePacket)new S_DoActionGFX(attackPc.getId(), 2));
                                attackPc.receiveDamage(this, nowDamage, false, true);
                            } else if (attackNpc != null) {
                                attackNpc.broadcastPacketX10((ServerBasePacket)new S_DoActionGFX(attackNpc.getId(), 2));
                                attackNpc.receiveDamage(this, nowDamage);
                            }
                    }
                }
            }
            if (getInventory().checkEquipped(145) || getInventory().checkEquipped(149))
                damage *= 1.5D;
            if (hasSkillEffect(219))
                damage *= 1.5D;
            int addhp = 0;
            if (this._elitePlateMail_Fafurion > 0 && _random.nextInt(1000) <= this._elitePlateMail_Fafurion) {
                sendPacketsX8((ServerBasePacket)new S_SkillSound(getId(), 2187));
                addhp = _random.nextInt(this._fafurion_hpmax - this._fafurion_hpmin + 1) + this._fafurion_hpmin;
            }
            int newHp = getCurrentHp() - (int)damage + addhp;
            if (newHp > getMaxHp())
                newHp = getMaxHp();
            if (newHp <= 0 && !isGm())
                death(attacker);
            setCurrentHp(newHp);
        } else if (!isDead()) {
            _log.error("人物hp減少處理失敗 可能原因: 初始hp為0");
                    death(attacker);
        }
    }

    public void death(L1Character lastAttacker) {
        synchronized (this) {
            if (isDead())
                return;
            setNowTarget((L1PcInstance)null);
            setDead(true);
            setStatus(8);
        }
        if (getTradeID() != 0) {
            L1Trade trade = new L1Trade();
            trade.tradeCancel(this);
        }
        GeneralThreadPool.get().execute(new Death(lastAttacker));
    }

    private class Death implements Runnable {
        private L1Character _lastAttacker;

        private Death(L1Character cha) {
            this._lastAttacker = cha;
        }

        public void run() {
            L1Character lastAttacker = this._lastAttacker;
            this._lastAttacker = null;
            L1PcInstance.this.setCurrentHp(0);
            L1PcInstance.this.setGresValid(false);
            while (L1PcInstance.this.isTeleport()) {
                try {
                    Thread.sleep(300L);
                } catch (Exception ignored) {}
            }
            if (L1PcInstance.this.isInParty())
                for (L1PcInstance member : L1PcInstance.this.getParty().partyUsers().values())
                    member.sendPackets((ServerBasePacket)new S_PacketBoxParty(L1PcInstance.this.getParty(), L1PcInstance.this));
            L1PcInstance.this.set_delete_time(300);
            if (!L1PcInstance.this.getPetList().isEmpty()) {
                byte b;
                int i;
                Object[] arrayOfObject;
                for (i = (arrayOfObject = L1PcInstance.this.getPetList().values().toArray()).length, b = 0; b < i; ) {
                    Object petList = arrayOfObject[b];
                    if (petList instanceof L1SummonInstance) {
                        L1SummonInstance summon = (L1SummonInstance)petList;
                        if (summon != null) {
                            if (summon.destroyed())
                                return;
                            if (summon.tamed()) {
                                summon.deleteMe();
                            } else {
                                try {
                                    summon.Death((L1Character)null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    b++;
                }
            }
            if (!L1PcInstance.this.getDolls().isEmpty()) {
                byte b;
                int i;
                Object[] arrayOfObject;
                for (i = (arrayOfObject = L1PcInstance.this.getDolls().values().toArray()).length, b = 0; b < i; ) {
                    Object obj = arrayOfObject[b];
                    L1DollInstance doll = (L1DollInstance)obj;
                    doll.deleteDoll();
                    b++;
                }
            }
            L1PcInstance.this.stopHpRegeneration();
            L1PcInstance.this.stopMpRegeneration();
            int targetobjid = L1PcInstance.this.getId();
            L1PcInstance.this.getMap().setPassable((Point)L1PcInstance.this.getLocation(), true);
            int tempchargfx = 0;
            if (L1PcInstance.this.hasSkillEffect(67)) {
                tempchargfx = L1PcInstance.this.getTempCharGfx();
                L1PcInstance.this.setTempCharGfxAtDead(tempchargfx);
            } else {
                L1PcInstance.this.setTempCharGfxAtDead(L1PcInstance.this.getClassId());
            }
            L1SkillUse l1skilluse = new L1SkillUse();
            l1skilluse.handleCommands(L1PcInstance.this, 44, L1PcInstance.this.getId(), L1PcInstance.this.getX(), L1PcInstance.this.getY(), 0, 1);
            if (tempchargfx == 5727 || tempchargfx == 5730 || tempchargfx == 5733 || tempchargfx == 5736)
                tempchargfx = 0;
            if (tempchargfx == 7351) {
                tempchargfx = L1PcInstance.this.getClassId();
                L1PcInstance.this.setTempCharGfx(tempchargfx);
            }
            if (tempchargfx != 0) {
                L1PcInstance.this.sendPacketsAll((ServerBasePacket)new S_ChangeShape(L1PcInstance.this, tempchargfx));
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (Exception ignored) {}
            }
            boolean isSafetyZone = false;
            boolean isCombatZone = false;
            boolean isWar = false;
            if (L1PcInstance.this.getZoneType() == 1)
                isSafetyZone = true;
            if (L1PcInstance.this.getZoneType() == -1)
                isCombatZone = true;
            if (lastAttacker instanceof L1GuardInstance) {
                if (L1PcInstance.this.get_PKcount() > 0)
                    L1PcInstance.this.set_PKcount(L1PcInstance.this.get_PKcount() - 1);
                L1PcInstance.this.setLastPk((Timestamp)null);
            }
            if (lastAttacker instanceof L1GuardianInstance) {
                if (L1PcInstance.this.getPkCountForElf() > 0)
                    L1PcInstance.this.setPkCountForElf(L1PcInstance.this.getPkCountForElf() - 1);
                L1PcInstance.this.setLastPkForElf((Timestamp)null);
            }
            L1PcInstance fightPc = null;
            if (lastAttacker instanceof L1PcInstance) {
                fightPc = (L1PcInstance)lastAttacker;
            } else if (lastAttacker instanceof L1PetInstance) {
                L1PetInstance npc = (L1PetInstance)lastAttacker;
                if (npc.getMaster() != null)
                    fightPc = (L1PcInstance)npc.getMaster();
            } else if (lastAttacker instanceof L1SummonInstance) {
                L1SummonInstance npc = (L1SummonInstance)lastAttacker;
                if (npc.getMaster() != null)
                    fightPc = (L1PcInstance)npc.getMaster();
            } else if (lastAttacker instanceof L1IllusoryInstance) {
                L1IllusoryInstance npc = (L1IllusoryInstance)lastAttacker;
                if (npc.getMaster() != null)
                    fightPc = (L1PcInstance)npc.getMaster();
            } else if (lastAttacker instanceof L1EffectInstance) {
                L1EffectInstance npc = (L1EffectInstance)lastAttacker;
                if (npc.getMaster() != null)
                    fightPc = (L1PcInstance)npc.getMaster();
            }
            L1PcInstance.this.sendPacketsAll((ServerBasePacket)new S_DoActionGFX(targetobjid, 8));
            if (fightPc != null) {
                if (L1PcInstance.this.getFightId() == fightPc.getId() && fightPc.getFightId() == L1PcInstance.this.getId()) {
                    L1PcInstance.this.setFightId(0);
                    L1PcInstance.this.sendPackets((ServerBasePacket)new S_PacketBox(5, 0, 0));
                    fightPc.setFightId(0);
                    fightPc.sendPackets((ServerBasePacket)new S_PacketBox(5, 0, 0));
                    return;
                }
                if (L1PcInstance.this.isEncounter() && fightPc.getLevel() > L1PcInstance.this.getLevel() && fightPc.getLevel() - L1PcInstance.this.getLevel() >= 10)
                    return;
                if (L1PcInstance.this.castleWarResult())
                    isWar = true;
                if (L1PcInstance.this.simWarResult(lastAttacker))
                    isWar = true;
                if (L1PcInstance.this.isInWarAreaAndWarTime(L1PcInstance.this, fightPc))
                    isWar = true;
                if (ConfigKill.KILLLEVEL && !fightPc.isGm()) {
                    L1ItemInstance item = fightPc.getWeapon();
                    String msg0 = new StringBuilder().append(fightPc.getWeapon().getEnchantLevel()).toString();

                    boolean isShow = false;
                    if (isWar) {
                        isShow = true;
                    } else if (!isCombatZone) {
                        isShow = true;
                    }
                    if (isShow) {
                        World.get().broadcastPacketToAll((ServerBasePacket)new S_KillMessage(fightPc.getName(), msg0, fightPc.getWeapon().getName(), L1PcInstance.this.getName()));
                        World.get().broadcastPacketToAll((ServerBasePacket)new S_KillMessage(2, fightPc.getName(), msg0, fightPc.getWeapon().getName(), L1PcInstance.this.getName()));
                        fightPc.get_other().add_killCount(1);
                        L1PcInstance.this.get_other().add_deathCount(1);
                    }
                }
            }
            if (isSafetyZone)
                return;
            if (isCombatZone)
                return;
            if (!L1PcInstance.this.getMap().isEnabledDeathPenalty())
                return;
            boolean castle_area = L1CastleLocation.checkInAllWarArea(L1PcInstance.this.getX(), L1PcInstance.this.getY(), L1PcInstance.this.getMapId());
            if (castle_area && !ConfigOther.ALT_WARPUNISHMENT)
                return;
            try {
                expRate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (L1PcInstance.this.getLawful() < 32767) {
                if (castle_area)
                    return;
                try {
                    lostRate();
                    lostSkillRate();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (fightPc != null) {
                if (isWar)
                    return;
                if (fightPc.getClan() != null && L1PcInstance.this.getClan() != null && WorldWar.get().isWar(fightPc.getClan().getClanName(), L1PcInstance.this.getClan().getClanName()))
                    return;
                if (fightPc.isSafetyZone())
                    return;
                if (fightPc.isCombatZone())
                    return;
                if (L1PcInstance.this.getLawful() >= 0 && !L1PcInstance.this.isPinkName()) {
                    int lawful;
                    boolean isChangePkCount = false;
                    if (fightPc.getLawful() < 30000) {
                        fightPc.set_PKcount(fightPc.get_PKcount() + 1);
                        isChangePkCount = true;
                        if (fightPc.isElf() && L1PcInstance.this.isElf())
                            fightPc.setPkCountForElf(fightPc.getPkCountForElf() + 1);
                    }
                    fightPc.setLastPk();
                    if (fightPc.getLawful() == 32767)
                        fightPc.setLastPk((Timestamp)null);
                    if (fightPc.isElf() && L1PcInstance.this.isElf())
                        fightPc.setLastPkForElf();
                    if (fightPc.getLevel() < 50) {
                        lawful = -1 * ((int)Math.pow(fightPc.getLevel(), 2.0D) << 2);
                    } else {
                        lawful = -1 * (int)(Math.pow(fightPc.getLevel(), 3.0D) * 0.08D);
                    }
                    if (fightPc.getLawful() - 1000 < lawful)
                        lawful = fightPc.getLawful() - 1000;
                    if (lawful <= -32768)
                        lawful = -32768;
                    fightPc.setLawful(lawful);
                    fightPc.sendPacketsAll((ServerBasePacket)new S_Lawful(fightPc));
                    if (ConfigAlt.ALT_PUNISHMENT)
                        if (isChangePkCount && fightPc.get_PKcount() >= 5 && fightPc.get_PKcount() < 10000) {
                            fightPc.sendPackets((ServerBasePacket)new S_BlueMessage(551, String.valueOf(fightPc.get_PKcount()), "10000"));
                        } else if (isChangePkCount && fightPc.get_PKcount() >= 10000) {
                            fightPc.beginHell(true);
                        }
                } else {
                    L1PcInstance.this.setPinkName(false);
                }
            }
        }

        private void expRate() throws Exception {
            L1ItemInstance item1 = L1PcInstance.this.getInventory().checkItemX(51000, 1L);
            if (item1 != null) {
                L1PcInstance.this.getInventory().removeItem(item1, 1L);
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage("+ item1.getName() + "));
                return;
            }
            L1PcInstance.this.deathPenalty();
            L1PcInstance.this.setGresValid(true);
            if (L1PcInstance.this.getExpRes() == 0)
                L1PcInstance.this.setExpRes(1);
            L1PcInstance.this.onChangeExp();
        }

        private void lostRate() throws Exception {
            L1ItemInstance item2 = L1PcInstance.this.getInventory().checkItemX(51001, 1L);
            if (item2 != null) {
                L1PcInstance.this.getInventory().removeItem(item2, 1L);
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage("+ item2.getName() + "));
                return;
            }
            int count = 0;
            int lawful = L1PcInstance.this.getLawful();
            if (lawful <= -32768 + L1PcInstance._random.nextInt(70) + 1) {
                count = L1PcInstance._random.nextInt(5) + 1;
            } else if (lawful > -32768 && lawful <= -30000 + L1PcInstance._random.nextInt(60) + 1) {
                count = L1PcInstance._random.nextInt(4) + 1;
            } else if (lawful > -30000 && lawful <= -20000 + L1PcInstance._random.nextInt(50) + 1) {
                count = L1PcInstance._random.nextInt(3) + 1;
            } else if (lawful > -20000 && lawful <= -10000 + L1PcInstance._random.nextInt(40) + 1) {
                count = L1PcInstance._random.nextInt(2) + 1;
            } else if (lawful > -10000 && lawful <= 0 + L1PcInstance._random.nextInt(20) + 1) {
                count = L1PcInstance._random.nextInt(1) + 1;
            } else if (lawful > 1 && lawful <= 30000 + L1PcInstance._random.nextInt(15) + 1) {
                count = L1PcInstance._random.nextInt(1) + 1;
            }
            if (count > 0)
                L1PcInstance.this.caoPenaltyResult(count);
        }

        private void lostSkillRate() throws Exception {
            L1ItemInstance item2 = L1PcInstance.this.getInventory().checkItemX(51002, 1L);
            if (item2 != null) {
                L1PcInstance.this.getInventory().removeItem(item2, 1L);
                L1PcInstance.this.sendPackets((ServerBasePacket)new S_ServerMessage("+ item2.getName() + "));
                return;
            }
            int skillCount = L1PcInstance.this._skillList.size();
            if (skillCount > 0) {
                int count = 0;
                int lawful = L1PcInstance.this.getLawful();
                int random = L1PcInstance._random.nextInt(200);
                if (lawful <= -32768) {
                    count = L1PcInstance._random.nextInt(4) + 1;
                } else if (lawful > -32768 && lawful <= -30000) {
                    if (random <= skillCount + 1)
                        count = L1PcInstance._random.nextInt(3) + 1;
                } else if (lawful > -30000 && lawful <= -20000) {
                    if (random <= (skillCount >> 1) + 1)
                        count = L1PcInstance._random.nextInt(2) + 1;
                } else if (lawful > -20000 && lawful <= -10000 && random <= (skillCount >> 2) + 1) {
                    count = 1;
                }
                if (count > 0)
                    L1PcInstance.this.delSkill(count);
            }
        }
    }

    private void caoPenaltyResult(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            L1ItemInstance item = getInventory().caoPenalty();
            if (item != null) {
                item.set_showId(get_showId());
                getInventory().tradeItem(item, item.isStackable() ? item.getCount() : serialVersionUID, World.get().getInventory(getX(), getY(), getMapId()));
                item.setKillDeathName(getName());
                String mapName = MapsTable.get().getMapName(getMapId(), getX(), getY());
                sendPackets(new S_ServerMessage(638, item.getLogName()));
                WriteLogTxt.Recording("死亡掉落物品記錄", "玩家【 " + getName() + " 】" + "地點:【 " + item.getLocation() + "】" + "的【 + " + item.getEnchantLevel() + " " + item.getName() + "(" + item.getCount() + ") 】在死亡時噴了，" + "道具ID：(" + item.getId() + ")" + "時間:(" + TimeInform.getNowTime(3, 0) + ")。");
                if (ConfigOther.item_getLogName) {
                    World.get().broadcastPacketToAll(new S_ServerMessage("【 " + getName() + "】地點【" + mapName + "】噴 【" + item.getLogName() + "】。"));
                }
            }
        }
    }


    private void delSkill(int count) {
        for (int i = 0; i < count; i++) {
            Integer skillid = this._skillList.get(_random.nextInt(this._skillList.size()));
            if (this._skillList.remove(skillid)) {
                String mapName = MapsTable.get().getMapName(getMapId(), getX(), getY());
                sendPackets(new S_DelSkill(this, skillid.intValue()));
                CharSkillReading.get().spellLost(getId(), skillid.intValue());
                L1Skills _skill = SkillsTable.get().getTemplate(skillid.intValue());
                sendPackets(new S_ServerMessage(638, _skill.getName()));
                WriteLogTxt.Recording("死亡掉落技能記錄", "地點:【 " + getLocation() + "】" + "IP:【 " + ((Object) getNetConnection().getIp()) + " 】" + "玩家【 " + getName() + " 】" + "的技能【" + _skill.getName() + "】，" + "時間:(" + TimeInform.getNowTime(3, 0) + ")。");
                if (ConfigOther.skill_getName) {
                    World.get().broadcastPacketToAll(new S_ServerMessage("【" + getName() + "】地點【" + mapName + "】噴，魔法【 " + _skill.getName() + " 】。"));
                }
            }
        }
    }


    public void stopPcDeleteTimer() {
        setDead(false);
        set_delete_time(0);
    }

    public boolean castleWarResult() {
        if (getClanid() != 0 && isCrown()) {
            L1Clan clan = WorldClan.get().getClan(getClanname());
            if (clan.getCastleId() == 0)
                for (L1War war : WorldWar.get().getWarList()) {
                    int warType = war.getWarType();
                    boolean isInWar = war.checkClanInWar(getClanname());
                    boolean isAttackClan = war.checkAttackClan(getClanname());
                    if (getId() == clan.getLeaderId() && warType == 1 && isInWar && isAttackClan) {
                        String enemyClanName = war.getEnemyClanName(getClanname());
                        if (enemyClanName != null)
                            war.ceaseWar(getClanname(), enemyClanName);
                        break;
                    }
                }
        }
        int castleId = 0;
        boolean isNowWar = false;
        castleId = L1CastleLocation.getCastleIdByArea(this);
        if (castleId != 0)
            isNowWar = ServerWarExecutor.get().isNowWar(castleId);
        return isNowWar;
    }

    public boolean simWarResult(L1Character lastAttacker) {
        if (getClanid() == 0)
            return false;
        L1PcInstance attacker = null;
        String enemyClanName = null;
        boolean sameWar = false;
        if (lastAttacker instanceof L1PcInstance) {
            attacker = (L1PcInstance)lastAttacker;
        } else if (lastAttacker instanceof L1PetInstance) {
            attacker = (L1PcInstance)((L1PetInstance)lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1SummonInstance) {
            attacker = (L1PcInstance)((L1SummonInstance)lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1IllusoryInstance) {
            attacker = (L1PcInstance)((L1IllusoryInstance)lastAttacker).getMaster();
        } else if (lastAttacker instanceof L1EffectInstance) {
            attacker = (L1PcInstance)((L1EffectInstance)lastAttacker).getMaster();
        } else {
            return false;
        }
        for (L1War war : WorldWar.get().getWarList()) {
            L1Clan clan = WorldClan.get().getClan(getClanname());
            int warType = war.getWarType();
            boolean isInWar = war.checkClanInWar(getClanname());
            if (attacker != null && attacker.getClanid() != 0)
                sameWar = war.checkClanInSameWar(getClanname(), attacker.getClanname());
            if (getId() == clan.getLeaderId() && warType == 2 && isInWar) {
                enemyClanName = war.getEnemyClanName(getClanname());
                if (enemyClanName != null)
                    war.ceaseWar(getClanname(), enemyClanName);
            }
            if (warType == 2 && sameWar)
                return true;
        }
        return false;
    }

    public void resExp() {
        int oldLevel = getLevel();
        long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0L;
        switch (oldLevel) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
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
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                exp = (long)(needExp * 0.05D);
                break;
            case 45:
                exp = (long)(needExp * 0.045D);
                break;
            case 46:
                exp = (long)(needExp * 0.04D);
                break;
            case 47:
                exp = (long)(needExp * 0.035D);
                break;
            case 48:
                exp = (long)(needExp * 0.03D);
                break;
            case 49:
                exp = (long)(needExp * 0.025D);
                break;
            default:
                exp = (long)(needExp * 0.025D);
                break;
        }
        if (exp == 0L)
            return;
        addExp(exp);
    }

    private long deathPenalty() {
        int oldLevel = getLevel();
        long needExp = ExpTable.getNeedExpNextLevel(oldLevel);
        long exp = 0L;
        switch (oldLevel) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                exp = 0L;
                break;
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
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
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                exp = (long)(needExp * 0.1D);
                break;
            case 45:
                exp = (long)(needExp * 0.09D);
                break;
            case 46:
                exp = (long)(needExp * 0.08D);
                break;
            case 47:
                exp = (long)(needExp * 0.07D);
                break;
            case 48:
                exp = (long)(needExp * 0.06D);
                break;
            case 49:
                exp = (long)(needExp * 0.05D);
                break;
            default:
                exp = (long)(needExp * 0.05D);
                break;
        }
        if (exp == 0L)
            return 0L;
        addExp(-exp);
        return exp;
    }

    public int getOriginalEr() {
        return this._originalEr;
    }

    public int getEr() {
        if (hasSkillEffect(174))
            return 0;
        int er = 0;
        if (isKnight()) {
            er = getLevel() >> 2;
        } else if (isCrown() || isElf()) {
            er = getLevel() >> 3;
        } else if (isDarkelf()) {
            er = getLevel() / 6;
        } else if (isWizard()) {
            er = getLevel() / 10;
        } else if (isDragonKnight()) {
            er = getLevel() / 7;
        } else if (isIllusionist()) {
            er = getLevel() / 9;
        }
        er += getDex() - 8 >> 1;
        er += getOriginalEr();
        if (hasSkillEffect(111))
            er += 12;
        if (hasSkillEffect(90))
            er += 15;
        if (hasSkillEffect(4009))
            er += 30;
        if (hasSkillEffect(4010))
            er += 15;
        return er;
    }

    public L1ItemInstance getWeapon() {
        return this._weapon;
    }

    public void setWeapon(L1ItemInstance weapon) {
        this._weapon = weapon;
    }

    public L1PcQuest getQuest() {
        return this._quest;
    }

    public L1CountQuest getCount_Quest() {
        return this._count_quest;
    }

    public L1EzpayQuest getEzpay_Quest() {
        return this._ezpay_quest;
    }

    public L1Map_Quest getMap_Quest() {
        return this._map_quest;
    }

    public L1ActionPc getAction() {
        return this._action;
    }

    public L1ActionPet getActionPet() {
        return this._actionPet;
    }

    public L1ActionSummon getActionSummon() {
        return this._actionSummon;
    }

    public boolean isCrown() {
        return !(getClassId() != 0 && getClassId() != 1);
    }

    public boolean isKnight() {
        return !(getClassId() != 61 && getClassId() != 48);
    }

    public boolean isElf() {
        return !(getClassId() != 138 && getClassId() != 37);
    }

    public boolean isWizard() {
        return !(getClassId() != 734 && getClassId() != 1186);
    }

    public boolean isDarkelf() {
        return !(getClassId() != 2786 && getClassId() != 2796);
    }

    public boolean isDragonKnight() {
        return !(getClassId() != 6658 && getClassId() != 6661);
    }

    public boolean isIllusionist() {
        return !(getClassId() != 6671 && getClassId() != 6650);
    }

    public String getAccountName() {
        return this._accountName;
    }

    public void setAccountName(String s) {
        this._accountName = s;
    }

    public short getBaseMaxHp() {
        return this._baseMaxHp;
    }

    public void addBaseMaxHp(short i) {
        i = (short)(i + this._baseMaxHp);
        if (i >= Short.MAX_VALUE) {
            i = Short.MAX_VALUE;
        } else if (i < 1) {
            i = 1;
        }
        addMaxHp(i - this._baseMaxHp);
        this._baseMaxHp = i;
    }

    public short getBaseMaxMp() {
        return this._baseMaxMp;
    }

    public void addBaseMaxMp(short i) {
        i = (short)(i + this._baseMaxMp);
        if (i >= Short.MAX_VALUE) {
            i = Short.MAX_VALUE;
        } else if (i < 1) {
            i = 1;
        }
        addMaxMp(i - this._baseMaxMp);
        this._baseMaxMp = i;
    }

    public int getBaseAc() {
        return this._baseAc;
    }

    public int getOriginalAc() {
        return this._originalAc;
    }

    public int getBaseStr() {
        return this._baseStr;
    }

    public void addBaseStr(int i) {
        i += this._baseStr;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addStr(i - this._baseStr);
        this._baseStr = i;
    }

    public int getBaseCon() {
        return this._baseCon;
    }

    public void addBaseCon(int i) {
        i += this._baseCon;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addCon(i - this._baseCon);
        this._baseCon = i;
    }

    public int getBaseDex() {
        return this._baseDex;
    }

    public void addBaseDex(int i) {
        i += this._baseDex;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addDex(i - this._baseDex);
        this._baseDex = i;
    }

    public int getBaseCha() {
        return this._baseCha;
    }

    public void addBaseCha(int i) {
        i += this._baseCha;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addCha(i - this._baseCha);
        this._baseCha = i;
    }

    public int getBaseInt() {
        return this._baseInt;
    }

    public void addBaseInt(int i) {
        i += this._baseInt;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addInt(i - this._baseInt);
        this._baseInt = i;
    }

    public int getBaseWis() {
        return this._baseWis;
    }

    public void addBaseWis(int i) {
        i += this._baseWis;
        if (i >= 254) {
            i = 254;
        } else if (i < 1) {
            i = 1;
        }
        addWis(i - this._baseWis);
        this._baseWis = i;
    }

    public int getOriginalStr() {
        return this._originalStr;
    }

    public void setOriginalStr(int i) {
        this._originalStr = i;
    }

    public int getOriginalCon() {
        return this._originalCon;
    }

    public void setOriginalCon(int i) {
        this._originalCon = i;
    }

    public int getOriginalDex() {
        return this._originalDex;
    }

    public void setOriginalDex(int i) {
        this._originalDex = i;
    }

    public int getOriginalCha() {
        return this._originalCha;
    }

    public void setOriginalCha(int i) {
        this._originalCha = i;
    }

    public int getOriginalInt() {
        return this._originalInt;
    }

    public void setOriginalInt(int i) {
        this._originalInt = i;
    }

    public int getOriginalWis() {
        return this._originalWis;
    }

    public void setOriginalWis(int i) {
        this._originalWis = i;
    }

    public int getOriginalDmgup() {
        return this._originalDmgup;
    }

    public int getOriginalBowDmgup() {
        return this._originalBowDmgup;
    }

    public int getOriginalHitup() {
        return this._originalHitup;
    }

    public int getOriginalBowHitup() {
        return this._originalHitup + this._originalBowHitup;
    }

    public int getOriginalMr() {
        return this._originalMr;
    }

    public int getOriginalMagicHit() {
        return this._originalMagicHit;
    }

    public int getOriginalMagicCritical() {
        return this._originalMagicCritical;
    }

    public int getOriginalMagicConsumeReduction() {
        return this._originalMagicConsumeReduction;
    }

    public int getOriginalMagicDamage() {
        return this._originalMagicDamage;
    }

    public int getOriginalHpup() {
        return this._originalHpup;
    }

    public int getOriginalMpup() {
        return this._originalMpup;
    }

    public int getBaseDmgup() {
        return this._baseDmgup;
    }

    public int getBaseBowDmgup() {
        return this._baseBowDmgup;
    }

    public int getBaseHitup() {
        return this._baseHitup;
    }

    public int getBaseBowHitup() {
        return this._baseBowHitup;
    }

    public int getBaseMr() {
        return this._baseMr;
    }

    public int getAdvenHp() {
        return this._advenHp;
    }

    public void setAdvenHp(int i) {
        this._advenHp = i;
    }

    public int getAdvenMp() {
        return this._advenMp;
    }

    public void setAdvenMp(int i) {
        this._advenMp = i;
    }

    public int getHighLevel() {
        return this._highLevel;
    }

    public void setHighLevel(int i) {
        this._highLevel = i;
    }

    public int getBonusStats() {
        return this._bonusStats;
    }

    public void setBonusStats(int i) {
        this._bonusStats = i;
    }

    public int getElixirStats() {
        return this._elixirStats;
    }

    public void setElixirStats(int i) {
        this._elixirStats = i;
    }

    public int getElfAttr() {
        return this._elfAttr;
    }

    public void setElfAttr(int i) {
        this._elfAttr = i;
    }

    public int getExpRes() {
        return this._expRes;
    }

    public void setExpRes(int i) {
        this._expRes = i;
    }

    public int getPartnerId() {
        return this._partnerId;
    }

    public void setPartnerId(int i) {
        this._partnerId = i;
    }

    public int getOnlineStatus() {
        return this._onlineStatus;
    }

    public void setOnlineStatus(int i) {
        this._onlineStatus = i;
    }

    public int getHomeTownId() {
        return this._homeTownId;
    }

    public void setHomeTownId(int i) {
        this._homeTownId = i;
    }

    public int getContribution() {
        return this._contribution;
    }

    public void setContribution(int i) {
        this._contribution = i;
    }

    public int getHellTime() {
        return this._hellTime;
    }

    public void setHellTime(int i) {
        this._hellTime = i;
    }

    public boolean isBanned() {
        return this._banned;
    }

    public void setBanned(boolean flag) {
        this._banned = flag;
    }

    public int get_food() {
        return this._food;
    }

    public void set_food(int i) {
        if (i > 225)
            i = 225;
        this._food = i;
        if (this._food == 225) {
            Calendar cal = Calendar.getInstance();
            long h_time = cal.getTimeInMillis() / 1000L;
            set_h_time(h_time);
        } else {
            set_h_time(-1L);
        }
    }

    public L1EquipmentSlot getEquipSlot() {
        return this._equipSlot;
    }

    public static L1PcInstance load(String charName) {
        L1PcInstance result = null;
        try {
            result = CharacterTable.get().loadCharacter(charName);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
        return result;
    }

    public void save() throws Exception {
        if (isGhost())
            return;
        if (isInCharReset())
            return;
        if (this._other != null)
            CharOtherReading.get().storeOther(getId(), this._other);
        CharacterTable.get().storeCharacter(this);
    }

    public void saveInventory() throws Exception {
        for (L1ItemInstance item : getInventory().getItems())
            getInventory().saveItem(item, item.getRecordingColumns());
    }

    public double getMaxWeight() {
        int str = getStr();
        int con = getCon();
        double maxWeight = 150.0D * Math.floor(0.6D * str + 0.4D * con + 1.0D) * get_weightUP();
        double weightReductionByArmor = getWeightReduction();
        weightReductionByArmor /= 100.0D;
        int weightReductionByMagic = 0;
        if (hasSkillEffect(14))
            weightReductionByMagic = 180;
        double originalWeightReduction = 0.0D;
        originalWeightReduction += 0.04D * (getOriginalStrWeightReduction() + getOriginalConWeightReduction());
        double weightReduction = 1.0D + weightReductionByArmor + originalWeightReduction;
        maxWeight *= weightReduction;
        maxWeight += weightReductionByMagic;
        maxWeight *= ConfigRate.RATE_WEIGHT_LIMIT;
        return maxWeight;
    }

    public boolean isFastMovable() {
        return !(!hasSkillEffect(52) && !hasSkillEffect(101) && !hasSkillEffect(150) && !hasSkillEffect(1017));
    }

    public boolean isFastAttackable() {
        return hasSkillEffect(186);
    }

    public boolean isBrave() {
        return hasSkillEffect(1000);
    }

    public boolean isElfBrave() {
        return hasSkillEffect(1016);
    }

    public boolean isBraveX() {
        return hasSkillEffect(998);
    }

    public boolean isHaste() {
        return !(!hasSkillEffect(1001) && !hasSkillEffect(43) && !hasSkillEffect(54) && getMoveSpeed() != 1);
    }

    public boolean isInvisDelay() {
        return (this.invisDelayCounter > 0);
    }

    public void addInvisDelayCounter(int counter) {
        synchronized (this._invisTimerMonitor) {
            this.invisDelayCounter += counter;
        }
    }

    public void beginInvisTimer() {
        addInvisDelayCounter(1);
        GeneralThreadPool.get().pcSchedule((L1PcMonitor)new L1PcInvisDelay(getId()), 3000L);
    }

    public synchronized void addContribution(int contribution) {
        this._contribution += contribution;
    }

    public synchronized void addExp(long exp) {
        long newexp = this._exp + exp;
        if (!isAddExp(newexp))
            return;
        this._exp = newexp;
    }

    private boolean isAddExp(long exp) {
        int level = ConfigOther.MAX_LEVEL + 1;
        long maxExp = (long)(ExpTable.getExpByLevel(level) - ExpTable.getNeedExpNextLevel(level) * 0.9D);
        if (exp >= maxExp) {
            this._exp = maxExp;
            return false;
        }
        return true;
    }

    private void levelUp(int gap) {
        resetLevel();
        for (int i = 0; i < gap; i++) {
            short randomHp = CalcStat.calcStatHp(getType(), getBaseMaxHp(), getBaseCon(), getOriginalHpup());
            short randomMp = CalcStat.calcStatMp(getType(), getBaseMaxMp(), getBaseWis(), getOriginalMpup());
            addBaseMaxHp(randomHp);
            addBaseMaxMp(randomMp);
        }
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        if (getLevel() > getHighLevel())
            setHighLevel(getLevel());
        setCurrentHp(getMaxHp());
        setCurrentMp(getMaxMp());
        try {
            save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            if (power())
                sendPackets((ServerBasePacket)new S_Bonusstats(getId()));
            Reward.getItem(this);
            sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
            MapLevelTable.get().get_level(getMapId(), this);
        }
    }

    public void showWindows() {
        if (power())
            sendPackets((ServerBasePacket)new S_Bonusstats(getId()));
    }

    public void isWindows() {
        if (power()) {
            sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "y_qs_10"));
        } else {
            sendPackets((ServerBasePacket)new S_NPCTalkReturn(getId(), "y_qs_00"));
        }
    }

    public boolean power() {
        if (getLevel() >= 51 && getLevel() - 50 > getBonusStats()) {
            int power = getBaseStr() + getBaseDex() + getBaseCon() + getBaseInt() + getBaseWis() + getBaseCha();
            if (power < ConfigAlt.POWER * 6)
                return true;
        }
        return false;
    }

    private void levelDown(int gap) {
        resetLevel();
        for (int i = 0; i > gap; i--) {
            short randomHp = CalcStat.calcStatHp(getType(), 0, getBaseCon(), getOriginalHpup());
            short randomMp = CalcStat.calcStatMp(getType(), 0, getBaseWis(), getOriginalMpup());
            addBaseMaxHp((short)-randomHp);
            addBaseMaxMp((short)-randomMp);
        }
        if (getLevel() == 1) {
            int initHp = CalcInitHpMp.calcInitHp(this);
            int initMp = CalcInitHpMp.calcInitMp(this);
            addBaseMaxHp((short)-getBaseMaxHp());
            addBaseMaxHp((short)initHp);
            setCurrentHp((short)initHp);
            addBaseMaxMp((short)-getBaseMaxMp());
            addBaseMaxMp((short)initMp);
            setCurrentMp((short)initMp);
        }
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseAc();
        resetBaseMr();
        try {
            save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            sendPackets((ServerBasePacket)new S_OwnCharStatus(this));
            MapLevelTable.get().get_level(getMapId(), this);
        }
    }

    public boolean isGhost() {
        return this._ghost;
    }

    private void setGhost(boolean flag) {
        this._ghost = flag;
    }

    public int get_ghostTime() {
        return this._ghostTime;
    }

    public void set_ghostTime(int ghostTime) {
        this._ghostTime = ghostTime;
    }

    public boolean isGhostCanTalk() {
        return this._ghostCanTalk;
    }

    private void setGhostCanTalk(boolean flag) {
        this._ghostCanTalk = flag;
    }

    public boolean isReserveGhost() {
        return this._isReserveGhost;
    }

    private void setReserveGhost(boolean flag) {
        this._isReserveGhost = flag;
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk) {
        beginGhost(locx, locy, mapid, canTalk, 0);
    }

    public void beginGhost(int locx, int locy, short mapid, boolean canTalk, int sec) {
        if (isGhost())
            return;
        setGhost(true);
        this._ghostSaveLocX = getX();
        this._ghostSaveLocY = getY();
        this._ghostSaveMapId = getMapId();
        this._ghostSaveHeading = getHeading();
        setGhostCanTalk(canTalk);
        L1Teleport.teleport(this, locx, locy, mapid, 5, true);
        if (sec > 0)
            set_ghostTime(sec);
    }

    public void makeReadyEndGhost() {
        setReserveGhost(true);
        L1Teleport.teleport(this, this._ghostSaveLocX, this._ghostSaveLocY, this._ghostSaveMapId, this._ghostSaveHeading, true);
    }

    public void endGhost() {
        set_ghostTime(-1);
        setGhost(false);
        setGhostCanTalk(true);
        setReserveGhost(false);
    }

    public void beginHell(boolean isFirst) {
        if (getMapId() != 666) {
            int locx = 32701;
            int locy = 32777;
            short mapid = 666;
            L1Teleport.teleport(this, 32701, 32777, (short)666, 5, false);
        }
        if (isFirst) {
            if (get_PKcount() <= 10) {
                setHellTime(300);
            } else {
                setHellTime(300 * (get_PKcount() - 10) + 300);
            }
            sendPackets((ServerBasePacket)new S_BlueMessage(552, String.valueOf(get_PKcount()), String.valueOf(getHellTime() / 60)));
        } else {
            sendPackets((ServerBasePacket)new S_BlueMessage(637, String.valueOf(getHellTime())));
        }
    }

    public void endHell() {
        int[] loc = L1TownLocation.getGetBackLoc(4);
        L1Teleport.teleport(this, loc[0], loc[1], (short)loc[2], 5, true);
        try {
            save();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void setPoisonEffect(int effectId) {
        sendPackets((ServerBasePacket)new S_Poison(getId(), effectId));
        if (!isGmInvis() && !isGhost() && !isInvisble())
            broadcastPacketAll((ServerBasePacket)new S_Poison(getId(), effectId));
    }

    public void healHp(int pt) {
        super.healHp(pt);
        sendPackets((ServerBasePacket)new S_HPUpdate(this));
    }

    public int getKarma() {
        return this._karma.get();
    }

    public void setKarma(int i) {
        this._karma.set(i);
    }

    public void addKarma(int i) {
        synchronized (this._karma) {
            this._karma.add(i);
        }
    }

    public int getKarmaLevel() {
        return this._karma.getLevel();
    }

    public int getKarmaPercent() {
        return this._karma.getPercent();
    }

    public Timestamp getLastPk() {
        return this._lastPk;
    }

    public void setLastPk(Timestamp time) {
        this._lastPk = time;
    }

    public void setLastPk() {
        this._lastPk = new Timestamp(System.currentTimeMillis());
    }

    public boolean isWanted() {
        if (this._lastPk == null)
            return false;
        if (System.currentTimeMillis() - this._lastPk.getTime() > 3600000L) {
            setLastPk((Timestamp)null);
            return false;
        }
        return true;
    }

    public Timestamp getLastPkForElf() {
        return this._lastPkForElf;
    }

    public void setLastPkForElf(Timestamp time) {
        this._lastPkForElf = time;
    }

    public void setLastPkForElf() {
        this._lastPkForElf = new Timestamp(System.currentTimeMillis());
    }

    public boolean isWantedForElf() {
        if (this._lastPkForElf == null)
            return false;
        if (System.currentTimeMillis() - this._lastPkForElf.getTime() > 86400000L) {
            setLastPkForElf((Timestamp)null);
            return false;
        }
        return true;
    }

    public Timestamp getDeleteTime() {
        return this._deleteTime;
    }

    public void setDeleteTime(Timestamp time) {
        this._deleteTime = time;
    }

    public int getMagicLevel() {
        return getClassFeature().getMagicLevel(getLevel());
    }

    public double get_weightUP() {
        return this._weightUP;
    }

    public void add_weightUP(int i) {
        this._weightUP += i / 100.0D;
    }

    public int getWeightReduction() {
        return this._weightReduction;
    }

    public void addWeightReduction(int i) {
        this._weightReduction += i;
    }

    public int getOriginalStrWeightReduction() {
        return this._originalStrWeightReduction;
    }

    public int getOriginalConWeightReduction() {
        return this._originalConWeightReduction;
    }

    public int getHasteItemEquipped() {
        return this._hasteItemEquipped;
    }

    public void addHasteItemEquipped(int i) {
        this._hasteItemEquipped += i;
    }

    public void removeHasteSkillEffect() {
        if (hasSkillEffect(29))
            removeSkillEffect(29);
        if (hasSkillEffect(76))
            removeSkillEffect(76);
        if (hasSkillEffect(152))
            removeSkillEffect(152);
        if (hasSkillEffect(43))
            removeSkillEffect(43);
        if (hasSkillEffect(54))
            removeSkillEffect(54);
        if (hasSkillEffect(1001))
            removeSkillEffect(1001);
    }

    public int getDamageReductionByArmor() {
        int damageReduction = 0;
        if (this._damageReductionByArmor > 10) {
            damageReduction = 10 + _random.nextInt(this._damageReductionByArmor - 10) + 1;
        } else {
            damageReduction = this._damageReductionByArmor;
        }
        return damageReduction;
    }

    public void addDamageReductionByArmor(int i) {
        this._damageReductionByArmor += i;
    }

    public int getHitModifierByArmor() {
        return this._hitModifierByArmor;
    }

    public void addHitModifierByArmor(int i) {
        this._hitModifierByArmor += i;
    }

    public int getDmgModifierByArmor() {
        return this._dmgModifierByArmor;
    }

    public void addDmgModifierByArmor(int i) {
        this._dmgModifierByArmor += i;
    }

    public int getBowHitModifierByArmor() {
        return this._bowHitModifierByArmor;
    }

    public void addBowHitModifierByArmor(int i) {
        this._bowHitModifierByArmor += i;
    }

    public int getBowDmgModifierByArmor() {
        return this._bowDmgModifierByArmor;
    }

    public void addBowDmgModifierByArmor(int i) {
        this._bowDmgModifierByArmor += i;
    }

    private void setGresValid(boolean valid) {
        this._gresValid = valid;
    }

    public boolean isGresValid() {
        return this._gresValid;
    }

    public boolean isFishing() {
        return this._isFishing;
    }

    public int get_fishX() {
        return this._fishX;
    }

    public int get_fishY() {
        return this._fishY;
    }

    public void setFishing(boolean flag, int fishX, int fishY) {
        this._isFishing = flag;
        this._fishX = fishX;
        this._fishY = fishY;
    }

    public int getCookingId() {
        return this._cookingId;
    }

    public void setCookingId(int i) {
        this._cookingId = i;
    }

    public int getDessertId() {
        return this._dessertId;
    }

    public void setDessertId(int i) {
        this._dessertId = i;
    }

    public void resetBaseDmgup() {
        int newBaseDmgup = 0;
        int newBaseBowDmgup = 0;
        if (isKnight() || isDarkelf() || isDragonKnight()) {
            newBaseDmgup = getLevel() / 10;
            newBaseBowDmgup = 0;
        } else if (isElf()) {
            newBaseDmgup = 0;
            newBaseBowDmgup = getLevel() / 10;
        }
        addDmgup(newBaseDmgup - this._baseDmgup);
        addBowDmgup(newBaseBowDmgup - this._baseBowDmgup);
        this._baseDmgup = newBaseDmgup;
        this._baseBowDmgup = newBaseBowDmgup;
    }

    public void resetBaseHitup() {
        int newBaseHitup = 0;
        int newBaseBowHitup = 0;
        if (isCrown()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        } else if (isKnight()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isElf()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        } else if (isDarkelf()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isDragonKnight()) {
            newBaseHitup = getLevel() / 3;
            newBaseBowHitup = getLevel() / 3;
        } else if (isIllusionist()) {
            newBaseHitup = getLevel() / 5;
            newBaseBowHitup = getLevel() / 5;
        }
        addHitup(newBaseHitup - this._baseHitup);
        addBowHitup(newBaseBowHitup - this._baseBowHitup);
        this._baseHitup = newBaseHitup;
        this._baseBowHitup = newBaseBowHitup;
    }

    public void resetBaseAc() {
        int newAc = CalcStat.calcAc(getLevel(), getBaseDex());
        addAc(newAc - this._baseAc);
        this._baseAc = newAc;
    }

    public void resetBaseMr() {
        int newMr = 0;
        if (isCrown()) {
            newMr = 10;
        } else if (isElf()) {
            newMr = 25;
        } else if (isWizard()) {
            newMr = 15;
        } else if (isDarkelf()) {
            newMr = 10;
        } else if (isDragonKnight()) {
            newMr = 18;
        } else if (isIllusionist()) {
            newMr = 20;
        }
        newMr += CalcStat.calcStatMr(getWis());
        newMr += getLevel() / 2;
        addMr(newMr - this._baseMr);
        this._baseMr = newMr;
    }

    public void resetLevel() {
        setLevel(ExpTable.getLevelByExp(this._exp));
    }

    public void resetOriginalHpup() {
        this._originalHpup = L1PcOriginal.resetOriginalHpup(this);
    }

    public void resetOriginalMpup() {
        this._originalMpup = L1PcOriginal.resetOriginalMpup(this);
    }

    public void resetOriginalStrWeightReduction() {
        this._originalStrWeightReduction = L1PcOriginal.resetOriginalStrWeightReduction(this);
    }

    public void resetOriginalDmgup() {
        this._originalDmgup = L1PcOriginal.resetOriginalDmgup(this);
    }

    public void resetOriginalConWeightReduction() {
        this._originalConWeightReduction = L1PcOriginal.resetOriginalConWeightReduction(this);
    }

    public void resetOriginalBowDmgup() {
        this._originalBowDmgup = L1PcOriginal.resetOriginalBowDmgup(this);
    }

    public void resetOriginalHitup() {
        this._originalHitup = L1PcOriginal.resetOriginalHitup(this);
    }

    public void resetOriginalBowHitup() {
        this._originalBowHitup = L1PcOriginal.resetOriginalBowHitup(this);
    }

    public void resetOriginalMr() {
        this._originalMr = L1PcOriginal.resetOriginalMr(this);
        addMr(this._originalMr);
    }

    public void resetOriginalMagicHit() {
        this._originalMagicHit = L1PcOriginal.resetOriginalMagicHit(this);
    }

    public void resetOriginalMagicCritical() {
        this._originalMagicCritical = L1PcOriginal.resetOriginalMagicCritical(this);
    }

    public void resetOriginalMagicConsumeReduction() {
        this._originalMagicConsumeReduction = L1PcOriginal.resetOriginalMagicConsumeReduction(this);
    }

    public void resetOriginalMagicDamage() {
        this._originalMagicDamage = L1PcOriginal.resetOriginalMagicDamage(this);
    }

    public void resetOriginalAc() {
        this._originalAc = L1PcOriginal.resetOriginalAc(this);
        addAc(0 - this._originalAc);
    }

    public void resetOriginalEr() {
        this._originalEr = L1PcOriginal.resetOriginalEr(this);
    }

    public void resetOriginalHpr() {
        this._originalHpr = L1PcOriginal.resetOriginalHpr(this);
    }

    public void resetOriginalMpr() {
        this._originalMpr = L1PcOriginal.resetOriginalMpr(this);
    }

    public void refresh() {
        resetLevel();
        resetBaseHitup();
        resetBaseDmgup();
        resetBaseMr();
        resetBaseAc();
        resetOriginalHpup();
        resetOriginalMpup();
        resetOriginalDmgup();
        resetOriginalBowDmgup();
        resetOriginalHitup();
        resetOriginalBowHitup();
        resetOriginalMr();
        resetOriginalMagicHit();
        resetOriginalMagicCritical();
        resetOriginalMagicConsumeReduction();
        resetOriginalMagicDamage();
        resetOriginalAc();
        resetOriginalEr();
        resetOriginalHpr();
        resetOriginalMpr();
        resetOriginalStrWeightReduction();
        resetOriginalConWeightReduction();
    }

    public L1ExcludingList getExcludingList() {
        return this._excludingList;
    }

    public int getTeleportX() {
        return this._teleportX;
    }

    public void setTeleportX(int i) {
        this._teleportX = i;
    }

    public int getTeleportY() {
        return this._teleportY;
    }

    public void setTeleportY(int i) {
        this._teleportY = i;
    }

    public short getTeleportMapId() {
        return this._teleportMapId;
    }

    public void setTeleportMapId(short i) {
        this._teleportMapId = i;
    }

    public int getTeleportHeading() {
        return this._teleportHeading;
    }

    public void setTeleportHeading(int i) {
        this._teleportHeading = i;
    }

    public int getTempCharGfxAtDead() {
        return this._tempCharGfxAtDead;
    }

    private void setTempCharGfxAtDead(int i) {
        this._tempCharGfxAtDead = i;
    }

    public boolean isCanWhisper() {
        return this._isCanWhisper;
    }

    public void setCanWhisper(boolean flag) {
        this._isCanWhisper = flag;
    }

    public boolean isShowTradeChat() {
        return this._isShowTradeChat;
    }

    public void setShowTradeChat(boolean flag) {
        this._isShowTradeChat = flag;
    }

    public boolean isShowWorldChat() {
        return this._isShowWorldChat;
    }

    public void setShowWorldChat(boolean flag) {
        this._isShowWorldChat = flag;
    }

    public int getFightId() {
        return this._fightId;
    }

    public void setFightId(int i) {
        this._fightId = i;
    }

    public void checkChatInterval() {
        long nowChatTimeInMillis = System.currentTimeMillis();
        if (this._chatCount == 0) {
            this._chatCount = (byte)(this._chatCount + 1);
            this._oldChatTimeInMillis = nowChatTimeInMillis;
            return;
        }
        long chatInterval = nowChatTimeInMillis - this._oldChatTimeInMillis;
        if (chatInterval > 2000L) {
            this._chatCount = 0;
            this._oldChatTimeInMillis = 0L;
        } else {
            if (this._chatCount >= 3) {
                setSkillEffect(4002, 120000);
                sendPackets((ServerBasePacket)new S_PacketBox(36, 120));
                sendPackets((ServerBasePacket)new S_ServerMessage(153));
                this._chatCount = 0;
                this._oldChatTimeInMillis = 0L;
            }
            this._chatCount = (byte)(this._chatCount + 1);
        }
    }

    public int getCallClanId() {
        return this._callClanId;
    }

    public void setCallClanId(int i) {
        this._callClanId = i;
    }

    public int getCallClanHeading() {
        return this._callClanHeading;
    }

    public void setCallClanHeading(int i) {
        this._callClanHeading = i;
    }

    public boolean isInCharReset() {
        return this._isInCharReset;
    }

    public void setInCharReset(boolean flag) {
        this._isInCharReset = flag;
    }

    public int getTempLevel() {
        return this._tempLevel;
    }

    public void setTempLevel(int i) {
        this._tempLevel = i;
    }

    public int getTempMaxLevel() {
        return this._tempMaxLevel;
    }

    public void setTempMaxLevel(int i) {
        this._tempMaxLevel = i;
    }

    public void setSummonMonster(boolean SummonMonster) {
        this._isSummonMonster = SummonMonster;
    }

    public boolean isSummonMonster() {
        return this._isSummonMonster;
    }

    public void setShapeChange(boolean isShapeChange) {
        this._isShapeChange = isShapeChange;
    }

    public boolean isShapeChange() {
        return this._isShapeChange;
    }

    public void setText(String text) {
        this._text = text;
    }

    public String getText() {
        return this._text;
    }

    public void setTextByte(byte[] textByte) {
        this._textByte = textByte;
    }

    public byte[] getTextByte() {
        return this._textByte;
    }

    public void set_other(L1PcOther other) {
        this._other = other;
    }

    public L1PcOther get_other() {
        return this._other;
    }

    public void set_otherList(L1PcOtherList other) {
        this._otherList = other;
    }

    public L1PcOtherList get_otherList() {
        return this._otherList;
    }

    public void setOleLocX(int oleLocx) {
        this._oleLocX = oleLocx;
    }

    public int getOleLocX() {
        return this._oleLocX;
    }

    public void setOleLocY(int oleLocy) {
        this._oleLocY = oleLocy;
    }

    public int getOleLocY() {
        return this._oleLocY;
    }

    public void setNowTarget(L1PcInstance target) {
        this._target = target;
    }

    public L1PcInstance getNowTarget() {
        return this._target;
    }

    public void set_dmgDown(int dmgDown) {
        this._dmgDown = dmgDown;
    }

    public int get_dmgDown() {
        return this._dmgDown;
    }

    public void setPetModel() {
        try {
            for (L1NpcInstance petNpc : getPetList().values()) {
                if (petNpc != null) {
                    if (petNpc instanceof L1SummonInstance) {
                        L1SummonInstance summon = (L1SummonInstance)petNpc;
                        summon.set_tempModel();
                        continue;
                    }
                    if (petNpc instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance)petNpc;
                        pet.set_tempModel();
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void getPetModel() {
        try {
            for (L1NpcInstance petNpc : getPetList().values()) {
                if (petNpc != null) {
                    if (petNpc instanceof L1SummonInstance) {
                        L1SummonInstance summon = (L1SummonInstance)petNpc;
                        summon.get_tempModel();
                        continue;
                    }
                    if (petNpc instanceof L1PetInstance) {
                        L1PetInstance pet = (L1PetInstance)petNpc;
                        pet.get_tempModel();
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public long get_h_time() {
        return this._h_time;
    }

    public void set_h_time(long time) {
        this._h_time = time;
    }

    public void set_dmgAdd(int int1, int int2) {
        this._int1 += int1;
        this._int2 += int2;
    }

    public int dmgAdd() {
        if (this._int2 == 0)
            return 0;
        if (_random.nextInt(100) + 1 <= this._int2) {
            if (!getDolls().isEmpty())
                for (L1DollInstance doll : getDolls().values())
                    doll.show_action(1);
            return this._int1;
        }
        return 0;
    }

    public void set_evasion(int int1) {
        this._evasion += int1;
    }

    public int get_evasion() {
        return this._evasion;
    }

    public void set_expadd(int int1) {
        this._expadd += int1 / 100.0D;
    }

    public double getExpAdd() {
        return this._expadd;
    }

    public void set_dmgDowe(int int1, int int2) {
        this._dd1 += int1;
        this._dd2 += int2;
    }

    public int dmgDowe() {
        if (this._dd2 == 0)
            return 0;
        if (_random.nextInt(100) + 1 <= this._dd2) {
            if (!getDolls().isEmpty())
                for (L1DollInstance doll : getDolls().values())
                    doll.show_action(2);
            return this._dd1;
        }
        return 0;
    }

    public boolean isFoeSlayer() {
        return this._isFoeSlayer;
    }

    public void isFoeSlayer(boolean isFoeSlayer) {
        this._isFoeSlayer = isFoeSlayer;
    }

    public long get_weaknss_t() {
        return this._weaknss_t;
    }

    public int get_weaknss() {
        return this._weaknss;
    }

    public void set_weaknss(int lv, long t) {
        this._weaknss = lv;
        this._weaknss_t = t;
    }

    public void set_actionId(int actionId) {
        this._actionId = actionId;
    }

    public int get_actionId() {
        return this._actionId;
    }

    public void set_hardinR(Chapter01R hardin) {
        this._hardin = hardin;
    }

    public Chapter01R get_hardinR() {
        return this._hardin;
    }

    public void set_unfreezingTime(int i) {
        this._unfreezingTime = i;
    }

    public int get_unfreezingTime() {
        return this._unfreezingTime;
    }

    public void add_magic_modifier_dmg(int add) {
        this._magic_modifier_dmg += add;
    }

    public int get_magic_modifier_dmg() {
        return this._magic_modifier_dmg;
    }

    public void add_magic_reduction_dmg(int add) {
        this._magic_reduction_dmg += add;
    }

    public int get_magic_reduction_dmg() {
        return this._magic_reduction_dmg;
    }

    public void rename(boolean b) {
        this._rname = b;
    }

    public boolean is_rname() {
        return this._rname;
    }

    public boolean is_retitle() {
        return this._retitle;
    }

    public void retitle(boolean b) {
        this._retitle = b;
    }

    public int is_repass() {
        return this._repass;
    }

    public void repass(int b) {
        this._repass = b;
    }

    public void add_trade_item(L1TradeItem info) {
        if (this._trade_items.size() == 16)
            return;
        this._trade_items.add(info);
    }

    public ArrayList<L1TradeItem> get_trade_items() {
        return this._trade_items;
    }

    public void get_trade_clear() {
        this._tradeID = 0;
        this._trade_items.clear();
    }

    public void set_mode_id(int mode) {
        this._mode_id = mode;
    }

    public int get_mode_id() {
        return this._mode_id;
    }

    public void set_check_item(boolean b) {
        this._check_item = b;
    }

    public boolean get_check_item() {
        return this._check_item;
    }

    public long get_global_time() {
        return this._global_time;
    }

    public void set_global_time(long global_time) {
        this._global_time = global_time;
    }

    public int get_doll_hpr() {
        return this._doll_hpr;
    }

    public void set_doll_hpr(int hpr) {
        this._doll_hpr = hpr;
    }

    public int get_doll_hpr_time() {
        return this._doll_hpr_time;
    }

    public void set_doll_hpr_time(int time) {
        this._doll_hpr_time = time;
    }

    public int get_doll_hpr_time_src() {
        return this._doll_hpr_time_src;
    }

    public void set_doll_hpr_time_src(int time) {
        this._doll_hpr_time_src = time;
    }

    public int get_doll_mpr() {
        return this._doll_mpr;
    }

    public void set_doll_mpr(int mpr) {
        this._doll_mpr = mpr;
    }

    public int get_doll_mpr_time() {
        return this._doll_mpr_time;
    }

    public void set_doll_mpr_time(int time) {
        this._doll_mpr_time = time;
    }

    public int get_doll_mpr_time_src() {
        return this._doll_mpr_time_src;
    }

    public void set_doll_mpr_time_src(int time) {
        this._doll_mpr_time_src = time;
    }

    public int[] get_doll_get() {
        return this._doll_get;
    }

    public void set_doll_get(int itemid, int count) {
        this._doll_get[0] = itemid;
        this._doll_get[1] = count;
    }

    public int get_doll_get_time() {
        return this._doll_get_time;
    }

    public void set_doll_get_time(int time) {
        this._doll_get_time = time;
    }

    public int get_doll_get_time_src() {
        return this._doll_get_time_src;
    }

    public void set_doll_get_time_src(int time) {
        this._doll_get_time_src = time;
    }

    public void set_board_title(String text) {
        this._board_title = text;
    }

    public String get_board_title() {
        return this._board_title;
    }

    public void set_board_content(String text) {
        this._board_content = text;
    }

    public String get_board_content() {
        return this._board_content;
    }

    public void set_spr_move_time(long spr_time) {
        this._spr_move_time = spr_time;
    }

    public long get_spr_move_time() {
        return this._spr_move_time;
    }

    public void set_spr_attack_time(long spr_time) {
        this._spr_attack_time = spr_time;
    }

    public long get_spr_attack_time() {
        return this._spr_attack_time;
    }

    public void set_spr_skill_time(long spr_time) {
        this._spr_skill_time = spr_time;
    }

    public long get_spr_skill_time() {
        return this._spr_skill_time;
    }

    public void set_delete_time(int time) {
        this._delete_time = time;
    }

    public int get_delete_time() {
        return this._delete_time;
    }

    public void set_up_hp_potion(int up_hp_potion) {
        this._up_hp_potion = up_hp_potion;
    }

    public int get_up_hp_potion() {
        return this._up_hp_potion;
    }

    public void set_elitePlateMail_Fafurion(int r, int hpmin, int hpmax) {
        this._elitePlateMail_Fafurion = r;
        this._fafurion_hpmin = hpmin;
        this._fafurion_hpmax = hpmax;
    }

    public void set_venom_resist(int i) {
        this._venom_resist += i;
    }

    public int get_venom_resist() {
        return this._venom_resist;
    }

    public AcceleratorChecker speed_Attack() {
        if (this._speed == null)
            this._speed = new AcceleratorChecker(this);
        return this._speed;
    }

    public final int getKillCount() {
        return this.killCount;
    }

    public final void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    public void setSummonId(int SummonId) {
        this._SummonId = SummonId;
    }

    public int getSummonId() {
        return this._SummonId;
    }

    public void ChangPassword(String password) {
        try {
            String newPassword = "";
            try {
                newPassword = encodePassword(password);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            AccountTable.changpassword(getAccountName(), newPassword);
            sendPackets((ServerBasePacket)new S_SystemMessage("+ password + "));
        } catch (Exception ignored) {}
    }

    private static String encodePassword(String rawPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] buf = rawPassword.getBytes("UTF-8");
        buf = MessageDigest.getInstance("SHA").digest(buf);
        return rawPassword;
    }

    public void AllDeleteCharacter(int A) {
        try {
            sendPackets(new S_SystemMessage("因為你不遵守伺服器遊戲規則，已被強制踢除遊戲並刪除角色!"));
            Thread.sleep((long) A);
            sendPackets(new S_Disconnect());
            new MySqlCharacterStorage().deleteCharacter(getAccountName(), getName());
        } catch (Exception ignored) {
        }
    }

    public void DeleteCharacter(int A) {
        try {
            sendPackets(new S_SystemMessage("因為你不遵守伺服器遊戲規則，已被強制踢除遊戲並刪除角色!"));
            Thread.sleep((long) A);
            sendPackets(new S_Disconnect());
            new MySqlCharacterStorage().deleteCharacter(getAccountName(), getName());
        } catch (Exception ignored) {
        }
    }


    public int getSuper() {
        return this._super;
    }

    public void setSuper(int i) {
        this._super = i;
    }

    public int getAi_Number() {
        return this._ai_number;
    }

    public void setAi_Number(int flag) {
        this._ai_number = flag;
    }

    public void set_point(int points) {
        this._points = points;
    }

    public int get_point() {
        return this._points;
    }

    public void setWeaponItemObjId(int itemObjId, int index) {
        this._weaponObjIdList[index] = itemObjId;
    }

    public int getWeaponItemObjId(int index) {
        return this._weaponObjIdList[index];
    }

    public int[] getWeaponItemList() {
        return this._weaponObjIdList;
    }

    public void set_allclanbid(boolean flag) {
        this._is_allclanbid = flag;
    }

    public boolean is_allclanbid() {
        return this._is_allclanbid;
    }

    public void setCallClan(boolean flag) {
        this._is_allCallClan = flag;
    }

    public boolean getCallClan() {
        return this._is_allCallClan;
    }

    public void addMatchingList(String name) {
        if (this._MatchingList.contains(name))
            return;
        this._MatchingList.add(name);
    }

    public void removeMatchingList(String name) {
        if (!this._MatchingList.contains(name))
            return;
        this._MatchingList.remove(name);
    }

    public ArrayList<String> getMatchingList() {
        return this._MatchingList;
    }

    public int getExpPoint() {
        return this._expPoint;
    }

    public void setExpPoint(int i) {
        this._expPoint = i;
    }

    public void setCarId(int i) {
        this._CardId += i;
    }

    public int getCardId() {
        return this._CardId;
    }

    public int getCard_Id() {
        return this._card;
    }

    public void setCard_Id(int card) {
        this._card = card;
    }

    public void setCmd(int l) {
        this._cmd = l;
    }

    public int getCmd() {
        return this._cmd;
    }

    public int getDollhole() {
        return this._dollhole;
    }

    public void setDollhole(int i) {
        this._dollhole = i;
    }

    public void addInviteList(String playername) {
        if (this._InviteList.contains(playername))
            return;
        this._InviteList.add(playername);
    }

    public void removeInviteList(String name) {
        if (!this._InviteList.contains(name))
            return;
        this._InviteList.remove(name);
    }

    public ArrayList<String> getInviteList() {
        return this._InviteList;
    }

    public void addCMAList(String clanname) {
        if (this._cmalist.contains(clanname))
            return;
        this._cmalist.add(clanname);
    }

    public void removeCMAList(String name) {
        if (!this._cmalist.contains(name))
            return;
        this._cmalist.remove(name);
    }

    public ArrayList<String> getCMAList() {
        return this._cmalist;
    }

    public int getEarth_Def() {
        return this._earth_def;
    }

    public void setEarth_Def(int i) {
        this._earth_def = i;
    }

    public void setShowEmblem(boolean b) {
        this._showemblem = b;
    }

    public boolean isShowEmblem() {
        return this._showemblem;
    }

    public void set_donus(int donus) {
        this._donus = donus;
    }

    public int getdonus() {
        return this._donus;
    }

    public void set_getbonus(int bonus) {
        this._bonus = bonus;
    }

    public int get_getbonus() {
        return this._bonus;
    }

    public void setTouKuiName(String name) {
        this._toukuiname = name;
    }

    public String getTouKuiName() {
        return this._toukuiname;
    }

    public void set_tuokui_objId(int eq_objId) {
        this._tuokui_objId = eq_objId;
    }

    public int get_tuokui_objId() {
        return this._tuokui_objId;
    }

    public int getUbScore() {
        return this._ubscore;
    }

    public void setUbScore(int i) {
        this._ubscore = i;
    }
}
