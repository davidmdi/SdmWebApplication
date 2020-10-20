package webCode.servlets.storeOwnerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import logic.Logic.My_CLASS.MyItems;
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

public class OpenNewStoreServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (
        PrintWriter out = response.getWriter()) {

        out.println("<div id='content' class='content'>");
        out.println("<div class='row'>");
        out.println("<div class ='col'>");
        //out.println("<div class='row'><h3>Open new store</h3></div>");
        out.println("<h3>Open new store</h3>");
        out.println("<form id='createNewOrderForm' action='' enctype=\"multipart/form-data\">");
        out.println("<div class=\"row\">");
        out.println("<div class=\"col-25\"><label for=\"storeName\">Store Name</label></div>");
        out.println("<div class=\"col-75\">");
        out.println("<input type=\"text\" class=\"text-text\" id=\"storeName\" name=\"storeName\" placeholder=\"Store name..\" required>");
        out.println("</div>");
        out.println("</div>");
        out.println("<div class=\"row\">");
        out.println("<div class=\"col-25\"><label for=\"storeLocation\">Store location</label></div>");
        out.println("<div class=\"col-75\">");
        out.println("<input type=\"number\" class=\"text-cord\" id=\"storeLocationX\" name=\"storeLocationX\" placeholder=\"X..\" min=\"1\" max=\"50\" title='X between 1 to 50' required>");
        out.println("<input type=\"number\" class=\"text-cord\" id=\"storeLocationY\" name=\"storeLocationY\" placeholder=\"Y..\" min=\"1\" max=\"50\" title='Y between 1 to 50' required>");
        out.println("</div>");
        out.println("</div>");
        out.println("<div class=\"row\">");
        out.println("<div class=\"col-25\"><label for=\"ppk\">PPK</label></div>");
        out.println("<div class=\"col-75\">");
        //out.println("<input type=\"number\" class=\"text-price\" id=\"ppk\" name=\"ppk\" placeholder=\"PPK..\" required step=\"0.01\" inputmode=\"decimal\">");
        out.println("<input type=\"number\" class=\"text-price\" id=\"ppk\" name=\"ppk\" placeholder=\"PPK..\" required>");
        out.println("</div>");
        out.println("</div>");
        out.println("<div class=\"row\"><h2>Store items</h2></div>");

        //create items list:
        Engine engine = ServletUtils.getEngine(getServletContext());
        String areaName =  SessionUtils.getAreaName(request);
        List<MyItem> zoneItems = engine.getMySupermarkets().getAreaItemsList(areaName);

        for(MyItem item : zoneItems){
            out.println("<div class=\"row\"><div name='item' class=\"item\">");
            out.println("<input type=\"checkbox\" class=\"regular-checkbox\" name='itemCheckBox' value="+item.getItemId()+">");
            out.println("<label for="+item.getItemId()+">"+item.getItemId()+"</label>");
            out.println("<label for="+item.getItemId()+">"+item.getName()+"</label>");
            out.println("<label for="+item.getItemId()+">"+item.getPurchaseCategory()+"</label>");
            out.println("<label for="+item.getItemId()+"> Price:</label>");
            //if(item.getPurchaseCategory().equalsIgnoreCase(MyItem.QUANTITY)){
                out.println("<input type=\"number\" class=\"text-price\" id=\"itemPrice\" name=\"itemPrice\" placeholder=\"0\" min=\"0\" value=\"0\">");

            /*
            // price is int. Not relevant:
            }else{ // = MyItem.WEIGHT
                out.println("<input type=\"number\" class=\"text-price\" id=\"itemPrice\" name=\"itemPrice\" placeholder=\"0.0\" inputmode=\"decimal\" min=\"0.0\" step=\"0.01\"  value=\"0.0\">");
            }

             */

            out.println("</div></div>");
        }
        //End items creation



        out.println("<div class=\"row\">");
        out.println("<input type=\"submit\" value=\"New store\">");
        out.println("</div>");
        out.println("</form>");
        out.println("</div>");
        out.println("</div>");
        out.println("</div>");
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
}
/*

<div id="content">
    <div class='row'>
        <div class ='col'>
            <div class='row'>
                <h3>Open new store</h3>
            </div>
            <form action="/action_page.php">
                <div class="row">
                    <div class="col-25">
                        <label for="fname">Store Name</label>
                    </div>
                    <div class="col-75">
                        <input type="text" class="text-text" id="fname" name="firstname" placeholder="Store name.." required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-25">
                        <label for="lname">Store locaton</label>
                    </div>
                    <div class="col-75">
                        <input type="number" class="text-cord" id="lname" name="lastname" placeholder="X.." min="1" max="50" required>
                        <input type="number" class="text-cord" id="lname" name="lastname" placeholder="Y.." min="1" max="50" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-25">
                        <label for="lname">PPK</label>
                    </div>
                    <div class="col-75">
                        <input type="number" class="text-price" id="lname" name="lastname" placeholder="PPK.." required step="0.01" inputmode="decimal">
                    </div>
                </div>
                <div class="row">
                    Store items:
                </div>
                <div class="row">
                    <div class="item">
                        <input type="checkbox" class="regular-checkbox" id="vehicle1" name="vehicle1" value="Bike">
                        <label for="vehicle1"> Item ID</label>
                        <label for="vehicle1"> Item Name</label>
                        <label for="vehicle1"> Purchase method</label>
                        <label for="vehicle1"> Price:</label>
                        <!-- $("input").prop('disabled', true); -->
                        <!-- input for whight item: -->
                        <input type="number" class="text-price" id="lname" name="lastname" placeholder="0.0" inputmode="decimal" min="0.0" step="0.01"  value="0.0">
                        <!-- input for quentity item:
                        <input type="number" class="text-price" id="lname" name="lastname" placeholder="0" min="0" value="0"> -->
                    </div>
                </div>
                <div class="row">
                    <input type="submit" value="New store">
                </div>
            </form>
        </div>
    </div>
</div>
 */
