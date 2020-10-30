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
        return String.format("%s open the store '%s' in your area! Store location: %s, store sells %d/%d items from your area.",
                storeOwnerName, storeName, storeLocation.toString(), storeTotalItems, areaTotalItems);
    }
}
/*
roni open the store ronila in your area, store location: (x=3,y=3), store sells 3/5 items from your area.
 */