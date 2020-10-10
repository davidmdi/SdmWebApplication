package logic.Logic.My_CLASS;


import SDM_CLASS.SDMItem;
import SDM_CLASS.SDMItems;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

public class MyItems {
    private SDMItems sdmItems;
    private List<MyItem>itemList;
    private Map<Integer,MyItem>itemsMap;


    public MyItems(SDMItems sdmItems) {
        this.sdmItems = sdmItems;
        this.itemsMap = buildItemMap(sdmItems);
        this.itemList = buildItemsList();
    }

    private List<MyItem> buildItemsList() {
        List<MyItem>list = new ArrayList<>();
        for (int itemId:itemsMap.keySet()) {
            list.add(itemsMap.get(itemId));
        }
        return list;
    }

    private Map<Integer, MyItem> buildItemMap(SDMItems sdmItems) {
        Map<Integer,MyItem> map = new HashMap<>();
        for (SDMItem item :sdmItems.getSDMItem()) {
            map.put(item.getId(),new MyItem(item));
        }

        return map;
    }

    public SDMItems getSdmItems() {
        return sdmItems;
    }

    public void setSdmItems(SDMItems sdmItems) {
        this.sdmItems = sdmItems;
    }

    public Map<Integer, MyItem> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(Map<Integer, MyItem> itemsMap) {
        this.itemsMap = itemsMap;
    }

    public List<MyItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<MyItem> itemList) {
        this.itemList = itemList;
    }

    public ObservableList<MyItem> getItemsObesrvableListSdm() {
        ObservableList<MyItem> returnValue = FXCollections.observableArrayList();
        Set<Integer> items = this.getItemsMap().keySet();
        for (int item : items) {
            returnValue.add(this.getItemsMap().get(item));
        }

        return returnValue;
    }

    public ObservableList<MyItem> getItemsObesrvableList() {
        ObservableList<MyItem> returnValue = FXCollections.observableArrayList();
        for (Integer id : this.itemsMap.keySet()) {
            returnValue.add(this.itemsMap.get(id));
        }

        return returnValue;

    }
}
