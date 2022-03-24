package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Trade;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.utils.FaceToFace;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Trade extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Trade.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isGhost()) {
                if (pc.isDead()) {
                    over();
                } else if (pc.isTeleport()) {
                    over();
                } else {
                    L1PcInstance target = FaceToFace.faceToFace(pc);
                    L1PcInstance srcTrade = (L1PcInstance) World.get().findObject(pc.getTradeID());
                    if (srcTrade != null) {
                        new L1Trade().tradeCancel(srcTrade);
                        over();
                    } else if (target == null) {
                        over();
                    } else {
                        L1PcInstance srcTradetarget = (L1PcInstance) World.get().findObject(target.getTradeID());
                        if (srcTradetarget != null) {
                            new L1Trade().tradeCancel(srcTradetarget);
                            over();
                            return;
                        }
                        if (target != null && !target.isParalyzed()) {
                            pc.get_trade_clear();
                            target.get_trade_clear();
                            pc.setTradeID(target.getId());
                            target.setTradeID(pc.getId());
                            target.sendPackets(new S_Message_YN(pc.getName()));
                        }
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
