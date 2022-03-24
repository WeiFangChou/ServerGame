package com.lineage.server.model;

import com.lineage.server.datatables.lock.CharacterQuestReading;
import com.lineage.server.model.Instance.L1PcInstance;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1PcQuest {
    public static final int QUEST_AREX = 22;
    public static final int QUEST_CADMUS = 31;
    public static final int QUEST_CRYSTAL = 33;
    public static final int QUEST_DESIRE = 36;
    public static final int QUEST_DOIL = 28;
    public static final int QUEST_DOROMOND = 20;
    public static final int QUEST_END = 255;
    public static final int QUEST_GENERALHAMELOFRESENTMENT = 41;
    public static final int QUEST_KAMYLA = 32;
    public static final int QUEST_KEPLISHA = 35;
    public static final int QUEST_LIZARD = 34;
    public static final int QUEST_LUKEIN1 = 23;
    public static final int QUEST_MOONOFLONGBOW = 40;
    public static final int QUEST_NOT = 0;
    public static final int QUEST_OILSKINMANT = 11;
    public static final int QUEST_RESTA = 30;
    public static final int QUEST_RUBA = 21;
    public static final int QUEST_RUDIAN = 29;
    public static final int QUEST_SHADOWS = 37;
    public static final int QUEST_SIMIZZ = 27;
    public static final int QUEST_TBOX1 = 24;
    public static final int QUEST_TBOX2 = 25;
    public static final int QUEST_TBOX3 = 26;
    public static final int QUEST_TOSCROLL = 39;
    private static final Log _log = LogFactory.getLog(L1PcQuest.class);
    private L1PcInstance _owner = null;
    private Map<Integer, Integer> _quest = null;

    public L1PcQuest(L1PcInstance owner) {
        this._owner = owner;
    }

    public L1PcInstance get_owner() {
        return this._owner;
    }

    public int get_step(int quest_id) {
        try {
            Integer step = this._quest.get(new Integer(quest_id));
            if (step == null) {
                return 0;
            }
            return step.intValue();
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return 0;
        }
    }

    public void set_step(int quest_id, int step) {
        try {
            if (this._quest.get(new Integer(quest_id)) == null) {
                CharacterQuestReading.get().storeQuest(this._owner.getId(), quest_id, step);
            } else {
                CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, step);
            }
            this._quest.put(new Integer(quest_id), new Integer(step));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void set_end(int quest_id) {
        try {
            if (this._quest.get(new Integer(quest_id)) == null) {
                CharacterQuestReading.get().storeQuest(this._owner.getId(), quest_id, 255);
            } else {
                CharacterQuestReading.get().updateQuest(this._owner.getId(), quest_id, 255);
            }
            this._quest.put(new Integer(quest_id), new Integer(255));
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public boolean isStart(int quest_id) {
        try {
            int step = get_step(quest_id);
            if (step <= 0 || step >= 255) {
                return false;
            }
            return true;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public boolean isEnd(int quest_id) {
        try {
            if (get_step(quest_id) == 255) {
                return true;
            }
            return false;
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
            return false;
        }
    }

    public void load() {
        try {
            this._quest = CharacterQuestReading.get().get(this._owner.getId());
            if (this._quest == null) {
                this._quest = new HashMap();
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
