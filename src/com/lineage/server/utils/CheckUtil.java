package com.lineage.server.utils;

import com.lineage.server.datatables.NpcTeleportTable;
import com.lineage.server.datatables.QuestMapTable;
import com.lineage.server.model.Instance.L1EffectInstance;
import com.lineage.server.model.Instance.L1IllusoryInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.Instance.L1PetInstance;
import com.lineage.server.model.Instance.L1SummonInstance;
import com.lineage.server.model.L1Character;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.server.ServerUseMapTimer;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CheckUtil {
    private static final Log _log = LogFactory.getLog(CheckUtil.class);

    private CheckUtil() {
    }

    public static L1PcInstance checkAtkPc(L1Character lastAttacker) {
        try {
            if (lastAttacker instanceof L1PcInstance) {
                return (L1PcInstance) lastAttacker;
            }
            if (lastAttacker instanceof L1PetInstance) {
                L1PetInstance atk = (L1PetInstance) lastAttacker;
                if (atk.getMaster() == null || !(atk.getMaster() instanceof L1PcInstance)) {
                    return null;
                }
                return (L1PcInstance) atk.getMaster();
            } else if (lastAttacker instanceof L1SummonInstance) {
                L1SummonInstance atk2 = (L1SummonInstance) lastAttacker;
                if (atk2.getMaster() == null || !(atk2.getMaster() instanceof L1PcInstance)) {
                    return null;
                }
                return (L1PcInstance) atk2.getMaster();
            } else if (lastAttacker instanceof L1IllusoryInstance) {
                L1IllusoryInstance atk3 = (L1IllusoryInstance) lastAttacker;
                if (atk3.getMaster() == null || !(atk3.getMaster() instanceof L1PcInstance)) {
                    return null;
                }
                return (L1PcInstance) atk3.getMaster();
            } else if (!(lastAttacker instanceof L1EffectInstance)) {
                return null;
            } else {
                L1EffectInstance atk4 = (L1EffectInstance) lastAttacker;
                if (atk4.getMaster() == null || !(atk4.getMaster() instanceof L1PcInstance)) {
                    return null;
                }
                return (L1PcInstance) atk4.getMaster();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    public static void isUserMap(L1PcInstance pc) {
        try {
            if (!pc.isGm()) {
                isTimeMap(pc);
                isPartyMap(pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void isPartyMap(L1PcInstance pc) {
        try {
            short mapid = pc.getMapId();
            Integer userCount = NpcTeleportTable.get().isPartyMap(mapid);
            if (userCount == null) {
                return;
            }
            if (!pc.isInParty()) {
                pc.sendPackets(new S_ServerMessage(425));
                L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
            } else if (pc.getParty().partyUserInMap(mapid) < userCount.intValue()) {
                L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private static void isTimeMap(L1PcInstance pc) {
        try {
            if (NpcTeleportTable.get().isTimeMap(pc.getMapId()) && ServerUseMapTimer.MAP.get(pc) == null) {
                L1Teleport.teleport(pc, 33080, 33392, (short) 4, 5, true);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public static boolean getUseItemAll(L1PcInstance pc) {
        if (!pc.hasSkillEffect(1011) && !pc.hasSkillEffect(L1SkillId.STATUS_POISON_PARALYZED) && !pc.hasSkillEffect(66) && !pc.hasSkillEffect(87)) {
            return true;
        }
        return false;
    }

    public static boolean getUseItem(L1PcInstance pc) {
        if (pc.hasSkillEffect(71)) {
            pc.sendPackets(new S_ServerMessage(698));
            return false;
        } else if (pc.hasSkillEffect(157) || pc.hasSkillEffect(L1SkillId.SHOCK_SKIN) || pc.hasSkillEffect(192) || pc.hasSkillEffect(50) || pc.hasSkillEffect(194)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPassable(L1PcInstance pc, int locx, int locy, short mapid) {
        if (QuestMapTable.get().isQuestMap(pc.getMapId())) {
            return false;
        }
        for (L1Object obj : World.get().getVisibleObjects(pc, 1)) {
            if (obj instanceof L1PcInstance) {
                L1PcInstance tgpc = (L1PcInstance) obj;
                if (!tgpc.isInvisble() && !tgpc.isGmInvis() && !tgpc.isGhost() && tgpc.getX() == locx && tgpc.getY() == locy && tgpc.getMapId() == mapid) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAttackSkill(L1Character cha) {
        try {
            if (!(cha instanceof L1EffectInstance) && !(cha instanceof L1IllusoryInstance)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
