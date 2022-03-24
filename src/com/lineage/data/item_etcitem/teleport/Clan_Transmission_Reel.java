package com.lineage.data.item_etcitem.teleport;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.GetbackTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1HouseLocation;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.L1TownLocation;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.WorldClan;

public class Clan_Transmission_Reel extends ItemExecutor {
    private Clan_Transmission_Reel() {
    }

    public static ItemExecutor get() {
        return new Clan_Transmission_Reel();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        L1Clan clan;
        if (pc.getMap().isEscapable() || pc.isGm()) {
            int castle_id = 0;
            int house_id = 0;
            if (!(pc.getClanid() == 0 || (clan = WorldClan.get().getClan(pc.getClanname())) == null)) {
                castle_id = clan.getCastleId();
                house_id = clan.getHouseId();
            }
            if (castle_id != 0) {
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int[] iArr = new int[3];
                    int[] loc = L1CastleLocation.getCastleLoc(castle_id);
                    L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
                    pc.getInventory().removeItem(item, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(647));
                    pc.sendPackets(new S_Paralysis(7, false));
                }
            } else if (house_id != 0) {
                if (pc.getMap().isEscapable() || pc.isGm()) {
                    int[] iArr2 = new int[3];
                    int[] loc2 = L1HouseLocation.getHouseLoc(house_id);
                    L1Teleport.teleport(pc, loc2[0], loc2[1], (short) loc2[2], 5, true);
                    pc.getInventory().removeItem(item, 1);
                } else {
                    pc.sendPackets(new S_ServerMessage(647));
                    pc.sendPackets(new S_Paralysis(7, false));
                }
            } else if (pc.getHomeTownId() > 0) {
                int[] loc3 = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
                L1Teleport.teleport(pc, loc3[0], loc3[1], (short) loc3[2], 5, true);
                pc.getInventory().removeItem(item, 1);
            } else {
                int[] loc4 = GetbackTable.GetBack_Location(pc, true);
                L1Teleport.teleport(pc, loc4[0], loc4[1], (short) loc4[2], 5, true);
                pc.getInventory().removeItem(item, 1);
            }
        } else {
            pc.sendPackets(new S_ServerMessage(647));
            pc.sendPackets(new S_Paralysis(7, false));
        }
        L1BuffUtil.cancelAbsoluteBarrier(pc);
    }
}
