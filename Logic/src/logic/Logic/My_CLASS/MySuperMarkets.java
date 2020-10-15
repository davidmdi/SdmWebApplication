package logic.Logic.My_CLASS;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}
