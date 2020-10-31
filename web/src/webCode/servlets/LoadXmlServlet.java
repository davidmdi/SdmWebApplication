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
        User currUser = SessionUtils.getUser(request, getServletContext());
        try (PrintWriter out = response.getWriter()) {
            if (currUser.getType().equalsIgnoreCase(User.OWNER)) {


                out.println("<div id=\"loadXml\">");
                out.println("<h3>Load xml</h3>");
                out.println("<p>Please choose XML file to load</p>");
                //out.println("Load XML file:");
                out.println(createFormString());
                out.println("</div>");
                out.flush();
            } else {
                out.println("<div id=\"loadXml\"></div>");
            }
        }
    }

    private String createFormString(){
        StringBuilder formString = new StringBuilder();
        formString.append("<form id=\"uploadForm\" action=\"/uploadXmlFile\" enctype=\"multipart/form-data\" method=\"POST\">");
        formString.append("<input class=\"custom-file-input\" type=\"file\" name=\"xmlFile\" accept=\".xml\"><br>");
        //formString.append("<input type=\"file\" name=\"xmlFile\" accept=\".xml\"><br>");
        formString.append("<input class='xml-button' type=\"Submit\" value=\"Upload File\"><br>");
        formString.append("<div id=errorMsg></div>");
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
