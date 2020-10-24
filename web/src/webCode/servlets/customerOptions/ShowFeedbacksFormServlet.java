package webCode.servlets.customerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStoreSingleOrderItems;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ShowFeedbacksFormServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /*
            DONT FORGET:
            -----------
            - delete order from session
            - delete MyStoreSingleOrderItems from session
         */
        Engine engine = ServletUtils.getEngine(getServletContext());
        String zoneName =  SessionUtils.getAreaName(request);
        Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap = null;  // = from session
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);

        //Map<StoreId, StoreName>
        Map<Integer, String> orderStoresInfo = superMarket.createStoresInfoMap(storeSingleOrderItemsMap.keySet());

        try (PrintWriter out = response.getWriter()) {
            out.println(buildFeedbacksDiv(orderStoresInfo));
        }
    }

    private String buildFeedbacksDiv(Map<Integer, String> orderStoresInfo){

        String div = "<div class=\"leaveFeedbacks\">" +
                        "<form id ='storesFeedBacks' method='POST' action='storesFeedBack'>" +
                            "<div class='row'>" +
                                "<h3>Feedbacks</h3>" +
                            "</div>";

        for(Integer storeId : orderStoresInfo.keySet()){
            div += buildFeedback(storeId, orderStoresInfo.get(storeId));
        }


        div += "<div class='row'>" +
                    "<input type=\"submit\" value='Send feedbacks'>" +
                "</div>" +
            "</form>" +
        "</div>";

        return div;
    }

    private String buildFeedback(int storeId, String storeName){
        String feedBack = "<div class='row'>" +
                            "<div class=\"feedback\" name='feedback'>" +
                                "<div class='col-15'>" +
                                    "<input type = \"checkbox\" name='feedbackCheckBox' value='"+storeId+"' class=\"regular-checkbox\">" +
                                    "<label name='storeNameFeedback' class='storeName-label' >"+storeName+"</label>" +
                                "</div>" +
                                "<div class='col-85'>" +
                                    "<div class='col-1'>" +
                                        "<label class='feedback-label'>Rate:</label>" +
                                        "<label class='feedback-label'>Comments:</label>" +
                                    "</div>" +
                                    "<div class='col-2'>" +
                                        "<input type = \"number\" name='feedbackRate' value='id' class=\"text-price\" placeholder ='1-5' min='1' max='5'>" +
                                        "<input type = \"text\" name='feedbackComment' class=\"text-text\">" +
                                    "</div>" +
                                "</div>" +
                            "</div>" +
                           "</div>";
        return feedBack;
    }

}
    /*
        <div class="leaveFeedbacks">
        <form id ='storesFeedBacks' method='POST' action=''>
            <div class='row'>
                <h3>Feedbacks</h3>
            </div>

            <div class='row'>
                <div class="feedback" name='feedback'>
                    <div class='col-15'>
                        <input type = "checkbox" name='feedbackCheckBox' value='id' class="regular-checkbox">
                        <label name='storeNameFeedback' class='storeName-label' >Rami levi</label>
                    </div>
                    <div class='col-85'>
                            <div class='col-1'>
                                <label class='feedback-label'>Rate:</label>
                                <label class='feedback-label'>Comments:</label>
                            </div>
                            <div class='col-2'>
                                <input type = "number" name='feedbackCheckBox' value='id' class="text-price" placeholder ='1-5' min='1' max='5'>
                                <input type = "text" name='feedbackCheckBox' class="text-text">
                            </div>
                    </div>
                </div>
            </div>

            <div class='row'>
                <div class="feedback" name='feedback'>
                    <div class='col-15'>
                        <input type = "checkbox" name='feedbackCheckBox' value='id' class="regular-checkbox">
                        <label name='storeNameFeedback' class='storeName-label' >Rami levi</label>
                    </div>
                    <div class='col-85'>
                            <div class='col-1'>
                                <label class='feedback-label'>Rate:</label>
                                <label class='feedback-label'>Comments:</label>
                            </div>
                            <div class='col-2'>
                                <input type = "number" name='feedbackCheckBox' value='id' class="text-price" placeholder ='1-5' min='1' max='5'>
                                <input type = "text" name='feedbackCheckBox' class="text-text">
                            </div>
                    </div>
                </div>
            </div>

        <div class='row'>
            <input type="submit" value='Send feedbacks'>
        </div>
        </form>
    </div>
     */
