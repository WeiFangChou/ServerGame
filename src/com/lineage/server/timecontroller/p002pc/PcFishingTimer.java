package com.lineage.server.timecontroller.p002pc;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.server.datatables.FishingTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Fishing;
import com.lineage.server.thread.PcOtherThreadPool;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* renamed from: com.lineage.server.timecontroller.pc.PcFishingTimer */
public class PcFishingTimer extends TimerTask {
    private static final List<L1PcInstance> _fishingList = new ArrayList();
    private static final Log _log = LogFactory.getLog(PcFishingTimer.class);
    private static final Random _random = new Random();
    private ScheduledFuture<?> _timer;

    public void start() {
        this._timer = PcOtherThreadPool.get().scheduleAtFixedRate(this, 20000, 20000);
    }

    public void run() {
        try {
            fishing();
        } catch (Exception e) {
            _log.error("釣魚時間軸異常重啟", e);
            PcOtherThreadPool.get().cancel(this._timer, false);
            new PcFishingTimer().start();
        }
    }

    public static void addMember(L1PcInstance pc) {
        if (pc != null && !_fishingList.contains(pc)) {
            _fishingList.add(pc);
        }
    }

    public static void removeMember(L1PcInstance pc) {
        if (pc != null && _fishingList.contains(pc)) {
            _fishingList.remove(pc);
        }
    }

    private void fishing() throws Exception {
        if (!_fishingList.isEmpty()) {
            for (int i = 0; i < _fishingList.size(); i++) {
                L1PcInstance pc = _fishingList.get(i);
                if (pc.isFishing()) {
                    if (pc.getMapId() != 5300) {
                        finishFishing(pc, true);
                    } else if (!pc.getInventory().checkItem(41295, 2)) {
                        finishFishing(pc, true);
                    } else if (pc.getOnlineStatus() != 1) {
                        finishFishing(pc, true);
                    } else if (pc.getNetConnection() == null) {
                        finishFishing(pc, true);
                    } else {
                        L1Fishing temp = FishingTable.get().get_item();
                        if (temp != null) {
                            if (_random.nextInt(temp.get_randomint()) > temp.get_random()) {
                                temp = null;
                            }
                            successFishing(pc, temp);
                        }
                    }
                }
            }
        }
    }

    public static void finishFishing(L1PcInstance pc, boolean msg) {
        pc.setFishing(false, -1, -1);
        if (msg) {
            pc.sendPackets(new S_ServerMessage(1163));
        }
        pc.sendPacketsAll(new S_CharVisualUpdate(pc));
        removeMember(pc);
    }

    private void successFishing(L1PcInstance pc, L1Fishing temp) throws Exception {
        if (temp == null) {
            if (!pc.getInventory().consumeItem(41295, 2)) {
                finishFishing(pc, true);
            }
        } else if (ItemTable.get().getTemplate(temp.get_itemid()) == null) {
        } else {
            if (pc.getInventory().consumeItem(41295, (long) temp.get_bait())) {
                CreateNewItem.createNewItem(pc, temp.get_itemid(), 1);
            } else {
                finishFishing(pc, true);
            }
        }
    }
}
