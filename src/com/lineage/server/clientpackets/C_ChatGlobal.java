package com.lineage.server.clientpackets;

import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.ItemTable;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChatGlobal;
import com.lineage.server.serverpackets.S_ChatTransaction;
import com.lineage.server.serverpackets.S_PacketBox;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.world.World;
import java.util.Calendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ChatGlobal extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ChatGlobal.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (decrypt.length > 108) {
                _log.warn("人物:" + pc.getName() + "對話(廣播)長度超過限制:" + client.getIp().toString());
                client.set_error(client.get_error() + 1);
                return;
            }
            boolean isStop = false;
            boolean errMessage = false;
            if (pc.hasSkillEffect(64) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(161) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE) && !pc.isGm()) {
                isStop = true;
            }
            if (pc.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
                isStop = true;
                errMessage = true;
            }
            if (isStop) {
                if (errMessage) {
                    pc.sendPackets(new S_ServerMessage(242));
                }
                over();
                return;
            }
            if (!pc.isGm()) {
                if (pc.getLevel() < ConfigAlt.GLOBAL_CHAT_LEVEL) {
                    pc.sendPackets(new S_ServerMessage(195, String.valueOf((int) ConfigAlt.GLOBAL_CHAT_LEVEL)));
                    over();
                    return;
                } else if (!World.get().isWorldChatElabled()) {
                    pc.sendPackets(new S_ServerMessage(510));
                    over();
                    return;
                }
            }
            if (ConfigOther.SET_GLOBAL_TIME > 0 && !pc.isGm()) {
                long time = Calendar.getInstance().getTimeInMillis() / 1000;
                if (time - pc.get_global_time() < ((long) ConfigOther.SET_GLOBAL_TIME)) {
                    over();
                    return;
                }
                pc.set_global_time(time);
            }
            int chatType = readC();
            String chatText = readS();
            switch (chatType) {
                case 3:
                    chatType_3(pc, chatText);
                    break;
                case 12:
                    chatType_12(pc, chatText);
                    break;
            }
            if (!pc.isGm()) {
                pc.checkChatInterval();
            }
            over();
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void chatType_12(L1PcInstance pc, String chatText) {
        S_ChatTransaction packet = new S_ChatTransaction(pc, chatText);
        String name = pc.getName();
        for (L1PcInstance listner : World.get().getAllPlayers()) {
            if (!listner.getExcludingList().contains(name) && listner.isShowTradeChat()) {
                listner.sendPackets(packet);
            }
        }
        if (ConfigRecord.LOGGING_CHAT_BUSINESS) {
            LogChatReading.get().noTarget(pc, chatText, 12);
        }
    }

    private void chatType_3(L1PcInstance pc, String chatText) throws Exception {
        S_ChatGlobal packet = new S_ChatGlobal(pc, chatText);
        if (pc.isGm()) {
            World.get().broadcastPacketToAll(packet);
            return;
        }
        String name = pc.getName();
        if (!pc.isGm()) {
            switch (ConfigOther.SET_GLOBAL) {
                case 0:
                    if (pc.get_food() >= 6) {
                        pc.set_food(pc.get_food() - ConfigOther.SET_GLOBAL_COUNT);
                        pc.sendPackets(new S_PacketBox(11, pc.get_food()));
                        break;
                    } else {
                        pc.sendPackets(new S_ServerMessage(462));
                        return;
                    }
                default:
                    L1ItemInstance item = pc.getInventory().checkItemX(ConfigOther.SET_GLOBAL, (long) ConfigOther.SET_GLOBAL_COUNT);
                    if (item != null) {
                        pc.getInventory().removeItem(item, (long) ConfigOther.SET_GLOBAL_COUNT);
                        break;
                    } else {
                        pc.sendPackets(new S_ServerMessage(337, ItemTable.get().getTemplate(ConfigOther.SET_GLOBAL).getName()));
                        return;
                    }
            }
        }
        for (L1PcInstance listner : World.get().getAllPlayers()) {
            if (!listner.getExcludingList().contains(name) && listner.isShowWorldChat()) {
                listner.sendPackets(packet);
            }
        }
        if (ConfigRecord.LOGGING_CHAT_WORLD) {
            LogChatReading.get().noTarget(pc, chatText, 3);
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
