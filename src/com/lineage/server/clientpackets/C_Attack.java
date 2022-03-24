package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.clientpackets.AcceleratorChecker.ACT_TYPE;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_AttackPacketPc;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Attack extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Attack.class);

    public C_Attack() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            try {
                this.read(decrypt);
                L1PcInstance pc = client.getActiveChar();
                pc.isFoeSlayer(false);
                if (pc.isGhost()) {
                    return;
                }

                if (pc.isDead()) {
                    return;
                }

                if (pc.isTeleport()) {
                    return;
                }

                if (pc.isPrivateShop()) {
                    return;
                }

                if (pc.getInventory().getWeight240() >= 197) {
                    pc.sendPackets(new S_ServerMessage(110));
                    return;
                }

                int targetId;
                if (ConfigOther.CHECK_ATTACK_INTERVAL) {
                    targetId = pc.speed_Attack().checkInterval(ACT_TYPE.ATTACK);
                    if (targetId == 2) {
                        return;
                    }
                }

                if (pc.isInvisble()) {
                    return;
                }

                if (pc.isInvisDelay()) {
                    return;
                }

                if (pc.isParalyzedX()) {
                    return;
                }

                if (pc.get_weaknss() != 0) {
                    long h_time = Calendar.getInstance().getTimeInMillis() / 1000L;
                    if (h_time - pc.get_weaknss_t() > 16L) {
                        pc.set_weaknss(0, 0L);
                    }
                }

                boolean var6 = false;

                int locx;
                int locy;
                try {
                    targetId = this.readD();
                    locx = this.readH();
                    locy = this.readH();
                } catch (Exception var13) {
                    return;
                }

                if (locx == 0) {
                    return;
                }

                if (locy == 0) {
                    return;
                }

                L1Object target = World.get().findObject(targetId);
                if (target instanceof L1Character && target.getMapId() != pc.getMapId()) {
                    return;
                }

                CheckUtil.isUserMap(pc);
                if (target instanceof L1NpcInstance) {
                    int hiddenStatus = ((L1NpcInstance)target).getHiddenStatus();
                    if (hiddenStatus == 1) {
                        return;
                    }

                    if (hiddenStatus == 2) {
                        return;
                    }
                }

                if (pc.hasSkillEffect(78)) {
                    pc.killSkillEffectTimer(78);
                    pc.startHpRegeneration();
                    pc.startMpRegeneration();
                }

                pc.killSkillEffectTimer(32);
                pc.delInvis();
                pc.setRegenState(1);
                if (target != null && !((L1Character)target).isDead()) {
                    if (target instanceof L1PcInstance) {
                        L1PcInstance tg = (L1PcInstance)target;
                        pc.setNowTarget(tg);
                    }

                    target.onAction(pc);
                    return;
                }

                pc.setHeading(pc.targetDirection(locx, locy));
                pc.sendPacketsAll(new S_AttackPacketPc(pc));
            } catch (Exception var14) {
            }

        } finally {
            this.over();
        }
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
