package webCode.servlets.singleAreaPage;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import logic.Logic.My_CLASS.MyStoreItem;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AreasStoresTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(request);
            MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);

            String areasStoresTable = buildAreasStoresTableString(superMarket);
            out.println(areasStoresTable.toString());
            out.flush();
        }
    }

    private String buildAreasStoresTableString(MySuperMarket superMarket){
        String res = "<table id='areasTable'>" +
                        "<thead>" +
                            "<tr>" +
                                "<th>Id</th>" +
                                "<th>Name</th>" +
                                "<th>Owner's name</th>" +
                                "<th>Coordinates</th>" +
                                "<th>Items</th>" +
                                "<th>Total orders</th>" +
                                "<th>Total items cost</th>" +
                                "<th>PPK</th>" +
                                "<th>Total delivery cost</th>" +
                            "</tr>" +
                        "</thead>" +
                        "<tbody>";

        for (MyStore store : superMarket.getStores().getStoreList()) {
            res += "<tr name='item'>" +
                    "<td>"+store.getId()+"</td>" +
                    "<td>"+store.getName()+"</td>" +
                    "<td>"+store.getOwnerName()+"</td>" +
                    "<td>("+store.getMyLocation().getX()+","+store.getMyLocation().getY()+")</td>" +
                    "<td>"+buildStoreItemsRowInTable(store.getStoreItems().getItemsList())+"</td>" +
                    "<td>"+store.getStoreSingleOrderItemsList().size()+"</td>" +
                    "<td>"+store.getTotalOrdersItemsCost()+"</td>" +
                    "<td>"+store.getPPK()+"</td>" +
                    "<td>"+store.getTotalDeliveryCosts()+"</td>" +
                    "</tr>";
        }

        res += "</tbody>" +
                "</table>";

        return res;
    }

    private String buildStoreItemsRowInTable(List<MyStoreItem> storeItemsList){
        String res = "<table id='storeItemsTable'>" +
                        "<thead>" +
                            "<tr>" +
                                "<th>Id</th>" +
                                "<th>Name</th>" +
                                "<th>Purchase method</th>" +
                                "<th>Price</th>" +
                                "<th>Total purchases</th>" +
                            "</tr>" +
                        "</thead>" +
                        "<tbody>";

        for (MyStoreItem storeItem : storeItemsList) {
            res += "<tr name='item'>" +
                    "<td>"+storeItem.getMyItem().getItemId()+"</td>" +
                    "<td>"+storeItem.getMyItem().getName()+"</td>" +
                    "<td>"+storeItem.getMyItem().getPurchaseCategory()+"</td>" +
                    "<td>"+storeItem.getPrice()+"</td>" +
                    "<td>"+storeItem.getHowManyTimeSold()+"</td>" +
                    "</tr>";
        }

        res += "</tbody>" +
                "</table>";

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