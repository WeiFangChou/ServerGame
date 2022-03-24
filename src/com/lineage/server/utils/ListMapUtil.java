package com.lineage.server.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ListMapUtil {
    private static final Log _log = LogFactory.getLog(ListMapUtil.class);

    public static void clear(Queue<?> queue) {
        if (queue != null) {
            try {
                queue.clear();
            } catch (Exception e) {
                _log.error("清空Queue發生異常", e);
            }
        }
    }

    public static void clear(Map<?, ?> map) {
        if (map != null) {
            try {
                map.clear();
            } catch (Exception e) {
                _log.error("清空Map發生異常", e);
            }
        }
    }

    public static void clear(ArrayList<?> list) {
        if (list != null) {
            try {
                list.clear();
            } catch (Exception e) {
                _log.error("清空ArrayList發生異常", e);
            }
        }
    }

    public static void clear(List<?> list) {
        if (list != null) {
            try {
                list.clear();
            } catch (Exception e) {
                _log.error("清空List發生異常", e);
            }
        }
    }
}
