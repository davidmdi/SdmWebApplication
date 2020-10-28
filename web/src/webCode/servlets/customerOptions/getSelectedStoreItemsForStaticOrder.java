package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
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

public class getSelectedStoreItemsForStaticOrder extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            // bring al items from selected storeJs
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);
            MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);
            String selectedStore = req.getParameter("storeSelection");
            MyStore.StoreJson storeJs = engine.getStoreJson(zoneName ,selectedStore );
            MyStore myStore = superMarket.getStores().findStoreByName(storeJs.name);
            //List<MyStoreItem.StoreItemJson> storeItemJsons = engine.getStoreItemsJson(zoneName ,selectedStore );
            out.println(createHtmlFormForSelectingItemsFromStaticOrder(storeJs , myStore));
            out.flush();
        }

    }
/*
Item: id name purchase method price amount
 */
    private String createHtmlFormForSelectingItemsFromStaticOrder(MyStore.StoreJson storeJs, MyStore myStore) {
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
                                            "<label id='storeNameLabel' >"+storeJs.name+"</label >" +
                                        "</div>"+
                                    "</div>"+
                                    "<div class=\"row\">"+
                                        "<div class=\"col-25\">"+
                                            "<label for=\"fname\">Store PPK</label>"+
                                        "</div>" +
                                        "<div class=\"col-75\">"+
                                            "<label >"+storeJs.ppk+"</label >" +
                                        "</div>"+
                                    "</div>"+
                                    "<div class=\"row\">"+
                                       "<div class=\"col-25\">"+
                                            "<label for=\"lname\">Store locaton</label>"+
                                        "</div>"+
                                        "<div class=\"col-75\">"+
                                            "<label>("+storeJs.x+","+storeJs.y+")</label>" +
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
                        for(MyStoreItem.StoreItemJson item : storeJs.storeItems) {
                            System.out.println(item.jsonItem.id);
                            System.out.println(myStore.getStoreItems().getItemsMap().get(item.jsonItem.id).getName());
                            System.out.println(myStore.getStoreItems().getItemsMap().get(item.jsonItem.id).getPrice());
                            int itemPrice = myStore.getStoreItems().getItemsMap().get(item.jsonItem.id).getPrice();
                           res += "<div class=\"item\" name='item'>" +
                                    "<input type = \"checkbox\" name='itemCheckBox' value='"+item.jsonItem.id+"' class=\"regular-checkbox\">" +
                                    "<label >"+item.jsonItem.id+"</label >" +
                                    "<label >"+ item.jsonItem.name+"</label >" +
                                    "<label > "+item.jsonItem.purchaseMethod+"</label >" +
                                    "<label > Price:"+itemPrice+"</label >";
                                   if(item.jsonItem.purchaseMethod.equalsIgnoreCase(MyItem.QUANTITY)){
                                      res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0' min = '0' value ='0'>";
                                   }else{
                                       res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0.0' inputmode = \"decimal\" min = \"0.0\" step = \"0.1\" value = \"0.0\" >";
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
/*
        <div id=\"content\">
                        <div class='row'>
                            <div class ='col'>
                                <div class='row'>
                                    <h3>Make static order</h3>
                                </div>
                                <form id ='createStaticOrder' method='POST' action=''>
                                    <div class=\"row\">
                                        <div class=\"col-25\">
                                            <label for=\"fname\">Store Name</label>
                                        </div>
                                        <div class=\"col-75\">
                                            <label id='storeNameLabel' >"+store.name+"</label >
                                        </div>
                                    </div>
                                    <div class=\"row\">
                                        <div class=\"col-25\">
                                            <label for=\"fname\">Store PPK</label>
                                        </div>
                                        <div class=\"col-75\">
                                            <label >"+store.ppk+"</label >
                                        </div>
                                    </div>
                                    <div class=\"row\">
                                       <div class=\"col-25\">
                                            <label for=\"lname\">Store locaton</label>
                                        </div>
                                        <div class=\"col-75\">
                                            <label>("+store.x+","+store.y+")</label>
                                        </div>
                                    </div>
                                    <div class=\"row\">
                                       <div class=\"col-25\">
                                            <label for=\"lname\">Delivery price</label>
                                        </div>
                                        <div class=\"col-75\">
                                            <label >"+deliveryPrice+"</label >
                                        </div>
                                    </div>
                                <div class=\"row\">
                           <div class=\"item\" name='item'>
                                    <input type = \"checkbox\" name='itemCheckBox' value='id' class=\"regular-checkbox\">
                                    <label >id</label >
                                    <label >name</label >
                                    <label >purchaseMethod</label >
                                    <label >price</label >
                                    <label >amount</label>
                                    <input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0' min = '0' value ='0'>
                            </div>
                           <div class=\"item\" name='item'>
                                    <input type = \"checkbox\" name='itemCheckBox' value='id' class=\"regular-checkbox\">
                                    <label >id</label >
                                    <label >name</label >
                                    <label >purchaseMethod</label >
                                    <label >price</label >
                                    <label >amount</label>
                                    <input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0' min = '0' value ='0'>
                            </div>
                           <div class=\"item\" name='item'>
                                    <input type = \"checkbox\" name='itemCheckBox' value='id' class=\"regular-checkbox\">
                                    <label >id</label >
                                    <label >name</label >
                                    <label >purchaseMethod</label >
                                    <label >price</label >
                                    <label >amount</label>
                                    <input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0.0' inputmode = \"decimal\" min = \"0.0\" step = \"0.01\" value = \"0.0\" >";
                           </div>
                        </div>
                            <div class=\"row\">
                    <input type=\"submit\" value='Make order'>
                </div>
            </form>
        </div>
   </div>
</div>
 */