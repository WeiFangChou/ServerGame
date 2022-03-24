package com.lineage.server.model.Instance;

import com.lineage.server.datatables.NPCTalkDataTable;
import com.lineage.server.model.L1AttackPc;
import com.lineage.server.model.L1NpcTalkData;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.serverpackets.S_NPCTalkReturn;
import com.lineage.server.templates.L1Npc;
import com.lineage.server.thread.GeneralThreadPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1TeleporterInstance extends L1NpcInstance {
    private static final Log _log = LogFactory.getLog(L1TeleporterInstance.class);
    private static final long serialVersionUID = 1;
    private boolean _isNowDely = false;

    public L1TeleporterInstance(L1Npc template) {
        super(template);
    }

    @Override // com.lineage.server.model.L1Object
    public void onAction(L1PcInstance pc) {
        try {
            new L1AttackPc(pc, this).action();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    @Override // com.lineage.server.model.L1Object
    public void onTalkAction(L1PcInstance player) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        int npcid = getNpcTemplate().get_npcId();
        player.getQuest();
        String htmlid = null;
        if (talking != null) {
            if (npcid == 50001) {
                if (player.isElf()) {
                    htmlid = "barnia3";
                } else if (player.isKnight() || player.isCrown()) {
                    htmlid = "barnia2";
                } else if (player.isWizard() || player.isDarkelf()) {
                    htmlid = "barnia1";
                }
            }
            if (htmlid != null) {
                player.sendPackets(new S_NPCTalkReturn(objid, htmlid));
            } else if (player.getLawful() < -1000) {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 2));
            } else {
                player.sendPackets(new S_NPCTalkReturn(talking, objid, 1));
            }
        }
    }

    @Override // com.lineage.server.model.Instance.L1NpcInstance
    public void onFinalAction(L1PcInstance player, String action) {
        int objid = getId();
        L1NpcTalkData talking = NPCTalkDataTable.get().getTemplate(getNpcTemplate().get_npcId());
        if (action.equalsIgnoreCase("teleportURL")) {
            player.sendPackets(new S_NPCTalkReturn(objid, new L1NpcHtml(talking.getTeleportURL())));
        } else if (action.equalsIgnoreCase("teleportURLA")) {
            player.sendPackets(new S_NPCTalkReturn(objid, new L1NpcHtml(talking.getTeleportURLA())));
        }
        if (action.startsWith("teleport ")) {
            doFinalAction(player, action);
        }
    }

    private void doFinalAction(L1PcInstance player, String action) {
        int objid = getId();
        int npcid = getNpcTemplate().get_npcId();
        boolean isTeleport = true;
        if (npcid == 50043) {
            if (this._isNowDely) {
                isTeleport = false;
            }
        } else if (npcid == 50625 && this._isNowDely) {
            isTeleport = false;
        }
        if (isTeleport) {
            try {
                if (action.equalsIgnoreCase("teleport 29")) {
                    L1Teleport.teleport(player, 32723, 32850, (short) 2000, 2, true);
                    L1Teleport.teleport(null, 32750, 32851, (short) 2000, 6, true);
                    L1Teleport.teleport(null, 32878, 32980, (short) 2000, 6, true);
                    L1Teleport.teleport(null, 32876, 33003, (short) 2000, 0, true);
                    GeneralThreadPool.get().execute(new TeleportDelyTimer());
                } else if (action.equalsIgnoreCase("teleport barlog")) {
                    L1Teleport.teleport(player, 32755, 32844, (short) 2002, 5, true);
                    GeneralThreadPool.get().execute(new TeleportDelyTimer());
                }
            } catch (Exception ignored) {
            }
        }
        player.sendPackets(new S_NPCTalkReturn(objid, (String) null));
    }

    /* access modifiers changed from: package-private */
    public class TeleportDelyTimer implements Runnable {
        public TeleportDelyTimer() {
        }

        public void run() {
            try {
                L1TeleporterInstance.this._isNowDely = true;
                Thread.sleep(900000);
            } catch (Exception e) {
                L1TeleporterInstance.this._isNowDely = false;
            }
            L1TeleporterInstance.this._isNowDely = false;
        }
    }
}
