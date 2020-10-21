package webCode.servlets.customerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticOrderHandlerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Engine engine = ServletUtils.getEngine(getServletContext());
        String zoneName =  SessionUtils.getAreaName(req);
        Map<Integer,Double> deliveryMap = new HashMap<>();
        BufferedReader reader = req.getReader();
        Gson gson = new Gson();
        StoreSelectioItems storeSelectioItems =  gson.fromJson(reader,StoreSelectioItems.class);
        System.out.println(storeSelectioItems);
        for (ItemSelected item : storeSelectioItems.selectedItemsList )
            deliveryMap.put(item.itemId , item.quantity);
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
