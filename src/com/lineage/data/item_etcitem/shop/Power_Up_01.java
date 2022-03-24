package com.lineage.data.item_etcitem.shop;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemPowerUpdateTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1ItemPowerUpdate;
import com.lineage.server.world.World;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Power_Up_01 extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(Power_Up_01.class);
    private static final Random _random = new Random();

    private Power_Up_01() {
    }

    public static ItemExecutor get() {
        return new Power_Up_01();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            L1ItemInstance tgItem = pc.getInventory().getItem(data[0]);
            if (tgItem != null) {
                L1ItemPowerUpdate info = ItemPowerUpdateTable.get().get(tgItem.getItemId());
                if (info == null) {
                    pc.sendPackets(new S_ServerMessage(79));
                } else if (info.get_mode() == 4) {
                    pc.sendPackets(new S_ServerMessage(79));
                } else if (info.get_nedid() != item.getItemId()) {
                    pc.sendPackets(new S_ServerMessage(79));
                } else {
                    Map<Integer, L1ItemPowerUpdate> tmplist = ItemPowerUpdateTable.get().get_type_id(tgItem.getItemId());
                    if (tmplist.isEmpty()) {
                        pc.sendPackets(new S_ServerMessage(79));
                        return;
                    }
                    int order_id = info.get_order_id();
                    L1ItemPowerUpdate tginfo = tmplist.get(Integer.valueOf(order_id + 1));
                    if (tginfo == null) {
                        pc.sendPackets(new S_ServerMessage(79));
                        return;
                    }
                    pc.getInventory().removeItem(item, 1);
                    if (_random.nextInt(L1SkillId.STATUS_BRAVE) <= tginfo.get_random()) {
                        pc.getInventory().removeItem(tgItem, 1);
                        L1ItemInstance tginfo_item = ItemTable.get().createItem(tginfo.get_itemid());
                        if (tginfo_item != null) {
                            tginfo_item.setIdentified(true);
                            tginfo_item.setCount(1);
                            pc.getInventory().storeItem(tginfo_item);
                            pc.sendPackets(new S_ServerMessage("\\fT" + tgItem.getName() + " 崩裂後獲得展新的 " + tginfo_item.getName()));
                            switch (tginfo.is_out()) {
                                case 1:
                                    World.get().broadcastPacketToAll(new S_ServerMessage("恭喜玩家【 " + pc.getName() + " 】使用【 " + item.getName() + " 】強化成功，獲得【 " + tginfo_item.getName() + "(" + tginfo_item.getCount() + ")】。"));
                                    break;
                                case 2:
                                    World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜玩家【 " + pc.getName() + " 】使用【 " + item.getName() + " 】強化成功，獲得【 " + tginfo_item.getName() + "(" + tginfo_item.getCount() + ")】。"));
                                    break;
                                case 3:
                                    World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, "恭喜玩家【 " + pc.getName() + " 】使用【 " + item.getName() + " 】強化成功，獲得【 " + tginfo_item.getName() + "(" + tginfo_item.getCount() + ")】。"));
                                    World.get().broadcastPacketToAll(new S_ServerMessage("恭喜玩家【 " + pc.getName() + " 】使用【 " + item.getName() + " 】強化成功，獲得【 " + tginfo_item.getName() + "(" + tginfo_item.getCount() + ")】。"));
                                    break;
                            }
                            WriteLogTxt.Recording("道具升階紀錄", "玩家:【" + pc.getName() + "】 " + "使用:【 " + item.getName() + "】" + "強化成功，獲得【 " + tginfo_item.getName() + "(" + tginfo_item.getCount() + ")】" + "道具ID：【" + item.getId() + "】");
                            return;
                        }
                        _log.error("給予物件失敗 原因: 指定編號物品不存在(" + tginfo.get_itemid() + ")");
                        return;
                    }
                    switch (info.get_mode()) {
                        case 0:
                            pc.sendPackets(new S_ServerMessage(L1SkillId.AQUA_PROTECTER, tgItem.getLogName(), "$252", "$248"));
                            return;
                        case 1:
                            pc.sendPackets(new S_ServerMessage("\\fR" + tgItem.getName() + "升級失敗!"));
                            pc.getInventory().removeItem(tgItem, 1);
                            CreateNewItem.createNewItem(pc, tmplist.get(Integer.valueOf(order_id - 1)).get_itemid(), 1);
                            return;
                        case 2:
                            pc.sendPackets(new S_ServerMessage(164, tgItem.getLogName(), "$252"));
                            pc.getInventory().removeItem(tgItem, 1);
                            return;
                        case 3:
                            if (_random.nextBoolean()) {
                                pc.getInventory().removeItem(tgItem, 1);
                                CreateNewItem.createNewItem(pc, tmplist.get(Integer.valueOf(order_id - 1)).get_itemid(), 1);
                                return;
                            }
                            pc.sendPackets(new S_ServerMessage(164, tgItem.getLogName(), "$252"));
                            pc.getInventory().removeItem(tgItem, 1);
                            return;
                        default:
                            return;
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
