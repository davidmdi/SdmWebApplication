package logic.Logic.My_CLASS;

public class OrderCreatedAlert implements Alertable{

    private int orderId;
    private String customerName;
    private int totalOrderItemsTypes;
    private double totalOrderItemsCost;
    private double totalOrderDeliveryCost;

    public OrderCreatedAlert(int orderId, String customerName, int totalOrderItemsTypes, double totalOrderItemsCost, double totalOrderDeliveryCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalOrderItemsTypes = totalOrderItemsTypes;
        this.totalOrderItemsCost = totalOrderItemsCost;
        this.totalOrderDeliveryCost = totalOrderDeliveryCost;
    }

    @Override
    public String alert() {

        return String.format("Order %d was created by %s." +
                " Total items types: %s" +
                " Total items cost: %.2f " +
                " Total delivery cost: %.2f ",orderId, customerName,
                totalOrderItemsTypes, totalOrderItemsCost, totalOrderDeliveryCost);
    }
}
