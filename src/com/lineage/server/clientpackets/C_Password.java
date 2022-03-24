package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.lock.AccountReading;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.serverpackets.S_RetrieveElfList;
import com.lineage.server.serverpackets.S_RetrieveList;
import com.lineage.server.serverpackets.S_RetrievePledgeList;
import com.lineage.server.serverpackets.S_ServerMessage;
import com.lineage.server.templates.L1Account;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_Password extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_Password.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            int mode = readC();
            int olepwd = readD();
            int newpwd = readD();
            if (olepwd < 0) {
                olepwd = -256;
            }
            L1PcInstance pc = client.getActiveChar();
            int srcpwd = client.getAccount().get_warehouse();
            int tmpId = pc.getTempID();
            if (olepwd != srcpwd) {
                pc.sendPackets(new S_ServerMessage(835));
                client.set_error(client.get_error() + 1);
                _log.error(String.valueOf(pc.getName()) + " 倉庫密碼輸入錯誤!!( " + client.get_error() + " 次)");
                return;
            }
            boolean isRepas = false;
            boolean isNpc = false;
            L1Object obj = World.get().findObject(newpwd);
            if (obj == null) {
                obj = World.get().findObject(tmpId);
                if (obj != null) {
                    isNpc = true;
                } else {
                    isRepas = true;
                }
            } else {
                isNpc = true;
            }
            if (isNpc) {
                if (olepwd != -256) {
                    if (obj instanceof L1NpcInstance) {
                        L1NpcInstance npc = (L1NpcInstance) obj;
                        int difflocx = Math.abs(pc.getX() - npc.getX());
                        int difflocy = Math.abs(pc.getY() - npc.getY());
                        if (difflocx > 3 || difflocy > 3) {
                            if (tmpId != 0) {
                                obj = World.get().findObject(tmpId);
                                npc = (L1NpcInstance) obj;
                            } else {
                                stopAction(client, pc);
                                over();
                                return;
                            }
                        }
                        switch (npc.getNpcId()) {
                            case 60028:
                                if (pc.getLevel() >= 5) {
                                    pc.sendPackets(new S_RetrieveElfList(newpwd, pc));
                                    break;
                                }
                                break;
                            default:
                                if (pc.getLevel() >= 5) {
                                    switch (mode) {
                                        case 1:
                                            pc.sendPackets(new S_RetrieveList(newpwd, pc));
                                            break;
                                        case 2:
                                            pc.sendPackets(new S_RetrievePledgeList(newpwd, pc));
                                            break;
                                    }
                                }
                                break;
                        }
                    }
                } else {
                    isRepas = true;
                }
            }
            if (isRepas) {
                if (newpwd == srcpwd) {
                    pc.sendPackets(new S_ServerMessage(342));
                    stopAction(client, pc);
                    over();
                    return;
                } else if (obj == null && newpwd < 1000000) {
                    L1Account account = client.getAccount();
                    account.set_warehouse(newpwd);
                    AccountReading.get().updateWarehouse(account.get_login(), newpwd);
                }
            }
            stopAction(client, pc);
            over();
        } catch (Exception ignored) {
        } finally {
            over();
        }
    }

    private void stopAction(ClientExecutor client, L1PcInstance pc) {
        pc.setTempID(0);
        client.set_error(0);
    }

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public String getType() {
        return getClass().getSimpleName();
    }
}
