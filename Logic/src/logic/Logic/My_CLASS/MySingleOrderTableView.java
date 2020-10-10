package logic.Logic.My_CLASS;

public class MySingleOrderTableView {
    private int itemId;
    private String itemName;
    private String purchesCatagory;
    private double quantity;
    private int singleItemPrice;
    private double price;
    private String priceString;
    private String offerKind;

    public MySingleOrderTableView(MyStoreItem item , double quantity) {
        this.itemId = item.getMyItem().getSdmItem().getId();
        this.itemName = item.getName();
        this.purchesCatagory = item.getMyItem().getSdmItem().getPurchaseCategory();
        this.quantity = quantity;
        this.offerKind = item.getItemKind();
        this.singleItemPrice = item.getPrice();
        this.price = calculatePrice();

    }

    private double calculatePrice() {
        if(this.offerKind.equals("offer"))
            return this.singleItemPrice;
        else
            return this.singleItemPrice * this.quantity;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPurchesCatagory() {
        return purchesCatagory;
    }

    public double getQuantity() {
        return quantity;
    }

    public int getSingleItemPrice() {
        return singleItemPrice;
    }

    public double getPrice() {
        return price;
    }

    public String getOfferKind() {
        return offerKind;
    }

    public String getPriceString() {
        return String.format("%.2f",this.price);
    }
}
