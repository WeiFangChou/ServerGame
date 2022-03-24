package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_SkillBuyItem;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SkillBuyItem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_SkillBuyItem.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    L1Object obj = World.get().findObject(readD());
                    if (obj == null) {
                        over();
                        return;
                    }
                    L1NpcInstance npc = null;
                    if (obj instanceof L1NpcInstance) {
                        npc = (L1NpcInstance) obj;
                    }
                    if (npc == null) {
                        over();
                        return;
                    }
                    npc.getNpcId();
                    pc.get_other().set_shopSkill(false);
                    pc.sendPackets(new S_SkillBuyItem(pc, npc));
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
