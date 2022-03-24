package com.lineage.data.npc.xljnet;

import com.lineage.config.ConfigDoll;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.Item_Dolls_Table;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_PacketBoxGree;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1Item;
import com.lineage.server.world.World;
import java.util.ArrayList;
import java.util.Random;

public class Npc_DollCombind extends NpcExecutor {
    private int[] doll1;
    private int[] doll2;
    private int[] doll3;
    private int[] doll4;
    private int[] doll5;
    private int[] doll6;
    Random _random;

    public Npc_DollCombind() {
        this.doll1 = ConfigDoll.DOLL_LIST_1;
        this.doll2 = ConfigDoll.DOLL_LIST_2;
        this.doll3 = ConfigDoll.DOLL_LIST_3;
        this.doll4 = ConfigDoll.DOLL_LIST_4;
        this.doll5 = ConfigDoll.DOLL_LIST_5;
        this.doll6 = ConfigDoll.DOLL_LIST_6;
        this._random = new Random();
    }

    public static NpcExecutor get() {
        return new Npc_DollCombind();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dollcombind1"));
    }

    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) throws Exception {
        boolean enough = false;
        ArrayList<Integer> cousumedolls = new ArrayList();
        int chance = 0;
        int needcount = 0;
        int[] olddolls = null;
        int newdoll = 0;
        int materials = 0;
        switch(cmd.hashCode()) {
            case 65:
                if (cmd.equals("A")) {
                    chance = ConfigDoll.CONSUME2;
                    needcount = ConfigDoll.NeedCount2;
                    olddolls = this.doll1;
                    newdoll = this.doll2[this._random.nextInt(this.doll2.length)];
                }
                break;
            case 66:
                if (cmd.equals("B")) {
                    chance = ConfigDoll.CONSUME3;
                    needcount = ConfigDoll.NeedCount3;
                    olddolls = this.doll1;
                    newdoll = this.doll2[this._random.nextInt(this.doll2.length)];
                }
                break;
            case 67:
                if (cmd.equals("C")) {
                    chance = ConfigDoll.CONSUME4;
                    needcount = ConfigDoll.NeedCount4;
                    olddolls = this.doll1;
                    newdoll = this.doll2[this._random.nextInt(this.doll2.length)];
                }
                break;
            case 68:
                if (cmd.equals("D")) {
                    chance = ConfigDoll.CONSUME2;
                    needcount = ConfigDoll.NeedCount2;
                    olddolls = this.doll2;
                    newdoll = this.doll3[this._random.nextInt(this.doll3.length)];
                }
                break;
            case 69:
                if (cmd.equals("E")) {
                    chance = ConfigDoll.CONSUME3;
                    needcount = ConfigDoll.NeedCount3;
                    olddolls = this.doll2;
                    newdoll = this.doll3[this._random.nextInt(this.doll3.length)];
                }
                break;
            case 70:
                if (cmd.equals("F")) {
                    chance = ConfigDoll.CONSUME4;
                    needcount = ConfigDoll.NeedCount4;
                    olddolls = this.doll2;
                    newdoll = this.doll3[this._random.nextInt(this.doll3.length)];
                }
                break;
            case 71:
                if (cmd.equals("G")) {
                    chance = ConfigDoll.CONSUME2;
                    needcount = ConfigDoll.NeedCount2;
                    olddolls = this.doll3;
                    newdoll = this.doll4[this._random.nextInt(this.doll4.length)];
                }
                break;
            case 72:
                if (cmd.equals("H")) {
                    chance = ConfigDoll.CONSUME3;
                    needcount = ConfigDoll.NeedCount3;
                    olddolls = this.doll3;
                    newdoll = this.doll4[this._random.nextInt(this.doll4.length)];
                }
                break;
            case 73:
                if (cmd.equals("I")) {
                    chance = ConfigDoll.CONSUME4;
                    needcount = ConfigDoll.NeedCount4;
                    olddolls = this.doll3;
                    newdoll = this.doll4[this._random.nextInt(this.doll4.length)];
                    materials = ConfigDoll.DOLLLIST1;
                }
                break;
            case 74:
                if (cmd.equals("J")) {
                    chance = ConfigDoll.CONSUME2;
                    needcount = ConfigDoll.NeedCount2;
                    olddolls = this.doll4;
                    newdoll = this.doll5[this._random.nextInt(this.doll5.length)];
                }
                break;
            case 75:
                if (cmd.equals("K")) {
                    chance = ConfigDoll.CONSUME3;
                    needcount = ConfigDoll.NeedCount3;
                    olddolls = this.doll4;
                    newdoll = this.doll5[this._random.nextInt(this.doll5.length)];
                }
                break;
            case 76:
                if (cmd.equals("L")) {
                    chance = ConfigDoll.CONSUME4;
                    needcount = ConfigDoll.NeedCount4;
                    olddolls = this.doll4;
                    newdoll = this.doll5[this._random.nextInt(this.doll5.length)];
                    materials = ConfigDoll.DOLLLIST2;
                }
                break;
            case 77:
                if (cmd.equals("M")) {
                    chance = ConfigDoll.CONSUME2;
                    needcount = ConfigDoll.NeedCount2;
                    olddolls = this.doll5;
                    newdoll = this.doll6[this._random.nextInt(this.doll6.length)];
                }
                break;
            case 78:
                if (cmd.equals("N")) {
                    chance = ConfigDoll.CONSUME4;
                    needcount = ConfigDoll.NeedCount4;
                    olddolls = this.doll5;
                    newdoll = this.doll6[this._random.nextInt(this.doll6.length)];
                    materials = ConfigDoll.DOLLLIST3;
                }
                break;
            case 79:
                if (cmd.equals("O")) {
                    chance = ConfigDoll.CONSUME3;
                    needcount = ConfigDoll.NeedCount3;
                    olddolls = this.doll5;
                    newdoll = this.doll6[this._random.nextInt(this.doll6.length)];
                }
        }

        for(int i = 0; i < olddolls.length; ++i) {
            L1ItemInstance[] dolls = pc.getInventory().findItemsId(olddolls[i]);
            if (dolls != null) {
                for(int c = 0; c < dolls.length; ++c) {
                    int itemid = dolls[c].getItemId();
                    cousumedolls.add(itemid);
                    if (cousumedolls.size() == needcount) {
                        break;
                    }
                }
            }

            if (cousumedolls.size() == needcount) {
                break;
            }
        }

        if (cousumedolls.size() == needcount) {
            enough = true;
            if (pc.getInventory().consumeItemsIdArray(cousumedolls)) {
                L1ItemInstance item;
                if (this._random.nextInt(100) < chance) {
                    pc.sendPackets(new S_SystemMessage("合成『魔法娃娃卡片』，成功。"));
                    item = ItemTable.get().createItem(newdoll);
                    item.setIdentified(true);
                    pc.getInventory().storeItem(item);
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
                    if (Item_Dolls_Table.get().contains(item.getItemId())) {
                        pc.sendPackets(new S_NpcChat(npc, "\\f2恭喜玩家：【" + pc.getName() + "】合成『魔法娃娃卡片』【" + item.getLogName() + "】成功。"));
                        World.get().broadcastPacketToAll(new S_ServerMessage("\\f2恭喜玩家：【" + pc.getName() + "】合成『魔法娃娃卡片』【" + item.getLogName() + "】成功。"));
                        pc.sendPackets(new S_PacketBoxGree("\\f2恭喜玩家：【" + pc.getName() + "】合成『魔法娃娃卡片』【" + item.getLogName() + "】成功。"));
                    }
                } else {
                    pc.sendPackets(new S_SystemMessage("合成『魔法娃娃卡片』，失敗。"));
                    item = ItemTable.get().createItem((Integer)cousumedolls.get(this._random.nextInt(cousumedolls.size())));
                    item.setIdentified(true);
                    pc.getInventory().storeItem(item);
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
                    L1Item temp = ItemTable.get().getTemplate(materials);
                    pc.getInventory().storeItem(materials, 1L);
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), temp.getName()));
                }
            }
        }

        if (!enough) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "dollcombind7"));
        }

    }
}
