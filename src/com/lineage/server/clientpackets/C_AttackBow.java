package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.clientpackets.AcceleratorChecker.ACT_TYPE;
import com.lineage.server.datatables.GfxIdOrginal;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_UseArrowSkill;
import com.lineage.server.templates.L1WilliamGfxIdOrginal;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_AttackBow extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_AttackBow.class);

    public C_AttackBow() {
    }

    public void start(byte[] decrypt, ClientExecutor client) {
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


            int targetX;
            boolean var8 = false;

            int locx;
            int locy;
            try {
                targetId = this.readD();
                locx = this.readH();
                locy = this.readH();
            } catch (Exception var20) {
                return;
            }

            if (locx == 0) {
                return;
            }

            if (locy == 0) {
                return;
            }

            L1Object target = World.get().findObject(targetId);
            if (!(target instanceof L1Character) || target.getMapId() == pc.getMapId()) {
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
                L1ItemInstance weapon = pc.getWeapon();
                if (weapon != null) {
                    int weaponType = weapon.getItem().getType1();
                    switch(weaponType) {
                        case 20:
                            L1ItemInstance arrow = pc.getInventory().getArrow();
                            if (arrow != null) {
                                int arrowGfxid = 66;
                                L1WilliamGfxIdOrginal gfxIdOrginal = GfxIdOrginal.get().getTemplate(pc.getTempCharGfx());
                                if (gfxIdOrginal != null) {
                                    arrowGfxid = gfxIdOrginal.getarrowGfxid();
                                }

                                this.arrowAction(pc, arrow, arrowGfxid, locx, locy);
                                return;
                            } else {
                                if (weapon.getItem().getItemId() != 190 && weapon.getItem().getItemId() != 100190) {
                                    this.nullAction(pc);
                                } else {
                                    this.arrowAction(pc, (L1ItemInstance)null, 2349, locx, locy);
                                }

                                return;
                            }
                        case 62:
                            L1ItemInstance sting = pc.getInventory().getSting();
                            if (sting != null) {
                                int stingGfxid = 2989;
                                L1WilliamGfxIdOrginal gfxIdOrginal = GfxIdOrginal.get().getTemplate(pc.getTempCharGfx());
                                if (gfxIdOrginal != null) {
                                    stingGfxid = gfxIdOrginal.getstingGfxid();
                                }

                                this.arrowAction(pc, sting, stingGfxid, locx, locy);
                            } else {
                                this.nullAction(pc);
                            }
                    }
                }

                return;
            }
        } catch (Exception var21) {
            return;
        } finally {
            this.over();
        }

    }

    private void arrowAction(L1PcInstance pc, L1ItemInstance item, int gfx, int targetX, int targetY) throws Exception {
        pc.sendPacketsAll(new S_UseArrowSkill(pc, gfx, targetX, targetY));
        if (item != null) {
            pc.getInventory().removeItem(item, 1L);
        }

    }

    private void nullAction(L1PcInstance pc) {
        int aid = 1;
        if (pc.getTempCharGfx() == 3860) {
            aid = 21;
        }

        pc.sendPacketsAll(new S_ChangeHeading(pc));
        pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), aid));
    }

    public String getType() {
        return this.getClass().getSimpleName();
    }
}
