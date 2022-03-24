package com.lineage.data.npc.teleport;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTeleportTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_PacketBoxGame;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.templates.L1TeleportLoc;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Npc_Teleport extends NpcExecutor {
    private Npc_Teleport() {
    }

    public static NpcExecutor get() {
        return new Npc_Teleport();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_0"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        if (cmd.equals("up")) {
            showPage(pc, npc, pc.get_other().get_page() - 1);
        } else if (cmd.equals("dn")) {
            showPage(pc, npc, pc.get_other().get_page() + 1);
        } else if (cmd.equals("del")) {
            if (ServerUseMapTimer.MAP.get(pc) != null) {
                pc.get_other().set_usemap(-1);
                pc.get_other().set_usemapTime(0);
                pc.sendPackets(new S_PacketBoxGame(72));
                ServerUseMapTimer.MAP.remove(pc);
            }
        } else if (cmd.matches("[0-9]+")) {
            teleport(pc, npc, Integer.valueOf(String.valueOf(pc.get_other().get_page()) + cmd));
        } else {
            pc.get_other().set_page(0);
            HashMap<Integer, L1TeleportLoc> teleportMap = NpcTeleportTable.get().get_teles(cmd);
            if (teleportMap == null) {
                pc.sendPackets(new S_ServerMessage(1447));
            } else if (teleportMap.size() <= 0) {
                pc.sendPackets(new S_ServerMessage(1447));
            } else {
                pc.get_otherList().teleport(teleportMap);
                showPage(pc, npc, 0);
            }
        }
    }

    private void teleport(L1PcInstance pc, L1NpcInstance npc, Integer key) throws Exception {
        L1TeleportLoc info = pc.get_otherList().teleportMap().get(key);
        if (info.get_time() == 0 || ServerUseMapTimer.MAP.get(pc) == null) {
            boolean party = false;
            if (info.get_user() > 0) {
                if (!pc.isInParty()) {
                    pc.sendPackets(new S_ServerMessage(425));
                    return;
                } else if (!pc.getParty().isLeader(pc)) {
                    pc.sendPackets(new S_ServerMessage("你必須是隊伍的領導者"));
                    return;
                } else if (pc.getParty().getNumOfMembers() < info.get_user()) {
                    pc.sendPackets(new S_ServerMessage("隊伍成員必須達到" + info.get_user() + "人"));
                    return;
                } else {
                    party = true;
                }
            }
            if (info.get_min() > pc.getLevel()) {
                pc.sendPackets(new S_ServerMessage("等級(" + pc.getLevel() + ")低於限制"));
            } else if (info.get_max() < pc.getLevel()) {
                pc.sendPackets(new S_ServerMessage("等級(" + pc.getLevel() + ")超過限制"));
            } else {
                int itemid = info.get_itemid();
                L1ItemInstance item = pc.getInventory().checkItemX(itemid, (long) info.get_price());
                if (item != null) {
                    if (info.get_time() != 0) {
                        pc.get_other().set_usemap(info.get_mapid());
                        ServerUseMapTimer.put(pc, info.get_time());
                    }
                    pc.getInventory().removeItem(item, (long) info.get_price());
                    if (party) {
                        ConcurrentHashMap<Integer, L1PcInstance> pcs = pc.getParty().partyUsers();
                        if (!pcs.isEmpty() && pcs.size() > 0) {
                            for (L1PcInstance tgpc : pcs.values()) {
                                if (info.get_time() != 0) {
                                    if (ServerUseMapTimer.MAP.get(tgpc) != null) {
                                        tgpc.get_other().set_usemap(-1);
                                        tgpc.get_other().set_usemapTime(0);
                                        tgpc.sendPackets(new S_PacketBoxGame(72));
                                        ServerUseMapTimer.MAP.remove(tgpc);
                                    }
                                    tgpc.get_other().set_usemap(info.get_mapid());
                                    ServerUseMapTimer.put(tgpc, info.get_time());
                                }
                                L1Teleport.teleport(tgpc, info.get_locx(), info.get_locy(), (short) info.get_mapid(), 5, true);
                            }
                            return;
                        }
                        return;
                    }
                    L1Teleport.teleport(pc, info.get_locx(), info.get_locy(), (short) info.get_mapid(), 5, true);
                    return;
                }
                pc.sendPackets(new S_ServerMessage(337, ItemTable.get().getTemplate(itemid).getName()));
            }
        } else if (pc.get_other().get_usemap() == info.get_mapid()) {
            L1Teleport.teleport(pc, info.get_locx(), info.get_locy(), (short) info.get_mapid(), 5, true);
        } else {
            pc.sendPackets(new S_ServerMessage("必須先消除其它地圖使用權才能進入這裡"));
        }
    }

    private void showPage(L1PcInstance pc, L1NpcInstance npc, int page) {
        L1Item itemtmp;
        Map<Integer, L1TeleportLoc> list = pc.get_otherList().teleportMap();
        int allpage = list.size() / 10;
        if (page > allpage || page < 0) {
            page = 0;
        }
        if (list.size() % 10 != 0) {
            allpage++;
        }
        pc.get_other().set_page(page);
        int showId = page * 10;
        StringBuilder stringBuilder = new StringBuilder();
        for (int key = showId; key < showId + 10; key++) {
            L1TeleportLoc info = list.get(Integer.valueOf(key));
            if (!(info == null || (itemtmp = ItemTable.get().getTemplate(info.get_itemid())) == null)) {
                stringBuilder.append(info.get_name());
                if (info.get_user() > 0) {
                    stringBuilder.append("隊伍:" + info.get_user());
                }
                stringBuilder.append(" (" + itemtmp.getName() + "-" + Integer.toString(info.get_price()) + "),");
            }
        }
        String[] clientStrAry = stringBuilder.toString().split(",");
        if (allpage == 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_1", clientStrAry));
        } else if (page < 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_3", clientStrAry));
        } else if (page >= allpage - 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_4", clientStrAry));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_t_2", clientStrAry));
        }
    }
}
