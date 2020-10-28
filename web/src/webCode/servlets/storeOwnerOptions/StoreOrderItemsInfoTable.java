package webCode.servlets.storeOwnerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import logic.Logic.My_CLASS.MyStoreItem;
import logic.Logic.My_CLASS.MyStoreSingleOrderItems;
import utils.ServletUtils;
import utils.SessionUtils;
import webCode.servlets.customerOptions.StaticOrderSummeryServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StoreOrderItemsInfoTable extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        try(PrintWriter out = response.getWriter()) {
            response.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName =  SessionUtils.getAreaName(request);
            String storeName = request.getParameter("storeName");
            String singleOrderIndex = request.getParameter("singleOrderIndex");
            System.out.println("storeName= "+storeName);
            System.out.println("singleOrderIndex= "+singleOrderIndex);
            MyStore store = engine.findZoneStoreByStoreName(zoneName, storeName);
            String orderItems = buildStoreOrderItemsTable(store, Integer.valueOf(singleOrderIndex));

            out.println(orderItems.toString());
            System.out.println(orderItems);
            out.flush();
        }
    }

    private String buildStoreOrderItemsTable(MyStore store, int singleOrderIndex) {

        MyStoreSingleOrderItems singleOrderItems = store.getStoreSingleOrderItemsList().get(singleOrderIndex);
        double amount;
        boolean isBoughtOnSell;
        String res = "<tbody>";

        for(MyStoreItem storeItem :  singleOrderItems.getThisStoreQuantityMapFromOrder().keySet()){
            amount = singleOrderItems.getThisStoreQuantityMapFromOrder().get(storeItem);
            isBoughtOnSell = storeItem.getItemKind().equalsIgnoreCase("store")? false : true;

            res+= "<tr>" +
                    "<td>"+storeItem.getMyItem().getItemId()+"</td>" +
                    "<td>"+storeItem.getMyItem().getName()+"</td>" +
                    "<td>"+storeItem.getMyItem().getPurchaseCategory()+"</td>" +
                    "<td>"+String.format("%.2f", amount)+"</td>" +
                    "<td>"+storeItem.getPrice()+"</td>" +
                    "<td>"+String.format("%.2f",storeItem.getPrice()*amount)+"</td>" + // = Total cost
                    "<td>"+isBoughtOnSell+"</td>" +
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
