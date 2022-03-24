package com.lineage.data.npc.xljnet;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.EzpayReading3;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Item;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NPC_Promotion extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(NPC_Promotion.class);

    private NPC_Promotion() {
    }

    public static NpcExecutor get() {
        return new NPC_Promotion();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "send_s_0"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equals("up")) {
            showPage(pc, npc, pc.get_other().get_page() - 1);
        } else if (cmd.equals("dn")) {
            showPage(pc, npc, pc.get_other().get_page() + 1);
        } else if (cmd.equalsIgnoreCase("1")) {
            pc.get_otherList().SHOPLIST.clear();
            Map<Integer, int[]> info = EzpayReading3.get().ezpayInfo(pc.getAccountName().toLowerCase());
            if (info.size() <= 0) {
                isCloseList = true;
                pc.sendPackets(new S_ServerMessage("並沒有查詢到您的相關商品記錄。"));
            } else {
                pc.get_other().set_page(0);
                int index = 0;
                for (Integer key : info.keySet()) {
                    int[] value = info.get(key);
                    if (value != null) {
                        pc.get_otherList().SHOPLIST.put(Integer.valueOf(index), value);
                        index++;
                    }
                }
                showPage(pc, npc, 0);
            }
        } else if (cmd.equalsIgnoreCase("2")) {
            Map<Integer, int[]> info2 = EzpayReading3.get().ezpayInfo(pc.getAccountName().toLowerCase());
            if (info2.size() <= 0) {
                pc.sendPackets(new S_ServerMessage("並沒有查詢到您的相關商品記錄。"));
            } else {
                for (Integer key2 : info2.keySet()) {
                    int[] value2 = info2.get(key2);
                    int id = value2[0];
                    int itemid = value2[1];
                    int count = value2[2];
                    if (EzpayReading3.get().update(pc.getAccountName(), id, pc.getName(), pc.getNetConnection().getIp().toString())) {
                        createNewItem(pc, npc, itemid, (long) count);
                        _log.fatal("帳號:" + pc.getAccountName().toLowerCase() + " 人物:" + pc.getName() + " 領取交易序號:" + id + "(" + itemid + ") 數量:" + count + " 完成!!");
                        WriteLogTxt.Recording("即時獎勵成功紀錄", "帳號:" + pc.getAccountName().toLowerCase() + " 人物:" + pc.getName() + " 領取交易序號:" + id + "(" + itemid + ") 數量:" + count + " 完成!!");
                    } else {
                        pc.sendPackets(new S_ServerMessage("\\fV領取失敗!!請聯繫線上GM!! ID:" + id));
                        _log.fatal("帳號:" + pc.getAccountName().toLowerCase() + " 人物:" + pc.getName() + " 領取交易序號:" + id + " 領取失敗!!");
                        WriteLogTxt.Recording("即時獎勵失敗紀錄", "帳號:" + pc.getAccountName().toLowerCase() + " 人物:" + pc.getName() + " 領取交易序號:" + id + " 領取失敗!!");
                    }
                }
            }
            isCloseList = true;
        } else {
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }

    private static void showPage(L1PcInstance pc, L1NpcInstance npc, int page) {
        L1Item itemtmp;
        Map<Integer, int[]> list = pc.get_otherList().SHOPLIST;
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
            int[] info = list.get(Integer.valueOf(key));
            if (!(info == null || (itemtmp = ItemTable.get().getTemplate(info[1])) == null)) {
                stringBuilder.append(String.valueOf(itemtmp.getName()) + "(" + info[2] + "),");
            }
        }
        String[] clientStrAry = stringBuilder.toString().split(",");
        if (allpage == 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "send_s_1", clientStrAry));
        } else if (page < 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "send_s_3", clientStrAry));
        } else if (page >= allpage - 1) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "send_s_4", clientStrAry));
        } else {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "send_s_2", clientStrAry));
        }
    }

    private static void createNewItem(L1PcInstance pc, L1NpcInstance npc, int item_id, long count) {
        if (pc != null) {
            try {
                L1ItemInstance item = ItemTable.get().createItem(item_id);
                if (item != null) {
                    item.setCount(count);
                    item.setIdentified(true);
                    pc.getInventory().storeItem(item);
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), String.valueOf(item.getItem().getName()) + "(" + count + ")"));
                    return;
                }
                _log.error("給予物件失敗 原因: 指定編號物品不存在(" + item_id + ")");
            } catch (Exception e) {
                _log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
