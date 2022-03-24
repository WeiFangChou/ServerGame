package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.timecontroller.event.ranking.RankingHeroTimer;

public class Npc_HeroRanking extends NpcExecutor {
    private Npc_HeroRanking() {
    }

    public static NpcExecutor get() {
        return new Npc_HeroRanking();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_h_1"));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        String[] userName = new String[11];
        if (cmd.equals("c")) {
            String[] names = RankingHeroTimer.userNameC();
            for (int i = 0; i < names.length; i++) {
                userName[i] = names[i];
            }
            userName[10] = "王族";
        } else if (cmd.equals("k")) {
            String[] names2 = RankingHeroTimer.userNameK();
            for (int i2 = 0; i2 < names2.length; i2++) {
                userName[i2] = names2[i2];
            }
            userName[10] = "騎士";
        } else if (cmd.equals("e")) {
            String[] names3 = RankingHeroTimer.userNameE();
            for (int i3 = 0; i3 < names3.length; i3++) {
                userName[i3] = names3[i3];
            }
            userName[10] = "精靈";
        } else if (cmd.equals("w")) {
            String[] names4 = RankingHeroTimer.userNameW();
            for (int i4 = 0; i4 < names4.length; i4++) {
                userName[i4] = names4[i4];
            }
            userName[10] = "法師";
        } else if (cmd.equals("d")) {
            String[] names5 = RankingHeroTimer.userNameD();
            for (int i5 = 0; i5 < names5.length; i5++) {
                userName[i5] = names5[i5];
            }
            userName[10] = "黑暗精靈";
        } else if (cmd.equals("g")) {
            String[] names6 = RankingHeroTimer.userNameG();
            for (int i6 = 0; i6 < names6.length; i6++) {
                userName[i6] = names6[i6];
            }
            userName[10] = "龍騎士";
        } else if (cmd.equals("i")) {
            String[] names7 = RankingHeroTimer.userNameI();
            for (int i7 = 0; i7 < names7.length; i7++) {
                userName[i7] = names7[i7];
            }
            userName[10] = "幻術師";
        } else if (cmd.equals("a")) {
            String[] names8 = RankingHeroTimer.userNameAll();
            for (int i8 = 0; i8 < names8.length; i8++) {
                userName[i8] = names8[i8];
            }
            userName[10] = "全職業";
        }
        if (userName != null) {
            pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_h_2", userName));
        }
    }
}
