package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.skill.L1SkillId;
import com.lineage.server.serverpackets.OpcodesServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_LoginToServerOK extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_LoginToServerOK.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int type = readC();
            int button = readC();
            L1PcInstance pc = client.getActiveChar();
            switch (type) {
                case 0:
                    if (button != 0) {
                        if (button == 1) {
                            pc.setShowWorldChat(true);
                            break;
                        }
                    } else {
                        pc.setShowWorldChat(false);
                        break;
                    }
                    break;
                case 2:
                    if (button != 0) {
                        if (button == 1) {
                            pc.setCanWhisper(true);
                            break;
                        }
                    } else {
                        pc.setCanWhisper(false);
                        break;
                    }
                    break;
                case 6:
                    if (button != 0) {
                        if (button == 1) {
                            pc.setShowTradeChat(true);
                            break;
                        }
                    } else {
                        pc.setShowTradeChat(false);
                        break;
                    }
                    break;
                case 255:
                    switch (button) {
                        case 90:
                        case 122:
                            pc.setShowWorldChat(false);
                            pc.setCanWhisper(false);
                            break;
                        case L1SkillId.COUNTER_BARRIER /*{ENCODED_INT: 91}*/:
                        case OpcodesServer.S_OPCODE_WAR /*{ENCODED_INT: 123}*/:
                            pc.setShowWorldChat(true);
                            pc.setCanWhisper(false);
                            break;
                        case 94:
                        case 126:
                            pc.setShowWorldChat(false);
                            pc.setCanWhisper(true);
                            break;
                        case 95:
                        case 127:
                            pc.setShowWorldChat(true);
                            pc.setCanWhisper(true);
                            break;
                    }
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
