package webCode.servlets.customerOptions;

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

public class DynamicOrderSummeryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp){ //throws ServletException, IOException {
       // try {
            processes(req, resp);
        //} catch (ParseException e) {
       //     e.printStackTrace();
        //}
    }

    private void processes(HttpServletRequest req, HttpServletResponse resp) {//throws ServletException, IOException, ParseException {
        try {
            resp.setContentType("text/html;charset=UTF-8");
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();
            StaticOrderSummeryServlet.Order staticOrderFromJs = gson.fromJson(reader
                    , StaticOrderSummeryServlet.Order.class);
            System.out.println(staticOrderFromJs);


            try (PrintWriter out = resp.getWriter()) {
                String res = buildDynamicOrderSemmeryForm(staticOrderFromJs, req);
                out.println(res);
                out.flush();
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private String buildDynamicOrderSemmeryForm(StaticOrderSummeryServlet.Order staticOrderFromJs
            , HttpServletRequest req) {
        Engine engine = ServletUtils.getEngine(getServletContext());
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);

        MyOrder myOrder = SessionUtils.getOrder(req);
        updateOrdersOffer(myOrder,staticOrderFromJs,superMarket);

        Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemMap = ServletUtils
                .createSingleOrderItemsMap(myOrder,req , engine);

        //need to save the order and StoreSingleOrderMap on session..

        synchronized (this) {
            SessionUtils.saveOrderDataOnSession(myOrder,storeSingleOrderItemMap,req);
        }
        return ServletUtils.buildHtmlFormForOrrderSummery(myOrder,storeSingleOrderItemMap,engine,req);
    }

    private void updateOrdersOffer(MyOrder myOrder, StaticOrderSummeryServlet.Order staticOrderFromJs
            , MySuperMarket superMarket) {
        Map<MyStoreItem, Double> quantityMap = myOrder.getQuantityMap();
        Map<MyStoreItem, Double> updatedQuantityMap = new HashMap<>();
        //copy store kind items to updateQuantityMap
        for(MyStoreItem storeItem :quantityMap.keySet()){
            updatedQuantityMap.put(storeItem,quantityMap.get(storeItem));
        }

        // if it is not a dummy offer object
        if(staticOrderFromJs.selectedOfferItemsList.get(0).itemId != -1 ) {
            // adding offer items to map.
            for (StaticOrderSummeryServlet.OfferItem offerItem : staticOrderFromJs.selectedOfferItemsList) {
                MyStore store = superMarket.getStores().getStoreMap().get(offerItem.storeId);
                MyItem myItem = superMarket.getItems().getItemsMap().get(offerItem.itemId);
                MyStoreItem offerItemToAdd = new MyStoreItem(myItem, offerItem.price, offerItem.storeId
                        ,"offer");
                updatedQuantityMap.put(offerItemToAdd, offerItem.quantity);
            }
        }
        myOrder.setQuantityMap(updatedQuantityMap);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        //try {
         processes(req, resp);
       // } catch (ParseException e) {
        //    e.printStackTrace();
        //}
    }
}
