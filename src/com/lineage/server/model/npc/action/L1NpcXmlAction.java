package com.lineage.server.model.npc.action;

import com.lineage.server.datatables.ExpTable;
import com.lineage.server.model.Instance.L1NpcInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.npc.L1NpcHtml;
import com.lineage.server.utils.RangeInt;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.w3c.dom.Element;

public abstract class L1NpcXmlAction implements L1NpcAction {
    private static final Map<Character, Integer> _charTypes = new HashMap();
    private final int[] _classes;
    private final RangeInt _level;
    public String _name;
    private final int[] _npcIds;
    public String _npcids = null;
    private final int _questId;
    private final int _questStep;

    @Override // com.lineage.server.model.npc.action.L1NpcAction
    public abstract L1NpcHtml execute(String str, L1PcInstance l1PcInstance, L1Object l1Object, byte[] bArr) throws Exception;

    @Override // com.lineage.server.model.npc.action.L1NpcAction
    public abstract void execute(String str, String str2);

    public L1NpcXmlAction(Element element) {
        String str = null;
        this._name = element.getAttribute("Name");
        this._name = !this._name.equals("") ? this._name : str;
        this._npcids = element.getAttribute("NpcId");
        this._npcIds = parseNpcIds(this._npcids);
        this._level = parseLevel(element);
        this._questId = L1NpcXmlParser.parseQuestId(element.getAttribute("QuestId"));
        this._questStep = L1NpcXmlParser.parseQuestStep(element.getAttribute("QuestStep"));
        this._classes = parseClasses(element);
    }

    private int[] parseClasses(Element element) {
        String classes = element.getAttribute("Class").toUpperCase();
        int[] result = new int[classes.length()];
        char[] charArray = classes.toCharArray();
        int length = charArray.length;
        int i = 0;
        int idx = 0;
        while (i < length) {
            result[idx] = _charTypes.get(Character.valueOf(charArray[i])).intValue();
            i++;
            idx++;
        }
        Arrays.sort(result);
        return result;
    }

    private RangeInt parseLevel(Element element) {
        int level = L1NpcXmlParser.getIntAttribute(element, "Level", 0);
        return level == 0 ? new RangeInt(L1NpcXmlParser.getIntAttribute(element, "LevelMin", 1), L1NpcXmlParser.getIntAttribute(element, "LevelMax", ExpTable.MAX_LEVEL)) : new RangeInt(level, level);
    }

    static {
        _charTypes.put('P', 0);
        _charTypes.put('K', 1);
        _charTypes.put('E', 2);
        _charTypes.put('W', 3);
        _charTypes.put('D', 4);
        _charTypes.put('R', 5);
        _charTypes.put('I', 6);
    }

    private int[] parseNpcIds(String npcIds) {
        StringTokenizer tok = new StringTokenizer(npcIds.replace(" ", ""), ",");
        int[] result = new int[tok.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(tok.nextToken());
        }
        Arrays.sort(result);
        return result;
    }

    private boolean acceptsNpcId(L1Object obj) {
        if (this._npcIds.length > 0) {
            if (!(obj instanceof L1NpcInstance)) {
                return false;
            }
            if (Arrays.binarySearch(this._npcIds, ((L1NpcInstance) obj).getNpcTemplate().get_npcId()) >= 0) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean acceptsLevel(int level) {
        return this._level.includes(level);
    }

    private boolean acceptsCharType(int type) {
        if (this._classes.length <= 0 || Arrays.binarySearch(this._classes, type) >= 0) {
            return true;
        }
        return false;
    }

    private boolean acceptsActionName(String name) {
        if (this._name == null) {
            return true;
        }
        return name.equals(this._name);
    }

    private boolean acceptsQuest(L1PcInstance pc) {
        if (this._questId == -1) {
            return true;
        }
        return this._questStep == -1 ? pc.getQuest().get_step(this._questId) > 0 : pc.getQuest().get_step(this._questId) == this._questStep;
    }

    @Override // com.lineage.server.model.npc.action.L1NpcAction
    public boolean acceptsRequest(String actionName, L1PcInstance pc, L1Object obj) {
        if (acceptsNpcId(obj) && acceptsLevel(pc.getLevel()) && acceptsQuest(pc) && acceptsCharType(pc.getType()) && acceptsActionName(actionName)) {
            return true;
        }
        return false;
    }

    @Override // com.lineage.server.model.npc.action.L1NpcAction
    public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc, L1Object obj, long amount) throws Exception {
        return null;
    }
}
