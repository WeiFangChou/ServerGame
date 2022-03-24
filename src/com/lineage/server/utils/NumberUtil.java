package com.lineage.server.utils;

import java.util.Random;

public class NumberUtil {
    public static int randomRound(double number) {
        double percentage = (number - Math.floor(number)) * 100.0d;
        if (percentage == 0.0d) {
            return (int) number;
        }
        if (((double) new Random().nextInt(100)) < percentage) {
            return ((int) number) + 1;
        }
        return (int) number;
    }
}
