package logic.Logic;


import SDM_CLASS.SuperDuperMarketDescriptor;
import javafx.beans.property.SimpleBooleanProperty;
import logic.Logic.My_CLASS.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.List;

public class Engine {

   private MySuperMarkets mySupermarkets;
   private MyUsers myUsers;
   private SimpleBooleanProperty validationForBuild ;
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
        this.validationForBuild = new SimpleBooleanProperty(false);
    }

    public synchronized String createSDMSuperMarket(String ownerName, InputStream inputStream) {
        String returnString = "";

        try {
            SuperDuperMarketDescriptor temp ;
            JAXBContext jaxbContext = JAXBContext.newInstance(SuperDuperMarketDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            temp = (SuperDuperMarketDescriptor) jaxbUnmarshaller.unmarshal(inputStream);
            returnString = XmlLoaderTask.buildSuperMarket(temp ,validationForBuild , this.getMySupermarkets() );
            if(validationForBuild.getValue()){
                MyOwner owner = this.getMyUsers().findOwnerByName(ownerName);
                owner.getZonesNames().add(temp.getSDMZone().getName()); // updates super zone name.
                this.mySupermarkets.addSuperMarketToList(owner,new MySuperMarket(temp,owner));
                validationForBuild.set(false); ; // after loading we reset the validation flag.
            }

        } catch (JAXBException e) {
            returnString = e.getMessage();
        } catch (NullPointerException e) {
            returnString = "<one of the members is NUll>";
        }

        return returnString;
    }

    public boolean isCordValid(String xCord, String yCord, String zoneName) {
        try {
            int x = Integer.parseInt(xCord);
            int y = Integer.parseInt(yCord);
            if(x<1||x>50||y<1||y>50)
                return false;
            MyLocation location = new MyLocation(x,y);
            List<MyStore> stores =  this.getMySupermarkets().getAreaStoresList(zoneName);
            for (MyStore store:stores) {
                MyLocation storeLocation = store.getMyLocation();
                if(storeLocation.compare(location))
                    return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
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

