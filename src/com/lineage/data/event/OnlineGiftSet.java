package com.lineage.data.event;

import com.lineage.config.ConfigOther;
import com.lineage.data.executor.EventExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Event;
import com.lineage.server.templates.L1Item;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.PerformanceTimer;
import com.lineage.server.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OnlineGiftSet extends EventExecutor {
    private static final Map<L1PcInstance, Integer> _getMap = new ConcurrentHashMap();
    private static final Map<Integer, ArrayList<GetItemData>> _giftList = new HashMap();
    private static final Log _log = LogFactory.getLog(OnlineGiftSet.class);
    private static int _time = 0;

    private OnlineGiftSet() {
    }

    public static EventExecutor get() {
        return new OnlineGiftSet();
    }

    public static void add(L1PcInstance tgpc) {
        if (_time != 0 && _getMap.get(tgpc) == null) {
            _getMap.put(tgpc, _time);
        }
    }

    public static void remove(L1PcInstance tgpc) {
        if (_time != 0) {
            _getMap.remove(tgpc);
        }
    }

    /* access modifiers changed from: private */
    public class GetItemData {
        public int _getAmount;
        public int _getItemId;

        private GetItemData() {
            this._getItemId = L1ItemId.ADENA;
            this._getAmount = 1;
        }

    }

    /* access modifiers changed from: private */
    public static void getitem(L1PcInstance tgpc) {
        ArrayList<GetItemData> value;
        try {
            if (check(tgpc)) {
                if (tgpc.isPrivateShop()) {
                    value = _giftList.get(1);
                } else if (tgpc.getClanid() != 0) {
                    value = _giftList.get(2);
                } else if (tgpc.isFishing()) {
                    value = _giftList.get(3);
                } else {
                    value = _giftList.get(4);
                }
                if (value == null) {
                    value = _giftList.get(4);
                }
                if (value != null) {
                    Iterator<GetItemData> it = value.iterator();
                    while (it.hasNext()) {
                        GetItemData iteminfo = it.next();
                        if (iteminfo != null) {
                            L1ItemInstance item = ItemTable.get().createItem(iteminfo._getItemId);
                            item.setCount((long) iteminfo._getAmount);
                            if (item != null && tgpc.getInventory().checkAddItem(item, 1) == 0) {
                                tgpc.getInventory().storeItem(item);
                                tgpc.sendPackets(new S_ServerMessage("??????????????????: " + item.getLogName()));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean check(L1PcInstance tgpc) {
        if (tgpc == null) {
            return false;
        }
        try {
            if (tgpc.getOnlineStatus() == 0 || tgpc.getNetConnection() == null || tgpc.getLevel() < ConfigOther.ONLINE_GIFT_LEVEL) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    @Override // com.lineage.data.executor.EventExecutor
    public void execute(final L1Event event) {
        final PerformanceTimer timer = new PerformanceTimer();
        boolean isError = false;
        try {
            final String[] set = event.get_eventother().split(",");

            try {
                _time = Integer.parseInt(set[0]);
                if (_time <= 0) {
                    _log.error("???????????????????????????(??????)?????? - ????????????????????????");
                    isError = true;
                    return;
                }

            } catch (Exception e) {
                _log.error("???????????????????????????(??????)?????? - ????????????????????????");
                isError = true;
                return;
            }

            try {
                for (int i = 1; i < set.length; i++) {
                    final String[] setItem = set[i].split("#");
                    GetItemData itemData = new GetItemData();

                    int type = Integer.parseInt(setItem[0]);// ??????????????????
                    switch (type) {
                        case 1:
                        case 2:
                            break;

                        default:
                            type = 3;
                            break;
                    }

                    itemData._getItemId = Integer.parseInt(setItem[1]);

                    L1Item item = ItemTable.get().getTemplate(
                            itemData._getItemId);
                    if (item == null) {
                        _log.error("?????????????????????????????? - ???????????????????????? - ??????????????????????????????:"
                                + itemData._getItemId);
                        isError = true;
                        break;
                    }
                    if (!item.isStackable()) {
                        _log.error("?????????????????????????????? - ?????????????????????????????????:"
                                + itemData._getItemId);
                        continue;
                    }
                    itemData._getAmount = Integer.parseInt(setItem[2]);
                    if (itemData._getAmount <= 0) {
                        _log.error("?????????????????????????????? - ???????????????????????? - ??????????????????0:"
                                + itemData._getItemId + " ???????????????1???");
                        itemData._getAmount = 1;
                    }

                    if (_giftList.get(type) == null) {
                        final ArrayList<GetItemData> value = new ArrayList<GetItemData>();
                        value.add(itemData);
                        _giftList.put(type, value);

                    } else {
                        _giftList.get(type).add(itemData);
                    }
                }

            } catch (Exception e) {
                _log.error("?????????????????????????????? - ?????????#????????????????????????3???(??????#????????????#????????????)");
                isError = true;
            }

        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);

        } finally {
            if (!isError) {
                final GetItemTimer getItemTimer = new GetItemTimer();
                getItemTimer.start();
                _log.info("????????????????????????: " + _giftList.size() + "(" + timer.get()
                        + "ms)");

            } else {
                _time = 0;
                _giftList.clear();
            }
        }
    }

    private class GetItemTimer extends TimerTask {
        private ScheduledFuture<?> _timer;

        private GetItemTimer() {
        }

        public void start() {
            this._timer = GeneralThreadPool.get().scheduleAtFixedRate(this, 60000L, 60000L);
        }

        public void run() {
            try {
                if (!OnlineGiftSet._getMap.isEmpty()) {
                    for (L1PcInstance tgpc : OnlineGiftSet._getMap.keySet()) {
                        Thread.sleep(1);
                        if (World.get().getPlayer(tgpc.getName()) == null) {
                            OnlineGiftSet._getMap.remove(tgpc);
                        } else {
                            Integer time = OnlineGiftSet._getMap.get(tgpc);
                            if (time != null) {
                                int time2 = time - 1;
                                if (time2 <= 0) {
                                    OnlineGiftSet.getitem(tgpc);
                                    OnlineGiftSet._getMap.put(tgpc, OnlineGiftSet._time);
                                } else {
                                    OnlineGiftSet._getMap.put(tgpc, time2);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                OnlineGiftSet._log.error("???????????????????????????????????????", e);
                GeneralThreadPool.get().cancel(this._timer, false);
                new GetItemTimer().start();
            }
        }
    }
}
