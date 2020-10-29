package webCode.servlets.alerts;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class CreateNewAlertServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter()) {

            Engine engine = ServletUtils.getEngine(getServletContext());
            User currUser = SessionUtils.getUser(request, getServletContext());

            if(currUser.getType().equalsIgnoreCase(User.OWNER)) {

                String ownerName = currUser.getName();
                List<String> ownerAlertsToString = engine.getMySupermarkets().getOwnerAlertsToString(ownerName);

                String alertsList = buildAlertsHTML(ownerAlertsToString);
                System.out.println(alertsList.toString());

                out.println(alertsList);
            }
            out.flush();
        }
    }

    private String buildAlertsHTML(List<String> ownerAlerts){
        String res = "";

        for(String alert : ownerAlerts){
            res += "<div class='alert'>" +
                        "<button type='submit' onclick='removeAlert()'>Close alert</button>" +
                        "<label>"+alert+"</label>" +
                    "</div>";
        }

        return res;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

}
/*
    <div id='userAlerts' class='alertsContainer'>
        <form id='aler1tId' class='alert'>
            <input type="submit" value='Close alert'>
            <label>Alert1 Info</label>
        </form>
        <form id='alert2Id' class='alert'>
            <input type="submit" value='Close alert'>
            <label>Alert2 Info</label>
        </form>
        <form id='alert3Id' class='alert'>
            <input type="submit" value='Close alert'>
            <label>Alert3 Info</label>
        </form>
    </div>
 */