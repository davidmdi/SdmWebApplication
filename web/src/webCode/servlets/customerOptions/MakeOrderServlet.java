package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyStore;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class MakeOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            //resp.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            //saving the query parameters as strings // need to save it on the session?
            String dateAsString = req.getParameter("dateFromUser");
            String orderType = req.getParameter("typeofOrder");
            int xCord = Integer.parseInt(req.getParameter("xCord"));
            int yCord = Integer.parseInt(req.getParameter("yCord"));
            // getting the zone from session.
            String zoneName = SessionUtils.getAreaName(req);
            if (!engine.isCoordinateAreValid(xCord, yCord, zoneName)) {
                out.println("<h6 id=\"errMsg\" style=\"color:red;\">error:hitting store location</h6>");
            } else {
                if (orderType.equalsIgnoreCase("static")) {
                    //create with store list/
                    out.println(createFormOfStoreList(engine,zoneName));

                } else {
                    out.println(createFormOfAreasItemsList(engine,zoneName));
                }
            }
        }
    }

    private String createFormOfAreasItemsList(Engine engine, String zoneName) {
        MySuperMarket superMarket = engine.getMySupermarkets().getAreaSuperMarketByName(zoneName);
        String res = "<div id=\"storeId\">" +
                "<div class='row'>" +
                "<div class ='col'>" +
                "<div class=\"row\"><h2>Choose your items:</h2></div>" +
                "<form id='itemsListForm' action='' enctype=\"multipart/form-data\">" +
                "<div class=\"row\">" +
                "<div class=\"col-25\"><label for=\"storeName\">Store Name</label></div>" +
                ""+
                "</form>";

        return res;
    }

    private String createFormOfStoreList(Engine engine, String zoneName) {

        String htmlBuilder = "<div id=\"storeId\">" +
         "<label for=\"store\">Choose a store:</label>" +
         "<form id=\"storeSelectForm\" action=\"presentSelectedStoreItems\" method=\"GET\"> "+
         "<select name=\"storeSelection\" id=\"storeSelection\" onchange=\"showSelectedStoreInfo()\">" +
         "<option  value=\"" + "select store"+ "\">"  + "</option>" ;

        List<MyStore> stores = engine.getMySupermarkets().getAreaStoresList(zoneName);
        for (MyStore store : stores){
            htmlBuilder+="<option value=\"" + store.getName()+ "\">" + +store.getId() + "," +
                    store.getName() + "," + store.getPPK() + "</option>" ;
        }
        
        htmlBuilder += "</select></form></div>";

        return htmlBuilder;
    }
}
