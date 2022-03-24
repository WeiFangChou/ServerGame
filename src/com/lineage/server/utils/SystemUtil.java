package com.lineage.server.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.nio.ByteBuffer;
import org.apache.commons.logging.Log;

public class SystemUtil {
    public static String toHex(ByteBuffer data) {
        StringBuilder result = new StringBuilder();
        int counter = 0;
        while (data.hasRemaining()) {
            if (counter % 16 == 0) {
                result.append(String.format("%04X: ", Integer.valueOf(counter)));
            }
            result.append(String.format("%02X ", Integer.valueOf(data.get() & 255)));
            counter++;
            if (counter % 16 == 0) {
                result.append("  ");
                toText(data, result, 16);
                result.append("\n");
            }
        }
        int rest = counter % 16;
        if (rest > 0) {
            for (int i = 0; i < 17 - rest; i++) {
                result.append("   ");
            }
            toText(data, result, rest);
        }
        return result.toString();
    }

    private static void toText(ByteBuffer data, StringBuilder result, int cnt) {
        int a = 0;
        int charPos = data.position() - cnt;
        while (a < cnt) {
            int charPos2 = charPos + 1;
            int c = data.get(charPos);
            if (c <= 31 || c >= 128) {
                result.append('.');
            } else {
                result.append((char) c);
            }
            a++;
            charPos = charPos2;
        }
    }

    public static long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) >> 20;
    }

    public static void printMemoryUsage(Log log) {
        MemoryUsage hm = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nhm = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        String s1 = String.valueOf(hm.getUsed() / 1048576) + "/" + (hm.getMax() / 1048576) + "mb";
        String s2 = String.valueOf(nhm.getUsed() / 1048576) + "/" + (nhm.getMax() / 1048576) + "mb";
        if (log != null) {
            log.info("已分配內存使用量: " + s1);
            log.info("非分配內存使用量: " + s2);
            System.out.println("[線程量] : [ 當前有 " + Thread.activeCount() + "個線程在運行 ]");
        }
    }
}
