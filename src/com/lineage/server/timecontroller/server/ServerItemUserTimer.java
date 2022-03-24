package com.lineage.server.timecontroller.server;

import com.lineage.server.datatables.lock.CharItemsReading;
import com.lineage.server.datatables.lock.PetReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Pet;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldItem;
import com.lineage.server.world.WorldPet;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServerItemUserTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(ServerItemUserTimer.class);
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 1000L, 1000L);
    }

    public void run() {
        try {
            Collection<L1ItemInstance> items = WorldItem.get().all();
            if (!items.isEmpty()) {
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                for (L1ItemInstance item : items) {
                    if (item.get_time() != null) {
                        checkItem(item, ts);
                        Thread.sleep(5);
                    }
                }
            }
        } catch (Exception e) {
            _log.error("物品使用期限計時時間軸異常重啟", e);
            GeneralThreadPool.get().cancel(this._timer, false);
            new ServerItemUserTimer().start();
        }
    }

    private static void checkItem(L1ItemInstance item, Timestamp ts) throws Exception {
        L1PetInstance tgpet;
        if (item.get_time().before(ts)) {
            L1Object object = World.get().findObject(item.get_char_objid());
            if (object == null) {
                CharItemsReading.get().deleteItem(item.get_char_objid(), item);
            } else if (object instanceof L1PcInstance) {
                L1PcInstance tgpc = (L1PcInstance) object;
                tgpc.getInventory().removeItem(item);
                tgpc.sendPackets(new S_ServerMessage(item.getName() + "有效日期已到期,被系統回收。"));
                L1Pet pet = PetReading.get().getTemplate(item.getId());
                if (pet != null && (tgpet = WorldPet.get().get(Integer.valueOf(pet.get_objid()))) != null) {
                    tgpet.dropItem(tgpet);
                    tgpet.deleteMe();
                }
            }
        }
    }
}
