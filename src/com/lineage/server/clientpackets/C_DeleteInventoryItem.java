package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.WriteLogTxt;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_DeleteInventoryItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_DeleteInventoryItem.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            int deleteCount = 0;
            L1ItemInstance item = pc.getInventory().getItem(readD());
            if (item != null) {
                if (item.getCount() <= 0) {
                    over();
                } else if (pc.isGm() || !item.getItem().isCantDelete()) {
                    Object[] petlist = pc.getPetList().values().toArray();
                    for (Object petObject : petlist) {
                        if ((petObject instanceof L1PetInstance) && item.getId() == ((L1PetInstance) petObject).getItemObjId()) {
                            pc.sendPackets(new S_ServerMessage(125));
                            over();
                            return;
                        }
                    }
                    if (pc.getDoll(item.getId()) != null) {
                        pc.sendPackets(new S_ServerMessage(1181));
                        over();
                    } else if (item.isEquipped()) {
                        pc.sendPackets(new S_ServerMessage(125));
                        over();
                    } else if (item.getBless() >= 128) {
                        pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
                        over();
                    } else {
                        if (item.getCount() > 1) {
                            deleteCount = readD();
                            pc.getInventory().removeItem(item, (long) deleteCount);
                        } else {
                            pc.getInventory().removeItem(item, item.getCount());
                        }
                        pc.turnOnOffLight();
                        WriteLogTxt.Recording("刪除物品紀錄", "IP：(" + ((Object) pc.getNetConnection().getIp()) + ")人物：【" + pc.getName() + "】刪除物品：【(+" + item.getEnchantLevel() + ")" + item.getName() + "(" + deleteCount + ")" + "】 ItmeID：┌" + item.getItemId() + "┐ 物品OBJID：┌" + item.getId() + "┐ ");
                        over();
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage(125));
                    over();
                }
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
