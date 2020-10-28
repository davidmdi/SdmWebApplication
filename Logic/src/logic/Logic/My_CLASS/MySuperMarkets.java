package logic.Logic.My_CLASS;

import java.util.*;

public class MySuperMarkets {
    Map<MyOwner, Set<MySuperMarket>> superMarkets;

    public MySuperMarkets() {
        this.superMarkets = new HashMap<>();
    }

    public Map<MyOwner, Set<MySuperMarket>> getSuperMarkets() {
        return superMarkets;
    }

    public void addSuperMarketToList(MyOwner owner ,MySuperMarket superMarketToAdd){
        if(this.superMarkets.containsKey(owner)){
            this.superMarkets.get(owner).add(superMarketToAdd);
        }
        else{
            Set<MySuperMarket> temp = new HashSet<>();
            temp.add(superMarketToAdd);
            this.superMarkets.put(owner,temp);
        }
    }

    public Set<MySuperMarket> getAreaSuperMarketsSet(String areaName){
        Set<MySuperMarket> zoneSuperMarkets = new HashSet<>();

        for(Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for(MySuperMarket superMarket : superMarketsSet){
                if(superMarket.getZoneName().equalsIgnoreCase(areaName))
                    zoneSuperMarkets.add(superMarket);
            }
        }

        return zoneSuperMarkets;
    }

    public synchronized MySuperMarket getAreaSuperMarketByName(String areaName){
        MySuperMarket zoneSuperMarket =null ;

        for(Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for(MySuperMarket superMarket : superMarketsSet){
                if(superMarket.getZoneName().equalsIgnoreCase(areaName))
                    zoneSuperMarket = (superMarket);
            }
        }

        return zoneSuperMarket;
    }


    public List<MyStore> getAreaStoresList(String areaName) {
        List<MyStore> areaStores = new ArrayList<>();
        Set<MySuperMarket> zoneSuperMarkets = getAreaSuperMarketsSet(areaName);

        for(MySuperMarket superMarket : zoneSuperMarkets){
            areaStores.addAll(superMarket.getStores().getStoreList());
        }

        return areaStores;
    }

    public List<MyItem> getAreaItemsList(String areaName) {
        List<MyItem> areaItems = new ArrayList<>();
        Set<MySuperMarket> zoneSuperMarkets = getAreaSuperMarketsSet(areaName);

        for(MySuperMarket superMarket : zoneSuperMarkets){
            areaItems.addAll(superMarket.getItems().getItemList());
        }

        return areaItems;
    }

    public synchronized boolean isThereAStoreInTheLocation(int x, int y){
        boolean res = false;

        for(Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for(MySuperMarket superMarket : superMarketsSet){
                for(MyStore store : superMarket.getStores().getStoreList()){
                    if((store.getMyLocation().getX() == x) && (store.getMyLocation().getY() == y))
                        res = true;
                }
            }
        }

        return res;
    }

    public void createNewStore(MyStore.StoreJson store, String zoneName, String ownerName) {
        int storeId = generateStoreId();
        for(MyStoreItem.StoreItemJson storeItem : store.storeItems)
            storeItem.storeId = storeId;

        //List<MyItem> storeItemsToAdd = createStoreItemsToAddList(store, zoneName);
        //MyStore storeToAdd = new MyStore(storeId, store, storeItemsToAdd, ownerName);
        MyStore storeToAdd = new MyStore(storeId, store, ownerName);
        //add store to zone:
        MySuperMarket superMarket = getSuperMarketByOwnerAndZone(ownerName, zoneName);
        superMarket.getStores().addStore(storeToAdd);
    }

    private MySuperMarket getSuperMarketByOwnerAndZone(String ownerName, String zoneName) {
        MyOwner zoneOwner = null;

        for(MyOwner owner : this.superMarkets.keySet()){
            if(owner.getUserName().equalsIgnoreCase(ownerName))
                zoneOwner = owner;
        }

        for(MySuperMarket superMarket : this.superMarkets.get(zoneOwner)){
            if(superMarket.getZoneName().equalsIgnoreCase(zoneName))
                return superMarket;
        }

        return null;
    }
/*
    public List<MyItem> createStoreItemsToAddList(MyStore.StoreJson store, String zoneName){
        List<MyItem> zoneItems = getAreaItemsList(zoneName);
        List<MyItem> storeItems = new ArrayList<>();

        for(MyItem.ItemJson itemJson : store.items){
            for(MyItem zoneItem : zoneItems){
                if(itemJson.id == zoneItem.getItemId())
                    storeItems.add(zoneItem);
            }
        }

        return storeItems;
    }
*/

    /* find max id and return (maxId+1) */
    private int generateStoreId() {
        int maxStoreId = 0;

        for(Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for(MySuperMarket superMarket : superMarketsSet){
                for(MyStore store : superMarket.getStores().getStoreList()){
                    if(store.getId() > maxStoreId)
                        maxStoreId = store.getId();
                }
            }
        }

        return (++maxStoreId);
    }

    public void addFeedBackAlert(FeedbackAlert feedbackAlert) {
        String ownerName = "";
        MyOwner owner = null;

        for(Set<MySuperMarket> superMarkets : this.superMarkets.values()){
            for(MySuperMarket superMarket : superMarkets){
                for(MyStore store : superMarket.getStores().getStoreList()){
                    if(feedbackAlert.getStoreName().equalsIgnoreCase(store.getName()))
                        ownerName = store.getOwnerName();
                }
            }
        }

        for(MyOwner myOwner : this.superMarkets.keySet()){
            if(myOwner.getUserName().equalsIgnoreCase(ownerName))
                owner = myOwner;
        }

        owner.addAlert(feedbackAlert);
    }

    public synchronized List<String> getOwnerAlertsToString(String ownerName) {
        MyOwner owner = null;
        List<String> stringAlerts = new ArrayList<>();

        for(MyOwner myOwner : this.superMarkets.keySet()){
            if(myOwner.getUserName().equalsIgnoreCase(ownerName))
                owner = myOwner;
        }

        for(Alertable alert : owner.getAlerts()){
            stringAlerts.add(alert.alert());
        }

        owner.removeAllAlerts(); //Delete alerts so each alert appear only once!!

        return stringAlerts;
    }
}
