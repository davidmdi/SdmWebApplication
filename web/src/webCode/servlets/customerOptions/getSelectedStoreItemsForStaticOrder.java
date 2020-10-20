package webCode.servlets.customerOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class getSelectedStoreItemsForStaticOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            String dateAsString = req.getParameter("dateFromUser");
            String orderType = req.getParameter("typeofOrder");
            int xCord = Integer.parseInt(req.getParameter("xCord"));
            int yCord = Integer.parseInt(req.getParameter("yCord"));
            String storeName = req.getParameter("storeSelection");
            out.println("<div id=\"content\">" +
                    "<h1>" + storeName + "</h1></div>");
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            String data = req.getParameter("selectedStore");
            out.println("<div id=\"content\">" +
                    "<h1>" + data + "</h1></div>");
        }
    }
}
