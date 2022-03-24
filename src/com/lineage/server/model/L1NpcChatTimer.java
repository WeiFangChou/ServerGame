package com.lineage.server.model;

import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.serverpackets.S_NpcChat;
import com.lineage.server.serverpackets.S_NpcChatGlobal;
import com.lineage.server.serverpackets.S_NpcChatShouting;
import com.lineage.server.templates.L1NpcChat;
import com.lineage.server.world.World;
import java.util.Iterator;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1NpcChatTimer extends TimerTask {
    private static final Log _log = LogFactory.getLog(L1NpcChatTimer.class);
    private final L1NpcInstance _npc;
    private final L1NpcChat _npcChat;

    public L1NpcChatTimer(L1NpcInstance npc, L1NpcChat npcChat) {
        this._npc = npc;
        this._npcChat = npcChat;
    }

    public void run() {
        try {
            if (this._npc != null && this._npcChat != null && this._npc.getHiddenStatus() == 0 && !this._npc._destroyed) {
                int chatTiming = this._npcChat.getChatTiming();
                int chatInterval = this._npcChat.getChatInterval();
                boolean isShout = this._npcChat.isShout();
                boolean isWorldChat = this._npcChat.isWorldChat();
                String chatId1 = this._npcChat.getChatId1();
                String chatId2 = this._npcChat.getChatId2();
                String chatId3 = this._npcChat.getChatId3();
                String chatId4 = this._npcChat.getChatId4();
                String chatId5 = this._npcChat.getChatId5();
                if (!chatId1.equals("")) {
                    chat(this._npc, chatTiming, chatId1, isShout, isWorldChat);
                }
                if (!chatId2.equals("")) {
                    Thread.sleep((long) chatInterval);
                    chat(this._npc, chatTiming, chatId2, isShout, isWorldChat);
                }
                if (!chatId3.equals("")) {
                    Thread.sleep((long) chatInterval);
                    chat(this._npc, chatTiming, chatId3, isShout, isWorldChat);
                }
                if (!chatId4.equals("")) {
                    Thread.sleep((long) chatInterval);
                    chat(this._npc, chatTiming, chatId4, isShout, isWorldChat);
                }
                if (!chatId5.equals("")) {
                    Thread.sleep((long) chatInterval);
                    chat(this._npc, chatTiming, chatId5, isShout, isWorldChat);
                }
            }
        } catch (Throwable e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    private void chat(L1NpcInstance npc, int chatTiming, String chatId, boolean isShout, boolean isWorldChat) {
        L1PcInstance pc;
        if (chatTiming == 0 && npc.isDead()) {
            return;
        }
        if (chatTiming == 1 && !npc.isDead()) {
            return;
        }
        if (chatTiming != 2 || !npc.isDead()) {
            if (!isShout) {
                npc.broadcastPacketX8(new S_NpcChat(npc, chatId));
            } else {
                npc.wideBroadcastPacket(new S_NpcChatShouting(npc, chatId));
            }
            if (isWorldChat) {
                Iterator<L1PcInstance> it = World.get().getAllPlayers().iterator();
                if (it.hasNext() && (pc = it.next()) != null) {
                    pc.sendPackets(new S_NpcChatGlobal(npc, chatId));
                }
            }
        }
    }
}
