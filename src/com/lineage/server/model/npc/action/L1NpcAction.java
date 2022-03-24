package com.lineage.server.model.npc.action;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.model.L1Object;
import com.lineage.server.model.npc.L1NpcHtml;

public interface L1NpcAction {
    boolean acceptsRequest(String str, L1PcInstance l1PcInstance, L1Object l1Object);

    L1NpcHtml execute(String str, L1PcInstance l1PcInstance, L1Object l1Object, byte[] bArr) throws Exception;

    void execute(String str, String str2);

    L1NpcHtml executeWithAmount(String str, L1PcInstance l1PcInstance, L1Object l1Object, long j) throws Exception;
}
