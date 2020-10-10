package logic.Logic.My_CLASS;

import java.security.acl.Owner;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MySuperMarkets {
    Map<Owner, Set<MySuperMarket>> superMarkets;

    public MySuperMarkets() {
        this.superMarkets = new HashMap<>();
    }

    public Map<Owner, Set<MySuperMarket>> getSuperMarkets() {
        return superMarkets;
    }

    public void addSuperMarketToList(Owner owner ,MySuperMarket superMarketToAdd){
        if(this.superMarkets.containsKey(owner)){
            this.superMarkets.get(owner).add(superMarketToAdd);
        }
        else{
            Set<MySuperMarket> temp = new HashSet<>();
            temp.add(superMarketToAdd);
            this.superMarkets.put(owner,temp);
        }
    }
}
