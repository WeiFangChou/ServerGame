package william;

public class L1William_Bless_armor {
    private int _itemId;
    private int _message;
    private int itemId_;

    public L1William_Bless_armor(int itemId, int item_Id, int message) {
        this._itemId = itemId;
        this.itemId_ = item_Id;
        this._message = message;
    }

    public int getItemId() {
        return this._itemId;
    }

    public int getItem_Id() {
        return this.itemId_;
    }

    public int getMessage() {
        return this._message;
    }

    public static int checkItemId(int itemId) {
        L1William_Bless_armor Bless_armor_Item = ItemBlessarmor.getInstance().getTemplate(itemId);
        if (Bless_armor_Item == null) {
            return 0;
        }
        return Bless_armor_Item.getItemId();
    }
}
