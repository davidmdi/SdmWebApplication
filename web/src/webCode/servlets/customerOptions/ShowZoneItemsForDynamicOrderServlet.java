package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ShowZoneItemsForDynamicOrderServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("text/html;charset=UTF-8");
            Engine engine = ServletUtils.getEngine(getServletContext());
            String zoneName = SessionUtils.getAreaName(req);

            out.println(createFormOfAreasItemsList(engine,zoneName));
        }
    }


    private String createFormOfAreasItemsList(Engine engine, String zoneName) {

        List<MyItem> zoneItems = engine.getMySupermarkets().getAreaItemsList(zoneName);

        String res = "<div id='staticOrDynamicOrder' class='row'>" +
                        "<div class='col'>" +
                            "<form id='dynamicOrderItems' action='fake' enctype=\"multipart/form-data\">" +
                                "<div class=\"row\"><h2>Choose items</h2></div>";

        for(MyItem item : zoneItems){
            res += "<div class=\"row\"><div name='item' class=\"item\">" +
                    "<input type=\"checkbox\" class=\"regular-checkbox\" name='itemCheckBox' value="+item.getItemId()+">" +
                    "<label name='itemId' for="+item.getItemId()+">"+item.getItemId()+"</label>" +
                    "<label name='itemName' for="+item.getItemId()+">"+item.getName()+"</label>" +
                    "<label name='itemPurchaseCategory' for="+item.getItemId()+">"+item.getPurchaseCategory()+"</label>" +
                    "<label for="+item.getItemId()+"> Amount:</label>";
            //"<input type=\"number\" class=\"text-price\" id=\"itemPrice\" name=\"itemPrice\" placeholder=\"0\" min=\"0\" value=\"0\">" +
            if(item.getPurchaseCategory().equalsIgnoreCase(MyItem.QUANTITY)){
                res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0' min = '0' value ='0'>";
            }else{
                res += "<input type =\"number\" name='itemAmount' class=\"text-price\" placeholder = '0.0' inputmode = \"decimal\" min = \"0.0\" step = \"0.1\" value = \"0.0\" >";
            }
            res += "</div></div>";
        }

        res += "<div class=\"row\">" +
                    "<input type=\"submit\" value=\"Shop\">" +
                "</div>" +
            "</form>" +
        "</div>" +
    "</div>";

        return res;
    }

}