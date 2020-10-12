package logic.Logic;


import SDM_CLASS.*;
import logic.Logic.My_CLASS.MyOwner;
import logic.Logic.My_CLASS.MySuperMarket;
import logic.Logic.My_CLASS.MySuperMarkets;
import logic.Logic.My_CLASS.MyUsers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Engine {

   private MySuperMarkets mySupermarkets;
   private MyUsers myUsers;
   private Boolean validationForBuild = false ;
   //private SimpleBooleanProperty isSuperMarketIsValid ;

    public MySuperMarkets getMySupermarkets() {
        return mySupermarkets;
    }

    public MyUsers getMyUsers() {
        return myUsers;
    }


    public Engine() {
        this.myUsers = new MyUsers();
        this.mySupermarkets = new MySuperMarkets() ;

    }

    public synchronized String loadFileFromOwner(String ownerName, InputStream inputStream) {
        try {
            JAXBContext jc = JAXBContext.newInstance("SDM_CLASS");
            Unmarshaller u = jc.createUnmarshaller();
            SuperDuperMarketDescriptor sdmJAXB = (SuperDuperMarketDescriptor) u.unmarshal(inputStream);
            String msg = buildSuperMarket(sdmJAXB);
            if(validationForBuild)
                createSDMSuperMarket(ownerName , inputStream );

        }catch (JAXBException e){e.printStackTrace();}


        return new String();
    }

    private String buildSuperMarket(SuperDuperMarketDescriptor temp) {
        String returnString = "";
        boolean isItemIdUnique , isStoreIdUnique, iseReferencedToExistItem ,
                isItemDefinedOnlyOnceAtEachStore ,isEachProductSailedByOneStoreAtLeast, isInCordRange,
                 isCordUniq,isLegaldiscount;
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
        if(isItemIdUnique && isStoreIdUnique && iseReferencedToExistItem && isItemDefinedOnlyOnceAtEachStore
                && isEachProductSailedByOneStoreAtLeast &&  isInCordRange
                && isLegaldiscount && isCordUniq ) { // from gui-> && isCustomerIdUniq && isCordUniq
            returnString = "<xml file has load successfully>";
            validationForBuild = true;
        }
        else {
            validationForBuild = false;

        }
        return returnString;
    }

    public synchronized String createSDMSuperMarket(String ownerName, InputStream inputStream) {
        String returnString = "";

        try {
            SuperDuperMarketDescriptor temp ;
            JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            temp = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(inputStream);
            returnString = buildSuperMarket(temp);
            if(validationForBuild){
                MyOwner owner = this.getMyUsers().findOwnerByName(ownerName);
                owner.getZonesNames().add(temp.getSDMZone().getName()); // updates super zone name.
                this.mySupermarkets.addSuperMarketToList(owner,new MySuperMarket(temp));
                validationForBuild = false ;
            }

        } catch (JAXBException e) {
            returnString = e.getMessage();
        } catch (NullPointerException e) {
            returnString = "<one of the members is NUll>";
        }

        return returnString;
    }

    private Set<Integer> builedIdSet(SDMStore store) {
        Set<Integer> returnSet = new HashSet<>();
        for (SDMSell id:store.getSDMPrices().getSDMSell()) {
            returnSet.add(id.getItemId());
        }
        return returnSet;
    }

    private boolean checkDiscountUnit(SDMDiscount discount, Set<Integer> idSet) {
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
    private boolean chekIsLegalDiscount(SuperDuperMarketDescriptor temp) {
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
    private boolean

    checkLIsInCordRange(SuperDuperMarketDescriptor temp) {
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
    private boolean checkIsItemDefinedOnlyOnceAtEachStore(SuperDuperMarketDescriptor temp) {
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
    private boolean checkIsEachProductSailedByOneStoreAtLeast(SuperDuperMarketDescriptor temp) {
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
    private boolean checkIseReferencedToExistItem(SuperDuperMarketDescriptor temp){
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
    private boolean checkIsStoreIdUnique(SDMStores sdmStores) {
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
    private boolean checkIsItemIdUnique(SDMItems sdmItems) {
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

    private boolean checKIsCordUniq(SuperDuperMarketDescriptor temp) {
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



//
//        public boolean isIsSuperMarketIsValid() {
//        return isSuperMarketIsValid.get();
//    }
//
//    public SimpleBooleanProperty isSuperMarketIsValidProperty() {
//        return isSuperMarketIsValid;
//    }
//
//
//    public MySuperMarket getMySupermarkets() {
//        return mySupermarkets;
//    }
//
//    public void setMySupermarket(MySuperMarkets mySupermarket) {
//        this.mySupermarkets = mySupermarket;
//    }
//
//    public boolean isSuperMarketIsValid() {
//        return isSuperMarketIsValid.get();
//    }
//
//    public void setSuperMarketIsValid(boolean superMarketIsValid) {
//        isSuperMarketIsValid.set(superMarketIsValid);
//    }
//
//    public Engine() {
//        this.mySupermarkets = null;
//        isSuperMarketIsValid = new SimpleBooleanProperty(false);
//    }
//
//
//
//    public List<String> askForStoreNamesList() {
//        return getMySupermarket().getStores().getStoresName();
//    }
//
//    public List<String> askforCustomerNames() {
//        return getMySupermarket().getCustomers().getCustomerNames();
//    }
//
//    public void createStoreSingleOrderInstance(MyOrder order,
//                                               Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap){
//        Map<MyStoreItem, Double> ordersQuantityMap = order.getQuantityMap();
//
//        for (MyStoreItem storeItem: ordersQuantityMap.keySet()) {
//            //initialize
//            int storeId= storeItem.getStoreId();
//            MyStore store = this.mySupermarket.getStores().getStoreMap().get(storeId);
//            double deliveryDistance = this.mySupermarket.caculateDeliveryDistance(store,order.getCustomer().getLocation(),
//                    store.getMyLocation());
//            double deliveryCost = order.getDeliveryCostMap().get(storeId);
//
//            //creat instance
//            if(!storeSingleOrderItemsMap.containsKey(storeId)){
//                MyStoreSingleOrderItems storeSingleOrderItems = new MyStoreSingleOrderItems(order.getOrderId(),
//                        order.getDate(),storeId,order.getCustomer(),order.getOrderKind() ,deliveryDistance , deliveryCost );
//                //saving the instance in map.
//                storeSingleOrderItemsMap.put(storeId,storeSingleOrderItems);
//            }
//
//            // adding item to sub quantity map
//            storeSingleOrderItemsMap.get(storeId).addToQuantityMap(storeItem,ordersQuantityMap.get(storeItem));
//
//    }
//
//  }
}

