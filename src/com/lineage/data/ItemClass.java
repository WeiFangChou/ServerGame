package com.lineage.data;

import com.lineage.data.executor.ItemExecutor;
import com.lineage.server.model.Instance.L1ItemInstance;
import com.lineage.server.model.Instance.L1PcInstance;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ItemClass {
    private static final Log _log = LogFactory.getLog(ItemClass.class);
    private static final Map<Integer, ItemExecutor> _classList = new HashMap();
    private static ItemClass _instance;

    public ItemClass() {
    }

    public static ItemClass get() {
        if (_instance == null) {
            _instance = new ItemClass();
        }

        return _instance;
    }

    public void addList(int itemid, String className, int mode) {
        if (!className.equals("0")) {
            try {
                String newclass = className;
                String[] set = null;
                if (className.indexOf(" ") != -1) {
                    set = className.split(" ");
                    newclass = set[0];
                }

                StringBuilder stringBuilder = new StringBuilder();
                switch(mode) {
                    case 0:
                        stringBuilder.append("com.lineage.data.item_etcitem.");
                        break;
                    case 1:
                        stringBuilder.append("com.lineage.data.item_weapon.");
                        break;
                    case 2:
                        stringBuilder.append("com.lineage.data.item_armor.");
                }

                stringBuilder.append(newclass);
                Class<?> cls = Class.forName(stringBuilder.toString());
                ItemExecutor exe = (ItemExecutor)cls.getMethod("get").invoke((Object)null);
                if (set != null) {
                    exe.set_set(set);
                }

                _classList.put(new Integer(itemid), exe);
            } catch (ClassNotFoundException var9) {
                String error = "發生[道具檔案]錯誤, 檢查檔案是否存在:" +var9.getMessage()+ className + " ItemId:" + itemid;
                _log.error(error);
                DataError.isError(_log, error, var9);
            } catch (IllegalArgumentException var10) {
                _log.error(var10.getLocalizedMessage(), var10);
            } catch (IllegalAccessException var11) {
                _log.error(var11.getLocalizedMessage(), var11);
            } catch (InvocationTargetException var12) {
                _log.error(var12.getLocalizedMessage(), var12);
            } catch (SecurityException var13) {
                _log.error(var13.getLocalizedMessage(), var13);
            } catch (NoSuchMethodException var14) {
                _log.error(var14.getLocalizedMessage(), var14);
            }

        }
    }

    public void item(int[] data, L1PcInstance pc, L1ItemInstance item) {
        if (pc != null) {
            if (item != null) {
                try {
                    ItemExecutor exe = (ItemExecutor)_classList.get(new Integer(item.getItemId()));
                    if (exe != null) {
                        exe.execute(data, pc, item);
                    }
                } catch (Exception var5) {
                    _log.error(var5.getLocalizedMessage(), var5);
                }

            }
        }
    }

    public void item_weapon(boolean equipped, L1PcInstance pc, L1ItemInstance item) {
        if (pc != null) {
            if (item != null) {
                try {
                    ItemExecutor exe = (ItemExecutor)_classList.get(new Integer(item.getItemId()));
                    if (exe != null) {
                        int[] data = new int[]{equipped ? 1 : 0};
                        exe.execute(data, pc, item);
                    }
                } catch (Exception var6) {
                    _log.error(var6.getLocalizedMessage(), var6);
                }

            }
        }
    }

    public void item_armor(boolean equipped, L1PcInstance pc, L1ItemInstance item) {
        if (pc != null) {
            if (item != null) {
                try {
                    ItemExecutor exe = (ItemExecutor)_classList.get(new Integer(item.getItemId()));
                    if (exe != null) {
                        int[] data = new int[]{equipped ? 1 : 0};
                        exe.execute(data, pc, item);
                    }
                } catch (Exception var6) {
                    _log.error(var6.getLocalizedMessage(), var6);
                }

            }
        }
    }
}
