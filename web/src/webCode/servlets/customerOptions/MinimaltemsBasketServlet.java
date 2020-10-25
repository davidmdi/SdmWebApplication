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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimaltemsBasketServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processes(req, resp);
    }

    private void processes(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        StaticOrderSummeryServlet.Order staticOrderFromJs =  gson.
                fromJson(reader, StaticOrderSummeryServlet.Order.class); // json order class from static
        try (PrintWriter out = resp.getWriter()) {
            out.println(buildTempSummery(staticOrderFromJs,req));
        }
    }

    private String buildTempSummery(StaticOrderSummeryServlet.Order staticOrderFromJs, HttpServletRequest req) {
        Engine engine = ServletUtils.getEngine(getServletContext());
        Date date = ServletUtils.buildDate(staticOrderFromJs.date);
        MyCustomer customer = ServletUtils.updateCustomerLocatin(engine,req , staticOrderFromJs.customerX ,
                staticOrderFromJs.customerY);
        String orderKind = staticOrderFromJs.type;
        Map<MyStoreItem,Double> quantityMap = createQuantityMapFromEngineFindBusket( engine,req,
                staticOrderFromJs.selectedStoreItemsList);
        Map<Integer,Double> deliveryCostMap = ServletUtils.createDeliveryCostMap(engine,req , customer ,quantityMap) ;

        //creating order :
        MyOrder order = new MyOrder(date,customer,quantityMap,orderKind,deliveryCostMap);

        //creating StoreSingleOrderMap
        Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap = ServletUtils.createSingleOrderItemsMap(order,req , engine);

        //need to save the order and StoreSingleOrderMap on session..
        synchronized (this) {
            req.getSession(true).setAttribute(Constants.CUSTOMER_ORDER,order);
            req.getSession(true).setAttribute(Constants.CUSTOMER_STORE_SINGLE_ORDER_MAP,storeSingleOrderItemsMap);
        }

        return buildHtmlForm(order,storeSingleOrderItemsMap,engine,req);
    }

    private Map<MyStoreItem, Double> createQuantityMapFromEngineFindBusket(Engine engine
         , HttpServletRequest req, List<StaticOrderSummeryServlet.StoreItem> selectedStoreItemsList) {
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(areaName);
        Map<MyItem,Double> itemsMap = createItemsMapFromJsonList(selectedStoreItemsList,superMarket);
        return superMarket.findBestPrice(itemsMap);

    }

    private Map<MyItem, Double> createItemsMapFromJsonList(
            List<StaticOrderSummeryServlet.StoreItem> selectedStoreItemsList, MySuperMarket superMarket) {
        Map<MyItem,Double> map = new HashMap<>();
        for(StaticOrderSummeryServlet.StoreItem ItemJson : selectedStoreItemsList){
            MyItem item = superMarket.getItems().getItemsMap().get(ItemJson.itemId);
            map.put(item,ItemJson.quantity);
        }
        return map;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processes(req, resp);
    }

}
