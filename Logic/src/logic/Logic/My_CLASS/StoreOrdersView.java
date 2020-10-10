package logic.Logic.My_CLASS;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StoreOrdersView {
    private int orderId;
    private String orderKind;
    private String date;
    private int howManyItemsSold;
    private String deliveryPrice;
    private String totalPrice;

    public StoreOrdersView(MyStoreSingleOrderItems singleOrderItems) {
        this.orderId = singleOrderItems.getOrderId();
        this.orderKind = singleOrderItems.getOrderKind();
        this.date = convertDateToString(singleOrderItems.getDate());
        this.howManyItemsSold = singleOrderItems.getThisStoreQuantityMapFromOrderMapSize();
        this.deliveryPrice = String.format("%.2f",singleOrderItems.getDeliveryCost());
        this.totalPrice = String.format("%.2f",singleOrderItems.calculatePrice());
    }

    private String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderKind() {
        return orderKind;
    }

    public String getDate() {
        return date;
    }

    public int getHowManyItemsSold() {
        return howManyItemsSold;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }
}
