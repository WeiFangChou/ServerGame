package com.lineage.server.model.npc;

public class L1NpcHtml {
    public static final L1NpcHtml HTML_CLOSE = new L1NpcHtml("");
    private final String[] _args;
    private final String _name;

    public L1NpcHtml(String name) {
        this(name, new String[0]);
    }

    public L1NpcHtml(String name, String... args) {
        if (name == null || args == null) {
            throw new NullPointerException();
        }
        this._name = name;
        this._args = args;
    }

    public String getName() {
        return this._name;
    }

    public String[] getArgs() {
        return this._args;
    }
}
