package webCode.servlets.storeOwnerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import utils.ServletUtils;
import utils.SessionUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class OwnerStoresListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            String areaName = SessionUtils.getAreaName(request);
            String ownerName = SessionUtils.getUsername(request);

            List<MyStore> zoneStores = engine.getMySupermarkets().getAreaStoresList(areaName);
            List<MyStore> ownerStores = new ArrayList<>();

            for(MyStore store : zoneStores){
                if(store.getOwnerName().equalsIgnoreCase(ownerName))
                    ownerStores.add(store);
            }

            List<StoreInfo> storesInfo = buildOwnerStoresInfoList(ownerStores);

            String storesListJson = gson.toJson(storesInfo);
            System.out.println("storesListJson= "+storesListJson);
            out.println(storesListJson);
            out.flush();
        }
    }

    private  List<StoreInfo> buildOwnerStoresInfoList(List<MyStore> zoneStores){
        List<StoreInfo> storesInfo = new ArrayList<>();

        for(MyStore store : zoneStores){
            storesInfo.add(new StoreInfo(store.getId(), store.getName()));
        }

        return storesInfo;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public class StoreInfo{
        public int id;
        public String name;

        public StoreInfo(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return "storeInfo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
