package logic.Logic.My_CLASS;


import SDM_CLASS.SDMOffer;
import SDM_CLASS.ThenYouGet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MyOrder {
    private static int idGenerator = 1 ;
    private int orderId;
    private Date date;
    private MyCustomer customer;
    private String orderKind; // static or dynamic
    private Map<MyStoreItem,Double> quantityMap; // <MystoreItem,quantity>
    private double orderCost;
    private double deliveryCost;// initialized in total cost calculation
    private Map<Integer, Double>deliveryCostMap; // <storeId,deliveryCost>
    private double totalCost;
    private Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap;
    private MyLocation fromWhereOrderWasMade ;


    public MyOrder(Date date, MyCustomer customer, Map<MyStoreItem, Double> quantityMap, String orderKind,
                    Map<Integer,Double>deliveryCostMap) {
        this.orderId = idGenerator++;
        this.date = date;
        this.customer = customer;
        this.orderKind = orderKind;
        this.quantityMap = quantityMap;
        this.orderCost = calculateorderCost();
        this.deliveryCostMap = deliveryCostMap; // parameter : Set<MyStoreItem>
        this.totalCost = calculateTotalCost();
    }

    private double calculateorderCost() {
        double orderCost = 0 ;
        for (MyStoreItem storeItem:this.quantityMap.keySet() ) {
            orderCost+= storeItem.getPrice() * this.quantityMap.get(storeItem);
        }
        return orderCost;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public MyCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(MyCustomer customer) {
        this.customer = customer;
    }

    public String getOrderKind() {
        return orderKind;
    }

    public void setOrderKind(String orderKind) {
        this.orderKind = orderKind;
    }

    public Map<MyStoreItem, Double> getQuantityMap() {
        return quantityMap;
    }

    public void setQuantityMap(Map<MyStoreItem, Double> quantityMap) {
        this.quantityMap = quantityMap;
    }

    public double getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(double orderCost) {
        this.orderCost = orderCost;
        this.totalCost = calculateTotalCost();
    }

    public Map<Integer, Double> getDeliveryCostMap() {
        return deliveryCostMap;
    }

    public void setDeliveryCostMap(Map<Integer, Double> deliveryCostMap) {
        this.deliveryCostMap = deliveryCostMap;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Map<Integer, MyStoreSingleOrderItems> getStoreSingleOrderItemsMap() {
        return storeSingleOrderItemsMap;
    }

    public void setStoreSingleOrderItemsMap(Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap) {
        this.storeSingleOrderItemsMap = storeSingleOrderItemsMap;
    }

    private double calculateTotalCost() {
        double totalDeliveryCost = 0 ;
        for (Integer id:this.deliveryCostMap.keySet()) {
            totalDeliveryCost += this.deliveryCostMap.get(id);
        }
        this.setDeliveryCost(totalDeliveryCost);
        return totalDeliveryCost + this.orderCost;
    }


    public void addDiscountSingleIemToOrder(MySuperMarket superMarket, SDMOffer sdmOffer, int storeId) {
        MyItem item = superMarket.getItems().getItemsMap().get(sdmOffer.getItemId());
        int price = sdmOffer.getForAdditional();
        double quatntity = sdmOffer.getQuantity();
        MyStoreItem storeItem = new MyStoreItem(item,price,storeId,"offer");
        this.getQuantityMap().put(storeItem,quatntity);

        //update costs of order.
        this.setOrderCost(this.getOrderCost() + storeItem.getPrice());
    }

    public void addDiscountsItemsdToOrder(MySuperMarket superMarket, ThenYouGet thenYouGet, int storeId) {
        List<SDMOffer> sdmOffers = thenYouGet.getSDMOffer();
        for (SDMOffer offer:sdmOffers) {
            addDiscountSingleIemToOrder(superMarket, offer ,storeId);
        }
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void updateOrderCost() {
        this.setOrderCost(calculateorderCost());
    }

    public MyLocation getFromWhereOrderWasMade() {
        return fromWhereOrderWasMade;
    }

    public void setFromWhereOrderWasMade(MyLocation fromWhereOrderWasMade) {
        this.fromWhereOrderWasMade = fromWhereOrderWasMade;
        for(MyStoreSingleOrderItems singleOrderItems : this.getStoreSingleOrderItemsMap().values()){
            singleOrderItems.setFromWhereOrderWasMade(fromWhereOrderWasMade);
        }
    }

    @Override
    public String toString() {
        return
                orderId + " made by: "
                + customer.getUserName();
    }

    public String getDateAsString() {
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        return simpleDateFormat.format(this.getDate());

    }
}
