package com.lineage.data.item_etcitem.wand;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_DoActionGFX;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_ShowPolyList;
import com.lineage.server.templates.L1Skills;
import com.lineage.server.world.World;
import java.util.Random;

public class Poly_Magic_Wand extends ItemExecutor {
    private Poly_Magic_Wand() {
    }

    public static ItemExecutor get() {
        return new Poly_Magic_Wand();
    }

    @Override // com.lineage.data.executor.ItemExecutor
    public void execute(int[] data, L1PcInstance pc, L1ItemInstance item) throws Exception {
        int itemId = item.getItemId();
        int spellsc_objid = data[0];
        if (pc.getMapId() == 63 || pc.getMapId() == 552 || pc.getMapId() == 555 || pc.getMapId() == 557 || pc.getMapId() == 558 || pc.getMapId() == 779) {
            pc.sendPackets(new S_ServerMessage(563));
            return;
        }
        pc.sendPacketsX8(new S_DoActionGFX(pc.getId(), 17));
        if ((item.getChargeCount() <= 0 && itemId != 40410) || pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
            pc.sendPackets(new S_ServerMessage(79));
            return;
        }
        L1Object target = World.get().findObject(spellsc_objid);
        if (target != null) {
            polyAction(pc, (L1Character) target);
            L1BuffUtil.cancelAbsoluteBarrier(pc);
            if (itemId == 40008 || itemId == 140008) {
                item.setChargeCount(item.getChargeCount() - 1);
                pc.getInventory().updateItem(item, 128);
                return;
            }
            pc.getInventory().removeItem(item, 1);
            return;
        }
        pc.sendPackets(new S_ServerMessage(79));
    }

    private void polyAction(L1PcInstance attacker, L1Character cha) {
        int npcId;
        boolean isSameClan = false;
        Random _random = new Random();
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            if (pc.getClanid() != 0 && attacker.getClanid() == pc.getClanid()) {
                isSameClan = true;
            }
        }
        if (attacker.getId() == cha.getId() || isSameClan || _random.nextInt(100) + 1 <= (((attacker.getLevel() - cha.getLevel()) * 3) + 100) - cha.getMr()) {
            int[] polyArray = {29, 945, 947, 979, 1037, 1039, 3860, 3861, 3862, 3863, 3864, 3865, 3904, 3906, 95, L1SkillId.BLOODY_SOUL, 2374, 2376, 2377, 2378, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 3873, 3874, 3875, 3876};
            int polyId = polyArray[_random.nextInt(polyArray.length)];
            if (cha instanceof L1PcInstance) {
                L1PcInstance pc2 = (L1PcInstance) cha;
                int awakeSkillId = pc2.getAwakeSkillId();
                if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
                    pc2.sendPackets(new S_ServerMessage(1384));
                } else if (pc2.getInventory().checkEquipped(20281)) {
                    pc2.sendPackets(new S_ShowPolyList(pc2.getId()));
                    if (!pc2.isShapeChange()) {
                        pc2.setSummonMonster(false);
                        pc2.setShapeChange(true);
                    }
                    pc2.sendPackets(new S_ServerMessage(966));
                } else {
                    L1Skills skillTemp = SkillsTable.get().getTemplate(67);
                    if (attacker.getId() == pc2.getId()) {
                        L1PolyMorph.doPoly(pc2, polyId, skillTemp.getBuffDuration(), 1);
                    } else {
                        attacker.sendPackets(new S_ServerMessage(79));
                    }
                }
            } else if (cha instanceof L1MonsterInstance) {
                L1MonsterInstance mob = (L1MonsterInstance) cha;
                if (mob.getLevel() < 50 && (npcId = mob.getNpcTemplate().get_npcId()) != 45338 && npcId != 45370 && npcId != 45456 && npcId != 45464 && npcId != 45473 && npcId != 45488 && npcId != 45497 && npcId != 45516 && npcId != 45529 && npcId != 45458) {
                    L1PolyMorph.doPoly(mob, polyId, SkillsTable.get().getTemplate(67).getBuffDuration(), 1);
                }
            }
        }
    }
}
