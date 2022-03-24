package com.lineage.server.clientpackets;

import com.lineage.config.ConfigOther;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.clientpackets.AcceleratorChecker;
import com.lineage.server.datatables.PolyTable;
import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1CastleLocation;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_Paralysis;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerWarExecutor;
import com.lineage.server.utils.CheckUtil;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_UseSkill extends ClientBasePacket {
    private static final int[] _cast_with_invis = {1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60, 61, 63, 67, 68, 69, 72, 73, 75, 78, 79, 88, 89, 90, 91, 97, 98, 99, 100, 101, L1SkillId.BURNING_SPIRIT, 104, 105, 106, 107, 109, 110, 111, 113, L1SkillId.GLOWING_AURA, 115, 116, 117, L1SkillId.RUN_CLAN, 129, L1SkillId.BODY_TO_MIND, 131, 133, 134, 137, 138, L1SkillId.BLOODY_SOUL, L1SkillId.ELEMENTAL_PROTECTION, 148, 149, 150, 151, 155, L1SkillId.STORM_EYE, L1SkillId.NATURES_TOUCH, 159, L1SkillId.BURNING_WEAPON, 164, 165, 166, L1SkillId.IRON_SKIN, L1SkillId.EXOTIC_VITALIZE, 170, L1SkillId.ELEMENTAL_FIRE, L1SkillId.SOUL_OF_FLAME, L1SkillId.ADDITIONAL_FIRE, L1SkillId.DRAGON_SKIN, 185, 190, 195, 201, 204, 209, 211, L1SkillId.ILLUSION_DIA_GOLEM, 216, L1SkillId.ILLUSION_AVATAR};
    private static final Log _log = LogFactory.getLog(C_UseSkill.class);

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        int callClanId;
        int result;
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isDead()) {
                if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else if (pc.getInventory().getWeight240() >= 197) {
                    pc.setTeleport(false);
                    pc.sendPackets(new S_Paralysis(7, false));
                    pc.sendPackets(new S_ServerMessage(316));
                    over();
                } else if (!pc.getMap().isUsableSkill()) {
                    pc.setTeleport(false);
                    pc.sendPackets(new S_Paralysis(7, false));
                    pc.sendPackets(new S_ServerMessage(563));
                    over();
                } else if (pc.isSkillDelay()) {
                    over();
                } else {
                    boolean isError = false;
                    L1PolyMorph poly = PolyTable.get().getTemplate(pc.getTempCharGfx());
                    if (poly != null && !poly.canUseSkill()) {
                        isError = true;
                    }
                    if (pc.isParalyzed() && !isError) {
                        isError = true;
                    }
                    if (pc.hasSkillEffect(64) && !isError) {
                        isError = true;
                    }
                    if (pc.hasSkillEffect(161) && !isError) {
                        isError = true;
                    }
                    if (pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE) && !isError) {
                        isError = true;
                    }
                    if (pc.isParalyzedX() && !isError) {
                        isError = true;
                    }
                    if (isError) {
                        pc.setTeleport(false);
                        pc.sendPackets(new S_Paralysis(7, false));
                        pc.sendPackets(new S_ServerMessage(285));
                        over();
                        return;
                    }
                    int skillId = (readC() << 3) + readC() + 1;
                    if (skillId > 239) {
                        over();
                    } else if (skillId < 0) {
                        over();
                    } else if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill(skillId)) {
                        pc.sendPackets(new S_ServerMessage((int) L1SkillId.STATUS_UNDERWATER_BREATH));
                        over();
                    } else if (!pc.isSkillMastery(skillId)) {
                        over();
                    } else {
                        int castleId = L1CastleLocation.getCastleIdByArea(pc);
                        if (castleId != 0 && ServerWarExecutor.get().isNowWar(castleId)) {
                            switch (skillId) {
                                case 50:
                                case 51:
                                case 72:
                                case L1SkillId.FREEZING_BLIZZARD /*{ENCODED_INT: 80}*/:
                                    over();
                                    return;
                            }
                        }
                        CheckUtil.isUserMap(pc);
                        String charName = null;
                        int targetId = 0;
                        int targetX = 0;
                        int targetY = 0;
                        if (ConfigOther.CHECK_SPELL_INTERVAL) {
                            if (SkillsTable.get().getTemplate(skillId).getActionId() == 18) {
                                result = pc.speed_Attack().checkInterval(AcceleratorChecker.ACT_TYPE.SPELL_DIR);
                            } else {
                                result = pc.speed_Attack().checkInterval(AcceleratorChecker.ACT_TYPE.SPELL_NODIR);
                            }
                            if (result == 2) {
                                over();
                                return;
                            }
                        }
                        if (decrypt.length > 4) {
                            switch (skillId) {
                                case 5:
                                    if (!L1BuffUtil.getUseItemTeleport(pc)) {
                                        pc.setTeleport(false);
                                        pc.sendPackets(new S_Paralysis(7, false));
                                        over();
                                        return;
                                    }
                                    try {
                                        readH();
                                        targetId = readD();
                                        break;
                                    } catch (Exception e) {
                                        break;
                                    }
                                case 51:
                                    try {
                                        if (!pc.getInventory().checkEquipped(20284)) {
                                            targetId = readD();
                                            targetX = readH();
                                            targetY = readH();
                                            break;
                                        } else {
                                            pc.setSummonId(readD());
                                            break;
                                        }
                                    } catch (Exception e2) {
                                        break;
                                    }
                                case 58:
                                case 63:
                                    targetX = readH();
                                    targetY = readH();
                                    break;
                                case 69:
                                    readH();
                                    targetId = readD();
                                    break;
                                case L1SkillId.SHOCK_STUN /*{ENCODED_INT: 87}*/:
                                    if (pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 3) {
                                        targetId = readD();
                                        targetX = readH();
                                        targetY = readH();
                                        break;
                                    } else {
                                        over();
                                        return;
                                    }
                                case 113:
                                    targetId = readD();
                                    targetX = readH();
                                    targetY = readH();
                                    pc.setText(readS());
                                    break;
                                case 116:
                                case L1SkillId.RUN_CLAN /*{ENCODED_INT: 118}*/:
                                    charName = readS();
                                    break;
                                default:
                                    targetId = readD();
                                    targetX = readH();
                                    targetY = readH();
                                    break;
                            }
                        } else {
                            switch (skillId) {
                                case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                                    if (pc.getWeapon() == null || pc.getWeapon().getItem().getType() != 3) {
                                        over();
                                        return;
                                    }
                            }
                        }
                        if (pc.hasSkillEffect(78)) {
                            pc.killSkillEffectTimer(78);
                            pc.startHpRegeneration();
                            pc.startMpRegeneration();
                        }
                        pc.removeSkillEffect(32);
                        if (skillId == 116 || skillId == 118) {
                            try {
                                if (charName.isEmpty()) {
                                    over();
                                    return;
                                }
                                L1PcInstance target = World.get().getPlayer(charName);
                                if (target == null) {
                                    pc.sendPackets(new S_ServerMessage(73, charName));
                                    over();
                                    return;
                                } else if (pc.getClanid() != target.getClanid()) {
                                    pc.sendPackets(new S_ServerMessage(414));
                                    over();
                                    return;
                                } else {
                                    targetId = target.getId();
                                    if (skillId == 116 && ((callClanId = pc.getCallClanId()) == 0 || callClanId != targetId)) {
                                        pc.setCallClanId(targetId);
                                        pc.setCallClanHeading(pc.getHeading());
                                    }
                                }
                            } catch (Exception ignored) {
                            }
                        }
                        new L1SkillUse().handleCommands(pc, skillId, targetId, targetX, targetY, 0, 0);
                        over();
                    }
                }
            }
        } catch (Exception e4) {
            _log.error(e4.getLocalizedMessage(), e4);
        } finally {
            over();
        }
    }

    private boolean isInvisUsableSkill(int useSkillid) {
        for (int skillId : _cast_with_invis) {
            if (skillId == useSkillid) {
                return true;
            }
        }
        return false;
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
