package webCode.servlets;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class ShowAreasTableServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            Engine engine = ServletUtils.getEngine(getServletContext());
            Set<MySuperMarket> superMarketsSet = getSuperMarketsSet(engine);

            String areasTable = buildAreasTableString(superMarketsSet);
            out.println(areasTable.toString());
            out.flush();
        }
    }

    private String buildAreasTableString(Set<MySuperMarket> supermarketsSet){
        String res = "<table id='areasTable'>" +
                    "<thead>" +
                        "<tr>" +
                            "<th>Owner name</th>" +
                            "<th>Zone name</th>" +
                            "<th>Total products for sell</th>" +
                            "<th>Total stores in area</th>" +
                            "<th>Total orders</th>" +
                            "<th>Avg orders price</th>" +
                        "</tr>" +
                    "</thead>" +
                    "<tbody>";
        if(supermarketsSet.size() == 0){
            res += "<tr><td>There are no areas</td><td></td><td></td><td></td><td></td><td></td></tr>";
        } else {
            for (MySuperMarket superMarket : supermarketsSet) {
                res += "<tr name='area' selectedArea='" + superMarket.getZoneName() + "'>" +
                        "<td>" + superMarket.getOwner().getUserName() + "</td>" +
                        "<td>" + superMarket.getZoneName() + "</td>" +
                        "<td>" + superMarket.getItems().getItemList().size() + "</td>" +
                        "<td>" + superMarket.getStores().getStoreList().size() + "</td>" +
                        "<td>" + superMarket.getOrders().getOrderList().size() + "</td>" +
                        "<td>" + superMarket.getOrders().getAvgOrdersPrice() + "</td>" +
                        "</tr>";
            }
        }
        res += "</tbody>" +
            "</table>";

        return res;
    }

    private Set<MySuperMarket> getSuperMarketsSet(Engine engine) {
        Set<MySuperMarket> superMarketsForJson = new HashSet<>();

        for(Set<MySuperMarket> supermarket : engine.getMySupermarkets().getSuperMarkets().values()){
            superMarketsForJson.addAll(supermarket);
        }

        return superMarketsForJson;
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