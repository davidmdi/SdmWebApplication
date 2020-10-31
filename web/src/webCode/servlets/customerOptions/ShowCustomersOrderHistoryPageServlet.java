package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyCustomer;
import logic.Logic.My_CLASS.MyOrder;
import logic.Logic.My_CLASS.MyOrders;
import logic.Logic.My_CLASS.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ShowCustomersOrderHistoryPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        try(PrintWriter out = resp.getWriter()) {
            User user = SessionUtils.getUser(req, getServletContext());
            Engine engine = ServletUtils.getEngine(getServletContext());
            MyCustomer customer = engine.getMyUsers().findCustomerByName(user.getName());
            if (customer == null || customer.getCustomerOrders() == null)
                out.println("<div id=\"content\">" + "<h4>No orders made yet.</h4>" + "</div>");
            else
                out.println(buildOrderHistoryTable(engine,customer));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildOrderHistoryTable(Engine engine, MyCustomer customer) {
        String res="<div id=\"content\">" +
                    "<div class='row'>" +
                        "<div class ='col'>" +
                            "<div class='row' id='customerOrdersHistory'>" +
                                "<div class='col'>" +
                                   "<h2>Orders History</h2><p>Your previous orders<p>" +
                                   "<table id=\"customerOrdersTable\">" +
                                   "<thead>" +
                                       "<tr>" +
                                           "<th>Order id</th>" +
                                           "<th>Date</th>" +
                                           "<th>Location</th>" +
                                           "<th>Total stores included</th>" +
                                           "<th>How many items</th>" +
                                           "<th>Items Cost</th>" +
                                           "<th>Delivery Cost</th>" +
                                           "<th>Total cost</th>" +
                                       "</tr>" +
                                    "</thead>" +
                                    "<tbody>";
                                  MyOrders orders = customer.getCustomerOrders();
                                  for (MyOrder order: orders.getOrderList()) {
                                     res += buildSingleOrderHtml(order);
                                  }
        res+=  "</tbody>" +
             "</table>" +
            "</div>" +
            "</div>" +
                "<div id='orderItems' class='row'><div class='col'><h3>Order Items</h3><table id='orderItemsTable'></table></div></div>" +
        "</div>" +
    "</div>" +
"</div>";

        return res;
    }

    private String buildSingleOrderHtml(MyOrder order) {
        return
                "<tr orderId='"+order.getOrderId()+"'>" +
                        "<td>"+order.getOrderId()+"</td>" +
                        "<td>"+order.getDate()+"</td>" +
                        "<td>"+order.getFromWhereOrderWasMade()+"</td>" +
                        "<td>"+order.getStoreSingleOrderItemsMap().size()+"</td>" +
                        "<td>"+order.getQuantityMap().size()+"</td>" +
                        "<td>"+order.getOrderCost() +"</td>"+
                        "<td>"+String.format("%.2f",order.getDeliveryCost()) +"</td>" +
                        "<td>"+String.format("%.2f",order.getTotalCost()) +"</td>" +
                "</tr>";
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }
}
