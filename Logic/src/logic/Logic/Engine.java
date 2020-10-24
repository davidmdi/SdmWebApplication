package logic.Logic;


import SDM_CLASS.SuperDuperMarketDescriptor;
import javafx.beans.property.SimpleBooleanProperty;
import logic.Logic.My_CLASS.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

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

    public synchronized boolean isCoordinateAreValid(int xCord, int yCord, String zoneName) {
            List<MyStore> stores = this.mySupermarkets.getAreaStoresList(zoneName);
            for (MyStore store : stores){ // hit's a store.
                if(store.getMyLocation().getSdmLocation().getX() == xCord ||
                store.getMyLocation().getSdmLocation().getY() == yCord)
                    return false;
            }
        return true ;
    }

    public synchronized boolean isStoreLocationValid(int x, int y){
        return((isThereACustomerInTheLocation(x,y) == false) &&
                (this.mySupermarkets.isThereAStoreInTheLocation(x, y) == false));
    }

    private synchronized boolean isThereACustomerInTheLocation(int x, int y){
        boolean res = false;

        for(MyCustomer customer : myUsers.getCustomerSet()){
            if(customer.getLocation() != null)
                if((customer.getLocation().getX() == x) && (customer.getLocation().getY() == y))
                    res = true;
        }

        return res;
    }

    public synchronized List<MyStoreItem.StoreItemJson> getStoreItemsJson(String zoneName, String selectedStore) {
        MySuperMarket superMarket = this.getMySupermarkets().getAreaSuperMarketByName(zoneName);
        MyStore store = superMarket.getStores().getSelectedStore(selectedStore);
        List<MyStoreItem.StoreItemJson> jsonList = store.getJsonItemList();
        return jsonList;

    }

    public MyStore.StoreJson getStoreJson(String zoneName, String selectedStore) {
        MySuperMarket superMarket = this.getMySupermarkets().getAreaSuperMarketByName(zoneName);
        MyStore store = superMarket.getStores().getSelectedStore(selectedStore);
        return store.getStoreJson();
    }

    public void addFeedBack(String zoneName, String customerName, MyFeedback.FeedbackJson feedbackJson) throws  ParseException{
        MySuperMarket superMarket = mySupermarkets.getAreaSuperMarketByName(zoneName);
        MyStore store = superMarket.getStores().getSelectedStore(feedbackJson.storeName);
        store.getStoreFeedbacks().addFeedback(new MyFeedback(customerName, feedbackJson));
        // ADD FEEDBACKS ALERT FOR OWNERS !!
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
    public void createStoreSingleOrderInstance(MyOrder order,MySuperMarket mySuperMarket,
         Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap){
        Map<MyStoreItem, Double> ordersQuantityMap = order.getQuantityMap();

        for (MyStoreItem storeItem: ordersQuantityMap.keySet()) {
            //initialize
            int storeId= storeItem.getStoreId();
            MyStore store = mySuperMarket.getStores().getStoreMap().get(storeId);
            double deliveryDistance = mySuperMarket.caculateDeliveryDistance(store,order.getCustomer().getLocation(),
                    store.getMyLocation());
            double deliveryCost = order.getDeliveryCostMap().get(storeId);

            //creat instance
            if(!storeSingleOrderItemsMap.containsKey(storeId)){
                MyStoreSingleOrderItems storeSingleOrderItems = new MyStoreSingleOrderItems(order.getOrderId(),
                        order.getDate(),storeId,order.getCustomer(),order.getOrderKind() ,deliveryDistance , deliveryCost );
                //saving the instance in map.
                storeSingleOrderItemsMap.put(storeId,storeSingleOrderItems);
            }

            // adding item to sub quantity map
            storeSingleOrderItemsMap.get(storeId).addToQuantityMap(storeItem,ordersQuantityMap.get(storeItem));

    }
  }



}

