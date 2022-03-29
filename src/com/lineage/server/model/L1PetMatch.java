package com.lineage.server.model;

import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.world.World;
import java.util.Timer;
import java.util.TimerTask;

public class L1PetMatch {
    public static final int MAX_PET_MATCH = 1;
    private static final short[] PET_MATCH_MAPID = {5125, 5131, 5132, 5133, 5134};
    public static final int STATUS_NONE = 0;
    public static final int STATUS_PLAYING = 3;
    public static final int STATUS_READY1 = 1;
    public static final int STATUS_READY2 = 2;
    private static L1PetMatch _instance;
    private String[] _pc1Name = new String[1];
    private String[] _pc2Name = new String[1];
    private L1PetInstance[] _pet1 = new L1PetInstance[1];
    private L1PetInstance[] _pet2 = new L1PetInstance[1];

    public static L1PetMatch getInstance() {
        if (_instance == null) {
            _instance = new L1PetMatch();
        }
        return _instance;
    }

    public int setPetMatchPc(int petMatchNo, L1PcInstance pc, L1PetInstance pet) {
        int status = getPetMatchStatus(petMatchNo);
        if (status == 0) {
            this._pc1Name[petMatchNo] = pc.getName();
            this._pet1[petMatchNo] = pet;
            return 1;
        } else if (status == 1) {
            this._pc2Name[petMatchNo] = pc.getName();
            this._pet2[petMatchNo] = pet;
            return 3;
        } else if (status != 2) {
            return 0;
        } else {
            this._pc1Name[petMatchNo] = pc.getName();
            this._pet1[petMatchNo] = pet;
            return 3;
        }
    }

