package logic.Logic.My_CLASS;


import SDM_CLASS.SDMDiscount;
import SDM_CLASS.SDMOffer;
import SDM_CLASS.SDMStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStore {
    private SDMStore sdmStore;
    private MyStoreItems storeItems;
    private Map<Integer,MyOrder> storeOrderMap;
    private MyLocation myLocation;
    private List<MyStoreSingleOrderItems> storeSingleOrderItemsList;
    private  String ownerName ;
    private double totalOrdersCost = 0 ;
    private double totalDeliveryCosts = 0 ;
    /*
    ownerName
    totalOrder costs
    total deliveryCosts
    * */



    public MyStore(SDMStore sdmStore, MyItems items, String ownerName) {
        this.sdmStore = sdmStore;
        this.storeOrderMap = new HashMap<>();
        this.ownerName = ownerName;
        this.storeItems = new MyStoreItems(sdmStore,items);
        this.myLocation = new MyLocation(this.getSdmStore().getLocation());
        this.storeSingleOrderItemsList = new ArrayList<>();

    }


    public SDMStore getSdmStore() {
        return sdmStore;
    }

    public void setSdmStore(SDMStore sdmStore) {
        this.sdmStore = sdmStore;
    }

    public MyStoreItems getStoreItems() {
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
        return this.getSdmStore().getName();
    }

    public int getId() {
        return this.sdmStore.getId();
    }

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
}
