package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.serverpackets.S_ChangeHeading;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SkillSound;
import com.lineage.server.world.World;
import java.util.Random;

public class Lightning_Magic_Wand extends ItemExecutor {
    private static int _gfxid = 10;

    private Lightning_Magic_Wand() {
    }

    public static ItemExecutor get() {
        return new Lightning_Magic_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) {
        int spellsc_objid = data[0];
        int spellsc_x = data[1];
        int spellsc_y = data[2];
        L1BuffUtil.cancelAbsoluteBarrier(pc);
        if (item.getChargeCount() <= 0) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        L1Object target = World.get().findObject(spellsc_objid);
        if (target != null) {
            doWandAction(pc, target);
        } else {
            pc.sendPacketsXR(new S_EffectLocation(new L1Location(spellsc_x, spellsc_y, pc.getMapId()), _gfxid), 7);
        }
        item.setChargeCount(item.getChargeCount() - 1);
        pc.getInventory().updateItem(item, 128);
    }

    private void doWandAction(L1PcInstance user, L1Object target) {
        Random _random = new Random();
        if (user.getId() != target.getId() && user.glanceCheck(target.getX(), target.getY())) {
            int dmg = Math.max(1, (_random.nextInt(11) - 5) + user.getInt());
            if (!(target instanceof L1PcInstance)) {
                if (target instanceof L1MonsterInstance) {
                    L1MonsterInstance mob = (L1MonsterInstance) target;
                    switch (mob.getNpcId()) {
                        case 71100:
                        case 91072:
                            user.sendPacketsXR(new S_EffectLocation(new L1Location(target.getX(), target.getY(), user.getMapId()), _gfxid), 7);
                            return;
                        default:
                            mob.receiveDamage(user, dmg);
                            break;
                    }
                }
            } else {
                L1PcInstance pc = (L1PcInstance) target;
                if (!pc.getMap().isSafetyZone(pc.getLocation()) && !user.checkNonPvP(user, pc) && !pc.hasSkillEffect(50) && !pc.hasSkillEffect(78) && !pc.hasSkillEffect(157)) {
                    int newHp = pc.getCurrentHp() - dmg;
                    if (newHp <= 0 && !pc.isGm()) {
                        pc.death(user);
                    }
                    pc.setCurrentHp(newHp);
                } else {
                    return;
                }
            }
            user.setHeading(user.targetDirection(target.getX(), target.getY()));
            user.sendPacketsX10(new S_ChangeHeading(user));
            user.sendPacketsX10(new S_DoActionGFX(user.getId(), 17));
            if (target instanceof L1PcInstance) {
                L1PcInstance tgpc = (L1PcInstance) target;
                tgpc.sendPacketsX10(new S_SkillSound(tgpc.getId(), _gfxid));
                tgpc.sendPacketsX10(new S_DoActionGFX(tgpc.getId(), 2));
            } else if (target instanceof L1MonsterInstance) {
                L1MonsterInstance mob2 = (L1MonsterInstance) target;
                mob2.broadcastPacketX10(new S_SkillSound(mob2.getId(), _gfxid));
                mob2.broadcastPacketX10(new S_DoActionGFX(mob2.getId(), 2));
            } else {
                user.sendPacketsXR(new S_EffectLocation(new L1Location(target.getX(), target.getY(), user.getMapId()), _gfxid), 7);
            }
        }
    }
}
