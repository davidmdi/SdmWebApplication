package webCode.servlets;

import logic.Logic.My_CLASS.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoadMoneyFormServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        User currUser = SessionUtils.getUser(request, getServletContext());

        try (PrintWriter out = response.getWriter()) {
            if (currUser.getType().equalsIgnoreCase(User.CUSTOMER)) {
                out.println("<div id=\"loadingMoneyToAccount\">");
                    out.println("<h2>Load money</h2>");
                    out.println("<Input  type='number' id=\"moneyToLoad\" name=\"moneyToLoad\" placeholder='amount..' step='10'/>");
                    out.println("<Button onclick='ajaxLoadMoney()'>Load Money</Button>");
                out.println("</div>");
                out.flush();
            } else {
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
