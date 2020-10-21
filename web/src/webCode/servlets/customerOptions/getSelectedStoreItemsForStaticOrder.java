package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import logic.Logic.My_CLASS.MyStore;
import logic.Logic.My_CLASS.MyStoreItem;
import utils.ServletUtils;
import utils.SessionUtils;

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
            // bring al items from selected store
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);
            String selectedStore = req.getParameter("storeSelection");
            MyStore.StoreJson store = engine.getStoreJson(zoneName ,selectedStore );
            //List<MyStoreItem.StoreItemJson> storeItemJsons = engine.getStoreItemsJson(zoneName ,selectedStore );
            out.println(createHtmlFormForSelectingItemsFromStaticOrder(store));
            out.flush();
        }

    }
/*
Item: id name purchase method price amount
 */
    private String createHtmlFormForSelectingItemsFromStaticOrder(MyStore.StoreJson store) {
            int deliveryPrice = 0;
        String res ="<div id=\"content\">"+
                        "<div class='row'>"+
                            "<div class ='col'>"+
                                "<div class='row'>"+
                                    "<h3>Make static order</h3>"+
                                "</div>"+
                                "<form id ='createStaticOrder' method='POST' action=''>"+
                                    "<div class=\"row\">"+
                                        "<div class=\"col-25\">"+
                                            "<label for=\"fname\">Store Name</label>"+
                                        "</div>"+
                                        "<div class=\"col-75\">"+
                                            "<label id='storeNameLabel' >"+store.name+"</label >" +
                                        "</div>"+
                                    "</div>"+
                                    "<div class=\"row\">"+
                                        "<div class=\"col-25\">"+
                                            "<label for=\"fname\">Store PPK</label>"+
                                        "</div>" +
                                        "<div class=\"col-75\">"+
                                            "<label >"+store.ppk+"</label >" +
                                        "</div>"+
                                    "</div>"+
                                    "<div class=\"row\">"+
                                       "<div class=\"col-25\">"+
                                            "<label for=\"lname\">Store locaton</label>"+
                                        "</div>"+
                                        "<div class=\"col-75\">"+
                                            "<label>("+store.x+","+store.y+")</label>" +
                                        "</div>"+
                                    "</div>"+
                                    "<div class=\"row\">"+
                                       "<div class=\"col-25\">"+
                                            "<label for=\"lname\">Delivery price</label>"+
                                        "</div>"+
                                        "<div class=\"col-75\">"+
                                            "<label >"+deliveryPrice+"</label >" +
                                        "</div>" +
                                    "</div>"+
                                "<div class=\"row\">";
                        for(MyStoreItem.StoreItemJson item : store.storeItems) {
                           res += "<div class=\"item\" name='item'>" +
                                    "<input type = \"checkbox\" name='itemCheckBox' value='"+item.jsonItem.id+"' class=\"regular-checkbox\">" +
                                    "<label >"+item.jsonItem.id+"</label >" +
                                    "<label >"+ item.jsonItem.name+"</label >" +
                                    "<label > "+item.jsonItem.purchaseMethod+"</label >" +
                                    "<label > Price:"+item.price+"</label >" +
                                    "<label > Price:"+item.price+"</label >";
                                   if(item.jsonItem.purchaseMethod.equalsIgnoreCase(MyItem.QUANTITY)){
                                      res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0' min = '0' value ='0'>";
                                   }else{
                                       res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0.0' inputmode = \"decimal\" min = \"0.0\" step = \"0.01\" value = \"0.0\" >";
                                   }
                            res += "</div>";
                        }

                    res+= "</div>" +
                            "<div class=\"row\">"+
                    "<input type=\"submit\" value='Make order'>"+
                "</div>"+
            "</form>"+
        "</div>"+
   "</div>"+
"</div>";

            return res;
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
