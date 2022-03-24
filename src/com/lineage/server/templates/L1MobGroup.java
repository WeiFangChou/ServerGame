package com.lineage.server.templates;

import com.lineage.server.utils.collections.Lists;
import java.util.Collections;
import java.util.List;

public class L1MobGroup {
    private final int _id;
    private final boolean _isRemoveGroupIfLeaderDie;
    private final int _leaderId;
    private final List<L1NpcCount> _minions = Lists.newArrayList();

    public L1MobGroup(int id, int leaderId, List<L1NpcCount> minions, boolean isRemoveGroupIfLeaderDie) {
        this._id = id;
        this._leaderId = leaderId;
        this._minions.addAll(minions);
        this._isRemoveGroupIfLeaderDie = isRemoveGroupIfLeaderDie;
    }

    public int getId() {
        return this._id;
    }

    public int getLeaderId() {
        return this._leaderId;
    }

    public List<L1NpcCount> getMinions() {
        return Collections.unmodifiableList(this._minions);
    }

    public boolean isRemoveGroupIfLeaderDie() {
        return this._isRemoveGroupIfLeaderDie;
    }
}
