package com.lineage.server.datatables.storage;

import com.lineage.server.model.Instance.L1PcInstance;
import com.lineage.server.templates.L1BookMark;
import java.util.ArrayList;

public interface CharBookStorage {
    void addBookmark(L1PcInstance l1PcInstance, String str);

    void deleteBookmark(L1PcInstance l1PcInstance, String str);

    L1BookMark getBookMark(L1PcInstance l1PcInstance, int i);

    ArrayList<L1BookMark> getBookMarks(L1PcInstance l1PcInstance);

    void load();

    void updateBookmarkName(L1BookMark l1BookMark);
}
