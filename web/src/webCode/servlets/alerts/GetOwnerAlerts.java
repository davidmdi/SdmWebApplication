package webCode.servlets.alerts;

import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.List;

public class GetOwnerAlerts extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");

        try(PrintWriter out = response.getWriter()) {
            //response.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            User currUser = SessionUtils.getUser(request, getServletContext());
            http://localhost:8080/webSdm
            if(currUser.getType().equalsIgnoreCase(User.OWNER)) {

                String ownerName = currUser.getName();
                List<String> ownerAlertsToString = new ArrayList<>();//engine.getOwnerAlertsToString(ownerName);
                //List<String> ownerAlertsToString = engine.getMySupermarkets().getOwnerAlertsToString(ownerName);

                Gson gson = new Gson();
                String alertsList = gson.toJson(ownerAlertsToString);
                System.out.println(alertsList);

                out.println(alertsList);
            }//else{
             //   out.println("{}");
            //}
            out.flush();
        }
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
