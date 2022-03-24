package com.lineage.server.model.Instance;

import com.lineage.server.datatables.DoorSpawnTable;
import com.lineage.server.datatables.lock.ClanReading;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1War;
import com.lineage.server.model.L1WarSpawn;
import com.lineage.server.serverpackets.S_CastleMaster;
import com.lineage.server.serverpackets.S_PacketBoxWar;
import com.lineage.server.serverpackets.S_RemoveObject;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1CrownInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1CrownInstance.class);
    private static final long serialVersionUID = 1;

    public L1CrownInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance player) {
        String playerClanName;
        L1Clan clan;
        boolean in_war = false;
        try {
            if (player.getClanid() == 0 || (clan = WorldClan.get().getClan((playerClanName = player.getClanname()))) == null || player.isDead() || player.getCurrentHp() <= 0 || player.isGhost() || player.isTeleport() || !player.isCrown()) {
                return;
            }
            if (!((player.getTempCharGfx() == 0 || player.getTempCharGfx() == 1) && player.getId() == clan.getLeaderId() && checkRange(player))) {
                return;
            }
            if (clan.getCastleId() != 0) {
                player.sendPackets(new S_ServerMessage(474));
                return;
            }
            int castle_id = L1CastleLocation.getCastleId(getX(), getY(), getMapId());
            boolean existDefenseClan = false;
            L1Clan defence_clan = L1CastleLocation.mapCastle().get(new Integer(castle_id));
            if (defence_clan != null) {
                existDefenseClan = true;
            }
            List<L1War> wars = WorldWar.get().getWarList();
            Iterator<L1War> it = wars.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                L1War war = it.next();
                if (castle_id == war.getCastleId()) {
                    in_war = war.checkClanInWar(playerClanName);
                    break;
                }
            }
            if (!existDefenseClan || in_war) {
                Iterator<L1War> it2 = wars.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    L1War war2 = it2.next();
                    if (war2.checkClanInWar(playerClanName) && existDefenseClan) {
                        war2.winCastleWar(playerClanName);
                        break;
                    }
                }
                if (existDefenseClan && defence_clan != null) {
                    defence_clan.setCastleId(0);
                    ClanReading.get().updateClan(defence_clan);
                    if (L1CastleLocation.mapCastle().get(new Integer(castle_id)) != null) {
                        L1CastleLocation.removeCastle(new Integer(castle_id));
                    }
                    World.get().broadcastPacketToAll(new S_CastleMaster(0, defence_clan.getLeaderId()));
                }
                clan.setCastleId(castle_id);
                ClanReading.get().updateClan(clan);
                if (L1CastleLocation.mapCastle().get(new Integer(castle_id)) == null) {
                    L1CastleLocation.putCastle(Integer.valueOf(castle_id), clan);
                }
                if (castle_id == 2) {
                    World.get().broadcastPacketToAll(new S_CastleMaster(8, player.getId()));
                } else {
                    World.get().broadcastPacketToAll(new S_CastleMaster(castle_id, player.getId()));
                }
                int[] iArr = new int[3];
                for (L1PcInstance pc : World.get().getAllPlayers()) {
                    if (pc.getClanid() != player.getClanid() && !pc.isGm() && L1CastleLocation.checkInWarArea(castle_id, pc)) {
                        int[] loc = L1CastleLocation.getGetBackLoc(castle_id);
                        L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
                    }
                }
                L1PcInstance[] clanMember = clan.getOnlineClanMember();
                if (clanMember.length > 0) {
                    for (L1PcInstance pc2 : clanMember) {
                        if (pc2.getId() == clan.getLeaderId()) {
                            pc2.sendPackets(new S_PacketBoxWar(3));
                        }
                        pc2.sendPackets(new S_PacketBoxWar(4));
                    }
                }
                deleteMe();
                for (L1Object l1object : World.get().getObject()) {
                    if (l1object instanceof L1TowerInstance) {
                        L1TowerInstance tower = (L1TowerInstance) l1object;
                        if (L1CastleLocation.checkInWarArea(castle_id, tower)) {
                            tower.deleteMe();
                        }
                    }
                }
                new L1WarSpawn().spawnTower(castle_id);
                L1DoorInstance[] doorList = DoorSpawnTable.get().getDoorList();
                for (L1DoorInstance door : doorList) {
                    if (L1CastleLocation.checkInWarArea(castle_id, door)) {
                        door.repairGate();
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void deleteMe() {
        this._destroyed = true;
        if (getInventory() != null) {
            getInventory().clearItems();
        }
        allTargetClear();
        this._master = null;
        World.get().removeVisibleObject(this);
        World.get().removeObject(this);
        Iterator<L1PcInstance> it = World.get().getRecognizePlayer(this).iterator();
        while (it.hasNext()) {
            L1PcInstance pc = it.next();
            pc.removeKnownObject(this);
            pc.sendPackets(new S_RemoveObject(this));
        }
        removeAllKnownObjects();
    }

    private boolean checkRange(L1PcInstance pc) {
        return getX() + -1 <= pc.getX() && pc.getX() <= getX() + 1 && getY() + -1 <= pc.getY() && pc.getY() <= getY() + 1;
    }
}
