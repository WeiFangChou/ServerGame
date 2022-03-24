package com.lineage.server.command.executor;

import com.lineage.server.datatables.SkillsTable;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1PolyMorph;
import com.lineage.server.model.skill.L1BuffUtil;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.model.skill.L1SkillUse;
import com.lineage.server.serverpackets.S_PacketBoxGm;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.thread.GeneralThreadPool;
import com.lineage.server.world.World;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1AllBuff implements L1CommandExecutor {
    private static final Log _log = LogFactory.getLog(L1AllBuff.class);
    private static final int[] allBuffSkill = {26, 42, 48, 55, 68, 79, 88, 89, 90, L1SkillId.BURNING_SPIRIT, 104, 105, 106, 111, L1SkillId.GLOWING_AURA, 117, 129, 137, L1SkillId.ELEMENTAL_PROTECTION, L1SkillId.AQUA_PROTECTER, L1SkillId.BURNING_WEAPON, L1SkillId.IRON_SKIN, L1SkillId.EXOTIC_VITALIZE, 170, L1SkillId.ELEMENTAL_FIRE, L1SkillId.SOUL_OF_FLAME, L1SkillId.ADDITIONAL_FIRE, L1SkillId.DRAGON_SKIN, 182};

    private L1AllBuff() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1AllBuff();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            String name = new StringTokenizer(arg).nextToken();
            if (name.equalsIgnoreCase("all")) {
                GeneralThreadPool.get().execute(new AllBuffRunnable(this, null));
            } else if (name.equalsIgnoreCase("gm")) {
                startGm(pc);
            } else {
                L1PcInstance target = World.get().getPlayer(name);
                if (target == null) {
                    pc.sendPackets(new S_ServerMessage(73, name));
                } else {
                    startPc(target);
                }
            }
        } catch (Exception e) {
            pc.sendPackets(new S_PacketBoxGm(pc, 4));
        }
    }

    public static void startPc(L1PcInstance target) {
        L1BuffUtil.haste(target, 3600000);
        L1BuffUtil.brave(target, 3600000);
        L1ItemInstance weapon = target.getWeapon();
        if (weapon != null) {
            int polyid = -1;
            switch (weapon.getItem().getType()) {
                case 1:
                case 2:
                case 3:
                case 6:
                case 15:
                    polyid = 6276;
                    break;
                case 4:
                case 10:
                case 13:
                    polyid = 6278;
                    break;
                case 5:
                case 14:
                case 18:
                    polyid = 7341;
                    break;
                case 7:
                case 16:
                case 17:
                    polyid = 6277;
                    break;
                case 11:
                case 12:
                    polyid = 6282;
                    break;
            }
            if (polyid != -1) {
                L1PolyMorph.doPoly(target, polyid, 7200, 1);
            }
        }
        for (int i = 0; i < allBuffSkill.length; i++) {
            new L1SkillUse().handleCommands(target, allBuffSkill[i], target.getId(), target.getX(), target.getY(), SkillsTable.get().getTemplate(allBuffSkill[i]).getBuffDuration(), 4);
        }
    }

    public static void startGm(L1PcInstance target) {
        L1BuffUtil.haste(target, 3600000);
        L1BuffUtil.brave(target, 3600000);
        L1PolyMorph.doPoly(target, 5641, 7200, 2);
        for (int i = 0; i < allBuffSkill.length; i++) {
            new L1SkillUse().handleCommands(target, allBuffSkill[i], target.getId(), target.getX(), target.getY(), SkillsTable.get().getTemplate(allBuffSkill[i]).getBuffDuration(), 4);
        }
    }

    private class AllBuffRunnable implements Runnable {
        private AllBuffRunnable() {
        }

        /* synthetic */ AllBuffRunnable(L1AllBuff l1AllBuff, AllBuffRunnable allBuffRunnable) {
            this();
        }

        public void run() {
            try {
                for (L1PcInstance tgpc : World.get().getAllPlayers()) {
                    L1AllBuff.startPc(tgpc);
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                L1AllBuff._log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
