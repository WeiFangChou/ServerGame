package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1CnInstance;
import com.lineage.server.model.Instance.L1GamInstance;
import com.lineage.server.model.Instance.L1GamblingInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldPet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_SelectTarget extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_SelectTarget.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int petId = readD();
            readC();
            int targetId = readD();
            L1PetInstance pet = WorldPet.get().get(Integer.valueOf(petId));
            if (pet != null) {
                L1Character target = (L1Character) World.get().findObject(targetId);
                if (target == null) {
                    over();
                    return;
                }
                boolean isCheck = false;
                if (target instanceof L1PcInstance) {
                    L1PcInstance tgpc = (L1PcInstance) target;
                    if (tgpc.checkNonPvP(tgpc, pet)) {
                        over();
                        return;
                    }
                    isCheck = true;
                } else if (target instanceof L1PetInstance) {
                    isCheck = true;
                } else if (target instanceof L1SummonInstance) {
                    isCheck = true;
                } else if (target instanceof L1CnInstance) {
                    over();
                    return;
                } else if (target instanceof L1GamblingInstance) {
                    over();
                    return;
                } else if (target instanceof L1GamInstance) {
                    over();
                    return;
                } else if (target instanceof L1IllusoryInstance) {
                    over();
                    return;
                }
                if (!isCheck || !target.isSafetyZone()) {
                    pet.setMasterSelectTarget(target);
                    over();
                    return;
                }
                over();
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
