package com.lineage.server.model.drop;

import com.lineage.config.ConfigAlt;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.datatables.ItemBsgTable;
import com.lineage.server.datatables.ItemMsgTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Inventory;
import com.lineage.server.serverpackets.S_PacketBoxGree1;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.utils.ListMapUtil;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import william.L1WilliamSystemMessage;

public class DropShare implements DropShareExecutor {
    private static final byte[] HEADING_TABLE_X;
    private static final byte[] HEADING_TABLE_Y;

    static {
        byte[] bArr = new byte[8];
        bArr[1] = 1;
        bArr[2] = 1;
        bArr[3] = 1;
        bArr[5] = -1;
        bArr[6] = -1;
        bArr[7] = -1;
        HEADING_TABLE_X = bArr;
        byte[] bArr2 = new byte[8];
        bArr2[0] = -1;
        bArr2[1] = -1;
        bArr2[3] = 1;
        bArr2[4] = 1;
        bArr2[5] = 1;
        bArr2[7] = -1;
        HEADING_TABLE_Y = bArr2;
    }

    @Override // com.lineage.server.model.drop.DropShareExecutor
    public void dropShare(L1NpcInstance npc, ArrayList<L1Character> acquisitorList, ArrayList<Integer> hateList) {
        GeneralThreadPool.get().schedule(new DropShareR(this, npc, acquisitorList, hateList, null), 0);
    }

    private class DropShareR implements Runnable {
        final ArrayList<L1Character> _acquisitorList;
        final ArrayList<Integer> _hateList;
        final L1NpcInstance _npc;

        /* synthetic */ DropShareR(DropShare dropShare, L1NpcInstance l1NpcInstance, ArrayList arrayList, ArrayList arrayList2, DropShareR dropShareR) {
            this(l1NpcInstance, arrayList, arrayList2);
        }

        private DropShareR(L1NpcInstance npc, ArrayList<L1Character> acquisitorList, ArrayList<Integer> hateList) {
            this._npc = npc;
            this._acquisitorList = acquisitorList;
            this._hateList = hateList;
        }

