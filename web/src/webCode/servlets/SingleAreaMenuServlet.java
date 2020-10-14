package webCode.servlets;

import logic.Logic.My_CLASS.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SingleAreaMenuServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        User currUser = SessionUtils.getUser(request, getServletContext());

        try (PrintWriter out = response.getWriter()) {
            if (currUser.getType().equalsIgnoreCase(User.OWNER)) {

                String ownerMenu = createOwnerMenuString();
                out.println(ownerMenu);

            } else { //User type is Customer
                String CustomerMenu = createCustomerMenuString();
                out.println(CustomerMenu);
            }

            out.flush();
        }
    }

    //Customer menu
    private String createCustomerMenuString(){
        StringBuilder customerMenuString = new StringBuilder();
        customerMenuString.append("<div id=\"menu\" class=\"topnav\">");
        customerMenuString.append("<a href=\"\" class=\"active\">Home</a>");
        customerMenuString.append("<a href=\"\">Shop</a>");
        customerMenuString.append("<a href=\"#news\">Rate</a>");
        customerMenuString.append("<a href=\"#contact\">Orders history</a>");
        customerMenuString.append("</div>");

        return  customerMenuString.toString();
    }

    //Owner menu
    private String createOwnerMenuString(){
        StringBuilder ownerMenuString = new StringBuilder();
        ownerMenuString.append("<div id=\"menu\" class=\"topnav\">");
        ownerMenuString.append("<a href=\"\" id='homePage' class=\"active\">Home</a>");
        ownerMenuString.append("<a href='#' id='storeOrders' onclick=\"return storeOrdersClicked()\">Store orders</a>");
        ownerMenuString.append("<a href='#' id='feedbacks' onclick=\"return feedbacksClicked()\">Feedbacks</a>");
        ownerMenuString.append("<a href='#' id='openStore' onclick=\"return openStoreClicked()\">Open store</a>");
        ownerMenuString.append("</div>");

        return  ownerMenuString.toString();
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
