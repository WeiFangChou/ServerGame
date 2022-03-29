package com.lineage.server.model.map;

import com.lineage.TextMapReader;
import com.lineage.server.utils.PerformanceTimer;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class L1WorldMap {
    private static L1WorldMap _instance;
    private static final Log _log = LogFactory.getLog(TextMapReader.class);
    private Map<Integer, L1Map> _maps;

    public static L1WorldMap get() {
        if (_instance == null) {
            _instance = new L1WorldMap();
        }
        return _instance;
    }

    public void load() {
        PerformanceTimer timer = new PerformanceTimer();
        _log.info("MAP進行數據加載...");
        this._maps = new TextMapReader().read();
        if (this._maps == null) {
            _log.error("MAP數據載入異常 maps未建立初始化");
            System.exit(0);
            return;
        }
        _log.info("MAP數據加載完成(" + timer.get() + " ms)");
    }

    public L1Map getMap(int mapId) {
        L1Map map = this._maps.get(Integer.valueOf(mapId));
        if (map == null) {
            return L1Map.newNull();
        }
        return map;
    }
}
