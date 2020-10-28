package webCode.servlets.customerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStoreItem;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class StaticOrderHandlerServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);
            MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);
            Map<MyStoreItem, Double> selectedItemsMap = buildDeliveryItemsMap(req, superMarket);
            ServletUtils.showOffersToUser(out,selectedItemsMap,superMarket);
        }

    }

    private Map<MyStoreItem, Double> buildDeliveryItemsMap(HttpServletRequest req, MySuperMarket superMarket) throws IOException {
        Map<MyStoreItem,Double> deliveryMap = new HashMap<>();
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        StoreSelectioItems storeSelectioItems =  gson.fromJson(reader,StoreSelectioItems.class);
        for (ItemSelected item : storeSelectioItems.selectedItemsList ) {
            MyStoreItem itemToAdd = superMarket.getStores().getSelectedStore(storeSelectioItems.storeName).
                    getStoreItems().getItemsMap().get(item.itemId);
            deliveryMap.put(itemToAdd,item.quantity);
        }

        return deliveryMap;
    }


    public class  StoreSelectioItems{
        public String storeName;
        public List<ItemSelected> selectedItemsList;

        public StoreSelectioItems(String storeName, List<ItemSelected> jsonItemList) {
            this.storeName = storeName;
            this.selectedItemsList = jsonItemList;
        }

        @Override
        public String toString() {
            return "StoreSelectioItems{" +
                    "storeName='" + storeName + '\'' +
                    ", selectedItemsList=" + selectedItemsList +
                    '}';
        }
    }

    public class ItemSelected{
        public int itemId;
        public double quantity;

        public ItemSelected(int itemId, double quantity) {
            this.itemId = itemId;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "itemId=" + itemId +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}


/*
</form>
				<! -- $('form *').prop('disabled', true); -->
				<form id ='selectSpecialOffers' method='POST' action=''>
					<div class='row'>
						<h3>Special offers</h3>
					</div>

					//ONE OF
					<div class='row'>
						<div class="discount" name='discount'>
							<input type = "checkbox" name='itemCheckBox' value='id' class="regular-checkbox">
							<label class='discount-header' >YallA BaLaGaN</label >
							<label >for buying '1.0' ketshop</label >
							<label >you can choose one of:</label >
							<select name="products" id="one-of" class='discount-one-of'>
								  <option value="volvo">1.0 of rice for 5 each</option>
								  <option value="saab">option 2</option>
							</select>
						</div>
					</div>

					//CHOOSE EVERTHING OR NOTHING
					<div class='row'>
						<div class="discount" name='discount'>
							<input type = "checkbox" name='itemCheckBox' value='id' class="regular-checkbox">
							<label class='discount-header' >1 + 1</label >
							<label >for buying '1.0' ketshop</label >
							<label >you can choose all or nothing: </label >
							<label >1.0 of rice for 5 each</label >
						</div>
					</div>


					<div class="row">
						<input type="submit" value='Make order'>
					</div>
				</form>
 */

