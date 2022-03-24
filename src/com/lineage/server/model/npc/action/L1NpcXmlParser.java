package com.lineage.server.model.npc.action;

import com.lineage.server.utils.IterableElementList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;

public class L1NpcXmlParser {
    private static final Map<String, Integer> _questIds = new HashMap();

    public static List<L1NpcAction> listActions(Element element) {
        List<L1NpcAction> result = new ArrayList<>();
        Iterator<Element> it = new IterableElementList(element.getChildNodes()).iterator();
        while (it.hasNext()) {
            L1NpcAction action = L1NpcActionFactory.newAction(it.next());
            if (action != null) {
                result.add(action);
            }
        }
        return result;
    }

    public static Element getFirstChildElementByTagName(Element element, String tagName) {
        Iterator<Element> it = new IterableElementList(element.getElementsByTagName(tagName)).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public static int getIntAttribute(Element element, String name, int defaultValue) {
        try {
            return Integer.valueOf(element.getAttribute(name)).intValue();
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolAttribute(Element element, String name, boolean defaultValue) {
        String value = element.getAttribute(name);
        if (!value.equals("")) {
            return Boolean.valueOf(value).booleanValue();
        }
        return defaultValue;
    }

    public static int parseQuestId(String questId) {
        if (questId.equals("")) {
            return -1;
        }
        Integer result = _questIds.get(questId.toLowerCase());
        if (result != null) {
            return result.intValue();
        }
        throw new IllegalArgumentException();
    }

    public static int parseQuestStep(String questStep) {
        if (questStep.equals("")) {
            return -1;
        }
        if (questStep.equalsIgnoreCase("End")) {
            return 255;
        }
        return Integer.parseInt(questStep);
    }
}
