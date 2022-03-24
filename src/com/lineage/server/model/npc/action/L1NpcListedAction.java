package com.lineage.server.model.npc.action;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.npc.L1NpcHtml;
import java.util.List;
import org.w3c.dom.Element;

public class L1NpcListedAction extends L1NpcXmlAction {
    private List<L1NpcAction> _actions;

    public L1NpcListedAction(Element element) {
        super(element);
        this._actions = L1NpcXmlParser.listActions(element);
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) throws Exception {
        L1NpcHtml r;
        L1NpcHtml result = null;
        for (L1NpcAction action : this._actions) {
            if (action.acceptsRequest(actionName, pc, obj) && (r = action.execute(actionName, pc, obj, args)) != null) {
                result = r;
            }
        }
        return result;
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, long amount) throws Exception {
        L1NpcHtml r;
        L1NpcHtml result = null;
        for (L1NpcAction action : this._actions) {
            if (action.acceptsRequest(actionName, pc, obj) && (r = action.executeWithAmount(actionName, pc, obj, amount)) != null) {
                result = r;
            }
        }
        return result;
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public void execute(String actionName, String npcid) {
        for (L1NpcAction action : this._actions) {
            if (action.acceptsRequest(actionName, null, null)) {
                action.execute(actionName, npcid);
            }
        }
    }
}
