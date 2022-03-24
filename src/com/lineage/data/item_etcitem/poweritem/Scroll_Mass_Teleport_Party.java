package com.lineage.data.item_etcitem.poweritem;

import com.lineage.config.ConfigOther;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Party;
import com.lineage.server.model.L1PcInventory;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Scroll_Mass_Teleport_Party extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Scroll_Mass_Teleport_Party.class);
    private int _remove;

    private Scroll_Mass_Teleport_Party() {
    }

    public static ItemExecutor get() {
        return new Scroll_Mass_Teleport_Party();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            String item_id = item.getName();
            String mapName = MapsTable.get().getMapName(pc.getMapId(), pc.getX(), pc.getY());
            boolean isNowWar = false;
            int castleId = L1CastleLocation.getCastleIdByArea(pc);
            if (castleId != 0) {
                isNowWar = ServerWarExecutor.get().isNowWar(castleId);
            }
            if (isNowWar) {
                pc.sendPackets(new S_SystemMessage("在攻城區內無法使用。"));
            } else if (pc.getMap().isMapteleport() <= 0 || ItemTable.get().getTemplate(pc.getMap().isMapteleport()) == null) {
                pc.sendPackets(new S_ServerMessage("地圖限制無法使用。"));
            } else if (!pc.isInParty()) {
                pc.sendPackets(new S_ServerMessage(425));
            } else {
                Iterator<L1Object> it = World.get().getVisibleObjects(pc, -1).iterator();
                while (it.hasNext()) {
                    L1Object obj = it.next();
                    if ((obj instanceof L1PcInstance) && pc.getParty() != ((L1PcInstance) obj).getParty()) {
                        pc.sendPackets(new S_SystemMessage("附近有其他隊伍無法使用。"));
                        return;
                    }
                }
                L1Party party = pc.getParty();
                if (party != null) {
                    ConcurrentHashMap<Integer, L1PcInstance> pcs = party.partyUsers();
                    if (!pcs.isEmpty() && pcs.size() > 0) {
                        for (L1PcInstance pc2 : pcs.values()) {
                            if (pc2 != null && pc.getId() != pc2.getId() && !pc2.isDead() && !pc2.isPrivateShop() && !pc2.isFishing() && !pc2.isGhost() && !pc2.isParalyzedX() && !pc2.isInvisble() && pc2.getMap().isMapteleport() > 0) {
                                L1BuffUtil.cancelAbsoluteBarrier(pc2);
                                if (pc2 != null) {
                                    pc2.setTempID(pc.getId());
                                    pc2.getParty();
                                    L1Teleport.teleportToTargetFront_Party(pc2, pc, item_id, mapName, 2, chkItem(pc2, ConfigOther.SET_ITEM_Party, ConfigOther.SET_ITEM_COUNT_Party));
                                }
                            }
                        }
                    } else {
                        return;
                    }
                }
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), L1PcInventory.COL_ATTR_ENCHANT_LEVEL));
                if (this._remove == 1) {
                    pc.getInventory().removeItem(item, 1);
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void set_set(String[] set) {
        try {
            this._remove = Integer.parseInt(set[1]);
        } catch (Exception ignored) {
        }
    }

    private boolean chkItem(L1PcInstance pc, int items, int counts) {
        if (CreateNewItem.checkNewItem(pc, items, counts) < 1) {
            return false;
        }
        return true;
    }
}
