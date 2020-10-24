package logic.Logic.My_CLASS;


import SDM_CLASS.SDMSell;
import SDM_CLASS.SDMStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStoreItems {
    private List<MyStoreItem> itemsList;
    private Map<Integer,MyStoreItem> itemsMap;

    public MyStoreItems(SDMStore sdmStore, MyItems items) {
        this.itemsList = buildItemsList(sdmStore,items);
        this.itemsMap = builedItemsMap();
    }

    public MyStoreItems() {
        this.itemsList = new ArrayList<>();
        this.itemsMap = new HashMap<>();
    }

    public void addStoreItem(MyStoreItem item){
        this.itemsList.add(item);
        this.itemsMap.put(item.getMyItem().getItemId(), item);
    }


    // only after creating the list.
    private Map<Integer, MyStoreItem> builedItemsMap() {
        Map<Integer, MyStoreItem> map = new HashMap<>();
        for ( MyStoreItem item : itemsList) {
            map.put(item.getMyItem().getSdmItem().getId() , item);
        }
        return map;
    }

    private List<MyStoreItem> buildItemsList(SDMStore sdmStore, MyItems items) {
        List<MyStoreItem> list = new ArrayList<>();
        for (SDMSell sell:sdmStore.getSDMPrices().getSDMSell()) {
            int id = sell.getItemId();
            list.add(new MyStoreItem(items.getItemsMap().get(id),sell.getPrice(),sdmStore.getId(),"store"));
        }
        return list;
    }


    public List<MyStoreItem> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<MyStoreItem> itemsList) {
        this.itemsList = itemsList;
    }

    public synchronized Map<Integer, MyStoreItem> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(Map<Integer, MyStoreItem> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public ObservableList<MyStoreItem> buildObservList() {
        ObservableList<MyStoreItem> list = FXCollections.observableArrayList();
        for (MyStoreItem item:this.getItemsList()) {
            list.add(item);
        }

        return list;

    }
}
