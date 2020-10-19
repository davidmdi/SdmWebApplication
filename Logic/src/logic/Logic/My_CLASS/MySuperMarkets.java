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

}
