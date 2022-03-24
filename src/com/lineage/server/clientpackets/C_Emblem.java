package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.ClanEmblemReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Emblem;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1EmblemIcon;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Emblem extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Emblem.class);

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
                } else {
                    int clan_id = pc.getClanid();
                    if (clan_id != 0) {
                        if (pc.getClan().getLeaderId() != pc.getId()) {
                            pc.sendPackets(new S_ServerMessage((int) L1SkillId.ILLUSION_AVATAR));
                            over();
                            return;
                        }
                        byte[] iconByte = readByte();
                        L1EmblemIcon emblemIcon = ClanEmblemReading.get().get(clan_id);
                        if (emblemIcon != null) {
                            emblemIcon.set_clanIcon(iconByte);
                            emblemIcon.set_update(emblemIcon.get_update() + 1);
                            ClanEmblemReading.get().updateClanIcon(emblemIcon);
                        } else {
                            emblemIcon = ClanEmblemReading.get().storeClanIcon(clan_id, iconByte);
                        }
                        World.get().broadcastPacketToAll(new S_Emblem(emblemIcon));
                    }
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
