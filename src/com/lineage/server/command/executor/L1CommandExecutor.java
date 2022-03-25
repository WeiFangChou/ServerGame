package com.lineage.server.command.executor;

import com.lineage.server.model.Instance.L1PcInstance;

public interface L1CommandExecutor {
    void execute(L1PcInstance l1PcInstance, String str, String str2) throws Exception;
}
