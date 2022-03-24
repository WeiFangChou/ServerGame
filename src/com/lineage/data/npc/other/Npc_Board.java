package com.lineage.data.npc.other;

import com.lineage.data.executor.NpcExecutor;
import com.lineage.server.datatables.lock.BoardReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_Board;
import com.lineage.server.serverpackets.S_BoardRead;
import com.lineage.server.serverpackets.S_CloseList;
import com.lineage.server.serverpackets.S_ServerMessage;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Npc_Board extends NpcExecutor {
    private static final Log _log = LogFactory.getLog(Npc_Board.class);

    private Npc_Board() {
    }

    public static NpcExecutor get() {
        return new Npc_Board();
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public int type() {
        return 3;
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void talk(L1PcInstance pc, L1NpcInstance npc) {
        try {
            pc.sendPackets(new S_Board(npc));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.data.executor.NpcExecutor
    public void action(L1PcInstance pc, L1NpcInstance npc, String cmd, long amount) {
        try {
            if (cmd.equalsIgnoreCase("n")) {
                if (BoardReading.get().getBoardTable((int) amount) != null) {
                    pc.sendPackets(new S_Board(npc, (int) amount));
                }
            } else if (cmd.equalsIgnoreCase("r")) {
                if (BoardReading.get().getBoardTable((int) amount) != null) {
                    pc.sendPackets(new S_BoardRead((int) amount));
                } else {
                    pc.sendPackets(new S_ServerMessage(1243));
                }
            } else if (cmd.equalsIgnoreCase("d")) {
                if (BoardReading.get().getBoardTable((int) amount) != null) {
                    BoardReading.get().deleteTopic((int) amount);
                } else {
                    pc.sendPackets(new S_ServerMessage(1243));
                }
            } else if (cmd.equalsIgnoreCase("w")) {
                String title = pc.get_board_title();
                String content = pc.get_board_content();
                if (pc.getInventory().consumeItem(L1ItemId.ADENA, 300)) {
                    BoardReading.get().writeTopic(pc, new SimpleDateFormat("yyyy/MM/dd").format(Long.valueOf(System.currentTimeMillis())), title, content);
                    pc.set_board_title(null);
                    pc.set_board_content(null);
                } else {
                    pc.sendPackets(new S_ServerMessage((int) L1SkillId.SHOCK_SKIN));
                }
            }
            if (0 != 0) {
                pc.sendPackets(new S_CloseList(pc.getId()));
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
