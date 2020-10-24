package logic.Logic.My_CLASS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyFeedback {
    private String storeName;
    private String customerName;
    private Date orderDate;
    private int rate;
    private String comments;

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public MyFeedback(String storeName, String customerName, Date orderDate, int rate, String comments){
        this.storeName = storeName;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.rate = rate;
        this.comments = comments;
    }

    public MyFeedback(String customerName, FeedbackJson feedbackJson) throws ParseException {
        this.storeName = feedbackJson.storeName;
        this.customerName = customerName;
        this.orderDate = formatter.parse(feedbackJson.orderDate);
        this.rate = feedbackJson.rate;
        this.comments = feedbackJson.comments;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public int getRate() {
        return rate;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "customerName:'" + customerName + '\'' +
                ", orderDate:" + orderDate +
                ", rate:" + rate +
                ", comments:'" + comments + '\'' +
                '}';
    }


    public static class FeedbackJson {
        public String storeName;
        public String orderDate;
        public int rate;
        public String comments;

        public FeedbackJson(String storeName, String orderDate, int rate, String comments) {
            this.storeName = storeName;
            this.orderDate = orderDate;
            this.rate = rate;
            this.comments = comments;
        }

        @Override
        public String toString() {
            return "FeedbackJson{" +
                    "storeName='" + storeName + '\'' +
                    ", orderDate='" + orderDate + '\'' +
                    ", rate=" + rate +
                    ", comments='" + comments + '\'' +
                    '}';
        }

    }
}
