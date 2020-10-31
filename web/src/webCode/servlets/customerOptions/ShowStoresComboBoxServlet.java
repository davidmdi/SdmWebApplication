package webCode.servlets.customerOptions;

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
import java.util.List;

public class ShowStoresComboBoxServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);

            out.println(createFormOfStoreList(engine,zoneName));
        }
    }

    private String createFormOfStoreList(Engine engine, String zoneName) {

        String htmlBuilder = "<div class='row' id='staticOrDynamicOrder'>" +
                "<div class='col'>" +
                "<div class='col-25'>" +
                "<label for=\"store\">Choose a store:</label>" +
                "</div>" +
                "<div class='col-75'>" +
                "<form id=\"storeSelectForm\" action=\"presentSelectedStoreItems\" method=\"GET\"> "+
                "<select name=\"storeSelection\" id=\"storeSelection\" onchange=\"showSelectedStoreInfo()\">" +
                    "<option  value=\"" + "Select store.."+ "\">"  + "</option>" ;

        List<MyStore> stores = engine.getMySupermarkets().getAreaStoresList(zoneName);
        for (MyStore store : stores){
            htmlBuilder+="<option value=\"" + store.getName()+ "\">" + store.getId() + "," +
                    store.getName() + "," + store.getPPK() + "</option>" ;
        }

        htmlBuilder += "</select>" +
                "</div>" +
                "</div>" +
                "</div>" +
                "</form>" +
                "</div>";

        return htmlBuilder;
    }
}
