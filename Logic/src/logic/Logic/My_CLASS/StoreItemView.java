package logic.Logic.My_CLASS;

public class StoreItemView {
    private int itemId;
    private String itemName;
    private String parchesCategory;
    private int price;
    private int howManyTimesSold;

    public StoreItemView(MyStoreItem storeItem) {
        this.itemId = storeItem.getMyItem().getSdmItem().getId();
        this.itemName = storeItem.getName();
        this.parchesCategory = storeItem.getMyItem().getSdmItem().getPurchaseCategory();
        this.price = storeItem.getPrice();
        this.howManyTimesSold = storeItem.getHowManyTimeSold() ;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getParchesCategory() {
        return parchesCategory;
    }

    public int getPrice() {
        return price;
    }

    public int getHowManyTimesSold() {
        return howManyTimesSold;
    }
}
