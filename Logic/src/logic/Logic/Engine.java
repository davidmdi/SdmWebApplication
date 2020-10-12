package logic.Logic;


import SDM_CLASS.SuperDuperMarketDescriptor;
import javafx.beans.property.SimpleBooleanProperty;
import logic.Logic.My_CLASS.MySuperMarkets;
import logic.Logic.My_CLASS.MyUsers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class
Engine {
     // need to have instance of superMarket!!!!!
    /*
    Engine:MySuperMarkets supermarkets, Users users.
s     */
   private MySuperMarkets mySupermarkets;
   private MyUsers myUsers;
   private SimpleBooleanProperty isSuperMarketIsValid ;

    public MySuperMarkets getMySupermarkets() {
        return mySupermarkets;
    }

    public MyUsers getMyUsers() {
        return myUsers;
    }


    public boolean isIsSuperMarketIsValid() {
        return isSuperMarketIsValid.get();
    }

    public SimpleBooleanProperty isSuperMarketIsValidProperty() {
        return isSuperMarketIsValid;
    }

    public Engine() {
        this.myUsers = new MyUsers();
        this.mySupermarkets = null ;
        isSuperMarketIsValid = new SimpleBooleanProperty(false);
    }

    public synchronized String loadFileFromOwner(String ownerName, InputStream inputStream) {
        try {
            JAXBContext jc = JAXBContext.newInstance("SDM_CLASS");
            Unmarshaller u = jc.createUnmarshaller();
            SuperDuperMarketDescriptor sdmJAXB = (SuperDuperMarketDescriptor) u.unmarshal(inputStream);
            //sdmJAXB.getSDMZone().getName();
            // buildsuper()...

        }catch (JAXBException e){e.printStackTrace();}


        return new String();
    }


    //    public boolean isIsSuperMarketIsValid() {
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
////    public void newLoadXml(String path , xmlController controler) throws ExecutionException, InterruptedException {
////        Consumer<MySuperMarket> superMarketConsumer = v-> {this.mySupermarket = v ;};
////        Consumer<Boolean> validationConsumer = v -> {this.isSuperMarketIsValid.set(v);} ;
////        XmlLoaderTask task = new XmlLoaderTask (path,superMarketConsumer,validationConsumer);
////        controler.bindToXmlLoaderGui(task); // wire up
////        new Thread(task).start();
////    }
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

