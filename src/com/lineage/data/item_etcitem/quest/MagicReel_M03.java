package com.lineage.data.item_etcitem.quest;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.CharItemsTimeReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.sql.Timestamp;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MagicReel_M03 extends ItemExecutor {
    private static final Log _log = LogFactory.getLog(MagicReel_M03.class);

    private MagicReel_M03() {
    }

    public static ItemExecutor get() {
        return new MagicReel_M03();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        try {
            L1ItemInstance tgitem = pc.getInventory().getItem(data[0]);
            if (tgitem.getItem().getType2() == 0) {
                int itemid = -1;
                switch (tgitem.getItemId()) {
                    case 49317:
                        itemid = 30421;
                        break;
                    case 49318:
                        itemid = 30423;
                        break;
                    case 49319:
                        itemid = 30425;
                        break;
                    case 49320:
                        itemid = 30422;
                        break;
                    case 49321:
                        itemid = 30424;
                        break;
                    case 49322:
                        itemid = 30426;
                        break;
                    case 49323:
                        itemid = 30429;
                        break;
                    case 49324:
                        itemid = 30430;
                        break;
                    case 49325:
                        itemid = 30428;
                        break;
                    case 49326:
                        itemid = 30427;
                        break;
                    case 49327:
                        itemid = 30471;
                        break;
                    case 49328:
                        itemid = 30472;
                        break;
                    case 49329:
                        itemid = 30473;
                        break;
                    case 49330:
                        itemid = 30474;
                        break;
                    case 49331:
                        itemid = 30475;
                        break;
                    case 49332:
                        itemid = 30476;
                        break;
                    case 49333:
                        itemid = 30477;
                        break;
                    case 49334:
                        itemid = 30478;
                        break;
                    default:
                        pc.sendPackets(new S_ServerMessage(79));
                        break;
                }
                if (itemid != -1) {
                    pc.getInventory().removeItem(item, 1);
                    pc.getInventory().removeItem(tgitem, 1);
                    L1ItemInstance newitem = ItemTable.get().createItem(itemid);
                    Timestamp ts = new Timestamp((10800 * 1000) + System.currentTimeMillis());
                    newitem.set_time(ts);
                    CharItemsTimeReading.get().addTime(newitem.getId(), ts);
                    CreateNewItem.createNewItem(pc, newitem, 1);
                    return;
                }
                return;
            }
            pc.sendPackets(new S_ServerMessage(79));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
