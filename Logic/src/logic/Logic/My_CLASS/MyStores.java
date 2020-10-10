package logic.Logic.My_CLASS;


import SDM_CLASS.SDMStore;
import SDM_CLASS.SDMStores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyStores {
    private SDMStores sdmStores;
    private List<MyStore> storeList;
    private Map<Integer, MyStore> storeMap;




    public MyStores(SDMStores sdmStores, MyItems items) {
        this.sdmStores = sdmStores;
        this.storeList = buildStoreList(items);
        this.storeMap = buildStoreMap();
    }


    // must creat MyStore List (buildStoreList() function ) before
    private Map<Integer, MyStore> buildStoreMap() {
        Map<Integer,MyStore> map = new HashMap<>();
        for (MyStore store:this.storeList) {
            map.put(store.getSdmStore().getId(),store);
        }
        return map;
    }

    private List<MyStore> buildStoreList(MyItems items) {
        List<MyStore> list = new ArrayList<>();
       List<SDMStore> sdm = getSdmStores().getSDMStore();
        for (SDMStore sdmStore:sdm) {
            list.add(new MyStore(sdmStore,items));
        }
        return list;
    }

    public SDMStores getSdmStores() {
        return sdmStores;
    }

    public void setSdmStores(SDMStores sdmStores) {
        this.sdmStores = sdmStores;
    }

    public List<MyStore> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<MyStore> storeList) {
        this.storeList = storeList;
    }

    public Map<Integer, MyStore> getStoreMap() {
        return storeMap;
    }

    public void setStoreMap(Map<Integer, MyStore> storeMap) {
        this.storeMap = storeMap;
    }


    public List<String> getStoresName() {
        List<String> list = new ArrayList<>();
        for (MyStore store : this.getStoreList())
            list.add("store id: "+store.getId() + ", store name: " + store.getName() + ", location: " + store.getMyLocation());
        return list;
    }
}
