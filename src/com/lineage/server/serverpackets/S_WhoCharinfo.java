package com.lineage.server.serverpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class S_WhoCharinfo extends ServerBasePacket {
    private byte[] _byte = null;

    public S_WhoCharinfo(L1Character cha, ClientExecutor client) {
        String lawfulness = "";
        int lawful = cha.getLawful();
        if (lawful < 0) {
            lawfulness = "($1503)";
        } else if (lawful >= 0 && lawful < 500) {
            lawfulness = "($1502)";
        } else if (lawful >= 500) {
            lawfulness = "($1501)";
        }
        writeC(14);
        writeH(166);
        writeC(1);
        String clan = "";
        String title = !cha.getTitle().equalsIgnoreCase("") ? String.valueOf(cha.getTitle()) + " " : "";
        String name = "";
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            name = pc.getName();
            if (ConfigOther.Xljnet) {
                L1PcInstance xljnetpc = client.getActiveChar();
                List<L1ItemInstance> items = pc.getInventory().getItems();
                List<L1ItemInstance> itemsx = new CopyOnWriteArrayList<>();
                for (L1ItemInstance item : items) {
                    if (xljnetpc.isGm()) {
                        itemsx.add(item);
                    } else if (item.isEquipped()) {
                        itemsx.add(item);
                    }
                }
                xljnetpc.sendPackets(new S_WhoXljnet(xljnetpc, 0, itemsx));
                xljnetpc.sendPackets(new S_ServerMessage("玩家名稱：" + pc.getName()));
                xljnetpc.sendPackets(new S_ServerMessage("等級：" + pc.getLevel() + " 經驗：" + ExpTable.getExpPercentage(pc.getLevel(), (long) ((int) pc.getExp())) + " %"));
                xljnetpc.sendPackets(new S_ServerMessage("血量：" + pc.getCurrentHp() + "/" + pc.getMaxHp() + " 魔量：" + pc.getCurrentMp() + "/" + ((int) pc.getMaxMp())));
                xljnetpc.sendPackets(new S_ServerMessage("防禦：" + pc.getAc() + " 迴避：" + pc.getEr()));
                xljnetpc.sendPackets(new S_ServerMessage("力量：" + ((int) pc.getStr()) + " 智力：" + ((int) pc.getInt())));
                xljnetpc.sendPackets(new S_ServerMessage("敏捷：" + ((int) pc.getDex()) + " 精神：" + ((int) pc.getWis())));
                xljnetpc.sendPackets(new S_ServerMessage("體質：" + ((int) pc.getCon()) + " 魅力：" + ((int) pc.getCha())));
                xljnetpc.sendPackets(new S_ServerMessage("抗地：" + pc.getEarth() + "% 抗火：" + pc.getFire() + "%"));
                xljnetpc.sendPackets(new S_ServerMessage("抗水：" + pc.getWater() + "% 抗風：" + pc.getWind() + "%"));
                xljnetpc.sendPackets(new S_ServerMessage("抗魔：" + pc.getMr() + "% 魔攻：" + pc.getSp()));
            }
            if (pc.getClanid() > 0) {
                clan = "[" + pc.getClanname() + "]";
            }
        }
        writeS(String.valueOf(title) + name + " " + lawfulness + " " + clan);
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = getBytes();
        }
        return this._byte;
    }

    @Override // com.lineage.server.serverpackets.ServerBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
