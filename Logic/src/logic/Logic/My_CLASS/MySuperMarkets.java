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
        List<MyItem> storeItemsToAdd = createStoreItemsToAddList(store, zoneName);
        MyStore storeToAdd = new MyStore(storeId, store, storeItemsToAdd, ownerName);
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
}
