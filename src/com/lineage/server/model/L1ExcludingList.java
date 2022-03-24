package com.lineage.server.model;

import java.util.ArrayList;
import java.util.Iterator;

public class L1ExcludingList {
    private ArrayList<String> _nameList = new ArrayList<>();

    public void add(String name) {
        this._nameList.add(name);
    }

    public String remove(String name) {
        Iterator<String> it = this._nameList.iterator();
        while (it.hasNext()) {
            String each = it.next();
            if (each.equalsIgnoreCase(name)) {
                this._nameList.remove(each);
                return each;
            }
        }
        return null;
    }

    public boolean contains(String name) {
        Iterator<String> it = this._nameList.iterator();
        while (it.hasNext()) {
            if (it.next().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFull() {
        return this._nameList.size() >= 16;
    }
}
