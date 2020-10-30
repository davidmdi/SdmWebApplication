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


        try(PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            User currUser = SessionUtils.getUser(request, getServletContext());

           if(currUser.getType().equalsIgnoreCase(User.OWNER)) {

                String userName = SessionUtils.getUsername(request);
                //List<String> ownerAlertsToString = engine.getOwnerAlertsToString(userName);
                String ownerAlertsToString = engine.getOwnerAlertsToString(userName);
               String alertsList = "";
                if(!ownerAlertsToString.equalsIgnoreCase(""))
                    alertsList = buildAlertsHTML(ownerAlertsToString);

                out.println(alertsList);
           }
           else{
               out.println("");
           }
            out.flush();
        }
    }

    private String buildAlertsHTML(String ownerAlert){
        String res = "<div class='alert'>" +
                        "<label style=\"user-select: none;\">"+ownerAlert+"</label>" +
                        "<button name='okButton' class='okButton' type='submit'>OK</button>" +
                     "</div>";

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