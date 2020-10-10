package webCode.servlets;

import logic.Logic.My_CLASS.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

                /*
                            <div id="loadXml">
                loadXml:
                <button id="openFile">Open file</button>
                <button id="loadFile">Load file</button>
                <label id="fileName">File name:</label>
                <label id="errorMsg">File name:</label>
            </div>
                 */
                out.println("<div id=\"loadXml\">Hi  " + currUser.getName() + "</div>");
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
