package com.lineage.data.item_etcitem.magicreel;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_EffectLocation;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.Random;

public class FireBadge extends ItemExecutor {
    private final Random _random = new Random();

    private FireBadge() {
    }

    public static ItemExecutor get() {
        return new FireBadge();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        if (pc != null && item != null) {
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            if (pc.isInvisble()) {
                pc.delInvis();
            }
            pc.sendPacketsAll(new S_DoActionGFX(pc.getId(), 19));
            if (item.getChargeCount() > 0) {
                item.setChargeCount(item.getChargeCount() - 1);
                pc.getInventory().updateItem(item, 128);
                int x = pc.getX();
                int y = pc.getY();
                int mapId = pc.getMapId();
                pc.sendPacketsXR(new S_EffectLocation(new L1Location(x + 1, y + 1, mapId), 1819), 7);
                pc.sendPacketsXR(new S_EffectLocation(new L1Location(x - 1, y + 1, mapId), 1819), 7);
                pc.sendPacketsXR(new S_EffectLocation(new L1Location(x - 1, y - 1, mapId), 1819), 7);
                pc.sendPacketsXR(new S_EffectLocation(new L1Location(x + 1, y - 1, mapId), 1819), 7);
                L1PcInstance tgpc = pc.getNowTarget();
                if (tgpc != null && !tgpc.isSafetyZone()) {
                    double dmg = ((double) (this._random.nextInt(60) + L1SkillId.AQUA_PROTECTER)) * 1.0d;
                    tgpc.receiveDamage(pc, dmg, false, false);
                    Iterator<L1PcInstance> it = World.get().getVisiblePlayer(tgpc, 4).iterator();
                    while (it.hasNext()) {
                        L1PcInstance tgClanPc = it.next();
                        if (tgClanPc.getClanid() == tgpc.getClanid() && !tgpc.isSafetyZone()) {
                            tgClanPc.receiveDamage(pc, 0.8d * dmg, false, false);
                            tgClanPc.broadcastPacketX8(new S_DoActionGFX(tgClanPc.getId(), 2));
                        }
                    }
                }
                Iterator<L1Object> it2 = World.get().getVisibleObjects(pc, 5).iterator();
                while (it2.hasNext()) {
                    L1Object object = it2.next();
                    if (object instanceof L1MonsterInstance) {
                        L1MonsterInstance mob = (L1MonsterInstance) object;
                        mob.receiveDamage(pc, this._random.nextInt(60) + L1SkillId.AQUA_PROTECTER);
                        mob.broadcastPacketX8(new S_DoActionGFX(mob.getId(), 2));
                    }
                }
            } else if (pc.getInventory().removeItem(item, 1) == 1) {
                pc.sendPackets(new S_ServerMessage(154));
            }
        }
    }
}
