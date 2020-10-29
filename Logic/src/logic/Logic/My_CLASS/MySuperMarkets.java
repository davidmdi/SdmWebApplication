package logic.Logic.My_CLASS;

import logic.Logic.Engine;

import java.util.*;

public class MySuperMarkets {
    Map<MyOwner, Set<MySuperMarket>> superMarkets;

    public MySuperMarkets() {
        this.superMarkets = new HashMap<>();
    }

    public Map<MyOwner, Set<MySuperMarket>> getSuperMarkets() {
        return superMarkets;
    }

    public void addSuperMarketToList(MyOwner owner, MySuperMarket superMarketToAdd) {
        if (this.superMarkets.containsKey(owner)) {
            this.superMarkets.get(owner).add(superMarketToAdd);
        } else {
            Set<MySuperMarket> temp = new HashSet<>();
            temp.add(superMarketToAdd);
            this.superMarkets.put(owner, temp);
        }
    }

    public Set<MySuperMarket> getAreaSuperMarketsSet(String areaName) {
        Set<MySuperMarket> zoneSuperMarkets = new HashSet<>();

        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                if (superMarket.getZoneName().equalsIgnoreCase(areaName))
                    zoneSuperMarkets.add(superMarket);
            }
        }

        return zoneSuperMarkets;
    }

    public synchronized MySuperMarket getAreaSuperMarketByName(String areaName) {
        MySuperMarket zoneSuperMarket = null;

        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                if (superMarket.getZoneName().equalsIgnoreCase(areaName))
                    zoneSuperMarket = (superMarket);
            }
        }

        return zoneSuperMarket;
    }


    public List<MyStore> getAreaStoresList(String areaName) {
        List<MyStore> areaStores = new ArrayList<>();
        Set<MySuperMarket> zoneSuperMarkets = getAreaSuperMarketsSet(areaName);

        for (MySuperMarket superMarket : zoneSuperMarkets) {
            areaStores.addAll(superMarket.getStores().getStoreList());
        }

        return areaStores;
    }

    public List<MyItem> getAreaItemsList(String areaName) {
        List<MyItem> areaItems = new ArrayList<>();
        Set<MySuperMarket> zoneSuperMarkets = getAreaSuperMarketsSet(areaName);

        for (MySuperMarket superMarket : zoneSuperMarkets) {
            areaItems.addAll(superMarket.getItems().getItemList());
        }

        return areaItems;
    }

    public synchronized boolean isThereAStoreInTheLocation(int x, int y) {
        boolean res = false;

        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                for (MyStore store : superMarket.getStores().getStoreList()) {
                    if ((store.getMyLocation().getX() == x) && (store.getMyLocation().getY() == y))
                        res = true;
                }
            }
        }

        return res;
    }

    public void createNewStore(MyStore.StoreJson store, String zoneName, String ownerName) {
        int storeId = generateStoreId();
        for (MyStoreItem.StoreItemJson storeItem : store.storeItems)
            storeItem.storeId = storeId;

        //List<MyItem> storeItemsToAdd = createStoreItemsToAddList(store, zoneName);
        //MyStore storeToAdd = new MyStore(storeId, store, storeItemsToAdd, ownerName);
        MySuperMarket superMarket = getAreaSuperMarketByName(zoneName);
        MyStore storeToAdd = new MyStore(storeId, store, ownerName);
        storeToAdd.createStoreItemsFromJson(store.storeItems, superMarket.getItems().getItemList());

        //add store to zone:
        superMarket.getStores().addStore(storeToAdd);
        //add alert only if the store owner is not the area owner!
        if (superMarket.getOwner().getUserName().equalsIgnoreCase(ownerName) == false) {
            addCompetingStoreAlert(superMarket, storeToAdd);
        }
    }

    private void addCompetingStoreAlert(MySuperMarket superMarket, MyStore store) {
        CompetingStoreAlert storeAlert = new CompetingStoreAlert(store.getOwnerName(), store.getName(),
                store.getMyLocation(), superMarket.getItems().getItemList().size(),
                store.getStoreItems().getItemsList().size());

        superMarket.getOwner().addAlert(storeAlert);
    }

    /* find max id and return (maxId+1) */
    private int generateStoreId() {
        int maxStoreId = 0;

        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                for (MyStore store : superMarket.getStores().getStoreList()) {
                    if (store.getId() > maxStoreId)
                        maxStoreId = store.getId();
                }
            }
        }

        return (++maxStoreId);
    }

    public synchronized List<String> getOwnerAlertsToString(String ownerName) {
        MyOwner owner = null;
        List<String> stringAlerts = new ArrayList<>();

        for (MyOwner myOwner : this.superMarkets.keySet()) {
            if (myOwner.getUserName().equalsIgnoreCase(ownerName))
                owner = myOwner;
        }

        for (Alertable alert : owner.getAlerts()) {
            stringAlerts.add(alert.alert());
        }

        owner.removeAllAlerts(); //Delete alerts so each alert appear only once!!

        return stringAlerts;
    }

    /*
    public void addNewOrderAlert(MyOrder order) {
        int storeId;
        String ownerName;
        MyOwner owner;
        OrderCreatedAlert newOrderAlert;

        for(MyStoreSingleOrderItems storeSingleOrder : order.getStoreSingleOrderItemsMap().values()){
            storeId = storeSingleOrder.getStoreId();
            ownerName = getStoreOwnerNameByStoreId(storeId);
            owner = findOwnerByName(ownerName);
            newOrderAlert = new OrderCreatedAlert(storeSingleOrder.getOrderId(), storeSingleOrder.getCustomer().getUserName(),
                    storeSingleOrder.getThisStoreQuantityMapFromOrderMapSize(),
                    storeSingleOrder.getOrderCost(), storeSingleOrder.getDeliveryCost());
            owner.addAlert(newOrderAlert);
        }
    }
     */

    public String getStoreOwnerNameByStoreId(int storeId) {
        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                for (MyStore store : superMarket.getStores().getStoreList()) {
                    if (store.getId() == storeId)
                        return store.getOwnerName();
                }
            }
        }

        return "";
    }

    public MyOwner findOwnerByName(String ownerName) {

        for (MyOwner owner : this.superMarkets.keySet()) {
            if (owner.getUserName().equalsIgnoreCase(ownerName))
                return owner;
        }

        return null;
    }

    public String getStoreOwnerNameByStoreName(String storeName) {

        for (Set<MySuperMarket> superMarketsSet : this.superMarkets.values()) {
            for (MySuperMarket superMarket : superMarketsSet) {
                for (MyStore store : superMarket.getStores().getStoreList()) {
                    if (store.getName().equalsIgnoreCase(storeName))
                        return store.getOwnerName();
                }
            }
        }

        return "";
    }


    public void updateOrder(MyOrder order, Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap,
                            MySuperMarket superMarket, Engine engine) {
        order.setStoreSingleOrderItemsMap(storeSingleOrderItemsMap);
        MyCustomer customer = order.getCustomer();
        // add order to customer
        customer.addOrder(order);
        //set orders Location
        order.setFromWhereOrderWasMade(customer.getLocation());

        // add order to super market orders
        superMarket.getOrders().addOrder(order);

        //  update items in stores
        for (Integer storeId : storeSingleOrderItemsMap.keySet()) {
            MyStore store = superMarket.getStores().getStoreMap().get(storeId);
            store.getStoreSingleOrderItemsList().add(storeSingleOrderItemsMap.get(storeId));
            Set<Integer> itemsIdSet = new HashSet<>();
            for (MyStoreItem storeItem
                    : storeSingleOrderItemsMap.get(storeId).getThisStoreQuantityMapFromOrder().keySet()) {
                int itemId = storeItem.getMyItem().getSdmItem().getId();

                // updating stores actual item - not storeItem that crated because of an offer.
                MyStoreItem actualItemFromStore = store.getStoreItems().getItemsMap().get(itemId);
                if (!itemsIdSet.contains(itemId)) {
                    actualItemFromStore.setHowManyTimeSold(actualItemFromStore.getHowManyTimeSold() + 1);
                    itemsIdSet.add(itemId);
                }
            }

        }

        // update MyItems how many times item sold.
        updatMyItemsHowManyTimeSold(order);
        updateMoneyTransfer(order, superMarket,engine);
        //addNewOrderAlert(order);
    }

    private void updateMoneyTransfer(MyOrder order, MySuperMarket superMarket, Engine engine) {
        double before = order.getCustomer().getUser().getAccount().getBalance();
        double amountTotransfer = order.getTotalCost();
        AccountAction actionForCustomer = new AccountAction("transfer", order.getDate(), amountTotransfer, before
                , before - amountTotransfer);
        order.getCustomer().getUser().getAccount().addAction(actionForCustomer);


        Set<Integer> myStoreSingleOrderItemsSet = order.getStoreSingleOrderItemsMap().keySet();
        for (int i : myStoreSingleOrderItemsSet) {
            MyStoreSingleOrderItems singleOrderItems = order.getStoreSingleOrderItemsMap().get(i);
            MyStore store = superMarket.getStores().getStoreMap().get(singleOrderItems.getStoreId());
            double beforeReceiving = superMarket.getOwner().getUser().getAccount().getBalance();
            double sumOfAction = singleOrderItems.calculatePrice() + singleOrderItems.getDeliveryCost();
            store.addPricesOfDeliveryAndItemsCosts(singleOrderItems);
            singleOrderItems.setOrderCost(singleOrderItems.calculatePrice()); // Updates order total cost
            double after = beforeReceiving + sumOfAction;
            AccountAction storeAction = new AccountAction("receive", order.getDate(),
                    sumOfAction, beforeReceiving, after);
            //this.getOwner().getUser().getAccount().addAction(storeAction);
            String ownerName = store.getOwnerName();
            MyOwner owner = engine.getMyUsers().findOwnerByName(ownerName);
            owner.getUser().getAccount().addAction(storeAction);

        }
    }

    private void updatMyItemsHowManyTimeSold(MyOrder order) {
        for (MyStoreItem storeItem : order.getQuantityMap().keySet()) {
            MyItem item = storeItem.getMyItem();
            item.setHowManyTimesItemSold(order.getQuantityMap().get(storeItem) + item.getHowManyTimesItemSold());
        }
    }
}

    /*
    public void addFeedBackAlert(MyOwner storeOwner, FeedbackAlert feedbackAlert) {
        String ownerName = "";
        MyOwner owner = null;

        for(Set<MySuperMarket> superMarkets : this.superMarkets.values()){
            for(MySuperMarket superMarket : superMarkets){
                for(MyStore store : superMarket.getStores().getStoreList()){
                    if(feedbackAlert.getStoreName().equalsIgnoreCase(store.getName()))
                        ownerName = store.getOwnerName();
                }
            }
        }


        for(MyOwner myOwner : this.superMarkets.keySet()){ // HERE !!
            if(myOwner.getUserName().equalsIgnoreCase(ownerName))
                owner = myOwner;
        }


        owner.addAlert(feedbackAlert);
    }
*/
