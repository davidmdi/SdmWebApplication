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

        return buildStoreSummeryForm(order,storeSingleOrderItemsMap,engine,req);
    }

    private String buildStoreSummeryForm(MyOrder order, Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap, Engine engine, HttpServletRequest req) {
        String res = "<div id='content'>" +
                            "<div class='row'>" +
                                "<div class ='col'>" +
                                    "<div class='row'>" +
                                        "<h3>Dynamic Order</h3>" +
                                    "</div>" +
                                    "<form id ='createStaticOrder' method='POST' action=''>";

        // for each Store:
        res += buildStoreSummery();


        res +=	"</form>" +
            "</div>" +
        "</div>" +
    "</div>";



    }

    private String buildStoreSummery() {
        /*
        String res = "<div class='storeSummery'>" +
                        "<div class="row">" +
						    "<div class="col-25">" +
							    "<label for=\"fname\">Store ID</label>" +
                            "</div>" +
						"<div class="col-75">" +
							"<label id='storeNameLabel' >store.id</label >" +
						"</div>" +
					"</div>" +
					"<div class="row"> "
						<div class="col-25">
							<label for=\"fname\">Store Name</label>
                </div>
						<div class="col-75">
							<label id='storeNameLabel' >store.name</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Location</label>
                </div>
						<div class="col-75">
							<label id='storeNameLabel' >(store.LocationX,store.LocationX)</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname">Store PPK</label>
						</div>
						<div class="col-75">
							<label >store.ppk</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Distance from customer</label>
						</div>
						<div class="col-75">
							<label >distance</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Delivery cost</label>
						</div>
						<div class="col-75">
							<label >20</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items types</label>
						</div>
						<div class="col-75">
							<label >3</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items cost</label>
						</div>
						<div class="col-75">
							<label >150</label >
						</div>
					</div>
				</div>
				<hr>

        return res;

         */
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
/*
    <div id="content">
		<div class='row'>
			<div class ='col'>
				<div class='row'>
					<h3>Dynamic Order</h3>
				</div>

				<form id ='createStaticOrder' method='POST' action=''>

				<div class='storeSummery'>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Store ID</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >store.id</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Store Name</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >store.name</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Location</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >(store.LocationX,store.LocationX)</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname">Store PPK</label>
						</div>
						<div class="col-75">
							<label >store.ppk</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Distance from customer</label>
						</div>
						<div class="col-75">
							<label >distance</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Delivery cost</label>
						</div>
						<div class="col-75">
							<label >20</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items types</label>
						</div>
						<div class="col-75">
							<label >3</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items cost</label>
						</div>
						<div class="col-75">
							<label >150</label >
						</div>
					</div>
				</div>
				<hr>
				<div class='storeSummery'>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Store ID</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >store.id</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Store Name</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >store.name</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for=\"fname\">Location</label>
						</div>
						<div class="col-75">
							<label id='storeNameLabel' >(store.LocationX,store.LocationX)</label >
						</div>
					</div>
					<div class="row">
						<div class="col-25">
							<label for="fname">Store PPK</label>
						</div>
						<div class="col-75">
							<label >store.ppk</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Distance from customer</label>
						</div>
						<div class="col-75">
							<label >distance</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Delivery cost</label>
						</div>
						<div class="col-75">
							<label >20</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items types</label>
						</div>
						<div class="col-75">
							<label >3</label >
						</div>
					</div>
					<div class="row">
					   <div class="col-25">
							<label for="lname">Total items cost</label>
						</div>
						<div class="col-75">
							<label >150</label >
						</div>
					</div>
				</div>
				<hr>
			</form>

        </div>
    </div>
</div>
*/