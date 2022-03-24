package com.lineage.server.clientpackets;

import com.eric.gui.J_Main;
import com.lineage.config.ConfigAlt;
import com.lineage.config.ConfigOther;
import com.lineage.config.ConfigRecord;
import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.LogChatReading;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.S_ChatWhisperFrom;
import com.lineage.server.serverpackets.S_ChatWhisperTo;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.serverpackets.S_SystemMessage;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_ChatWhisper extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_ChatWhisper.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance whisperFrom = client.getActiveChar();
            if (decrypt.length > 108) {
                _log.warn("人物:" + whisperFrom.getName() + "對話(密語)長度超過限制:" + client.getIp().toString());
                client.set_error(client.get_error() + 1);
            } else if (whisperFrom.hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
                whisperFrom.sendPackets(new S_ServerMessage(242));
                over();
            } else if (whisperFrom.getLevel() >= ConfigAlt.WHISPER_CHAT_LEVEL || whisperFrom.isGm()) {
                String targetName = readS();
                String text = readS();
                L1PcInstance whisperTo = World.get().getPlayer(targetName);
                if (whisperTo == null) {
                    whisperFrom.sendPackets(new S_ServerMessage(73, targetName));
                    over();
                } else if (whisperTo.equals(whisperFrom)) {
                    over();
                } else if (whisperTo.getExcludingList().contains(whisperFrom.getName())) {
                    whisperFrom.sendPackets(new S_ServerMessage(117, whisperTo.getName()));
                    over();
                } else if (!whisperTo.isCanWhisper()) {
                    whisperFrom.sendPackets(new S_ServerMessage((int) L1SkillId.CUBE_IGNITION, whisperTo.getName()));
                    over();
                } else {
                    if (ConfigOther.GM_OVERHEARD) {
                        for (L1Object visible : World.get().getAllPlayers()) {
                            if (visible instanceof L1PcInstance) {
                                L1PcInstance GM = (L1PcInstance) visible;
                                if (GM.isGm() && whisperFrom.getId() != GM.getId()) {
                                    GM.sendPackets(new S_SystemMessage("【密語】" + whisperFrom.getName() + "對" + targetName + ":" + text));
                                }
                            }
                        }
                    }
                    whisperFrom.sendPackets(new S_ChatWhisperTo(whisperTo, text));
                    whisperTo.sendPackets(new S_ChatWhisperFrom(whisperFrom, text));
                    if (ConfigRecord.LOGGING_CHAT_WHISPER) {
                        LogChatReading.get().isTarget(whisperFrom, whisperTo, text, 9);
                    }
                    if (ConfigOther.GUI) {
                        J_Main.getInstance().addPrivateChat(whisperFrom.getName(), whisperTo.getName(), text);
                    }
                    over();
                }
            } else {
                whisperFrom.sendPackets(new S_ServerMessage(404, String.valueOf((int) ConfigAlt.WHISPER_CHAT_LEVEL)));
                over();
            }
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
