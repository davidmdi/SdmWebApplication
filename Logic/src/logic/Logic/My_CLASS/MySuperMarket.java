package logic.Logic.My_CLASS;


import SDM_CLASS.SDMDiscount;
import SDM_CLASS.SuperDuperMarketDescriptor;
import javafx.scene.control.DatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class MySuperMarket {
   private SuperDuperMarketDescriptor sdmSuper;
   private MyOwner owner;
   private MyItems items;
   private MyStores stores;
   private MyOrders orders;
   private String  zoneName ;
   //MyCustomers customers;



    public MySuperMarket(SuperDuperMarketDescriptor sdmSuper , MyOwner owner) {
        this.owner = owner;
        this.sdmSuper = sdmSuper;
        this.items = new MyItems(sdmSuper.getSDMItems());
        this.stores = new MyStores(sdmSuper.getSDMStores(),items,owner.getUserName());
        this.orders = new MyOrders();
        this.zoneName = sdmSuper.getSDMZone().getName();
        //this.customers = new MyCustomers(sdmSuper.getSDMCustomers()) ;

        //updates for each item avg price & how many stores sells each item.
        updateMyItemsParameters();

    }

    public void updateMyItemsParameters() {
        double updatedPrice = 0 ;
        Map<Integer,Integer> itemPriceMap = new HashMap<>(); // save prices sum fo avg.
        for (MyStore store: this.getStores().getStoreList()) {
            for (MyStoreItem storeItem:store.getStoreItems().getItemsList()) {
                MyItem myItem = storeItem.getMyItem();
                int itemId = myItem.getSdmItem().getId();
                myItem.setHowManyStoresSellsThisItem(myItem.getHowManyStoresSellsThisItem() + 1);
                if(!itemPriceMap.containsKey(itemId)){
                    itemPriceMap.put(itemId,storeItem.getPrice());
                    updatedPrice = storeItem.getPrice();
                }
                else{
                    double oldPrice = itemPriceMap.get(itemId);
                     updatedPrice = oldPrice + storeItem.getPrice();
                    itemPriceMap.put(itemId,(int)updatedPrice);
                }
                myItem.setAverageItemPrice(updatedPrice/myItem.getHowManyStoresSellsThisItem());
            }
        }
    }

    public SuperDuperMarketDescriptor getSdmSuper() {
        return sdmSuper;
    }

    public void setSdmSuper(SuperDuperMarketDescriptor sdmSuper) {
        this.sdmSuper = sdmSuper;
    }

    public synchronized MyStores getStores() {
        return stores;
    }

    public synchronized void setStores(MyStores stores) {
        this.stores = stores;
    }

    public MyItems getItems() {
        return items;
    }

    public void setItems(MyItems items) {
        this.items = items;
    }

    public MyOrders getOrders() {
        return orders;
    }

    public void setOrders(MyOrders orders) {
        this.orders = orders;
    }

    public MyOwner getOwner() {
        return owner;
    }

    public String getZoneName() {
        return zoneName;
    }

    /*
    public MyCustomers getCustomers() {
        return customers;
    }

    public void setCustomers(MyCustomers customers) {
        this.customers = customers;
    }
*/
//    public void addItemToMapFromGui(MyStoreItem item, Map<MyStoreItem, Double> selectedItemsMap) {
//
//            if(selectedItemsMap.containsKey(item))
//                selectedItemsMap.replace(item,item.getSpinner().getValue());
//            else
//                selectedItemsMap.put(item,item.getSpinner().getValue());
//
//    }

    public Double caculateDeliveryDistance(MyStore store, MyLocation M, MyLocation N) {
        int Mx = M.getX();
        int My = M.getY();
        int Nx = N.getX();
        int Ny = N.getY();
        int dx = Mx-Nx;
        int dy = My-Ny;
        return (Math.sqrt(dx*dx + dy*dy));
    }

    public Double calculateDeliveryCost(MyStore value, MyLocation M, MyLocation N) {
        double distance = caculateDeliveryDistance(value, M, N);
        return distance * value.getPPK();
    }

    public double calculateItemCost(Map<MyStoreItem, Double> selectedItemsMap) {
        double cost = 0;
        for (MyStoreItem item:selectedItemsMap.keySet()) {
            double quantity = selectedItemsMap.get(item);
            int price = item.getPrice();
            cost += (quantity * price);
        }
        return cost;
    }

    public void cleanZerosFromSelcetedItemsMap(Map<MyStoreItem, Double> selectedItemsMap) {
        for (MyStoreItem item: selectedItemsMap.keySet()) {
            if(selectedItemsMap.get(item) == 0.0)
                selectedItemsMap.remove(item);
        }
    }

    public MyOrder buildOrder(DatePicker orderDatePicker, MyCustomer customer, Map<MyStoreItem, Double> selectedItemsMap,
                           String orderKind, double orderCost, Map<Integer, Double> deliveryMap) {

        MyOrder order = new MyOrder( getDateFromDatePicker(orderDatePicker),  customer,  selectedItemsMap,
                 orderKind, deliveryMap);

//        resetSpinners(selectedItemsMap.keySet()); // reset spinner to selected items
        return order;
    }

    //updates order to customer orders , super market orders and store sold items.
    public void updateOrder(MyOrder order, Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap){
        order.setStoreSingleOrderItemsMap(storeSingleOrderItemsMap);
        MyCustomer customer = order.getCustomer();
        // add order to customer
        customer.addOrder(order);
        //set orders Location
        order.setFromWhereOrderWasMade(customer.getLocation());

        // add order to super market orders
        this.getOrders().addOrder(order);

        //  update items in stores
        for (Integer storeId: storeSingleOrderItemsMap.keySet()) {
            MyStore store = this.getStores().getStoreMap().get(storeId);
            store.getStoreSingleOrderItemsList().add(storeSingleOrderItemsMap.get(storeId));
            Set<Integer> itemsIdSet = new HashSet<>();
            for (MyStoreItem storeItem
                    :storeSingleOrderItemsMap.get(storeId).getThisStoreQuantityMapFromOrder().keySet() ) {
                int itemId = storeItem.getMyItem().getSdmItem().getId();

                // updating stores actual item - not storeItem that crated because of an offer.
                MyStoreItem actualItemFromStore = store.getStoreItems().getItemsMap().get(itemId);
                if(!itemsIdSet.contains(itemId)) {
                   actualItemFromStore.setHowManyTimeSold(actualItemFromStore.getHowManyTimeSold()+1);
                    itemsIdSet.add(itemId);
                }
            }

        }

        // update MyItems how many times item sold.
        updatMyItemsHowManyTimeSold(order);
        updateMoneyTransfer(order);
        addNewOrderAlert(order);
    }

    private void addNewOrderAlert(MyOrder order) {
        /*
        for(MyStoreSingleOrderItems storeSingleOrder : order.getStoreSingleOrderItemsMap().values()){

        }
         */
    }

    private void updateMoneyTransfer(MyOrder order) {
        double before = order.getCustomer().getUser().getAccount().getBalance();
        double amountTotransfer = order.getTotalCost();
        AccountAction actionForCustomer = new AccountAction("transfer",order.getDate(),amountTotransfer,before
        ,before-amountTotransfer);
        order.getCustomer().getUser().getAccount().addAction(actionForCustomer);
        Set<Integer> myStoreSingleOrderItemsSet = order.getStoreSingleOrderItemsMap().keySet();
        for(int i : myStoreSingleOrderItemsSet ){
            MyStoreSingleOrderItems singleOrderItems = order.getStoreSingleOrderItemsMap().get(i);
            MyStore store = this.getStores().getStoreMap().get(singleOrderItems.getStoreId());
            double  beforeReceiving = this.getOwner().getUser().getAccount().getBalance();
            double sumOfAction = singleOrderItems.calculatePrice() + singleOrderItems.getDeliveryCost();
            store.addPricesOfDeliveryAndItemsCosts(singleOrderItems);
            singleOrderItems.setOrderCost(singleOrderItems.calculatePrice()); // Updates order total cost
            double after = beforeReceiving + sumOfAction;
            AccountAction storeAction = new AccountAction("receive",order.getDate(),
                    sumOfAction,beforeReceiving,after);
            this.getOwner().getUser().getAccount().addAction(storeAction);
        }
    }

    private void updatMyItemsHowManyTimeSold(MyOrder order) {
        for (MyStoreItem storeItem: order.getQuantityMap().keySet()) {
            MyItem item = storeItem.getMyItem();
            item.setHowManyTimesItemSold(order.getQuantityMap().get(storeItem) + item.getHowManyTimesItemSold());
        }
    }

    private Date getDateFromDatePicker(DatePicker orderDatePicker) {
        LocalDate localDate = orderDatePicker.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
         return Date.from(instant);
    }

//    public void resetSpinners(Set<MyStoreItem> keySet) {
//        for (MyStoreItem item:keySet) {
//            item.getMyItem().setSpinnerValue(0.0);
//        }
//    }

    public Map<MyStoreItem, Double> findBestPrice(Map<MyItem, Double> selectedMyItemsMap) {
        Map<MyStoreItem,Double> map = new HashMap<>();
        List<MyStore> storeList = this.getStores().getStoreList();
        for (MyItem item:selectedMyItemsMap.keySet()) {
            MyStoreItem storeItem = null;
            for (MyStore store: storeList) {
                Map<Integer,MyStoreItem>itemsMap = store.getStoreItems().getItemsMap();
                if(itemsMap.containsKey(item.getSdmItem().getId())){
                    if(storeItem == null)
                        storeItem = itemsMap.get(item.getSdmItem().getId());
                    else
                        if(storeItem.getPrice() > itemsMap.get(item.getSdmItem().getId()).getPrice())
                            storeItem = itemsMap.get(item.getSdmItem().getId());
                }

            }
            map.put(storeItem,selectedMyItemsMap.get(storeItem.getMyItem()));
        }
        return map;
    }

    public void calculateDeliveryCostMap(Map<MyStoreItem, Double> myStoreItemDoubleMap,
                                         Map<Integer, Double> deliveryMap, MyCustomer customer) {
        for (MyStoreItem storeItem:myStoreItemDoubleMap.keySet()) {
            if(!deliveryMap.containsKey(storeItem.getStoreId())){
                MyStore store = this.getStores().getStoreMap().get(storeItem.getStoreId());
                deliveryMap.put(storeItem.getStoreId(),
                        calculateDeliveryCost(store,store.getMyLocation(),customer.getLocation()));
            }
        }
    }

//    public void resetSpinnersForDynamic(Map<MyItem, Double> selectedMyItemsMap) {
//        for (MyItem item:selectedMyItemsMap.keySet()) {
//            item.setSpinnerValue(0.0);
//        }
//
//    }

    public int getDiscountStoreId(SDMDiscount discount) {
        int returnInt = -1 ;
        for (MyStore store:this.getStores().getStoreList()) {
            if(store.getSdmStore().getSDMDiscounts() != null)
            for (SDMDiscount sdmDiscount: store.getSdmStore().getSDMDiscounts().getSDMDiscount()) {
                    if(discount.getName().equals(sdmDiscount.getName()))
                        return store.getId();


            }
        }
        return returnInt;
    }


    public boolean isItemSoldInOtherStorBeforeDelating(MyStoreItem storeItem) {
        int counter = 0 ;
        for (MyStore store: stores.getStoreList()) {
            if(store.getStoreItems().getItemsMap().keySet().contains(storeItem.getMyItem().getItemId()))
                counter++;
        }
        if(counter > 1)
            return  true;
        else
        return false;
    }

    /* return Map<StoreId, StoreName>*/
    public Map<Integer, String> createStoresInfoMap(Set<Integer> orderStoresIdSet) {
        Map<Integer, String> storesInfo = new HashMap<>();
        MyStore currStore;

        for(Integer storeId : orderStoresIdSet){
            currStore = this.getStores().getStoreMap().get(storeId);
            storesInfo.put(storeId, currStore.getName());
        }

        return storesInfo;
    }

//    public int getMaxRows() {
//        List<MyStore> storeList = stores.getStoreList();
//        //List<MyCustomer> customerList = customers.getCustomerList();
//
//        int maxRowStore = 0 ;
//        int maxRowCustomer=0;
//        for (MyStore store: storeList) {
//            int y = store.getMyLocation().getSdmLocation().getY();
//            if(y>maxRowStore)
//                maxRowStore = y ;
//        }
//        for (MyCustomer customer: customerList) {
//            int y = customer.getLocation().getSdmLocation().getY();
//            if(y>maxRowCustomer)
//                maxRowCustomer = y ;
//        }
//
//
//        return Math.max(maxRowCustomer,maxRowStore);
//    }

//    public int getMaxCols() {
//        List<MyStore> storeList = stores.getStoreList();
//       // List<MyCustomer> customerList = customers.getCustomerList();
//
//        int maxColStore = 0 ;
//        int maxColCustomer=0;
//        for (MyStore store: storeList) {
//            int x = store.getMyLocation().getSdmLocation().getX();
//            if(x > maxColStore)
//                maxColStore = x ;
//        }
//        for (MyCustomer customer: customerList) {
//            int x = customer.getLocation().getSdmLocation().getX();
//            if(x>maxColCustomer)
//                maxColCustomer = x ;
//        }
//
//
//        return Math.max(maxColCustomer, maxColStore);
//    }
}
