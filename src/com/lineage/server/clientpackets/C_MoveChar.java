package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.clientpackets.AcceleratorChecker.ACT_TYPE;
import com.lineage.server.datatables.DungeonRTable;
import com.lineage.server.datatables.DungeonTable;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Trade;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_MoveCharPacket;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.WorldTrap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import william.L1PcUnlock;

public class C_MoveChar extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_MoveChar.class);
    private static final byte[] HEADING_TABLE_X = new byte[]{0, 1, 1, 1, 0, -1, -1, -1};
    private static final byte[] HEADING_TABLE_Y = new byte[]{-1, -1, 0, 1, 1, 1, 0, -1};

    public C_MoveChar() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            this.read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (pc.isDead()) {
                return;
            }

            if (pc.isTeleport()) {
                return;
            }

            if (pc.isParalyzed()) {
                return;
            }

            if (pc.isPrivateShop()) {
                return;
            }

            if (pc.isSleeped()) {
                return;
            }

            if (pc.hasSkillEffect(192)) {
                return;
            }

            if (pc.isInCharReset()) {
                return;
            }


            boolean var6 = false;

            int locx;
            int locy;
            int heading;
            try {
                locx = this.readH();
                locy = this.readH();
                heading = this.readC();
                heading = Math.min(heading, 7);
            } catch (Exception var19) {
                return;
            }

            if (pc.getTradeID() != 0) {
                L1Trade trade = new L1Trade();
                trade.tradeCancel(pc);
            }

            pc.killSkillEffectTimer(32);
            pc.setCallClanId(0);
            if (!pc.hasSkillEffect(78)) {
                pc.setRegenState(2);
            }

            pc.getMap().setPassable(pc.getLocation(), true);
            int oleLocx = pc.getX();
            int oleLocy = pc.getY();
            int newlocx = locx + HEADING_TABLE_X[heading];
            int newlocy = locy + HEADING_TABLE_Y[heading];

            try {
                boolean isError = false;
                if (locx != oleLocx && locy != oleLocy) {
                    isError = true;
                }

                if (pc.isPrivateShop()) {
                    isError = true;
                }

                if (pc.isParalyzedX()) {
                    isError = true;
                }

                if (!isError) {
                    boolean isPassable = pc.getMap().isPassable(oleLocx, oleLocy, heading, (L1Character)null);
                    if (!isPassable && CheckUtil.checkPassable(pc, newlocx, newlocy, pc.getMapId())) {
                        isError = true;
                    }
                }

                if (isError) {
                    L1PcUnlock.Pc_Unlock(pc);
                    return;
                }
            } catch (Exception var18) {
                _log.error(var18.getLocalizedMessage(), var18);
            }

            if (ConfigOther.CHECK_MOVE_INTERVAL) {
                int result = pc.speed_Attack().checkInterval(ACT_TYPE.MOVE);
                if (result == 2) {
                    return;
                }
            }

            CheckUtil.isUserMap(pc);
            if (DungeonTable.get().dg(newlocx, newlocy, pc.getMap().getId(), pc)) {
                return;
            }

            if (DungeonRTable.get().dg(newlocx, newlocy, pc.getMap().getId(), pc)) {
                return;
            }

            pc.setOleLocX(oleLocx);
            pc.setOleLocY(oleLocy);
            pc.getLocation().set(newlocx, newlocy);
            pc.setHeading(heading);
            if (!pc.isGmInvis() && !pc.isGhost() && !pc.isInvisble()) {
                pc.broadcastPacketAll(new S_MoveCharPacket(pc));
            }

            pc.setNpcSpeed();
            pc.getMap().setPassable(pc.getLocation(), false);
            WorldTrap.get().onPlayerMoved(pc);
        } catch (Exception var20) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.over();
        }

    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
