package com.lineage.data.npc.xljnet;

import com.lineage.config.ConfigPoly;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.Item_Poly_Table;
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

public class Npc_PolyCombind extends NpcExecutor {
    private int[] Poly1;
    private int[] Poly2;
    private int[] Poly3;
    private int[] Poly4;
    private int[] Poly5;
    private int[] Poly6;
    Random _random;

    public Npc_PolyCombind() {
        this.Poly1 = ConfigPoly.Poly_LIST_1;
        this.Poly2 = ConfigPoly.Poly_LIST_2;
        this.Poly3 = ConfigPoly.Poly_LIST_3;
        this.Poly4 = ConfigPoly.Poly_LIST_4;
        this.Poly5 = ConfigPoly.Poly_LIST_5;
        this.Poly6 = ConfigPoly.Poly_LIST_6;
        this._random = new Random();
    }

    public static NpcExecutor get() {
        return new Npc_PolyCombind();
    }

    public int type() {
        return 3;
    }

    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "Polycombind1"));
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
                    chance = ConfigPoly.CONSUME2;
                    needcount = ConfigPoly.NeedCount2;
                    olddolls = this.Poly1;
                    newdoll = this.Poly2[this._random.nextInt(this.Poly2.length)];
                }
                break;
            case 66:
                if (cmd.equals("B")) {
                    chance = ConfigPoly.CONSUME3;
                    needcount = ConfigPoly.NeedCount3;
                    olddolls = this.Poly1;
                    newdoll = this.Poly2[this._random.nextInt(this.Poly2.length)];
                }
                break;
            case 67:
                if (cmd.equals("C")) {
                    chance = ConfigPoly.CONSUME4;
                    needcount = ConfigPoly.NeedCount4;
                    olddolls = this.Poly1;
                    newdoll = this.Poly2[this._random.nextInt(this.Poly2.length)];
                }
                break;
            case 68:
                if (cmd.equals("D")) {
                    chance = ConfigPoly.CONSUME2;
                    needcount = ConfigPoly.NeedCount2;
                    olddolls = this.Poly2;
                    newdoll = this.Poly3[this._random.nextInt(this.Poly3.length)];
                }
                break;
            case 69:
                if (cmd.equals("E")) {
                    chance = ConfigPoly.CONSUME3;
                    needcount = ConfigPoly.NeedCount3;
                    olddolls = this.Poly2;
                    newdoll = this.Poly3[this._random.nextInt(this.Poly3.length)];
                }
                break;
            case 70:
                if (cmd.equals("F")) {
                    chance = ConfigPoly.CONSUME4;
                    needcount = ConfigPoly.NeedCount4;
                    olddolls = this.Poly2;
                    newdoll = this.Poly3[this._random.nextInt(this.Poly3.length)];
                }
                break;
            case 71:
                if (cmd.equals("G")) {
                    chance = ConfigPoly.CONSUME2;
                    needcount = ConfigPoly.NeedCount2;
                    olddolls = this.Poly3;
                    newdoll = this.Poly4[this._random.nextInt(this.Poly4.length)];
                }
                break;
            case 72:
                if (cmd.equals("H")) {
                    chance = ConfigPoly.CONSUME3;
                    needcount = ConfigPoly.NeedCount3;
                    olddolls = this.Poly3;
                    newdoll = this.Poly4[this._random.nextInt(this.Poly4.length)];
                }
                break;
            case 73:
                if (cmd.equals("I")) {
                    chance = ConfigPoly.CONSUME4;
                    needcount = ConfigPoly.NeedCount4;
                    olddolls = this.Poly3;
                    newdoll = this.Poly4[this._random.nextInt(this.Poly4.length)];
                    materials = ConfigPoly.PolyLIST1;
                }
                break;
            case 74:
                if (cmd.equals("J")) {
                    chance = ConfigPoly.CONSUME2;
                    needcount = ConfigPoly.NeedCount2;
                    olddolls = this.Poly4;
                    newdoll = this.Poly5[this._random.nextInt(this.Poly5.length)];
                }
                break;
            case 75:
                if (cmd.equals("K")) {
                    chance = ConfigPoly.CONSUME3;
                    needcount = ConfigPoly.NeedCount3;
                    olddolls = this.Poly4;
                    newdoll = this.Poly5[this._random.nextInt(this.Poly5.length)];
                }
                break;
            case 76:
                if (cmd.equals("L")) {
                    chance = ConfigPoly.CONSUME4;
                    needcount = ConfigPoly.NeedCount4;
                    olddolls = this.Poly4;
                    newdoll = this.Poly5[this._random.nextInt(this.Poly5.length)];
                    materials = ConfigPoly.PolyLIST2;
                }
                break;
            case 77:
                if (cmd.equals("M")) {
                    chance = ConfigPoly.CONSUME2;
                    needcount = ConfigPoly.NeedCount2;
                    olddolls = this.Poly5;
                    newdoll = this.Poly6[this._random.nextInt(this.Poly6.length)];
                }
                break;
            case 78:
                if (cmd.equals("N")) {
                    chance = ConfigPoly.CONSUME4;
                    needcount = ConfigPoly.NeedCount4;
                    olddolls = this.Poly5;
                    newdoll = this.Poly6[this._random.nextInt(this.Poly6.length)];
                    materials = ConfigPoly.PolyLIST3;
                }
                break;
            case 79:
                if (cmd.equals("O")) {
                    chance = ConfigPoly.CONSUME3;
                    needcount = ConfigPoly.NeedCount3;
                    olddolls = this.Poly5;
                    newdoll = this.Poly6[this._random.nextInt(this.Poly6.length)];
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
                    pc.sendPackets(new S_SystemMessage("合成『變身卡片』，成功。"));
                    item = ItemTable.get().createItem(newdoll);
                    item.setIdentified(true);
                    pc.getInventory().storeItem(item);
                    pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()));
                    if (Item_Poly_Table.get().contains(item.getItemId())) {
                        pc.sendPackets(new S_NpcChat(npc, "\\f2恭喜玩家：【" + pc.getName() + "】合成『變身卡片』【" + item.getLogName() + "】成功。"));
                        World.get().broadcastPacketToAll(new S_ServerMessage("\\f2恭喜玩家：【" + pc.getName() + "】合成『變身卡片』【" + item.getLogName() + "】成功。"));
                        pc.sendPackets(new S_PacketBoxGree("\\f2恭喜玩家：【" + pc.getName() + "】合成『變身卡片』【" + item.getLogName() + "】成功。"));
                    }
                } else {
                    pc.sendPackets(new S_SystemMessage("合成『變身卡片』，失敗。"));
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
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "Polycombind7"));
        }

    }
}
