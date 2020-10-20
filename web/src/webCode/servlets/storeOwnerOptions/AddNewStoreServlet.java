package webCode.servlets.storeOwnerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddNewStoreServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        Engine engine = ServletUtils.getEngine(getServletContext());
        String zoneName =  SessionUtils.getAreaName(request);
        String ownerName =  SessionUtils.getUsername(request);
        String msg;

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        MyStore.StoreJson store = gson.fromJson(reader, MyStore.StoreJson.class);


        if(engine.isStoreLocationValid(store.x, store.y) == false)
            msg = "Location not valid";
        else{ //create store
            engine.getMySupermarkets().createNewStore(store, zoneName, ownerName);
            msg = "Store '"+store.name+"' added successfully.";
        }

        try (PrintWriter out = response.getWriter()) {
            out.println(msg.toString());
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

/*
    public class Store{
        private String name;
        private int x;
        private int y;
        private double ppk;
        private List<Item> items;

        public Store(String name, int x, int y, double ppk, List<Item> items){
            this.name = name;
            this.x = x;
            this.y = y;
            this.ppk = ppk;
            this.items = items;
        }

        @Override
        public String toString() {
            return "Store{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", ppk=" + ppk +
                    ", items=" + items +
                    '}';
        }
    }

    public class Item{
        private int id;
        private double price;

        public Item(int id, double price){
            this.id = id;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", price=" + price +
                    '}';
        }
    }

 */
}
