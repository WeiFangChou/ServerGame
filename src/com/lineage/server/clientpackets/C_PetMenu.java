package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.serverpackets.S_PetInventory;
import com.lineage.server.world.WorldPet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_PetMenu extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_PetMenu.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (pc != null) {
                if (pc.isGhost()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    int petId = readD();
                    L1PetInstance pet = WorldPet.get().get(Integer.valueOf(petId));
                    if (pet == null) {
                        over();
                    } else if (pc.getPetList().get(Integer.valueOf(petId)) == null) {
                        over();
                    } else {
                        pc.sendPackets(new S_PetInventory(pet));
                        over();
                    }
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
