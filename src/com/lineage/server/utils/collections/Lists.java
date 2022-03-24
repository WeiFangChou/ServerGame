package com.lineage.server.utils.collections;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
        return new CopyOnWriteArrayList<>();
    }
}
