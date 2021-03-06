package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.*;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ShowCustomersOrderItemsTableServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try (PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            User user = SessionUtils.getUser(request, getServletContext());
            MyCustomer customer = engine.getMyUsers().findCustomerByName(user.getName());
            String zoneName = SessionUtils.getAreaName(request);
            Map<Integer, MyStore> storeMap = engine.getMySupermarkets().
                    getAreaSuperMarketByName(zoneName).getStores().getStoreMap();

            int orderId = Integer.valueOf(request.getParameter("orderId"));
            System.out.println("orderId= " + orderId);
            MyOrder order = customer.getCustomerOrders().getOrderMap().get(orderId);

            String orderItemsTable = buildCustomerOrderItemsTable(order,storeMap);

            out.println(orderItemsTable.toString());
            System.out.println(orderItemsTable);
            out.flush();
        }
    }
    private String buildCustomerOrderItemsTable(MyOrder order, Map<Integer, MyStore> storeMap){

        String res = "<table id='orderItemsTable'>" +
                        "<thead>" +
                            "<tr>" +
                                "<th>Item Id</th>" +
                                "<th>Name</th>" +
                                "<th>Purchase method</th>" +
                                "<th>Store</th>" +
                                "<th>Purchase Amount</th>" +
                                "<th>Price</th>" +
                                "<th>Total Price</th>" +
                                "<th>Bought On Sale</th>" +
                            "</tr>" +
                        "</thead>" +
                        buildOrderItemsTBody(order,storeMap) +
                        "</table>";

        return res;
    }

    private String buildOrderItemsTBody(MyOrder order, Map<Integer, MyStore> storeMap) {
        String res = "<tbody>";
        MyStore store;
        double amount;
        boolean isBoughtOnSell;

        for(MyStoreItem storeItem : order.getQuantityMap().keySet()){
            amount = order.getQuantityMap().get(storeItem);
            isBoughtOnSell = storeItem.getItemKind().equalsIgnoreCase("store")? false : true;
            store = storeMap.get(storeItem.getStoreId());

            res += "<tr>" +
                    "<td>" + storeItem.getMyItem().getItemId() + "</td>" +
                    "<td>" + storeItem.getMyItem().getName() + "</td>" +
                    "<td>" + storeItem.getMyItem().getPurchaseCategory() + "</td>" +
                    "<td> Store Id: "+ store.getId()+", Name: "+store.getName()+"</td>" +
                    "<td>" + amount+ "</td>" +
                    "<td>" + storeItem.getPrice() + "</td>" +
                    "<td>" + String.format("%.2f", storeItem.getPrice() * amount) + "</td>" + // = Total cost
                    "<td>" + isBoughtOnSell + "</td>" +
                    "</tr>";
        }

        res += "</tbody>";

        return res;
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
