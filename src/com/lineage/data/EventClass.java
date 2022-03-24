package com.lineage.data;

import com.lineage.data.executor.EventExecutor;
import com.lineage.server.templates.L1Event;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EventClass {
    private static final Map<Integer, EventExecutor> _classList = new HashMap();
    private static EventClass _instance;
    private static final Log _log = LogFactory.getLog(EventClass.class);

    public static EventClass get() {
        if (_instance == null) {
            _instance = new EventClass();
        }
        return _instance;
    }

    public void addList(int eventid, String className) {
        if (!className.equals("0")) {
            try {
                _classList.put(new Integer(eventid), (EventExecutor) Class.forName("com.lineage.data.event." + className).getMethod("get", new Class[0]).invoke(null, new Object[0]));
            } catch (ClassNotFoundException e) {
                String error = "發生[Event(活動設置)檔案]錯誤, 檢查檔案是否存在:" + className + " EventId:" + eventid;
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

    public void startEvent(L1Event event) {
        try {
            EventExecutor exe = _classList.get(new Integer(event.get_eventid()));
            if (exe != null) {
                exe.execute(event);
            }
        } catch (Exception e) {
            _log.error(e.getLocalizedMessage(), e);
        }
    }
}
