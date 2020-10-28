package webCode.servlets.generalServlets;

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

public class ZoneStoresListServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            String areaName = SessionUtils.getAreaName(request);
            List<MyStore> zoneStores = engine.getMySupermarkets().getAreaStoresList(areaName);
            List<StoreInfo> storesInfo = buildStoresInfoList(zoneStores);

            String storesListJson = gson.toJson(storesInfo);
            System.out.println("storesListJson= "+storesListJson);
            out.println(storesListJson);
            out.flush();

            /*
            String json = gson.toJson(zoneStores);
            System.out.println(json);
            System.out.println(json);

            out.println(json);
            out.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
             */
        }
    }

    private  List<StoreInfo> buildStoresInfoList(List<MyStore> zoneStores){
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
