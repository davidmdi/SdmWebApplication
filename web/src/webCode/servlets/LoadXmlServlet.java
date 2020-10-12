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

                out.println("<div id=\"loadXml\">Hi  " + currUser.getName());
                out.println(createFormString());
                out.println("</div>");
                out.flush();
            } else {
                System.out.println("Customer");
                out.println("<div id=\"loadXml\"></div>");
            }
        }
    }

    private String createFormString(){
        StringBuilder formString = new StringBuilder();
        formString.append("<form id=\"uploadForm\" action=\"/uploadXmlFile\" enctype=\"multipart/form-data\" method=\"POST\">");
        formString.append("<input type=\"file\" name=\"xmlFile\" accept=\".xml\"><br>");
        formString.append("<input type=\"Submit\" value=\"Upload File\"><br>");
        formString.append("</form>");

    return  formString.toString();
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