    private synchronized int getPetMatchStatus(int petMatchNo) {
        int i = 0;
        synchronized (this) {
            L1PcInstance pc1 = null;
            if (this._pc1Name[petMatchNo] != null) {
                pc1 = World.get().getPlayer(this._pc1Name[petMatchNo]);
            }
            L1PcInstance pc2 = null;
            if (this._pc2Name[petMatchNo] != null) {
                pc2 = World.get().getPlayer(this._pc2Name[petMatchNo]);
            }
            if (!(pc1 == null && pc2 == null)) {
                if (pc1 != null || pc2 == null) {
                    if (pc1 == null || pc2 != null) {
                        if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo] && pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
                            i = 3;
                        } else if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
                            this._pc2Name[petMatchNo] = null;
                            this._pet2[petMatchNo] = null;
                            i = 1;
                        } else if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
                            this._pc1Name[petMatchNo] = null;
                            this._pet1[petMatchNo] = null;
                            i = 2;
                        }
                    } else if (pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
                        i = 1;
                    } else {
                        this._pc1Name[petMatchNo] = null;
                        this._pet1[petMatchNo] = null;
                    }
                } else if (pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
                    i = 2;
                } else {
                    this._pc2Name[petMatchNo] = null;
                    this._pet2[petMatchNo] = null;
                }
            }
        }
        return i;
    }

    private int decidePetMatchNo() {
        for (int i = 0; i < 1; i++) {
            int status = getPetMatchStatus(i);
            if (status == 1 || status == 2) {
                return i;
            }
        }
        for (int i2 = 0; i2 < 1; i2++) {
            if (getPetMatchStatus(i2) == 0) {
                return i2;
            }
        }
        return -1;
    }

    public synchronized boolean enterPetMatch(L1PcInstance pc, int amuletId) {
        boolean z = false;
        synchronized (this) {
            int petMatchNo = decidePetMatchNo();
            if (petMatchNo != -1) {
                L1PetInstance pet = withdrawPet(pc, amuletId);
                L1Teleport.teleport(pc, 32799, 32868, PET_MATCH_MAPID[petMatchNo], 0, true);
                new L1PetMatchReadyTimer(petMatchNo, pc, pet).begin();
                z = true;
            }
        }
        return z;
    }

    private L1PetInstance withdrawPet(L1PcInstance pc, int amuletId) {
        L1Pet l1pet = PetReading.get().getTemplate(amuletId);
        if (l1pet == null) {
            return null;
        }
        L1PetInstance pet = new L1PetInstance(NpcTable.get().getTemplate(l1pet.get_npcid()), pc, l1pet);
        pet.setPetcost(6);
        return pet;
    }

    public void startPetMatch(int petMatchNo) {
        this._pet1[petMatchNo].setCurrentPetStatus(1);
        this._pet1[petMatchNo].setTarget(this._pet2[petMatchNo]);
        this._pet2[petMatchNo].setCurrentPetStatus(1);
        this._pet2[petMatchNo].setTarget(this._pet1[petMatchNo]);
        new L1PetMatchTimer(this._pet1[petMatchNo], this._pet2[petMatchNo], petMatchNo).begin();
    }

    public void endPetMatch(int petMatchNo, int winNo) throws Exception {
        L1PcInstance pc1 = World.get().getPlayer(this._pc1Name[petMatchNo]);
        L1PcInstance pc2 = World.get().getPlayer(this._pc2Name[petMatchNo]);
        if (winNo == 1) {
            giveMedal(pc1, petMatchNo, true);
            giveMedal(pc2, petMatchNo, false);
        } else if (winNo == 2) {
            giveMedal(pc1, petMatchNo, false);
            giveMedal(pc2, petMatchNo, true);
        } else if (winNo == 3) {
            giveMedal(pc1, petMatchNo, false);
            giveMedal(pc2, petMatchNo, false);
        }
        qiutPetMatch(petMatchNo);
    }

    private void giveMedal(L1PcInstance pc, int petMatchNo, boolean isWin) {
        if (pc == null || pc.getMapId() != PET_MATCH_MAPID[petMatchNo]) {
            return;
        }
        if (isWin) {
            pc.sendPackets(new S_ServerMessage(1166, pc.getName()));
            L1ItemInstance item = ItemTable.get().createItem(41309);
            if (item != null && pc.getInventory().checkAddItem(item, 3) == 0) {
                item.setCount(3);
                pc.getInventory().storeItem(item);
                pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
                return;
            }
            return;
        }
        L1ItemInstance item2 = ItemTable.get().createItem(41309);
        if (item2 != null && pc.getInventory().checkAddItem(item2, 1) == 0) {
            item2.setCount(1);
            pc.getInventory().storeItem(item2);
            pc.sendPackets(new S_ServerMessage(403, item2.getLogName()));
        }
    }

    private void qiutPetMatch(int petMatchNo) throws Exception {
        L1PcInstance pc1 = World.get().getPlayer(this._pc1Name[petMatchNo]);
        if (pc1 != null && pc1.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
            Object[] array = pc1.getPetList().values().toArray();
            for (Object object : array) {
                if (object instanceof L1PetInstance) {
                    L1PetInstance pet = (L1PetInstance) object;
                    pet.dropItem(pet);
                    pc1.getPetList().remove(Integer.valueOf(pet.getId()));
                    pet.deleteMe();
                }
            }
            L1Teleport.teleport(pc1, 32630, 32744,  4, 4, true);
        }
        this._pc1Name[petMatchNo] = null;
        this._pet1[petMatchNo] = null;
        L1PcInstance pc2 = World.get().getPlayer(this._pc2Name[petMatchNo]);
        if (pc2 != null && pc2.getMapId() == PET_MATCH_MAPID[petMatchNo]) {
            Object[] array2 = pc2.getPetList().values().toArray();
            for (Object object2 : array2) {
                if (object2 instanceof L1PetInstance) {
                    L1PetInstance pet2 = (L1PetInstance) object2;
                    pet2.dropItem(pet2);
                    pc2.getPetList().remove(Integer.valueOf(pet2.getId()));
                    pet2.deleteMe();
                }
            }
            L1Teleport.teleport(pc2, 32630, 32744,  4, 4, true);
        }
        this._pc2Name[petMatchNo] = null;
        this._pet2[petMatchNo] = null;
    }

    public class L1PetMatchReadyTimer extends TimerTask {
        private final L1PcInstance _pc;
        private final L1PetInstance _pet;
        private final int _petMatchNo;

        public L1PetMatchReadyTimer(int petMatchNo, L1PcInstance pc, L1PetInstance pet) {
            this._petMatchNo = petMatchNo;
            this._pc = pc;
            this._pet = pet;
        }

        public void begin() {
            new Timer().schedule(this, 3000);
        }

        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    if (this._pc == null || this._pet == null) {
                        cancel();
                        return;
                    }
                } catch (Throwable th) {
                    return;
                }
            } while (this._pc.isTeleport());
            if (L1PetMatch.getInstance().setPetMatchPc(this._petMatchNo, this._pc, this._pet) == 3) {
                L1PetMatch.getInstance().startPetMatch(this._petMatchNo);
            }
            cancel();
        }
    }

    public class L1PetMatchTimer extends TimerTask {
        private int _counter = 0;
        private final L1PetInstance _pet1;
        private final L1PetInstance _pet2;
        private final int _petMatchNo;

        public L1PetMatchTimer(L1PetInstance pet1, L1PetInstance pet2, int petMatchNo) {
            this._pet1 = pet1;
            this._pet2 = pet2;
            this._petMatchNo = petMatchNo;
        }

        public void begin() {
            new Timer().schedule(this, 0);
        }

        public void run() {
            int winner;
            do {
                try {
                    Thread.sleep(3000);
                    this._counter++;
                    if (this._pet1 == null || this._pet2 == null) {
                        cancel();
                        return;
                    } else if (this._pet1.isDead() || this._pet2.isDead()) {
                        if (!this._pet1.isDead() && this._pet2.isDead()) {
                            winner = 1;
                        } else if (!this._pet1.isDead() || this._pet2.isDead()) {
                            winner = 3;
                        } else {
                            winner = 2;
                        }
                        L1PetMatch.getInstance().endPetMatch(this._petMatchNo, winner);
                        cancel();
                        return;
                    }
                } catch (Throwable th) {
                    return;
                }
            } while (this._counter != 100);
            try {
                L1PetMatch.getInstance().endPetMatch(this._petMatchNo, 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cancel();
        }
    }
}
