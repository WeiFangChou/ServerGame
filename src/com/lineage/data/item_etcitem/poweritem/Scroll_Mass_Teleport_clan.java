package com.lineage.data.item_etcitem.poweritem;

import com.lineage.config.ConfigOther;
import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.World;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Scroll_Mass_Teleport_clan extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Scroll_Mass_Teleport_clan.class);
    private int _remove;

    private Scroll_Mass_Teleport_clan() {
    }

    public static ItemExecutor get() {
        return new Scroll_Mass_Teleport_clan();
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
            } else if (pc.getClanid() == 0) {
                pc.sendPackets(new S_SystemMessage("未加入血盟無法使用。"));
            } else if (pc.getClan().getOnlineClanMemberSize() < 2) {
                pc.sendPackets(new S_SystemMessage("血盟沒有其他在線上成員。"));
            } else {
                Iterator<L1Object> it = World.get().getVisibleObjects(pc, -1).iterator();
                while (it.hasNext()) {
                    L1Object obj = it.next();
                    if (obj instanceof L1PcInstance) {
                        L1PcInstance tgpc = (L1PcInstance) obj;
                        if (!(tgpc.getClanid() == 0 || pc.getClanid() == tgpc.getClanid())) {
                            pc.sendPackets(new S_SystemMessage("附近有其他血盟成員無法使用。"));
                            return;
                        }
                    }
                }
                L1Clan clan = pc.getClan();
                if (clan != null) {
                    for (String member : clan.getAllMembers()) {
                        L1PcInstance clan_pc = World.get().getPlayer(member);
                        if (clan_pc != null && pc.getId() != clan_pc.getId() && !clan_pc.isDead() && !clan_pc.isPrivateShop() && !clan_pc.isFishing() && !clan_pc.isGhost() && !clan_pc.isParalyzedX() && !clan_pc.isInvisble() && clan_pc.getMap().isMapteleport() > 0 && clan_pc != null) {
                            L1BuffUtil.cancelAbsoluteBarrier(clan_pc);
                            clan_pc.setTempID(pc.getId());
                            clan_pc.getClassId();
                            clan_pc.setCallClan(true);
                            L1Teleport.teleportToTargetFront_clan(clan_pc, pc, item_id, mapName, 2, chkItem(clan_pc, ConfigOther.SET_ITEM, ConfigOther.SET_ITEM_COUNT));
                        }
                    }
                }
                L1BuffUtil.cancelAbsoluteBarrier(pc);
                pc.sendPacketsAll(new S_SkillSound(pc.getId(), 3198));
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