        public void run() {
            byte b;
            byte b2;
            try {
                L1Inventory inventory = this._npc.getInventory();
                if (inventory != null) {
                    if (inventory.getSize() <= 0) {
                        ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                        ListMapUtil.clear((ArrayList<?>) this._hateList);
                    } else if (this._acquisitorList.size() != this._hateList.size()) {
                        ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                        ListMapUtil.clear((ArrayList<?>) this._hateList);
                    } else {
                        int totalHate = 0;
                        for (int i = this._hateList.size() - 1; i >= 0; i--) {
                            L1Character acquisitor = this._acquisitorList.get(i);
                            if (ConfigAlt.AUTO_LOOT == 2 && ((acquisitor instanceof L1SummonInstance) || (acquisitor instanceof L1PetInstance))) {
                                this._acquisitorList.remove(i);
                                this._hateList.remove(i);
                            } else if (acquisitor == null || acquisitor.getMapId() != this._npc.getMapId() || acquisitor.getLocation().getTileLineDistance(this._npc.getLocation()) > ConfigAlt.LOOTING_RANGE) {
                                this._acquisitorList.remove(i);
                                this._hateList.remove(i);
                            } else {
                                totalHate += this._hateList.get(i).intValue();
                            }
                        }
                        L1Inventory targetInventory = null;
                        Random random = new Random();
                        List<L1ItemInstance> list = inventory.getItems();
                        if (list.isEmpty()) {
                            ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                            ListMapUtil.clear((ArrayList<?>) this._hateList);
                        } else if (list.size() <= 0) {
                            ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                            ListMapUtil.clear((ArrayList<?>) this._hateList);
                        } else {
                            for (L1ItemInstance item : list) {
                                int itemId = item.getItemId();
                                if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) {
                                    item.setNowLighting(false);
                                }
                                if ((ConfigAlt.AUTO_LOOT != 0 || itemId == 40308) && totalHate > 0) {
                                    int randomInt = random.nextInt(totalHate);
                                    int chanceHate = 0;
                                    int j = this._hateList.size() - 1;
                                    while (true) {
                                        if (j < 0) {

                                            break;
                                        }
                                        Thread.sleep(1);
                                        chanceHate += this._hateList.get(j).intValue();
                                        if (chanceHate > randomInt) {
                                            L1Character acquisitor2 = this._acquisitorList.get(j);
                                            if (acquisitor2.getInventory().checkAddItem(item, item.getCount()) == 0) {
                                                targetInventory = acquisitor2.getInventory();
                                                if (acquisitor2 instanceof L1PcInstance) {
                                                    L1PcInstance player = (L1PcInstance) acquisitor2;
                                                    if (player.isInParty()) {
                                                        if (player.setShowDrop() == 1) {
                                                            player.sendPackets(new S_ServerMessage(143, this._npc.getNameId(), item.getLogName()));
                                                        }
                                                        Object[] pcs = player.getParty().partyUsers().values().toArray();
                                                        if (pcs.length <= 0) {
                                                            ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                                                            ListMapUtil.clear((ArrayList<?>) this._hateList);
                                                            return;
                                                        }
                                                        int length = pcs.length;
                                                        for (int i2 = 0; i2 < length; i2++) {
                                                            Object obj = pcs[i2];
                                                            if (obj instanceof L1PcInstance) {
                                                                L1PcInstance tgpc = (L1PcInstance) obj;
                                                                if (tgpc.setShowDrop() == 0) {
                                                                    tgpc.sendPackets(new S_ServerMessage(813, this._npc.getNameId(), item.getLogName(), player.getName()));
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        player.sendPackets(new S_ServerMessage(143, this._npc.getNameId(), item.getLogName()));
                                                    }
                                                    if (ItemMsgTable.get().contains(item.getItemId())) {
                                                        World.get().broadcastPacketToAll(new S_ServerMessage(L1WilliamSystemMessage.ShowMessage(1) + "【 " + player.getName() + " 】" + L1WilliamSystemMessage.ShowMessage(7) + "【 " + this._npc.getNameId() + " 】" + L1WilliamSystemMessage.ShowMessage(8) + "【 " + item.getName() + "(" + item.getCount() + ")】。"));
                                                    }
                                                    if (ItemBsgTable.get().contains(item.getItemId())) {
                                                        World.get().broadcastPacketToAll(new S_PacketBoxGree1(2, L1WilliamSystemMessage.ShowMessage(1) + "【 " + player.getName() + " 】" + L1WilliamSystemMessage.ShowMessage(7) + "【 " + this._npc.getNameId() + " 】" + L1WilliamSystemMessage.ShowMessage(8) + "【 " + item.getName() + "(" + item.getCount() + ")】。"));
                                                    }
                                                    if (ItemMsgTable.get().contains(itemId)) {
                                                        WriteLogTxt.Recording("打到寶物紀錄", "玩家:【" + player.getName() + "】 " + "地點:【 " + player.getLocation() + "】" + "【 " + item.getName() + "(" + item.getCount() + ")】" + "道具ID：【" + item.getId() + "】");
                                                    }
                                                    if (ItemBsgTable.get().contains(itemId)) {
                                                        WriteLogTxt.Recording("打到寶物中央紀錄", "玩家:【" + player.getName() + "】 " + "地點:【 " + player.getLocation() + "】" + "【 " + item.getName() + "(" + item.getCount() + ")】" + "道具ID：【" + item.getId() + "】");
                                                    }
                                                } else {
                                                    continue;
                                                }
                                            } else {
                                                item.set_showId(this._npc.get_showId());
                                                targetInventory = World.get().getInventory(acquisitor2.getX(), acquisitor2.getY(), acquisitor2.getMapId());
                                            }
                                        } else {
                                            j--;
                                        }
                                    }
                                } else {
                                    List<Integer> dirList = new ArrayList<>();
                                    for (int j2 = 0; j2 < 8; j2++) {
                                        dirList.add(Integer.valueOf(j2));
                                    }
                                    while (true) {
                                        if (dirList.size() == 0) {
                                            b = 0;
                                            b2 = 0;
                                            break;
                                        }
                                        int randomInt2 = random.nextInt(dirList.size());
                                        int dir = dirList.get(randomInt2).intValue();
                                        dirList.remove(randomInt2);
                                        b = DropShare.HEADING_TABLE_X[dir];
                                        b2 = DropShare.HEADING_TABLE_Y[dir];
                                        Thread.sleep(1);
                                        if (this._npc.getMap().isPassable(this._npc.getX(), this._npc.getY(), dir, null)) {
                                            break;
                                        }
                                    }
                                    item.set_showId(this._npc.get_showId());
                                    targetInventory = World.get().getInventory(this._npc.getX() + b, this._npc.getY() + b2, this._npc.getMapId());
                                    ListMapUtil.clear(dirList);
                                }
                                inventory.tradeItem(item, item.getCount(), targetInventory);
                            }
                            ListMapUtil.clear(list);
                            ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                            ListMapUtil.clear((ArrayList<?>) this._hateList);
                        }
                    }
                }
            } catch (Exception ignored) {
            } finally {
                ListMapUtil.clear((ArrayList<?>) this._acquisitorList);
                ListMapUtil.clear((ArrayList<?>) this._hateList);
            }
        }
    }
}
