package com.lineage.server.model;

import com.lineage.server.datatables.lock.CastleReading;
import com.lineage.server.serverpackets.S_War;
import com.lineage.server.templates.L1Castle;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1War {
    private static final Log _log = LogFactory.getLog(L1War.class);
    private String _attackClanName = null;
    private final ConcurrentHashMap<String, L1Clan> _attackList = new ConcurrentHashMap<>();
    private int _castleId = 0;
    private L1Clan _defenceClan = null;
    private String _defenceClanName = null;
    private boolean _isWarTimerDelete = false;
    private int _warType = 0;

    public int get_castleId() {
        return this._castleId;
    }

    public String get_defenceClanName() {
        return this._defenceClanName;
    }

    public String get_attackClanName() {
        return this._attackClanName;
    }

    class SimWarTimer implements Runnable {
        public SimWarTimer() {
        }

        public void run() {
            for (int loop = 0; loop < 240; loop++) {
                try {
                    Thread.sleep(60000);
                    if (L1War.this._isWarTimerDelete) {
                        return;
                    }
                } catch (Exception ignored) {
                }
            }
            L1War.this.ceaseWar(L1War.this._attackClanName, L1War.this._defenceClanName);
            L1War.this.delete();
        }
    }

    public void handleCommands(int war_type, String attack_clan_name, String defence_clan_name) {
        try {
            this._warType = war_type;
            this._defenceClanName = defence_clan_name;
            this._defenceClan = WorldClan.get().getClan(this._defenceClanName);
            declareWar(attack_clan_name, defence_clan_name);
            this._attackList.clear();
            addAttackClan(attack_clan_name);
            switch (war_type) {
                case 1:
                    this._castleId = getCastleId();
                    break;
                case 2:
                    GeneralThreadPool.get().execute(new SimWarTimer());
                    break;
            }
            WorldWar.get().addWar(this);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void requestCastleWar(int type, String attack_clan_name) {
        if (attack_clan_name != null) {
            try {
                if (WorldClan.get().getClan(attack_clan_name) != null) {
                    World.get().broadcastPacketToAll(new S_War(type, attack_clan_name, this._defenceClanName));
                    if (this._defenceClan != null) {
                        switch (type) {
                            case 2:
                                World.get().broadcastPacketToAll(new S_War(4, this._defenceClanName, attack_clan_name));
                                removeAttackClan(attack_clan_name);
                                break;
                        }
                    }
                }
                if (this._attackList.size() <= 0) {
                    this._isWarTimerDelete = true;
                    delete();
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                if (this._attackList.size() <= 0) {
                    this._isWarTimerDelete = true;
                    delete();
                }
            } catch (Throwable th) {
                if (this._attackList.size() <= 0) {
                    this._isWarTimerDelete = true;
                    delete();
                }
                throw th;
            }
        }
    }

    private void requestSimWar(int type, String clan1_name, String clan2_name) {
        if (clan1_name == null || clan2_name == null) {
            switch (type) {
                case 2:
                case 3:
                    this._isWarTimerDelete = true;
                    delete();
                    return;
                default:
                    return;
            }
        } else {
            try {
                L1Clan clan1 = WorldClan.get().getClan(clan1_name);
                if (clan1 == null) {
                    switch (type) {
                        case 2:
                        case 3:
                            this._isWarTimerDelete = true;
                            delete();
                            return;
                        default:
                            return;
                    }
                } else {
                    L1Clan clan2 = WorldClan.get().getClan(clan2_name);
                    if (clan2 == null) {
                        switch (type) {
                            case 2:
                            case 3:
                                this._isWarTimerDelete = true;
                                delete();
                                return;
                            default:
                                return;
                        }
                    } else {
                        switch (type) {
                            case 1:
                                clan1.sendPacketsAll(new S_War(1, clan1_name, clan2_name));
                                clan2.sendPacketsAll(new S_War(1, clan1_name, clan2_name));
                                break;
                            case 2:
                                clan1.sendPacketsAll(new S_War(2, clan1_name, clan2_name));
                                clan2.sendPacketsAll(new S_War(4, clan2_name, clan1_name));
                                clan1.sendPacketsAll(new S_War(3, clan1_name, clan2_name));
                                clan2.sendPacketsAll(new S_War(3, clan1_name, clan2_name));
                                break;
                            case 3:
                                clan1.sendPacketsAll(new S_War(3, clan1_name, clan2_name));
                                clan2.sendPacketsAll(new S_War(3, clan1_name, clan2_name));
                                break;
                        }
                        switch (type) {
                            case 2:
                            case 3:
                                this._isWarTimerDelete = true;
                                delete();
                                return;
                            default:
                                return;
                        }
                    }
                }
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
                switch (type) {
                    case 2:
                    case 3:
                        this._isWarTimerDelete = true;
                        delete();
                        return;
                    default:
                        return;
                }
            } catch (Throwable th) {
                switch (type) {
                    case 2:
                    case 3:
                        this._isWarTimerDelete = true;
                        delete();
                        break;
                }
                throw th;
            }
        }
    }

    public void winCastleWar(String clan_name) {
        try {
            World.get().broadcastPacketToAll(new S_War(4, clan_name, this._defenceClanName));
            Set<String> clanList = getAttackClanList();
            if (!clanList.isEmpty()) {
                for (String enemy_clan_name : clanList) {
                    World.get().broadcastPacketToAll(new S_War(3, this._defenceClanName, enemy_clan_name));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            this._isWarTimerDelete = true;
            delete();
        }
    }

    public void ceaseCastleWar() {
        try {
            Set<String> clanList = getAttackClanList();
            if (!clanList.isEmpty()) {
                for (String enemy_clan_name : clanList) {
                    World.get().broadcastPacketToAll(new S_War(4, this._defenceClanName, enemy_clan_name));
                }
                for (String enemy_clan_name2 : clanList) {
                    World.get().broadcastPacketToAll(new S_War(3, this._defenceClanName, enemy_clan_name2));
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        } finally {
            this._isWarTimerDelete = true;
            delete();
        }
    }

    public void declareWar(String attack_clan_name, String defence_clan_name) {
        try {
            if (getWarType() == 1) {
                requestCastleWar(1, attack_clan_name);
                return;
            }
            this._attackClanName = attack_clan_name;
            requestSimWar(1, attack_clan_name, defence_clan_name);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void surrenderWar(String clan1_name, String clan2_name) {
        try {
            if (getWarType() == 1) {
                requestCastleWar(2, clan1_name);
            } else {
                requestSimWar(2, clan1_name, clan2_name);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void ceaseWar(String clan1_name, String clan2_name) {
        try {
            if (getWarType() == 1) {
                requestCastleWar(3, clan1_name);
            } else {
                requestSimWar(3, clan1_name, clan2_name);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void ceaseWar() {
        try {
            ceaseWar(this._attackClanName, this._defenceClanName);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void winWar(String clan1_name, String clan2_name) {
        try {
            if (getWarType() == 1) {
                requestCastleWar(4, clan1_name);
            } else {
                requestSimWar(4, clan1_name, clan2_name);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean checkClanInWar(String clan_name) {
        if (this._defenceClanName.toLowerCase().equals(clan_name.toLowerCase())) {
            return true;
        }
        return checkAttackClan(clan_name);
    }

    public boolean checkClanInSameWar(String player_clan_name, String target_clan_name) {
        boolean player_clan_flag;
        boolean target_clan_flag;
        if (this._defenceClanName.toLowerCase().equals(player_clan_name.toLowerCase())) {
            player_clan_flag = true;
        } else {
            player_clan_flag = checkAttackClan(player_clan_name);
        }
        if (this._defenceClanName.toLowerCase().equals(target_clan_name.toLowerCase())) {
            target_clan_flag = true;
        } else {
            target_clan_flag = checkAttackClan(target_clan_name);
        }
        if (!player_clan_flag || !target_clan_flag) {
            return false;
        }
        return true;
    }

    public String getEnemyClanName(String player_clan_name) {
        if (!this._defenceClanName.toLowerCase().equals(player_clan_name.toLowerCase())) {
            return this._defenceClanName;
        }
        Set<String> clanList = getAttackClanList();
        if (!clanList.isEmpty()) {
            Iterator<String> iter = clanList.iterator();
            if (iter.hasNext()) {
                return iter.next();
            }
        }
        return null;
    }

    public void delete() {
        try {
            this._attackList.clear();
            WorldWar.get().removeWar(this);
            _log.info(String.valueOf(this._defenceClanName) + " 戰爭終止完成 剩餘戰爭清單數量:" + WorldWar.get().getWarList().size());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public int getWarType() {
        return this._warType;
    }

    public void addAttackClan(String attack_clan_name) {
        L1Clan attack_clan = WorldClan.get().getClan(attack_clan_name);
        if (attack_clan != null) {
            this._attackList.put(attack_clan_name, attack_clan);
        }
    }

    public void removeAttackClan(String attack_clan_name) {
        if (this._attackList.get(attack_clan_name) != null) {
            this._attackList.remove(attack_clan_name);
        }
    }

    public boolean checkAttackClan(String attack_clan_name) {
        if (this._attackList.get(attack_clan_name) != null) {
            return true;
        }
        return false;
    }

    public Set<String> getAttackClanList() {
        return this._attackList.keySet();
    }

    public int getCastleId() {
        switch (this._warType) {
            case 1:
                L1Clan clan = WorldClan.get().getClan(this._defenceClanName);
                if (clan != null) {
                    return clan.getCastleId();
                }
                break;
        }
        return 0;
    }

    public L1Castle getCastle() {
        switch (this._warType) {
            case 1:
                L1Clan clan = WorldClan.get().getClan(this._defenceClanName);
                if (clan != null) {
                    return CastleReading.get().getCastleTable(clan.getCastleId());
                }
                break;
        }
        return null;
    }
}
