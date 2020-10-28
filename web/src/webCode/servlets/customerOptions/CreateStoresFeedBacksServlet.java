package webCode.servlets.customerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.FeedbackAlert;
import logic.Logic.My_CLASS.MyFeedback;
import logic.Logic.My_CLASS.MyStore;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.List;

public class CreateStoresFeedBacksServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
            Request: FeedbackList = [{Feedback}, {Feedback},....]

            function FeedbackList(feedbacksList){
                this.feedbacksList = feedbacksList;
            }

            function Feedback(storeName, orderDate, rate, comments){
                    this.storeName = storeName;
                    this.orderDate = orderDate; // = String od date value!
                    this.rate = rate;
                    this.comments = comments;
            }

         */
        response.setContentType("text/html;charset=UTF-8");
        Engine engine = ServletUtils.getEngine(getServletContext());
        String zoneName =  SessionUtils.getAreaName(request);
        String customerName =  SessionUtils.getUsername(request);


        try(PrintWriter out = response.getWriter()) {
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            FeedBackList feedbackList = gson.fromJson(reader, FeedBackList.class);
            System.out.println(feedbackList);
            try {
                for (MyFeedback.FeedbackJson feedbackJson : feedbackList.feedbacksList) {
                    engine.addFeedBack(zoneName, customerName, feedbackJson);
                    addFeedBackAlert(engine, feedbackJson);
                }

                out.println("Feedback accepted");
                out.flush();
            }catch (ParseException e){
                out.println("Wrong date format");
                out.flush();
            }
        }

    }

    private void addFeedBackAlert(Engine engine, MyFeedback.FeedbackJson feedbackJson){
        FeedbackAlert feedbackAlert = new FeedbackAlert(feedbackJson.storeName,
                feedbackJson.rate, feedbackJson.comments);
        System.out.println(feedbackAlert);

        //insert to owner alerts
        engine.getMySupermarkets().addFeedBackAlert(feedbackAlert);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public class FeedBackList{
        private List<MyFeedback.FeedbackJson> feedbacksList;

        public FeedBackList(List<MyFeedback.FeedbackJson> feedbacksList) {
            this.feedbacksList = feedbacksList;
        }

        @Override
        public String toString() {
            return "FeedBackList{" +
                    "feedbacksList=" + feedbacksList +
                    '}';
        }
    }
}
