package logic.Logic.My_CLASS;

public class FeedbackAlert implements Alertable{

    private String storeName;
    private int rate;
    private String comments;


    @Override
    public String alert() {
        return String.format("Your store %s got new Feedback:\n"+
                "Rate: %d\n" +
                "Comments: %s", storeName, rate, comments);
    }
}
