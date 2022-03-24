package com.lineage.server.model.npc.action;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

public class L1NpcActionFactory {
    private static Map<String, Constructor<?>> _actions = new HashMap();
    private static final Log _log = LogFactory.getLog(L1NpcActionFactory.class);

    static {
        try {
            _actions.put("Action", loadConstructor(L1NpcListedAction.class));
            _actions.put("MakeItem", loadConstructor(L1NpcMakeItemAction.class));
            _actions.put("ShowHtml", loadConstructor(L1NpcShowHtmlAction.class));
            _actions.put("SetQuest", loadConstructor(L1NpcSetQuestAction.class));
            _actions.put("Teleport", loadConstructor(L1NpcTeleportAction.class));
        } catch (NoSuchMethodException e) {
            _log.error("NpcAction加載失敗", e);
        }
    }

    private static Constructor<?> loadConstructor(Class<?> c) throws NoSuchMethodException {
        return c.getConstructor(Element.class);
    }

    public static L1NpcAction newAction(Element element) {
        try {
            return (L1NpcAction) _actions.get(element.getNodeName()).newInstance(element);
        } catch (NullPointerException e) {
            _log.error("未定義的NPC對話設置", e);
        } catch (Exception e2) {
            _log.error("NpcAction加載失敗", e2);
        }
        return null;
    }
}
