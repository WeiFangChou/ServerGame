package com.lineage.data;

import com.lineage.data.executor.QuestExecutor;
import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1Quest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QuestClass {
    private static final Map<Integer, QuestExecutor> _classList = new HashMap();
    private static QuestClass _instance;
    private static final Log _log = LogFactory.getLog(QuestClass.class);

    public static QuestClass get() {
        if (_instance == null) {
            _instance = new QuestClass();
        }
        return _instance;
    }

    public void addList(int questid, String className) {
        if (!className.equals("0")) {
            try {
                _classList.put(new Integer(questid), (QuestExecutor) Class.forName("com.lineage.data.quest." + className).getMethod("get", new Class[0]).invoke(null, new Object[0]));
            } catch (ClassNotFoundException e) {
                String error = "發生[Quest(任務)檔案]錯誤, 檢查檔案是否存在:" + className + " QuestId:" + questid;
                _log.error(error);
                DataError.isError(_log, error, e);
            } catch (IllegalArgumentException e2) {
                _log.error(e2.getLocalizedMessage(), e2);
            } catch (IllegalAccessException e3) {
                _log.error(e3.getLocalizedMessage(), e3);
            } catch (InvocationTargetException e4) {
                _log.error(e4.getLocalizedMessage(), e4);
            } catch (SecurityException e5) {
                _log.error(e5.getLocalizedMessage(), e5);
            } catch (NoSuchMethodException e6) {
                _log.error(e6.getLocalizedMessage(), e6);
            }
        }
    }

    public void execute(L1Quest quest) {
        try {
            QuestExecutor exe = _classList.get(new Integer(quest.get_id()));
            if (exe != null) {
                exe.execute(quest);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void startQuest(L1PcInstance pc, int questid) {
        try {
            QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.startQuest(pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void endQuest(L1PcInstance pc, int questid) {
        try {
            QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.endQuest(pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void showQuest(L1PcInstance pc, int questid) {
        try {
            QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.showQuest(pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }

    public void stopQuest(L1PcInstance pc, int questid) {
        try {
            QuestExecutor exe = _classList.get(new Integer(questid));
            if (exe != null) {
                exe.stopQuest(pc);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
