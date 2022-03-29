package com.lineage.server.model.skill.skillmode;

import com.lineage.data.quest.EWLv40_1;
import com.lineage.data.quest.ElfLv45_2;
import com.lineage.data.quest.KnightLv45_1;
import com.lineage.data.quest.WizardLv50_1;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Magic;
import com.lineage.server.model.L1PinkName;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillMode;
import com.lineage.server.serverpackets.S_ChangeName;
import com.lineage.server.serverpackets.S_ChangeShape;
import com.lineage.server.serverpackets.S_CharVisualUpdate;
import com.lineage.server.serverpackets.S_DoActionShop;
import com.lineage.server.serverpackets.S_SkillBrave;
import com.lineage.server.serverpackets.S_SkillHaste;
import com.lineage.server.utils.L1SpawnUtil;

public class CANCELLATION extends SkillMode {
    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1PcInstance srcpc, L1Character cha, L1Magic magic, int integer) throws Exception {
        if (cha instanceof L1NpcInstance) {
            L1NpcInstance tgnpc = (L1NpcInstance) cha;
            switch (tgnpc.getNpcTemplate().get_npcId()) {
                case KnightLv45_1._searcherid:
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        tgnpc.setTempCharGfx(1314);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 1314));
                        return 0;
                    }
                    break;
                case WizardLv50_1._npcId:
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        tgnpc.setTempCharGfx(5217);
                        tgnpc.setPassispeed(480);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 5217));
                        tgnpc.setNameId("$6068");
                        tgnpc.broadcastPacketAll(new S_ChangeName(tgnpc.getId(), "$6068"));
                        return 0;
                    }
                    break;
                case EWLv40_1._roiid:
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        tgnpc.setTempCharGfx(4310);
                        tgnpc.broadcastPacketAll(new S_ChangeShape(tgnpc, 4310));
                        return 0;
                    }
                    break;
                case ElfLv45_2._npcId:
                    if (tgnpc.getGfxId() == tgnpc.getTempCharGfx()) {
                        int x = tgnpc.getX();
                        int y = tgnpc.getY();
                        int m = tgnpc.getMapId();
                        int h = tgnpc.getHeading();
                        tgnpc.deleteMe();
                        L1SpawnUtil.spawnT(45641, x, y, m, h, 300);
                        return 0;
                    }
                    break;
            }
        }
        if (srcpc != null && srcpc.isInvisble()) {
            srcpc.delInvis();
        }
        for (int skillNum = 1; skillNum <= 220; skillNum++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum) || cha.isDead()) {
                cha.removeSkillEffect(skillNum);
            }
        }
        cha.curePoison();
        cha.cureParalaysis();
        for (int skillNum2 = 998; skillNum2 <= 1026; skillNum2++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum2) || cha.isDead()) {
                cha.removeSkillEffect(skillNum2);
            }
        }
        for (int skillNum3 = 3000; skillNum3 <= 3047; skillNum3++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum3)) {
                cha.removeSkillEffect(skillNum3);
            }
        }
        cha.removeSkillEffect(L1SkillId.STATUS_FREEZE);
        if (cha instanceof L1PcInstance) {
            L1PcInstance tgpc = (L1PcInstance) cha;
            L1PolyMorph.undoPoly(tgpc);
            tgpc.sendPackets(new S_CharVisualUpdate(tgpc));
            tgpc.broadcastPacketAll(new S_CharVisualUpdate(tgpc));
            if (tgpc.getHasteItemEquipped() > 0) {
                tgpc.setMoveSpeed(0);
                tgpc.sendPacketsAll(new S_SkillHaste(tgpc.getId(), 0, 0));
            }
            tgpc.sendPacketsAll(new S_CharVisualUpdate(tgpc));
            if (tgpc.isPrivateShop()) {
                tgpc.sendPacketsAll(new S_DoActionShop(tgpc.getId(), tgpc.getShopChat()));
            }
            L1PinkName.onAction(tgpc, srcpc);
        } else {
            L1NpcInstance tgnpc2 = (L1NpcInstance) cha;
            tgnpc2.setMoveSpeed(0);
            tgnpc2.setBraveSpeed(0);
            tgnpc2.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
            tgnpc2.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0, 0));
            tgnpc2.setWeaponBreaked(false);
            tgnpc2.setParalyzed(false);
        }
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public int start(L1NpcInstance npc, L1Character cha, L1Magic magic, int integer) throws Exception {
        for (int skillNum = 1; skillNum <= 220; skillNum++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum) || cha.isDead()) {
                cha.removeSkillEffect(skillNum);
            }
        }
        cha.curePoison();
        cha.cureParalaysis();
        for (int skillNum2 = 998; skillNum2 <= 1026; skillNum2++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum2) || cha.isDead()) {
                cha.removeSkillEffect(skillNum2);
            }
        }
        for (int skillNum3 = 3000; skillNum3 <= 3047; skillNum3++) {
            if (!L1SkillMode.get().isNotCancelable(skillNum3)) {
                cha.removeSkillEffect(skillNum3);
            }
        }
        cha.removeSkillEffect(L1SkillId.STATUS_FREEZE);
        if (cha instanceof L1PcInstance) {
            L1PcInstance tgpc = (L1PcInstance) cha;
            L1PolyMorph.undoPoly(tgpc);
            tgpc.sendPackets(new S_CharVisualUpdate(tgpc));
            tgpc.broadcastPacketAll(new S_CharVisualUpdate(tgpc));
            if (tgpc.getHasteItemEquipped() > 0) {
                tgpc.setMoveSpeed(0);
                tgpc.sendPacketsAll(new S_SkillHaste(tgpc.getId(), 0, 0));
            }
            tgpc.sendPacketsAll(new S_CharVisualUpdate(tgpc));
            if (tgpc.isPrivateShop()) {
                tgpc.sendPacketsAll(new S_DoActionShop(tgpc.getId(), tgpc.getShopChat()));
            }
        } else {
            L1NpcInstance tgnpc = (L1NpcInstance) cha;
            tgnpc.setMoveSpeed(0);
            tgnpc.setBraveSpeed(0);
            tgnpc.broadcastPacketAll(new S_SkillHaste(cha.getId(), 0, 0));
            tgnpc.broadcastPacketAll(new S_SkillBrave(cha.getId(), 0, 0));
            tgnpc.setWeaponBreaked(false);
            tgnpc.setParalyzed(false);
        }
        return 0;
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void start(L1PcInstance srcpc, Object obj) throws Exception {
    }

    @Override // com.lineage.server.model.skill.skillmode.SkillMode
    public void stop(L1Character cha) throws Exception {
    }
}
