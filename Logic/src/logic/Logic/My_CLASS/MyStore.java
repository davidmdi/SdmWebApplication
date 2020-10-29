package logic.Logic.My_CLASS;


import SDM_CLASS.SDMDiscount;
import SDM_CLASS.SDMOffer;
import SDM_CLASS.SDMStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class MyStore {
    private SDMStore sdmStore;
    private MyStoreItems storeItems;
    private Map<Integer,MyOrder> storeOrderMap;
    private MyLocation myLocation;
    private List<MyStoreSingleOrderItems> storeSingleOrderItemsList;
    private  String ownerName ;
    private double totalOrdersCost = 0 ;
    private double totalDeliveryCosts = 0 ;
    private int totalOrdersItemsCost = 0 ;
    private double totalOrdersDeliveryCost = 0.0 ;

    private MyFeedbacks storeFeedbacks;
    private String name;
    private int PPK;
    private int id;

    /*
    ownerName
    totalOrder costs
    total deliveryCosts
    * */

    public MyStore(SDMStore sdmStore, MyItems items, String ownerName) {
        this.id = sdmStore.getId();
        this.name = sdmStore.getName();
        this.PPK = sdmStore.getDeliveryPpk();
        this.sdmStore = sdmStore;
        this.storeOrderMap = new HashMap<>();
        this.ownerName = ownerName;
        this.storeItems = new MyStoreItems(sdmStore,items);
        this.myLocation = new MyLocation(this.getSdmStore().getLocation());
        this.storeSingleOrderItemsList = new ArrayList<>();
        this.storeFeedbacks = new MyFeedbacks();
    }

    public MyStore(int id, StoreJson store, String ownerName) {
        this.id = id;
        this.name = store.name;
        this.PPK = store.ppk;
        this.ownerName = ownerName;
        this.storeOrderMap = new HashMap<>();

        this.storeItems = new MyStoreItems();
        //createStoreItemsFromJson(store.storeItems);
        this.myLocation = new MyLocation(store.x, store.y);
        this.storeSingleOrderItemsList = new ArrayList<>();

        this.storeFeedbacks = new MyFeedbacks();

    }

    public void createStoreItemsFromJson(List<MyStoreItem.StoreItemJson> storeItems, List<MyItem> superMarketItems) {
        MyStoreItem newStoreItem;
        MyItem item;
        MyStoreItem storeItem;
        int index = 0;

        for(MyStoreItem.StoreItemJson storeItemJson : storeItems){
            for(MyItem superItem : superMarketItems) {
                if(superItem.getItemId() == storeItemJson.jsonItem.id){
                    //item = new MyItem(storeItemJson.jsonItem.id, storeItemJson.jsonItem.name, storeItemJson.jsonItem.purchaseMethod);
                    storeItem = new MyStoreItem(superItem, storeItemJson.price, storeItemJson.storeId, "store");
                    this.storeItems.addStoreItem(storeItem);
                }
            }
        }

    }

//    private void createStoreItems(List<MyItem.ItemJson> itemsJson, List<MyItem> storeItemsToAdd){
//        MyStoreItem newStoreItem;
//        int index = 0;
//
//        for(MyItem item : storeItemsToAdd){
//            // MyStoreItem.itemKind == "store" ???
//            newStoreItem = new MyStoreItem(item, itemsJson.get(index++).price, this.id, "store");
//            this.storeItems.addStoreItem(newStoreItem);
//        }
//    }


    public MyFeedbacks getStoreFeedbacks() {
        return storeFeedbacks;
    }

    public SDMStore getSdmStore() {
        return sdmStore;
    }

    public void setSdmStore(SDMStore sdmStore) {
        this.sdmStore = sdmStore;
    }

    public synchronized MyStoreItems getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(MyStoreItems storeItems) {
        this.storeItems = storeItems;
    }

    public Map<Integer, MyOrder> getStoreOrderMap() {
        return storeOrderMap;
    }

    public void setStoreOrderMap(Map<Integer, MyOrder> storeOrderMap) {
        this.storeOrderMap = storeOrderMap;
    }

    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
    }


    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public int getPPK() { return PPK; }

    public String getOwnerName() {
        return ownerName;
    }

    public double getTotalOrdersCost() {
        return totalOrdersCost;
    }

    public double getTotalDeliveryCosts() {
        return totalDeliveryCosts;
    }

    @Override
    public String toString() {
        return "Store id: " + getId() +
                 " ,Store name: " + getName() +
                " ,Location:" + getMyLocation();
    }

    public List<MyStoreSingleOrderItems> getStoreSingleOrderItemsList() {
        return storeSingleOrderItemsList;
    }

    public void setStoreSingleOrderItemsList(List<MyStoreSingleOrderItems> storeSingleOrderItemsList) {
        this.storeSingleOrderItemsList = storeSingleOrderItemsList;
    }

    public double calculateTotalDelivryEarn() {
        double retValue = 0 ;
        for (MyStoreSingleOrderItems items: this.getStoreSingleOrderItemsList()) {
            retValue = retValue + items.getDeliveryCost();
        }
        return retValue;
    }



    public ObservableList<StoreItemView> createObservableListForItemsView() {
        ObservableList<StoreItemView> observableList = FXCollections.observableArrayList();
        for (MyStoreItem storeItem:this.getStoreItems().getItemsList()) {
            observableList.add(new StoreItemView(storeItem));
        }

        return observableList;
    }

    public ObservableList<StoreOrdersView> createObservableListForOrdersView() {
        ObservableList<StoreOrdersView> observableList = FXCollections.observableArrayList();
        for (MyStoreSingleOrderItems singleOrderItems:this.getStoreSingleOrderItemsList() ) {
            observableList.add(new StoreOrdersView(singleOrderItems));
        }
        return observableList;
    }

    public ObservableList<StoreOffersView> createObservableListForOffersView() {
        ObservableList<StoreOffersView> observableList = FXCollections.observableArrayList();
        if(this.getSdmStore().getSDMDiscounts() != null) {
            for (SDMDiscount discount : this.getSdmStore().getSDMDiscounts().getSDMDiscount()) {
                observableList.add(new StoreOffersView(discount, this));
            }
        }
        return observableList;
    }

    public void deleteItemFromStore(MyStoreItem storeItem) {
        storeItems.getItemsList().remove(storeItem);
        storeItems.getItemsMap().remove(storeItem.getMyItem().getItemId(),storeItem);
        deleteOfferThatContainsThisItem(storeItem);
        }

    private void deleteOfferThatContainsThisItem(MyStoreItem storeItem) {
        int itemId  = storeItem.getMyItem().getItemId();
        List<SDMDiscount> discountList = this.getSdmStore().getSDMDiscounts().getSDMDiscount();
        List<SDMDiscount> discontToremove = new ArrayList<>();
        for (SDMDiscount discount:discountList) {
            if(discount.getIfYouBuy().getItemId() == itemId)
                discontToremove.add(discount);
            else {
                for (SDMOffer offer:discount.getThenYouGet().getSDMOffer()) {
                    if(offer.getItemId()==itemId)
                        discontToremove.add(discount);
                }
            }
        }
        if(!discontToremove.isEmpty())
            for (SDMDiscount discount:discontToremove) {
                discountList.remove(discount);
            }

    }

    public boolean addItemToStore(MyItem item,int price) {
        if(storeItems.getItemsMap().containsKey(item.getItemId()))
            return false;
        else{
            MyStoreItem addingItem= new MyStoreItem(item,price,this.getId(),"store");
            this.storeItems.getItemsMap().put(item.getItemId(),addingItem);
            this.storeItems.getItemsList().add(addingItem);
            return true;
        }
    }

    public void updatePrice(MyStoreItem storeItem, int newPrice) {
        MyStoreItem itemToUpdate =  this.storeItems.getItemsMap().get(storeItem.getMyItem().getItemId());
        itemToUpdate.setPrice(newPrice);
    }

    public synchronized List<MyStoreItem.StoreItemJson> getJsonItemList() {
        List<MyStoreItem.StoreItemJson> returnList = new ArrayList<>();

        for(MyStoreItem storeItem : this.storeItems.getItemsList()){
            MyItem.ItemJson itemJson = new MyItem.ItemJson(storeItem.getStoreId(),storeItem.getName(),
                    storeItem.getMyItem().getPurchaseCategory());
            returnList.add(new MyStoreItem.StoreItemJson(this.id,itemJson));
        }
        return  returnList;

    }

    public StoreJson getStoreJson() {
        List<MyStoreItem.StoreItemJson> storeItemsJson = new LinkedList<>();
        for(MyStoreItem storeItem : this.storeItems.getItemsList()){
            storeItemsJson.add(new MyStoreItem.StoreItemJson(this.id,
                    new MyItem.ItemJson(storeItem.getMyItem().getItemId() ,
                            storeItem.getName() , storeItem.getMyItem().getPurchaseCategory())));
        }
        return new StoreJson(this.name, this.myLocation.getX(), this.myLocation.getY(), this.PPK, storeItemsJson);
    }

    public void setTotalDeliveryCosts(double totalDeliveryCosts) {
        this.totalDeliveryCosts = totalDeliveryCosts;
    }

    public int getTotalOrdersItemsCost() {
        return totalOrdersItemsCost;
    }

    public void setTotalOrdersItemsCost(int totalOrdersItemsCost) {
        this.totalOrdersItemsCost = totalOrdersItemsCost;
    }

    public double getTotalOrdersDeliveryCost() {
        return totalOrdersDeliveryCost;
    }

    public void addPricesOfDeliveryAndItemsCosts(MyStoreSingleOrderItems singleOrderItems) {
        this.totalOrdersDeliveryCost +=singleOrderItems.getDeliveryCost();
        this.totalOrdersItemsCost +=singleOrderItems.calculatePrice();
    }


    public class StoreJson{
        public String name;
        public int x;
        public int y;
        public int ppk;
        public List<MyStoreItem.StoreItemJson> storeItems;

        public StoreJson(String name, int x, int y, int ppk, List<MyStoreItem.StoreItemJson> storeItems){
            this.name = name;
            this.x = x;
            this.y = y;
            this.ppk = ppk;
            this.storeItems = storeItems;
        }

        @Override
        public String toString() {
            return "Store{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", ppk=" + ppk +
                    ", items=" + storeItems +
                    '}';
        }
    }
}

/*
public class StoreJson{
        public String name;
        public int x;
        public int y;
        public int ppk;
        public List<MyItem.ItemJson> items;

        public StoreJson(String name, int x, int y, int ppk, List<MyItem.ItemJson> items){
            this.name = name;
            this.x = x;
            this.y = y;
            this.ppk = ppk;
            this.items = items;
        }

        @Override
        public String toString() {
            return "Store{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", ppk=" + ppk +
                    ", items=" + items +
                    '}';
        }
    }
 */
