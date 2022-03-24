package com.lineage.server.datatables;

import com.lineage.server.templates.L1Znoe;
import com.lineage.server.utils.StreamUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadZnoe {
    private static LoadZnoe _instance;
    private static final HashMap<Integer, ArrayList<L1Znoe>> mapList = new HashMap<>();

    private LoadZnoe() {
    }

    public static LoadZnoe getInstance() {
        if (_instance == null) {
            _instance = new LoadZnoe();
        }
        return _instance;
    }

    public void load() throws Throwable {
        Throwable th;
        InputStreamReader is = null;
        BufferedReader br = null;
        try {
            File file = new File("./znoe/zone.tbl");
            if (!file.isFile() || !file.exists()) {
                System.out.println("文件不存在!");
            } else {
                InputStreamReader is2 = new InputStreamReader(new FileInputStream(file), "big5");
                try {
                    BufferedReader br2 = new BufferedReader(is2);
                    while (true) {
                        try {
                            String lineTxt = br2.readLine();
                            if (lineTxt == null) {
                                break;
                            }
                            Matcher m = Pattern.compile("(.*?)(\\s+)(\\d+)(\\s+)(\\d+)(\\s+)(\\d+)(\\s+)(\\d+)(\\s+)(\\d+)(\\s+)(\\d+)").matcher(lineTxt);
                            if (m.find()) {
                                String mapName = m.group(1);
                                int mapId = Integer.parseInt(m.group(5));
                                int startX = Integer.parseInt(m.group(7));
                                int startY = Integer.parseInt(m.group(9));
                                int endX = Integer.parseInt(m.group(11));
                                int endY = Integer.parseInt(m.group(13));
                                ArrayList<L1Znoe> list = mapList.get(Integer.valueOf(mapId));
                                if (list == null) {
                                    list = new ArrayList<>();
                                    mapList.put(Integer.valueOf(mapId), list);
                                }
                                list.add(new L1Znoe(mapId, mapName, startX, startY, endX, endY));
                            }
                        } catch (Exception e) {
                            br = br2;
                            is = is2;
                            try {
                                System.out.println("文件读取错误!");
                                StreamUtil.close(br);
                                StreamUtil.close(is);
                            } catch (Throwable th2) {
                                th = th2;
                                StreamUtil.close(br);
                                StreamUtil.close(is);
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            br = br2;
                            is = is2;
                            StreamUtil.close(br);
                            StreamUtil.close(is);
                            throw th;
                        }
                    }
                    br = br2;
                    is = is2;
                } catch (Exception e2) {
                    is = is2;
                    System.out.println("文件读取错误!");
                    StreamUtil.close(br);
                    StreamUtil.close(is);
                } catch (Throwable th4) {
                    th = th4;
                    is = is2;
                    StreamUtil.close(br);
                    StreamUtil.close(is);
                    throw th;
                }
            }
            StreamUtil.close(br);
            StreamUtil.close(is);
        } catch (Exception e3) {
            System.out.println("文件读取错误!");
            StreamUtil.close(br);
            StreamUtil.close(is);
        }
    }

    public String findMapName(int mapId, int x, int y) {
        ArrayList<L1Znoe> list = mapList.get(Integer.valueOf(mapId));
        if (list == null) {
            return "未知";
        }
        if (list.size() <= 1) {
            return list.get(0).getMapName();
        }
        Iterator<L1Znoe> it = list.iterator();
        while (it.hasNext()) {
            L1Znoe znoe = it.next();
            if (x >= znoe.getStartX() && x <= znoe.getEndX() && y >= znoe.getStartY() && y <= znoe.getEndY()) {
                return znoe.getMapName();
            }
        }
        return list.get(0).getMapName();
    }
}
