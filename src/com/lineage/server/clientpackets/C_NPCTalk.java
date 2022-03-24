package com.lineage.server.clientpackets;

import com.lineage.echo.ClientExecutor;
import com.lineage.server.datatables.NpcActionTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.model.npc.action.L1NpcAction;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.world.World;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class C_NPCTalk extends ClientBasePacket {
    private static final Log _log = LogFactory.getLog(C_NPCTalk.class);

    @Override // com.lineage.server.clientpackets.ClientBasePacket
    public void start(byte[] decrypt, ClientExecutor client) {
        try {
            read(decrypt);
            L1PcInstance pc = client.getActiveChar();
            if (!pc.isDead()) {
                if (pc.isTeleport()) {
                    over();
                } else if (pc.isPrivateShop()) {
                    over();
                } else {
                    int objid = readD();
                    L1Object obj = World.get().findObject(objid);
                    pc.get_otherList().clear();
                    pc.get_other().set_gmHtml(null);
                    if (obj == null || pc == null) {
                        _log.error("指定的OBJID不存在: " + objid);
                    } else {
                        pc.setTempID(objid);
                        if (obj instanceof L1NpcInstance) {
                            L1NpcInstance npc = (L1NpcInstance) obj;
                            if (npc.TALK != null) {
                                npc.TALK.talk(pc, npc);
                                over();
                                return;
                            }
                        }
                        L1NpcAction action = NpcActionTable.getInstance().get(pc, obj);
                        if (action != null) {
                            L1NpcHtml html = action.execute("", pc, obj, new byte[0]);
                            if (html != null) {
                                pc.sendPackets(new S_NPCTalkReturn(obj.getId(), html));
                            }
                            over();
                            return;
                        }
                        obj.onTalkAction(pc);
                    }
                    over();
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
