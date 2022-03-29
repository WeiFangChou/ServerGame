package com.lineage.data.npc.shop;

import com.lineage.data.event.VIPSet;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.VIPReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_VIP extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_VIP.class);

    private Npc_VIP() {
    }

    public static NpcExecutor get() {
        return new Npc_VIP();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        Timestamp time = VIPReading.get().getOther(pc);
        if (time != null) {
            String key = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2", new String[]{key}));
            return;
        }
        L1Item item = ItemTable.get().getTemplate(VIPSet.ITEMID);
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v1", new String[]{String.valueOf(VIPSet.ADENA), item.getName(), String.valueOf(VIPSet.DATETIME)}));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        int mapid = -1;
        int x = -1;
        int y = -1;
        if (cmd.equalsIgnoreCase("1")) {
            Timestamp time = VIPReading.get().getOther(pc);
            if (time != null) {
                String key = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2", new String[]{key}));
                return;
            }
            L1ItemInstance itemT = pc.getInventory().checkItemX(VIPSet.ITEMID, (long) VIPSet.ADENA);
            if (itemT == null) {
                pc.sendPackets(new S_ServerMessage(337, "天寶"));
            } else {
                pc.getInventory().removeItem(itemT, (long) VIPSet.ADENA);
                Timestamp value = new Timestamp((((long) (VIPSet.DATETIME * 24 * 60 * 60)) * 1000) + System.currentTimeMillis());
                VIPReading.get().storeOther(pc.getId(), value);
                String key2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
                pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_v2", new String[]{key2}));
                return;
            }
        } else if (cmd.equalsIgnoreCase("2")) {
            mapid = L1SkillId.STATUS_BLUE_POTION;
            x = 32756;
            y = 32679;
        } else if (cmd.equalsIgnoreCase("3")) {
            mapid = 200;
            x = 32756;
            y = 32942;
        } else if (cmd.equalsIgnoreCase("4")) {
            mapid = 10022;
            x = 32756;
            y = 32679;
        } else if (cmd.equalsIgnoreCase("5")) {
            mapid = L1PcInstance.CLASSID_DRAGON_KNIGHT_FEMALE;
            x = 32800;
            y = 32751;
        } else if (cmd.equalsIgnoreCase("6")) {
            mapid = 10021;
            x = 32756;
            y = 32679;
        } else if (cmd.equalsIgnoreCase("7")) {
            mapid = 7781;
            x = 32739;
            y = 32686;
        }
        if (mapid != -1) {
            Timestamp time2 = VIPReading.get().getOther(pc);
            if (time2 != null) {
                if (time2.after(new Timestamp(System.currentTimeMillis()))) {
                    teleport(pc, x, y, mapid);
                } else {
                    VIPReading.get().delOther(pc.getId());
                }
            }
        } else {
            pc.sendPackets(new S_ServerMessage("等級(" + pc.getLevel() + ")已經超過限制"));
        }
        pc.sendPackets(new S_CloseList(pc.getId()));
    }

    private void teleport(L1PcInstance pc, int x, int y, int mapid) {
        try {
            L1Location newLocation = new L1Location(x, y, mapid).randomLocation(200, false);
            L1Teleport.teleport(pc, newLocation.getX(), newLocation.getY(),  newLocation.getMapId(), 5, true);
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
