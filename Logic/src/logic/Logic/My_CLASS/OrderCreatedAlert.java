package logic.Logic.My_CLASS;

public class OrderCreatedAlert implements Alertable{

    private int orderId;
    private String customerName;
    private int totalOrderItemsTypes;
    private double totalOrderItemsCost;
    private double totalOrderDeliveryCost;

    public OrderCreatedAlert(int orderId, String customerName, int totalOrderItemsTypes, int totalOrderItemsCost, int totalOrderDeliveryCost) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalOrderItemsTypes = totalOrderItemsTypes;
        this.totalOrderItemsCost = totalOrderItemsCost;
        this.totalOrderDeliveryCost = totalOrderDeliveryCost;
    }

    @Override
    public String alert() {
        return String.format("Order %s was created by %d.\n" +
                "Total items types: %s\n" +
                "Total items cost: %2.f\n" +
                "Total delivery cost: %2.f",orderId, customerName,
                totalOrderItemsTypes, totalOrderItemsCost, totalOrderDeliveryCost);
    }
}
