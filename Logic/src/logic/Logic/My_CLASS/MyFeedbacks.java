package logic.Logic.My_CLASS;

import java.util.ArrayList;
import java.util.List;

public class MyFeedbacks {
    private List<MyFeedback> feedbacksList;

    public MyFeedbacks() {
        this.feedbacksList = new ArrayList<>();
    }

    public void addFeedback(MyFeedback feedback){
        this.feedbacksList.add(feedback);
    }

    public List<MyFeedback> getFeedbacksList() {
        return feedbacksList;
    }
}
