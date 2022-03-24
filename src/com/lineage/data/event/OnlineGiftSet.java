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
            _getMap.put(tgpc, Integer.valueOf(_time));
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

        /* synthetic */ GetItemData(OnlineGiftSet onlineGiftSet, GetItemData getItemData) {
            this();
        }
    }

    /* access modifiers changed from: private */
    public static void getitem(L1PcInstance tgpc) {
        ArrayList<GetItemData> value;
        try {
            if (check(tgpc)) {
                if (tgpc.isPrivateShop()) {
                    value = _giftList.get(new Integer(1));
                } else if (tgpc.getClanid() != 0) {
                    value = _giftList.get(new Integer(2));
                } else if (tgpc.isFishing()) {
                    value = _giftList.get(new Integer(3));
                } else {
                    value = _giftList.get(new Integer(4));
                }
                if (value == null) {
                    value = _giftList.get(new Integer(4));
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
                                tgpc.sendPackets(new S_ServerMessage("獲得連線獎勵: " + item.getLogName()));
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
    public void execute(L1Event event) {
        PerformanceTimer timer = new PerformanceTimer();
        boolean isError = false;
        try {
            String[] set = event.get_eventother().split(",");
            try {
                _time = Integer.parseInt(set[0]);
                if (_time <= 0) {
                    _log.error("設定給予獎勵的時間(分鐘)異常 - 將不啟用本項設置");
                    if (1 == 0) {
                        new GetItemTimer(this, null).start();
                        _log.info("載入給予獎勵物品: " + _giftList.size() + "(" + timer.get() + "ms)");
                        return;
                    }
                    _time = 0;
                    _giftList.clear();
                    return;
                }
                int i = 1;
                while (true) {
                    try {
                        if (i < set.length) {
                            String[] setItem = set[i].split("#");
                            GetItemData itemData = new GetItemData(this, null);
                            int type = Integer.parseInt(setItem[0]);
                            switch (type) {
                                case 1:
                                case 2:
                                case 3:
                                    break;
                                default:
                                    type = 4;
                                    break;
                            }
                            itemData._getItemId = Integer.parseInt(setItem[1]);
                            L1Item item = ItemTable.get().getTemplate(itemData._getItemId);
                            if (item == null) {
                                _log.error("設定給予獎勵物品異常 - 將不啟用本項設置 - 找不到這個編號的物品:" + itemData._getItemId);
                                isError = true;
                            } else {
                                if (!item.isStackable()) {
                                    _log.error("設定給予獎勵物品異常 - 這個編號的物品無發堆疊:" + itemData._getItemId);
                                } else {
                                    itemData._getAmount = Integer.parseInt(setItem[2]);
                                    if (itemData._getAmount <= 0) {
                                        _log.error("設定給予獎勵物品異常 - 將不啟用本項設置 - 數量小於等於0:" + itemData._getItemId + " 預設將只給1個");
                                        itemData._getAmount = 1;
                                    }
                                    if (_giftList.get(new Integer(type)) == null) {
                                        ArrayList<GetItemData> value = new ArrayList<>();
                                        value.add(itemData);
                                        _giftList.put(new Integer(type), value);
                                    } else {
                                        _giftList.get(new Integer(type)).add(itemData);
                                    }
                                }
                                i++;
                            }
                        }
                    } catch (Exception e) {
                        _log.error("設定給予獎勵物品異常 - 請檢查#內的設置是否吻合3項(狀態#物品編號#給予數量)");
                        isError = true;

                    }
                    if (!isError) {
                        new GetItemTimer(this, null).start();
                        _log.info("載入給予獎勵物品: " + _giftList.size() + "(" + timer.get() + "ms)");
                        return;
                    }
                    _time = 0;
                    _giftList.clear();
                }

            } catch (Exception e2) {
                _log.error("設定給予獎勵的時間(分鐘)異常 - 將不啟用本項設置");
                if (1 == 0) {
                    new GetItemTimer(this, null).start();
                    _log.info("載入給予獎勵物品: " + _giftList.size() + "(" + timer.get() + "ms)");
                    return;
                }
                _time = 0;
                _giftList.clear();
            }
        } catch (Exception e3) {
            _log.error(e3.getLocalizedMessage(), e3);
            if (0 == 0) {
                new GetItemTimer(this, null).start();
                _log.info("載入給予獎勵物品: " + _giftList.size() + "(" + timer.get() + "ms)");
                return;
            }
            _time = 0;
            _giftList.clear();
        } catch (Throwable th) {
            if (0 == 0) {
                new GetItemTimer(this, null).start();
                _log.info("載入給予獎勵物品: " + _giftList.size() + "(" + timer.get() + "ms)");
            } else {
                _time = 0;
                _giftList.clear();
            }
            throw th;
        }
    }

    private class GetItemTimer extends TimerTask {
        private ScheduledFuture<?> _timer;

        private GetItemTimer() {
        }

        /* synthetic */ GetItemTimer(OnlineGiftSet onlineGiftSet, GetItemTimer getItemTimer) {
            this();
        }

        public void start() {
            this._timer = GeneralThreadPool.get().scheduleAtFixedRate((TimerTask) this, 60000L, 60000L);
        }

        public void run() {
            try {
                if (!OnlineGiftSet._getMap.isEmpty()) {
                    for (L1PcInstance tgpc : OnlineGiftSet._getMap.keySet()) {
                        Thread.sleep(1);
                        if (World.get().getPlayer(tgpc.getName()) == null) {
                            OnlineGiftSet._getMap.remove(tgpc);
                        } else {
                            Integer time = (Integer) OnlineGiftSet._getMap.get(tgpc);
                            if (time != null) {
                                Integer time2 = Integer.valueOf(time.intValue() - 1);
                                if (time2.intValue() <= 0) {
                                    OnlineGiftSet.getitem(tgpc);
                                    OnlineGiftSet._getMap.put(tgpc, Integer.valueOf(OnlineGiftSet._time));
                                } else {
                                    OnlineGiftSet._getMap.put(tgpc, time2);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                OnlineGiftSet._log.error("在線獎勵清單時間軸異常重啟", e);
                GeneralThreadPool.get().cancel(this._timer, false);
                new GetItemTimer().start();
            }
        }
    }
}
