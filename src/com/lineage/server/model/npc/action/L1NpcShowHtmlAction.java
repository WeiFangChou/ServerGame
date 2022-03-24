package com.lineage.server.model.npc.action;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.utils.IterableElementList;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class L1NpcShowHtmlAction extends L1NpcXmlAction {
    private final String[] _args;
    private final String _htmlId;

    public L1NpcShowHtmlAction(Element element) {
        super(element);
        this._htmlId = element.getAttribute("HtmlId");
        NodeList list = element.getChildNodes();
        ArrayList<String> dataList = new ArrayList<>();
        Iterator<Element> it = new IterableElementList(list).iterator();
        while (it.hasNext()) {
            Element elem = it.next();
            if (elem.getNodeName().equalsIgnoreCase("Data")) {
                dataList.add(elem.getAttribute("Value"));
            }
        }
        this._args = (String[]) dataList.toArray(new String[dataList.size()]);
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj, byte[] args) {
        return new L1NpcHtml(this._htmlId, this._args);
    }

    @Override // com.lineage.server.model.npc.action.L1NpcXmlAction, com.lineage.server.model.npc.action.L1NpcAction
    public void execute(String actionName, String npcid) {
    }
}
