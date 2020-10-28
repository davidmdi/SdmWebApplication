package webCode.servlets.storeOwnerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import logic.Logic.My_CLASS.MyStoreSingleOrderItems;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

            out.println(orders.toString());
            out.flush();
        }
    }

    private String buildStoreOrdersTable(MyStore store) {
        int singleOrderIndex = 0;
        String res = "<tbody>";

        if(store.getStoreSingleOrderItemsList().size() == 0 ){ //add empty row
            res+= "<tr storeName='' singleOrderIndex='' orderId=''>" +
                    "<td>There are no orders.</td>" +
                    "<td></td><td></td><td></td><td></td><td><td>" +
                    "</tr>";
        }else{
            for(MyStoreSingleOrderItems singleOrder : store.getStoreSingleOrderItemsList()){
                res+= "<tr storeName='"+store.getName()+"' singleOrderIndex='"+(singleOrderIndex++)+"' orderId='"+singleOrder.getOrderId()+"'>" +
                        "<td>"+singleOrder.getOrderId()+"</td>" +
                        "<td>"+singleOrder.getDate()+"</td>" +
                        "<td>"+singleOrder.getFromWhereOrderWasMade()+"</td>" +
                        "<td>"+singleOrder.getCustomer().getLocation()+"</td>" +
                        "<td>"+singleOrder.getThisStoreQuantityMapFromOrderMapSize()+"</td>" +
                        "<td>"+String.format("%.2f",singleOrder.getOrderCost())+"</td>" +
                        "<td>"+String.format("%.2f",singleOrder.getDeliveryCost())+"</td>" +
                        "</tr>";
            }
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

/*

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