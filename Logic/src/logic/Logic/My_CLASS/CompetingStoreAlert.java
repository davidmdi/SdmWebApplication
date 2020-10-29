package logic.Logic.My_CLASS;

public class CompetingStoreAlert implements Alertable{

    private String storeOwnerName;
    private String storeName;
    private MyLocation storeLocation;
    private int areaTotalItems;
    private int storeTotalItems;

    public CompetingStoreAlert(String storeOwnerName, String storeName, MyLocation storeLocation, int areaTotalItems, int storeTotalItems) {
        this.storeOwnerName = storeOwnerName;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.areaTotalItems = areaTotalItems;
        this.storeTotalItems = storeTotalItems;
    }

    @Override
    public String alert() {
        return String.format("%s open the store %s in your area, store location: %s, store sells %d/%d items from your area.",
                storeOwnerName, storeName, storeLocation.toString(), storeTotalItems, areaTotalItems);
    }
}
