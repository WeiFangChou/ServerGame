package com.lineage.server.model.skill.skillmode;

import com.lineage.echo.OpcodesClient;
import com.lineage.server.model.Instance.L1MonsterInstance;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.util.Random;

public class SHAPE_CHANGE extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        int npcId;
        Random _random = new Random();
        int[] polyArray = {29, 945, 947, 979, 1037, 1039, 3860, 3861, 3862, 3863, 3864, 3865, 3904, 3906, 95, L1SkillId.BLOODY_SOUL, 2374, 2376, 2377, 2378, 3866, 3867, 3868, 3869, 3870, 3871, 3872, 3873, 3874, 3875, 3876};
        int polyId = polyArray[_random.nextInt(polyArray.length)];
        if (cha instanceof L1PcInstance) {
            L1PcInstance pc = (L1PcInstance) cha;
            int awakeSkillId = pc.getAwakeSkillId();
            if (awakeSkillId == 185 || awakeSkillId == 190 || awakeSkillId == 195) {
                pc.sendPackets(new S_ServerMessage(1384));
                return 0;
            } else if (pc.getMapId() == 800) {
                return 0;
            } else {
                if (pc.getInventory().checkEquipped(20281) || pc.getInventory().checkEquipped(120281)) {
                    pc.sendPackets(new S_Message_YN((int) OpcodesServer.S_OPCODE_INVLIST));
                    if (!pc.isShapeChange()) {
                        pc.setSummonMonster(false);
                        pc.setShapeChange(true);
                    }
                    pc.sendPackets(new S_ServerMessage(966));
                } else if (srcpc.getId() == pc.getId()) {
                    pc.sendPackets(new S_Message_YN((int) OpcodesServer.S_OPCODE_INVLIST));
                    if (!pc.isShapeChange()) {
                        pc.setSummonMonster(false);
                        pc.setShapeChange(true);
                    }
                } else {
                    pc.sendPackets(new S_ServerMessage((int) OpcodesClient.C_OPCODE_TRADEADDITEM, srcpc.getName()));
                    L1PolyMorph.doPoly(pc, polyId, integer, 1);
                }
                L1PinkName.onAction(pc, srcpc);
                return 0;
            }
        } else if (!(cha instanceof L1MonsterInstance)) {
            return 0;
        } else {
            L1MonsterInstance mob = (L1MonsterInstance) cha;
            if (mob.getLevel() >= 50 || (npcId = mob.getNpcTemplate().get_npcId()) == 45464 || npcId == 45473 || npcId == 45488 || npcId == 45497 || npcId == 45458 || npcId == 45752 || npcId == 45492 || npcId == 46035 || npcId == 99006) {
                return 0;
            }
            L1PolyMorph.doPoly(mob, polyId, integer, 1);
            return 0;
        }
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
        L1PolyMorph.undoPoly(cha);
    }
}
