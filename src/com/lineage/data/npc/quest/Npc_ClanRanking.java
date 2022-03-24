package com.lineage.data.npc.quest;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.echo.OpcodesClient;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Clan;
import com.lineage.server.model.L1War;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_Message_YN;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.timecontroller.event.ranking.RankingClanTimer;
import com.lineage.server.world.World;
import com.lineage.server.world.WorldClan;
import com.lineage.server.world.WorldWar;
import java.util.List;

public class Npc_ClanRanking extends NpcExecutor {
    private Npc_ClanRanking() {
    }

    public static NpcExecutor get() {
        return new Npc_ClanRanking();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        String[] userName = RankingClanTimer.userName();
        String[] out = new String[20];
        for (int i = 0; i < userName.length; i++) {
            if (!userName[i].equals(" ")) {
                out[i] = String.valueOf(userName[i].replace(",", "(")) + ")";
                out[i + 10] = "對" + out[i] + "宣戰";
            }
        }
        pc.sendPackets(new S_NPCTalkReturn(npc.getId(), "y_c_1", out));
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        if (pc.getClanid() == 0) {
            pc.sendPackets(new S_ServerMessage(1064));
            return;
        }
        L1Clan clan = pc.getClan();
        if (clan == null) {
            pc.sendPackets(new S_ServerMessage(1064));
        } else if (!pc.isCrown()) {
            pc.sendPackets(new S_ServerMessage(478));
        } else if (clan.getLeaderId() != pc.getId()) {
            pc.sendPackets(new S_ServerMessage(518));
        } else {
            String[] clanNameList = RankingClanTimer.userName();
            int i = -1;
            if (cmd.matches("[0-9]+")) {
                i = Integer.valueOf(cmd).intValue();
            }
            if (i != -1) {
                String tg = clanNameList[i];
                if (!tg.equals(" ")) {
                    String tgClanName = tg.split(",")[0];
                    L1Clan clanX = WorldClan.get().getClan(tgClanName);
                    if (clanX.getCastleId() != 0) {
                        pc.sendPackets(new S_ServerMessage(166, "不能對傭有城堡的血盟宣戰!"));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                    } else if (clanX.getClanId() != pc.getClanid()) {
                        List<L1War> warList = WorldWar.get().getWarList();
                        String clanName = clan.getClanName();
                        for (L1War war : warList) {
                            if (!war.checkClanInSameWar(clanName, tgClanName)) {
                                return;
                            }
                            if (war.checkClanInWar(tgClanName)) {
                                pc.sendPackets(new S_ServerMessage((int) OpcodesClient.C_OPCODE_CHARRESET, tgClanName));
                                return;
                            }
                        }
                        L1PcInstance enemyLeader = World.get().getPlayer(clanX.getLeaderName());
                        if (enemyLeader == null) {
                            pc.sendPackets(new S_ServerMessage(218, tgClanName));
                            return;
                        }
                        enemyLeader.setTempID(pc.getId());
                        enemyLeader.sendPackets(new S_Message_YN(217, clanName, pc.getName()));
                        pc.sendPackets(new S_CloseList(pc.getId()));
                    }
                }
            }
        }
    }
}
