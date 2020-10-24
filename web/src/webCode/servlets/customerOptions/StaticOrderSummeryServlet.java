package webCode.servlets.customerOptions;

import com.google.gson.Gson;
import constants.Constants;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.*;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticOrderSummeryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException
            , IOException {
        try {
            processes(req, resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void processes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException {

        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        Order staticOrderFromJs =  gson.fromJson(reader, Order.class);
        System.out.println(staticOrderFromJs);

        try (PrintWriter out = resp.getWriter()) {
            out.println(buildOrderSemmeryForm(staticOrderFromJs,req));
        }

    }

    private String buildOrderSemmeryForm(Order staticOrderFromJs, HttpServletRequest req) throws ParseException {
        Engine engine = ServletUtils.getEngine(getServletContext());
        // Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap = new HashMap<>();

        //initialize data:
        Date date = buildDate(staticOrderFromJs.date);
        MyCustomer  customer = updateCustomerLocatin(engine,req , staticOrderFromJs.customerX ,
                staticOrderFromJs.customerY);
        String orderKind = staticOrderFromJs.type;
        Map<MyStoreItem,Double> quantityMap = createQuantityMap( engine,req,
          staticOrderFromJs.selectedStoreItemsList, staticOrderFromJs.selectedOfferItemsList);
        Map<Integer,Double> deliveryCostMap = createDeliveryCostMap(engine,req , customer ,quantityMap) ;

        //creating order :
        MyOrder order = new MyOrder(date,customer,quantityMap,orderKind,deliveryCostMap);

        //creating StoreSingleOrderMap
       Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap = createSingleOrderItemsMap(order,req , engine);

        //need to save the order and StoreSingleOrderMap on session..
        synchronized (this) {
            req.getSession(true).setAttribute(Constants.CUSTOMER_ORDER,order);
            req.getSession(true).setAttribute(Constants.CUSTOMER_STORE_SINGLE_ORDER_MAP,storeSingleOrderItemsMap);
        }

        return buildHtmlForm(order,storeSingleOrderItemsMap,engine,req);
    }

    private Date buildDate(String date) throws ParseException {
        Date date1=new SimpleDateFormat("YYYY-MM-dd").parse(date);
        return date1;
    }


    private Map<Integer, MyStoreSingleOrderItems> createSingleOrderItemsMap(MyOrder order
            , HttpServletRequest req, Engine engine) {
        Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap = new HashMap<>();
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);
        engine.createStoreSingleOrderInstance(order,superMarket,storeSingleOrderItemsMap);
        return storeSingleOrderItemsMap;
    }

    private Map<Integer, Double> createDeliveryCostMap(Engine engine, HttpServletRequest req
            , MyCustomer customer, Map<MyStoreItem, Double> quantityMap) {

        Map<Integer, Double> deliveryCostMap = new HashMap<>();
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);
        superMarket.calculateDeliveryCostMap(quantityMap,deliveryCostMap,customer);

        return deliveryCostMap;
    }

    private Map<MyStoreItem, Double> createQuantityMap(Engine engine, HttpServletRequest req,
                List<StoreItem> selectedStoreItemsList,List<OfferItem> selectedOfferItemsList) {
        Map<MyStoreItem, Double> quantityMap = new HashMap<>();
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);

        for (StoreItem storeItemJson : selectedStoreItemsList ){ // adding store items to map.
            MyStore store = superMarket.getStores().findStoreByName(storeItemJson.storeName); // founded by name
            MyStoreItem storeItemToAdd = store.getStoreItems().getItemsMap().get(storeItemJson.itemId);
            quantityMap.put(storeItemToAdd,storeItemJson.quantity);
        }
        if(selectedOfferItemsList.get(0).itemId != -1 ) { // if it is not a dummy offer object
            for (OfferItem offerItem : selectedOfferItemsList) { // adding offer items to map.
                MyStore store = superMarket.getStores().getStoreMap().get(offerItem.storeId);
                MyItem myItem = superMarket.getItems().getItemsMap().get(offerItem.itemId);
                MyStoreItem offerItemToAdd = new MyStoreItem(myItem, offerItem.price, offerItem.storeId, "offer");
                quantityMap.put(offerItemToAdd, offerItem.quantity);
            }
        }

        return quantityMap;
    }

    private MyCustomer updateCustomerLocatin(Engine engine, HttpServletRequest req, int customerX, int customerY) {
        String customerName = SessionUtils.getUsername(req);
        MyCustomer customer = engine.getMyUsers().findCustomerByName(customerName);
        MyLocation customerLocation = new MyLocation(customerX,customerY);
        customer.setLocation(customerLocation);
        return customer;
    }

    private String buildHtmlForm(MyOrder order, Map<Integer
            , MyStoreSingleOrderItems> storeSingleOrderItemsMap, Engine engine, HttpServletRequest req) {

        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);
        String res = "";

        res+="<div id=\"content\">" +
                "<div class='row'>" +
                "<div class ='col'>" +
                "<div class='row'>" +
                "<h3>Order summery</h3>" +
                "</div>";
        for(Integer storeId : storeSingleOrderItemsMap.keySet()){
            MyStore store = superMarket.getStores().getStoreMap().get(storeId);
            res+= "<div class='store'>" +
                    "<div class=\"row\">" +
                      "<div class=\"col-25\">" +
                         "<label for=\"fname\">Store Id</label>" +
                      "</div>" +
                      "<div class=\"col-75\">" +
                         "<label id='storeNameLabel' > "+store.getId() +"</label >" +
                      "</div>" +
                    "</div>" +
                    "<div class=\"row\">" +
                       "<div class=\"col-25\">" +
                            "<label for=\"fname\">Store Name</label>" +
                       "</div>" +
                       "<div class=\"col-75\">" +
                          "<label id='storeNameLabel' > "+store.getName() +"</label >" +
                       "</div>" +
                    "</div>" +
                    "<div class=\"row\">" +
                       "<div class=\"col-25\">" +
                           "<label for=\"fname\">Store PPK</label>" +
                       "</div>" +
                       "<div class=\"col-75\">" +
                           "<label >"+store.getPPK()+"</label >" +
                       "</div>" +
                     "</div>" +

                    "<div class=\"row\">" +
                    "<div class=\"col-25\">" +
                    "<label for=\"fname\">Distance from customer</label>" +
                    "</div>" +
                    "<div class=\"col-75\">" +
                    "<label >"+String.format("%.2f",storeSingleOrderItemsMap.get(storeId).getDistanceFromCustomer())+"</label >" +
                    "</div>" +
                    "</div>" +

                    "<div class=\"row\">" +
                    "<div class=\"col-25\">" +
                    "<label for=\"fname\">Delivery Cost</label>" +
                    "</div>" +
                    "<div class=\"col-75\">" +
                    "<label >"+String.format("%.2f",storeSingleOrderItemsMap.get(storeId).getDeliveryCost())+"</label >" +
                    "</div>" +
                    "</div>" +

                       "<div id=\"orderItems\">" +
                         "<div class=\"row\">" +
                            "<h4>Order Items:</h4>" +
                         "</div>" +
                       "<table id=\"orderItemsTable\">" +
                            "<tr>" +
                              "<th>Item Id</th>" +
                              "<th>Item Name</th>" +
                              "<th>Purchase Category</th>" +
                              "<th>Quantity</th>" +
                              "<th>Single item price</th>" +
                              "<th>Total Price</th>" +
                              "<th>Store/Offer</th>" +
                               "</tr>";

            for(MyStoreItem storeItem :
                    storeSingleOrderItemsMap.get(storeId).getThisStoreQuantityMapFromOrder().keySet()){
                double quantity = storeSingleOrderItemsMap.get(storeId)
                        .getThisStoreQuantityMapFromOrder().get(storeItem);

                res+="<tr>" +
                        "<td>"+storeItem.getMyItem().getItemId()+"</td>" +
                        "<td>"+storeItem.getName()+"</td>" +
                        "<td>"+storeItem.getMyItem().getPurchaseCategory()+"</td>" +
                        "<td>"+quantity+"</td>" +
                        "<td>"+storeItem.getPrice()+"</td>" +
                        "<td>"+String.format("%.2f",quantity * storeItem.getPrice())+"</td>" +
                        "<td>"+storeItem.getItemKind()+"</td>" +
                        "</tr>";

            }
            res+= " </table>" +
                    "<div class=\"row\">" +
                    "<form id=\"sendOrder\" method=\"POST\" action=\"openFeedbackOption\">" +
                    "<input id=\"accept\" type='submit' name=\"\" value='Approve order'/>" +
                    "<input id=\"declain\" type='submit' name=\"\" value='Abort'/>" +
                    "</form>" +
                    "</div>" +
                    "</div>" +
                    "<div class='leaveFeedbacks'>" +
                    "</div>" +
                    "</div>" +
                    "</div>" +
                    "</div>";
        }

        return res;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            processes(req, resp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /*public class witch gets query parameters from req's json */
    public class Order{
        public String date;
        public String type;
        public int customerX;
        public int customerY;
        List<StoreItem> selectedStoreItemsList;
        List<OfferItem> selectedOfferItemsList;

        public Order(String date, String type, int customerX, int customerY,
                     List<StoreItem> selectedStoreItemsList, List<OfferItem> selectedOfferItemsList) {
            this.date = date;
            this.type = type;
            this.customerX = customerX;
            this.customerY = customerY;
            this.selectedStoreItemsList = selectedStoreItemsList;
            System.out.println(selectedOfferItemsList);
            this.selectedOfferItemsList = selectedOfferItemsList;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "date='" + date + '\'' +
                    ", type='" + type + '\'' +
                    ", customerX=" + customerX +
                    ", customerY=" + customerY +
                    ", selectedStoreItemsList=" + selectedStoreItemsList +
                    ", selectedOfferItemsList=" + selectedOfferItemsList +
                    '}';
        }
    }

    public class StoreItem{
        public String storeName;
        public int itemId;
        public String type; // offer / store should be store type.
        public double quantity;

        public StoreItem(String storeName, int itemId, String type, double quantity) {
            this.storeName = storeName;
            this.itemId = itemId;
            this.type = type;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "StoreItem{" +
                    "storeName='" + storeName + '\'' +
                    ", itemId=" + itemId +
                    ", type='" + type + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    public class  OfferItem{
        public int storeId;
        public int itemId;
        public String type; // offer / store should be store type.
        public double quantity;
        public int price;

        public OfferItem(int storeId, int itemId, String type, double quantity, int price) {
            this.storeId = storeId;
            this.itemId = itemId;
            this.type = type;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return "OfferItem{" +
                    "storeId=" + storeId +
                    ", itemId=" + itemId +
                    ", type='" + type + '\'' +
                    ", quantity=" + quantity +
                    ", price=" + price +
                    '}';
        }
    }
}



//
//    function Order(date, orderKind, customerX, customerY, selectedStoreItemsList, selectedOfferItemsList){
//        this.date = date;
//        this.type = orderKind;
//        this.customerX = customerX;
//        this.customerY = customerY;
//        this.selectedStoreItemsList = selectedStoreItemsList;
//        this.selectedOfferItemsList = selectedOfferItemsList;
//    }
//
//    function StoreItem(storeName, itemId, itemQuantity){
//        this.storeName = storeName;
//        this.id = itemId;
//        this.type = "store";
//        this.quantity = itemQuantity;
//        /*
//         * store id
//         * item id
//         *  itemKind = "store"
//         * quantity
//         * */
//    }
//
//    function OfferItem(storeId, itemId, itemQuantity, itemPrice) {
//        this.storeId = storeId;
//        this.id = itemId;
//        this.type = "offer";
//        this.quantity = itemQuantity;
//        this.price = itemPrice;
//    }
