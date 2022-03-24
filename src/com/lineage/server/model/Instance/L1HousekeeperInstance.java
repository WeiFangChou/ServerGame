package com.lineage.server.model.Instance;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.datatables.lock.HouseReading;
import com.lineage.server.model.L1AttackMode;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1House;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.world.WorldClan;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1HousekeeperInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1HousekeeperInstance.class);
    private static final long serialVersionUID = 1;

    public L1HousekeeperInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            L1AttackMode attack = new L1AttackPc(pc, this);
            attack.calcHit();
            attack.action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance pc) {
        int houseId;
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        int npcid = getNpcTemplate().get_npcId();
        String htmlid = null;
        String[] htmldata = null;
        boolean isOwner = false;
        if (talking != null) {
            L1Clan clan = WorldClan.get().getClan(pc.getClanname());
            if (!(clan == null || (houseId = clan.getHouseId()) == 0 || npcid != HouseReading.get().getHouseTable(houseId).getKeeperId())) {
                isOwner = true;
            }
            if (!isOwner) {
                L1House targetHouse = null;
                Iterator<L1House> it = HouseReading.get().getHouseTableList().values().iterator();
                while (true) {
                    if (it.hasNext()) {
                        L1House house = it.next();
                        if (npcid == house.getKeeperId()) {
                            targetHouse = house;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                boolean isOccupy = false;
                String clanName = null;
                String leaderName = null;
                Iterator<L1Clan> iter = WorldClan.get().getAllClans().iterator();
                while (true) {
                    if (iter.hasNext()) {
                        L1Clan targetClan = iter.next();
                        if (targetHouse.getHouseId() == targetClan.getHouseId()) {
                            isOccupy = true;
                            clanName = targetClan.getClanName();
                            leaderName = targetClan.getLeaderName();
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (isOccupy) {
                    htmlid = "agname";
                    htmldata = new String[]{clanName, leaderName, targetHouse.getHouseName()};
                } else {
                    htmlid = "agnoname";
                    htmldata = new String[]{targetHouse.getHouseName()};
                }
            }
            if (htmlid != null) {
                if (htmldata != null) {
                    pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
                } else {
                    pc.sendPackets(new S_NPCTalkReturn(objid, htmlid));
                }
            } else if (pc.getLawful() < -1000) {
                pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance pc, String action) {
    }

    public void doFinalAction(L1PcInstance pc) {
    }
}
