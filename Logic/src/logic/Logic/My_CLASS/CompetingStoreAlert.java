package logic.Logic.My_CLASS;

public class CompetingStoreAlert implements Alertable{

    private String storeOwnerName;
    private String storeName;
    private MyLocation storeLocation;
    private int areaTotalItems;
    private int storeTotalItems;

    @Override
    public String alert() {
        return String.format("%s open the store %s in your area at location %s",
                storeOwnerName, storeName, storeLocation.toString());
    }
}
