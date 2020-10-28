package logic.Logic.My_CLASS;

public class FeedbackAlert implements Alertable{

    private String storeName;
    private int rate;
    private String comments;

    public FeedbackAlert(String storeName, int rate, String comments) {
        this.storeName = storeName;
        this.rate = rate;
        this.comments = comments;
    }

    @Override
    public String alert() {
        return String.format("Your store %s got new Feedback:\n"+
                "Rate: %d\n" +
                "Comments: %s", storeName, rate, comments);
    }

    public String getStoreName() {
        return storeName;
    }

    public int getRate() {
        return rate;
    }

    public String getComments() {
        return comments;
    }
}
