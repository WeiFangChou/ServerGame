package com.lineage.server.model.npc.action;

import com.lineage.server.datatables.NpcTeleportTable;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Location;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.L1Teleport;
import com.lineage.server.model.item.L1ItemId;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.serverpackets.S_ServerMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

public class L1NpcTeleportAction extends L1NpcXmlAction {
    private static final Log _log = LogFactory.getLog(L1NpcTeleportAction.class);
    private final boolean _effect;
    private final int _heading;
    private final L1Location _loc;
    private final int _price;

    public L1NpcTeleportAction(Element element) {
        super(element);
        this._loc = new L1Location(L1NpcXmlParser.getIntAttribute(element, "X", -1), L1NpcXmlParser.getIntAttribute(element, "Y", -1), L1NpcXmlParser.getIntAttribute(element, "Map", -1));
        this._heading = L1NpcXmlParser.getIntAttribute(element, "Heading", 5);
        this._price = L1NpcXmlParser.getIntAttribute(element, "Price", 0);
        this._effect = L1NpcXmlParser.getBoolAttribute(element, "Effect", true);
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
        try {
            if (!pc.getInventory().checkItem(L1ItemId.ADENA, (long) this._price)) {
                pc.sendPackets(new S_ServerMessage(337, "$4"));
                return L1NpcHtml.HTML_CLOSE;
            }
            pc.getInventory().consumeItem(L1ItemId.ADENA, (long) this._price);
            L1Teleport.teleport(pc, this._loc.getX(), this._loc.getY(), (short) this._loc.getMapId(), this._heading, this._effect);
            return null;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public void execute(String actionName, String npcid) {
        NpcTeleportTable.get().set(actionName, this._loc.getX(), this._loc.getY(), this._loc.getMapId(), this._price, npcid);
    }
}
