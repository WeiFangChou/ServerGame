package com.lineage.server.command.executor;

import com.lineage.config.Config;
import com.lineage.server.datatables.lock.AuctionBoardReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.templates.L1AuctionBoardTmp;
import com.lineage.server.world.World;
import java.util.Calendar;
import java.util.TimeZone;

public class L1Board_House implements L1CommandExecutor {
    private L1Board_House() {
    }

    public static L1CommandExecutor getInstance() {
        return new L1Board_House();
    }

    @Override // com.lineage.server.command.executor.L1CommandExecutor
    public void execute(L1PcInstance pc, String cmdName, String arg) {
        try {
            Calendar DeadTime = getRealTime();
            DeadTime.add(12, Integer.parseInt(arg));
            for (L1AuctionBoardTmp board : AuctionBoardReading.get().getAuctionBoardTableList().values()) {
                board.setDeadline(DeadTime);
                AuctionBoardReading.get().updateAuctionBoard(board);
            }
            World.get().broadcastPacketToAll(new S_SystemMessage("血盟小屋將在" + arg + "分鐘後結標，需要購買的請盡快下標。"));
        } catch (Exception e) {
            pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " 分鐘數"));
        }
    }

    private Calendar getRealTime() {
        return Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
    }
}
