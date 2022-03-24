package com.lineage.server.model.skill;

import com.lineage.server.model.skill.skillmode.ADLV80_1;
import com.lineage.server.model.skill.skillmode.ADLV80_2;
import com.lineage.server.model.skill.skillmode.ADVANCE_SPIRIT;
import com.lineage.server.model.skill.skillmode.AGLV85_1X;
import com.lineage.server.model.skill.skillmode.AQUA_PROTECTER;
import com.lineage.server.model.skill.skillmode.ARM_BREAKER;
import com.lineage.server.model.skill.skillmode.AWAKEN_ANTHARAS;
import com.lineage.server.model.skill.skillmode.AWAKEN_FAFURION;
import com.lineage.server.model.skill.skillmode.AWAKEN_VALAKAS;
import com.lineage.server.model.skill.skillmode.BLOODLUST;
import com.lineage.server.model.skill.skillmode.BLOODY_SOUL;
import com.lineage.server.model.skill.skillmode.BODY_TO_MIND;
import com.lineage.server.model.skill.skillmode.BONE_BREAK;
import com.lineage.server.model.skill.skillmode.CALL_CLAN;
import com.lineage.server.model.skill.skillmode.CALL_OF_NATURE;
import com.lineage.server.model.skill.skillmode.CANCELLATION;
import com.lineage.server.model.skill.skillmode.CONFUSION;
import com.lineage.server.model.skill.skillmode.CURE_POISON;
import com.lineage.server.model.skill.skillmode.CURSE_BLIND;
import com.lineage.server.model.skill.skillmode.CURSE_PARALYZE;
import com.lineage.server.model.skill.skillmode.DRAGON1;
import com.lineage.server.model.skill.skillmode.DRAGON2;
import com.lineage.server.model.skill.skillmode.DRAGON3;
import com.lineage.server.model.skill.skillmode.DRAGON4;
import com.lineage.server.model.skill.skillmode.DRAGON5;
import com.lineage.server.model.skill.skillmode.DRAGON6;
import com.lineage.server.model.skill.skillmode.DRAGON7;
import com.lineage.server.model.skill.skillmode.ELEMENTAL_FALL_DOWN;
import com.lineage.server.model.skill.skillmode.FOE_SLAYER;
import com.lineage.server.model.skill.skillmode.GREATER_ELEMENTAL;
import com.lineage.server.model.skill.skillmode.GREATER_RESURRECTION;
import com.lineage.server.model.skill.skillmode.HASTE;
import com.lineage.server.model.skill.skillmode.ILLUSION_AVATAR;
import com.lineage.server.model.skill.skillmode.LESSER_ELEMENTAL;
import com.lineage.server.model.skill.skillmode.MASS_TELEPORT;
import com.lineage.server.model.skill.skillmode.MIND_BREAK;
import com.lineage.server.model.skill.skillmode.MIRROR_IMAGE;
import com.lineage.server.model.skill.skillmode.MOVE_STOP;
import com.lineage.server.model.skill.skillmode.PANIC;
import com.lineage.server.model.skill.skillmode.PHANTASM;
import com.lineage.server.model.skill.skillmode.REMOVE_CURSE;
import com.lineage.server.model.skill.skillmode.RESIST_FEAR;
import com.lineage.server.model.skill.skillmode.RESURRECTION;
import com.lineage.server.model.skill.skillmode.RUN_CLAN;
import com.lineage.server.model.skill.skillmode.SHAPE_CHANGE;
import com.lineage.server.model.skill.skillmode.SHOCK_STUN;
import com.lineage.server.model.skill.skillmode.SOLID_CARRIAGE;
import com.lineage.server.model.skill.skillmode.STATUS_FREEZE;
import com.lineage.server.model.skill.skillmode.SUMMON_MONSTER;
import com.lineage.server.model.skill.skillmode.SkillMode;
import com.lineage.server.model.skill.skillmode.TELEPORT;
import com.lineage.server.model.skill.skillmode.TELEPORT_TO_MATHER;
import com.lineage.server.model.skill.skillmode.TRIPLE_ARROW;
import com.lineage.server.model.skill.skillmode.TRUE_TARGET;
import com.lineage.server.model.skill.skillmode.UNCANNY_DODGE;
import com.lineage.server.model.skill.skillmode.WIND_SHACKLE;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1SkillMode {
    private static L1SkillMode _instance;
    private static final Log _log = LogFactory.getLog(L1SkillMode.class);
    private static final Map<Integer, SkillMode> _skillMode = new HashMap();

    public static L1SkillMode get() {
        if (_instance == null) {
            _instance = new L1SkillMode();
        }
        return _instance;
    }

    public boolean isNotCancelable(int skillNum) {
        return skillNum == 12 || skillNum == 21 || skillNum == 78 || skillNum == 79 || skillNum == 87 || skillNum == 107 || skillNum == 88 || skillNum == 90 || skillNum == 91 || skillNum == 185 || skillNum == 190 || skillNum == 195 || skillNum == 4002 || skillNum == 4005 || skillNum == 4006 || skillNum == 4007 || skillNum == 4003 || skillNum == 195;
    }

    public void load() {
        try {
            _skillMode.put(5, new TELEPORT());
            _skillMode.put(69, new MASS_TELEPORT());
            _skillMode.put(43, new HASTE());
            _skillMode.put(44, new CANCELLATION());
            _skillMode.put(9, new CURE_POISON());
            _skillMode.put(37, new REMOVE_CURSE());
            _skillMode.put(51, new SUMMON_MONSTER());
            _skillMode.put(61, new RESURRECTION());
            _skillMode.put(67, new SHAPE_CHANGE());
            _skillMode.put(75, new GREATER_RESURRECTION());
            _skillMode.put(79, new ADVANCE_SPIRIT());
            _skillMode.put(33, new CURSE_PARALYZE());
            _skillMode.put(Integer.valueOf((int) L1SkillId.CURSE_PARALYZE2), new CURSE_PARALYZE());
            _skillMode.put(20, new CURSE_BLIND());
            _skillMode.put(40, new CURSE_BLIND());
            _skillMode.put(116, new CALL_CLAN());
            _skillMode.put(Integer.valueOf((int) L1SkillId.RUN_CLAN), new RUN_CLAN());
            _skillMode.put(113, new TRUE_TARGET());
            _skillMode.put(87, new SHOCK_STUN());
            _skillMode.put(90, new SOLID_CARRIAGE());
            _skillMode.put(165, new CALL_OF_NATURE());
            _skillMode.put(133, new ELEMENTAL_FALL_DOWN());
            _skillMode.put(Integer.valueOf((int) L1SkillId.BODY_TO_MIND), new BODY_TO_MIND());
            _skillMode.put(Integer.valueOf((int) L1SkillId.BLOODY_SOUL), new BLOODY_SOUL());
            _skillMode.put(Integer.valueOf((int) L1SkillId.TRIPLE_ARROW), new TRIPLE_ARROW());
            _skillMode.put(131, new TELEPORT_TO_MATHER());
            _skillMode.put(Integer.valueOf((int) L1SkillId.AQUA_PROTECTER), new AQUA_PROTECTER());
            _skillMode.put(162, new GREATER_ELEMENTAL());
            _skillMode.put(154, new LESSER_ELEMENTAL());
            _skillMode.put(167, new WIND_SHACKLE());
            _skillMode.put(106, new UNCANNY_DODGE());
            _skillMode.put(103, new CURSE_BLIND());
            _skillMode.put(185, new AWAKEN_ANTHARAS());
            _skillMode.put(190, new AWAKEN_FAFURION());
            _skillMode.put(195, new AWAKEN_VALAKAS());
            _skillMode.put(Integer.valueOf((int) L1SkillId.FOE_SLAYER), new FOE_SLAYER());
            _skillMode.put(Integer.valueOf((int) L1SkillId.BLOODLUST), new BLOODLUST());
            _skillMode.put(188, new RESIST_FEAR());
            _skillMode.put(202, new CONFUSION());
            _skillMode.put(212, new PHANTASM());
            _skillMode.put(213, new ARM_BREAKER());
            _skillMode.put(217, new PANIC());
            _skillMode.put(208, new BONE_BREAK());
            _skillMode.put(207, new MIND_BREAK());
            _skillMode.put(Integer.valueOf((int) L1SkillId.ILLUSION_AVATAR), new ILLUSION_AVATAR());
            _skillMode.put(201, new MIRROR_IMAGE());
            _skillMode.put(Integer.valueOf((int) L1SkillId.STATUS_FREEZE), new STATUS_FREEZE());
            _skillMode.put(Integer.valueOf((int) L1SkillId.MOVE_STOP), new MOVE_STOP());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON1), new DRAGON1());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON2), new DRAGON2());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON3), new DRAGON3());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON4), new DRAGON4());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON5), new DRAGON5());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON6), new DRAGON6());
            _skillMode.put(Integer.valueOf((int) L1SkillId.DRAGON7), new DRAGON7());
            _skillMode.put(Integer.valueOf((int) L1SkillId.ADLV80_1), new ADLV80_1());
            _skillMode.put(4010, new ADLV80_2());
            _skillMode.put(Integer.valueOf((int) L1SkillId.AGLV85_1X), new AGLV85_1X());
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public SkillMode getSkill(int skillid) {
        return _skillMode.get(Integer.valueOf(skillid));
    }
}
