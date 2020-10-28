package webCode.servlets.storeOwnerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyOrder;
import logic.Logic.My_CLASS.MyStore;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

//@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class StoreOrdersInfoTable extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException{

        try(PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName =  SessionUtils.getAreaName(request);
            String storeName = request.getParameter("storeName");
            System.out.println("storeName= "+storeName);
            MyStore store = engine.findZoneStoreByStoreName(zoneName, storeName);
            String orders = buildStoreOrdersTable(store);
            //PrintWriter out = response.getWriter();
            out.println(orders.toString());
            System.out.println(orders);
            out.flush();
        }
    }

    private String buildStoreOrdersTable(MyStore store) {
        String res = "<tbody>";

        if(store.getStoreOrderMap().values().size() == 0 ){
            //add empty row
            res+= "<tr>" +
                    "<td>There are no orders.</td>" +
                    "<td></td><td></td><td></td><td></td><td><td>" +
                    "</tr>";
        }else{
            for(MyOrder order : store.getStoreOrderMap().values()){
                res+= "<tr>" +
                        "<td>"+order.getOrderId()+"</td>" +
                        "<td>"+order.getDate()+"</td>" +
                        "<td>"+ order.getCustomer().getUserName()+"</td>" +
                        "<td>"+ order.getCustomer().getLocation()+"</td>" +
                        "<td>"+order.getQuantityMap().size()+"</td>" +
                        "<td>"+order.getOrderCost()+"</td>" +
                        "<td>"+order.getDeliveryCost()+"</td>" +
                        "</tr>";
            }
        }

/*
        res+= "<tr>" +
                "<td>order.getOrderId()</td>" +
                "<td>order.getDate()</td>" +
                "<td>rder.getCustomer().getUserName()</td>" +
                "<td>order.getCustomer().getLocation()</td>" +
                "<td>order.getQuantityMap().size()</td>" +
                "<td>order.getOrderCost()</td>" +
                "<td>order.getDeliveryCost()</td>" +
                "</tr>";
*/

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
