package logic.Logic;


import SDM_CLASS.*;
import javafx.beans.property.SimpleBooleanProperty;
import logic.Logic.My_CLASS.MySuperMarket;
import logic.Logic.My_CLASS.MySuperMarkets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XmlLoaderTask  {

    public static String buildSuperMarket(SuperDuperMarketDescriptor temp, SimpleBooleanProperty validationForBuild,
                                          MySuperMarkets mySupermarkets) {
        String returnString = "";
        boolean isItemIdUnique , isStoreIdUnique, iseReferencedToExistItem ,
                isItemDefinedOnlyOnceAtEachStore ,isEachProductSailedByOneStoreAtLeast, isInCordRange,
                isCordUniq,isLegaldiscount , isZoneExist;
        if(!(isItemIdUnique = checkIsItemIdUnique(temp.getSDMItems())))
            returnString = "<There are two different items with same id , please load legal .xml>";
        if(!(isStoreIdUnique = checkIsStoreIdUnique(temp.getSDMStores())))
            returnString = "<There are two different stores with same id , please load legal .xml>";
        if(!(iseReferencedToExistItem = checkIseReferencedToExistItem(temp )))
            returnString = "<There is price witch references to un-exist item in the store , please load legal .xml>";
        if(!(isEachProductSailedByOneStoreAtLeast = checkIsEachProductSailedByOneStoreAtLeast(temp)))
            returnString = "<There is Item witch is not sold by any store, please load legal .xml>";
        if(!(isItemDefinedOnlyOnceAtEachStore = checkIsItemDefinedOnlyOnceAtEachStore(temp)))
            returnString = "<There is Item witch sold at same store twice or more , please load legal .xml>";
        if(!(isInCordRange = checkLIsInCordRange(temp)))
            returnString = "<There is store out of range , please load legal .xml >";
        if(!(isCordUniq = checKIsCordUniq(temp)))
            returnString = "<There are two objects at same location , please load legal .xml>";
        if(!(isLegaldiscount = chekIsLegalDiscount(temp)))
            returnString = "<There is item in the discount items offer witch is not sold, please load legal .xml>";
        if(!(isZoneExist = checkIsZoneExist(temp , mySupermarkets)))
            returnString = "<There is valid super at specified zone, please load legal .xml>";
        if(isItemIdUnique && isStoreIdUnique && iseReferencedToExistItem && isItemDefinedOnlyOnceAtEachStore
                && isEachProductSailedByOneStoreAtLeast &&  isInCordRange
                && isLegaldiscount && isCordUniq && isZoneExist ) { // from gui-> && isCustomerIdUniq && isCordUniq
            returnString = "<xml file has load successfully>";
            validationForBuild.set(true);
        }
        else {
            validationForBuild.set(false);

        }
        return returnString;
    }

    // checks that zone name is uniq.
    // for each owner checks that his super not contains the zone name of the wanted super.
    private static boolean checkIsZoneExist(SuperDuperMarketDescriptor temp, MySuperMarkets mySupermarkets) {
        String tempZoneName = temp.getSDMZone().getName();
        for(Set<MySuperMarket> superMarketSet : mySupermarkets.getSuperMarkets().values()){
            for(MySuperMarket superMarket : superMarketSet ){
                if(tempZoneName.equalsIgnoreCase(superMarket.getZoneName()))
                    return false;
            }
        }
        return true;
    }

    private static Set<Integer> builedIdSet(SDMStore store) {
        Set<Integer> returnSet = new HashSet<>();
        for (SDMSell id:store.getSDMPrices().getSDMSell()) {
            returnSet.add(id.getItemId());
        }
        return returnSet;
    }

    private static boolean checkDiscountUnit(SDMDiscount discount, Set<Integer> idSet) {
        int ifYouBuyItemId = discount.getIfYouBuy().getItemId();
        if(!idSet.contains(ifYouBuyItemId))
            return false;
        else {
            List<SDMOffer> offerList = discount.getThenYouGet().getSDMOffer();
            for (SDMOffer offer:offerList) {
                if(!idSet.contains(offer.getItemId()))
                    return false;
            }
        }
        return true;
    }

    //3.3.3
    private static boolean chekIsLegalDiscount(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        List<SDMStore> storeList = temp.getSDMStores().getSDMStore();
        for (SDMStore store:storeList) {
            Set<Integer> idSet = builedIdSet(store);
            if(store.getSDMDiscounts() != null) {
                List<SDMDiscount> discountList = store.getSDMDiscounts().getSDMDiscount();
                for (SDMDiscount discount : discountList) {
                    returnBoolean = checkDiscountUnit(discount, idSet);
                    if (!returnBoolean)
                        return returnBoolean;
                }
            }
        }
        return returnBoolean;
    }

    //3.7 + 3.3.1
    private static boolean checkLIsInCordRange(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int x ,y ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            x = store.getLocation().getX();
            y = store.getLocation().getY();
            if(x<1 || x>50 || y<1 || y>50)
                returnBoolean =false ;
        }
        return returnBoolean;
    }

    //3.6
    private static boolean checkIsItemDefinedOnlyOnceAtEachStore(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        int size ;
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            size = store.getSDMPrices().getSDMSell().size();
            for(int i = 0 ; i < size-1 ;++i) {
                for (int j = i + 1; j < size; ++j) {
                    if (store.getSDMPrices().getSDMSell().get(i).getItemId() ==
                            store.getSDMPrices().getSDMSell().get(j).getItemId()) {
                        returnBoolean = false;
                    }
                }
            }
        }

        return returnBoolean;
    }

    //3.5
    private static boolean checkIsEachProductSailedByOneStoreAtLeast(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        Set<Integer> itemesIdByStore = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                itemesIdByStore.add(sell.getItemId());
            }
        }
        if(!(itemesIdByStore.containsAll(itemsById)))
            returnBoolean =false;
        return returnBoolean;
    }

    //3.4
    private static boolean checkIseReferencedToExistItem(SuperDuperMarketDescriptor temp){
        boolean returnBoolean = true;
        Set<Integer> itemsById = new HashSet<Integer>();
        for (SDMItem item :temp.getSDMItems().getSDMItem()) {
            itemsById.add(item.getId());
        }
        for (SDMStore store: temp.getSDMStores().getSDMStore()) {
            for (SDMSell sell:store.getSDMPrices().getSDMSell()) {
                if(!(itemsById.contains(sell.getItemId())))
                    returnBoolean =false;
            }
        }
        return returnBoolean;
    }

    //3.3
    private static boolean checkIsStoreIdUnique(SDMStores sdmStores) {
        boolean returnBoolean = true;
        int size = sdmStores.getSDMStore().size();
        for(int i = 0 ; i<size-1;++i)
            for(int j=i+1;j<size;++j)
                if(sdmStores.getSDMStore().get(i).getId() == sdmStores.getSDMStore().get(j).getId() &&
                        !sdmStores.getSDMStore().get(i).getName().equals(sdmStores.getSDMStore().get(j).getName())){
                    returnBoolean = false;
                    break;
                }
        return returnBoolean;
    }

    //3.2
    private static boolean checkIsItemIdUnique(SDMItems sdmItems) {
        boolean returneBoolean = true ;
        int size = sdmItems.getSDMItem().size();
        for(int i = 0 ; i<size-1 ; ++i)
            for(int j = i+1 ; j < size;++j )
                if(sdmItems.getSDMItem().get(i).getId() == sdmItems.getSDMItem().get(j).getId() &&
                        !sdmItems.getSDMItem().get(i).getName().equals(sdmItems.getSDMItem().get(j).getName())){
                    returneBoolean = false ;
                    break;
                }
        return returneBoolean ;
    }

    private static boolean checKIsCordUniq(SuperDuperMarketDescriptor temp) {
        boolean returnBoolean =true;
        List<Location> locations = new ArrayList<>();
        List<SDMStore> stores = temp.getSDMStores().getSDMStore();

        for (SDMStore store : stores) {
            locations.add(store.getLocation());
        }
        int size = locations.size(); // locations size
        for(int i = 0 ; i<size-1;++i) {
            for (int j = i + 1; j < size; ++j) {
                if(locations.get(i).getX() == locations.get(j).getX() &&
                        locations.get(i).getY() == locations.get(j).getY())
                    returnBoolean = false   ;
            }
        }
        return returnBoolean;
    }

}
