package com.lineage;

import com.lineage.server.datatables.MapsTable;
import com.lineage.server.model.map.L1Map;
import com.lineage.server.model.map.L1V1Map;
import com.lineage.server.templates.MapData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TextMapReader extends MapReader {
    private static final String MAP_DIR = "./maps/";
    private static final Log _log = LogFactory.getLog(TextMapReader.class);

    private byte[][] read(int mapId, int xSize, int ySize) {
        byte[][] map = (byte[][]) Array.newInstance(Byte.TYPE, xSize, ySize);
        try {
            LineNumberReader in = new LineNumberReader(new BufferedReader(new FileReader(MAP_DIR + mapId + ".txt")));
            int y = 0;
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    break;
                } else if (line.trim().length() != 0 && !line.startsWith("#")) {
                    int x = 0;
                    StringTokenizer tok = new StringTokenizer(line, ",");
                    while (tok.hasMoreTokens()) {
                        try {
                            map[x][y] = Byte.parseByte(tok.nextToken());
                        } catch (ArrayIndexOutOfBoundsException e) {
                            _log.error("指定地圖加載障礙數據異常: " + mapId + " X:" + x + " Y:" + y, e);
                        }
                        x++;
                    }
                    y++;
                }
            }
            in.close();
        } catch (Exception e2) {
            _log.error("指定地圖加載障礙數據異常: " + mapId);
        }
        return map;
    }

    @Override // com.lineage.MapReader
    public Map<Integer, L1Map> read() {
        L1V1Map map;
        Map<Integer, L1Map> maps = new HashMap<>();
        Map<Integer, MapData> mapDatas = MapsTable.get().getMaps();
        for (Integer key : mapDatas.keySet()) {
            MapData mapData = mapDatas.get(key);
            int mapId = mapData.mapId;
            try {
                map = new L1V1Map((short) mapId, read(mapId, (mapData.endX - mapData.startX) + 1, (mapData.endY - mapData.startY) + 1), mapData.startX, mapData.startY, mapData.isUnderwater, mapData.markable, mapData.teleportable, mapData.escapable, mapData.isUseResurrection, mapData.isUsePainwand, mapData.isEnabledDeathPenalty, mapData.isTakePets, mapData.isRecallPets, mapData.isUsableItem, mapData.isUsableSkill, mapData.isMapteleport);
            } catch (Exception e) {
                _log.error("地圖資料生成數據載入異常: " + mapId, e);
                map = null;
            }
            if (map != null) {
                maps.put(Integer.valueOf(mapId), map);
            }
        }
        return maps;
    }
}
