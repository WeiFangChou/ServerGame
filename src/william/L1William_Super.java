package william;

public class L1William_Super {
    private int SuperCount;
    private int SuperitemId;
    private int _itemId;
    private int _message;
    private int itemId_;

    public L1William_Super(int itemId, int item_Id, int Super_itemId, int Super_Count, int message) {
        this._itemId = itemId;
        this.itemId_ = item_Id;
        this.SuperitemId = Super_itemId;
        this.SuperCount = Super_Count;
        this._message = message;
    }

    public int getItemId() {
        return this._itemId;
    }

    public int getItem_Id() {
        return this.itemId_;
    }

    public int getSuperitemId() {
        return this.SuperitemId;
    }

    public int getSuperCount() {
        return this.SuperCount;
    }

    public int getMessage() {
        return this._message;
    }

    public static int checkItemId(int itemId) {
        L1William_Super Super_Item = Item_Super.getInstance().getTemplate(itemId);
        if (Super_Item == null) {
            return 0;
        }
        return Super_Item.getItemId();
    }
}
