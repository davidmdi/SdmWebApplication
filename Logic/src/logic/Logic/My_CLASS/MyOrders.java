package logic.Logic.My_CLASS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrders {
    private List<MyOrder> orderList;
    private Map<Integer,MyOrder> orderMap;
    private double avgOrdersPrice; //without delivery price !
    private double sumOfOrdersPrice; //without delivery price !


    public MyOrders() {
        orderList = new ArrayList<>();
        orderMap = new HashMap<>();
        this.avgOrdersPrice = 0;
        this.sumOfOrdersPrice = 0;
    }

    public List<MyOrder> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<MyOrder> orderList) {
        this.orderList = orderList;
    }

    public Map<Integer, MyOrder> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<Integer, MyOrder> orderMap) {
        this.orderMap = orderMap;
    }

    public void addOrder(MyOrder order){
        orderList.add(order);
        orderMap.put(order.getOrderId(),order);

        this.sumOfOrdersPrice += order.getOrderCost();
        this.avgOrdersPrice = order.getOrderCost() / this.orderList.size();
    }

    public double getAvgOrdersPrice() {
        return avgOrdersPrice;
    }


}
