package com.lineage.server.model.drop;

import com.lineage.config.ConfigRate;
import com.lineage.server.datatables.DropItemTable;
import com.lineage.server.datatables.DropLimitTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.templates.L1Drop;
import com.lineage.server.templates.L1DropMap;
import com.lineage.server.templates.L1DropMob;
import com.lineage.server.templates.L1Item;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SetDrop implements SetDropExecutor {
    private static Map<Integer, ArrayList<L1Drop>> _droplist;
    private static Map<Integer, ArrayList<L1DropMap>> _droplistX;
    private static final Log _log = LogFactory.getLog(SetDrop.class);
    private static Map<Integer, L1DropMob> _moblist;
    private static final Random _random = new Random();

    public static ArrayList<L1Drop> getDrops(int mobID) {
        return _droplist.get(Integer.valueOf(mobID));
    }

    @Override // com.lineage.server.model.drop.SetDropExecutor
    public void addDropMap(Map<Integer, ArrayList<L1Drop>> droplists) {
        if (_droplist != null) {
            _droplist.clear();
        }
        _droplist = droplists;
    }

    @Override // com.lineage.server.model.drop.SetDropExecutor
    public void addDropMapX(Map<Integer, ArrayList<L1DropMap>> droplists) {
        if (_droplistX != null) {
            _droplistX.clear();
        }
        _droplistX = droplists;
    }

    @Override // com.lineage.server.model.drop.SetDropExecutor
    public void addDropMob(Map<Integer, L1DropMob> droplists) {
        if (_moblist != null) {
            _moblist.clear();
        }
        _moblist = droplists;
    }

    @Override // com.lineage.server.model.drop.SetDropExecutor
    public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
        setDrop(npc, inventory, 0.0d);
    }

    @Override // com.lineage.server.model.drop.SetDropExecutor
    public void setDrop(L1NpcInstance npc, L1Inventory inventory, double random) {
        int mobId = npc.getNpcTemplate().get_npcId();
        ArrayList<L1DropMap> droplistX = _droplistX.get(Integer.valueOf(npc.getMapId()));
        if (droplistX != null) {
            setDrop(npc, inventory, droplistX);
        }
        if (_moblist != null) {
            setDrop(npc, inventory, _moblist);
        }
        ArrayList<L1Drop> dropList = _droplist.get(Integer.valueOf(mobId));
        if (dropList != null) {
            double droprate = ConfigRate.RATE_DROP_ITEMS;
            if (droprate <= 0.0d) {
                droprate = 0.0d;
            }
            double droprate2 = droprate + random;
            double adenarate = ConfigRate.RATE_DROP_ADENA;
            if (adenarate <= 0.0d) {
                adenarate = 0.0d;
            }
            if (droprate2 > 0.0d || adenarate > 0.0d) {
                Iterator<L1Drop> it = dropList.iterator();
                while (it.hasNext()) {
                    L1Drop drop = it.next();
                    int itemId = drop.getItemid();
                    if (adenarate != 0.0d || itemId != 40308) {
                        int randomChance = _random.nextInt(1000000) + 1;
                        double rateOfMapId = MapsTable.get().getDropRate(npc.getMapId());
                        double rateOfItem = DropItemTable.get().getDropRate(itemId);
                        if (droprate2 != 0.0d && ((double) drop.getChance()) * droprate2 * rateOfMapId * rateOfItem >= ((double) randomChance)) {
                            double amount = DropItemTable.get().getDropAmount(itemId);
                            long min = (long) (((double) drop.getMin()) * amount);
                            long itemCount = min;
                            long addCount = (((long) (((double) drop.getMax()) * amount)) - min) + 1;
                            if (addCount > 1) {
                                itemCount += (long) _random.nextInt((int) addCount);
                            }
                            if (itemId == 40308) {
                                itemCount = (long) (((double) itemCount) * adenarate);
                            }
                            if (itemCount < 0) {
                                itemCount = 0;
                            }
                            if (itemCount > 2000000000) {
                                itemCount = 2000000000;
                            }
                            if (itemCount <= 0) {
                                _log.error("NPC加入背包物件數量為0(" + mobId + " itemId: " + itemId + ")");
                            } else if (!DropLimitTable.get().checkItemId(itemId, itemCount)) {
                                additem(npc, inventory, itemId, itemCount);
                            }
                        }
                    }
                }
            }
        }
    }

    private void setDrop(L1NpcInstance npc, L1Inventory inventory, ArrayList<L1DropMap> dropListX) {
        double droprate = ConfigRate.RATE_DROP_ITEMS;
        if (droprate <= 0.0d) {
            droprate = 0.0d;
        }
        double adenarate = ConfigRate.RATE_DROP_ADENA;
        if (adenarate <= 0.0d) {
            adenarate = 0.0d;
        }
        if (droprate > 0.0d || adenarate > 0.0d) {
            Iterator<L1DropMap> it = dropListX.iterator();
            while (it.hasNext()) {
                L1DropMap drop = it.next();
                int itemId = drop.getItemid();
                if (!(adenarate == 0.0d && itemId == 40308) && npc.getMapId() == drop.get_mapid()) {
                    boolean noadd = ((((double) drop.getChance()) * droprate) * MapsTable.get().getDropRate(npc.getMapId())) * DropItemTable.get().getDropRate(itemId) < ((double) (_random.nextInt(1000000) + 1));
                    if (droprate != 0.0d && !noadd) {
                        double amount = DropItemTable.get().getDropAmount(itemId);
                        long min = (long) (((double) drop.getMin()) * amount);
                        long itemCount = min;
                        long addCount = (((long) (((double) drop.getMax()) * amount)) - min) + 1;
                        if (addCount > 1) {
                            itemCount += (long) _random.nextInt((int) addCount);
                        }
                        if (itemId == 40308) {
                            itemCount = (long) (((double) itemCount) * adenarate);
                        }
                        if (itemCount < 0) {
                            itemCount = 0;
                        }
                        if (itemCount > 2000000000) {
                            itemCount = 2000000000;
                        }
                        if (itemCount <= 0) {
                            _log.error("NPC加入背包物件數量為0(" + npc.getNpcId() + " itemId: " + itemId + ") 指定地圖");
                        } else if (!DropLimitTable.get().checkItemId(itemId, itemCount)) {
                            additem(npc, inventory, itemId, itemCount);
                        }
                    }
                }
            }
        }
    }

    private void setDrop(L1NpcInstance npc, L1Inventory inventory, Map<Integer, L1DropMob> moblist) {
        for (Integer key : moblist.keySet()) {
            if (moblist.get(key).getChance() >= _random.nextInt(1000000) + 1) {
                int min = moblist.get(key).getMin();
                int addCount = (moblist.get(key).getMax() - min) + 1;
                if (addCount > 1) {
                    min += _random.nextInt(addCount);
                }
                if (min <= 0) {
                    _log.error("NPC加入背包物件數量為0(" + npc.getNpcId() + " itemId: " + key + ") 全怪掉落");
                } else if (!DropLimitTable.get().checkItemId(key.intValue(), (long) min)) {
                    additem(npc, inventory, key.intValue(), (long) min);
                }
            }
        }
    }

    private void additem(L1NpcInstance npc, L1Inventory inventory, int itemId, long itemCount) {
        try {
            L1Item tmp = ItemTable.get().getTemplate(itemId);
            if (tmp == null) {
                _log.error("掉落物品設置錯誤(無這編號物品): " + itemId);
            } else if (tmp.isStackable()) {
                L1ItemInstance item = ItemTable.get().createItem(itemId);
                if (item != null) {
                    item.setCount(itemCount);
                    inventory.storeItem(item);
                }
            } else {
                for (int i = 0; ((long) i) < itemCount; i++) {
                    L1ItemInstance item2 = ItemTable.get().createItem(itemId);
                    if (item2 != null) {
                        item2.setCount(1);
                        inventory.storeItem(item2);
                    }
                }
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
