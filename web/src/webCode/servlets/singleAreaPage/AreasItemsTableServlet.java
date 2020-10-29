package webCode.servlets.singleAreaPage;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AreasItemsTableServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(request);
            MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);

            String areasTable = buildAreasItemsTableString(superMarket);
            out.println(areasTable.toString());
            out.flush();

        }
    }

        private String buildAreasItemsTableString(MySuperMarket superMarket){
            String res = "<table id='areasTable'>" +
                            "<thead>" +
                                "<tr>" +
                                    "<th>Id</th>" +
                                    "<th>Name</th>" +
                                    "<th>Purchase method</th>" +
                                    "<th>Sold by X stores</th>" +
                                    "<th>Average price</th>" +
                                    "<th>Purchases amount</th>" +
                                "</tr>" +
                            "</thead>" +
                            "<tbody>";

            for (MyItem item : superMarket.getItems().getItemList()) {
                res += "<tr name='item'>" +
                        "<td>"+item.getItemId()+"</td>" +
                        "<td>"+item.getName()+"</td>" +
                        "<td>"+item.getPurchaseCategory()+"</td>" +
                        "<td>"+item.getHowManyStoresSellsThisItem()+"</td>" +
                        "<td>"+String.format("%.2f",item.getAverageItemPrice())+"</td>" +
                        "<td>"+String.format("%.2f",item.getHowManyTimesItemSold())+"</td>" +
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