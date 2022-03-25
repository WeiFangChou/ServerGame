package com.lineage.server.model;

import com.lineage.config.Config;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.UBSpawnTable;
import com.lineage.server.datatables.UBTable;
import com.lineage.server.datatables.UbSupplies;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.RangeInt;
import com.lineage.server.world.World;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1UltimateBattle {
    private static int BEFORE_MINUTE = 5;
    private static final Log _log = LogFactory.getLog(L1UltimateBattle.class);
    private boolean _active;
    private boolean _enterDarkelf;
    private boolean _enterDragonKnight;
    private boolean _enterElf;
    private boolean _enterFemale;
    private boolean _enterIllusionist;
    private boolean _enterKnight;
    private boolean _enterMage;
    private boolean _enterMale;
    private boolean _enterRoyal;
    private int _hpr;
    private boolean _isNowUb;
    private int _locX;
    private int _locX1;
    private int _locX2;
    private int _locY;
    private int _locY1;
    private int _locY2;
    private L1Location _location;
    private Set<Integer> _managers = new HashSet();
    private short _mapId;
    private int _maxLevel;
    private int _maxPlayer;
    private final ArrayList<L1PcInstance> _members = new ArrayList<>();
    private int _minLevel;
    private int _mpr;
    private int _pattern;
    private int _ubId;
    private String[] _ubInfo;
    private String _ubName;
    private SortedSet<Integer> _ubTimes = new TreeSet();
    private boolean _usePot;

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendRoundMessage(int curRound) {
        sendMessage(new int[]{893, 894, 895, 896}[curRound - 1], "");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void spawnSupplies(int curRound) throws Exception {
        List<L1UbSupplie> sup = UbSupplies.getInstance().getUbSupplies(curRound);
        if (sup.size() != 0) {
            for (L1UbSupplie t : sup) {
                spawnGroundItem(t.getUbItemId(), (long) t.getUbItemStackCont(), t.getUbItemCont());
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void removeRetiredMembers() {
        L1PcInstance[] temp = getMembersArray();
        for (int i = 0; i < temp.length; i++) {
            if (temp[i].getMapId() != this._mapId) {
                removeMember(temp[i]);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void sendMessage(int type, String msg) {
        for (L1PcInstance pc : getMembersArray()) {
            pc.sendPackets(new S_ServerMessage(type, msg));
        }
    }

    private void spawnGroundItem(int itemId, long stackCount, int count) {
        L1Item temp = ItemTable.get().getTemplate(itemId);
        if (temp != null) {
            for (int i = 0; i < count; i++) {
                L1Location loc = this._location.randomLocation((getLocX2() - getLocX1()) / 2, false);
                if (temp.isStackable()) {
                    L1ItemInstance item = ItemTable.get().createItem(itemId);
                    item.setEnchantLevel(0);
                    item.setCount(stackCount);
                    L1GroundInventory ground = World.get().getInventory(loc.getX(), loc.getY(), this._mapId);
                    if (ground.checkAddItem(item, stackCount) == 0) {
                        ground.storeItem(item);
                    }
                } else {
                    for (int createCount = 0; ((long) createCount) < stackCount; createCount++) {
                        L1ItemInstance item2 = ItemTable.get().createItem(itemId);
                        item2.setEnchantLevel(0);
                        L1GroundInventory ground2 = World.get().getInventory(loc.getX(), loc.getY(), this._mapId);
                        if (ground2.checkAddItem(item2, stackCount) == 0) {
                            ground2.storeItem(item2);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void clearColosseum() {
        for (Object obj : World.get().getVisibleObjects(this._mapId).values()) {
            if (obj instanceof L1MonsterInstance) {
                L1MonsterInstance mob = (L1MonsterInstance) obj;
                if (!mob.isDead()) {
                    mob.setDead(true);
                    mob.setStatus(8);
                    mob.setCurrentHpDirect(0);
                    mob.deleteMe();
                }
            } else if (obj instanceof L1Inventory) {
                ((L1Inventory) obj).clearItems();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class UbThread implements Runnable {
        UbThread() {
        }

        private void countDown() throws InterruptedException {
            for (int loop = 0; loop < (L1UltimateBattle.BEFORE_MINUTE * 60) - 10; loop++) {
                Thread.sleep(1000);
            }
            L1UltimateBattle.this.removeRetiredMembers();
            L1UltimateBattle.this.sendMessage(637, "10");
            Thread.sleep(5000);
            L1UltimateBattle.this.sendMessage(637, "5");
            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(637, "4");
            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(637, "3");
            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(637, "2");
            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(637, "1");
            Thread.sleep(1000);
            L1UltimateBattle.this.sendMessage(632, "無限大戰 開始");
            L1UltimateBattle.this.removeRetiredMembers();
        }

        private void waitForNextRound(int curRound) throws InterruptedException {
            int wait = new int[]{6, 6, 2, 18}[curRound - 1];
            for (int i = 0; i < wait; i++) {
                Thread.sleep(10000);
            }
            L1UltimateBattle.this.removeRetiredMembers();
        }

        public void run() {
            try {
                L1UltimateBattle.this.setActive(true);
                countDown();
                L1UltimateBattle.this.setNowUb(true);
                for (int round = 1; round <= 4; round++) {
                    L1UltimateBattle.this.sendRoundMessage(round);
                    Iterator<L1UbSpawn> it = UBSpawnTable.getInstance().getPattern(L1UltimateBattle.this._ubId, L1UltimateBattle.this._pattern).getSpawnList(round).iterator();
                    while (it.hasNext()) {
                        L1UbSpawn spawn = it.next();
                        if (L1UltimateBattle.this.getMembersCount() > 0) {
                            spawn.spawnAll();
                        }
                        Thread.sleep((long) (spawn.getSpawnDelay() * L1SkillId.STATUS_BRAVE));
                    }
                    if (L1UltimateBattle.this.getMembersCount() > 0) {
                        L1UltimateBattle.this.spawnSupplies(round);
                    }
                    for (L1PcInstance pc : L1UltimateBattle.this.getMembersArray()) {
                        UBTable.getInstance().writeUbScore(L1UltimateBattle.this.getUbId(), pc);
                    }
                    waitForNextRound(round);
                }
                L1PcInstance[] membersArray = L1UltimateBattle.this.getMembersArray();
                int length = membersArray.length;
                for (int i = 0; i < length; i++) {
                    L1PcInstance pc2 = membersArray[i];
                    Random random = new Random();
                    L1Teleport.teleport(pc2, 33503 + random.nextInt(4), random.nextInt(4) + 32764, (short) 4, 5, true);
                    L1UltimateBattle.this.removeMember(pc2);
                }
                L1UltimateBattle.this.clearColosseum();
                L1UltimateBattle.this.setActive(false);
                L1UltimateBattle.this.setNowUb(false);
            } catch (Exception e) {
                L1UltimateBattle._log.error(e.getLocalizedMessage(), e);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void start() {
        this._pattern = new Random().nextInt(UBSpawnTable.getInstance().getMaxPattern(this._ubId)) + 1;
        GeneralThreadPool.get().execute(new UbThread());
    }

    public void addMember(L1PcInstance pc) {
        if (!this._members.contains(pc)) {
            this._members.add(pc);
        }
    }

    public void removeMember(L1PcInstance pc) {
        this._members.remove(pc);
    }

    public void clearMembers() {
        this._members.clear();
    }

    public boolean isMember(L1PcInstance pc) {
        return this._members.contains(pc);
    }

    public L1PcInstance[] getMembersArray() {
        return (L1PcInstance[]) this._members.toArray(new L1PcInstance[this._members.size()]);
    }

    public int getMembersCount() {
        return this._members.size();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setNowUb(boolean i) {
        this._isNowUb = i;
    }

    public boolean isNowUb() {
        return this._isNowUb;
    }

    public int getUbId() {
        return this._ubId;
    }

    public void setUbId(int id) {
        this._ubId = id;
    }

    public short getMapId() {
        return this._mapId;
    }

    public void setMapId(short mapId) {
        this._mapId = mapId;
    }

    public int getMinLevel() {
        return this._minLevel;
    }

    public void setMinLevel(int level) {
        this._minLevel = level;
    }

    public int getMaxLevel() {
        return this._maxLevel;
    }

    public void setMaxLevel(int level) {
        this._maxLevel = level;
    }

    public int getMaxPlayer() {
        return this._maxPlayer;
    }

    public void setMaxPlayer(int count) {
        this._maxPlayer = count;
    }

    public void setEnterRoyal(boolean enterRoyal) {
        this._enterRoyal = enterRoyal;
    }

    public void setEnterKnight(boolean enterKnight) {
        this._enterKnight = enterKnight;
    }

    public void setEnterMage(boolean enterMage) {
        this._enterMage = enterMage;
    }

    public void setEnterElf(boolean enterElf) {
        this._enterElf = enterElf;
    }

    public void setEnterDarkelf(boolean enterDarkelf) {
        this._enterDarkelf = enterDarkelf;
    }

    public void setEnterDragonKnight(boolean enterDragonKnight) {
        this._enterDragonKnight = enterDragonKnight;
    }

    public void setEnterIllusionist(boolean enterIllusionist) {
        this._enterIllusionist = enterIllusionist;
    }

    public void setEnterMale(boolean enterMale) {
        this._enterMale = enterMale;
    }

    public void setEnterFemale(boolean enterFemale) {
        this._enterFemale = enterFemale;
    }

    public boolean canUsePot() {
        return this._usePot;
    }

    public void setUsePot(boolean usePot) {
        this._usePot = usePot;
    }

    public int getHpr() {
        return this._hpr;
    }

    public void setHpr(int hpr) {
        this._hpr = hpr;
    }

    public int getMpr() {
        return this._mpr;
    }

    public void setMpr(int mpr) {
        this._mpr = mpr;
    }

    public int getLocX1() {
        return this._locX1;
    }

    public void setLocX1(int locX1) {
        this._locX1 = locX1;
    }

    public int getLocY1() {
        return this._locY1;
    }

    public void setLocY1(int locY1) {
        this._locY1 = locY1;
    }

    public int getLocX2() {
        return this._locX2;
    }

    public void setLocX2(int locX2) {
        this._locX2 = locX2;
    }

    public int getLocY2() {
        return this._locY2;
    }

    public void setLocY2(int locY2) {
        this._locY2 = locY2;
    }

    public void setName(String name) {
        this._ubName = name;
    }

    public String getName() {
        return this._ubName;
    }

    public void resetLoc() {
        this._locX = (this._locX2 + this._locX1) / 2;
        this._locY = (this._locY2 + this._locY1) / 2;
        this._location = new L1Location(this._locX, this._locY, this._mapId);
    }

    public L1Location getLocation() {
        return this._location;
    }

    public void addManager(int npcId) {
        this._managers.add(Integer.valueOf(npcId));
    }

    public boolean containsManager(int npcId) {
        return this._managers.contains(Integer.valueOf(npcId));
    }

    public void addUbTime(int time) {
        this._ubTimes.add(Integer.valueOf(time));
    }

    public String getNextUbTime() {
        return intToTimeFormat(nextUbTime());
    }

    private int nextUbTime() {
        SortedSet<Integer> tailSet = this._ubTimes.tailSet(Integer.valueOf(Integer.valueOf(new SimpleDateFormat("HHmm").format(getRealTime().getTime())).intValue()));
        if (tailSet.isEmpty()) {
            tailSet = this._ubTimes;
        }
        return tailSet.first().intValue();
    }

    private static String intToTimeFormat(int n) {
        return String.valueOf(n / 100) + ":" + ((n % 100) / 10) + (n % 10);
    }

    private static Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }

    public boolean checkUbTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        Calendar realTime = getRealTime();
        realTime.add(12, BEFORE_MINUTE);
        return this._ubTimes.contains(Integer.valueOf(Integer.valueOf(sdf.format(realTime.getTime())).intValue()));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setActive(boolean f) {
        this._active = f;
    }

    public boolean isActive() {
        return this._active;
    }

    public boolean canPcEnter(L1PcInstance pc) {
        if (!RangeInt.includes(pc.getLevel(), this._minLevel, this._maxLevel)) {
            return false;
        }
        if ((!pc.isCrown() || !this._enterRoyal) && ((!pc.isKnight() || !this._enterKnight) && ((!pc.isWizard() || !this._enterMage) && ((!pc.isElf() || !this._enterElf) && ((!pc.isDarkelf() || !this._enterDarkelf) && ((!pc.isDragonKnight() || !this._enterDragonKnight) && (!pc.isIllusionist() || !this._enterIllusionist))))))) {
            return false;
        }
        return true;
    }

    public String[] makeUbInfoStrings() {
        if (this._ubInfo != null) {
            return this._ubInfo;
        }
        String nextUbTime = getNextUbTime();
        StringBuilder classesBuff = new StringBuilder();
        if (this._enterDarkelf) {
            classesBuff.append("黑暗妖精 ");
        }
        if (this._enterMage) {
            classesBuff.append("法師 ");
        }
        if (this._enterElf) {
            classesBuff.append("妖精 ");
        }
        if (this._enterKnight) {
            classesBuff.append("騎士 ");
        }
        if (this._enterRoyal) {
            classesBuff.append("王族 ");
        }
        if (this._enterDragonKnight) {
            classesBuff.append("龍騎士 ");
        }
        if (this._enterIllusionist) {
            classesBuff.append("幻術師 ");
        }
        String classes = classesBuff.toString().trim();
        StringBuilder sexBuff = new StringBuilder();
        if (this._enterMale) {
            sexBuff.append("男 ");
        }
        if (this._enterFemale) {
            sexBuff.append("女 ");
        }
        this._ubInfo = new String[]{nextUbTime, classes, sexBuff.toString().trim(), String.valueOf(this._minLevel), String.valueOf(this._maxLevel), this._location.getMap().isEscapable() ? "可能" : "不可能", this._location.getMap().isUseResurrection() ? "可能" : "不可能", "可能", String.valueOf(this._hpr), String.valueOf(this._mpr), this._location.getMap().isTakePets() ? "可能" : "不可能", this._location.getMap().isRecallPets() ? "可能" : "不可能"};
        return this._ubInfo;
    }
}
