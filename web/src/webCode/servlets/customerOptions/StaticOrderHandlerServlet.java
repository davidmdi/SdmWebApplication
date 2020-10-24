package webCode.servlets.customerOptions;

import SDM_CLASS.IfYouBuy;
import SDM_CLASS.SDMDiscount;
import SDM_CLASS.SDMOffer;
import SDM_CLASS.ThenYouGet;
import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
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
import java.util.Set;



public class StaticOrderHandlerServlet extends HttpServlet {
    @Override

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (PrintWriter out = resp.getWriter()) {
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);
            MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);
            Map<MyStoreItem, Double> selectedItemsMap = buildDeliveryItemsMap(req, superMarket);
            showOffersToUser(out,selectedItemsMap,superMarket);
        }

    }


    private void showOffersToUser(PrintWriter out, Map<MyStoreItem, Double> selectedItemsMap, MySuperMarket superMarket) {

        out.println("<form id ='selectSpecialOffers' method='POST' action='/createStaticOrder'>" +
                "<div class='row'>" +
                "<h3>Special offers</h3>" +
                "</div>");

        Set<MyStoreItem> storeItemSet =selectedItemsMap.keySet() ;
        for (MyStoreItem storeItem:storeItemSet) {
            MyStore store = superMarket.getStores().getStoreMap().get(storeItem.getStoreId());
            if(store.getSdmStore().getSDMDiscounts() != null) { // if the store has discounters.
                for (SDMDiscount discount : store.getSdmStore().getSDMDiscounts().getSDMDiscount()) {
                    IfYouBuy ifYouBuy = discount.getIfYouBuy();
                    if(storeItem.getMyItem().getSdmItem().getId() == ifYouBuy.getItemId() &&
                            selectedItemsMap.get(storeItem) >= ifYouBuy.getQuantity())
                        for (double i=0;
                             i<Math.floor(selectedItemsMap.get(storeItem)/ifYouBuy.getQuantity());i++){
                            out.println(createSellTile(discount,superMarket,store)); // need to build single offer html
                        }
                }
            }
        }

        out.println("<div class=\"row\">");
             out.println("<input type=\"submit\" value='Make order' >");
        out.println("</div>");
        out.println("</form>");


    }

    private String createSellTile(SDMDiscount discount, MySuperMarket superMarket, MyStore store) {
        String res = "";
        ThenYouGet thenYouGet = discount.getThenYouGet();
        if(thenYouGet.getOperator().equals("ONE-OF"))
            res = oneOfHtml(discount, superMarket,store);
        else
            res = takeAllOrNothingHtml(discount, superMarket,store);

        return res;
    }

    private String takeAllOrNothingHtml(SDMDiscount discount, MySuperMarket superMarket, MyStore store) {
        ThenYouGet thenYouGet = discount.getThenYouGet();
        String ifYouBuyItemName = superMarket.getItems().getItemsMap().
                get(discount.getIfYouBuy().getItemId()).getName();
        double ifYouBuyQuantityString = discount.getIfYouBuy().getQuantity();
        String allItemNames = "";
        Double itemTotalPrice = 0.0;
        int index = 0;
        String res = "<div class='row'>" +
                "<div class=\"discount\" name='discount'>" +
                "<input type = \"checkbox\" name='discountCheckBox' value='ALL-NOTHING' class=\"regular-checkbox\">" +
                "<label class='discount-header' >"+discount.getName()+"</label >" +
                "<label > for buying "+ ifYouBuyQuantityString +" " + ifYouBuyItemName+" "+"</label >";
        for (SDMOffer offer:thenYouGet.getSDMOffer()) {
            String itemName = superMarket.getItems().getItemsMap().get(offer.getItemId()).getName();
            allItemNames = allItemNames + " " + offer.getQuantity() + " " + itemName +
                     " for " + offer.getForAdditional() + " NIS ;";
            itemTotalPrice = itemTotalPrice +offer.getForAdditional();
            allItemNames = allItemNames + "\n";
            res += "<input type=\"hidden\" id='"+index+"' name=\"offerItemId\" value='"+offer.getItemId()+"'>" +
                    "<input type=\"hidden\" id='"+index+"' name=\"offerItemPrice\" value='"+offer.getForAdditional()+"'>" +
                    "<input type=\"hidden\" id='"+index+"' name=\"offerItemQuantity\" value='"+offer.getQuantity()+"'>" +
                    "<input type=\"hidden\" id='"+index+"' name=\"offerItemStoreId\" value='"+store.getId()+"'>";
            index++;
        }
        allItemNames = allItemNames + "Total of " + itemTotalPrice + "NIS ";
        res+="<label >you can choose all or nothing: </label >" +
                "<label >"+ allItemNames +"</label >" +
                "</div>" +
                "</div>";

        return res;
    }

    //need have:::::::::    itemId,price,storeId

    private String oneOfHtml(SDMDiscount discount, MySuperMarket superMarket, MyStore store) {
        ThenYouGet thenYouGet = discount.getThenYouGet();
        String ifYouBuyItemName = superMarket.getItems().getItemsMap().
                get(discount.getIfYouBuy().getItemId()).getName();
        double ifYouBuyQuantityString = discount.getIfYouBuy().getQuantity();
        String res =  "<div class='row'>" +
                "<div class=\"discount\" name='discount'>" +
                "<input type = \"checkbox\" name='discountCheckBox' value='ONE-OF' class=\"regular-checkbox\">" +
                "<label class='discount-header' >"+discount.getName() +"</label >" +
                "<label >for buying "+ ifYouBuyQuantityString +" " + ifYouBuyItemName  +"</label >" +
                "<label >you can choose one of:</label >" +
                "<select name=\"products\" id=\"oneOfOfferSelect\" class='discount-one-of'>" ;
        for (SDMOffer offer: thenYouGet.getSDMOffer()) {
            String itemName = superMarket.getItems().getItemsMap().get(offer.getItemId()).getName();
            double itemQuantity = offer.getQuantity();
            int forAddition = offer.getForAdditional();
            res += "<option value=\"volvo\" offerItemId='"+offer.getItemId()+"' " +
                    "offerItemPrice='"+offer.getForAdditional()+"' offerItemStoreId='"+store.getId()+"' " +
                    "offerItemQuantity='"+offer.getQuantity()+"'>" +
                    itemQuantity + " of "+itemName + " for " +forAddition +
                    "NIS for each</option>";

        }
             res += "<input type=\"hidden\" id=\"selectedOfferItemId\" name=\"selectedOfferItemId\" value=''>" +
                "<input type=\"hidden\" id=\"selectedOfferItemPrice\" name=\"selectedOfferItemPrice\" value=''>" +
                "<input type=\"hidden\" id=\"selectedOfferItemQuantity\" name=\"selectedOfferItemQuantity\" value=''>" +
                "<input type=\"hidden\" id=\"selectedOfferItemStoreId\" name=\"selectedOfferItemStoreId\" value=''>";
              res+=  "</select>" +
                   "</div>" +
                "</div>";
        return res;
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

