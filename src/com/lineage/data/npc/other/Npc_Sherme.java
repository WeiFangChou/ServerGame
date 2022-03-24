package com.lineage.data.npc.other;

import com.lineage.data.cmd.CreateNewItem;
import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ItemCount;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Sherme extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Sherme.class);

    private Npc_Sherme() {
    }

    public static NpcExecutor get() {
        return new Npc_Sherme();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "sherme2"));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        boolean isCloseList = false;
        if (cmd.equalsIgnoreCase("a")) {
            int[] items = {42514, L1ItemId.ADENA};
            int[] counts = {1, 100000};
            int[] gitems = {42518};
            int[] gcounts = {1};
            long xcount = CreateNewItem.checkNewItem(pc, items, counts);
            if (xcount == 1) {
                CreateNewItem.createNewItem(pc, items, counts, gitems, 1, gcounts);
                isCloseList = true;
            } else if (xcount > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount, "a1"));
            } else if (xcount < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("a1")) {
            int[] items2 = {42514, L1ItemId.ADENA};
            int[] counts2 = {1, 100000};
            int[] gitems2 = {42518};
            int[] gcounts2 = {1};
            if (CreateNewItem.checkNewItem(pc, items2, counts2) >= amount) {
                CreateNewItem.createNewItem(pc, items2, counts2, gitems2, amount, gcounts2);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("b")) {
            int[] items3 = {42515, L1ItemId.ADENA};
            int[] counts3 = {1, 100000};
            int[] gitems3 = {42519};
            int[] gcounts3 = {1};
            long xcount2 = CreateNewItem.checkNewItem(pc, items3, counts3);
            if (xcount2 == 1) {
                CreateNewItem.createNewItem(pc, items3, counts3, gitems3, 1, gcounts3);
                isCloseList = true;
            } else if (xcount2 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount2, "b1"));
            } else if (xcount2 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("b1")) {
            int[] items4 = {42515, L1ItemId.ADENA};
            int[] counts4 = {1, 100000};
            int[] gitems4 = {42519};
            int[] gcounts4 = {1};
            if (CreateNewItem.checkNewItem(pc, items4, counts4) >= amount) {
                CreateNewItem.createNewItem(pc, items4, counts4, gitems4, amount, gcounts4);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("c")) {
            int[] items5 = {42517, L1ItemId.ADENA};
            int[] counts5 = {1, 100000};
            int[] gitems5 = {42521};
            int[] gcounts5 = {1};
            long xcount3 = CreateNewItem.checkNewItem(pc, items5, counts5);
            if (xcount3 == 1) {
                CreateNewItem.createNewItem(pc, items5, counts5, gitems5, 1, gcounts5);
                isCloseList = true;
            } else if (xcount3 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount3, "c1"));
            } else if (xcount3 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("c1")) {
            int[] items6 = {42517, L1ItemId.ADENA};
            int[] counts6 = {1, 100000};
            int[] gitems6 = {42521};
            int[] gcounts6 = {1};
            if (CreateNewItem.checkNewItem(pc, items6, counts6) >= amount) {
                CreateNewItem.createNewItem(pc, items6, counts6, gitems6, amount, gcounts6);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("d")) {
            int[] items7 = {42516, L1ItemId.ADENA};
            int[] counts7 = {1, 100000};
            int[] gitems7 = {42520};
            int[] gcounts7 = {1};
            long xcount4 = CreateNewItem.checkNewItem(pc, items7, counts7);
            if (xcount4 == 1) {
                CreateNewItem.createNewItem(pc, items7, counts7, gitems7, 1, gcounts7);
                isCloseList = true;
            } else if (xcount4 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount4, "d1"));
            } else if (xcount4 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("d1")) {
            int[] items8 = {42516, L1ItemId.ADENA};
            int[] counts8 = {1, 100000};
            int[] gitems8 = {42520};
            int[] gcounts8 = {1};
            if (CreateNewItem.checkNewItem(pc, items8, counts8) >= amount) {
                CreateNewItem.createNewItem(pc, items8, counts8, gitems8, amount, gcounts8);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("e")) {
            int[] items9 = {42525, 42526, L1ItemId.ADENA};
            int[] counts9 = {1, 1, 200000};
            int[] gitems9 = {42522};
            int[] gcounts9 = {1};
            long xcount5 = CreateNewItem.checkNewItem(pc, items9, counts9);
            if (xcount5 == 1) {
                CreateNewItem.createNewItem(pc, items9, counts9, gitems9, 1, gcounts9);
                isCloseList = true;
            } else if (xcount5 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount5, "e1"));
            } else if (xcount5 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("e1")) {
            int[] items10 = {42525, 42526, L1ItemId.ADENA};
            int[] counts10 = {1, 1, 200000};
            int[] gitems10 = {42522};
            int[] gcounts10 = {1};
            if (CreateNewItem.checkNewItem(pc, items10, counts10) >= amount) {
                CreateNewItem.createNewItem(pc, items10, counts10, gitems10, amount, gcounts10);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("f")) {
            int[] items11 = {42522, 42527, L1ItemId.ADENA};
            int[] counts11 = {1, 1, 200000};
            int[] gitems11 = {42523};
            int[] gcounts11 = {1};
            long xcount6 = CreateNewItem.checkNewItem(pc, items11, counts11);
            if (xcount6 == 1) {
                CreateNewItem.createNewItem(pc, items11, counts11, gitems11, 1, gcounts11);
                isCloseList = true;
            } else if (xcount6 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount6, "f1"));
            } else if (xcount6 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("f1")) {
            int[] items12 = {42522, 42527, L1ItemId.ADENA};
            int[] counts12 = {1, 1, 200000};
            int[] gitems12 = {42523};
            int[] gcounts12 = {1};
            if (CreateNewItem.checkNewItem(pc, items12, counts12) >= amount) {
                CreateNewItem.createNewItem(pc, items12, counts12, gitems12, amount, gcounts12);
            }
            isCloseList = true;
        } else if (cmd.equalsIgnoreCase("g")) {
            int[] items13 = {42523, 42528, L1ItemId.ADENA};
            int[] counts13 = {1, 1, 200000};
            int[] gitems13 = {42524};
            int[] gcounts13 = {1};
            long xcount7 = CreateNewItem.checkNewItem(pc, items13, counts13);
            if (xcount7 == 1) {
                CreateNewItem.createNewItem(pc, items13, counts13, gitems13, 1, gcounts13);
                isCloseList = true;
            } else if (xcount7 > 1) {
                pc.sendPackets(new S_ItemCount(npc.getId(), (int) xcount7, "g1"));
            } else if (xcount7 < 1) {
                isCloseList = true;
            }
        } else if (cmd.equalsIgnoreCase("g1")) {
            int[] items14 = {42523, 42528, L1ItemId.ADENA};
            int[] counts14 = {1, 1, 200000};
            int[] gitems14 = {42524};
            int[] gcounts14 = {1};
            if (CreateNewItem.checkNewItem(pc, items14, counts14) >= amount) {
                CreateNewItem.createNewItem(pc, items14, counts14, gitems14, amount, gcounts14);
            }
            isCloseList = true;
        }
        if (isCloseList) {
            pc.sendPackets(new S_CloseList(pc.getId()));
        }
    }
}
