package webCode.servlets.storeOwnerOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StoreFeedbacksServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            /* IN JS: UPDATE STORES & DATA SAME AS STORE ORDERS */

            out.println("<div id='content' class='content'>");
            out.println("<div class=\"row\">");
            out.println("<div>");
            out.println("<h3>Stores you own</h3><p>Choose store:</p>");
            out.println("<ul id=\"storesList\" class=\"store-list\"></ul>");
            out.println("<form id=\"selectStore\" method=\"POST\" action=\"showStoreOrders\">");
            out.println("<input id=\"selected_store\" type='hidden'/>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class=\"col\">");
            //storeFeedbacks:
            out.println("<div id=\"storeFeedbacks\">");
            out.println("<h3>Store Feedbacks</h3><p>Feedbacks of your selected store:<p>");
            out.println("<table id=\"storeFeedbacksTable\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Customer name</th><th>Order date</th><th>Rate (1-5)</th><th>Comments</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody></tbody>");
            out.println("</table>");
            out.println("</div>");
            out.println("</div>");//end 'col'
            out.println("</div>");//end 'row'
            out.println("</div>");
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
