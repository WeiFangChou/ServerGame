package com.lineage.data.item_etcitem.doll;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.DollPowerTable;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.NpcTable;
import com.lineage.server.model.Instance.L1DollInstance;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1War;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Doll;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.world.WorldWar;
import java.util.Iterator;

public class Magic_Doll extends ItemExecutor {
    private Magic_Doll() {
    }

    public static ItemExecutor get() {
        return new Magic_Doll();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        useMagicDoll(pc, item.getItemId(), item.getId());
    }

    private void useMagicDoll(L1PcInstance pc, int itemId, int itemObjectId) throws Exception {
        if (pc.getDoll(itemObjectId) != null) {
            pc.getDoll(itemObjectId).deleteDoll();
        } else if (pc.getDolls().size() >= ConfigAlt.MAX_DOLL_COUNT) {
            pc.sendPackets(new S_ServerMessage(319));
        } else if (pc.getDolls().size() >= 1 && pc.getDollhole() < 1) {
            pc.sendPackets(new S_ServerMessage("必須先擴充後，才能召喚第二隻娃娃。"));
        } else if (pc.getDolls().size() < 2 || pc.getDollhole() >= 2) {
            if (!pc.getDolls().isEmpty()) {
                for (L1DollInstance doll : pc.getDolls().values()) {
                    if (pc.getInventory().getItem(doll.getItemObjId()).getItemId() == itemId) {
                        pc.sendPackets(new S_ServerMessage("不能攜帶相同的娃娃"));
                        return;
                    }
                }
            }
            if (!ConfigOther.WAR_DOLL && pc.getClan() != null) {
                boolean inWar = false;
                if (pc.getClan().getCastleId() == 0) {
                    Iterator<L1War> iter = WorldWar.get().getWarList().iterator();
                    while (true) {
                        if (iter.hasNext()) {
                            if (iter.next().checkClanInWar(pc.getClan().getClanName())) {
                                inWar = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } else if (ServerWarExecutor.get().isNowWar(pc.getClan().getCastleId())) {
                    inWar = true;
                }
                if (inWar) {
                    pc.sendPackets(new S_ServerMessage(1531));
                    return;
                }
            }
            boolean iserror = false;
            L1Doll type = DollPowerTable.get().get_type(itemId);
            if (type != null) {
                if (type.get_need() != null) {
                    int[] itemids = type.get_need();
                    int[] counts = type.get_counts();
                    for (int i = 0; i < itemids.length; i++) {
                        if (!pc.getInventory().checkItem(itemids[i], (long) counts[i])) {
                            pc.sendPackets(new S_ServerMessage(337, ItemTable.get().getTemplate(itemids[i]).getName()));
                            iserror = true;
                        }
                    }
                    if (!iserror) {
                        for (int i2 = 0; i2 < itemids.length; i2++) {
                            pc.getInventory().consumeItem(itemids[i2], (long) counts[i2]);
                        }
                    }
                }
                if (!iserror) {
                    new L1DollInstance(NpcTable.get().getTemplate(71082), pc, itemObjectId, type);
                }
            }
        } else {
            pc.sendPackets(new S_ServerMessage("必須先擴充後，才能召喚第三隻娃娃。"));
        }
    }
}
