package webCode.servlets;

import com.google.gson.Gson;
import constants.Constants;
import logic.users.User;
import logic.users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static constants.Constants.USERKIND;
import static constants.Constants.USERNAME;

public class LoadXmlServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        //UserManager userManager = ServletUtils.getUserManager(getServletContext());
        User currUser = SessionUtils.getUser(request, getServletContext());
        try (PrintWriter out = response.getWriter()) {
            if (currUser.getType().equalsIgnoreCase(User.OWNER)) {
                System.out.println("Owner");

                //Gson gson = new Gson();
                //UserManager userManager = ServletUtils.getUserManager(getServletContext());
                //Set<User> usersList = userManager.getUsers();
                //String json = gson.toJson(usersList);

                out.println("<div id=\"loadXml\">Hi + " + currUser.getName() + "</div>");
                out.flush();
            } else {
                System.out.println("Customer");
                out.println("<div id=\"loadXml\"></div>");
            }
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
