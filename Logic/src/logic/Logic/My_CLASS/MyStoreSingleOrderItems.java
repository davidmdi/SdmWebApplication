package logic.Logic.My_CLASS;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyStoreSingleOrderItems {
    private int orderId;
    private Date date;
    private int storeId;
    private MyCustomer customer;
    private Map<MyStoreItem,Double> thisStoreQuantityMapFromOrder;
    private double distanceFromCustomer;
    private double deliveryCost;
    private double orderCost ;
    private int thisStoreQuantityMapFromOrderMapSize;
    private String orderKind;
    private MyLocation fromWhereOrderWasMade ;

    public MyStoreSingleOrderItems(int orderId, Date date, int storeId, MyCustomer customer, String orderKind,
                                   double deliveryDistance, double deliveryCost) {
        this.orderId = orderId;
        this.date = date;
        this.storeId = storeId;
        this.customer = customer;
        this.orderKind = orderKind;
        this.thisStoreQuantityMapFromOrder = new HashMap<>();
        this.thisStoreQuantityMapFromOrderMapSize=0;
        this.distanceFromCustomer = deliveryDistance;
        this.deliveryCost = deliveryCost;

    }

    public void addToQuantityMap(MyStoreItem key , Double value){
        this.thisStoreQuantityMapFromOrder.put(key,value);
        thisStoreQuantityMapFromOrderMapSize++;

    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public MyCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(MyCustomer customer) {
        this.customer = customer;
    }

    public Map<MyStoreItem, Double> getThisStoreQuantityMapFromOrder() {
        return thisStoreQuantityMapFromOrder;
    }

    public void setThisStoreQuantityMapFromOrder(Map<MyStoreItem, Double> thisStoreQuantityMapFromOrder) {
        this.thisStoreQuantityMapFromOrder = thisStoreQuantityMapFromOrder;
    }

    public double getDistanceFromCustomer() {
        return distanceFromCustomer;
    }

    public void setDistanceFromCustomer(double distanceFromCustomer) {
        this.distanceFromCustomer = distanceFromCustomer;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public int getThisStoreQuantityMapFromOrderMapSize() {
        return thisStoreQuantityMapFromOrderMapSize;
    }

    public void setThisStoreQuantityMapFromOrderMapSize(int thisStoreQuantityMapFromOrderMapSize) {
        this.thisStoreQuantityMapFromOrderMapSize = thisStoreQuantityMapFromOrderMapSize;
    }

    public String getOrderKind() {
        return orderKind;
    }

    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public Date getDate() {
        return date;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
    }

    public MyLocation getFromWhereOrderWasMade() {
        return fromWhereOrderWasMade;
    }

    public void setFromWhereOrderWasMade(MyLocation fromWhereOrderWasMade) {
        this.fromWhereOrderWasMade = fromWhereOrderWasMade;
    }

    public ObservableList<MySingleOrderTableView> createObservableListForOrderSummery() {
        ObservableList<MySingleOrderTableView> observableList = FXCollections.observableArrayList();
        for (MyStoreItem item:this.thisStoreQuantityMapFromOrder.keySet()) {
            observableList.add(new MySingleOrderTableView(item,this.thisStoreQuantityMapFromOrder.get(item)));
        }
        return observableList;
    }

    public double calculatePrice() {
        double retValue = 0 ;
        for (MyStoreItem item:this.getThisStoreQuantityMapFromOrder().keySet()) {
            if(item.getItemKind().equals("offer"))
                retValue = retValue + item.getPrice();
            else
                retValue = retValue + item.getPrice() * thisStoreQuantityMapFromOrder.get(item);
        }
        return retValue;
    }
}
